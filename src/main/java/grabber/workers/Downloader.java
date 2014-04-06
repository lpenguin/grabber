package grabber.workers;

import grabber.database.BufferedWriter;
import grabber.database.Database;
import grabber.task.result.DownloadResult;
import grabber.task.DownloadTask;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.concurrent.*;

/**
 * Created by nikita on 23.03.14.
 */
public class Downloader implements Runnable, Pushable<DownloadTask> {
    private static final int DOWNLOAD_TASK_BUFFER = 10;
    private final Logger logger = Logger.getLogger(Downloader.class);
    private final Database database;

    Pushable<DownloadResult> downloadTo;
    final BlockingQueue<DownloadTask> tasks = new LinkedBlockingQueue<DownloadTask>();
    final ExecutorService workerService;
    final ExecutorService handlerService;
    Future resultsHandlerFuture;
    final ResultsHandler resultsHandler;
    final BlockingQueue<Future<DownloadResult>> results = new LinkedBlockingQueue<Future<DownloadResult>>();
    BufferedWriter<DownloadTask> taskWriter;

    public Downloader(Database database, int numThreads) {
        workerService = Executors.newFixedThreadPool(numThreads);
        handlerService = Executors.newSingleThreadExecutor();
        resultsHandler = new ResultsHandler();
        this.database = database;
        taskWriter = new BufferedWriter<DownloadTask>(database.getTaskDao(), DOWNLOAD_TASK_BUFFER);
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
            logger.info("Starting");
            resultsHandlerFuture = handlerService.submit(resultsHandler);
            while(!Thread.interrupted()) {
                DownloadTask task = tasks.take();
                try {
                    taskWriter.add(task);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                resultsHandler.addFuture(workerService.submit(new Worker(task)));
            }
            logger.info("exiting");
            resultsHandlerFuture.cancel(true);
            taskWriter.flush();
        } catch (InterruptedException e) {
            logger.error("interrupted");
            resultsHandlerFuture.cancel(true);
            try {
                taskWriter.flush();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private class ResultsHandler implements Runnable{
        private final Logger logger = Logger.getLogger(ResultsHandler.class);
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
                while (!Thread.interrupted()) {
                    checkResults();
                }
                logger.info("exiting");
            }catch (InterruptedException e){
                logger.error("interrupted");
            } catch (ExecutionException e) {
                logger.error("ExecutionException", e);
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
