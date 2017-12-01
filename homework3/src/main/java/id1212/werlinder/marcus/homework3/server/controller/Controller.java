package id1212.werlinder.marcus.homework3.server.controller;

import id1212.werlinder.marcus.homework3.common.FileClient;
import id1212.werlinder.marcus.homework3.common.FileServer;
import id1212.werlinder.marcus.homework3.common.dtoInfo.Credentials;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Controller extends UnicastRemoteObject implements FileServer {

    public Controller() throws RemoteException {
    }

    @Override
    public long login(FileClient console, Credentials credentials) throws RemoteException {
        return 0;
    }

    @Override
    public void register(FileClient console, Credentials credentials) throws RemoteException {

    }
}
