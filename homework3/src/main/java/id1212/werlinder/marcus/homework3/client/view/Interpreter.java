package id1212.werlinder.marcus.homework3.client.view;

import id1212.werlinder.marcus.homework3.common.Constants;
import id1212.werlinder.marcus.homework3.common.FileClient;
import id1212.werlinder.marcus.homework3.common.FileHandler;
import id1212.werlinder.marcus.homework3.common.FileServer;
import id1212.werlinder.marcus.homework3.common.dtoInfo.Credentials;
import id1212.werlinder.marcus.homework3.common.dtoInfo.FileStruct;
import id1212.werlinder.marcus.homework3.common.dtoInfo.SocketIdentifier;

import javax.security.auth.login.LoginException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class Interpreter implements Runnable{
    private FileServer server;
    private boolean running = false;
    private Console console;
    private CmdParser parser;
    private long userId;
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
                    case UPLOAD:
                        upload();
                        break;
                    case QUIT:
                        disconnect();
                        break;
                    default:
                        console.print("You are using an invalid");
                }
            } catch (Exception e) {

            }
        }
    }

    private void upload() throws IOException, IllegalAccessException, InvalidCommandException {
        if(userId == 0) throw new IllegalArgumentException("You must have logged in before you can upload files");

        try {
            String localFilename = parser.getArgument(0);

            Path path = Paths.get("client_files/%s", localFilename);

            if(!Files.exists(path))
                throw new FileNotFoundException("We could not find the file " + localFilename);

            long fileSize = Files.size(path);

            String fileName = parser.getArgument(1);
            boolean access = Boolean.valueOf(parser.getArgument(2));
            boolean readable = Boolean.valueOf(parser.getArgument(3));
            boolean writable = Boolean.valueOf(parser.getArgument(4));

            FileStruct FileToServer = new FileStruct(userId, fileName, fileSize, access, readable, writable);

            server.upload(userId, FileToServer);
            FileHandler.sendFile(socket, path);
        } catch (Exception e) {
            console.print("It didn't work to upload your file with your command");
        }
    }

    private void disconnect() throws RemoteException {
        running = false;
        server.disconnect(userId);
        boolean forceUnexport = false;
        UnicastRemoteObject.unexportObject(console, forceUnexport);
    }

    private void register() throws RemoteException{
        try{
            Credentials credentials = extractCredentials(parser);
            server.register(console, credentials);
        } catch (Exception e) {
            console.print("We couldn't register a new user");
        }
    }

    private void login() throws LoginException, RemoteException {
        try{
            Credentials credentials = extractCredentials(parser);
            userId = server.login(console, credentials);
            createServerSocket(userId);
            System.out.println(userId);
        } catch (Exception e) {
            console.print("We could not handle you login sorry");
        }
    }

    private void createServerSocket(long userId) throws IOException {
        //We open a socket channel
        socket = SocketChannel.open();
        socket.connect(new InetSocketAddress(Constants.SERV_ADRESS, Constants.SERV_PORT));

        //We want to bind the userId to this socket so we can keep track on the server side who create what socket connection
        ObjectOutputStream output = new ObjectOutputStream(socket.socket().getOutputStream());

        output.writeObject(new SocketIdentifier(userId));
        output.flush();
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
