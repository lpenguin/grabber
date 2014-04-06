package grabber;

import grabber.dao.sqlite.SqliteDaoFactory;
import grabber.database.Database;
import grabber.store.ContentStore;
import grabber.store.FeedStore;
import grabber.utils.FeedSearcher;
import grabber.utils.TwitterConfigurator;
import grabber.workers.Downloader;
import grabber.workers.ResultsHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


/**
 * Created by nikita on 23.03.14.
 */
public class App {
    private final Downloader downloader;
    private final ResultsHandler resultsHandler;
    private ExecutorService executorService;
    private final List<Future> futures = new LinkedList<Future>();

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
            TwitterConfigurator.getInstance().configure();
            if(TwitterConfigurator.getInstance().getAccessToken() == null)
                TwitterConfigurator.getInstance().loadAccessTokenInteractive();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                System.out.println("SHUTDOWN HOOOK");
                for (Future future : futures) {
                    future.cancel(true);
                }

                //TODO: нужен ли shutdown и awaitTermination?
                try {
                    executorService.shutdown();
                    executorService.awaitTermination(10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        });
    }

    public static void main(String[] args){
        String connString = "jdbc:sqlite:testdb.db";
        try {
            Database database = new Database(new SqliteDaoFactory(), connString, false);
            FeedStore.getInstance().initialize(database);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        Admin admin = new Admin();
        admin.process(args);
//        App app = new App();
//        app.process();
//        try {
//            System.in.read();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        System.exit(0);
    }

    public void process(){
        executorService = Executors.newFixedThreadPool(3);
        futures.add(executorService.submit(contentStore));
        futures.add(executorService.submit(downloader));
        futures.add(executorService.submit(resultsHandler));
    }
}
