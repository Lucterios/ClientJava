package org.lucterios.graphic;

import javax.swing.JMenu;

import org.lucterios.gui.AbstractImage;

public class MenuNode extends JMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mDescription;

	public MenuNode() {
		super();
	}

	public AbstractImage getMenuIcon(){
		return new SwingImage(this.getIcon());
	}

	public void setDescription(String mDescription) {
		this.mDescription = mDescription;
	}

	public String getDescription() {
		return mDescription;
	}

}