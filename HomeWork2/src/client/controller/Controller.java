package client.controller;

import client.net.Listener;
import client.net.ServerConnection;

import java.io.IOException;

/**
 * The controller is not completely necessary because we could have just directly called the serverConnection
 * but to keep a better structure we add the controller layer
 */
public class Controller {
    private final ServerConnection serverConnection = new ServerConnection();

    // Establish a connection where we get host and port from NonBlockingInterpreter
    public void connect(String host, int port) {
        serverConnection.connect(host, port);
    }

    // Time to end the program
    public void disconnect() throws IOException {
        serverConnection.disconnect();
    }

    public boolean isConnected() {
        return serverConnection.isConnected();
    }


     // We want to send a guess to the server
     public void guess(String clientGuess){
         serverConnection.sendGuess(clientGuess);
     }

    // We want to start a new game
    public void startGame() {
        serverConnection.newGame();
    }

    // A listener so we can send error messages and stuff like that to nonblocking interpreter
    public void addServConnectionListener(Listener listener) {
        serverConnection.addListener(listener);
    }
}
