package org.lucterios.engine.application.comp;

import java.io.File;
import java.net.MalformedURLException;

import org.lucterios.engine.application.observer.ObserverAcknowledge;
import org.lucterios.engine.utils.FileMonitoring;
import org.lucterios.engine.utils.FileMonitoring.MonitoringCallback;
import org.lucterios.engine.presentation.FileDownload;
import org.lucterios.engine.presentation.Singletons;
import org.lucterios.engine.presentation.FileDownload.FileDownloadCallBack;
import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.ui.GUIActionListener;
import org.lucterios.utils.DesktopInterface;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;
import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.graphic.FilesFilter;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUIHyperText;
import org.lucterios.gui.GUIParam;

public class CmpDownLoad extends CmpAbstractEvent implements
		FileDownloadCallBack, MonitoringCallback {

	private static File CurrentDirectory = null;

	private GUIHyperText lbl_message;
	private GUIButton btn_open;
	private GUIButton btn_save;

	private String m_FileToDownload;
	private String m_FileName;
	private File m_LocalFile;
	private FileMonitoring m_file_monitoring = null;
	private boolean m_isCompress = false;
	private boolean m_isHttpFile = false;
	private int m_maxsize = 1048576;

	public CmpDownLoad() {
		super();
		if (CurrentDirectory == null) {
			String homeDir = System.getProperty("user.home");
			CurrentDirectory = new java.io.File(homeDir);
			if (new java.io.File(homeDir + "/Desktop").exists())
				CurrentDirectory = new java.io.File(homeDir + "/Desktop");
			if (new java.io.File(homeDir + "/Bureau").exists())
				CurrentDirectory = new java.io.File(homeDir + "/Bureau");
		}
	}

	private void closeMonitoring() {
		if (m_file_monitoring != null)
			m_file_monitoring.stop();
		m_file_monitoring = null;
	}

	public void close() {
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
		lbl_message = mPanel.createHyperText(new GUIParam(0,0));

		mParam.setX(1);
		mParam.setPad(3);
		btn_save = mPanel.createButton(mParam);
		btn_save.setTextString("Sauver sous...");
		btn_save.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				saveExtractedFile();
			}
		});
		mParam.setX(2);
		mParam.setPad(3);
		btn_open = mPanel.createButton(mParam);
		btn_open.setTextString("Ouvrir...");
		btn_open.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				openExtractedFile();
			}
		});
		GUIButton[] btns = new GUIButton[] { btn_save, btn_open };
		mPanel.calculBtnSize(btns);
	}

	protected void refreshComponent() {
		super.refreshComponent();
		m_LocalFile = null;
		btn_open.setEnabled(false);
		btn_save.setEnabled(false);
		m_isCompress = (getXmlItem().getAttributeInt("Compress", 0) != 0);
		m_isHttpFile = (getXmlItem().getAttributeInt("HttpFile", 0) != 0);
		m_maxsize = getXmlItem().getAttributeInt("maxsize", 1048576);
		;
		m_FileName = getXmlItem().getText();
		String message = m_FileName;
		if (mEventAction == null)
			message += "(en lecture-seul)";
		lbl_message.setTextString("<CENTER>" + message + "</CENTER>");
		SimpleParsing filename = getXmlItem().getFirstSubTag("FILENAME");
		if (filename != null)
			m_FileToDownload = filename.getText();
		else
			m_FileToDownload = "";
		FileDownload download = new FileDownload();
		download.extractFile(m_FileName, m_FileToDownload, m_isCompress, this);
	}

	protected void openExtractedFile() {
		btn_open.setEnabled(mEventAction == null);
		if (m_LocalFile.exists()) {
			try {
				if (mEventAction != null) {
					closeMonitoring();
					m_file_monitoring = new FileMonitoring(m_LocalFile, this);
				}
				DesktopInterface.getInstance().launch(
						m_LocalFile.toURI().toURL().toString());
			} catch (LucteriosException e1) {
				ExceptionDlg.throwException(e1);
			} catch (MalformedURLException e) {
				ExceptionDlg.throwException(e);
			}
		}
	}

	protected void saveExtractedFile() {
		java.io.File file_exp = Singletons.getWindowGenerator().selectSaveFileDialog(new FilesFilter(new String[] { ".*" },"Fichier à sauver"),
				this.getObsCustom().getGUIObject(),m_FileName);
		if (file_exp!=null) {
			CurrentDirectory = file_exp.getParentFile();
			try {
				org.lucterios.utils.Tools.copyFile(m_LocalFile, file_exp);
			} catch (LucteriosException e) {
				ExceptionDlg.throwException(e);
			}
		}
	}

	public void failure(String aMessage) {
		lbl_message.setTextString("<CENTER><font color='red'>Erreur<br>" + m_FileName
				+ ": " + aMessage + " </font><CENTER>");
	}

	public void success(String aLocalFile) {
		m_LocalFile = new File(aLocalFile);
		if (mEventAction == null)
			m_LocalFile.setReadOnly();
		btn_open.setEnabled(m_LocalFile.exists());
		btn_save.setEnabled(m_LocalFile.exists());
	}

	public void fileModified() {
		if (Singletons.getWindowGenerator().showConfirmDialog("Le fichier a été modifié.\nVoulez vous le ré-injecter dans l'application?",
				"Extraction")) {
			mEventAction.setOwner(new ObserverAcknowledge() {
				public MapContext getParameters(String aActionId, int aSelect,
						boolean aCheckNull) throws LucteriosException {
					MapContext requete = new MapContext();
					requete.putAll(getObsCustom().getContext());
					requete
							.put(getName(), CmpUpload.getFileContentBase64(
									m_LocalFile, m_isCompress, m_isHttpFile,
									m_maxsize));
					if (m_isCompress)
						requete.put(getName() + CmpUpload.SUFFIX_FILE_NAME,
								m_LocalFile.getName());
					return requete;
				}
			});
			mEventAction.actionPerformed();
		}
	}

}
