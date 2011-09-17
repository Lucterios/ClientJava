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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.lucterios.ui.GUIActionListener;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.Tools;
import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUICheckBox;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIFrame;
import org.lucterios.gui.GUIMemo;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.gui.GUIFrame.FrameVisitor;

/**
 *
 * @author  lg
 */
public class DemoPrint implements FrameVisitor 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private MainPrintPanel newContentPane;
    private GUIContainer scrCode;
    private GUIMemo CodeEditor;
    private GUIMemo XmlDataEditor;

    private GUIContainer pnl_main;
    private GUIContainer pnl_bnt;
    private GUIButton btn_in;
    private GUIButton btn_out;
    private GUICheckBox cb_WithText;

    public String modelFileName="";
    public String xmlFileName="";
    
    public DemoPrint()
    {
        super();     
    }
    
	public void execute(GUIFrame mainFrame) {
        mainFrame.setTextTitle("Exemple");
        GUIParam param;

        pnl_main=mainFrame.getContainer();

        param=new GUIParam(0,4,2,1);
        newContentPane = new MainPrintPanel(pnl_main.createContainer(ContainerType.CT_NORMAL, param));

        param=new GUIParam(0,0,1,3);
        scrCode=pnl_main.createContainer(ContainerType.CT_SCROLL, param);
        CodeEditor=scrCode.createMemo(new GUIParam(0,0));
        CodeEditor.setText("");

        param=new GUIParam(1,2,1,1);
        pnl_bnt=pnl_main.createContainer(ContainerType.CT_NORMAL, param);
        
        param=new GUIParam(0,0);
        btn_in=pnl_bnt.createButton(param);
        btn_in.setTextString("Import");
        btn_in.addActionListener(new GUIActionListener() {		
			public void actionPerformed() {
				btn_in_actionPerformed();
			}
		});

        param=new GUIParam(1,0);
        btn_out=pnl_bnt.createButton(param);
        btn_out.setTextString("Export");
        btn_out.addActionListener(new GUIActionListener() {
            public void actionPerformed() 
            {
            	btn_out_actionPerformed();
            }
        });

        param=new GUIParam(0,1,3,1);
        cb_WithText=pnl_bnt.createCheckBox(param);
        cb_WithText.setTextString("With text export ?");
                
        param=new GUIParam(1,0,1,2);
        GUIContainer xmlCode=pnl_main.createContainer(ContainerType.CT_SCROLL, param);
        XmlDataEditor=xmlCode.createMemo(new GUIParam(0,0));
        XmlDataEditor.setText("<DATA>\n</DATA>");
        
        mainFrame.getGenerator().invokeLater(new Runnable() {
			public void run() {
		        loadModel();
			}
		});
    }

    public void btn_out_actionPerformed() 
    {
        CodeEditor.setText(newContentPane.getManager().getPage().Save());
    }
    
    public void btn_in_actionPerformed() 
    {
        try
        {
            newContentPane.getData(CodeEditor.getValue(),XmlDataEditor.getValue());
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

	private void loadModel() {
		try {
			File f_model=new File(modelFileName);
			File f_xml=new File(xmlFileName);
			if (f_model.isFile() && f_xml.isFile())
			{
				InputStream is_model = new FileInputStream(f_model);
				InputStream is_xml=new FileInputStream(f_xml);
			    CodeEditor.setText(Tools.parseISToString(is_model));
			    XmlDataEditor.setText(Tools.parseISToString(is_xml));
			    btn_in_actionPerformed();
			}
		} 
		catch (Exception e) {
        	ExceptionDlg.throwException(e);
		} 
	}
    
    public static void main(String args[]) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
    	ExceptionDlg.mGenerator=Tools.findGenerator("org.lucterios.swing.SGenerator");
		try {
	        if (args.length>1) {
	        	GUIFrame frame=ExceptionDlg.mGenerator.getFrame();
		        DemoPrint demo=new DemoPrint();
		        if ((args.length==2) || (args.length==3)) {
		        	demo.modelFileName=args[0];
		        	demo.xmlFileName=args[0];
		        }
		        frame.setFrameVisitor(demo);
		        frame.setVisible(true);
	        }
	        else
	        	throw new LucteriosException("Minimum 1 arguments");
		} 
		catch (Throwable e) {
        	ExceptionDlg.throwException(e);
		} 
    }

}