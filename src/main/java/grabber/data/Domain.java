package grabber.data;
import java.net.URL;

/**
 * Created by nikita on 26.03.14.
 */
public class Domain {
    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    private int id;
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
