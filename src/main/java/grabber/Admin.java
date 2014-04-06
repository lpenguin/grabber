package grabber;

import grabber.dao.sqlite.SqliteDaoFactory;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by nikita on 02.04.14.
 */
public class Admin {

    private final FeedSearcher feedSearcher;
    private final ContentStore contentStore;
    private final Downloader downloader;
    private final ResultsHandler resultsHandler;
    private final Database database;
    private ExecutorService executorService;
    private final List<Future> futures = new LinkedList<Future>();

    public static void main(String[] args){
        String connString = "jdbc:sqlite:testdb.db";
        Database database;
        try {
            database = new Database(new SqliteDaoFactory(), connString);
            FeedStore.getInstance().initialize(database);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        if(args.length == 0) {
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(System.in));
            String s = null;
            try {
                s = br.readLine();
                args = s.split(" ");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Admin admin = new Admin(database);
        admin.process(args);
    }

    public Admin(Database database){
        this.database = database;
        contentStore = new ContentStore();
        downloader = new Downloader(database, 10);
        resultsHandler = new ResultsHandler(contentStore, downloader);
        downloader.setDownloadTo(resultsHandler);
        feedSearcher = new FeedSearcher(downloader);
    }

    public void process(String[] args){
        if(args.length == 1 && (args[0].equals("create-tables") || args[0].equals("ct"))){
            database.createTables();
        }else{
            FeedStore.getInstance().load();
            parseArgs(args);
        }
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
            }else if(command.equals("list-domains")
                    || command.equals("ld")) {
                listDomains();
            }else if(command.equals("list-feeds")
                    || command.equals("lf")) {
                listFeeds();
            } else if(command.equals("search-feeds") ||
                    command.equals("sf")){
                searchFeeds();
            } else
                System.out.println("no such command: "+command);
            try {
                FeedStore.getInstance().save();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void searchFeeds() {
        resultsHandler.setDownloadAfterSearch(false);
        executorService = Executors.newFixedThreadPool(3);
        futures.add(executorService.submit(contentStore));
        futures.add(executorService.submit(downloader));
        futures.add(executorService.submit(resultsHandler));

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                System.out.println("SHUTDOWN HOOOK");
                for (Future future : futures) {
                    future.cancel(true);
                }

                //TODO: нужен ли shutdown и awaitTermination?
                try {
                    executorService.shutdown();
                    executorService.awaitTermination(10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        });

        List<Domain> domains = FeedStore.getInstance().listDomains();
        for (Domain domain : domains) {
            feedSearcher.search(domain);
        }
        try {
            System.in.read();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.exit(0);

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
        Domain domain = FeedStore.getInstance().findDomainByName(domainName);
        if(domain != null)
            FeedStore.getInstance().addFeed(new TwitterFeed(domain, account));
    }

    private void addRssFeed(List<String> strings) {
        String domainName = strings.remove(0);
        String url = strings.remove(0);
        Domain domain = FeedStore.getInstance().findDomainByName(domainName);
        if(domain != null)
            FeedStore.getInstance().addFeed(new RssFeed(domain, url));
    }

    private void addDomain(List<String> strings) {
        String name = strings.remove(0);
        String url = strings.remove(0);
        FeedStore.getInstance().addDomain(new Domain(name, url));
    }

}
