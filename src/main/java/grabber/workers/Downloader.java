package grabber.workers;

import grabber.result.DownloadResult;
import grabber.task.DownloadTask;
import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.util.concurrent.*;

/**
 * Created by nikita on 23.03.14.
 */
public class Downloader implements Runnable, Pushable<DownloadTask> {
    Pushable<DownloadResult> downloadTo;
    final BlockingQueue<DownloadTask> tasks = new LinkedBlockingQueue<DownloadTask>();
    final ExecutorService workerService;
    final ExecutorService handlerService;
    final ResultsHandler resultsHandler;
    final BlockingQueue<Future<DownloadResult>> results = new LinkedBlockingQueue<Future<DownloadResult>>();

    public Downloader(int numThreads) {
        workerService = Executors.newFixedThreadPool(numThreads);
        handlerService = Executors.newSingleThreadExecutor();
        resultsHandler = new ResultsHandler();
    }

    public void setDownloadTo(Pushable<DownloadResult> downloadTo){
        this.downloadTo = downloadTo;
    }
    @Override
    public void push(DownloadTask object) {
        System.out.printf("New task: %s", object.getClass().getSimpleName());
        tasks.add(object);
    }

    @Override
    public void run() {
        try {
            handlerService.execute(resultsHandler);
            while(true) {
                resultsHandler.addFuture(workerService.submit(new Worker(tasks.take())));
            }
        } catch (InterruptedException e) {
            System.out.println("Downloader interrupted. Exiting");
            return;
        }
    }

    private class ResultsHandler implements Runnable{
        synchronized void addFuture(Future<DownloadResult> future){
            results.add(future);
        }

        synchronized void checkResults() throws ExecutionException, InterruptedException {
            for (Future<DownloadResult> result : results) {
                if(result.isDone()) {
                    DownloadResult downloadResult = result.get();
                    System.out.println("Task is done: "+downloadResult.getClass().getSimpleName()+", "+downloadResult.toString());
                    downloadTo.push(downloadResult);
                    results.remove(result);
                }
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    checkResults();
                }
            }catch (InterruptedException e){
                System.out.println("ResultsHandler interrupted. Exiting");
                return;
            } catch (ExecutionException e) {
                System.out.println("ResultsHandler execution ex");
                e.printStackTrace();
            }
        }
    }

    private class Worker implements Callable<DownloadResult> {
        final private DownloadTask task;

        private Worker(DownloadTask task) {
            this.task = task;
        }

        @Override
        public DownloadResult call() throws Exception {
            return task.download();
        }


    }

}
