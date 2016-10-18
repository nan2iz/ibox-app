package edu.csupomona.cs585.ibox.sync;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GoogleDriveFileSyncManagerTest {

	private Drive service;
	
	@Rule 
	public TemporaryFolder tempFolder = new TemporaryFolder();
	
	@Before
	public void setup(){
		service = mock(Drive.class);
	}
	
	
	@Test
	public void testAddFile() throws IOException{
		writeLog("Perform AddFile Test");
		java.io.File dummyFile = tempFolder.newFile();
		//java.io.File localFile = new DummyMember();
		
		GoogleDriveFileSyncManager googleDriveManager = new GoogleDriveFileSyncManager(service);
		googleDriveManager.addFile(dummyFile);
		

	}
	
	@Test
	public void testUpdateFile(){
		writeLog("Perform UpdateFile Test");
	}
	
	@Test
	public void testDeleteFile(){
		writeLog("Perform DeleteFile Test");
		
	}
	
	
	private void writeLog(String msg){
		String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
		System.out.println(timeStamp + " --> " +msg);
		
	}
	
	

}
