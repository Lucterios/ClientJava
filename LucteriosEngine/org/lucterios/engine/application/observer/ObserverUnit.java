package org.lucterios.engine.application.observer;

import java.io.File;

import org.lucterios.engine.application.ActionImpl;
import org.lucterios.engine.application.ApplicationDescription;
import org.lucterios.engine.application.Connection;
import org.lucterios.engine.presentation.ObserverFactoryMock;
import org.lucterios.engine.presentation.Singletons;
import org.lucterios.engine.transport.HttpTransportStub;
import org.lucterios.engine.utils.LucteriosConfiguration;
import org.lucterios.mock.MockGenerator;
import org.lucterios.utils.DesktopBasic;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;

import junit.framework.TestCase;

public class ObserverUnit extends TestCase implements Connection {

	private SimpleParsing mParse;
	
	private ApplicationDescription mDescription;
	private String mSubTitle;
	private String mLogin; 
	private String mRealName; 
	private boolean mRefreshMenu;

	public void setValue(ApplicationDescription aDescription, String aSubTitle,
			String aLogin, String aRealName, boolean refreshMenu) {
		mDescription=aDescription;
		mSubTitle=aSubTitle;
		mLogin=aLogin; 
		mRealName=aRealName; 
		mRefreshMenu=refreshMenu;
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Singletons.setHttpTransportClass(HttpTransportStub.class);
		Singletons.setWindowGenerator(new MockGenerator());
		Singletons.setDesktop(new DesktopBasic());
		Singletons.setActionClass(ActionImpl.class);
		Singletons.initalize(new LucteriosConfiguration(new File(".")), new ObserverFactoryMock());
		Singletons.Transport().setSession("abc123");
		mParse = new SimpleParsing();
		ObserverAuthentification.mConnection=this;
		setValue(null,"","","",false);
	}

	@Override
	protected void tearDown() throws Exception {
		ObserverAuthentification.mConnection=null;
		super.tearDown();
	}

	public void testAuthentification() throws LucteriosException {
		String xml_receive="<REPONSE><CONNECTION>" +
				"<TITLE>Lucterios standard</TITLE><SUBTITLE>Application générique de gestion</SUBTITLE>" +
				"<VERSION>1.4.2.5</VERSION><SERVERVERSION>1.2.5.606</SERVERVERSION>" +
				"<COPYRIGHT>General Public Licence - http://www.lucterios.org</COPYRIGHT>" +
				"<LOGONAME>http://demo.lucterios.org/Lucterios/extensions/applis/images/logo.gif</LOGONAME>" +
				"<LOGIN>admin</LOGIN><REALNAME>Administrateur</REALNAME>" +
				"<SUPPORT_EMAIL><![CDATA[Support Lucterios <support@lucterios.org>]]></SUPPORT_EMAIL><INFO_SERVER><![CDATA[{[italic]}Apache/2.2.14 (Ubuntu){[/italic]}]]></INFO_SERVER>" +
				"</CONNECTION>" +
				"<PARAM name='ses' type='str'>admin1315821586</PARAM>" +
				"<![CDATA[OK]]></REPONSE>";
		
		mParse.parse(xml_receive,true);
		ObserverAuthentification obs=new ObserverAuthentification();
		obs.setSource("CORE", "Auth");
		obs.setContent(mParse);
		obs.show("Mon titre");

		assertEquals("Title","Lucterios standard",mDescription.getTitle());
		assertEquals("ApplisVersion","1.4.2.5",mDescription.getApplisVersion());
		assertEquals("ServerVersion","1.2.5.606",mDescription.getServerVersion());
		assertEquals("CopyRigth","General Public Licence - http://www.lucterios.org",mDescription.getCopyRigth());
		assertEquals("LogoIconName","http://demo.lucterios.org/Lucterios/extensions/applis/images/logo.gif",mDescription.getLogoIconName());
		assertEquals("InfoServer","{[italic]}Apache/2.2.14 (Ubuntu){[/italic]}",mDescription.getInfoServer());
		assertEquals("SupportEmail","Support Lucterios <support@lucterios.org>",mDescription.getSupportEmail());
				
		assertEquals("SubTitle","Application générique de gestion",mSubTitle);
		assertEquals("Login","admin",mLogin); 
		assertEquals("RealName","Administrateur",mRealName); 
		assertEquals("RefreshMenu",true,mRefreshMenu);
		assertEquals("RefreshMenu obs",false,obs.getRefreshMenu());
	}

	public void testAcknowledge() throws LucteriosException {
		String xml_receive="<REPONSE>" +
				"<TITLE><![CDATA[Valider]]></TITLE>" +
				"<CONTEXT><PARAM name='SuperTableTest'><![CDATA[101]]></PARAM></CONTEXT>" +
				"<ACTION extension='TestValidation' action='SuperTableTest_APAS_Fiche' close='1' modal='1'><![CDATA[editer]]></ACTION>" +
				"<CLOSE_ACTION><ACTION extension='CORE' action='UNLOCK' close='1' modal='1' unique='1'><![CDATA[unlock]]></ACTION></CLOSE_ACTION>"+
				"</REPONSE>";

		mParse.parse(xml_receive,true);
		ObserverAcknowledge obs=new ObserverAcknowledge();
		obs.setSource("CORE", "validerAct");
		obs.setContent(mParse);
		obs.show("Mon titre");
		
		assertEquals("Context size",1,obs.getContext().size());
		assertEquals("Context 1","101",obs.getContext().get("SuperTableTest"));

		assertNotNull("RedirectAction", obs.getRedirectAction());
		assertEquals("RedirectAction-Extension","TestValidation",obs.getRedirectAction().getExtension());
		assertEquals("RedirectAction-Action","SuperTableTest_APAS_Fiche",obs.getRedirectAction().getAction());
		
		
	}
}
