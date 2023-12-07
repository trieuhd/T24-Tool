package xhtd_ab.start;

import java.util.Calendar;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import xhtd_ab.service.ServiceSchedule;

public class Scheduler {
	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
//        calendar.set(
//           Calendar.DAY_OF_WEEK,
//           Calendar.MONDAY
//        );
        calendar.set(Calendar.HOUR_OF_DAY, 07);
        calendar.set(Calendar.MINUTE, 50);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);



        Timer time = new Timer();

        // Start running the task at 00:00:00, period is set to 2 hours
        time.schedule(new ServiceSchedule(), calendar.getTime(), TimeUnit.HOURS.toMillis(24));
	}
}
