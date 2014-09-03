package co.bukr;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.coimotion.csdk.common.COIMCallListener;
import com.coimotion.csdk.common.COIMException;
import com.coimotion.csdk.util.Assist;
import com.coimotion.csdk.util.ReqUtil;
import com.coimotion.csdk.util.sws;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link ReadingFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link ReadingFragment#newInstance} factory method to create an instance of
 * this fragment.
 * 
 */
public class BookcaseFragment extends Fragment implements OnRefreshListener, OnQueryTextListener {
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
	private ArrayList<Card> mBookCards = new ArrayList<Card>();
	private CardGridView mGirdView;
	private PullToRefreshLayout mPullToRefreshLayout;
	private SearchView mSearchView;
	private CardGridArrayAdapter mCardArrayAdapter;

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
	public static BookcaseFragment newInstance(int sectionNumber) {
		BookcaseFragment fragment = new BookcaseFragment();
		Bundle args = new Bundle();
		args.putInt("section_number", sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public BookcaseFragment() {
		// Required empty public constructor
		
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		if (getArguments() != null) {
//			mParam1 = getArguments().getString(ARG_PARAM1);
//			mParam2 = getArguments().getString(ARG_PARAM2);
//		}
		
		//setHasOptionsMenu(true);

		try {
			ReqUtil.initSDK(getActivity().getApplication());
			sws.initSws(getActivity().getApplication());
		} catch (COIMException e) {
		} catch (Exception e) {
		}

		initImageLoader(getActivity());
		
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.bookcase, menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);
		mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		mSearchView.setOnQueryTextListener(this);
		//mSearchView.onActionViewExpanded();
		//customizeSearchIcon();

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

	
	public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
        .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
        .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
        //.writeDebugLogs()
        .denyCacheImageMultipleSizesInMemory()
        .build();
		
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	

	public void showReadingPeople(final boolean isRefresh) {
		mBookCards.clear();

		//Map<String, Object> mapParam = new HashMap<String, Object>();
		//mapParam.put("title", "我的書櫃");
		//mapParam.put("descTx", "用來收藏自己的書");
		//mapParam.put("share", "1");

		ReqUtil.send("Bookcase/tag/listBooks/3", null, new COIMCallListener() {

			@Override
			public void onSuccess(JSONObject result) {
				Log.i(LOG_TAG, "success: "+result);
				JSONArray jsonBooks  = Assist.getList(result);
				
				for(int i = 0; i < jsonBooks.length(); i++)  {
					JSONObject jsonBook;
					
					try {
						jsonBook = (JSONObject) jsonBooks.get(i);
						//Log.i(LOG_TAG, "book: " + jsonBook);

						Log.i(LOG_TAG, "bkID: " + jsonBook.getString("bkID"));
						Log.i(LOG_TAG, "iconURI: " + jsonBook.getString("iconURI"));
						Log.i(LOG_TAG, "title: " + jsonBook.getString("title"));
						
						String bkID = jsonBook.getString("bkID");
						String iconURI = jsonBook.getString("iconURI");
						String title = jsonBook.getString("title");
						
						BookcaseGridCard bookCard = new BookcaseGridCard(getActivity(), BookcaseFragment.this);
						bookCard.setBookItem(new BookItem(bkID, iconURI, title));
						bookCard.init();
						mBookCards.add(bookCard);
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}
				
		        mCardArrayAdapter = new CardGridArrayAdapter(getActivity(), mBookCards);

				
				mGirdView.setAdapter(mCardArrayAdapter);
				mGirdView.setTextFilterEnabled(true);
				
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
		showReadingPeople(false);
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

	@Override
	public void onRefreshStarted(View view) {
		showReadingPeople(true);
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
        	mGirdView.clearTextFilter();
        } else {
        	mGirdView.setFilterText(newText.toString());
        }

		return true;
	}

}
