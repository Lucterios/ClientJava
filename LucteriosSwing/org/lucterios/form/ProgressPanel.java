package org.lucterios.form;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

public class ProgressPanel extends JComponent {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int MaxValue = 30;
	public Color progressColor=Color.GREEN;
	public Color backgroudColor=Color.LIGHT_GRAY;

	protected Thread  animation = null;
    protected boolean started = false;
    protected int value = 0;
    protected boolean increase;

	public ProgressPanel(boolean aIncrease)
    {
		increase=aIncrease;
		stop();
    }
	
	public void start()
    {
        if (animation == null) {
	        animation = new Thread(new Animator());
	        animation.setPriority(Thread.MAX_PRIORITY);
	        animation.start();
        }
    }

    public void stop()
    {
        setVisible(true);
    	value=-1;
        repaint();        
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
    
    public void paintComponent(Graphics g)
    {
        int width  = getWidth();
        int height = getHeight();
        int progress_step = (width/(MaxValue+1)); 
        
    	int begin=progress_step*value;
    	int end=progress_step*value+progress_step;
        
        if ((value>=0) && (value<=MaxValue))
        {
            g.setColor(backgroudColor);
            g.fillRect(0, 0, begin, height);
            g.fillRect(end, 0, width-end, height);
        	g.setColor(progressColor);
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
                repaint();
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
