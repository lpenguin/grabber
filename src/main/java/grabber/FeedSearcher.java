package grabber;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import grabber.data.Domain;
import grabber.data.DownloadTask;
import grabber.workers.Pushable;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
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
            toDownload.push(new DownloadTask(DownloadTask.Type.RSS_SEARCH, site, searchType(site.getUrl(), "rss")));
            toDownload.push(new DownloadTask(DownloadTask.Type.RSS_SEARCH, site, searchType(site.getUrl(), "atom")));
        }catch (MalformedURLException e){
            System.out.println("Invalid url: "+e);
        }
    }

    private URL searchType(URL site, String type) throws MalformedURLException {
        return new URL(String.format("%s://%s/%s", site.getProtocol(), site.getHost(), type));
    }
}
