/**
 * 
 */
package com.cse4340.appointment.maker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import android.bluetooth.BluetoothClass.Device;
import android.text.format.Time;

/**
 * @author Brandon
 *
 */
public class Invitation implements Serializable {
	private static ArrayList<Device> myAttendees;
	private static Time myStartTime = null, myEndTime = null;
	private static boolean[][] myAvailableTimes;
	private static int myDuration, hrSpan;	// Measured in number of minutes
	private static String myLocation, myTitle, myDescription;
	
	/**
	 * 
	 * @param attendees the attendees to set
	 * @param startTime the startTime to set
	 * @param endTime the endTime to set
	 */
	public Invitation(ArrayList<Device> attendees, Time startTime, Time endTime) {
		myAttendees = attendees;
		myStartTime = startTime;
		myEndTime = endTime;
		hrSpan = countHours();
		myAvailableTimes = new boolean[myAttendees.size()][hrSpan];
	}
	
	/**
	 * @return the attendees
	 */
	public ArrayList<Device> getAttendees() {
		return myAttendees;
	}

	/**
	 * @return the startTime
	 */
	public Time getStartTime() {
		return myStartTime;
	}
	
	/**
	 * @return the endTime
	 */
	public Time getEndTime() {
		return myEndTime;
	}

	/**
	 * @param row the row to set
	 * @param col the col to set
	 * @return the availableTimes
	 */
	public boolean getAvailable(int row, int col) {
		return myAvailableTimes[row][col];
	}

	/**
	 * @param row the row to set
	 * @param col the col to set
	 * @param available the available to set
	 */
	public void setAvailable(int row, int col, boolean available) {
		myAvailableTimes[row][col] = available;
	}
	
	/**
	 * @return the duration in minutes
	 */
	public int getDuration() {
		return myDuration;
	}

	/**
	 * @param duration the duration to set in minutes
	 */
	public void setDuration(int duration) {
		myDuration = duration;
	}
	
	public int getHrSpan () {
		return hrSpan;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return myLocation;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		myLocation = location;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return myTitle;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		myTitle = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return myDescription;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		myDescription = description;
	}

	/**
	 * 
	 * @return the total number of hours that the meeting organizer's request spans
	 */
	private int countHours() {
		long startMillis = myStartTime.toMillis(true), endMillis = myEndTime.toMillis(true);
		long totalMillis = endMillis - startMillis;
		long totalMins = TimeUnit.MILLISECONDS.toMinutes(totalMillis);
		double totalHrs = Math.ceil(totalMins / 60.0);
		
		return (int) totalHrs;
	}
}