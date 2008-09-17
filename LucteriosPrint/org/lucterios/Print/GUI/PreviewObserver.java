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

package org.lucterios.Print.GUI;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

import org.lucterios.Print.Data.PrintAbstract;
import org.lucterios.Print.Data.PrintPage;
import org.lucterios.Print.Data.PrintVector;
import org.lucterios.Print.Observer.PrintManager;
import org.lucterios.Print.Observer.PrintObserverAbstract;
import org.lucterios.utils.graphic.SpinEdit;


public class PreviewObserver extends JPanel implements PrintObserverAbstract,PrintPanelBase.ObserverCallBack 
{
	private static final long serialVersionUID = 1L;
	private PrintManager mPrintManager;
    /** Creates new form BrowserObserver */
    public PreviewObserver(PrintManager aPrintManager) 
    {
        super();
        mRefreshing=false;
        mPrintManager=aPrintManager;
        mPrintManager.addObserver(this);
        initComponents();
        initSheet();
        initPage();        
        refresh();
    }

    private JPanel mTopPanel; 
    private JLabel mlblEchelle;
    private JLabel mlblPages;
    private SpinEdit mEchelles;
    private JComboBox mPages;
    private JPanel mMain; 

    private PrintPanelBase mSheet; 
    private PrintPanelBase mHeader; 
    private PrintPanelBase mFooter; 
    private PrintPanelBase mRight; 
    private PrintPanelBase mLeft; 
    private PrintPanelBase mBody; 

    
    private void initComponents() 
    {
        GridBagConstraints gridBagConstraints;
    	
        setLayout(new java.awt.BorderLayout());
        mTopPanel=new JPanel();
        mTopPanel.setLayout(new GridBagLayout());
        mTopPanel.setOpaque(false);
    	
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
        mTopPanel.add(new JLabel(), gridBagConstraints);
    	
        mlblEchelle = new JLabel();
        mlblEchelle.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        mlblEchelle.setText("Echelle");
        mlblEchelle.setOpaque(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(1,3,3,1);
        mTopPanel.add(mlblEchelle, gridBagConstraints);
        mEchelles= new SpinEdit(100,1,1000);
        mEchelles.setName("mEchelles");
        mEchelles.setMinimumSize(new java.awt.Dimension(70,20));
        mEchelles.setPreferredSize(new java.awt.Dimension(70,20));
        mEchelles.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) 
			{
				changeEchelle();
			}
		});
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
        mTopPanel.add(mEchelles, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
        mTopPanel.add(new JLabel(), gridBagConstraints);
        
        mlblPages = new JLabel();
        mlblPages.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        mlblPages.setText("Pages");
        mlblPages.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(1,3,3,1);
        mTopPanel.add(mlblPages, gridBagConstraints);
        mPages= new JComboBox();
        mPages.setName("mPages");
        mPages.setMinimumSize(new java.awt.Dimension(75,20));
        mPages.setPreferredSize(new java.awt.Dimension(75,20));
        mPages.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) 
			{
				changePageSelected();
			}
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
        mTopPanel.add(mPages, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
        mTopPanel.add(new JLabel(), gridBagConstraints);
        
        add(mTopPanel, java.awt.BorderLayout.NORTH);
        
        mMain=new JPanel();
        mMain.setBackground(Color.GRAY);
        mMain.setBorder(new BevelBorder(BevelBorder.LOWERED));
        mMain.setLayout(new GridBagLayout());
        add(new JScrollPane(mMain), java.awt.BorderLayout.CENTER);
        
        setOpaque(false);
        setMinimumSize(new java.awt.Dimension(200, 250));
        setPreferredSize(new java.awt.Dimension(400, 300));
    }

    private void addBorder(int x, int y, int width)
    {
    	GridBagConstraints gridBagConstraints = new GridBagConstraints();
	    gridBagConstraints.gridx = x;
	    gridBagConstraints.gridy = y;
	    gridBagConstraints.gridwidth = width;
	    gridBagConstraints.weightx = 1.0;
	    JLabel border=new JLabel();
	    border.setMinimumSize(new java.awt.Dimension(25, 25));
	    mMain.add(border, gridBagConstraints);
    }
    
    private void initSheet()
    {
        GridBagConstraints gridBagConstraints;
        mSheet=new PrintPanelBase(this);
        mSheet.setLayout(new java.awt.BorderLayout());
        mHeader=new PrintPanelBase(this);
        mSheet.add(mHeader,java.awt.BorderLayout.NORTH);
        mFooter=new PrintPanelBase(this); 
        mSheet.add(mFooter,java.awt.BorderLayout.SOUTH);
        mRight=new PrintPanelBase(this); 
        mSheet.add(mRight,java.awt.BorderLayout.EAST);
        mLeft=new PrintPanelBase(this); 
        mSheet.add(mLeft,java.awt.BorderLayout.WEST);
        mBody=new PrintPanelBase(this); 
        mSheet.add(mBody,java.awt.BorderLayout.CENTER);

        addBorder(0,0,3);
        addBorder(0,2,3);
        addBorder(0,1,1);
        addBorder(2,1,1);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        mMain.add(mSheet, gridBagConstraints);
    }

    private void initPage()
    {
        PrintPage page=mPrintManager.getPage();
        mSheet.setPrintType(PrintPanelBase.PAGE);
        mHeader.setPrintType(PrintPanelBase.NORTH);
        mFooter.setPrintType(PrintPanelBase.SOUTH);
        mRight.setPrintType(PrintPanelBase.EAST);
        mLeft.setPrintType(PrintPanelBase.WEST);
        mBody.setPrintType(PrintPanelBase.CENTER);

        mSheet.setPrintObject(page);
	    mHeader.setPrintObject(page);
	    mFooter.setPrintObject(page);
	    mRight.setPrintObject(page); 
	    mLeft.setPrintObject(page);
	    mBody.setPrintObject(page);
    }

	public void refresh() 
	{
		PrintPage page=mPrintManager.getPage();
		mRefreshing=true;
		try
		{
			mPages.removeAllItems();
			PrintVector page_list=page.body;
			for(int index=0;index<page_list.size();index++)
				mPages.addItem(page_list.get(index));
		}
		finally
		{
			mRefreshing=false;
		}
		initialize();
		changePageSelected();
		changeEchelle();
	}

	public void beforeChangeCurrentEvent() {}

	public void changeCurrentEvent() 
	{
		mRefreshing=true;
		try
		{
			int num=mPrintManager.getBodyNumber();
			if (num!=-1)
				mPages.setSelectedIndex(num);
		    mBody.setBodyNum(num); 
		}
		finally
		{
			mRefreshing=false;
		}
		mSheet.setVisitObject(mPrintManager.getCurrent());
	    mHeader.setVisitObject(mPrintManager.getCurrent());
	    mFooter.setVisitObject(mPrintManager.getCurrent());
	    mRight.setVisitObject(mPrintManager.getCurrent()); 
	    mLeft.setVisitObject(mPrintManager.getCurrent()); 
	    mBody.setVisitObject(mPrintManager.getCurrent());
	    ModifyEvent();
	}

	public void addItemEvent() 
	{
        refresh();
	}

	public void deleteCurrentEvent() 
	{
        refresh();
	}

	public void initialize() 
	{
		mSheet.initialize();
	    mHeader.initialize();
	    mFooter.initialize();
	    mRight.initialize(); 
	    mLeft.initialize(); 
	    mBody.initialize(); 
	}
	
	public void ModifyEvent() 
	{
		mSheet.refresh();
	    mHeader.refresh();
	    mFooter.refresh();
	    mRight.refresh(); 
	    mLeft.refresh(); 
	    mBody.refresh(); 
	}

	private boolean mRefreshing;
	private void changePageSelected()
	{
		if (!mRefreshing)
		{
			mRefreshing=true;
			try
			{
		        PrintAbstract body=(PrintAbstract)mPages.getSelectedItem();
				mPrintManager.setCurrent(body);
			}
			finally
			{
				mRefreshing=false;
			}
		}
	}
	
	private void changeEchelle()
	{
		int num=(int)mEchelles.getNumber();
		mSheet.setSizeEchelle(num);
	    mHeader.setSizeEchelle(num);
	    mFooter.setSizeEchelle(num);
	    mRight.setSizeEchelle(num); 
	    mLeft.setSizeEchelle(num); 
	    mBody.setSizeEchelle(num); 
	    changeCurrentEvent();
	}

	PrintPanelAbstract mCurrent;
	public void setCurrent(PrintPanelAbstract newCurrent) 
	{
		mCurrent=newCurrent;		
	}
	
	public void selectObject(PrintAbstract printObject)
	{
		mPrintManager.setCurrent(printObject);
	}
}

