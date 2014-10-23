package co.bukr;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.internal.base.BaseCard;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
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

public class BookListCard extends Card {
	private final static String LOG_TAG = "BookListCard";

	String mainTitle;
	String secondaryTitle;
	String mainHeader;
	int resourceIdThumb;
	private CommentItem mCommentItem;

	private BookCommentActivity mbookCommentActivity;

	private ImageLoader imageLoader = ImageLoader.getInstance();

	public BookListCard(Context context, BookCommentActivity bookCommentActivity) {
		super(context, R.layout.carddemo_cursor_inner_content);
		mbookCommentActivity = bookCommentActivity;
	}

	public BookListCard(Context context, int innerLayout) {
		super(context, innerLayout);
		// init();
	}

	public void init() {
		CardHeader header = new BookGridCardHeader(getContext());
		
		SharedPreferences pref = getContext().getSharedPreferences("bukr", 0);

		if (mCommentItem.mPsnID.equals(pref.getString("psnID", "-1"))) {
		
		header.setButtonOverflowVisible(true);

		header.setPopupMenu(R.menu.popup_comment,
				new CardHeader.OnClickCardHeaderPopupMenuListener() {
					@Override
					public void onMenuItemClick(BaseCard card, MenuItem item) {
						int id = item.getItemId();
						switch (id) {
						case R.id.del:
							delComment();
							//addFavorite();
							break;
						default:
							break;
						}
					}
				});
		}
		
		addCardHeader(header);

//		GplayGridThumb thumbnail = new GplayGridThumb(getContext());
//		thumbnail.setExternalUsage(true);
//
//		addCardThumbnail(thumbnail);

//		setOnLongClickListener(new OnLongCardClickListener() {
//			
//			@Override
//			public boolean onLongClick(Card card, View view) {
//				delComment();
//				return true;
//			}
//
//		});

		
		
//		setOnClickListener(new OnCardClickListener() {
//			@Override
//			public void onClick(Card card, View view) {
//
//				Config.bkID = mBookItem.getBkID();
//				// Log.i(LOG_TAG, "bkID: " + Config.bkID);
//
//				Intent intent = new Intent();
//				// intent.putExtra("spID", spID);
//				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				intent.setClass(getContext(), BookActivity.class);
//				getContext().startActivity(intent);
//
//			}
//		});

	}

	private void delComment() {

		ReqUtil.send(Config.BukrData + "/comment/delete/" + mCommentItem.mUcID , null,
				new COIMCallListener() {


					@Override
					public void onSuccess(JSONObject result) {
						mbookCommentActivity.showComments();
					}
					

					@Override
					public void onFail(HttpResponse response,
							Exception exception) {
						Log.i(LOG_TAG,
								"fail: " + exception.getLocalizedMessage());

					}
				});

	}

	
//	private void addFavorite() {
//		
//		Map<String, Object> mapParam = new HashMap<String, Object>();
//		mapParam.put("bkID", mBookItem.mUcID);
//
//		ReqUtil.send("Bookcase/tag/addBook/3", mapParam, new COIMCallListener() {
//			
//
//			@Override
//			public void onSuccess(JSONObject result) {
//				Log.i(LOG_TAG, "success: "+result);
//				//JSONArray jsonBooks  = Assist.getList(result);
//				
//			}
//			
//			@Override
//			public void onFail(HttpResponse response, Exception exception) {
//				Log.i(LOG_TAG, "fail: "+ exception.getLocalizedMessage());
//				
//			}
//		});
//		
//	}

	
	class BookGridCardHeader extends CardHeader {

		public BookGridCardHeader(Context context) {
			this(context, R.layout.book_list_card_header);
		}

		public BookGridCardHeader(Context context, int innerLayout) {
			super(context, innerLayout);
		}

		@Override
		public void setupInnerViewElements(ViewGroup parent, View view) {

			if (view != null) {
				ImageView icon = (ImageView) view.findViewById(R.id.icon);
				imageLoader.displayImage(
						mCommentItem.mIconURI,
						icon, Config.OPTIONS, null);

				
				
				TextView textView = (TextView) view
						.findViewById(R.id.card_header_inner_simple_title);

				if (textView != null) {
					textView.setText(mCommentItem.mDspName);
				}

				TextView cardHeaderDate = (TextView) view
						.findViewById(R.id.card_header_date);
				cardHeaderDate.setText(mCommentItem.mMdTime.substring(0, 10));

			
			}
		}
	}

	class GplayGridThumb extends CardThumbnail {
		private ImageLoader imageLoader = ImageLoader.getInstance();

		public GplayGridThumb(Context context) {
			super(context);
		}

		@Override
		public void setupInnerViewElements(ViewGroup parent, View viewImage) {

//			String url = null;
//			url = mBookItem.getIconURI().trim();
//			imageLoader.displayImage(url, (ImageView) viewImage,
//					Config.OPTIONS, null);

			// viewImage.getLayoutParams().width = 196;
			// viewImage.getLayoutParams().height = 196;

		}
	}

	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {
		// Retrieve elements
		TextView mTitleTextView = (TextView) parent
				.findViewById(R.id.carddemo_cursor_main_inner_title);
		TextView mSecondaryTitleTextView = (TextView) parent
				.findViewById(R.id.carddemo_cursor_main_inner_subtitle);

		if (mTitleTextView != null)
			mTitleTextView.setText(mCommentItem.mBody);

//		if (mSecondaryTitleTextView != null)
//			mSecondaryTitleTextView.setText(mCommentItem.getTitle());

	}

	public void setCommentItem(CommentItem commentItem) {
		mCommentItem = commentItem;
	}
}
