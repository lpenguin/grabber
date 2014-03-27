package grabber;

import grabber.data.Domain;
import grabber.feed.FeedBase;

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
}
