package co.bukr;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import com.coimotion.csdk.common.COIMCallListener;
import com.coimotion.csdk.util.Assist;
import com.coimotion.csdk.util.ReqUtil;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class AddCommentActivity extends Activity {


	protected static final String LOG_TAG = "AddCommentActivity";
	private EditText mNote;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("撰寫書評");
		setContentView(R.layout.activity_add_comment);
		mNote = (EditText) findViewById(R.id.note);
		
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
		case android.R.id.home:
			finish();
			break;
		case R.id.add:
			if (!mNote.getText().toString().trim().equals(""))
				addComment();
			finish();
			break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}
	
	private void addComment() {
		
		Map<String, Object> mapParam = new HashMap<String, Object>();
		//mapParam.put("title", "title");
		mapParam.put("body", mNote.getText());
		//mapParam.put("kw", keyWord);

		ReqUtil.send(Config.BukrData + "/comment/create/" + Config.bkID, mapParam,
				new COIMCallListener() {


					@Override
					public void onSuccess(JSONObject result) {
						//mBody.setText("");
						Log.i(LOG_TAG, "success: " + result);

						//JSONArray jsonBooks  = Assist.getList(result);
							
					}
					

					@Override
					public void onFail(HttpResponse response,
							Exception exception) {
						Log.i(LOG_TAG,
								"fail: " + exception.getLocalizedMessage());

					}
				});

	}


}
