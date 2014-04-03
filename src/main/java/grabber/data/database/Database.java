package grabber.data.database;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import grabber.data.Content;
import grabber.data.Domain;
import grabber.data.feed.RssFeed;
import grabber.data.feed.TwitterFeed;

import java.sql.SQLException;

/**
 * Created by nikita on 30.03.14.
 */
public class Database {
    public JdbcConnectionSource getConnectionSource() {
        return connectionSource;
    }

    private JdbcConnectionSource connectionSource;

    public Database(JdbcConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
    }

    public void init(){
        try {
            TableUtils.createTableIfNotExists(connectionSource, Domain.class);
            TableUtils.createTableIfNotExists(connectionSource, RssFeed.class);
            TableUtils.createTableIfNotExists(connectionSource, TwitterFeed.class);
            TableUtils.createTableIfNotExists(connectionSource, Content.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
