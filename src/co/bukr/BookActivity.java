package co.bukr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.coimotion.csdk.common.COIMCallListener;
import com.coimotion.csdk.util.Assist;
import com.coimotion.csdk.util.ReqUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class BookActivity extends Activity implements OnClickListener  {
	private final static String LOG_TAG = "BookActivity";
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private ImageView mImageItem;
	private TextView mTextItem;
	private TextView mTitle;
	private TextView mAuthor;
	private TextView mPublisher;
	private TextView mPrice;
	private TextView mSellPrice;
	private ImageView mBackGround;
	private ImageView mAddFavorite;
	
	private boolean mHasAdd;
	private MenuItem mReading;
	private ImageView mBookComment;
	private ArrayList<String> mBooklists = new ArrayList<String>();
	private ImageView mAddShoppingCard;
	private SharedPreferences mPref;

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

		mImageItem = (ImageView) findViewById(R.id.item_image);
		mTextItem = (TextView) findViewById(R.id.item_text);
		
		mTitle = (TextView) findViewById(R.id.title);
		mAuthor = (TextView) findViewById(R.id.author);
		mPublisher = (TextView) findViewById(R.id.publisher);
		mPrice = (TextView) findViewById(R.id.price);
		mSellPrice = (TextView) findViewById(R.id.sell_price);
		
		mBackGround = (ImageView) findViewById(R.id.background);
		
		String url = "http://imagizer.imageshack.us/a/img540/761/dOp0Wo.jpg";
		imageLoader.displayImage(
				url,
				mBackGround, Config.OPTIONS, null);
		
		mAddFavorite = (ImageView) findViewById(R.id.add_favorite);
		mAddFavorite.setOnClickListener(this);
		
		mBookComment = (ImageView) findViewById(R.id.book_comment);
		
		mBookComment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intentReading = new Intent();
				intentReading.setClass(BookActivity.this, BookCommentActivity.class);
				startActivity(intentReading);
			}
		});
		
		mAddShoppingCard = (ImageView) findViewById(R.id.add_shopping_car);
		mAddShoppingCard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Assist.showToast(BookActivity.this, "[加入購物車] 功能開發中...");				
			}
		});
		
		mPref = getApplication().getSharedPreferences("bukr", 0);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		getFavorite();
		
	}
	
	private void getFavorite() {
		//Map<String, Object> mapParam = new HashMap<String, Object>();
		//mapParam.put("bkID", Config.bkID);

		ReqUtil.send(Config.BukrData+"/faviGroup/contains/"+Config.bkID, null, new COIMCallListener() {

			@Override
			public void onSuccess(JSONObject result) {
				Log.i(LOG_TAG, "success: "+result);
				JSONArray jsonBooks  = Assist.getList(result);
				
				if ( jsonBooks.length() == 0 /*Assist.getErrCode(result) == 0*/) {
					mAddFavorite.setImageResource(R.drawable.save_button);
					mHasAdd = false;
				} else {
					mAddFavorite.setImageResource(R.drawable.done_button);
					mHasAdd = true;
				}

				//這本書已加入的「書單」有那些？先存起來，方便等一下全部取消用
				for(int i = 0; i < jsonBooks.length(); i++)  {
					JSONObject jsonBook;
					
					try {
						jsonBook = (JSONObject) jsonBooks.get(i);
						Log.i(LOG_TAG, "title: " + jsonBook.getString("fgID"));
						
						String fgID = jsonBook.getString("fgID");
						mBooklists .add(fgID);
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}

				
			}
			
			@Override
			public void onFail(HttpResponse response, Exception exception) {
				Log.i(LOG_TAG, "fail: "+ exception.getLocalizedMessage());
				
			}
		});
		
	}

	@Override
	public void onClick(View v) {
		if (mPref.getBoolean("login", false) == false) {
			showLoginAgainDialog();
			//goLogin();
		} else {
		
			if (mHasAdd) {
				showDelDialog();
			} else {
				addFavorite();
			}
		}
	}

	private void showLoginAgainDialog() {
		// get prompts.xml view
		//LayoutInflater layoutInflater = LayoutInflater.from(this);
		//View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		//alertDialogBuilder.setView(promptView);

		//final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
		// setup a dialog window
		alertDialogBuilder
				.setCancelable(false)
				.setTitle("提醒一下")
				.setMessage("這個功能只有會員才能使用，要現在註冊/登入嗎？")
				.setPositiveButton("登入", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						goLogin();
					}
				})
				.setNegativeButton("知道了",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		// create an alert dialog
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
		
	}

	private void goLogin() {

		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setClass(getBaseContext(), LoginActivity.class);
		startActivity(intent);
	}

	
	
	protected void showDelDialog() {

		// get prompts.xml view
		//LayoutInflater layoutInflater = LayoutInflater.from(this);
		//View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		//alertDialogBuilder.setView(promptView);

		//final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
		// setup a dialog window
		alertDialogBuilder
				.setCancelable(false)
				.setTitle("取消收藏")
				.setMessage("將會取消此書的所有收藏")
				.setPositiveButton("確認", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						delFavorite();
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

	
	private void delFavorite() {
		
		for (String fgID : mBooklists) {
			removeBookFromFavorite(fgID);
		}
		
		mHasAdd = false;
		mAddFavorite.setImageResource(R.drawable.save_button);

	}
	

	private void removeBookFromFavorite(String fgID) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("bkID", Config.bkID);

		ReqUtil.send(Config.BukrData + "/faviGroup/rmBook/"+ fgID, mapParam, new COIMCallListener() {
			

			@Override
			public void onSuccess(JSONObject result) {
				Log.i(LOG_TAG, "success: "+result);
				//JSONArray jsonBooks  = Assist.getList(result);
				
				if (Assist.getErrCode(result) == 0) {
					//Assist.showToast(getBaseContext(), "取消收藏成功!");

				} else {
					//Assist.showToast(getBaseContext(), "取消收藏失敗!");
				}
			}
			
			@Override
			public void onFail(HttpResponse response, Exception exception) {
				Log.i(LOG_TAG, "fail: "+ exception.getLocalizedMessage());
				
			}
		});
	}

	private void addFavorite() {
		Intent intentTag = new Intent();
		intentTag.setClass(this, TagActivity.class);
		startActivity(intentTag);
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.book, menu);
		mReading = menu.findItem(R.id.action_reading);
		
		showBookDetail();

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
		case R.id.action_reading:
			Intent intentReading = new Intent();
			intentReading.setClass(this, TryActivity.class);
			startActivity(intentReading);
			break;
//		case R.id.action_share:
//			Assist.showToast(BookActivity.this, "分享功能開發...");
//			break;
		case R.id.action_edit:
			Intent intentTag = new Intent();
			intentTag.setClass(this, TagActivity.class);
			startActivity(intentTag);
			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	private void showBookDetail() {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("record", "1");
		mapParam.put("info", "1");
		mapParam.put("waCode", "bukruat");

		//public data
		ReqUtil.send(Config.CoimtionData + "/book/info/" + Config.bkID, mapParam,
				new COIMCallListener() {

					//private BooksAdapter adapter;

					@Override
					public void onSuccess(JSONObject result) {
						Log.i(LOG_TAG, "success: " + result);

						try {
							//JSONObject jsonBook = (JSONObject) result.get("value");
							JSONObject jsonBook = Assist.getValue(result);
							Log.i(LOG_TAG, "success: " + jsonBook);
							

							//String bkID = jsonBook.getString("bksID");
							String icon = "http";
							if (!jsonBook.isNull("icon")) {
								icon = jsonBook.getString("icon");
							}
							String title = jsonBook.getString("title");
							String author = jsonBook.getString("author");
							String publisher = jsonBook.getString("publisher");
							String price = jsonBook.getString("price");
							String sellPrice = getSellPrice(jsonBook.getInt("price"));
							
							
							String bnAbstract = "";
							if (!jsonBook.isNull("bnAbstract") ) {
								bnAbstract = jsonBook.getString("bnAbstract");
							}
							
							String bnExerpt = "";
							if (!jsonBook.isNull("bnExcerpt")) {
								bnExerpt = jsonBook.getString("bnExcerpt");
								Config.content = bnExerpt;
							}
							
							Log.i(LOG_TAG, "icon: " + icon);
							Log.i(LOG_TAG, "title: " + title);
							Log.i(LOG_TAG, "author: " + author);
							Log.i(LOG_TAG, "publisher: " + publisher);
							Log.i(LOG_TAG, "publisher: " + price);
							Log.i(LOG_TAG, "sellPrice: " + sellPrice);
							Log.i(LOG_TAG, "bnExerpt: " + bnExerpt);
							

							imageLoader.displayImage(
									BukrUtlis.getBookIconUrl(icon),
									mImageItem, Config.OPTIONS, null);
							mTitle.setText(title);
							mAuthor.setText(author );
							mPublisher.setText(publisher );
							mPrice.setText("定價："+ price );
							mSellPrice.setText("特價："+ sellPrice);
							mTextItem.setText(Html.fromHtml(bnAbstract));
							
							// 有可能menu還沒new出來
							if (bnExerpt.isEmpty() && mReading != null && mReading.getIcon()!= null) {
								mReading.setEnabled(false);
								mReading.getIcon().setAlpha(125);
							} else {
								mReading.getIcon().setAlpha(255);
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
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

	private String getSellPrice(int price) {
		//int sellPrice = (int) (price * 0.9);
		//return String.valueOf(sellPrice);
		return "N/A";
	}

}
