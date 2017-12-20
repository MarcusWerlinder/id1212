package common;

public enum MsgType {
    /**
     * Starting a new game
     */
    START,
    /**
     * Server gives the guesser a response
     */
    GUESS_R,
    /**
     * Client sends a guess to server
     */
    GUESS,
    /**
     * The client wants to disconnect
     */
    DISCONNECT
}
