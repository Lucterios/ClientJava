package org.lucterios.client.gui;


import javax.swing.ImageIcon;

import org.lucterios.client.application.observer.ObserverCustom;
import org.lucterios.client.presentation.Observer.MapContext;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.graphic.JAdvancePanel;

public class TogglePanel extends JAdvancePanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ObserverCustom mObserverCustom=null;
	private String title;
	private ImageIcon mIcon;
	
	public TogglePanel(String title,ImageIcon icon,String extension,String action){
		mObserverCustom=new ObserverCustom();
		mObserverCustom.setGUIContainer(this);
		mObserverCustom.setSource(extension, action);
		mObserverCustom.setContext(new MapContext());
		this.title=title;
		this.mIcon=icon;
	}

	public String getTitle() {
		return title;
	}

	public void refreshPanel() throws LucteriosException{
		setVisible(false);
		mObserverCustom.refresh();
	}

	public ImageIcon getIcon() {
		// TODO Auto-generated method stub
		return mIcon;
	}
	
}
