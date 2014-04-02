package grabber.data;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.net.URL;

/**
 * Created by nikita on 26.03.14.
 */
@DatabaseTable(tableName = "domains")
public class Domain {
    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String name;
    @DatabaseField
    private String url;

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public Domain(){}
    public Domain(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
