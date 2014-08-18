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
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class BookActivity extends Activity {
	private final static String LOG_TAG = "Book";
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private ImageView mImageItem;
	private TextView mTextItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book);

		try {
			ReqUtil.initSDK(getApplication());
		} catch (COIMException e) {
		} catch (Exception e) {
		}
		
		initImageLoader(this);
		showBookDetail();
		
		mImageItem = (ImageView) findViewById(R.id.item_image);
		mTextItem = (TextView) findViewById(R.id.item_text);
		
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


	private void showBookDetail() {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("record", "1");

		ReqUtil.send("twBook/book/info/" + Config.bkID, mapParam,
				new COIMCallListener() {

					private BooksAdapter adapter;

					@Override
					public void onSuccess(JSONObject result) {
						Log.i(LOG_TAG, "success: " + result);
						
						
						try {
							JSONObject jsonBook = (JSONObject) result.get("value");

							Log.i(LOG_TAG,
									"bkID: " + jsonBook.getString("bkID"));
							Log.i(LOG_TAG,
									"iconURI: "
											+ jsonBook.getString("iconURI"));
							Log.i(LOG_TAG,
									"title: " + jsonBook.getString("title"));
							
							imageLoader.displayImage(
									jsonBook.getString("iconURI").trim(), 
									mImageItem,
									Config.OPTIONS, null);
							
							mTextItem.setText(
									"書名：" + jsonBook.getString("title") +
									"\n定價：" + jsonBook.getString("price") +
									"\n出版社名稱：" + jsonBook.getString("publisher") +
									"\nISBN：" + jsonBook.getString("ISBN")
									);


						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						
/*						JSONArray jsonBooks = Assist.getList(result);

						for (int i = 0; i < jsonBooks.length(); i++) {
							JSONObject jsonBook;

							try {
								jsonBook = (JSONObject) jsonBooks.get(i);
								// Log.i(LOG_TAG, "book: " + jsonBook);

								Log.i(LOG_TAG,
										"bkID: " + jsonBook.getString("bkID"));
								Log.i(LOG_TAG,
										"iconURI: "
												+ jsonBook.getString("iconURI"));
								Log.i(LOG_TAG,
										"title: " + jsonBook.getString("title"));

								String iconURI = jsonBook.getString("iconURI");
								String title = jsonBook.getString("title");
								String bkID = jsonBook.getString("bkID");

							} catch (JSONException e) {
								e.printStackTrace();
							}

						}
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
