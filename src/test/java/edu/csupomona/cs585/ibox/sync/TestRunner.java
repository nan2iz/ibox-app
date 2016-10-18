package edu.csupomona.cs585.ibox.sync;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {

	public static void main(String[] args){
		Result result = JUnitCore.runClasses(GoogleDriveFileSyncManagerTest.class);
		
		for(Failure failure : result.getFailures()){
			System.out.println(failure.toString());
		}
		
		System.out.println("Test Result: " + result.wasSuccessful());
		
	}
	
}
