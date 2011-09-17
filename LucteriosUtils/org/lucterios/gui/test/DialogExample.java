package org.lucterios.gui.test;

import java.io.File;
import java.util.Date;

import org.lucterios.graphic.date.DatePickerSimple;
import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.graphic.MemoryJauge;
import org.lucterios.graphic.NavigatorBar;
import org.lucterios.graphic.ProgressPanel;
import org.lucterios.graphic.TimeLabel;
import org.lucterios.graphic.WaitingWindow;
import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUICheckList;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIEdit;
import org.lucterios.gui.GUIForm;
import org.lucterios.gui.GUILabel;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUIWindows;
import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.gui.GUIDialog.DialogVisitor;
import org.lucterios.gui.GUIGenerator.FileFilter;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.gui.GUIParam.ReSizeMode;
import org.lucterios.ui.GUIActionListener;
import org.lucterios.utils.LucteriosException;

public class DialogExample implements DialogVisitor {

	private GUIDialog mDialog;
	private GUIContainer mTab;
	
	public void execute(GUIDialog dialog) {
		mDialog=dialog;
		mDialog.setTextTitle("Example dialog");
		mTab=mDialog.getContainer().createContainer(ContainerType.CT_TAB, new GUIParam(0, 0));
		initScroll();	
		initGraph();	
		initComp();
		initBtn();				
	}
	
	ProgressPanel progress;
	TimeLabel time;
	public void closing() {
        progress.stop();
		time.stop();
	}	

	private void initScroll() {
		GUIContainer sv=mTab.addTab(ContainerType.CT_SCROLL,"Scroll", AbstractImage.Null);
		for(int index=0;index<10;index++) {
			GUILabel tv=sv.createLabel(new GUIParam(0, index,1, 1, ReSizeMode.RSM_NONE, FillMode.FM_NONE));
			tv.setTextString("Text"+(index+1));
			GUIEdit et=sv.createEdit(new GUIParam(1, index, 1, 1, ReSizeMode.RSM_HORIZONTAL, FillMode.FM_HORIZONTAL));
			et.setTextString("---");
		}
	}
	
	private class ActionLink implements GUIActionListener{
		String text;
		
		public ActionLink(String text){
			this.text=text;
		}
		public void actionPerformed() {
			mDialog.getGenerator().showMessageDialog(this.text, "Action Link");
		}
	}
	
	private void initGraph(){
		GUIContainer prg=mTab.addTab(ContainerType.CT_SCROLL,"Graph", AbstractImage.Null);
		GUIContainer pnl;

		pnl=prg.createContainer(ContainerType.CT_NORMAL,new GUIParam(0,0,1, 1, ReSizeMode.RSM_HORIZONTAL, FillMode.FM_HORIZONTAL));
		pnl.setSize(10,10);
		NavigatorBar nav=new NavigatorBar(pnl);
		nav.addLink("---", null);
		nav.addLink("AAA", new ActionLink("AAA"));
		nav.addLink("BBB", new ActionLink("BBB"));
		nav.addLink("CCC", new ActionLink("CCC"));

		pnl=prg.createContainer(ContainerType.CT_NORMAL,new GUIParam(0,1,1, 1, ReSizeMode.RSM_HORIZONTAL, FillMode.FM_HORIZONTAL,20,20));		
        progress= new ProgressPanel(true,pnl);
        progress.backgroudColor=pnl.getBackgroundColor();
        progress.MaxValue=10;
        progress.start();
        
		pnl=prg.createContainer(ContainerType.CT_NORMAL,new GUIParam(0,2,1, 1, ReSizeMode.RSM_HORIZONTAL, FillMode.FM_HORIZONTAL,25,25));		
		MemoryJauge jauge=new MemoryJauge(pnl);
		nav.addLink("Jauge", jauge);
		
		time=new TimeLabel(prg,new GUIParam(0,3,1, 1, ReSizeMode.RSM_NONE,FillMode.FM_HORIZONTAL));
		time.addActionListener(jauge);
		time.start();
		
		prg.createContainer(ContainerType.CT_NORMAL,new GUIParam(0,4));
	}

	private void initBtn() {
		GUIContainer btnPnl=mDialog.getContainer().createContainer(ContainerType.CT_NORMAL,new GUIParam(0,1,1,1, ReSizeMode.RSM_HORIZONTAL, FillMode.FM_HORIZONTAL));
		GUIButton bt1=btnPnl.createButton(new GUIParam(0, 0, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_NONE));
        bt1.setTextString("Open file");
        bt1.addActionListener(new GUIActionListener() {		
			public void actionPerformed() {
				openFile();
			}
		});
		GUIButton bt2=btnPnl.createButton(new GUIParam(1, 0, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_NONE));
        bt2.setTextString("Date selected");
        bt2.addActionListener(new GUIActionListener() {		
			public void actionPerformed() {
				dateSelected();
			}
		});
		GUIButton bt3=btnPnl.createButton(new GUIParam(2, 0, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_NONE));
        bt3.setTextString("Exception dlg");
        bt3.addActionListener(new GUIActionListener() {		
			public void actionPerformed() {
				exceptionDlg();
			}
		});
		GUIButton bt4=btnPnl.createButton(new GUIParam(3, 0, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_NONE));
        bt4.setTextString("Waiting window");
        bt4.addActionListener(new GUIActionListener() {		
			public void actionPerformed() {
				showWaitingWindow();
			}
		});
		GUIButton bt5=btnPnl.createButton(new GUIParam(4, 0, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_NONE));
        bt5.setTextString("Open form");
        bt5.addActionListener(new GUIActionListener() {		
			public void actionPerformed() {
				openForm();
			}
		});
        btnPnl.calculBtnSize(new GUIButton[]{bt1,bt2,bt3,bt4,bt5});
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
		try{
			GUIDialog dateDlg=mDialog.getGenerator().newDialog(mDialog,null);
			DatePickerSimple date_simple=new DatePickerSimple();
			dateDlg.setDialogVisitor(date_simple);
			dateDlg.setVisible(true);
			mDialog.getGenerator().showMessageDialog(date_simple.getSelectedDate().toString(),"Date selected");
		}catch (Exception e) {
			ExceptionDlg.throwException(e);
		}
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

	private void showWaitingWindow(){
		try{
			final GUIWindows wind=mDialog.getGenerator().newWindows();
			WaitingWindow ww=new WaitingWindow("Wait a moment.<br>5 sec...","Waiting example");
			wind.setWindowVisitor(ww);
			wind.setVisible(true);
			final Date date=new Date();		
			time.addActionListener(new GUIActionListener() {		
				public void actionPerformed() {
					try{
						Date new_date=new Date();
						if ((new_date.getTime()-date.getTime())>5000)
							wind.setVisible(false);
					}catch (Exception e) {
						ExceptionDlg.throwException(e);
					}
				}
			});
		}catch (Exception e) {
			ExceptionDlg.throwException(e);
		}
	}
	
	private void openForm() {
		mDialog.getContainer().invokeLater(new Runnable() {
			public void run() {
				try {
					GUIForm form=mDialog.getGenerator().newForm("example");
					form.setFormVisitor(new FormExample());
					form.setVisible(true);
				}catch (Exception e) {
					ExceptionDlg.throwException(e);
				}
			}
		});
	}

	private void initComp(){
		GUIContainer prg=mTab.addTab(ContainerType.CT_SCROLL,"Composants", AbstractImage.Null);

		GUILabel tv=prg.createLabel(new GUIParam(0, 0,1, 1, ReSizeMode.RSM_NONE, FillMode.FM_NONE));
		tv.setTextString("Séléction");
		
		GUICheckList checkList=prg.createCheckList(new GUIParam(1,0));
		checkList.clearList();
		checkList.setListData(new Object[]{"aaa","bbbb","ccc","dddd"});
		checkList.setMultiSelection(true);
		checkList.setSelectedIndices(new int[]{1,2});
		
		prg.createContainer(ContainerType.CT_NORMAL,new GUIParam(0,4));
	}
	
}
