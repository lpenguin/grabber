package grabber.store;

import grabber.result.ContentDownloadResult;
import org.apache.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by nikita on 23.03.14.
 */
public class ContentStore implements Runnable {
    private final Logger logger = Logger.getLogger(ContentStore.class);
    private BlockingQueue<ContentDownloadResult> writeQueue = new LinkedBlockingQueue<ContentDownloadResult>();

    public ContentStore() {

    }

    @Override
    public void run() {
        try {
            logger.info("Starting");
            while (!Thread.interrupted()) {
                write(writeQueue.take());
            }
            logger.info("exiting");
        }catch (InterruptedException e){
            logger.error("interrupted");
        }
    }

    public synchronized void write(ContentDownloadResult downloadResult){
        System.out.println("Writing: "+downloadResult.getDownloadTask().getClass().getSimpleName());
    }

}
