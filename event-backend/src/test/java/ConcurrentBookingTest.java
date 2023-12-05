import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ConcurrentBookingTest {

    public static void main(String[] args) {

        int numThreads = 10;
        CountDownLatch startLatch = new CountDownLatch(1);
        String eventID = "0c8d2d57-47f0-49e8-8b55-edd44521ad05"; // example UUID for the event
        String sectionID = "bdb888ed-355b-4697-b9ca-b383e2190f30"; // example UUID for the section
        int version = 28; // Starting version
        List<Thread> threads = new ArrayList<>();

        System.out.println("Starting threads");
        for (int i = 0; i < numThreads; i++) {
            Thread t = new BookingThread(eventID, sectionID, version, startLatch);
            threads.add(t);
            t.start();
        }

        startLatch.countDown();

        try {
            for (Thread t: threads) {
                t.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Threads finished");
    }
}
