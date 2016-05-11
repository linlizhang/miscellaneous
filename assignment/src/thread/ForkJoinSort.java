import java.util.concurrent.*;
import java.util.Arrays;

public class ForkJoinSort {

    static class SortTask extends RecursiveAction {
       
        static final int THRESHOLD = 4;
    
        final long[] array; 
        final int lo;
        final int hi;

        public SortTask(long[] array, int lo, int hi) {

            this.array = array;
            this.lo = lo;
            this.hi = hi;
        }

        public SortTask(long[] array) {
            this(array, 0, array.length);
        }

        protected void compute() {
            if (hi - lo < THRESHOLD) {
                sortSequentially(lo, hi);
            } else {
                System.out.println("split the task");
                int mid = (lo + hi) >>> 1;
                invokeAll(new SortTask(array, lo, mid),
                    new SortTask(array, mid , hi));
                merge(lo, mid, hi);
            }

        }
        
        void sortSequentially(int lo, int hi) {
            Arrays.sort(array, lo, hi);
        }

        void merge(int lo, int mid, int hi) {
            System.out.println("merge the result of sub-task");
            long[] buf = Arrays.copyOfRange(array, lo, mid);
            for (int i = 0, j = lo, k = mid; i < buf.length; j++) {
                array[j] = (k == hi || buf[i] < array[k]) ? buf[i++] : array[k++];
            }
        }
    }


    class IncrementTask extends RecursiveAction {

        final long[] array; 

        final int lo, hi;

        static final int THRESHOLD = 4;
    

        IncrementTask(long[] array, int lo, int hi) {
            this.array = array;
            this.lo = lo;
            this.hi =hi;
        }


        protected void compute() {

            if (hi - lo < THRESHOLD) {
                for (int i = lo; i< hi; i++) {
                    array[i]++;
                }
            } else {

                int mid = (lo + hi) >>> 1;
                invokeAll(new IncrementTask(array, lo, mid),
                    new IncrementTask(array, mid, hi));
            }
        }
    }


    public static void main(String[] args) {

        long[] array = {34, 56,34, 12, 56, 77, 8, 98, 1214, 678, 3422, 6768, 234, 34, 689, 1038, 45872};
        System.out.println("the original array: "); 
        for ( int i = 0 ; i < array.length; i++ ) {
            System.out.print(array[i] + " " );
        } 
        
        ForkJoinPool pool = new ForkJoinPool();
        SortTask sort = new SortTask(array);
        pool.invoke(sort);
        for ( int i = 0 ; i < array.length; i++ ) {
            System.out.print(array[i] + " " );
        } 
        
    }
}

