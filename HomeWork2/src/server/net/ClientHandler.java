package server.net;

import common.*;
import server.controller.Controller;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;

public class ClientHandler implements Runnable{
    private final ServerMain server;
    private final SocketChannel clientChannel;
    private final MessageContr msgContr = new MessageContr();
    private SelectionKey channelKey;
    private final ByteBuffer msgFromClient = ByteBuffer.allocateDirect(Constants.MSG_MAX_LEN);
    private final Queue<ByteBuffer> msgQ = new ArrayDeque<>();
    Controller contr = new Controller();

    @Override
    public void run() {
        while (msgContr.hasNext()) {
            Message msg = msgContr.nextMsg();

            switch (msg.getType()) {
                case GUESS:
                    try {
                        gameResponse(contr.guess(msg.getBody()));
                    } catch (Exception e) {
                        System.out.println("It didn't work");
                    }
                    break;
                case START:
                    System.out.println("We are starting a new instance of the hangman game");
                    try {
                        contr.startNewGameInst();
                    } catch (Exception e) {
                    }
                    break;
                case DISCONNECT:
                    System.out.println("Client wants to disconnect");
                    disconnectClient();
                    break;
                default:
                    System.out.println("Something went wrong");
            }

        }
    }

    private void gameResponse(String response) throws  IOException, IllegalArgumentException {
        if(response == null) throw new IllegalArgumentException("Where is the answer?");

        addMsg(MsgType.GAME_ANSWER, response);
    }

    private void addMsg(MsgType gameAnswer, String response) {
        ByteBuffer msg = MessageCreator.createMsg(gameAnswer, response);

        synchronized (msgQ) {
            msgQ.add(msg);
        }

        server.addPendingMsg(channelKey);
        server.wakeup();
    }

    ClientHandler(ServerMain server, SocketChannel clientChannel) {
        this.server = server;
        this.clientChannel = clientChannel;
        contr.newHangmanGame();
    }

    void recvMsg() throws IOException {
        msgFromClient.clear();
        int readBytes = clientChannel.read(msgFromClient);

        if (readBytes == -1) throw new IOException("Client closed the connection...");

        String receivedMsg = extractMessageFromBuffer();
        msgContr.addMessage(receivedMsg);

        CompletableFuture.runAsync(this);
    }

    private String extractMessageFromBuffer() {
        msgFromClient.flip();
        byte[] bytes = new byte[msgFromClient.remaining()];
        msgFromClient.get(bytes);
        return new String(bytes);
    }

    //We need to register the key for
    public void registerKey(SelectionKey register) {
        this.channelKey = register;
    }

    //We know for sure that we are ready to send a message to the player
    public void sendMessage() throws IOException {
        ByteBuffer msg;
        synchronized (msgQ) {
            while ((msg = msgQ.peek()) != null){
                sendMsg(msg);
                msgQ.poll();
            }
        }
    }

    //We have take all precautions, we can send the msg now
    private void sendMsg(ByteBuffer msg) throws IOException {
        clientChannel.write(msg);

        if (msg.hasRemaining()) throw new IOException("We couldn't send everything");
    }

    void disconnectClient() {
        try {
            clientChannel.close();
            System.out.println("We have successfully closed the client connection");
        } catch (IOException e) {

        }
    }
}
