package grabber.data;

import grabber.task.DownloadTask;

/**
 * Created by nikita on 30.03.14.
 */
public class Content {
    private DownloadTask downloadTask;
    private int id;
    private int taskId;
    private String text;

    public Content(int id, String text, DownloadTask downloadTask) {
        this.id = id;
        this.text = text;
        this.downloadTask = downloadTask;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public DownloadTask getDownloadTask() {
        return downloadTask;
    }

    public void setDownloadTask(DownloadTask downloadTask) {
        this.downloadTask = downloadTask;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }
}
