package com.example.bookstoremb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ListActivity {

    TextView selection;
    String[] items = {"Alice in wonderland", "Jouney to the West", "Shelock Holme", "The Mask"};
    String URL = "http://192.168.1.130:8080/rest/private/bookstore/searchAllBook";
    String result;
    String username = "root";
    String password = "gtn";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        HttpClient client = new DefaultHttpClient();
//        HttpGet request = new HttpGet(URL);
//        request.setHeader("Authorization",
//                      "Basic "
//                          + Base64.encodeToString((username + ":" + password).getBytes(),
//                                                  Base64.NO_WRAP));
//        ResponseHandler<String> handler = new BasicResponseHandler();
//        try {
//          HttpResponse response = client.execute(request);
//          HttpEntity entity = response.getEntity();
//          if (entity != null) {
//            InputStream is = entity.getContent();
//            String responseStr = convertStreamToString(is);
//          }
//        } catch (ClientProtocolException cpe) {
//          cpe.printStackTrace();
//        } catch (IOException e) {
//          e.printStackTrace();
//        }
//        client.getConnectionManager().shutdown();
        setListAdapter(new ArrayAdapter<String>(this, R.layout.row, R.id.label, items));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
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
    
    private String convertStreamToString(InputStream is) {

      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      StringBuilder sb = new StringBuilder();

      String line = null;
      try {
          while ((line = reader.readLine()) != null) {
              sb.append(line + "\n");
          }
      } catch (IOException e) {
          e.printStackTrace();
      } finally {
          try {
              is.close();
          } catch (IOException e) {
              e.printStackTrace();
          }
      }
      return sb.toString();
  }
    
}
