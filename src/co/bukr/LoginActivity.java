package co.bukr;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
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

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		getApplication().getSharedPreferences("artMania", 0).edit()
				.putBoolean("closeApp", true).commit();
	}





	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
				Log.i(LOG_TAG, "result: " + result);
				goToHome();
			}

			@Override
			public void onFail(HttpResponse arg0, Exception arg1) {
				Log.i(LOG_TAG, "err: " + arg1.getLocalizedMessage());

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
					goToHome();

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

		
		pDialog = ProgressDialog.show(LoginActivity.this, "", "註冊中…", true);

		ReqUtil.registerUser(mapParam, new COIMCallListener() {

			@Override
			public void onSuccess(JSONObject result) {
				pDialog.dismiss();

				if (Assist.getErrCode(result) == 0) {
					Log.i(LOG_TAG, "success\n" + result);
					mSignupDialog.dismiss();
					goToHome();

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch (id) {
		case R.id.action_logout:
			logoutBukr();
			break;

		default:
			break;
		}

		// return super.onOptionsItemSelected(item);
		return true;
	}

	private void logoutBukr() {
		ReqUtil.logout(new COIMCallListener() {

			@Override
			public void onSuccess(JSONObject result) {
				Log.i(LOG_TAG, "result: " + result);
				// goToHome();
			}

			@Override
			public void onFail(HttpResponse arg0, Exception arg1) {
				Log.i(LOG_TAG, "err: " + arg1.getLocalizedMessage());
			}
		});
	}
	
}

