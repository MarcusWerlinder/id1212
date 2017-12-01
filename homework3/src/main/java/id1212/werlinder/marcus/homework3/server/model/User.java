package id1212.werlinder.marcus.homework3.server.model;

import id1212.werlinder.marcus.homework3.common.dtoInfo.Credentials;
import id1212.werlinder.marcus.homework3.server.integration.UserDB;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import javax.security.auth.login.LoginException;
import java.nio.channels.SocketChannel;
import java.rmi.RemoteException;

public class User {
    private UserDB userDB;
    private Credentials credentials;
    private SocketChannel socketChannel;

    public User(Credentials credentials){
        this.credentials = credentials;
    }

    /**
     * We want to register a new user
     */
    public void register() throws RemoteException {
        userDB = new UserDB();

        try {
            userDB.setUsername(credentials.getUser());
            userDB.setPassword(credentials.getUserPass());

            Session session = userDB.getSession();
            session.beginTransaction();
            session.save(userDB);

            session.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Time to log into our server and access the database
     */
    public long login() throws RemoteException, LoginException {
        Session session = userDB.getSession();
        try {
           session.beginTransaction();
           Query query = session.getNamedQuery("getUser");
           query.setString(0, credentials.getUser());
           query.setString(1, credentials.getUserPass());

           userDB = (UserDB) query.getSingleResult();

            return userDB.getId();
        } catch (NoResultException e) {
            throw new LoginException("Wrong username or password");
        }
    }
}
