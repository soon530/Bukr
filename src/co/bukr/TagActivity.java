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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.coimotion.csdk.common.COIMCallListener;
import com.coimotion.csdk.util.Assist;
import com.coimotion.csdk.util.ReqUtil;

public class TagActivity extends Activity implements OnQueryTextListener {
	private final static String LOG_TAG = "TagActivity";
//    private SearchView mSearchView;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
	protected ArrayList<String> mTags = new ArrayList<String>();
	protected ArrayList<String> mFgID = new ArrayList<String>();
	private TextView mBookTagsShow;
	private String mBookTags;
	private ImageView mAddToBooklist;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setIcon(
				   new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		getActionBar().setDisplayHomeAsUpEnabled(false);
		setContentView(R.layout.searchview_filter);
		
		
//        mSearchView = (SearchView) findViewById(R.id.search_view);
        mListView = (ListView) findViewById(R.id.list_view);
        mAddToBooklist = (ImageView) findViewById(R.id.add_to_booklist);
//        mBookTagsShow = (TextView) findViewById(R.id.book_tags);
//        setupSearchView();
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Log.i(LOG_TAG, "listview item");
				
				CheckBox check = (CheckBox)view.findViewById(R.id.checkBox1);
				
				//check.setChecked(!check.isChecked());

				String fgID = mFgID.get(position);
				if (check.isChecked()) {
					delBook(fgID);
				} else {
					addBook(fgID);					
				}
				getBookTags();
				
//				String fgID = mFgID.get(position);
//				Log.i(LOG_TAG, "mBookTags:" + mBookTags + ", mTags.get():" + mTags.get(position) + ", contains:" + mBookTags.contains(mTags.get(position)));
//				//如果書本中，有這個分類，就將這個分類刪除
//				if (mBookTags.contains(mTags.get(position))) {
//					delBook(fgID);
//				} else { //如果書本中，沒有這個分類，就將這個分類增加
//					addBook(fgID);
//				}
//				getBookTags();
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
        
        
        mAddToBooklist.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showInputDialog();
			}
		});
		
		showTags();
	}
	
	protected void showInputDialog() {

		// get prompts.xml view
		LayoutInflater layoutInflater = LayoutInflater.from(TagActivity.this);
		View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				TagActivity.this);
		alertDialogBuilder.setView(promptView);

		final EditText editText = (EditText) promptView
				.findViewById(R.id.edittext);
		// setup a dialog window
		alertDialogBuilder
				.setCancelable(false)
				.setTitle("請輸入書單名稱")
				.setPositiveButton("確認", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//resultText.setText("Hello, " + editText.getText());
						addTag(editText.getText().toString());
						showTags();
					}
				})
				.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		// create an alert dialog
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();

	}

    private void getBookTags() {
    	mBookTags = "";
		ReqUtil.send(Config.BukrData+"/faviGroup/contains/" + Config.bkID, null, new COIMCallListener() {


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
				//該有的tag後，做選取動作
				setBookListChecked();
				//mBookTagsShow.setText(mBookTags);
			}
			
			public void onFail(HttpResponse response, Exception exception) {
				Log.i(LOG_TAG, "fail: "+ exception.getLocalizedMessage());
				
			}
		});

		
	}

	protected void setBookListChecked() {
				
		for (int i=0; i < mListView.getCount(); i++) {

			View view = mListView.getChildAt(i);
			if (view != null) {
				CheckBox chk = (CheckBox) view.findViewById(R.id.checkBox1);
		
			
				if (mBookTags.contains(chk.getText())) {
					chk.setChecked(true);
				} else {
					chk.setChecked(false);
				}
			}
		}
	
	}

	protected void delBook(String fgID) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("bkID", Config.bkID);

		ReqUtil.send(Config.BukrData+"/faviGroup/rmBook/" + fgID, mapParam, new COIMCallListener() {


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

		ReqUtil.send(Config.BukrData+"/faviGroup/addBook/" + fgID, mapParam, new COIMCallListener() {


			@Override
			public void onSuccess(JSONObject result) {
				Log.i(LOG_TAG, "success: "+result);
				
			}
			
			public void onFail(HttpResponse response, Exception exception) {
				Log.i(LOG_TAG, "fail: "+ exception.getLocalizedMessage());
				
			}
		});
    	
	}

//	private void setupSearchView() {
//        mSearchView.setIconifiedByDefault(false);
//        mSearchView.setOnQueryTextListener(this);
//        mSearchView.setSubmitButtonEnabled(false);
//        mSearchView.setQueryHint("搜尋tags");
//    }

	public void showTags() {
		//mBookCards.clear();

		//Map<String, Object> mapParam = new HashMap<String, Object>();
		//mapParam.put("title", "我的書櫃");
		//mapParam.put("descTx", "用來收藏自己的書");
		//mapParam.put("share", "1");

		ReqUtil.send(Config.BukrData+"/faviGroup/list", null, new COIMCallListener() {


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
				//Collections.reverse(mTags);
				//Collections.reverse(mFgID);
				mAdapter = new ArrayAdapter<String>(TagActivity.this, R.layout.row_booklist, R.id.checkBox1 , mTags);
		        mListView.setAdapter(mAdapter);
		        mListView.setTextFilterEnabled(true);
		        //打勾勾
		        getBookTags();
			}
			
			
			public void onFail(HttpResponse response, Exception exception) {
				Log.i(LOG_TAG, "fail: "+ exception.getLocalizedMessage());
				
			}
		});

	}
	
	private void delTag(String fgID) {
		
		ReqUtil.send(Config.BukrData+"/faviGroup/remove/" + fgID, null, new COIMCallListener() {


			@Override
			public void onSuccess(JSONObject result) {
				Log.i(LOG_TAG, "success: "+result);
				
				showTags();
				//getBookTags();
			}
			
			
			public void onFail(HttpResponse response, Exception exception) {
				Log.i(LOG_TAG, "fail: "+ exception.getLocalizedMessage());
				
			}
		});

	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		//addTag(query);
		//mSearchView.setQuery("", false);
		return false;
	}

	private void addTag(String query) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("title", query);
		//mapParam.put("descTx", "用來收藏自己的書");
		mapParam.put("share", "0");

		
		ReqUtil.send(Config.BukrData+"/faviGroup/create", mapParam, new COIMCallListener() {


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
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.add, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch (id) {
		case R.id.add:
			finish();
			break;
			
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	

}
