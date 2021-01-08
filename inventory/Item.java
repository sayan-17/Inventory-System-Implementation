

public class Item {
	private String itemName,  itemType, currency, description, stageOfManufacture, storeSomeStringData = "";
	private float price;
	private int quantity;
	
	public Item (){
		itemName = "N/A";
		itemType = "N/A";
		currency = "N/A";
		price = 0;
		quantity = 0;
		description = "N/A";
		stageOfManufacture = "N/A";
	}
	
	public Item (String name, String itemType, String stage){
		itemName = name;
		this.itemType = itemType;
		currency = "N/A";
		price = 0;
		quantity = 0;
		description = "N/A";
		stageOfManufacture = stage;
	}
	
	public Item (String name, String type, String curr, String desc, String stage, float price, int quantity){
		itemName = name;
		itemType = type;
		currency = curr;
		this.price = price;
		this.quantity = quantity;
		description = desc;
		stageOfManufacture = stage;
	}
	
	public void setName(String name){
		this.itemName = name;
	}
	
	public void setType(String type){
		this.itemType = type;
	}
	
	public void setPrice(float price){
		this.price = price;
	}
	
	public void setCurrency(String currency){
		this.currency = currency;
	}
	
	public void setCurrencyAndPrice (float price, String  currency){
		this.price = price;
		this.currency = currency;
	}
	
	public void setQuantity(int quantity){
		this.quantity = quantity;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public void setStageOfManufacture(String stageOfManufacture){
		this.stageOfManufacture = stageOfManufacture;
	}
	
	public String toSQLString (){
		return "'" + itemName + "','" + itemType + "','" + currency + "','" +
				description + "','" + stageOfManufacture + "'," + price + "," +  quantity;
	}
	public void addQuantity (int val){
		this.quantity +=val;
	}
	
	public void storeItem(String inventoryName) throws Exception {
		new DBManager().addItemToInventory(this, inventoryName);
	}
	
	public void removeItem(String inventoryName) throws Exception {
		new DBManager().removeItemFromInventory(this, inventoryName);
	}
	
	public void removeItem(String itemName, String itemType, String itemStage,String inventoryName) throws Exception {
		new DBManager().removeItemFromInventory(itemName, itemType, itemStage, inventoryName);
	}
	
	public void updateItem(Item item, String inventoryName) throws Exception {
		item.removeItem(inventoryName);
		item.storeItem(inventoryName);
	}
	
	public String getItemType() {
		return itemType;
	}
	
	public String getItemName() {
		return itemName;
	}
	
	public String getStage() {
		return stageOfManufacture;
	}
	
	public float getPrice() {
		return price;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public String getDescription() {
		return description;
	}
	
	public int getQuantity(){
		return quantity;
	}
	
	@Override
	public String toString(){
		return "[ ITEM NAME: '" + itemName + "', ITEM TYPE : '" + itemType +
				"', CURRENCY : '" + currency + "', DESCRIPTION : '" + description + "', STAGE : '" + stageOfManufacture +
				"', PRICE : '" +  price + "', QUANTITY : '" + quantity + "' ] " + storeSomeStringData;
	}
	
	public void setStoreSomeStringData(String stringData){
		this.storeSomeStringData = stringData;
	}
}
