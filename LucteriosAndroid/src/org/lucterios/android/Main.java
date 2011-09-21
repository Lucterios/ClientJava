package org.lucterios.android;

import org.lucterios.android.widget.WFrame;
import org.lucterios.android.widget.WGenerator;

import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.graphic.resources.Resources;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIFrame;
import org.lucterios.gui.GUIMenu;
import org.lucterios.gui.GUIFrame.FrameVisitor;
import org.lucterios.gui.test.DialogExample;
import org.lucterios.gui.test.DialogSimple;
import org.lucterios.ui.GUIActionListener;

import android.view.Menu;

public class Main extends WFrame implements FrameVisitor {

	public Main() {
		super(new WGenerator());
		((WGenerator)getGenerator()).setFrame(this);
		ExceptionDlg.mGenerator=getGenerator();
		setFrameVisitor(this);
	}
	
	public Main(WGenerator generator) {
		super(generator);
		generator.setFrame(this);
		setFrameVisitor(this);
	}

	public void execute(GUIFrame frame) {
		setImage(getGenerator().CreateImage(Resources.class.getResource("LucteriosLogo.png")));
	}
		
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result=super.onCreateOptionsMenu(menu);
    	GUIMenu disconnect=addMenu(false);
    	disconnect.setText("Déconnecter");
    	disconnect.setActionListener(new GUIActionListener() {
			public void actionPerformed() {
				getGenerator().showMessageDialog("Déconnecter", "Main");
			}
    	});
    	GUIMenu help=addMenu(false);
    	help.setText("Aide...");
    	help.setActionListener(new GUIActionListener() {
			public void actionPerformed() {
				GUIDialog dlg=getGenerator().newDialog(Main.this);
				dlg.setDialogVisitor(new DialogExample());
				dlg.setVisible(true);
			}    		
    	});
    	GUIMenu help2=addMenu(false);
    	help2.setText("Aide2...");
    	help2.setActionListener(new GUIActionListener() {
			public void actionPerformed() {
				GUIDialog dlg=getGenerator().newDialog(Main.this);
				dlg.setDialogVisitor(new DialogSimple());
				dlg.setVisible(true);
			}    		
    	});
    	GUIMenu about=addMenu(false);
    	about.setText("A propos...");
    	about.setActionListener(new GUIActionListener() {
			public void actionPerformed() {
				getGenerator().showErrorDialog("A propos", "Main");
			}    		
    	});
    	GUIMenu exit=addMenu(false);
    	exit.setText("Quitter");
    	exit.setActionListener(new GUIActionListener() {
			public void actionPerformed() {
				System.exit(0);
			}    		
    	});
    	return result;
	}
   
}