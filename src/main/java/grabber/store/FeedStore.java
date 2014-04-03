package grabber.store;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.sun.syndication.feed.atom.Feed;
import grabber.data.Domain;
import grabber.data.feed.FeedBase;
import grabber.data.feed.RssFeed;
import grabber.data.feed.TwitterFeed;
import grabber.database.BufferedWriter;
import grabber.database.Database;

import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nikita on 27.03.14.
 */
public class FeedStore{
    private static final int FEED_FLUSH_SIZE = 30;
    private static final int DOMAIN_FLUSH_SIZE = 30;
    private Database database;

    private FeedStore(){}
    private static FeedStore instance;
    public static FeedStore getInstance(){
        if(instance == null)
            instance = new FeedStore();
        return instance;
    }

    private final List<Domain> domains = new LinkedList<Domain>();
    private final List<FeedBase> feeds = new LinkedList<FeedBase>();

    private BufferedWriter<RssFeed> rssFeedBufferedWriter;
    private BufferedWriter<TwitterFeed> twitterFeedBufferedWriter;
    private BufferedWriter<Domain> domainWriter;

    public void initialize(Database database){
        this.database = database;

        rssFeedBufferedWriter = new BufferedWriter<RssFeed>(database.getRssFeedDao(), FEED_FLUSH_SIZE);
        twitterFeedBufferedWriter = new BufferedWriter<TwitterFeed>(database.getTwitterFeedDao(), FEED_FLUSH_SIZE);
        domainWriter = new BufferedWriter<Domain>(database.getDomainDao(), DOMAIN_FLUSH_SIZE);
    }

    public void load(){
        try {
            domains.addAll(database.getDomainDao().queryForAll());
            feeds.addAll(database.getRssFeedDao().queryForAll());
            feeds.addAll(database.getTwitterFeedDao().queryForAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void save() throws SQLException {
        rssFeedBufferedWriter.flush();
        twitterFeedBufferedWriter.flush();
        domainWriter.flush();
    }

    public void addFeed(Domain domain, FeedBase feed){
        feeds.add(feed);
        try {
            if (feed instanceof RssFeed) {
                rssFeedBufferedWriter.add((RssFeed) feed);
            }else if(feed instanceof TwitterFeed){
                twitterFeedBufferedWriter.add((TwitterFeed)feed);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public List<Domain> listDomains(){
        return domains;
    }

    public void addDomain(Domain domain){
        domains.add(domain);
        try {
            domainWriter.add(domain);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


}
