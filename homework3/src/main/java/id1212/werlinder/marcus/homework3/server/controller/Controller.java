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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.StringJoiner;
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

            if(file.getOwner().getId() == client.getUserDB().getId()) {
                fileDI.update(fileStruct);
                uploadFile(client, fileStruct);
            } else if (!file.isPublicAccess()) {
                throw new IllegalAccessException("Sorry your not the owner and this is not a public file");
            } else if (!file.isWritable()) {
                throw new IllegalAccessException("Owner hasn't set this file to writable");
            } else {
                fileDI.updateFileSize(fileStruct);
                uploadFile(client, fileStruct);

                String msgToOwner = String.format("User \"%s\" has updated your public file: \"%s\"",
                        client.getUserDB().getUsername(), fileStruct.getFilename());

                alertOwnerFileChange(file, msgToOwner);
            }

        } catch (NoResultException e) {
            //The file doesn't exist yet we can insert it, we don't need to check anything
            fileDI.insert(client, fileStruct);
            uploadFile(client, fileStruct);
        }
    }

    @Override
    public FileStruct getFileInfo(long userId, String filename) throws RemoteException, IllegalAccessException {
        ClientHandler client = auth(userId);
        FileDI fileDI = new FileDI();

        FileDB file = fileDI.getFileByName(filename);

        if (file.getOwner().getId() == client.getUserDB().getId()) {
            return new FileStruct(file);
        } else if (!file.isPublicAccess()) {
            throw new IllegalAccessException("You are not allowed to download this file, it's not public");
        } else if (!file.isReadable()) {
            throw new IllegalAccessException("This file is not readable");
        } else {
            return new FileStruct(file);
        }
    }

    @Override
    public void download(long userId, String filename) throws IOException, IllegalAccessException {
        ClientHandler client = auth(userId);
        FileDI fileDI = new FileDI();

        FileDB file = fileDI.getFileByName(filename);
        Path serverFilePath = Paths.get("server_files/" + filename);

        if (file.getOwner().getId() == client.getUserDB().getId()) {
            FileHandler.sendFile(client.getSocketChannel(), serverFilePath);
        } else if (!file.isPublicAccess()) {
            throw new IllegalArgumentException("This file is not for the public eye");
        } else if (!file.isReadable()) {
            throw new IllegalArgumentException("This file was not meant to be read by anyone");
        } else {
            FileHandler.sendFile(client.getSocketChannel(), serverFilePath);

            String msgToOwner = String.format("The user \"%s\" has downloaded your file: \"%s\"", client.getUserDB().getUsername(), filename);

            alertOwnerFileChange(file, msgToOwner);
        }
    }

    @Override
    public void unregister(long userId) throws IOException, IllegalAccessException {
        ClientHandler client = auth(userId);
        client.remove();
        disconnect(userId);
    }

    @Override
    public void notifyFileUpdate(long userId, String fileToNotifyAbout) throws RemoteException, IllegalAccessException {
        ClientHandler client = auth(userId);
        FileDB file = client.getFileDI().getFileByName(fileToNotifyAbout);

        if (file.getOwner().getId() != client.getUserDB().getId())
            throw new IllegalAccessException("this isn't your file we can't notify you about this");

        client.addFileToUpdateOn(file.getId());
    }

    @Override
    public void list(long userId) throws RemoteException, IllegalAccessException {

        ClientHandler client = auth(userId);
        FileDI fileDI = new FileDI();

        for (FileDB file : fileDI.getFiles(client.getUserDB())) {
            StringJoiner msg = new StringJoiner(", ");
            msg.add("Name: " + file.getName());
            msg.add("Size: " + file.getSize() + " Bytes");
            msg.add("Owner: " + file.getOwner().getUsername());
            msg.add("Public: " + file.isPublicAccess());
            msg.add("Read: " + file.isReadable());
            msg.add("Writable: " + file.isWritable());
            client.sendToClient(msg.toString());
        }
    }

    @Override
    public void delete(long userId, String filename) throws RemoteException, IllegalAccessException {
        ClientHandler client = auth(userId);

        FileDB file = client.getFileDI().getFileByName(filename);

        if (file.getOwner().getId() != client.getUserDB().getId())
            throw new IllegalAccessException("You are not the owner, don't try to delete this file");

        client.getFileDI().deleteFile(file);
        client.sendToClient(String.format("The file \"%s\" has been deleted.", filename));
    }

    private void alertOwnerFileChange(FileDB file, String msg) throws RemoteException {
        long fileOwnderId = file.getOwner().getId();

        //Check if the owner is actually online otherwise we can't alert
        if (!clients.containsKey(fileOwnderId)) return;

        clients.get(file.getOwner().getId()).alertFileUpdate(file.getId(), msg);
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
