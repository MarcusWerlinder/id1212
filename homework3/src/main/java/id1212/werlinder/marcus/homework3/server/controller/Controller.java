package id1212.werlinder.marcus.homework3.server.controller;

import id1212.werlinder.marcus.homework3.common.FileClient;
import id1212.werlinder.marcus.homework3.common.FileHandler;
import id1212.werlinder.marcus.homework3.common.FileServer;
import id1212.werlinder.marcus.homework3.common.dtoInfo.Credentials;
import id1212.werlinder.marcus.homework3.common.dtoInfo.FileStruct;
import id1212.werlinder.marcus.homework3.server.integration.FileDI;
import id1212.werlinder.marcus.homework3.server.model.ClientHandler;
import id1212.werlinder.marcus.homework3.server.model.FileDB;

import javax.persistence.NoResultException;
import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class Controller extends UnicastRemoteObject implements FileServer {
    private final Map<Long, ClientHandler> clients = new ConcurrentHashMap<>();

    public Controller() throws RemoteException {
    }

    /**
     * We check if the userId provided is legit if so we return the ClientHandler for that user
     * @param userId each user has its own unique userId that is registered on login
     * @return a ClientHandler
     * @throws IllegalAccessException
     */
    private ClientHandler auth(long userId) throws IllegalAccessException {
        if(!clients.containsKey(userId))
            throw new IllegalAccessException("Please login in first");

        return clients.get(userId);
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

    @Override
    public void upload(long userId, FileStruct fileStruct) throws RemoteException, IllegalAccessException {
        ClientHandler client = auth(userId);
        FileDI fileDI = new FileDI();

        try {
            FileDB file = fileDI.getFileByName(fileStruct.getFilename());

        } catch (NoResultException e) {
            //The file doesn't exist yet we can insert it, we don't need to check anything
            fileDI.insert(client, fileStruct);
            uploadFile(client, fileStruct);
        }
    }

    private void uploadFile(ClientHandler client, FileStruct fileStruct) {
        CompletableFuture.runAsync(() -> {
            try {
                FileHandler.recievingFile(client.getSocketChannel(), Paths.get("server_files/" + fileStruct.getFilename()), fileStruct.getSize());

                client.sendToClient("Your file has been uploaded gratz");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void attachSocketToUser(long userId, SocketChannel socketChannel) throws RemoteException{
        clients.get(userId).attachSocketHandler(socketChannel);
    }
}
