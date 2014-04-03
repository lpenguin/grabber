package grabber;

import grabber.data.Domain;
import grabber.database.Database;
import grabber.store.FeedStore;

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
