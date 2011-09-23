package org.lucterios.android;

import org.lucterios.android.widget.WFrame;
import org.lucterios.android.widget.WGenerator;

import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.gui.test.FrameTest;

public class Main extends WFrame {

	public Main() {
		super(new WGenerator());
		((WGenerator)getGenerator()).setFrame(this);
		ExceptionDlg.mGenerator=getGenerator();
		setFrameVisitor(new FrameTest());
	}
   
}