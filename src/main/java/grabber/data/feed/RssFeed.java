package grabber.data.feed;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import grabber.data.Domain;
import grabber.database.Database;
import grabber.task.DownloadTask;
import grabber.task.RssDownloadTask;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by nikita on 27.03.14.
 */
@DatabaseTable(tableName = "rss_feeds")
public class RssFeed extends FeedBase {

    @DatabaseField
    private String url;

    public RssFeed(Domain domain, String url) {
        super(domain);
        this.url = url;
    }

    public RssFeed(){}

    public URL getUrl() {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public DownloadTask getTask() {
        return new RssDownloadTask(getDomain(), getUrl());
    }

    @Override
    public Dao getDao(Database database) {
        return database.getRssFeedDao();
    }
}
