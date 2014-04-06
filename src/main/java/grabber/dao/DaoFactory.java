package grabber.dao;

import java.sql.Connection;

/**
 * Created by nikita on 06.04.14.
 */
public interface DaoFactory {
    <T> Dao<T> createDao(Connection connection, Class<T> tClass);
}
