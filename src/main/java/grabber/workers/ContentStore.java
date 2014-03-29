package grabber.workers;

import grabber.result.DownloadResult;
import org.apache.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by nikita on 23.03.14.
 */
public class ContentStore implements Runnable, Pushable<DownloadResult>{
    private final Logger logger = Logger.getLogger(ContentStore.class);
    private BlockingQueue<DownloadResult> writeQueue = new LinkedBlockingQueue<DownloadResult>();

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

    private void write(DownloadResult downloadResult){
        System.out.println("Writing: "+downloadResult.getDownloadTask().getClass().getSimpleName());
    }

    @Override
    public void push(DownloadResult object) {
        writeQueue.add(object);
    }
}
