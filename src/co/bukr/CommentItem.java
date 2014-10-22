package co.bukr;

public class CommentItem {
	String mUcID;
	String mBody;
	String mMdTime;
	String mDspName;
	String mIconURI;
	String mPsnID;
	
	public CommentItem(String ucID, String body, String mdTime, String dspName, String iconURI, String psnID) {
		mUcID= ucID;
		mBody = body;
		mMdTime = mdTime;
		mDspName = dspName;
		mIconURI = iconURI;
		mPsnID = psnID;
	}
}
