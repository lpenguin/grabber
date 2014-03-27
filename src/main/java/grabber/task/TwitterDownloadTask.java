package grabber.task;

import grabber.data.Domain;
import grabber.feed.TwitterFeed;
import grabber.result.DownloadResult;

/**
 * Created by nikita on 27.03.14.
 */
public class TwitterDownloadTask extends DownloadTask {
    private final TwitterFeed feed;

    public TwitterDownloadTask(Domain domain, TwitterFeed feed) {
        super(domain);
        this.feed = feed;
    }

    @Override
    public DownloadResult download() {
        return null;
    }
}
