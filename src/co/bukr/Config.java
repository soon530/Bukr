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
	// for image download
	public static String COIM_APP_KEY = "206a7379-8a22-d654-240e-26783313863c";
	public static String COIM_SERVER_URL = "bukrprod.coimapi.tw";
	
	public static String my_favorite_title="";
	public static Bitmap book_cover = null;
	public static String book0 = "";
	public static String book1 = "";
	public static String book2 = "";
	public static String BukrData = "bukrprod";
	public static String CoimtionData = "book";
	public static String WA_CODE = "bukrprod";
	
}
