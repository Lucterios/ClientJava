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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

public class WaitingWindow extends JWindow
{
	private static final long serialVersionUID = 1L;
	private static final Color BACKGROUND_COLOR=Color.WHITE;  
	private static final Color FRAME_COLOR=Color.GREEN;  
	private static final Color PROGRESS_COLOR=Color.ORANGE;  
	
	private java.awt.Cursor mHourGlass=java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR);
	private String mTitle;
	private String mText;

	public WaitingWindow(String aText,String aTitle)
	{
		super();
		mTitle=aTitle;
		mText=aText;
		initial();
	}
	
	public WaitingWindow(String aText) 
	{
		super();
		mTitle="";
		mText=aText;
		initial();
	}
	
	private void initial()
	{
		setCursor(mHourGlass);
        setBackground(FRAME_COLOR);
		
		JPanel main=new JPanel();
		main.setBackground(FRAME_COLOR);
		main.setLayout(new BorderLayout());
        Border border = main.getBorder();
        Border margin = new EmptyBorder(3,3,3,3);
        main.setBorder(new CompoundBorder(border, margin));
        getContentPane().add(main, BorderLayout.NORTH);
		
		
        JEditorPane title = new JEditorPane();
        title.setBackground(BACKGROUND_COLOR);
        title.setEditable(false);
        title.setContentType("text/html");
        title.setText("<center><b>"+mTitle+"</b></center>");
        main.add(title, BorderLayout.NORTH);


        GifAnime image = new GifAnime(new javax.swing.ImageIcon(getClass().getResource("resources/attente-optim.gif")));
        image.setBackground(BACKGROUND_COLOR);
        image.setLayout(new GridBagLayout());
        main.add(image, BorderLayout.CENTER);

        String[] lines=mText.split("<br>");
        int index=0;
        for(String line:lines) {
	        JLabel text = new JLabel();
	        text.setBackground(BACKGROUND_COLOR);
	        text.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
	        text.setText(line);
	        GridBagConstraints cnt=new GridBagConstraints();
	        cnt.gridx=0;
	        cnt.gridy=index++;
	        image.add(text, cnt);
        }
        
        ProgressPanel progress= new ProgressPanel(true);
        progress.setPreferredSize(new Dimension(10,10));        
        progress.backgroudColor=BACKGROUND_COLOR;
        progress.progressColor=PROGRESS_COLOR;
        progress.MaxValue=7;
        main.add(progress, BorderLayout.SOUTH);        
        
        pack(); 
        Toolkit k = Toolkit.getDefaultToolkit();
        Dimension tailleEcran = k.getScreenSize();
        Dimension taille=getSize();
        setLocation((tailleEcran.width-taille.width)/2, (tailleEcran.height-taille.height)*4/5);
        progress.start();
    }
}
