package edu.csupomona.cs585.ibox.sync;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.Drive.Files.List;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GoogleDriveFileSyncManagerTest {

	private Drive mockService;
	private java.io.File dummyLocalFile;
	private GoogleDriveFileSyncManager mockGoogleDrv;
	private File mockReturnFile;
	private Files mockFiles;
	private FileList googleFileList;
	private List mockRequestList;
	
	private final String testID = "ID:TEST0001";
	private final String fileName = "dummyFile.txt";
	private final String testGoogleID = "id1";
	
	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();
	
	@Before
	public void setup() throws IOException{
		
		mockService = mock(Drive.class);
		mockGoogleDrv = new GoogleDriveFileSyncManager(mockService);
		dummyLocalFile = tempFolder.newFile(fileName);
		mockReturnFile = new File();
		mockReturnFile.setId(testID);
		mockFiles = mock(Files.class);
		mockRequestList = mock(List.class);
		
		ArrayList<File> googleFile = new ArrayList<File>();
		googleFile.add((new File()).setTitle(dummyLocalFile.getName()).setId(testGoogleID));
		googleFileList = new FileList();
		googleFileList.setItems(googleFile);
		
	}
	
	
	@Test
	public void testAddFile() throws IOException{
		
		writeLog("Perform AddFile Test");

		Files.Insert mockInsert = mock(Files.Insert.class);
		
		when(mockService.files()).thenReturn(mockFiles);
		when(mockFiles.insert(any(File.class), any(FileContent.class))).thenReturn(mockInsert);
		when(mockInsert.execute()).thenReturn(mockReturnFile);
		
		mockGoogleDrv.addFile(dummyLocalFile);

		assertEquals(testID, mockReturnFile.getId());
		verify(mockInsert).execute();

	}
	
	@Test
	public void testUpdateFile() throws IOException{
		
		writeLog("Perform UpdateFile Test");
		
		Files.Update mockUpdate = mock(Files.Update.class);
		
		getFileId();
		
		when(mockService.files()).thenReturn(mockFiles);
		when(mockFiles.update(any(String.class), any(File.class), any(FileContent.class))).thenReturn(mockUpdate);
		when(mockUpdate.execute()).thenReturn(mockReturnFile);
		
		mockGoogleDrv.updateFile(dummyLocalFile);
		
		verify(mockUpdate).execute();
	}
	
	@Test
	public void testDeleteFile() throws IOException{
		
		writeLog("Perform DeleteFile Test");

		Files.Delete delete = mock(Files.Delete.class);
		
		getFileId();
		
		when(mockService.files()).thenReturn(mockFiles);
		when(mockFiles.delete(any(String.class))).thenReturn(delete);
		when(delete.execute()).thenReturn(null);
		
		mockGoogleDrv.deleteFile(dummyLocalFile);
		
		verify(delete).execute();
	}
	
	public void getFileId() throws IOException{
		
		when(mockService.files()).thenReturn(mockFiles);
		when(mockFiles.list()).thenReturn(mockRequestList);
		when(mockRequestList.execute()).thenReturn(googleFileList);
		
	}
	
	private void writeLog(String msg){
		
		String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
		System.out.println(timeStamp + " --> " + msg);
		
	}


}
