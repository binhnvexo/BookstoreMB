/**
 * 
 */
package com.example.bookstoremb;

import android.app.Activity;
import android.os.Bundle;
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

}
