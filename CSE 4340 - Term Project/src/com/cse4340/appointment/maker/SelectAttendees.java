package com.cse4340.appointment.maker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

public class SelectAttendees extends Activity {
	
	private SimpleCursorAdapter adapter = null;
	//private ListView lv = null;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_attendees);
        
    }

    
}
