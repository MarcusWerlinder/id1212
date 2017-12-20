package client.net;

import common.Message;
import common.MsgType;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ServerConnection {
    private static final int SOCKET_CD = 30000; // Set socket timeout to half a minute
    private static final int SOCKET_BORED = 1800000; // Time out if nothing happens within half an hour
    private Socket socket;
    private ObjectOutputStream toServer;
    private ObjectInputStream fromServer;
    private boolean connected;

    /**
     * Create a new instance and connect to specified server.
     * Also starts a listening thread if we get messages from the server.
     *
     * @param host  Host name or IP of the server
     * @param port  The servers port number
     * @param broadcastHandler Called whenever a broadcast is recieved from server
     * @throws IOException if we couldn't connect to the server
     */
    public void connect(String host, int port, OutputHandler broadcastHandler) throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), SOCKET_CD);
        socket.setSoTimeout(SOCKET_BORED);
        connected = true;
        toServer = new ObjectOutputStream(socket.getOutputStream());
        fromServer = new ObjectInputStream(socket.getInputStream());
        new Thread(new Listener(broadcastHandler)).start();
    }

    public void disconnect() throws IOException {
        sendMsg(MsgType.DISCONNECT);
        socket.close();
        socket = null;
        connected = false;
    }

    public void sendGuess(String guess) throws IOException {
        sendMsg(MsgType.GUESS, guess);
    }

    private void sendMsg(MsgType type) throws IOException{
        send(new Message(type, ""));
    }

    private void sendMsg(MsgType type, String body) throws IOException {
        send(new Message(type, body));
    }

    private void send(Message msg) throws IOException {
        toServer.writeObject(msg);
        toServer.flush();
        toServer.reset();
    }

    private class Listener implements Runnable {
        private final OutputHandler outputHandler;

        private Listener(OutputHandler outputHandler){
            this.outputHandler = outputHandler;
        }

        @Override
        public void run(){
            try {
                while (true) {
                    Message msg = (Message) fromServer.readObject();

                    outputHandler.handleMsg("Got message");
                }
            }
            catch (Throwable connectionFailure){
                if (connected){
                    outputHandler.handleMsg("Lost connection");
                }
            }
        }

    }

}
