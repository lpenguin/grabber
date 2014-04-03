package grabber.database;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
* Created by nikita on 03.04.14.
*/
public class BufferedWriter<T>{
    private final Dao<T, ?> dao;
    private List<T> objects = new LinkedList<T>();
    private final int flushSize;

    public BufferedWriter(Dao<T, ?> dao, int flushSize){
        this.dao = dao;
        this.flushSize = flushSize;
    }

    public void add(T object) throws SQLException {
        objects.add(object);
        if(objects.size() >= flushSize)
            flush();
    }

    public void flush() throws SQLException {
        for (T object : objects) {
            dao.create(object);
        }
        objects.clear();

    }
}
