package client.net;

import common.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Handles the connection to the server and messaging between client and server
 */
public class ServerConnection implements Runnable{

    private static SocketChannel socketChannel;
    private static SocketAddress serverAddress;
    private static boolean connected;
    private static Selector selector;
    private final Queue<ByteBuffer> msgQ = new ArrayDeque<>();
    private static final ByteBuffer msgFromServer = ByteBuffer.allocateDirect(Constants.MSG_MAX_LEN);
    private static final MessageContr msgContr = new MessageContr();
    private boolean timeToSend = false;
    private final List<Listener> listeners = new ArrayList<>();

    /**
     * We initialise a socketChannel
     * @throws IOException
     */
    private static void initConnection() throws IOException {
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(serverAddress);
        connected = true;
    }

    /**
     * We initialise the selector for our connection to the server
     * @throws IOException
     */
    private static void initSelector() throws IOException {
        selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
    }

    @Override
    public void run() {
        try {
            initConnection();
            initSelector();

            while(connected){

                //We know there is something in the message que that we can send to the server
                if (timeToSend) {
                    socketChannel.keyFor(selector).interestOps(SelectionKey.OP_WRITE);
                    timeToSend = false;
                }

                selector.select();
                for (SelectionKey key : selector.selectedKeys()) {
                    selector.selectedKeys().remove(key);
                    if(!key.isValid()) {
                        continue;
                    }
                    if (key.isConnectable()) {
                        completeConnection(key);
                    } else if (key.isWritable()) {
                        sendToServer(key);
                    } else if (key.isReadable()) {
                        reciveFromServer(key);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This key can connect now
     * @param key
     * @throws IOException
     */
    private void completeConnection(SelectionKey key) throws IOException {
        socketChannel.finishConnect();
        key.interestOps(SelectionKey.OP_WRITE);

        printToListerner("We managed to connect to: " + serverAddress);
    }

    /**
     * Extract the message from server
     */
    private void reciveFromServer(SelectionKey key) throws IOException{
        msgFromServer.clear();
        int numOfReadB = socketChannel.read(msgFromServer);
        if (numOfReadB == -1) {
            throw new IOException("We crashed and we burned");
        }
        String recvdString = extractMessageFromBuffer();
        msgContr.addMessage(recvdString);

        while (msgContr.hasNext()) {
            Message message = msgContr.nextMsg();
            printToListerner(message.getBody());
        }
    }

    private static String extractMessageFromBuffer() {
        msgFromServer.flip();
        byte[] bytes = new byte[msgFromServer.remaining()];
        msgFromServer.get(bytes);
        return new String(bytes);
    }

    /**
     * We know for sure now that we can send the messages we have stored up in our que
     */
    private void sendToServer(SelectionKey key) throws IOException {
        ByteBuffer msg;

        synchronized (msgQ) {
            while((msg = msgQ.peek()) != null) {
                socketChannel.write(msg);
                if(msg.hasRemaining()) return; //We couldn't send the whole msg
                //Remove the msg
                msgQ.remove();
            }
        }
        //We want to read now
        key.interestOps(SelectionKey.OP_READ);
    }

    public void connect(String host, int port) {
        try {
            serverAddress = new InetSocketAddress(host, port);
            new Thread(this).start();
        } catch (Exception e) {

        }
    }

    //We tell the server that we are disconnection then we close the channel to the server
    public void disconnect() throws IOException{
        connected = false;
        sendMsg(MsgType.DISCONNECT);
        socketChannel.close();
        socketChannel.keyFor(selector).cancel();
    }

    //Just to make sure we actually are connected so NonBlockingInterpreter doesn't try to do other stuff before connecting
    public boolean isConnected() {
        return connected;
    }

    /**
     * We want to send a guess to the gameServer
     * sendGuess -> sendMsg (Get right format) -> send -> puts message in que and wakes up selector -> selector send to server
     * @param clientGuess our guess that we want to send to the server
     */
    public void sendGuess(String clientGuess) {
        sendMsg(MsgType.GUESS, clientGuess);
    }

    /**
     * sendMsg can come in two different types, one is only a command and the second is a command and text (when a user guess)
     * @param type
     */
    private void sendMsg(MsgType type) {
        send(type, "");
    }

    private void sendMsg(MsgType type, String body) {
        send(type, body);
    }

    /**
     * We have prepared a msg to the server that we want to send now
     * @param type tells what type of command it is
     * @param body the string attached to the command
     */
    private void send(MsgType type, String body) {
        ByteBuffer msg = MessageCreator.createMsg(type, body);

        synchronized (msgQ) {
            msgQ.add(msg);
        }

        timeToSend = true;
        selector.wakeup();
    }

    //We tell the server that we want to create a new instance of the game
    public void newGame() {
        sendMsg(MsgType.START);
    }

    //If we want a message to be print back at NonBlockingInterpreter
    private void printToListerner(String body) {
        listeners.forEach(listener -> listener.print(body));
    }

    /**
     * We add a listener so we can send messages back to the NonBlocking interpreter
     * @param listener
     */
    public void addListener(Listener listener) {
        listeners.add(listener);
    }
}
