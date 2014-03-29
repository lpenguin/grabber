package grabber.feed;

import grabber.data.Domain;
import grabber.task.DownloadTask;
import grabber.task.TwitterDownloadTask;

/**
 * Created by nikita on 27.03.14.
 */
public class TwitterFeed extends FeedBase{
    public String getAccountName() {
        return accountName;
    }

    private final String accountName;

    public TwitterFeed(Domain domain, String accountName) {
        super(domain);
        this.accountName = accountName;
    }

    @Override
    public DownloadTask getTask() {
        return new TwitterDownloadTask(getDomain(), this, 1);
    }
}
