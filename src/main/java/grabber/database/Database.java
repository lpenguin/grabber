package grabber.database;

import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import grabber.data.Domain;

import java.sql.SQLException;

/**
 * Created by nikita on 02.04.14.
 */
public class Database {
    private static Database instance;
    private DatabaseType databaseType;

    public JdbcConnectionSource getConnectionSource() {
        return connectionSource;
    }

    private JdbcConnectionSource connectionSource;

    public static Database getInstance(){
        if(instance == null)
            instance = new Database();
        return instance;
    }

    private Database(){}

    public void connect(String connectionStr) throws SQLException {
        connectionSource = new JdbcConnectionSource(connectionStr);
//        this.databaseType = databaseType;
        createTables();
    }

    public void createTables(){
        try {
            TableUtils.createTableIfNotExists(connectionSource, Domain.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
