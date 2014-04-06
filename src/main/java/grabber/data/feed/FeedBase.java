package grabber.data.feed;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import grabber.data.Domain;
import grabber.data.HavingDao;
import grabber.database.Saveble;
import grabber.task.DownloadTask;

/**
 * Created by nikita on 27.03.14.
 */

public abstract class FeedBase implements HavingDao, Saveble{
    @DatabaseField(generatedId = true, canBeNull = false)
    private int id;

    @DatabaseField(canBeNull = false)
    private int domainId;

    public void setDomainId(int domainId) {
        this.domainId = domainId;
    }
    public int getDomainId() {
        return domainId;
    }

    @Override
    public void save() {
        domainId = domain.getId();
    }

    //    @DatabaseField(canBeNull = false, foreign = true)
    private Domain domain;

    public FeedBase(){}

    public FeedBase(Domain domain) {
        this.domain = domain;
    }

    public FeedBase(int id, int domainId){
        this.id = id;
        this.domainId = domainId;
    }

    public Domain getDomain() {
        return domain;
    }

    public abstract DownloadTask getTask();


    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public void setId(int id) {
        this.id = id;
    }
}
