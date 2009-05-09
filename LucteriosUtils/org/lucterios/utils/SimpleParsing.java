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

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SimpleParsing extends DefaultHandler 
{	
	public int hashCode() {
		return 0;
	}

	public final static String EMPTY_NAME="empty";
	public final static String XML_ENCODING="<?xml version='1.0' encoding='ISO-8859-1'?>";

	private SimpleParsing mParent;
	private TreeMap mAttrs=new TreeMap();
	private String mSimpleName=EMPTY_NAME;
	private ArrayList mTexts=new ArrayList();
	private ArrayList mFields=new ArrayList();

	private SimpleParsing(SimpleParsing aParent) 
	{
		super();
		mParent=aParent;
	}
	
	public SimpleParsing() 
	{
		super();
		mParent=null;
	}
	
	public boolean equals(Object value)
	{
		if (SimpleParsing.class.isInstance(value))
		{
			boolean result=true;
			SimpleParsing other_parse=(SimpleParsing)value;

			result=result && (getTagName().equals( other_parse.getTagName() ));
			
			result=result && (mAttrs.size()==other_parse.mAttrs.size());
			result=result && isAttributesEquals(mAttrs,other_parse.mAttrs);
			result=result && (getTagCount()==other_parse.getTagCount());
			for(int index=0;result && (index<getTagCount());index++)
				result=result && getSubTag(index).equals(other_parse.getSubTag(index));
			result=result && getText().equals(other_parse.getText());
			return result;
		}
		else
			return false;
	}

	private boolean isAttributesEquals(TreeMap firstAttributes,TreeMap secondaryAttributes) {
		boolean result=true;
		for (Iterator iterator = firstAttributes.entrySet().iterator(); iterator.hasNext();){
			Map.Entry entry = (Map.Entry) iterator.next();
			String key = (String) entry.getKey();
			result=result && secondaryAttributes.containsKey(key);
			result=result && firstAttributes.get(key).equals(secondaryAttributes.get(key));
		}
		return result;
	}
	
	private SimpleParsing mCurrent=null;
	public boolean parse(String aText)
	{
		boolean res;
		
		mCurrent=null;
		SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = factory.newSAXParser();
            InputStream is;
            if (aText.startsWith("<?xml"))
            	is=new BufferInputStream(aText);
            else
        		is=new BufferInputStream(XML_ENCODING+aText);
	        saxParser.parse(is, this);
			res=true;
		}
		catch(Exception e)
		{
			res=false;
		}
		return res;
	}

    private void fillElement (String aSimpleName,Attributes aAttrs) {
    	mSimpleName=aSimpleName;
    	mTexts.clear();
    	mFields.clear();
    	mAttrs.clear();
    	for(int idx=0;idx<aAttrs.getLength();idx++)
    		mAttrs.put(aAttrs.getQName(idx),aAttrs.getValue(idx));
    }
    public void startElement (String namespaceURI,String simpleName,String qualifiedName,Attributes attrs) throws SAXException {
    	if (mCurrent==null)
    		mCurrent=this;
    	else {
    		SimpleParsing new_item=new SimpleParsing(mCurrent);
    		mCurrent.mFields.add(new_item);
    		mCurrent=new_item;
    	}
    	mCurrent.fillElement(qualifiedName, attrs);
    }

    public void endElement (String namespaceURI,String simpleName,String qualifiedName) throws SAXException {
    	mCurrent=mCurrent.mParent;
    }

    public void characters (char buf [], int offset, int len) throws SAXException {
    	while (mCurrent.mFields.size()>=mCurrent.mTexts.size())
    		mCurrent.mTexts.add("");
    	int idx=mCurrent.mTexts.size()-1;
    	String line=(String)mCurrent.mTexts.get(idx);
    	line+=new String(buf, offset,len);
    	mCurrent.mTexts.set(idx,line);
    }
	
	public String getTagName()
	{
		return mSimpleName;
	}
	
	public String getAttribut(String aName,String aDefault)
	{
		if (mAttrs!=null) {
			String value=null;
			if (mAttrs.containsKey(aName))
				value=(String)mAttrs.get(aName);	
			if (value!=null)
				return value;
			else
				return aDefault;
		}
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

	public double getAttributDouble(String aName,double aDefault)
	{
		String value=getAttribut(aName);
        try
        {
            return new Double(value).doubleValue();
        }
        catch(java.lang.Exception e)
        {
            return aDefault;
        }
	}
	
	private String getContent()
	{
		String content="<"+getTagName();		
		for (Iterator iterator = mAttrs.entrySet().iterator(); iterator.hasNext();){
			Map.Entry entry = (Map.Entry) iterator.next();
			String key = (String) entry.getKey();
			content+=" "+key+"='"+mAttrs.get(key)+"'";	
		}
		if ((mTexts.size()==0) && (mFields.size()==0))
			content+="/>";
		else
			content+=">"+getSubContent()+"</"+getTagName()+">";
		return content;
	}
	
	private String getCData()
	{
		String value=getText();
		if (value.length()>0)
			return "<![CDATA["+value+"]]>";
		else
			return "";
	}
	
	public String getSubContent()
	{
		String sub_content=getCData();
		for(int idx=0;idx<mFields.size();idx++)
			sub_content+=((SimpleParsing)mFields.get(idx)).getContent();
		return sub_content;
	}

	public String getText(int index)
	{
		String val="";
		for(int idx=0;idx<mTexts.size();idx++)
			if (idx==index)
				val+=(String)mTexts.get(idx);
		return val;
	}
	
	public String getText()
	{
		String value="";
		for(int idx=0;idx<mTexts.size();idx++)
			value+=(String)mTexts.get(idx);
		return value.trim();
	}

	public int getCDataInt(int aDefault)
	{
		String value=getText();
        try
        {
            return new Integer(value).intValue();
        }
        catch(java.lang.Exception e)
        {
            return aDefault;
        }
	}

	public double getCDataDouble(double aDefault)
	{
		String value=getText();
        try
        {
            return new Double(value).doubleValue();
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
			return first.getText();
		else
			return "";
	}
	
	public String[] getTagNames()
	{
		String[] res=null;	
		res=new String[mFields.size()];
		for(int idx=0;idx<mFields.size();idx++)
			res[idx]=((SimpleParsing)mFields.get(idx)).getTagName();
		return res;
	}

	public int getTagCount()
	{
		return mFields.size();
	}
		
	public SimpleParsing getSubTag(int aIndex)
	{
		return (SimpleParsing)mFields.get(aIndex);
	}
	
	public SimpleParsing[] getSubTag(String aName)
	{
		String[] names=getTagNames();
		int nb=0;
		for(int idx=0;idx<names.length;idx++)
			if (names[idx]==aName)
				nb++;
		SimpleParsing[] pars_ret=new SimpleParsing[nb];
		int tag_idx=0;
		for(int idx=0;idx<mFields.size();idx++)
			if (((SimpleParsing)mFields.get(idx)).getTagName()==aName)
				pars_ret[tag_idx++]=(SimpleParsing)mFields.get(idx);
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
		return getContent();
	}
}
