package grabber.dao.base;

import grabber.dao.Dao;
import grabber.dao.base.DaoBase;
import grabber.data.feed.FeedBase;
import grabber.data.feed.RssFeed;
import grabber.data.feed.TwitterFeed;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by nikita on 06.04.14.
 */
public abstract class FeedDaoBase extends DaoBase implements Dao<FeedBase> {
    protected FeedDaoBase(Connection connection) {
        super(connection);
    }

    @Override
    public void insert(FeedBase feedBase) throws SQLException {
        if (feedBase instanceof RssFeed)
            insertRssFeed((RssFeed) feedBase);
        else if (feedBase instanceof TwitterFeed)
            insertTwitterFeed((TwitterFeed) feedBase);
        else
            throw new SQLException("There is no method to insert feedBase: " + feedBase);
    }

    public abstract void insertRssFeed(RssFeed rssFeed) throws SQLException;

    public abstract void insertTwitterFeed(TwitterFeed twitterFeed) throws SQLException;

    public enum FeedType {
        Rss, Twitter
    }
}
