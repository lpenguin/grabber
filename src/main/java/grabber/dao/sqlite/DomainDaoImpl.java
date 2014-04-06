package grabber.dao.sqlite;

import grabber.dao.DaoBase;
import grabber.dao.DomainDao;
import grabber.data.Domain;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nikita on 06.04.14.
 */
public class DomainDaoImpl extends DaoBase implements DomainDao{
    private static final String CREATE_TABLE_QUERY = "CREATE TABLE domains (id integer primary key autoincrement, name text, url text)";
    private static final String ALL_QUERY = "SELECT id, name, url FROM domains";
    private static final String INSERT_QUERY = "INSERT INTO domains (name, url) values('?', '?')";

    protected DomainDaoImpl(Connection connection) {
        super(connection);
    }

    @Override
    public List<Domain> queryAll() throws SQLException {
        List<Domain> domains = new LinkedList<Domain>();

        Statement statement = getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(ALL_QUERY);
        while (resultSet.next()){
            domains.add(new Domain(resultSet.getInt(0), resultSet.getString(1), resultSet.getString(2)));
        }
        statement.close();
        return domains;
    }

    @Override
    public void insert(Domain domain) throws SQLException {
        Statement statement = getConnection().createStatement();
        statement.executeUpdate(substituteValues(INSERT_QUERY, new String[]{domain.getName(), domain.getUrl()}),
                Statement.RETURN_GENERATED_KEYS);
        domain.setId(getInsertedId(statement));
        statement.close();
    }

    @Override
    public void createTable() throws SQLException {
        Statement statement = getConnection().createStatement();
        statement.execute(CREATE_TABLE_QUERY);
        statement.close();
    }
}
