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

package org.lucterios.Print;


import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.lucterios.Print.resources.Resources;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.Tools;
import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.graphic.HtmlLabel;

/**
 *
 * @author  lg
 */
public class DemoPrint extends JFrame
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MainPrintPanel newContentPane;
    private JScrollPane scrCode;
    private HtmlLabel CodeEditor;
    private JEditorPane XmlDataEditor;

    private JPanel pnl_main;
    private JPanel pnl_bnt;
    private JButton btn_in;
    private JButton btn_out;
    private JCheckBox cb_WithText;
    
    public DemoPrint()
    {
        super("Exemple");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pnl_main=new JPanel();
        pnl_main.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints;

        Image font_img=Toolkit.getDefaultToolkit().getImage(Resources.class.getResource("MainFont.jpg"));
        newContentPane = new MainPrintPanel();
        newContentPane.setFontImage(font_img,MainPrintPanel.TEXTURE);
        newContentPane.setOpaqueChild(false);
        //newContentPane.getManager().
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.weightx = 1.0;
        pnl_main.add(newContentPane, gridBagConstraints);


        CodeEditor=new HtmlLabel();
        CodeEditor.setText("");
        scrCode=new JScrollPane();
        scrCode.setViewportView(CodeEditor);
        scrCode.setMinimumSize(new java.awt.Dimension(100,200));
        scrCode.setPreferredSize(new java.awt.Dimension(200,200));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        pnl_main.add(scrCode, gridBagConstraints);

        pnl_bnt=new JPanel();
        pnl_bnt.setLayout(new java.awt.GridBagLayout());
        btn_in=new JButton();
        btn_in.setText("Import");
        btn_in.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) 
            {
                    btn_in_actionPerformed(e);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        pnl_bnt.add(btn_in, gridBagConstraints);

        btn_out=new JButton();
        btn_out.setText("Export");
        btn_out.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) 
            {
                    btn_out_actionPerformed(e);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        pnl_bnt.add(btn_out, gridBagConstraints);

        cb_WithText=new JCheckBox();
        cb_WithText.setText("With text export ?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth=3;
        pnl_bnt.add(cb_WithText, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        pnl_main.add(pnl_bnt, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight=2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;

        XmlDataEditor=new JEditorPane();
        XmlDataEditor.setContentType("text/plain");
        XmlDataEditor.setText("<DATA>\n</DATA>");
        JScrollPane xmlCode=new JScrollPane();
        xmlCode.setViewportView(XmlDataEditor);
        xmlCode.setMinimumSize(new java.awt.Dimension(100,150));
        xmlCode.setPreferredSize(new java.awt.Dimension(200,150));        
        pnl_main.add(xmlCode, gridBagConstraints);

        setContentPane(pnl_main);

        pack();
        setLocationRelativeTo(null);
    }

    public void btn_out_actionPerformed(java.awt.event.ActionEvent e) 
    {
        CodeEditor.setText(newContentPane.getManager().getPage().Save());
    }
    
    public void btn_in_actionPerformed(java.awt.event.ActionEvent e) 
    {
        try
        {
            newContentPane.getData(CodeEditor.getText(),XmlDataEditor.getText());
        }
        catch(java.io.IOException ierr)
        {
            System.out.println("+++ Erreur "+ierr);
        }
        catch(org.xml.sax.SAXException serr)
        {
            System.out.println("%%% Erreur "+serr);
        }
    }

    public static void main(String args[])
    {
        if (args.length>1) {
	        DemoPrint frame=new DemoPrint();
	        if ((args.length==2) || (args.length==3))
	        	frame.loadModel(args[0],args[1]);
	        frame.setVisible(true);
        }
        else
        	ExceptionDlg.throwException(new LucteriosException("Minimum 1 arguments"));
    }

	private void loadModel(String modelFileName, String xmlFileName) {
		File f_model=new File(modelFileName);
		File f_xml=new File(xmlFileName);
		if (f_model.isFile() && f_xml.isFile())
		{
			try {
				InputStream is_model = new FileInputStream(f_model);
				InputStream is_xml=new FileInputStream(f_xml);
			    CodeEditor.setText(Tools.parseISToString(is_model));
			    XmlDataEditor.setText(Tools.parseISToString(is_xml));
			    btn_in_actionPerformed(null);
			} 
			catch (LucteriosException e) {} 
			catch (FileNotFoundException e) {}
		}
	}
}