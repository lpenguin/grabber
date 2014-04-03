package grabber.data;

import com.j256.ormlite.dao.Dao;
import grabber.database.Database;

/**
 * Created by nikita on 03.04.14.
 */
public interface HavingDao {
    Dao getDao(Database database);
}
