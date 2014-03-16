package com.myapps.classroomsearch;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class credits extends Activity{

	TextView t2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.credit);
		
		t2 = (TextView) findViewById(R.id.text2);
		t2.setText( Html.fromHtml("<a href=\"mailto:ujjwalarora1992@gmail.com\">ujjwalarora1992@gmail.com</a> "));
		t2.setMovementMethod(LinkMovementMethod.getInstance());
		
	}
	
}