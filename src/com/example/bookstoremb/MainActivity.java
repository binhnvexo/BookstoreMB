package com.example.bookstoremb;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bookstoremb.adapter.BookstoreAdapter;
import com.example.bookstoremb.models.Book;
import com.example.bookstoremb.utils.Constants;
import com.example.bookstoremb.utils.RestClient;
import com.example.bookstoremb.utils.Utils;
import com.example.bookstoremb.wrapper.SearchWrapper;

public class MainActivity extends ListActivity {

    TextView selection;
    String[] items = {};
    String SEARCH_ALL_BOOK_URL = "http://192.168.1.130:8080/rest/private/bookstore/searchAllBook";
    String SEARCH_BOOK_BY_NAME = "http://192.168.1.130:8080/rest/private/bookstore/searchBookByName/";
    String result;
    List<Book> books;
    Button btnReturn;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout main = (LinearLayout) findViewById(R.id.main);
        btnReturn = (Button) findViewById(R.id.btnReturn);
        books = new ArrayList<Book>();
        Bundle extras = getIntent().getExtras();
        try {
          if (extras == null) {
            main.removeView(btnReturn);
            renderList(SEARCH_ALL_BOOK_URL);
          } else {
            String bookName = extras.getString(Constants.BOOK_NAME);
            if (bookName == null || "".equals(bookName)) {
              main.removeView(btnReturn);
              renderList(SEARCH_ALL_BOOK_URL);
            } else {
              renderList(SEARCH_BOOK_BY_NAME + bookName.replaceAll(" ", ""));
            }
          }
        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        } catch (JSONException e) {
          e.printStackTrace();
        }
        
    }

    private void renderList(String url) throws JSONException, UnsupportedEncodingException {
      BookstoreAdapter adapter;
      RestClient rest = new RestClient(url);
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
        main.addView(btnReturn);
        main.addView(nocontent);
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
        LayoutInflater inflater = LayoutInflater.from(this);
        View searchView = inflater.inflate(R.layout.activity_search, null);
        final SearchWrapper wrapper = new SearchWrapper(searchView, this);
        final Context nestContext = this;
        new AlertDialog.Builder(this).setTitle(R.string.tltsearch)
                                   .setView(searchView)
                                   .setPositiveButton(R.string.btnsearch, new OnClickListener() {

                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {
                                       String bookName = wrapper.getBookNameStr();
                                       Intent intent = new Intent(nestContext, MainActivity.class);
                                       intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                       intent.putExtra(Constants.BOOK_NAME, bookName);
                                       startActivity(intent);
                                     }
                                   })
                                   .setNegativeButton(R.string.btncancel, new OnClickListener() {

                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {

                                     }
                                   })
                                   .show();
        break;
      }
      return super.onMenuItemSelected(featureId, item);
    }

}
