package co.bukr;

public class CommentItem {
	String mUcID;
	String mBody;
	String mMdTime;
	String mDspName;
	
	public CommentItem(String ucID, String body, String mdTime, String dspName) {
		mUcID= ucID;
		mBody = body;
		mMdTime = mdTime;
		mDspName = dspName;
	}
}
