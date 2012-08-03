package com.example.bookstoremb;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookstoremb.adapter.BookstoreAdapter;
import com.example.bookstoremb.models.Book;
import com.example.bookstoremb.utils.Constants;
import com.example.bookstoremb.utils.RestClient;
import com.example.bookstoremb.utils.Utils;
import com.example.bookstoremb.wrapper.SearchWrapper;

@SuppressLint("NewApi")
public class MainActivity extends ListActivity {

    private static final String SEARCH_ALL_BOOK_URL = "http://192.168.1.130:8080/rest/private/bookstore/searchAllBook";
    private static final String SEARCH_BOOK_BY_NAME = "http://192.168.1.130:8080/rest/private/bookstore/searchBookByName/";
    private List<Book> books;
    private Button btnReturn;
    private TextView nocontent;
    private LinearLayout main;
    private String searchCondition;
    private String bookName;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT > 9) {
          StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
          StrictMode.setThreadPolicy(policy);
        }
        initView();
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
          main.removeView(btnReturn);
          searchCondition = SEARCH_ALL_BOOK_URL;
          renderList(SEARCH_ALL_BOOK_URL);
        } else {
          bookName = extras.getString(Constants.BOOK_NAME);
          searchCondition = extras.getString(Constants.SEARCH_CONDITION);
          if (bookName == null || "".equals(bookName)) {
            renderList(searchCondition);
          } else {
            searchCondition = SEARCH_BOOK_BY_NAME + bookName.replaceAll(" ", "");
            renderList(SEARCH_BOOK_BY_NAME + bookName.replaceAll(" ", ""));
          }
        }
    }

    private void initView() {
      bookName = "";
      searchCondition = "";
      main = (LinearLayout) findViewById(R.id.main);
      nocontent = new TextView(this);
      nocontent.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
      nocontent.setText(R.string.nocontent);
      books = new ArrayList<Book>();
      btnReturn = (Button) findViewById(R.id.btnReturn);
      btnReturn.setOnClickListener(new OnClickListener() {
        
        @Override
        public void onClick(View v) {
          main.removeView(btnReturn);
          main.removeView(nocontent);
          renderList(SEARCH_ALL_BOOK_URL);
        }
        
      });
    }
    
    private void renderList(String url) {
      try {
        BookstoreAdapter adapter;
        RestClient rest = new RestClient(url);
        rest.execute(RestClient.RequestMethod.GET);
        books = new ArrayList<Book>();
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
          prepareViewResponseError();
          main.addView(btnReturn, 1);
        }
      } catch (JSONException e) {
        e.printStackTrace();
        prepareViewResponseError();
      } catch (ClientProtocolException e) {
        e.printStackTrace();
        prepareViewResponseError();
      } catch (IOException e) {
        e.printStackTrace();
        prepareViewResponseError();
      }
    }
    
    private void prepareViewResponseError() {
      main.removeView(btnReturn);
      main.removeView(nocontent);
      main.addView(nocontent, 0);
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
      intent.putExtra(Constants.SEARCH_CONDITION, searchCondition);
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
        System.exit(0);
        break;
      case Constants.MENU_SEARCH:
        LayoutInflater inflater = LayoutInflater.from(this);
        View searchView = inflater.inflate(R.layout.activity_search, null);
        final SearchWrapper wrapper = new SearchWrapper(searchView, this);
        final Context nestContext = this;
        new AlertDialog.Builder(this).setTitle(R.string.tltsearch)
                                   .setView(searchView)
                                   .setPositiveButton(R.string.btnsearch, new DialogInterface.OnClickListener() {

                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {
                                       String bookName = wrapper.getBookNameStr();
                                       Intent intent = new Intent(nestContext, MainActivity.class);
                                       intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                       intent.putExtra(Constants.BOOK_NAME, bookName);
                                       startActivity(intent);
                                     }
                                   })
                                   .setNegativeButton(R.string.btncancel, new DialogInterface.OnClickListener() {

                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {
                                       
                                     }
                                   })
                                   .show();
        break;
      }
      return super.onMenuItemSelected(featureId, item);
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
      if (keyCode == KeyEvent.KEYCODE_BACK) {
        if (bookName != null || "".equals(bookName)) {
          main.removeView(btnReturn);
          main.removeView(nocontent);
          bookName = "";
          searchCondition = SEARCH_ALL_BOOK_URL;
          renderList(SEARCH_ALL_BOOK_URL);
          return true;
        }
      }
      return super.onKeyDown(keyCode, event);
    }
    
}