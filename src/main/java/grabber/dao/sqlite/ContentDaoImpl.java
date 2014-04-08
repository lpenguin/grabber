package grabber.dao.sqlite;

import grabber.dao.Dao;
import grabber.dao.DaoBase;
import grabber.data.Content;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by nikita on 08.04.14.
 */
public class ContentDaoImpl extends DaoBase implements Dao<Content>{
    private static final String CREATE_TABLE_QUERY = "CREATE TABLE contents (id integer primary key autoincrement, task_id integer, content text)";
    private static final String ALL_QUERY = "SELECT id, task_id, content FROM contents";
    private static final String INSERT_QUERY = "INSERT INTO contents (task_id, content) values(?, '?')";


    protected ContentDaoImpl(Connection connection) {
        super(connection);
    }

    @Override
    public List<Content> queryAll() throws SQLException {
        throw new NotImplementedException();
    }

    @Override
    public void insert(Content object) throws SQLException {
        throw new NotImplementedException();
    }

    @Override
    public void createTable() throws SQLException {
        throw new NotImplementedException();
    }
}
