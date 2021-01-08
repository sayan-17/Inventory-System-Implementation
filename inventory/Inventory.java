import java.util.ArrayList;

public class Inventory {
	
	private String name, storageType;
	private ArrayList<String> subInventoryList;
	private ArrayList<Item> itemList;
	private int noOfSubInventory, noOfItems;
	
	public Inventory() {
		name = "N/A";
		subInventoryList = new ArrayList<>();
		itemList = new ArrayList<>();
		storageType = "N/A";
		noOfSubInventory = noOfItems = 0;
	}
	
	public Inventory(String name) {
		this.name = name;
		subInventoryList = new ArrayList<>();
		itemList = new ArrayList<>();
		storageType = "N/A";
		noOfSubInventory = noOfItems = 0;
	}
	
	public Inventory(String name, String type) {
		this.name = name;
		subInventoryList = new ArrayList<>();
		itemList = new ArrayList<>();
		storageType = type;
		noOfSubInventory = noOfItems = 0;
	}
	
	public Inventory(String name, String storageType, String subInventoryList, int noOfSubInventory, int noOfItems) {
		this.name = name;
		this.storageType = storageType;
		this.noOfSubInventory = noOfSubInventory;
		this.noOfItems = noOfItems;
		this.subInventoryList = storeSubInventoryList(subInventoryList);
		itemList = new ArrayList<>();
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setStorageType(String type){
		this.storageType = type;
	}
	
	public void addItem (Item item) throws Exception {
		++noOfItems;
		try {
			
			item.storeItem(this.name);
			new DBManager().updateItemCount(this.name, noOfItems);
		
		}catch (Exception e){
			this.itemList.add(item);
		}
		
	}
	
	public Inventory createSubInventory(String name) throws Exception {
		this.subInventoryList.add(name);
		this.noOfSubInventory++;
		Inventory sub = new Inventory(name, this.storageType);
		updateInventory(this);
		return  sub;
	}
	
	public boolean isEmpty(){
		if (noOfItems == 0 && noOfSubInventory == 0)
			return false;
		return true;
	}
	
	public boolean hasSubInventory (){
		if (noOfSubInventory == 0)
			return false;
		return true;
	}
	
	public String getInventoryName (){
		return name;
	}
	
	public int getNoOfSubInventory(){
		return noOfSubInventory;
	}
	
	public int getNoOfItems(){
		return noOfItems;
	}
	
	public void storeInventory() throws Exception {
		
		DBManager manager = new DBManager();
		manager.createFinalInventory(this.name);
		manager.addInventory(this);
		
		if (noOfItems > 0)
			for(Item item : itemList) {
				this.addItem(item);
			}
	}
	
	public void removeInventory(String name) throws Exception {
		new DBManager().removeInventory(name);
	}
	
	public void updateInventory(Inventory inventory) throws Exception {
		removeInventory(inventory.name);
		inventory.storeInventory();
	}
	
	public void readItems() throws Exception {
		itemList = new DBManager().getItems(this.name, "");
	}
	
	public String toSQLString() {
		return "'" + name + "','" + storageType + "','" +
				getSubInventoryInDBFormat() + "'," +  noOfSubInventory + "," + noOfItems + "";
	}
	
	private String getSubInventoryInDBFormat() {
		
		StringBuilder list = new StringBuilder();
		
		if (noOfSubInventory == 0)
			return  "NO SUB INVENTORIES";
		
		int flag = 0;
		for (String inventoryName : subInventoryList){
			if((++flag) < noOfSubInventory)
				list = list.append(inventoryName + ",");
			else
				list = list.append(inventoryName);
		}
		
		return list.toString();
	}
	
	private ArrayList<String> storeSubInventoryList (String list){
		
		int startIndex = 0, endIndex;
		ArrayList<String> subList = new ArrayList<>();
		while (true){
			endIndex = list.indexOf(",", startIndex);
			if (endIndex == -1) {
				subList.add(list.substring(startIndex, list.length() - 1));
				break;
			}
			subList.add(list.substring(startIndex,endIndex - 1));
			startIndex = endIndex + 1;
		}
		return subList;
	}
	
	@Override
	public String toString(){
		StringBuilder string = new StringBuilder();
		string.append("NAME : " + name + ", STORAGE TYPE : " + storageType +
						", NO OF SUB INVENTORIES : " + noOfSubInventory +
							", NO OF ITEMS : " + noOfItems + "\n");
		
		string.append("SUB INVENTORIES : " + getSubInventoryInDBFormat() + "\n");
		
		if (itemList == null)
			string.append("NO ITEMS YET");
		else {
			for (Item item : itemList) {
				string.append(item.toString() + "\n");
			}
		}
		return  string.toString();
	}
}