package grabber.result;

import grabber.task.DownloadTask;
import grabber.workers.ResultsHandler;

/**
* Created by nikita on 24.03.14.
*/
public abstract class DownloadResult {
    public DownloadTask getDownloadTask() {
        return downloadTask;
    }

   public DownloadResult(DownloadTask downloadTask,  boolean statusOk) {
        this.downloadTask = downloadTask;
        this.statusOk = statusOk;
    }

    private final DownloadTask downloadTask;
    private final boolean statusOk;

    public boolean isStatusOk() {
        return statusOk;
    }

    public abstract void handle(ResultsHandler handler);
}
