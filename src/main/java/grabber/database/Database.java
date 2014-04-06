package grabber.database;

import grabber.dao.Dao;
import grabber.dao.DaoFactory;
import grabber.data.Domain;
import grabber.data.feed.FeedBase;
import grabber.task.DownloadTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by nikita on 02.04.14.
 */
public class Database {
    private final DaoFactory daoFactory;
    private Dao<Domain> domainDao;
    private Dao<FeedBase> feedDao;
    private Dao<DownloadTask> taskDao;
    private Connection connection;

    public Database(DaoFactory daoFactory, String connectionStr) throws SQLException {
        this.daoFactory = daoFactory;
        connect(connectionStr);
        createdDaos();
    }

    public Connection getConnection() {
        return connection;
    }

    private void connect(String connectionStr) throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        connection = DriverManager.getConnection(connectionStr);
    }

    private void createdDaos() throws SQLException {
        domainDao = daoFactory.createDao(connection, Domain.class);
        feedDao = daoFactory.createDao(connection, FeedBase.class);
        taskDao = daoFactory.createDao(connection, DownloadTask.class);
    }

    public void createTables() {
        try {
            domainDao.createTable();
            feedDao.createTable();
            taskDao.createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<Domain> getDomainDao() {
        return domainDao;
    }

    public Dao<DownloadTask> getTaskDao() {
        return taskDao;
    }

    public Dao<FeedBase> getFeedDao() {
        return feedDao;
    }
}
