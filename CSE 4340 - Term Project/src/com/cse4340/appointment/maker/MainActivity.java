package com.cse4340.appointment.maker;

import java.util.ArrayList;

import android.app.Activity;
import android.bluetooth.BluetoothClass.Device;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ArrayList<Device> attendees = new ArrayList<Device>();
        attendees.add(new Device());
        Time startTime = new Time(), endTime = new Time();
        startTime.set(0, 0, 1, 1, 11, 2012);
        endTime.set(0, 0, 23, 2, 11, 2012);
        
        Invitation appointment = new Invitation(attendees, startTime, endTime);
        appointment.setDescription("Test Description");
        appointment.setDuration(60);
        appointment.setLocation("Test Location");
        appointment.setTitle("Test Title");
        
        new ProcessInvitationTask(getContentResolver()).execute(appointment);
        
        /*	Initiator Device:
         *	1.	Identify which devices are "invited" (UI - Jacky)
         *	2.	Identify length of meeting & preferred appointment date/time. (UI - Jacky)
         *	3.	Create invite object. (Calendar - Brandon)
         *	4.	Determine if any invitees are present in network.  If not, repeat this step. (Detection/Transmission - Robert)
		 *	5.	Ensure that nearby devices all have contributed & have the same object.  If timeout occurs, return to step 4. (Detection/Transmission - Robert)
		 *	6.	Check updated object to determine whether all invitees are accounted for.  If so, proceed to step 7.  If not, return to 5.
		 *	7.	Select appointment time from remaining timeslots in object.  If no timeslots remain, notify user that no appointment could be made amongst invitees. (Calendar - Brandon)
		 *	// Final time selection has been made here
		 *	8.	Determine if any invitees are present in network.  If not, repeat this step. (Detection/Transmission - Robert)
		 *	9.	Transmit appointment request to next device present in network.  If no more devices present in network, skip to step 10. (Detection/Transmission - Robert)
		 *	10.	If all invitee devices have confirmed, finish.  Otherwise, return to step 8.
		 *
		 *	Invitee Device:
		 *	1.	If invite object received, proceed to next step.
		 *	2.	Compare list of proposed times against calendar availability and remove times that are not available.
		 *	3.	Update object to show this device as accounted for.
		 *	4.	Transmit object back to initiator.
		 *	5.	If at least one proposed time remains in object�s list, save object and proceed to step 6.  Otherwise, return to step 1.
		 *	6.	Determine if any unaccounted for invitees are present in network.  If not, repeat this step.
		 *	7.	Transmit object to next device present in network.  If no more devices present in network, skip to step 9.
		 *	8.	Wait to receive updated object back.  If timeout occurs, return to step 6.  Otherwise, return to 7.
		 *	9.	Check updated object to determine whether all invitees are accounted for.  If so, proceed to step 10.  If not, return to 6.
		 *	10.	Wait until connection with initiator is available and then transmit object.
		 *
		 *	1.	If appointment request received, proceed to next step.
		 *	2.	If available, schedule appointment.
		 *	3.	Return true if scheduled and false if not.
         */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void setupMeeting(View view) {
    	Intent intent = new Intent(this, Setup_meeting.class);
    	startActivity(intent);
    }
}
