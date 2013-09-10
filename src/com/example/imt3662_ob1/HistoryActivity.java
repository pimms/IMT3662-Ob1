package com.example.imt3662_ob1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SimpleAdapter;

public class HistoryActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_history);
	    
	    String from[] = new String[] { "address", "count" };
	    int[] to = new int[] { R.id.history_list_address, R.id.history_list_count };
	    
	    List<HashMap<String, String>> listValues = new ArrayList<HashMap<String, String>>();
	    
	    // Get a map of all addresses and the number of times we've been there
	    DBHelper dbHelper = new DBHelper(this);
	    dbHelper.insertAddress("prompefisveien 2");
	    HashMap<String, Integer> values = dbHelper.getAddressesAndVisitCount();
	    
	    // Convert the raw HashMap into values we can use
	    Iterator iter = values.entrySet().iterator();
	    while (iter.hasNext()) {
	    	// Get an entry
	    	Map.Entry<String, Integer> pair = (Map.Entry<String, Integer>)iter.next();
	    	iter.remove();
	    	
	    	// Store it in the final list
	    	HashMap<String, String> row = new HashMap<String, String>();
	    	row.put(from[0], pair.getKey());
	    	row.put(from[1], pair.getValue() + " visits");
	    	listValues.add(row);
	    	
	    	Log.e("ADDR", pair.getKey());
	    }
	    
	    SimpleAdapter adapter = new SimpleAdapter(this, listValues, R.layout.history_list_item, from, to);
	    setListAdapter(adapter);
	}
}
