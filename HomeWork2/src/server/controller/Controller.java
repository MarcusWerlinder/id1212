package server.controller;

import server.game.HangmanGame;
import server.wordHandler.AllWords;

//May be unnecessary but we add this extra layer to split game from server
public class Controller {
    private HangmanGame game;

    public void newHangmanGame() {
        game = new HangmanGame(this);
    }

    public void startNewGameInst() throws IllegalAccessException {
        game.newGameInst();
    }

    public String guess(String guess) {
        return game.guess(guess);
    }

    public String randWord() {
        return AllWords.getRanWord();
    }
}
