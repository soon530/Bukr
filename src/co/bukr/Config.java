package co.bukr;

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
	public static String COIM_APP_KEY = "ab2b2c86-cd6e-a51a-a800-b56fb9fefd3b";
	public static String my_favorite_title="";
}
