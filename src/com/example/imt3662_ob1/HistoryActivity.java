package com.example.imt3662_ob1;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class HistoryActivity extends ListActivity {
	ArrayList<String> listItems;
	ArrayAdapter<String> adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_history);
	    
	    listItems = new ArrayList<String>();
	    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
	    setListAdapter(adapter);
	    
	    // Add all the addresses
	    DBHelper dbHelper = new DBHelper(this);
	    ArrayList<String> addr = dbHelper.getAllAddresses();
	    
	    for (String str : addr) {
	    	listItems.add(str);
	    }
	    
	    adapter.notifyDataSetChanged();
	}
}
