package modules;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Class that execute specific "job" at specific time, and run it every "delta"
 * time
 * 
 * @author Or Man
 * @version 1.1
 * @since 21/12/2020
 */
public class PeriodicallyRunner {
	private ScheduledExecutorService scheduler;

	/**
	 * Creates {@link PeriodicallyRunner} that execute specific <b>"job"</b> at
	 * specific <b>time(targetHour,targrtMin)</b>, and run it every <b>"delta"</b>
	 * time
	 * 
	 * @param targetHour starting hour
	 * @param targetMin  starting minutes
	 * @param deltaUnit  {@link TimeUnit} the unit of the delta
	 * @param delta      the time between runs in <b>deltaUnit</b>
	 * @param job        runnable to execute
	 */
	public PeriodicallyRunner(int targetHour, int targetMin, TimeUnit deltaUnit, long delta, Runnable job) {
		LocalDateTime localNow = LocalDateTime.now();
		ZoneId currentZone = ZoneId.systemDefault();
		ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
		ZonedDateTime zonedNextTarget = zonedNow.withHour(targetHour).withMinute(targetMin).withSecond(0);
		if (zonedNow.compareTo(zonedNextTarget) > 0)
			zonedNextTarget = zonedNextTarget.plusDays(1);

		Duration duration = Duration.between(zonedNow, zonedNextTarget);
		long initalDelay = duration.getSeconds();

		scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(job, initalDelay, deltaUnit.toSeconds(delta), TimeUnit.SECONDS);
	}

	/**
	 * Creates {@link PeriodicallyRunner} that execute specific <b>"job"</b> at
	 * specific <b>time(targetHour,targrtMin)</b>, and run it every 1 day
	 * 
	 * @param targetHour starting hour
	 * @param targetMin  starting minutes
	 * @param job        runnable to execute
	 * @return the {@link PeriodicallyRunner}
	 */
	public static PeriodicallyRunner runEveryDayAt(int targetHour, int targetMin, Runnable job) {
		return new PeriodicallyRunner(targetHour, targetMin, TimeUnit.DAYS, 1, job);
	}

	/**
	 * stop the execution of the job, waits to the execution to end, if already
	 * started
	 */
	public void stop() {
		scheduler.shutdown();
		try {
			scheduler.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException ex) {

		}
	}

}
