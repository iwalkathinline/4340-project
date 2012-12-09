package com.4340-project;


import java.util.ArrayList;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass.Device;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SelectAttendees extends Activity {
	
	private static final int REQUEST_ENABLE_BT = 1;
	
	//GUI 
	ListView listDevicesFound;
	Button btnScanDevice;
	Button btnNext;
	TextView stateBluetooth;
	
	BluetoothAdapter bluetoothAdapter;
	//List that holds all scaned devices
	public static ArrayList<BluetoothDevice> allDevices = new ArrayList<BluetoothDevice>();
	//List that holds selected devices
	public static ArrayList<BluetoothDevice> selectedDevices = new ArrayList<BluetoothDevice>();
	 
	ArrayAdapter<String> btArrayAdapter;
 
 /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_attendees);
        
        
        btnNext = (Button)findViewById(R.id.selectTimes);
        stateBluetooth = (TextView)findViewById(R.id.bluetoothstate);
        
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        
        //Prepare the list that will hold paired devices
        //GUI
        btnScanDevice = (Button)findViewById(R.id.scandevice);
        listDevicesFound = (ListView)findViewById(R.id.devicesfound);
        btArrayAdapter = new ArrayAdapter<String>(SelectAttendees.this, android.R.layout.simple_list_item_multiple_choice);
        listDevicesFound.setAdapter(btArrayAdapter);
        listDevicesFound.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        
        //Check what is the state of bluetooth
        CheckBlueToothState();
        
        //Button's Listeners
        btnScanDevice.setOnClickListener(btnScanDeviceOnClickListener);
        btnNext.setOnClickListener(btnNextOnClickListener);
        
        registerReceiver(ActionFoundReceiver, 
          new IntentFilter(BluetoothDevice.ACTION_FOUND));
    }
    
     @Override
	 protected void onDestroy() {
	  // TODO Auto-generated method stub
	  super.onDestroy();
	  unregisterReceiver(ActionFoundReceiver);
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
	           btnScanDevice.setEnabled(true);
	          }
	         }else{
	          stateBluetooth.setText("Bluetooth is NOT Enabled!");
	          Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	             startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
	         }
	        }
	    }
    
	 /** Button Next Listener
	  *  Description: When button "Next" is clicked, the selected bluetooth devices
	  *  	will be stored in the selectedDevices array, then it will be directed
	  *  	to setup_meeting activity 
	  */
	 private Button.OnClickListener btnNextOnClickListener 
	     = new Button.OnClickListener(){

			  @Override
			  public void onClick(View arg0) {
				
				  //Count the number of devices found as paired
				  //and go trough everyone to check if it is selected (or checked)
				  int count = listDevicesFound.getAdapter().getCount();
				  for (int i = 0; i < count; i++) {
					  //isItemChecked returns boolean
					  if (listDevicesFound.isItemChecked(i)) {
						  //If device it is selected, then store it in selectedDevices 
						  //How: Go trought the pairedDevices Set and select the device that is 
						  //in the i position. Add it to the selectedPairedDevices array
						  selectedDevices.add(allDevices.get(i));
							              
					  }
				  } 
				
				//Debug
					 String names = "";
					 for (BluetoothDevice device : selectedDevices) {
					       String deviceBTName = device.getName();
					       names += deviceBTName + "/n"; 
					 }
					 Toast msg = Toast.makeText(SelectAttendees.this, names, Toast.LENGTH_LONG);

					 msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);

					 msg.show();
			  }
	 };
	 
	/** Button Scan Device Listener
	 *  Description: When button is clicked, the ListView will be cleared
	 *  	and the discovery of bluetooth devices will start 
	 */
    private Button.OnClickListener btnScanDeviceOnClickListener
    = new Button.OnClickListener(){
    	
		  @Override
		  public void onClick(View arg0) {
		   // TODO Auto-generated method stub
		   btArrayAdapter.clear();
		   bluetoothAdapter.startDiscovery();
		  }
  };  
  


 @Override
 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  // TODO Auto-generated method stub
  if(requestCode == REQUEST_ENABLE_BT){
   CheckBlueToothState();
  }
 }
    
 private final BroadcastReceiver ActionFoundReceiver = new BroadcastReceiver(){

  @Override
  public void onReceive(Context context, Intent intent) {
   // TODO Auto-generated method stub
   String action = intent.getAction();
   if(BluetoothDevice.ACTION_FOUND.equals(action)) {
             BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
             btArrayAdapter.add(device.getName() + "\n" + device.getAddress());
             btArrayAdapter.notifyDataSetChanged();
             allDevices.add(device);
         }
  }};
    
}