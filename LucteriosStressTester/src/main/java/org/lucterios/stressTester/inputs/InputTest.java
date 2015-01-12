package org.lucterios.stressTester.inputs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;
import org.lucterios.utils.Tools;

public class InputTest {
	
	private File inputFile;

	private URL server;
	private String user;
	private String password;
	
	private ArrayList<ActionTest> actionTests=new ArrayList<ActionTest>();
	
	public InputTest(File aInputFile) throws LucteriosException {
		inputFile=aInputFile;
		read();
	}

	public String getUser(){
		return user;
	}
	public String getPassword(){
		return password;
	}

	public ArrayList<ActionTest> getActionTests(){
		return actionTests;
	}
	
	private void read() throws LucteriosException {
		SimpleParsing parse=new SimpleParsing();
		try {
			parse.parse(Tools.parseISToString(new FileInputStream(inputFile)));
		} catch (FileNotFoundException e) {
			throw new LucteriosException("Input file not found");
		}
		try {
			server=new URL(parse.getCDataOfFirstTag("server"));
		} catch (MalformedURLException e) {
			throw new LucteriosException("server url mal formed!");
		}
		user=parse.getCDataOfFirstTag("user");
		password=parse.getCDataOfFirstTag("password");
		SimpleParsing[] actions=parse.getSubTag("action");
		for(SimpleParsing action:actions) {
			actionTests.add(new ActionTest(action));
		}		
	}

	public String getServer() {
		return server.toString();
	}
	
	public String getServerHost() {
		return server.getHost();
	}
	
	public int getPort() {
		return server.getPort();
	}

	public String getRootPath() {
		return server.getPath();
	}

	public boolean getSecurity() {
		return server.getProtocol().equals("https");
	}
	

}
