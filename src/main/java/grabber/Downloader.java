package grabber;

import java.net.URL;
import java.util.concurrent.BlockingQueue;

/**
 * Created by nikita on 23.03.14.
 */
public class Downloader implements Runnable{
    private BlockingQueue<URL> queue;
    private Writer writer;


    public Downloader(BlockingQueue<URL> queue, Writer writer) {
        this.queue = queue;
        this.writer = writer;
    }

    @Override
    public void run() {
        try {
            writer.write(download(queue.take()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Writer.Result download(URL url){
        return null;
    }
}
