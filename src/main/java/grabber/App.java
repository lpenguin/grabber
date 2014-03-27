package grabber;

import grabber.data.Domain;
import grabber.workers.ContentStore;
import grabber.workers.Downloader;
import grabber.workers.ResultsHandler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by nikita on 23.03.14.
 */
public class App {
    private final Downloader downloader;
    private final ResultsHandler resultsHandler;

    public ContentStore getContentStore() {
        return contentStore;
    }

    private final ContentStore contentStore;
    private final FeedSearcher feedSearcher;

    private static App instance;

    public static App getInstance(){
        if(instance == null)
            instance = new App();
        return instance;
    }

    private App() {
        contentStore = new ContentStore();
        downloader = new Downloader(10);
        resultsHandler = new ResultsHandler(contentStore, downloader);
        downloader.setDownloadTo(resultsHandler);
        feedSearcher = new FeedSearcher(downloader);
        try {
            TwitterAdapter.getInstance().configure("lilac_penguin", "gfhjkm_31");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        App app = new App();
        //app.process();
    }

    public void process(){
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.execute(contentStore);
        executorService.execute(downloader);
        executorService.execute(resultsHandler);

        try {
            feedSearcher.search(new Domain("vk.com", new URL("http://vk.com")));
            feedSearcher.search(new Domain("habr.ru", new URL("http://habr.ru")));
            feedSearcher.search(new Domain("pravda.com.ua", new URL("http://pravda.com.ua")));
        }catch (MalformedURLException e){

        }
    }
}
