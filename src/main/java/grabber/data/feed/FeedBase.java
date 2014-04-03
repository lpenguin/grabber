package grabber.data.feed;

import com.j256.ormlite.field.DatabaseField;
import grabber.data.Domain;
import grabber.data.HavingDao;
import grabber.task.DownloadTask;

/**
 * Created by nikita on 27.03.14.
 */
public abstract class FeedBase implements HavingDao{
    @DatabaseField(id = true)
    private int id;

//    @DatabaseField(canBeNull = false, foreign = true)
    private Domain domain;

    public FeedBase(){}
    public FeedBase(Domain domain) {
        this.domain = domain;
    }

    public Domain getDomain() {
        return domain;
    }

    public abstract DownloadTask getTask();
}
