package org.lucterios.stressTester;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

import org.lucterios.engine.application.ActionImpl;
import org.lucterios.engine.transport.HttpTransportImpl;
import org.lucterios.engine.presentation.Observer;
import org.lucterios.engine.presentation.ObserverFactory;
import org.lucterios.engine.presentation.Singletons;
import org.lucterios.engine.transport.HttpTransport;
import org.lucterios.stressTester.inputs.ActionTest;
import org.lucterios.stressTester.inputs.InputTest;
import org.lucterios.stressTester.observer.ObserverTestAcknowledge;
import org.lucterios.stressTester.observer.ObserverTestAuthentification;
import org.lucterios.stressTester.observer.ObserverTestCustom;
import org.lucterios.stressTester.observer.ObserverTestDialogBox;
import org.lucterios.stressTester.observer.ObserverTestException;
import org.lucterios.stressTester.observer.ObserverTestMenu;
import org.lucterios.stressTester.observer.ObserverTestPrint;
import org.lucterios.stressTester.observer.ObserverTestTemplate;
import org.lucterios.stressTester.outputs.OutputList;
import org.lucterios.stressTester.outputs.OutputSystemout;
import org.lucterios.stressTester.outputs.OutputXMLFile;
import org.lucterios.utils.LucteriosException;

public class Main {

	private InputTest m_test;
	private OutputList m_outlist;
	private HttpTransport m_current_transport;
	private ObserverFactory m_current_factory;
	
	public Main(String xmlInputFileName,String xmloutputFileName) throws LucteriosException{
		File inputFile=new File(xmlInputFileName);
		File outputFile=new File(xmloutputFileName);
		outputFile.delete();
		if (outputFile.exists())
			throw new LucteriosException("output file not removable");
		m_test=new InputTest(inputFile);
		m_outlist=new OutputList();		
		m_outlist.add(new OutputSystemout());
		m_outlist.add(new OutputXMLFile(outputFile,OutputXMLFile.class.getResourceAsStream("default.xsl")));
	}

	private boolean initialize() throws LucteriosException, IOException {
		boolean result;
		Singletons.setActionClass(ActionImpl.class);
		Singletons.setHttpTransportClass(HttpTransportImpl.class);
		Singletons.initalize(new File("."));
		
		m_current_transport = Singletons.Transport();
		m_current_transport.connectToServer(m_test.getServerHost(), m_test.getRootPath(), m_test.getPort(), m_test.getSecurity());
		m_current_factory = Singletons.Factory();
		m_current_factory.AddObserver("CORE.Auth", ObserverTestAuthentification.class);
		m_current_factory.AddObserver("CORE.Menu", ObserverTestMenu.class);
		m_current_factory.AddObserver("Core.Acknowledge", ObserverTestAcknowledge.class);
		m_current_factory.AddObserver("CORE.Exception", ObserverTestException.class);
		m_current_factory.AddObserver("Core.DialogBox", ObserverTestDialogBox.class);
		m_current_factory.AddObserver("Core.Custom", ObserverTestCustom.class);
		m_current_factory.AddObserver("Core.Print", ObserverTestPrint.class);
		m_current_factory.AddObserver("Core.Template", ObserverTestTemplate.class);
		m_current_factory.AddObserver("", ObserverTestAcknowledge.class);			
		m_current_factory.setHttpTransport(m_current_transport);
		result=m_current_factory.setAuthentification(m_test.getUser(), m_test.getPassword());
		if (result)
			System.out.println(String.format("Connected to %s with %s ", m_test.getServer(), m_test.getUser(), m_test.getPassword()));
		else
			System.out.println(String.format("Failure to log in %s with %s/%s ", m_test.getServer(), m_test.getUser(), m_test.getPassword()));
		return result;
	}
	
	private void runStressTest() throws LucteriosException{	
			int nbTest=m_test.getActionTests().size();
			Random rand=new Random(new Date().getTime());
			while (true) {
				ActionTest currentTest=m_test.getActionTests().get(rand.nextInt(nbTest));
				runTest(currentTest);
				try {
					Thread.sleep((rand.nextInt(8)+2)*1000);
				} catch (InterruptedException e) {
					throw new LucteriosException("Thread.sleep error",e);
				}
			}
	}

	private void runCheckTest() throws LucteriosException{	
		int nbTest=m_test.getActionTests().size();
		for(int test_idx=0;test_idx<nbTest;test_idx++) {
			ActionTest currentTest=m_test.getActionTests().get(test_idx);
			runTest(currentTest);
		}
	}

	private void runTest(ActionTest currentTest) throws LucteriosException {
		long begin_time=new Date().getTime();
		try {
			Observer obs=m_current_factory.callAction(currentTest.getExtension(), currentTest.getName(), currentTest.getAttributMap());
			m_outlist.addResult(currentTest.getId(),currentTest.getActionName(),(int)(new Date().getTime()-begin_time), obs.getObserverName()+":"+obs.getContentText(), currentTest.isWaiting(obs));
		}catch(LucteriosException le) {
			m_outlist.addResult(currentTest.getId(),currentTest.getActionName(),(int)(new Date().getTime()-begin_time), le.getClass().getName()+":"+le.getMessage()+"|"+le.mRequest+"|"+le.mReponse, false);
		}catch(Exception ex) {
			m_outlist.addResult(currentTest.getId(),currentTest.getActionName(),(int)(new Date().getTime()-begin_time), ex.getClass().getName()+":"+ex.getMessage(), false);
		}
	}
	
	/**
	 * @param args
	 * @throws LucteriosException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws LucteriosException, IOException {
		boolean running=(args.length==3);
		running=running && ("stress".equalsIgnoreCase(args[2]) || "check".equalsIgnoreCase(args[2]));
		if (running) {
			Main main=new Main(args[0],args[1]);
			if (main.initialize()) {
				if ("stress".equalsIgnoreCase(args[2]))
					main.runStressTest();
				else
					main.runCheckTest();
			}
		} 
		else
			help();
		System.exit(0);
	}

	private static void help() {
		System.out.println("Syntax:java -jar LucteriosStressTester <input xml file> <output xml file> [stress|check]");
	}

}
