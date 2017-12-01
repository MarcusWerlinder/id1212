package id1212.werlinder.marcus.homework3.common;

import id1212.werlinder.marcus.homework3.common.dtoInfo.Credentials;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FileServer extends Remote{
    String REGISTRY_NAME = "fileServer";
    /**
     * This function enables users to login
     */
    long login(FileClient console, Credentials credentials) throws RemoteException;

    /**
     * A new user needs to be able to register to the database
     */
    void register(FileClient console, Credentials credentials) throws  RemoteException;


}
