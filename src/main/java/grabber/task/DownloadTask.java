package grabber.task;

import grabber.data.Domain;
import grabber.task.result.DownloadResult;

/**
 * Created by nikita on 24.03.14.
 */
public abstract class DownloadTask {
    private int id;
    private int domainId;
    private Domain domain;
    private long downloadTime;

    public long getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(long downloadTime) {
        this.downloadTime = downloadTime;
    }

    protected DownloadTask(int id, int domainId, long downloadTime) {
        this.id = id;
        this.domainId = domainId;
        this.downloadTime = downloadTime;
    }

    public DownloadTask(Domain domain) {
        setDomain(domain);
    }

    public Domain getDomain() {
        return domain;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDomainId() {
        return domainId;
    }

    public void setDomainId(int domainId) {
        this.domainId = domainId;
    }

    public abstract DownloadResult download();

    public void setDomain(Domain domain) {
        this.domain = domain;
        if(domain != null)
            domainId = domain.getId();
    }
}
