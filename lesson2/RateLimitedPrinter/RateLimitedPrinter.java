import java.sql.Timestamp;
import java.util.Date;

public class RateLimitedPrinter {
    private final Date date;
    private final int interval;

    public RateLimitedPrinter(int interval) {
        this.interval = interval;
        this.date = new Date(0);
    }

    public void print(String message) {
        if (date.getTime() + interval < System.currentTimeMillis()) {
            System.out.print(message);
            date.setTime(System.currentTimeMillis());
        }
    }
}
