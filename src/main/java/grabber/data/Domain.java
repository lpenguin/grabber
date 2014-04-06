package grabber.data;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import grabber.database.Database;

/**
 * Created by nikita on 26.03.14.
 */

@DatabaseTable(tableName = "domains")
public class Domain implements HavingDao {
    @DatabaseField(generatedId = true, canBeNull = false)
    private int id;
    @DatabaseField
    private String name;
    @DatabaseField
    private String url;

    public Domain() {
    }

    public Domain(int id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    public Domain(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public Dao getDao(Database database) {
        return database.getDomainDao();
    }
}
