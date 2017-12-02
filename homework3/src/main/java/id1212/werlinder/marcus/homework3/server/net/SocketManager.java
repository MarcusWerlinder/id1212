package id1212.werlinder.marcus.homework3.server.net;

import id1212.werlinder.marcus.homework3.common.dtoInfo.SocketIdentifier;
import id1212.werlinder.marcus.homework3.server.controller.Controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.channels.SocketChannel;

public class SocketManager {
    SocketManager(Controller controller, SocketChannel socketChannel) throws IOException {
        ObjectInputStream inputStream = new ObjectInputStream(socketChannel.socket().getInputStream());

        attachToUser(controller, inputStream, socketChannel);
    }

    private void attachToUser(Controller controller, ObjectInputStream inputStream, SocketChannel socketChannel) {
        System.out.println("Waiting for userId");

        try {
            SocketIdentifier identifier = (SocketIdentifier) inputStream.readObject();
            controller.attachSocketToUser(identifier.getUserId(), socketChannel);

            System.out.println("We managed to bind the socket to a user id");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
