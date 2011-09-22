package org.lucterios.gui.test;

import org.lucterios.graphic.resources.Resources;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIFrame;
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

	public void closing() {

	}

	private void initComp() {
	}

	private void initBtn() {
		GUIContainer btnPnl = mFrame.getContainer().createContainer(
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
		GUIButton bt3 = btnPnl.createButton(new GUIParam(2, 0, 1, 1,
				ReSizeMode.RSM_NONE, FillMode.FM_NONE));
		bt3.setTextString("Exit");
		bt3.setImage(mFrame.getGenerator().CreateImage(Resources.class.getResource("paste.gif")));
		bt3.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				System.exit(0);
			}
		});
		btnPnl.calculBtnSize(new GUIButton[] { bt1, bt2, bt3 });
	}

}
