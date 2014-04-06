package grabber.task.result;

import grabber.task.DownloadTask;
import grabber.workers.ResultsHandler;

/**
 * Created by nikita on 27.03.14.
 */
public class ContentDownloadResult extends DownloadResult {
    private String content;

    public String getContent() {
        return content;
    }

    public ContentDownloadResult(DownloadTask downloadTask, boolean statusOk, String content) {
        super(downloadTask, statusOk);
        this.content = content;
    }

    @Override
    public void handle(ResultsHandler handler) {
        handler.handleContent(this);
    }
}
