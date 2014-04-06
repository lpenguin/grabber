package grabber.data;

import grabber.data.feed.FeedBase;

/**
 * Created by nikita on 30.03.14.
 */
public class Content {
    private int id;
    private String text;
    private FeedBase feedBase;

    public Content(int id, String text, FeedBase feedBase) {
        this.id = id;
        this.text = text;
        this.feedBase = feedBase;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public FeedBase getFeedBase() {
        return feedBase;
    }
}
