package co.bukr;

import android.app.Application;

import com.coimotion.csdk.common.COIMException;
import com.coimotion.csdk.util.ReqUtil;
import com.coimotion.csdk.util.sws;

public class BukrApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		try {
			ReqUtil.initSDK(this);
			sws.initSws(this);
		} catch (COIMException e) {
		} catch (Exception e) {
		}

	}
}
