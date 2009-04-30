package org.lucterios.utils.graphic;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MemoryJauge extends Component implements ActionListener,MouseListener {

	private static final long serialVersionUID = 1L;

	private int currentHeapPercent;
	
	private int currentUsedPercent;
	
	private int cellWidth = 3;

	private int cellSpace = 2;
	
	private Runtime mRuntime;
	public MemoryJauge() {
	    super();
		mRuntime = Runtime.getRuntime();
		this.addMouseListener(this);
	}

	public void actionPerformed(ActionEvent arg0) {
		refreshMemoryInformations();
	}	
	
	public void refreshMemoryInformations() {
		long maxHeap         = mRuntime.maxMemory();
		long currentHeapSize = mRuntime.totalMemory();
		long used            = currentHeapSize-mRuntime.freeMemory();
		int newPercent = (int) ((used * 100) / maxHeap);
		currentHeapPercent = (int) ((currentHeapSize * 100) / maxHeap);
		if (newPercent != currentUsedPercent){
			currentUsedPercent = newPercent;
			repaint();
		}
	}

	public void paint(java.awt.Graphics g) {
	      super.paint(g);
	      Dimension size = getSize();
	      int current = (size.width * currentHeapPercent)/100;

	      g.setColor(new Color(50,50,50));
	      g.fillRect(0,0, current, size.height);

	      g.setColor(Color.BLACK);
	      g.fillRect(current, 0, size.width-current,size.height);
	      
	      int rectLimit = (size.width* currentUsedPercent)/100;
	      int redLimit = 75;
	      if (currentUsedPercent > redLimit){
	         redLimit = currentUsedPercent;
	      }
	      redLimit  = (size.width* redLimit)/100;
	      Color fillColor;
	      Color emptyColor;
	      Color redColor = new Color(128,0,0);
	      if (currentUsedPercent > 75){
	         fillColor = Color.RED;
	         emptyColor = new Color(128,0,0);
	      }
	      else {
	         fillColor = Color.GREEN;
	         emptyColor = new Color(0,128,0);
	      }
	      g.setColor(fillColor);
	      int  currentZone = 0;
	      int x = 2;
	      int drawLimit = size.width - 2;
	      int maxY = size.height - 4;
	      int roundW = cellWidth / 3;
	      int roundH = maxY / 3;
	      while (x < drawLimit){
	         int zone = 0;
	         if (x > redLimit){
	            zone = 2;
	         }
	         else if (x > rectLimit){
	            zone = 1;
	         }
	         if (zone != currentZone){
	            currentZone = zone;
	            if (zone == 1){
	               g.setColor(emptyColor);
	            }
	            else {
	              g.setColor(redColor);
	            }
	         }
	         g.fillRoundRect(x, 2,cellWidth, maxY, roundW, roundH);
	         x +=cellWidth + cellSpace;
	      }
	}

	public void mouseClicked(MouseEvent aEvent) {
		if (aEvent.getClickCount()==2) {
			org.lucterios.utils.Tools.clearGC();
		}
	}

	public void mouseEntered(MouseEvent aEvent) {}

	public void mouseExited(MouseEvent aEvent) {}

	public void mousePressed(MouseEvent aEvent) {}

	public void mouseReleased(MouseEvent aEvent) {}
}
