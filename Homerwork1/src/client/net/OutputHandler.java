package client.net;

public interface OutputHandler {
    /**
     * The model for outPutHandler that implementations must follow
     */
    public void handleMsg(String msg);
}
