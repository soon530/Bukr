package co.bukr;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.coimotion.csdk.common.COIMCallListener;
import com.coimotion.csdk.util.Assist;
import com.coimotion.csdk.util.ReqUtil;
import com.felipecsl.abslistviewhelper.library.AbsListViewHelper;

public class MyTagFragment extends Fragment  {
	private final static String LOG_TAG = "MyTagActivity";
    private CardGridView mListView;
    //private ArrayAdapter<String> mAdapter;
	protected ArrayList<String> mTags = new ArrayList<String>();
	protected ArrayList<String> mFgID = new ArrayList<String>();
	private FrameLayout mHeaderView;
	private AbsListViewHelper helper;
	private ArrayList<Card> mBookCards = new ArrayList<Card>();
	private CardGridArrayAdapter mCardArrayAdapter;

	
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

		mHeaderView = (FrameLayout) rootView.findViewById(R.id.header);
        mListView = (CardGridView) rootView.findViewById(R.id.list_view);

      mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Config.fgID = mFgID.get(position);
				
				goBookcase();
			}
		});

      
      helper = new AbsListViewHelper(mListView, savedInstanceState)
      .setHeaderView(mHeaderView);
		
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		showTags();
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
						
						BookcaseGridCard bookCard = new BookcaseGridCard(getActivity(), MyTagFragment.this );
						bookCard.setFavoriteItem(new FavoriteItem(fgID, title));
						bookCard.init();
						mBookCards.add(bookCard);

					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}
				Collections.reverse(mTags);
				Collections.reverse(mFgID);
				//mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mTags);
		        //mListView.setAdapter(mAdapter);

		        mCardArrayAdapter = new CardGridArrayAdapter(getActivity(), mBookCards);
		        mListView.setAdapter(mCardArrayAdapter);
				
			}
			
			
			public void onFail(HttpResponse response, Exception exception) {
				Log.i(LOG_TAG, "fail: "+ exception.getLocalizedMessage());
				
			}
		});

	}
	
	protected void showInputDialog(final FavoriteItem favoriteItem) {

		// get prompts.xml view
		LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
		View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());
		alertDialogBuilder.setView(promptView);

		final EditText editText = (EditText) promptView
				.findViewById(R.id.edittext);
		editText.setText(favoriteItem.mTitle);
		// setup a dialog window
		alertDialogBuilder
				.setCancelable(false)
				.setTitle("請輸入書單名稱")
				.setPositiveButton("確認", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//resultText.setText("Hello, " + editText.getText());
						//addTag(editText.getText().toString());
						//showTags();
						editFavorite(editText.getText().toString(), favoriteItem);
					}
				})
				.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		// create an alert dialog
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();

	}

	private void editFavorite(String title, FavoriteItem favorite) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("title", title);

		ReqUtil.send("bukrBooks/faviGroup/update/"+ favorite.mFgID, null, new COIMCallListener() {

			@Override
			public void onSuccess(JSONObject result) {
				Log.i(LOG_TAG, "success: "+result);
				MyTagFragment.this.showTags();
			}
			
			@Override
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
