package id1212.werlinder.marcus.homework3.server.net;

import id1212.werlinder.marcus.homework3.server.controller.Controller;

public class SocketListener implements Runnable{
    private Controller controller;

    public SocketListener(Controller controller) {
        this.controller = controller;

        Thread listener = new Thread(this);
        listener.setPriority(Thread.MAX_PRIORITY);
        listener.start();
    }

    @Override
    public void run() {

    }
}
