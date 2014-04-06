package grabber.data.feed;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import grabber.data.Domain;
import grabber.database.Database;
import grabber.task.DownloadTask;
import grabber.task.TwitterDownloadTask;

/**
 * Created by nikita on 27.03.14.
 */
@DatabaseTable(tableName = "twitter_feeds")
public class TwitterFeed extends FeedBase {
    @DatabaseField
    private String accountName;

    public TwitterFeed() {
    }

    public TwitterFeed(Domain domain, String accountName) {
        super(domain);
        this.accountName = accountName;
    }

    public TwitterFeed(int id, int domainId, String accountName) {
        super(id, domainId);
        this.accountName = accountName;
    }

    public String getAccountName() {
        return accountName;
    }

    @Override
    public DownloadTask getTask() {
        return new TwitterDownloadTask(getDomain(), this, 1);
    }

    @Override
    public Dao getDao(Database database) {
        return database.getTwitterFeedDao();
    }
}
