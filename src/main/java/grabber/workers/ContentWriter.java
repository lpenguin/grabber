package grabber.workers;

import grabber.data.DownloadResult;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by nikita on 23.03.14.
 */
public class ContentWriter implements Runnable, Pushable<DownloadResult>{

    private BlockingQueue<DownloadResult> writeQueue = new LinkedBlockingQueue<DownloadResult>();

    public ContentWriter() {

    }

    @Override
    public void run() {
        try {
            while (true) {
                write(writeQueue.take());
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private void write(DownloadResult downloadResult){
        System.out.println("Writing: "+downloadResult.getDownloadTask().getUrl());
    }

    @Override
    public void push(DownloadResult object) {
        writeQueue.add(object);
    }
}
