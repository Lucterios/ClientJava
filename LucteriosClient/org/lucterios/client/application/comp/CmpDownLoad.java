package org.lucterios.client.application.comp;

import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.lucterios.client.application.observer.ObserverAcknowledge;
import org.lucterios.client.presentation.FileDownload;
import org.lucterios.client.presentation.FileDownload.FileDownloadCallBack;
import org.lucterios.utils.DesktopTools;
import org.lucterios.utils.FileMonitoring;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;
import org.lucterios.utils.FileMonitoring.MonitoringCallback;
import org.lucterios.utils.graphic.ExceptionDlg;
import org.lucterios.utils.graphic.FilesFilter;
import org.lucterios.utils.graphic.ImagePreview;
import org.lucterios.utils.graphic.Tools;

public class CmpDownLoad extends CmpAbstractEvent implements FileDownloadCallBack,MonitoringCallback {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static File CurrentDirectory=null;
	
	private JPanel pnl_Btn;
	private JEditorPane lbl_message;
	private JButton btn_open;
	private JButton btn_save;
	
	private String m_FileToDownload;
	private String m_FileName;
	private File m_LocalFile; 
	private FileMonitoring m_file_monitoring=null;
	private boolean m_isCompress=false;
	private boolean m_isHttpFile=false;

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

	public void close(){
		if (m_file_monitoring!=null)
			m_file_monitoring.stop();
		super.close();
	}
	
	protected boolean hasChanged() {
		return false;
	}

	public Map getRequete(String aActionIdent) {
		TreeMap tree_map = new TreeMap();
		return tree_map;
	}

	protected void initComponent() {
		java.awt.GridBagConstraints gridBagConstraints;
		pnl_Btn = new JPanel();
		pnl_Btn.setName("pnl_Btn");
		pnl_Btn.setOpaque(this.isOpaque());
		pnl_Btn.setLayout(new GridBagLayout());
		add(pnl_Btn, java.awt.BorderLayout.CENTER);

		lbl_message = new JEditorPane();
		lbl_message.setEditable(false);
		lbl_message.setContentType("text/html");
		lbl_message.setAlignmentY(0.5f);
		lbl_message.setOpaque(false);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 2;
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
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
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
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
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
		m_isCompress=(mXmlItem.getAttributInt("Compress",0)!=0); 
		m_isHttpFile=(mXmlItem.getAttributInt("HttpFile",0)!=0);
		m_FileName=mXmlItem.getText();
		String message=m_FileName;
		if (mEventAction==null)
			message+="(en lecture-seul)";
		lbl_message.setText("<CENTER>"+message+"</CENTER>");
		SimpleParsing filename=mXmlItem.getFirstSubTag("FILENAME");
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
				if (mEventAction!=null)
					m_file_monitoring=new FileMonitoring(m_LocalFile,this);
				DesktopTools.instance().launch(m_LocalFile.toURI().toString());
			} catch (LucteriosException e1) 
			{
				ExceptionDlg.throwException(e1);
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
		if (this.mObsCustom.getGUIDialog() != null)
			returnVal = file_dlg.showSaveDialog(this.mObsCustom.getGUIDialog());
		else
			returnVal = file_dlg.showSaveDialog(this.mObsCustom.getGUIFrame());
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
		lbl_message.setText("<CENTER><font color='red'>Erreur (fichier "+m_FileName+"): "+aMessage+" </font><CENTER>");
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
				public Map getParameters(String aActionId, int aSelect, boolean aCheckNull) {
					Map requete = new TreeMap();
					requete.putAll(mObsCustom.getContext());
					requete.put(getName(), CmpUpload.getFileContentBase64(m_LocalFile,m_isCompress,m_isHttpFile));
					return requete;
				}
			});
			mEventAction.actionPerformed(null);
		}
	}

}
