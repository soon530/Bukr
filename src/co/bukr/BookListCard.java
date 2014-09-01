package co.bukr;

import com.nostra13.universalimageloader.core.ImageLoader;

import co.bukr.BookGridCard.BookGridCardHeader;
import co.bukr.BookGridCard.GplayGridThumb;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class BookListCard extends Card {

	String mainTitle;
	String secondaryTitle;
	String mainHeader;
	int resourceIdThumb;
	private BookItem mBookItem;

	public BookListCard(Context context) {
		super(context, R.layout.carddemo_cursor_inner_content);
	}

	public BookListCard(Context context, int innerLayout) {
		super(context, innerLayout);
		// init();
	}

	public void init() {
		CardHeader header = new BookGridCardHeader(getContext());
		header.setButtonOverflowVisible(true);

		header.setPopupMenu(R.menu.popup_edit,
				new CardHeader.OnClickCardHeaderPopupMenuListener() {
					@Override
					public void onMenuItemClick(BaseCard card, MenuItem item) {
						int id = item.getItemId();
						switch (id) {
						case R.id.card_edit:
							break;
						default:
							break;
						}
					}
				});

		addCardHeader(header);

		GplayGridThumb thumbnail = new GplayGridThumb(getContext());
		thumbnail.setExternalUsage(true);

		addCardThumbnail(thumbnail);

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

			String url = null;
			url = mBookItem.getIconURI().trim();
			imageLoader.displayImage(url, (ImageView) viewImage,
					Config.OPTIONS, null);

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
			mTitleTextView.setText(mBookItem.getBkID());

		if (mSecondaryTitleTextView != null)
			mSecondaryTitleTextView.setText(mBookItem.getTitle());

	}

	public void setBookItem(BookItem bookItem) {
		mBookItem = bookItem;
	}
}
