package grabber.dao;

import grabber.data.Domain;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by nikita on 06.04.14.
 */
public interface DomainDao {
    List<Domain> queryAll() throws SQLException;
    void insert(Domain domain) throws SQLException;
    void createTable() throws SQLException;
}
