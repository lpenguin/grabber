package grabber;

import grabber.data.DownloadResult;
import grabber.data.DownloadTask;
import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.BlockingQueue;

/**
 * Created by nikita on 23.03.14.
 */
public class Downloader implements Runnable{
    private BlockingQueue<DownloadTask> downloadQueue;
    private BlockingQueue<DownloadResult> writeQueue;


    public Downloader(BlockingQueue<DownloadTask> downloadQueue, BlockingQueue<DownloadResult> writeQueue) {
        this.downloadQueue = downloadQueue;
        this.writeQueue = writeQueue;
    }

    @Override
    public void run() {
        try {
            while(true) {
                try{
                    writeQueue.put(download(downloadQueue.take()));
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private DownloadResult download(DownloadTask task) throws IOException {
        System.out.println("Downloading: "+task.getUrl());
        return new DownloadResult(task.getUrl(), Request.Get(task.getUrl().toString()).execute().returnContent().asString());
    }
}
