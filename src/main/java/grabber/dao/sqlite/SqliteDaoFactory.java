package grabber.dao.sqlite;

import grabber.dao.Dao;
import grabber.dao.DaoFactory;
import grabber.data.Domain;
import grabber.data.feed.FeedBase;
import grabber.task.DownloadTask;

import java.sql.Connection;

/**
 * Created by nikita on 06.04.14.
 */
public class SqliteDaoFactory implements DaoFactory {
    @Override
    public <T> Dao<T> createDao(Connection connection, Class<T> tClass) {
        if(tClass.isAssignableFrom(Domain.class)){
            return (Dao<T>) new DomainDaoImpl(connection);
        } else if(tClass.isAssignableFrom(FeedBase.class)){
            return (Dao<T>) new FeedDaoImpl(connection);
        } else if(tClass.isAssignableFrom(DownloadTask.class)){
            return (Dao<T>) new DownloadTaskDaoImpl(connection);
        }
        return null;
    }
}
