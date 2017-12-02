package id1212.werlinder.marcus.homework3.common;

import id1212.werlinder.marcus.homework3.common.dtoInfo.Credentials;
import id1212.werlinder.marcus.homework3.common.dtoInfo.FileStruct;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FileServer extends Remote{
    String REGISTRY_NAME = "fileServer";
    /**
     * This function enables users to login
     */
    long login(FileClient console, Credentials credentials) throws RemoteException, LoginException;

    /**
     * A new user needs to be able to register to the database
     */
    void register(FileClient console, Credentials credentials) throws Exception;

    /**
     * A method so user can disconnect from the server
     */
    void disconnect(long id) throws RemoteException;

    /**
     * Upload a specified file
     */
    void upload(long userId, FileStruct fileStruct) throws RemoteException, IllegalAccessException;

    FileStruct getFileInfo(long userId, String fileName) throws RemoteException, IllegalAccessException;

    void download(long userId, String fileName) throws IOException, IllegalAccessException;
}
