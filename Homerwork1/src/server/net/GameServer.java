package server.net;

import client.controller.Controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class GameServer {
    private static final int LINGER_TIME = 5000;
    private static final int SOCKET_BORED = 1800000;
    private final Controller contr = new Controller();
    private final List<GameHandler> clients = new ArrayList<>();
    private int portNo = 8080;

    /**
     * Takes one command line argument and litsents on port 8080
     */
    public static void main(String[] args) {
        GameServer server = new GameServer();
        server.serve();
    }

    private void serve() {
        try{
            ServerSocket listeningSocket = new ServerSocket(portNo);
            while(true){
                System.out.println("Listening for clients...");
                Socket clientSocket = listeningSocket.accept();
                startHandler(clientSocket);
            }
        } catch (IOException e) {
            System.err.println("Server crashed");
        }
    }

    private void startHandler(Socket clientSocket) throws SocketException {
        clientSocket.setSoLinger(true, LINGER_TIME);
        clientSocket.setSoTimeout(SOCKET_BORED);
        GameHandler handler = new GameHandler(this, clientSocket);
        synchronized (clients) {
            clients.add(handler);
        }
        Thread handlerThread = new Thread(handler);
        handlerThread.setPriority(Thread.MAX_PRIORITY);
        handlerThread.start();
    }
}
