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

package org.lucterios.utils.graphic;

import java.awt.Color;
import javax.swing.text.*;

import org.lucterios.utils.SimpleParsing;
import org.lucterios.utils.Tools;

import java.util.*;

public class LucteriosDocumentParser {

	static final int BOLD=1;
	static final int ITALIC=2;
	static final int UNDERLINE=3;

	static class Run {
		StyleContext styles;

		public String content="";
		public Color color=Color.black;
		public boolean bold=false;
		public boolean italic=false;
		public boolean underline=false;
		//public boolean negatif=false;

		public Run(StyleContext styles,String content) {
		  this.content = content;
		  this.styles = styles;
		}

		public Run(Element aItem) {

			try{
				this.content = aItem.getDocument().getText(aItem.getStartOffset(),aItem.getEndOffset()-aItem.getStartOffset());
			}
		    catch (BadLocationException e) {
				System.err.println(e.getMessage());
		    }
		    this.styles = null;
		  AttributeSet as = aItem.getAttributes().copyAttributes();
		  if(as != null) {
		  	Enumeration<?>  names = as.getAttributeNames();
		    while(names.hasMoreElements()) {
				Object nextName = names.nextElement();
				Object value=as.getAttribute(nextName);
				String nextName_txt = nextName.toString();
				if (nextName_txt.equalsIgnoreCase("bold"))
					bold=((Boolean)value).booleanValue();
				if (nextName_txt.equalsIgnoreCase("italic"))
					italic=((Boolean)value).booleanValue();
				if (nextName_txt.equalsIgnoreCase("underline"))
					underline=((Boolean)value).booleanValue();
				if (nextName_txt.equalsIgnoreCase("Foreground"))
					color=(Color)value;
			}
		  }
		}

		public String toText()
		{
			String text=content;
		  	if (underline) text="{[underline]}"+text+"{[/underline]}";
		  	if (italic) text="{[italic]}"+text+"{[/italic]}";
		  	if (bold) text="{[bold]}"+text+"{[/bold]}";
			if (color.equals( Color.blue ))
				text="{[font color='#0000ff']}"+text+"{[/font]}";
			if (color.equals( Color.red ))
				text="{[font color='#ff0000']}"+text+"{[/font]}";
			if (color.equals( Color.green ))
				text="{[font color='#00ff00']}"+text+"{[/font]}";
			return text;
		}

		public Run(String content,Run aModel) {
		  this.content = content.replaceAll("&#160;"," ");
		  if (aModel!=null){
			  this.styles = aModel.styles;
			  this.color=aModel.color;
			  this.bold=aModel.bold;
			  this.italic=aModel.italic;
			  this.underline=aModel.underline;
		  }
		}

		public String toString()
		{
			String text=content;
			text+=" "+color.toString();
		  	if (bold) text+=" bold";
		  	if (italic) text+=" italic";
		  	if (underline) text+=" underline";
			return text;
		}

		public Style getStyle()
		{
			Style s = styles.getStyle(StyleContext.DEFAULT_STYLE);
			StyleConstants.setItalic(s, italic);
			StyleConstants.setBold(s, bold);
			StyleConstants.setUnderline(s, underline);
			StyleConstants.setForeground(s,color);
			/*if (negatif)
				StyleConstants.setFontFamily(s,"SansSerif");
			else*/
			StyleConstants.setFontFamily(s,"Serif");
			return s;
		}
	}

	DefaultStyledDocument doc;
	StyleContext styles;

	public LucteriosDocumentParser(DefaultStyledDocument doc, StyleContext styles) {
		this.doc = doc;
		this.styles = styles;
	}

	public void loadDocument(String aText) {
		aText=aText.trim().replaceAll("\n","");
//		aText=aText.replaceAll("&#160;"," ");
		String[] lines=aText.split("\\{\\[newline\\]\\}");
		for (int i = 0; i < lines.length; i++)
			addParagraph(lines[i]);
	}

	private String subItem(Element aItems)
	{
		String text="";
		if (AbstractDocument.LeafElement.class.isInstance(aItems))
		{
			Run current=new Run(aItems);
			text+=current.toText();
		}
		int nb=aItems.getElementCount();
		for(int idx=0;idx<nb;idx++)
			text+=subItem(aItems.getElement(idx));
		if (AbstractDocument.BranchElement.class.isInstance(aItems))
			text=text.trim()+"{[newline]}\n";
		return text;
	}

	public String saveDocument() {
		String text=subItem(this.doc.getDefaultRootElement());
		text=text.trim();
		text=text.replace((char)160,' ');
		text=Tools.replace(text," ","&#160;");
		text=Tools.replace(text,"font&#160;color","font color");
		while (text.endsWith("{[newline]}"))
			text=text.substring(0,text.length()-11).trim();
		text=text.trim().replaceAll("\n","");
		return text;
	}

	private ArrayList<Run> getRunsByPara(SimpleParsing aItem,Run aModel)
	{
		ArrayList<Run> list=new ArrayList<Run>();
		Run new_model=new Run("",aModel);
		if (aItem.getTagName().equalsIgnoreCase("b"))
			new_model.bold=true;
		if (aItem.getTagName().equalsIgnoreCase("i"))
			new_model.italic=true;
		if (aItem.getTagName().equalsIgnoreCase("u"))
			new_model.underline=true;
		if (aItem.getTagName().equalsIgnoreCase("font"))
		{
			String txt_color=aItem.getAttribut("color");
			if (txt_color.equalsIgnoreCase("#0000ff"))
				new_model.color=Color.blue;
			if (txt_color.equalsIgnoreCase("#00ff00"))
				new_model.color=Color.green;
			if (txt_color.equalsIgnoreCase("#ff0000"))
				new_model.color=Color.red;
		}
		list.add(new Run(aItem.getText(0),new_model));
		for(int idx=0;idx<aItem.getTagCount();idx++)
		{
			list.addAll(getRunsByPara(aItem.getSubTag(idx),new_model));
			list.add(new Run(aItem.getText(idx+1),new_model));
		}
		return list;
	}
	
	private String getFormatedText(String line)
	{
		String res="<P>"+Tools.convertLuctoriosFormatToHtml(line)+"</P>";
		return res; 
	}
	
	private void addParagraph(String line)
	{
		SimpleParsing paragraph=new SimpleParsing();
		if (paragraph.parse(getFormatedText(line)))
		{
			ArrayList<Run> list=getRunsByPara(paragraph,new Run(this.styles,""));			
			try
			{
				for (int i = 0; i < list.size(); i++) {
					Run run = list.get(i);
					doc.insertString(doc.getLength(), run.content, run.getStyle());
				}
				Style normal = styles.getStyle(StyleContext.DEFAULT_STYLE);
				doc.setLogicalStyle(doc.getLength() - 1, normal);
				doc.insertString(doc.getLength(), "\n", null);
			} catch (BadLocationException e) {
				ExceptionDlg.throwException(e);
			}
		}
  }
}
