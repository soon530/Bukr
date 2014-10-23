package co.bukr;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coimotion.csdk.common.COIMCallListener;
import com.coimotion.csdk.util.Assist;
import com.coimotion.csdk.util.ReqUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class BookcaseGridCard extends Card {

	private final static String LOG_TAG = "BookGridCard";
	private MyTagFragment mBookcase;
	private FavoriteItem mFavoriteItem;
	private ImageView mBookBig;
	private ImageView mBookSmall1;
	private ImageView mBookSmall2;
	
	private ImageLoader imageLoader = ImageLoader.getInstance();


	public BookcaseGridCard(Context context, MyTagFragment bookcaseFragment) {
		super(context, R.layout.booklist_cover);
		mBookcase = bookcaseFragment;
	}

	public BookcaseGridCard(Context context, int innerLayout) {
		super(context, innerLayout);
		// init();
	}

	public void init() {

		CardHeader header = new BookGridCardHeader(getContext());
		
		SharedPreferences pref = getContext().getSharedPreferences("bukr", 0);
		if (!mFavoriteItem.mFgID.equals(pref.getString("rootId", "-1"))) {
		header.setButtonOverflowVisible(true);
		int popupMenuLayout = R.menu.popup_favorite; 

		header.setPopupMenu(popupMenuLayout,
				new CardHeader.OnClickCardHeaderPopupMenuListener() {
					@Override
					public void onMenuItemClick(BaseCard card, MenuItem item) {
						int id = item.getItemId();
						switch (id) {
						//case R.id.add:
						case R.id.edit:
							
							mBookcase.showInputDialog(mFavoriteItem);
//							Config.bkID = mBookItem.mBkID;
//							Intent intentTag = new Intent();
//							intentTag.setClass(getContext(), TagActivity.class);
//							getContext().startActivity(intentTag);

							//delFavorite();

							break;
							
						case R.id.delete:
							mBookcase.showDelDialog(mFavoriteItem);
							
						default:
							break;
						}
					}

				});
		}
		addCardHeader(header);

//		GplayGridThumb thumbnail = new GplayGridThumb(getContext());
//		thumbnail.setExternalUsage(true);
		
		//CardThumbnail thumbnail = new CardThumbnail(getContext());
		/*
		 * if (resourceIdThumbnail > -1)
		 * thumbnail.setDrawableResource(resourceIdThumbnail); else
		*/
		//thumbnail.setDrawableResource(R.drawable.login_reading_gril);
		 
		
		
		
		//Drawable drawable = new BitmapDrawable(Config.book_cover);
		//thumbnail.setDrawableResource(drawable.get);
		//addCardThumbnail(thumbnail);
		
		
		setOnClickListener(new OnCardClickListener() {
			@Override
			public void onClick(Card card, View view) {

				Config.fgID = mFavoriteItem.mFgID;
				Config.my_favorite_title = mFavoriteItem.mTitle;
				// Log.i(LOG_TAG, "bkID: " + Config.bkID);

				Intent intent = new Intent();
				// intent.putExtra("spID", spID);
				intent.setClass(getContext(), BooklistActivity.class);
				getContext().startActivity(intent);

			}
		});
	}
	
	

	private void delFavorite() {
		
		Map<String, Object> mapParam = new HashMap<String, Object>();
		//mapParam.put("bkID", mBookItem.mBkID);

		ReqUtil.send("Bookcase/tag/rmBook/3", mapParam, new COIMCallListener() {
			

			@Override
			public void onSuccess(JSONObject result) {
				Log.i(LOG_TAG, "success: "+result);
				JSONArray jsonBooks  = Assist.getList(result);
				
				if (Assist.getErrCode(result) == 0) {
					Assist.showToast(getContext(), "取消收藏成功!");
				} else {
					Assist.showToast(getContext(), "取消收藏失敗!");
				}

				//mBookcase.showReadingPeople(true);
			}
			
			@Override
			public void onFail(HttpResponse response, Exception exception) {
				Log.i(LOG_TAG, "fail: "+ exception.getLocalizedMessage());
				
			}
		});
		
	}

	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {

		mBookBig = (ImageView) view.findViewById(R.id.book_big); 
		mBookSmall1 = (ImageView) view.findViewById(R.id.book_small1); 
		mBookSmall2 = (ImageView) view.findViewById(R.id.book_small2); 
				
		showReading();
		
		/*
		 * TextView title = (TextView) view
		 * .findViewById(R.id.carddemo_gplay_main_inner_title);
		 */
		// title.setText(totalRecord + " " + totalFavorite + privateOrPublic);

		//view.setVisibility(View.GONE);
//		TextView subtitle = (TextView) view
//				.findViewById(R.id.carddemo_gplay_main_inner_subtitle);

		// subtitle.setText(description);

	}

	
	private void showReading() {
		
//		Map<String, Object> mapParam = new HashMap<String, Object>();
//		mapParam.put("cycle", "i");
//		mapParam.put("favi", "1");
		
		ReqUtil.send(Config.BukrData + "/faviGroup/listBooks/" + mFavoriteItem.mFgID, null, new COIMCallListener() {
			@Override
			public void onSuccess(JSONObject result) {
				Log.i(LOG_TAG, "success: "+result);
				JSONArray jsonBooks  = Assist.getList(result);
			
				
				for(int i = 0; i < jsonBooks.length(); i++)  {
					JSONObject jsonBook;
					
					try {
						jsonBook = (JSONObject) jsonBooks.get(i);

						//String bkID = jsonBook.getString("bkID");
						String iconUrl = "http";
						if (!jsonBook.isNull("icon")) {
							iconUrl = BukrUtlis.getBookIconUrl(jsonBook.getString("icon"));
						}

						
						//String iconUrl = BukrUtlis.getBookIconUrl(jsonBook.getString("icon"));
						//String title = jsonBook.getString("title");
						//String author = jsonBook.getString("author");
						//boolean isFavi = true;  //jsonBook.getInt("isFavi") == 1 ? true : false;
						
//						Log.i(LOG_TAG, "bkID: " + bkID)
						Log.i(LOG_TAG, "iconUrl: " + iconUrl);
//						Log.i(LOG_TAG, "title: " + title);
//						Log.i(LOG_TAG, "author: " + author);
						
						
						
						switch (i) {
						case 0:
							if (iconUrl.equals("http")) {
								mBookBig.setImageResource(R.drawable.image_on_fail);
							} else {
								imageLoader.displayImage(iconUrl, mBookBig, Config.OPTIONS, null);
							}
							break;

						case 1:
							if (iconUrl.equals("http")) {
								mBookSmall1.setImageResource(R.drawable.image_on_fail);
							} else {
								imageLoader.displayImage(iconUrl, mBookSmall1, Config.OPTIONS, null);
							}
							break;

						case 2:
							if (iconUrl.equals("http")) {
								mBookSmall2.setImageResource(R.drawable.image_on_fail);
							} else {
								imageLoader.displayImage(iconUrl, mBookSmall2, Config.OPTIONS, null);
							}
							break;

						default:
							break;
						}
						
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}

				
				if (jsonBooks.length() == 0) {
					mBookBig.setImageResource(R.drawable.image_on_fail);
					mBookSmall1.setImageResource(R.drawable.image_on_fail);
					mBookSmall2.setImageResource(R.drawable.image_on_fail);
				} if (jsonBooks.length() == 1) {
					mBookSmall1.setImageResource(R.drawable.image_on_fail);
					mBookSmall2.setImageResource(R.drawable.image_on_fail);
				} if (jsonBooks.length() == 2) {
					mBookSmall2.setImageResource(R.drawable.image_on_fail);
				}
				
			}
			
			@Override
			public void onFail(HttpResponse response, Exception exception) {
				Log.i(LOG_TAG, "fail: "+ exception.getLocalizedMessage());
				
			}
		});
		
	}

	
	class BookGridCardHeader extends CardHeader {

		public BookGridCardHeader(Context context) {
			this(context, R.layout.inner_base_header);
		}

		public BookGridCardHeader(Context context, int innerLayout) {
			super(context, innerLayout);
		}

		@Override
		public void setupInnerViewElements(ViewGroup parent, View view) {

			if (view != null) {
				TextView textView = (TextView) view
						.findViewById(R.id.card_header_inner_simple_title);

				if (textView != null) {
					textView.setText(mFavoriteItem.mTitle);
				}
			}
		}
	}

//	class GplayGridThumb extends CardThumbnail {
//		private ImageLoader imageLoader = ImageLoader.getInstance();
//
//		public GplayGridThumb(Context context) {
//			super(context);
//		}
//
//		@Override
//		public void setupInnerViewElements(ViewGroup parent, View viewImage) {
//
//			//applyBitmap(viewImage, Config.book_cover);
//			
//			((ImageView) viewImage).setImageBitmap(Config.book_cover);
//			
//			//String url;
//			//url = "";//mBookItem.getIconURI().trim();
//			//imageLoader.displayImage(url, (ImageView) viewImage, Config.OPTIONS, null);
//			
//			
//			// viewImage.getLayoutParams().width = 196;
//			// viewImage.getLayoutParams().height = 196;
//
//		}
//		
////		@Override
////		public boolean applyBitmap(View imageView, Bitmap bitmap) {
////			Bitmap temp = bitmap;
////			if (Config.book_cover != null) {
////				temp = Config.book_cover;
////			}
////			return super.applyBitmap(imageView, temp);
////		}
//	}

	public void setFavoriteItem(FavoriteItem favoriteItem) {
		mFavoriteItem = favoriteItem;
	}
}
