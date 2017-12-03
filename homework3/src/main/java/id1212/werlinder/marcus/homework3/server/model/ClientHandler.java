package id1212.werlinder.marcus.homework3.server.model;

import id1212.werlinder.marcus.homework3.common.FileClient;
import id1212.werlinder.marcus.homework3.common.dtoInfo.Credentials;
import id1212.werlinder.marcus.homework3.server.integration.FileDI;
import id1212.werlinder.marcus.homework3.server.integration.UserDI;

import javax.security.auth.login.LoginException;
import java.nio.channels.SocketChannel;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ClientHandler {
    private final static UserDI userdi = new UserDI();
    private static FileDI filedDI = new FileDI();
    private UserDB userDB;
    private List<FileClient> msgToClient = new ArrayList<>();
    private List<Long> filesToAlert = new ArrayList<>();
    private SocketChannel socketChannel;

    /**
     * We want to register a new user
     */
    public void register(Credentials credentials) throws Exception {
        userdi.register(credentials);

        sendToClient("We have been successfully registered as: " + credentials.getUser() + " " + credentials.getUserPass());
    }

    /**
     * Time to log into our server and access the database
     */
    public long login(Credentials credentials) throws RemoteException, LoginException {
        userDB = userdi.login(credentials);
        sendToClient("You are logged in");

        return userDB.getId();
    }

    public void sendToClient(String msg) {
        CompletableFuture.runAsync(() -> {
            msgToClient.forEach(FileClient -> {
                try {
                    FileClient.print(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    public void addFileClient(FileClient console) {
        msgToClient.add(console);
    }

    public void attachSocketHandler(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        sendToClient("An socket has been attached to your client");
    }

    public UserDB getUserDB() {
        return userDB;
    }

    public FileDI getFileDI() {
        return filedDI;
    }

    public void addFileToUpdateOn(long fileId) throws RemoteException {
        filesToAlert.add(fileId);

        sendToClient("We will notify you when this file is updated");
    }

    public void alertFileUpdate(long fileId, String alertMsg) throws RemoteException {
        if (!filesToAlert.contains(fileId)) return;

        sendToClient(alertMsg);
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public void remove() throws RemoteException{
        userdi.remove(userDB);
    }
}
