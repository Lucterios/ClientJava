package org.lucterios.client.application.comp;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.lucterios.client.application.observer.ObserverAcknowledge;
import org.lucterios.client.presentation.FileDownload;
import org.lucterios.client.presentation.FileDownload.FileDownloadCallBack;
import org.lucterios.client.presentation.Observer.MapContext;
import org.lucterios.utils.DesktopTools;
import org.lucterios.utils.FileMonitoring;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;
import org.lucterios.utils.FileMonitoring.MonitoringCallback;
import org.lucterios.utils.graphic.ExceptionDlg;
import org.lucterios.utils.graphic.FilesFilter;
import org.lucterios.utils.graphic.HtmlLabel;
import org.lucterios.utils.graphic.ImagePreview;
import org.lucterios.utils.graphic.Tools;

public class CmpDownLoad extends CmpAbstractEvent implements FileDownloadCallBack,MonitoringCallback {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static File CurrentDirectory=null;
	
	private JPanel pnl_Btn;
	private HtmlLabel lbl_message;
	private JButton btn_open;
	private JButton btn_save;
	
	private String m_FileToDownload;
	private String m_FileName;
	private File m_LocalFile; 
	private FileMonitoring m_file_monitoring=null;
	private boolean m_isCompress=false;
	private boolean m_isHttpFile=false;
	private int m_maxsize=1048576;

	public CmpDownLoad() {
		super();
		if (CurrentDirectory==null) {
			String homeDir = System.getProperty("user.home");
			CurrentDirectory = new java.io.File(homeDir);
			if (new java.io.File(homeDir+"/Desktop").exists())
				CurrentDirectory = new java.io.File(homeDir+"/Desktop");
			if (new java.io.File(homeDir+"/Bureau").exists())
				CurrentDirectory = new java.io.File(homeDir+"/Bureau");
		}
	}

	private void closeMonitoring(){
		if (m_file_monitoring!=null)
			m_file_monitoring.stop();
		m_file_monitoring=null;
	}
	
	public void close(){
		closeMonitoring();
		super.close();
	}
	
	protected boolean hasChanged() {
		return false;
	}

	public MapContext getRequete(String aActionIdent) {
		MapContext tree_map = new MapContext();
		return tree_map;
	}

	protected void initComponent() {
		java.awt.GridBagConstraints gridBagConstraints;
		pnl_Btn = new JPanel();
		pnl_Btn.setName("pnl_Btn");
		pnl_Btn.setOpaque(this.isOpaque());
		pnl_Btn.setLayout(new GridBagLayout());
		add(pnl_Btn, java.awt.BorderLayout.CENTER);

		lbl_message = new HtmlLabel();
		lbl_message.setEditable(false);
		lbl_message.setAlignmentY(0.5f);
		lbl_message.setOpaque(false);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		pnl_Btn.add(lbl_message, gridBagConstraints);

		btn_save = new JButton();
		btn_save.setText("Sauver sous...");
		btn_save.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				saveExtractedFile();
			}});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 0;
		gridBagConstraints.insets= new Insets(5,10,2,10);
		pnl_Btn.add(btn_save, gridBagConstraints);

		btn_open = new JButton();
		btn_open.setText("Ouvrir...");
		btn_open.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				openExtractedFile();
			}});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 0;
		gridBagConstraints.insets= new Insets(5,10,2,10);
		pnl_Btn.add(btn_open, gridBagConstraints);
		JButton[] btns=new JButton[]{btn_save,btn_open};
		Tools.calculBtnSize(btns);
	}
	
	protected void refreshComponent() throws LucteriosException {
		super.refreshComponent();
		m_LocalFile=null;
		btn_open.setEnabled(false);
		btn_save.setEnabled(false); 
		m_isCompress=(getXmlItem().getAttributInt("Compress",0)!=0); 
		m_isHttpFile=(getXmlItem().getAttributInt("HttpFile",0)!=0);
		m_maxsize=getXmlItem().getAttributInt("maxsize",1048576);;
		m_FileName=getXmlItem().getText();
		String message=m_FileName;
		if (mEventAction==null)
			message+="(en lecture-seul)";
		lbl_message.setText("<CENTER>"+message+"</CENTER>");
		SimpleParsing filename=getXmlItem().getFirstSubTag("FILENAME");
		if (filename!=null)
			m_FileToDownload=filename.getText();
		else
			m_FileToDownload="";
		FileDownload download=new FileDownload();
		download.extractFile(m_FileName,m_FileToDownload,m_isCompress,this);
	}

	protected void openExtractedFile() {
		btn_open.setEnabled(mEventAction==null); 
	    if (m_LocalFile.exists()) {
			try {
				if (mEventAction!=null) {				
					closeMonitoring();
					m_file_monitoring=new FileMonitoring(m_LocalFile,this);
				}
				DesktopTools.instance().launch(m_LocalFile.toURI().toURL().toString());
			} catch (LucteriosException e1) 
			{
				ExceptionDlg.throwException(e1);
			} catch (MalformedURLException e) {
				ExceptionDlg.throwException(e);
			}	    	
	    }
	}
	
	protected void saveExtractedFile() {
		JFileChooser file_dlg;
		file_dlg = new JFileChooser(CurrentDirectory);
		java.io.File file_name = new java.io.File(m_FileName);
		file_dlg.setSelectedFile(file_name);
		file_dlg.setAccessory(new ImagePreview(file_dlg));
		file_dlg.setFileFilter(new FilesFilter(new String[]{".*"},"Fichier à sauver"));
		int returnVal;
		if (this.getObsCustom().getGUIDialog() != null)
			returnVal = file_dlg.showSaveDialog((Component)this.getObsCustom().getGUIDialog());
		else
			returnVal = file_dlg.showSaveDialog((Component)this.getObsCustom().getGUIFrame());
	    if (m_LocalFile.exists() && (returnVal == JFileChooser.APPROVE_OPTION)) {
	    	java.io.File file_exp = file_dlg.getSelectedFile();
	    	CurrentDirectory=file_exp.getParentFile();
	    	try {
				org.lucterios.utils.Tools.copyFile(m_LocalFile,file_exp);
			} catch (LucteriosException e) {
				ExceptionDlg.throwException(e);
			}
	    }
	}

	public void failure(String aMessage) {
		lbl_message.setText("<CENTER><font color='red'>Erreur<br>"+m_FileName+": "+aMessage+" </font><CENTER>");
	}

	public void success(String aLocalFile) {
		m_LocalFile=new File(aLocalFile);
		if (mEventAction==null)
			m_LocalFile.setReadOnly(); 
		btn_open.setEnabled(m_LocalFile.exists());
		btn_save.setEnabled(m_LocalFile.exists());
	}

	public void fileModified() {
		if (JOptionPane.showConfirmDialog(this,"Le fichier a été modifié.\nVoulez vous le ré-injecter dans l'application?","Extraction", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
			mEventAction.setOwner(new ObserverAcknowledge(){
				public MapContext getParameters(String aActionId, int aSelect, boolean aCheckNull) throws LucteriosException {
					MapContext requete = new MapContext();
					requete.putAll(getObsCustom().getContext());
					requete.put(getName(), CmpUpload.getFileContentBase64(m_LocalFile,m_isCompress,m_isHttpFile,m_maxsize));
					if (m_isCompress) 
						requete.put(getName()+CmpUpload.SUFFIX_FILE_NAME,m_LocalFile.getName());
					return requete;
				}
			});
			mEventAction.actionPerformed(null);
		}
	}

}
