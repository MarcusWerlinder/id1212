package server.net;

import common.Message;
import common.MessageException;
import common.MsgType;
import server.controller.Controller;

import java.io.*;
import java.net.Socket;

public class GameHandler implements Runnable {
    private ObjectInputStream  fromPlayer;
    private ObjectOutputStream toPlayer;
    private Controller ctrl = new Controller();
    private final GameServer server;
    private final Socket playerSocket;
    private boolean connected;

    GameHandler(GameServer server, Socket playerSocket) {
        this.server = server;
        this.playerSocket = playerSocket;
        ctrl.newHangmanGame();
        connected = true;
    }

    @Override
    public void run() {
        try {
            fromPlayer = new ObjectInputStream(playerSocket.getInputStream());
            toPlayer = new ObjectOutputStream(playerSocket.getOutputStream());
        } catch (IOException e) {
            System.err.println("Failed to read/write stream");
            e.printStackTrace();
        }

        while(connected) {
            try {
                Message msg = (Message) fromPlayer.readObject();

                switch (msg.getType()) {
                    case GUESS:
                        System.out.println("We got the message it's: " + msg.getBody());
                        guessResponse(ctrl.guess(msg.getBody()));
                        break;
                    case START:
                        System.out.println("We need to start a game instance for the client");
                        ctrl.startNewGameInstance();
                        break;
                    case DISCONNECT:
                        disconnectPlayer();
                        break;
                    default:
                        throw new MessageException("");
                }
            } catch (IOException | ClassNotFoundException e) {
                disconnectPlayer();
                throw new MessageException(e);
            }
        }
    }

    private void guessResponse (String response) throws IOException {
        if(response == null) return;

        sendMsg(MsgType.GUESS_R, response);
    }

    private void sendMsg(MsgType type, String body) throws IOException {
        Message message = new Message(type, body);
        toPlayer.writeObject(message);
        toPlayer.flush(); // Flush the pipe
        toPlayer.reset(); // Remove object cache
    }

    private void disconnectPlayer() {
        System.out.println("Disconnecting a player!");

        try {
            playerSocket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        connected = false;
        server.removeHandler(this);
    }
}
