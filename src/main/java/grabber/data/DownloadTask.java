package grabber.data;

import java.net.URL;

/**
 * Created by nikita on 24.03.14.
 */
public class DownloadTask {
    public enum Type {
        RSS, HTML
    }

    private Type type;
    private URL url;

    public Type getType() {
        return type;
    }

    public URL getUrl() {
        return url;
    }

    public DownloadTask(Type type, URL url) {

        this.type = type;
        this.url = url;
    }
}
