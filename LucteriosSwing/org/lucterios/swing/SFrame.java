package org.lucterios.swing;

import javax.swing.JFrame;

import org.lucterios.gui.GUIFrame;
import org.lucterios.gui.GUIGenerator;

public class SFrame extends JFrame implements GUIFrame {

	private static final long serialVersionUID = 1L;

	private GUIGenerator mGenerator;
	public SFrame(GUIGenerator generator) {
		super();
		mGenerator=generator;
	}
	
	public GUIGenerator getGenerator() {
		return mGenerator;
	}

	public void refresh() {
		
	}

	public void setActive(boolean aIsActive) {
		
	}

}
