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
