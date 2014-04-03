package grabber.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import grabber.data.Domain;
import grabber.data.feed.RssFeed;
import grabber.data.feed.TwitterFeed;

import java.sql.SQLException;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * Created by nikita on 02.04.14.
 */
public class Database {
    private static Database instance;
    private DatabaseType databaseType;
    private Dao<Domain, ?> domainDao;
    private Dao<RssFeed, ?> rssFeedDao;
    private Dao<TwitterFeed, ?> twitterFeedDao;

    public JdbcConnectionSource getConnectionSource() {
        return connectionSource;
    }

    private JdbcConnectionSource connectionSource;

    public static Database getInstance(){
        if(instance == null)
            instance = new Database();
        return instance;
    }

    private Database(){}

    public void connect(String connectionStr) throws SQLException {
        connectionSource = new JdbcConnectionSource(connectionStr);
        createTables();
        createdDaos();
    }

    private void createdDaos() throws SQLException {
        domainDao = DaoManager.createDao(Database.getInstance().getConnectionSource(), Domain.class);
        rssFeedDao = DaoManager.createDao(Database.getInstance().getConnectionSource(), RssFeed.class);
        twitterFeedDao = DaoManager.createDao(Database.getInstance().getConnectionSource(), TwitterFeed.class);
    }

    private void createTables(){
        try {
            TableUtils.createTableIfNotExists(connectionSource, Domain.class);
            TableUtils.createTableIfNotExists(connectionSource, RssFeed.class);
            TableUtils.createTableIfNotExists(connectionSource, TwitterFeed.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<Domain, ?> getDomainDao() {
        return domainDao;
    }

    public Dao<RssFeed, ?> getRssFeedDao() {
        return rssFeedDao;
    }

    public Dao<TwitterFeed, ?> getTwitterFeedDao() {
        return twitterFeedDao;
    }
}
