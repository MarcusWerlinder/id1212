package server.controller;

import server.game.Game;

public class Controller {

    private final Game game = new Game();

    /**
     * appends guesses to the game
     */
    public void appendGuess(String entry) {
        game.appendGuess(entry);
    }

    public String getGame() {
        return "You got the game";
    }


    public void guess(String guess) {
        game.guess(guess);
    }
}
