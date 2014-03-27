package grabber.result;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import grabber.feed.RssFeed;
import grabber.task.RssDownloadTask;
import grabber.workers.ResultsHandler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nikita on 27.03.14.
 */
public class RssDownloadResult extends DownloadResult {
    private final SyndFeed feedInner;

    public RssFeed getFeed() {
        return feed;
    }

    private final RssFeed feed;

    public RssDownloadResult(RssDownloadTask downloadTask, boolean statusOk, SyndFeed feedInner) {
        super(downloadTask, statusOk);
        this.feedInner = feedInner;
        feed = new RssFeed(downloadTask.getDomain(), downloadTask.getUrl());
    }

    public List<URL> getItems(){
        List<URL> res = new LinkedList<URL>();
        List entries = feedInner.getEntries();
        System.out.println("Got rss entries: " + entries.size() + ", " + getDownloadTask().toString());
        for (Object e : entries) {
            SyndEntry entry = (SyndEntry)e;
            try {
                res.add(new URL(entry.getUri()));
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }
        }
        return res;
    }

    @Override
    public void handle(ResultsHandler handler) {
        handler.handleRSS(this);
    }
}
