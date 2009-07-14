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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.lucterios.utils.EncodeBase64FromInputStream;
import org.lucterios.utils.LucteriosException;

public class ModelConverter 
{
	private String m_Model;
	private String m_Url;

	public ModelConverter(String aModel,String aUrl) 
	{
		super();
		m_Model=aModel;
		m_Url=aUrl;
	}
	
	public boolean Run()
	{
		int pos_body=m_Model.indexOf("<body");
		if (pos_body>=0)
			m_Model=m_Model.substring(0,pos_body)+"\n<xsl:template name='body'>\n"+m_Model.substring(pos_body);

		InsertInTag("header","<xsl:template name=\"header\">\n","\n</xsl:template>\n");
		InsertInTag("bottom","<xsl:template name=\"bottom\">\n","\n</xsl:template>\n");
		InsertInTag("left","<xsl:template name=\"left\">\n","\n</xsl:template>\n");
		InsertInTag("rigth","<xsl:template name=\"rigth\">\n","\n</xsl:template>\n");
		InsertInTagWithDataLoop("body","<xsl:call-template name='header'/>\n<xsl:call-template name='bottom'/>\n<xsl:call-template name='left'/>\n<xsl:call-template name='rigth'/>\n","<page>\n","\n</page>");
		InsertInTagWithDataLoop("columns","","","");
		InsertInTagWithDataLoop("rows","","","");
		InsertInTagWithDataLoop("cell","","","");
		ChangeSelect();
		ConvertImage();

		int pos_model=m_Model.indexOf("<model");
		int pos_model_sep=-1;
		if (pos_model>=0)
			pos_model_sep=m_Model.indexOf(">",pos_model);
		if ((pos_model>=0) && (pos_model_sep>=0))
		{
			String header;
			header="<?xml version='1.0' encoding='ISO-8859-1' ?>\n";
			header+="<xsl:stylesheet version='1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>\n";
			header+="\n<xsl:template match='/'>\n";
			header+= m_Model.substring(pos_model,pos_model_sep+1)+"\n";
			header+="<xsl:call-template name='body'/>\n";
			header+="</model>\n";
			header+="</xsl:template>\n";
			m_Model=m_Model.substring(pos_model_sep+1);
			m_Model=replace(m_Model,"</model>","</xsl:template>\n\n</xsl:stylesheet>");
			m_Model=header+m_Model;
			m_Model=replace(m_Model,"select=\"\"","select='.'");
			m_Model=replace(m_Model,"select=''","select='.'");
		}
		return true;
	}

	static public String TransformXsl(String aXmldata,String aXsldata,boolean addProperties) throws LucteriosException
	{
		try {
			StringWriter str_wrt=new StringWriter();

			TransformerFactory factory = TransformerFactory.newInstance();
		    Transformer analyseur = factory.newTransformer();

		    DOMResult xml = new DOMResult();
		    analyseur.transform(new StreamSource(new StringReader(aXmldata)), xml);

		    Transformer xslt = factory.newTransformer(new StreamSource(new StringReader(aXsldata)));
		    Result out;
		    if (addProperties) {
		    	out = new DOMResult();
		    }
		    else {
		    	out=new StreamResult(str_wrt);
		    	xslt.setOutputProperty(OutputKeys.METHOD, "text");
		    }
		    xslt.transform(new DOMSource(xml.getNode()), out);

		    if (addProperties){
			    analyseur.setOutputProperty(OutputKeys.INDENT, "yes");
			    analyseur.setOutputProperty(OutputKeys.ENCODING, "iso-8859-1");
			    analyseur.transform(new DOMSource(((DOMResult)out).getNode()),new StreamResult(str_wrt));
		    }
			return str_wrt.getBuffer().toString();
		} catch (TransformerConfigurationException e) {
			throw new LucteriosException("Erreur de transformation",e);
		} catch (TransformerException e) {
			throw new LucteriosException("Erreur de transformation",e);
		}
	}

	public String toXap(String aXmldata,String aXmlresultfile) throws LucteriosException
	{
		String Xap=TransformXsl(aXmldata,m_Model,true);
		Xap=convertLuctoriosFormatToFop(Xap);
		if (!aXmlresultfile.equals( "" ))
			Save(aXmlresultfile,Xap);
		return Xap;
	}
	
    static public String replace(String aValue,String aTarget,String aRemplacement)
    {
        String result=aValue;
        int len=aTarget.length();
        int pos;
        while((pos=result.indexOf(aTarget))!=-1)
        {
            result=result.substring(0, pos)+aRemplacement+result.substring(pos+len);
        }
        return result;
    }	

	public String convertLuctoriosFormatToFop(String aXml_text)
	{
		String xml_text=aXml_text;
 		xml_text=replace(xml_text,"<b>","<font Font-weight=\"bold\">");
 		xml_text=replace(xml_text,"<i>","<font Font-style=\"italic\">");
 		xml_text=replace(xml_text,"<u>","<font text-decoration=\"underline\">");
 		xml_text=replace(xml_text,"</b>","</font>");
 		xml_text=replace(xml_text,"</i>","</font>");
 		xml_text=replace(xml_text,"</u>","</font>");
 		xml_text=replace(xml_text,"{[newline]}","<br/>");
 		xml_text=replace(xml_text,"{[bold]}","<font Font-weight=\"bold\">");
 		xml_text=replace(xml_text,"{[/bold]}","</font>");
 		xml_text=replace(xml_text,"{[italic]}","<font Font-style=\"italic\">");
 		xml_text=replace(xml_text,"{[/italic]}","</font>");
 		xml_text=replace(xml_text,"{[underline]}","<font text-decoration=\"underline\">");
 		xml_text=replace(xml_text,"{[/underline]}","</font>");
 		xml_text=replace(xml_text,"{[","<");              
 		xml_text=replace(xml_text,"]}",">");              
        return xml_text;
      }

	public void Save(String aModelFile,String aContent)
	{
		if (aContent==null)
			aContent=m_Model;
		/*$wh = fopen($modelFile, 'w');
		if ($wh!==false)
		{
			foreach(split("\n",$content) as $line)
				fwrite($wh, $line."\n");
		}
		fclose($wh);*/
   	}
	
	private int[] GetTagPosition(String aTagName,int aStart)
	{
		return GetItemPosition("<"+aTagName,"</"+aTagName+">","/>",aStart);
	}

	private void InsertInTag(String aTagName,String aAddBegin,String aEndBegin)
	{
		int start=0;
		int len=0;
		while (len!=-1)
		{
			int[] tag_pos=GetTagPosition(aTagName,start);
			int pos=tag_pos[0];
			len=tag_pos[1];
			if (len!=-1)
			{
				String new_text=aAddBegin+m_Model.substring(pos,pos+len)+aEndBegin;
				m_Model=m_Model.substring(0,pos)+new_text+m_Model.substring(pos+len);
				start=pos+new_text.length();
			}
		}
	}

	private int[] GetItemPosition(String aItembegin,String aItemend,String aEnding,int aStart)
	{
		int posIn=m_Model.indexOf(aItembegin,aStart);
		int posOut=m_Model.indexOf(aItemend,aStart);
		if (posOut>=0) 
			posOut+=aItemend.length()-1;
		if ((posIn>=0) && (posOut<0) && (aEnding!=null))
		{
			int end=m_Model.indexOf(aEnding.charAt(1),posIn);
			if ((end>1) && (m_Model.charAt(end-1)==aEnding.charAt(0)))
				posOut=end;
		}
		int[] res={-1,-1};
		if ((posIn>=0) && (posOut>=0) && (posOut>=posIn))
		{
			res[0]=posIn;
			res[1]=posOut-posIn+1;
		}
		return res;
	}

	private void ConvertImage()
	{
		int start=0;
		int len=0;
		while (len!=-1)
		{
			int[] tag_pos=GetTagPosition("image",start);
			int pos=tag_pos[0];
			len=tag_pos[1];
			if (len!=-1)
			{
				int pos_begin=m_Model.indexOf(">",pos);
				int pos_end=m_Model.indexOf("<",pos_begin);
				if ((pos_begin>=0) && (pos_begin<(pos+len-1)) && (pos_end>=0) && (pos_end<(pos+len-1)))
				{
					pos_begin++;
					String file_name=m_Url+m_Model.substring(pos_begin,pos_end).trim();
					String base64_mime_image;
					try {
						EncodeBase64FromInputStream encoder = new EncodeBase64FromInputStream(new java.io.FileInputStream(new File(file_name)));
						base64_mime_image="data:image/*;base64,"+encoder.encodeString();				
					} catch (FileNotFoundException e) {
						base64_mime_image=file_name;
					}
					m_Model=m_Model.substring(0,pos_begin)+"\n"+base64_mime_image+"\n"+m_Model.substring(pos_end);
					start=pos+len+m_Url.length();
				}
				else
					start=pos+len;
			}
		}
	}

	private void ChangeSelect()
	{
		int start=0;
		int len=0;
		while (len!=-1)
		{
			int[] tag_pos=GetItemPosition("[{","}]",null,start);
			int pos=tag_pos[0];
			len=tag_pos[1];
			if (len!=-1)
			{
				String item=m_Model.substring(pos+2,pos+len-2);
				String new_text="<xsl:value-of select='"+item+"'/>";
				m_Model=m_Model.substring(0,pos)+new_text+m_Model.substring(pos+len);
				start=pos+new_text.length();
			}
		}
		start=0;
		len=0;
		while (len!=-1)
		{
			int[] tag_pos=GetItemPosition("[(",")]",null,start);
			int pos=tag_pos[0];
			len=tag_pos[1];
			if (len!=-1)
			{
				String item=m_Model.substring(pos+2,pos+len-2);
				String new_text="<xsl:value-of select='"+item+"'/>";
				m_Model=m_Model.substring(0,pos)+new_text+m_Model.substring(pos+len);
				start=pos+new_text.length();
			}
		}
	}

	private void InsertInTagWithDataLoop(String aTagName,String aLoopingBegin,String aAddbegin,String aAddend)
	{
		int start=0;
		int len=0;
		while (len!=-1)
		{
			int[] tag_pos=GetTagPosition(aTagName,start);
			int pos=tag_pos[0];
			len=tag_pos[1];
			int pos_end=-1;
			if (pos!=-1)
				pos_end=m_Model.indexOf(">",pos);
			if ((len!=-1) && (pos_end>=0))
			{
				String data= getDateInModel(pos, pos_end);
				String new_text;
				if ((!data.equals( "" )) && (!data.equals( "\".\"" )) && (!data.equals( "'.'" )) && (!data.equals( "''" )) && (!data.equals( "\"\"" )))
				{
					data=replace(data,"'","\"");
					new_text="<xsl:for-each select="+data+">\n"+aAddbegin+aLoopingBegin+m_Model.substring(pos,pos+len)+aAddend+"\n</xsl:for-each>";
				}
				else
				{
					new_text=aAddbegin+aLoopingBegin+m_Model.substring(pos,pos+len)+aAddend;
				}
				m_Model=m_Model.substring(0,pos)+new_text+m_Model.substring(pos+len);
				start=pos+new_text.length();
			}
			else
				len=-1;
		}
	}

	private String getDateInModel(int pos, int pos_end) 
	{
		String data="";
		int pos_data=m_Model.indexOf("data",pos);
		if ((pos_data>=0) && (pos_data<pos_end))
		{
			int pos_equal=m_Model.indexOf("=",pos_data);
			int pos_cote=m_Model.indexOf(" ",pos_equal);
			if (pos_end<pos_cote) pos_cote=pos_end;
			if ((pos_equal>=0) && (pos_cote>=0) && (pos_equal<pos_cote))
				data=m_Model.substring(pos_equal+1,pos_cote);
		}
		return data;
	}
	
}
