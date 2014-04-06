package grabber.data.feed;

import grabber.data.Domain;
import grabber.database.Database;
import grabber.task.DownloadTask;
import grabber.task.TwitterDownloadTask;

/**
 * Created by nikita on 27.03.14.
 */
public class TwitterFeed extends FeedBase {
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
        return new TwitterDownloadTask(getDomain(), accountName, 1);
    }

}
