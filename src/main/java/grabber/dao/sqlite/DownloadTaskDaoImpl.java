package grabber.dao.sqlite;

import grabber.dao.DownloadTaskDaoBase;
import grabber.task.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nikita on 06.04.14.
 */
public class DownloadTaskDaoImpl extends DownloadTaskDaoBase {
    private static final String CREATE_TABLE_QUERY =
            "CREATE TABLE tasks (id integer primary key autoincrement, domain_id integer not null, type integer not null, url text, account text, page integer)";
    private static final String ALL_QUERY = "SELECT type, id, domain_id, url, account, page FROM tasks";
    private static final String INSERT_RSS_QUERY = "INSERT INTO tasks (domain_id, type, url) values(?, ?, '?')";
    private static final String INSERT_RSS_SEARCH_QUERY = "INSERT INTO tasks (domain_id, type, url) values(?, ?, '?')";
    private static final String INSERT_CONTENT_QUERY = "INSERT INTO tasks (domain_id, type, url) values(?, ?, '?')";
    private static final String INSERT_TWITTER_QUERY = "INSERT INTO feeds (domain_id, type, account, page) values(?, ?, '?', ?)";

    protected DownloadTaskDaoImpl(Connection connection) {
        super(connection);
    }

    @Override
    public void insertContentTask(ContentDownloadTask task) throws SQLException {
        Statement statement = getConnection().createStatement();
        statement.executeUpdate(substituteValues(INSERT_CONTENT_QUERY, new Object[]{task.getDomainId(), TaskType.Content.ordinal(), task.getUrl()}));
        task.setId(Helper.getLastInsertId(statement));
        statement.close();
    }

    @Override
    public void insertRssTask(RssDownloadTask task) throws SQLException {
        Statement statement = getConnection().createStatement();
        statement.executeUpdate(substituteValues(INSERT_RSS_QUERY, new Object[]{task.getDomainId(), TaskType.Rss.ordinal(), task.getUrl()}));
        task.setId(Helper.getLastInsertId(statement));
        statement.close();
    }

    @Override
    public void insertRssSearchTask(RssSearchTask task) throws SQLException {
        Statement statement = getConnection().createStatement();
        statement.executeUpdate(substituteValues(INSERT_RSS_SEARCH_QUERY, new Object[]{task.getDomainId(), TaskType.RssSearch.ordinal(), task.getUrl()}));
        task.setId(Helper.getLastInsertId(statement));
        statement.close();
    }

    @Override
    public void insertTwitterTask(TwitterDownloadTask task) throws SQLException {
        Statement statement = getConnection().createStatement();
        statement.executeUpdate(substituteValues(INSERT_TWITTER_QUERY, new Object[]{
                        task.getDomainId(), TaskType.Twitter.ordinal(), task.getAccount(), task.getPage()}));
        task.setId(Helper.getLastInsertId(statement));
        statement.close();
    }

    @Override
    public List<DownloadTask> queryAll() throws SQLException {
        List<DownloadTask> tasks = new LinkedList<DownloadTask>();

        Statement statement = getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(ALL_QUERY);
        while (resultSet.next()){
            TaskType taskType = TaskType.values()[resultSet.getInt(1)];
            switch (taskType){
                case Rss:
                    try {
                        tasks.add(new RssDownloadTask(resultSet.getInt(2), resultSet.getInt(3), new URL(resultSet.getString(4))));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    break;
                case RssSearch:
                    try {
                        tasks.add(new RssDownloadTask(resultSet.getInt(2), resultSet.getInt(3), new URL(resultSet.getString(4))));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    break;
                case Content:
                    try {
                        tasks.add(new ContentDownloadTask(resultSet.getInt(2), resultSet.getInt(3), new URL(resultSet.getString(4))));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    break;
                case Twitter:
                    tasks.add(new TwitterDownloadTask(resultSet.getInt(2), resultSet.getInt(3), resultSet.getString(5), resultSet.getInt(6)));
                    break;
            }
        }
        statement.close();
        return tasks;
    }

    @Override
    public void createTable() throws SQLException {
        Statement statement = getConnection().createStatement();
        statement.execute(CREATE_TABLE_QUERY);
        statement.close();
    }
}
