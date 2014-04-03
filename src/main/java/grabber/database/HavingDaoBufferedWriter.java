package grabber.database;

import com.j256.ormlite.dao.Dao;
import grabber.data.HavingDao;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nikita on 03.04.14.
 */
public class HavingDaoBufferedWriter<T extends HavingDao>{
    private final Database database;
    private List<T> objects = new LinkedList<T>();
    private final int flushSize;

    public HavingDaoBufferedWriter(Database database, int flushSize){
        this.flushSize = flushSize;
        this.database = database;
    }

    public void add(T object) throws SQLException {
        objects.add(object);
        if(objects.size() >= flushSize)
            flush();
    }

    public void flush() throws SQLException {
        for (T object : objects) {
            object.getDao(database).create(object);
        }
        objects.clear();

    }
}
