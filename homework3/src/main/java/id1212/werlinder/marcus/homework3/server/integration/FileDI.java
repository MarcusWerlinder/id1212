package id1212.werlinder.marcus.homework3.server.integration;

import id1212.werlinder.marcus.homework3.common.dtoInfo.FileStruct;
import id1212.werlinder.marcus.homework3.server.model.ClientHandler;
import id1212.werlinder.marcus.homework3.server.model.FileDB;
import id1212.werlinder.marcus.homework3.server.model.UserDB;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import java.util.List;

public class FileDI {

    /**
     * Retrieve file info from the database
     */
    public FileDB getFileByName(String filename) {
        Session session = FileDB.getSession();
        try{
            session.beginTransaction();
            Query query = session.getNamedQuery("checkFile");
            query.setString(0, filename);

            return (FileDB) query.getSingleResult();
        } catch (NoResultException e) {
            throw new NoResultException("We didn't find the file");
        }
    }

    /**
     * Inserting a new file in the database
     */
    public void insert(ClientHandler client, FileStruct fileDTO) {
        Session session = FileDB.getSession();

        try {
            session.beginTransaction();

            FileDB file = new FileDB();
            file.setOwner(client.getUserDB());
            file.setName(fileDTO.getFilename());
            file.setPublicAccess(fileDTO.isPublicAll());
            file.setSize(fileDTO.getSize());
            file.setReadable(fileDTO.isReadable());
            file.setWritable(fileDTO.isWritable());

            session.save(file);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public void update(FileStruct fileStruct) {
        Session session = FileDB.getSession();

        try {
            session.beginTransaction();

            FileDB file = getFileByName(fileStruct.getFilename());
            file.setPublicAccess(fileStruct.isPublicAll());
            file.setReadable(fileStruct.isReadable());
            file.setWritable(fileStruct.isWritable());
            file.setSize(fileStruct.getSize());

            session.update(file);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public void updateFileSize(FileStruct fileStruct) {
        Session session = FileDB.getSession();

        try {
            session.beginTransaction();

            FileDB file = getFileByName(fileStruct.getFilename());
            file.setSize(fileStruct.getSize());

            session.update(file);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    @SuppressWarnings("unchecked")
    public List<FileDB> getFiles(UserDB userDB) {
        Session session = FileDB.getSession();

        Query query = session.createQuery("Select files from file files where files.owner=:user or files.publicAccess = true");
        query.setParameter("user", userDB);

        return query.getResultList();
    }
}
