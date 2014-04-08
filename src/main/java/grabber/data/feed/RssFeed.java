package grabber.data.feed;

import grabber.data.Domain;
import grabber.database.Database;
import grabber.task.DownloadTask;
import grabber.task.RssDownloadTask;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by nikita on 27.03.14.
 */
public class RssFeed extends FeedBase {
    private String url;

    public RssFeed(Domain domain, String url) {
        super(domain);
        this.url = url;
    }

    public RssFeed(int id, int domainId, long lastDownloadTime, String url) {
        super(id, domainId, lastDownloadTime);
        this.url = url;
    }

    public RssFeed() {
    }

    public URL getUrl() {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public DownloadTask getDownloadTask() {
        return new RssDownloadTask(getDomain(), getUrl());
    }
}
