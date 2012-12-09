/**
 * 
 */
package com.cse4340.appointment.maker;

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
	static Cursor cursor;
	
	public ProcessInvitationTask(ContentResolver cr) {
		myCR = cr;
	}
	
	@Override
	protected Invitation doInBackground(Invitation... invitation) {
		Uri uri = Calendars.CONTENT_URI;
		
		cursor = myCR.query(Uri.parse("content://com.android.calendar/calendars"), 
				(new String[] {Calendars._ID, Calendars.ACCOUNT_NAME, Calendars.CALENDAR_DISPLAY_NAME, Calendars.OWNER_ACCOUNT}), 
				null, 
				null, 
				null);
		
		HashSet<String> calendarIds = new HashSet<String>();
		
		try {
			System.out.println("Count = " + cursor.getCount());
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					String _id = cursor.getString(0);
					String displayName = cursor.getString(1);
					Boolean selected = !cursor.getString(2).equals("0");
					
					System.out.println("Id: " + _id + " Display Name: " + displayName + " Selected: " + selected);
					calendarIds.add(_id);
				}
			}
		} catch (AssertionError ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// For each calendar, display all the events from the previous week to the end of next week
		for (String id : calendarIds) {
			Uri.Builder builder = Uri.parse("content://com.android.calendar/instances/when").buildUpon();
			long now = new Date().getTime();
			
			ContentUris.appendId(builder, now - DateUtils.DAY_IN_MILLIS * 10000);
			ContentUris.appendId(builder, now + DateUtils.DAY_IN_MILLIS * 10000);
			
			Cursor eventCursor = myCR.query(builder.build(), 
					new String[] {Instances.EVENT_ID, Instances.BEGIN, Instances.TITLE}, 
					null, 
					null, 
					null);
			
			if (eventCursor.getCount() > 0) {
				eventCursor.moveToFirst();
				
				final String title = eventCursor.getString(0);
				final Date begin = new Date(eventCursor.getLong(1));
				final Date end = new Date(eventCursor.getLong(2));
				
				System.out.println("Title: " + title);
				System.out.println("Begin: " + begin);
				System.out.println("End: " + end);
				
				/*while (eventCursor.moveToNext()) {
					
					//final Boolean allDay = !eventCursor.getString(3).equals("0");
					
					System.out.println("Title: " + title);
					System.out.println("Begin: " + begin);
					System.out.println("End: " + end);
					//System.out.println("All Day: " + allDay);
				}*/
			}
		}
		
		/*final String[] INSTANCE_PROJECTION = new String[] {
			Instances.EVENT_ID,		// 0
			Instances.BEGIN, 		// 1
			Instances.TITLE			// 2
		};
		
		// The indices for the projection array above
		final int PROJECTION_ID_INDEX = 0;
		final int PROJECTION_BEGIN_INDEX = 1;
		final int PROJECTION_TITLE_INDEX = 2;
		
		// Specify the date range you want to search for recurring event instances
		Calendar beginTime = Calendar.getInstance();
		beginTime.set(2012, 12, 8, 8, 0);
		long startMillis = beginTime.getTimeInMillis();
		Calendar endTime = Calendar.getInstance();
		endTime.set(2012, 12, 8, 23, 0);
		long endMillis = endTime.getTimeInMillis();
		
		Cursor cur = null;
		
		// Construct the query with the desired date range
		Uri.Builder builder = Instances.CONTENT_URI.buildUpon();
		ContentUris.appendId(builder, startMillis);
		ContentUris.appendId(builder, endMillis);
		
		// Submit the query
		cur = myCR.query(builder.build(), 
				INSTANCE_PROJECTION, 
				null, 
				null, 
				null);
		
		cur.moveToFirst();
		while (cur.moveToNext()) {
			final String title = cur.getString(PROJECTION_TITLE_INDEX);
			final Date begin = new Date(cur.getLong(PROJECTION_BEGIN_INDEX));
			final Date end = new Date(cur.getLong(2));
			
			long beginVal = 0;
			
			// Get the field values
			beginVal = cur.getLong(PROJECTION_BEGIN_INDEX);
			
			// Do something with the values
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(beginVal);
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			System.out.println("Title: " + title);
			System.out.println("Begin: " + formatter.format(begin));
			System.out.println("End: " + formatter.format(end));
		}
		
		/*Intent intent = new Intent(Intent.ACTION_INSERT)
				.setData(Events.CONTENT_URI)
				.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
				.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
				.putExtra(Events.TITLE, invitation[0].getTitle())
				.putExtra(Events.DESCRIPTION, invitation[0].getDescription())
				.putExtra(Events.AVAILABILITY, Events.AVAILABILITY_TENTATIVE)
				.putExtra(Intent.EXTRA_EMAIL, "thomas.gallagher@mavs.uta.edu");
		*/
		return invitation[0];
	}
}