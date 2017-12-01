package id1212.werlinder.marcus.homework3.client.view;

import id1212.werlinder.marcus.homework3.common.FileClient;
import id1212.werlinder.marcus.homework3.common.FileServer;
import id1212.werlinder.marcus.homework3.common.dtoInfo.Credentials;

import java.nio.channels.SocketChannel;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class Interpreter implements Runnable{
    private FileServer server;
    private boolean running = false;
    private Console console;
    private CmdParser parser;
    private SocketChannel socket;


    public void start(FileServer server) throws RemoteException {
        this.server = server;

        if(running) return;
        running = true;
        console = new Console();
        new Thread(this).start();
    }

    /**
     * This part reads the users input and responds with the appropriate method
     */
    @Override
    public void run(){
        while(running){
            try{
                parser = new CmdParser(console.readNextLine());

                switch (parser.getCmd()) {
                    case LOGIN:
                        login();
                        break;
                    case REGISTER:
                        register();
                        break;
                    case QUIT:
                        break;
                }
            } catch (Exception e) {

            }
        }
    }

    private void register() throws RemoteException{
        try{
            Credentials credentials = extractCredentials(parser);
            server.register(console, credentials);
        } catch (Exception e) {
            console.print("We couldn't register a new user");
        }
    }

    private void login() {
    }

    private Credentials extractCredentials(CmdParser parser) {
        String username = parser.getArgument(0);
        String password = parser.getArgument(1);
        return new Credentials(username, password);
    }

    public class Console extends UnicastRemoteObject implements FileClient {
        private static final String PROMPT = "> ";
        private final ThreadSafeStdOut outMsg = new ThreadSafeStdOut();
        private final Scanner console = new Scanner(System.in);

        Console() throws RemoteException {
        }

        @Override
        public void print(String msg) throws RemoteException {
            outMsg.println("\r" + msg);
            outMsg.print(PROMPT);
        }

        @Override
        public void error(String msg, Exception e) throws RemoteException {
            outMsg.println("Error: ");
            outMsg.println(msg);
        }

        @Override
        public void disconnect() throws RemoteException {
            print("You successfully disconnected");
        }

        String readNextLine() throws RemoteException {
            outMsg.println(PROMPT);

            return console.nextLine();
        }
    }
}
