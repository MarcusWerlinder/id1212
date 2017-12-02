package id1212.werlinder.marcus.homework3.server.startup;

import id1212.werlinder.marcus.homework3.common.FileServer;
import id1212.werlinder.marcus.homework3.server.controller.Controller;
import id1212.werlinder.marcus.homework3.server.integration.HibernateStarter;
import id1212.werlinder.marcus.homework3.server.net.SocketListener;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerMain {
    /**
     * This will start everything up for us
     */
    public static void main(String[] args) {
        Logger log = Logger.getLogger("org.hibernate");
        log.setLevel(Level.WARNING);

        try {
            HibernateStarter.initSessionFactory();
            System.out.println("We started the hibernating");

            ServerMain server = new ServerMain();
            Controller controller = new Controller();

            server.startRmi(controller);
            System.out.println("The RMI server has successfully started");

            server.startServerListener(controller);
        } catch (Exception e) {
            System.err.println("We couldn't start the server");
            e.printStackTrace();
        }
    }

    private void startServerListener(Controller controller) {
        new SocketListener(controller);
    }

    private void startRmi(Controller controller) throws RemoteException, MalformedURLException {
        try {
            LocateRegistry.getRegistry().list();
        } catch (RemoteException noRegistryIsRunning) {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        }

        Naming.rebind(FileServer.REGISTRY_NAME, controller);
    }
}
