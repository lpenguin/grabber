package grabber.data.feed;

import grabber.data.Domain;
import grabber.task.DownloadTask;

/**
 * Created by nikita on 27.03.14.
 */
public abstract class FeedBase {
    public Domain getDomain() {
        return domain;
    }

    private final Domain domain;

    public FeedBase(Domain domain) {
        this.domain = domain;
    }

    public abstract DownloadTask getTask();
}
