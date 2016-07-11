package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
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

import models.AddCollection;
import models.AuthUser;
import models.CustomizationDataValue;
import models.CustomizationInventory;
import models.EmailDetails;
import models.FollowBrand;
import models.Inventory;
import models.Location;
import models.MyProfile;
import models.PhotographerHoursOfOperation;
import models.PriceAlert;
import models.PriceChange;
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
import viewmodel.InventoryVM;
import viewmodel.KeyValueDataVM;
import viewmodel.LeadVM;
import viewmodel.PinVM;
import viewmodel.PortalNameVM;
import viewmodel.RequestInfoVM;
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
		    	Form<InventoryVM> form = DynamicForm.form(InventoryVM.class).bindFromRequest();
		    	InventoryVM vm = form.get();
		    	
		    	Inventory inventoryObj = Inventory.getByProductId(vm.productId);
		    	Inventory inventory = new Inventory();
		    	if(inventoryObj == null) {
			    	
		    		inventory.productId = vm.productId;
			    	inventory.title = vm.title;
			    	inventory.description = vm.description;
			    	inventory.cost = vm.cost;
			    	inventory.price = vm.price;
			    	inventory.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    		inventory.collection = AddCollection.findById(vm.collection);
		    		inventory.save();
		    	}
		    	
		    	saveCustomInventoryData(inventory.id,vm);
		    	
		    	
		    	return ok(Json.toJson(inventory.id));
	    	}	
	    }
	   
	   
	   private static void saveCustomInventoryData(Long InventoryId,InventoryVM vm) {
	       	
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
	

	    public static Result getVehicleById(Long id) {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	Inventory inventory = Inventory.findById(id);
				
		    	InventoryVM inventoryVm = new InventoryVM();
				inventoryVm.id = inventory.getId();
				inventoryVm.title = inventory.getTitle();
				inventoryVm.description = inventory.getDescription();
				inventoryVm.cost = inventory.getCost();
				inventoryVm.price = inventory.getPrice();
				inventoryVm.productId = inventory.getProductId();
				findCustomeInventoryData(inventoryVm.id,inventoryVm);
				
				
				
		    	return ok(Json.toJson(inventoryVm));
	    	}	
	    }
	    
	    
	    public static void findCustomeInventoryData(Long id,InventoryVM inventoryvm){
	    	List<CustomizationInventory> custData = CustomizationInventory.findByIdList(id);
	    	List<KeyValueDataVM> keyValueList = new ArrayList<>();
	    	Map<String, String> mapCar = new HashMap<String, String>();
	    	for(CustomizationInventory custD:custData){
	    		mapCar.put(custD.keyValue, custD.value);
	    		//if(custD.displayGrid.equals("true")){
	    			//if(keyValueList.size() == 0){
	    				KeyValueDataVM keyValue = new KeyValueDataVM();
	            		keyValue.key = custD.keyValue;
	            		keyValue.value = custD.value;
	            		keyValue.displayGrid = custD.displayGrid;
	            		keyValueList.add(keyValue);
	    			//}else{
	            		/*for(KeyValueDataVM ks:keyValueList){
	    					if(!ks.equals(custD.keyValue)){
	    						KeyValueDataVM keyValue = new KeyValueDataVM();
	    	            		keyValue.key = custD.keyValue;
	    	            		keyValue.value = custD.value;
	    	            		keyValue.displayGrid = custD.displayGrid;
	    	            		keyValueList.add(keyValue);
	    					}
	    				}
	    			}*/
	    			
	    		//}
	    		
	    	}
	    	inventoryvm.customData = keyValueList;
	    	inventoryvm.customMapData = mapCar;
	    }
	    
	    public static Result updateVehicleById() throws SocketException, IOException{
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	
	    		Form<InventoryVM> form = DynamicForm.form(InventoryVM.class).bindFromRequest();
		    	InventoryVM vm = form.get();
		    	Inventory inventory = Inventory.findById(vm.id);
		    	if(inventory != null) {
		    		inventory.setProductId(vm.productId);
		    		inventory.setCost(vm.cost);
		    		inventory.setPrice(vm.price);
		    		inventory.setTitle(vm.title);
		    		inventory.setDescription(vm.description);
		    		inventory.setCollection(AddCollection.findById(vm.collection));
		    		inventory.update();
			    	
			    	saveCustomInventoryData(inventory.id,vm);
			    
			    	
		    	}	
		    	return ok();
	    	}	
	    }  
	    
	  public static AuthUser getLocalUser() {
	    	String id = session("USER_KEY");
	    	AuthUser user = AuthUser.find.byId(Integer.parseInt(id));
			return user;
		}

	
}