package co.bukr;

public class DrawerItem {
	String ItemName;
	int imgResID;
	int imgResIDColor;
	
	public DrawerItem(String itemName, int imgResID, int imgResIDColor) {
		super();
		ItemName = itemName;
		this.imgResID = imgResID;
		this.imgResIDColor = imgResIDColor;
	}

	public String getItemName() {
		return ItemName;
	}

	public void setItemName(String itemName) {
		ItemName = itemName;
	}

	public int getImgResID() {
		return imgResID;
	}

	public void setImgResID(int imgResID) {
		this.imgResID = imgResID;
	}

	public int getImgResIDColor() {
		return imgResIDColor;
	}

	public void setImgResIDColor(int imgResIDColor) {
		this.imgResIDColor = imgResIDColor;
	}

}
