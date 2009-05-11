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

package org.lucterios.utils.graphic;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.Tools;

/**
 *
 * @author  lag
 */
public class ExceptionDlg extends javax.swing.JDialog {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public interface InfoDescription {
		void sendSupport(String mComplement);
	}
	
	public static final int FAILURE=0;
	public static final int CRITIC=1;
	public static final int GRAVE=2;
	public static final int IMPORTANT=3;
	public static final int MINOR=4;
	
	public static InfoDescription mInfoDescription=null;

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) 
    {
    	try
    	{
    		throw new LucteriosException("Lucterios Exception","<REQUETE extension='CORE' action='printmodel_APAS_reinit'></REQUETE>","syntax error line 123 in truc.php\n\n<REPONSES><REPONSE observer='Core.DialogBox' source_extension='CORE' source_action='printmodel_APAS_reinit'><CONTEXT><PARAM name='print_model'><![CDATA[107]]></PARAM><PARAM name='CONFIRME'><![CDATA[YES]]></PARAM></CONTEXT><TEXT type='2'><![CDATA[Etes-vous sûre de réinitialiser ce modèle?]]></TEXT><ACTIONS><ACTION icon='images/ok.png' extension='CORE' action='printmodel_APAS_reinit'><![CDATA[Oui]]></ACTION><ACTION icon='images/cancel.png'><![CDATA[Non]]></ACTION></ACTIONS></REPONSE></REPONSES>");
    	}
    	catch(Exception e)
    	{
    		throwException(e);
    	}

    	try
    	{
    		throw new Exception("Standard Exception");
    	}
    	catch(Exception e)
    	{
    		throwException(e);
    	}
    }
    
    private javax.swing.JScrollPane Extra;
    private javax.swing.JScrollPane Reponse;
    private javax.swing.JScrollPane Requette;
    private javax.swing.JScrollPane Stack;
    private javax.swing.JTabbedPane PnlExtra;
    private javax.swing.JPanel PnlMain;
    private javax.swing.JToggleButton btn_more;
    private javax.swing.JButton btn_exit;
    private javax.swing.JButton btn_send;
    private javax.swing.JLabel lbl_img;
    private javax.swing.JEditorPane edExtra;
    private javax.swing.JEditorPane lbl_message;
    private javax.swing.JTextArea txtReponse;
    private javax.swing.JTextArea txtRequette;
    private javax.swing.JTextArea txtStack;
	
    public ExceptionDlg() 
    {
        java.awt.GridBagConstraints gridBagConstraints;

        PnlMain = new javax.swing.JPanel();
        lbl_img = new javax.swing.JLabel();
        lbl_message = new javax.swing.JEditorPane();
        btn_more = new javax.swing.JToggleButton();

        PnlExtra = new javax.swing.JTabbedPane();

        getContentPane().add(PnlMain, java.awt.BorderLayout.CENTER);
        getContentPane().add(PnlExtra, java.awt.BorderLayout.SOUTH);
        setTitle("Erreur");
        setModal(true);
        PnlMain.setLayout(new java.awt.GridBagLayout());

        PnlMain.setName("PnlMain");
        lbl_img.setIcon(new javax.swing.ImageIcon(getClass().getResource("resources/error.png")));
        lbl_img.setName("lbl_img");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        PnlMain.add(lbl_img, gridBagConstraints);

        lbl_message.setEditable(false);
        lbl_message.setContentType("text/html");
        lbl_message.setFocusable(false);
        lbl_message.setBackground(getContentPane().getBackground());
        lbl_message.setName("lbl_message");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        PnlMain.add(lbl_message, gridBagConstraints);

        JPanel PnlBtn = new javax.swing.JPanel();
        PnlBtn.setLayout(new java.awt.GridBagLayout());
        
        btn_more.setText(">>");
        btn_more.setName("btn_more");
        btn_more.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_moreActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        PnlBtn.add(btn_more, gridBagConstraints);

        btn_send=new JButton();
        btn_send.setText("Envoyer au support");
        btn_send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	sendSupport();
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        PnlBtn.add(btn_send, gridBagConstraints);
        
        
        btn_exit=new JButton();
        btn_exit.setText("Ok");
        btn_exit.setMnemonic('O');
        btn_exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	dispose();
                setVisible(false);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        PnlBtn.add(btn_exit, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        PnlMain.add(PnlBtn, gridBagConstraints);

        PnlExtra.setName("PnlExtra");
    }

    protected void sendSupport() {
    	if (mInfoDescription!=null) {
    		String complement=lbl_message.getText().trim() + "\n";
    		if (edExtra!=null)
    			complement+=edExtra.getText().trim() + "\n";
    		if (txtReponse!=null)
    			complement+=txtReponse.getText().trim() + "\n";
    		if (txtRequette!=null)
    			complement+=txtRequette.getText().trim() + "\n";
    		if (txtStack!=null)
    			complement+=txtStack.getText().trim() + "\n";
    		mInfoDescription.sendSupport(complement);
    	}
	}

	private void btn_moreActionPerformed(java.awt.event.ActionEvent evt) 
    {
        int w=400;
        int h=getSize().height;
        PnlExtra.setVisible(btn_more.isSelected());
        if (btn_more.isSelected())
        {
            btn_more.setText("<<");
            setSize(w,h+200);
            PnlExtra.setPreferredSize(new java.awt.Dimension(w,200));
            PnlExtra.setMinimumSize(new java.awt.Dimension(w,200));
            pack();
            PnlExtra.setPreferredSize(new java.awt.Dimension(w,200));
            PnlExtra.setMinimumSize(new java.awt.Dimension(w,200));
        }
        else
        {
            PnlExtra.setMaximumSize(new java.awt.Dimension(w,0));
            PnlExtra.setPreferredSize(new java.awt.Dimension(w,0));
            PnlExtra.setMinimumSize(new java.awt.Dimension(w,0));
            btn_more.setText(">>");
            setSize(w,h-PnlExtra.getSize().height);
            pack();
        }
        pack();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize(); 
		setLocation((screen.width-getSize().width)/2,(screen.height-getSize().height)/2);			
    }

    protected void addTrace(Exception expt)
    {
        StringWriter str_wrt=new StringWriter();
        PrintWriter prt=new PrintWriter(str_wrt);
        expt.printStackTrace(prt);
        addStack(str_wrt.getBuffer().toString());
    }

    protected void addStack(String aText)
    {
    	if (aText.length()>0)
    	{
            Stack = new javax.swing.JScrollPane();
            txtStack = new javax.swing.JTextArea();
            txtStack.setEditable(false);
            txtStack.setFocusable(false);
            txtStack.setFont(new Font("Bitstream Vera Sans Mono",Font.PLAIN,12));
            Stack.setViewportView(txtStack);
            PnlExtra.addTab("Pile d'appel", Stack);
            txtStack.setText(aText);
    	}
    }
    
    protected void addExtra(String aText)
    {
    	if (aText.length()>0)
    	{
		    Extra = new javax.swing.JScrollPane();
		    edExtra = new javax.swing.JEditorPane();
		    edExtra.setEditable(false);
		    edExtra.setFocusable(true);
		    edExtra.setContentType("text/html");
		    Extra.setViewportView(edExtra);
		    PnlExtra.addTab("Extra", Extra);
	        edExtra.setText(aText);
    	}
    }
    
    protected void addRequette(String aText)
    {
    	if (aText.length()>0)
    	{
		    Requette = new javax.swing.JScrollPane();
		    txtRequette = new javax.swing.JTextArea();
		    txtRequette.setEditable(false);
		    txtRequette.setFocusable(true);
		    Requette.setViewportView(txtRequette);
		    PnlExtra.addTab("Requette", Requette);
		    txtRequette.setText(aText);
    	}
    }

    protected void addReponse(String aText)
    {
    	if (aText.length()>0)
    	{
		    Reponse = new javax.swing.JScrollPane();
		    txtReponse = new javax.swing.JTextArea();
		    txtReponse.setEditable(false);
		    txtReponse.setFocusable(true);
		    Reponse.setViewportView(txtReponse);
		    PnlExtra.addTab("Reponse", Reponse);
	        txtReponse.setText(aText);
    	}
    }

    public ImageIcon getIcon(int aIconType)
    {
		switch(aIconType)
		{
			case FAILURE:
			case CRITIC:
				return new javax.swing.ImageIcon(getClass().getResource("resources/error.png"));
			case GRAVE:
			case IMPORTANT:
				return new javax.swing.ImageIcon(getClass().getResource("resources/warning.png"));
			case MINOR:
				return new javax.swing.ImageIcon(getClass().getResource("resources/info.png"));
			default:
				return new javax.swing.ImageIcon(getClass().getResource("resources/error.png"));
		}
    	
    }
    
    protected void setMessage(String aText,int aType)
    {
        lbl_message.setText(aText);
        lbl_img.setIcon(getIcon(aType));
        btn_more.setVisible((aType==FAILURE) || (aType==CRITIC) || (aType==GRAVE));
        btn_send.setVisible(btn_more.isVisible() && (mInfoDescription!=null));
    }
    
    protected void setException(Exception e)
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
	        		addExtra(rep.substring(0,pos-1));
	            addReponse(rep.substring(pos).replaceAll("><",">\n<"));
	        }
	        else
	        	addExtra(rep);
	        addRequette(le.mRequest.replaceAll("><",">\n<"));
    	}
    	else
    		lbl_img.setIcon(getIcon(IMPORTANT));
    }
    
    public void showDialog()
    {
        PnlExtra.setVisible(btn_more.isSelected());
        setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);       		
        btn_moreActionPerformed(null);
        setVisible(true);
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
        for(int index=0;index<stack_texts.length;index++)
            stack_text+=formatStack(stack_texts[index])+"\n";
        stack_text=stack_text.replace('|','\t');
        err_dlg.addStack(stack_text);
        if ((aType==FAILURE) || (aType==CRITIC))
        	err_dlg.addExtra(aExtra);
        err_dlg.showDialog();
    }
    
    public static void throwException(Exception expt)
    {
        ExceptionDlg err_dlg=new ExceptionDlg();
        if (LucteriosException.class.isInstance(expt))
            err_dlg.setLucteriosException((LucteriosException)expt);
        else
        	err_dlg.setException(expt);
        err_dlg.showDialog();
    }
}
