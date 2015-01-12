package org.lucterios.graphic;

import java.util.Date;

import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIGraphic;
import org.lucterios.ui.GUIActionListener;

public class MemoryJauge implements GUIActionListener,GUIContainer.Redrawing {

	private static final long TIME_TO_REFRESH = 60*1000; // 1min

	private int currentHeapPercent;
	
	private int currentUsedPercent;
	
	private int cellWidth = 3;

	private int cellSpace = 2;
	
	private Runtime mRuntime;
	
	private GUIContainer mContainer;
	private Date mLastDate=new Date();
	
	public MemoryJauge(GUIContainer container) {
	    super();
	    mContainer=container;
	    mContainer.addActionListener(this);
	    mContainer.setRedraw(this);
		mRuntime = Runtime.getRuntime();
	}

	public void actionPerformed() {
		Date current=new Date();
		if ((current.getTime()-mLastDate.getTime())>TIME_TO_REFRESH) {
			org.lucterios.utils.Tools.clearGC();
			refreshMemoryInformations();
			mLastDate=new Date();
		}
	}	

	public void refreshMemoryInformations() {
		final long maxHeap         = mRuntime.maxMemory();
		final long currentHeapSize = mRuntime.totalMemory();
		final long used            = currentHeapSize-mRuntime.freeMemory();
		int newPercent = (int) ((used * 100) / maxHeap);
		currentHeapPercent = (int) ((currentHeapSize * 100) / maxHeap);
		if (newPercent != currentUsedPercent){
			currentUsedPercent = newPercent;
			mContainer.invokeLater(new Runnable(){
				public void run() {
					mContainer.setToolTipText((used/(1024*1024))+"Mo/"+(maxHeap/(1024*1024))+"Mo"); 
					mContainer.repaint();
				}				
			});
		}
	}

	public void paint(GUIGraphic g) {
	      mContainer.getSizeX();
	      int current = (mContainer.getSizeX() * currentHeapPercent)/100;

	      g.setColor(0x323232);
	      g.fillRect(0,0, current, mContainer.getSizeY());

	      g.setColor(0x000000);
	      g.fillRect(current, 0, mContainer.getSizeX()-current,mContainer.getSizeY());
	      
	      int rectLimit = (mContainer.getSizeX()* currentUsedPercent)/100;
	      int redLimit = 75;
	      if (currentUsedPercent > redLimit){
	         redLimit = currentUsedPercent;
	      }
	      redLimit  = (mContainer.getSizeX()* redLimit)/100;
	      int fillColor;
	      int emptyColor;
	      int redColor = 0x800000;
	      if (currentUsedPercent > 75){
	         fillColor = 0xFF0000;
	         emptyColor = 0x800000;
	      }
	      else {
	         fillColor = 0x00FF000;
	         emptyColor = 0x008000;
	      }
	      g.setColor(fillColor);
	      int  currentZone = 0;
	      int x = 2;
	      int drawLimit = mContainer.getSizeX() - 2;
	      int maxY = mContainer.getSizeY() - 4;
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

}
