package id1212.werlinder.marcus.homework3.server.integration;

import id1212.werlinder.marcus.homework3.common.dtoInfo.Credentials;
import id1212.werlinder.marcus.homework3.server.model.UserDB;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import javax.security.auth.login.LoginException;

public class UserDI {
    private UserDB userDao;


    public UserDB login(Credentials credentials) throws LoginException {
        Session session = UserDB.getSession();
        try {
            session.beginTransaction();
            Query query = session.getNamedQuery("getUser");
            query.setString(0, credentials.getUser());
            query.setString(1, credentials.getUserPass());
            return (UserDB) query.getSingleResult();
        }  catch (NoResultException e) {
            throw new LoginException("Wrong username or password");
        }
    }

    public void register(Credentials credentials) throws Exception {
        userDao = new UserDB();

        //Check if someone has already registered with this username
        if (usernameAlreadyExists(credentials.getUser()))
            throw new Exception("A user already has this username");

        Session session = UserDB.getSession();

        try {
            userDao.setUsername(credentials.getUser());
            userDao.setPassword(credentials.getUserPass());

            session.beginTransaction();
            session.save(userDao);

            session.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            session.close();
        }
    }

    /**
     * We want to make sure we don't make multiple accounts with the same name
     * @param user the username we want to test
     * @return tells if this is our first time our not
     */
    private boolean usernameAlreadyExists(String user) {
        Session session = UserDB.getSession();

        try{
            Query query = session.getNamedQuery("checkDouble");
            query.setString(0, user);

            query.getSingleResult();

            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    public void remove(UserDB userDB) {
        Session session = UserDB.getSession();
        try {
            session.beginTransaction();
            session.delete(userDB);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
