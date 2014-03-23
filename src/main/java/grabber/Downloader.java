package grabber;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.BlockingQueue;

/**
 * Created by nikita on 23.03.14.
 */
public class Downloader implements Runnable{
    private BlockingQueue<URL> downloadQueue;
    private BlockingQueue<Writer.Result> writeQueue;


    public Downloader(BlockingQueue<URL> downloadQueue, BlockingQueue<Writer.Result> writeQueue) {
        this.downloadQueue = downloadQueue;
        this.writeQueue = writeQueue;
    }

    @Override
    public void run() {
        try {
            while(true) {
                try{
                    writeQueue.put(download(downloadQueue.take()));
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Writer.Result download(URL url) throws IOException {
        System.out.println("Downloading: "+url);
        return new Writer.Result(url, Jsoup.connect(url.toString()).get().html());
    }
}
