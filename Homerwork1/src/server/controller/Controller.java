package server.controller;

import server.game.HangmanGame;

public class Controller {

    private HangmanGame game;

    public void newHangmanGame() {
        game = new HangmanGame();
    }

    public void startNewGameInstance() {
        game.newGameInst();
    }

    public String guess(String guess) {
        return game.guess(guess);
    }
}
