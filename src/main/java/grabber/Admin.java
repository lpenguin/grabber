package grabber;

import com.sun.syndication.feed.atom.Feed;
import grabber.data.Domain;
import grabber.data.feed.FeedBase;
import grabber.data.feed.RssFeed;
import grabber.data.feed.TwitterFeed;
import grabber.database.Database;
import grabber.store.ContentStore;
import grabber.store.FeedStore;
import grabber.utils.FeedSearcher;
import grabber.workers.Downloader;
import grabber.workers.ResultsHandler;
import twitter4j.auth.AccessToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nikita on 02.04.14.
 */
public class Admin {

    private final FeedSearcher feedSearcher;
    private final ContentStore contentStore;
    private final Downloader downloader;
    private final ResultsHandler resultsHandler;

    public Admin(){
        contentStore = new ContentStore();
        downloader = new Downloader(10);
        resultsHandler = new ResultsHandler(contentStore, downloader);
        downloader.setDownloadTo(resultsHandler);
        feedSearcher = new FeedSearcher(downloader);
    }

    public void process(String[] args){
        FeedStore.getInstance().load();
        parseArgs(args);
    }

    private void parseArgs(String[] args){
        List<String> strings = new LinkedList<String>(Arrays.asList(args));
        if(args.length >= 1){
            String command = strings.remove(0);
            System.out.println(command);
            if(command.equals("add-domain") ||
                    command.equals("ad")){
                addDomain(strings);
            }else if(command.equals("add-rss-feed") ||
                    command.equals("arf")){
                addRssFeed(strings);
            }else if(command.equals("add-twitter-feed") ||
                    command.equals("atf")){
                addTwitterFeed(strings);
            }else if(command.equals("list-domains")) {
                listDomains();
            }else if(command.equals("list-feeds")) {
                listFeeds();
            } else
                listDomains();
            try {
                FeedStore.getInstance().save();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void listFeeds() {
        List<FeedBase> feedBases = FeedStore.getInstance().listFeeds();
        for (FeedBase feedBase : feedBases) {
            if(feedBase instanceof RssFeed) {
                RssFeed rssFeed = (RssFeed) feedBase;
                System.out.printf("Feed domain: %s [%s]%n", rssFeed.getDomain().getName(), rssFeed.getUrl());
            } else if(feedBase instanceof TwitterFeed) {
                TwitterFeed feed = (TwitterFeed) feedBase;
                System.out.printf("Feed domain: %s [@%s]%n", feed.getDomain().getName(), feed.getAccountName());
            }
        }

    }

    private void listDomains(){
        List<Domain> domains = FeedStore.getInstance().listDomains();
        for (Domain domain : domains) {
            System.out.printf("Domain: %s [%d, %s]%n", domain.getName(), domain.getId(), domain.getUrl());
        }

    }
    private void addTwitterFeed(List<String> strings) {
        String domainName = strings.remove(0);
        String account = strings.remove(0);
        Domain domain = FeedStore.getInstance().searchDomain(domainName);
        if(domain != null)
            FeedStore.getInstance().addFeed(new TwitterFeed(domain, account));
    }

    private void addRssFeed(List<String> strings) {
        String domainName = strings.remove(0);
        String url = strings.remove(0);
        Domain domain = FeedStore.getInstance().searchDomain(domainName);
        if(domain != null)
            FeedStore.getInstance().addFeed(new RssFeed(domain, url));
    }

    private void addDomain(List<String> strings) {
        String name = strings.remove(0);
        String url = strings.remove(0);
        FeedStore.getInstance().addDomain(new Domain(name, url));
    }

}
