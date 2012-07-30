/**
 * 
 */
package com.example.bookstoremb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.bookstoremb.models.Book;
import com.example.bookstoremb.utils.Constants;
import com.example.bookstoremb.utils.Utils;

/**
 * @author Administrator
 *
 */
public class ContentActivity extends Activity {

  private Book book;
  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content);
		TextView name = (TextView) findViewById(R.id.name);
		TextView content = (TextView) findViewById(R.id.content);
		Bundle extras = getIntent().getExtras();
		book = new Book();
		if (extras != null) {
		  book.setBookId(extras.getString(Constants.BOOK_ID));
		  book.setName(extras.getString(Constants.BOOK_NAME));
		  book.setCategory(Utils.bookCategoryStringToEnum(extras.getString(Constants.BOOK_CATEGORY)));
		  book.setContent(extras.getString(Constants.BOOK_CONTENT));
    }
		name.setText(book.getName());
		content.setText(book.getContent());
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
