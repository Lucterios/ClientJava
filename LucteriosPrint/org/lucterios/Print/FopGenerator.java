/*
*    This file is part of Lucterios.
*
*    Lucterios is free software; you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation; either version 2 of the License, or
*    (at your option) any later version.
*
*    Lucterios is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with Lucterios; if not, write to the Free Software
*    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*
*	Contributeurs: Fanny ALLEAUME, Pierre-Olivier VERSCHOORE, Laurent GAY
*/

package org.lucterios.Print;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.print.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.fop.render.awt.AWTRenderer;
import org.apache.fop.render.print.PrintRenderer;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.Tools;
import org.lucterios.utils.graphic.JAdvancePanel;

public class FopGenerator
{
	public interface ClosePreview
	{
		void close();
	}	
	class CloseCallBack implements ClosePreview
	{
		JDialog mDlg;
		public CloseCallBack(JDialog aDlg){
			mDlg=aDlg;
		}
		public void close()
		{
			mDlg.dispose();
		}
	};

	protected String mXMLContent=null;
	protected String mFoContent=null;
	protected String mTitle="";
	
	public FopGenerator(String xmlContent,String title,boolean fopFormat) throws LucteriosException
	{
		mXMLContent=xmlContent;
		if (fopFormat)
			mFoContent=xmlContent;
		else
		{
			String xsl_lucterios=Tools.parseISToString(getClass().getResourceAsStream("resources/LucteriosPrintStyleForFo.xsl"));
			mFoContent=ModelConverter.TransformXsl(mXMLContent,xsl_lucterios,true);
		}
		mTitle=title;
	}
	
	protected InputStream getSource() throws IOException, FOPException 
	{		
		if (mFoContent!=null)
        {
			String xml_content=mFoContent.toString();
			if (xml_content.indexOf("encoding")==-1)
				xml_content="<?xml version='1.0' encoding='iso-8859-1'?>\n"+xml_content;
			InputStream fo_stream= new org.lucterios.utils.BufferInputStream(xml_content);
			return fo_stream;
		}
		else
		    return null;
	}	
	
	public JPanel viewFO(ClosePreview aCloseCallBack) throws LucteriosException
	{	
		try 
		{
			 FopFactory fopFactory = FopFactory.newInstance();
			 AWTRenderer renderer = new AWTRenderer();
			 FOUserAgent agent = fopFactory.newFOUserAgent();
			 agent.setRendererOverride(renderer);
			 renderer.setPreviewDialogDisplayed(false);
			 renderer.setUserAgent(agent);
			 PrintPreviewPanel previewPanel = new PrintPreviewPanel(agent,aCloseCallBack);
			 	 
			 Fop fop = fopFactory.newFop(agent);
	         TransformerFactory factory = TransformerFactory.newInstance();
	         Transformer transformer = factory.newTransformer();
	         Source src = new StreamSource(getSource());
	         Result res = new SAXResult(fop.getDefaultHandler());
	         transformer.transform(src, res);
	         previewPanel.setFontImage(SelectPrintDlg.FontImage, JAdvancePanel.TEXTURE);
	         previewPanel.reload();
	         return previewPanel;		
		} 
		catch (java.lang.Exception e) 
		{
			
			throw new LucteriosException("Erreur de previsualisation","",Tools.HTMLEntityEncode(mFoContent),e);
		}
	}
	
	public void saveFilePDF(File aPdfFile,JFrame aOwnerF,JDialog aOwnerD) throws LucteriosException 
	{		
		if (aPdfFile==null)
		{
			aPdfFile=SelectPrintDlg.getSelectedFileName(SelectPrintDlg.getDefaultFileName(mTitle,ExtensionFilter.EXTENSION_EXPORT_PDF),aOwnerF,aOwnerD,ExtensionFilter.EXTENSION_EXPORT_PDF);
			if (aPdfFile==null)
				return;
		}
		try {
			if (aPdfFile.exists())
				aPdfFile.delete();
		    FopFactory fopFactory = FopFactory.newInstance();
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            OutputStream out = new FileOutputStream(aPdfFile);
            Fop fop= fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(); // identity transformer
            Source src = new StreamSource(getSource());
            Result res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(src, res);
			
		} catch (Exception e) {
			throw new LucteriosException("Erreur de sauvegarde","",Tools.HTMLEntityEncode(mFoContent),e);
		}
	}		

	public void saveFileCSV(File aCSVFile,JFrame aOwnerF,JDialog aOwnerD) throws LucteriosException 
	{		
		if (aCSVFile==null)
		{
			aCSVFile=SelectPrintDlg.getSelectedFileName(SelectPrintDlg.getDefaultFileName(mTitle,ExtensionFilter.EXTENSION_EXPORT_CSV),aOwnerF,aOwnerD,ExtensionFilter.EXTENSION_EXPORT_CSV);
			if (aCSVFile==null)
				return;
		}
		try {
			if (aCSVFile.exists())
				aCSVFile.delete();
			String xsl_lucterios=Tools.parseISToString(getClass().getResourceAsStream("resources/ConvertxlpToCSV.xsl"));
			String source_content=Tools.replace(mXMLContent.replace('\t',' '),"<br/>"," ");

			String cvs_content=ModelConverter.TransformXsl(source_content,xsl_lucterios,false);
			
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(aCSVFile),"iso-8859-1"));
			writer.write(cvs_content.replace('\t',' '));
			writer.close();
		} catch (Exception e) {
			throw new LucteriosException("Erreur de sauvegarde","",Tools.HTMLEntityEncode(mFoContent),e);
		}
	}		
	
	public void print() throws LucteriosException 
	{
        	PrinterJob pj = PrinterJob.getPrinterJob();
        	pj.setCopies(1);
            if (pj.printDialog())
            {
              try 
              {
            	FopFactory fopFactory = FopFactory.newInstance();            	
                FOUserAgent userAgent = fopFactory.newFOUserAgent();
                PrintRenderer renderer = new PrintRenderer(pj);
                renderer.setUserAgent(userAgent);
                userAgent.setRendererOverride(renderer);
                Fop fop = fopFactory.newFop(userAgent);
                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer(); // identity transformer
                Source src = new StreamSource(getSource());
                Result res = new SAXResult(fop.getDefaultHandler());
                transformer.transform(src, res);           	
            	
              } 
              catch (java.lang.Exception e) 
              {
        			throw new LucteriosException("Erreur d'impression","",Tools.HTMLEntityEncode(mFoContent),e);
              }
            }
	}

	public void SelectPrintMedia(JFrame aOwnerF,JDialog aOwnerD,int aMode,boolean aWithExportText,File aPDFFile,File aCSVFile) throws LucteriosException
	{
		if (mFoContent!=null)
		{
			switch(aMode)
			{
				case  SelectPrintDlg.MODE_PRINT:
				{
			    	print();
					break;
				}
				case  SelectPrintDlg.MODE_PREVIEW:
				{
					JDialog dlg;
					if (aOwnerD!=null)
						dlg=new JDialog(aOwnerD);
					else if (aOwnerF!=null)
						dlg=new JDialog(aOwnerF);
					else dlg=new JDialog();
					dlg.setTitle(mTitle);
					dlg.getContentPane().add(viewFO(new CloseCallBack(dlg)));
					dlg.pack();
					Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
					dlg.setSize(screen);
					dlg.setVisible(true);
					break;
				}
				case  SelectPrintDlg.MODE_EXPORT_PDF:
				{
		    		saveFilePDF(aPDFFile,aOwnerF,aOwnerD);
					break;
				}
				case  SelectPrintDlg.MODE_EXPORT_CSV:
				{
		    		saveFileCSV(aCSVFile,aOwnerF,aOwnerD);
					break;
				}
				default:
				{
					org.lucterios.Print.SelectPrintDlg print_dlg=null;
					if ((aOwnerD==null) && (aOwnerF==null))
						print_dlg=new org.lucterios.Print.SelectPrintDlg(aWithExportText);
					if (aOwnerD!=null)
						print_dlg=new org.lucterios.Print.SelectPrintDlg(aOwnerD,aWithExportText);
					else if (aOwnerF!=null)
						print_dlg=new org.lucterios.Print.SelectPrintDlg(aOwnerF,aWithExportText);
					print_dlg.setTitle(mTitle);
					int new_mode=print_dlg.getChose();
					if (new_mode!=SelectPrintDlg.MODE_NONE)
						SelectPrintMedia(aOwnerF,aOwnerD,new_mode,aWithExportText,new File(print_dlg.getExportFile(SelectPrintDlg.MODE_EXPORT_PDF)),new File(print_dlg.getExportFile(SelectPrintDlg.MODE_EXPORT_CSV)));
				}
					break;
			}			
		}
	}
}
