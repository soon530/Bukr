package co.bukr;

public class CommentItem {
	String mUcID;
	String mBody;
	String mMdTime;
	String mDspName;
	String mIconURI;
	
	public CommentItem(String ucID, String body, String mdTime, String dspName, String iconURI) {
		mUcID= ucID;
		mBody = body;
		mMdTime = mdTime;
		mDspName = dspName;
		mIconURI = iconURI;
	}
}
