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

package org.lucterios.utils;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;

public class SimpleParsing 
{	
	public int hashCode() {
		return 0;
	}

	private Element mItem;
	public final static String EMPTY_NAME="empty";

	public SimpleParsing() 
	{
		super();
		mItem=new Element(EMPTY_NAME); 
	}

	public boolean equals(Object value)
	{
		if (SimpleParsing.class.isInstance(value))
		{
			boolean result=true;
			SimpleParsing other_parse=(SimpleParsing)value;

			result=result && (getTagName().equals( other_parse.getTagName() ));
			
			List this_attributes=mItem.getAttributes();
			List other_attributes=other_parse.mItem.getAttributes();
			result=result && (this_attributes.size()==other_attributes.size());
			result = isAttributesEquals(this_attributes,other_attributes);
			result=result && (getTagCount()==other_parse.getTagCount());
			for(int index=0;result && (index<getTagCount());index++)
				result=result && getSubTag(index).equals(other_parse.getSubTag(index));
			result=result && getCData().equals(other_parse.getCData());
			return result;
		}
		else
			return false;
	}

	private boolean isAttributesEquals(List firstAttributes,List secondaryAttributes) {
		boolean result=true;
		for(int index=0;result && (index<firstAttributes.size());index++)
		{
			result=result && ((Attribute)firstAttributes.get(index)).getName().equals(((Attribute)secondaryAttributes.get(index)).getName());
			result=result && ((Attribute)firstAttributes.get(index)).getValue().equals(((Attribute)secondaryAttributes.get(index)).getValue());
		}
		return result;
	}
	
	private SimpleParsing(Element aItem) 
	{
		super();
		mItem=aItem;
	}
		
	public boolean parse(String aText)
	{
		boolean res;
		SAXBuilder sxb = new SAXBuilder();
		try
		{
			Document doc= sxb.build(new StringReader(aText));
			mItem = doc.getRootElement();
			res=true;
		}
		catch(Exception e)
		{
			res=false;
		}
		return res;
	}

	public String getTagName()
	{
		return mItem.getName();
	}
	
	public String getAttribut(String aName,String aDefault)
	{
		String value=mItem.getAttributeValue(aName);
		if (value!=null)
			return value;
		else
			return aDefault;
	}

	public String getAttribut(String aName)
	{
		return getAttribut(aName,"");
	}
	
	public int getAttributInt(String aName,int aDefault)
	{
		String value=getAttribut(aName);
        try
        {
            return new Integer(value).intValue();
        }
        catch(java.lang.Exception e)
        {
            return aDefault;
        }
	}
	
	public float getAttributFloat(String aName,float aDefault)
	{
		String value=getAttribut(aName);
        try
        {
            return new Double(value).floatValue();
        }
        catch(java.lang.Exception e)
        {
            return aDefault;
        }
	}
	
	private String getContent(Element aItem)
	{
		String content="<"+aItem.getName();
		List attribs=aItem.getAttributes();
		Iterator i_att = attribs.iterator();
		while(i_att.hasNext())
		{
			Attribute cur=(Attribute)i_att.next();
			content+=" "+cur.getName()+"='"+cur.getValue()+"'";
		}
		
		String value=getCdata(aItem);
		List children=aItem.getChildren();

		if ((value.length()==0) && (children.size()==0))
			content+="/>";
		else
		{
			content+=">"+value;		
			Iterator i_child = children.iterator();
			while(i_child.hasNext())
				content+=getContent((Element)i_child.next());
			content+="</"+aItem.getName()+">";
		}
		return content;
	}
	
	private String getCdata(Element aItem)
	{
		String value=aItem.getTextTrim();
		if (value.length()>0)
			return "<![CDATA["+value+"]]>";
		else
			return "";
	}
	
	public String getSubContent()
	{
		String sub_content=getCdata(mItem);
		List children=mItem.getChildren();
		Iterator i = children.iterator();
		while(i.hasNext())
			sub_content+=getContent((Element)i.next());
		return sub_content;
	}

	public String getCData(int index)
	{
		List content=mItem.getContent();
		String val="";
		int child_idx=0;
		for(int idx=0;idx<content.size();idx++)
		{
			if (Element.class.isInstance(content.get(idx)))
				child_idx++;
			if (Text.class.isInstance(content.get(idx)) && (child_idx==index))
				val=((Text)content.get(idx)).getText();
			if (CDATA.class.isInstance(content.get(idx)) && (child_idx==index))
				val=((Text)content.get(idx)).getText();
		}
		return val;
	}
	
	public String getCData()
	{
		return mItem.getTextTrim();
	}

	public int getCDataInt(int aDefault)
	{
		String value=getCData();
        try
        {
            return new Integer(value).intValue();
        }
        catch(java.lang.Exception e)
        {
            return aDefault;
        }
	}

	public float getCDataFloat(float aDefault)
	{
		String value=getCData();
        try
        {
            return new Float(value).floatValue();
        }
        catch(java.lang.Exception e)
        {
            return aDefault;
        }
	}
	
	public String getCDataOfFirstTag(String aName)
	{
		SimpleParsing first=getFirstSubTag(aName);
		if (first!=null)
			return first.getCData();
		else
			return "";
	}
	
	public String[] getTagNames()
	{
		String[] res=null;	
		List children=mItem.getChildren();
		res=new String[children.size()];
		Iterator i = children.iterator();
		int tag_idx=0;
		while(i.hasNext())
		{
	      Element courant = (Element)i.next();
		  res[tag_idx++]=courant.getName();
	    }		
				
		/*int nb=0;
		for(int index=1;index<(mContent.length-1);index++)
			if (mContent[index].startsWith("\t<") && !mContent[index].startsWith("\t</"))
				nb++;
		res=new String[nb];
		int tag_idx=0;
		for(int index=1;index<(mContent.length-1);index++)
			if (mContent[index].startsWith("\t<") && !mContent[index].startsWith("\t</"))
			{
				String line=mContent[index].substring(2,mContent[index].length()-1);
				if (line.endsWith("/")) line=line.substring(0,line.length()-1);
				res[tag_idx++]=line.split(" ")[0];
			}*/
		return res;
	}

	public int getTagCount()
	{
		List children=mItem.getChildren();
		return children.size();
		
		/*int nb=0;
		for(int index=1;index<(mContent.length-1);index++)
			if (mContent[index].startsWith("\t<") && !mContent[index].startsWith("\t</"))
				nb++;
		return nb;*/
	}
		
	public SimpleParsing getSubTag(int aIndex)
	{
		List children=mItem.getChildren();
		return new SimpleParsing((Element)children.get(aIndex));
		
		/*SimpleParsing res=null;
		int tag_idx=0;
		int index=1;
		while ((index<(mContent.length-1)) && (res==null))
		{
			if (mContent[index].startsWith("\t<") && !mContent[index].startsWith("\t</"))
			{
				if (tag_idx==aIndex)
				{
					res=new SimpleParsing();
					String new_content="";
					if (mContent[index].endsWith("/>"))
						new_content=mContent[index++].trim();
					else
					{
						while ((index<(mContent.length-1)) && !mContent[index].startsWith("\t</"))
							new_content+=mContent[index++].trim();
						new_content+=mContent[index++].trim();
					}
					res.parse(new_content);
				}
				else
					index++;
				tag_idx++;
			}
			else
				index++;
		}
		return res;*/
	}
	
	public SimpleParsing[] getSubTag(String aName)
	{
		List children=mItem.getChildren(aName);
		SimpleParsing[] pars_ret=new SimpleParsing[children.size()];
		Iterator i = children.iterator();
		int tag_idx=0;
		while(i.hasNext())
	      pars_ret[tag_idx++]=new SimpleParsing((Element)i.next());
		return pars_ret;
	}
	
	public SimpleParsing getFirstSubTag(String aName)
	{
		SimpleParsing[] pars_ret=getSubTag(aName);
		if (pars_ret.length>0)
			return pars_ret[0];
		else
			return null;
	}
	
	public String toString()
	{
		return getContent(mItem);
	}
}
