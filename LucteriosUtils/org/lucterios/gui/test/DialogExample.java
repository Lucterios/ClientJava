package org.lucterios.gui.test;

import java.io.File;

import org.lucterios.graphic.date.DatePickerSimple;
import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIEdit;
import org.lucterios.gui.GUILabel;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUIButton.GUIActionListener;
import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.gui.GUIDialog.DialogVisitor;
import org.lucterios.gui.GUIGenerator.FileFilter;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.gui.GUIParam.ReSizeMode;
import org.lucterios.utils.LucteriosException;

public class DialogExample implements DialogVisitor {

	private GUIDialog mDialog;
	
	public void execute(GUIDialog dialog) {
		mDialog=dialog;
		mDialog.setTitle("Exemple dialogue");
		initScroll();	
		initBtn();				
	}

	private void initScroll() {
		GUIContainer sv=mDialog.getContainer().createContainer(ContainerType.CT_SCROLL, new GUIParam(0, 0));
		for(int index=0;index<10;index++) {
			GUILabel tv=sv.createLabel(new GUIParam(0, index,1, 1, ReSizeMode.RSM_NONE, FillMode.FM_NONE));
			tv.setTextString("Text"+(index+1));
			GUIEdit et=sv.createEdit(new GUIParam(1, index, 1, 1, ReSizeMode.RSM_HORIZONTAL, FillMode.FM_HORIZONTAL));
			et.setTextString("---");
		}
	}

	private void initBtn() {
		GUIContainer btnPnl=mDialog.getContainer().createContainer(ContainerType.CT_NORMAL,new GUIParam(0,1,1,1, ReSizeMode.RSM_HORIZONTAL, FillMode.FM_HORIZONTAL));
		GUIButton bt1=btnPnl.createButton(new GUIParam(0, 3, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_NONE));
        bt1.setTextString("Open file");
        bt1.addActionListener(new GUIActionListener() {		
			public void actionPerformed() {
				openFile();
			}
		});
		GUIButton bt2=btnPnl.createButton(new GUIParam(1, 3, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_NONE));
        bt2.setTextString("Date selected");
        bt2.addActionListener(new GUIActionListener() {		
			public void actionPerformed() {
				dateSelected();
			}
		});
		GUIButton bt3=btnPnl.createButton(new GUIParam(2, 3, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_NONE));
        bt3.setTextString("Exception dlg");
        bt3.addActionListener(new GUIActionListener() {		
			public void actionPerformed() {
				exceptionDlg();
			}
		});
        btnPnl.calculBtnSize(new GUIButton[]{bt1,bt2,bt3});
	}

	private void openFile() {
		File selected=mDialog.getGenerator().selectOpenFileDialog(new FileFilter() {
			public String getDescription() {
				return "Open file";
			}
			
			public boolean accept(File aFile) {
				return aFile.isFile() && aFile.getName().startsWith("a");
			}
		},mDialog);
		if (selected!=null)
			mDialog.getGenerator().showMessageDialog(selected.getAbsolutePath(), "File selected");
		else
			mDialog.getGenerator().showErrorDialog("No file selected!", "File selected");
			
	}

	private void dateSelected() {
		GUIDialog dateDlg=mDialog.getGenerator().newDialog(mDialog,null);
		DatePickerSimple date_simple=new DatePickerSimple();
		dateDlg.setDialogVisitor(date_simple);
		dateDlg.setVisible(true);
		mDialog.getGenerator().showMessageDialog(date_simple.getSelectedDate().toString(),"Date selected");
	}

	private void exceptionDlg() {
		ExceptionDlg.mGenerator=mDialog.getGenerator();
		try {
			throw new LucteriosException("Error");
		}
		catch (Exception e) {
			ExceptionDlg.throwException(e);
		}
	}
	
}
