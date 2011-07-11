package org.lucterios.stressTester.outputs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.lucterios.utils.LucteriosException;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class OutputXMLFile implements OutputModel {
	
	private File xmlFile;

	private Document xmldoc;
	private Element root;
	private Transformer transformer=null;
	private File htmlFile=null;
	
	public OutputXMLFile(File aXmlFile,InputStream stream) throws  LucteriosException{
		xmlFile=aXmlFile;
		xmldoc= new DocumentImpl();
		root = xmldoc.createElement("test_result");
		xmldoc.appendChild(root);

		if (stream!=null) {
		    try {
		    	htmlFile =new File(aXmlFile.getAbsolutePath().replace("\\.xml", "")+".html");
		    	TransformerFactory tFactory = TransformerFactory.newInstance();
				transformer =  tFactory.newTransformer(new StreamSource(stream));
			} catch (TransformerConfigurationException e) {
				throw new LucteriosException("XSL error",e);
			}
		}
		save();
	}
	
	private void save()  throws LucteriosException{
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(xmlFile);

			OutputFormat of = new OutputFormat("XML","ISO-8859-1",true);
			of.setIndent(1);
			of.setIndenting(true);
			XMLSerializer serializer = new XMLSerializer(fos,of);
	
			serializer.asDOMSerializer();
			serializer.serialize( xmldoc.getDocumentElement() );
			fos.close();
			if ((transformer!=null) && (htmlFile!=null)) {
			    transformer.transform(new StreamSource(xmlFile), new StreamResult(htmlFile));
			}
		} catch (IOException e) {
			throw new LucteriosException("XML save error",e);
		} catch (TransformerException e) {
			throw new LucteriosException("HTML save error",e);
		}
	}
	

	public void addResult(int aId, String aActionName, int aTime, String aObservation, boolean success) throws LucteriosException {
			Element test = xmldoc.createElementNS(null, "test");
			test.setAttributeNS(null, "id",""+aId);
			test.setAttributeNS(null, "time",""+aTime);
			test.setAttributeNS(null, "success",success?"1":"0");
			Text test_name = xmldoc.createTextNode(aActionName);
			test.appendChild(test_name);
			Element obs = xmldoc.createElementNS(null, "observation");
			CDATASection data= xmldoc.createCDATASection(aObservation);
			obs.appendChild(data);
			test.appendChild(obs);
			root.appendChild(test);
			save();
	}

}
