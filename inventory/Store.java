import java.util.ArrayList;

public class Store {
	
	private ArrayList<String> inventoryList = new ArrayList<>();
	
	public Store(){
		inventoryList = new DBManager().getInventoryList();
	}
	
	private boolean isPresent(String name){
		if(inventoryList.contains(name))
			return true;
		else
			return false;
	}
	
	public Inventory makeNewInventory(String name, String storageType) throws Exception {
		if (isPresent(name))
			return null;
		
		Inventory inventory = new Inventory(name, storageType);
		inventory.storeInventory();
		inventoryList.add(inventory.getInventoryName());
		return inventory;
	}
	
	public boolean deleteInventory(String name) throws Exception {
		if(!isPresent(name))
			return false;
		new Inventory().removeInventory(name);
		return true;
	}
	
	public boolean updateInventory(Inventory updated) throws Exception {
		if(!isPresent(updated.getInventoryName()))
			return false;
		updated.updateInventory(updated);
		return true;
	}
	
	public boolean addItemToInventory(Item item, String inventoryName) throws Exception {
		if(!isPresent(inventoryName))
			return false;
		item.storeItem(inventoryName);
		return true;
	}
	
	public Item makeNewItem(String name, String type, String stage){
		return new Item(name, type, stage);
	}
	
	public Inventory searchInventory(String name) throws Exception {
		return new DBManager().searchInventory(name);
	}
	
	public ArrayList<Item> searchItem(String name) throws Exception {
		ArrayList<Item> found = new DBManager().searchItem(name);
		if(found == null)
			return null;
		return found;
	}
	
	public ArrayList<Item> searchItem(String name, String inventory) throws Exception {
		ArrayList<Item> found = new DBManager().searchItem(name, inventory);
		if(found == null)
			return null;
		return found;
	}
	
	public Item searchItemReturnFirstMatch(String name) throws Exception {
		ArrayList<Item> found = new DBManager().searchItem(name);
		if(found == null)
			return null;
		return found.get(0);
	}
	
	public Item searchItemReturnFirstMatch(String name, String inventory) throws Exception {
		ArrayList<Item> found = new DBManager().searchItem(name, inventory);
		if(found == null)
			return null;
		return found.get(0);
	}
	
	public Item searchExactItem(String name, String type, String inventory) throws Exception {
		ArrayList<Item> list = searchItem(name, inventory);
		for (Item item : list){
			System.out.println(item.toString());
			if (item.getItemType().equals(type))
				return item;
		}
		return null;
	}
	
	public boolean removeItem (String itemName, String itemType, String itemStage, String inventory) throws Exception {
		if(!isPresent(inventory))
			return false;
		new Item().removeItem(itemName, itemType, itemStage, inventory);
		return true;
	}
	
	public void updateItem (Item updated, String inventory) throws Exception{
		updated.updateItem(updated, inventory);
	}
	
	public String listAllInventories() throws Exception {
		ArrayList<Inventory> list = new DBManager().getInventory("");
		
		if(list == null)
			return "NO INVENTORIES YET";
		
		StringBuilder allList = new StringBuilder();
		for(Inventory inventory : list){
			allList.append(inventory.toString() + "\n");
		}
		return allList.toString();
	}
	
	public String listAllItems() throws Exception {
		ArrayList<Item> list = new DBManager().getAllItems();
		
		if(list == null)
			return "NO INVENTORIES YET";
		
		StringBuilder allList = new StringBuilder();
		for(Item item : list){
			allList.append(item.toString() + "\n");
		}
		return allList.toString();
	}
	
	public String listAllItemsInInventory (String inventory) throws Exception {
		ArrayList<Item> list = new DBManager().getItems(inventory, "");
		
		if(list == null)
			return "NO INVENTORIES YET";
		
		StringBuilder allList = new StringBuilder();
		for(Item item : list){
			allList.append(item.toString() + "\n");
		}
		return allList.toString();
	}
	
	public void addQuantity (int val, String itemName, String itemType, String inventory) throws Exception {
		Item item = searchExactItem(itemName, itemType, inventory);
		item.addQuantity(val);
		new DBManager().updateItemQty(item, inventory, val);
	}
	
	public boolean addQuantity(int val, Item item, Inventory inventory) throws Exception {
		if (!isPresent(inventory.getInventoryName()))
			return false;
		addQuantity(val, item.getItemName(), item.getItemType(), inventory.getInventoryName());
		return true;
	}
	
	public boolean setItemQuantityInventory (int val, String inventory) throws Exception {
		if (isPresent(inventory))
			return false;
		new DBManager().setItemCount(inventory, val);
		return true;
	}
	
	public boolean setItemQuantity (int val, Item item, String inventory) throws Exception {
		if (!isPresent(inventory))
			return false;
		new DBManager().setItemQty(item, inventory, val);
		return true;
	}
}