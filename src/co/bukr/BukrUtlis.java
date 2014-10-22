package co.bukr;

import android.net.Uri;
import android.util.Log;

public class BukrUtlis {
	static String getBookIconUrl(String icon) {
		String uri = new Uri.Builder()
				.scheme("http")
				.authority(Config.COIM_SERVER_URL)
				.appendPath("books")
				.appendPath("auxi")
				.appendPath("node")
				.appendQueryParameter("path", icon)
				.appendQueryParameter("_key", Config.COIM_APP_KEY)
				.build()
				.toString();
		
		Log.i("getBookIconUrl", uri);
		return uri;
	}
}
