package grabber;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by nikita on 24.03.14.
 */
public class FeedSearcher {
    private static final int HTTP_OK = 200;
    public URL search(URL site){
        URL res = searchType(site, "rss");
        if(res == null )
            return searchType(site, "atom");
        return res;
    }

    private URL searchType(URL site, String type){
        try {
            URL url = new URL(String.format("%s://%s/%s", site.getProtocol(), site.getHost(), type));
            (new SyndFeedInput()).build(new XmlReader(url));
            return url;
        }catch (IOException e) {
//            e.printStackTrace();
            return null;
        } catch (FeedException e) {
//            e.printStackTrace();
            return null;
        }
    }
}
