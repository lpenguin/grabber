package grabber.store;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import grabber.data.Domain;
import grabber.data.feed.FeedBase;
import grabber.database.Database;

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

    private class DomainWithFeeds {
        private DomainWithFeeds(Domain domain){
            this.domain = domain;
            this.feeds = new LinkedList<FeedBase>();
        }

        private DomainWithFeeds(Domain domain, List<FeedBase> feeds) {
            this.domain = domain;
            this.feeds = feeds;
        }

        final Domain domain;
        final List<FeedBase> feeds;
    }

    private final List<DomainWithFeeds> domainsWithFeeds = new LinkedList<DomainWithFeeds>();
    private final List<DomainWithFeeds> domainWithFeedsToFlush = new LinkedList<DomainWithFeeds>();

    public void load(){
        try {
            Dao<Domain, ?> domainDao = DaoManager.createDao(Database.getInstance().getConnectionSource(), Domain.class);
            List<Domain> domains = domainDao.queryForAll();
            for (Domain domain : domains) {
                domainsWithFeeds.add(new DomainWithFeeds(domain));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void save() throws SQLException {
        Dao<Domain, ?> domainDao = DaoManager.createDao(Database.getInstance().getConnectionSource(), Domain.class);
        for (DomainWithFeeds domainWithFeeds : domainWithFeedsToFlush) {
            domainDao.create(domainWithFeeds.domain);
        }
    }

    public void addFeed(Domain domain, FeedBase feed){
        for (DomainWithFeeds domainWithFeeds : this.domainsWithFeeds) {
            if(domainWithFeeds.domain.getUrl().equals(domain.getUrl())){
                domainWithFeeds.feeds.add(feed);
                return;
            }
        }
        List<FeedBase> feeds = new LinkedList<FeedBase>();
        feeds.add(feed);
        domainsWithFeeds.add(new DomainWithFeeds(domain, feeds));
    }

    public List<Domain> listDomains(){
        List<Domain> domains = new LinkedList<Domain>();
        for (DomainWithFeeds domainWithFeed : domainsWithFeeds) {
            domains.add(domainWithFeed.domain);
        }
        return domains;
    }

    public void addDomain(Domain domain){
        DomainWithFeeds domainWithFeeds = new DomainWithFeeds(domain);
        domainsWithFeeds.add(domainWithFeeds);
        domainWithFeedsToFlush.add(domainWithFeeds);
    }
}
