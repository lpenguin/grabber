package grabber;

import grabber.data.DownloadResult;
import grabber.data.DownloadTask;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by nikita on 23.03.14.
 */
public class App {
    private BlockingQueue<DownloadTask> downloadQueue = new LinkedBlockingDeque<DownloadTask>();
    private BlockingQueue<DownloadResult> writeQueue = new LinkedBlockingDeque<DownloadResult>();

    private int writersCount;
    private int downloadersCount;

    public App(int writersCount, int downloadersCount) {
        this.writersCount = writersCount;
        this.downloadersCount = downloadersCount;

    }

    public static void main(String[] args){
        App app = new App(3, 2);
        try {
            System.out.println((new FeedSearcher()).search(new URL("http://habr.ru")));
            System.out.println((new FeedSearcher()).search(new URL("http://vk.com")));
            System.out.println((new FeedSearcher()).search(new URL("http://pravda.com.ua")));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //app.process();
    }

    public void process(){
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for(int i = 0; i < writersCount; i++){
            executorService.submit(new Writer(writeQueue));
        }

        for(int i = 0; i < downloadersCount; i++){
            executorService.submit(new Downloader(downloadQueue, writeQueue));
        }

        executorService.submit(new DownloadsFiller(downloadQueue, 5*1000));
    }
}
