public class ThreadWithFinal {

    private String s;

    public ThreadWithFinal() {

        Thread thread_1 = new Thread(new Thread1());
        Thread thread_2 = new Thread(new Thread2());
        thread_1.start();
        thread_2.start();

    }

    public static void main(String[] args) {
        new ThreadWithFinal();
    }

    
    class Thread1 implements Runnable{
           
        public void run() {
            s = "/tmp/usr".substring(4);
            System.out.println("thread 1 " + s);
        } 
    }

    class Thread2 implements Runnable {
        
        public void run() {
            String my = s;
            System.out.println("thread 2 " + my);
            if ("/tmp".equals(my)) System.out.println(my);
            
        }
    }
}

