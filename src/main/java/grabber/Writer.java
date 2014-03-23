package grabber;

import grabber.data.DownloadResult;

import java.util.concurrent.BlockingQueue;

/**
 * Created by nikita on 23.03.14.
 */
public class Writer implements Runnable{

    private BlockingQueue<DownloadResult> writeQueue;

    public Writer(BlockingQueue<DownloadResult> writeQueue) {
        this.writeQueue = writeQueue;
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
        System.out.println("Writing: "+ downloadResult.getBody());
    }
}
