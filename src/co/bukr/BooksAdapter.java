package co.bukr;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class BooksAdapter extends ArrayAdapter<BookItem> {
	int mLayoutResourceId;
	Context mContext;
	ArrayList<BookItem> mBooks = new ArrayList<BookItem>();
	private ImageLoader imageLoader = ImageLoader.getInstance();

	public BooksAdapter(Context context, int resource, ArrayList<BookItem> books) {
		super(context, resource, books);
		mLayoutResourceId = resource;
		mContext = context;
		mBooks = books;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		RecordHolder holder = null;
		if (row == null) {
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			row = inflater.inflate(mLayoutResourceId, parent, false);
			holder = new RecordHolder();
			holder.txtTitle = (TextView) row.findViewById(R.id.item_text);
			holder.imageItem = (ImageView) row.findViewById(R.id.item_image);
			row.setTag(holder);
		} else {
			holder = (RecordHolder) row.getTag();
		}
		BookItem item = mBooks.get(position);
		holder.txtTitle.setText(item.getTitle());
		
		
		imageLoader.displayImage(item.getIconURI().trim(), holder.imageItem, Config.OPTIONS, null);

		//holder.imageItem.setImageResource(resId);(item.getImage());
		return row;
	}

	static class RecordHolder {
		ImageView imageItem;
		TextView txtTitle;
	}

}
