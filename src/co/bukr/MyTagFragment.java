package co.bukr;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.coimotion.csdk.common.COIMCallListener;
import com.coimotion.csdk.common.COIMException;
import com.coimotion.csdk.util.Assist;
import com.coimotion.csdk.util.ReqUtil;

public class MyTagFragment extends Fragment  {
	private final static String LOG_TAG = "MyTagActivity";
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
	protected ArrayList<String> mTags = new ArrayList<String>();
	protected ArrayList<String> mFgID = new ArrayList<String>();

	public static Fragment newInstance(int sectionNumber) {
		MyTagFragment fragment = new MyTagFragment();
		Bundle args = new Bundle();
		args.putInt("section_number", sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		//getActivity().requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
//		getActivity().getActionBar().setBackgroundDrawable(
//				new ColorDrawable(android.R.color.transparent));

		super.onCreate(savedInstanceState);
		
		//getActionBar().setDisplayHomeAsUpEnabled(true);
		//setContentView(R.layout.searchview_filter);
  
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_my_tag, container, false); 

        mListView = (ListView) rootView.findViewById(R.id.list_view);

      mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Config.fgID = mFgID.get(position);
				
				goBookcase();
			}
		});

		showTags();
		
		return rootView;
	}
	
	protected void goBookcase() {
		Fragment newFragment = new BookcaseFragment(); 
	    // consider using Java coding conventions (upper first char class names!!!)
	    FragmentTransaction transaction = getFragmentManager().beginTransaction();

	    // Replace whatever is in the fragment_container view with this fragment,
	    // and add the transaction to the back stack
	    transaction.replace(R.id.container, newFragment);
	    transaction.addToBackStack(null);

	    // Commit the transaction
	    transaction.commit();		
	}

	public void showTags() {

		ReqUtil.send("bukrBooks/faviGroup/list", null, new COIMCallListener() {


			@Override
			public void onSuccess(JSONObject result) {
				Log.i(LOG_TAG, "success: "+result);
				mTags.clear();
				JSONArray jsonBooks  = Assist.getList(result);
				for(int i = 0; i < jsonBooks.length(); i++)  {
					JSONObject jsonBook;
					
					try {
						jsonBook = (JSONObject) jsonBooks.get(i);
						Log.i(LOG_TAG, "title: " + jsonBook.getString("title"));
						
						String title = jsonBook.getString("title");
						String fgID = jsonBook.getString("fgID");
						mTags.add(title);
						mFgID.add(fgID);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}
				Collections.reverse(mTags);
				Collections.reverse(mFgID);
				mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mTags);
		        mListView.setAdapter(mAdapter);
		        mListView.setTextFilterEnabled(true);
				
			}
			
			
			public void onFail(HttpResponse response, Exception exception) {
				Log.i(LOG_TAG, "fail: "+ exception.getLocalizedMessage());
				
			}
		});

	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
/*		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
*/		
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				"section_number"));
	}

}
