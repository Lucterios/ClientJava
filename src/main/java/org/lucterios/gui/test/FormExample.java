package org.lucterios.gui.test;

import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIEdit;
import org.lucterios.gui.GUIForm;
import org.lucterios.gui.GUILabel;
import org.lucterios.gui.GUIMemo;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUISpinEdit;
import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.gui.GUIForm.FormVisitor;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.gui.GUIParam.ReSizeMode;
import org.lucterios.ui.GUIActionListener;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.Tools;

public class FormExample implements FormVisitor {

	private GUIForm mOwner;

	private GUIContainer pnl_new_btn;
	private GUILabel lbl_name;
	private GUIEdit txt_name;
	private GUILabel lbl_value;
	private GUISpinEdit txt_value;

	private GUIContainer pnl_btn;
	private GUIButton btn_AddNew;
	private GUIButton btn_ExitNew;

	private GUILabel lbl_memo;

	private GUIMemo txt_memo;
	
	public void execute(GUIForm form) {
		mOwner=form;
		mOwner.setTextTitle("Example form");
		Init();
		InitBtn();
	}

	public void InitBtn() {
		pnl_btn = mOwner.getGUIContainer().createContainer(ContainerType.CT_NORMAL, new GUIParam(0, 1,1,1,ReSizeMode.RSM_NONE,FillMode.FM_NONE));

		btn_AddNew = pnl_btn.createButton(new GUIParam(0,0, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH));
		btn_AddNew.setMnemonic('o');
		btn_AddNew.setTextString("OK");
		btn_AddNew.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				btn_Ok();
			}
		});

		btn_ExitNew = pnl_btn.createButton(new GUIParam(1, 0, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH));
		btn_ExitNew.setMnemonic('n');
		btn_ExitNew.setTextString("Annuler");
		btn_ExitNew.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				btn_cancel();
			}
		});
		GUIButton[] btns = { btn_AddNew, btn_ExitNew };
		mOwner.getGUIContainer().calculBtnSize(btns);
		mOwner.setDefaultButton(btn_AddNew);
	}

	public void Init() {
		pnl_new_btn = mOwner.getGUIContainer().createContainer(ContainerType.CT_NORMAL, new GUIParam(0, 0));

		lbl_name = pnl_new_btn.createLabel(new GUIParam(0, 0, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH));
		lbl_name.setTextString("Extension");
		lbl_name.setStyle(1);
		
		txt_name = pnl_new_btn.createEdit(new GUIParam(1, 0, 1, 1, ReSizeMode.RSM_HORIZONTAL, FillMode.FM_BOTH));

		lbl_value = pnl_new_btn.createLabel(new GUIParam(0, 1, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH));
		lbl_value.setTextString("Application associée");
		lbl_value.setStyle(1);

		txt_value = pnl_new_btn.createSpinEdit(new GUIParam(1, 1, 1, 1, ReSizeMode.RSM_HORIZONTAL, FillMode.FM_BOTH));
		txt_value.init(0, -10,+10);
		
		lbl_memo = pnl_new_btn.createLabel(new GUIParam(0, 2, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH));
		lbl_memo.setTextString("Code text");
		lbl_memo.setStyle(1);

		txt_memo = pnl_new_btn.createMemo(new GUIParam(1, 2, 1, 1, ReSizeMode.RSM_BOTH, FillMode.FM_BOTH));
		txt_memo.setFirstLine(1);
		try {
			txt_memo.setValue(Tools.parseISToString(this.getClass().getResourceAsStream("FormExample.java")));
		} catch (LucteriosException e) {
			e.printStackTrace();
		}
		
	}

	protected void btn_cancel() {
		try{
			mOwner.setVisible(false);
		}catch (Exception e) {
			ExceptionDlg.throwException(e);
		}
	}

	protected void btn_Ok() {
		btn_cancel();
		String result=String.format("Name=%s\nValue=%d",txt_name.getTextString(),txt_value.getNumber());
		mOwner.getGenerator().showMessageDialog(result, "Result");
	}	
}
