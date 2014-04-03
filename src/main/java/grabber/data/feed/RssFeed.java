package grabber.data.feed;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import grabber.data.Domain;
import grabber.task.DownloadTask;
import grabber.task.RssDownloadTask;

import java.net.URL;

/**
 * Created by nikita on 27.03.14.
 */
@DatabaseTable(tableName = "rss_feeds")
public class RssFeed extends FeedBase {
    @DatabaseField
    private URL url;

    public RssFeed(Domain domain, URL url) {
        super(domain);
        this.url = url;
    }

    public URL getUrl() {
        return url;
    }

    @Override
    public DownloadTask getTask() {
        return new RssDownloadTask(getDomain(), getUrl());
    }
}
