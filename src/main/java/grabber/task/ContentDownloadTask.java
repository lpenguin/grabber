package grabber.task;

import grabber.data.Domain;
import grabber.result.ContentDownloadResult;
import grabber.result.DownloadResult;
import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.net.URL;

/**
 * Created by nikita on 27.03.14.
 */
public class ContentDownloadTask extends DownloadTask{
    public URL getUrl() {
        return url;
    }

    private final URL url;

    public ContentDownloadTask(Domain domain, URL url) {
        super(domain);
        this.url = url;
    }

    @Override
    public DownloadResult download() {
        System.out.println("Downloading: "+ContentDownloadTask.class.getSimpleName()+", "+url);
        try {
            String body = Request.Get(url.toString()).execute().returnContent().asString();
            return new ContentDownloadResult(ContentDownloadTask.this, true, body);
        } catch (IOException e) {
            return new ContentDownloadResult(ContentDownloadTask.this, false, "");
        }
    }

}
