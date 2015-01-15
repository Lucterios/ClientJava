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

package org.lucterios.update;

import java.awt.BorderLayout;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JWindow;

public class WaitWindow extends JWindow 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WaitWindow(String aTitle) 
	{
		super();
        JEditorPane title = new JEditorPane();
        title.setBackground(getContentPane().getBackground());
        title.setEditable(false);
        title.setContentType("text/html");
        title.setText("<center><b>"+aTitle+"</b></center>");
        getContentPane().add(title, BorderLayout.NORTH);

		JLabel image = new JLabel(new javax.swing.ImageIcon(getClass().getResource("/install.png")));
        image.setAlignmentX(0.5f);
        image.setAlignmentY(0.5f);
        getContentPane().add(image, BorderLayout.CENTER);

        JEditorPane text = new JEditorPane();
        text.setBackground(getContentPane().getBackground());
        text.setEditable(false);
        text.setContentType("text/html");
        text.setText("<center>Installation en cours.<br>Veuillez patienter.</center>");
        getContentPane().add(text, BorderLayout.SOUTH);

        pack(); 
        setLocationRelativeTo(null); 	
		setVisible(true);
	}

}
