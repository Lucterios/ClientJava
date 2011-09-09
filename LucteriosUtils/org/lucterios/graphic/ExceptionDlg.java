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

package org.lucterios.graphic;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIGenerator;
import org.lucterios.gui.GUIHyperText;
import org.lucterios.gui.GUIImage;
import org.lucterios.gui.GUIMemo;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.gui.GUIParam.ReSizeMode;
import org.lucterios.ui.GUIActionListener;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.Tools;
import org.lucterios.utils.Tools.InfoDescription;

/**
 *
 * @author  lag
 */
public class ExceptionDlg {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int FAILURE=0;
	public static final int CRITIC=1;
	public static final int GRAVE=2;
	public static final int IMPORTANT=3;
	public static final int MINOR=4;
	
	public static InfoDescription mInfoDescription=null;
	public static GUIGenerator mGenerator=null;

    private GUIContainer Extra;
    private GUIContainer Reponse;
    private GUIContainer Requette;
    private GUIContainer Stack;
    private GUIContainer PnlExtra;
    private GUIContainer PnlMain;
    private GUIButton btn_more;
    private GUIButton btn_exit;
    private GUIButton btn_send;
    private GUIImage lbl_img;
    private GUIHyperText edExtra;
    private GUIHyperText lbl_message;
    private GUIMemo txtReponse;
    private GUIMemo txtRequette;
    private GUIMemo txtStack;
    
    private GUIDialog mDialog;
	
    public ExceptionDlg() 
    {
    	mDialog=mGenerator.newDialog(null);
    	mDialog.setTitle("Erreur");

    	PnlMain = mDialog.getContainer().createContainer(ContainerType.CT_NORMAL, new GUIParam(0,0,1,1,ReSizeMode.RSM_HORIZONTAL,FillMode.FM_BOTH));
    	lbl_img = PnlMain.createImage(new GUIParam(0,0,1,1,ReSizeMode.RSM_NONE,FillMode.FM_NONE)); 
        lbl_img.setImage(mGenerator.CreateImage(getClass().getResource("resources/error.png")));
        lbl_message = PnlMain.createHyperText(new GUIParam(1,0,1,1,ReSizeMode.RSM_HORIZONTAL,FillMode.FM_NONE));

        GUIContainer PnlBtn= PnlMain.createContainer(ContainerType.CT_NORMAL,new GUIParam(0,1,2,1,ReSizeMode.RSM_HORIZONTAL,FillMode.FM_BOTH));
        btn_more = PnlBtn.createButton(new GUIParam(2,0,1,1,ReSizeMode.RSM_NONE,FillMode.FM_NONE));
        btn_more.setToggle(true);
        btn_more.setTextString(">>");
        btn_more.addActionListener(new GUIActionListener() {		
			public void actionPerformed() {
                btn_moreActionPerformed();
				
			}
		});

        btn_send = PnlBtn.createButton(new GUIParam(1,0,1,1,ReSizeMode.RSM_NONE,FillMode.FM_NONE));
        btn_send.setTextString("Envoyer au support");
        btn_send.addActionListener(new GUIActionListener() {
            public void actionPerformed() {
            	sendSupport();
            }
        });
      
        
        btn_exit = PnlBtn.createButton(new GUIParam(0,0,1,1,ReSizeMode.RSM_NONE,FillMode.FM_NONE));
        btn_exit.setTextString("Ok");
        btn_exit.setMnemonic('O');
        btn_exit.addActionListener(new GUIActionListener() {
            public void actionPerformed() {
            	mDialog.dispose();
            	mDialog.close();
            }
        });
        	
    	PnlExtra = mDialog.getContainer().createContainer(ContainerType.CT_TAB, new GUIParam(0,1,2,1,ReSizeMode.RSM_BOTH,FillMode.FM_BOTH));
   	
    }

    protected String removeHTMLHeader(String aText) {
    	String resValue=aText.trim();
    	resValue=Tools.replace(resValue,"<html>","");
    	resValue=Tools.replace(resValue,"</html>","");
    	resValue=Tools.replace(resValue,"<head>","");
    	resValue=Tools.replace(resValue,"</head>","");
    	resValue=Tools.replace(resValue,"<body>","");
    	resValue=Tools.replace(resValue,"</body>","");
    	resValue=Tools.replace(resValue,"<br><br>","<br>");
    	return resValue.trim();
    }
    
    protected void sendSupport() {
    	if (mInfoDescription!=null) {
    		String complement="Décrivez le plus précisément possible, comment vous avez obtenu ce problème.<br>Merci de votre aide.<br><br>";
    		complement+="<h1>"+removeHTMLHeader(lbl_message.getTextString())+"</h1>";
    		if (edExtra!=null)
    			complement+=removeHTMLHeader(edExtra.getTextString()) + "<br><br>";
    		if (txtReponse!=null)
    			complement+=Tools.HTMLEntityEncode(txtReponse.getValue()) + "<br><br>";
    		if (txtRequette!=null)
    			complement+=Tools.HTMLEntityEncode(txtRequette.getValue()) + "<br><br>";
    		if (txtStack!=null)
    			complement+=removeHTMLHeader(txtStack.getValue()) + "<br>";
    		mInfoDescription.sendSupport("Rapport de bogue",complement);
    	}
	}

	private void btn_moreActionPerformed() 
    {
        int w=400;
        int h=mDialog.getSizeY();
        PnlExtra.setVisible(btn_more.isSelected());
        if (btn_more.isSelected())
        {
            btn_more.setTextString("<<");
            mDialog.setSize(w,h+200);
            PnlExtra.setSize(w,200);
            mDialog.pack();
            PnlExtra.setSize(w,200);
        }
        else
        {
            PnlExtra.setSize(w,0);
            btn_more.setTextString(">>");
            mDialog.setSize(w,h-PnlExtra.getSizeY());
            mDialog.pack();
        }
        mDialog.pack();
        mDialog.initialPosition();
    }

    protected void addTrace(Throwable expt)
    {
        StringWriter str_wrt=new StringWriter();
        PrintWriter prt=new PrintWriter(str_wrt);
        expt.printStackTrace(prt);
        addStack(str_wrt.getBuffer().toString());
        expt.printStackTrace();
    }

    protected void addStack(String aText)
    {
    	if (aText.length()>0)
    	{
            Stack = PnlExtra.addTab(ContainerType.CT_SCROLL,"Pile d'appel", AbstractImage.Null);
            txtStack = Stack.createMemo(new GUIParam(0,0));
            txtStack.setText(aText);
    	}
    }
    
    protected void addExtra(String aText)
    {
    	if (aText.length()>0)
    	{
		    Extra = PnlExtra.addTab(ContainerType.CT_SCROLL, "Extra", AbstractImage.Null);
		    edExtra = Extra.createHyperText(new GUIParam(0,0));
	        edExtra.setTextString(aText.replaceAll("\n", "<br>").replaceAll(" ","&nbsp;"));
    	}
    }
    
    protected void addRequette(String aText)
    {
    	if (aText.length()>0)
    	{
		    Requette = PnlExtra.addTab(ContainerType.CT_SCROLL,"Requette", AbstractImage.Null);
		    txtRequette = Requette.createMemo(new GUIParam(0,0));
		    txtRequette.setText(aText);
    	}
    }

    protected void addReponse(String aText)
    {
    	if (aText.length()>0)
    	{
		    Reponse = PnlExtra.addTab(ContainerType.CT_SCROLL,"Reponse", AbstractImage.Null);
		    txtReponse = Reponse.createMemo(new GUIParam(0,0));
	        txtReponse.setText(aText);
    	}
    }

    public AbstractImage getIcon(int aIconType)
    {
		switch(aIconType)
		{
			case FAILURE:
			case CRITIC:
				return mGenerator.CreateImage(getClass().getResource("resources/error.png"));
			case GRAVE:
			case IMPORTANT:
				return mGenerator.CreateImage(getClass().getResource("resources/warning.png"));
			case MINOR:
				return mGenerator.CreateImage(getClass().getResource("resources/info.png"));
			default:
				return mGenerator.CreateImage(getClass().getResource("resources/error.png"));
		}
    	
    }
    
    protected void setMessage(String aText,int aType)
    {
        lbl_message.setTextString(aText);
        lbl_img.setImage(getIcon(aType));
        btn_more.setVisible((aType==FAILURE) || (aType==CRITIC) || (aType==GRAVE));
        btn_send.setVisible(btn_more.isVisible() && (mInfoDescription!=null));
    }
    
    protected void setException(Throwable e)
    {
    	setMessage(e.getClass().getName()+" "+e.getMessage(),FAILURE);
        addTrace(e);
    } 
    
    protected void setLucteriosException(LucteriosException le)
    {
    	setMessage("<b>Message</b> "+le.getMessage()+"<br>"+le.getExtraInfo(),FAILURE);
        btn_more.setVisible(le.mWithTrace);
    	if (le.mWithTrace) { 
    		addTrace(le);
        
	        String rep=le.mReponse;
	        int pos=rep.indexOf("?>");
	        if (pos>=0)
	            rep=rep.substring(pos+2);
	        pos=rep.indexOf("<REPONSES");
	        if (pos>=0)
	        {
	        	if (pos>0)
	        		addExtra(rep.substring(0,pos-1).trim());
	            addReponse(rep.substring(pos).replaceAll("><",">\n<"));
	        }
	        else
	        	addExtra(rep.trim());
	        addRequette(le.mRequest.replaceAll("><",">\n<"));
    	}
    	else
    		lbl_img.setImage(getIcon(IMPORTANT));
    }
    
    public void showDialog()
    {
    	mDialog.pack();
    	mDialog.initialPosition();
        PnlExtra.setVisible(btn_more.isSelected());
        btn_moreActionPerformed();
		mDialog.setVisible(true);
    }

    static protected String formatStack(String aStack)
    {
        String[] stack_lines=aStack.split("\\|");
        String stack_line="";
        if (stack_lines.length>1)
        	stack_line+=Tools.padRigth(stack_lines[1],50);
        if (stack_lines.length>2)
        	stack_line+=Tools.padRigth(stack_lines[2],5);
        if (stack_lines.length>3)
        	stack_line+=stack_lines[3];
        return stack_line;
    }
    
    public static void show(int aType,String aMessage,String aStack,String aExtra)
    {
        ExceptionDlg err_dlg=new ExceptionDlg();
        err_dlg.setMessage(Tools.convertLuctoriosFormatToHtml(aMessage),aType);
        String stack_text="";
        String[] stack_texts=aStack.split("\\{\\[newline\\]\\}");
        for(String stacktext:stack_texts)
            stack_text+=formatStack(stacktext)+"\n";
        stack_text=stack_text.replace('|','\t');
        err_dlg.addStack(stack_text);
        if ((aType==FAILURE) || (aType==CRITIC))
        	err_dlg.addExtra(aExtra.trim());
        err_dlg.showDialog();
    }
    
    public static void throwException(Throwable expt)
    {
        ExceptionDlg err_dlg=new ExceptionDlg();
        if (LucteriosException.class.isInstance(expt))
            err_dlg.setLucteriosException((LucteriosException)expt);
        else
        	err_dlg.setException(expt);
        err_dlg.showDialog();
    }
}
