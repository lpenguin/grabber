package grabber.workers;

import grabber.FeedStore;
import grabber.result.ContentDownloadResult;
import grabber.result.DownloadResult;
import grabber.result.RssDownloadResult;
import grabber.result.RssSearchResult;
import grabber.task.ContentDownloadTask;
import grabber.task.DownloadTask;

import java.net.URL;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by nikita on 26.03.14.
 */
public class ResultsHandler implements Runnable, Pushable<DownloadResult> {
    final BlockingQueue<DownloadResult> results = new LinkedBlockingQueue<DownloadResult>();
    final ContentStore contentStore;
    final Pushable<DownloadTask> downloader;

    public ResultsHandler(ContentStore contentStore, Pushable<DownloadTask> downloader) {
        this.contentStore = contentStore;
        this.downloader = downloader;
    }

    @Override
    public void push(DownloadResult object) {
        handleResult(object);
    }

    @Override
    public void run() {
        try {
            while (true) {
                handleResult(results.take());
            }
        } catch (InterruptedException e) {
            System.out.printf("ResultsHandler interrupted. Exiting");
            e.printStackTrace();
        }

    }

    private void handleResult(DownloadResult result) {
        System.out.println("handleResult: " + result.getDownloadTask());
        result.handle(this);
    }

    public void handleContent(ContentDownloadResult result){
        contentStore.push(result);
    }

    public void handleRssSearch(RssSearchResult result) {
        if(result.isStatusOk()){
            FeedStore.getInstance().addFeed(result.getDownloadTask().getDomain(), result.getFeed());
            handleRSS(result);
        }
    }

    public void handleRSS(RssDownloadResult result){
        if(!result.isStatusOk())
            return;
        List<URL> rssLinks = result.getItems();
        for (URL rssLink : rssLinks) {
            downloader.push(new ContentDownloadTask(result.getDownloadTask().getDomain(), rssLink));
        }

    }
}
