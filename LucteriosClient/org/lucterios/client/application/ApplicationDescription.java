package org.lucterios.client.application;

import java.awt.Image;
import java.net.URLEncoder;

import javax.swing.ImageIcon;

import org.lucterios.client.Constants;
import org.lucterios.client.presentation.Singletons;
import org.lucterios.utils.DesktopTools;
import org.lucterios.utils.graphic.ExceptionDlg;
import org.lucterios.utils.graphic.ExceptionDlg.InfoDescription;

public class ApplicationDescription implements InfoDescription {

	private String mTitle;
	private String mApplisVersion;
	private String mServerVersion;
	private String mCopyRigth;
	private String mLogoIconName;
	private Image mLogoIcon;
	private String mLogin;
	private String mInfoServer;
	private String mSupportEmail;
	
	public ApplicationDescription(String aTitle, String aCopyRigth, String aLogoName,
			String aAppliVersion, String aServerVersion) {
		mTitle=aTitle;
		mApplisVersion=aAppliVersion;
		mServerVersion=aServerVersion;
		mCopyRigth=aCopyRigth;
		setLogoIconName(aLogoName);
		ExceptionDlg.mInfoDescription=this;
	}

	private void setLogoIconName(String aLogoIconName) {
		this.mLogoIconName = aLogoIconName;
		ImageIcon img=Singletons.Transport().getIcon(mLogoIconName);
		if (img!=null) 
			mLogoIcon=img.getImage();
		else
			mLogoIcon=null;
	}

	public String getTitle() {
		return mTitle;
	}

	public String getLogoIconName() {
		return mLogoIconName;
	}

	public String getApplisVersion() {
		return mApplisVersion;
	}

	public String getServerVersion() {
		return mServerVersion;
	}

	public String getCopyRigth() {
		return mCopyRigth;
	}

	public Image getLogoImage() {
		return mLogoIcon;
	}

	public ImageIcon getLogoIcon() {
		return new ImageIcon(mLogoIcon);
	}

	public void setlogin(String aLogin) {
		mLogin=aLogin;
	}

	public void setInfoServer(String aInfoServer) {
		mInfoServer=aInfoServer;
	}

	public void setSupportEmail(String aSupportEmail) {
		mSupportEmail=aSupportEmail;
	}
		
	public String getHTML() {
		String resValue="<center><h1>" + mTitle + "</h1></center>\n"
		+ "<table width='100%'>"
		+ "<tr><td><center>Version</center></td><td><center>"+ mApplisVersion + "</center></td></tr>"
		+ "<tr><td colspan='2'><font size='-1'><center><i>" + mCopyRigth + "</i></center></font></td></tr>"
		+ "<tr><td><center>Serveur</td><td><center>" + mServerVersion + "</center></td></tr>"
		+ "<tr><td><center>Client JAVA</td><td><center>" + Constants.Version() + "</center></td></tr>"
		+ "<tr><td colspan='2'>Connexion : " + mLogin + "</td></tr>"
		+ "</table>";
		return resValue;
	}

	public String getText(String mComplement) {
		String resValue="\n\n-----------------------------------------\n"
		+ mComplement + "\n"
		+ "-----------------------------------------\n"
		+ mTitle + "\n\n"
		+ "Version = "+ mApplisVersion + "\n"
		+ mCopyRigth + "\n"
		+ "Serveur = " + mServerVersion + "\n"
		+ "Client JAVA = " + Constants.Version() + "\n"
		+ "Connexion = " + mLogin + "\n"
		+ "-----------------------------------------\n"
		+ mInfoServer + "\n"
		+ "-----------------------------------------\n"
        +"java.specification.version = " + System.getProperty("java.specification.version") + "\n"
        +"java.specification.vendor = " + System.getProperty("java.specification.vendor") + "\n"
        +"java.specification.name = " + System.getProperty("java.specification.name") + "\n"
        +"java.vendor.url = " + System.getProperty("java.vendor.url") + "\n"
        +"java.class.version = " + System.getProperty("java.class.version") + "\n"
        +"os.home = " + System.getProperty("os.name") + "\n"
        +"os.arch = " + System.getProperty("os.arch") + "\n"
        +"os.version = " + System.getProperty("os.version") + "\n"
        +"user.name = " + System.getProperty("user.name") + "\n"
        +"user.home = " + System.getProperty("user.home") + "\n"
        +"user.dir = " + System.getProperty("user.dir") + "\n";
		return resValue;
	}
	
	public void sendSupport(String mComplement) {
		try {
			String url="mailto:"+mSupportEmail;
			url+="?subject=demande de support";
			String body=URLEncoder.encode(getText(mComplement),"UTF-8");
			url+="&body="+body.replace("+"," ");
			DesktopTools.instance().launch(url);
		} catch (Exception e) {
			ExceptionDlg.throwException(e);
		}
	}

	public String getInfoServer() {
		return mInfoServer;
	}

	public String getSupportEmail() {
		return mSupportEmail;
	}

}
