package com.4340-project;



import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListPairedDevices extends Activity {
	
	private static final int REQUEST_ENABLE_BT = 1;
	public static ArrayList<BluetoothDevice> selectedPairedDevices = new ArrayList<BluetoothDevice>();
	Set<BluetoothDevice> pairedDevices;
 
	private static ListView listDevicesFound;
	private static BluetoothAdapter bluetoothAdapter;
	private static TextView stateBluetooth;
	private static Button btnNext;
	
	private static ArrayAdapter<String> btArrayAdapter;
	
		/** Called when the activity is first created. */	
		 @Override
		 protected void onCreate(Bundle savedInstanceState) {
			  super.onCreate(savedInstanceState);
			  setContentView(R.layout.list_paired_devices);
		  
	
		      stateBluetooth = (TextView)findViewById(R.id.bluetoothstate);
			  bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			  
			  //Get paired devices and add them to the Set<BluetoothDevice> pairedDevices
			  pairedDevices = bluetoothAdapter.getBondedDevices();
			  
			  //Prepare the list that will hold paired devices
			  //GUI
			  listDevicesFound = (ListView)findViewById(R.id.pairedDevices);
			  btArrayAdapter = new ArrayAdapter<String>(ListPairedDevices.this, android.R.layout.simple_list_item_multiple_choice);
			  listDevicesFound.setAdapter(btArrayAdapter);
			  listDevicesFound.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			  btnNext = (Button) findViewById(R.id.button1);
			  
			  //Check what is the state of bluetooth
			  CheckBlueToothState();
	
			  //Button's Listeners
			  btnNext.setOnClickListener(btnNextOnClickListener);
			  
			  //If the phone has paired devices
			  //get the name and type of the device and display it in ListView
			  // show mac address??? (for later)
			  if (pairedDevices.size() > 0) {
			      for (BluetoothDevice device : pairedDevices) {
			       String deviceBTName = device.getName();
			       String deviceBTMajorClass 
			        = getBTMajorDeviceClass(device
			          .getBluetoothClass()
			          .getMajorDeviceClass());
			       btArrayAdapter.add(deviceBTName + "/n" 
			         + deviceBTMajorClass);
			       btArrayAdapter.notifyDataSetChanged();
			       
			      }
		      /* else{
		    	  //Write code to unable "next" button and Show "there is not paired devices"
		      } */
	  }
	  
	  
	  
}	  
		 /** Button Next Listener
		  *  Description: When button "Next" is clicked, the selected paired bluetooth devices
		  *  	will be stored in the selectedDevices array, then it will be directed to
		  *  	setup_meeting activity 
		  */
		 private Button.OnClickListener btnNextOnClickListener
		 	 = new Button.OnClickListener() {
		  
		  @Override
		  public void onClick(View arg0) {
			  
			   //Count the number of devices found as paired
			   //and go trough everyone to check if it is selected (or checked)
			   int count = listDevicesFound.getAdapter().getCount();
				 for (int i = 0; i < count; i++) {
					 			 //isItemChecked returns boolean
					             if (listDevicesFound.isItemChecked(i)) {
					                 
					                	 int find = 0;
					                	 //If it is selected, then store it in selectedPairedDevices array
					                	 //How: Go trought the pairedDevices Set and select the device that is 
					                	 //in the i position. Add it to the selectedPairedDevices array
					                	 for (BluetoothDevice device : pairedDevices) {
					     		    		if(i == find){
					     		    			selectedPairedDevices.add(device);
					     		    		}
					     		    		find++;
					                	 }
					                 
					                 
					             }
					         } 
				 
				 //Debug
				 String names = "";
				 for (BluetoothDevice device : selectedPairedDevices) {
				       String deviceBTName = device.getName();
				       names += deviceBTName + "/n"; 
				 }
				 Toast msg = Toast.makeText(ListPairedDevices.this, names, Toast.LENGTH_LONG);

				 msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);

				 msg.show();
				      } 
		  
 
	  };  
 
 
 private String getBTMajorDeviceClass(int major){
  switch(major){ 
  case BluetoothClass.Device.Major.AUDIO_VIDEO:
   return "AUDIO_VIDEO";
  case BluetoothClass.Device.Major.COMPUTER:
   return "COMPUTER";
  case BluetoothClass.Device.Major.HEALTH:
   return "HEALTH";
  case BluetoothClass.Device.Major.IMAGING:
   return "IMAGING"; 
  case BluetoothClass.Device.Major.MISC:
   return "MISC";
  case BluetoothClass.Device.Major.NETWORKING:
   return "NETWORKING"; 
  case BluetoothClass.Device.Major.PERIPHERAL:
   return "PERIPHERAL";
  case BluetoothClass.Device.Major.PHONE:
   return "PHONE";
  case BluetoothClass.Device.Major.TOY:
   return "TOY";
  case BluetoothClass.Device.Major.UNCATEGORIZED:
   return "UNCATEGORIZED";
  case BluetoothClass.Device.Major.WEARABLE:
   return "AUDIO_VIDEO";
  default: return "unknown!";
  }
 } 
 
 private void CheckBlueToothState(){
     if (bluetoothAdapter == null){
         stateBluetooth.setText("Bluetooth NOT support");
        }else{
         if (bluetoothAdapter.isEnabled()){
          if(bluetoothAdapter.isDiscovering()){
           stateBluetooth.setText("Bluetooth is currently in device discovery process.");
          }else{
           stateBluetooth.setText("Bluetooth is Enabled.");
          }
         }else{
          stateBluetooth.setText("Bluetooth is NOT Enabled!");
          Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
             startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
         }
        }
    }
 
 public void selectTimes(View view){
	 
	 

	 //Intent intent = new Intent(this, Setup_meeting.class);
 	 //startActivity(intent);
 }
 
 
 
}