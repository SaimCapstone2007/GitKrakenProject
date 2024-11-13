import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GitKrakenDemo
{

    private static final int THREAD_COUNT = 1000;
    private static final long RANGE_LIMIT = 1000000000L;

    public static void main(String[] args)
    {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        List<Future<Long>> futures = new ArrayList<>();

        long rangePerThread = RANGE_LIMIT / THREAD_COUNT;

        long startTime = System.nanoTime();

        for (int i = 0; i < THREAD_COUNT; i++)
        {
            long start = i * rangePerThread + 1;
            long end = (i == THREAD_COUNT - 1) ? RANGE_LIMIT : start + rangePerThread - 1;
            Callable<Long> task = () -> countNumbersInRange(start, end);
            Future<Long> future = executor.submit(task);
            futures.add(future);
        }

        long totalCount = 0;
        for (Future<Long> future : futures)
        {
            try
            {
                totalCount += future.get();
            }
            catch (InterruptedException | ExecutionException e)
            {
                e.printStackTrace();
            }
        }//hi

        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;

        executor.shutdown();

        System.out.println("Total count from all threads: " + totalCount);
        System.out.println("Time taken: " + duration + " milliseconds");
    }

    private static long countNumbersInRange(long start, long end)
    {
        return end - start + 1;
    }
}
