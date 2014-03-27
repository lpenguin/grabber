package grabber.feed;

import grabber.data.Domain;
import grabber.task.DownloadTask;

/**
 * Created by nikita on 27.03.14.
 */
public class TwitterFeed extends FeedBase{
    private final String accountName;

    public TwitterFeed(Domain domain, String accountName) {
        super(domain);
        this.accountName = accountName;
    }

    @Override
    public DownloadTask getTask() {
        return null;
    }
}
