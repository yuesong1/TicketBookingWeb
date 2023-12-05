
import java.util.concurrent.CountDownLatch;
import java.util.HashMap;

import com.unimelb.swen90007.reactexampleapi.api.domain.BookingLogic;
import com.unimelb.swen90007.reactexampleapi.api.objects.Exceptions.OptimisticLockingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class BookingThread extends Thread {

    private CountDownLatch startLatch;
    private String eventID;
    private String sectionID;
    private int version;

    public BookingThread(String eventID, String sectionID, int version, CountDownLatch startLatch) {
        this.eventID = eventID;
        this.sectionID = sectionID;
        this.version = version;
        this.startLatch = startLatch;
    }

    @Override
    public void run() {
        try {
            startLatch.await();

            // Mock an HttpServletRequest with required parameters
            MockHttpServletRequest mockRequest = new MockHttpServletRequest();

            // Set body content (JSON format)
            String requestBody = String.format(
                    "{\"eventid\": \"%s\", \"sectionid\": \"%s\", \"customeremail\": \"example@email.com\", \"numtickets\": \"1\", \"version\": \"%d\"}",
                    eventID, sectionID, version
            );
            mockRequest.setBody(requestBody);

            // Call your booking logic
            try {
                // Assuming BookingLogic class has the createBookings method
                BookingLogic bookingLogic = new BookingLogic();
                bookingLogic.createBookings(mockRequest);
            } catch (OptimisticLockingException e) {
                System.err.println("Optimistic locking conflict encountered.");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
