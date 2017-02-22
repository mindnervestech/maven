package controllers;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.gargoylesoftware.htmlunit.javascript.host.Console;

import controllers.MailchipControllers.MailIntegrationServices;

import models.AuthUser;
import models.Contacts;
import models.CustomizationCrm;
import models.CustomizationDataValue;
import models.GroupTable;
import models.Location;
import models.MailchimpList;
import models.MailchimpSchedular;
import models.Permission;
import models.PhotographerHoursOfOperation;
import models.RequestMoreInfo;
import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import viewmodel.ContactsVM;
import viewmodel.GroupVM;
import viewmodel.HoursOperation;
import viewmodel.KeyValueDataVM;
import viewmodel.OldNewContactVM;
import viewmodel.PortalNameVM;
import viewmodel.UserVM;
import views.html.home;

public class CrmController extends Controller {

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

	
	
	
	public static Result saveContactsData() {
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		Date date = new Date();
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    		AuthUser userObj = (AuthUser) getLocalUser();
    		MultipartFormData body = request().body().asMultipartFormData();
    		Form<ContactsVM> form = DynamicForm.form(ContactsVM.class).bindFromRequest();
    		ContactsVM vm = form.get();
    		Contacts obj = Contacts.findByEmail(vm.email);
    		String msg = "";
    		if(obj == null) {
    		Contacts contacts = new Contacts();
    		 contacts.setFirstName(vm.firstName);
 		    contacts.setCompanyName(vm.companyName);
 		    contacts.setEmail(vm.email);
 		    contacts.setPhone(vm.phone);
 		    contacts.setWebsite(vm.website);
 		    contacts.setAllAddresses(vm.allAddresses);
 		   	contacts.setTitle(vm.title);
 		   	contacts.setSalutation(vm.salutation);
 		   	contacts.setSuffix(vm.suffix);
 		   	contacts.setCity(vm.city);
 		   	contacts.setState(vm.state);
 		   	contacts.setZip(vm.zip);
 		   	contacts.setAllPhone(vm.allPhone);
 		   	contacts.setAllEmail(vm.allEmail);
 		   	contacts.setTitle(vm.title);
 		   	contacts.setBirthday(vm.birthday);
 		   	contacts.setBackgroundInfo(vm.backgroundInfo);
 		   	contacts.setIndustry(vm.industry);
 		   	contacts.setRelationships(vm.relationships);
 		   	contacts.setNotes(vm.notes);
 		   	if(vm.assignedTo == null){
 		   		contacts.setAssignedTo(userObj.id.toString());
 		   	}else{
 		   		contacts.setAssignedTo(vm.assignedTo);
 		   	}
 		   	contacts.setCreationDate(df.format(date));
 		    contacts.setCampaignSource(vm.campaignSource);
 		    contacts.setPriority(vm.priority);
 		    GroupTable gr = null;
 		    if(vm.groups != null)
 		    	gr = GroupTable.findByName(vm.groups.name);
 		    contacts.setGroups(gr);
 		    contacts.setWorkEmail(vm.workEmail);
 		    contacts.setWorkEmail1(vm.workEmail1);
 		    contacts.setWorkPhone(vm.workPhone);
 		    contacts.setWorkPhone1(vm.workPhone1);
 		    contacts.setEmail1(vm.email1);
 		    contacts.setPhone1(vm.phone1);
 		    //contacts.userId = userObj;
 		    contacts.setUser(userObj.id);
 		    contacts.setLocations(userObj.location);
 		    contacts.setType("Offline");
 		    contacts.setEnthicity(vm.enthicity);
 		    contacts.setCustZipCode(vm.zip);
 		   contacts.setLastName(vm.lastName);
	 		 if(vm.newsletter == true) {
	 			contacts.setNewsLetter(1);
	  		}
	  		if(vm.newsletter == false) {
	  			contacts.setNewsLetter(0);
	  		}
 		   //contacts.setNewsLetter(1);
    			contacts.save();
    			MailchimpSchedular mSchedular = MailchimpSchedular.findByLocations(16L);
    			if(mSchedular != null){
    				if(mSchedular.synchronizeContact){
    					saveCustomCrmData(contacts.contactId,vm,body);
    	    			MailIntegrationServices objMail = new MailIntegrationServices();
    	    			msg = objMail.addUser(vm.lastName, vm.firstName, vm.email);
    				}
    			}
    			
    			
    			
    		} else {
    			msg = "Email already exists";
    		}
    		return ok(msg);
    	}
	}
	
	public static Result updateContactsData() {
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		String msg = "";
    		Date date = new Date();
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    		MultipartFormData body = request().body().asMultipartFormData();
    		Form<ContactsVM> form = DynamicForm.form(ContactsVM.class).bindFromRequest();
    		ContactsVM vm = form.get();
    		Contacts contacts = Contacts.findById(vm.contactId);
    			contacts.setType(vm.type);
    			contacts.setSalutation(vm.salutation);
    			contacts.setFirstName(vm.firstName);
    			contacts.setMiddleName(vm.middleName);
    			contacts.setLastName(vm.lastName);
    			contacts.setSuffix(vm.suffix);
    			contacts.setCompanyName(vm.companyName);
    			contacts.setEmail(vm.email);
    			contacts.setPhone(vm.phone);
    			contacts.setStreet(vm.street);
    			contacts.setCity(vm.city);
    			contacts.setState(vm.state);
    			contacts.setZip(vm.zip);
    			contacts.setLastEditedDate(df.format(date));
    			contacts.setCustZipCode(vm.zip);
    			contacts.setEnthicity(vm.enthicity);
    			contacts.setCountry(vm.country);
    			contacts.setAllEmail(vm.allEmail);
    			contacts.setAllPhone(vm.allPhone);
    			contacts.setWebsite(vm.website);
    			contacts.setAllAddresses(vm.allAddresses);
    			contacts.setTitle(vm.title);
    			contacts.setBirthday(vm.birthday);
    			contacts.setBackgroundInfo(vm.backgroundInfo);
    			contacts.setIndustry(vm.industry);
    			contacts.setNumberOfEmployees(vm.numberOfEmployees);
    			contacts.setCreationDate(vm.creationDate);
    			contacts.setAssignedTo(vm.assignedTo);
    			contacts.setCampaignSource(vm.campaignSource);
    			contacts.setPriority(vm.priority);
    			contacts.setGroups(vm.groups);
    			contacts.setRelationships(vm.relationships);
    			contacts.setNotes(vm.notes);
    			contacts.update();
    			saveCustomCrmData(contacts.contactId,vm,body);
    			MailchimpSchedular mSchedular = MailchimpSchedular.findByLocations(16L);
    			if(mSchedular != null){
    				if(mSchedular.synchronizeContact){
    					MailIntegrationServices objMail = new MailIntegrationServices();
    	    			msg = objMail.unsubscribe( vm.lastName,vm.firstName, vm.email);
    				}
    			}
    		return ok(msg);
    	}
	}
	
	/*private static void saveCustomCrmData(Long InventoryId,ContactsVM vm) {
       	
       	for(KeyValueDataVM custom:vm.customData){
       		
       		CustomizationCrm cDataValue = CustomizationCrm.findByKeyAndLeadId(custom.key,InventoryId);
       		if(cDataValue == null){
       			CustomizationCrm cValue = new CustomizationCrm();
       			cValue.keyValue = custom.key;
       			cValue.value = custom.value;
       			cValue.crmId = InventoryId;
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
       }*/
	
	 private static void saveCustomCrmData(Long crmId,ContactsVM vm,MultipartFormData bodys) {
	    	String formName = "";
	    	for(KeyValueDataVM custom:vm.customData){
	    		
	    		CustomizationCrm cDataValue = CustomizationCrm.findByKeyAndLeadId(custom.key,crmId);
	    		if(cDataValue == null){
	    			CustomizationCrm cValue = new CustomizationCrm();
	    			cValue.fieldId = custom.fieldId;
	    			cValue.keyValue = custom.key;
	    			if(custom.component.equals("fileuploaders")){
	    				String fileN = custom.value.replaceAll("[-+^:,() ]","");
	    				cValue.value = File.separator+session("USER_LOCATION")+File.separator+"CRM"+File.separator+crmId+File.separator+fileN; 
	    			}else{
	    				cValue.value = custom.value;
	    			}
	    			
	    			cValue.crmId = crmId;
	    			cValue.formName = custom.formName;
	    			if(!cValue.formName.equals("Create New Lead")){
	    				formName = cValue.formName;
	    			}
	    				    			
	    			if(custom.displayGrid == null){
	    				cValue.displayGrid = "false";
	    			}else{
	    				cValue.displayGrid = custom.displayGrid;
	    			}
	    			
	    			
	    			cValue.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	    			cValue.save();
	    			
	    		}else{
	    			cDataValue.setKeyValue(custom.key);
	    			
	    			if(custom.component.equals("fileuploaders")){
	    				String fileN = custom.value.replaceAll("[-+^:,() ]","");
	    				cDataValue.setValue(File.separator+session("USER_LOCATION")+File.separator+"CRM"+File.separator+crmId+File.separator+fileN); 
	    			}else{
	    				cDataValue.setValue(custom.value);
	    			}
	    			
	    			cDataValue.setFormName(custom.formName);
	    			formName = custom.formName;
	    			
	    			if(custom.displayGrid == null){
	    				cDataValue.setDisplayGrid("false");
	    			}else{
	    				cDataValue.setDisplayGrid(custom.displayGrid);
	    			}
					cDataValue.setFormName(custom.formName);
	    			cDataValue.update();
	    		}
				
			}
	    	
	    	
	    	if(bodys != null){
	    		FilePart picture = bodys.getFile("file0");
	    		if (picture != null) {
	    			String fileName = picture.getFilename().replaceAll("[-+^:,() ]","");
	    			File file = picture.getFile();
	    			try {
	    				File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+"CRM"+File.separator+crmId+File.separator+fileName);
	    	    	    if(!fdir.exists()) {
	    	    	    	fdir.mkdir();
	    	    	    }
	    	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"CRM"+File.separator+crmId+File.separator+fileName;
	    	    	    FileUtils.moveFile(file, new File(filePath));
	    	    	    
	    			
	    			} catch (Exception e) {
						e.printStackTrace();
					}
	    		}
	    	}
	    	
			
	    }
	 
	public static Result callList() {
		
		MailIntegrationServices obj = new MailIntegrationServices();
		obj.getLists(Long.valueOf(session("USER_LOCATION")));
		//getLists();
		return ok();
	}
	
	public static Result checkMailChim(){
		MailIntegrationServices objMail = new MailIntegrationServices();
		return ok(objMail.checkMailChim());
	}
	
	 public static Result getMailChimpList(){

			Date curr = new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String cDate = df.format(curr);
			Date cuDate = null;
			try {
				cuDate = df.parse(cDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(cuDate);
			MailchimpSchedular mScheduler = MailchimpSchedular.findByLocations(16L);
			if(mScheduler != null){
				if(mScheduler.schedularTime.equals("Daily")){
					cal1.add(Calendar.DATE, -1);
				}else if(mScheduler.schedularTime.equals("Weekly")){
					cal1.add(Calendar.DATE, -7);
				}else if(mScheduler.schedularTime.equals("Monthly")){
					cal1.add(Calendar.MONTH, -1);
				}
			}	
			
			cuDate = cal1.getTime();
			
			
			List<Contacts> contacts = Contacts.findByDateWise(cuDate);
			for(Contacts cont:contacts){
				 MailIntegrationServices objMail = new MailIntegrationServices();
					//obj.getLists(Long.valueOf(session("USER_LOCATION")));
					objMail.addUser(cont.firstName, cont.lastName, cont.email);
			}
		 
		
		 
		 return ok();
	 }
	 
	 public static Result importContacts(){
		 Form<OldNewContactVM> form = DynamicForm.form(OldNewContactVM.class).bindFromRequest();
		 OldNewContactVM vm = form.get();
		 if(vm.newContact != null){
			 for(ContactsVM cVm:vm.newContact){
				 Contacts contacts = Contacts.findByEmail(cVm.email);
				 if(contacts == null){
					 saveContact(cVm);
				 }else{
					 updateContact(contacts,cVm);
				 }
			 }
		 }
		
		 if(vm.oldContact != null){
			 for(ContactsVM cVm:vm.oldContact){
				 Contacts contacts = Contacts.findByEmail(cVm.email);
				 if(contacts == null){
					 saveContact(cVm);
				 }else{
					 updateContact(contacts,cVm);
				 }
			 }
		 }
		 
		 
		 return ok();
	 }
	 
	 
	
	 private static void saveContact(ContactsVM vm) {
		 AuthUser userObj = (AuthUser) getLocalUser();	
		 Date date = new Date();
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		 Contacts contacts = new Contacts();
		    contacts.setFirstName(vm.firstName);
		    contacts.setCompanyName(vm.companyName);
		    contacts.setEmail(vm.email);
		    contacts.setPhone(vm.phone);
		    contacts.setWebsite(vm.website);
		    contacts.setAllAddresses(vm.allAddresses);
		   	contacts.setTitle(vm.title);
		   if(vm.assignedTo == null){
		   		contacts.setAssignedTo(userObj.id.toString());
		   	}else{
		   		contacts.setAssignedTo(userObj.id.toString());
		   	}
		    contacts.setCampaignSource(vm.campaignSource);
		    contacts.setPriority(vm.priority);
		    contacts.setCreationDate(df.format(date));
		    GroupTable gr = null;
		    if(vm.groups != null)
		    	gr = GroupTable.findByName(vm.groups.name);
		    contacts.setGroups(gr);
		    contacts.setWorkEmail(vm.workEmail);
		    contacts.setWorkEmail1(vm.workEmail1);
		    contacts.setWorkPhone(vm.workPhone);
		    contacts.setWorkPhone1(vm.workPhone1);
		    contacts.setEmail1(vm.email1);
		    contacts.setPhone1(vm.phone1);
		    //contacts.userId = userObj;
		    //contacts.setUser(userObj.id);
		    //contacts.setLocations(userObj.location);
		    contacts.setType("Offline");
		    contacts.setEnthicity(vm.enthicity);
		    contacts.setCustZipCode(vm.zip);
		    contacts.setLastName(vm.lastName);
		    contacts.newsLetter = 0;
		    contacts.save();
		
	}
	 
	 private static void updateContact(Contacts contacts ,ContactsVM vm) {
		 Date date = new Date();
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		 AuthUser userObj = (AuthUser) getLocalUser();  
		 contacts.setFirstName(vm.firstName);
		    contacts.setCompanyName(vm.companyName);
		    contacts.setEmail(vm.email);
		    contacts.setPhone(vm.phone);
		    contacts.setWebsite(vm.website);
		    contacts.setAllAddresses(vm.allAddresses);
		   	contacts.setTitle(vm.title);
		   if(vm.assignedTo == null){
		   		contacts.setAssignedTo(userObj.id.toString());
		   	}else{
		   		contacts.setAssignedTo(userObj.id.toString());
		   	}
		    contacts.setCampaignSource(vm.campaignSource);
		    contacts.setPriority(vm.priority);
		    GroupTable gr = null;
		    if(vm.groups != null)
		    	gr = GroupTable.findByName(vm.groups.name);
		    if(gr != null){
		    	contacts.setGroups(gr);
		    }
		    contacts.setLastEditedDate(df.format(date));
		    contacts.setWorkEmail(vm.workEmail);
		    contacts.setWorkEmail1(vm.workEmail1);
		    contacts.setWorkPhone(vm.workPhone);
		    contacts.setWorkPhone1(vm.workPhone1);
		    contacts.setEmail1(vm.email1);
		    contacts.setPhone1(vm.phone1);
		    //contacts.userId = userObj;
		    //contacts.setUser(userObj.id);
		    //contacts.setLocations(userObj.location);
		    contacts.setType("Offline");
		    contacts.setEnthicity(vm.enthicity);
		    contacts.setCustZipCode(vm.zip);
		    contacts.setLastName(vm.lastName);
		    
		    contacts.update();
		
	}
	 
	 private static void createColumnName(List<String> columnName) {
			// TODO Auto-generated method stub
		 columnName.add("Type");
		 columnName.add("Salutation");
		 columnName.add("First Name");
		 columnName.add("Middle Name");
		 columnName.add("Last Name");
		 columnName.add("Suffix");
		 columnName.add("Company");
		 columnName.add("Primary Email");
		 columnName.add("Primary Phone");
		 columnName.add("Primary Street");
		 columnName.add("Primary City");
		 columnName.add("Primary State");
		 columnName.add("Primary Zip");
		 columnName.add("Primary Country");
		 columnName.add("All Email");
		 columnName.add("All Phone");
		 columnName.add("Website");
		 columnName.add("All Addresses"); 	
		 columnName.add("Title");
		 columnName.add("Birthday");
		 columnName.add("Background Info");
		 columnName.add("Industry");
		 columnName.add("# of Employees");
		 columnName.add("Creation Date");
		 columnName.add("Last Edited Date");
		 columnName.add("Assigned To");
		 columnName.add("Campaign Source");
		 columnName.add("Priority");
		 columnName.add("Groups");
		 columnName.add("Relationships");
		 columnName.add("Notes");
		 
		 
	  }
	 
	 
	public static Result uploadContactsFile() throws Exception {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	MultipartFormData body = request().body().asMultipartFormData();
		    	List<ContactsVM> cListNew = new ArrayList<>();
		    	List<ContactsVM> cListOld = new ArrayList<>();
		    	Map<String,Object> map = new HashMap<String,Object>();
		    	AuthUser userObj = (AuthUser) getLocalUser();
		    	List<String> columnName = new ArrayList<>();
		    	List<String> otherColumnName = new ArrayList<>();
		    	
		    	createColumnName(columnName);
		    	FilePart fileData = body.getFile("file0");
		    	  if (fileData != null) {
		    	    String fileName = fileData.getFilename();
		    	    File file = fileData.getFile();
		    	    com.opencsv.CSVReader reader = new com.opencsv.CSVReader(new FileReader(file), ',', '"','\0', 0);
		    	    String[] firstRow = null; 
		    	    List<String[]> allRows = reader.readAll();
		    	    int notFirstTime = 0;
		    	     for(String[] row : allRows){
		    	    	
		    	    	 if(notFirstTime == 0){
		    	    		 firstRow = row;
		    	    		 notFirstTime++;
		    	    	 }else{
		    	    		 List<KeyValueDataVM> keyValue = new ArrayList<>();
		    	    		
		    	    		 ContactsVM contact = new ContactsVM();
			    	    	 int flag = 0;
				    	    	 Contacts contactObj = null;
				    	    	 if(row.length >= 9) {
					    	    	 if( row[8] != null && !row[8].isEmpty()) {
					    	    		 if(!row[8].trim().equals("")) {
					    	    			 contactObj = Contacts.findByEmail(row[8]);
					    	    		 }
					    	    	 }else{
				    	    			 flag = 1;
				    	    		 }
				    	    	 }
				    	    	 if(contactObj == null) {
					    	    	for(int i=0;i< row.length;i++){
					    	    		 if(firstRow[i].equalsIgnoreCase("ContactId")){
					    	    			// contact.contactId = Long.parseLong(row[i]);
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Type")){
					    	    			 contact.type = row[i];
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Salutation")){
					    	    			 contact.salutation = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("First Name")){
					    	    			 contact.firstName = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Middle Name")){
					    	    			 contact.middleName = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Last Name")){
					    	    			 contact.lastName = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Suffix")){
					    	    			 contact.suffix = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Company Name")){
					    	    			 contact.companyName = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Primary Email")){
					    	    			 contact.email = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Primary Phone")){
					    	    			 contact.phone = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Primary Street")){
					    	    			 contact.street = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Primary City")){
					    	    			 contact.city = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Primary State")){
					    	    			 contact.state = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Primary Zip")){
					    	    			 contact.zip = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Primary Country")){
					    	    			 contact.country = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("All Email")){
					    	    			 contact.allEmail = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("All Phone")){
					    	    			 contact.allPhone = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Website")){
					    	    			 contact.website = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("All Addresses")){
					    	    			 contact.allAddresses = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Title")){
					    	    			 contact.title = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Birthday")){
					    	    			 contact.birthday = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Background Info")){
					    	    			 contact.backgroundInfo = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Industry")){
					    	    			 contact.industry = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("# of Employees")){
					    	    			 contact.firstName = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Creation Date")){
					    	    			 contact.creationDate = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Assigned To")){
					    	    			 contact.assignedTo = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Campaign Source")){
					    	    			 contact.campaignSource = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Last Edited Date")){
					    	    			 contact.lastEditedDate = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Priority")){
					    	    			 contact.priority = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Groups")){
					    	    			 contact.groupsName = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Relationships")){
					    	    			 contact.relationships = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Notes")){
					    	    			 contact.notes = row[i];
					    	    		 }else{
					    	    			 KeyValueDataVM keyV = new KeyValueDataVM();
					    	    			 if(!otherColumnName.contains(firstRow[i])){
						    	    			 otherColumnName.add(firstRow[i]);
					    	    			 }
					    	    			 keyV.key = firstRow[i];
					    	    			 keyV.value = row[i];
					    	    			 keyValue.add(keyV);
					    	    		 }
					    	    		 
					    	    	}
					    	    	contact.customData = keyValue;
					    	    	if(flag == 0){
					    	    		 cListNew.add(contact);
					    	    	 }
					    	   
					    	    	 
					    	    	// cList.add(contact);
					    	    	 //contact.newsLetter = 0;
					    	    	 //contact.save();
					    	    	 
					    	    	/* MailchimpSchedular mSchedular = MailchimpSchedular.findByLocations(16L); //Long.valueOf(session("USER_LOCATION"))
					     			if(mSchedular != null){
					     				if(mSchedular.schedularTime.equals("Immediately")){
					     					MailIntegrationServices objMail = new MailIntegrationServices();
					     	    			//obj.getLists(Long.valueOf(session("USER_LOCATION")));
					     	    			objMail.addUser(contact.firstName, contact.lastName, contact.email);
					     				}
					     			}*/
					    	    	 /*if(flag == 0){
					    	    		 cListNew.add(contact);
					    	    	 }*/
				    	    	 } else {
				    	    		 for(int i=0;i< row.length;i++){
					    	    		 if(firstRow[i].equalsIgnoreCase("ContactId")){
					    	    			 contact.contactId = Long.parseLong(row[i]);
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Type")){
					    	    			 contact.type = row[i];
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Salutation")){
					    	    			 contact.salutation = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("First Name")){
					    	    			 contact.firstName = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Middle Name")){
					    	    			 contact.middleName = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Last Name")){
					    	    			 contact.lastName = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Suffix")){
					    	    			 contact.suffix = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Company Name")){
					    	    			 contact.companyName = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Primary Email")){
					    	    			 contact.email = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Primary Phone")){
					    	    			 contact.phone = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Primary Street")){
					    	    			 contact.street = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Primary City")){
					    	    			 contact.city = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Primary State")){
					    	    			 contact.state = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Primary Zip")){
					    	    			 contact.zip = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Primary Country")){
					    	    			 contact.country = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("All Email")){
					    	    			 contact.allEmail = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("All Phone")){
					    	    			 contact.allPhone = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Website")){
					    	    			 contact.website = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("All Addresses")){
					    	    			 contact.allAddresses = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Title")){
					    	    			 contact.title = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Birthday")){
					    	    			 contact.birthday = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Background Info")){
					    	    			 contact.backgroundInfo = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Industry")){
					    	    			 contact.industry = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("# of Employees")){
					    	    			 contact.firstName = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Creation Date")){
					    	    			 contact.creationDate = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Assigned To")){
					    	    			 contact.assignedTo = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Campaign Source")){
					    	    			 contact.campaignSource = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Last Edited Date")){
					    	    			 contact.lastEditedDate = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Priority")){
					    	    			 contact.priority = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Groups")){
					    	    			 contact.groupsName = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Relationships")){
					    	    			 contact.relationships = row[i];
					    	    			 
					    	    		 }else if(firstRow[i].equalsIgnoreCase("Notes")){
					    	    			 contact.notes = row[i];
					    	    		 }else{
					    	    			 KeyValueDataVM keyV = new KeyValueDataVM();
					    	    			 if(!otherColumnName.contains(firstRow[i])){
						    	    			 otherColumnName.add(firstRow[i]);
						    	    			
					    	    			 }
					    	    			 keyV.key = firstRow[i];
					    	    			 keyV.value = row[i];
					    	    			 keyValue.add(keyV);
					    	    		 }
					    	    		 
					    	    	}
				    	    		 contact.customData = keyValue;
				    	    		 if(flag == 0){
					    	    		 cListOld.add(contact);
					    	    	 }
				    	    		// cList.add(contact);
					    	    	 //contactObj.update();
					    	    	 
					    	    	/* MailchimpSchedular mSchedular = MailchimpSchedular.findByLocations(16L); //Long.valueOf(session("USER_LOCATION"))
						     			if(mSchedular != null){
						     				if(mSchedular.schedularTime.equals("Immediately")){
						     					MailIntegrationServices objMail = new MailIntegrationServices();
						     	    			//obj.getLists(Long.valueOf(session("USER_LOCATION")));
						     	    			objMail.addUser(contactObj.firstName, contactObj.lastName, contactObj.email);
						     				}
						     			}*/
				    	    	 }
				    	    	 
		    	    	 }
		    	    	
		    	     }
		    	  } 
		    	  
		    	  map.put("columnName", columnName);
		    	  map.put("otherColumnName", otherColumnName);
		    	  map.put("newContact", cListNew);
		    	  map.put("oldContact", cListOld);
		    	  return ok(Json.toJson(map));
	    	}	
	    }
	 

	public static Result removeAllContactsData(){
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		
	    		AuthUser userObj = (AuthUser) getLocalUser();
	    		List<Contacts> contactsList;
	    		if(userObj.role.equalsIgnoreCase("Manager")){
	    			contactsList = Contacts.getAllContactsByLocation(Long.valueOf(session("USER_LOCATION")));
	    		}else{
	    			contactsList = Contacts.getAllContactsByUser(userObj.id);
	    		}
	    		
	    		for (Contacts con : contactsList) {
					con.delete();
				}
	    		return ok();
	    	}
		}
	 
	 public static Result exportContactsData(){
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		FileWriter fileWriter = null;
	    		String COMMA_DELIMITER = ",";
	    		String NEW_LINE_SEPARATOR = "\n";
	    		
	    		File fdir = new File(rootDir+File.separator+"CsvFile");
	       	    if(!fdir.exists()) {
	       	    	fdir.mkdir();
	       	    }
	       	    String filePath = rootDir+File.separator+"CsvFile/contacts.csv";
	    		
	    		
	    		try {
	       			Boolean sts = FileUtils.deleteQuietly(new File(filePath));
	    			} catch (Exception e) {
	    				e.printStackTrace();
	    			}
	    		
	    		String FILE_HEADER = "ContactId,Type,Salutation,FirstName,MiddleName,LastName,Suffix,CompanyName,Email,WorkEmail,Email1,WorkEmail1,Phone,WorkPhone,Phone1,WorkPhone1,Street,City,State,Zip,Countrt,AllEmail,AllPhone,Website,AllAddress,Title,Birthday,BackgroundInfo,Industry,NumberOfEmployees,CreationDate,LastEditedDate,AssignnedTo,CampaignSource,Priority,Groups,Relationship,Notes,Version,Newsletter,AddedBy";
	    		try {

	    			fileWriter = new FileWriter(filePath);
	        		fileWriter.append(FILE_HEADER.toString());
	        		fileWriter.append(NEW_LINE_SEPARATOR);
	        		
	        		List<Contacts> list = Contacts.getAllContacts();
	        		
	        		for (Contacts contacts : list) {
	        			
	        			fileWriter.append(String.valueOf(contacts.contactId));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.type));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.salutation));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.firstName));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.middleName));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.lastName));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.suffix));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.companyName));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.email));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.workEmail));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.email1));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.workEmail1));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.phone));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.workPhone));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.phone1));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.workPhone1));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.street));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.city));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.state));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.zip));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.country));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.allEmail));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.allPhone));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.website));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.allAddresses));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.title));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.birthday));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.backgroundInfo));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.industry));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.numberOfEmployees));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.creationDate));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.lastEditedDate));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.assignedTo));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.campaignSource));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.priority));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.groups));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.relationships));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.notes));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.version));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.newsLetter));
	        			fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.user));
	        			fileWriter.append(NEW_LINE_SEPARATOR);
					}
	        		System.out.println("CSV file was created successfully !!!");
	        		
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
					            try {
					                fileWriter.flush();
					                fileWriter.close();
					            } catch (IOException e) {
					                System.out.println("Error while flushing/closing fileWriter !!!");
					                e.printStackTrace();
					            }
					             
					        }
	    		
	    		return ok();
	    	}
		}
	 

	 	public static Result getAllGroupList(){
	 		if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		List<GroupTable> groupList =GroupTable.findAllGroup();
	    		List<GroupVM> grList= new ArrayList<>();
	    		for (GroupTable gr : groupList) {
	    			String mailList = "";
	    			if(gr.nickValue != null){
	    				String[] value = gr.nickValue.split(",");
		    			for(int i=0;i<value.length;i++){
		    				MailchimpList mail = MailchimpList.findById(Long.parseLong(value[i]));
		    				 mailList = mailList + mail.nickName;
		    				 mailList = mailList +" "+ ",";
		    			}
	    			}
	    			GroupVM vm = new GroupVM();
		    		List<Contacts> conList = new ArrayList<>();
	    			if(gr.name.equalsIgnoreCase("Undefined"))
	    				conList= Contacts.findByDefault(gr);
	    			else
	    				conList= Contacts.findByGroup(gr);
	    			vm.group = gr;
	    			if(!mailList.equals("")){
	    				vm.nickValue = mailList;
	    			}
	    			vm.contactCount = conList.size();
	    			grList.add(vm);
				}
	    		return ok(Json.toJson(grList));
	    	}
	 	}
	 	
	 	public static Result updateGroup(){
			String msg = "";
			Form<GroupTable> form = DynamicForm.form(GroupTable.class).bindFromRequest();
			GroupTable vm = form.get();
			try {
			   	GroupTable infoObj = GroupTable.findById(vm.id);
	    		infoObj.setName(vm.name);
	    		infoObj.setNickValue(vm.nickValue);
	    		infoObj.update();
			} catch (Exception e) {
				msg = "error";
			}
			return ok(msg);
		}
	 	
	 	public static Result saveNewGroup(){
			String msg = "";
			Form<GroupTable> form = DynamicForm.form(GroupTable.class).bindFromRequest();
			GroupTable vm = form.get();
			try {
				GroupTable gTable = new GroupTable();
			   	  gTable.setName(vm.name);
			   	  gTable.setNickValue(vm.nickValue);
			   	  gTable.save();
			} catch (Exception e) {
				msg = "error";
			}
			return ok(msg);
		}
		
		public static Result getAllContactsData() {
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		AuthUser userObj = (AuthUser) getLocalUser();
	    		List<ContactsVM> contactsVMList = new ArrayList<>();
	    		List<Contacts> contactsList = null;
	    		HashMap<String, Boolean> permission = new HashMap<String, Boolean>();
	    		List<Permission> userPermissions = userObj.getPermission();
	    		for(Permission per: userPermissions) {
	    			//permission.put(per.name, true);
	    			System.out.println("permission name"+per.name);
	    			if(per.name.equals("Manage Own Contacts data base")){
		    				if(userObj.role.equalsIgnoreCase("General Manager")){
		    	    			contactsList = Contacts.getAllContacts();
		    	    		}else if(userObj.role.equalsIgnoreCase("Manager")){
		    	    			contactsList = Contacts.getAllContacts();
		    	    			//contactsList = Contacts.getAllContactsByLocation(Long.valueOf(session("USER_LOCATION")));
		    	    		}else if(userObj.role.equalsIgnoreCase("Sales Person")){
		    	    			contactsList = Contacts.getAllContactsByUserId(userObj.id.toString());
		    	    		}
	    			}
	    			else if(per.name.equals("Access to the Whole Contacts data base")){
	    				contactsList = Contacts.getAllContacts();                            
	    			}
	    		
	    		}
	    		
	    		for(Contacts contact : contactsList) {
	    			ContactsVM vm = new ContactsVM();
	    			vm.contactId = contact.contactId;
	       			vm.type = contact.type;
	       			if(contact.salutation != null){
	       				if(contact.salutation.equals("null")){
		       				vm.salutation = "";
		       			}else{
		       				vm.salutation = contact.salutation;
		       			}
	       			}
	       			vm.firstName = contact.firstName;
	       			vm.middleName = contact.middleName;
	       			vm.lastName = contact.lastName;
	       			if(contact.suffix != null){
	       				if(contact.suffix.equals("null")){
		       				vm.suffix = "";
		       			}else{
		       				vm.suffix = contact.suffix;
		       			}
	       			}
	       			if(contact.companyName != null){
	       				if(contact.companyName.equals("null")){
		       				vm.companyName = "";
		       			}else{
		       				vm.companyName = contact.companyName;
		       			}
	       			}
	       			vm.email = contact.email;
	       			vm.phone = contact.phone;
	       			vm.street = contact.street;
	       			vm.city = contact.city;
	       			vm.state = contact.state;
	       			vm.zip = contact.custZipCode;
	       			vm.country = contact.country;
	       			vm.allEmail = contact.allEmail;
	       			if(contact.allPhone != null){
	       				if(contact.allPhone.equals("null")){
		       				vm.allPhone = "";
		       			}else{
		       				vm.allPhone = contact.allPhone;
		       			}
	       			}
	       			vm.website = contact.website;
	       			vm.allAddresses = contact.allAddresses;
	       			if(contact.title != null){
	       				if(contact.title.equals("null")){
		       				vm.title = "";
		       			}else{
		       				vm.title = contact.title;
		       			}
	       			}
	       			vm.fullName = contact.firstName+" "+contact.lastName;
	       			vm.enthicity = contact.enthicity;
	       			if(contact.birthday != null){
	       				if(contact.birthday.equals("null")){
		       				vm.birthday = "";
		       			}else{
		       				vm.birthday = contact.birthday;
		       			}
	       			}
	       			if(contact.backgroundInfo != null){
	       				if(contact.backgroundInfo.equals("null")){
		       				vm.backgroundInfo = "";
		       			}else{
		       				vm.backgroundInfo = contact.backgroundInfo;
		       			}
	       			}
	       			if(contact.industry != null){
	       				if(contact.industry.equals("null")){
		       				vm.industry = "";
		       			}else{
		       				vm.industry = contact.industry;
		       			}
	       			}
	       			vm.numberOfEmployees = contact.numberOfEmployees;
	       			if(contact.creationDate != null){
	       				if(contact.creationDate.equals("null")){
		       				vm.creationDate = "";
		       			}else{
		       				vm.creationDate = contact.creationDate;
		       			}
	       			}
	       			if(contact.lastEditedDate != null){
	       				if(contact.lastEditedDate.equals("null")){
		       				vm.lastEditedDate = "";
		       			}else{
		       				vm.lastEditedDate = contact.lastEditedDate;
		       			}
	       			}
	       			if(contact.assignedTo !=null && !contact.assignedTo.equals("null")){
	       				AuthUser user = AuthUser.findById(Integer.parseInt(contact.assignedTo));
	       				if(user != null)
	           			vm.assignedToName = user.firstName+" "+user.lastName;
	       			}
	       			vm.assignedTo = contact.assignedTo;
	       			vm.campaignSource = contact.campaignSource;
	       			if(contact.priority != null){
	       				if(contact.priority.equals("null")){
		       				vm.priority = "";
		       			}else{
		       				vm.priority = contact.priority;
		       			}
	       			}
	       			if(contact.relationships != null){
	       				if(contact.relationships.equals("null")){
		       				vm.relationships = "";
		       			}else{
		       				vm.relationships = contact.relationships;
		       			}
	       			}
	       			if(contact.notes != null){
	       				if(contact.notes.equals("null")){
		       				vm.notes = "";
		       			}else{
		       				vm.notes = contact.notes;
		       			}
	       			}
	       			vm.groups = contact.groups;
	       			vm.workEmail = contact.workEmail;
	       			vm.workEmail1 = contact.workEmail1;
	       			vm.workPhone = contact.workPhone;
	       			vm.workPhone1 = contact.workPhone1;
	       			vm.email1 = contact.email1;
	       			vm.phone1 = contact.phone1;
	       			findCustomCrmData(contact.contactId,vm);
	    			if(contact.newsLetter == 0) {
	    				vm.newsletter = false;
	    			} else {
	    				vm.newsletter = true;
	    			}
	    			contactsVMList.add(vm);
	    		}
	    		return ok(Json.toJson(contactsVMList));
	    	}
		}
		
		public static void findCustomCrmData(Long id,ContactsVM inventoryvm){
	    	List<CustomizationCrm> custData = CustomizationCrm.findByIdList(id);
	    	List<KeyValueDataVM> keyValueList = new ArrayList<>();
	    	Map<String, String> mapCar = new HashMap<String, String>();
	    	for(CustomizationCrm custD:custData){
	    		mapCar.put(custD.keyValue, custD.value);
	    		if(custD.displayGrid.equals("true")){
	    			KeyValueDataVM keyValue = new KeyValueDataVM();
	            	keyValue.key = custD.keyValue;
	            	keyValue.value = custD.value;
	            	keyValue.displayGrid = custD.displayGrid;
	            	keyValue.formName = custD.formName;
	            	keyValueList.add(keyValue);
	    		}
	    		
	    	}
	    	inventoryvm.customData = keyValueList;
	    	inventoryvm.customMapData = mapCar;
	    }
	 
		
		public static Result getAllContactsByLocation(Long id){
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		
	    		AuthUser userObj = (AuthUser) getLocalUser();
	    		List<ContactsVM> contactsVMList = new ArrayList<>();
	    		List<Contacts> contactsList = Contacts.getAllContactsByLocation(id);
	    			
	    		for(Contacts contact : contactsList) {
	    			ContactsVM vm = new ContactsVM();
	    			vm.contactId = contact.contactId;
	       			vm.type = contact.type;
	       			vm.salutation = contact.salutation;
	       			vm.firstName = contact.firstName;
	       			vm.middleName = contact.middleName;
	       			vm.lastName = contact.lastName;
	       			vm.suffix = contact.suffix;
	       			if(contact.companyName.equals("null") || contact.companyName.trim().equals("")){
	       				vm.companyName = "";
	       			}else{
	       				vm.companyName = contact.companyName;
	       			}
	       			
	       			vm.email = contact.email;
	       			vm.phone = contact.phone;
	       			vm.street = contact.street;
	       			vm.city = contact.city;
	       			vm.state = contact.state;
	       			vm.zip = contact.custZipCode;
	       			vm.country = contact.country;
	       			vm.allEmail = contact.allEmail;
	       			vm.allPhone = contact.allPhone;
	       			vm.website = contact.website;
	       			vm.allAddresses = contact.allAddresses;
	       			vm.title = contact.title;
	       			vm.fullName = contact.firstName+" "+contact.lastName;
	       			vm.enthicity = contact.enthicity;
	       			if(contact.assignedTo !=null){
	       				AuthUser user = AuthUser.findById(Integer.parseInt(contact.assignedTo));
	           			vm.assignedToName = user.firstName+" "+user.lastName;
	       			}
	       			vm.assignedTo = contact.assignedTo;
	       			vm.campaignSource = contact.campaignSource;
	       			vm.priority = contact.priority;
	       			vm.groups = contact.groups;
	       			vm.relationships = contact.relationships;
	       			vm.notes = contact.notes;
	       			vm.workEmail = contact.workEmail;
	       			vm.workEmail1 = contact.workEmail1;
	       			vm.workPhone = contact.workPhone;
	       			vm.workPhone1 = contact.workPhone1;
	       			vm.email1 = contact.email1;
	       			vm.phone1 = contact.phone1;
	       			
	    			if(contact.newsLetter == 0) {
	    				vm.newsletter = false;
	    			} else {
	    				vm.newsletter = true;
	    			}
	    			contactsVMList.add(vm);
	    		}
	    		return ok(Json.toJson(contactsVMList));
	    	}
		}
		

		public static Result addNewsLetter(String flag,Long id) {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	Contacts infoObj = Contacts.findById(id);
		    		if(flag.equals("true")) {
		    			infoObj.setNewsLetter(1);
		    		}
		    		if(flag.equals("false")) {
		    			infoObj.setNewsLetter(0);
		    		}
		    		infoObj.update();
		    		return ok();
	    	}  		
		}

		public static Result deleteContactsById(String id ){
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
			   	return ok(home.render("",userRegistration));
			   	} else {
				   	String msg;
				   	String arr[] = id.split(",");
		    		for(int i=0;i<arr.length;i++){
		    			 Contacts contact = Contacts.findById(Long.parseLong(arr[i]));
		    			 if(contact !=null){
		    				 contact.delete();
						 }
		    		}
		    		msg = "success";
				   	return ok(msg);
			   	}
			}
		
	 
	  public static AuthUser getLocalUser() {
	    	String id = session("USER_KEY");
	    	AuthUser user = AuthUser.find.byId(Integer.parseInt(id));
	    	//AuthUser user = getLocalUser();
			return user;
		}

	  public static Result getUsers(){
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
			   	return ok(home.render("",userRegistration));
			   	} else {
			   	
			   	List<AuthUser> userList = AuthUser.getUserByType();
			   	List<UserVM> vmList = new ArrayList<>();
			   	for(AuthUser user : userList) {
			   	UserVM vm = new UserVM();
			   		vm.fullName = user.firstName + " "+ user.lastName;
			   		vm.firstName = user.firstName;
			   		vm.lastName = user.lastName;
			   		vm.email = user.email;
			   		vm.phone = user.phone;
			   		vm.userType = user.role;
			   		vm.imageName = user.imageName;
			   		vm.imageUrl = user.imageUrl;
			   		vm.id = user.id;
			   		vmList.add(vm);
			   	}
			   	return ok(Json.toJson(vmList));
			   	}
	   }
	  
	  public static Result getAllLocations(){
			List<Location> list = Location.findAllActiveType();
			return ok(Json.toJson(list));
		}
	  
	  public static Result getgroupInfo(){
		   
		   List<GroupTable> gList = GroupTable.findAllGroup();
			return ok(Json.toJson(gList));
	   }
	  
	  public static Result saveGroup(String createGroup){
		   if(session("USER_KEY") == null || session("USER_KEY") == "") {
		   		return ok(home.render("",userRegistration));
		   	} else {
		   	  GroupTable gTable = new GroupTable();
		   	  gTable.setName(createGroup);
		   	  gTable.save();
		   	}
		   return ok("");
	   }
	  
	  public static Result deleteGroup(Long groupId){
		   if(session("USER_KEY") == null || session("USER_KEY") == "") {
		   		return ok(home.render("",userRegistration));
		   	} else {
		   	  GroupTable gTable = GroupTable.findById(groupId);
		   	  GroupTable defaultGr = GroupTable.findByName("Undefined");
		   	  List<Contacts> grCon = Contacts.findByGroup(gTable);
		   	  for (Contacts con : grCon) {
				con.setGroups(defaultGr);
				con.update();
			}
		   	  gTable.delete();
		   	}
		   return ok("");
	   }
	  
	  public static Result changeContactAssignedUser(String arrayString,Integer user) {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		AuthUser userObj = AuthUser.findById(user);
	    	
	    		String arr[] = arrayString.split(",");
	    		for(int i=0;i<arr.length;i++){
	    			 Contacts info = Contacts.findById(Long.parseLong(arr[i]));
	    			 	info.setAssignedTo(String.valueOf(user));
					    info.update();
	    		}
	    		return ok();
	    	}
	    }
	  
	  public static Result getGroupOfData(){
	 		if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		List<GroupTable> groupList = GroupTable.findAllGroup();
	    		List<GroupVM> grList= new ArrayList<>();
	    		for (GroupTable gr : groupList) {
	    			GroupVM vm = new GroupVM();
		    		vm.name = gr.name;
		    		vm.id = gr.id;
	    			grList.add(vm);
				}
	    		return ok(Json.toJson(grList));
	    	}
	 	}
	  
	  public static Result saveChangeGroupData(String arrayString,Integer user) {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		Long groupId = user.longValue();
	    		GroupTable group = GroupTable.findById(groupId);
	    		String arr[] = arrayString.split(",");
	    		for(int i=0;i<arr.length;i++){
	    			 Contacts info = Contacts.findById(Long.parseLong(arr[i]));
	    			 	info.setGroups(group);
					    info.update();
					 CustomizationCrm custCRM = CustomizationCrm.findByCRMId(Long.parseLong(arr[i]));
					 if(custCRM != null){
						 custCRM.setValue(group.name);
						 custCRM.update();
						 
					 }else{
						 CustomizationCrm cValue = new CustomizationCrm();
						 cValue.keyValue = "Nt_crm_group";
						 cValue.value = group.name;
						 cValue.crmId = Long.parseLong(arr[i]);
						 cValue.formName = "New Contact";
						 cValue.displayGrid = "true";
						 cValue.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
						 cValue.fieldId = 14800902841L;
						 cValue.save();
					 }
	    		}
	    		return ok();
	    	}
	    }
	  
	  public static Result getAllNickName(){
			List<MailchimpList> list = MailchimpList.getAll();
			return ok(Json.toJson(list));
		}
	  
}