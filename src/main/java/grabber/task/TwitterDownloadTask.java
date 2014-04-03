package grabber.task;

import grabber.utils.TwitterConfigurator;
import grabber.data.Domain;
import grabber.data.feed.TwitterFeed;
import grabber.result.DownloadResult;
import grabber.result.TwitterDownloadResult;
import twitter4j.Paging;
import twitter4j.TwitterException;

/**
 * Created by nikita on 27.03.14.
 */
public class TwitterDownloadTask extends DownloadTask {
    private final TwitterFeed feed;
    private final int page;

    private static final int PAGE_SIZE = 40;



    public TwitterDownloadTask(Domain domain, TwitterFeed feed, int page) {
        super(domain);
        this.feed = feed;
        this.page = page;
    }

    @Override
    public DownloadResult download() {
        try {
            return new TwitterDownloadResult(this, true, TwitterConfigurator.getInstance().getTwitter().getUserTimeline(feed.getAccountName(), new Paging(page, PAGE_SIZE)));
        } catch (TwitterException e) {
            return new TwitterDownloadResult(this, false, null);
        }
    }

    public TwitterFeed getFeed() {
        return feed;
    }

    public int getPage() {
        return page;
    }
}
