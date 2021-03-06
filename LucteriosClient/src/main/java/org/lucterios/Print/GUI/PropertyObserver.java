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

import org.lucterios.Print.Data.*;
import org.lucterios.Print.Observer.*;
import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUILabel;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.gui.GUIParam.ReSizeMode;
        
/**
 *
 * @author  lg
 */
public class PropertyObserver extends PropertyPanel implements PrintObserverAbstract
{
    private PrintManager mPrintManager;
	
    /** Creates new form PropertyObserver 
     * @param guiContainer */
    public PropertyObserver(PrintManager aPrintManager, GUIContainer guiContainer)
    {
    	super(guiContainer);
        mPrintManager=aPrintManager;
        mPrintManager.addObserver(this);
        initComponents();
        refresh();
    }

    private void initComponents() {
        GUIParam param;

        param=new GUIParam(0,0,3,1,ReSizeMode.RSM_NONE,FillMode.FM_NONE);
        lblProperty = mContainer.createHyperText(param);
        lblProperty.setTextString("<b><u>Propriétés</u></b>");
        
        param=new GUIParam(0,1,3,1,ReSizeMode.RSM_NONE,FillMode.FM_NONE);
        lblTitle = mContainer.createHyperText(param);
        
        param=new GUIParam(0,50,3,1);
        lblAligne = mContainer.createLabel(param);

        mContainer.setSize(200, 350);
        mContainer.setMinimumSize(200, 200);
    }

    private void initModel()
    {
        addEdit(2,"Largeur de page (mm)","page_width",null);
        addEdit(3,"Hauteur de page (mm)","page_height",null);
        addEdit(4,"Marge haute (mm)","margin_top",null);
        addEdit(5,"Marge basse (mm)","margin_bottom",null);
        addEdit(6,"Marge droite (mm)","margin_right",null);
        addEdit(7,"Marge gauche (mm)","margin_left",null);
    }

    private void initArea()
    {
        int posY=2;
        if (!mCurrent.getNoAttributProperty().contains("extent"))
            addEdit(posY++,"Ampleur (mm)","extent",null);
        if (!mCurrent.getNoAttributProperty().contains("data"))
            addEdit(posY++,"Donnée liée","data",mCurrent.getSelection());
    }
    
    private void initContainer()
    {
        addEdit(2,"Largeur (mm)","width",null);
        addEdit(3,"Hauteur (mm)","height",null);
        addEdit(4,"Position haute (mm)","top",null);
        addEdit(5,"Position gauche (mm)","left",null);
        addEdit(6,"Remplissage (mm)","padding",null);
        addEdit(7,"Espacement (mm)","spacing",null);
    }

    private void initAbstractText(int posY,String subName)
    {
        addEdit(posY++,"Alignement",subName+"print_text%text_align",new SelectData[]{new SelectData("Gauche%left"),new SelectData("Centr%center"),new SelectData("Droite%right"), new SelectData("Debut%start") , new SelectData("Fin%end") , new SelectData("Justifi%justify") });
        addEdit(posY++,"Font",subName+"print_text%font_family",new SelectData[]{new SelectData("sans-serif") , new SelectData("serif") , new SelectData("Times") , new SelectData("Courier") , new SelectData("Helvetica") , new SelectData("Lucida Sans") });
        addEdit(posY++,"Taille",subName+"print_text%font_size",null);
        addEdit(posY++,"Hauteur de ligne",subName+"print_text%line_height",null);
    }
    
    private void initImage()
    {
        initContainer();
        addEdit(8,"Image","picture",null);
    }

    private void initText()
    {
        initContainer();
        initAbstractText(8,"");
    }

    private void initColumn()
    {
        addEdit(2,"Largeur (mm)","width",null);
        addEdit(3,"Donnée liée","data",mCurrent.getSelection());
        addEdit(4,"Couleur de Bordure","cell%border_color",new SelectData[]{new SelectData("Noire%black"),new SelectData("Bleu%blue"),new SelectData("Rouge%red"),new SelectData("Vert%green")});
        addEdit(5,"Style de Bordure","cell%border_style",new SelectData[]{new SelectData("Normal%"),new SelectData("Solide%solid")});
        addEdit(6,"Epaisseur de Bordure (mm)","cell%border_width",null);
        addEdit(7,"Alignement vert.","display_align",new SelectData[]{new SelectData("before"),new SelectData("center"),new SelectData("after")});
        initAbstractText(8,"cell%");
    }

    private void initRow()
    {
        addEdit(3,"Donnée liée","data",mCurrent.getSelection());
    }

    private void initCell()
    {
        addEdit(2,"Donnée liée","data",mCurrent.getSelection());
        addEdit(3,"Couleur de Bordure","border_color",new SelectData[]{new SelectData("Noire%black"),new SelectData("Bleu%blue"),new SelectData("Rouge%red"),new SelectData("Vert%green")});
        addEdit(4,"Style de Bordure","border_style",new SelectData[]{new SelectData("Normal%"),new SelectData("Solide%solid")});
        addEdit(5,"Epaisseur de Bordure (mm)","border_width",null);
        addEdit(6,"Alignement vert.","display_align",new SelectData[]{new SelectData("before"),new SelectData("center"),new SelectData("after")});
        initAbstractText(7,"");
    }

    private void initTable()
    {
        initContainer();
        PrintTable tbl=(PrintTable)mCurrent;
        GUIParam param;
        
        GUILabel lbl_cmp_title;
        param=addComponent(8,"Colonnes",2);
        lbl_cmp_title = mContainer.createLabel(param);
        lbl_cmp_title.setTextString(new Integer(tbl.columns.size()).toString());
        
        param=addComponent(9,"lignes",2);
        lbl_cmp_title = mContainer.createLabel(param);
        lbl_cmp_title.setTextString(new Integer(tbl.rows.size()).toString());
    }
    
    public void refresh()
    {
		if (!mDataChanging)
			try
			{
				mDataChanging=true;
		        clear();
		        mCurrent=mPrintManager.getCurrent();
		        if (mCurrent!=null)
		            refreshCurrent();
		        mContainer.repaint();       
			}
		finally
		{
			mDataChanging=false;
		}		
    }

	private void refreshCurrent() 
	{
		lblTitle.setTextString("<i>"+mCurrent.toString()+"</i>");
		if (PrintPage.class.isInstance(mCurrent))
		    initModel();
		else if (PrintArea.class.isInstance(mCurrent))
		    initArea();
		else if (PrintImage.class.isInstance(mCurrent))
		    initImage();
		else if (PrintText.class.isInstance(mCurrent))
		    initText();
		else if (PrintTable.class.isInstance(mCurrent))
		    initTable();
		else if (PrintColumn.class.isInstance(mCurrent))
		    initColumn();
		else if (PrintRow.class.isInstance(mCurrent))
		    initRow();
		else if (PrintCell.class.isInstance(mCurrent))
		    initCell();
	}

    public void beforeChangeCurrentEvent()
    {
        mCurrent=mPrintManager.getCurrent();
        readData();
        mPrintManager.DataChanged();
    }

    public void ModifyEvent()
    {
        refresh();
    }

    public void changeCurrentEvent()
    {
        refresh();
    }

    public void addItemEvent()
    {
        clear();
    }

    public void deleteCurrentEvent()
    {
        clear();
    }

    private boolean mDataChanging=false;
    
	public void focusLost(GUIComponent origine,GUIComponent target) 
	{
		if (!mDataChanging)
		try
		{
			mDataChanging=true;
			beforeChangeCurrentEvent();	
		}
		finally
		{
			mDataChanging=false;
		}		
	}

}
