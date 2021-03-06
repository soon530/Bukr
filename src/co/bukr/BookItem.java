package co.bukr;

public class BookItem {
	String mBkID;
	String mIconURI;
	String mTitle;
	String mAuthor;
	boolean mIsFavi;
	
	public BookItem(String bkID, String iconURI, String title, String author, boolean isFavi) {
		super();
		mIconURI= iconURI;
		mTitle = title;
		mBkID = bkID;
		mAuthor = author;
		mIsFavi = isFavi;
	}

	public String getBkID() {
		return mBkID;
	}

	public void setBkID(String bkID) {
		mBkID = bkID;
	}

	
	public String getIconURI() {
		return mIconURI;
	}

	public void setIconURI(String iconURI) {
		mIconURI = iconURI;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

}
