package grabber.task;

import grabber.data.Domain;
import grabber.task.result.DownloadResult;
import grabber.task.result.TwitterDownloadResult;
import grabber.utils.TwitterConfigurator;
import twitter4j.Paging;
import twitter4j.TwitterException;

/**
 * Created by nikita on 27.03.14.
 */
public class TwitterDownloadTask extends DownloadTask {
    private static final int PAGE_SIZE = 40;
    private final String account;
    private final int page;


    public TwitterDownloadTask(int id, int domain_id, long downloadTime, String account, int page) {
        super(id, domain_id, downloadTime);
        this.account = account;
        this.page = page;
    }

    public TwitterDownloadTask(Domain domain, String account, int page) {
        super(domain);
        this.account = account;
        this.page = page;
    }

    @Override
    public DownloadResult download() {
        try {
            return new TwitterDownloadResult(this, true, TwitterConfigurator.getInstance().getTwitter().getUserTimeline(getAccount(), new Paging(page, PAGE_SIZE)));
        } catch (TwitterException e) {
            return new TwitterDownloadResult(this, false, null);
        }
    }

    public String getAccount() {
        return account;
    }

    public int getPage() {
        return page;
    }
}
