package co.bukr;

public class BookItem {
	String mIconURI;
	String mTitle;
	
	public BookItem(String iconURI, String title) {
		super();
		mIconURI= iconURI;
		mTitle = title;
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
