/**
 * 
 */
package com.example.bookstoremb;

import com.example.bookstoremb.utils.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
public class ContentActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content);
		TextView str = (TextView) findViewById(R.id.name);
		str.setText("Hello world");
	}

	/* (non-Javadoc)
   * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(menu.NONE, Constants.MENU_BACK, menu.NONE, R.string.menu_back);
    return super.onCreateOptionsMenu(menu);
  }

  /* (non-Javadoc)
   * @see android.app.Activity#onMenuItemSelected(int, android.view.MenuItem)
   */
  @Override
  public boolean onMenuItemSelected(int featureId, MenuItem item) {
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
    return super.onMenuItemSelected(featureId, item);
  }

}
