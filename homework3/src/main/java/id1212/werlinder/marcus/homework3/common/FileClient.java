package id1212.werlinder.marcus.homework3.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FileClient extends Remote{
    /**
     * for messages
     */
    void print(String msg) throws RemoteException;

    /**
     * being able to send a message with error exceptions
     */
    void error(String msg, Exception e) throws RemoteException;

    /**
     * Sending info when we disconnect
     */
    void disconnect() throws RemoteException;
}
