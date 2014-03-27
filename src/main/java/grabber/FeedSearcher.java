package grabber;

import grabber.data.Domain;
import grabber.task.DownloadTask;
import grabber.task.RssSearchTask;
import grabber.workers.Pushable;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by nikita on 24.03.14.
 */
public class FeedSearcher {
    public FeedSearcher(Pushable<DownloadTask> toDownload) {
        this.toDownload = toDownload;
    }

    private final Pushable<DownloadTask> toDownload;

    public void search(Domain site){
        try {
            toDownload.push(new RssSearchTask(site, searchType(site.getUrl(), "atom")));
            toDownload.push(new RssSearchTask(site, searchType(site.getUrl(), "rss")));
        }catch (MalformedURLException e){
            System.out.println("Invalid url: "+e);
        }
    }

    private URL searchType(URL site, String type) throws MalformedURLException {
        return new URL(String.format("%s://%s/%s", site.getProtocol(), site.getHost(), type));
    }
}
