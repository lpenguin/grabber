package grabber.data;

import java.net.URL;

/**
 * Created by nikita on 26.03.14.
 */
public class Domain {
    private String name;
    private URL url;

    public String getName() {
        return name;
    }

    public URL getUrl() {
        return url;
    }

    public Domain(String name, URL url) {

        this.name = name;
        this.url = url;
    }
}
