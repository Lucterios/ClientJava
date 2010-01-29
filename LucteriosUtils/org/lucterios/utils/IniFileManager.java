package org.lucterios.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class IniFileManager extends Properties {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String IniFileName;

	public IniFileManager(String aIniFileName) throws IOException {
		IniFileName=aIniFileName;
		load();
	}
	
	public void save() throws IOException{
		store(new FileOutputStream(IniFileName), "");
		load();
	}
	
	private void load() throws IOException{
		clear();
		load(new FileInputStream(IniFileName));
	}
	
	public int getValueSectionInt(String category, String component,int defaultValue) {
		try {	
				String value = getValueSection(category,component);
				return Integer.parseInt(value);
		}catch (Exception e) {}
		return defaultValue;
	}

	public String getValueSection(String category, String component) 
	{
		return getProperty(String.format("%s.%s", category, component),"");
	}
	
	public StringDico getValuesSection(String category) 
	{
		String begin=category+".";
		StringDico values = new StringDico();
		for(Object key:keySet().toArray()){
			if (key.toString().startsWith(begin))
				values.put(key.toString().substring(begin.length()), getProperty(key.toString(),""));
		}
		return values;
	}

	public void clearSection(String category) {
		String begin=category+".";
		for(Object key:keySet().toArray()){
			if (key.toString().startsWith(begin))
				remove(key);
		}
	}
	
	public void setValueSection(String category, String component,String value) 
	{
		setProperty(String.format("%s.%s", category, component),value);
	}
	
	public void setValueSectionInt(String category, String component,int value) 
	{
		setProperty(String.format("%s.%s", category, component),new Integer(value).toString());
	}

	public void setValuesSection(String category,StringDico values){
		clearSection(category);
		for(Object comp:values.keySet().toArray()){
			setValueSection(category, comp.toString(),values.get(comp.toString()));
		}
	}

	
}
