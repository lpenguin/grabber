package grabber.task;

import com.sun.syndication.io.FeedException;
import grabber.data.Domain;
import grabber.task.result.DownloadResult;
import grabber.task.result.RssSearchResult;

import java.io.IOException;
import java.net.URL;

/**
 * Created by nikita on 27.03.14.
 */
public class RssSearchTask extends RssDownloadTask {
    public RssSearchTask(int id, int domainId, URL url) {
        super(id, domainId, url);
    }

    public RssSearchTask(Domain domain, URL url) {
        super(domain, url);
    }

    @Override
    public DownloadResult download() {
        try {
            return new RssSearchResult(this, true, downloadFeed());
        } catch (IOException e) {
            return new RssSearchResult(this, false, null);
        } catch (FeedException e) {
            return new RssSearchResult(this, false, null);
        }


    }
}
