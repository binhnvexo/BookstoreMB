package com.example.bookstoremb;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bookstoremb.models.Book;
import com.example.bookstoremb.utils.Constants;
import com.example.bookstoremb.utils.RestClient;
import com.example.bookstoremb.utils.Utils;

public class MainActivity extends ListActivity {

    TextView selection;
    String[] items = {};
    String SEARCH_ALL_BOOK_URL = "http://192.168.1.130:8080/rest/private/bookstore/searchAllBook";
    String result;
    String username = "root";
    String password = "gtn";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<Book> books = new ArrayList<Book>();
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
          }
        } catch (JSONException jse) {
          jse.printStackTrace();
        } catch (UnsupportedEncodingException ue) {
          ue.printStackTrace();
        }
        List<String> bookNames = new ArrayList<String>();
        for (Book b : books) {
          bookNames.add(b.getName());
        }
        items = bookNames.toArray(new String[bookNames.size()]);
        setListAdapter(new ArrayAdapter<String>(this, R.layout.row, R.id.label, items));
    }

    /* (non-Javadoc)
     * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
     */
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
      super.onListItemClick(l, v, position, id);
      Intent intent = new Intent(this, ContentActivity.class);
      startActivity(intent);
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      menu.add(menu.NONE, Constants.MENU_CLOSE, menu.NONE, R.string.menu_close);
      return super.onCreateOptionsMenu(menu);
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onMenuItemSelected(int, android.view.MenuItem)
     */
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
      if (Constants.MENU_CLOSE == item.getItemId()) {
        finish();
      }
      return super.onMenuItemSelected(featureId, item);
    }

}
