package grabber.dao.sqlite;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by nikita on 06.04.14.
 */
public class Helper {
    public static int getLastInsertId(Statement statement) throws SQLException {
        ResultSet generatedKeys = statement.executeQuery("SELECT last_insert_rowid()");
        if (generatedKeys.next()) {
            return generatedKeys.getInt(1);
        }
        return -1;
    }
}
