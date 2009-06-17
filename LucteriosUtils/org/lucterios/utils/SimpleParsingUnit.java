/*
*    This file is part of Lucterios.
*
*    Lucterios is free software; you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation; either version 2 of the License, or
*    (at your option) any later version.
*
*    Lucterios is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with Lucterios; if not, write to the Free Software
*    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*
*	Contributeurs: Fanny ALLEAUME, Pierre-Olivier VERSCHOORE, Laurent GAY
*/

package org.lucterios.utils;


import junit.framework.TestCase;

public class SimpleParsingUnit extends TestCase 
{
	private SimpleParsing mParsing;
	
	protected void setUp() throws Exception 
	{
		super.setUp();
		mParsing=new SimpleParsing();
	}

	protected void tearDown() throws Exception 
	{
		mParsing=null;
		super.tearDown();
	}

	public void testEmpty()
	{
		assertFalse(mParsing.parse(""));
		assertEquals("Tag",SimpleParsing.EMPTY_NAME,mParsing.getTagName());
		assertEquals("Attr","",mParsing.getAttribut("aa"));
		assertEquals("SubContent","",mParsing.getSubContent());
		assertEquals("CData","",mParsing.getText());
		assertEquals("SubTag",0,mParsing.getSubTag("TOTO").length);
	}

	public void testSimple()
	{
		assertTrue(mParsing.parse("<SIMPLE aa='123'><![CDATA[WXCVBN]]><SUBSIMPLE><![CDATA[101]]></SUBSIMPLE><SUBSIMPLE><![CDATA[102]]></SUBSIMPLE></SIMPLE>"));
		assertEquals("Tag","SIMPLE",mParsing.getTagName());
		assertEquals("Attr","123",mParsing.getAttribut("aa"));
		assertEquals("SubContent","<![CDATA[WXCVBN]]><SUBSIMPLE><![CDATA[101]]></SUBSIMPLE><SUBSIMPLE><![CDATA[102]]></SUBSIMPLE>",mParsing.getSubContent());
		assertEquals("CData","WXCVBN",mParsing.getText());
		SimpleParsing[] sub=mParsing.getSubTag("SUBSIMPLE");
		assertEquals("SubTag",2,sub.length);
		assertEquals("CData 1","101",sub[0].getText());
		assertEquals("CData 2","102",sub[1].getText());
	}
	
	public void testSimpleWithComment()
	{
		assertTrue(mParsing.parse("<?xml version='1.0' encoding='ISO-8859-1'?><!-- commentaire --><SIMPLE aa='123'><![CDATA[WXCVBN]]><SUBSIMPLE><![CDATA[101]]></SUBSIMPLE><SUBSIMPLE><![CDATA[102]]><!-- Autre commentaire --></SUBSIMPLE></SIMPLE><!-- dernier commentaire -->"));
		assertEquals("Tag","SIMPLE",mParsing.getTagName());
		assertEquals("Attr","123",mParsing.getAttribut("aa"));
		assertEquals("SubContent","<![CDATA[WXCVBN]]><SUBSIMPLE><![CDATA[101]]></SUBSIMPLE><SUBSIMPLE><![CDATA[102]]></SUBSIMPLE>",mParsing.getSubContent());
		assertEquals("CData","WXCVBN",mParsing.getText());
		SimpleParsing[] sub=mParsing.getSubTag("SUBSIMPLE");
		assertEquals("SubTag",2,sub.length);
		assertEquals("CData 1","101",sub[0].getText());
		assertEquals("CData 2","102",sub[1].getText());
	}
	
	public void testConfigure()
	{
		assertTrue(mParsing.parse("<?xml version='1.0' encoding='ISO-8859-1'?><CONFIG><TITLE>Systeme Pitet</TITLE><PROXY></PROXY><PROXY_PORT>0</PROXY_PORT><SERVER name='local' host='localhost' port='80' dir='FFSS/'/><SERVER name='Fixe' host='82.225.46.111' port='80' dir='FFSS/'/><SERVER name='apas' host='apas.reali-soft.com' port='80' dir='/'/><SERVER name='ffss' host='ffss.reali-soft.com' port='80' dir='/'/><SERVER name='sss' host='82.233.108.66' port='80' dir='FFSS/'/></CONFIG>"));
		assertEquals("title","Systeme Pitet",mParsing.getCDataOfFirstTag("TITLE"));
		assertEquals("proxy","",mParsing.getCDataOfFirstTag("PROXY"));
		assertEquals("proxy_port","0",mParsing.getCDataOfFirstTag("PROXY_PORT"));
        SimpleParsing[] servers=mParsing.getSubTag("SERVER");
		assertEquals("servers",5,servers.length);
	}
	
	public void testComplexe()
	{
		assertTrue(mParsing.parse("<?xml version='1.0' encoding='ISO-8859-1'?><REPONSES><REPONSE observer='Core.DialogBox' source_extension='CORE' source_action='printmodel_APAS_reinit'><CONTEXT><PARAM name='print_model'><![CDATA[107]]></PARAM><PARAM name='CONFIRME'><![CDATA[YES]]></PARAM></CONTEXT><TEXT type='2'><![CDATA[Etes-vous sure de reinitialiser ce modele?]]></TEXT><ACTIONS><ACTION icon='images/ok.png' extension='CORE' action='printmodel_APAS_reinit'><![CDATA[Oui]]></ACTION><ACTION icon='images/cancel.png'><![CDATA[Non]]></ACTION></ACTIONS></REPONSE></REPONSES>"));
		assertEquals("Tag","REPONSES",mParsing.getTagName());
		
		SimpleParsing[] sub_rep=mParsing.getSubTag("REPONSE");
		assertEquals("SubTag A",1,sub_rep.length);
		assertEquals("Tag 1","REPONSE",sub_rep[0].getTagName());
		assertEquals("Attr a","Core.DialogBox",sub_rep[0].getAttribut("observer"));
		assertEquals("Attr b","CORE",sub_rep[0].getAttribut("source_extension"));
		assertEquals("Attr c","printmodel_APAS_reinit",sub_rep[0].getAttribut("source_action"));
		
		SimpleParsing sub_context=sub_rep[0].getFirstSubTag("CONTEXT");		
		SimpleParsing[] sub_param=sub_context.getSubTag("PARAM");
		assertEquals("SubTag C",2,sub_param.length);
		assertEquals("Attr d","print_model",sub_param[0].getAttribut("name"));
		assertEquals("CData 1","107",sub_param[0].getText());
		assertEquals("Attr e","CONFIRME",sub_param[1].getAttribut("name"));
		assertEquals("CData 2","YES",sub_param[1].getText());
		
		SimpleParsing[] sub_text=sub_rep[0].getSubTag("TEXT");
		assertEquals("SubTag D",1,sub_text.length);
		assertEquals("Attr f","2",sub_text[0].getAttribut("type"));
		assertEquals("CData 3","Etes-vous sure de reinitialiser ce modele?",sub_text[0].getText());
		
		SimpleParsing sub_actions=sub_rep[0].getFirstSubTag("ACTIONS");		
		SimpleParsing[] sub_action=sub_actions.getSubTag("ACTION");
		assertEquals("SubTag F",2,sub_action.length);
		assertEquals("Attr g","images/ok.png",sub_action[0].getAttribut("icon"));
		assertEquals("Attr h","CORE",sub_action[0].getAttribut("extension"));
		assertEquals("Attr i","printmodel_APAS_reinit",sub_action[0].getAttribut("action"));
		assertEquals("CData 4","Oui",sub_action[0].getText());
		assertEquals("Attr j","images/cancel.png",sub_action[1].getAttribut("icon"));
		assertEquals("Attr k","",sub_action[1].getAttribut("extension"));
		assertEquals("Attr l","",sub_action[1].getAttribut("action"));
		assertEquals("CData 5","Non",sub_action[1].getText());
	}

	public void testSubTagNames()
	{
		assertTrue(mParsing.parse("<?xml version='1.0' encoding='ISO-8859-1'?><REPONSES><REPONSE observer='Core.DialogBox' source_extension='CORE' source_action='printmodel_APAS_reinit'><CONTEXT><PARAM name='print_model'><![CDATA[107]]></PARAM><PARAM name='CONFIRME'><![CDATA[YES]]></PARAM></CONTEXT><TEXT type='2'><![CDATA[Etes-vous sure de reinitialiser ce modele?]]></TEXT><ACTIONS><ACTION icon='images/ok.png' extension='CORE' action='printmodel_APAS_reinit'><![CDATA[Oui]]></ACTION><ACTION icon='images/cancel.png'><![CDATA[Non]]></ACTION></ACTIONS></REPONSE></REPONSES>"));

		SimpleParsing sub_rep=mParsing.getFirstSubTag("REPONSE");
		String[] tag_names=sub_rep.getTagNames();
		assertEquals("names",3,tag_names.length);
		assertEquals("name 1","CONTEXT",tag_names[0]);
		assertEquals("name 2","TEXT",tag_names[1]);
		assertEquals("name 3","ACTIONS",tag_names[2]);

		SimpleParsing sub_text=sub_rep.getSubTag(1);	
		assertEquals("Attr f","2",sub_text.getAttribut("type"));
		assertEquals("CData 3","Etes-vous sure de reinitialiser ce modele?",sub_text.getText());
	}
	
	public void testMenu()
	{
		assertTrue(mParsing.parse("<?xml version='1.0' encoding='ISO-8859-1'?><REPONSES><REPONSE observer='CORE.Menu' source_extension='CORE' source_action='menu'><CONTEXT></CONTEXT><MENUS>	<MENU id='Ad_ministration' extension='CORE' modal='0'>		<![CDATA[Ad_ministration]]>		<MENU id='_Changerdemotdepasse' extension='CORE' action='users_APAS_changerpassword' modal='1'>			<![CDATA[_Changer de mot de passe]]>        </MENU>        <MENU id='_Avance' extension='CORE'>         	<![CDATA[_Avance]]>         	<MENU id='_Parametres' extension='CORE' action='extension_params_APAS_list'>         		<![CDATA[_Parametres]]>			</MENU>			<MENU id='Autorisationd`acces_reseau' extension='CORE' action='access_APAS_list'>				<![CDATA[Autorisation d`acces _reseau]]>			</MENU>			<MENU id='_Session' extension='CORE' action='sessions_APAS_list'>				<![CDATA[_Session]]>			</MENU>        </MENU>    </MENU></MENUS></REPONSE></REPONSES>"));
		
		SimpleParsing rep=mParsing.getFirstSubTag("REPONSE");		
		SimpleParsing menus=rep.getFirstSubTag("MENUS");		

		SimpleParsing[] menu1=menus.getSubTag("MENU");		
		assertEquals("menu1",1,menu1.length);		
		assertEquals("menu1 titre","Ad_ministration",menu1[0].getText());

		SimpleParsing[] menu2=menu1[0].getSubTag("MENU");		
		assertEquals("menu2",2,menu2.length);		
		assertEquals("menu2 titre0","_Changer de mot de passe",menu2[0].getText());
		assertEquals("menu2 titre1","_Avance",menu2[1].getText());
		
		SimpleParsing[] menu3=menu2[1].getSubTag("MENU");		
		assertEquals("menu3",3,menu3.length);		
		assertEquals("menu3 titre0","_Parametres",menu3[0].getText());
		assertEquals("menu3 titre1","Autorisation d`acces _reseau",menu3[1].getText());
		assertEquals("menu3 titre2","_Session",menu3[2].getText());
	}

	public void testEquals()
	{
		mParsing.parse("<SIMPLE aa='123'><![CDATA[WXCVBN]]><SUBSIMPLE><![CDATA[101]]></SUBSIMPLE><SUBSIMPLE><![CDATA[102]]></SUBSIMPLE></SIMPLE>");
		assertFalse("A",mParsing.equals(null));
		assertFalse("B","".equals( mParsing ));
		
		SimpleParsing other_parsing=new SimpleParsing();
		assertFalse("C",mParsing.equals(other_parsing));

		other_parsing.parse("<SIMPLE aa='123'><![CDATA[WXCVBN]]></SIMPLE>");
		assertFalse("C",mParsing.equals(other_parsing));

		other_parsing.parse("<SIMPLE aa='123'><![CDATA[WXCVBN]]><SUBSIMPLE><![CDATA[102]]></SUBSIMPLE></SIMPLE>");
		assertFalse("D",mParsing.equals(other_parsing));
		
		other_parsing.parse("<SIMPLE aa='123'><![CDATA[WXCVBN]]><SUBSIMPLE><![CDATA[101]]></SUBSIMPLE><SUBSIMPLE><![CDATA[102]]></SUBSIMPLE></SIMPLE>");
		assertTrue("E",mParsing.equals(other_parsing));
	}
	
	public void testSubText()
	{
		mParsing.parse("<P><![CDATA[WXCVBN]]><A/><![CDATA[UYT]]><B><![CDATA[BGTF]]></B><![CDATA[OIUY]]><C><![CDATA[102]]></C></P>");
		assertEquals("A","WXCVBNUYTOIUY",mParsing.getText());
		assertEquals("B","WXCVBN",mParsing.getText(0));
		assertEquals("C","UYT",mParsing.getText(1));
		assertEquals("D","OIUY",mParsing.getText(2));
		assertEquals("E","",mParsing.getText(3));
	}
	
	public void testSpecialText()
	{
		char myspace=160;
		assertTrue("A",mParsing.parse("<P>abc&#160;def&#160;ghi&#160;jkl</P>"));
		assertEquals("B","abc"+myspace+"def"+myspace+"ghi"+myspace+"jkl",mParsing.getText(0));
		assertEquals("C","",mParsing.getText(1));
		
		assertTrue("D",mParsing.parse("<P><![CDATA[e\"'(-è_çà)=*ù!:,?./§%µ£+°0987654321¹~#|`\\^@;<>[]{}]]></P>"));
		assertEquals("E","e\"'(-è_çà)=*ù!:,?./§%µ£+°0987654321¹~#|`\\^@;<>[]{}",mParsing.getText(0));
	}
	
}
