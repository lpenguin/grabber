package grabber.data;

import java.net.URL;

/**
* Created by nikita on 24.03.14.
*/
public class DownloadResult {
    public URL getUrl() {
        return url;
    }

    public String getBody() {
        return body;
    }

    public DownloadResult(URL url, String body) {

        this.url = url;
        this.body = body;
    }

    private URL url;
    private String body;


}
