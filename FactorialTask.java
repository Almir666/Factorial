import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class FactorialTask extends RecursiveTask<Long> {
    private final int start;
    private final int end;
    private static final int TRHRESHOLD = 10; 

    public FactorialTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        if((end - start) <= TRHRESHOLD) {
            return calculateFactorial(start, end);
        } else {
            int mid = (start + end) / 2;
            FactorialTask task1 = new FactorialTask(start, mid);
            FactorialTask task2 = new FactorialTask(mid + 1, end);

            task1.fork();
            task2.fork();

            long fact1 = task1.join();
            long fact2 = task2.join();

            return fact1 * fact2;
        }
    }

    public Long calculateFactorial(int start, int end) {
        long result = 1; 
        for (int i = start; i <= end; i++) {
            result *= i;
        }
        return result;
    }



    public static void main(String[] args) {
        int n = 10; 
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        FactorialTask factorialTask = new FactorialTask(1, n);

        long result = forkJoinPool.invoke(factorialTask);
        forkJoinPool.close();

        System.out.println("Факториал " + n + "! = " + result);
    }
}
