package viewmodel;

import java.util.ArrayList;
import java.util.List;

public class LeadVM {
	public String id;
	public String vin;
	public String custName;
	public String custEmail;
	public String custNumber;
	public String stockNumber;
	public String manufacturers;
	/*public String make;
	public String model;*/
	//public String makeSelect;
	//public String modelSelect;
	public String leadType;
	public String hearedFrom;
	public String contactedFrom;
	public String bestDay;
	public String bestTime;
	public String prefferedContact;
	public String comments;
	public List<String> options;
	public String year;
	public String custZipCode;
	//public String exteriorColour;
	//public String kilometres;
	//public String engine;
	//public String doors;
	//public String transmission;
	//public String drivetrain;
	//public String bodyRating;
	//public String tireRating;
	//public String engineRating;
	public String transmissionRating;
	//public String glassRating;
	//public String interiorRating;
	//public String exhaustRating;
	public String rentalReturn;
	public String odometerAccurate;
	public String serviceRecords;
	public String lienholder;
	public String titleholder;
	public String equipment;
	//public String vehiclenew;
	//public String accidents;
	public String damage;
	public String salvage;
	public String paint;
	public String enthicity;
	public String productId;
	public List<InventoryVM> stockWiseData = new ArrayList<InventoryVM>();
	public List<KeyValueDataVM> customData  = new ArrayList<KeyValueDataVM>();
}