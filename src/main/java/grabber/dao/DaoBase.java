package grabber.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

/**
 * Created by nikita on 06.04.14.
 */
public abstract class DaoBase {
    private final Connection connection;

    protected DaoBase(Connection connection) {

        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public static String substituteValues(String query, Object[] values){
        for (Object value : values) {
            query = query.replaceFirst("\\?", value.toString());
        }
        return query;
    }

    public static int getInsertedId(Statement statement) throws SQLException {
        return statement.getGeneratedKeys().getInt(0);
    }
}
