package co.bukr;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public class Config {
	public static final DisplayImageOptions OPTIONS 
	= new DisplayImageOptions.Builder()
	.cacheInMemory(true)
	.cacheOnDisc(true)
	.displayer(new SimpleBitmapDisplayer())
	.showImageOnFail(R.drawable.book_back)
	//.showImageOnLoading(R.drawable.photo_mother_loading)
	.build();

	public static final DisplayImageOptions OPTIONS_ICON 
	= new DisplayImageOptions.Builder()
	.cacheInMemory(true)
	.cacheOnDisc(true)
	.displayer(new RoundedBitmapDisplayer(1000))
	.showImageOnFail(R.drawable.profile_pic)
	.bitmapConfig(Bitmap.Config.RGB_565)
	//.showImageOnLoading(R.drawable.photo_mother_loading)
	.build();

	
	
	public static String bkID = ""; 
	public static String content = "";
	public static String fgID="";
	public static String COIM_APP_KEY = "ef072a5d-c1ab-682a-617a-9699f6762d7d";
	public static String COIM_SERVER_URL = "bukruat.coimapi.tw";
	public static String my_favorite_title="";
	public static Bitmap book_cover = null;
	public static String book0 = "";
	public static String book1 = "";
	public static String book2 = "";
	public static String BukrData = "bukruat";
	public static String CoimtionData = "book";
	
}
