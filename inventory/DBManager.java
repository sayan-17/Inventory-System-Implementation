import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;

public class DBManager {
	
	private final String driver = "com.mysql.jdbc.Driver";
	private final String location = "jdbc:mysql://localhost:3306/inventorySystem";
	private final String user = "root";
	private final String password = "sayan";
	
	private final String inventoryListTabName = "INVENTORY_LIST";
	
	public final static String ALL_COLS = "*";
	
	public final static String INV_NAME = "NAME";
	public final static String INV_TYPE = "STORAGE_TYPE";
	public final static String INV_SUB_LIST = "SUB_LIST";
	public final static String INV_NO_OF_SUB = "NO_OF_SUB";
	public final static String INV_NO_OF_ITEMS = "NO_OF_ITEMS";
	
	public final static String ITEM_NAME = "ITEM_NAME";
	public final static String ITEM_TYPE = "ITEM_TYPE";
	public final static String ITEM_CURRENCY = "CURRENCY";
	public final static String ITEM_ABOUT = "DESCRIPTION";
	public final static String ITEM_STAGE = "STAGE";
	public final static String ITEM_PRICE = "PRICE";
	public final static String ITEM_QTY = "QUANTITY";
	
	public void executeStatement(String command) throws Exception {
		
		Class.forName(driver);
			
		Connection con = DriverManager.getConnection(location, user, password);
			
		Statement stmt = con.createStatement();
		command = command.toLowerCase();
		System.out.println(command);
		stmt.execute(command);
	}
	
	public ResultSet executeQuery(String command) throws Exception {
		
		ResultSet set;
		Class.forName(driver);
			
		Connection con = DriverManager.getConnection(location, user, password);
			
		Statement stmt = con.createStatement();
		command = command.toLowerCase();
		System.out.println(command);
		set = stmt.executeQuery(command);
		
		return set;
	}
	
	public void createFinalInventory (String name) throws Exception {
		String command = "CREATE TABLE " + name +
							"(ITEM_NAME VARCHAR(100), " +
							"ITEM_TYPE VARCHAR(100), " +
							"CURRENCY VARCHAR(20), " +
							"DESCRIPTION VARCHAR(1000), " +
							"STAGE VARCHAR(20), " +
							"PRICE FLOAT, " +
							"QUANTITY INT);";
		
		executeStatement(command);
	}
	
	public void addItemToInventory (Item item, String inventory) throws Exception {
		String command = "INSERT INTO " + inventory + " VALUES " +
						"(" + item.toSQLString() +
						");";
		executeStatement(command);
	}
	
	public void removeItemFromInventory (Item item, String inventory) throws Exception {
		removeItemFromInventory(item.getItemName(), item.getItemType(), item.getStage(), inventory);
	}
	
	public void removeItemFromInventory (String itemName, String itemType, String itemStage, String inventory) throws Exception {
		String command = "DELETE FROM " + inventory + " WHERE " + DBManager.ITEM_NAME + " = '" + itemName
								+ "' AND ITEM_TYPE = '" + itemType + "';";
		executeStatement(command);
	}
	
	public ArrayList<Item> getItems (String inventory, String extra) throws Exception  {
		ArrayList<Item> itemList = new ArrayList<>();
		String command = "SELECT " + DBManager.ALL_COLS + " FROM " + inventory + " " + extra + ";";
		
		ResultSet set = executeQuery(command);
		
		
		while (set.next()){
			String itemName,  itemType, currency, description, stageOfManufacture;
			float price;
			int quantity;
			
			itemName = set.getString(ITEM_NAME);
			itemType = set.getString(ITEM_TYPE);
			currency = set.getString(ITEM_CURRENCY);
			description = set.getString(ITEM_ABOUT);
			stageOfManufacture = set.getString(ITEM_STAGE);
			price = set.getInt(ITEM_PRICE);
			quantity = set.getInt(ITEM_QTY);
			
			Item item = new Item(itemName, itemType, currency, description, stageOfManufacture, price, quantity);
			
			itemList.add(item);
		}
		
		return itemList;
	}
	
	public ArrayList<Item> getAllItems () throws Exception  {
		ArrayList<Inventory> inventories = getInventory("");
		ArrayList<Item> found = new ArrayList<>();
		for (Inventory inv : inventories){
			ArrayList<Item> item = getItems (inv.getInventoryName(), "");
			if (item == null)
				continue;
			for (Item itm : item){
				itm.setStoreSomeStringData(inv.getInventoryName());
				found.add(itm);
			}
		}
		
		return found;
	}
	
	public void addInventory (Inventory inventory) throws Exception {
		String command = "INSERT INTO " + inventoryListTabName + " VALUES " +
				" ( " + inventory.toSQLString() +
				" );";
		
		executeStatement(command);
	}
	
	public void removeInventory (String inventory) throws Exception {
		String command = "DELETE FROM " + inventoryListTabName + " WHERE " + DBManager.INV_NAME +
							" = '" + inventory + "';";
		executeStatement(command);
		command = "DROP TABLE " + inventory + ";";
		executeStatement(command);
	}
	
	public ArrayList<Inventory> getInventory (String extra) throws Exception {
		
		ArrayList<Inventory> allInventoryList = new ArrayList<>();
		String command = "SELECT " + DBManager.ALL_COLS + " FROM " + inventoryListTabName + " " + extra + ";";
		
		ResultSet set = executeQuery(command);
		
		if (set.wasNull())
			return null;
		
		while (set.next()){
			String name,  storageType, subInventoryList;
			int noOfSubInventory, noOfItems;
			
			name = set.getString(INV_NAME);
			storageType = set.getString(INV_TYPE);
			subInventoryList = set.getString(INV_SUB_LIST);
			noOfSubInventory = set.getInt(INV_NO_OF_SUB);
			noOfItems = set.getInt(INV_NO_OF_ITEMS);
			Inventory inventory = new Inventory(name, storageType, subInventoryList, noOfSubInventory, noOfItems);
			allInventoryList.add(inventory);
		}
		
		return allInventoryList;
	}
	
	public ArrayList<Item> searchItem (String itemName, String inventory) throws Exception {
		
		String extra = "WHERE " + DBManager.ITEM_NAME + " = '" + itemName + "'";
		ArrayList<Item> items;
		
		try{
			
			items = getItems(inventory, extra);
		
		}catch (SQLException e){
			return null;
		}
		
		return items;
	}
	
	public Inventory searchInventory (String inventory) throws  Exception {
	
		String command = "WHERE " + DBManager.INV_NAME + " = '" + inventory + "'";
		ArrayList<Inventory> found;
		
		try {
			
			found = getInventory(command);
			
		}catch (SQLException e){
			return null;
		}
		
		if(found.size() > 1)
			return null;
		else
			return found.get(0);
	}
	
	public ArrayList<Item> searchItem (String itemName) throws Exception {
		
		ArrayList<Inventory> inventories = getInventory("");
		ArrayList<Item> found = new ArrayList<>();
		for (Inventory inv : inventories){
			ArrayList<Item> item = searchItem(itemName, inv.getInventoryName());
			if (item == null)
				continue;
			for (Item itm : item){
				itm.setStoreSomeStringData(inv.getInventoryName());
				found.add(itm);
			}
		}
		
		return found;
	}
	
	public void updateItemCount(String inventory, int count) throws Exception {
		String command = "UPDATE " + inventoryListTabName + " SET " + DBManager.INV_NO_OF_ITEMS
							+ " = " + DBManager.INV_NO_OF_ITEMS + "+" + count  + " WHERE " +
							DBManager.INV_NAME + " = '"  + inventory + "';";
		executeStatement(command);
	}
	
	public void setItemCount(String inventory, int count) throws Exception {
		String command = "UPDATE " + inventoryListTabName + " SET " + DBManager.INV_NO_OF_ITEMS
				+ " = "  + count  + " WHERE " +
				DBManager.INV_NAME + " = '"  + inventory + "';";
		executeStatement(command);
	}
	
	public void updateItemQty (Item item, String inventory, int count) throws Exception{
		String command = "UPDATE " + inventory + " SET " + DBManager.ITEM_QTY
				+ " = " + DBManager.ITEM_QTY + "+" + count +
				" WHERE " + DBManager.ITEM_NAME + " = '"  + item.getItemName() + "';";
		executeStatement(command);
	}
	
	public void setItemQty (Item item, String inventory, int count) throws Exception{
		String command = "UPDATE " + inventory + " SET " + DBManager.ITEM_QTY
				+ " = " + count + " WHERE " + DBManager.ITEM_NAME + " = '"  + item.getItemName() + "';";
		executeStatement(command);
	}
	
	public ArrayList<String> getInventoryList(){
		
		ArrayList<String> list = new ArrayList<>();
		try {
			ArrayList<Inventory> inventories = getInventory("");
			for (Inventory inv : inventories){
				list.add(inv.getInventoryName());
			}
			
		}catch (Exception e){
			list.add("No Inventories Yet");
		}
		return list;
	}
	
}