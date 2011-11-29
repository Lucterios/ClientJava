package org.lucterios.graphic;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIComponent.GUIFocusListener;

public class FocusListenerList extends ArrayList<GUIFocusListener> implements FocusListener {

	private static final long serialVersionUID = 1L;

    public void focusLost(java.awt.event.FocusEvent e) 
    {
		GUIComponent origine=null;
		if (GUIComponent.class.isInstance(e.getComponent()))
			origine=(GUIComponent)e.getComponent();
		GUIComponent target=null;
		if (GUIComponent.class.isInstance(e.getOppositeComponent()))
			target=(GUIComponent)e.getOppositeComponent();
		//System.out.println("Focus lost \n\torigine:"+e.getComponent()+"\n\ttarget:"+e.getOppositeComponent());
		for(GUIFocusListener l:this) {
			if (l!=null) 
				try {
					l.focusLost(origine,target);
				}
				catch (Throwable exept) {
					exept.printStackTrace();
				}
		}
    }

	public void focusGained(FocusEvent e) { }
	
}
