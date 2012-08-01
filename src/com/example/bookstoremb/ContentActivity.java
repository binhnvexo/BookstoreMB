/**
 * 
 */
package com.example.bookstoremb;

import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.bookstoremb.adapter.BookstoreAdapter;
import com.example.bookstoremb.models.Author;
import com.example.bookstoremb.models.Book;
import com.example.bookstoremb.utils.Constants;
import com.example.bookstoremb.utils.RestClient;
import com.example.bookstoremb.utils.Utils;

/**
 * @author Administrator
 *
 */
public class ContentActivity extends Activity {

  private Book book;
  private String SEARCH_AUTHOR = "http://192.168.1.130:8080/rest/private/bookstore/searchAuthorByBookId/";
  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content);
		TextView name = (TextView) findViewById(R.id.name);
		TextView content = (TextView) findViewById(R.id.content);
		TextView category = (TextView) findViewById(R.id.category);
		TextView author = (TextView) findViewById(R.id.author);
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
		category.setText(Utils.bookCategoryEnumToString(book.getCategory()));
		content.setMovementMethod(new ScrollingMovementMethod());
		Author au = new Author();
		try {
		  RestClient rest = new RestClient(SEARCH_AUTHOR + book.getBookId());
      rest.execute(RestClient.RequestMethod.GET);
      if (rest.getResponseCode() == 200) {
        JSONObject json = new JSONObject(rest.getResponseStr());
        au = Utils.createAuthorFromJSON(json);
      }
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    }
		author.setText(au.getName());
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
