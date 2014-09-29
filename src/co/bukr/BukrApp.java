package co.bukr;

import android.app.Application;
import android.content.Context;

import com.coimotion.csdk.common.COIMException;
import com.coimotion.csdk.util.ReqUtil;
import com.coimotion.csdk.util.sws;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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
		
		initImageLoader(this);

	}
	
	public void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				// .writeDebugLogs()
				.denyCacheImageMultipleSizesInMemory().build();

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

}
