package modules;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

public class WakeableThread extends Thread {

	public static long TIME_ACCURECY = 1000;
	
	private boolean needToWake = false;
	private Object lock = new Object();
	
	public WakeableThread(Runnable arg0) {
		super(arg0);
	}

	public void sleepUntillWoken() {
		needToWake = false;
		synchronized (lock) {
			while(!needToWake) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
				}
			}
		}
		
	}
	
	public boolean sleepUntillWoken(LocalTime endTime) {
		needToWake = false;
		synchronized (lock) {
			while(!needToWake && LocalTime.now().isBefore(endTime)) {
				try {
					lock.wait(TIME_ACCURECY);
				} catch (InterruptedException e) {
				}
			}
		}
		return needToWake;
	}
	
	public boolean sleepUntillWoken(TimeUnit deltaUnit, long delta) {
		needToWake = false;
		LocalTime endTime = LocalTime.now().plusSeconds(deltaUnit.toSeconds(delta));
		synchronized (lock) {
			while(!needToWake && LocalTime.now().isBefore(endTime)) {
				try {
					lock.wait(TIME_ACCURECY);
				} catch (InterruptedException e) {
				}
			}
		}
		return needToWake;
	}
	
	public void wake() {
		needToWake = true;
		lock.notify();
	}
}
