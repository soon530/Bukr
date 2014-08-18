package co.bukr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.coimotion.csdk.common.COIMCallListener;
import com.coimotion.csdk.common.COIMException;
import com.coimotion.csdk.util.Assist;
import com.coimotion.csdk.util.ReqUtil;
import com.coimotion.csdk.util.sws;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SimpleAdapter;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link ReadingFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link ReadingFragment#newInstance} factory method to create an instance of
 * this fragment.
 * 
 */
public class ReadingFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private final static String LOG_TAG = "Reading";
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private OnFragmentInteractionListener mListener;
	private ArrayList<BookItem> mBooks = new ArrayList<BookItem>();
	private GridView mGirdView;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment ReadingFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static ReadingFragment newInstance(int sectionNumber) {
		ReadingFragment fragment = new ReadingFragment();
		Bundle args = new Bundle();
		args.putInt("section_number", sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public ReadingFragment() {
		// Required empty public constructor
		
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		if (getArguments() != null) {
//			mParam1 = getArguments().getString(ARG_PARAM1);
//			mParam2 = getArguments().getString(ARG_PARAM2);
//		}

		try {
			ReqUtil.initSDK(getActivity().getApplication());
			sws.initSws(getActivity().getApplication());
		} catch (COIMException e) {
		} catch (Exception e) {
		}

		
	}
	

	private void showReadingPeople() {
		
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("cycle", "week");

		ReqUtil.send("twBook/book/whatsHot", mapParam, new COIMCallListener() {
			
			private BooksAdapter adapter;

			@Override
			public void onSuccess(JSONObject result) {
				Log.i(LOG_TAG, "success: "+result);
				JSONArray jsonBooks  = Assist.getList(result);
				
				for(int i = 0; i < jsonBooks.length(); i++)  {
					JSONObject jsonBook;
					
					try {
						jsonBook = (JSONObject) jsonBooks.get(i);
						//Log.i(LOG_TAG, "book: " + jsonBook);

						Log.i(LOG_TAG, "iconURI: " + jsonBook.getString("iconURI"));
						Log.i(LOG_TAG, "title: " + jsonBook.getString("title"));
						
						String iconURI = jsonBook.getString("iconURI");
						String title = jsonBook.getString("title");
						mBooks.add(new BookItem(iconURI, title));						
						
						
						
						
						adapter = new BooksAdapter(
								getActivity(), 
								R.layout.row_books, 
								mBooks  
								);
						
						mGirdView.setAdapter(adapter);

						
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}
				
				
			}
			
			@Override
			public void onFail(HttpResponse response, Exception exception) {
				Log.i(LOG_TAG, "fail: "+ exception.getLocalizedMessage());
				
			}
		});
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_reading, container, false); 
		mGirdView = (GridView) rootView.findViewById(R.id.gridView1);
		
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		showReadingPeople();
		super.onViewCreated(view, savedInstanceState);
	}
	


	// TODO: Rename method, update argument and hook method into UI event
/*	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}
*/
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

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(Uri uri);
	}

}
