package grabber.task;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import grabber.data.Domain;
import grabber.result.DownloadResult;
import grabber.result.RssDownloadResult;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

/**
 * Created by nikita on 27.03.14.
 */
public class RssDownloadTask extends DownloadTask{
    public RssDownloadTask(Domain domain, URL url) {
        super(domain);
        this.url = url;
    }

    public URL getUrl() {
        return url;
    }

    private final URL url;

    protected SyndFeed downloadFeed() throws IOException, FeedException {
         return (new SyndFeedInput()).build(new XmlReader(url));

    }

    @Override
    public DownloadResult download() {
        try {
            return new RssDownloadResult(this, true, downloadFeed());
        } catch (IOException e) {
            return new RssDownloadResult(this, false, null);
        } catch (FeedException e) {
            return new RssDownloadResult(this, false, null);
        }

    }
}
