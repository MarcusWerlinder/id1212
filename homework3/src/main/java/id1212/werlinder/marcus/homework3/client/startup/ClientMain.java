package id1212.werlinder.marcus.homework3.client.startup;

import id1212.werlinder.marcus.homework3.client.view.Interpreter;
import id1212.werlinder.marcus.homework3.common.FileServer;

import java.rmi.Naming;

public class ClientMain {
    public static void main(String[] args){
        try {
            FileServer server = (FileServer) Naming.lookup(FileServer.REGISTRY_NAME);
            new Interpreter().start(server);
        } catch (Exception e) {
            System.err.println("Failed");
            e.printStackTrace();
        }
    }
}
