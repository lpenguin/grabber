package grabber;

import grabber.data.Domain;

import java.net.URL;
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
        private DomainFeeds(Domain domain, List<URL> feeds) {
            this.domain = domain;
            this.feeds = feeds;
        }

        final Domain domain;
        final List<URL> feeds;
    }

    private final List<DomainFeeds> domains = new LinkedList<DomainFeeds>();

    public void addFeed(Domain domain, URL feed){
        for (DomainFeeds domainFeeds : domains) {
            if(domainFeeds.domain.getUrl().equals(domain.getUrl())){
                domainFeeds.feeds.add(feed);
                return;
            }
        }
        List<URL> feeds = new LinkedList<URL>();
        feeds.add(feed);
        domains.add(new DomainFeeds(domain, feeds));

    }
}
