package grabber.data.feed;

import grabber.data.Domain;
import grabber.task.DownloadTask;
import grabber.task.RssDownloadTask;

import java.net.URL;

/**
 * Created by nikita on 27.03.14.
 */
public class RssFeed extends FeedBase {
    public URL getUrl() {
        return url;
    }

    public RssFeed(Domain domain, URL url) {
        super(domain);
        this.url = url;
    }

    private final URL url;

    @Override
    public DownloadTask getTask() {
        return new RssDownloadTask(getDomain(), getUrl());
    }
}
