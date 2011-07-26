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

import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIHyperText;
import org.lucterios.gui.GUILabel;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUIWindows;
import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.gui.GUIParam.ReSizeMode;
import org.lucterios.gui.GUIWindows.WindowVisitor;

public class WaitingWindow implements WindowVisitor
{
	private static final long serialVersionUID = 1L;
	private static final int BACKGROUND_COLOR=0xFFFFFF;  
	//private static final int FRAME_COLOR=0xFF0000;  
	private static final int PROGRESS_COLOR=0xFFC800;  
	
	private String mTitle;
	private String mText;
	private GUIWindows mWindows;

	public WaitingWindow(String aText,String aTitle)
	{
		super();
		mTitle=aTitle;
		mText=aText;
	}

	public void execute(GUIWindows windows)
	{
		mWindows=windows;
		initial();
	}
	
	private void initial()
	{
		mWindows.setWaitingCursor();
		GUIParam param=new GUIParam(0,0,1,1,ReSizeMode.RSM_HORIZONTAL,FillMode.FM_HORIZONTAL);
		param.setPad(0);

		GUIContainer main=mWindows.getContainer();
        GUIHyperText title = main.createHyperText(param);
        title.setBackgroundColor(BACKGROUND_COLOR);
        title.setTextString("<center><b>"+mTitle+"</b></center>");

        param.setY(1);
        GUIContainer imgPnl = main.createContainer(ContainerType.CT_NORMAL,param);
        imgPnl.setBackgroundColor(BACKGROUND_COLOR);
        new GifAnime(mWindows.getGenerator().CreateImage(getClass().getResource("attente-optim.gif")),imgPnl);

        String[] lines=mText.split("<br>");
        int index=0;
        for(String line:lines) {
	        GUILabel text = imgPnl.createLabel(new GUIParam(0,index++,1,1,ReSizeMode.RSM_NONE,FillMode.FM_NONE));
	        text.setBackgroundColor(BACKGROUND_COLOR);
	        text.setTextString(line);
        }
        
        param.setY(2);
        GUIContainer progPnl = main.createContainer(ContainerType.CT_NORMAL,param);
        ProgressPanel progress= new ProgressPanel(true,progPnl);
        progress.backgroudColor=BACKGROUND_COLOR;
        progress.progressColor=PROGRESS_COLOR;
        progress.MaxValue=7;
        progress.start();
    }
}
