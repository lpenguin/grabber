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
    private static final String CREATE_TABLE_QUERY =
            "CREATE TABLE feeds (" +
                    "id integer primary key autoincrement, " +
                    "domain_id integer not null, " +
                    "type integer not null, " +
                    "url text, " +
                    "account text," +
                    "last_download_time integer" +
                    ")";
    private static final String ALL_QUERY =
            "SELECT id, domain_id, type, url, account, last_download_time FROM feeds";
    private static final String INSERT_RSS_QUERY = "INSERT INTO feeds (domain_id, type, url, last_download_time) values(?, ?, '?', ?)";
    private static final String INSERT_TWITTER_QUERY = "INSERT INTO feeds (domain_id, type, account, last_download_time) values(?, ?, '?', ?)";

    protected FeedDaoImpl(Connection connection) {
        super(connection);
    }

    @Override
    public void insertRssFeed(RssFeed rssFeed) throws SQLException {
        Statement statement = getConnection().createStatement();
        statement.executeUpdate(substituteValues(INSERT_RSS_QUERY, new Object[]{
                        rssFeed.getDomainId(), FeedType.Rss.ordinal(), rssFeed.getUrl(), rssFeed.getLastDownloadTime()}));
        rssFeed.setId(Helper.getLastInsertId(statement));
        statement.close();
    }

    @Override
    public void insertTwitterFeed(TwitterFeed twitterFeed) throws SQLException {
        Statement statement = getConnection().createStatement();
        statement.executeUpdate(substituteValues(INSERT_TWITTER_QUERY, new Object[]{
                twitterFeed.getDomainId(), FeedType.Twitter.ordinal(), twitterFeed.getAccountName(),
                twitterFeed.getLastDownloadTime()}));
        twitterFeed.setId(Helper.getLastInsertId(statement));
        statement.close();
    }

    @Override
    public List<FeedBase> queryAll() throws SQLException {
        List<FeedBase> feeds = new LinkedList<FeedBase>();

        Statement statement = getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(ALL_QUERY);
        while (resultSet.next()){
            FeedType feedType = FeedType.values()[resultSet.getInt(3)];
            switch (feedType){
                case Rss:
                    feeds.add(new RssFeed(resultSet.getInt(1), resultSet.getInt(2), resultSet.getLong(6), resultSet.getString(4)));
                    break;
                case Twitter:
                    feeds.add(new TwitterFeed(resultSet.getInt(1), resultSet.getInt(2), resultSet.getLong(6), resultSet.getString(5)));
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
