package grabber.dao.base;

import grabber.dao.Dao;
import grabber.dao.base.DaoBase;
import grabber.task.*;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by nikita on 06.04.14.
 */
public abstract class DownloadTaskDaoBase extends DaoBase implements Dao<DownloadTask> {
    protected DownloadTaskDaoBase(Connection connection) {
        super(connection);
    }

    @Override
    public void insert(DownloadTask task) throws SQLException {
        if (task instanceof ContentDownloadTask)
            insertContentTask((ContentDownloadTask) task);
        else if (task instanceof RssSearchTask)
            insertRssSearchTask((RssSearchTask) task);
        else if (task instanceof RssDownloadTask)
            insertRssTask((RssDownloadTask) task);
        else if (task instanceof TwitterDownloadTask)
            insertTwitterTask((TwitterDownloadTask) task);
        else throw new SQLException("There is no method to insert task: " + task);
    }

    public abstract void insertContentTask(ContentDownloadTask task) throws SQLException;
    public abstract void insertRssTask(RssDownloadTask task) throws SQLException;
    public abstract void insertRssSearchTask(RssSearchTask task) throws SQLException;
    public abstract void insertTwitterTask(TwitterDownloadTask task) throws SQLException;

    public enum TaskType {
        Content, Rss, RssSearch, Twitter
    }
}
