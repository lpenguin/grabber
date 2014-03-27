package grabber.result;

import com.sun.syndication.feed.synd.SyndFeed;
import grabber.task.DownloadTask;
import grabber.task.RssDownloadTask;
import grabber.workers.ResultsHandler;

/**
 * Created by nikita on 27.03.14.
 */
public class RssSearchResult extends RssDownloadResult{
    @Override
    public void handle(ResultsHandler handler) {
        handler.handleRssSearch(this);
    }

    public RssSearchResult(RssDownloadTask downloadTask, boolean statusOk, SyndFeed feed) {
        super(downloadTask, statusOk, feed);
    }
}
