package grabber.data;

import java.net.URL;

/**
 * Created by nikita on 24.03.14.
 */
public class DownloadTask {
    public enum Type {
        RSS, HTML, RSS_SEARCH
    }

    final private Type type;
    final private URL url;

    public Domain getDomain() {
        return domain;
    }

    final private Domain domain;

    public Type getType() {
        return type;
    }

    public URL getUrl() {
        return url;
    }

    public DownloadTask(Type type, Domain domain, URL url) {
        this.domain = domain;
        this.type = type;
        this.url = url;
    }
}
