package co.bukr;

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

import com.nostra13.universalimageloader.core.ImageLoader;

public class BookGridCard extends Card {

	private BookItem mBookItem;

	public BookGridCard(Context context) {
		super(context, R.layout.book_card_view_inner_content);
	}

	public BookGridCard(Context context, int innerLayout) {
		super(context, innerLayout);
		// init();
	}

	public void init() {

		CardHeader header = new BookGridCardHeader(getContext());
		header.setButtonOverflowVisible(true);
		//header.setTitle(mBookItem.mTitle);

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
		/*
		 * if (resourceIdThumbnail > -1)
		 * thumbnail.setDrawableResource(resourceIdThumbnail); else
		 * thumbnail.setDrawableResource(R.drawable.ic_launcher);
		 */
		addCardThumbnail(thumbnail);

		setOnClickListener(new OnCardClickListener() {
			@Override
			public void onClick(Card card, View view) {
				/*
				 * Intent intent = new Intent(getContext(),
				 * BabyRecordActivity.class);
				 * intent.putExtra(Config.BABY_OBJECT_ID,
				 * mBabyDiary.getObjectId());
				 * intent.putExtra(Config.TOTAL_RECORD,
				 * mBabyDiary.getTotalRecord());
				 * getContext().startActivity(intent);
				 */}
		});
	}

	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {

		TextView title = (TextView) view
				.findViewById(R.id.carddemo_gplay_main_inner_title);

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
