package co.bukr;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.coimotion.csdk.common.COIMCallListener;
import com.coimotion.csdk.common.COIMException;
import com.coimotion.csdk.util.Assist;
import com.coimotion.csdk.util.ReqUtil;
import com.coimotion.csdk.util.sws;

public class LoginActivity extends Activity implements OnClickListener {
	private static final String LOG_TAG = "loginActivity";
	private final static String checkTokenURL = "core/user/profile";

	private ProgressDialog pDialog;

	// Login or Singup 
	private ImageButton mFB;
	private ImageButton mLogin;
	private TextView mSignup;
	private TextView mGo;

	//Login Dialog
	private Dialog mLoginDialog;
	private EditText mLoginDialogName;
	private EditText mLoginDialogPassword;
	private ImageView mLoginDialogLogin;
	
	//Signup Dialog
	private Dialog mSignupDialog;
	private EditText mSignupDialogName;
	private EditText mSignupDialogPassword;
	private ImageView mSignupDialogLogin;
	private EditText mSignupDialogAgain;
	private EditText mSignupDialogEmail;
	
	SharedPreferences pref;
	private String mFgID;

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		pref = getApplication().getSharedPreferences("bukr", 0);
		if (pref.getBoolean("login", false) == true) {
			goToHome();
		}
		
		setContentView(R.layout.activity_login2);

		mLogin = (ImageButton) findViewById(R.id.login_login);
		mLogin.setOnClickListener(this);
		mLoginDialog = getLoginDialog();

		mSignup = (TextView) findViewById(R.id.login_singup);
		mSignup.setOnClickListener(this);
		mSignupDialog = getSignupDialog();

		mGo = (TextView) findViewById(R.id.login_go);
		mGo.setOnClickListener(this);

		mFB = (ImageButton) findViewById(R.id.login_facebook);
		mFB.setOnClickListener(this);
	}
	
	private Dialog getLoginDialog() {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.activity_login_dialog);

		mLoginDialogName = (EditText) dialog.findViewById(R.id.name);
		mLoginDialogPassword = (EditText) dialog.findViewById(R.id.password);
		mLoginDialogLogin = (ImageView) dialog.findViewById(R.id.login);
		mLoginDialogLogin.setOnClickListener(this);

		return dialog;
	}
	
	private Dialog getSignupDialog() {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.activity_singup_dialog);

		mSignupDialogName = (EditText) dialog.findViewById(R.id.name);
		mSignupDialogEmail = (EditText) dialog.findViewById(R.id.email);
		mSignupDialogPassword = (EditText) dialog.findViewById(R.id.password);
		mSignupDialogAgain = (EditText) dialog.findViewById(R.id.again);
		mSignupDialogLogin = (ImageView) dialog.findViewById(R.id.signup);
		mSignupDialogLogin.setOnClickListener(this);

		return dialog;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.login_login:
			mLoginDialog.show();
			break;
		case R.id.login_singup:
			mSignupDialog.show();
			break;
		case R.id.login_facebook:
			loginBukrFB();
			break;
		case R.id.login:
			loginBukr();
			break;
		case R.id.signup:
			signupBukr();
			break;
		case R.id.login_go:
			goToHome();
			break;

		default:
			break;
		}

	}

	private void loginBukrFB() {
		sws.loginFB(LoginActivity.this, new COIMCallListener() {

			@Override
			public void onSuccess(JSONObject result) {
				Log.i(LOG_TAG, "loginBukrFB() result: " + result);
				pref.edit().putBoolean("login", true).commit();
				showUserProfile();
				
				CreatRootId();
				//goToHome();
			}

			@Override
			public void onFail(HttpResponse arg0, Exception arg1) {
				Log.i(LOG_TAG, "err: " + arg1.getLocalizedMessage());

			}
		});

	}
	
	private void showUserProfile() {
		
		ReqUtil.send("core/user/profile", null, new COIMCallListener() {
			@Override
			public void onSuccess(JSONObject result) {
				Log.i(LOG_TAG, "showUserProfile() success: "+result);
				
				
				JSONObject jsonBook = Assist.getValue(result);
				Log.i(LOG_TAG, "success: " + jsonBook);
				try {
					
					if (!jsonBook.isNull("psnID")) {
						pref.edit().putString("psnID", jsonBook.getString("psnID")).commit();
					} else {
						pref.edit().putString("psnID", "-1").commit();
					}
					
					if (!jsonBook.isNull("dspName")) {
							pref.edit().putString("dspName", jsonBook.getString("dspName")).commit();
					} else {
						pref.edit().putString("dspName", "匿名").commit();
					}
					
					if (!jsonBook.isNull("iconURI")) {
						pref.edit().putString("iconURI", jsonBook.getString("iconURI")).commit();
					} else {
						pref.edit().putString("iconURI", "").commit();
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			
			@Override
			public void onFail(HttpResponse response, Exception exception) {
				Log.i(LOG_TAG, "fail: "+ exception.getLocalizedMessage());
				
			}
		});
		
	}


	private void loginBukr() {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("accName", mLoginDialogName.getText().toString());
		mapParam.put("passwd", mLoginDialogPassword.getText().toString());

		
		pDialog = ProgressDialog.show(LoginActivity.this, "", "登入中…", true);

		ReqUtil.login("core/user/login", mapParam, new COIMCallListener() {

			@Override
			public void onSuccess(JSONObject result) {
				pDialog.dismiss();

				if (Assist.getErrCode(result) == 0) {
					Log.i(LOG_TAG, "success\n" + result);
					mLoginDialog.dismiss();
					pref.edit().putBoolean("login", true).commit();
					showUserProfile();
					CreatRootId();
					//goToHome();

				} else {
					Assist.showAlert(LoginActivity.this, Assist.getMessage(result));
				}

			}

			@Override
			public void onFail(HttpResponse response, Exception exception) {
				pDialog.dismiss();
				Log.i(LOG_TAG, "err: " + exception.getLocalizedMessage());
				Assist.showAlert(LoginActivity.this, exception.getLocalizedMessage());
			}
		});
	}

	private void signupBukr() {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("accName", mSignupDialogName.getText().toString());
		mapParam.put("email", mSignupDialogEmail.getText().toString());
		mapParam.put("passwd", mSignupDialogPassword.getText().toString());
		mapParam.put("passwd2", mSignupDialogAgain.getText().toString());
		mapParam.put("dspName", mSignupDialogName.getText().toString());
		
		pDialog = ProgressDialog.show(LoginActivity.this, "", "註冊中…", true);

		ReqUtil.registerUser(mapParam, new COIMCallListener() {

			@Override
			public void onSuccess(JSONObject result) {
				pDialog.dismiss();

				if (Assist.getErrCode(result) == 0) {
					Log.i(LOG_TAG, "success\n" + result);
					mSignupDialog.dismiss();
					pref.edit().putBoolean("login", true).commit();
					showUserProfile();
					CreatRootId();
					
					//goToHome();

				} else {
					Assist.showAlert(LoginActivity.this, Assist.getMessage(result));
				}

			}

			@Override
			public void onFail(HttpResponse response, Exception exception) {
				pDialog.dismiss();
				Log.i(LOG_TAG, "err: " + exception.getLocalizedMessage());
				Assist.showAlert(LoginActivity.this, exception.getLocalizedMessage());
			}
		});

	}

	private void goToHome() {
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setClass(LoginActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.main, menu);
//		return super.onCreateOptionsMenu(menu);
//	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		int id = item.getItemId();
//
//		switch (id) {
//		case R.id.action_logout:
//			logoutBukr();
//			break;
//
//		default:
//			break;
//		}
//
//		// return super.onOptionsItemSelected(item);
//		return true;
//	}

	

	private void CreatRootId() {
		ReqUtil.send(Config.BukrData+"/faviGroup/list", null, new COIMCallListener() {

			@Override
			public void onSuccess(JSONObject result) {
				Log.i(LOG_TAG, "success: "+result);
				
				mFgID = "-1";
				
				JSONArray jsonBooks  = Assist.getList(result);
				for(int i = 0; i < jsonBooks.length(); i++)  {
					JSONObject jsonBook;
					
					try {
						jsonBook = (JSONObject) jsonBooks.get(i);
						
						mFgID = jsonBook.getString("fgID");
						Log.i(LOG_TAG, "fgID: " + mFgID);
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}
				
				isCreatRootIDOrNot();
				
			}
			
			
			public void onFail(HttpResponse response, Exception exception) {
				Log.i(LOG_TAG, "fail: "+ exception.getLocalizedMessage());
				
			}
		});

	}


	protected void isCreatRootIDOrNot() {

		if (mFgID.equals("-1")) { //沒有rootID，所以要自動建一個
			addFirstTag();
			
		}else { // 有FgID，所以存起來就好
			pref.edit().putString("rootId", mFgID).commit();
			//Config.root = mFgID;
		}
		
		//Log.i(LOG_TAG, "fgID root: " + Config.root);

		
		goToHome();
	}


	private void addFirstTag() {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("title", "所有的藏書");
		//mapParam.put("descTx", "用來收藏自己的書");
		mapParam.put("share", "0");

		
		ReqUtil.send(Config.BukrData+"/faviGroup/create", mapParam, new COIMCallListener() {


			@Override
			public void onSuccess(JSONObject result) {
				Log.i(LOG_TAG, "success: "+result);
				JSONObject jsonBook = Assist.getValue(result);

				try {
					pref.edit().putString("rootId", jsonBook.getString("id")).commit();
					//Config.root = jsonBook.getString("id");

					//goToHome();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			public void onFail(HttpResponse response, Exception exception) {
				Log.i(LOG_TAG, "fail: "+ exception.getLocalizedMessage());
				
			}
		});

	}

	
}

