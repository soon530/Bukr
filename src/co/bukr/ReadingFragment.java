package co.bukr;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.coimotion.csdk.common.COIMCallListener;
import com.coimotion.csdk.util.Assist;
import com.coimotion.csdk.util.ReqUtil;
import com.coimotion.csdk.util.sws;

public class ReadingFragment extends Fragment implements OnRefreshListener {
	private final static String LOG_TAG = "Reading";


	private OnFragmentInteractionListener mListener;
	//private ArrayList<BookItem> mBooks = new ArrayList<BookItem>();
	private ArrayList<Card> mBookCards = new ArrayList<Card>();
	private CardGridView mGirdView;
	private PullToRefreshLayout mPullToRefreshLayout;

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
		setHasOptionsMenu(true);
//		if (getArguments() != null) {
//			mParam1 = getArguments().getString(ARG_PARAM1);
//			mParam2 = getArguments().getString(ARG_PARAM2);
//		}
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.search, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();

		switch (id) {
		case R.id.action_search:
			Intent intent = new Intent();
			intent.setClass(getActivity(), SearchActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	private void showReading(final boolean isRefresh) {
		mBookCards.clear();

		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("cycle", "i");
		mapParam.put("favi", "1");
		
		ReqUtil.send("bukrBooks/book/whatsHot", mapParam, new COIMCallListener() {
			@Override
			public void onSuccess(JSONObject result) {
				Log.i(LOG_TAG, "success: "+result);
				JSONArray jsonBooks  = Assist.getList(result);
				
				for(int i = 0; i < jsonBooks.length(); i++)  {
					JSONObject jsonBook;
					
					try {
						jsonBook = (JSONObject) jsonBooks.get(i);

						String bkID = jsonBook.getString("bkID");
						String iconUrl = BukrUtlis.getBookIconUrl(jsonBook.getString("icon"));
						String title = jsonBook.getString("title");
						String author = jsonBook.getString("author");
						boolean isFavi =  jsonBook.getInt("isFavi") == 1 ? true : false;
						
//						Log.i(LOG_TAG, "bkID: " + bkID);
//						Log.i(LOG_TAG, "iconUrl: " + iconUrl);
//						Log.i(LOG_TAG, "title: " + title);
//						Log.i(LOG_TAG, "author: " + author);
						
						BookGridCard bookCard = new BookGridCard(getActivity());
						bookCard.setBookItem(new BookItem(bkID, iconUrl, title, author, isFavi));
						bookCard.init();
						mBookCards.add(bookCard);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}
				
		        CardGridArrayAdapter mCardArrayAdapter = new CardGridArrayAdapter(getActivity(), mBookCards);
				mGirdView.setAdapter(mCardArrayAdapter);
				
				if (isRefresh) 
					mPullToRefreshLayout.setRefreshComplete();
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
		View rootView = inflater.inflate(R.layout.book_card_grid, container, false); 
		mGirdView = (CardGridView) rootView.findViewById(R.id.book_card_grid);
		
		// Retrieve the PullToRefreshLayout from the content view
		mPullToRefreshLayout = (PullToRefreshLayout) rootView
				.findViewById(R.id.carddemo_extra_ptr_layout);

		// Now setup the PullToRefreshLayout
		 ActionBarPullToRefresh.from(getActivity())
		 	// Mark All Children as pullable
		 	.allChildrenArePullable()
		 	// Set the OnRefreshListener
			.listener(this)
			// Finally commit the setup to our PullToRefreshLayout
			.setup(mPullToRefreshLayout);

		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		//showReading(false);
		//isLoginFB();
		//showUserProfile();
		super.onViewCreated(view, savedInstanceState);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		showReading(false);
	}

	private void isLoginFB() {
		sws.checkFB(getActivity(), new COIMCallListener() {
			
			@Override
			public void onSuccess(JSONObject result) {
				Log.i(LOG_TAG, "isLoginFB() success: "+result);
				
			}
			
			@Override
			public void onFail(HttpResponse response, Exception exception) {
				Log.i(LOG_TAG, "fail: "+ exception.getLocalizedMessage());
				
			}
		});
	}

	private void showUserProfile() {
			
		ReqUtil.send("core/user/profile", null, new COIMCallListener() {
			@Override
			public void onSuccess(JSONObject result) {
				Log.i(LOG_TAG, "showUserProfile() success: "+result);
			}
			
			@Override
			public void onFail(HttpResponse response, Exception exception) {
				Log.i(LOG_TAG, "fail: "+ exception.getLocalizedMessage());
				
			}
		});
		
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

	@Override
	public void onRefreshStarted(View view) {
		showReading(true);
	}

}
