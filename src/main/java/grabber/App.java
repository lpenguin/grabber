package grabber;

import grabber.data.Domain;
import grabber.data.DownloadResult;
import grabber.data.DownloadTask;
import grabber.workers.ContentWriter;
import grabber.workers.Downloader;
import grabber.workers.DownloadsHandler;

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
    private final Downloader downloader;
    private final DownloadsHandler downloadsHandler;
    private final ContentWriter contentWriter;
    private final FeedSearcher feedSearcher;

    public App() {
        contentWriter = new ContentWriter();
        downloader = new Downloader(10);
        downloadsHandler = new DownloadsHandler(contentWriter, downloader);
        downloader.setDownloadTo(downloadsHandler);
        feedSearcher = new FeedSearcher(downloader);
    }

    public static void main(String[] args){
        App app = new App();
        app.process();
    }

    public void process(){
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.execute(contentWriter);
        executorService.execute(downloader);
        executorService.execute(downloadsHandler);

        try {
            feedSearcher.search(new Domain("vk.com", new URL("http://vk.com")));
            feedSearcher.search(new Domain("habr.ru", new URL("http://habr.ru")));
            feedSearcher.search(new Domain("pravda.com.ua", new URL("http://pravda.com.ua")));
        }catch (MalformedURLException e){

        }
    }
}
