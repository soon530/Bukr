package co.bukr;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.coimotion.csdk.common.COIMCallListener;
import com.coimotion.csdk.common.COIMException;
import com.coimotion.csdk.util.Assist;
import com.coimotion.csdk.util.ReqUtil;
import com.coimotion.csdk.util.sws;

public class LoginActivity extends Activity implements OnClickListener{
	private static final String LOG_TAG = "loginActivity";
	private final static String checkTokenURL = "core/user/profile";

	private EditText accNameText, passwdText, passwd2Text;
	private Button submitBut;
	private RadioButton loginRadio, regRadio;
	private RadioGroup radioGroup;
	private ProgressDialog pDialog;

	private Button mGo;

	private Button mFB;

	// for new layout
	private ImageView mLogin;
	private Dialog mLoginDialog;
	private TextView mSignup;
	private Dialog mSignupDialog;

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		getApplication().getSharedPreferences("artMania", 0).edit()
				.putBoolean("closeApp", true).commit();
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
		default:
			break;
		}
		
	}

	private Dialog getLoginDialog() {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.activity_login_dialog);
		return dialog;
	}

	private Dialog getSignupDialog() {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.activity_singup_dialog);
		return dialog;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
/*		try {
			ReqUtil.initSDK(getApplication());
			sws.initSws(getApplication());
		} catch (COIMException e) {
		} catch (Exception e) {
		}
*/
		setContentView(R.layout.activity_login2);
		
		mLogin = (ImageView) findViewById(R.id.login_login);
		mLoginDialog = getLoginDialog();
		mLogin.setOnClickListener(this);
		
		
		mSignup = (TextView) findViewById(R.id.login_singup);
		mSignupDialog = getSignupDialog();
		mSignup.setOnClickListener(this);
		

/*		accNameText = (EditText) findViewById(R.id.accName);
		passwdText = (EditText) findViewById(R.id.passwd);
		passwd2Text = (EditText) findViewById(R.id.passwd2);
		submitBut = (Button) findViewById(R.id.submitBut);
		submitBut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (loginRadio.isChecked()) {
					Log.i(LOG_TAG, "login mode");
					pDialog = ProgressDialog.show(LoginActivity.this, "",
							"登入中…", true);
					Map<String, Object> mapParam = new HashMap<String, Object>();
					mapParam.put("accName", accNameText.getText().toString());
					mapParam.put("passwd", passwdText.getText().toString());
					ReqUtil.login("core/user/login", mapParam,
							new COIMCallListener() {

								@Override
								public void onFail(HttpResponse response,
										Exception ex) {
									pDialog.dismiss();
									AlertDialog.Builder builder = new AlertDialog.Builder(
											LoginActivity.this);
									builder.setTitle("Login");
									builder.setMessage(ex.getLocalizedMessage());
									builder.show();
									Log.i(LOG_TAG,
											"fail\n" + ex.getLocalizedMessage());
								}

								
								 * @Override public void onSuccess(Map<String,
								 * Object> result) { Log.i(LOG_TAG, "success\n"
								 * + result); pDialog.dismiss(); Intent intent =
								 * new Intent();
								 * intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
								 * ); intent.setClass(LoginActivity.this,
								 * MainActivity.class); startActivity(intent);
								 * finish();
								 * 
								 * }
								 
								@Override
								public void onSuccess(JSONObject result) {

									pDialog.dismiss();

									if (Assist.getErrCode(result) == 0) {
										// JSONArray list =
										// Assist.getList(result);
										// Log.i(LOG_TAG, "success\n" + result);

										goToHome();

									} else {
										Assist.showAlert(LoginActivity.this,
												Assist.getMessage(result));
									}

								}

							});
				}
				if (regRadio.isChecked()) {
					Log.i(LOG_TAG, "reg mode");
					pDialog = ProgressDialog.show(LoginActivity.this, "",
							"註冊中…", true);
					Map<String, Object> mapParam = new HashMap<String, Object>();
					mapParam.put("accName", accNameText.getText().toString());
					mapParam.put("passwd", passwdText.getText().toString());
					mapParam.put("passwd2", passwd2Text.getText().toString());
					ReqUtil.registerUser(mapParam, new COIMCallListener() {

						@Override
						public void onFail(HttpResponse response, Exception ex) {
							pDialog.dismiss();
							AlertDialog.Builder builder = new AlertDialog.Builder(
									LoginActivity.this);
							builder.setTitle("Login");
							builder.setMessage(ex.getLocalizedMessage());
							builder.show();
							Log.i(LOG_TAG, "fail\n" + ex.getLocalizedMessage());
						}

						@Override
						public void onSuccess(JSONObject result) {
							Log.i(LOG_TAG, "success\n" + result);
							pDialog.dismiss();

							if (Assist.getErrCode(result) == 0) {
								goToHome();
							} else {
								Assist.showAlert(LoginActivity.this,
										Assist.getMessage(result));
							}
						}
					});
				}
			}
		});

		loginRadio = (RadioButton) findViewById(R.id.loginRadio);
		regRadio = (RadioButton) findViewById(R.id.regRadio);

		radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (loginRadio.isChecked()) {
					passwd2Text.setVisibility(View.INVISIBLE);
					submitBut.setText("登入");
				}
				if (regRadio.isChecked()) {
					passwd2Text.setVisibility(View.VISIBLE);
					submitBut.setText("註冊");
				}
			}
		});

		mGo = (Button) findViewById(R.id.go);
		mGo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//logoutBukr();
				goToHome();
			}
		});

		mFB = (Button) findViewById(R.id.facebook);
		mFB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				loginBukrFB();
				//checkBukrFBID();
				//checkBukrFB();
			}
		});
*/	
	}

	private void loginBukrFB() {
		sws.loginFB(LoginActivity.this, new COIMCallListener() {
			
			@Override
			public void onSuccess(JSONObject result) {
				Log.i(LOG_TAG, "result: " + result);
				//goToHome();
			}
			
			@Override
			public void onFail(HttpResponse arg0, Exception arg1) {
				Log.i(LOG_TAG, "err: " + arg1.getLocalizedMessage());

			}
		});
		
	}
	private void checkBukrFBID() {
		ReqUtil.send(checkTokenURL, null, new COIMCallListener() {
			
			@Override
			public void onSuccess(JSONObject result) {
				Log.i(LOG_TAG, "result: " + result);
				
			}
			
			@Override
			public void onFail(HttpResponse arg0, Exception arg1) {
				Log.i(LOG_TAG, "err: " + arg1.getLocalizedMessage());
			}
		});
		
	}
	
	private void checkBukrFB() {
		sws.checkFB(LoginActivity.this, new COIMCallListener() {
			
			@Override
			public void onSuccess(JSONObject result) {
				Log.i(LOG_TAG, "result: " + result);
			}
			
			@Override
			public void onFail(HttpResponse arg0, Exception arg1) {
				Log.i(LOG_TAG, "err: " + arg1.getLocalizedMessage());
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
	
	private void logoutBukr() {
		ReqUtil.logout(new COIMCallListener() {
			
			@Override
			public void onSuccess(JSONObject result) {
				Log.i(LOG_TAG, "result: " + result);
				//goToHome();
			}
			
			@Override
			public void onFail(HttpResponse arg0, Exception arg1) {
				Log.i(LOG_TAG, "err: " + arg1.getLocalizedMessage());
			}
		});	
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

		
		//return super.onOptionsItemSelected(item);
		return true;
	}

	
}
