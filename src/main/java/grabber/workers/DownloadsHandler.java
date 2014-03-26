package grabber.workers;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import grabber.FeedStore;
import grabber.data.DownloadResult;
import grabber.data.DownloadTask;

import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by nikita on 26.03.14.
 */
public class DownloadsHandler implements Runnable, Pushable<DownloadResult> {
    final BlockingQueue<DownloadResult> results = new LinkedBlockingQueue<DownloadResult>();
    final Pushable<DownloadResult> contentWriter;
    final Pushable<DownloadTask> downloader;

    public DownloadsHandler(Pushable<DownloadResult> contentWriter, Pushable<DownloadTask> downloader) {
        this.contentWriter = contentWriter;
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
            System.out.printf("DownloadsHandler interrupted. Exiting");
            e.printStackTrace();
        }

    }

    private void handleResult(DownloadResult result){
        System.out.println("handleResult: "+result.getDownloadTask().getType()+", "+result.getDownloadTask().getUrl());
        switch (result.getDownloadTask().getType()){
            case HTML:
                handleHTML(result);
                break;
            case RSS:
                handleRSS(result);
                break;
            case RSS_SEARCH:
                handleRssSearch(result);
                break;
            
        }
    }

    private void handleHTML(DownloadResult result){
        contentWriter.push(result);
    }

    private void handleRssSearch(DownloadResult result) {
        if(result.isStatusOk()){
            FeedStore.getInstance().addFeed(result.getDownloadTask().getDomain(), result.getDownloadTask().getUrl());
            downloader.push(new DownloadTask(DownloadTask.Type.RSS, result.getDownloadTask().getDomain(), result.getDownloadTask().getUrl()));
        }
    }

    private void handleRSS(DownloadResult result){
        if(!result.isStatusOk())
            return;
        List<URL> rssLinks = getRssLinks(result);
        for (URL rssLink : rssLinks) {
            downloader.push(new DownloadTask(DownloadTask.Type.HTML, result.getDownloadTask().getDomain(), rssLink));
        }

    }
    
    private List<URL> getRssLinks(DownloadResult result){
        List<URL> res = new LinkedList<URL>();
        try {
            SyndFeed feed = (new SyndFeedInput()).build(new StringReader(result.getBody()));
            List entries = feed.getEntries();
            System.out.println("Got rss entries: "+entries.size()+", "+result.getDownloadTask().getUrl());
            for (Object e : entries) {
                SyndEntry entry = (SyndEntry)e;
                res.add(new URL(entry.getUri()));
            }

        } catch (FeedException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return res;
    }
}
