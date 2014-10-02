package co.bukr;

import java.util.HashMap;
import java.util.Map;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.internal.base.BaseCard;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
	private BookItem mBookItem;
	private BookcaseFragment mBookcase;

	public BookcaseGridCard(Context context, BookcaseFragment bookcaseFragment) {
		super(context, R.layout.book_card_view_inner_content);
		mBookcase = bookcaseFragment;
	}

	public BookcaseGridCard(Context context, int innerLayout) {
		super(context, innerLayout);
		// init();
	}

	public void init() {

		CardHeader header = new BookGridCardHeader(getContext());
		header.setButtonOverflowVisible(true);
		// header.setTitle(mBookItem.mTitle);
		int popupMenuLayout = R.menu.popup_add; 
		if (mBookItem.mIsFavi) {
			popupMenuLayout = R.menu.popup_edit;
		}

		header.setPopupMenu(popupMenuLayout,
				new CardHeader.OnClickCardHeaderPopupMenuListener() {
					@Override
					public void onMenuItemClick(BaseCard card, MenuItem item) {
						int id = item.getItemId();
						switch (id) {
						//case R.id.add:
						case R.id.edit:

							//delFavorite();

							break;
						default:
							break;
						}
					}

				});

		addCardHeader(header);

		GplayGridThumb thumbnail = new GplayGridThumb(getContext());
		thumbnail.setExternalUsage(true);
		/*
		 * if (resourceIdThumbnail > -1)
		 * thumbnail.setDrawableResource(resourceIdThumbnail); else
		 * thumbnail.setDrawableResource(R.drawable.ic_launcher);
		 */
		addCardThumbnail(thumbnail);

		setOnClickListener(new OnCardClickListener() {
			@Override
			public void onClick(Card card, View view) {

				Config.bkID = mBookItem.getBkID();
				// Log.i(LOG_TAG, "bkID: " + Config.bkID);

				Intent intent = new Intent();
				// intent.putExtra("spID", spID);
				intent.setClass(getContext(), BookActivity.class);
				getContext().startActivity(intent);

			}
		});
	}

	private void delFavorite() {
		
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("bkID", mBookItem.mBkID);

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

				mBookcase.showReadingPeople(true);
			}
			
			@Override
			public void onFail(HttpResponse response, Exception exception) {
				Log.i(LOG_TAG, "fail: "+ exception.getLocalizedMessage());
				
			}
		});
		
	}

	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {

		/*
		 * TextView title = (TextView) view
		 * .findViewById(R.id.carddemo_gplay_main_inner_title);
		 */
		// title.setText(totalRecord + " " + totalFavorite + privateOrPublic);

		TextView subtitle = (TextView) view
				.findViewById(R.id.carddemo_gplay_main_inner_subtitle);

		// subtitle.setText(description);

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
					textView.setText(mBookItem.mTitle);
				}
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

			String url;
			url = mBookItem.getIconURI().trim();
			imageLoader.displayImage(url, (ImageView) viewImage,
					Config.OPTIONS, null);

			// viewImage.getLayoutParams().width = 196;
			// viewImage.getLayoutParams().height = 196;

		}
	}

	public void setBookItem(BookItem bookItem) {
		mBookItem = bookItem;
	}
}
