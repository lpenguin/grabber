package grabber.result;

import grabber.task.DownloadTask;
import grabber.workers.ResultsHandler;
import twitter4j.ResponseList;
import twitter4j.Status;

/**
 * Created by nikita on 28.03.14.
 */
public class TwitterDownloadResult extends DownloadResult {
    public ResponseList<Status> getUserTimeline() {
        return userTimeline;
    }

    private ResponseList<Status> userTimeline;

    public TwitterDownloadResult(DownloadTask downloadTask, boolean statusOk, ResponseList<Status> userTimeline) {
        super(downloadTask, statusOk);
        this.userTimeline = userTimeline;
    }

    @Override
    public void handle(ResultsHandler handler) {
        handler.handleTwitter(this);
    }
}
