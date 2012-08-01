package com.example.bookstoremb;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.conn.routing.RouteInfo.TunnelType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.layout;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bookstoremb.adapter.BookstoreAdapter;
import com.example.bookstoremb.models.Book;
import com.example.bookstoremb.utils.Constants;
import com.example.bookstoremb.utils.RestClient;
import com.example.bookstoremb.utils.Utils;

public class MainActivity extends ListActivity {

    TextView selection;
    String[] items = {};
    String SEARCH_ALL_BOOK_URL = "http://192.168.1.130:8080/rest/private/bookstore/searchAllBook";
    String result;
    List<Book> books;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BookstoreAdapter adapter;
        books = new ArrayList<Book>();
        try {
          RestClient rest = new RestClient(SEARCH_ALL_BOOK_URL);
          rest.execute(RestClient.RequestMethod.GET);
          if (rest.getResponseCode() == 200) {
            JSONArray jsons = (JSONArray) new JSONArray(rest.getResponseStr());
            for (int i = 0; i < jsons.length(); i++) {
              JSONObject json = jsons.getJSONObject(i);
              Book book = Utils.createBookFromJSON(json);
              books.add(book);
              
            }
            adapter = new BookstoreAdapter(this, books);
            this.setListAdapter(adapter);
          } else {
            adapter = new BookstoreAdapter(this, books);
            this.setListAdapter(adapter);
            LinearLayout main = (LinearLayout) findViewById(R.id.main);
            TextView nocontent = new TextView(this);
            nocontent.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            nocontent.setText(R.string.nocontent);
            main.removeAllViews();
            main.addView(nocontent);
          }
        } catch (JSONException jse) {
          jse.printStackTrace();
        } catch (UnsupportedEncodingException ue) {
          ue.printStackTrace();
        }
    }

    /* (non-Javadoc)
     * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
     */
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
      super.onListItemClick(l, v, position, id);
      Book book = new Book();
      book = books.get(position);
      
      Intent intent = new Intent(this, ContentActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      intent.putExtra(Constants.BOOK_ID, book.getBookId());
      intent.putExtra(Constants.BOOK_NAME, book.getName());
      intent.putExtra(Constants.BOOK_CATEGORY, Utils.bookCategoryEnumToString(book.getCategory()));
      intent.putExtra(Constants.BOOK_CONTENT, book.getContent());
      startActivity(intent);
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      menu.add(menu.NONE, Constants.MENU_CLOSE, menu.NONE, R.string.menu_close);
      menu.add(menu.NONE, Constants.MENU_SEARCH, menu.NONE, R.string.menu_search);
      return super.onCreateOptionsMenu(menu);
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onMenuItemSelected(int, android.view.MenuItem)
     */
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
      switch (item.getItemId()) {
      case Constants.MENU_CLOSE:
        finish();
        break;
      case Constants.MENU_SEARCH:
        break;
      }
      return super.onMenuItemSelected(featureId, item);
    }

}
