package org.lucterios.stressTester.inputs;

import org.lucterios.client.presentation.Observer.MapContext;
import org.lucterios.utils.SimpleParsing;

public class ActionTest {
	
	private int id;
	private String actionName;
	private String extension;
	private String name;
	private MapContext attributMap=new MapContext(); 
	
	public ActionTest(SimpleParsing aAction){
		id=aAction.getAttributInt("id",0);
		SimpleParsing item=aAction.getFirstSubTag("name");
		actionName=item.getText();
		String[] action_text=actionName.split("\\.");
		extension=action_text[0];
		name=action_text[1].replaceAll("::","_APAS_");
		SimpleParsing[] params=aAction.getSubTag("param");
		for(SimpleParsing param:params){
			attributMap.put(param.getAttribut("name"), param.getText());
		}
	}
	
	public int getId(){
		return id;
	}
	public String getActionName(){
		return actionName;
	}
	public String getExtension(){
		return extension;
	}
	public String getName(){
		return name;
	}
	public MapContext getAttributMap(){
		return attributMap;
	} 
	
}
