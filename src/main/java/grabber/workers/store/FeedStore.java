package grabber.workers.store;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import grabber.data.Domain;
import grabber.data.database.Database;
import grabber.data.feed.FeedBase;
import grabber.data.feed.RssFeed;
import grabber.data.feed.TwitterFeed;
import twitter4j.Twitter;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nikita on 27.03.14.
 */
public class FeedStore{
    private FeedStore(){}
    private static FeedStore instance;
    public static FeedStore getInstance(){
        if(instance == null)
            instance = new FeedStore();
        return instance;
    }

    private class DomainFeeds {
        private DomainFeeds(Domain domain, List<FeedBase> feeds) {
            this.domain = domain;
            this.feeds = feeds;
        }

        final Domain domain;
        final List<FeedBase> feeds;
    }

    private final List<DomainFeeds> domains = new LinkedList<DomainFeeds>();

    public void addFeed(Domain domain, FeedBase feed){
        for (DomainFeeds domainFeeds : domains) {
            if(domainFeeds.domain.getUrl().equals(domain.getUrl())){
                domainFeeds.feeds.add(feed);
                return;
            }
        }
        List<FeedBase> feeds = new LinkedList<FeedBase>();
        feeds.add(feed);
        domains.add(new DomainFeeds(domain, feeds));

    }

    public void loadFeeds(Database database){
        try {
            Dao<Domain, ?> domainDao = DaoManager.createDao(database.getConnectionSource(), Domain.class);
            Dao<RssFeed, ?> rssFeedDao = DaoManager.createDao(database.getConnectionSource(), RssFeed.class);
            Dao<TwitterFeed, ?> twitterFeedDao = DaoManager.createDao(database.getConnectionSource(), TwitterFeed.class);

            List<Domain> domains1 = domainDao.queryForAll();
            for (Domain domain : domains1) {

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
