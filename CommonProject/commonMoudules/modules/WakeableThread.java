package modules;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

/**
 * extends the Thread class to create Thread that you can wake.<br>
 * while asleep({@link #sleepUntillWoken()}) can be woken by<br>
 * {@link #wake()}.
 * <p>
 * the accuracy can be set by {@link #TIME_ACCURECY}
 * 
 * @author Or man
 * @since 30/12/2020
 * @version v1.0
 * @see Thread
 */
public class WakeableThread extends Thread {

	/**
	 * while sleeping the Thread check for the waking conditions every TIME_ACCURECY
	 * milliseconds
	 */
	public static long TIME_ACCURECY = 1000;

	private boolean needToWake = false;
	private Object lock = new Object();

	/**
	 * create the Thread from runnable
	 * 
	 * @param arg0 {@link Runnable} to run
	 * @see Thread
	 */
	public WakeableThread(Runnable arg0) {
		super(arg0);
	}

	/**
	 * will sleep until someone call the {@link #wake()} method
	 */
	public void sleepUntillWoken() {
		needToWake = false;
		synchronized (lock) {
			while (!needToWake) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
				}
			}
		}

	}

	/**
	 * sleep until someone call the {@link #wake()} method or the time has passed
	 * 
	 * @param endTime {@link LocalTime} the time to wake up if not woken by
	 *                {@link #wake()}
	 * @return true if woken by the time(endTime passed), or false if woken by the
	 *         {@link #wake()}
	 */
	public boolean sleepUntillWoken(LocalTime endTime) {
		needToWake = false;
		synchronized (lock) {
			while (!needToWake && LocalTime.now().isBefore(endTime)) {
				try {
					lock.wait(TIME_ACCURECY);
				} catch (InterruptedException e) {
				}
			}
		}
		return needToWake;
	}

	/**
	 * sleep until someone call the {@link #wake()} method or the time has passed
	 * 
	 * @param deltaUnit {@link TimeUnit} the time units to wake up if not woken by
	 *                  {@link #wake()}
	 * @param delta     the time to sleep in time units of <b>deltaUnit</b>
	 * @return true if woken by the time(endTime passed), or false if woken by the
	 *         {@link #wake()}
	 */
	public boolean sleepUntillWoken(TimeUnit deltaUnit, long delta) {
		needToWake = false;
		LocalTime endTime = LocalTime.now().plusSeconds(deltaUnit.toSeconds(delta));
		synchronized (lock) {
			while (!needToWake && LocalTime.now().isBefore(endTime)) {
				try {
					lock.wait(TIME_ACCURECY);
				} catch (InterruptedException e) {
				}
			}
		}
		return needToWake;
	}

	/**
	 * wake up the thread if asleep from {@link #sleepUntillWoken()},
	 * {@link #sleepUntillWoken(LocalTime)} or
	 * {@link #sleepUntillWoken(TimeUnit, long)}
	 */
	public void wake() {
		needToWake = true;
		synchronized (lock) {
			lock.notify();
		}
	}
}
