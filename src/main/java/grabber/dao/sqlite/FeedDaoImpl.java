package grabber.dao.sqlite;

import grabber.dao.FeedDaoBase;
import grabber.data.Domain;
import grabber.data.feed.FeedBase;
import grabber.data.feed.RssFeed;
import grabber.data.feed.TwitterFeed;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nikita on 06.04.14.
 */
public class FeedDaoImpl extends FeedDaoBase {
    private static final String CREATE_TABLE_QUERY = "CREATE TABLE feeds (id integer primary key autoincrement, domain_id integer not null, type integer not null, url text, account text)";
    private static final String ALL_QUERY = "SELECT id, domain_id, type, url, account FROM feeds";
    private static final String INSERT_RSS_QUERY = "INSERT INTO feeds (domain_id, type, url) values(?, ?, '?')";
    private static final String INSERT_TWITTER_QUERY = "INSERT INTO feeds (domain_id, type, account) values(?, ?, '?')";

    protected FeedDaoImpl(Connection connection) {
        super(connection);
    }

    @Override
    public void insertRssFeed(RssFeed rssFeed) throws SQLException {
        Statement statement = getConnection().createStatement();
        statement.executeUpdate(substituteValues(INSERT_RSS_QUERY, new Object[]{rssFeed.getDomainId(), FeedType.Rss.ordinal(), rssFeed.getUrl()}),
                Statement.RETURN_GENERATED_KEYS);
        rssFeed.setId(getInsertedId(statement));
        statement.close();
    }

    @Override
    public void insertTwitterFeed(TwitterFeed twitterFeed) throws SQLException {
        Statement statement = getConnection().createStatement();
        statement.executeUpdate(substituteValues(INSERT_TWITTER_QUERY, new Object[]{twitterFeed.getDomainId(), FeedType.Twitter.ordinal(), twitterFeed.getAccountName()}),
                Statement.RETURN_GENERATED_KEYS);
        twitterFeed.setId(getInsertedId(statement));
        statement.close();
    }

    @Override
    public List<FeedBase> queryAll() throws SQLException {
        List<FeedBase> feeds = new LinkedList<FeedBase>();

        Statement statement = getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(ALL_QUERY);
        while (resultSet.next()){
            FeedType feedType = FeedType.values()[resultSet.getInt(2)];
            switch (feedType){
                case Rss:
                    feeds.add(new RssFeed(resultSet.getInt(0), resultSet.getInt(1), resultSet.getString(4)));
                    break;
                case Twitter:
                    feeds.add(new TwitterFeed(resultSet.getInt(0), resultSet.getInt(1), resultSet.getString(3)));
                    break;
            }
        }
        statement.close();
        return feeds;
    }

    @Override
    public void createTable() throws SQLException {
        Statement statement = getConnection().createStatement();
        statement.execute(CREATE_TABLE_QUERY);
        statement.close();
    }
}
