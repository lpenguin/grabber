package grabber.workers;

import grabber.task.result.*;
import grabber.task.ContentDownloadTask;
import grabber.task.DownloadTask;
import grabber.task.TwitterDownloadTask;
import grabber.store.ContentStore;
import grabber.store.FeedStore;
import org.apache.log4j.Logger;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.URLEntity;

import java.net.MalformedURLException;
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

    private final Logger logger = Logger.getLogger(ResultsHandler.class);

    public boolean isDownloadAfterSearch() {
        return downloadAfterSearch;
    }

    public void setDownloadAfterSearch(boolean downloadAfterSearch) {
        this.downloadAfterSearch = downloadAfterSearch;
    }

    private boolean downloadAfterSearch = true;

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
            logger.info("Starting");
            while (!Thread.interrupted()) {
                handleResult(results.take());
            }
            logger.info("exiting");
        } catch (InterruptedException e) {
            logger.error("interrupted");
        }

    }

    private void handleResult(DownloadResult result) {
        System.out.println("handleResult: " + result.getDownloadTask());
        result.handle(this);
    }

    public void handleContent(ContentDownloadResult result){
        contentStore.write(result);
    }

    public void handleRssSearch(RssSearchResult result) {
        if(result.isStatusOk()){
            FeedStore.getInstance().addFeed(result.getFeed());
            if(downloadAfterSearch)
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

    public void handleTwitter(TwitterDownloadResult result){
        ResponseList<Status> userTimeline = result.getUserTimeline();
        for (Status status : userTimeline) {
            URLEntity[] urlEntities = status.getURLEntities();
            for (URLEntity urlEntity : urlEntities) {
                System.out.println("status.getId(): "+status.getId()+" "+urlEntity.getExpandedURL());
                try {
                    downloader.push(new ContentDownloadTask(result.getDownloadTask().getDomain(), new URL(urlEntity.getExpandedURL())));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }



//            System.out.println("status.getInReplyToScreenName(): "+status.getRetweetedStatus());
//            List<URL> links = linkExtractor.getLinks(status.getText());
//            for (URL link : links) {
//                downloader.push(new ContentDownloadTask(result.getDownloadTask().getDomain(), link));
//            }

        }

        TwitterDownloadTask task = (TwitterDownloadTask) result.getDownloadTask();
        downloader.push(new TwitterDownloadTask(task.getDomain(), task.getAccount(), task.getPage()+1));

    }


}
