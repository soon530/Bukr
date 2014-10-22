package co.bukr;

import org.apache.http.HttpResponse;
import org.json.JSONObject;

import com.coimotion.csdk.common.COIMCallListener;
import com.coimotion.csdk.util.ReqUtil;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link AboutMeFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link AboutMeFragment#newInstance} factory method to create an instance of
 * this fragment.
 * 
 */
public class AboutMeFragment extends Fragment implements OnClickListener {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	protected static final String LOG_TAG = "AboutMeFragment";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private OnFragmentInteractionListener mListener;
	private ImageView mFacebook;
	private ImageView mEmail;
	private ImageView mLogout;
	private SharedPreferences pref;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment ReadingFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static AboutMeFragment newInstance(int sectionNumber) {
		AboutMeFragment fragment = new AboutMeFragment();
		Bundle args = new Bundle();
		args.putInt("section_number", sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public AboutMeFragment() {
		// Required empty public constructor
	}

/*	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}
*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_about_me, container, false); 

		mFacebook = (ImageView) rootView.findViewById(R.id.facebook);
		mEmail = (ImageView) rootView.findViewById(R.id.email);
		mLogout = (ImageView) rootView.findViewById(R.id.logout);
		
		
		mFacebook.setOnClickListener(this);
		mEmail.setOnClickListener(this);
		mLogout.setOnClickListener(this);
		return rootView;
	}

	// TODO: Rename method, update argument and hook method into UI event
/*	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}
*/
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
/*		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
*/		
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				"section_number"));

	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(Uri uri);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.facebook:
			goFacebook();
			break;
		case R.id.email:
			goEmail();
			break;
		case R.id.logout:
			goLogout();
			break;

		default:
			break;
		}
	}

	private void goFacebook() {
		String uri = "fb://page/130253673844953";
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
		startActivity(intent);
	}

	private void goEmail() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("message/rfc822");
		intent.putExtra(Intent.EXTRA_EMAIL,
				new String[] { "service@bukr.co" });
		intent.putExtra(Intent.EXTRA_SUBJECT, "給Bukr的建議");
		intent.putExtra(Intent.EXTRA_TEXT, "Bukr開發小組您好： \n\n    我對Bukr的想法是...");
		startActivity(Intent.createChooser(intent, ""));
		
	}

	private void goLogout() {
		ReqUtil.send("core/user/logout", null, new COIMCallListener() {

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
		
		pref = getActivity().getApplication().getSharedPreferences("bukr", 0);
		pref.edit().putBoolean("login", false).commit();
		goLogin();
	}
	
	private void goLogin() {

		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setClass(getActivity(), LoginActivity.class);
		startActivity(intent);
	}



}
