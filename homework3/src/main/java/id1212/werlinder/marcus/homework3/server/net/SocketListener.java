package id1212.werlinder.marcus.homework3.server.net;

import id1212.werlinder.marcus.homework3.common.Constants;
import id1212.werlinder.marcus.homework3.server.controller.Controller;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CompletableFuture;

public class SocketListener implements Runnable{
    private Controller controller;

    public SocketListener(Controller controller) {
        this.controller = controller;

        Thread listener = new Thread(this);
        listener.setPriority(Thread.MAX_PRIORITY);
        listener.start();
    }

    @Override
    public void run() {
        try (ServerSocketChannel serverSocket = ServerSocketChannel.open()) {
            serverSocket.socket().bind(new InetSocketAddress(Constants.SERV_PORT));

            while (true) {
                System.out.println("Listening...");
                SocketChannel client = serverSocket.accept();
                System.out.println("We have connected: " + client.getRemoteAddress());

                CompletableFuture.runAsync(() -> {
                    try {
                        new SocketManager(controller, client);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
