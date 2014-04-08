package grabber.workers;

import grabber.data.feed.FeedBase;
import grabber.store.FeedStore;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by nikita on 08.04.14.
 */
public class FeedUpdater implements Runnable {
    private final Logger logger = Logger.getLogger(getClass());
    private final Downloader downloader;
    private final FeedStore feedStore;

    private static final int FEED_UPDATE_INTERVAL = 1000 * 4;
    private static final int FEED_DOWNLOAD_INTERVAL = 1000 * 60 * 60;

    public FeedUpdater(Downloader downloader, FeedStore feedStore) {
        this.downloader = downloader;
        this.feedStore = feedStore;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                checkFeeds();
                Thread.sleep(FEED_UPDATE_INTERVAL);
            }
            logger.info("exiting");
        }catch (InterruptedException e){
            logger.error("interripted, exiting");
        }
    }

    private void checkFeeds() {
        logger.info("checking feeds");
        List<FeedBase> feeds = feedStore.listFeeds();
        for (FeedBase feed : feeds) {
            if(System.currentTimeMillis() - feed.getLastDownloadTime() > FEED_DOWNLOAD_INTERVAL){
                logger.info("New feed to download: "+feed);
                downloader.push(feed.getDownloadTask());
                feed.setLastDownloadTime(System.currentTimeMillis());
                //TODO: update feed in dao
            }
        }

    }
}
