package viewmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InventoryVM {

	public Long id;
	public String title;
	public String description;
	//public double price;
	//public double cost;
	public String price;
	public String cost;
	public String productId;
	public Long collection;
	public String collectionName;
	public String imagePath;
	public String imgId;
	public String stockNumber;
	
	public List<KeyValueDataVM> customData  = new ArrayList<KeyValueDataVM>();
	public Map<String, String> customMapData;
}
