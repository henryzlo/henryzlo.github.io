// TimePack.java

package edu.umb.cs.timing;

import java.util.*;

/**
 * Timing package for Java: elapsed time and CPU time for process Supports
 * multiple "Clocks" that separately start and stop and accumulate elapsed and
 * CPU time. S09: CPU time support based on new Java capability used
 * 
 * @author Ethan Bolker
 */

public class TimePack {
	private List<Clock> allClocks;
	private Clock timePackClock;
	private Date date;
	private List<String> log;

	public TimePack() {
		allClocks = new ArrayList<Clock>(10);
		log = new LinkedList<String>();
		timePackClock = newClock("log");
		timePackClock.start();
		date = new Date();
	}

	/**
	 * Obtain a new Clock from the time package.
	 * 
	 * @param name
	 *            Name to show in report
	 */
	public Clock newClock(String name) {
		Clock c = new Clock(name);
		allClocks.add(c);
		return c;
	}

	public Clock newClock() {
		Clock c = new Clock();
		allClocks.add(c);
		return c;
	}

	/**
	 * Report on all the Clocks created by this time package.
	 */

	public String report() {
		// bring log time up to date--
		timePackClock.stop();
		timePackClock.start();
		return toString();
	}

	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append(date.toString() + "\n");
		s.append("clock report (times in ms)\n");
		s.append(Clock.getHeader());
		Iterator<Clock> i = allClocks.iterator();
		while (i.hasNext()) {
			s.append(i.next());
			s.append('\n');
		}
		return s.toString();
	}

	/**
	 * 
	 * Get the time this time package has been going
	 */
	public long getLogTime() {
		return timePackClock.getTotalTime();
	}

	public void log(String s) {
		log.add(s);
	}

	// timing package test
	public static void main(String[] args) {
		TimePack t = new TimePack();
		Clock one = t.newClock("one");
		Clock two = t.newClock("two");
		one.start();
		one.start();
		try {
			// use 1 sec time (with negligible CPU time)
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		one.stop();
		one.stop();
		two.start();
		final int MILLION = 1000000;
		for (int i = 0; i < 100 * MILLION; i++)
			// use some CPU time
			;
		two.stop();
		one.start();
		for (int i = 0; i < 50 * MILLION; i++)
			// use some CPU time
			;
		one.stop();
		System.out.println(t.report());
	}

}
