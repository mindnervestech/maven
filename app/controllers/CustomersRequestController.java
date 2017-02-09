package controllers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
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
import org.apache.commons.lang.time.DateUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import models.AddCollection;
import models.AuthUser;
import models.CustomerPdf;
import models.CustomerRequest;
import models.CustomerRequestManufacturerSettings;
import models.CustomizationDataValue;
import models.CustomizationForm;
import models.EmailDetails;
import models.Inventory;
import models.InventoryImage;
import models.InventorySetting;
import models.LeadType;
import models.Location;
import models.MyProfile;
import models.Permission;
import models.Product;
import models.ProductImages;
import models.RequestMoreInfo;
import models.SalesPersonZipCode;
import models.ScheduleTest;
import models.SiteLogo;
import models.ToDo;
import models.TradeIn;
import models.UserNotes;
import models.Vehicle;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import viewmodel.CreateFormVM;
import viewmodel.CreateNewFormVM;
import viewmodel.KeyValueDataVM;
import viewmodel.NoteVM;
import viewmodel.ProductVM;
import viewmodel.RequestInfoVM;
import viewmodel.SpecificationVM;
import views.html.home;

import com.fasterxml.jackson.databind.JsonNode;

public class CustomersRequestController extends Controller {

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

	  public static Result getAllOtherLeadInfo(String leadId) {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		//Long.valueOf(session("USER_LOCATION"))
		    	AuthUser user = (AuthUser) getLocalUser();
		    	List<RequestMoreInfo> listData = new ArrayList<>();
		    	if(user.role == null || user.role.equals("General Manager")) {
	    			listData = RequestMoreInfo.findAllOtherLeadIdWise(leadId);
	    		} else {
	    			if(user.role.equals("Manager")) {
	    				listData = RequestMoreInfo.findAllLocationAndOtherLeadDataManager(Long.valueOf(session("USER_LOCATION")),leadId);
	    			} else {
	    				//listData = RequestMoreInfo.findAllByDate();
	    				listData = RequestMoreInfo.findAllLocationAndOtherLeadDataNull(Long.valueOf(session("USER_LOCATION")),leadId,user);
	    			}
	    		}
		    	List<RequestInfoVM> infoVMList = new ArrayList<>();
		    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		    	for(RequestMoreInfo info: listData) {
		    		RequestInfoVM vm = new RequestInfoVM();
		    		vm.id = info.id;
		    		AddCollection productInfo = AddCollection.findById(Long.parseLong(info.productId));
		    		vm.productId = info.productId;
		    		if(productInfo != null) {
		    			vm.title = productInfo.title;
		    			vm.price = (int) productInfo.price;
		    			vm.description = productInfo.description;
		    			/*vm.cost = String.valueOf(productInfo.cost);
		    			AddCollection aCollection = AddCollection.findById(productInfo.collection.id);
		    			vm.collectionName = aCollection.title;*/
		    			ProductImages pImage = ProductImages.findDefaultImg(productInfo.id);
		        		if(pImage!=null) {
		        			vm.imgId = pImage.getId().toString();
		        		}
		        		else {
		        			vm.imgId = "/assets/images/no-image.jpg";
		        		}
		    		}
		    		vm.name = info.name;
		    		vm.phone = info.phone;
		    		vm.email = info.email;
		    		vm.howContactedUs = info.contactedFrom;
		    		vm.howFoundUs = info.hearedFrom;
		    		vm.custZipCode = info.custZipCode;
		    		vm.requestDate = df.format(info.requestDate);
		    		vm.userRole = user.role;
		    		vm.premiumFlagForSale = user.premiumFlag;
		    		if(info.assignedTo == null) {
		    			vm.status = "Unclaimed";
		    		} else {
			    		if(info.assignedTo != null && info.status == null) {
			    			vm.status = "In Progress";
			    		} else {
			    			vm.status = info.status;
			    		}
		    		}
		    		if(info.assignedTo != null) {
		    			vm.salesRep = info.assignedTo.getFirstName()+" "+info.assignedTo.getLastName();
		    		}
		    		
		    		if(info.isRead == 0) {
		    			vm.isRead = false;
		    		}
		    		
		    		if(info.isRead == 1) {
		    			vm.isRead = true;
		    		}
		    		LeadType lType = LeadType.findById(Long.parseLong(info.isContactusType));
		    		if(lType != null){
		    			vm.showOnWeb = lType.shows;
		    			vm.callToAction = lType.callToAction;
		    			vm.typeOfLead = lType.leadName;
		    			vm.leadId = lType.id;
		    			CustomizationForm cDataValue = CustomizationForm.findByLocationsAndType(Long.valueOf(session("USER_LOCATION")),lType.leadName);
		    			if(cDataValue != null){
		    				vm.customizDataValue = cDataValue;
		    			}
		    			
		    		}
		    		
		    		if(lType != null){
		    			vm.showOnWeb = lType.shows;
		    				//if(vm.showOnWeb == 1 && vm.callToAction == true){
			    				Application.findCustomeData(info.id,vm,Long.parseLong(leadId));
			    			//}
		    			
		    			
		    		}
		    		//Application.findCustomeData(info.id,vm,Long.parseLong(leadId));
		    		infoVMList.add(vm);
		    	}
		    	
		    	return ok(Json.toJson(infoVMList));
	    	}	
	    }	

	  
	  public static Result getAllOtherLeadInfoRequ(String leadId) {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		//Long.valueOf(session("USER_LOCATION"))
		    	AuthUser user = (AuthUser) getLocalUser();
		    	List<RequestMoreInfo> listData = new ArrayList<>();
		    	if(user.role == null || user.role.equals("General Manager")) {
	    			listData = RequestMoreInfo.findAllOtherLeadIdWise(leadId);
	    		} else {
	    			if(user.role.equals("Manager")) {
	    				listData = RequestMoreInfo.findAllLocationAndOtherLeadDataManager(Long.valueOf(session("USER_LOCATION")),leadId);
	    			} else {
	    				listData = RequestMoreInfo.findAllLocationAndOtherLeadData(Long.valueOf(session("USER_LOCATION")),leadId,user);
	    			}
	    		}
		    	int addleadFlagAll = 0;
		    	List<CustomerRequestManufacturerSettings> cuSettings = null;
		    	List<SalesPersonZipCode> sList = null;
		    	CustomerRequest cRequest = CustomerRequest.getBylocation(Location.findById(Long.valueOf(session("USER_LOCATION"))));
		    	if(cRequest != null){
		    		if(cRequest.redirectValue.equals("Automatically redirect an online customer requests based on") && cRequest.personValue.equals("Manufacturer")){
		    			  cuSettings = CustomerRequestManufacturerSettings.findByUserList(user);
		    		}else if(cRequest.redirectValue.equals("Automatically redirect an online customer requests based on") && cRequest.personValue.equals("Price")){
		    			 
		    		}else if(cRequest.redirectValue.equals("Automatically redirect an online customer requests based on") && cRequest.personValue.equals("Zip Code")){
		    			 sList = SalesPersonZipCode.findByUserList(user);
		    		}else if(cRequest.redirectValue.equals("Redirect all online requests to") && (cRequest.personValue.equals("Myself") || cRequest.personValue.equals("Sales Person(s)"))){
		    			 if(user.id.equals(cRequest.users.id)){
		    				 addleadFlagAll = 1;
		    			 }
		    		}else if(cRequest.redirectValue.equals("Redirect all online requests to") && cRequest.personValue.equals("Me and all Sales people")){
		    			 addleadFlagAll = 1;
		    		}
		    		
		    		if((cRequest.redirectValue.equals("Automatically redirect an online customer requests based on") || cRequest.redirectValue.equals("Redirect all online requests to")) && user.role.equals("Manager")){
		    			addleadFlagAll = 1;
		    		}
		    	}
		    	int permis = 0;
		    	if(!user.role.equals("Manager")){
		    		for(Permission permission: user.permission){
			    		if(permission.id == 8){
			    			permis = 1;
			    		}
			    	}
		    	}
		    	
		    	
		    	List<RequestInfoVM> infoVMList = new ArrayList<>();
		    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		    	for(RequestMoreInfo info: listData) {
		    		RequestInfoVM vm = new RequestInfoVM();
		    		vm.id = info.id;
		    		int addleadFlag = 0;
		    		AddCollection productInfo = AddCollection.findById(Long.parseLong(info.productId));
		    		vm.productId = info.productId;
		    		if(productInfo != null) {
		    			vm.title = productInfo.title;
		    			vm.price = (int) productInfo.price;
		    			vm.description = productInfo.description;
		    			/*vm.cost = String.valueOf(productInfo.cost);
		    			AddCollection aCollection = AddCollection.findById(productInfo.collection.id);
		    			vm.collectionName = aCollection.title;*/
		    			ProductImages pImage = ProductImages.findDefaultImg(productInfo.id);
		        		if(pImage!=null) {
		        			vm.imgId = pImage.getId().toString();
		        		}
		        		else {
		        			vm.imgId = "/assets/images/no-image.jpg";
		        		}
		        		
		        		if(cuSettings != null){
			    			for(CustomerRequestManufacturerSettings cSettings:cuSettings){
				    			if(cSettings.manufacturer.id == productInfo.id){
				    				addleadFlag = 1;
				    			}
				    		}
		        		}
		    		}
		    		if(sList != null){
		    			for(SalesPersonZipCode sZipCode:sList){
			    			if(sZipCode.zipCode.equals(info.custZipCode)){
			    				addleadFlag = 1;
			    			}
			    		}
		    		}
		    		
		    			    		
		    		vm.name = info.name;
		    		vm.phone = info.phone;
		    		vm.email = info.email;
		    		vm.howContactedUs = info.contactedFrom;
		    		vm.howFoundUs = info.hearedFrom;
		    		vm.custZipCode = info.custZipCode;
		    		vm.requestDate = df.format(info.requestDate);
		    		vm.userRole = user.role;
		    		vm.premiumFlagForSale = user.premiumFlag;
		    		vm.message = info.message;
		    		if(info.assignedTo == null) {
		    			vm.status = "Unclaimed";
		    		} else {
			    		if(info.assignedTo != null && info.status == null) {
			    			vm.status = "In Progress";
			    		} else {
			    			vm.status = info.status;
			    		}
		    		}
		    		if(info.assignedTo != null) {
		    			vm.salesRep = info.assignedTo.getFirstName()+" "+info.assignedTo.getLastName();
		    		}
		    		
		    		if(info.isRead == 0) {
		    			vm.isRead = false;
		    		}
		    		
		    		if(info.isRead == 1) {
		    			vm.isRead = true;
		    		}
		    		LeadType lType = LeadType.findById(Long.parseLong(info.isContactusType));
		    		if(lType != null){
		    			vm.showOnWeb = lType.shows;
		    			vm.callToAction = lType.callToAction;
		    			vm.typeOfLead = lType.leadName;
		    			vm.leadId = lType.id;
		    			CustomizationForm cDataValue = CustomizationForm.findByLocationsAndType(Long.valueOf(session("USER_LOCATION")),lType.leadName);
		    			if(cDataValue != null){
		    				vm.customizDataValue = cDataValue;
		    			}
		    			vm.viewPdfId = 0L;
		    			if(lType.actionOutcomes != null){
		    				String arr[] = lType.actionOutcomes.split(",");
		    				for(int i=0;i<arr.length;i++){
		    					if(arr[i].equals("Generate PDF from the form")){
		    						vm.viewPdfId = 1L;
		    						
		    					}
		    				}
		    			}
		    		}
		    		
		    		if(lType != null){
		    			vm.showOnWeb = lType.shows;
		    				if(vm.showOnWeb == 1 && vm.callToAction == true){
			    				Application.findCustomeDataRequest(info.id,vm,Long.parseLong(leadId));
			    			}
		    			
		    			
		    		}
		    		//Application.findCustomeData(info.id,vm,Long.parseLong(leadId));
		    		if(addleadFlag == 1 || addleadFlagAll == 1){
		    			if(permis == 0){
		    				if(info.onlineOrOfflineLeads != 1){
		    					infoVMList.add(vm);
		    				}
		    			}else{
		    				infoVMList.add(vm);
		    			}
			    		
		    		}else if(cRequest == null){
		    			if(permis == 0){
		    				if(info.onlineOrOfflineLeads != 1){
		    					infoVMList.add(vm);
		    				}
		    			}else{
		    				infoVMList.add(vm);
		    			}
		    		}
		    	}
		    	
		    	return ok(Json.toJson(infoVMList));
	    	}	
	    }	
	  
	  public static Result getAllSalesPersonOtherLead(Integer id){
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		AuthUser user;
	    		if(id == 0){
	    			user = getLocalUser();
	    		}else{
	    			user = AuthUser.findById(id);
	    		}
	    			
		    	List<RequestMoreInfo> listData = RequestMoreInfo.findAllOtherLead(user);
		    	List<RequestInfoVM> infoVMList = new ArrayList<>();
		    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		    	SimpleDateFormat timedf = new SimpleDateFormat("HH:mm:ss");
		    	for(RequestMoreInfo info: listData) {
		    		if(!info.isContactusType.equals("contactUs")){
		    			
		    		RequestInfoVM vm = new RequestInfoVM();
		    		vm.id = info.id;
		    		AddCollection productInfo = AddCollection.findById(Long.parseLong(info.productId));
		    		vm.productId = info.productId;
		    		vm.productList = info.productList;
		    		if(productInfo != null) {
		    			vm.title = productInfo.title;
		    			vm.price = (int) productInfo.price;
		    			vm.description = productInfo.description;
		    			vm.cost = String.valueOf(productInfo.cost);
		    			//AddCollection aCollection = AddCollection.findById(productInfo.collection.id);
		    			//vm.collectionName = aCollection.title;
		    			ProductImages pImage = ProductImages.findDefaultImg(productInfo.id);
		        		if(pImage!=null) {
		        			vm.imgId = pImage.getId().toString();
		        		}
		        		else {
		        			vm.imgId = "/assets/images/no-image.jpg";
		        		}
		    		}
		    		vm.message = info.message;
		    		vm.name = info.name;
		    		vm.phone = info.phone;
		    		vm.email = info.email;
		    		vm.bestDay = info.bestDay;
		    		vm.bestTime = info.bestTime;
		    		vm.custZipCode = info.custZipCode;
		    		vm.onlineOfflineLeads = info.onlineOrOfflineLeads;
		    		vm.isContactusType = info.isContactusType;
		    		vm.saveLeadTypeAs = info.saveLeadTypeAs;
		    		vm.requestDate = df.format(info.requestDate);
		    		vm.productId = info.productId;
		    		vm.viewPdfId = 0L;
		    		if(info.saveLeadTypeAs == null){
		    			AddCollection aCollection = AddCollection.findById(Long.parseLong(info.productId));
		    			if(aCollection != null){
		    				vm.collectionName = aCollection.title;
		    			}
		    		}else if(info.saveLeadTypeAs.equals("SubCollection")){
		    			AddCollection aCollection = AddCollection.findById(Long.parseLong(info.productId));
		    			if(aCollection != null){
		    				vm.collectionName = aCollection.title;
		    			}
		    		}else if(info.saveLeadTypeAs.equals("MainCollection")){
		    			InventorySetting iSetting = InventorySetting.findById(Long.parseLong(info.productId));
		    			if(iSetting != null){
		    				vm.collectionName = iSetting.collection;
		    			}
		    		}else if(info.saveLeadTypeAs.equals("Product")){
		    			if(info.productList != null){
		    				String arrList[] = info.productList.split(",");
			    			String collNames = null;
			    			ArrayList<ProductVM> pList = new ArrayList<ProductVM>();
			    			for(int i=0;i<arrList.length;i++){
			    				ProductVM pVm = new ProductVM();
			    				Product product = Product.findById(Long.parseLong(arrList[i]));
			    				pVm.id = product.id;
			    				pVm.primaryTitle = product.primaryTitle;
			    				if(product != null){
			    					if(i == 0){
			    						if(i == arrList.length - 1){
				    						collNames = product.primaryTitle;
				    					}else {
				    						collNames = product.primaryTitle +" ,";
				    					}
			    					}else{
			    						if(i == arrList.length - 1){
				    						collNames = collNames + product.primaryTitle;
				    					}else {
				    						collNames = collNames + product.primaryTitle +",";
				    					}
			    						
			    					}
			    				}
			    				pList.add(pVm);
			    			}
			    			vm.collectionName = collNames;
			    			vm.collectionIds = pList;
		    			}
		    		}
		    		LeadType lType = LeadType.findById(Long.parseLong(info.isContactusType));
		    		if(lType != null){
		    			vm.showOnWeb = lType.shows;
		    			vm.callToAction = lType.callToAction;
		    			vm.typeOfLead = lType.leadName;
		    			vm.leadId = lType.id;
		    			if(info.onlineOrOfflineLeads == 1){
		    				String arr[] = lType.actionOutcomes.split(",");
		    				for(int i=0;i<arr.length;i++){
		    					if(arr[i].equals("Generate PDF from the form")){
		    						vm.viewPdfId = 1L;
		    					}
		    				}
			    			
			    		}
		    		}
		    			
		    		List<UserNotes> notesList = UserNotes.findRequestMore(info);
		    		Integer nFlag = 0;
		    		List<NoteVM> list = new ArrayList<>();
		    		for(UserNotes noteObj :notesList) {
		    			NoteVM obj = new NoteVM();
		    			obj.id = noteObj.id;
		    			obj.note = noteObj.note;
		    			obj.action = noteObj.action;
		    			obj.date = df.format(noteObj.createdDate);
		    			obj.time = timedf.format(noteObj.createdTime);
		    			if(noteObj.saveHistory != null){
		    				if(noteObj.saveHistory.equals(1)){
		        				nFlag = 1;
		        			}
		    			}
		    			list.add(obj);
		    		}
		    		vm.note = list;
		    		vm.noteFlag = nFlag;
		    		vm.requestDate = df.format(info.requestDate);
		    		if(info.isRead == 0) {
		    			vm.isRead = false;
		    		}
		    		
		    		if(info.isRead == 1) {
		    			vm.isRead = true;
		    		}
		    		
		    		if(lType != null){
		    			vm.showOnWeb = lType.shows;
		    				//if(vm.showOnWeb == 1 && vm.callToAction == true){
			    				Application.findCustomeData(info.id,vm,lType.id);
			    			//}
		    			
		    			
		    		}
		    		Application.findRequestParentChildAndBro(infoVMList, info, df, vm);
		    	}
		    	
		    	}
		    	
		    	return ok(Json.toJson(infoVMList));
	    	}	
	    
	    	
	    }
	  

	  public static AuthUser getLocalUser() {
	    	String id = session("USER_KEY");
	    	AuthUser user = AuthUser.find.byId(Integer.parseInt(id));
	    	//AuthUser user = getLocalUser();
			return user;
		}

	  public static Result getAllContactInfo() {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		//Long.valueOf(session("USER_LOCATION"))
		    	AuthUser user = (AuthUser) getLocalUser();
		    	List<RequestMoreInfo> listData = new ArrayList<>();
		    	if(user.role == null || user.role.equals("General Manager")) {
	    			listData = RequestMoreInfo.findAllContactData();
	    		} else {
	    			if(user.role.equals("Manager")) {
	    				listData = RequestMoreInfo.findAllLocationDataManagerContactUs(Long.valueOf(session("USER_LOCATION")));
	    			} else {
	    				//listData = RequestMoreInfo.findAllByDate();
	    				listData = RequestMoreInfo.findAllLocationDataContactUs(Long.valueOf(session("USER_LOCATION")));
	    			}
	    		}
		    	List<RequestInfoVM> infoVMList = new ArrayList<>();
		    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		    	for(RequestMoreInfo info: listData) {
		    		RequestInfoVM vm = new RequestInfoVM();
		    		vm.id = info.id;
		    		Inventory productInfo = Inventory.getByProductId(info.productId);
		    		vm.productId = info.productId;
		    		if(productInfo != null) {
		    			vm.title = productInfo.title;
		    			vm.price = (int) productInfo.price;
		    			vm.description = productInfo.description;
		    			vm.cost = String.valueOf(productInfo.cost);
		    			AddCollection aCollection = AddCollection.findById(productInfo.collection.id);
		    			vm.collectionName = aCollection.title;
		    			InventoryImage pImage = InventoryImage.getDefaultImage(productInfo.productId);
		        		if(pImage!=null) {
		        			vm.imgId = pImage.getId().toString();
		        		}
		        		else {
		        			vm.imgId = "/assets/images/no-image.jpg";
		        		}
		    		}
		    		vm.message=info.message;
		    		vm.name = info.name;
		    		vm.phone = info.phone;
		    		vm.email = info.email;
		    		vm.howContactedUs = info.contactedFrom;
		    		vm.howFoundUs = info.hearedFrom;
		    		vm.custZipCode = info.custZipCode;
		    		vm.enthicity = info.enthicity;
		    		vm.requestDate = df.format(info.requestDate);
		    		vm.userRole = user.role;
		    		vm.premiumFlagForSale = user.premiumFlag;
		    		if(info.assignedTo == null) {
		    			vm.status = "Unclaimed";
		    		} else {
			    		if(info.assignedTo != null && info.status == null) {
			    			vm.status = "In Progress";
			    		} else {
			    			vm.status = info.status;
			    		}
		    		}
		    		if(info.assignedTo != null) {
		    			vm.salesRep = info.assignedTo.getFirstName()+" "+info.assignedTo.getLastName();
		    		}
		    		
		    		if(info.isRead == 0) {
		    			vm.isRead = false;
		    		}
		    		
		    		if(info.isRead == 1) {
		    			vm.isRead = true;
		    		}
		    		
		    		infoVMList.add(vm);
		    	}
		    	
		    	return ok(Json.toJson(infoVMList));
	    	}	
	    }
	  
	  public static Result requestInfoRichNotification(Long id){
		  if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		RequestMoreInfo infoObj = RequestMoreInfo.findById(id);
	    		infoObj.setRichNotification(1);
	    		infoObj.update();
	    		return ok();
	    	}
	  }
	  
	  public static Result requestInfoMarkRead(String flag,Long id) {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		Date currDate = new Date();
	    		AuthUser user = (AuthUser) getLocalUser();
		    	RequestMoreInfo infoObj = RequestMoreInfo.findById(id);
		    		if(flag.equals("true")) {
		    			infoObj.setIsRead(1);
		    			infoObj.setAssignedTo(user);
		    			
		    			UserNotes uNotes = new UserNotes();
			    		uNotes.setNote("Lead has been claimed");
			    		uNotes.setAction("Other");
			    		uNotes.createdDate = currDate;
			    		uNotes.createdTime = currDate;
			    		uNotes.user = user;
		        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
			    		uNotes.requestMoreInfo = RequestMoreInfo.findById(infoObj.id);
			    		uNotes.save();
		    		}
		    		if(flag.equals("false")) {
		    			infoObj.setIsRead(0);
		    			infoObj.setAssignedTo(null);
		    		}
		    		infoObj.update();
		    	
		        	
		        	return ok();
	    	}	
	    }
	  
	  public static Result changeAssignedUser(String arrayString,Integer user,String leadType) {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		AuthUser userObj = AuthUser.findById(user);
	    	
	    		String arr[] = arrayString.split(",");
	    		for(int i=0;i<arr.length;i++){
	    			 RequestMoreInfo info = RequestMoreInfo.findById(Long.parseLong(arr[i]));
					    info.setAssignedTo(userObj);
					    info.setPremiumFlag(0);
					    info.setIsRead(1);
					    info.setStatus(null);
					    info.setLeadStatus(null);
					    info.setIsReassigned(true);
					    info.update();
	    		}
				   
				
	    		return ok();
	    	}
	    }
	  
	  public static Result getSalesUserValue(){
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		List<AuthUser> SalesUserList = AuthUser.getAllUserByLocation(Location.findById(Long.valueOf(session("USER_LOCATION"))));
	    		return ok(Json.toJson(SalesUserList));
	    	}
	    }
	  
	    public static Result deletePremiumLead(Long id,String leadType) {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		if(leadType.equals("Schedule Test")) {
	    			ScheduleTest schedule = ScheduleTest.findById(id);
	    			List<UserNotes> userNote = UserNotes.findScheduleTest(schedule);
	    			for (UserNotes userNotes : userNote) {
	    				userNotes.delete();
					}
	    			schedule.delete();
	    		}
				if(leadType.equals("Request More Info")) {
				    RequestMoreInfo info = RequestMoreInfo.findById(id);
				    List<UserNotes> userNote = UserNotes.findRequestMore(info);
				    for (UserNotes userNotes : userNote) {
	    				userNotes.delete();
					}
				    info.delete();
				}
				if(leadType.equals("Trade In")) {
					TradeIn tradeIn = TradeIn.findById(id);
					List<UserNotes> userNote = UserNotes.findTradeIn(tradeIn);
							for (UserNotes userNotes : userNote) {
			    				userNotes.delete();
							}
					tradeIn.delete();
				}
	    		return ok();
	    	}
	    }
	    
	    public static Result getAllPremiumIn(){
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	AuthUser user = (AuthUser) getLocalUser();
		    	List<TradeIn> listData = new ArrayList<>();
		    	List<RequestMoreInfo> lInfos = new ArrayList<>();
		    	List<ScheduleTest> sList = new ArrayList<>();
		    	if(user.role == null || user.role.equals("General Manager")) {
	    			listData = TradeIn.findAllData();
	    		} else {
	    			int flag = 0;
	    			if(user.role.equals("Sales Person")) {
	    				if(user.premiumFlag.equals("1")){
	    					flag = 1;
	    				}
	    			}
	    			if(user.role.equals("Manager") || flag == 1) {
	    				listData = TradeIn.findAllLocationDataManagerPremium(Long.valueOf(session("USER_LOCATION")));
	    				sList = ScheduleTest.findAllLocationDataManagerPremium(Long.valueOf(session("USER_LOCATION")));
	    				lInfos = RequestMoreInfo.findAllLocationDataManagerPremium(Long.valueOf(session("USER_LOCATION")));
	    			} /*else {
	    				listData = TradeIn.findAllByLocationDate(Long.valueOf(session("USER_LOCATION")));
	    			}*/
	    		}
		    	List<RequestInfoVM> infoVMList = new ArrayList<>();
		    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		    	for(TradeIn info: listData) {
		    		RequestInfoVM vm = new RequestInfoVM();
		    		vm.id = info.id;
		    		Inventory productInfo = Inventory.getByProductId(info.productId);
		    		vm.productId = info.productId;
		    		if(productInfo != null) {
		    			vm.title = productInfo.title;
		    			vm.price = (int) productInfo.price;
		    			vm.description = productInfo.description;
		    			vm.cost = String.valueOf(productInfo.cost);
		    			AddCollection aCollection = AddCollection.findById(productInfo.collection.id);
		    			vm.collectionName = aCollection.title;
		    			InventoryImage pImage = InventoryImage.getDefaultImage(productInfo.productId);
		        		if(pImage!=null) {
		        			vm.imgId = pImage.getId().toString();
		        		}
		        		else {
		        			vm.imgId = "/assets/images/no-image.jpg";
		        		}
		    		}
		    		if(info.lastName != null){
		    			vm.name = info.firstName+" "+info.lastName;
		    		}else{
		    			vm.name = info.firstName;
		    		}
		    			
		    		
		    		vm.phone = info.phone;
		    		vm.email = info.email;
		    		vm.howContactedUs = info.contactedFrom;
		    		vm.howFoundUs = info.hearedFrom;
		    		vm.custZipCode = info.custZipCode;
		    		vm.enthicity = info.enthicity;
		    		vm.pdfPath = info.pdfPath;
		    		vm.userRole = user.role;
		    		vm.leadType = "Trade In";
		    		
		    		
		    		if(info.assignedTo == null) {
		    			vm.status = "Unclaimed";
		    		} else {
			    		if(info.assignedTo != null && info.status == null) {
			    			vm.status = "In Progress";
			    		} else {
			    			vm.status = info.status;
			    		}
		    		}
		    		if(info.assignedTo != null) {
		    			vm.salesRep = info.assignedTo.getFirstName()+" "+info.assignedTo.getLastName();
		    		}
		    		vm.requestDate = df.format(info.tradeDate);
		    		if(info.isRead == 0) {
		    			vm.isRead = false;
		    		}
		    		
		    		if(info.isRead == 1) {
		    			vm.isRead = true;
		    		}
		    		infoVMList.add(vm);
		    	}
		    	
		    	for(RequestMoreInfo info: lInfos) {
		    		RequestInfoVM vm = new RequestInfoVM();
		    		vm.id = info.id;
		    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
		    		vm.vin = info.vin;
		    		if(vehicle != null) {
		    			vm.model = vehicle.model;
		    			vm.make = vehicle.make;
		    			vm.stock = vehicle.stock;
		    			vm.year = vehicle.year;
		    			vm.mileage = vehicle.mileage;
		    			vm.price = vehicle.price;
		    			
		    			vm.bodyStyle =vehicle.bodyStyle;
		    			vm.drivetrain = vehicle.drivetrain;
		    			vm.engine = vehicle.engine;
		    			vm.transmission = vehicle.transmission;
		    			InventoryImage vehicleImage = InventoryImage.getDefaultImage(vehicle.vin);
		        		if(vehicleImage!=null) {
		        			vm.imgId = vehicleImage.getId().toString();
		        		}
		        		else {
		        			vm.imgId = "/assets/images/no-image.jpg";
		        		}
		    		}
		    		vm.name = info.name;
		    		vm.phone = info.phone;
		    		vm.email = info.email;
		    		vm.howContactedUs = info.contactedFrom;
		    		vm.howFoundUs = info.hearedFrom;
		    		vm.custZipCode = info.custZipCode;
		    		vm.enthicity = info.enthicity;
		    		vm.requestDate = df.format(info.requestDate);
		    		vm.leadType = "Request More Info";
		    		
		    		if(info.assignedTo == null) {
		    			vm.status = "Unclaimed";
		    		} else {
			    		if(info.assignedTo != null && info.status == null) {
			    			vm.status = "In Progress";
			    		} else {
			    			vm.status = info.status;
			    		}
		    		}
		    		if(info.assignedTo != null) {
		    			vm.salesRep = info.assignedTo.getFirstName()+" "+info.assignedTo.getLastName();
		    		}
		    		
		    		if(info.isRead == 0) {
		    			vm.isRead = false;
		    		}
		    		
		    		if(info.isRead == 1) {
		    			vm.isRead = true;
		    		}
		    		
		    		infoVMList.add(vm);
		    	}
		    	
		    	Calendar time = Calendar.getInstance();
		    	for(ScheduleTest info: sList) {
		    		RequestInfoVM vm = new RequestInfoVM();
		    		vm.id = info.id;
		    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
		    		vm.vin = info.vin;
		    		if(vehicle != null) {
		    			vm.model = vehicle.model;
		    			vm.make = vehicle.make;
		    			vm.stock = vehicle.stock;
		    			vm.year = vehicle.year;
		    			vm.mileage = vehicle.mileage;
		    			vm.price = vehicle.price;
		    			
		    			vm.bodyStyle =vehicle.bodyStyle;
		    			vm.drivetrain = vehicle.drivetrain;
		    			vm.engine = vehicle.engine;
		    			vm.transmission = vehicle.transmission;
		    			InventoryImage vehicleImage = InventoryImage.getDefaultImage(vehicle.vin);
		        		if(vehicleImage!=null) {
		        			vm.imgId = vehicleImage.getId().toString();
		        		}
		        		else {
		        			vm.imgId = "/assets/images/no-image.jpg";
		        		}
		    		}
		    		vm.name = info.name;
		    		vm.phone = info.phone;
		    		vm.email = info.email;
		    		if(info.bestDay != null){
		    			/*String chaArr[] = info.bestDay.split("-");
		    			vm.bestDay = chaArr[1]+"/"+chaArr[2]+"/"+chaArr[0];*/
		    			vm.bestDay = info.bestDay;
		    		}
		    		vm.bestTime = info.bestTime;
		    		vm.howContactedUs = info.contactedFrom;
		    		vm.howFoundUs = info.hearedFrom;
		    		vm.custZipCode = info.custZipCode;
		    		vm.enthicity = info.enthicity;
		    		vm.leadType = "Schedule Test";
		    		
		    		
		    		if(info.assignedTo == null) {
		    			vm.status = "Unclaimed";
		    		} else {
			    		if(info.assignedTo != null && info.leadStatus == null) {
			    			vm.status = "In Progress";
			    		} else {
			    			vm.status = info.leadStatus;
			    		}
		    		}
		    		if(info.assignedTo != null) {
		    			vm.salesRep = info.assignedTo.getFirstName()+" "+info.assignedTo.getLastName();
		    		}
		    		
		    		if(info.getConfirmDate() != null) {
		    			vm.confirmDate = df.format(info.getConfirmDate());
		    		}
		    		
		    		if(info.getConfirmTime() != null) {
		    			time.setTime(info.getConfirmTime());
		    			String ampm = "";
		    			if(time.get(Calendar.AM_PM) == Calendar.PM) {
		    				ampm = "PM";
		    			} else {
		    				ampm = "AM";
		    			}
		    			vm.confirmTime = time.get(Calendar.HOUR) + ":" + time.get(Calendar.MINUTE) + " " + ampm;
		    		}
		    		if(info.scheduleDate != null){
		    			vm.requestDate = df.format(info.scheduleDate);
		    		}
		    		
		    		if(info.isRead == 0) {
		    			vm.isRead = false;
		    		}
		    		
		    		if(info.isRead == 1) {
		    			vm.isRead = true;
		    		}
		    		infoVMList.add(vm);
		    	}
		    	return ok(Json.toJson(infoVMList));
	    	}	
	    }
	    
	    public static Result releaseLeads(Long id,String leadType) {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		if(leadType.equals("Schedule Test")) {
	    			ScheduleTest schedule = ScheduleTest.findById(id);
	    			schedule.setPremiumFlag(0);
	    			
	    			schedule.update();
	    			String vin=schedule.vin;
	    			
	    			Location loc=schedule.locations;
	    			String confirmDate=schedule.bestDay;
	    			String confirmTime=schedule.bestTime;
	    			String pref=schedule.preferredContact;
	    			String pdffilePath=null;
	    			scheduleTestReleaseMail(vin,loc,confirmDate,confirmTime,pref,leadType,pdffilePath);
	    		}
				if(leadType.equals("Request More Info")) {
				    RequestMoreInfo info = RequestMoreInfo.findById(id);
				    info.setPremiumFlag(0);
				    info.update();
				    
	                String vin=info.vin;
	    			String pdffilePath=null;
	    			Location loc=info.locations;
	    			String confirmDate=info.bestDay;
	    			String confirmTime=info.bestTime;
	    			String pref=info.preferredContact;
	    			scheduleTestReleaseMail(vin,loc,confirmDate,confirmTime,pref,leadType,pdffilePath);
				}
				
	    		return ok();
	    	}
	    }
	    
	    public static void scheduleTestReleaseMail(String vin,Location loc,String confirmDate,String confirmTime,String preferred,String leadType,String pdffilePath){
	    	AuthUser locUser=getLocalUser();
	    	List <AuthUser> userList=AuthUser.findByLocatio(loc);
			InternetAddress[] usersArray = new InternetAddress[userList.size()];
			int index = 0;
			for (AuthUser assi : userList) {
				try {
					
					usersArray[index] = new InternetAddress(assi.getCommunicationemail());
					index++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			MyProfile  user1=MyProfile.findByUser(locUser);
			String email=user1.email;
			/*List<UserVM> list = new ArrayList<>() ;
			for(AuthUser assi : userList){
				
				UserVM vm1=new UserVM();
				vm1.fullName=assi.firstName+" "+assi.lastName;
				list.add(vm1);
				
				
				
			}
			*/
			
			
			EmailDetails details=EmailDetails.findByLocation(Long.valueOf(session("USER_LOCATION")));
			String emailName=details.name;
			String port=details.port;
			String gmail=details.host;
			final	String emailUser=details.username;
			final	String emailPass=details.passward;
			
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.host", gmail);
			props.put("mail.smtp.port", 587);
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
				message.addRecipients(Message.RecipientType.BCC,usersArray);
				if(leadType.equals("Request More Info")){
					message.setSubject("Request More Info");
				}else if(leadType.equals("Trade In")){
					message.setSubject("Trade In");
				}else{
					message.setSubject("Schedule Test Drive");
				}
				Multipart multipart = new MimeMultipart();
				BodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart = new MimeBodyPart();
				
				VelocityEngine ve = new VelocityEngine();
				ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
				ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
				ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
				ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
				ve.init();
				Template t=null;
				
				if(leadType.equals("Request More Info")){
					 t = ve.getTemplate("/public/emailTemplate/requestMoreInfo_HTML.vm");
					
				}else if(leadType.equals("Trade In")){
					t = ve.getTemplate("/public/emailTemplate/tRADEINAPPRAISAL_HTML.vm");
					
				}
				else{
					 t = ve.getTemplate("/public/emailTemplate/ScheduleTestDrive_HTML.vm");
				}
				 
		        VelocityContext context = new VelocityContext();
		        
		        //context.put("title", vm.name);
		       // context.put("location", loc.getName());
		       // context.put("meetingBy", user.getFirstName()+" "+user.getLastName());
		        Vehicle vehicle = Vehicle.findByVinAndStatus(vin);
		        String months[] = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
			       
		        int dayOfmonth=1;
		        int month=0;
		        if(confirmDate != null){
		        try {
		        	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		        	String dateInString = confirmDate;
		        	String arr[] = dateInString.toString().split("-");
			        if(arr.length >=2){
			        	dayOfmonth = Integer.parseInt(arr[0]);
				        month = Integer.parseInt(arr[1]);
			        }else{
			        	Date date =formatter.parse(confirmDate);
			        	Calendar cal = Calendar.getInstance();
				         cal.setTime((Date)date);
				         dayOfmonth = cal.get(Calendar.DAY_OF_MONTH);
				         month = cal.get(Calendar.MONTH)+1;
			        }
				} catch (Exception e) {
					e.printStackTrace();
				}
		        String monthName = months[month-1];
		        context.put("part1Date",  dayOfmonth);
		        context.put("part2Date",  monthName);
		        SimpleDateFormat localDateFormat = new SimpleDateFormat("hh:mm:aa");
		        String time = confirmTime;
		        
		        context.put("bestTime", time);
		        }
//		String dateInString = vm.getBestDay();

		
		        
		        if(pdffilePath != null){
		        	context.put("pdffilePath", pdffilePath);
		        }
		        
		        context.put("hostnameimg", imageUrlPath);
		        context.put("hostnameUrl", imageUrlPath);
	         if(vehicle.mileage!= null){
		        	
		        	context.put("mileage",vehicle.mileage);
		        	 
		        }
		        else{
		        	context.put("mileage","");
		        }
	         context.put("preferred",  preferred.toUpperCase());
		        context.put("year", vehicle.year);
		        context.put("make", vehicle.make);
		        context.put("model", vehicle.model);
		        context.put("price", "$"+vehicle.price);
		        context.put("stock", vehicle.stock);
		        context.put("vin", vehicle.vin);
		        context.put("make", vehicle.make);
		        context.put("typeofVehicle", vehicle.typeofVehicle);
		        
		        if(leadType.equals("Trade In")){
		        	 context.put("first_name", locUser.firstName);
		        	 context.put("last_name", locUser.lastName);
			        
		        }else{
		        	 context.put("name", locUser.fullName());
		        }
		       
		        context.put("email",locUser.email);
		        if(leadType.equals("Trade In")){
		        	context.put("work_phone",  locUser.phone);
			        
		        }else{
		        	context.put("phone",  locUser.phone);
			        
		        }
		        
		        InventoryImage image = InventoryImage.getDefaultImage(vehicle.vin);
		        if(image!=null) {
		        	context.put("path", image.path);
		        } else {
		        	context.put("path", "");
		        }
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
		}
	    
	    public static Result getAllRequestInfo() {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		//Long.valueOf(session("USER_LOCATION"))
		    	AuthUser user = (AuthUser) getLocalUser();
		    	List<RequestMoreInfo> listData = new ArrayList<>();
		    	if(user.role == null || user.role.equals("General Manager")) {
	    			listData = RequestMoreInfo.findAllData();
	    		} else {
	    			if(user.role.equals("Manager")) {
	    				listData = RequestMoreInfo.findAllLocationDataManager(Long.valueOf(session("USER_LOCATION")));
	    			} else {
	    				//listData = RequestMoreInfo.findAllByDate();
	    				listData = RequestMoreInfo.findAllLocationData(Long.valueOf(session("USER_LOCATION")));
	    			}
	    		}
		    	List<RequestInfoVM> infoVMList = new ArrayList<>();
		    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		    	for(RequestMoreInfo info: listData) {
		    		RequestInfoVM vm = new RequestInfoVM();
		    		vm.id = info.id;
		    		Inventory inventoryInfo = Inventory.getByProductId(info.productId);
		    		vm.productId = info.productId;
		    		if(inventoryInfo != null) {
		    			vm.title = inventoryInfo.title;
		    			vm.price = (int) inventoryInfo.price;
		    		}
		    		if(vm.title == null){
		    			vm.title = info.section;
		    		}
		    		vm.name = info.name;
		    		vm.phone = info.phone;
		    		vm.email = info.email;
		    		vm.howContactedUs = info.contactedFrom;
		    		vm.howFoundUs = info.hearedFrom;
		    		vm.custZipCode = info.custZipCode;
		    		vm.enthicity = info.enthicity;
		    		vm.requestDate = df.format(info.requestDate);
		    		vm.userRole = user.role;
		    		vm.premiumFlagForSale = user.premiumFlag;
		    		if(info.assignedTo == null) {
		    			vm.status = "Unclaimed";
		    		} else {
			    		if(info.assignedTo != null && info.status == null) {
			    			vm.status = "In Progress";
			    		} else {
			    			vm.status = info.status;
			    		}
		    		}
		    		if(info.assignedTo != null) {
		    			vm.salesRep = info.assignedTo.getFirstName()+" "+info.assignedTo.getLastName();
		    		}
		    		
		    		if(info.isRead == 0) {
		    			vm.isRead = false;
		    		}
		    		
		    		if(info.isRead == 1) {
		    			vm.isRead = true;
		    		}
		    		
		    		findCustomeData(info.id,vm,Long.parseLong(info.isContactusType));
		    		
		    		infoVMList.add(vm);
		    	}
		    	
		    	return ok(Json.toJson(infoVMList));
	    	}	
	    }
	    
	    public static void findCustomeData(Long id,RequestInfoVM vm,Long leadType){
	    	List<CustomizationDataValue> custData = CustomizationDataValue.findByCustomeLead(leadType, id);
	    	List<KeyValueDataVM> keyValueList = new ArrayList<>();
	    	Map<String, String> mapCar = new HashMap<String, String>();
	    	for(CustomizationDataValue custD:custData){
	    		mapCar.put(custD.keyValue, custD.value);
	    		if(custD.displayGrid.equals("true")){
	    			//if(keyValueList.size() == 0){
	    				KeyValueDataVM keyValue = new KeyValueDataVM();
	            		keyValue.key = custD.keyValue;
	            		keyValue.value = custD.value;
	            		keyValue.displayGrid = custD.displayGrid;
	            		keyValueList.add(keyValue);
	    			
	    			
	    		}
	    		
	    	}
	    	vm.customData = keyValueList;
	    	vm.customMapData = mapCar;
	    }
	    
	    public static Result getAllScheduleTest() {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	AuthUser user = (AuthUser) getLocalUser();
		    	List<ScheduleTest> listData = new ArrayList<>();
	    		if(user.role == null || user.role.equals("General Manager")) {
	    			listData = ScheduleTest.findAllData();
	    		} else {
	    			if(user.role.equals("Manager")) {
	    				listData = ScheduleTest.findAllLocationDataManager(Long.valueOf(session("USER_LOCATION")));
	    			} else {
	    				listData = ScheduleTest.findAllLocationData(Long.valueOf(session("USER_LOCATION")));
	    				//listData = ScheduleTest.findAllByLocationDate(Long.valueOf(session("USER_LOCATION")));
	    			}
	    		}
		    	List<RequestInfoVM> infoVMList = new ArrayList<>();
		    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		    	Calendar time = Calendar.getInstance();
		    	for(ScheduleTest info: listData) {
		    		RequestInfoVM vm = new RequestInfoVM();
		    		vm.id = info.id;
		    		
		    		
		    		Inventory productInfo = Inventory.getByProductId(info.productId);
		    		vm.productId = info.productId;
		    		if(productInfo != null) {
		    			vm.title = productInfo.title;
		    			vm.price = (int) productInfo.price;
		    			vm.description = productInfo.description;
		    			vm.cost = String.valueOf(productInfo.cost);
		    			AddCollection aCollection = AddCollection.findById(productInfo.collection.id);
		    			vm.collectionName = aCollection.title;
		    			InventoryImage pImage = InventoryImage.getDefaultImage(productInfo.productId);
		        		if(pImage!=null) {
		        			vm.imgId = pImage.getId().toString();
		        		}
		        		else {
		        			vm.imgId = "/assets/images/no-image.jpg";
		        		}
		    		}
		    		
		    		vm.name = info.name;
		    		vm.phone = info.phone;
		    		vm.email = info.email;
		    		if(info.bestDay != null){
		    			String chaArr[] = info.bestDay.split("-");
		    			vm.bestDay = chaArr[2]+"-"+chaArr[1]+"-"+chaArr[0];
		    			//vm.bestDay = info.bestDay;
		    		}
		    		vm.bestTime = info.bestTime;
		    		vm.howContactedUs = info.contactedFrom;
		    		vm.howFoundUs = info.hearedFrom;
		    		vm.custZipCode = info.custZipCode;
		    		vm.enthicity = info.enthicity;
		    		vm.userRole = user.role;
		    		vm.premiumFlagForSale = user.premiumFlag;
		    		
		    		if(info.assignedTo == null) {
		    			vm.status = "Unclaimed";
		    		} else {
			    		if(info.assignedTo != null && info.leadStatus == null) {
			    			vm.status = "In Progress";
			    		} else {
			    			vm.status = info.leadStatus;
			    		}
		    		}
		    		if(info.assignedTo != null) {
		    			vm.salesRep = info.assignedTo.getFirstName()+" "+info.assignedTo.getLastName();
		    		}
		    		
		    		if(info.getConfirmDate() != null) {
		    			vm.confirmDate = df.format(info.getConfirmDate());
		    		}
		    		
		    		if(info.getConfirmTime() != null) {
		    			time.setTime(info.getConfirmTime());
		    			String ampm = "";
		    			if(time.get(Calendar.AM_PM) == Calendar.PM) {
		    				ampm = "PM";
		    			} else {
		    				ampm = "AM";
		    			}
		    			vm.confirmTime = time.get(Calendar.HOUR) + ":" + time.get(Calendar.MINUTE) + " " + ampm;
		    		}
		    		if(info.scheduleDate != null){
		    			vm.requestDate = df.format(info.scheduleDate);
		    		}
		    		
		    		if(info.isRead == 0) {
		    			vm.isRead = false;
		    		}
		    		
		    		if(info.isRead == 1) {
		    			vm.isRead = true;
		    		}
		    		
		    		findCustomeData(info.id,vm,2L);
		    		infoVMList.add(vm);
		    	}
		    	
		    	return ok(Json.toJson(infoVMList));
	    	}	
	    }
	    
	    public static Result scheduleTestMarkRead(String flag,Long id) {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		Date currDate = new Date();
		    	ScheduleTest scheduleObj = ScheduleTest.findById(id);
		    	AuthUser user = (AuthUser) getLocalUser();
		    	if(flag.equals("true")) {
		    		scheduleObj.setIsRead(1);
		    		scheduleObj.setAssignedTo(user);
		    		
		    		UserNotes uNotes = new UserNotes();
		    		uNotes.setNote("Lead has been claimed");
		    		uNotes.setAction("Other");
		    		uNotes.createdDate = currDate;
		    		uNotes.createdTime = currDate;
		    		uNotes.user = user;
	        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    		uNotes.scheduleTest = ScheduleTest.findById(scheduleObj.id);
		    		uNotes.save();
				}
				if(flag.equals("false")) {
					scheduleObj.setIsRead(0);
					scheduleObj.setAssignedTo(null);
				}
				
				scheduleObj.update();
				
				ToDo todo = new ToDo();
				Vehicle vobj = Vehicle.findByVinAndStatus(scheduleObj.vin);
				todo.task = "Follow up with the client about test Drive for "+vobj.make+" "+vobj.model+" ("+vobj.vin+")";
				todo.assignedTo = user;
				todo.assignedBy = user;
				todo.priority = "High";
				todo.status = "Assigned";
				Calendar cal = Calendar.getInstance();
				Date date = new Date();
				cal.setTime(date);
				cal.add(Calendar.DATE, 1);
				todo.dueDate = cal.getTime();
				todo.saveas = 0;
				//todo.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
				todo.save();
				
				List<ScheduleTest> listData = new ArrayList<>();
				if(user.role == null) {
	    			listData = ScheduleTest.findAllData();
	    		} else {
	    			if(user.role.equals("General Manager")) {
	    				listData = ScheduleTest.findAllData();
	    			} else {
	    				listData = ScheduleTest.findAllByDate();
	    			}
	    		}
		    	List<RequestInfoVM> infoVMList = new ArrayList<>();
		    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		    	for(ScheduleTest info: listData) {
		    		RequestInfoVM vm = new RequestInfoVM();
		    		vm.id = info.id;
		    		Inventory productInfo = Inventory.getByProductId(info.productId);
		    		vm.productId = info.productId;
		    		if(productInfo != null) {
		    			vm.title = productInfo.title;
		    			vm.price = (int) productInfo.price;
		    			vm.description = productInfo.description;
		    			vm.cost = String.valueOf(productInfo.cost);
		    			AddCollection aCollection = AddCollection.findById(productInfo.collection.id);
		    			vm.collectionName = aCollection.title;
		    			InventoryImage pImage = InventoryImage.getDefaultImage(productInfo.productId);
		        		if(pImage!=null) {
		        			vm.imgId = pImage.getId().toString();
		        		}
		        		else {
		        			vm.imgId = "/assets/images/no-image.jpg";
		        		}
		    		}
		    		vm.name = info.name;
		    		vm.phone = info.phone;
		    		vm.email = info.email;
		    		if(info.bestDay != null){
		    			String chaArr[] = info.bestDay.split("-");
		    			vm.bestDay = chaArr[2]+"-"+chaArr[1]+"-"+chaArr[0];
		    			//vm.bestDay = info.bestDay;
		    		}
		    		vm.bestTime = info.bestTime;
		    		if(info.scheduleDate != null){
		    			vm.requestDate = df.format(info.scheduleDate);
		    		}
		    		
		    		if(info.assignedTo == null) {
		    			vm.status = "Unclaimed";
		    		} else {
			    		if(info.assignedTo != null && info.leadStatus == null) {
			    			vm.status = "In Progress";
			    		} else {
			    			vm.status = info.leadStatus;
			    		}
		    		}
		    		if(info.assignedTo != null) {
		    			vm.salesRep = info.assignedTo.getFirstName()+" "+info.assignedTo.getLastName();
		    		}
		    		
		    		if(info.isRead == 0) {
		    			vm.isRead = false;
		    		}
		    		
		    		if(info.isRead == 1) {
		    			vm.isRead = true;
		    		}
		    		infoVMList.add(vm);
		    	}
		    	
		    	return ok(Json.toJson(infoVMList));
	    	}	
	    }
	    
	    public static Result saveConfirmData() {
	    	
	    	Date currDate = new Date();
	    	AuthUser user = (AuthUser) getLocalUser();
	    	Form<RequestInfoVM> form = DynamicForm.form(RequestInfoVM.class).bindFromRequest();
	    	RequestInfoVM vm = form.get();
	    	boolean flag = false;
	    	String vin = null;
	    	Date confirmDate = null;
	    	RequestInfoVM listOfString=new RequestInfoVM();
	    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat parseTime = new SimpleDateFormat("hh:mm a");
			Date time = null;
			String msg = "success";
			
			try {
				 confirmDate = df.parse(vm.confirmDate);
				 time = parseTime.parse(vm.confirmTime);
				 
					if(vm.option==0) {
						ScheduleTest  scheduleTest = ScheduleTest.findById(vm.id);
						if(scheduleTest != null) {
							
							scheduleTest.setConfirmDate(confirmDate);
							scheduleTest.setConfirmTime(time);
							scheduleTest.setEmail(vm.email);
							vin = scheduleTest.vin;
							listOfString.name=scheduleTest.name;
							
							
							List<ScheduleTest> list = ScheduleTest.findByVin(vm.vin);
		    				for (ScheduleTest info2 : list) {
		    					if(info2.confirmDate != null && info2.confirmTime != null){
		    						if(info2.confirmDate.equals(confirmDate)){
		    							Date newDate = DateUtils.addHours(info2.confirmTime, 1);
		    							if((time.after(info2.confirmTime) && time.before(newDate)) || time.equals(info2.confirmTime)){
		    								msg = "error";
		    							}
		    						}
		    					}
							}
		    				List<RequestMoreInfo> list1 = RequestMoreInfo.findByVin(vm.vin);
		    				for (RequestMoreInfo info2 : list1) {
		    					if(info2.confirmDate != null && info2.confirmTime != null){
		    						if(info2.confirmDate.equals(confirmDate)){
		    							Date newDate = DateUtils.addHours(info2.confirmTime, 1);
		    							if((time.after(info2.confirmTime) && time.before(newDate)) || time.equals(info2.confirmTime)){
		    								msg = "error";
		    							}
		    						}
		    					}
							}
		    				List<TradeIn> list2 = TradeIn.findByVin(vm.vin);
		    				for (TradeIn info2 : list2) {
		    					if(info2.confirmDate != null && info2.confirmTime != null){
		    						if(info2.confirmDate.equals(confirmDate)){
		    							Date newDate = DateUtils.addHours(info2.confirmTime, 1);
		    							if((time.after(info2.confirmTime) && time.before(newDate)) || time.equals(info2.confirmTime)){
		    								msg = "error";
		    							}
		    						}
		    					}
							}
							if(msg.equals("success")){
								flag = true;
								scheduleTest.update();
								
								UserNotes uNotes = new UserNotes();
			    	    		uNotes.setNote("Test Drive Re-scheduled");
			    	    		uNotes.setAction("Other");
			    	    		uNotes.createdDate = currDate;
			    	    		uNotes.createdTime = currDate;
			    	    		uNotes.user = user;
				        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
			    	    		uNotes.scheduleTest = ScheduleTest.findById(scheduleTest.id);
			    	    		uNotes.save();
							}
						}	
					}else if(vm.option == 1) {
						RequestMoreInfo info = RequestMoreInfo.findById(vm.id);
						if(info!=null) {
							
							info.setConfirmDate(confirmDate);
							info.setConfirmTime(time);
							info.setEmail(vm.email);
							vin = info.vin;
							
							List<ScheduleTest> list = ScheduleTest.findByVin(vm.vin);
		    				for (ScheduleTest info2 : list) {
		    					if(info2.confirmDate != null && info2.confirmTime != null){
		    						if(info2.confirmDate.equals(confirmDate)){
		    							Date newDate = DateUtils.addHours(info2.confirmTime, 1);
		    							if((time.after(info2.confirmTime) && time.before(newDate)) || time.equals(info2.confirmTime)){
		    								msg = "error";
		    							}
		    						}
		    					}
							}
		    				List<RequestMoreInfo> list1 = RequestMoreInfo.findByVin(vm.vin);
		    				for (RequestMoreInfo info2 : list1) {
		    					if(info2.confirmDate != null && info2.confirmTime != null){
		    						if(info2.confirmDate.equals(confirmDate)){
		    							Date newDate = DateUtils.addHours(info2.confirmTime, 1);
		    							if((time.after(info2.confirmTime) && time.before(newDate)) || time.equals(info2.confirmTime)){
		    								msg = "error";
		    							}
		    						}
		    					}
							}
		    				List<TradeIn> list2 = TradeIn.findByVin(vm.vin);
		    				for (TradeIn info2 : list2) {
		    					if(info2.confirmDate != null && info2.confirmTime != null){
		    						if(info2.confirmDate.equals(confirmDate)){
		    							Date newDate = DateUtils.addHours(info2.confirmTime, 1);
		    							if((time.after(info2.confirmTime) && time.before(newDate)) || time.equals(info2.confirmTime)){
		    								msg = "error";
		    							}
		    						}
		    					}
							}
							if(msg.equals("success")){
								flag = true;
								info.update();
								
								UserNotes uNotes = new UserNotes();
			    	    		uNotes.setNote("Test Drive Re-scheduled");
			    	    		uNotes.setAction("Other");
			    	    		uNotes.createdDate = currDate;
			    	    		uNotes.createdTime = currDate;
			    	    		uNotes.user = user;
				        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
			    	    		uNotes.requestMoreInfo = RequestMoreInfo.findById(info.id);
			    	    		uNotes.save();
							}
							
						}
					}else if(vm.option == 2) {
						TradeIn info = TradeIn.findById(vm.id);
						if(info!=null) {
							
							info.setConfirmDate(confirmDate);
							info.setConfirmTime(time);
							info.setEmail(vm.email);
							vin = info.vin;
							
							List<ScheduleTest> list = ScheduleTest.findByVin(vm.vin);
		    				for (ScheduleTest info2 : list) {
		    					if(info2.confirmDate != null && info2.confirmTime != null){
		    						if(info2.confirmDate.equals(confirmDate)){
		    							Date newDate = DateUtils.addHours(info2.confirmTime, 1);
		    							if((time.after(info2.confirmTime) && time.before(newDate)) || time.equals(info2.confirmTime)){
		    								msg = "error";
		    							}
		    						}
		    					}
							}
		    				List<RequestMoreInfo> list1 = RequestMoreInfo.findByVin(vm.vin);
		    				for (RequestMoreInfo info2 : list1) {
		    					if(info2.confirmDate != null && info2.confirmTime != null){
		    						if(info2.confirmDate.equals(confirmDate)){
		    							Date newDate = DateUtils.addHours(info2.confirmTime, 1);
		    							if((time.after(info2.confirmTime) && time.before(newDate)) || time.equals(info2.confirmTime)){
		    								msg = "error";
		    							}
		    						}
		    					}
							}
		    				List<TradeIn> list2 = TradeIn.findByVin(vm.vin);
		    				for (TradeIn info2 : list2) {
		    					if(info2.confirmDate != null && info2.confirmTime != null){
		    						if(info2.confirmDate.equals(confirmDate)){
		    							Date newDate = DateUtils.addHours(info2.confirmTime, 1);
		    							if((time.after(info2.confirmTime) && time.before(newDate)) || time.equals(info2.confirmTime)){
		    								msg = "error";
		    							}
		    						}
		    					}
							}
							if(msg.equals("success")){
								flag = true;
								info.update();
								
								UserNotes uNotes = new UserNotes();
			    	    		uNotes.setNote("Test Drive Re-scheduled");
			    	    		uNotes.setAction("Other");
			    	    		uNotes.createdDate = currDate;
			    	    		uNotes.createdTime = currDate;
			    	    		uNotes.user = user;
				        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
			    	    		uNotes.tradeIn = TradeIn.findById(info.id);
			    	    		uNotes.save();
							}
							
						}
					}
				
					String userName=null;
					if(flag) {
			    		Map map = new HashMap();
			    		map.put("email",vm.email);
			    		map.put("email",vm.email);
			    		map.put("confirmDate", confirmDate);
			    		map.put("confirmTime", vm.confirmTime);
			    		map.put("CnfDateNature",vm.cnfDateNature);
			    		map.put("vin", vin);
			    		userName=user.firstName+" "+user.lastName;
			    		map.put("uname", user.firstName+" "+user.lastName);
			    		map.put("uphone", user.phone);
			    		map.put("uemail", user.email);
			    		map.put("clientName", listOfString.name);
			    	//	makeToDo(vm.vin);
			    		sendMailForReschedule(map);
			    	}
			} catch (Exception e) {
				e.printStackTrace();
			}
			/*if(vm.option==0) {
				ScheduleTest  scheduleTest = ScheduleTest.findById(vm.id);
				if(scheduleTest != null) {
					flag = true;
					try {
						confirmDate = df.parse(vm.confirmDate);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					scheduleTest.setConfirmDate(confirmDate);
					try {
						scheduleTest.setConfirmTime(parseTime.parse(vm.confirmTime));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					scheduleTest.setEmail(vm.email);
					vin = scheduleTest.vin;
					scheduleTest.update(); 
				}
			} else if(vm.option == 1) {
				RequestMoreInfo info = RequestMoreInfo.findById(vm.id);
				if(info!=null) {
					flag = true;
					try {
						confirmDate = df.parse(vm.confirmDate);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					info.setConfirmDate(confirmDate);
					try {
						info.setConfirmTime(parseTime.parse(vm.confirmTime));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					info.setEmail(vm.email);
					vin = info.vin;
					info.update();
				}
			} else if(vm.option == 2) {
				TradeIn info = TradeIn.findById(vm.id);
				if(info!=null) {
					flag = true;
					try {
						confirmDate = df.parse(vm.confirmDate);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					info.setConfirmDate(confirmDate);
					try {
						info.setConfirmTime(parseTime.parse(vm.confirmTime));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					info.setEmail(vm.email);
					vin = info.vin;
					info.update();
				}
			}
	    	if(flag) {
	    		Map map = new HashMap();
	    		map.put("email",vm.email);
	    		map.put("email",vm.email);
	    		map.put("confirmDate", confirmDate);
	    		map.put("confirmTime", vm.confirmTime);
	    		map.put("vin", vin);
	    		map.put("uname", user.firstName+" "+user.lastName);
	    		map.put("uphone", user.phone);
	    		map.put("uemail", user.email);
	    		makeToDo(vm.vin);
	    		sendMail(map);
	    	}*/
			listOfString.mesg=msg; 
	    	return ok(Json.toJson(listOfString));
	    }
	    
	    private static void sendMailForReschedule(Map map) {
	    	
	    	AuthUser logoUser = getLocalUser();
	    //AuthUser logoUser = AuthUser.findById(Integer.getInteger(session("USER_KEY")));
	    	SiteLogo logo = SiteLogo.findByLocation(Long.valueOf(session("USER_LOCATION"))); // findByUser(logoUser);
			
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
	    		/*InternetAddress[] usersArray = new InternetAddress[2];
	    		int index = 0;
	    		usersArray[0] = new InternetAddress(map.get("email").toString());
	    		usersArray[1] = new InternetAddress(map.get("custEmail").toString());*/
	    		
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(emailUser));
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(map.get("email").toString()));
				message.setSubject("Test Drive's Information has been changed");
				Multipart multipart = new MimeMultipart();
				BodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart = new MimeBodyPart();
				
				VelocityEngine ve = new VelocityEngine();
				ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
				ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
				ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
				ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
				ve.init();
			
				
		        Template t = ve.getTemplate("/public/emailTemplate/testDriveInfoChanged_HTML.vm"); 
		        VelocityContext context = new VelocityContext();
		        String months[] = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
		       
		        int dayOfmonth=1;
		        int month=0;
		        try {
		        	String arr[] = map.get("confirmDate").toString().split("-");
			        if(arr.length >=2){
			        	dayOfmonth = Integer.parseInt(arr[2]);
				        month = Integer.parseInt(arr[1]);
			        }else{
			        	Calendar cal = Calendar.getInstance();
				         cal.setTime((Date)map.get("confirmDate"));
				         dayOfmonth = cal.get(Calendar.DAY_OF_MONTH);
				         month = cal.get(Calendar.MONTH)+1;
			        }
				} catch (Exception e) {
					e.printStackTrace();
				}
		        
		        String monthName = months[month-1];
		        context.put("hostnameUrl", imageUrlPath);
		        context.put("siteLogo", logo.logoImagePath);
		        context.put("dayOfmonth", dayOfmonth);
		        context.put("monthName", monthName);
		        context.put("confirmTime", map.get("confirmTime"));
		        
		        Vehicle vehicle = Vehicle.findByVinAndStatus(map.get("vin").toString());
		        context.put("year", vehicle.year);
		        context.put("make", vehicle.make);
		        context.put("model", vehicle.model);
		        context.put("price", "$"+vehicle.price);
		        context.put("stock", vehicle.stock);
		        context.put("vin", vehicle.vin);
		        context.put("make", vehicle.make);
	              if(vehicle.mileage!= null){
		        	
		        	context.put("mileage",vehicle.mileage);
		        	 
		        }
		        else{
		        	context.put("mileage","");
		        }
	              if( map.get("clientName")!= null){
	  	        	
	            	  context.put("clientName", map.get("clientName"));
	  	        	 
	  	        }
	  	        else{
	  	        	 context.put("clientName","");
	  	        }
	             context.put("typeofVehicle", vehicle.typeofVehicle);
		        context.put("name", map.get("uname"));
		        context.put("email", map.get("uemail"));
		        context.put("phone",  map.get("uphone"));
		        String weather= map.get("CnfDateNature").toString();
		        String arr1[] = weather.split("&");
		        String nature=arr1[0];
		        String temp=arr1[1];
		        context.put("nature",nature);
		        context.put("temp", temp);
		        InventoryImage image = InventoryImage.getDefaultImage(vehicle.vin);
		        if(image!=null) {
		        	context.put("defaultImage", image.path);
		        } else {
		        	context.put("defaultImage", "");
		        }
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
	    }
	    
	    public static Result getAllTradeIn() {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	AuthUser user = (AuthUser) getLocalUser();
		    	List<TradeIn> listData = new ArrayList<>();
		    	if(user.role == null || user.role.equals("General Manager")) {
	    			listData = TradeIn.findAllData();
	    		} else {
	    			if(user.role.equals("Manager")) {
	    				listData = TradeIn.findAllLocationDataManager(Long.valueOf(session("USER_LOCATION")));
	    			} else {
	    				listData = TradeIn.findAllByLocationDate(Long.valueOf(session("USER_LOCATION")));
	    			}
	    		}
		    	List<RequestInfoVM> infoVMList = new ArrayList<>();
		    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		    	for(TradeIn info: listData) {
		    		RequestInfoVM vm = new RequestInfoVM();
		    		vm.id = info.id;
		    		Inventory productInfo = Inventory.getByProductId(info.productId);
		    		vm.productId = info.productId;
		    		if(productInfo != null) {
		    			vm.title = productInfo.title;
		    			vm.price = (int) productInfo.price;
		    			vm.description = productInfo.description;
		    			vm.cost = String.valueOf(productInfo.cost);
		    			AddCollection aCollection = AddCollection.findById(productInfo.collection.id);
		    			vm.collectionName = aCollection.title;
		    			InventoryImage pImage = InventoryImage.getDefaultImage(productInfo.productId);
		        		if(pImage!=null) {
		        			vm.imgId = pImage.getId().toString();
		        		}
		        		else {
		        			vm.imgId = "/assets/images/no-image.jpg";
		        		}
		    		}
		    		
		    		if(info.lastName != null){
		    			vm.name = info.firstName+" "+info.lastName;
		    		}else{
		    			vm.name = info.firstName;
		    		}
		    		
		    		vm.phone = info.phone;
		    		vm.email = info.email;
		    		vm.howContactedUs = info.contactedFrom;
		    		vm.howFoundUs = info.hearedFrom;
		    		vm.custZipCode = info.custZipCode;
		    		vm.enthicity = info.enthicity;
		    		vm.pdfPath = info.pdfPath;
		    		vm.userRole = user.role;
		    		vm.premiumFlagForSale = user.premiumFlag;
		    		
		    		if(info.assignedTo == null) {
		    			vm.status = "Unclaimed";
		    		} else {
			    		if(info.assignedTo != null && info.status == null) {
			    			vm.status = "In Progress";
			    		} else {
			    			vm.status = info.status;
			    		}
		    		}
		    		if(info.assignedTo != null) {
		    			vm.salesRep = info.assignedTo.getFirstName()+" "+info.assignedTo.getLastName();
		    		}
		    		vm.requestDate = df.format(info.tradeDate);
		    		if(info.isRead == 0) {
		    			vm.isRead = false;
		    		}
		    		
		    		if(info.isRead == 1) {
		    			vm.isRead = true;
		    		}
		    		
		    		findCustomeData(info.id,vm,3L);
		    		infoVMList.add(vm);
		    	}
		    	
		    	return ok(Json.toJson(infoVMList));
	    	}	
	    }
	    
	    public static Result tradeInMarkRead(String flag,Long id) {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		Date currDate = new Date();
	    		AuthUser user = (AuthUser) getLocalUser();
		    	TradeIn tradeObjV = TradeIn.findById(id);
		    	if(flag.equals("true")) {
		    		tradeObjV.setIsRead(1);
		    		tradeObjV.setAssignedTo(user);
		    		
		    		UserNotes uNotes = new UserNotes();
		    		uNotes.setNote("Lead has been claimed");
		    		uNotes.setAction("Other");
		    		uNotes.createdDate = currDate;
		    		uNotes.createdTime = currDate;
		    		uNotes.user = user;
	        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    		uNotes.tradeIn = TradeIn.findById(tradeObjV.id);
		    		uNotes.save();
				}
				if(flag.equals("false")) {
					tradeObjV.setIsRead(0);
					tradeObjV.setAssignedTo(null);
				}
				tradeObjV.update();
				
				List<TradeIn> listData = new ArrayList<>();
	    		if(user.role == null) {
	    			listData = TradeIn.findAllData();
	    		} else {
	    			if(user.role.equals("General Manager")) {
	    				listData = TradeIn.findAllData();
	    			} else {
	    				listData = TradeIn.findAllByDate();
	    			}
	    		}
		    	List<RequestInfoVM> infoVMList = new ArrayList<>();
		    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		    	for(TradeIn info: listData) {
		    		RequestInfoVM vm = new RequestInfoVM();
		    		vm.id = info.id;
		    		Inventory productInfo = Inventory.getByProductId(info.productId);
		    		vm.productId = info.productId;
		    		if(productInfo != null) {
		    			vm.title = productInfo.title;
		    			vm.price = (int) productInfo.price;
		    			vm.description = productInfo.description;
		    			vm.cost = String.valueOf(productInfo.cost);
		    			AddCollection aCollection = AddCollection.findById(productInfo.collection.id);
		    			vm.collectionName = aCollection.title;
		    			InventoryImage pImage = InventoryImage.getDefaultImage(productInfo.productId);
		        		if(pImage!=null) {
		        			vm.imgId = pImage.getId().toString();
		        		}
		        		else {
		        			vm.imgId = "/assets/images/no-image.jpg";
		        		}
		    		}
		    		if(info.lastName != null){
		    			vm.name = info.firstName+" "+info.lastName;
		    		}else{
		    			vm.name = info.firstName;
		    		}

		    		vm.phone = info.phone;
		    		vm.email = info.email;
		    		vm.pdfPath = info.pdfPath;
		    		vm.requestDate = df.format(info.tradeDate);
		    		
		    		if(info.assignedTo == null) {
		    			vm.status = "Unclaimed";
		    		} else {
			    		if(info.assignedTo != null && info.status == null) {
			    			vm.status = "In Progress";
			    		} else {
			    			vm.status = info.status;
			    		}
		    		}
		    		if(info.assignedTo != null) {
		    			vm.salesRep = info.assignedTo.getFirstName()+" "+info.assignedTo.getLastName();
		    		}
		    		
		    		if(info.isRead == 0) {
		    			vm.isRead = false;
		    		}
		    		
		    		if(info.isRead == 1) {
		    			vm.isRead = true;
		    		}
		    		infoVMList.add(vm);
		    	}
		    	
		    	return ok(Json.toJson(infoVMList));
	    	}	
	    	
	    }
	    
	    
	    public static Result exportOtherLeadsData(String leadId){
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
	       	    String filePath = rootDir+File.separator+"CsvFile/Request.csv";
	    		
	    		
	    		try {
	       			Boolean sts = FileUtils.deleteQuietly(new File(filePath));
	    			} catch (Exception e) {
	    				e.printStackTrace();
	    			}
	    		Map<String, String> mapOffline = new HashMap<String, String>();
	    		String uniquetitle = null;
	    		ArrayList<CreateNewFormVM> create = new ArrayList<>(); 
	    		
	    		if(!leadId.equals("0")){
	    			List<CustomizationDataValue> custdata = CustomizationDataValue.findByRequestId(Long.parseLong(leadId));
		    		for(CustomizationDataValue cust : custdata){
		    			CreateNewFormVM vm = new CreateNewFormVM();
		    			
		    			String langValue = mapOffline.get(cust.keyValue); 
		            	if (langValue == null) {
		            		uniquetitle = cust.keyValue;
		            		mapOffline.put(cust.keyValue, cust.keyValue);
		            		vm.name = cust.keyValue;
		            	}
	 					create.add(vm);
		    		}
	    		}
	    		String leadName = " ";
	    		for(CreateNewFormVM formvm : create){
	    			if(formvm.name != null){
	    				leadName = leadName + ",";
	    				leadName = leadName + formvm.name;
	    			}
	    		}
	    		
	    		String FILE_HEADER = "Id,Name,Email,Phone,RequestDate,RequestTime,IsRead,RichNotification,Status,Reason,IsScheduled,ConfirmDate,ConfirmTime,IsReassigned,ContactedFrom,CustZipCode,ScheduleEmail,StatusDate,StatusTime,PremiumFlag,OnlineOrOfflineLeads,IsContactusType,Message,ProductId,Section,PdfPath,NotifFlag,AssignedTo,User,Locations"+leadName;
	    		//PreferredContact ,Vin,BestDay,BestTime,ScheduleDate,LeadStatus,HearedFrom,Enthicity,TestDriveCompletedComment,TestDriveCompletedDuration,ParentId,TestDriveStatus,
	    		try {
	    			fileWriter = new FileWriter(filePath);
	        		fileWriter.append(FILE_HEADER.toString());
	        		fileWriter.append(NEW_LINE_SEPARATOR);
	        		List<RequestMoreInfo> list= null;
	        		if(leadId.equals("0")){
	        			 list = RequestMoreInfo.getAllRequest();
	        		}else{
	        			 list = RequestMoreInfo.findByContactUsType(Long.valueOf(session("USER_LOCATION")),leadId);
	        		}
	        		AddCollection pro = null;
	        		LeadType lead = null;
	        		AuthUser auth = null;
	        		AuthUser user = null;
	        		Location location = null;
	        		List<CustomizationDataValue> custdata1 = null;
	        		for (RequestMoreInfo request : list) {
	        			if(request.productId != null){
	        				 pro = AddCollection.findById(Long.parseLong(request.productId));
	        			}
	        			if(request.isContactusType != null){
	        				lead = LeadType.findById(Long.parseLong(request.isContactusType));
	        			}
	        			if(request.assignedTo != null){
	        				auth = AuthUser.findById(request.assignedTo.id);
	        			}
	        			if(request.user != null){
	        				user = AuthUser.findById(request.user.id);
	        			}
	        			if(request.locations != null){
	        				location = Location.findById(request.locations.id);
	        			}
	        			if(request.id != null){
	        				custdata1 = CustomizationDataValue.findByRequestLeadId(request.id);
	        			}
	        			fileWriter.append(String.valueOf(request.id));
	            		fileWriter.append(COMMA_DELIMITER);
	            		if(request.name != null){
	            			fileWriter.append(String.valueOf(request.name));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}
	            		else{
	            			fileWriter.append(String.valueOf(" "));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}
	            		if(request.email != null){
	            			fileWriter.append(String.valueOf(request.email));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}else{
	            			fileWriter.append(String.valueOf(""));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}
	        			if(request.phone != null){
	        				fileWriter.append(String.valueOf(request.phone));
		            		fileWriter.append(COMMA_DELIMITER);
	        			}
	        			else{
	        				fileWriter.append(String.valueOf(""));
		            		fileWriter.append(COMMA_DELIMITER);
	        			}
	        			if(request.requestDate != null){
	        				fileWriter.append(String.valueOf(request.requestDate));
		            		fileWriter.append(COMMA_DELIMITER);
	        			}
	        			else{
	        				fileWriter.append(String.valueOf(""));
		            		fileWriter.append(COMMA_DELIMITER);
	        			}
	        			if(request.requestTime != null){
	        				fileWriter.append(String.valueOf(request.requestTime));
		            		fileWriter.append(COMMA_DELIMITER);
	        			}
	        			else{
	        				fileWriter.append(String.valueOf(""));
		            		fileWriter.append(COMMA_DELIMITER);
	        			}
	        			fileWriter.append(String.valueOf(request.isRead));
		            	fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(request.richNotification));
	            		fileWriter.append(COMMA_DELIMITER);
	            		if(request.status != null){
	            			fileWriter.append(String.valueOf(request.status));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}
	            		else{
	            			fileWriter.append(String.valueOf(""));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}
	            		if(request.reason != null){
	            			fileWriter.append(String.valueOf(request.reason));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}
	            		else{
	            			fileWriter.append(String.valueOf(""));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}
	            		if(request.isScheduled != null){
	            			fileWriter.append(String.valueOf(request.isScheduled));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}
	            		else{
	            			fileWriter.append(String.valueOf(""));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}
	            		if(request.confirmDate != null){
	            			fileWriter.append(String.valueOf(request.confirmDate));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}
	            		else{
	            			fileWriter.append(String.valueOf(""));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}
	            		if(request.confirmTime != null){
	            			fileWriter.append(String.valueOf(request.confirmTime));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}
	            		else{
	            			fileWriter.append(String.valueOf(""));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}
	            		if(request.isReassigned != null){
	            			fileWriter.append(String.valueOf(request.isReassigned));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}
	            		else{
	            			fileWriter.append(String.valueOf(""));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}
	            		if(request.contactedFrom != null){
	            			fileWriter.append(String.valueOf(request.contactedFrom));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}
	            		else{
	            			fileWriter.append(String.valueOf(""));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}
	            		if(request.custZipCode != null){
	            			fileWriter.append(String.valueOf(request.custZipCode));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}
	            		else{
	            			fileWriter.append(String.valueOf(""));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}
	        			fileWriter.append(String.valueOf(request.scheduleEmail));
	            		fileWriter.append(COMMA_DELIMITER);
	            		if(request.statusDate != null){
	            			fileWriter.append(String.valueOf(request.statusDate));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}
	            		else{
	            			fileWriter.append(String.valueOf(""));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}
	            		if(request.statusTime != null){
	            			fileWriter.append(String.valueOf(request.statusTime));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}
	            		else{
	            			fileWriter.append(String.valueOf(""));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}
	            		if(request.premiumFlag != null){
	            			fileWriter.append(String.valueOf(request.premiumFlag));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}
	            		else{
	            			fileWriter.append(String.valueOf(""));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}
	        			fileWriter.append(String.valueOf(request.onlineOrOfflineLeads));
	            		fileWriter.append(COMMA_DELIMITER);
	            		if(lead.leadName != null){
	            			fileWriter.append(String.valueOf(lead.leadName));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}
	            		else{
	            			fileWriter.append(String.valueOf(""));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}
	            		if(request.message != null){
	            			fileWriter.append(String.valueOf(request.message));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}
	            		else{
	            			fileWriter.append(String.valueOf(""));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}
	            		if(pro.title != null){
	            			fileWriter.append(String.valueOf(pro.title));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}
	            		else{
	            			fileWriter.append(String.valueOf(""));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}
	            		if(request.section != null){
	            			fileWriter.append(String.valueOf(request.section));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}
	            		else{
	            			fileWriter.append(String.valueOf(""));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}
	            		if(request.pdfPath != null){
	            			fileWriter.append(String.valueOf(request.pdfPath));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}
	            		else{
	            			fileWriter.append(String.valueOf(""));
		            		fileWriter.append(COMMA_DELIMITER);
	            		}
	        			fileWriter.append(String.valueOf(request.notifFlag));
	        			fileWriter.append(COMMA_DELIMITER);
	        			if(auth.firstName != null){
	        				fileWriter.append(String.valueOf(auth.firstName));
		            		fileWriter.append(COMMA_DELIMITER);
	        			}
	        			else{
	        				fileWriter.append(String.valueOf(""));
		            		fileWriter.append(COMMA_DELIMITER);
	        			}
	            		if(request.user != null){
	            			fileWriter.append(String.valueOf(user.firstName));
	            			fileWriter.append(COMMA_DELIMITER);
	            		}else{
	            			fileWriter.append(String.valueOf(""));
	            			fileWriter.append(COMMA_DELIMITER);
	            		}
	            		if(location.name != null){
	            			fileWriter.append(String.valueOf(location.name));
		        			fileWriter.append(COMMA_DELIMITER);
	            		}
	            		else{
	            			fileWriter.append(String.valueOf(""));
		        			fileWriter.append(COMMA_DELIMITER);
	            		}
		        		for(CustomizationDataValue cust1 : custdata1){
		        			if(cust1.value != null){
		        				fileWriter.append(String.valueOf(cust1.value));
			            		fileWriter.append(COMMA_DELIMITER);
		        			}
	        			}
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

}