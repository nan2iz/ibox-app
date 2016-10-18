package edu.csupomona.cs585.ibox.sync;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.File;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GoogleDriveFileSyncManagerTest {

	private Drive service;
	private java.io.File localFile;
	private GoogleDriveFileSyncManager googleDriveManager;
	
	
	@Before
	public void setup(){
		service = mock(Drive.class);
		googleDriveManager = new GoogleDriveFileSyncManager(service);
		localFile = mock(java.io.File.class);
		
	}
	
	
	@Test
	public void testAddFile() throws IOException{
		writeLog("Perform AddFile Test");
		
		File file = new File();
		file.setId("testID");
		
		Files files = mock(Files.class);
		Files.Insert insert = mock(Files.Insert.class);
		when(service.files()).thenReturn(files);
		when(files.insert(Mockito.any(File.class), Mockito.any(FileContent.class))).thenReturn(insert);
		when(insert.execute()).thenReturn(file);
		
		googleDriveManager.addFile(localFile);
		
		//File Correntness Check
		assertEquals("test", file.getId());

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
