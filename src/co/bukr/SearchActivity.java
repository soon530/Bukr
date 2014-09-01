package co.bukr;

import android.app.Activity;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

public class SearchActivity extends Activity implements OnQueryTextListener {

	private SearchView mSearchView;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.search, menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);
		mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		mSearchView.setOnQueryTextListener(this);
		mSearchView.setIconifiedByDefault(false);

		customizeSearchIcon();

		// setupSearchView(searchItem);
		return super.onCreateOptionsMenu(menu);
	}

	private void customizeSearchIcon() {
		int searchIconId = mSearchView.getContext().getResources()
				.getIdentifier("android:id/search_button", null, null);
		ImageView searchIcon = (ImageView) mSearchView
				.findViewById(searchIconId);
		searchIcon.setImageResource(R.drawable.search);
	}

	@Override
	public boolean onQueryTextChange(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
