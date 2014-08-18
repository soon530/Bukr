package co.bukr;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public class Config {
	public static final DisplayImageOptions OPTIONS 
	= new DisplayImageOptions.Builder()
	.cacheInMemory(true)
	.cacheOnDisc(true)
	.displayer(new SimpleBitmapDisplayer())
	//.showImageOnFail(R.drawable.photo_mother_fail1)
	//.showImageOnLoading(R.drawable.photo_mother_loading)
	.build();

	public static String bkID = ""; 
	
}
