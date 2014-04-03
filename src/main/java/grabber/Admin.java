package grabber;

import grabber.data.Domain;
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

    public void process(){
        FeedStore.getInstance().load();
        menu();
    }

    private void menu(){
        System.out.println("Availible domains: ");
        List<Domain> domains = FeedStore.getInstance().listDomains();
        for (Domain domain : domains) {
            System.out.printf("Domain: %s [%d, %s]%n", domain.getName(), domain.getId(), domain.getUrl());
        }

        try {
            Domain domain = readDomain();
            FeedStore.getInstance().addDomain(domain);
            FeedStore.getInstance().save();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private Domain readDomain() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.printf("Enter domain name: ");
        String name = br.readLine();
        System.out.printf("Enter domain url: ");
        String url = br.readLine();
        return new Domain(name, url);
    }

}
