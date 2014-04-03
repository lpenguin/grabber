package grabber.store;

import com.j256.ormlite.dao.Dao;
import grabber.data.Domain;
import grabber.data.feed.FeedBase;
import grabber.database.BufferedWriter;
import grabber.database.Database;
import grabber.database.HavingDaoBufferedWriter;

import java.sql.SQLException;
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

    private HavingDaoBufferedWriter<FeedBase> feedBufferedWriter;
    private BufferedWriter<Domain> domainWriter;

    public void initialize(Database database){
        this.database = database;

        feedBufferedWriter = new HavingDaoBufferedWriter<FeedBase>(database, FEED_FLUSH_SIZE);
        domainWriter = new BufferedWriter<Domain>(database.getDomainDao(), DOMAIN_FLUSH_SIZE);
    }

    public void load(){
        try {
            domains.addAll(database.getDomainDao().queryForAll());
            feeds.addAll(loadDomain(database.getRssFeedDao().queryForAll()));
            feeds.addAll(loadDomain(database.getTwitterFeedDao().queryForAll()));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<? extends FeedBase> loadDomain(List<? extends FeedBase> feeds) throws SQLException {
        Dao<Domain, Integer> domainDao = database.getDomainDao();
        for (FeedBase feed : feeds) {
            Domain domain = domainDao.queryForId(feed.getDomainId());
            feed.setDomain(domain);
        }

        return feeds;
    }
    public void save() throws SQLException {
        feedBufferedWriter.flush();
        domainWriter.flush();
    }

    public void addFeed(FeedBase feed){
        feeds.add(feed);
        try {
            feedBufferedWriter.add(feed);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public List<Domain> listDomains(){
        return domains;
    }
    public List<FeedBase> listFeeds() { return feeds; }
    public void addDomain(Domain domain){
        domains.add(domain);
        try {
            domainWriter.add(domain);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public Domain searchDomain(String name){
        for (Domain domain : domains) {
            if(domain.getName().equals(name))
                return domain;
        }
        return null;
    }
}
