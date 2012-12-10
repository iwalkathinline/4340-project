package com.finalproject;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

public class Setup_meeting extends Activity {

	/* Duration Variables */
	private Button btnPlus;
	private Button btnMinus;
	public TextView numberPicker;
	public int currentNumber;
	
	/* Date Variables */
	private TextView startDateDisplay;
	private TextView endDateDisplay;
	private ImageButton startPickDate;
	private ImageButton endPickDate;
	public int fromYear, fromMonth, fromDay;
	public int toYear, toMonth, toDay;


	static final int DATE_PICKER_TO = 0;
	static final int DATE_PICKER_FROM = 1;
	
	
	/* Time Variables */
	private TextView startTimeDisplay;
	private TextView endTimeDisplay;
	private ImageButton startPickTime;
	private ImageButton endPickTime;
	public int fromHour, fromMin;
	public int toHour, toMin;
	
	static final int TIME_PICKER_TO = 2;
	static final int TIME_PICKER_FROM = 3;


/** Called when the activity is first created. */
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.setup_meeting);

    /* ================ Calendar ================ */
    /*  capture our View elements for the start date function   */
    startDateDisplay = (TextView) findViewById(R.id.startDateDisplay);
    startPickDate = (ImageButton) findViewById(R.id.startpickDate);

    /* add a click listener to the button   */
    startPickDate.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            showDialog(DATE_PICKER_FROM);
        }
    });

    /* get the current date */
    final Calendar c = Calendar.getInstance();
    fromYear = c.get(Calendar.YEAR);
    fromMonth = c.get(Calendar.MONTH);
    fromDay = c.get(Calendar.DAY_OF_MONTH);

    /* display the current date (this method is below)  */
    updateStartDisplay();


    /* capture our View elements for the end date function */
    endDateDisplay = (TextView) findViewById(R.id.enddateDisplay);
    endPickDate = (ImageButton) findViewById(R.id.endpickDate);

    /* add a click listener to the button   */
    endPickDate.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            showDialog(DATE_PICKER_TO);
        }
    });

    /* get the current date */
    final Calendar c1 = Calendar.getInstance();
    toYear = c1.get(Calendar.YEAR);
    toMonth = c1.get(Calendar.MONTH);
    toDay = c1.get(Calendar.DAY_OF_MONTH);

    /* display the current date (this method is below)  */
    updateEndDisplay();
    
    /* ================ Duration ================ */
    numberPicker = (TextView) findViewById(R.id.numberPickerDisplay);
    btnPlus = (Button) findViewById(R.id.plus);
    btnMinus = (Button) findViewById(R.id.minus);
   
    btnMinus.setOnClickListener(new View.OnClickListener() {
       public void onClick(View v) {
    	   currentNumber = Integer.parseInt((String) numberPicker.getText().toString());
    	   if(currentNumber == 1){
    		   currentNumber--;
    		   numberPicker.setText(String.valueOf(currentNumber));
    		   btnMinus.setEnabled(false);
    	   }
    	   else{
    		   currentNumber--;
    		   numberPicker.setText(String.valueOf(currentNumber));
    		   btnPlus.setEnabled(true);
    	   }
       }
    });
   
    btnPlus.setOnClickListener(new View.OnClickListener() {
       public void onClick(View v) {
    	   currentNumber = Integer.parseInt((String) numberPicker.getText().toString());
    	   if(currentNumber == 7){
    		   currentNumber++;
    		   numberPicker.setText(String.valueOf(currentNumber));
    		   btnPlus.setEnabled(false);
    	   }
    	   else{
	    	   currentNumber++;
			   numberPicker.setText(String.valueOf(currentNumber));
			   btnMinus.setEnabled(true);
    	   }
		   
       }
    });
    
    /* ================ Time ================ */
    startTimeDisplay = (TextView) findViewById(R.id.startTimeDisplay);
    startPickTime = (ImageButton) findViewById(R.id.startPickTime);
    
    startPickTime.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            showDialog(TIME_PICKER_FROM);
        }
    });
    
    final Calendar c2 = Calendar.getInstance();
    fromHour = c2.get(Calendar.HOUR_OF_DAY);
    fromMin = c2.get(Calendar.MINUTE);
    
    updateStartTimeDisplay();
    
    endTimeDisplay = (TextView) findViewById(R.id.endTimeDisplay);
    endPickTime = (ImageButton) findViewById(R.id.endPickTime);
    
    endPickTime.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            showDialog(TIME_PICKER_TO);
        }
    });
    
    final Calendar c3 = Calendar.getInstance();
    toHour = c3.get(Calendar.HOUR_OF_DAY);
    toMin = c3.get(Calendar.MINUTE);
    
    updateEndTimeDisplay();
    
    
}

private void updateEndTimeDisplay() {
	endTimeDisplay.setText(
			new StringBuilder()
			.append(pad(toHour)).append(":")
			.append(pad(toMin)));
}

private void updateStartTimeDisplay() {
	startTimeDisplay.setText(
			new StringBuilder()
			.append(pad(fromHour)).append(":")
			.append(pad(fromMin)));
}

private void updateEndDisplay() {
    endDateDisplay.setText(
            new StringBuilder()
                // Month is 0 based so add 1
                .append(toMonth + 1).append("-")
                .append(toDay).append("-")
                .append(toYear).append(" "));

}



private void updateStartDisplay() {
    startDateDisplay.setText(
            new StringBuilder()
                // Month is 0 based so add 1
                .append(fromMonth + 1).append("-")
                .append(fromDay).append("-")
                .append(fromYear).append(" "));


}

private DatePickerDialog.OnDateSetListener from_dateListener =
new DatePickerDialog.OnDateSetListener() {

public void onDateSet(DatePicker view, int year, 
                      int monthOfYear, int dayOfMonth) {
    fromYear = year;
    fromMonth = monthOfYear;
    fromDay = dayOfMonth;
    updateStartDisplay();
}
};


private DatePickerDialog.OnDateSetListener to_dateListener =
new DatePickerDialog.OnDateSetListener() {

public void onDateSet(DatePicker view, int year, 
                      int monthOfYear, int dayOfMonth) {
    toYear = year;
    toMonth = monthOfYear;
    toDay = dayOfMonth;
    updateEndDisplay();
}
};


private TimePickerDialog.OnTimeSetListener from_timeListener =
new TimePickerDialog.OnTimeSetListener() {
	
	public void onTimeSet(TimePicker view, int hourOfDay, int minute){
		fromHour = hourOfDay;
		fromMin = minute;
		updateStartTimeDisplay();
	}
	
};

private TimePickerDialog.OnTimeSetListener to_timeListener =
new TimePickerDialog.OnTimeSetListener() {
	
	public void onTimeSet(TimePicker view, int hourOfDay, int minute){
		toHour = hourOfDay;
		toMin = minute;
		updateEndTimeDisplay();
	}
	
};


@Override
protected Dialog onCreateDialog(int id) {
	switch (id) {
		case DATE_PICKER_FROM:
			return new DatePickerDialog(this,
			        from_dateListener,
			        fromYear, fromMonth, fromDay);
		case DATE_PICKER_TO:
			return new DatePickerDialog(this,
			        to_dateListener,
			        toYear, toMonth, toDay);
		case TIME_PICKER_FROM:
			return new TimePickerDialog(this,
					from_timeListener, 
					fromHour, fromMin, false);
		case TIME_PICKER_TO:
			return new TimePickerDialog(this,
					to_timeListener, 
					toHour, toMin, false);
			
	}
	return null;
}


private static String pad(int c) {
    if (c >= 10)
        return String.valueOf(c);
    else
        return "0" + String.valueOf(c);
}

public void selectPairedDevices(View view) {
	Intent intent = new Intent(this, ListPairedDevices.class);
	startActivity(intent);
}

public void scanDevices(View view) {
	Intent intent = new Intent(this, SelectAttendees.class);
	startActivity(intent);
}




}