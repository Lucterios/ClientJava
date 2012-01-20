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

import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIGraphic;

public class GifAnime implements GUIContainer.Redrawing {
    
    private AbstractImage monImage;
	private GUIContainer mPanel;
    
    /** Creates new form GifAnime */
    public GifAnime(AbstractImage img,GUIContainer panel) {
        this.monImage = img;
        this.mPanel=panel;
        mPanel.setBackgroundColor(0xFFFFFF);
        mPanel.setSize(monImage.getWidth(), monImage.getHeight());
        mPanel.setRedraw(this);
    }
        
	public void paint(GUIGraphic g) {
	      // efface le contenu précédent :
	      g.setColor(0xFFFFFF);
	      g.fillRect(0, 0, this.monImage.getWidth(), this.monImage.getHeight());
	      // dessine le gif :
	      g.drawImage(this.monImage, 0, 0, this.monImage.getWidth(), this.monImage.getHeight(), this.mPanel);
	}
}
