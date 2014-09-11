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
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.searchview_filter);
		
		
        mSearchView = (SearchView) findViewById(R.id.search_view);
        mListView = (ListView) findViewById(R.id.list_view);
        setupSearchView();

/*        mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView textView = (TextView) view.findViewById(android.R.id.text1);

		        textView.setTextColor(Color.RED);
				
			}
		});
*/		
		
		try {
			ReqUtil.initSDK(getApplication());
		} catch (COIMException e) {
		} catch (Exception e) {
		}

		showTags();
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
						mTags.add(title);
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}
				Collections.reverse(mTags);
				mAdapter = new ArrayAdapter<String>(TagActivity.this, android.R.layout.simple_list_item_1, mTags);
		        mListView.setAdapter(mAdapter);
		        mListView.setTextFilterEnabled(true);
				
				
			}
			
			
			public void onFail(HttpResponse response, Exception exception) {
				Log.i(LOG_TAG, "fail: "+ exception.getLocalizedMessage());
				
			}
		});

	}
	
	private void delTag() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		addTag(query);
		
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
				
//				JSONArray jsonBooks  = Assist.getList(result);
//				
//				for(int i = 0; i < jsonBooks.length(); i++)  {
//					JSONObject jsonBook;
//					
//					try {
//						jsonBook = (JSONObject) jsonBooks.get(i);
//						Log.i(LOG_TAG, "title: " + jsonBook.getString("title"));
//						
//						String title = jsonBook.getString("title");
//						mTags.add(title);
//						
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}
//					
//				}
//				
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



	

}
