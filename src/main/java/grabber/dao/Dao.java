package grabber.dao;

import grabber.data.feed.FeedBase;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by nikita on 06.04.14.
 */
public interface Dao<T> {
    List<T> queryAll() throws SQLException;
    void insert(T object) throws SQLException;
    void createTable() throws SQLException;
}
