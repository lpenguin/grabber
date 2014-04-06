package grabber.dao;

import grabber.data.feed.FeedBase;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by nikita on 06.04.14.
 */
public interface FeedDao {
    List<FeedBase> queryAll() throws SQLException;
    void insert(FeedBase feedBase) throws SQLException;
    void createTable() throws SQLException;
}
