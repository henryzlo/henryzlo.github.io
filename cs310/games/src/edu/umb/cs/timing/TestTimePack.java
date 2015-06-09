package edu.umb.cs.timing;
// TestPack.java

/**
 *  Test Timing package for Java
 */

public class TestTimePack
{
    // timing package test
    public static void main( String[] args ) 
    {
	TimePack t = new TimePack();
	Clock one = t.newClock("one");
	Clock two = t.newClock("two");
	one.start(); 
	try {
	    // use 1 sec time (with negligable CPU time)
	    Thread.sleep(1000);  
	}
	catch( InterruptedException e ) {
	}
	one.stop();
	one.start();
	two.start();
	final int MILLION = 1000000;
	for (int i=0;i<410*MILLION; i++) // use some CPU time
	    ;
	two.stop();
	one.stop();
	System.out.println(t.report());
	double cpuPerCall = 
	    (double)one.getTotalCPUTime()/(double)one.getCallCount();
	System.out.println("clock one CPU time per call: " +
			   cpuPerCall);
    }
}
