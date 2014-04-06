package grabber.dao;

import grabber.data.feed.FeedBase;
import grabber.data.feed.RssFeed;
import grabber.data.feed.TwitterFeed;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by nikita on 06.04.14.
 */
public abstract class FeedDaoBase extends DaoBase implements FeedDao {
    public enum FeedType {
        Rss, Twitter
    }

    protected FeedDaoBase(Connection connection) {
        super(connection);
    }

    @Override
    public void insert(FeedBase feedBase) throws SQLException {
        if(feedBase instanceof RssFeed)
            insertRssFeed((RssFeed)feedBase);
        else if(feedBase instanceof TwitterFeed)
            insertTwitterFeed((TwitterFeed) feedBase);
        throw new SQLException("There is no method to insert feedBase: "+feedBase);
    }

    public abstract void insertRssFeed(RssFeed rssFeed) throws SQLException ;
    public abstract void insertTwitterFeed(TwitterFeed twitterFeed) throws SQLException ;
}
