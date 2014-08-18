package co.bukr;

public class BookItem {
	String mBkID;
	String mIconURI;
	String mTitle;
	
	public BookItem(String bkID, String iconURI, String title) {
		super();
		mIconURI= iconURI;
		mTitle = title;
		mBkID = bkID;
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
