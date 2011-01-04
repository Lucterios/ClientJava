package org.lucterios.stressTester.observer;

import org.lucterios.client.presentation.ObserverAbstract;
import org.lucterios.client.presentation.ObserverConstant;
import org.lucterios.client.utils.IForm;
import org.lucterios.client.utils.IDialog;
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
