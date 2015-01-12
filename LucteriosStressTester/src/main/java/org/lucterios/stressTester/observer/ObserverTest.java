package org.lucterios.stressTester.observer;

import org.lucterios.engine.presentation.ObserverAbstract;
import org.lucterios.engine.presentation.ObserverConstant;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIForm;
import org.lucterios.utils.LucteriosException;

public class ObserverTest extends ObserverAbstract {

	public String getObserverName() {
		return getClass().getName().substring(11);
	}

	public byte getType() {
		return ObserverConstant.TYPE_NONE;
	}

	public void setNameComponentFocused(String aNameComponentFocused) {	}

	public void show(String aTitle, GUIForm new_frame) throws LucteriosException {}

	public void show(String aTitle, GUIDialog aGUI) throws LucteriosException {}

}
