package co.bukr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.coimotion.csdk.common.COIMCallListener;
import com.coimotion.csdk.common.COIMException;
import com.coimotion.csdk.util.Assist;
import com.coimotion.csdk.util.ReqUtil;

public class TagActivity extends Activity implements OnQueryTextListener {
	private final static String LOG_TAG = "TagActivity";
    private SearchView mSearchView;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
	protected ArrayList<String> mTags = new ArrayList<String>();
	protected ArrayList<String> mFgID = new ArrayList<String>();
	private TextView mBookTagsShow;
	private String mBookTags;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.searchview_filter);
		
		
        mSearchView = (SearchView) findViewById(R.id.search_view);
        mListView = (ListView) findViewById(R.id.list_view);
        mBookTagsShow = (TextView) findViewById(R.id.book_tags);
        setupSearchView();

        mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				String fgID = mFgID.get(position);
				Log.i(LOG_TAG, "mBookTags:" + mBookTags + ", mTags.get():" + mTags.get(position) + ", contains:" + mBookTags.contains(mTags.get(position)));
				//如果書本中，有這個分類，就將這個分類刪除
				if (mBookTags.contains(mTags.get(position))) {
					delBook(fgID);
				} else { //如果書本中，沒有這個分類，就將這個分類增加
					addBook(fgID);
				}
				getBookTags();
			}
		});
        
        mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				delTag(mFgID.get(position));
			
				// true		不會再call item click
				// false	會再去call item click
				return true;
			}
		});
		
		try {
			ReqUtil.initSDK(getApplication());
		} catch (COIMException e) {
		} catch (Exception e) {
		}

		getBookTags();
		showTags();
	}

    private void getBookTags() {
    	mBookTags = "";
		ReqUtil.send("Bookcase/tag/contains/" + Config.bkID, null, new COIMCallListener() {


			@Override
			public void onSuccess(JSONObject result) {
				Log.i(LOG_TAG, "success: "+result);
				JSONArray jsonBooks  = Assist.getList(result);
				for(int i = 0; i < jsonBooks.length(); i++)  {
					JSONObject jsonBook;
					
					try {
						jsonBook = (JSONObject) jsonBooks.get(i);
						Log.i(LOG_TAG, "title: " + jsonBook.getString("title"));
						
						String title = jsonBook.getString("title");
						mBookTags = mBookTags + " " + title;
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}

				mBookTagsShow.setText(mBookTags);
			}
			
			
			public void onFail(HttpResponse response, Exception exception) {
				Log.i(LOG_TAG, "fail: "+ exception.getLocalizedMessage());
				
			}
		});

		
	}


	protected void delBook(String fgID) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("bkID", Config.bkID);

		ReqUtil.send("Bookcase/tag/rmBook/" + fgID, mapParam, new COIMCallListener() {


			@Override
			public void onSuccess(JSONObject result) {
				Log.i(LOG_TAG, "success: "+result);
				
			}
			
			public void onFail(HttpResponse response, Exception exception) {
				Log.i(LOG_TAG, "fail: "+ exception.getLocalizedMessage());
				
			}
		});
	
	}

	protected void addBook(String fgID) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("bkID", Config.bkID);

		ReqUtil.send("Bookcase/tag/addBook/" + fgID, mapParam, new COIMCallListener() {


			@Override
			public void onSuccess(JSONObject result) {
				Log.i(LOG_TAG, "success: "+result);
				
			}
			
			public void onFail(HttpResponse response, Exception exception) {
				Log.i(LOG_TAG, "fail: "+ exception.getLocalizedMessage());
				
			}
		});
    	
	}

	private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(false);
        mSearchView.setQueryHint("搜尋tags");
    }

	public void showTags() {
		//mBookCards.clear();

		//Map<String, Object> mapParam = new HashMap<String, Object>();
		//mapParam.put("title", "我的書櫃");
		//mapParam.put("descTx", "用來收藏自己的書");
		//mapParam.put("share", "1");

		ReqUtil.send("Bookcase/tag/list", null, new COIMCallListener() {


			@Override
			public void onSuccess(JSONObject result) {
				Log.i(LOG_TAG, "success: "+result);
				mTags.clear();
				JSONArray jsonBooks  = Assist.getList(result);
				for(int i = 0; i < jsonBooks.length(); i++)  {
					JSONObject jsonBook;
					
					try {
						jsonBook = (JSONObject) jsonBooks.get(i);
						Log.i(LOG_TAG, "title: " + jsonBook.getString("title"));
						
						String title = jsonBook.getString("title");
						String fgID = jsonBook.getString("fgID");
						mTags.add(title);
						mFgID.add(fgID);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}
				Collections.reverse(mTags);
				Collections.reverse(mFgID);
				mAdapter = new ArrayAdapter<String>(TagActivity.this, android.R.layout.simple_list_item_1, mTags);
		        mListView.setAdapter(mAdapter);
		        mListView.setTextFilterEnabled(true);
				
			}
			
			
			public void onFail(HttpResponse response, Exception exception) {
				Log.i(LOG_TAG, "fail: "+ exception.getLocalizedMessage());
				
			}
		});

	}
	
	private void delTag(String fgID) {
		
		ReqUtil.send("Bookcase/tag/remove/" + fgID, null, new COIMCallListener() {


			@Override
			public void onSuccess(JSONObject result) {
				Log.i(LOG_TAG, "success: "+result);
				
				showTags();
				getBookTags();
			}
			
			
			public void onFail(HttpResponse response, Exception exception) {
				Log.i(LOG_TAG, "fail: "+ exception.getLocalizedMessage());
				
			}
		});

	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		addTag(query);
		mSearchView.setQuery("", false);
		return false;
	}

	private void addTag(String query) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("title", query);
		//mapParam.put("descTx", "用來收藏自己的書");
		mapParam.put("share", "0");

		
		ReqUtil.send("Bookcase/tag/create", mapParam, new COIMCallListener() {


			@Override
			public void onSuccess(JSONObject result) {
				Log.i(LOG_TAG, "success: "+result);
				
				mListView.clearTextFilter();
				showTags();
				
			}
			
			
			public void onFail(HttpResponse response, Exception exception) {
				Log.i(LOG_TAG, "fail: "+ exception.getLocalizedMessage());
				
			}
		});

	}

	@Override
	public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            mListView.clearTextFilter();
        } else {
            mListView.setFilterText(newText.toString());
        }
        return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch (id) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	

}
