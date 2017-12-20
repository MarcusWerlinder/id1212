package client.controller;

import client.net.OutputHandler;
import client.net.ServerConnection;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.CompletableFuture;

public class Controller {
    private final ServerConnection serverConnection = new ServerConnection();

    /**
     * We want to do a ServerConnection
     */
    public void connect(String host, int port, OutputHandler outputHandler){
        CompletableFuture.runAsync(() -> {
            try {
                serverConnection.connect(host, port, outputHandler);
            } catch (IOException ioe) {
                throw new UncheckedIOException(ioe);
            }
        }).thenRun(() -> outputHandler.handleMsg("Connected to " + host + ":" + port));
    }

    /**
     * We block the channel until we have disconnected
     */
    public void disconnect() throws IOException {
        serverConnection.disconnect();
    }

    /**
     * Here we try to send the guess
     */
    public void sendGuess(String guess){
        CompletableFuture.runAsync(() -> {
            try {
                serverConnection.sendGuess(guess);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
