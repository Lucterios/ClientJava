package org.lucterios.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import junit.framework.TestCase;

public class ZipMangerUnit extends TestCase {

	private ZipManager mZipManager;
	private File mZipFile;
	private File mDataFile;
	
	protected void setUp() throws Exception 
	{
		super.setUp();
		mZipFile=new File("subdir/test.zip");
		mDataFile=new File("subdir/test.txt");
		mZipManager=new ZipManager(mZipFile);
		mZipFile.delete();
		mDataFile.delete();
		File dir=new File("subdir");
		dir.delete();
		dir.mkdir();
	}

	protected void tearDown() throws Exception 
	{
		mZipManager=null;
		//mZipFile.delete();
		//mDataFile.delete();
		//File dir=new File("subdir");
		//dir.delete();
		super.tearDown();
	}

	public void testSimple() throws Exception
	{
		String TEXT="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
		BufferedWriter out = new BufferedWriter(new FileWriter(mDataFile));
		for(int i=0;i<100;i++)
			out.write(TEXT+'\n');
		out.flush();
        out.close();		
		mZipManager.compressSimpleFile(mDataFile);
		mDataFile.delete();
		mZipManager.extractFirstFile(mDataFile);
		BufferedReader input =  new BufferedReader(new FileReader(mDataFile));
	    String line = null; 
	    int num=0;
	    while ((line = input.readLine()) != null)
	    	assertEquals("Line #"+num++,TEXT,line);
	    input.close();
	}
	
	public void testComplexe() throws Exception {
		File example_File=new File("examples.txt");
		mZipManager.compressSimpleFile(example_File);
		mDataFile.delete();
		mZipManager.extractFirstFile(mDataFile);
		
		BufferedReader input1 =  new BufferedReader(new FileReader(example_File));
		BufferedReader input2 =  new BufferedReader(new FileReader(mDataFile));
	    String line1; 
	    String line2; 
	    int num=0;
	    while ((line2 = input2.readLine()) != null) {
	    	line1 = input1.readLine(); 
	    	assertEquals("Line #"+num++,line1,line2);
	    }
	    assertEquals("Final",null,input1.readLine());
	    input1.close();
	    input2.close();
	}
	
}
