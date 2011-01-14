package org.lucterios.stressTester.observer;

import org.lucterios.engine.presentation.ObserverAbstract;
import org.lucterios.engine.presentation.ObserverConstant;
import org.lucterios.engine.utils.IDialog;
import org.lucterios.engine.utils.IForm;
import org.lucterios.utils.LucteriosException;

public class ObserverTest extends ObserverAbstract {

	public String getObserverName() {
		return getClass().getName().substring(11);
	}

	public byte getType() {
		return ObserverConstant.TYPE_NONE;
	}

	public void setNameComponentFocused(String aNameComponentFocused) {	}

	public void show(String aTitle, IForm new_frame) throws LucteriosException {}

	public void show(String aTitle, IDialog aGUI) throws LucteriosException {}

}
