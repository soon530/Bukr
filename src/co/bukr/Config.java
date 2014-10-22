package co.bukr;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public class Config {
	public static final DisplayImageOptions OPTIONS 
	= new DisplayImageOptions.Builder()
	.cacheInMemory(true)
	.cacheOnDisc(true)
	.displayer(new SimpleBitmapDisplayer())
	.showImageOnFail(R.drawable.image_on_fail)
	//.showImageOnLoading(R.drawable.photo_mother_loading)
	.build();

	public static String bkID = ""; 
	public static String content = "";
	public static String fgID="";
	public static String COIM_APP_KEY = "ef072a5d-c1ab-682a-617a-9699f6762d7d";
	public static String my_favorite_title="";
	public static Bitmap book_cover = null;
	public static String book0 = "";
	public static String book1 = "";
	public static String book2 = "";
}
