package client.view;

public enum Commands {
    /**
     * Start
     */
    START,
    /**
     * We want to guess a letter or the whole word
     */
    GUESS,
    /**
     * If we want to connect to the server first IP then Port
     */
    CON,
    /**
     *Quit everything
     */
    QUIT,
    /**
     * Bad command
     */
    BAD_COMMAND
}
