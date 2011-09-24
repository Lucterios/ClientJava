package org.lucterios.engine.application.observer;

import java.io.File;
import java.io.IOException;

import org.lucterios.engine.application.ActionImpl;
import org.lucterios.engine.application.ApplicationDescription;
import org.lucterios.engine.application.Connection;
import org.lucterios.engine.application.comp.Cmponent;
import org.lucterios.engine.presentation.ObserverFactoryMock;
import org.lucterios.engine.presentation.Singletons;
import org.lucterios.engine.transport.HttpTransportStub;
import org.lucterios.engine.utils.LucteriosConfiguration;
import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.mock.MockButton;
import org.lucterios.mock.MockContainer;
import org.lucterios.mock.MockGenerator;
import org.lucterios.mock.MockHyperText;
import org.lucterios.mock.MockImage;
import org.lucterios.mock.MockLabel;
import org.lucterios.mock.MockMemo;
import org.lucterios.utils.DesktopBasic;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;
import org.lucterios.utils.StringList;
import org.lucterios.utils.Tools;

import junit.framework.TestCase;

public class ObserverUnit extends TestCase implements Connection {

	private SimpleParsing mParse;
	
	private ApplicationDescription mDescription;
	private String mSubTitle;
	private String mLogin; 
	private String mRealName; 
	private boolean mRefreshMenu;
	
	private ObserverFactoryMock mObsFactory;
	private MockGenerator mGenerator;

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
		setValue(null,"","","",false);
		mGenerator=new MockGenerator();
		mObsFactory=new ObserverFactoryMock();
		mParse = new SimpleParsing();

		Singletons.setHttpTransportClass(HttpTransportStub.class);
		Singletons.setWindowGenerator(mGenerator);
		Singletons.setDesktop(new DesktopBasic());
		Singletons.setActionClass(ActionImpl.class);
		Singletons.initalize(new LucteriosConfiguration(new File(".")),mObsFactory);
		Singletons.Transport().setSession("abc123");

		ExceptionDlg.mGenerator=mGenerator;
		ExceptionDlg.mLastException=null;
		ExceptionDlg.mInfoDescription=new ApplicationDescription("test", "", "", "0.0", "0.0");
		ObserverFactoryMock.NewObserver=new ObserverAcknowledge();
		ObserverAuthentification.mConnection=this;
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

	public void testPrint() throws LucteriosException, IOException {
		String xml_receive="<REPONSE>" +
				"<TITLE><![CDATA[Imprimer une liste de personneMorale]]></TITLE>" +
				"<CONTEXT><PARAM name='Filtretype'><![CDATA[2]]></PARAM><PARAM name='PRINT_MODE'><![CDATA[4]]></PARAM></CONTEXT>" +
				"<PRINT title='Liste des Personnes Morales' type='2' mode='4' withTextExport='1'>" +
				"<![CDATA[CiJMaXN0ZSBkZXMgUGVyc29ubmVzIE1vcmFsZXMiCgoKIlJhaXNvbiBTb2NpYWxlIjsiQWRyZXNzZSI7IlTpbOlwaG9uZXMiOyJDb3VycmllbCI7CiJMZXMgcCd0aXQgTWlja2V5IjsicGxhY2UgR3JlbmV0dGUgMzgwMDAgR1JFTk9CTEUgRlJBTkNFIjsiMDQuMTIuMzQuNTYuNzggMDYuOTguNzYuNTQuMzIiOyJwZXRpdC5taWNrZXlAZnJlZS5mciI7CiJMZSBnbG9zIG1pbmV0IjsiQ291cnMgQmVyaWF0IDM4MDAwIEdSRU5PQkxFIEZSQU5DRSI7IjA0LjE5LjI4LjM3LjQ2IjsiZ2xvcy5taW5ldEBob3RtYWlsLmZyIjsKImpqaiI7ImpqaiA2NDAwMCBQQVUgRlJBTkNFIjsiIjsibGF1cmVudDM4NjAwQGZyZWUuZnIiOwoKCiIiCgoK]]>" +
				"</PRINT>"+
				"</REPONSE>";
		mGenerator.setSelectFile(new File("./testprint.csv"));
		mGenerator.getSelectFile().delete();
		assertEquals("File deleted",false,mGenerator.getSelectFile().isFile());
		mParse.parse(xml_receive,true);
		ObserverPrint  obs=new ObserverPrint();
		obs.setSource("CORE", "validerAct");
		obs.setContent(mParse);
		obs.show("Mon titre");
		
		assertEquals("Context size",2,obs.getContext().size());
		assertEquals("Context 1","2",obs.getContext().get("Filtretype"));
		assertEquals("Context 2","4",obs.getContext().get("PRINT_MODE"));
		
		assertEquals("File exist",true,mGenerator.getSelectFile().isFile());
		StringList contents=Tools.readFileText(mGenerator.getSelectFile(),"ISO-8859-1");
		assertEquals("File size",13,contents.size());
		assertEquals("title","\"Liste des Personnes Morales\"",contents.get(1).trim());
		assertEquals("header","\"Raison Sociale\";\"Adresse\";\"Téléphones\";\"Courriel\";",contents.get(4).trim());
		assertEquals("line #2","\"Le glos minet\";\"Cours Beriat 38000 GRENOBLE FRANCE\";\"04.19.28.37.46\";\"glos.minet@hotmail.fr\";",contents.get(6).trim());
		
		mGenerator.getSelectFile().delete();
		assertEquals("Call Nb",0,mObsFactory.CallList.size());
	}
	
	public void testDialog() throws LucteriosException {
		String xml_receive="<REPONSE>" +
				"<TITLE><![CDATA[Confirmation]]></TITLE>" +
				"<CONTEXT>" +
					"<PARAM name='Filtretype'><![CDATA[2]]></PARAM>" +
					"<PARAM name='ORIGINE'><![CDATA[personneMorale_APAS_Fiche]]></PARAM>" +
					"<PARAM name='RECORD_ID'><![CDATA[100]]></PARAM>" +
					"<PARAM name='TABLE_NAME'><![CDATA[org_lucterios_contacts_personneMorale]]></PARAM>" +
					"<PARAM name='abstractContact'><![CDATA[1196]]></PARAM>" +
					"<PARAM name='personneMorale'><![CDATA[100]]></PARAM>" +
					"<PARAM name='CONFIRME'><![CDATA[YES]]></PARAM>" +
				"</CONTEXT>" +
				"<TEXT type='2'><![CDATA[Voulez vous supprimer 'jjj'?]]></TEXT>" +
				"<ACTIONS>" +
					"<ACTION icon='images/ok.png' sizeicon='1731' extension='org_lucterios_contacts' action='personneAbstraite_APAS_Delete' close='1' modal='1'><![CDATA[Oui]]></ACTION>" +
					"<ACTION icon='images/cancel.png' sizeicon='1656'><![CDATA[Non]]></ACTION>" +
				"</ACTIONS>"+
				"</REPONSE>";

		mParse.parse(xml_receive,true);
		GUIDialog dialog=mGenerator.newDialog(null);
		ObserverDialogBox obs=new ObserverDialogBox();
		obs.setSource("org_lucterios_contacts", "personneMorale_APAS_Fiche");
		obs.setContent(mParse);
		obs.show("Confirmation",dialog);

		MockContainer cont=(MockContainer)dialog.getContainer();
		assertEquals("nb component",3,cont.count());
		
		assertEquals("class 1",MockImage.class,cont.get(0).getClass());
		assertEquals("class 2",MockHyperText.class,cont.get(1).getClass());
		assertEquals("Text 2","Voulez vous supprimer 'jjj'?",((MockHyperText)cont.get(1)).getTextString());

		MockContainer cont3 = checkContainer(cont.get(2),2,ContainerType.CT_NORMAL,"3");
		assertEquals("class 3.1",MockButton.class,cont3.get(0).getClass());
		assertEquals("class 3.2",MockButton.class,cont3.get(1).getClass());
		assertEquals("enabled 3.1",true,cont3.getButton(0).isVisible());
		assertEquals("enabled 3.2",true,cont3.getButton(1).isVisible());
		assertEquals("enabled 3.1","Oui",cont3.getButton(0).getTextString());
		assertEquals("enabled 3.2","Non",cont3.getButton(1).getTextString());
		
		assertEquals("Context size",7,obs.getContext().size());
		assertEquals("Context 1","100",obs.getContext().get("personneMorale"));
		assertEquals("Context 2","YES",obs.getContext().get("CONFIRME"));
		
		assertEquals("Call Nb",0,mObsFactory.CallList.size());
	}
	

	public void testCustom1() throws LucteriosException {
		String xml_receive="<REPONSE>" +
		"<TITLE><![CDATA[Résumé]]></TITLE>" +
		"<CONTEXT></CONTEXT>" +
		"<COMPONENTS>" +
		"<LABELFORM name='documenttitle' description=''  tab='0' x='0' y='70' colspan='4' rowspan='1'><![CDATA[{[center]}{[bold]}{[underline]}Gestion documentaire{[/underline]}{[/bold]}{[/center]}]]></LABELFORM>" +
		"<LABELFORM name='lbl_nbdocument' description=''  tab='0' x='0' y='71' colspan='4' rowspan='1'><![CDATA[{[center]}13 fichiers actuellement disponibles{[/center]}]]></LABELFORM>" +
		"<LABELFORM name='lbl_remainingsize' description=''  tab='0' x='0' y='72' colspan='4' rowspan='1'><![CDATA[{[center]}{[italic]}Taille de stockage: restant 30.03 Mo. sur 1 Go.{[/italic]}{[/center]}]]></LABELFORM>" +
		"<LABELFORM name='documentend' description=''  tab='0' x='0' y='73' colspan='4' rowspan='1'><![CDATA[{[center]}{[hr/]}{[/center]}]]></LABELFORM>" +
		"<LABELFORM name='updatestitle' description=''  tab='0' x='0' y='100' colspan='4' rowspan='1'><![CDATA[{[center]}{[bold]}{[underline]}Mises à jour{[/underline]}{[/bold]}{[/center]}]]></LABELFORM>" +
		"<LABELFORM name='updatelbl' description=''  tab='0' x='0' y='101' colspan='4' rowspan='1'><![CDATA[{[center]}Votre logiciel est à jour.{[/center]}]]></LABELFORM>" +
		"<LABELFORM name='updatesend' description=''  tab='0' x='0' y='103' colspan='4' rowspan='1'><![CDATA[{[center]}{[hr/]}{[/center]}]]></LABELFORM>" +
		"</COMPONENTS>"+
		"</REPONSE>";

		mParse.parse(xml_receive,true);
		GUIDialog dialog=mGenerator.newDialog(null);
		ObserverCustom obs=new ObserverCustom();
		obs.setSource("CORE","status");
		obs.setContent(mParse);
		obs.show("Résumé",dialog);
		
		MockContainer cont=(MockContainer)dialog.getContainer();
		assertEquals("nb component",2,cont.count());
		MockContainer cont1 = checkContainer(cont.get(0),8,ContainerType.CT_SCROLL,"1");
		MockContainer cont1_1 = checkContainer(cont1.get(0),1,ContainerType.CT_NORMAL,"1.1");
		assertEquals("name 1.1","documenttitle",((Cmponent)cont1_1.getObject()).getName());
		MockContainer cont1_2 = checkContainer(cont1.get(1),1,ContainerType.CT_NORMAL,"1.2");
		assertEquals("name 1.2","lbl_nbdocument",((Cmponent)cont1_2.getObject()).getName());
		MockContainer cont1_3 = checkContainer(cont1.get(2),1,ContainerType.CT_NORMAL,"1.3");
		assertEquals("name 1.3","lbl_remainingsize",((Cmponent)cont1_3.getObject()).getName());
		MockContainer cont1_4 = checkContainer(cont1.get(3),1,ContainerType.CT_NORMAL,"1.4");
		assertEquals("name 1.4","documentend",((Cmponent)cont1_4.getObject()).getName());
		MockContainer cont1_5 = checkContainer(cont1.get(4),1,ContainerType.CT_NORMAL,"1.5");
		assertEquals("name 1.5","updatestitle",((Cmponent)cont1_5.getObject()).getName());
		MockContainer cont1_6 = checkContainer(cont1.get(5),1,ContainerType.CT_NORMAL,"1.6");
		assertEquals("name 1.6","updatelbl",((Cmponent)cont1_6.getObject()).getName());
		MockContainer cont1_7 = checkContainer(cont1.get(6),1,ContainerType.CT_NORMAL,"1.7");
		assertEquals("name 1.7","updatesend",((Cmponent)cont1_7.getObject()).getName());
		assertEquals("class 1.8",MockLabel.class,cont1.get(7).getClass());

		checkContainer(cont.get(1),0,ContainerType.CT_NORMAL,"2");
	}
	
}
