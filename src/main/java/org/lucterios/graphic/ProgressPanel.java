package org.lucterios.graphic;

import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIGraphic;

public class ProgressPanel implements GUIContainer.Redrawing {

    public int MaxValue = 30;
	public int progressColor=-1;
	public int backgroudColor=0xC0C0C0;

	protected Thread  animation = null;
    protected boolean started = false;
    protected int value = 0;
    protected boolean increase;
    protected GUIContainer mContainer;
    protected int curentprogressColor;

	public ProgressPanel(boolean aIncrease,GUIContainer container)
    {
		mContainer=container;
		mContainer.setRedraw(this);
		increase=aIncrease;
		stop();
    }
	
	public void start()
    {
		curentprogressColor=progressColor;
		if (curentprogressColor==-1)
			curentprogressColor=mContainer.getSelectColor();
    	mContainer.repaint();        
        if (animation == null) {
	        animation = new Thread(new Animator());
	        animation.setPriority(Thread.MAX_PRIORITY);
	        animation.start();
        }
    }

    public void stop()
    {
    	mContainer.setVisible(true);
    	value=-1;
    	mContainer.repaint();        
        if (animation != null) {
	        animation.interrupt();
	        animation = null;
        }
    }	
    
    public void setActive(boolean aIsActive) 
    {
		if (aIsActive)
			stop();
		else
			start();
    }
    
    public void paint(GUIGraphic g)
    {
        int width  = mContainer.getSizeX();
        int height = mContainer.getSizeY();
        int progress_step = (width/(MaxValue+1)); 
        
    	int begin=progress_step*value;
    	int end=progress_step*value+progress_step;
        
        if ((value>=0) && (value<=MaxValue))
        {
            g.setColor(backgroudColor);
            g.fillRect(0, 0, begin, height);
            g.fillRect(end, 0, width-end, height);
        	g.setColor(curentprogressColor);
        	g.fillRect(begin,0,progress_step,height);
        }
        else
        {
            g.setColor(backgroudColor);
            g.fillRect(0, 0, width, height);            	
        }
    }
    
    protected class Animator implements Runnable
    {

        protected Animator()
        {
        }

        public void run()
        {
            started = true;
        	if (increase)
        		value=0;
        	else
            	value=MaxValue;
            while (!Thread.interrupted())
            {
            	if (increase)
            		value++;
            	else
            		value--;
            	if (value>MaxValue)
            		value=0;
            	if (value<0)
            		value=MaxValue;
            	mContainer.invokeLater(new Runnable() {				
					public void run() {
		            	mContainer.repaint();
					}
				});
                try
                {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                    break;
                }
                Thread.yield();
            }
            started = false;
        }
    }
}
