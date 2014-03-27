package grabber.task;

import grabber.data.Domain;
import grabber.result.DownloadResult;

/**
 * Created by nikita on 24.03.14.
 */
public abstract class DownloadTask {
    public Domain getDomain() {
        return domain;
    }

    final private Domain domain;

    public DownloadTask(Domain domain) {
        this.domain = domain;
    }

    public abstract DownloadResult download();

}
