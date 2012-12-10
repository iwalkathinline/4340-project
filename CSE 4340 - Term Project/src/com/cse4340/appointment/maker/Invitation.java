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
	private ArrayList<Device> myAttendees;
	private Time myStartTime = null, myEndTime = null;
	private boolean[][] myAvailableTimes;
	private int myDuration;	// Measured in number of minutes
	private String myLocation, myTitle, myDescription;
	
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
		
		initializeArray();
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
	 * @return the availableTimes
	 */
	public boolean[][] getAvailableTimes() {
		return myAvailableTimes;
	}

	/**
	 * @param availableTimes the availableTimes to set
	 */
	public void setAvailableTimes(boolean[][] availableTimes) {
		myAvailableTimes = availableTimes;
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
	
	private void initializeArray() {
		int numOfHours = countHours();
		
		myAvailableTimes = new boolean[myAttendees.size()][numOfHours];
		
		for (int row = 0; row < myAttendees.size(); row++) {
			for (int col = 0; col < numOfHours; col++) {
				System.out.println("[" + row + "][" + col + "] = " + myAvailableTimes[row][col]);
			}
		}
	}

	private int countHours() {
		long startMillis = myStartTime.toMillis(true);
		long endMillis = myEndTime.toMillis(true);
		long totalMillis = endMillis - startMillis;
		long totalMins = TimeUnit.MILLISECONDS.toMinutes(totalMillis);
		double totalHrs = Math.ceil(totalMins / 60.0);
		
		return (int) totalHrs;
	}
}