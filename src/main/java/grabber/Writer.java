package grabber;

import java.net.URL;
import java.util.concurrent.BlockingQueue;

/**
 * Created by nikita on 23.03.14.
 */
public class Writer implements Runnable{
    public static class Result{
        public URL getUrl() {
            return url;
        }

        public String getBody() {
            return body;
        }

        public Result(URL url, String body) {

            this.url = url;
            this.body = body;
        }

        private URL url;
        private String body;


    }

    private BlockingQueue<Result> writeQueue;

    public Writer(BlockingQueue<Result> writeQueue) {
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

    private void write(Result result){
        System.out.println("Writing: "+result.getBody());
    }
}
