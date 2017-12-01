package id1212.werlinder.marcus.homework3.server.model;

import id1212.werlinder.marcus.homework3.common.FileClient;
import id1212.werlinder.marcus.homework3.common.dtoInfo.Credentials;
import id1212.werlinder.marcus.homework3.server.integration.UserDB;
import id1212.werlinder.marcus.homework3.server.integration.UserDI;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import javax.security.auth.login.LoginException;
import java.nio.channels.SocketChannel;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ClientHandler {
    private final static UserDI userdi = new UserDI();
    private UserDB userDB;
    private List<FileClient> msgToClient = new ArrayList<>();
    private SocketChannel socketChannel;

    /**
     * We want to register a new user
     */
    public void register(Credentials credentials) throws Exception {
        userdi.register(credentials);

        sendToClient("We have been successfully registered");
    }

    /**
     * Time to log into our server and access the database
     */
    public long login(Credentials credentials) throws RemoteException, LoginException {
        userDB = userdi.login(credentials);
        sendToClient("You are logged in");

        return userDB.getId();
    }

    void sendToClient(String msg) {
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
}
