package org.lucterios.client.application;

import java.awt.Image;
import java.net.URLEncoder;

import javax.swing.ImageIcon;

import org.lucterios.client.Constants;
import org.lucterios.client.presentation.Singletons;
import org.lucterios.utils.DesktopTools;
import org.lucterios.utils.Tools;
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
		
	public String getHTML(String mComplement) {
		String resValue="";
		if ((mComplement!=null) && (mComplement.length()>0))
			resValue+=mComplement + "<br>";
		resValue+="<hr><center><h1>" + mTitle + "</h1></center>\n"
		+ "<table width='100%'>"
		+ "<tr><td><center>Version</center></td><td><center>"+ mApplisVersion + "</center></td></tr>"
		+ "<tr><td><center>Serveur</center></td><td><center>" + mServerVersion + "</center></td></tr>"
		+ "<tr><td><center>Client JAVA</center></td><td><center>" + Constants.Version() + "</center></td></tr>"
		+ "<tr><td colspan='2'><font size='-1'><i>" + mCopyRigth + "</i></font></td></tr>"
		+ "<tr><td colspan='2'>Connexion : " + mLogin + "</td></tr>"
		+ "</table>"
		+ "<hr>";
		if ((mInfoServer!=null) && (mInfoServer.length()>0))
			resValue+= "<br>"+Tools.convertLuctoriosFormatToHtml(mInfoServer) + "<br><br>";
    	Runtime rt = Runtime.getRuntime();
		long memory_maxHeap         = rt.maxMemory();
		long currentHeapSize 		= rt.totalMemory();
		long memory_used            = currentHeapSize-rt.freeMemory();
		resValue+="<hr>"
		+ "<table width='100%'>"
        + "<tr><td>Memoire utilisé</td><td>" + memory_used/(1024*1024) + " Mo</td></tr>"
        + "<tr><td>Memoire max</td><td>" + memory_maxHeap/(1024*1024) + " Mo</td></tr>"
        + "<tr><td>Version java</td><td>" + System.getProperty("java.specification.version") + "</td></tr>"
        + "<tr><td>Diffuseur java</td><td>" + System.getProperty("java.specification.vendor") + "</td></tr>"
        + "<tr><td>URL diffuseur java</td><td>" + System.getProperty("java.vendor.url") + "</td></tr>"
        + "<tr><td>Version classe java</td><td>" + System.getProperty("java.class.version") + "</td></tr>"
        + "<tr><td>Système d'exploitation</td><td>" + System.getProperty("os.name") + "</td></tr>"
        + "<tr><td>Architecture</td><td>" + System.getProperty("os.arch") + "</td></tr>"
        + "<tr><td>Version OS</td><td>" + System.getProperty("os.version") + "</td></tr>"
        + "<tr><td>Utilisateur</td><td>" + System.getProperty("user.name") + "</td></tr>"
        + "<tr><td>Répertoire utilisateur</td><td>" + System.getProperty("user.home") + "</td></tr>"
        + "<tr><td>Répertoire courant</td><td>" + System.getProperty("user.dir") + "</td></tr>"
		+ "</table>";
		return resValue;
	}
	
	public void sendSupport(String aTitle,String aComplement) {
		try {
			String url="mailto:"+mSupportEmail;
			url+="?subject="+aTitle;
			String body=URLEncoder.encode(getHTML(aComplement),"UTF-8");
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
