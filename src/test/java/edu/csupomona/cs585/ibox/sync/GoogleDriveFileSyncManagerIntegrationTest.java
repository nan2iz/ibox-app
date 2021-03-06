package edu.csupomona.cs585.ibox.sync;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.Before;

import com.google.api.services.drive.Drive.Files.List;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;


public class GoogleDriveFileSyncManagerIntegrationTest {
	
	private static GoogleDriveFileSyncManager googleDrv;
	private static java.io.File localFile;
	private static java.io.File dummyFile;
	
	
	//private final static String testPath = "C:/Nanwarin"; //For PC
	private final static String testPath = "/Users/nanwarinchantarutai/Documents/TestFolder"; //for Mac
	private final static String testFile = "test.txt";

	@Rule
	public final ExpectedException exception = ExpectedException.none();
	
	@BeforeClass 
	public static void initial() throws IOException{
		
		writeLog("Start integration test .. ");
		
		googleDrv = new GoogleDriveFileSyncManager(GoogleDriveServiceProvider.get().getGoogleDriveClient());

	}
	
	@AfterClass
	public static void lastCleanup(){
		
		try {
			if(isFileExistOnGoogle(testFile))
					googleDrv.deleteFile(localFile);
		} catch (IOException e) {
			e.printStackTrace();
			writeLog("Failed to clean up");
		}
			
	}
	
	@Before
	public void setup() throws IOException{
		
		localFile = new java.io.File(testPath, testFile);
		createFileLocalFile();
	}
	
	@After
	public void cleanup(){
		localFile.delete();
	}

	@Test
	public void testAddFile() throws IOException{
		
		writeLog("Perform AddFile integration test");

		googleDrv.addFile(localFile);

		assertTrue(isFileExistOnGoogle(localFile.getName()));
	}
	

	
	@Test
	public void testUpdateFile() throws IOException{
		
		writeLog("Perform updateFile integration test");

		modifyFile();
		googleDrv.updateFile(localFile);
		
		assertEquals(localFile.length(), checkFileSize(localFile.getName()));
		
	}
	
	@Test
	public void testUpdateFileWhenFileIsNotExistOnGoogle() throws IOException{
		
		writeLog("Perform updateFile when file is not exist on google");
		
		createNewFile();
		googleDrv.updateFile(localFile);
		
		assertTrue(isFileExistOnGoogle(localFile.getName()));
		
		dummyFile.delete();
	}
	
	@Test
	public void testDeleteFile() throws IOException, InterruptedException{
		
		writeLog("Perform deleteFile integration test");
		
		googleDrv.deleteFile(localFile);
		
		assertFalse(isFileExistOnGoogle(localFile.getName()));
		
	}
	
	@Test
	public void testDeleteFileWhenFileIsNotExist() throws IOException{
		
		writeLog("Perform deleteFile when file is not exist on Google Drive");
		
		createNewFile();
		exception.expect(FileNotFoundException.class);
		googleDrv.deleteFile(dummyFile);
		
		dummyFile.delete();
	}
	
	private static boolean isFileExistOnGoogle(String fileName) throws IOException{

		List request = googleDrv.service.files().list(); 
		FileList googleFileList = request.execute();
		for(File file: googleFileList.getItems()){
			if(file.getTitle().equals(fileName))
				{
					System.out.print("File tile: " + file.getTitle());
					System.out.print(" File name: " + fileName);
					System.out.print(" File ID" + file.getId());
					return true;
				}
		}
		
		return false;
	}
	
	private static long checkFileSize(String fileName) throws IOException{

		List request = googleDrv.service.files().list(); 
		FileList googleFileList = request.execute();
		for(File file: googleFileList.getItems()){
			if(file.getTitle().equals(fileName))
				return file.getFileSize();
		}
		
		return -1;
	}
	
	public static void createFileLocalFile() throws IOException{
		if(localFile.createNewFile())
			writeLog("Created localFile: " + localFile.getName() + " located at " + localFile.getPath());
		else
			writeLog("Could not create localFile");
	}
	
	public static void createNewFile() throws IOException{
		
		dummyFile = new java.io.File(testPath, "dummyTest.txt");
		
		if(dummyFile.createNewFile())
			writeLog("Created localFile: " + localFile.getName() + " located at " + localFile.getPath());
		else
			writeLog("Could not create localFile");

	}
	
	public static void modifyFile(){

		try {
			BufferedWriter bWriter = new BufferedWriter(new FileWriter(localFile));
			
			PrintWriter pWriter = new PrintWriter(bWriter);
			
			pWriter.println("Modify localFile");
			pWriter.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
			
			bWriter.close();
			pWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		writeLog("Modified file size: " + localFile.length());
	}
	
	private static void writeLog(String msg){
		
		String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
		System.out.println(timeStamp + " --> " + msg);
		
	}


}
