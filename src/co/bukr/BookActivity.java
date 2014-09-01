package co.bukr;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.coimotion.csdk.common.COIMCallListener;
import com.coimotion.csdk.common.COIMException;
import com.coimotion.csdk.util.ReqUtil;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class BookActivity extends Activity  {
	private final static String LOG_TAG = "Book";
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private ImageView mImageItem;
	private TextView mTextItem;
	private TextView mTitle;
	private TextView mAuthor;
	private TextView mPublisher;
	private TextView mPrice;
	private TextView mSellPrice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(android.R.color.transparent));
		// getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setIcon(R.drawable.nav_logo);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		// 设置ActionBar 背景色 透明
		setContentView(R.layout.activity_book2);

		try {
			ReqUtil.initSDK(getApplication());
		} catch (COIMException e) {
		} catch (Exception e) {
		}

		initImageLoader(this);
		showBookDetail();

		mImageItem = (ImageView) findViewById(R.id.item_image);
		mTextItem = (TextView) findViewById(R.id.item_text);
		
		mTitle = (TextView) findViewById(R.id.title);
		mAuthor = (TextView) findViewById(R.id.author);
		mPublisher = (TextView) findViewById(R.id.publisher);
		mPrice = (TextView) findViewById(R.id.price);
		mSellPrice = (TextView) findViewById(R.id.sell_price);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.book, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch (id) {
		case android.R.id.home:
			finish();
			break;
		case R.id.action_search:
			Intent intent = new Intent();
			intent.setClass(this, SearchActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				// .writeDebugLogs()
				.denyCacheImageMultipleSizesInMemory().build();

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	private void showBookDetail() {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("record", "1");
		mapParam.put("info", "1");

		ReqUtil.send("twBook/book/info/" + Config.bkID, mapParam,
				new COIMCallListener() {

					//private BooksAdapter adapter;

					@Override
					public void onSuccess(JSONObject result) {
						Log.i(LOG_TAG, "success: " + result);

						try {
							JSONObject jsonBook = (JSONObject) result
									.get("value");

							Log.i(LOG_TAG,
									"bkID: " + jsonBook.getString("bkID"));
							Log.i(LOG_TAG,
									"iconURI: " + jsonBook.getString("iconURI"));
							Log.i(LOG_TAG,
									"title: " + jsonBook.getString("title"));

							Log.i(LOG_TAG,
									"infoList: "
											+ jsonBook.getString("infoList"));

							JSONArray infoList = jsonBook
									.getJSONArray("infoList");

							// JSONArray jsonInfoList =
							// Assist.getList(infoList);

							String s = "";
							for (int i = 0; i < infoList.length(); i++) {
								JSONObject json_data = infoList
										.getJSONObject(i);
								s = s + "*" + i + "*"
										+ json_data.getString("body");
							}

							imageLoader.displayImage(
									jsonBook.getString("iconURI").trim(),
									mImageItem, Config.OPTIONS, null);

							mTitle.setText(jsonBook.getString("title"));
							mAuthor.setText("Eason");
							mPublisher.setText(jsonBook.getString("publisher"));
							
							int sellPrice = (int) (jsonBook.getInt("price") * 0.9);
							mPrice.setText("定價："+ jsonBook.getString("price"));
							mSellPrice.setText("特價："+String.valueOf(sellPrice));
							mTextItem.setText(Html.fromHtml(s));

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						/*
						 * JSONArray jsonBooks = Assist.getList(result);
						 * 
						 * for (int i = 0; i < jsonBooks.length(); i++) {
						 * JSONObject jsonBook;
						 * 
						 * try { jsonBook = (JSONObject) jsonBooks.get(i); //
						 * Log.i(LOG_TAG, "book: " + jsonBook);
						 * 
						 * Log.i(LOG_TAG, "bkID: " +
						 * jsonBook.getString("bkID")); Log.i(LOG_TAG,
						 * "iconURI: " + jsonBook.getString("iconURI"));
						 * Log.i(LOG_TAG, "title: " +
						 * jsonBook.getString("title"));
						 * 
						 * String iconURI = jsonBook.getString("iconURI");
						 * String title = jsonBook.getString("title"); String
						 * bkID = jsonBook.getString("bkID");
						 * 
						 * } catch (JSONException e) { e.printStackTrace(); }
						 * 
						 * }
						 */
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
