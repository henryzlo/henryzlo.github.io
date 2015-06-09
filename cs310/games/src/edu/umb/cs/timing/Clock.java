// Clock.java
// for SystemInformation--

package edu.umb.cs.timing;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * A Clock is an independent timer. It can be turned on and off, and in the
 * intervals it is on, it adds up its elapsed and CPU time
 * 
 * @author Ethan Bolker
 * @since December 1995 
 * CPU time via new ThreadMXBean capability: 
 * Betty O'Neil Jan '09
 * Note: CPU time is only accurate to about 16 ms.
 * CPU time for all live threads is available
 * from ThreadMXBean, but not the CPU for now-dead threads, 
 * so it is not possible through ThreadMXBean to calculate 
 * the process CPU time in general. This package detects 
 * when a different thread calls stop than called start and 
 * sets multithreadingDetected in that case. It gets CPU
 * time for all live threads at start() so that it can
 * look up the start time for the thread that calls stop()
 * even if it is different from the one that called start().
 * The reported CPU time is the CPU time (user + system) since 
 * start was called, in the thread that called stop.
 */

public class Clock {
	private int running;
	private long totalTime; // elapsed time, in ms
	private long startTime;
	private long totalCPUTime; // thread CPU time, in ms
	private boolean CPUTimeEnabled = true; // until see multithreading
	private long startThreadID;
	private boolean multithreadingDetected = false;
	private int callCount;
	private String name;
	private ThreadMXBean tb; // for obtaining CPU time
	// for saving start-cpu for each thread alive in this process--
	private Map<Long, Long> threadStartCPUTimes = new HashMap<Long, Long>();
	

	public Clock(String name) {
		this.name = name;
		running = 0;
		tb = ManagementFactory.getThreadMXBean();
	}

	public Clock(int number) {
		this("" + number);
	}

	public Clock() {
		this("");
	}

	public void start() {
		callCount++;
		if (running == 0) {
			startTime = System.currentTimeMillis();
			startThreadID = Thread.currentThread().getId();
			long[] threadIDs = tb.getAllThreadIds();
			// save all the thread CPU times, since the caller
			// of start may not be the in the same thread as
			// the caller of stop
			for (int i=0; i< threadIDs.length; i++) {
				threadStartCPUTimes.put(threadIDs[i], 
						tb.getThreadCpuTime(threadIDs[i]));
			}
		}
		running++;
	}

	public void stop() {
		if (running == 0) {
			// not considered an error: OK to call stop() just to make sure its stopped
			return;
		}
		running--;
		if (running == 0) {
			// System.out.println("timer stop: totalCPUTime = " + totalCPUTime);
			totalTime += System.currentTimeMillis() - startTime;
			
			// the thread that called stop is the one that
			// we report the CPU time for
			long thisThread = Thread.currentThread().getId();
			Long startCPUTime = threadStartCPUTimes.get(thisThread);
			if (startCPUTime == null)
				startCPUTime = 0L; // thread started since start() call
			// convert from nanosecs to millisecs
			totalCPUTime += (tb.getCurrentThreadCpuTime() - startCPUTime) / 1000000;
			
			// if current thread id not the same as in start,
			// multithreading must be in use
			if (thisThread != startThreadID) {
				multithreadingDetected = true;
			}
		}
	}

	public void reset() {
		totalTime = 0;
		totalCPUTime = 0;
		multithreadingDetected = false;
	}

	public boolean isRunning() {
		return running > 0 ? true : false;
	}
	
	public boolean multipleThreadsDetected() {
		return multithreadingDetected;
	}

	/**
	 * Return total elapsed time
	 */
	public long getTotalTime() {
		return totalTime;
	}

	/**
	 * Return total CPU time, sum of CPU times in all
	 * start-stop intervals, where in each interval the 
	 * contribution is the CPU time of the thread that called stop.
	 */
	public long getTotalCPUTime() {
		if (CPUTimeEnabled) {
			return totalCPUTime;
		} else
			return 0;
	}

	/**
	 * How many times has this Clock been started?
	 * 
	 * @return the call count.
	 */
	public int getCallCount() {
		return callCount;
	}

	/**
	 * Return header line for report
	 */
	public static String getHeader() {
		StringBuffer s = new StringBuffer();
		s.append(fixedLenRep("name"));
		s.append(fixedLenRep("totalTime"));
		s.append(fixedLenRep("totalCPU"));
		s.append(fixedLenRep("callCount"));
		s.append(fixedLenRep("running"));
		return "" + s + "\n";
	}

	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append(fixedLenRep(name));
		if (callCount > 0) {
			s.append(fixedLenRep("" + totalTime));
			s.append(fixedLenRep("" + totalCPUTime));
			s.append(fixedLenRep("" + callCount));
			s.append(fixedLenRep("" + running));
		}
		return s.toString();
	}

	// helper function for neat columns of output
	// Use 10 columns, right-justified, blank-filled, at least one blank
	// If x doesn't fit, print blank plus 9 stars
	//
	// Should look into DecimalFormat- like printf

	static private String fixedLenRep(String s) {
		final int nColumns = 10;
		String blanks = "            "; // nColumns or more
		String stars = " ***********"; // ...
		int nBlanks = nColumns - s.length();
		return nBlanks > 0 ? blanks.substring(0, nBlanks) + s : stars
				.substring(0, nColumns);
	}
}
