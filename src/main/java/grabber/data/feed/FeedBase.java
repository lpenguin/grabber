package grabber.data.feed;

import grabber.data.Domain;
import grabber.task.DownloadTask;

/**
 * Created by nikita on 27.03.14.
 */

public abstract class FeedBase {
    private int id;
    private int domainId;
    //    @DatabaseField(canBeNull = false, foreign = true)
    private Domain domain;

    public FeedBase() {
    }

    public FeedBase(Domain domain) {
        this.domain = domain;
    }

    public FeedBase(int id, int domainId) {
        this.id = id;
        this.domainId = domainId;
    }

    public int getDomainId() {
        return domainId;
    }

    public void setDomainId(int domainId) {
        this.domainId = domainId;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public abstract DownloadTask getTask();

    public void setId(int id) {
        this.id = id;
    }
}
