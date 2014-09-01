package co.bukr;

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

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

public class SearchActivity extends Activity implements OnQueryTextListener {
	private final static String LOG_TAG = "SearchActivity";

	private SearchView mSearchView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			ReqUtil.initSDK(getApplication());
		} catch (COIMException e) {
		} catch (Exception e) {
		}

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.search, menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);
		mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		mSearchView.setOnQueryTextListener(this);
		mSearchView.setIconifiedByDefault(false);

		customizeSearchIcon();

		// setupSearchView(searchItem);
		return super.onCreateOptionsMenu(menu);
	}

	private void customizeSearchIcon() {
		int searchIconId = mSearchView.getContext().getResources()
				.getIdentifier("android:id/search_button", null, null);
		ImageView searchIcon = (ImageView) mSearchView
				.findViewById(searchIconId);
		searchIcon.setImageResource(R.drawable.search);
	}

	@Override
	public boolean onQueryTextChange(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String keyWord) {

		searchBook(keyWord);
		
		return false;
	}
	
	private void searchBook(String keyWord) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("pubName", keyWord);

		ReqUtil.send("twBook/book/search", mapParam,
				new COIMCallListener() {


					@Override
					public void onSuccess(JSONObject result) {
						Log.i(LOG_TAG, "success: " + result);

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
								
								//BookGridCard bookCard = new BookGridCard(getActivity());
								//bookCard.setBookItem(new BookItem(bkID, iconURI, title));
								//bookCard.init();
								//mBookCards.add(bookCard);
								
								//mBooks.add(new BookItem(bkID, iconURI, title));						


								
							} catch (JSONException e) {
								e.printStackTrace();
							}

						}
					}
					

					@Override
					public void onFail(HttpResponse response,
							Exception exception) {
						Log.i(LOG_TAG,
								"fail: " + exception.getLocalizedMessage());

					}
				});

	}


}
