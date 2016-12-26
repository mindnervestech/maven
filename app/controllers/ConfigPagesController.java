package controllers;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.AddProduct;
import models.AuthUser;
import models.AutoPortal;
import models.CoverImage;
import models.CreateNewForm;
import models.CustomerPdf;
import models.CustomerRequest;
import models.CustomerRequestManufacturerSettings;
import models.CustomizationDataValue;
import models.CustomizationForm;
import models.Domain;
import models.EmailDetails;
import models.FeaturedImageConfig;
import models.InternalPdf;
import models.InventorySetting;
import models.LeadType;
import models.Location;
import models.MailchimpList;
import models.MailchimpSchedular;
import models.MarketingAcounts;
import models.MyProfile;
import models.NewFormWebsite;
import models.NewsletterDate;
import models.OutcomeMenu;
import models.Permission;
import models.PhotographerHoursOfOperation;
import models.PremiumLeads;
import models.RequestMoreInfo;
import models.SalesPersonZipCode;
import models.Site;
import models.SliderImageConfig;
import models.State;
import models.VehicleImageConfig;
import models.WebAnalytics;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import scheduler.NewsLetter;
import securesocial.core.Identity;
import viewmodel.AddProductVM;
import viewmodel.AutoPortalVM;
import viewmodel.CreateNewFormVM;
import viewmodel.CustomerRequestManufacrurerSettingsVM;
import viewmodel.CustomerRequestVM;
import viewmodel.DocumentationVM;
import viewmodel.ImageVM;
import viewmodel.InventorySettingVM;
import viewmodel.LeadTypeVM;
import viewmodel.MailchimpPageVM;
import viewmodel.NewFormWebsiteVM;
import viewmodel.SalePersonZipCodeVM;
import viewmodel.SiteVM;
import viewmodel.UserVM;
import viewmodel.WebAnalyticsVM;
import viewmodel.ZipCodeVM;
import viewmodel.domainVM;

public class ConfigPagesController extends Controller{
	
	
	final static String rootDir = Play.application().configuration()
			.getString("image.storage.path");
	
	final static String pdfRootDir = Play.application().configuration()
			.getString("pdfRootDir");
	
	final static String userRegistration = Play.application().configuration()
			.getString("userRegistration");
	
	final static String imageUrlPath = Play.application().configuration()
			.getString("image.url.path");
	
	final static String vehicleUrlPath = Play.application().configuration()
			.getString("vehicle.url.path");
	
	final static String mashapeKey = Play.application().configuration()
			.getString("mashapeKey");
			
	final static String emailUsername = Play.application().configuration()
			.getString("mail.username");
	
	final static String emailPassword = Play.application().configuration()
			.getString("mail.password");
	
	
		public static Result saveWebsite(){
		
    		Form<WebAnalyticsVM> form = DynamicForm.form(WebAnalyticsVM.class).bindFromRequest();
    		WebAnalyticsVM vm = form.get();
    		
    		WebAnalytics analytics =  WebAnalytics.findByLocations(Long.valueOf(session("USER_LOCATION")));
			if(analytics == null){
    			WebAnalytics web=new WebAnalytics();
    			web.tracking_code = vm.tracking_code;
    			web.locations=Location.findById(Long.valueOf(session("USER_LOCATION")));
    			web.save();
			}
			else{
				analytics.setTracking_code(vm.tracking_code);
				analytics.update();
			}
    		return ok();
    	}
		
		public static Result updateListItem(){
			String msg = "";
			Form<MailchimpList> form = DynamicForm.form(MailchimpList.class).bindFromRequest();
			MailchimpList vm = form.get();
			try {
				vm.update();
			} catch (Exception e) {
				msg = "error";
			}
			return ok(msg);
		}
		
		public static Result saveNewList(){
			String msg = "";
			Form<MailchimpList> form = DynamicForm.form(MailchimpList.class).bindFromRequest();
			MailchimpList vm = form.get();
			try {
				vm.save();
			} catch (Exception e) {
				msg = "error";
			}
			return ok(msg);
		}
		
		public static Result deleteList(){
			Form<MailchimpList> form = DynamicForm.form(MailchimpList.class).bindFromRequest();
			MailchimpList vm = form.get();
			try {
				vm.delete();
			} catch (Exception e) {
				e.printStackTrace();
				return ok("error");
			}
			return ok();
		}
		
		public static Result savemailchimpPage(){
			
    		Form<MailchimpPageVM> form = DynamicForm.form(MailchimpPageVM.class).bindFromRequest();
    		MailchimpPageVM vm = form.get();
    		Date date = new Date();
    		
    		MailchimpSchedular lead =  MailchimpSchedular.findByLocations(Long.valueOf(session("USER_LOCATION")));
			if(lead == null){
				MailchimpSchedular mail=new MailchimpSchedular();
    			mail.schedularTime = vm.schedularTime;
    			mail.locations=Location.findById(Long.valueOf(session("USER_LOCATION")));
    			mail.currDate = date;
    			mail.mailchimpUserName = vm.mailchimpUserName;
    			mail.mailchimpPassword = vm.mailchimpPassword;
    			mail.apikey = vm.apikey;
    			mail.list = vm.list;
    			mail.synchronizeContact = vm.synchronizeContact;
    			mail.save();
			}
			else{
				lead.setSchedularTime(vm.schedularTime);
				lead.setCurrDate(date);
				lead.setMailchimpUserName(vm.mailchimpUserName);
				lead.setMailchimpPassword(vm.mailchimpPassword);
				lead.setApikey(vm.apikey);
				lead.setList(vm.list);
				lead.setSynchronizeContact(vm.synchronizeContact);
				lead.update();
    			
			}
    		return ok();
    	}
		
		public static Result getAllMailchimpList(){
			List<MailchimpList> list = MailchimpList.getAll();
			return ok(Json.toJson(list));
		}
		
		
		public static Result getAlloutcome(){
			List<OutcomeMenu> outmenu = OutcomeMenu.getAllData();
			return ok(Json.toJson(outmenu));
		}
		
		public static Result getwebsiteAnalyticsData() {
			
			WebAnalytics lead = WebAnalytics.findByLocations(Long.valueOf(session("USER_LOCATION")));
			
			return ok(Json.toJson(lead)); 
			
		}
		
		public MailchimpSchedular getmailchimpApikey() {
			MailchimpSchedular lead = MailchimpSchedular.findByLocations(Long.valueOf(session("USER_LOCATION")));
			return lead; 
		}

		public static Result getmailchimpData() {
			
			MailchimpSchedular lead = MailchimpSchedular.findByLocations(Long.valueOf(session("USER_LOCATION")));
			
			return ok(Json.toJson(lead)); 
			
		}

		public static Result getImageConfig() {
	    	
		    	AuthUser user = (AuthUser) getLocalUser();
		    	Map<String,Object> map = new HashMap<>();
		    	SliderImageConfig config = SliderImageConfig.findByUser(user);
		    	if(config != null) {
			    	ImageVM vm1 = new ImageVM();
			    	vm1.width = config.cropWidth;
			    	vm1.height = config.cropHeight;
			    	map.put("slider", vm1);
		    	}
		    	FeaturedImageConfig config2 = FeaturedImageConfig.findByUser(user);
		    	if(config2 != null) {
			    	ImageVM vm2 = new ImageVM();
			    	vm2.width = config2.cropWidth;
			    	vm2.height = config2.cropHeight;
			    	map.put("featured", vm2);
		    	}
		    	
		    	CoverImage image=CoverImage.findByLocation(Long.valueOf(session("USER_LOCATION")));
		    	if(image != null){
		    		
		    		ImageVM vm2 = new ImageVM();
			    	vm2.width = image.cropWidth;
			    	vm2.height = image.cropHeight;
			    	map.put("coverData", vm2);
		    		
		    	}
		    	
		    	
		    	VehicleImageConfig conf=VehicleImageConfig.findByLocation(Long.valueOf(session("USER_LOCATION")));
		    	if(conf != null){
		    		
		    		ImageVM vm2 = new ImageVM();
			    	vm2.width = conf.cropWidth;
			    	vm2.height = conf.cropHeight;
			    	map.put("vehicleImageConfig", vm2);
		    		
		    	}
		    	
		    	
		    	
		    	List<NewsletterDate> objList = NewsletterDate.findAll();
		    	SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
		    	if(objList.size() > 0) {
		    		map.put("NewsletterDate", objList.get(0).dateOfMonth);
		    		if(objList.get(0).newsletterTime != null) {
		    			map.put("newsletterTime", df.format(objList.get(0).newsletterTime));
		    		}
		    		map.put("NewsletterId", objList.get(0).id);
		    		map.put("NewsletterTimeZone", objList.get(0).timeZone);
		    	} else {
		    		map.put("NewsletterId", 0);
		    		map.put("NewsletterTimeZone", "");
		    	}
		    	
		       PremiumLeads pLeads = PremiumLeads.findByLocation(Long.valueOf(session("USER_LOCATION")));
		       if(pLeads != null){	
		    	   map.put("premiumLeads", pLeads);
		       }  
		       
		       Domain domain = Domain.findByLocation(Long.valueOf(session("USER_LOCATION")));
		       if(domain != null){
		    	   domainVM dVm = new domainVM();
		    	   dVm.domain = domain.domain;
		    	   dVm.hostingProvider = domain.hostingProvider;
		    	   dVm.userName = domain.userName;
		    	   dVm.password = domain.password;
		    	   map.put("domain", dVm);
		       }
		    	
		    	return ok(Json.toJson(map));
	    	
	    }
		
		public static AuthUser getLocalUser() {
	    	String id = session("USER_KEY");
	    	AuthUser user = AuthUser.find.byId(Integer.parseInt(id));
	    	//AuthUser user = getLocalUser();
			return user;
		}
		
		public static Result getAllSalesUsers(){
	    	
	    		AuthUser users = getLocalUser();
	    		List<AuthUser> userList = AuthUser.findByLocatioUsers(users.location);
	    		List<UserVM> vmList = new ArrayList<>();
	    	
	    		for(AuthUser user : userList) {
	    			List<String> parmi = new ArrayList<>();
	    			UserVM vm = new UserVM();
	    			vm.fullName = user.firstName + " "+ user.lastName;
	    			vm.firstName = user.firstName;
	    			vm.lastName = user.lastName;
	    			vm.email = user.email;
	    			vm.phone = user.phone;
	    			vm.userType = user.role;
	    			vm.commission =user.commission;
	    			vm.contractDur = user.contractDur;
	    			vm.age = user.age;
	    			vm.userGender = user.userGender;
	    			vm.experience = user.experience;
	    			vm.trainingPro = user.trainingPro;
	    			vm.salary = user.salary;
	    			vm.trialPeriod = user.trialPeriod;
	    			vm.trainingCost = user.trainingCost;
	    			vm.trainingHours = user.trainingHours;
	    			vm.quota = user.quota;
	    			vm.premiumFlag = user.premiumFlag;
	    			vm.imageName = user.imageName;
	    			vm.imageUrl = user.imageUrl;
	    			vm.trial = user.trial;
	    			vm.id = user.id;
	    			vm.priceStart = user.priceStart;
	    			vm.priceEnd = user.priceEnd;
	    			for(Permission permission:user.permission){
	    				parmi.add(permission.name);
	    			}
	    			vm.permissions = parmi;
	    			if(user.role.equals("Photographer")){
	    				SimpleDateFormat parseTime = new SimpleDateFormat("hh:mm a");
	    				
	    				PhotographerHoursOfOperation pOperation = PhotographerHoursOfOperation.findByUser(user);
	    				if(pOperation != null){
	    					if(pOperation.friOpen != null){
	    						if(pOperation.friOpen == 0){
	    							vm.hOperation.friOpen = false;
	    						}else{
	    							vm.hOperation.friOpen = true;
	    						}
	        				}
	        				if(pOperation.tueOpen != null){
	        					if(pOperation.tueOpen == 0){
	    							vm.hOperation.tueOpen = false;
	    						}else{
	    							vm.hOperation.tueOpen = true;
	    						}
	        				}
	        				if(pOperation.thuOpen != null){
	        					if(pOperation.thuOpen == 0){
	    							vm.hOperation.thuOpen = false;
	    						}else{
	    							vm.hOperation.thuOpen = true;
	    						}
	        				}
	        				if(pOperation.wedOpen != null){
	        					if(pOperation.wedOpen == 0){
	    							vm.hOperation.wedOpen = false;
	    						}else{
	    							vm.hOperation.wedOpen = true;
	    						}
	        				}
	        				if(pOperation.monOpen != null){
	        					if(pOperation.monOpen == 0){
	    							vm.hOperation.monOpen = false;
	    						}else{
	    							vm.hOperation.monOpen = true;
	    						}
	        				}
	        				if(pOperation.satOpen != null){
	        					if(pOperation.satOpen == 0){
	    							vm.hOperation.satOpen = false;
	    						}else{
	    							vm.hOperation.satOpen = true;
	    						}
	        				}
	        				if(pOperation.sunOpen != null){
	        					if(pOperation.sunOpen == 0){
	    							vm.hOperation.sunOpen = false;
	    						}else{
	    							vm.hOperation.sunOpen = true;
	    						}
	        				}
	        				
	        				if(pOperation.friOpenTime != null){
	        					vm.hOperation.friOpenTime = parseTime.format(pOperation.friOpenTime);
	        				}
	        				if(pOperation.tueOpenTime != null){
	        					vm.hOperation.tueOpenTime = parseTime.format(pOperation.tueOpenTime);
	        				}
	        				if(pOperation.thuOpenTime != null){
	        					vm.hOperation.thuOpenTime = parseTime.format(pOperation.thuOpenTime);
	        				}
	        				if(pOperation.wedOpenTime != null){
	        					vm.hOperation.wedOpenTime = parseTime.format(pOperation.wedOpenTime);
	        				}
	        				if(pOperation.monOpenTime != null){
	        					vm.hOperation.monOpenTime = parseTime.format(pOperation.monOpenTime);
	        				}
	        				if(pOperation.satOpenTime != null){
	        					vm.hOperation.satOpenTime = parseTime.format(pOperation.satOpenTime);
	        				}
	        				if(pOperation.sunOpenTime != null){
	        					vm.hOperation.sunOpenTime = parseTime.format(pOperation.sunOpenTime);
	        				}
	    				}
	    				
	    				
	    			}
	    			
	    			if(!vm.userType.equals("Manager")){
	    				vmList.add(vm);
	    			}
	    		}
	    		return ok(Json.toJson(vmList));
	    	
	    }
		
		public static Result getSocialMediadetail(){
			
	    		DocumentationVM vm= new DocumentationVM();
	    		AuthUser user=(AuthUser)getLocalUser();
	    		MyProfile profile=MyProfile.findByUser(user);
	    		if(profile != null){
	    			vm.facebookLink=profile.facebook;
	    		}
	    		if(profile != null){
	    			vm.googleLink=profile.googleplus;
	    		}
	    		if(profile != null){
	    			vm.instagramLink=profile.instagram;
	    		}
	    		if(profile != null){
	    			vm.twitterLink=profile.twitter;
	    		}
	    		if(profile != null){
	    			vm.pinterestLink=profile.pinterest;
	    		}
	    		if(profile != null){
	    			vm.yelpLink=profile.yelp;
	    		}
	    		
	    		return ok(Json.toJson(vm));
	    	
		}
		
		 public static Result getCustomerPdfData(){
				
		    		List<CustomerPdf> list = CustomerPdf.getAllPdfData();
		    		List<DocumentationVM> modelList = new ArrayList<>();
		    		
		    		for (CustomerPdf pdf : list) {
		    			DocumentationVM vm=new DocumentationVM();
		    			vm.customerPdfName=pdf.pdf_name;
		    			vm.customerPdfId=pdf.id;
		    			modelList.add(vm);
					}
		    		
		    		return ok(Json.toJson(modelList));
		    	
			}
		 
		 public static Result deleteLeadType(Long leadId){
			 AuthUser user=(AuthUser)getLocalUser();
			 List<RequestMoreInfo> reInfo = RequestMoreInfo.findAllOtherLeadIdUser(leadId.toString(),user);
			 boolean isDeleted = true;
			 for (RequestMoreInfo rInfo : reInfo) {				
				 if(!"CANCEL".equalsIgnoreCase(rInfo.getStatus()) && !"Sold".equalsIgnoreCase(rInfo.getStatus())){
					 isDeleted = false;
					 break;
				 }
			}
			 if(isDeleted){
				 LeadType type = LeadType.findById(leadId);
				 type.setDeleted(true);
				 type.update();
				 /*NewFormWebsite nWebsite = NewFormWebsite.findByName(type.leadName);
				 if(nWebsite != null){
					 nWebsite.delete();
				 }*/
				
				 return ok();
			 }else{
				 return ok("not delete");
			 }
			 
			 
			 
		 }
		 
		 public static Result getEmailDetails(){
				
		    		EmailDetails detail=EmailDetails.findByLocation(Long.valueOf(session("USER_LOCATION")));
		    		AutoPortalVM vm =new AutoPortalVM();
		    		if(detail != null){
		    			vm.name=detail.name;
		    			vm.username=detail.username;
		    			vm.host=detail.host;
		    			vm.port=detail.port;
		    			vm.passward=detail.passward;
		    			
		    			
		    		}
		    		
		    		return ok(Json.toJson(vm));
		    	
			}
		 
		 public static Result getInternalPdfData(){
				
		    		List<InternalPdf> list = InternalPdf.findByLocation(Long.valueOf(session("USER_LOCATION")));
		    		List<DocumentationVM> modelList = new ArrayList<>();
		    		
		    		for (InternalPdf pdf : list) {
		    			DocumentationVM vm=new DocumentationVM();
		    			vm.internalPdfName=pdf.pdf_name;
		    			vm.internalPdfId=pdf.id;
		    			modelList.add(vm);
					}
		    		
		    		return ok(Json.toJson(modelList));
		    	
			}
		 
		 public static Result getAllSites() {
		    	
		    	List<Site> siteList = Site.getAllSites();
		    	List<SiteVM> vmList = new ArrayList<>();
		    	for(Site site: siteList) {
		    		SiteVM siteVM = new SiteVM();
		    		siteVM.id = site.getId();
		    		siteVM.name = site.getName();
		    		vmList.add(siteVM);
		    	}
		    	
		    	return ok(Json.toJson(vmList));
		    	
		    }
		 
		 
		 public static Result getsystemInfo(){
		    	
		    		List<CreateNewForm> formList = CreateNewForm.findByLocation(Long.valueOf(session("USER_LOCATION")));
		    		//List <CreateNewForm> formList = CreateNewForm.getAllData();
			    	SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
			    	
			    	ArrayList<CreateNewFormVM> formVMs = new ArrayList<>(); 
			     	for(CreateNewForm vm : formList){
			     		//VehicleImage vehicleImg = VehicleImage.getDefaultImage(vm.vin);
			     		CreateNewFormVM form = new CreateNewFormVM();
			     		form.id = vm.id;
			     		form.name = vm.name;
			     		
			     		formVMs.add(form);
			  	}
			     	
			     	return ok(Json.toJson(formVMs));
		    }
		 
		 public static Result getLeadTypeData(){
		    	
		    		List<LeadType> leadtypeObjList = LeadType.findByLocation(Long.valueOf(session("USER_LOCATION")));
		    		//List <LeadType> leadtypeObjList = LeadType.getLeadData();
			    	SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
			    	
			    	ArrayList<LeadTypeVM> leadVMs = new ArrayList<>(); 
			     	for(LeadType vm : leadtypeObjList){
			     		//VehicleImage vehicleImg = VehicleImage.getDefaultImage(vm.vin);
			     		LeadTypeVM lead = new LeadTypeVM();
			     		lead.id = vm.id;
			     		lead.leadName = vm.leadName;
			     		lead.checkValue = vm.shows;
			     		lead.callToAction = vm.callToAction;
			     		lead.actionClientPdf = vm.actionClientPdf;
			     		lead.actionOutcomes = vm.actionOutcomes;
			     		lead.actionTitle = vm.actionTitle;
			     		
			     		leadVMs.add(lead);
			  	}
			     	
			     	return ok(Json.toJson(leadVMs));
		    }
		 
		 public static Result allFormName() {
				
				List<CreateNewForm> formname = CreateNewForm.findByLocation(Long.valueOf(session("USER_LOCATION")));
				return ok(Json.toJson(formname)); 
				
			}
		 
		 public static Result getCheckButton(Long leadId,Integer intValue){
				LeadType lType = LeadType.findById(leadId);
				lType.setShows(intValue);
				if(intValue == 1){
					lType.setCallToAction(true);
				}else if(intValue == 0){
					lType.setCallToAction(false);
				}
				lType.update();
				return ok();
			}
		 
		 public static Result getAllLeadData() {
				
				List<LeadType> lead = LeadType.findByLocation(Long.valueOf(session("USER_LOCATION")));
				//List<LeadType> lead = LeadType.getLeadData();
				return ok(Json.toJson(lead)); 
				
			}
		 
		 public static Result getAllManufacturer() {
				List<AddProduct> manufact = AddProduct.getProductByDraftStatus();
				return ok(Json.toJson(manufact)); 
			}
		 
		 public static Result getAllFrontAndSalesPer() {
				List<AuthUser> frontAndSales = AuthUser.getAllSalesAndFrontUser();
				return ok(Json.toJson(frontAndSales)); 
			}
		 public static Result getAllCustomerManufacturer() {
				List<CustomerRequestManufacturerSettings> frontAndSales = CustomerRequestManufacturerSettings.getAllcustManufactList();
				return ok(Json.toJson(frontAndSales)); 
			}
		 public static Result getAllSalesPersonZipCode() {
				List<SalesPersonZipCode> frontAndSales = SalesPersonZipCode.getAllcustManufactList();
				return ok(Json.toJson(frontAndSales)); 
			}
		 
		 public static Result getAllStateCodes() {
				List<State> frontAndSales = State.getAllStateCodes();
				return ok(Json.toJson(frontAndSales)); 
			}
		 
		 public static Result getAllSalesPersons() {
				List<AuthUser> user = AuthUser.getAllSalesUser();
				return ok(Json.toJson(user)); 
			}
		 
		 public static Result updateProductName(Long id,String name) {
				CreateNewForm cForm = CreateNewForm.findById(id);
				if(cForm != null){
					if(!cForm.name.equals("Create New Lead")){
							CustomizationForm cDataValue = CustomizationForm.findByLeadType(cForm.name);
						if(cDataValue != null){
							cDataValue.setDataType(name);
							cDataValue.update();
						}
						List<CustomizationDataValue> cValue = CustomizationDataValue.findByformName(cForm.name);
						for(CustomizationDataValue cu:cValue){
							cu.setFormName(name);
							cu.update();
						}
						cForm.setName(name);
						cForm.update();
					}
					
				}
				return ok(); 
			}
		 
		 
		 
		public static Result saveManfactSales() {
			 Form<CustomerRequestManufacrurerSettingsVM> form = DynamicForm.form(CustomerRequestManufacrurerSettingsVM.class).bindFromRequest();
			 CustomerRequestManufacrurerSettingsVM vm = form.get();
			 	deleteCustManfuctList();
		    	for(AddProductVM aProduct:vm.allManufacturerList){
		    		for(UserVM user:aProduct.userData){
		    			if(user.premiumFlag.equals("true")){
		    			CustomerRequestManufacturerSettings custManufact = new CustomerRequestManufacturerSettings();
		    			custManufact.manufacturer = AddProduct.findById(aProduct.id);
		    			custManufact.user = AuthUser.findById(user.id);
		    			custManufact.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    			custManufact.save();
		    			}
		    		}
		    	} 
		    	
		    	 CustomerRequest custData = CustomerRequest.getBylocation(Location.findById(Long.valueOf(session("USER_LOCATION"))));
					if(custData == null){
						CustomerRequest lead = new CustomerRequest();
						
				    	  
				    	   lead.redirectValue = "Automatically redirect an online customer requests based on";
				    	   lead.personValue = "Manufacturer";
				    	   lead.location = Location.findById(Long.valueOf(session("USER_LOCATION")));
				    	   lead.save();
					}else{
							custData.setPersonValue("Manufacturer");
							custData.setRedirectValue("Automatically redirect an online customer requests based on");
							custData.setLocation(Location.findById(Long.valueOf(session("USER_LOCATION"))));
							custData.update();
					}
			 return ok();
	    }
		 
		public static Result saveZipCodeDetails() {
			 Form<SalePersonZipCodeVM> form = DynamicForm.form(SalePersonZipCodeVM.class).bindFromRequest();
			 SalePersonZipCodeVM vm = form.get();
			 	deleteZipcodeData();
		    	for(UserVM aProduct:vm.allFronAndSalesList){
		    		if(aProduct.zipCode != null){
		    			for(ZipCodeVM code:aProduct.zipCode){
		    				SalesPersonZipCode custManufact = new SalesPersonZipCode();
				    		custManufact.zipCode = code.zipcode;
				    		custManufact.city = code.city;
				    		custManufact.state = code.state;
				    		custManufact.user = AuthUser.findById(aProduct.id); 
				    		custManufact.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
				    		custManufact.save();
		    			}
		    		}
		    	} 
		    	
		    	 CustomerRequest custData = CustomerRequest.getBylocation(Location.findById(Long.valueOf(session("USER_LOCATION"))));
					if(custData == null){
						CustomerRequest lead = new CustomerRequest();
						
				    	  
				    	   lead.redirectValue = "Automatically redirect an online customer requests based on";
				    	   lead.personValue = "Zip Code";
				    	   lead.location = Location.findById(Long.valueOf(session("USER_LOCATION")));
				    	   lead.save();
					}else{
							custData.setPersonValue("Zip Code");
							custData.setRedirectValue("Automatically redirect an online customer requests based on");
							custData.setLocation(Location.findById(Long.valueOf(session("USER_LOCATION"))));
							custData.update();
					}
			 return ok();
	    }
		
		public static Result deleteZipcodeData(){
			List<SalesPersonZipCode> custList = SalesPersonZipCode.getAllcustManufactList();
			for(SalesPersonZipCode delList:custList){
				delList.delete();
			}
			return ok();
		}
		
		public static Result deleteCustManfuctList(){
			
			List<CustomerRequestManufacturerSettings> custList = CustomerRequestManufacturerSettings.getAllcustManufactList();
			for(CustomerRequestManufacturerSettings delList:custList){
				delList.delete();
			}
			return ok();
		}
		
		 public static Result saveSalesPersons() {
				Form<CustomerRequestVM> form = DynamicForm.form(CustomerRequestVM.class).bindFromRequest();
				CustomerRequestVM vm = form.get();
				CustomerRequest custData = null;
				custData = CustomerRequest.getBylocation(Location.findById(Long.valueOf(session("USER_LOCATION"))));
				if(custData == null){
					CustomerRequest lead = new CustomerRequest();
					if(vm.personValue.equals("Myself")){
						lead.users = AuthUser.findById(Integer.parseInt(session("USER_KEY")));
					}else{
						 lead.users = AuthUser.findById(vm.userId);
					}
			    	  
			    	   lead.redirectValue = vm.redirectValue;
			    	   lead.personValue = vm.personValue;
			    	   lead.location = Location.findById(Long.valueOf(session("USER_LOCATION")));
			    	   lead.save();
				}else{
					if(vm.personValue.equals("Myself")){
						custData.setUsers(AuthUser.findById(Integer.parseInt(session("USER_KEY"))));
					}else{
						custData.setUsers(AuthUser.findById(vm.userId));
					}
						
						custData.setPersonValue(vm.personValue);
						custData.setRedirectValue(vm.redirectValue);
						custData.setLocation(Location.findById(Long.valueOf(session("USER_LOCATION"))));
						custData.update();
				}
				
		    	   return ok();
			}
		 
		 public static Result getAllCustomerReqData(){
		    	
	    		CustomerRequest custList = CustomerRequest.getAllPermissionData();
		     	return ok(Json.toJson(custList));
	    }
		 
		 public static Result addnewrUser() {
				Form<LeadTypeVM> form = DynamicForm.form(LeadTypeVM.class).bindFromRequest();
				LeadTypeVM vm=form.get();
				//AuthUser user=new AuthUser();
				Date date = new Date();
				
				LeadType lead = new LeadType();
				    	 
		    	   lead.id = vm.id;
		    	   lead.leadName = vm.leadName;
		    	   if(vm.callToAction == null){
		    		   lead.callToAction = false;
		    	   }else{
		    		   lead.callToAction = vm.callToAction;
		    	   }
		    	   if(lead.callToAction == false){
		    		   lead.shows = 0;
		    	   }else{
		    		   lead.shows = 1;
		    	   }
		    	   
		    	   lead.deleted = false;
		    	   
		    	   lead.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    	   lead.save();
		    	   
		    	   NewFormWebsite site = new NewFormWebsite();
		    	   site.lead_name = vm.leadName;
		    	   site.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    	   site.save();
		   		
		    	   return ok(Json.toJson(lead));
			}
		 
		 public static Result getdeletelead(Long Id) {
				
				LeadType lead = LeadType.findById(Id);
			    	
				//lead.setStatus("Deactive");
				lead.delete();
			    	
					return ok();
				}
		 
		 public static Result Updatecheckbox() {
				Form<LeadTypeVM> form = DynamicForm.form(LeadTypeVM.class).bindFromRequest();
				LeadTypeVM vm=form.get();
				Date date = new Date();
				
				LeadType lead = LeadType.findById(vm.id);
				if(lead != null){
					CustomizationForm cForm = CustomizationForm.findByLeadType(lead.leadName);
					if(cForm != null){
						cForm.setJsonData(cForm.jsonData.replace("\"displayWebsite\":false", "\"displayWebsite\":true"));
						cForm.update();
					}
					if(!vm.profile.equals("0")){
						List<CustomizationDataValue> cDataValue = CustomizationDataValue.findByleadType(lead.id);
						for(CustomizationDataValue cData:cDataValue){
							if(!cData.formName.equals("Create New Lead")){
								cData.setDisplayWebsite("true");
								
								//"displayWebsite":false
								cData.update();
							}
						}
					}
					
					lead.setProfile(vm.profile);
			    	lead.setMaunfacturersIds(vm.maunfacturersIds);
			    	lead.update();
				}
				  
		    	  
		    	  return ok();
			}	
		 
		 public static Result savePriceFromTo() {
				Form<UserVM> form = DynamicForm.form(UserVM.class).bindFromRequest();
				UserVM vm=form.get();
				
				AuthUser lead = AuthUser.findById(vm.id);
				if(lead != null){
					lead.setPriceStart(vm.priceStart);
					lead.setPriceEnd(vm.priceEnd);
			    	lead.update();
				}
		    	  return ok();
			}
		 public static Result saveOutListAll(String status,Integer id) {
			 
			 CustomerRequest custData = CustomerRequest.getBylocation(Location.findById(Long.valueOf(session("USER_LOCATION"))));
				if(custData == null){
					CustomerRequest lead = new CustomerRequest();
					
			    	  
			    	   lead.redirectValue = "Automatically redirect an online customer requests based on";
			    	   lead.personValue = "price";
			    	   lead.location = Location.findById(Long.valueOf(session("USER_LOCATION")));
			    	   lead.save();
				}else{
						custData.setPersonValue("Price");
						custData.setRedirectValue("Automatically redirect an online customer requests based on");
						custData.setLocation(Location.findById(Long.valueOf(session("USER_LOCATION"))));
						custData.update();
				}
			 
			 
			 if(status.equals("Released to all of the sales people")){
				 List<AuthUser> lead = AuthUser.getAllSalesAndFrontUser();
				 for(AuthUser au:lead){
					 au.setOutLeftAll(status);
					 au.update();
				 }
			 }else if(status.equals("Sent to one of the sales people")){
			
				 List<AuthUser> lead = AuthUser.getAllSalesAndFrontUser();
				 for(AuthUser au:lead){
					 if(au.id.equals(id)){
						 au.setOutLeftAll(status);
					 }else{
						 au.setOutLeftAll(null);
					 }
					 
					 au.update();
				 }
				 
			 }
				
		    	  return ok();
			}
		 
		 
	 public static Result saveZipCode(String status,Integer id) {
			 
		 CustomerRequest custData = CustomerRequest.getBylocation(Location.findById(Long.valueOf(session("USER_LOCATION"))));
			if(custData == null){
				CustomerRequest lead = new CustomerRequest();
				
		    	  
		    	   lead.redirectValue = "Automatically redirect an online customer requests based on";
		    	   lead.personValue = "Zip Code";
		    	   lead.location = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    	   lead.save();
			}else{
					custData.setPersonValue("Zip Code");
					custData.setRedirectValue("Automatically redirect an online customer requests based on");
					custData.setLocation(Location.findById(Long.valueOf(session("USER_LOCATION"))));
					custData.update();
			}
		 
		 
		 if(status.equals("Released to all of the sales people")){
			 List<AuthUser> lead = AuthUser.getAllSalesAndFrontUser();
			 for(AuthUser au:lead){
				 au.setOutLeftAll(status);
				 au.update();
			 }
		 }else if(status.equals("Sent to one of the sales people")){
		
			 List<AuthUser> lead = AuthUser.getAllSalesAndFrontUser();
			 for(AuthUser au:lead){
				 if(au.id.equals(id)){
					 au.setOutLeftAll(status);
				 }else{
					 au.setOutLeftAll(null);
				 }
				 
				 au.update();
			 }
			 
		 }
				
		    	  return ok();
			}
		 
		 
		 public static Result addnewForm() {
				Form<CreateNewFormVM> form = DynamicForm.form(CreateNewFormVM.class).bindFromRequest();
				CreateNewFormVM vm=form.get();
				//AuthUser user=new AuthUser();
				Date date = new Date();
				
				CreateNewForm lead = new CreateNewForm();
				    	 
		    	   lead.id = vm.id;
		    	   lead.name = vm.name;
		    	   lead.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    	   lead.save();
		   		
		    	   return ok();
		    }
		 
		 public static Result deleteCreateNewForm(Long formId){
			 CreateNewForm cForm = CreateNewForm.findById(formId);
			 cForm.delete();
			 return ok();
		 }
		 
		 public static Result addnewWebSiteForm() {
				Form<NewFormWebsiteVM> form = DynamicForm.form(NewFormWebsiteVM.class).bindFromRequest();
				NewFormWebsiteVM  vm=form.get();
				//AuthUser user=new AuthUser();
				Date date = new Date();
				
				NewFormWebsite lead = new NewFormWebsite();
				    	 
		    	   lead.id = vm.id;
		    	   lead.title = vm.title;
		    	   lead.form_type = vm.form_type;
		    	   lead.lead_name = vm.lead_name;
		    	   lead.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    	   lead.save();
		   		
		    	   return ok();
		    }
		 
		 public static Result deleteFormWebsite(Long formId){
			 NewFormWebsite nWebsite = NewFormWebsite.findById(formId);
			 nWebsite.delete();
			 return ok();
		 }
		 
		 public static Result updatenewWebSiteForm() {
				Form<NewFormWebsiteVM> form = DynamicForm.form(NewFormWebsiteVM.class).bindFromRequest();
				NewFormWebsiteVM  vm=form.get();
				//AuthUser user=new AuthUser();
				Date date = new Date();
				
				NewFormWebsite lead =  NewFormWebsite.findById(vm.id);
				    	 
		    	   if(lead != null){
		    	   lead.setOutcome(vm.outcome);
		    	   }
		    	   lead.update();
		   		
		    	   return ok();
		    	   
		    }
		 
		 public static Result getFormWebSiteData(){
		    	
		    		List<NewFormWebsite> leadtypeObjList = NewFormWebsite.findByLocation(Long.valueOf(session("USER_LOCATION")));
		    		
			    	SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
			    	
			    	ArrayList<NewFormWebsiteVM> leadVMs = new ArrayList<>(); 
			     	for(NewFormWebsite vm : leadtypeObjList){
			     		
			     		NewFormWebsiteVM lead = new NewFormWebsiteVM();
			     		lead.id = vm.id;
			     		lead.title = vm.title;
			     		lead.form_type = vm.form_type;
			     		lead.lead_name = vm.lead_name;
			     		lead.outcome = vm.outcome;
			     		
			     		leadVMs.add(lead);
			  	}
			     	return ok(Json.toJson(leadVMs));
		    	
		    }
		 
		 public static Result getEditFormWebsite() {
				Form<NewFormWebsiteVM> form = DynamicForm.form(NewFormWebsiteVM.class).bindFromRequest();
				NewFormWebsiteVM  vm=form.get();
				Date date = new Date();
				NewFormWebsite lead =  NewFormWebsite.findById(vm.id);
				   	   lead.setTitle(vm.title);
		    		   lead.setForm_type(vm.form_type);
		    		   if(vm.form_type.equals("Contact Form"))
		    		   {
		    			   lead.setLead_name(vm.lead_name);
		    		   }
		    		   else
		    		   {
		    			   lead.setLead_name("");
		    		   }
		    	    lead.update();
		   			return ok();
		    }
		 
		 public static Result showEditData(Long id) {
				NewFormWebsite  formname = NewFormWebsite.findById(id);
				return ok(Json.toJson(formname)); 
			}
		 
		 public static Result getLeadTypeDataById(Long id) {
				LeadType  leadData = LeadType.findById(id);
				LeadTypeVM vm = new LeadTypeVM();
				if(leadData != null){
					
					vm.id = leadData.id;
					vm.leadName = leadData.leadName;
					//vm.checkValue = leadData.
					vm.profile = leadData.profile;
					vm.callToAction = leadData.callToAction;
					vm.actionTitle = leadData.actionTitle;
					vm.actionOutcomes = leadData.actionOutcomes;
					vm.actionClientPdf = leadData.actionClientPdf;
					vm.maunfacturersIds = leadData.maunfacturersIds;
					vm.confirmationMsg = leadData.confirmationMsg;
					CustomizationForm cForm = CustomizationForm.findByLeadType(leadData.leadName);
					if(cForm != null){
						vm.systemOutcome = cForm.outcome;
						if(cForm.sendPdf != null){
							vm.sendpdfIds = cForm.sendPdf.id;
						}
						
					}
				}
				
				
				return ok(Json.toJson(vm)); 
			}
		 public static Result UpdateLeadType() {
				Form<LeadTypeVM> form = DynamicForm.form(LeadTypeVM.class).bindFromRequest();
				LeadTypeVM vm=form.get();
				Date date = new Date();
				
				LeadType lead = LeadType.findById(vm.id);
				   lead.setId(vm.id); 	 
		    	   lead.setLeadName(vm.leadName);
		    	   if(vm.callToAction == null){
		    		   lead.setCallToAction(false);
		    		   lead.setShows(0);
		    	   }else{
		    		   lead.setCallToAction(vm.callToAction);
		    		   if(vm.callToAction == false){
		    			   lead.setShows(0);
		    		   }else{
		    			   lead.setShows(1);
		    		   }
		    		   
		    	   }
		    	  // lead.setLocations(vm.id);
		    	  lead.update();
		    	  return ok();
			}	
		 
		 public static Result saveLeadFormPopup() {
				Form<LeadTypeVM> form = DynamicForm.form(LeadTypeVM.class).bindFromRequest();
				LeadTypeVM vm=form.get();
				Date date = new Date();
				AuthUser userObj = (AuthUser) getLocalUser();
				LeadType lead = LeadType.findById(vm.id);
				
				   lead.setActionOutcomes(vm.actionOutcomes);
				   lead.setActionTitle(vm.actionTitle);
				   lead.setActionClientPdf(vm.actionClientPdf);
				   lead.setConfirmationMsg(vm.confirmationMsg);
				   CustomizationForm cForm = CustomizationForm.findByLocationsAndType(Long.valueOf(session("USER_LOCATION")), vm.leadName);
				   if(cForm != null){
					   cForm.setOutcome(vm.outcome);
					   if(vm.sendpdfIds != null){
						   cForm.setSendPdf(CustomerPdf.findPdfById(vm.sendpdfIds));
					   }
					   
					   cForm.update();
				   }
				   lead.update();
				   
		    	  MultipartFormData body = request().body().asMultipartFormData();
		    	  FilePart pdfFile = null ;
		    	  if(body != null){
			    		List<FilePart> filePart =  body.getFiles();
			    		if (filePart != null) {
			    			if(filePart.size() > 0){
			    				for(int i= 0; i<filePart.size(); i++){
				    	    		 pdfFile = filePart.get(i);
				       				 String fileName = pdfFile.getFilename().replaceAll("[-+^:,() ]","");
				       				String ext = FilenameUtils.getExtension(fileName);
				       				fileName = vm.leadName +"_"+ fileName;
				       	       	    File fdir = new File(rootDir+File.separator+userObj.id+File.separator+"LeadFile");
				       	       	    if(!fdir.exists()) {
				       	       	    	fdir.mkdir();
				       	       	    }
				       	       	String filePath = rootDir+File.separator+userObj.id+File.separator+"LeadFile"+File.separator+fileName;
				       	       	 try {
				       	       		 	Boolean sts = FileUtils.deleteQuietly(new File(filePath));
				       	       		 	System.out.println("delete "+sts);
				       				} catch (Exception e) {
				       					e.printStackTrace();
				       				}
				       	       	    File file = pdfFile.getFile();
				       	       	    try {
				       	       	  if(ext.equalsIgnoreCase("pdf")){
			       	       	    		FileUtils.moveFile(file, new File(filePath));
			       	       	    		lead.setActionClientPdf(userObj.id+File.separator+"LeadFile"+File.separator+fileName);
			       	       	    		lead.update();
			       	       	    	}
									} catch (Exception e) {
										e.printStackTrace();
									}
			    				}	 
			    			}
			    		}
		    	  }
		    	  return ok();
			}	
		 
		 public static Result saveEmailDetails() {
		    	
			       Identity user=getLocalUser();
		    		Form<AutoPortalVM> form = DynamicForm.form(AutoPortalVM.class).bindFromRequest();
		    		AutoPortalVM vm = form.get();
			    	
		    		EmailDetails por=EmailDetails.findByLocation(Long.valueOf(session("USER_LOCATION")));
		    		
		    		if(por == null ){
		    		EmailDetails email=new EmailDetails();
		    		email.passward=vm.passward;
		    		email.port=vm.port;
		    		email.name=vm.name;
		    		email.host=vm.host;
		    		email.username=vm.username;
			    	email.locations=Location.findById(Long.valueOf(session("USER_LOCATION")));
		    		email.save();
		    		}
		    		else{
		    			por.setUsername(vm.username);
		    			por.setPassward(vm.passward);
		    			por.setPort(vm.port);
		    			por.setHost(vm.host);
		    			por.setName(vm.name);
		    			por.update();
		    		}
		    		    		
		    		
		    		
			    	return ok();
		    	
		    }
		 
		    public static Result saveDomain(){
				
		    		 Identity user=getLocalUser();
		    		Form<domainVM> form = DynamicForm.form(domainVM.class).bindFromRequest();
		    		domainVM vm = form.get();
		    		
		    		
		    		Domain por=Domain.findByLocation(Long.valueOf(session("USER_LOCATION")));
		    		
		    		if(por == null ){
		    			Domain domain1=new Domain();
		    			domain1.domain=vm.domain;
		    			domain1.hostingProvider = vm.hostingProvider;
		    			domain1.userName = vm.userName;
		    			domain1.password = vm.password;
		    			domain1.locations=Location.findById(Long.valueOf(session("USER_LOCATION")));
		    			domain1.save();
		    		}
		    		else
		    		{
		    			por.setDomain(vm.domain);
		    			por.setHostingProvider(vm.hostingProvider);
		    			por.setUserName(vm.userName);
		    			por.setPassword(vm.password);
		    			por.update();
		    		}
		    		
		    		return ok();
		    	
			}
		    
		    public static Result saveAutoPortal() {
		    	
			    	Form<AutoPortalVM> form = DynamicForm.form(AutoPortalVM.class).bindFromRequest();
		    		AutoPortalVM vm = form.get();
			    	
		    		AutoPortal por=AutoPortal.findByType("AutoTrader.com");
		    		if(por == null ){
		    			if(vm.sitename.equalsIgnoreCase("AutoTrader.com")){
		    		AutoPortal portal=new AutoPortal();
			    	portal.login=vm.login;
			    	portal.passward=vm.passward;
			    	portal.port=vm.port;
			    	portal.path=vm.path;
		    		portal.sitename=vm.sitename;
		    		portal.save();
		    			}
		    		}
		    		else{
		    			if(vm.sitename.equalsIgnoreCase("AutoTrader.com")){
		    			por.setLogin(vm.login);
		    			por.setPassward(vm.passward);
		    			por.setPort(vm.port);
		    			por.setPath(vm.path);
		    			por.update();
		    			}
		    		}
		    		
		    		AutoPortal por1=AutoPortal.findByType("Cars.com");
		    		if(por1 == null){
		    			if(vm.sitename.equalsIgnoreCase("Cars.com")){
		    		AutoPortal portal=new AutoPortal();
			    	portal.login=vm.login;
			    	portal.passward=vm.passward;
			    	portal.port=vm.port;
			    	portal.path=vm.path;
		    		portal.sitename=vm.sitename;
		    		portal.save();
		    			}
		    		}
		    		else{
		    			if(vm.sitename.equalsIgnoreCase("Cars.com")){
		    			por1.setLogin(vm.login);
		    			por1.setPassward(vm.passward);
		    			por1.setPort(vm.port);
		    			por1.setPath(vm.path);
		    			por1.update();
		    			}
		    		}
		    		

		    		AutoPortal por2=AutoPortal.findByType("CarsGuru");
		    		if(por2 == null){
		    			if(vm.sitename.equalsIgnoreCase("CarsGuru")){
		    		AutoPortal portal=new AutoPortal();
			    	portal.login=vm.login;
			    	portal.passward=vm.passward;
			    	portal.port=vm.port;
			    	portal.path=vm.path;
		    		portal.sitename=vm.sitename;
		    		portal.save();
		    			}
		    		}
		    		else{
		    			if(vm.sitename.equalsIgnoreCase("CarsGuru")){
		    			por2.setLogin(vm.login);
		    			por2.setPassward(vm.passward);
		    			por2.setPort(vm.port);
		    			por2.setPath(vm.path);
		    			por2.update();
		    			}
		    		}
		    		
		    		AutoPortal por3=AutoPortal.findByType("TrueCar");
		    		if(por3 == null){
		    			if(vm.sitename.equalsIgnoreCase("TrueCar")){
		    		AutoPortal portal=new AutoPortal();
			    	portal.login=vm.login;
			    	portal.passward=vm.passward;
			    	portal.port=vm.port;
			    	portal.path=vm.path;
		    		portal.sitename=vm.sitename;
		    		portal.save();
		    			}
		    		}
		    		else{
		    			if(vm.sitename.equalsIgnoreCase("TrueCar")){
		    			por3.setLogin(vm.login);
		    			por3.setPassward(vm.passward);
		    			por3.setPort(vm.port);
		    			por3.setPath(vm.path);
		    			por3.update();
		    			}
		    		}
		    		
		    		
		    		AutoPortal por4=AutoPortal.findByType("Carfax");
		    		if(por4 == null){
		    			if(vm.sitename.equalsIgnoreCase("Carfax")){
		    		AutoPortal portal=new AutoPortal();
			    	portal.login=vm.login;
			    	portal.passward=vm.passward;
			    	portal.port=vm.port;
			    	portal.path=vm.path;
		    		portal.sitename=vm.sitename;
		    		portal.save();
		    			}
		    		}
		    		else{
		    			if(vm.sitename.equalsIgnoreCase("Carfax")){
		    			por4.setLogin(vm.login);
		    			por4.setPassward(vm.passward);
		    			por4.setPort(vm.port);
		    			por4.setPath(vm.path);
		    			por4.update();
		    			}
		    		}
		    		
		    		
		    		
		    		AutoPortal por5=AutoPortal.findByType("Craigslist");
		    		if(por5== null){
		    			if(vm.sitename.equalsIgnoreCase("Craigslist")){
		    		AutoPortal portal=new AutoPortal();
			    	portal.login=vm.login;
			    	portal.passward=vm.passward;
			    	portal.port=vm.port;
			    	portal.path=vm.path;
		    		portal.sitename=vm.sitename;
		    		portal.save();
		    			}
		    		}
		    		else{
		    			if(vm.sitename.equalsIgnoreCase("Craigslist")){
		    			por5.setLogin(vm.login);
		    			por5.setPassward(vm.passward);
		    			por5.setPort(vm.port);
		    			por5.setPath(vm.path);
		    			por5.update();
		    			}
		    		}
		    		
		    		
		    		AutoPortal por6=AutoPortal.findByType("Ebay");
		    		if(por6 == null){
		    			if(vm.sitename.equalsIgnoreCase("Ebay")){
		    		AutoPortal portal=new AutoPortal();
			    	portal.login=vm.login;
			    	portal.passward=vm.passward;
			    	portal.port=vm.port;
			    	portal.path=vm.path;
		    		portal.sitename=vm.sitename;
		    		portal.save();
		    			}
		    		}
		    		else{
		    			if(vm.sitename.equalsIgnoreCase("Ebay")){
		    			por6.setLogin(vm.login);
		    			por6.setPassward(vm.passward);
		    			por6.setPort(vm.port);
		    			por6.setPath(vm.path);
		    			por6.update();
		    			}
		    		}
		    		
		    		AutoPortal por7=AutoPortal.findByType("Kelly");
		    		if(por7 == null){
		    			if(vm.sitename.equalsIgnoreCase("Kelly")){
		    		AutoPortal portal=new AutoPortal();
			    	portal.login=vm.login;
			    	portal.passward=vm.passward;
			    	portal.port=vm.port;
			    	portal.path=vm.path;
		    		portal.sitename=vm.sitename;
		    		portal.save();
		    			}
		    		}
		    		else{
		    			if(vm.sitename.equalsIgnoreCase("Kelly")){
		    			por7.setLogin(vm.login);
		    			por7.setPassward(vm.passward);
		    			por7.setPort(vm.port);
		    			por7.setPath(vm.path);
		    			por7.update();
		    			}
		    		}
			    	return ok();
		    	
		    }
		    
		    public static Result saveEmailLinks() {
		    	
			       Identity user=getLocalUser();
		    		Form<DocumentationVM> form = DynamicForm.form(DocumentationVM.class).bindFromRequest();
		    		DocumentationVM vm = form.get();
			    	
		    		MyProfile por=MyProfile.findByLocations(Long.valueOf(session("USER_LOCATION")));
		    		
		    		if(por == null ){
		    			MyProfile email=new MyProfile();
		    		email.facebook=vm.facebookLink;
		    		email.twitter=vm.twitterLink;
		    		email.yelp=vm.yelpLink;
		    		email.pinterest=vm.pinterestLink;
		    		email.googleplus=vm.googleLink;
		    		email.instagram=vm.instagramLink;
			    	email.locations=Location.findById(Long.valueOf(session("USER_LOCATION")));
		    		email.save();
		    		}
		    		else{
		    			por.setFacebook(vm.facebookLink);
		    			por.setTwitter(vm.twitterLink);
		    			por.setInstagram(vm.instagramLink);
		    			por.setPinterest(vm.pinterestLink);
		    			por.setGoogleplus(vm.googleLink);
		    			por.setYelp(vm.yelpLink);
		    			por.update();
		    		}
		        	return ok();
		    }
		    
		    public static Result acountDetails() {
		    	
		    		Form<AutoPortalVM> form = DynamicForm.form(AutoPortalVM.class).bindFromRequest();
		    		AutoPortalVM vm = form.get();
			    	
		    		MarketingAcounts por=MarketingAcounts.findByType("facebook");
		    		if(por == null ){
		    			if(vm.sitename.equalsIgnoreCase("facebook")){
		           MarketingAcounts portal=new MarketingAcounts();
		            portal.username=vm.username;
			    	portal.passward=vm.passward;
		    		portal.sitename=vm.sitename;
		    		portal.locations=Location.findById(Long.valueOf(session("USER_LOCATION")));
		    		portal.save();
		    			}
		    		}
		    		else{
		    			if(vm.sitename.equalsIgnoreCase("facebook")){
		    			por.setPassward(vm.passward);
		    			por.setUsername(vm.username);
		    			por.update();
		    			}
		    		}
		    		
		    		MarketingAcounts por1=MarketingAcounts.findByType("twitter");
		    		if(por1 == null ){
		    			if(vm.sitename.equalsIgnoreCase("twitter")){
		           MarketingAcounts portal=new MarketingAcounts();
		            portal.username=vm.username;
			    	portal.passward=vm.passward;
		    		portal.sitename=vm.sitename;
		    		portal.locations=Location.findById(Long.valueOf(session("USER_LOCATION")));
		    		portal.save();
		    			}
		    		}
		    		else{
		    			if(vm.sitename.equalsIgnoreCase("twitter")){
		    			por1.setPassward(vm.passward);
		    			por1.setUsername(vm.username);
		    			por1.update();
		    			}
		    		}

		    		
		    		
		    		MarketingAcounts por2=MarketingAcounts.findByType("instagram");
		    		if(por2 == null ){
		    			if(vm.sitename.equalsIgnoreCase("instagram")){
		           MarketingAcounts portal=new MarketingAcounts();
		            portal.username=vm.username;
			    	portal.passward=vm.passward;
		    		portal.sitename=vm.sitename;
		    		portal.locations=Location.findById(Long.valueOf(session("USER_LOCATION")));
		    		portal.save();
		    			}
		    		}
		    		else{
		    			if(vm.sitename.equalsIgnoreCase("instagram")){
		    			por2.setPassward(vm.passward);
		    			por2.setUsername(vm.username);
		    			por2.update();
		    			}
		    		}
		    		
		    		
		    		MarketingAcounts por3=MarketingAcounts.findByType("pinterest");
		    		if(por3 == null ){
		    			if(vm.sitename.equalsIgnoreCase("pinterest")){
		           MarketingAcounts portal=new MarketingAcounts();
		            portal.username=vm.username;
			    	portal.passward=vm.passward;
		    		portal.sitename=vm.sitename;
		    		portal.locations=Location.findById(Long.valueOf(session("USER_LOCATION")));
		    		portal.save();
		    			}
		    		}
		    		else{
		    			if(vm.sitename.equalsIgnoreCase("pinterest")){
		    			por3.setPassward(vm.passward);
		    			por3.setUsername(vm.username);
		    			por3.update();
		    			}
		    		}
		    		

		    		MarketingAcounts por4=MarketingAcounts.findByType("google");
		    		if(por4 == null ){
		    			if(vm.sitename.equalsIgnoreCase("google")){
		           MarketingAcounts portal=new MarketingAcounts();
		            portal.username=vm.username;
			    	portal.passward=vm.passward;
		    		portal.sitename=vm.sitename;
		    		portal.locations=Location.findById(Long.valueOf(session("USER_LOCATION")));
		    		portal.save();
		    			}
		    		}
		    		else{
		    			if(vm.sitename.equalsIgnoreCase("google")){
		    			por4.setPassward(vm.passward);
		    			por4.setUsername(vm.username);
		    			por4.update();
		    			}
		    		}
		    		return ok();
		    }
		    
		    public static Result  getCustomerPdfDataById(Long id){
				
		    	 CustomerPdf pdf = CustomerPdf.findPdfById(id);
		    		DocumentationVM vm = new DocumentationVM();
		    		if(pdf != null){
		    			vm.internalPdfName=pdf.pdf_name;
		    		}
		    		
		    		return ok(Json.toJson(vm.internalPdfName));
		    }
		    
		    public static Result deletePdfById(Long id){
				
		    		CustomerPdf pdf = CustomerPdf.findPdfById(id);
		    		File file = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+"Customer_Pdf"+File.separator+pdf.pdf_name);
		        	//File thumbFile = new File(rootDir+File.separator+image.vin+"-"+user.id+File.separator+"thumbnail_"+image.imgName);
			    	file.delete();
			    	//thumbFile.delete();
		    		pdf.delete();
		    		return ok();
		    }
		    
		    public static Result getInternalPdfDataById(Long id){
				InternalPdf pdf = InternalPdf.findPdfById(id);
		    		DocumentationVM vm = new DocumentationVM();
		    		if(pdf != null){
		    			vm.internalPdfName=pdf.pdf_name;
		    		}
		    		return ok(Json.toJson(vm.internalPdfName));
		    }
		    
		    public static Result deleteInternalPdf(Long id){
				
		    		InternalPdf pdf = InternalPdf.findPdfById(id);
		    		File file = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+"Internal_Pdf"+File.separator+pdf.pdf_name);
		    		
			    	//File thumbFile = new File(rootDir+File.separator+image.vin+"-"+user.id+File.separator+"thumbnail_"+image.imgName);
			    	file.delete();
			    	//thumbFile.delete();
		    		pdf.delete();
		    		return ok();
		    }
		    
		    public static Result setPermiumFlag(Integer id){
		    	AuthUser userList = AuthUser.findById(id);
		    	if(userList.premiumFlag == null){
		    		userList.setPremiumFlag("1");
		    	}else if(userList.premiumFlag.equals("0")){
		    		userList.setPremiumFlag("1");
		    	}else if(userList.premiumFlag.equals("1")){
		    		userList.setPremiumFlag("0");
		    	}
		    	userList.update();
		    	return ok();
		    	
		    }
		    
		    public static Result saveSliderConfig(Integer width,Integer height) {
		    	
			    	AuthUser user = (AuthUser) getLocalUser();
			    	SliderImageConfig config = SliderImageConfig.findByUser(user);
			    	
			    	if(config != null) {
			    		config.setCropHeight(height);
			    		config.setCropWidth(width);
			    		config.update();
			    	} else {
			           config = new SliderImageConfig();
			           config.setCropHeight(height);
			           config.setCropWidth(width);
			           config.user = user;
						config.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
			           config.save();
			        }
			    	return ok();
		    		
		    }
		    
		    public static Result saveFeaturedConfig(Integer width,Integer height) {
		    	
			    	AuthUser user = (AuthUser) getLocalUser();
			    	FeaturedImageConfig config = FeaturedImageConfig.findByUser(user);
			    	
			    	if(config != null) {
			    		config.setCropHeight(height);
			    		config.setCropWidth(width);
			    		config.update();
			    	} else {
			           config = new FeaturedImageConfig();
			           config.setCropHeight(height);
			           config.setCropWidth(width);
			           config.user = user;
			           config.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
			           config.save();
			        }
			
			    	return ok();
		    	
		    }
		    
		    public static Result saveVehicleConfig(Integer width,Integer height) {
		    	
			    	AuthUser user = (AuthUser) getLocalUser();
			    	VehicleImageConfig config = VehicleImageConfig.findByUser(user);
			    	
			    	if(config != null) {
			    		config.setCropHeight(height);
			    		config.setCropWidth(width);
			    		config.update();
			    	} else {
			           config = new VehicleImageConfig();
			           config.setCropHeight(height);
			           config.setCropWidth(width);
			           config.user = user;
			           config.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
			           config.save();
			        }
			
			    	return ok();
		    		
		    }
		    
		    public static Result setCoverImage(Integer width,Integer height) {


			    	AuthUser user = (AuthUser) getLocalUser();
			    	CoverImage config = CoverImage.findByUser(user);
			    	
			    	if(config != null) {
			    		config.setCropHeight(height);
			    		config.setCropWidth(width);
			    		config.update();
			    	} else {
			           config = new CoverImage();
			           config.setCropHeight(height);
			           config.setCropWidth(width);
			           config.user = user;
			           config.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
			           config.save();
			        }
			
			    	return ok();
		    		
		    }
		    
		    public static Result savePremiumConfig(String priceVehical,String premiumFlag){
		    	
			    	AuthUser user = (AuthUser) getLocalUser();
			    	PremiumLeads preMim = PremiumLeads.findByLocation(Long.valueOf(session("USER_LOCATION")));
			    	if(preMim != null){
			    		preMim.setPremium_amount(priceVehical);
			    		if(premiumFlag.equals("false")){
			    			preMim.setPremium_flag(0);
			    		}else if(premiumFlag.equals("true")){
			    			preMim.setPremium_flag(1);
			    		}
			    		preMim.update();
			    	}else{
			    		PremiumLeads pLeads = new PremiumLeads();
			    		pLeads.setPremium_amount(priceVehical);
			    		if(premiumFlag.equals("false")){
			    			pLeads.setPremium_flag(0);
			    		}else if(premiumFlag.equals("true")){
			    			pLeads.setPremium_flag(1);
			    		}else if(premiumFlag.equals("0")){
			    			pLeads.setPremium_flag(0);
			    		}	
			    		pLeads.setUser(user);
			    		pLeads.setLocations(Location.findById(Long.valueOf(session("USER_LOCATION"))));
			    		pLeads.save();
			    	}
			
			    	return ok();
		    		
		    }
		    
		    public static Result saveNewsletterDate(String date,String time,Long id,String newsTimeZone) throws ParseException {
				
		    		SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
		    		if(id == 0) {
			    		NewsletterDate obj = new NewsletterDate();
			    		obj.dateOfMonth = date;
			    		Date dt = new Date();
						obj.newsletterTime = df.parse(time);
						Date d1 = df.parse(time);
						Calendar cal = Calendar.getInstance();
		  		   	  	switch(newsTimeZone){
		  		   	  
			  		   	  case "EST" : 
			  		   	  
			  		   	  cal = Calendar.getInstance();
			  		   	  cal.setTime(d1);
			  		   	  cal.add(Calendar.HOUR_OF_DAY, 5);
			  		   	  d1 = cal.getTime();
			  		   	  obj.gmtTime = d1;
			  		   	  obj.timeZone = newsTimeZone;
			  		   	  
			  		   	  break;
			  		   	  case "CST" : 
			  		   	  
			  		   	  cal = Calendar.getInstance();
			  		   	  cal.setTime(d1);
			  		   	  cal.add(Calendar.HOUR_OF_DAY, 6);
			  		   	  d1 = cal.getTime();
			  		   	  obj.gmtTime = d1;
			  		   	  obj.timeZone = newsTimeZone;
			  		   	  break;
			  		   	  
			  		   	  case "PST" : 
			  		   	  
			  		   	  cal = Calendar.getInstance();
			  		   	  cal.setTime(d1);
			  		   	  cal.add(Calendar.HOUR_OF_DAY, 8);
			  		   	  d1 = cal.getTime();
			  		   	  obj.gmtTime = d1;
			  		   	  obj.timeZone = newsTimeZone;
			  		   	  break;
			  		   	  
			  		   	  case "MST" : 
			  		   	  
			  		   	  cal = Calendar.getInstance();
			  		   	  cal.setTime(d1);
			  		   	  cal.add(Calendar.HOUR_OF_DAY, 7);
			  		   	  d1 = cal.getTime();
			  		   	  obj.gmtTime = d1;
			  		   	  obj.timeZone = newsTimeZone;
			  		   	  break;
		  		   	  }
			    		obj.save();
			    		NewsLetter.newsletterSchedulling();
		    		} else {
		    			NewsletterDate obj = NewsletterDate.findById(id);
		    			obj.setDateOfMonth(date);
		    			obj.setNewsletterTime(df.parse(time));
		    			Date d1 = df.parse(time);
						Calendar cal = Calendar.getInstance();
		  		   	  	switch(newsTimeZone){
		  		   	  
			  		   	  case "EST" : 
			  		   	  
			  		   	  cal = Calendar.getInstance();
			  		   	  cal.setTime(d1);
			  		   	  cal.add(Calendar.HOUR_OF_DAY, 5);
			  		   	  d1 = cal.getTime();
			  		   	  obj.setGmtTime(d1);
			  		   	  obj.setTimeZone(newsTimeZone);
			  		   	  
			  		   	  break;
			  		   	  case "CST" : 
			  		   	  
			  		   	  cal = Calendar.getInstance();
			  		   	  cal.setTime(d1);
			  		   	  cal.add(Calendar.HOUR_OF_DAY, 6);
			  		   	  d1 = cal.getTime();
			  		   	  obj.setGmtTime(d1);
			  		   	  obj.setTimeZone(newsTimeZone);
			  		   	  break;
			  		   	  
			  		   	  case "PST" : 
			  		   	  
			  		   	  cal = Calendar.getInstance();
			  		   	  cal.setTime(d1);
			  		   	  cal.add(Calendar.HOUR_OF_DAY, 8);
			  		   	  d1 = cal.getTime();
			  		   	  obj.setGmtTime(d1);
			  		   	  obj.setTimeZone(newsTimeZone);
			  		   	  break;
			  		   	  
			  		   	  case "MST" : 
			  		   	  
			  		   	  cal = Calendar.getInstance();
			  		   	  cal.setTime(d1);
			  		   	  cal.add(Calendar.HOUR_OF_DAY, 7);
			  		   	  d1 = cal.getTime();
			  		   	  obj.setGmtTime(d1);
			  		   	  obj.setTimeZone(newsTimeZone);
			  		   	  break;
		  		   	  }
		    			obj.update();
		    			NewsLetter.newsletterSchedulling();
		    		}
		    		
		    		return ok();
		    	
			}
		    
		    public static Result saveMainCollect(){
				
	    		Form<InventorySettingVM> form = DynamicForm.form(InventorySettingVM.class).bindFromRequest();
	    		InventorySettingVM vm = form.get();
	    		deleteMainCollectionType();
	    		for(InventorySettingVM inven:vm.addMainCollFields){
	    			InventorySetting inventory = new InventorySetting();
	    			inventory.collection = inven.collection;
	    			inventory.enableInven = true;
	    			inventory.locations=Location.findById(Long.valueOf(session("USER_LOCATION")));
	    			inventory.save();
	    		}
	    			
	    		return ok();
	    	}
		    
		   public static Result deleteMainCollectionType(){
				List<InventorySetting> custList = InventorySetting.getAllCollection();
				for(InventorySetting delList:custList){
					delList.delete();
				}
				return ok();
			}
		    
		    public static Result deleteMainCollection(){
		    	Form<InventorySettingVM> form = DynamicForm.form(InventorySettingVM.class).bindFromRequest();
	    		InventorySettingVM vm = form.get();
	    		
	    		List<InventorySetting> lead =  InventorySetting.findByLocation(Long.valueOf(session("USER_LOCATION")));
	    		for(InventorySetting inven:lead){
	    			inven.setEnableInven(false);
	    			inven.update();
	    		}
	    			
	    		return ok();
			}
		    public static Result getAllInventoryData() {
				List<InventorySetting> lead = InventorySetting.findByLocation(Long.valueOf(session("USER_LOCATION")));
				return ok(Json.toJson(lead)); 
				
			}
}
