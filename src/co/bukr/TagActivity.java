package co.bukr;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardHeader.OnClickCardHeaderPopupMenuListener;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.view.CardListView;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import co.bukr.BookListCard.BookGridCardHeader;

import com.coimotion.csdk.common.COIMCallListener;
import com.coimotion.csdk.common.COIMException;
import com.coimotion.csdk.util.Assist;
import com.coimotion.csdk.util.ReqUtil;

public class TagActivity extends Activity {
	private final static String LOG_TAG = "TagActivity";
	private CardListView mListView;
	private CardArrayAdapter mCardArrayAdapter;
	//protected ArrayAdapter<Card> mBookCards;
	private ArrayList<Card> mBookCards = new ArrayList<Card>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.book_card_list);
		mListView = (CardListView) findViewById(R.id.book_card_list);
		
		
		try {
			ReqUtil.initSDK(getApplication());
		} catch (COIMException e) {
		} catch (Exception e) {
		}

		showTags();
	}

	public void showTags() {
		//mBookCards.clear();

		//Map<String, Object> mapParam = new HashMap<String, Object>();
		//mapParam.put("title", "我的書櫃");
		//mapParam.put("descTx", "用來收藏自己的書");
		//mapParam.put("share", "1");

		ReqUtil.send("Bookcase/tag/list", null, new COIMCallListener() {


			@Override
			public void onSuccess(JSONObject result) {
				Log.i(LOG_TAG, "success: "+result);
				JSONArray jsonBooks  = Assist.getList(result);
				
				for(int i = 0; i < jsonBooks.length(); i++)  {
					JSONObject jsonBook;
					
					try {
						jsonBook = (JSONObject) jsonBooks.get(i);
						Log.i(LOG_TAG, "title: " + jsonBook.getString("title"));
						
						String title = jsonBook.getString("title");
						
						//BookListCard bookCard = new BookListCard(TagActivity.this);
						//bookCard.setBookItem(new BookItem(bkID, iconURI, title));
						//bookCard.init();
						
						Card bookCard = new Card(getBaseContext());
						bookCard.setTitle(title);

						CardHeader header = new CardHeader(getBaseContext());
						header.setButtonOverflowVisible(true);

						header.setPopupMenu(R.menu.popup_edit,
								new CardHeader.OnClickCardHeaderPopupMenuListener() {
									@Override
									public void onMenuItemClick(BaseCard card, MenuItem item) {
										int id = item.getItemId();
										switch (id) {
										case R.id.card_edit:
											//addFavorite();
											break;
										default:
											break;
										}
									}
								});

						bookCard.addCardHeader(header);

						mBookCards.add(bookCard);
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}
				
		        mCardArrayAdapter = new CardArrayAdapter(TagActivity.this, mBookCards);

				
		        mListView.setAdapter(mCardArrayAdapter);
		        //mListView.setTextFilterEnabled(true);
				
				
			}
			
			@Override
			public void onFail(HttpResponse response, Exception exception) {
				Log.i(LOG_TAG, "fail: "+ exception.getLocalizedMessage());
				
			}
		});
		
	}


	

}
