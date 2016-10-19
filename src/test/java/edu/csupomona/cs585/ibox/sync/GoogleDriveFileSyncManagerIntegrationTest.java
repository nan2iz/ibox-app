package edu.csupomona.cs585.ibox.sync;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.api.services.drive.model.File;

import edu.csupomona.cs585.ibox.WatchDir;

public class GoogleDriveFileSyncManagerIntegrationTest {
	
	private static GoogleDriveFileSyncManager googleDrv;
	private static java.io.File localFile;
	private File file;
	
	private final String testFilePath = "C:\\Nanwarin\test.txt";
	private final static String testPath = "C:\\Nanwarin";

	
	@BeforeClass 
	public static void appClassExecution() throws IOException{
		
		writeLog("Start integration test .. ");
		
		googleDrv = new GoogleDriveFileSyncManager(GoogleDriveServiceProvider.get().getGoogleDriveClient());
		
		Path dir = Paths.get(testPath);
		new WatchDir(dir, googleDrv).processEvents();
		
	}
	
	@AfterClass
	public static void cleanup(){
		try {
			googleDrv.deleteFile(localFile);
			localFile.delete();
		} catch (IOException e) {
			e.printStackTrace();
			writeLog("Could not cleanup: " + e);
		}
	}
	

	@Test
	public void testAddFile(){
		file = new File();
		
		
		
	}
	
	private static void writeLog(String msg){
		
		String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
		System.out.println(timeStamp + " --> " + msg);
		
	}


}
