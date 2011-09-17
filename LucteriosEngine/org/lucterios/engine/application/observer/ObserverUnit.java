package org.lucterios.engine.application.observer;

import java.io.File;

import org.lucterios.engine.application.ActionImpl;
import org.lucterios.engine.application.ApplicationDescription;
import org.lucterios.engine.application.Connection;
import org.lucterios.engine.presentation.ObserverFactoryMock;
import org.lucterios.engine.presentation.Singletons;
import org.lucterios.engine.transport.HttpTransportStub;
import org.lucterios.engine.utils.LucteriosConfiguration;
import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.mock.MockButton;
import org.lucterios.mock.MockContainer;
import org.lucterios.mock.MockGenerator;
import org.lucterios.mock.MockHyperText;
import org.lucterios.mock.MockImage;
import org.lucterios.mock.MockMemo;
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
	
	private ObserverFactoryMock mObsFactory;

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
		mObsFactory=new ObserverFactoryMock();
		ObserverFactoryMock.NewObserver=new ObserverAcknowledge();
		Singletons.initalize(new LucteriosConfiguration(new File(".")),mObsFactory);
		Singletons.Transport().setSession("abc123");
		mParse = new SimpleParsing();
		ObserverAuthentification.mConnection=this;
		setValue(null,"","","",false);
		ExceptionDlg.mGenerator=Singletons.getWindowGenerator();
		ExceptionDlg.mLastException=null;
		ExceptionDlg.mInfoDescription=new ApplicationDescription("test", "", "", "0.0", "0.0");
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

		assertEquals("Call Nb",2,mObsFactory.CallList.size());
		assertEquals("Call #1","CORE->UNLOCK(SuperTableTest='101',)",mObsFactory.CallList.get(0));
		assertEquals("Call #2","TestValidation->SuperTableTest_APAS_Fiche(SuperTableTest='101',)",mObsFactory.CallList.get(1));
	}
	
	private MockContainer checkContainer(GUIComponent comp,int expectedSize,ContainerType expectedType,String label) {
		assertEquals("class "+label,MockContainer.class,comp.getClass());
		MockContainer cont=(MockContainer)comp;
		assertEquals("nb "+label,expectedSize,cont.count());
		assertEquals("type "+label,expectedType,cont.getType());
		return cont;
	}
		
	public void testException1() throws LucteriosException {
		String xml_receive="<REPONSE>" +
				"<TITLE><![CDATA[]]></TITLE>" +
				"<CONTEXT></CONTEXT>" +
				"<EXCEPTION>" +
				"<MESSAGE><![CDATA[Critique]]></MESSAGE>" +
				"<CODE><![CDATA[1]]></CODE>" +
				"<MODE><![CDATA[0]]></MODE>" +
				"<DEBUG_INFO><![CDATA[0|[TestValidation]TableTest::ValiderErreur(act)|57|TableTest::ValiderErreur{[newline]}1|[CORE]BoucleReponse(inc)|81|call_user_func{[newline]}2|[CORE]BoucleReponse(inc)|118|callAction{[newline]}3|[init]|96|BoucleReponse{[newline]}]]></DEBUG_INFO>" +
				"<TYPE><![CDATA[LucteriosException]]></TYPE>" +
				"<USER_INFO><![CDATA[]]></USER_INFO>" +
				"</EXCEPTION>"+
				"</REPONSE>";

		mParse.parse(xml_receive,true);
		ObserverException obs=new ObserverException();
		obs.setSource("CORE", "validerAct");
		obs.setContent(mParse);
		obs.show("Mon titre");
		
		assertNotNull("LastException", ExceptionDlg.mLastException);
		assertNotNull("LastException.getDialog", ExceptionDlg.mLastException.getDialog());
		GUIContainer cont=ExceptionDlg.mLastException.getDialog().getContainer();
		assertEquals("nb component",2,cont.count());
		
		MockContainer cont1 = checkContainer(cont.get(0),3,ContainerType.CT_NORMAL,"1");
		assertEquals("class 1.1",MockImage.class,cont1.get(0).getClass());
		assertEquals("Image 1.1 H",48,cont1.getImage(0).getImage().getHeight());
		assertEquals("Image 1.1 W",48,cont1.getImage(0).getImage().getWidth());
		assertEquals("class 1.2",MockHyperText.class,cont1.get(1).getClass());
		assertEquals("Text 1.2","Critique",cont1.getHyperText(1).getTextString());

		MockContainer cont1_3= checkContainer(cont1.get(2),3,ContainerType.CT_NORMAL,"1.3");
		assertEquals("class 1.3.1",MockButton.class,cont1_3.get(0).getClass());
		assertEquals("class 1.3.2",MockButton.class,cont1_3.get(1).getClass());
		assertEquals("class 1.3.3",MockButton.class,cont1_3.get(2).getClass());
		assertEquals("enabled 1.3.1",true,cont1_3.getButton(0).isVisible());
		assertEquals("enabled 1.3.2",true,cont1_3.getButton(1).isVisible());
		assertEquals("enabled 1.3.3",true,cont1_3.getButton(2).isVisible());

		MockContainer cont2 = checkContainer(cont.get(1),2,ContainerType.CT_TAB,"2");
		
		MockContainer cont2_1= checkContainer(cont2.get(0),1,ContainerType.CT_SCROLL,"2.1");
		assertEquals("class 2.1.1",MockMemo.class,cont2_1.get(0).getClass());
		assertEquals("Memo 2.1.1",
				"[TestValidation]TableTest::ValiderErreur(act)     57   TableTest::ValiderErreur\n" +
				"[CORE]BoucleReponse(inc)                          81   call_user_func\n" +
				"[CORE]BoucleReponse(inc)                          118  callAction\n" +
				"[init]                                            96   BoucleReponse\n",cont2_1.getMemo(0).getValue());		
		MockContainer cont2_2= checkContainer(cont2.get(1),1,ContainerType.CT_SCROLL,"2.2");
		assertEquals("class 2.2.1",MockHyperText.class,cont2_2.get(0).getClass());
		assertEquals("Text 2.2.1","LucteriosException",cont2_2.getHyperText(0).getTextString());

		assertEquals("Call Nb",0,mObsFactory.CallList.size());
	}

	public void testException2() throws LucteriosException {
		String xml_receive="<REPONSE>" +
				"<TITLE><![CDATA[]]></TITLE>" +
				"<CONTEXT></CONTEXT>" +
				"<EXCEPTION>" +
				"<MESSAGE><![CDATA[Mineur]]></MESSAGE>" +
				"<CODE><![CDATA[4]]></CODE>" +
				"<MODE><![CDATA[0]]></MODE>" +
				"<DEBUG_INFO><![CDATA[0|[TestValidation]TableTest::ValiderErreur(act)|66|TableTest::ValiderErreur{[newline]}1|[CORE]BoucleReponse(inc)|81|call_user_func{[newline]}2|[CORE]BoucleReponse(inc)|118|callAction{[newline]}3|[init]|96|BoucleReponse{[newline]}]]></DEBUG_INFO>" +
				"<TYPE><![CDATA[LucteriosException]]></TYPE>" +
				"<USER_INFO><![CDATA[]]></USER_INFO>" +
				"</EXCEPTION>"+
				"</REPONSE>";
		mParse.parse(xml_receive,true);
		ObserverException obs=new ObserverException();
		obs.setSource("CORE", "validerAct");
		obs.setContent(mParse);
		obs.show("Mon titre");
		
		assertNotNull("LastException", ExceptionDlg.mLastException);
		assertNotNull("LastException.getDialog", ExceptionDlg.mLastException.getDialog());
		MockContainer cont=(MockContainer)ExceptionDlg.mLastException.getDialog().getContainer();
		assertEquals("nb component",2,cont.count());
		
		MockContainer cont1 = checkContainer(cont.get(0),3,ContainerType.CT_NORMAL,"1");
		assertEquals("class 1.1",MockImage.class,cont1.get(0).getClass());
		assertEquals("Image 1.1 H",48,((MockImage)cont1.get(0)).getImage().getHeight());
		assertEquals("Image 1.1 W",48,((MockImage)cont1.get(0)).getImage().getWidth());
		assertEquals("class 1.2",MockHyperText.class,cont1.get(1).getClass());
		assertEquals("Text 1.2","Mineur",((MockHyperText)cont1.get(1)).getTextString());

		MockContainer cont1_3= checkContainer(cont1.get(2),3,ContainerType.CT_NORMAL,"1.3");
		assertEquals("class 1.3.1",MockButton.class,cont1_3.get(0).getClass());
		assertEquals("class 1.3.2",MockButton.class,cont1_3.get(1).getClass());
		assertEquals("class 1.3.3",MockButton.class,cont1_3.get(2).getClass());
		assertEquals("enabled 1.3.1",true,cont1_3.getButton(0).isVisible());
		assertEquals("enabled 1.3.2",false,cont1_3.getButton(1).isVisible());
		assertEquals("enabled 1.3.3",false,cont1_3.getButton(2).isVisible());

		MockContainer cont2 = checkContainer(cont.get(1),1,ContainerType.CT_TAB,"2");

		MockContainer cont2_1= checkContainer(cont2.get(0),1,ContainerType.CT_SCROLL,"2.1");
		assertEquals("class 2.1.1",MockMemo.class,cont2_1.get(0).getClass());
		assertEquals("Memo 2.1.1",
				"[TestValidation]TableTest::ValiderErreur(act)     66   TableTest::ValiderErreur\n" +
				"[CORE]BoucleReponse(inc)                          81   call_user_func\n" +
				"[CORE]BoucleReponse(inc)                          118  callAction\n" +
				"[init]                                            96   BoucleReponse\n",cont2_1.getMemo(0).getValue());		
		
		assertEquals("Call Nb",0,mObsFactory.CallList.size());
	}
	
}
