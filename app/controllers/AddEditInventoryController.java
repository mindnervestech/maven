package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import au.com.bytecode.opencsv.CSVWriter;

import models.AuthUser;
import models.CustomizationDataValue;
import models.CustomizationInventory;
import models.EmailDetails;
import models.FollowBrand;
import models.Location;
import models.MyProfile;
import models.PhotographerHoursOfOperation;
import models.Site;
import models.SiteLogo;
import models.Vehicle;
import models.VehicleImage;
import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import securesocial.core.Identity;
import viewmodel.HoursOperation;
import viewmodel.KeyValueDataVM;
import viewmodel.LeadVM;
import viewmodel.PortalNameVM;
import viewmodel.SpecificationVM;
import viewmodel.VehicleVM;
import views.html.home;

public class AddEditInventoryController extends Controller {

	final static String rootDir = Play.application().configuration()
			.getString("image.storage.path");
	final static String pdfRootDir = Play.application().configuration()
			.getString("pdfRootDir");

	final static String imageUrlPath = Play.application().configuration()
			.getString("image.url.path");

	final static String userRegistration = Play.application().configuration()
			.getString("userRegistration");

	final static String vehicleUrlPath = Play.application().configuration()
			.getString("vehicle.url.path");

	final static String mashapeKey = Play.application().configuration()
			.getString("mashapeKey");

	final static String emailUsername = Play.application().configuration()
			.getString("mail.username");

	final static String emailPassword = Play.application().configuration()
			.getString("mail.password");

	static String simulatevin = "{    'success': true,    'specification': {        'vin': 'WDDNG7KB7DA494890',        'year': '2013',        'make': 'Mercedes-Benz',        'model': 'S-Class',        'trim_level': 'S65 AMG',        'engine': '6.0L V12 SOHC 36V TURBO',        'style': 'SEDAN 4-DR',        'made_in': 'GERMANY',        'steering_type': 'R&P',        'anti_brake_system': '4-Wheel ABS',        'tank_size': '23.80 gallon',        'overall_height': '58.00 in.',        'overall_length': '206.50 in.',        'overall_width': '73.70 in.',        'standard_seating': '5',        'optional_seating': null,        'highway_mileage': '19 miles/gallon',        'city_mileage': '12 miles/gallon'    },    'vin': 'WDDNG7KB7DA494890'}";

	private static boolean simulate = false;

	
	 
	
	   public static Result saveVehicle() throws IOException {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {

	    		Identity user = getLocalUser();
		    	AuthUser userObj = (AuthUser)user;
		    	Form<SpecificationVM> form = DynamicForm.form(SpecificationVM.class).bindFromRequest();
		    	SpecificationVM vm = form.get();
		    	
		    	Vehicle vehicleObj = Vehicle.findByVinAndStatus(vm.vin);
		    	Vehicle vehicle = new Vehicle();
		    	if(vehicleObj == null) {
		    		
			    	
		    		vehicle.setTitle(vm.make+" "+vm.model+" "+vm.year);
			    	vehicle.category = vm.category;
			    	vehicle.vin = vm.productId;
			    	vehicle.typeofVehicle=vm.typeofVehicle;
			    	
			    	vehicle.publicStatus = "draft";
			    	vehicle.year = vm.year;
			    	vehicle.make = vm.make;
			    	vehicle.model = vm.model;
			    	vehicle.trim = vm.trim_level;
			    	vehicle.label = vm.label;
			    	vehicle.stock = vm.stock;
			    	vehicle.cityMileage = vm.city_mileage;
			        vehicle.mileage=vm.mileage;
			    	vehicle.highwayMileage = vm.highway_mileage;
			    	vehicle.cost = vm.cost;
			    	vehicle.price = vm.price;
			    	vehicle.madeIn = vm.made_in;
			    	vehicle.optionalSeating = vm.optional_seating;
			    	vehicle.exteriorColor = vm.extColor;
			    	vehicle.colorDescription = vm.colorDesc;
			    	vehicle.doors = vm.doors;
			    	vehicle.stereo = vm.stereo;
			    	vehicle.engine = vm.engine;
			    	vehicle.bodyStyle = vm.style;
			    	vehicle.location = vm.location;
			    	vehicle.description = vm.description;
					
			    	vehicle.drivetrain = vm.drivetrain;
			    	vehicle.fuelType = vm.fuelType;
			    	vehicle.fuelTank = vm.fuelTank;
			    	vehicle.headlights = vm.headlights;
			    	vehicle.mirrors = vm.mirrors;
			    	vehicle.groundClearance = vm.groundClearance;
			    	vehicle.roof = vm.roof;
			    	vehicle.height = vm.height;
			    	vehicle.length = vm.length;
			    	vehicle.width = vm.width;
			    	vehicle.acceleration = vm.acceleration;
			    	vehicle.standardSeating = vm.standardSeating;
			    	vehicle.engineType = vm.engine;
			    	vehicle.cylinders = vm.cylinders;
			    	vehicle.displacement = vm.displacement;
			    	vehicle.camType = vm.camType;
			    	vehicle.valves = vm.valves;
			    	vehicle.fuelQuality = vm.fuelQuality;
			    	vehicle.horsePower = vm.horsePower;
			    	vehicle.transmission = vm.transmission;
			    	vehicle.gears = vm.gears;
			    	vehicle.brakes = vm.brakes;
			    	vehicle.frontBrakeDiameter = vm.frontBrakeDiameter;
			    	vehicle.frontBrakeType = vm.frontBrakeType;
			    	vehicle.rearBrakeDiameter = vm.rearBrakeDiameter;
			    	vehicle.rearBrakeType = vm.rearBrakeType;
			    	vehicle.activeHeadRestrains = vm.activeHeadRestraints;
			    	vehicle.bodySideReinforcements = vm.bodySideReinforcements;
			    	vehicle.crumpleZones = vm.crumpleZones;
			    	vehicle.impactAbsorbingBumpers = vm.impactAbsorbingBumpers;
			    	vehicle.impactSensor = vm.impactSensors;
			    	vehicle.parkingSensors = vm.parkingSensors;
			    	vehicle.seatbelts = vm.seatbelts;
			    	vehicle.audiSideAssist = vm.audiSideAssist;
			    	vehicle.interiorColor = vm.intColor;
			    	vehicle.comfortFeatures = vm.comfortFeatures;
			    	vehicle.powerOutlet = vm.powerOutlets;
			    	vehicle.powerSteering = vm.powerSteering;
			    	vehicle.rearViewCamera = vm.rearViewCamera;
			    	vehicle.rearViewMonitor = vm.rearViewMonitor;
			    	vehicle.remoteTrunkRelease = vm.remoteTrunkRelease;
			    	vehicle.steeringWheel = vm.steeringWheel;
			    	vehicle.steeringWheelControls = vm.steeringWheelControls;
					
			    	vehicle.standardSeating = vm.standardSeating;
					
			    	vehicle.mileage = vm.mileage;
					vehicle.user = (AuthUser)user;
					vehicle.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
					
			    	vehicle.madeIn = vm.made_in;
			    	vehicle.optionalSeating = vm.optional_seating;
			    	vehicle.status = "Newly Arrived";
			    	vehicle.postedDate = new Date();
			    	List<Site> siteList = new ArrayList<>();
			    	if(vm.siteIds != null) {
				    	for(Long obj: vm.siteIds) {
				    		Site siteObj = Site.findById(obj);
				    		siteList.add(siteObj);
				    	}
				    	vehicle.site = siteList;
			    	}
			    	vehicle.save();
		    	}
		    	MultipartFormData bodys = request().body().asMultipartFormData();
		    	saveCustomInventoryData(vehicle.id,vm,bodys);
		    	sendEmailToBrandFollowers(vehicle.make);
		    	//Vehicle vehicleObj2 = Vehicle.findByVinAndStatus(vm.vin);
		    //	List<Site> siteList = vehicleObj2.getSite();
		    //	List<MyProfile> profile = MyProfile.findByLocation(Long.valueOf(session("USER_LOCATION"))); //findByUser(userObj);
		    	
		    /*	if(!siteList.isEmpty()) {
			    	for(Site siteObj: siteList) {
			    		
			    		if(siteObj.getName().equals("CarsGuru")) {
			    			FTPClient client = new FTPClient();
			    	        FileInputStream fis = null;
			    	        client.connect("ftp.cargurus.com");
			                client.login("glider", "GLF8yP");
			                String filename = vm.vin+".csv";
			                CSVWriter writer = new CSVWriter(new FileWriter(filename));
			            	List<Vehicle> vehicleList = Vehicle.getAllVehicles(userObj);
			            	
			            	String []rowHeaders = new String[29];
			            	rowHeaders[0] = "VIN";
			            	rowHeaders[1] = "Make";
			            	rowHeaders[2] = "Model";
			            	rowHeaders[3] = "Year";
			            	rowHeaders[4] = "Trim";
			            	rowHeaders[5] = "Price";
			            	rowHeaders[6] = "Mileage";
			            	rowHeaders[7] = "Picture Urls";
			            	rowHeaders[8] = "Exterior Color";
			            	rowHeaders[9] = "Dealer Comments";
			            	rowHeaders[10] = "Stock Number";
			            	rowHeaders[11] = "Transmission Type";
			            	rowHeaders[12] = "Installed Options";
			            	rowHeaders[13] = "Dealer Id";
			            	rowHeaders[14] = "Dealer Name";
			            	rowHeaders[15] = "Dealer Street Address";
			            	rowHeaders[16] = "Dealer City";
			            	rowHeaders[17] = "Delaer State";
			            	rowHeaders[18] = "Dealer Zip";
			            	rowHeaders[19] = "Dealer CRM Email";
			            	rowHeaders[20] = "MSRP";
			            	rowHeaders[21] = "Interior Color";
			            	rowHeaders[22] = "Certified";
			            	rowHeaders[23] = "Is New";
			            	rowHeaders[24] = "Engine";
			            	rowHeaders[25] = "Dealer Latitude and Longitude";
			            	rowHeaders[26] = "Dealer Radius";
			            	rowHeaders[27] = "Dealer Phone Number";
			            	rowHeaders[28] = "Dealer Website Url";
			            	
			            	writer.writeNext(rowHeaders);
			            	
			            	for(Vehicle vehicleData: vehicleList) {
			            		String []row = new String[29];
			            		row[0] = vehicleData.vin;
			            		row[1] = vehicleData.make;
			            		row[2] = vehicleData.model;
			            		row[3] = vehicleData.year;
			            		row[4] = vehicleData.trim;
			            		row[5] = vehicleData.price.toString();
			            		row[6] = vehicleData.mileage;
			            		List<VehicleImage> vImageList = VehicleImage.getByVin(vehicleData.vin);
			            		String str = "";
			            		for(VehicleImage img : vImageList) {
			            			str = str +imageUrlPath+img.path+",";
			            		}
			            		row[7] = str;
			            		row[8] = vehicleData.exteriorColor;
			            		row[9] = "";
			            		row[10] = vehicleData.stock;
			            		row[11] = vehicleData.transmission;
			            		
			            		String standardFeatures = "";
			            		if(vehicleData.drivetrain != null) {
			            			standardFeatures = standardFeatures + vehicleData.drivetrain+",";
			            		}
			            		if(vehicleData.fuelType != null) {
			            			standardFeatures = standardFeatures + vehicleData.fuelType+",";
			            		}
			            		if(vehicleData.fuelTank != null) {
			            			standardFeatures = standardFeatures + vehicleData.fuelTank+",";
			            		}
			            		if(vehicleData.headlights != null) {
			            			standardFeatures = standardFeatures + vehicleData.headlights+",";
			            		}
			            		if(vehicleData.mirrors != null) {
			            			standardFeatures = standardFeatures + vehicleData.mirrors+",";
			            		}
			            		if(vehicleData.roof != null) {
			            			standardFeatures = standardFeatures + vehicleData.roof+",";
			            		}
			            		if(vehicleData.acceleration != null) {
			            			standardFeatures = standardFeatures + vehicleData.acceleration+",";
			            		}
			            		if(vehicleData.standardSeating != null) {
			            			standardFeatures = standardFeatures + vehicleData.standardSeating+",";
			            		}
			            		if(vehicleData.engine != null) {
			            			standardFeatures = standardFeatures + vehicleData.engine+",";
			            		}
			            		if(vehicleData.camType != null) {
			            			standardFeatures = standardFeatures + vehicleData.camType+",";
			            		}
			            		if(vehicleData.valves != null) {
			            			standardFeatures = standardFeatures + vehicleData.valves+",";
			            		}
			            		if(vehicleData.cylinders != null) {
			            			standardFeatures = standardFeatures + vehicleData.cylinders+",";
			            		}
			            		if(vehicleData.fuelQuality != null) {
			            			standardFeatures = standardFeatures + vehicleData.fuelQuality+",";
			            		}
			            		if(vehicleData.horsePower != null) {
			            			standardFeatures = standardFeatures + vehicleData.horsePower+",";
			            		}
			            		if(vehicleData.transmission != null) {
			            			standardFeatures = standardFeatures + vehicleData.transmission+",";
			            		}
			            		if(vehicleData.gears != null) {
			            			standardFeatures = standardFeatures + vehicleData.gears+",";
			            		}
			            		if(vehicleData.brakes != null) {
			            			standardFeatures = standardFeatures + vehicleData.brakes+",";
			            		}
			            		if(vehicleData.frontBrakeDiameter != null) {
			            			standardFeatures = standardFeatures + vehicleData.frontBrakeDiameter+",";
			            		}
			            		if(vehicleData.frontBrakeType != null) {
			            			standardFeatures = standardFeatures + vehicleData.frontBrakeType+",";
			            		}
			            		if(vehicleData.rearBrakeDiameter != null) {
			            			standardFeatures = standardFeatures + vehicleData.rearBrakeDiameter+",";
			            		}
			            		if(vehicleData.rearBrakeType != null) {
			            			standardFeatures = standardFeatures + vehicleData.rearBrakeType;
			            		}
			            		
			            		row[12] = standardFeatures;
			            		
			            		if(profile != null) {
			            			row[13] = profile.get(0).dealer_id;
			            			row[14] = profile.get(0).myname;
			            			row[15] = profile.get(0).address;
			            			row[16] = profile.get(0).city;
				            		row[17] = profile.get(0).state;
				            		row[18] = profile.get(0).zip;
				            		row[19] = profile.get(0).email;
				            		row[25] = profile.get(0).latlong;
				            		row[27] = profile.get(0).phone;
				            		row[28] = profile.get(0).web;
			            		} else {
			            			row[13] = "";
			            			row[14] = "";
			            			row[15] = "";
			            			row[16] = "";
				            		row[17] = "";
				            		row[18] = "";
				            		row[19] = "";
				            		row[25] = "";
				            		row[27] = "";
				            		row[28] = "";
			            		}
			            		
			            		row[20] = vehicleData.getPrice().toString();
			            		row[21] = vehicleData.interiorColor;
			            		row[22] = "";
			            		row[23] = "N";
			            		row[24] = vehicleData.engine;
			            		row[26] = "1000";
			            		
			            		writer.writeNext(row);
			            	}
			            	
			            	 writer.close();
			                fis = new FileInputStream(filename);
			                client.storeFile(filename, fis);
			                client.logout();
			                if (fis != null) {
			                    fis.close();
			                }
			    		}   
			    		
			    		
			    		if(siteObj.getName().equals("Carfax")) {
			    			FTPClient client = new FTPClient();
			    	        FileInputStream fis = null;
			    	        client.connect("ftp.vast.com");
			                client.login("gliderllc.com", "73f1vvRw4a");
			                String filename = vm.vin+".csv";
			                CSVWriter writer = new CSVWriter(new FileWriter(filename));
			    	    	List<Vehicle> vehicleList = Vehicle.getAllVehicles(userObj);
			    	    	String []rowHeaders = new String[48];
			    	    	rowHeaders[0] = "Record Id";
			    	    	rowHeaders[1] = "Vin";
			    	    	rowHeaders[2] = "Stock Number";
			    	    	rowHeaders[3] = "Title";
			    	    	rowHeaders[4] = "Url";
			    	    	rowHeaders[5] = "Category";
			    	    	rowHeaders[6] = "Images";
			    	    	rowHeaders[7] = "Address";
			    	    	rowHeaders[8] = "City";
			    	    	rowHeaders[9] = "State";
			    	    	rowHeaders[10] = "Zip";
			    	    	rowHeaders[11] = "Country";
			    	    	rowHeaders[12] = "Seller Type";
			    	    	rowHeaders[13] = "Dealer Name";
			    	    	rowHeaders[14] = "Dealer Id";
			    	    	rowHeaders[15] = "Dealer Email";
			    	    	rowHeaders[16] = "Dealer Phone";
			    	    	rowHeaders[17] = "Delaer Website";
			    	    	rowHeaders[18] = "Dealer Fee";
			    	    	rowHeaders[19] = "Make";
			    	    	rowHeaders[20] = "Model";
			    	    	rowHeaders[21] = "Trim";
			    	    	rowHeaders[22] = "Body";
			    	    	rowHeaders[23] = "Mileage";
			    	    	rowHeaders[24] = "Year";
			    	    	rowHeaders[25] = "Currency";
			    	    	rowHeaders[26] = "Price";
			    	    	rowHeaders[27] = "MSRP";
			    	    	rowHeaders[28] = "Internet Price";
			    	    	rowHeaders[29] = "Selling Price";
			    	    	rowHeaders[30] = "Retail Price";
			    	    	rowHeaders[31] = "Invoice Price";
			    	    	rowHeaders[32] = "Exterior Color";
			    	    	rowHeaders[33] = "Interior Color";
			    	    	rowHeaders[34] = "Interior Material";
			    	    	rowHeaders[35] = "Doors";
			    	    	rowHeaders[36] = "Cylinders";
			    	    	rowHeaders[37] = "Engine Size";
			    	    	rowHeaders[38] = "Drive Type";
			    	    	rowHeaders[39] = "Transmission";
			    	    	rowHeaders[40] = "Cpo";
			    	    	rowHeaders[41] = "Description";
			    	    	rowHeaders[42] = "Standard Features";
			    	    	rowHeaders[43] = "Optional Features";
			    	    	rowHeaders[44] = "Seller Comments";
			    	    	rowHeaders[45] = "Vehicle Condition";
			    	    	rowHeaders[46] = "Listing Time";
			    	    	rowHeaders[47] = "Expire Time";
			    	    	writer.writeNext(rowHeaders);
			    			
			    	    	for(Vehicle vehicleCarfax: vehicleList) {
			    	    		String []row = new String[48];
			    	    		row[0] = "12345678";
			    	    		row[1] = vehicleCarfax.vin;
			    	    		row[2] = vehicleCarfax.stock;
			    	    		row[3] = vehicleCarfax.year+" "+vehicleCarfax.make+" "+vehicleCarfax.model;
			    	    		row[4] = vehicleUrlPath+vehicleCarfax.vin;
			    	    		row[5] = vehicleCarfax.category;
			    	    		List<VehicleImage> vImageList = VehicleImage.getByVin(vehicleCarfax.vin);
			    	    		String str = "";
			    	    		for(VehicleImage img : vImageList) {
			    	    			str = str +imageUrlPath+img.path+"|";
			    	    		}
			    	    		row[6] = str;
			    	    		if(profile != null) {
			    	    			row[7] = profile.get(0).address;
			    	    		} else {
			    	    			row[7] = "";
			    	    		}
			    	    		row[8] = "San Francisco";
			    	    		row[9] = "CA";
			    	    		row[10] = "94105";
			    	    		row[11] = "United States";
			    	    		row[12] = "Dealer";
			    	    		if(profile != null) {
			    	    			row[13] = profile.get(0).myname;
			    	    			row[14] = profile.get(0).dealer_id;
				    	    		row[15] = profile.get(0).email;
				    	    		row[16] = profile.get(0).phone;
				    	    		row[17] = profile.get(0).web;
			    	    		} else {
			    	    			row[13] = "";
			    	    			row[14] = "";
				    	    		row[15] = "";
				    	    		row[16] = "";
				    	    		row[17] = "";
			    	    		}
			    	    		
			    	    		row[18] = "";
			    	    		row[19] = vehicleCarfax.make;
			    	    		row[20] = vehicleCarfax.model;
			    	    		row[21] = vehicleCarfax.trim;
			    	    		row[22] = vehicleCarfax.bodyStyle;
			    	    		row[23] = vehicleCarfax.mileage;
			    	    		row[24] = vehicleCarfax.year;
			    	    		row[25] = "USD";
			    	    		row[26] = vehicleCarfax.price.toString();
			    	    		row[27] = vehicleCarfax.getPrice().toString();
			    	    		row[28] = "";
			    	    		row[29] = "";
			    	    		row[30] = "";
			    	    		row[31] = "";
			    	    		row[32] = vehicleCarfax.exteriorColor;
			    	    		row[33] = vehicleCarfax.interiorColor;
			    	    		row[34] = "fabric";
			    	    		row[35] = vehicleCarfax.doors;
			    	    		row[36] = vehicleCarfax.cylinders;
			    	    		row[37] = vehicleCarfax.engine;
			    	    		row[38] = vehicleCarfax.drivetrain;
			    	    		row[39] = vehicleCarfax.transmission;
			    	    		row[40] = "YES";
			    	    		row[41] = vehicleCarfax.description; //description
			    	    		
			    	    		String standardFeatures = "";
			    	    		if(vehicleCarfax.drivetrain != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.drivetrain+",";
			    	    		}
			    	    		if(vehicleCarfax.fuelType != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.fuelType+",";
			    	    		}
			    	    		if(vehicleCarfax.fuelTank != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.fuelTank+",";
			    	    		}
			    	    		if(vehicleCarfax.headlights != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.headlights+",";
			    	    		}
			    	    		if(vehicleCarfax.mirrors != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.mirrors+",";
			    	    		}
			    	    		if(vehicleCarfax.roof != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.roof+",";
			    	    		}
			    	    		if(vehicleCarfax.acceleration != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.acceleration+",";
			    	    		}
			    	    		if(vehicleCarfax.standardSeating != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.standardSeating+",";
			    	    		}
			    	    		if(vehicleCarfax.engine != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.engine+",";
			    	    		}
			    	    		if(vehicleCarfax.camType != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.camType+",";
			    	    		}
			    	    		if(vehicleCarfax.valves != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.valves+",";
			    	    		}
			    	    		if(vehicleCarfax.cylinders != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.cylinders+",";
			    	    		}
			    	    		if(vehicleCarfax.fuelQuality != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.fuelQuality+",";
			    	    		}
			    	    		if(vehicleCarfax.horsePower != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.horsePower+",";
			    	    		}
			    	    		if(vehicleCarfax.transmission != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.transmission+",";
			    	    		}
			    	    		if(vehicleCarfax.gears != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.gears+",";
			    	    		}
			    	    		if(vehicleCarfax.brakes != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.brakes+",";
			    	    		}
			    	    		if(vehicleCarfax.frontBrakeDiameter != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.frontBrakeDiameter+",";
			    	    		}
			    	    		if(vehicleCarfax.frontBrakeType != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.frontBrakeType+",";
			    	    		}
			    	    		if(vehicleCarfax.rearBrakeDiameter != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.rearBrakeDiameter+",";
			    	    		}
			    	    		if(vehicleCarfax.rearBrakeType != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.rearBrakeType;
			    	    		}
			    	    		row[42] = standardFeatures;
			    	    		
			    	    		
			    	    		String standardOptions = "";
			    	    		
			    	    		if(vehicleCarfax.activeHeadRestrains != null) {
			    	    			standardOptions = standardOptions + vehicleCarfax.activeHeadRestrains+",";
			    	    		}
			    	    		if(vehicleCarfax.bodySideReinforcements != null) {
			    	    			standardOptions = standardOptions + vehicleCarfax.bodySideReinforcements+",";
			    	    		}
			    	    		if(vehicleCarfax.crumpleZones != null) {
			    	    			standardOptions = standardOptions + vehicleCarfax.crumpleZones+",";
			    	    		}
			    	    		if(vehicleCarfax.impactAbsorbingBumpers != null) {
			    	    			standardOptions = standardOptions + vehicleCarfax.impactAbsorbingBumpers+",";
			    	    		}
			    	    		if(vehicleCarfax.impactSensor != null) {
			    	    			standardOptions = standardOptions + vehicleCarfax.impactSensor+",";
			    	    		}
			    	    		if(vehicleCarfax.parkingSensors != null) {
			    	    			standardOptions = standardOptions + vehicleCarfax.parkingSensors+",";
			    	    		}
			    	    		if(vehicleCarfax.seatbelts != null) {
			    	    			standardOptions = standardOptions + vehicleCarfax.seatbelts+",";
			    	    		}
			    	    		if(vehicleCarfax.interiorColor != null) {
			    	    			standardOptions = standardOptions + vehicleCarfax.interiorColor+",";
			    	    		}
			    	    		if(vehicleCarfax.powerOutlet != null) {
			    	    			standardOptions = standardOptions + vehicleCarfax.powerOutlet+",";
			    	    		}
			    	    		if(vehicleCarfax.powerSteering != null) {
			    	    			standardOptions = standardOptions + vehicleCarfax.powerSteering+",";
			    	    		}
			    	    		if(vehicleCarfax.rearViewCamera != null) {
			    	    			standardOptions = standardOptions + vehicleCarfax.rearViewCamera+",";
			    	    		}
			    	    		if(vehicleCarfax.rearViewMonitor != null) {
			    	    			standardOptions = standardOptions + vehicleCarfax.rearViewMonitor+",";
			    	    		}
			    	    		if(vehicleCarfax.remoteTrunkRelease != null) {
			    	    			standardOptions = standardOptions + vehicleCarfax.remoteTrunkRelease+",";
			    	    		}
			    	    		if(vehicleCarfax.steeringWheel != null) {
			    	    			standardOptions = standardOptions + vehicleCarfax.steeringWheel+",";
			    	    		}
			    	    		if(vehicleCarfax.steeringWheelControls != null) {
			    	    			standardOptions = standardOptions + vehicleCarfax.steeringWheelControls;
			    	    		}
			    	    		
			    	    		row[43] = standardOptions;
			    	    		row[44] = "";
			    	    		row[45] = "Used";
			    	    		row[46] = "2012-09-16-11:00:00";
			    	    		row[47] = "2012-10-16-11:00:00";
			    	    		
			    	    		writer.writeNext(row);
			    	    	}
			    	    	
			    	    	 writer.close();
			    	    	 fis = new FileInputStream(filename);
				                client.storeFile(filename, fis);
				                client.logout();
				                if (fis != null) {
				                    fis.close();
				                }
			    		}
			    		
			    		
			    	}
		    	}*/
		    	
		    	return ok(Json.toJson(vehicle.id));
	    	}	
	    }
	   
	   
	   private static void saveCustomInventoryData(Long InventoryId,SpecificationVM vm,MultipartFormData bodys) {
	       	
	       	for(KeyValueDataVM custom:vm.customData){
	       		
	       		CustomizationInventory cDataValue = CustomizationInventory.findByKeyAndLeadId(custom.key,InventoryId);
	       		if(cDataValue == null){
	       			CustomizationInventory cValue = new CustomizationInventory();
	       			cValue.keyValue = custom.key;
	       			cValue.value = custom.value;
	       			cValue.InventoryId = InventoryId;
	       			cValue.displayGrid = custom.displayGrid;
	       			cValue.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	       			cValue.save();
	       			
	       		}else{
	       			cDataValue.setKeyValue(custom.key);
	       			cDataValue.setValue(custom.value);
	       			cDataValue.setDisplayGrid(custom.displayGrid);
	       			cDataValue.update();
	       		}
	   			
	   		}
	       }
	
	   
	   public static void sendEmailToBrandFollowers(String brand) {
	    	
	    	AuthUser user = (AuthUser) getLocalUser();
	    	List<FollowBrand> brandFollowers = FollowBrand.getAllBrandFollowersName(brand);
	    	for(FollowBrand row: brandFollowers) {
	    		AuthUser logoUser = getLocalUser();	
	    		//AuthUser logoUser = AuthUser.findById(Integer.getInteger(session("USER_KEY")));
		    	    SiteLogo logo = SiteLogo.findByLocation(Long.valueOf(session("USER_LOCATION")));  //findByUser(logoUser);
			    	String email = row.email;
			    	/*List<FollowBrand> brandList = FollowBrand.getBrandsByEmail(email);
			    	for(FollowBrand brandObj: brandList) {*/
			    		
			    		List<Vehicle> vehicleList = Vehicle.getVehiclesByMake(row.brand, Location.findById(Long.valueOf(session("USER_LOCATION"))));
			    		List<VehicleVM> vehicleVMList = new ArrayList<>();
			    		
			    		for(Vehicle vehicle: vehicleList) {
			    			VehicleImage defaultImage = VehicleImage.getDefaultImage(vehicle.vin);
			    			VehicleVM vm = new VehicleVM();
			    			vm.vin = vehicle.vin;
			    			vm.make = vehicle.make;
			    			vm.model = vehicle.model;
			    			vm.price = "$"+vehicle.price;
			    			if(defaultImage != null) {
			    				vm.imageUrl = defaultImage.thumbPath;
			    			}
			    			vehicleVMList.add(vm);
			    		}
			    		
			    		 EmailDetails details=EmailDetails.findByLocation(Long.valueOf(session("USER_LOCATION")));
			 			String emailName=details.name;
			 			String port=details.port;
			 			String gmail=details.host;
			 			final	String emailUser=details.username;
			 			final	String emailPass=details.passward;
			    		
				    	Properties props = new Properties();
						props.put("mail.smtp.auth", "true");
						props.put("mail.smtp.host", gmail);
						props.put("mail.smtp.port", port);
						props.put("mail.smtp.starttls.enable", "true");
						Session session = Session.getInstance(props, new javax.mail.Authenticator() {
							protected PasswordAuthentication getPasswordAuthentication() {
								return new PasswordAuthentication(emailUser, emailPass);
							}
						});
						
						try
						{
							Message message = new MimeMessage(session);
				    		try{
							message.setFrom(new InternetAddress(emailUser,emailName));
				    		}catch(UnsupportedEncodingException e){
				    			e.printStackTrace();
				    		}
							message.setRecipients(Message.RecipientType.TO,
							InternetAddress.parse(email));
							message.setSubject("CAR BRAND INVENTORY UPDATE");
							Multipart multipart = new MimeMultipart();
							BodyPart messageBodyPart = new MimeBodyPart();
							messageBodyPart = new MimeBodyPart();
							
							VelocityEngine ve = new VelocityEngine();
							ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
							ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
							ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
							ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
							ve.init();
						
							
					        Template t = ve.getTemplate("/public/emailTemplate/brandFollowersTemplate.vm"); 
					        VelocityContext context = new VelocityContext();
					        
					        context.put("hostnameUrl", imageUrlPath);
					        context.put("siteLogo", logo.logoImagePath);
					        
					        context.put("name", row.name);
					        context.put("brand", row.brand);
					        context.put("vehicleList", vehicleVMList);
					       
					        
					        StringWriter writer = new StringWriter();
					        t.merge( context, writer );
					        String content = writer.toString(); 
							
							messageBodyPart.setContent(content, "text/html");
							multipart.addBodyPart(messageBodyPart);
							message.setContent(multipart);
							Transport.send(message);
							
						}
						catch (Exception e)
						{
							e.printStackTrace();
						} 
			    	//}
	    	}
	    }
	   
	    public static Result saveVehiclePdf(Long id) throws IOException {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		Identity user = getLocalUser();
		    	AuthUser userObj = (AuthUser)user;
	    		MultipartFormData body = request().body().asMultipartFormData();
	    		Vehicle vehicle = Vehicle.findById(id);
	    		if(vehicle != null) {
	    	       	
	    	       	if(body != null){
	    	       		FilePart picture = body.getFile("file0");
	    	       		if (picture != null) {
	    	       			String fileName = picture.getFilename().replaceAll("[-+^:,() ]","");
	    	       			File file = picture.getFile();
	    	       			try {
	    	       				File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+vehicle.vin+"-"+userObj.id+File.separator+"customization_data"+File.separator+fileName);
	    	       	    	    if(!fdir.exists()) {
	    	       	    	    	fdir.mkdir();
	    	       	    	    }
	    	       	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+vehicle.vin+"-"+userObj.id+File.separator+"customization_data"+File.separator+fileName;
	    	       	    	    FileUtils.moveFile(file, new File(filePath));
	    	       	    	    
	    	       	    	 CustomizationInventory cDataValueEdit = CustomizationInventory.findByKeyAndLeadId("fileupload",id);
	    	       	    		if(cDataValueEdit == null){
	    	       	    			CustomizationInventory cValue = new CustomizationInventory();
	    	       	    				cValue.keyValue = "fileupload";
	    	       	    				cValue.value = session("USER_LOCATION")+File.separator+vehicle.vin+"-"+userObj.id+File.separator+"customization_data"+File.separator+fileName;
	    	       	    				cValue.InventoryId = id;
	    	       	    				cValue.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	    	       	    				cValue.save();
	    	       	    			
	    	       	    		}else{
	    	       	    			//cDataValue.setKeyValue(custom.key);
	    	       	    			cDataValueEdit.setValue(session("USER_LOCATION")+File.separator+vehicle.vin+"-"+userObj.id+File.separator+"customization_data"+File.separator+fileName);
	    	       	    			cDataValueEdit.update();
	    	       	    		}
	    	       				
	    	       			} catch (Exception e) {
	    	   					e.printStackTrace();
	    	   				}
	    	       		}
	    	       	}
	    			
	    		/*	if(body != null){
			    		FilePart picture = body.getFile("file0");
			    		if (picture != null) {
			    			String fileName = picture.getFilename().replaceAll("[-+^:,() ]","");
			    			File file = picture.getFile();
			    			try {
			    				File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+vehicle.vin+"-"+userObj.id+File.separator+"PDF_brochure");
			    	    	    if(!fdir.exists()) {
			    	    	    	fdir.mkdir();
			    	    	    }
			    	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+vehicle.vin+"-"+userObj.id+File.separator+"PDF_brochure"+fileName;
			    	    	    FileUtils.moveFile(file, new File(filePath));
			    	    	    vehicle.setPdfBrochureName(fileName);
			    	    	    vehicle.setPdfBrochurePath(session("USER_LOCATION")+File.separator+vehicle.vin+"-"+userObj.id+File.separator+"PDF_brochure"+fileName);
			    	    	    vehicle.update();
			    			} catch (Exception e) {
								e.printStackTrace();
							}
			    		}
			    	}*/
	    		}
	    	}
	    	return ok();
	    }
	
	  public static AuthUser getLocalUser() {
	    	String id = session("USER_KEY");
	    	AuthUser user = AuthUser.find.byId(Integer.parseInt(id));
			return user;
		}

	
}