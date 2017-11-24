package client.net;

public interface Listener {
    /**
     * interface to print
     */
    void print(String msg);

    /**
     * printing a custom made error message
     */
    void errorMsg(String msg, Exception e);

    /**
     * Information about the disconnection
     */
    void disconnect();
}
