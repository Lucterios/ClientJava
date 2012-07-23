package org.lucterios.gui.test;

import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.graphic.resources.Resources;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIForm;
import org.lucterios.gui.GUIFrame;
import org.lucterios.gui.GUIMenu;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.gui.GUIFrame.FrameVisitor;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.gui.GUIParam.ReSizeMode;
import org.lucterios.ui.GUIActionListener;

public class FrameTest implements FrameVisitor {

	private GUIFrame mFrame;

	public void execute(GUIFrame frame) {
		mFrame = frame;
		mFrame.setImage(mFrame.getGenerator().CreateImage(Resources.class.getResource("LucteriosLogo.png")));
		mFrame.setTextTitle("Test frame");
		initComp();
		initBtn();
	}
	
	public void menuCreate(){
    	GUIMenu disconnect=mFrame.addMenu(false);
    	disconnect.setText("Déconnecter");
    	disconnect.setActionListener(new GUIActionListener() {
			public void actionPerformed() {
				mFrame.getGenerator().showMessageDialog("Déconnecter", "Main");
			}
    	});
    	GUIMenu help=mFrame.addMenu(false);
    	help.setText("Aide...");
    	help.setActionListener(new GUIActionListener() {
			public void actionPerformed() {
				GUIDialog dlg=mFrame.getGenerator().newDialog(mFrame);
				dlg.setDialogVisitor(new DialogExample());
				dlg.setVisible(true);
			}    		
    	});
    	GUIMenu help2=mFrame.addMenu(false);
    	help2.setText("Aide2...");
    	help2.setActionListener(new GUIActionListener() {
			public void actionPerformed() {
				GUIDialog dlg=mFrame.getGenerator().newDialog(mFrame);
				dlg.setDialogVisitor(new DialogSimple());
				dlg.setVisible(true);
			}    		
    	});
    	GUIMenu about=mFrame.addMenu(false);
    	about.setText("A propos...");
    	about.setActionListener(new GUIActionListener() {
			public void actionPerformed() {
				mFrame.getGenerator().showErrorDialog("A propos", "Main");
			}    		
    	});
    	GUIMenu exit=mFrame.addMenu(false);
    	exit.setText("Quitter");
    	exit.setActionListener(new GUIActionListener() {
			public void actionPerformed() {
				System.exit(0);
			}    		
    	});
	}

	public void closing() {

	}

	private void initComp() {
	}

	private void initBtn() {
		GUIContainer btnPnl = mFrame.getGUIContainer().createContainer(
				ContainerType.CT_NORMAL,
				new GUIParam(0, 5, 2, 1, ReSizeMode.RSM_HORIZONTAL,
						FillMode.FM_NONE));
		GUIButton bt1 = btnPnl.createButton(new GUIParam(0, 0, 1, 1,
				ReSizeMode.RSM_NONE, FillMode.FM_NONE));
		bt1.setTextString("Simple...");
		bt1.setImage(mFrame.getGenerator().CreateImage(Resources.class.getResource("new.gif")));
		bt1.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				GUIDialog dialog=mFrame.getGenerator().newDialog(null);
				dialog.setDialogVisitor(new DialogSimple());
				dialog.setVisible(true);				
			}
		});
		GUIButton bt2 = btnPnl.createButton(new GUIParam(1, 0, 1, 1,
				ReSizeMode.RSM_NONE, FillMode.FM_NONE));
		bt2.setTextString("Example...");
		bt2.setImage(mFrame.getGenerator().CreateImage(Resources.class.getResource("open.gif")));
		bt2.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				GUIDialog dialog=mFrame.getGenerator().newDialog(null);
				dialog.setDialogVisitor(new DialogExample());
				dialog.setVisible(true);				
			}
		});
		GUIButton bt4=btnPnl.createButton(new GUIParam(2, 0, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_NONE));
        bt4.setTextString("Open form");
        bt4.addActionListener(new GUIActionListener() {		
			public void actionPerformed() {
				openForm();
			}
		});
		GUIButton bt3 = btnPnl.createButton(new GUIParam(3, 0, 1, 1,
				ReSizeMode.RSM_NONE, FillMode.FM_NONE));
		bt3.setTextString("Exit");
		bt3.setImage(mFrame.getGenerator().CreateImage(Resources.class.getResource("paste.gif")));
		bt3.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				System.exit(0);
			}
		});
		btnPnl.calculBtnSize(new GUIButton[] { bt1, bt2, bt3, bt4 });
	}

	private void openForm() {
		try {
			GUIForm form=mFrame.getGenerator().newForm("example");
			form.setFormVisitor(new FormExample());
			form.setVisible(true);
		}catch (Exception e) {
			ExceptionDlg.throwException(e);
		}
	}
	
}
