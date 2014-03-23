package grabber;

import grabber.data.DownloadTask;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

/**
 * Created by nikita on 24.03.14.
 */
public class DownloadsFiller implements Runnable {

    private BlockingQueue<DownloadTask> downloadQueue;
    private final int sleepMillis;

    public DownloadsFiller(BlockingQueue<DownloadTask> downloadQueue, int sleepMillis) {
        this.downloadQueue = downloadQueue;
        this.sleepMillis = sleepMillis;
    }

    @Override
    public void run() {
        try {
            while (true) {
                List<DownloadTask> uris = pollNewDownloads();
                downloadQueue.addAll(uris);
                Thread.sleep(sleepMillis);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private List<DownloadTask> pollNewDownloads() throws InterruptedException{
        LinkedList<DownloadTask> urls = new LinkedList<DownloadTask>();
        try {
            urls.add(new DownloadTask(DownloadTask.Type.HTML, new URL("http://habr.ru")));
            urls.add(new DownloadTask(DownloadTask.Type.HTML, new URL("http://habr.ru")));
            urls.add(new DownloadTask(DownloadTask.Type.HTML, new URL("http://habr.ru")));
            urls.add(new DownloadTask(DownloadTask.Type.HTML, new URL("http://habr.ru")));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return urls;
    }
}
