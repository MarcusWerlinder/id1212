package server.game;

import server.wordHandler.AllWords;

public class HangmanGame {
    private int totalScore = 0;
    private HangmanInstance gameInstance;

    public HangmanGame() {
        newGameInst();
    }

    /**
     * We start a new game here
     */
    public void newGameInst(){
        if (gameInstance != null && gameInstance.canStartNew())
            throw new IllegalStateException("A game is already running");

        gameInstance = new HangmanInstance(AllWords.getRanWord());
    }

    /**
     * We have a guess now we will call the game instance and see if it was correct
     */
    public String guess(String ourGuess) {
        if (gameInstance == null) return ("Start a game before you guess");
        if (gameInstance.guessedCorrect()) return ("You have already guessed the correct word, start a new game");
        //Some tests we need to pass before we do another guess
        if (gameInstance.getTries() >= gameInstance.getTotalTries()) return ("You have already guessed to many times");
        if(gameInstance.alreadyGuesses(ourGuess.toLowerCase())) return ("You have already guessed this");

        gameInstance.guess(ourGuess);
        if(gameInstance.guessedCorrect()) totalScore++;
        else if (!gameInstance.canStartNew() && totalScore != 0) totalScore--;

        //We want to send a response back to the player
        return "Word: " + gameInstance.getWord() + " Attempts: " + gameInstance.getTries() + "/" + gameInstance.getTotalTries() + " score: " + totalScore;
    }
}
