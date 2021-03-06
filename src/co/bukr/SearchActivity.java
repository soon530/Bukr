package co.bukr;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.MergeCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.coimotion.csdk.common.COIMCallListener;
import com.coimotion.csdk.common.COIMException;
import com.coimotion.csdk.util.Assist;
import com.coimotion.csdk.util.ReqUtil;

public class SearchActivity extends Activity implements OnQueryTextListener {
	private final static String LOG_TAG = "SearchActivity";

	private SearchView mSearchView;
	//private ArrayList<Card> mBookCards = new ArrayList<Card>();
	private CardGridView mGridView;

	private String mKeyWork = "";
	ArrayList<Card> bookCards = new ArrayList<Card>();

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.book_card_grid);
		mGridView = (CardGridView) findViewById(R.id.book_card_grid);
	
		if (!mKeyWork.isEmpty()) {
			searchBook(mKeyWork);
		}

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.search_reading, menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);
		mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		mSearchView.setOnQueryTextListener(this);
		//mSearchView.setIconifiedByDefault(false);
		mSearchView.onActionViewExpanded();
		Log.i(LOG_TAG, "SearchView width: "+mSearchView.getWidth());
		// 先寫死，之後再用density去算
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int withDpi = (int) (metrics.density * 280f);
		mSearchView.setMaxWidth(withDpi);
		//customizeSearchIcon();
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch (id) {
		case android.R.id.home:
			finish();
			break;
		case R.id.action_scan:
			IntentIntegrator scanIntegrator = new IntentIntegrator(this);
			scanIntegrator.initiateScan();
			break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, data);
		if (scanningResult != null) {
			String scanContent = scanningResult.getContents();
			Assist.showToast(SearchActivity.this, "ISBN：" + scanContent);
			scanBook(scanContent);
		} else {
			Assist.showToast(SearchActivity.this, "掃描不到任何資料!");
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void scanBook(final String scanContent) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("ISBN", scanContent);
		ReqUtil.send(Config.CoimtionData+"/book/search", mapParam,
				new COIMCallListener() {
					@Override
					public void onSuccess(JSONObject result) {
						Log.i(LOG_TAG, "success: " + result);

						JSONArray jsonBooks  = Assist.getList(result);
						
						if (jsonBooks.length() == 0) {
							Assist.showToast(SearchActivity.this, "沒有搜尋到任何資料!");
						} else {
							try {
								JSONObject jsonBook = (JSONObject) jsonBooks.get(0);
								Log.i(LOG_TAG, "bkID: " + jsonBook.getString("bkID"));
								String bkID = jsonBook.getString("bkID");

								Config.bkID = bkID;

								Intent intent = new Intent();
								// intent.putExtra("spID", spID);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.setClass(getBaseContext(), BookActivity.class);
								startActivity(intent);
								
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

	private void customizeSearchIcon() {
		int searchIconId = mSearchView.getContext().getResources()
				.getIdentifier("android:id/search_button", null, null);
		ImageView searchIcon = (ImageView) mSearchView
				.findViewById(searchIconId);
		searchIcon.setImageResource(R.drawable.search);
	}

	@Override
	public boolean onQueryTextChange(String arg0) {
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String keyWord) {
		mKeyWork = keyWord;
		searchBook(keyWord);
		mSearchView.clearFocus();
		return false;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		
	}
	
	
	private void searchBook(String keyWord) {
		bookCards.clear();
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("_ps", "33");
		//mapParam.put("pubName", keyWord);
		mapParam.put("kw", keyWord);
		mapParam.put("favi", "1");
		mapParam.put("waCode", Config.WA_CODE);
		
		ReqUtil.send(Config.CoimtionData+"/book/search", mapParam,
				new COIMCallListener() {


					@Override
					public void onSuccess(JSONObject result) {
						Log.i(LOG_TAG, "success: " + result);

						JSONArray jsonBooks  = Assist.getList(result);
						
						if (jsonBooks.length() == 0) {
							Assist.showToast(SearchActivity.this, "沒有搜尋到任何資料!");
						}
						
						for(int i = 0; i < jsonBooks.length(); i++)  {
							JSONObject jsonBook;
							
							try {
								jsonBook = (JSONObject) jsonBooks.get(i);
								//Log.i(LOG_TAG, "book: " + jsonBook);

								String bkID = jsonBook.getString("bkID");
								String iconURI = "http";
								if (!jsonBook.isNull("icon")) {
									iconURI = BukrUtlis.getBookIconUrl(jsonBook.getString("icon"));
								}
								
								String title = jsonBook.getString("title");
								String author = jsonBook.getString("author");
								boolean isFavi =  jsonBook.getInt("isFavi") == 1 ? true : false;

								Log.i(LOG_TAG, "bkID: " + bkID);
								//Log.i(LOG_TAG, "iconURI: " + jsonBook.getString("iconURI"));
								Log.i(LOG_TAG, "title: " + title);
								Log.i(LOG_TAG, "isFavi: " + isFavi);

								BookGridCard bookCard = new BookGridCard(getBaseContext());
								bookCard.setBookItem(new BookItem(bkID, iconURI, title, author, isFavi));
								bookCard.init();
								bookCards.add(bookCard);
								
								
							} catch (JSONException e) {
								e.printStackTrace();
							}

						}
						
						CardGridArrayAdapter mCardArrayAdapter = new CardGridArrayAdapter(getBaseContext() , bookCards);

				        mGridView.setAdapter(mCardArrayAdapter);
					
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
