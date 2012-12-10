/**
 * 
 */
package com.cse4340.appointment.maker;

import java.util.Calendar;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CalendarContract.Instances;
import android.text.format.Time;

/**
 * @author Brandon
 *
 */
public class ProcessInvitationTask extends AsyncTask<Invitation, Void, Invitation> {
	private ContentResolver myCR;
	private static Cursor cursor = null;
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
		
		if (!eliminateTimes())
			System.out.println("Times couldn't be modified, check your invitation object.");
		
		return myInvitation;
	}
	
	/**
	 * Returns true if the organizer's unavailable times were successfully eliminated from the Invitation's availableTimes array, 
	 * otherwise returns false. Be sure that startDateTime and endDateTime have both been set before calling this method.
	 */
	public boolean eliminateTimes() {
		boolean available = false;
		Time hrStart = new Time(myInvitation.getStartTime()), hrEnd = new Time(myInvitation.getStartTime());
		hrEnd.set(0, 0, hrStart.hour + 1, hrStart.monthDay, hrStart.month, hrStart.year);
		
		// If startDateTime and endDateTime haven't been set yet, then a run-time error will occur in this method
		if (myInvitation.getStartTime() == null || myInvitation.getEndTime() == null)
			return false;
		
		// Loop through each hour block to check for event instances
		for (int hr = 0; hr < myInvitation.getHrSpan(); hr++) {
			available = !hasEvent(hrStart, hrEnd); // Negation necessary since we need available times in our array
			myInvitation.setAvailable(0, hr, available);
			
			// Advance starting & ending markers by one hour
			hrStart.set(hrEnd);
			hrEnd.set(0, 0, hrStart.hour + 1, hrStart.monthDay, hrStart.month, hrStart.year);
		}
		
		return true;
	}

	private boolean hasEvent(Time start, Time end) {
		// Specify the date range you want to search for instances
		Calendar cal = Calendar.getInstance();
		cal.set(start.year, 
				start.month, // Month is 0-based
				start.monthDay, 
				start.hour, 
				start.minute);
		long startMillis = cal.getTimeInMillis();
		cal = Calendar.getInstance();
		cal.set(end.year, 
				end.month, // Month is 0-based
				end.monthDay, 
				end.hour, 
				end.minute);
		long endMillis = cal.getTimeInMillis();
		
		// Construct the query with the desired date range.
		Uri.Builder builder = Instances.CONTENT_URI.buildUpon();
		ContentUris.appendId(builder, startMillis);
		ContentUris.appendId(builder, endMillis);
		
		// Submit the query
		cursor = myCR.query(builder.build(), 
				null, 
				null, 
				null, 
				null);
		
		// If a record exists, then you have an event
		if (cursor.getCount() > 0)
			return true;
		
		return false;
	}
}