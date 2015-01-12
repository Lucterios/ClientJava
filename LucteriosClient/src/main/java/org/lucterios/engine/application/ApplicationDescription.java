package org.lucterios.engine.application;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.lucterios.engine.presentation.Singletons;
import org.lucterios.utils.Tools;
import org.lucterios.utils.Tools.InfoDescription;
import org.lucterios.gui.AbstractImage;

public class ApplicationDescription implements InfoDescription {

	private String mTitle;
	private String mApplisVersion;
	private String mServerVersion;
	private String mCopyRigth;
	private String mLogoIconName;
	private AbstractImage mLogoIcon;
	private String mLogin;
	private String mInfoServer;
	private String mSupportEmail;
	
	public static String gVersion="x.x.x.x";

	public ApplicationDescription(String aTitle, String aCopyRigth,
			String aLogoName, String aAppliVersion, String aServerVersion) {
		mTitle = aTitle;
		mApplisVersion = aAppliVersion;
		mServerVersion = aServerVersion;
		mCopyRigth = aCopyRigth;
		setLogoIconName(aLogoName);
		Singletons.getDesktop().setInfoDescription(this);
	}

	private void setLogoIconName(String aLogoIconName) {
		this.mLogoIconName = aLogoIconName;
		mLogoIcon = Singletons.Transport().getIcon(mLogoIconName, 0);
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

	public AbstractImage getLogoImage() {
		return mLogoIcon;
	}

	public Object getLogo() {
		if (mLogoIcon != null)
			return mLogoIcon.getData();
		else
			return null;
	}

	public void setlogin(String aLogin) {
		mLogin = aLogin;
	}

	public void setInfoServer(String aInfoServer) {
		mInfoServer = aInfoServer;
	}

	public void setSupportEmail(String aSupportEmail) {
		mSupportEmail = aSupportEmail;
	}

	public String getHTML(String mComplement) {
		String resValue = "";
		if ((mComplement != null) && (mComplement.length() > 0))
			resValue += mComplement + "<br>";
		resValue += "<hr><center><h1>" + mTitle + "</h1></center>"
				+ "<table width='100%'>"
				+ "<tr><td><center>Version</center></td><td><center>"
				+ mApplisVersion + "</center></td></tr>"
				+ "<tr><td><center>Serveur</center></td><td><center>"
				+ mServerVersion + "</center></td></tr>"
				+ "<tr><td><center>Client JAVA</center></td><td><center>"
				+ gVersion + "</center></td></tr>"
				+ "<tr><td colspan='2'><font size='-1'><i>" + mCopyRigth
				+ "</i></font></td></tr>" + "<tr><td colspan='2'>Connexion : "
				+ mLogin + "</td></tr>" + "</table>" + "<hr>";
		if ((mInfoServer != null) && (mInfoServer.length() > 0))
			resValue += Tools.convertLuctoriosFormatToHtml(mInfoServer)+ "<br>";
		Runtime rt = Runtime.getRuntime();
		long memory_maxHeap = rt.maxMemory();
		long currentHeapSize = rt.totalMemory();
		long memory_used = currentHeapSize - rt.freeMemory();
		resValue += "<hr>" + "<table width='100%'>"
				+ "<tr><td>Memoire utilisé</td><td>"
				+ memory_used / (1024 * 1024)
				+ " Mo</td></tr>"
				+ "<tr><td>Memoire max</td><td>"
				+ memory_maxHeap / (1024 * 1024)
				+ " Mo</td></tr>"
				+ "<tr><td>Version java</td><td>"
				+ System.getProperty("java.specification.version")
				+ "</td></tr>"
				+ "<tr><td>Diffuseur java</td><td>"
				+ System.getProperty("java.specification.vendor")
				+ "</td></tr>"
				+ "<tr><td>URL diffuseur java</td><td>"
				+ System.getProperty("java.vendor.url")
				+ "</td></tr>"
				+ "<tr><td>Version classe java</td><td>"
				+ System.getProperty("java.class.version")
				+ "</td></tr>"
				+ "<tr><td>Système d'exploitation</td><td>"
				+ System.getProperty("os.name")
				+ "</td></tr>"
				+ "<tr><td>Architecture</td><td>"
				+ System.getProperty("os.arch")
				+ "</td></tr>"
				+ "<tr><td>Version OS</td><td>"
				+ System.getProperty("os.version")
				+ "</td></tr>"
				+ "<tr><td>Utilisateur</td><td>"
				+ System.getProperty("user.name")
				+ "</td></tr>"
				+ "<tr><td>Répertoire utilisateur</td><td>"
				+ System.getProperty("user.home")
				+ "</td></tr>"
				+ "<tr><td>Répertoire courant</td><td>"
				+ System.getProperty("user.dir") + "</td></tr>" + "</table>";
		return resValue;
	}

	private String getText(String aComplement) throws UnsupportedEncodingException {
			String text_html=getHTML(aComplement);
			int pos1=1;
			int pos2=1;
			while ((pos1>0) && (pos2>0)) { 
				pos1=text_html.indexOf("<style type=\"text/css\">");
				pos2=text_html.indexOf("</style>");
				if ((pos1>0) && (pos2>0)) {
					text_html=text_html.substring(0,pos1)+text_html.substring(pos2+10);
				}
			}
			text_html=text_html.replaceAll("(<center>|</center>|<table width='100%'>|</table>|<tr><td(| colspan='2')>|<font size='-1'>|</font>|<i>|</i>)", "");
			text_html=text_html.replaceAll("<h1>\\s*", "#### ");
			text_html=text_html.replaceAll("[<br>|\\s|\n]*</h1>", " ####\n");
			text_html=text_html.replaceAll("<b>","[");
			text_html=text_html.replaceAll("</b>","]");
			text_html=text_html.replaceAll("&#60;","<");
			text_html=text_html.replaceAll("&#61;","=");
			text_html=text_html.replaceAll("&#62;",">");
			text_html=text_html.replaceAll("&#95;","_");
			text_html=text_html.replaceAll("&#39;","'");
			text_html=text_html.replaceAll("&#32;"," ");
			text_html=text_html.replaceAll("&#33;","!");
			text_html=text_html.replaceAll("&#91;","[");
			text_html=text_html.replaceAll("&#93;","]");
			text_html=text_html.replaceAll("&#47;","/");
			text_html=text_html.replaceAll("(</td></tr>|<br>)","\n");
			text_html=Tools.replace(text_html,"</td><td>"," : ");
			text_html=text_html.replaceAll("(<hr>|<hr/>)","__________________________________________\n");
			return URLEncoder.encode(text_html, "UTF-8");
	}
	
	public void sendSupport(String aTitle, String aComplement) {
		try {
			String url = "mailto:" + mSupportEmail;
			url += "?subject=" + aTitle;
			String body = getText(aComplement);
			url += "&body=" + body.replace("+", " ");
			Singletons.getDesktop().launch(url);
		} catch (Exception e) {
			Singletons.getDesktop().throwException(e);
		}
	}

	public String getInfoServer() {
		return mInfoServer;
	}

	public String getSupportEmail() {
		return mSupportEmail;
	}

	public String getLogin() {
		return mLogin;
	}

}
