package org.lucterios.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

public class IniFileReader {
	
	private BufferedReader buffer_reader=null;
	private FileReader reader=null;
	
	public IniFileReader(String aIniFileName) {
		if (buffer_reader==null)
		try {	
			File ini_file=new File(aIniFileName); 
			if (ini_file.exists()){		
				reader = new FileReader(ini_file);
				buffer_reader = new BufferedReader(reader);
			}
		}
		catch (Exception e) {}
	}
	
	public void close() {
		try {
			buffer_reader.close();
			reader.close();
		} catch (IOException e) {}
		buffer_reader=null;
		reader=null;
	}
	
	public int getValueSectionInt(String category, String component) {
		try {	
				String value = getValueSection(category,component);
				return Integer.parseInt(value);
		}catch (Exception e) {}
		return -1;
	}

	public String getValueSection(String category, String component) 
	{
		String value = null;
		if (buffer_reader!=null)
		try {	
			buffer_reader.mark(5*1024);
			String current_temp = buffer_reader.readLine();
			while ((value == null) && (current_temp != null)) {
				if (current_temp.trim().equals("[" + category + "]")) {
					current_temp = buffer_reader.readLine();
					while ((value == null) && (current_temp != null) && !current_temp.trim().startsWith("[")) {
						if (current_temp.startsWith(component + "=")) 
							value = current_temp.substring(component.length() + 1);
						current_temp = buffer_reader.readLine();	
					}
				}
				current_temp = buffer_reader.readLine();
			}
			buffer_reader.reset();
		}catch (Exception e) {}
		return value;
	}
	
	public TreeMap getValuesSection(String category) 
	{
		TreeMap values = new TreeMap();
		if (buffer_reader!=null)
		try {	
			buffer_reader.mark(5*1024);
			String current_temp = buffer_reader.readLine();
			while ((current_temp != null)) {
				if (current_temp.trim().equals("[" + category + "]")) {
					current_temp = buffer_reader.readLine();
					while ((current_temp != null) && !current_temp.trim().startsWith("[")) {
						int equal_pos=current_temp.indexOf('=');
						if (equal_pos!=-1)
							values.put(current_temp.substring(0,equal_pos),current_temp.substring(equal_pos+1));
						current_temp = buffer_reader.readLine();	
					}
				}
				current_temp = buffer_reader.readLine();
			}
			buffer_reader.reset();
		}catch (Exception e) {}
		return values;
	}
	
}
