package pimms.oblig1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.SimpleAdapter;

public class ListHistoryActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_list_history);
	    
	    String from[] = new String[] { "address", "count" };
	    int[] to = new int[] { R.id.history_list_address, R.id.history_list_count };
	    
	    List<HashMap<String, String>> listValues = new ArrayList<HashMap<String, String>>();
	    
	    DBHelper dbHelper = new DBHelper(this);
	    List<AddressRecord> values = dbHelper.getAddressRecords();
	    
	    for (AddressRecord record : values) {
	    	// Store it in the final list
	    	HashMap<String, String> row = new HashMap<String, String>();
	    	row.put(from[0], record.getAddress());
	    	row.put(from[1], record.getVisitCount() + " visits");
	    	listValues.add(row);
	    }
	    
	    SimpleAdapter adapter = new SimpleAdapter(this, listValues, R.layout.history_list_item, from, to);
	    setListAdapter(adapter);
	}
}
