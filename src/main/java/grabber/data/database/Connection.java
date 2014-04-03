package grabber.data.database;

import com.j256.ormlite.jdbc.JdbcConnectionSource;

import javax.sql.ConnectionPoolDataSource;
import java.sql.SQLException;

/**
 * Created by nikita on 30.03.14.
 */
public class Connection {
    private final static String SOURCE_URL = "";
    public static JdbcConnectionSource getSource() throws SQLException {
        JdbcConnectionSource connectionSource = new JdbcConnectionSource(SOURCE_URL);
        return connectionSource;
    }
}
