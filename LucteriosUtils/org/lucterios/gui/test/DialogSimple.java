package org.lucterios.gui.test;

import org.lucterios.graphic.resources.Resources;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUICheckList;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIEdit;
import org.lucterios.gui.GUIHyperMemo;
import org.lucterios.gui.GUIHyperText;
import org.lucterios.gui.GUIImage;
import org.lucterios.gui.GUILabel;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.gui.GUIDialog.DialogVisitor;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.gui.GUIParam.ReSizeMode;
import org.lucterios.ui.GUIActionListener;
import org.lucterios.utils.Tools;

public class DialogSimple implements DialogVisitor {

	private GUIDialog mDialog;
	private GUIContainer mContainer;
	private GUIHyperText mHyperText;
	private GUIHyperMemo mHyperMemo;

	public void execute(GUIDialog dialog) {
		mDialog = dialog;
		mDialog.setTextTitle("Simple dialog");
		mContainer = mDialog.getContainer();
		initComp();
		initBtn();
	}

	public void closing() {

	}

	private void initBtn() {
		GUIContainer btnPnl = mDialog.getContainer().createContainer(
				ContainerType.CT_NORMAL,
				new GUIParam(0, 5, 2, 1, ReSizeMode.RSM_HORIZONTAL,
						FillMode.FM_NONE));
		GUIButton bt1 = btnPnl.createButton(new GUIParam(0, 0, 1, 1,
				ReSizeMode.RSM_NONE, FillMode.FM_NONE));
		bt1.setTextString("Success...");
		bt1.setImage(mDialog.getGenerator().CreateImage(Resources.class.getResource("new.gif")));
		bt1.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				String text=mHyperMemo.save();
				text=Tools.convertLuctoriosFormatToHtml(text);
				mHyperText.setTextString(text);
				mDialog.getGenerator().showMessageDialog("Success message",
						mDialog.getTextTitle());
			}
		});
		GUIButton bt2 = btnPnl.createButton(new GUIParam(1, 0, 1, 1,
				ReSizeMode.RSM_NONE, FillMode.FM_NONE));
		bt2.setTextString("Error...");
		bt2.setImage(mDialog.getGenerator().CreateImage(Resources.class.getResource("open.gif")));
		bt2.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				mHyperText.repaint();
				mDialog.getGenerator().showErrorDialog("Error message",
						mDialog.getTextTitle());
			}
		});
		GUIButton bt3 = btnPnl.createButton(new GUIParam(2, 0, 1, 1,
				ReSizeMode.RSM_NONE, FillMode.FM_NONE));
		bt3.setTextString("Exit");
		bt3.setImage(mDialog.getGenerator().CreateImage(Resources.class.getResource("paste.gif")));
		bt3.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				mDialog.setVisible(false);
			}
		});
		btnPnl.calculBtnSize(new GUIButton[] { bt1, bt2, bt3 });
	}

	private void initComp() {
		GUIContainer scroll=mContainer.createContainer(ContainerType.CT_SCROLL,new GUIParam(0, 0));
		GUILabel lb;
		lb = scroll.createLabel(new GUIParam(0, 0, 1, 1,
				ReSizeMode.RSM_NONE, FillMode.FM_NONE));
		lb.setTextString("Select");

		GUICheckList checkList = scroll.createCheckList(new GUIParam(1, 0));
		checkList.clearList();
		checkList.setMultiSelection(true);
		checkList.setListData(new Object[] { "aaa", "bbbb", "ccc", "dddd" });
		checkList.setSelectedIndices(new int[] { 1, 2 });

		lb = scroll.createLabel(new GUIParam(0, 1, 1, 1,
				ReSizeMode.RSM_NONE, FillMode.FM_NONE));
		lb.setTextString("Complex text");
		mHyperMemo = scroll.createHyperMemo(new GUIParam(1, 1));
		mHyperMemo.load("blablablablablabla{[newline]}blablablabla");	

		lb = scroll.createLabel(new GUIParam(0, 2, 1, 1,
				ReSizeMode.RSM_NONE, FillMode.FM_NONE));
		lb.setTextString("Picture");		
		GUIImage img=scroll.createImage(new GUIParam(1, 2));
		img.setImage(Resources.class.getResource("warning.png"));

		lb = scroll.createLabel(new GUIParam(0, 3, 1, 1,
				ReSizeMode.RSM_NONE, FillMode.FM_NONE));
		lb.setTextString("Html");		
		mHyperText=scroll.createHyperText(new GUIParam(1, 3));

		lb = scroll.createLabel(new GUIParam(0, 4, 1, 1,
				ReSizeMode.RSM_NONE, FillMode.FM_NONE));
		lb.setTextString("Edit float");		
		GUIEdit ed1=scroll.createEdit(new GUIParam(1, 4));
		ed1.setRange(-54, 184, 2);
		ed1.setFloatEditor(true);
		
		lb = scroll.createLabel(new GUIParam(0, 5, 1, 1,
				ReSizeMode.RSM_NONE, FillMode.FM_NONE));
		lb.setTextString("Edit limited");		
		GUIEdit ed2=scroll.createEdit(new GUIParam(1, 5));
		ed2.setTextCondition("",10);
		
		lb = scroll.createLabel(new GUIParam(0, 6, 1, 1,
				ReSizeMode.RSM_NONE, FillMode.FM_NONE));
		lb.setTextString("Edit regular expression");		
		GUIEdit ed3=scroll.createEdit(new GUIParam(1, 6));
		ed3.setTextCondition("(7|70[0-9]{0,4})",0);
		
		scroll.createContainer(ContainerType.CT_NORMAL, new GUIParam(0, 10));
	}

}
