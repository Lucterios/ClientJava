package org.lucterios.stressTester;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

import org.lucterios.client.presentation.Observer;
import org.lucterios.client.presentation.ObserverFactory;
import org.lucterios.client.presentation.ObserverFactoryImpl;
import org.lucterios.client.transport.HttpTransport;
import org.lucterios.client.transport.HttpTransportImpl;
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

	/**
	 * @param args
	 * @throws LucteriosException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws LucteriosException, IOException {
		if (args.length==2) {
			File inputFile=new File(args[0]);
			File outputFile=new File(args[1]);
			outputFile.delete();
			if (outputFile.exists())
				throw new LucteriosException("output file not removable");
			InputTest test=new InputTest(inputFile);
			OutputList outlist=new OutputList();
			outlist.add(new OutputSystemout());
			outlist.add(new OutputXMLFile(outputFile,OutputXMLFile.class.getResourceAsStream("default.xsl")));
			runStressTest(test,outlist);
		}
		else
			System.out.println("Syntax:java -jar LucteriosStressTester <input xml file> <output xml file>");
	}
	
	public static void runStressTest(InputTest aTest,OutputList aOutlist) throws LucteriosException{
		HttpTransport current_transport = new HttpTransportImpl();
		current_transport.connectToServer(aTest.getServerHost(), aTest.getRootPath(), aTest.getPort(), aTest.getSecurity());
		ObserverFactory current_factory = new ObserverFactoryImpl();
		current_factory.AddObserver("CORE.Auth", ObserverTestAuthentification.class);
		current_factory.AddObserver("CORE.Menu", ObserverTestMenu.class);
		current_factory.AddObserver("Core.Acknowledge", ObserverTestAcknowledge.class);
		current_factory.AddObserver("CORE.Exception", ObserverTestException.class);
		current_factory.AddObserver("Core.DialogBox", ObserverTestDialogBox.class);
		current_factory.AddObserver("Core.Custom", ObserverTestCustom.class);
		current_factory.AddObserver("Core.Print", ObserverTestPrint.class);
		current_factory.AddObserver("Core.Template", ObserverTestTemplate.class);
		current_factory.AddObserver("", ObserverTestAcknowledge.class);		
		
		current_factory.setHttpTransport(current_transport);
		if (current_factory.setAuthentification(aTest.getUser(), aTest.getPassword())) {
			System.out.println(String.format("Connected to %s with %s ", aTest.getServer(), aTest.getUser(), aTest.getPassword()));
			int nbTest=aTest.getActionTests().size();
			Random rand=new Random(new Date().getTime());
			while (true) {
				long begin_time=new Date().getTime();
				ActionTest currentTest=aTest.getActionTests().get(rand.nextInt(nbTest));
				try {
					Observer obs=current_factory.callAction(currentTest.getExtension(), currentTest.getName(), currentTest.getAttributMap());
					aOutlist.addResult(currentTest.getId(),currentTest.getActionName(),(int)(new Date().getTime()-begin_time), obs.getObserverName()+":"+obs.getContentText());
				}catch(LucteriosException le) {
					aOutlist.addResult(currentTest.getId(),currentTest.getActionName(),(int)(new Date().getTime()-begin_time), le.getClass().getName()+":"+le.getMessage()+"|"+le.mRequest+"|"+le.mReponse);
				}catch(Exception ex) {
					aOutlist.addResult(currentTest.getId(),currentTest.getActionName(),(int)(new Date().getTime()-begin_time), ex.getClass().getName()+":"+ex.getMessage());
				}
				try {
					Thread.sleep((rand.nextInt(8)+2)*1000);
				} catch (InterruptedException e) {
					throw new LucteriosException("Thread.sleep error",e);
				}
			}
		}
		else {
			System.out.println(String.format("Failure to log in %s with %s/%s ", aTest.getServer(), aTest.getUser(), aTest.getPassword()));
		}		
	}

}
