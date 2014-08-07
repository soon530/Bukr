package co.bukr;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerAdapter extends ArrayAdapter<DrawerItem> {
	Context context;
	List<DrawerItem> drawerItemList;
	int layoutResID;

	public DrawerAdapter(Context context, int layoutResourceID,
			List<DrawerItem> listItems) {
		super(context, layoutResourceID, listItems);
		this.context = context;
		this.drawerItemList = listItems;
		this.layoutResID = layoutResourceID;

	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		DrawerItemHolder drawerHolder;
		View view = convertView;

		if (view == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			drawerHolder = new DrawerItemHolder();

			view = inflater.inflate(layoutResID, parent, false);
			drawerHolder.ItemName = (TextView) view
					.findViewById(R.id.drawer_itemName);
			drawerHolder.icon = (ImageView) view.findViewById(R.id.drawer_icon);
			drawerHolder.iconColor = (ImageView) view.findViewById(R.id.drawer_icon_color);
			
			view.setTag(drawerHolder);

		} else {
			drawerHolder = (DrawerItemHolder) view.getTag();

		}

		DrawerItem dItem = (DrawerItem) this.drawerItemList.get(position);

		drawerHolder.icon.setImageDrawable(view.getResources().getDrawable(
				dItem.getImgResID()));
		drawerHolder.iconColor.setImageDrawable(view.getResources().getDrawable(dItem.getImgResIDColor()));
		drawerHolder.ItemName.setText(dItem.getItemName());
		drawerHolder.ItemName.setAlpha(0.56f);
		drawerHolder.iconColor.setVisibility(View.GONE);
		
		// don't count header view position
		// if (position == 0) {
		// drawerHolder.icon.setVisibility(View.GONE);
		// drawerHolder.iconColor.setVisibility(View.VISIBLE);
		// drawerHolder.ItemName.setTextColor(getContext().getResources().getColor(R.color.Bukr));
		// }

		if (position == 4 || position == 5)
			drawerHolder.icon.setVisibility(View.GONE);
		
		
		return view;
	}

	public static class DrawerItemHolder {
		TextView ItemName;
		ImageView icon;
		ImageView iconColor;
	}
}
