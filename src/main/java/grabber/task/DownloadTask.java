package grabber.task;

import grabber.data.Domain;
import grabber.task.result.DownloadResult;

/**
 * Created by nikita on 24.03.14.
 */
public abstract class DownloadTask {
    private int id;
    private int domain_id;
    private Domain domain;
    private long downloadTime;

    public long getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(long downloadTime) {
        this.downloadTime = downloadTime;
    }

    protected DownloadTask(int id, int domain_id, long downloadTime) {
        this.id = id;
        this.domain_id = domain_id;
        this.downloadTime = downloadTime;
    }

    public DownloadTask(Domain domain) {
        this.domain = domain;
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
        return domain_id;
    }

    public void setDomain_id(int domain_id) {
        this.domain_id = domain_id;
    }

    public abstract DownloadResult download();

}
