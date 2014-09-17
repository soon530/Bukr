package co.bukr;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.coimotion.csdk.common.COIMCallListener;
import com.coimotion.csdk.common.COIMException;
import com.coimotion.csdk.util.Assist;
import com.coimotion.csdk.util.ReqUtil;

public class BookCommentActivity extends Activity {
	private final static String LOG_TAG = "SearchActivity";

	private TextView mContent;
	private CardListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("觀看書評");
		setContentView(R.layout.book_card_list);
		mListView = (CardListView) findViewById(R.id.book_card_list);
		try {
			ReqUtil.initSDK(getApplication());
		} catch (COIMException e) {
		} catch (Exception e) {
		}

		showComments();
		//mContent = (TextView) findViewById(R.id.content);
		//mContent.setText(Html.fromHtml(Config.content));
	}
	
	private void showComments() {
		//final ArrayList<Card> bookCards = new ArrayList<Card>();

		//Map<String, Object> mapParam = new HashMap<String, Object>();
		//mapParam.put("_ps", "12");
		//mapParam.put("pubName", keyWord);
		//mapParam.put("kw", keyWord);
		
		ReqUtil.send("books/comment/list/" + Config.bkID, null,
				new COIMCallListener() {


					@Override
					public void onSuccess(JSONObject result) {
						Log.i(LOG_TAG, "success: " + result);

						JSONArray jsonBooks  = Assist.getList(result);
												
//						for(int i = 0; i < jsonBooks.length(); i++)  {
//							JSONObject jsonBook;
//							
//							try {
//								jsonBook = (JSONObject) jsonBooks.get(i);
//								//Log.i(LOG_TAG, "book: " + jsonBook);
//
//								Log.i(LOG_TAG, "bkID: " + jsonBook.getString("bkID"));
//								Log.i(LOG_TAG, "iconURI: " + jsonBook.getString("iconURI"));
//								Log.i(LOG_TAG, "title: " + jsonBook.getString("title"));
//								
//								String bkID = jsonBook.getString("bkID");
//								String iconURI = jsonBook.getString("iconURI");
//								String title = jsonBook.getString("title");
//								
//								BookGridCard bookCard = new BookGridCard(getBaseContext());
//								bookCard.setBookItem(new BookItem(bkID, iconURI, title));
//								bookCard.init();
//								bookCards.add(bookCard);
//								
//
//
//								
//							} catch (JSONException e) {
//								e.printStackTrace();
//							}
//
//						}
//						
//						CardArrayAdapter mCardAdapter = new CardArrayAdapter(getBaseContext() , bookCards);
//
//				        mListView.setAdapter(mCardArrayAdapter);
					
					}
					

					@Override
					public void onFail(HttpResponse response,
							Exception exception) {
						Log.i(LOG_TAG,
								"fail: " + exception.getLocalizedMessage());

					}
				});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.comment, menu);
		return super.onCreateOptionsMenu(menu);
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch (id) {
		case android.R.id.home:
			finish();
			break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

}
