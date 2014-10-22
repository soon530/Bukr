package co.bukr;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardGridView;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;












import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coimotion.csdk.common.COIMCallListener;
import com.coimotion.csdk.util.Assist;
import com.coimotion.csdk.util.ReqUtil;
import com.felipecsl.abslistviewhelper.library.AbsListViewHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

public class BooklistActivity extends Activity {
	private final static String LOG_TAG = "BooklistActivity";

	//private ArrayList<Card> mBookCards = new ArrayList<Card>();
	private CardGridView mGridView;
	private ArrayList<Card> mBookCards = new ArrayList<Card>();

	private FrameLayout mHeaderView;

	private AbsListViewHelper helper;

	private TextView mBooklistName;
	
	private ImageLoader imageLoader = ImageLoader.getInstance();

	private LinearLayout mBookCover;

	private ImageView mBook0;

	private ImageView mBook1;

	private ImageView mBook2;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(android.R.color.transparent));
		getActionBar().setIcon(R.drawable.nav_logo);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.activity_booklist);
		mHeaderView = (FrameLayout) findViewById(R.id.header);
		mBooklistName = (TextView) findViewById(R.id.name);
		mGridView = (CardGridView) findViewById(R.id.book_card_grid);
		
	
	      helper = new AbsListViewHelper(mGridView, savedInstanceState)
	      .setHeaderView(mHeaderView);

	      mBooklistName.setText(Config.my_favorite_title);
		
		
		
		 View bookCoverView = getLayoutInflater().inflate(R.layout.booklist_cover, null, false);
		 mBookCover = (LinearLayout) bookCoverView.findViewById(R.id.book_cover);

		 
		 mBook0 = (ImageView) bookCoverView.findViewById(R.id.book_big);
		 mBook1 = (ImageView) bookCoverView.findViewById(R.id.book_small1);
		 mBook2 = (ImageView) bookCoverView.findViewById(R.id.book_small2);

	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		showReading(false);
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

	private void showReading(final boolean isRefresh) {
		mBookCards.clear();
//		Map<String, Object> mapParam = new HashMap<String, Object>();
//		mapParam.put("cycle", "i");
//		mapParam.put("favi", "1");
		
		ReqUtil.send( Config.BukrData+"/faviGroup/listBooks/" + Config.fgID, null, new COIMCallListener() {
			@Override
			public void onSuccess(JSONObject result) {
				Log.i(LOG_TAG, "success: "+result);
				JSONArray jsonBooks  = Assist.getList(result);
				
				for(int i = 0; i < jsonBooks.length(); i++)  {
					JSONObject jsonBook;
					
					try {
						jsonBook = (JSONObject) jsonBooks.get(i);

						String bkID = jsonBook.getString("bkID");
						String iconUrl = "http";
						if (jsonBook.isNull("icon")) {
							iconUrl = BukrUtlis.getBookIconUrl(jsonBook.getString("icon"));
						}
						String title = jsonBook.getString("title");
						String author = jsonBook.getString("author");
						boolean isFavi = true;  //jsonBook.getInt("isFavi") == 1 ? true : false;
						
//						Log.i(LOG_TAG, "bkID: " + bkID);
//						Log.i(LOG_TAG, "iconUrl: " + iconUrl);
//						Log.i(LOG_TAG, "title: " + title);
//						Log.i(LOG_TAG, "author: " + author);
						
						if (i == 0) {
							Config.book0 = iconUrl;
						}else if(i ==1) {
							Config.book1 = iconUrl;
						}else if (i == 2) {
							Config.book2 = iconUrl;
						}
						
						BookGridCard bookCard = new BookGridCard(getBaseContext());
						bookCard.setBookItem(new BookItem(bkID, iconUrl, title, author, isFavi));
						bookCard.init();
						mBookCards.add(bookCard);
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}
				
		        CardGridArrayAdapter mCardArrayAdapter = new CardGridArrayAdapter(getBaseContext(), mBookCards);
				mGridView.setAdapter(mCardArrayAdapter);
				
				getFavoriteCover();
				
//				if (isRefresh) 
//					mPullToRefreshLayout.setRefreshComplete();
			}
			
			@Override
			public void onFail(HttpResponse response, Exception exception) {
				Log.i(LOG_TAG, "fail: "+ exception.getLocalizedMessage());
				
			}
		});
		
	}


	protected void getFavoriteCover() {

		
		imageLoader.displayImage(Config.book0, mBook0, Config.OPTIONS, null);
		imageLoader.displayImage(Config.book1, mBook1, Config.OPTIONS, null);
		imageLoader.displayImage(Config.book2, mBook2, Config.OPTIONS, null);


		 Config.book_cover = convertViewToBitmap(mBookCover);

	}
	
	public static Bitmap convertViewToBitmap(View view){
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
	    view.buildDrawingCache();
	    Bitmap bitmap = view.getDrawingCache();

	    return bitmap;
	}

}
