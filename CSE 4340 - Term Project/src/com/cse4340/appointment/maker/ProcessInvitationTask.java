/**
 * 
 */
package com.cse4340.appointment.maker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Instances;
import android.text.format.DateUtils;

/**
 * @author Brandon
 *
 */
public class ProcessInvitationTask extends AsyncTask<Invitation, Void, Invitation> {
	private ContentResolver myCR;
	private static Cursor cursor;
	private static Invitation myInvitation;
	
	/**
	 * 
	 * @param cr - The ContentResolver, just passing in getContentResolver() usually works fine
	 */
	public ProcessInvitationTask(ContentResolver cr) {
		myCR = cr;
	}
	
	@Override
	protected Invitation doInBackground(Invitation... invitation) {
		myInvitation = invitation[0];
		
		if (eliminateTimes())
			System.out.println("Times eliminated successfully!");
		
		return myInvitation;
	}
	
	/**
	 * Returns true if the organizer's unavailable times were successfully eliminated from the Invitation's availableTimes array, 
	 * otherwise returns false. Be sure that startDateTime and endDateTime have both been set before calling this method.
	 */
	public boolean eliminateTimes() {
		// Get duration
		// Check calendar for all available durations between start and end
		// Modify availableTimes[] to reflect only the first row's eliminations
		
		// If startDateTime and endDateTime haven't been set yet, the a run-time error will occur in this method
		if (myInvitation.getStartTime() == null || myInvitation.getEndTime() == null)
			return false;
		
		final String[] INSTANCE_PROJECTION = new String[] {
				Instances.EVENT_ID,		// 0
				Instances.BEGIN, 		// 1
				Instances.TITLE, 		// 2
				Instances.START_DAY, 	// 3
				Instances.START_MINUTE, // 4
				Instances.END_DAY, 		// 5
				Instances.END_MINUTE, 	// 6
				Instances.DURATION, 	// 7
				Instances.ORGANIZER, 	// 8
				Instances.DESCRIPTION	// 9
		};
		
		final int PROJECTION_ID_INDEX = 0;
		final int PROJECTION_BEGIN_INDEX = 1;
		final int PROJECTION_TITLE_INDEX = 2;
		final int PROJECTION_START_DAY_INDEX = 3;
		final int PROJECTION_START_MINUTE_INDEX = 4;
		final int PROJECTION_END_DAY_INDEX = 5;
		final int PROJECTION_END_MINUTE_INDEX = 6;
		final int PROJECTION_DURATION_INDEX = 7;
		final int PROJECTION_ORGANIZER_INDEX = 8;
		final int PROJECTION_DESCRIPTION_INDEX = 9;
		
		Calendar beginTime = Calendar.getInstance();
		beginTime.set(myInvitation.getStartTime().year, 
				myInvitation.getStartTime().month, // Month is 0-based
				myInvitation.getStartTime().monthDay, 
				myInvitation.getStartTime().hour, 
				myInvitation.getStartTime().minute);
		long startMillis = beginTime.getTimeInMillis();
		Calendar endTime = Calendar.getInstance();
		endTime.set(myInvitation.getEndTime().year, 
				myInvitation.getEndTime().month, // Month is 0-based
				myInvitation.getEndTime().monthDay, 
				myInvitation.getEndTime().hour, 
				myInvitation.getEndTime().minute);
		long endMillis = endTime.getTimeInMillis();
		
		Cursor cur = null;
		
		Uri.Builder builder = Instances.CONTENT_URI.buildUpon();
		ContentUris.appendId(builder, startMillis);
		ContentUris.appendId(builder, endMillis);
		
		cur = myCR.query(builder.build(), 
				INSTANCE_PROJECTION, 
				null, 
				null, 
				null);
		
		while (cur.moveToNext()) {
			String title = null;
			long eventID = 0;
			long beginVal = 0;
			int startDay = 0;
			int startMinute = 0;
			int endDay= 0;
			int endMinute = 0;
			String duration = null;
			String organizer = null;
			String description = null;
			
			eventID = cur.getLong(PROJECTION_ID_INDEX);
			beginVal = cur.getLong(PROJECTION_BEGIN_INDEX);
			title = cur.getString(PROJECTION_TITLE_INDEX);
			startDay = cur.getInt(PROJECTION_START_DAY_INDEX);
			startMinute = cur.getInt(PROJECTION_START_MINUTE_INDEX);
			endDay = cur.getInt(PROJECTION_END_DAY_INDEX);
			endMinute = cur.getInt(PROJECTION_END_MINUTE_INDEX);
			duration = cur.getString(PROJECTION_DURATION_INDEX);
			organizer = cur.getString(PROJECTION_ORGANIZER_INDEX);
			description = cur.getString(PROJECTION_DESCRIPTION_INDEX);
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(beginVal);
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			
			System.out.println("Event: " + title);
			System.out.println("Begin: " + formatter.format(calendar.getTime()));
			System.out.println("Event ID: " + eventID);
			System.out.println("Start Day: " + startDay);
			System.out.println("Start Minute: " + startMinute);
			System.out.println("End Day: " + endDay);
			System.out.println("End Minute: " + endMinute);
			System.out.println("Duration: " + duration);
			System.out.println("Organizer: " + organizer);
			System.out.println("Description: " + description);
		}
		
		return true;
	}
}