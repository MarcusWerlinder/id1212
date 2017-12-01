package id1212.werlinder.marcus.homework3.server.controller;

import id1212.werlinder.marcus.homework3.common.FileClient;
import id1212.werlinder.marcus.homework3.common.FileServer;
import id1212.werlinder.marcus.homework3.common.dtoInfo.Credentials;
import id1212.werlinder.marcus.homework3.server.model.ClientHandler;

import javax.security.auth.login.LoginException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Controller extends UnicastRemoteObject implements FileServer {
    private final Map<Long, ClientHandler> clients = new ConcurrentHashMap<>();

    public Controller() throws RemoteException {
    }

    @Override
    public long login(FileClient console, Credentials credentials) throws RemoteException, LoginException {
        ClientHandler clientHandler = new ClientHandler();
        clientHandler.addFileClient(console);

        long id = clientHandler.login(credentials);
        clients.put(id, clientHandler);

        return id;
    }

    @Override
    public void register(FileClient console, Credentials credentials) throws Exception {
        ClientHandler clientHandler = new ClientHandler();
        clientHandler.addFileClient(console);
        clientHandler.register(credentials);
    }

    @Override
    public void disconnect(long id) throws RemoteException {
        clients.remove(id);
    }
}
