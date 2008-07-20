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

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.lucterios.Print.FopGenerator.ClosePreview;
import org.lucterios.utils.LucteriosException;

public class ModelApplet extends Applet implements ClosePreview 
{
	private static final long serialVersionUID = 1L;
	final static public String ENCODE="iso-8859-1";
    
	private JTabbedPane PnlTab;
	private JPanel MainPnl;
	private MainPrintPanel newContentPane;
	private JButton preview;
	
    public void init()
    {
		setLayout(new BorderLayout());
		MainPnl=new JPanel();
		PnlTab=new JTabbedPane();
		add(PnlTab,BorderLayout.CENTER);
		PnlTab.addTab("Model",MainPnl);
		MainPnl.setLayout(new BorderLayout());
		
        newContentPane = new MainPrintPanel();
        MainPnl.add(new JScrollPane(newContentPane),BorderLayout.CENTER);
        
        preview=new JButton();
        preview.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event)
			{
				preview();
			}
        });
        preview.setText("Previsualisation");
        MainPnl.add(preview,BorderLayout.SOUTH);        
    }

	private void preview()
	{
    	try 
    	{
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	    	try 
	    	{
	    		while (PnlTab.getTabCount()>1)
	    			PnlTab.remove(1);
	    		String print_xml=getText();
	    		ModelConverter model=new ModelConverter(print_xml,"");
	    		model.Run();
	    		String print_pre_fop=model.toXap(xml_code,"");
	    		FopGenerator fop_generator=new FopGenerator(print_pre_fop,"Exemple",false);
	    		JPanel pnl_view=fop_generator.viewFO(this);
	    		if (pnl_view==null) {
	    			PnlTab.addTab("Preview",new JScrollPane(pnl_view));
	    			PnlTab.setSelectedIndex(1);
	    		}
	    	} finally {
	    		setCursor(Cursor.getDefaultCursor());
	    	}
		} catch (LucteriosException e) {
    		e.printStackTrace();
		}
	}
	
    private String xml_code="";
    private String xml_model="";
    public void start()
    {
        try
        {
            xml_code=java.net.URLDecoder.decode(this.getParameter("xml_code").trim(),ENCODE);
            xml_model=java.net.URLDecoder.decode(this.getParameter("xml_model").trim(),ENCODE);
            newContentPane.getData(xml_model,xml_code);
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

    public String getText()
    {
        return newContentPane.getManager().getPage().Save();
    }

	public void close() {
		while (PnlTab.getTabCount()>1)
			PnlTab.remove(1);
		PnlTab.setSelectedIndex(0);	
	}
}
