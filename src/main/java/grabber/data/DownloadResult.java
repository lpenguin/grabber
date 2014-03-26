package grabber.data;

import java.net.URL;

/**
* Created by nikita on 24.03.14.
*/
public class DownloadResult {
    public DownloadTask getDownloadTask() {
        return downloadTask;
    }

    public String getBody() {
        return body;
    }

    public DownloadResult(DownloadTask downloadTask, String body, boolean status) {
        this.downloadTask = downloadTask;
        this.body = body;
        this.status = status;
    }

    private final DownloadTask downloadTask;
    private final String body;
    private final boolean status;


    public boolean isStatusOk() {
        return status;
    }
}
