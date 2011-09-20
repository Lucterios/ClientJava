package org.lucterios.android;

import org.lucterios.android.widget.WGenerator;

import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIGenerator;
import org.lucterios.gui.test.DialogExample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.Toast;

public class Main extends Activity {

	private GUIGenerator mGenerator;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mGenerator=new WGenerator();
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	MenuItem disconnect=menu.add("Déconnecter");
    	disconnect.setOnMenuItemClickListener(new OnMenuItemClickListener(){
			public boolean onMenuItemClick(MenuItem item) {
				Toast.makeText(Main.this, "Déconnecter", Toast.LENGTH_SHORT).show();
				return true;
			}    		
    	});
    	MenuItem help=menu.add("Aide...");
    	help.setOnMenuItemClickListener(new OnMenuItemClickListener(){
			public boolean onMenuItemClick(MenuItem item) {
				GUIDialog dlg=mGenerator.newDialog(null);
				dlg.setDialogVisitor(new DialogExample());
				dlg.setVisible(true);
				return true;
			}    		
    	});
    	MenuItem about=menu.add("A propos...");
    	about.setOnMenuItemClickListener(new OnMenuItemClickListener(){
			public boolean onMenuItemClick(MenuItem item) {
				Toast.makeText(Main.this, "A propos", Toast.LENGTH_SHORT).show();
				return true;
			}    		
    	});
    	MenuItem exit=menu.add("Quitter");
    	exit.setOnMenuItemClickListener(new OnMenuItemClickListener(){
			public boolean onMenuItemClick(MenuItem item) {
				System.exit(0);
				return true;
			}    		
    	});
		return true;
	}
   
}