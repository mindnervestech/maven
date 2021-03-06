package controllers;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.net.ssl.HttpsURLConnection;

import models.ActionAdd;
import models.AddCollection;
import models.AuthUser;
import models.Blog;
import models.ClickyActionList;
import models.ClickyVisitorsList;
import models.CollectionImages;
import models.Comments;
import models.ContactHeader;
import models.ContactOtherField;
import models.Contacts;
import models.CreateNewForm;
import models.CustomerPdf;
import models.CustomerRequest;
import models.CustomerRequestManufacturerSettings;
import models.CustomizationCrm;
import models.CustomizationDataValue;
import models.CustomizationForm;
import models.EmailDetails;
import models.FeaturedImage;
import models.HeardAboutUs;
import models.HoursOfOperation;
import models.InternalPdf;
import models.Inventory;
import models.InventoryImage;
import models.InventorySetting;
import models.LeadType;
import models.LeadsDateWise;
import models.Location;
import models.MyProfile;
import models.Permission;
import models.PhotographerHoursOfOperation;
import models.PlanLocationTotal;
import models.PlanSalesTotal;
import models.PlanSchedule;
import models.PlanScheduleMonthlyLocation;
import models.PlanScheduleMonthlySalepeople;
import models.PriceAlert;
import models.PriceChange;
import models.Product;
import models.ProductImages;
import models.Registration;
import models.RequestMoreInfo;
import models.SalesPersonZipCode;
import models.SalesPlanSchedule;
import models.ScheduleTest;
import models.Site;
import models.SiteAboutUs;
import models.SiteComparison;
import models.SiteContent;
import models.SiteInventory;
import models.SiteLogo;
import models.SliderImage;
import models.SoldContact;
import models.SoldInventory;
import models.ToDo;
import models.TradeIn;
import models.UserNotes;
import models.Vehicle;
import models.VehicleAudio;
import models.VehicleHeader;
import models.VehicleImageConfig;
import models.Video;
import models.VirtualTour;
import models.Warranty;
import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import securesocial.core.Identity;
import securesocial.core.java.SecureSocial;
import viewmodel.AddCollectionVM;
import viewmodel.AudioVM;
import viewmodel.BarChartVM;
import viewmodel.ClickyPagesVM;
import viewmodel.CloseLeadVM;
import viewmodel.ContactsVM;
import viewmodel.CreateNewFormVM;
import viewmodel.DateAndValueVM;
import viewmodel.DocumentationVM;
import viewmodel.HeardAboutUsVm;
import viewmodel.HoursOperation;
import viewmodel.ImageVM;
import viewmodel.InfoCountVM;
import viewmodel.InventoryVM;
import viewmodel.KeyValueDataVM;
import viewmodel.LeadDateWiseVM;
import viewmodel.LeadVM;
import viewmodel.LocationMonthPlanVM;
import viewmodel.LocationVM;
import viewmodel.LocationWiseDataVM;
import viewmodel.NoteVM;
import viewmodel.PageVM;
import viewmodel.PermissionVM;
import viewmodel.PinVM;
import viewmodel.PlanScheduleVM;
import viewmodel.PriceChangeVM;
import viewmodel.PriceFormatDate;
import viewmodel.ProductVM;
import viewmodel.RequestInfoVM;
import viewmodel.SalepeopleMonthPlanVM;
import viewmodel.ScheduleTestVM;
import viewmodel.SetPriceChangeFlag;
import viewmodel.SoldContactVM;
import viewmodel.SpecificationVM;
import viewmodel.ToDoVM;
import viewmodel.TradeInVM;
import viewmodel.UserNoteVM;
import viewmodel.UserVM;
import viewmodel.VehicleVM;
import viewmodel.VideoVM;
import viewmodel.VirtualTourVM;
import viewmodel.bodyStyleSetVM;
import viewmodel.sendDataVM;
import viewmodel.sendDateAndValue;
import views.html.agreement;
import views.html.home;
import views.html.homeSA;
import views.html.index;
import au.com.bytecode.opencsv.CSVWriter;

import com.avaje.ebean.SqlRow;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.Oauth2Scopes;
import com.google.api.services.tasks.TasksScopes;
import com.google.api.services.tasks.model.Task;
import com.google.api.services.tasks.model.TaskList;
import com.google.api.services.tasks.model.TaskLists;
import com.google.api.services.tasks.model.Tasks;
import com.mnt.dataone.Equipment;
import com.mnt.dataone.InstalledEquipment;
import com.mnt.dataone.Option;
import com.mnt.dataone.OptionalEquipment;
import com.mnt.dataone.ResponseData;
import com.mnt.dataone.Specification;
import com.mnt.dataone.Specification_;
import com.mnt.dataone.Value;


public class Application extends Controller {
  
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
			
			//public static int userId = -1361609913;
			
	static String simulatevin = "{    'success': true,    'specification': {        'vin': 'WDDNG7KB7DA494890',        'year': '2013',        'make': 'Mercedes-Benz',        'model': 'S-Class',        'trim_level': 'S65 AMG',        'engine': '6.0L V12 SOHC 36V TURBO',        'style': 'SEDAN 4-DR',        'made_in': 'GERMANY',        'steering_type': 'R&P',        'anti_brake_system': '4-Wheel ABS',        'tank_size': '23.80 gallon',        'overall_height': '58.00 in.',        'overall_length': '206.50 in.',        'overall_width': '73.70 in.',        'standard_seating': '5',        'optional_seating': null,        'highway_mileage': '19 miles/gallon',        'city_mileage': '12 miles/gallon'    },    'vin': 'WDDNG7KB7DA494890'}";
	
	private static boolean simulate = false;
    /*public static Result index() {
        return ok(index.render("Your new application is ready."));
    }*/
	
	
	//private final static Log logger = LogFactory.getLog(GoogleConnectController.class);
		private static final String APPLICATION_NAME = "Web client 1";
		private static HttpTransport httpTransport;
		private static HttpTransport httpTransporttask;
		private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
		private static com.google.api.services.calendar.Calendar client;
		private static com.google.api.services.tasks.Tasks service;
		private static int flagValue = 0;
		
		static GoogleClientSecrets clientSecrets;
		static GoogleClientSecrets clientSecretstask;
		static GoogleAuthorizationCodeFlow flow;
		static GoogleAuthorizationCodeFlow flowtask;
		static com.google.api.client.auth.oauth2.Credential credential;
		static com.google.api.client.auth.oauth2.Credential credentialtask;

		private static String clientId="657059082204-1uh3d2dt5cik1269s55bc80tlpd52gsb.apps.googleusercontent.com";
		private static String clientSecret="Xx2gAJ4ucJ-rmcYdO3wwB5_D";
		private static String redirectURI="http://www.glider-autos.com/oauth2Callback";
		private static String redirectURIUpdate="http://www.glider-autos.com/updatecalenderdata";
		private Set<Event> events=new HashSet<Event>();
		static List<Tasks> tasksList = new ArrayList<>();
		static List<Event> events1 = new ArrayList<>();
		
		private static Oauth2 oauth2;
		
		
		public void setEvents(Set<Event> events) {
			this.events = events;
		}
		

		public static Result preflight(String all) {
	        response().setHeader("Access-Control-Allow-Origin", "*");
	        response().setHeader("Allow", "*");
	        response().setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
	        response().setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Referer, User-Agent");
	        return ok();
	    }
		
		
		
		public static Result locationWise(Long locationId){
			AuthUser user = AuthUser.getOnlyGM();
			
			if(user != null) {

						session("USER_KEY", user.id+"");
						session("USER_ROLE", user.role+""	);
						
						if(user.location != null){
							session("USER_LOCATION", user.location.id+"");
						}else if(user.location == null){
							Location location = Location.findManagerType(user);
							if(location != null){
								session("USER_LOCATION", location.id+"");
							}
						}
			    		HashMap<String, Boolean> permission = new HashMap<String, Boolean>();
			    		List<Permission> userPermissions = user.getPermission();
			    		for(Permission per: userPermissions) {
			    			permission.put(per.name, true);
			    		}
			    		
			    		 return ok(index.render(Json.stringify(Json.toJson(permission)), session("USER_ROLE"),session("USER_KEY"),Json.stringify(Json.toJson(events1)),Json.stringify(Json.toJson(tasksList)),locationId.toString(),userRegistration));
			    	
			} else {
				return ok(home.render("Invalid Credentials",userRegistration));
			}
			
			
		}
		
		public static Result gmIsManager(Long locationId) {
			
			AuthUser user = AuthUser.getlocationAndManagerOne(Location.findById(locationId));
			
			if(user != null) {

						session("USER_KEY", user.id+"");
						session("USER_ROLE", user.role+""	);
						
						if(user.location != null){
							session("USER_LOCATION", user.location.id+"");
						}else if(user.location == null){
							Location location = Location.findManagerType(user);
							if(location != null){
								session("USER_LOCATION", location.id+"");
							}
						}
			    		HashMap<String, Boolean> permission = new HashMap<String, Boolean>();
			    		List<Permission> userPermissions = user.getPermission();
			    		for(Permission per: userPermissions) {
			    			permission.put(per.name, true);
			    		}
			    		
			    		 return ok(index.render(Json.stringify(Json.toJson(permission)), session("USER_ROLE"),session("USER_KEY"),Json.stringify(Json.toJson(events1)),Json.stringify(Json.toJson(tasksList)), "0",userRegistration));
			    	
			} else {
				return ok(home.render("Invalid Credentials",userRegistration));
			}
			
		}
	public static Result login() {
		
		
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String email = Form.form().bindFromRequest().get("email");
		String password= Form.form().bindFromRequest().get("password");
		String tokanNo= Form.form().bindFromRequest().get("tokan");
		Date curDate = new Date();
		
		AuthUser user = null;
		if(email != null && password != null){
			 user = AuthUser.find.where().eq("email", email).eq("password", password).eq("account", "active").findUnique();
		}else{
			AuthUser user1 = AuthUser.find.where().eq("email", "art@gliderllc.com").eq("password", "123456").eq("account", "active").findUnique();
			
			if(userRegistration.equals("true")){
				
				Registration registration = Registration.getTokanNo(tokanNo);
				
				if(registration != null){
					String cDate = df.format(curDate);
					Date cdates = null;
					try {
						cdates = df.parse(cDate);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if((cdates.equals(registration.startDate)||cdates.after(registration.startDate)) && ((cdates.equals(registration.expiryDate)||cdates.before(registration.expiryDate)))){

								session("USER_KEY", user1.id+"");
								session("USER_ROLE", user1.role+"");
								
								if(user1.location != null){
									session("USER_LOCATION", user1.location.id+"");
								}else if(user1.location == null){
									Location location = Location.findManagerType(user1);
									if(location != null){
										session("USER_LOCATION", location.id+"");
									}
								}
								
								
								//return  redirect("/dealer/index.html#/");
					    		HashMap<String, Boolean> permission = new HashMap<String, Boolean>();
					    		List<Permission> userPermissions = user1.getPermission();
					    		for(Permission per: userPermissions) {
					    			permission.put(per.name, true);
					    		}
					    		
					    		registration.setActivity(curDate);
					    		registration.update();
					    		
					    		 return ok(index.render(Json.stringify(Json.toJson(permission)), session("USER_ROLE"),session("USER_KEY"),Json.stringify(Json.toJson(events1)),Json.stringify(Json.toJson(tasksList)),"0",userRegistration));
					    		//return redirect("/googleConnectionStatus");
						
						
					}else{
						return ok(home.render("Your account has been suspended, please contact your management for further questions",userRegistration));
					}
				}else{
					return ok(home.render("Invalid Credentials",userRegistration));
				}
				
				
				
			}
		}
		
		
	
		if(user != null) {
		
			if(user.role.equalsIgnoreCase("Admin")){
				session("USER_KEY", user.id+"");
				session("USER_ROLE", user.role+"");
				HashMap<String, Boolean> permission = new HashMap<String, Boolean>();
	    		List<Permission> userPermissions = user.getPermission();
	    		for(Permission per: userPermissions) {
	    			permission.put(per.name, true);
	    		}
	    		
	    		 return ok(index.render(Json.stringify(Json.toJson(permission)), session("USER_ROLE"),session("USER_KEY"),Json.stringify(Json.toJson(events1)),Json.stringify(Json.toJson(tasksList)),"0",userRegistration));
			}else if(user.role.equalsIgnoreCase("Photographer")){
				session("USER_KEY", user.id+"");
				session("USER_ROLE", user.role+"");
				if(user.location != null){
					session("USER_LOCATION", user.location.id+"");
				}
				HashMap<String, Boolean> permission = new HashMap<String, Boolean>();
	    		List<Permission> userPermissions = user.getPermission();
	    		for(Permission per: userPermissions) {
	    			permission.put(per.name, true);
	    		}
	    		
	    		 return ok(index.render(Json.stringify(Json.toJson(permission)), session("USER_ROLE"),session("USER_KEY"),Json.stringify(Json.toJson(events1)),Json.stringify(Json.toJson(tasksList)),"0",userRegistration));
			}else if(user.role.equalsIgnoreCase("General Manager")){
				if(user.getNewUser()== 1){

					session("USER_KEY", user.id+"");
					session("USER_ROLE", user.role+""	);
					
					if(user.location != null){
						session("USER_LOCATION", user.location.id+"");
					}else if(user.location == null){
						Location location = Location.findManagerType(user);
						if(location != null){
							session("USER_LOCATION", location.id+"");
						}
					}
					//return  redirect("/dealer/index.html#/");
		    		HashMap<String, Boolean> permission = new HashMap<String, Boolean>();
		    		List<Permission> userPermissions = user.getPermission();
		    		for(Permission per: userPermissions) {
		    			permission.put(per.name, true);
		    		}
		    		
		    		 return ok(index.render(Json.stringify(Json.toJson(permission)), session("USER_ROLE"),session("USER_KEY"),Json.stringify(Json.toJson(events1)),Json.stringify(Json.toJson(tasksList)),"0",userRegistration));
		    		//return redirect("/googleConnectionStatus");
				}else{
					return ok(home.render(user.getEmail(),userRegistration));
				}
			}else{
				Location loc = Location.findById(user.location.id);
				if(loc.getType().equalsIgnoreCase("active")){
					if(user.getNewUser()== 1){

						session("USER_KEY", user.id+"");
						session("USER_ROLE", user.role+"");
						
						if(user.location != null){
							session("USER_LOCATION", user.location.id+"");
						}else if(user.location == null){
							Location location = Location.findManagerType(user);
							if(location != null){
								session("USER_LOCATION", location.id+"");
							}
						}
						
						
						//return  redirect("/dealer/index.html#/");
			    		HashMap<String, Boolean> permission = new HashMap<String, Boolean>();
			    		List<Permission> userPermissions = user.getPermission();
			    		for(Permission per: userPermissions) {
			    			permission.put(per.name, true);
			    		}
			    		
			    		 return ok(index.render(Json.stringify(Json.toJson(permission)), session("USER_ROLE"),session("USER_KEY"),Json.stringify(Json.toJson(events1)),Json.stringify(Json.toJson(tasksList)),"0",userRegistration));
			    		//return redirect("/googleConnectionStatus");
					}else{
						return ok(home.render(user.getEmail(),userRegistration));
					}
				}else{
					return ok(home.render("Your account has been suspended, please contact your management for further questions",userRegistration));
				}
			}
    		//return ok(index.render(Json.stringify(Json.toJson(permission)), session("USER_ROLE"),session("USER_KEY"),Json.stringify(Json.toJson(events1)),Json.stringify(Json.toJson(tasksList))));
		} else {
			return ok(home.render("Invalid Credentials",userRegistration));
		}
	}
	
	public static Result getfindGmIsManager(){
		AuthUser user = getLocalUser();
		Location location = Location.findById(user.location.id);
		int flag = 0;
		if(location != null){
			if(location.manager != null){
				flag = 1;
			}
		}
		//Location location = Location.findManagerType(user);
		return ok(Json.toJson(flag));
	}
	
	public static Result acceptAgreement() {
		String email = Form.form().bindFromRequest().get("email");
		String userName = Form.form().bindFromRequest().get("name");
		String userDate = Form.form().bindFromRequest().get("date");
		String userPhone = Form.form().bindFromRequest().get("phone");
		AuthUser user = AuthUser.findByEmail(email);
		if(user != null) {
			user.setNewUser(1);
			user.update();
			session("USER_KEY", user.id+"");
			session("USER_ROLE", user.role+"");
			
			if(user.location != null){
				session("USER_LOCATION", user.location.id+"");
			}else if(user.location == null){
				Location location = Location.findManagerType(user);
				if(location != null){
					session("USER_LOCATION", location.id+"");
				}
			}
			
			
			HashMap<String, Boolean> permission = new HashMap<String, Boolean>();
    		List<Permission> userPermissions = user.getPermission();
    		for(Permission per: userPermissions) {
    			permission.put(per.name, true);
    		}
    		
    		
    		 String domain = Play.application().configuration().getString("domain");
    	        String wkpath = Play.application().configuration().getString("wkpath");
    	        String folderPath = Play.application().path().getAbsolutePath() + "/pdf";
    	        File folder = new File(folderPath);
    	        if (!folder.exists()) {
    	            folder.mkdir();
    	        }
    	        String pdfFilePath = Play.application().path().getAbsolutePath() + "/pdf/"+ "agreement.pdf";
    	        try {
    	            Process p = Runtime.getRuntime().exec(wkpath + " --viewport-size 1280x800 " + domain + "getAgreement/" +userName + "/"+userDate +"/"+userPhone +" "+pdfFilePath);
    	            BufferedReader inStreamReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
    	            String line = inStreamReader.readLine();
    	            while (line != null) {
    	                line = inStreamReader.readLine();
    	            }
    	        } catch (Exception e) {
    	            System.out.println("coming-->" + e.getMessage());
    	        }
    	        File f = new File(pdfFilePath);

	        agreementEmail();
    		
    		//agreementEmail(userName,userDate,userPhone);
    		//return ok(agreement.render(userName,userDate,userPhone));
    		//return redirect("/googleConnectionStatus");
    		//return ok();
	        	return ok(index.render(Json.stringify(Json.toJson(permission)), session("USER_ROLE"),session("USER_KEY"),Json.stringify(Json.toJson(events1)),Json.stringify(Json.toJson(tasksList)),"0",userRegistration));
		}else {
			return ok(home.render("Invalid Credentials",userRegistration));
		}
	}
	
	public static Result agreementEmail(){
		
	        String to = "info@gliderllc.com";
	        String from = "glider.autos@gmail.com";
	        String host = "mail.smtp.host";
            
	        
	        EmailDetails details=EmailDetails.findByLocation(Long.valueOf(session("USER_LOCATION")));
			String emailName=details.name;
			String port=details.port;
			String gmail=details.host;
			final	String emailUser=details.username;
			final	String emailPass=details.passward;
	        Properties props = new Properties();
	 		props.put("mail.smtp.auth", "true");
	 		props.put("mail.smtp.starttls.enable", "true");
	 		props.put("mail.smtp.host", gmail);
	 		props.put("mail.smtp.port", port);

	        // Get the Session object.
	        Session session = Session.getInstance(props,
	           new javax.mail.Authenticator() {
	              protected PasswordAuthentication getPasswordAuthentication() {
	                 return new PasswordAuthentication(emailUser, emailPass);
	              }
	           });

	        try {
	           Message message = new MimeMessage(session);
                try{
	           message.setFrom(new InternetAddress(emailUser,emailName));
                }catch(UnsupportedEncodingException e){
                	e.printStackTrace();
                }

	           message.setRecipients(Message.RecipientType.TO,
	              InternetAddress.parse(to));

	           message.setSubject("User Agreement");

	           BodyPart messageBodyPart = new MimeBodyPart();

	           messageBodyPart.setText("This is message body");

	           Multipart multipart = new MimeMultipart();

	           multipart.addBodyPart(messageBodyPart);

	           messageBodyPart = new MimeBodyPart();
	           String pdfFilePath = Play.application().path().getAbsolutePath() + "/pdf/"+ "agreement.pdf";
	           DataSource source = new FileDataSource(pdfFilePath);
	           messageBodyPart.setDataHandler(new DataHandler(source));
	           messageBodyPart.setFileName(pdfFilePath);
	           multipart.addBodyPart(messageBodyPart);

	           message.setContent(multipart);

	           Transport.send(message);

	           System.out.println("Sent message successfully....");
	    
	        } catch (MessagingException e) {
	           throw new RuntimeException(e);
	        }
		return ok();
	}
	
	public static Result getAgreement(String userName,String userDate, String userPhone) {
		return ok(agreement.render(userName,userDate,userPhone));
	}
	
	public static Result agreementEmail(String userName,String userDate, String userPhone) {
		
		 EmailDetails details=EmailDetails.findByLocation(Long.valueOf(session("USER_LOCATION")));
			String emailName=details.name;
			String port=details.port;
			String gmail=details.host;
			final	String emailUser=details.username;
			final	String emailPass=details.passward;
		
		Properties props = new Properties();
 		props.put("mail.smtp.auth", "true");
 		props.put("mail.smtp.starttls.enable", "true");
 		props.put("mail.smtp.host", gmail);
 		props.put("mail.smtp.port", port);
  
 		Session session = Session.getInstance(props,
 		  new javax.mail.Authenticator() {
 			protected PasswordAuthentication getPasswordAuthentication() {
 				return new PasswordAuthentication(emailUser, emailPass);
 			}
 		  });
  
 		try{
 		   
  			Message message = new MimeMessage(session);
  			message.setFrom(new InternetAddress("dineshkudale2@gmail.com"));
  			message.setRecipients(Message.RecipientType.TO,
  			InternetAddress.parse("dineshkudale2@gmail.com"));
  			message.setSubject("Your username and password ");	  			
  			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			ve.init();
			
			Template t = ve.getTemplate("/public/emailTemplate/agreementTemplate.vm"); 
	        VelocityContext context = new VelocityContext();
	        
	        context.put("userName", userName);
	        context.put("userDate", userDate);
	        context.put("userPhone", userPhone);
	       
	        StringWriter writer = new StringWriter();
	        t.merge( context, writer );
	        String content = writer.toString();
			
			messageBodyPart.setContent(content, "text/html");
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
			Transport.send(message);
			System.out.println("Agree mail");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ok();
	}
	
	public static Result mgLogin(){
		AuthUser user = AuthUser.find.where().eq("email", "art@gliderllc.com").eq("password", "123456").eq("account", "active").findUnique();
		
		Location loc = Location.findById(user.location.id);
		if(loc.getType().equalsIgnoreCase("active")){
			if(user.getNewUser()== 1){

				session("USER_KEY", user.id+"");
				session("USER_ROLE", user.role+"");
				
				if(user.location != null){
					session("USER_LOCATION", user.location.id+"");
				}else if(user.location == null){
					Location location = Location.findManagerType(user);
					if(location != null){
						session("USER_LOCATION", location.id+"");
					}
				}
				
				
				//return  redirect("/dealer/index.html#/");
	    		HashMap<String, Boolean> permission = new HashMap<String, Boolean>();
	    		List<Permission> userPermissions = user.getPermission();
	    		for(Permission per: userPermissions) {
	    			permission.put(per.name, true);
	    		}
	    		
	    		 return ok(index.render(Json.stringify(Json.toJson(permission)), session("USER_ROLE"),session("USER_KEY"),Json.stringify(Json.toJson(events1)),Json.stringify(Json.toJson(tasksList)),"0",userRegistration));
	    		//return redirect("/googleConnectionStatus");
			}else{
				return ok(home.render(user.getEmail(),userRegistration));
			}
		}else{
			return ok(home.render("Your account has been suspended, please contact your management for further questions",userRegistration));
		}
	}
	
	public static Result salePLogin(){
		AuthUser user = AuthUser.find.where().eq("email", "felocipto@gmail.com").eq("password", "YNMAG7").eq("account", "active").findUnique();
		Location loc = Location.findById(user.location.id);
		if(loc.getType().equalsIgnoreCase("active")){
			if(user.getNewUser()== 1){

				session("USER_KEY", user.id+"");
				session("USER_ROLE", user.role+"");
				
				if(user.location != null){
					session("USER_LOCATION", user.location.id+"");
				}else if(user.location == null){
					Location location = Location.findManagerType(user);
					if(location != null){
						session("USER_LOCATION", location.id+"");
					}
				}
				
	    		HashMap<String, Boolean> permission = new HashMap<String, Boolean>();
	    		List<Permission> userPermissions = user.getPermission();
	    		for(Permission per: userPermissions) {
	    			permission.put(per.name, true);
	    		}
	    		
	    		 return ok(index.render(Json.stringify(Json.toJson(permission)), session("USER_ROLE"),session("USER_KEY"),Json.stringify(Json.toJson(events1)),Json.stringify(Json.toJson(tasksList)),"0",userRegistration));
			}else{
				return ok(home.render(user.getEmail(),userRegistration));
			}
		}else{
			return ok(home.render("Your account has been suspended, please contact your management for further questions",userRegistration));
		}
	}
	
	public static Result gmLogin(){
		
		AuthUser user = AuthUser.find.where().eq("email", "mindnervesdemo@gmail.com").eq("password", "123456").eq("account", "active").findUnique();
		
		if(userRegistration.equals("true")){
			
					if(user.getNewUser()== 1){

						session("USER_KEY", user.id+"");
						session("USER_ROLE", user.role+""	);
						
						if(user.location != null){
							session("USER_LOCATION", user.location.id+"");
						}else if(user.location == null){
							Location location = Location.findManagerType(user);
							if(location != null){
								session("USER_LOCATION", location.id+"");
							}
						}
						//return  redirect("/dealer/index.html#/");
			    		HashMap<String, Boolean> permission = new HashMap<String, Boolean>();
			    		List<Permission> userPermissions = user.getPermission();
			    		for(Permission per: userPermissions) {
			    			permission.put(per.name, true);
			    		}
			    		
			    		 return ok(index.render(Json.stringify(Json.toJson(permission)), session("USER_ROLE"),session("USER_KEY"),Json.stringify(Json.toJson(events1)),Json.stringify(Json.toJson(tasksList)),"0",userRegistration));
					}else{
							return ok(home.render(user.getEmail(),userRegistration));
						}
					
				
			
		}else{
			return ok(home.render("Invalid Tokan No",userRegistration));
		}
	}
	
	public static Result logout() {
		session().clear();
		return ok(home.render("",userRegistration));
	}
	
	public static Result home() {
		return ok(home.render("",userRegistration));
	}
	public static Result adminHome() {
		return ok(homeSA.render("",userRegistration));
	}
	public static Result test() {
		return ok(agreement.render("fdfdsf","fdsf","1"));
	}
	
	
    public static Result index() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return redirect("/login");
    	} else {
    		//return redirect("/dealer/index.html");
    		AuthUser user = getLocalUser();
    		HashMap<String, Boolean> permission = new HashMap<String, Boolean>();
    		List<Permission> userPermissions = user.getPermission();
    		for(Permission per: userPermissions) {
    			permission.put(per.name, true);
    		}
    		return ok(index.render(Json.stringify(Json.toJson(permission)), session("USER_ROLE"),session("USER_KEY"),Json.stringify(Json.toJson(events1)),Json.stringify(Json.toJson(tasksList)),"0",userRegistration));
    	}
    }
    
    public static Result getAllPermission(){
    	List<Permission> permissionList = Permission.getAllPermission();
    	return ok(Json.toJson(permissionList));
    }
    
    public static Result getAllMeetingData(){
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    	List<ScheduleTestVM> scheListData= new ArrayList<>();
    	List<ScheduleTest> scheList = ScheduleTest.findAllBestDatAndTime(Long.valueOf(session("USER_LOCATION")));
    	for (ScheduleTest obj : scheList) {
    		ScheduleTestVM vm =new ScheduleTestVM();
			vm.name = obj.name;
			vm.id = obj.id;
			if(obj.bestDay != null){
				String[] dates = obj.bestDay.split("-");
				vm.bestDay = dates[2]+"-"+dates[1]+"-"+dates[0];
			}
			vm.bestTime = obj.bestTime;
			scheListData.add(vm);
		}
    	return ok(Json.toJson(scheListData));
    }
    
    public static Result getAllPermissionById(){
    	List<PermissionVM> permissionListData= new ArrayList<>();
    	List<Permission> permission = Permission.getAllPermissionById();
		for (Permission obj : permission) {
			PermissionVM vm =new PermissionVM();
			vm.name = obj.name;
			vm.id = obj.id;
			getFetureChildData(vm.id, vm);
			permissionListData.add(vm);
		}
		return ok(Json.toJson(permissionListData));
    }
    
    public static void getFetureChildData(Integer id, PermissionVM vm1){
    	List<PermissionVM> permissionListChild= new ArrayList<>();
    	List<Permission> permission = Permission.getAllPermissionChildData(id);
		for (Permission obj : permission) {
			PermissionVM vm =new PermissionVM();
			vm.name = obj.name;
			vm.id = obj.id;
			vm.parent_id = obj.parentId;
			
			permissionListChild.add(vm);
		}
		vm1.childData = permissionListChild;
    }
    
    public static Result changePermission(Long locationId,Integer managerId,String gmIsManager){
    	AuthUser user = AuthUser.findById(managerId);
    	user.deleteManyToManyAssociations("permission");
    	List<Permission> permissionList = Permission.getAllPermission();
    	 List<Permission> permissionData = new ArrayList<>();
    	if(gmIsManager.equals("1")) {
    		  
    		 for(Permission obj: permissionList) {
				   if(!obj.name.equals("Dealer's Profile") && !obj.name.equals("My Locations") && !obj.name.equals("Deactivate Locations")) {
					   permissionData.add(obj);
				   }
  		   }
  		   user.permission = permissionData;
    		   //user.permission.addAll(permissionList);
    		   
    	   }else{
    		   
    		   if(user.role.equals("General Manager")){
    			   for(Permission obj: permissionList) {
    				   if(obj.name.equals("CRM") || obj.name.equals("My Profile") || obj.name.equals("Dashboard") || obj.name.equals("Dealer's Profile") || obj.name.equals("My Locations") || obj.name.equals("Deactivate Locations")) {
    					   permissionData.add(obj);
    				   }
        		   }
        		   user.permission = permissionData;
    		   }
    		  
    	   }
    	user.update();
    	return ok();
    }
	
    public static Result getUserPermissions() {
    	AuthUser user = getLocalUser();
		HashMap<String, Boolean> permission = new HashMap<String, Boolean>();
		List<Permission> userPermissions = user.getPermission();
		for(Permission per: userPermissions) {
			permission.put(per.name, true);
		}
		return ok(Json.toJson(permission));
    }
    
    public static Result getUserInfo() {
    	AuthUser user = getLocalUser();
    	if(user.imageUrl== null){
    		user.imageUrl ="/profile-pic.jpg";
    	}
		return ok(Json.toJson(user));
	}
    
    public static AuthUser getLocalUser() {
    	String id = session("USER_KEY");
    	AuthUser user = AuthUser.find.byId(Integer.parseInt(id));
    	//AuthUser user = getLocalUser();
		return user;
	}

    @SecureSocial.UserAwareAction
    public static Result userAware() {
        Identity user = getLocalUser();
        final String userName = user != null ? user.fullName() : "guest";
        return ok("Hello " + userName);
    }

    public static Result ajaxCall() {
        // return some json
    	return null;
    }
  
    public static Result getVehicleInfo(String vin) throws IOException,Exception {
    	
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser user = getLocalUser();
    		Location location=Location.findById(Long.valueOf(session("USER_LOCATION")));
	    	Vehicle vehicle = Vehicle.findByVinAndStatusForGM(vin,location); 
	    	if(vehicle == null) {
	    		PinVM pinObj = new PinVM();
	    		if(!simulate ) {
	    		/*URL url;
					url = new URL("https://vindecoder.p.mashape.com/decode_vin?vin="+vin);
				URLConnection conn = url.openConnection();
				conn.setRequestProperty("X-Mashape-Key", mashapeKey);
				conn.setRequestProperty("Accept", "application/json");
				
				BufferedReader br = new BufferedReader(
		                           new InputStreamReader(conn.getInputStream()));
				
				StringBuilder sb = new StringBuilder();
				String line;
			    while ((line = br.readLine()) != null) {
			        sb.append(line);
			    }
			 
				ObjectMapper mapper = new ObjectMapper();
				
				pinObj = new ObjectMapper().readValue(sb.toString(), PinVM.class);*/
	    			
	    			pinObj.vin = vin;
	    			SpecificationVM specificationVM = new SpecificationVM();
	    			String postData = "client_id=11178&authorization_code=c382533644b1c8e3a0607671ea0caf1742961b84&decoder_query=";
	    		      //String vinNum = "SCFFDCCDXBGE12780";
	    		      String decoderQuery = URLEncoder.encode(
	    		    		  "{	\"decoder_settings\" : {	    \"version\" : \"7.0.1\",		\"display\" : \"full\",		\"styles\" : \"on\",		\"style_data_packs\" : {			\"basic_data\" : \"on\",			\"pricing\" : \"on\",			\"engines\" : \"on\",			\"transmissions\" : \"on\",			\"specifications\" : \"on\",			\"installed_equipment\" : \"on\",			\"optional_equipment\" : \"on\",			\"colors\" : \"on\",			\"safety_equipment\" : \"on\",			\"warranties\" : \"on\",			\"fuel_efficiency\" : \"on\"			},		\"common_data\" : \"on\",		\"common_data_packs\" : {			\"basic_data\" : \"on\",			\"pricing\" : \"on\",			\"engines\" : \"on\",			\"transmissions\" : \"on\",			\"specifications\" : \"on\",			\"installed_equipment\" : \"on\"		}	},	\"query_requests\" : {		\"Request-Sample\" : {			\"vin\" : \""+vin+"\",			\"year\" : \"on\",			\"make\" : \"on\",			\"model\" : \"on\",			\"trim\" : \"on\",			\"model_number\" : \"on\",			\"package_code\" : \"on\",			\"drive_type\" : \"on\",			\"vehicle_type\" : \"on\",			\"body_type\" : \"on\",			\"doors\" : \"on\",			\"bedlength\" : \"on\",			\"wheelbase\" : \"on\",			\"msrp\" : \"on\",			\"invoice_price\" : \"on\",			\"engine\" : {				\"description\" : \"on\",				\"block_type\" : \"on\",				\"cylinders\" : \"on\",				\"displacement\" : \"on\",				\"fuel_type\" : \"on\"			},			\"transmission\" : {				\"description\" : \"on\",				\"trans_type\" : \"on\",				\"trans_speeds\" : \"on\"			},			\"optional_equipment_codes\" : \"on\",			\"installed_equipment_descriptions\" : \"on\",			\"interior_color\" : {				\"description\" : \"on\",				\"color_code\" : \"on\"			},			\"exterior_color\" : {				\"description\" : \"on\",				\"color_code\" : \"on\"			}		}	}}"
	    		    		  ,"UTF-8");
	    	    	
	    		      
	    		      URL decoder_url = new URL("https://api.dataonesoftware.com/webservices/vindecoder/decode");
	    		       HttpsURLConnection connection = (HttpsURLConnection) decoder_url.openConnection();
	    		       connection.setRequestMethod("POST");
	    		       connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	    		       connection.setDoInput(true);
	    		       connection.setDoOutput(true);
	    					
	    		       // Post the request data
	    		       DataOutputStream output = new DataOutputStream(connection.getOutputStream());
	    		       output.writeBytes(postData + decoderQuery);
	    		       output.flush();
	    		       output.close();
	    					
	    		       // Retrieve the response data
	    		       DataInputStream input = new DataInputStream(connection.getInputStream()); 
	    		       // read in each character until end-of-stream is detected 
	    		       StringBuilder json = new StringBuilder();
	    		       for (int c = input.read(); c != -1; c = input.read()) {
	    		    	   json.append((char)c);
	    		    	    
	    		       }
	    		       input.close();
	    		       //System.out.print( json.toString() );
	    		       ResponseData mapperObj = new com.fasterxml.jackson.databind.ObjectMapper().readValue(json.toString(), ResponseData.class);
	    		       
	    		       if(mapperObj.getQueryResponses().getRequestSample().getQueryError().getErrorMessage().equals("")) {
	    		    	   pinObj.success = true;
	    		       } else {
	    		    	   pinObj.success = false;
	    		    	   return ok(Json.toJson(pinObj));
	    		       }
	    		       
	    		       specificationVM.year = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getBasicData().getYear();
	    		       specificationVM.make = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getBasicData().getMake();
	    		       specificationVM.model = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getBasicData().getModel();
	    		       specificationVM.mileage = "";
	    		       specificationVM.bodyStyle = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getBasicData().getBodyType();
	    		       specificationVM.doors = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getBasicData().getDoors();
	    		       
	    		       for(Specification seating: mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getSpecifications()) {
	    		    	   if(seating.getCategory().equals("Seating")) {
	    			    	   for(Specification_ spec: seating.getSpecifications()) {
	    			    		   if(spec.getName().equals("Max Seating")) {
	    			    			   specificationVM.standardSeating = spec.getValue();
	    			    		   }
	    			    	   }
	    		    	   }
	    		       }
	    		       
	    		       specificationVM.drivetrain = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getBasicData().getDriveType();
	    		       specificationVM.engine = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getEngines().get(0).getDisplacement()+" L";
	    		       specificationVM.transmission = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getTransmissions().get(0).getDetailType();
	    		       
	    		       for(InstalledEquipment equip: mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getInstalledEquipment()) {
	    		    	   if(equip.getCategory().equals("Brakes")) {
	    			    	   for(Equipment spec: equip.getEquipment()) {
	    			    		   if(spec.getName().equals("ABS")) {
	    			    			   for(Value vl: spec.getValues()){
	    			    				   if(vl.getInstalledEquipmentId().equals("5000000168"))
	    			    					   specificationVM.brakes = vl.getValue();
	    			    			   }
	    			    		   }
	    			    	   }
	    		    	   }
	    		       }
	    		       specificationVM.horsePower = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getEngines().get(0).getMaxHp();
	    		       
	    		       for(Specification seating: mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getSpecifications()) {
	    		    	   if(seating.getCategory().equals("Performance Specifications")) {
	    			    	   for(Specification_ spec: seating.getSpecifications()) {
	    			    		   if(spec.getName().equals("Acceleration (0-60MPH)")) {
	    			    			   specificationVM.acceleration = spec.getValue()+" sec";
	    			    		   }
	    			    	   }
	    		    	   }
	    		       }
	    		       
	    		       specificationVM.extColor = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getUsStyles().get(0).getColors().getExteriorColors().get(0).getMfrColorName();
	    		       specificationVM.intColor = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getUsStyles().get(0).getColors().getInteriorColors().get(0).getMfrColorName();
	    		       specificationVM.vin = vin;
	    		       specificationVM.city_mileage = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getUsStyles().get(0).getEpaFuelEfficiency().get(0).getCity();
	    		       specificationVM.highway_mileage = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getUsStyles().get(0).getEpaFuelEfficiency().get(0).getHighway();
	    		       specificationVM.fuelType = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getEngines().get(0).getFuelType();
	    		       
	    		       for(Specification seating: mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getSpecifications()) {
	    		    	   if(seating.getCategory().equals("Fuel Tanks")) {
	    			    	   for(Specification_ spec: seating.getSpecifications()) {
	    			    		   if(spec.getName().equals("Fuel Tank 1 Capacity (Gallons)")) {
	    			    			   specificationVM.fuelTank = spec.getValue();
	    			    		   }
	    			    	   }
	    		    	   }
	    		       }
	    		       
	    		       for(InstalledEquipment equip: mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getInstalledEquipment()) {
	    		    	   if(equip.getCategory().equals("Lights")) {
	    			    	   for(Equipment spec: equip.getEquipment()) {
	    			    		   if(spec.getName().equals("Headlights")) {
	    			    			   for(Value vl: spec.getValues()){
	    			    				   if(vl.getInstalledEquipmentId().equals("5000001530"))
	    			    					   specificationVM.headlights = vl.getValue();
	    			    			   }
	    			    		   }
	    			    	   }
	    		    	   }
	    		       } 
	    		       
	    		       for(InstalledEquipment equip: mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getInstalledEquipment()) {
	    		    	   if(equip.getCategory().equals("Mirrors")) {
	    			    	   for(Equipment spec: equip.getEquipment()) {
	    			    		   if(spec.getName().equals("Exterior mirrors")) {
	    			    			   specificationVM.mirrors = spec.getValues().get(0).getValue();
	    			    		   }
	    			    	   }
	    		    	   }
	    		       } 
	    		       
	    		       for(Specification seating: mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getSpecifications()) {
	    		    	   if(seating.getCategory().equals("Measurements of Size and Shape")) {
	    			    	   for(Specification_ spec: seating.getSpecifications()) {
	    			    		   if(spec.getName().equals("Ground Clearance")) {
	    			    			   specificationVM.groundClearance = spec.getValue();
	    			    		   }
	    			    		   if(spec.getName().equals("Height")) {
	    			    			   specificationVM.height = spec.getValue();
	    			    		   }
	    			    		   if(spec.getName().equals("Length")) {
	    			    			   specificationVM.length = spec.getValue();
	    			    		   }
	    			    		   if(spec.getName().equals("Width")) {
	    			    			   specificationVM.width = spec.getValue();
	    			    		   }
	    			    	   }
	    		    	   }
	    		       }
	    		       
	    		       for(InstalledEquipment equip: mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getInstalledEquipment()) {
	    		    	   if(equip.getCategory().equals("Roof")) {
	    			    	   for(Equipment spec: equip.getEquipment()) {
	    			    		   if(spec.getName().equals("Sunroof")) {
	    			    			   specificationVM.roof = spec.getValues().get(0).getValue();
	    			    		   }
	    			    	   }
	    		    	   }
	    		       } 
	    		      
	    		       specificationVM.engineType = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getEngines().get(0).getBlockType();
	    		       specificationVM.cylinders = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getEngines().get(0).getCylinders();
	    		       specificationVM.displacement = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getEngines().get(0).getDisplacement()+" L";
	    		       specificationVM.camType = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getEngines().get(0).getCamType();
	    		       specificationVM.valves = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getEngines().get(0).getValves();
	    		       specificationVM.fuelQuality = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getEngines().get(0).getFuelQuality();
	    		       specificationVM.gears = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getTransmissions().get(0).getGears();
	    		       
	    		       for(InstalledEquipment equip: mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getInstalledEquipment()) {
	    		    	   if(equip.getCategory().equals("Brakes")) {
	    			    	   for(Equipment spec: equip.getEquipment()) {
	    				    		   if(spec.getName().equals("Front brake diameter")) {
	    				    			   specificationVM.frontBrakeDiameter = spec.getValues().get(0).getValue()+" inches";
	    				    			   }
	    				    		   
	    				    		   if(spec.getName().equals("Front brakes")) {
	    				    			   specificationVM.frontBrakeType = spec.getValues().get(0).getValue();
	    		    				   }
	    			    		   
	    				    		   if(spec.getName().equals("Rear brakes")) {
	    				    			   specificationVM.rearBrakeType = spec.getValues().get(0).getValue();
	    		    				   }
	    				    		   
	    				    		   if(spec.getName().equals("Rear brake diameter")) {
	    				    			   specificationVM.rearBrakeDiameter = spec.getValues().get(0).getValue()+" inches";
	    		    				   }
	    			    		   }
	    		    	   }
	    		       }
	    		       
	    		       specificationVM.comfortFeatures = "";
	    		       specificationVM.steeringWheelControls = "";
	    		       for(InstalledEquipment equip: mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getInstalledEquipment()) {
	    		    	   if(equip.getCategory().equals("Safety")) {
	    			    	   for(Equipment spec: equip.getEquipment()) {
	    			    		   if(spec.getName().equals("Active head restraints")) {
	    			    			   specificationVM.activeHeadRestraints = spec.getValues().get(0).getValue();
	    			    		   }
	    			    		   
	    			    		   if(spec.getName().equals("Body side reinforcements")) {
	    			    			   specificationVM.bodySideReinforcements = spec.getValues().get(0).getValue();
	    			    		   }
	    			    		   
	    			    		   if(spec.getName().equals("Crumple zones")) {
	    			    			   specificationVM.crumpleZones = spec.getValues().get(0).getValue();
	    			    		   }
	    			    		   
	    			    		   if(spec.getName().equals("Impact absorbing bumpers")) {
	    			    			   specificationVM.impactAbsorbingBumpers = spec.getValues().get(0).getValue();
	    			    		   }
	    			    		   
	    			    		   if(spec.getName().equals("Impact sensor")) {
	    			    			   specificationVM.impactSensors = spec.getValues().get(0).getValue();
	    			    		   }
	    			    		   
	    			    		   if(spec.getName().equals("Parking sensors")) {
	    			    			   specificationVM.parkingSensors = spec.getValues().get(0).getValue();
	    			    		   }
	    			    		   
	    			    	   }
	    		    	   }
	    		    	   
	    		    	   if(equip.getCategory().equals("Seatbelts")) {
	    		    		   specificationVM.seatbelts = equip.getEquipment().get(0).getValues().get(0).getValue();
	    		    	   }
	    		    	   
	    		    	   
	    		    	   if(equip.getCategory().equals("Comfort Features")) {
	    		    		   for(Equipment spec: equip.getEquipment()) {
	    		    			   specificationVM.comfortFeatures = specificationVM.comfortFeatures+spec.getValues().get(0).getValue()+",";
	    		    		   }
	    		    	   }
	    		    	   
	    		    	   if(equip.getCategory().equals("Convenience Features")) {
	    		    		   for(Equipment spec: equip.getEquipment()) {
	    		    			   if(spec.getName().equals("Power outlet(s)")) {
	    		    				   specificationVM.powerOutlets = spec.getValues().get(0).getValue();
	    		    			   }
	    		    			   if(spec.getName().equals("Power steering")) {
	    		    				   specificationVM.powerSteering = spec.getValues().get(0).getValue();
	    		    			   }
	    		    			   if(spec.getName().equals("Rear view camera")) {
	    		    				   specificationVM.rearViewCamera = spec.getValues().get(0).getValue();
	    		    			   }
	    		    			   if(spec.getName().equals("Rear view monitor")) {
	    		    				   specificationVM.rearViewMonitor = spec.getValues().get(0).getValue();
	    		    			   }
	    		    			   if(spec.getName().equals("Remote trunk release")) {
	    		    				   specificationVM.remoteTrunkRelease = spec.getValues().get(0).getValue();
	    		    			   }
	    		    			   if(spec.getName().equals("Steering wheel")) {
	    		    				   specificationVM.steeringWheel = spec.getValues().get(0).getValue();
	    		    			   }
	    		    			   if(spec.getName().equals("Steering wheel mounted controls")) {
	    		    				   for(Value val: spec.getValues()) {
	    		    					   specificationVM.steeringWheelControls = specificationVM.steeringWheelControls+val.getValue()+",";
	    		    				   }
	    		    			   }
	    		    		   }
	    		    	   }
	    		    	   
	    		       } 
	    		       
	    		      for(OptionalEquipment opts:mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getUsStyles().get(0).getOptionalEquipment()) {
	    		    	  if(opts.getCategory().equals("Safety")) {
	    		    		  for(Option option : opts.getOptions()) {
	    		    			  if(option.getName().equals("Audi Side Assist")) {
	    		    				  specificationVM.audiSideAssist = option.getDescription();
	    		    			  }
	    		    		  }
	    		    		  
	    		    	  }
	    		      }
	    		       
	    		      specificationVM.trim_level = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getBasicData().getTrim();
	    		      specificationVM.label = "";
	    		      specificationVM.stock = "";
	    		       
	    		      specificationVM.cost = Integer.parseInt(mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getPricing().getInvoicePrice());
	    		      specificationVM.price = Integer.parseInt(mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getPricing().getMsrp());
	    		      specificationVM.colorDesc = "";
	    		      specificationVM.category = "";
	    		      specificationVM.stereo = "";
	    		       
	    		      specificationVM.fuel = "";
	    		      specificationVM.location = "";
	    		      specificationVM.made_in = "";
	    		      specificationVM.optional_seating = "";
	    		      specificationVM.status = "Newly Listed";
	    		      pinObj.specification = specificationVM;
	    		      
	    			
		    	} else {
		    		pinObj.success = true;
		    		pinObj.vin = "WDDNG7KB7DA494890";
					
					SpecificationVM specificationVM = new SpecificationVM();
					specificationVM.vin = "WDDNG7KB7DA494890";
					specificationVM.year = "2013";
					specificationVM.make = "Mercedes-Benz";
					specificationVM.model = "S-Class";
					specificationVM.trim_level = "S65 AMG";
					specificationVM.engine = "6.0L V12 SOHC 36V TURBO";
					specificationVM.style = "SEDAN 4-DR";
					specificationVM.made_in = "GERMANY";
					specificationVM.optional_seating = "";
					specificationVM.highway_mileage = "19 miles/gallon";
					specificationVM.city_mileage = "12 miles/gallon";
					pinObj.specification = specificationVM;
		    	}
		    	
				return ok(Json.toJson(pinObj));
				
	    	} else {
				PinVM pinVM = new PinVM();
				pinVM.success = true;
				pinVM.vin = vehicle.getVin();
				
				SpecificationVM specificationVM = new SpecificationVM();
				specificationVM.category = vehicle.getCategory();
				specificationVM.vin = vehicle.getVin();
				specificationVM.year = vehicle.getYear();
				specificationVM.make = vehicle.getMake();
				specificationVM.model = vehicle.getModel();
				specificationVM.trim_level = vehicle.getTrim();
				specificationVM.label = vehicle.getLabel();
				specificationVM.stock = vehicle.getStock();
				specificationVM.city_mileage = vehicle.getCityMileage();
				specificationVM.highway_mileage = vehicle.getHighwayMileage();
				specificationVM.cost = vehicle.getCost();
				specificationVM.price = vehicle.getPrice();
				specificationVM.made_in = vehicle.getMadeIn();
				specificationVM.optional_seating = vehicle.getOptionalSeating();
				specificationVM.extColor = vehicle.getExteriorColor();
				specificationVM.colorDesc = vehicle.getColorDescription();
				specificationVM.doors = vehicle.getDoors();
				specificationVM.stereo = vehicle.getStereo();
				specificationVM.engine = vehicle.getEngine();
				specificationVM.style = vehicle.getBodyStyle();
				specificationVM.location = vehicle.getLocation();
				specificationVM.description = vehicle.getDescription();
				
				
				specificationVM.drivetrain = vehicle.getDrivetrain();
				specificationVM.fuelType = vehicle.getFuelType();
				specificationVM.fuelTank = vehicle.getFuelTank();
				specificationVM.headlights = vehicle.getHeadlights();
				specificationVM.mirrors = vehicle.getMirrors();
				specificationVM.groundClearance = vehicle.getGroundClearance();
				specificationVM.roof = vehicle.getRoof();
				specificationVM.height = vehicle.getHeight();
				specificationVM.length = vehicle.getLength();
				specificationVM.width = vehicle.getWidth();
				specificationVM.acceleration = vehicle.getAcceleration();
				specificationVM.standardSeating = vehicle.getStandardSeating();
				specificationVM.engineType = vehicle.getEngineType();
				specificationVM.cylinders = vehicle.getCylinders();
				specificationVM.displacement = vehicle.getDisplacement();
				specificationVM.camType = vehicle.getCamType();
				specificationVM.valves = vehicle.getValves();
				specificationVM.fuelQuality = vehicle.getFuelQuality();
				specificationVM.horsePower = vehicle.getHorsePower();
				specificationVM.transmission = vehicle.getTransmission();
				specificationVM.gears = vehicle.getGears();
				specificationVM.brakes = vehicle.getBrakes();
				specificationVM.frontBrakeDiameter = vehicle.getFrontBrakeDiameter();
				specificationVM.frontBrakeType = vehicle.getFrontBrakeType();
				specificationVM.rearBrakeDiameter = vehicle.getRearBrakeDiameter();
				specificationVM.rearBrakeType = vehicle.getRearBrakeType();
				specificationVM.activeHeadRestraints = vehicle.getActiveHeadRestrains();
				specificationVM.bodySideReinforcements = vehicle.getBodySideReinforcements();
				specificationVM.crumpleZones = vehicle.getCrumpleZones();
				specificationVM.impactAbsorbingBumpers = vehicle.getImpactAbsorbingBumpers();
				specificationVM.impactSensors = vehicle.getImpactSensor();
				specificationVM.parkingSensors = vehicle.getParkingSensors();
				specificationVM.seatbelts = vehicle.getSeatbelts();
				specificationVM.audiSideAssist = vehicle.getAudiSideAssist();
				specificationVM.intColor = vehicle.getInteriorColor();
				specificationVM.comfortFeatures = vehicle.getComfortFeatures();
				specificationVM.powerOutlets = vehicle.getPowerOutlet();
				specificationVM.powerSteering = vehicle.getPowerSteering();
				specificationVM.rearViewCamera = vehicle.getRearViewCamera();
				specificationVM.rearViewMonitor = vehicle.getRearViewMonitor();
				specificationVM.remoteTrunkRelease = vehicle.getRemoteTrunkRelease();
				specificationVM.steeringWheel = vehicle.getSteeringWheel();
				specificationVM.steeringWheelControls = vehicle.getSteeringWheelControls();
				
				specificationVM.standardSeating = vehicle.getStandardSeating();
				
				specificationVM.mileage = vehicle.getMileage();
				
				List<Long> siteIds = new ArrayList<>();
				for(Site site: vehicle.getSite()) {
					siteIds.add(site.id);
				}
				specificationVM.siteIds = siteIds;
				
				pinVM.specification = specificationVM;
				
		    	return ok(Json.toJson(pinVM));
	    	}
    	}	
    }
    
    
    
    
    /*public static Result getAddPrice(Long id,Integer price){
    	Vehicle vehicle = Vehicle.findById(id);
    	if(vehicle != null){
    		vehicle.setPrice(price);
    		vehicle.update();
    	}
    	return ok();
    }*/
    
    /*public static Result setArrivelDate(Long id,String aDate){
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    	Vehicle vehicle = Vehicle.findById(id);
    	if(vehicle != null){
    		try {
				vehicle.setComingSoonDate(df.parse(aDate));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		vehicle.update();
    	}
    	return ok();
    }*/
    
   /* public static Result sendComingSoonPOpUp(){
    	List<PriceAlert> price=PriceAlert.getAllRecordPopUp();
    	DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
    	Date cDate = new Date();
    	String cd = df2.format(cDate);
    	Date curDate = null;
		try {
			curDate = df2.parse(cd);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	Date curDate = null;
    	List<RequestInfoVM> rList = new ArrayList<>();
    	//for(PriceAlert alert:price){
    	
    	Date date=new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
			Location location = Location.findById(Long.valueOf(session("USER_LOCATION")));
			df2.setTimeZone(TimeZone.getTimeZone(location.time_zone));
			String date1=df2.format(date);
			try {
				curDate = formatter.parse(date1);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		List<Vehicle> vehList=Vehicle.findByComingSoonDate(curDate);
    		
    		for(Vehicle vehicle:vehList){
    			if(vehicle.locations != null){
        		//	Date date=new Date();
        			//SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        			
            			//Location location = Location.findById(vehicle.locations.id);
            			//df2.setTimeZone(TimeZone.getTimeZone(location.time_zone));
        		//	try {
        				
        				    				
        				//String date1=df2.format(date);
        				
        				//if(vehicle.comingSoonDate.equals(formatter.parse(date1))){
        					RequestInfoVM rVm = new RequestInfoVM();
        					rVm.id = vehicle.id;
        					rVm.vin = vehicle.vin;
        					rVm.make =  vehicle.make;
        					rVm.model = vehicle.model;
        					rVm.year = vehicle.year;
        					rVm.price = vehicle.price;
        					int vCount = 0;
        					List<PriceAlert> vehCount = PriceAlert.getByVin(vehicle.vin);
        					for(PriceAlert pAlert:vehCount){
        						vCount++;
        					}
        					rVm.subscribers = vCount;
        					
        					rVm.comingSoonDate = formatter.format(vehicle.comingSoonDate);
        					VehicleImage vehicleImg = VehicleImage.getDefaultImage(vehicle.vin);
        					if(vehicleImg != null) {
        						rVm.imageUrl = "http://glider-autos.com/glivrImg/images"+vehicleImg.thumbPath;
        					}else {
        						rVm.imageUrl = "/profile-pic.jpg";
        					}
        					
        					rList.add(rVm);
        				//}
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
        			
        		}
    		}
    		
    	//}
    	return ok(Json.toJson(rList));
    }*/
    
    /*public  static Result sendComingSoonEmail(String vin){
    	
    	DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
    	
    		
    		Vehicle vehicle=Vehicle.findByVinAndComingSoonDate(vin);
    		if(vehicle != null){
    			if(vehicle.locations != null){
    			Date date=new Date();
    			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    			
        			Location location = Location.findById(vehicle.locations.id);
        			df2.setTimeZone(TimeZone.getTimeZone(location.time_zone));
    			try {
    				String date1=df2.format(date);
    				System.out.println(">>>>>>>>>>");
    				System.out.println(date1);
    				System.out.println(vehicle.comingSoonDate);
    				System.out.println(formatter.parse(date1));
    				
    				if(vehicle.comingSoonDate.equals(formatter.parse(date1))){
    					vehicle.setComingSoonFlag(0);
    					vehicle.update();
    				}
    				
    				List<PriceAlert> price=PriceAlert.getByVin(vin);
    	    		for(PriceAlert alert:price){
    	    		//	Vehicle vehicle1=Vehicle.findByVinAndComingSoonDate(vin);
    	    			if(alert != null){
    	    				if(alert.locations != null){
    	    				alert.setPopupFlag(0);
        					alert.update();
        					String subject=vehicle.make+" "+vehicle.model+" "+"has Arrived";
    	    	    		String comment="Hi"+" "+alert.name+" "+vehicle.make+" "+vehicle.model+" "+"has Arrived";
    	    		sendEmailForComingSoonVehicle(alert.email,subject,comment,vehicle.vin);
    	    			}
    	    			}
				}
    				
    			}
    			catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			
    			}
    		}
	    	    		
    			
    		
    	return ok();
    }*/
/*public static Result sendEmailForComingSoonVehicle(String email,String subject,String comment,String vin) {
		
		final String username = emailUsername;
		final String password = emailPassword;
		
		Vehicle vehicle = Vehicle.findByVinAndStatus(vin);
		List<Vehicle> sameBodyList = Vehicle.getRandom(vehicle.vin);
		 SiteLogo logo = SiteLogo.findByLocation(Long.valueOf(session("USER_LOCATION")));
		 
		 Vehicle sameBodyStyle = sameBodyList.get(0);
			VehicleImage sameBodyStyleDefault = VehicleImage.getDefaultImage(sameBodyStyle.vin);
			
			Vehicle sameEngine = sameBodyList.get(1);
			VehicleImage sameEngineDefault = VehicleImage.getDefaultImage(sameEngine.vin);
			
			Vehicle sameMake =  sameBodyList.get(2);
			VehicleImage sameMakeDefault = VehicleImage.getDefaultImage(sameMake.vin);
		Properties props = new Properties();  
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.starttls.enable", "true");
		System.out.println(email);
		System.out.println(username);
		System.out.println(password);
		
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		    try {  
		     MimeMessage message = new MimeMessage(session);  
		     message.setFrom(new InternetAddress(username));  
		     message.addRecipient(Message.RecipientType.TO,new InternetAddress(email));  
		     message.setSubject(subject);  
		     message.setText(comment);  
		     Transport.send(message);  
		  
		     System.out.println("message sent successfully...");  
		   
		     } catch (MessagingException e) {
		    	 e.printStackTrace();
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
    		}
    		catch(UnsupportedEncodingException e){
    			e.printStackTrace();
    		}
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(email));
			message.setSubject("Vehicle that you were interested in has arrived");
			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			ve.init();
		
			
	        Template t = ve.getTemplate("/public/emailTemplate/NotifyMeVehicleLaunch.vm"); 
	        VelocityContext context = new VelocityContext();
	        
	        context.put("hostnameUrl", imageUrlPath);
	        context.put("siteLogo", logo.logoImagePath);
	        
	        context.put("year", vehicle.year);
	        context.put("make", vehicle.make);
	        context.put("model", vehicle.model);
	        context.put("vins", vehicle.vin);
	       // context.put("oldPrice", "$"+alert.oldPrice);
	        
	        if(vehicle.price != null) {
	        	context.put("newPrice", "$"+vehicle.price);
	        	} else {
	        		context.put("newPrice", "");
	        	}
	        context.put("bodyStyle", vehicle.bodyStyle);
	        context.put("mileage", vehicle.mileage);
	        
	        if(vehicle.doors != null) {
	        	context.put("doors", vehicle.doors);
	        	} else {
	        		context.put("doors", "");
	        	}
	        
	        
	        if(vehicle.standardSeating != null) {
	        	context.put("seats", vehicle.standardSeating);
	        	} else {
	        		context.put("seats", "" );
	        	}
	        
	        if(vehicle.drivetrain != null) {
	        	context.put("driveTrain", vehicle.drivetrain);
	        	} else {
	        		context.put("driveTrain", "");
	        	}
	        
	        if(vehicle.engine != null) {
	        	context.put("engine", vehicle.engine);
	        	} else {
	        		context.put("engine", "");
	        	}
	        
	        
	        if(vehicle.transmission != null) {
	        	 context.put("transmission", vehicle.transmission);
	        	} else {
	        		 context.put("transmission", "");
	        	}
	        
	        if(vehicle.brakes != null) {
	        	context.put("brakes", vehicle.brakes);
	        	} else {
	        		context.put("brakes", "");
	        	}
	        
	        
	        if(vehicle.horsePower != null) {
	        	context.put("horsePower", vehicle.horsePower);
	        	} else {
	        		context.put("horsePower", "");
	        	}
	        
	        context.put("email", profile.email);
	        String firstThreeDigit=profile.phone;
	        firstThreeDigit=firstThreeDigit.substring(0, 3);
	        String secondThreeDigit=profile.phone;
	        secondThreeDigit=secondThreeDigit.substring(3, 6);
	        String thirdThreeDigit=profile.phone;
	        thirdThreeDigit=thirdThreeDigit.substring(6, 10);
	        context.put("firstThreeDigit", firstThreeDigit);
	        context.put("secondThreeDigit", secondThreeDigit);
	        context.put("thirdThreeDigit", thirdThreeDigit);
	        
	        //context.put("phone", profile.phone);
	        if(sameBodyStyle != null) {
	        	if(sameBodyStyle.price != null) {
	        		context.put("bodyStylePrice", "$"+sameBodyStyle.price.toString());
	        	} else {
	        		context.put("bodyStylePrice", "");
	        	}
	        	context.put("bodyStyleVin", sameBodyStyle.vin);
	        	context.put("bodyStyleYear", sameBodyStyle.year);
	        	context.put("bodyStyleMake", sameBodyStyle.make);
	        	context.put("bodyStyleModel", sameBodyStyle.model);
	        } else {
	        	context.put("bodyStylePrice", "");
	        	context.put("bodyStyleVin", "");
	        }
	        if(sameEngine != null) {
	        	if(sameEngine.price != null) {
	        		context.put("enginePrice", "$"+sameEngine.price.toString());
	        	} else {
	        		context.put("enginePrice", "");
	        	}
	        	context.put("engineVin", sameEngine.vin);
	        	context.put("engineMake", sameEngine.make);
	        	context.put("engineYear", sameEngine.year);
	        	context.put("engineModel", sameEngine.model);
	        } else {
	        	context.put("enginePrice","");
	        	context.put("engineVin", "");
	        }
	        if(sameMake != null) {
	        	if(sameMake.price != null) {
	        		context.put("makePrice", "$"+sameMake.price.toString());
	        	} else {
	        		context.put("makePrice", "");
	        	}
	        	context.put("makeVin", sameMake.vin);
	        	context.put("sameMake", sameMake.make);
	        	context.put("sameModel", sameMake.model);
	        	context.put("sameYear", sameMake.year);
	        } else {
	        	context.put("makePrice", "");
	        	context.put("makeVin", "");
	        }
	        
	        if(sameBodyStyleDefault != null) {
	        	context.put("sameBodyStyleDefault", sameBodyStyleDefault.thumbPath);
	        } else {
	        	context.put("sameBodyStyleDefault", "/no-image.jpg");
	        }
	        if(sameEngineDefault != null) {
	        	context.put("sameEngineDefault", sameEngineDefault.thumbPath);
	        } else {
	        	context.put("sameEngineDefault", "/no-image.jpg");
	        }
	        if(sameMakeDefault != null) {
	        	context.put("sameMakeDefault", sameMakeDefault.thumbPath);
	        } else {
	        	context.put("sameMakeDefault", "/no-image.jpg");
	        }
	        VehicleImage image = VehicleImage.getDefaultImage(vehicle.vin);
	        if(image != null) {
	        	context.put("defaultImage", image.path);
	        } else {
	        	context.put("defaultImage", "/no-image.jpg");
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

				
		return ok();
	}
    */
    
    
    
    public static Result saveInternalPdf() throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		Identity user = getLocalUser();
	    	AuthUser userObj = (AuthUser)user;
    		MultipartFormData body = request().body().asMultipartFormData();
    	    InternalPdf pdf=new InternalPdf();
    			if(body != null){
		    		FilePart picture = body.getFile("file0");
		    		if (picture != null) {
		    			String fileName = picture.getFilename().replaceAll("[-+^:,() ]","");
		    			File file = picture.getFile();
		    			try {
		    				File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+"Internal_Pdf");
		    	    	    if(!fdir.exists()) {
		    	    	    	fdir.mkdir();
		    	    	    }
		    	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"Internal_Pdf"+File.separator+fileName;
		    	    	    FileUtils.moveFile(file, new File(filePath));
		    	    	    pdf.pdf_name=fileName;
		    	    	    pdf.pdf_path=session("USER_LOCATION")+File.separator+"Internal_Pdf"+File.separator+fileName;
		    	    	    pdf.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    	    	    pdf.save();
		    			} catch (Exception e) {
							e.printStackTrace();
						}
		    		}
    		}
    	}
    	return ok();
    }
    
   
    
   
    
    public static Result getAutoPortalData(){
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		List<HoursOfOperation>list=HoursOfOperation.findByTypeForServices();
    		DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
    		List <HoursOperation> alist=new ArrayList<>();
    		HoursOperation vm=new HoursOperation();
    		for (HoursOfOperation op : list) {
    			
    			String day=(String)op.getDay();
    			if(day != null){
    				if(day.equalsIgnoreCase("Sunday")){
    					if(op.getOpenTime() != null){
    					vm.sunOpenTime=dateFormat.format((Date)op.getOpenTime());
    					}
    					if(op.getCloseTime() != null){
    					vm.sunCloseTime=dateFormat.format((Date)op.getCloseTime());
    					}
    					vm.sunFlag=op.getDayFlag();
    					if(vm.sunFlag == 1){
    						vm.sunOpen=true;
    					}
    					vm.checkForSunday=op.getCheckValue();
    					if(vm.checkForSunday == 1){
    						vm.serviceCheck=true;
    					}
    					System.out.println(">>>>");
    					System.out.println(vm.satOpenTime);
    					System.out.println(vm.satCloseTime);
    					System.out.println(vm.sunFlag);
    				}
    				if(day.equalsIgnoreCase("Monday")){
    					if(op.getOpenTime() != null){
    					vm.monOpenTime=dateFormat.format((Date)op.getOpenTime());
    					}
    					if(op.getCloseTime() != null){
    					vm.monCloseTime=dateFormat.format((Date)op.getCloseTime());
    					}
    					vm.monFlag=op.getDayFlag();
    					if(vm.monFlag == 1){
    						vm.monOpen=true;
    					}
    					
    					vm.checkForMonday=op.getCheckValue();
    					if(vm.checkForSunday == 1){
    						vm.serviceCheck=true;
    					}
    					
    					
    				}
    				
    				if(day.equalsIgnoreCase("Tuesday")){
    					if(op.getOpenTime() != null){
    					vm.tueOpenTime=dateFormat.format((Date)op.getOpenTime());
    					}
    					if(op.getCloseTime() != null){
    					vm.tueCloseTime=dateFormat.format((Date)op.getCloseTime());
    					}
    					vm.tueFlag=op.getDayFlag();
    					
    					if(vm.tueFlag == 1){
    						vm.tueOpen=true;
    					}
    					
    				}
    				if(day.equalsIgnoreCase("Wednesday")){
    					if(op.getOpenTime() != null){
    					vm.wedOpenTime=dateFormat.format((Date)op.getOpenTime());
    					}
    					if(op.getCloseTime() != null){
    					vm.wedCloseTime=dateFormat.format((Date)op.getCloseTime());
    					}
    					vm.wedFlag=op.getDayFlag();
    					if(vm.wedFlag == 1){
    						vm.wedOpen=true;
    					}
    					
    				}
    				if(day.equalsIgnoreCase("Thursday")){
    					if(op.getOpenTime() != null){
    					vm.thuOpenTime=dateFormat.format((Date)op.getOpenTime());
    					}
    					if(op.getCloseTime() != null){
    					vm.thuCloseTime=dateFormat.format((Date)op.getCloseTime());
    					}
    					vm.thuFlag=op.getDayFlag();
    					if(vm.thuFlag == 1){
    						vm.thuOpen=true;
    					}
    					
    					
    				}
    				
    				if(day.equalsIgnoreCase("Friday")){
    					if(op.getOpenTime() != null){
    					vm.friOpenTime=dateFormat.format((Date)op.getOpenTime());
    					}
    					if(op.getCloseTime() != null){
    					vm.friCloseTime=dateFormat.format((Date)op.getCloseTime());
    					}
    					vm.friFlag=op.getDayFlag();
    					if(vm.friFlag == 1){
    						vm.friOpen=true;
    					}
    					
    					
    				}
    				
    				if(day.equalsIgnoreCase("Saturday")){
    					if(op.getOpenTime() != null){
    					vm.satOpenTime=dateFormat.format((Date)op.getOpenTime());
    					}
    					if(op.getCloseTime() != null){
    					vm.satCloseTime=dateFormat.format((Date)op.getCloseTime());
    					}
    					vm.satFlag=op.getDayFlag();
    					if(vm.satFlag == 1){
    						vm.satOpen=true;
    					}
    					
    					
    				}
    				
    				
    			}
    			
    			
			}
    		
    		alist.add(vm);
    		return ok(Json.toJson(alist));
    	}
	}	
	
    
    
    
    
    
    
    
    
    public static Result saveCustomerPdf() throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		Identity user = getLocalUser();
	    	AuthUser userObj = (AuthUser)user;
    		MultipartFormData body = request().body().asMultipartFormData();
    	    CustomerPdf pdf=new CustomerPdf();
    			if(body != null){
		    		FilePart picture = body.getFile("file0");
		    		if (picture != null) {
		    			String fileName = picture.getFilename().replaceAll("[-+^:,() ]","");
		    			File file = picture.getFile();
		    			try {
		    				File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+"Customer_Pdf");
		    	    	    if(!fdir.exists()) {
		    	    	    	fdir.mkdir();
		    	    	    }
		    	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"Customer_Pdf"+File.separator+fileName;
		    	    	    FileUtils.moveFile(file, new File(filePath));
		    	    	    pdf.pdf_name=fileName;
		    	    	    pdf.pdf_path=session("USER_LOCATION")+File.separator+"Customer_Pdf"+File.separator+fileName;
		    	    	    pdf.locations=Location.findById(Long.valueOf(session("USER_LOCATION")));
		    	    	    pdf.save();
		    			} catch (Exception e) {
							e.printStackTrace();
						}
		    		}
    		}
    	}
    	return ok();
    }
       
    
    public static Result  getCustomerPdfForVehicle(String vin){
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    	 //CustomerPdf pdf = CustomerPdf.findPdfById(id);
    		Vehicle vehicle=Vehicle.findByVin(vin);
    		DocumentationVM vm = new DocumentationVM();
    		if(vehicle != null){
    			vm.customerPdfName=vehicle.pdfBrochureName;
    			vm.customerPdfPath=vehicle.pdfBrochurePath;
    			vm.vehicleVin=vehicle.vin;
    		}
    		return ok(Json.toJson(vm));
    	}
	}
	
    
   /* public static Result getAutoDealerTableData(){
    	List<AuthUser> auUser = AuthUser.getOnlyPhotographer();
    	List<UserVM> userlist = new ArrayList<>();
    	System.out.println("(((())0909090");
    	for(AuthUser au:auUser){
    		UserVM use = new UserVM();
    		use.firstName = au.firstName;
    		use.lastName = au.lastName;
    		use.locationId = au.location.id;
    		use.password = au.password;
    		use.role = au.role;
    		use.email = au.email;
    		userlist.add(use);
    	}
    	return ok(Json.toJson(userlist));
    }*/
    
    public static Result getLocationDays(){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser userObj = (AuthUser) getLocalUser();
    		Location loc= Location.findById(userObj.location.id);
    		Date date = new Date();
    		long diff;
    		long days = 0;
    		SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
    		String modifiedDate= myFormat.format(new Date());
    		try {
    			date = myFormat.parse(modifiedDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
    		
    		if(loc.createdDate !=null){
    			diff = date.getTime() - loc.createdDate.getTime();
    			days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    		}
    		
    	/*------------------Demo test-----------------------*/
    		
    	/*List<SqlRow> rows = Vehicle.getDataInAutodealar();
	    	for(SqlRow row : rows) {
	    		String pass = (String) row.get("password");
	    		String pass1 = (String) row.get("userName");
	    		
	    	}*/
    		return ok(Json.toJson(days));
    	}
    }
    
    private static void reorderImagesForFirstTime(List<InventoryImage> imageList) {
    	if(imageList.size() > 0) {
    		if(imageList.get(0).row == null) {
    			for(int i = 0, col = 0 ; i < imageList.size() ; i++) {
    				imageList.get(i).setRow(  col / 6);
    				imageList.get(i).setCol( col % 6);
    				col++;
    				imageList.get(i).update();
    			}
    			
    		}
    		
    	}
		
    }
    
    public  static Result changeNotifFlag(long id,String title){
    	if(title.equalsIgnoreCase("month plan")){
    		PlanScheduleMonthlySalepeople plan=PlanScheduleMonthlySalepeople.findById(id);
    		plan.setNotifFlag(1);
    		plan.update();
    	}
    	else if(title.equalsIgnoreCase("invitation received")){
    		ScheduleTest plan=ScheduleTest.findById(id);
    		plan.setMeetingNotifFlag(1);
    		plan.update();
    	}
    	
    	else if(title.equalsIgnoreCase("accept meeting")){
    		ScheduleTest plan=ScheduleTest.findById(id);
    		plan.setMeetingAcceptFlag(0);
    		plan.update();
    	}
    	else if(title.equalsIgnoreCase("declined meeting")){
    		ScheduleTest plan=ScheduleTest.findById(id);
    		plan.setMeetingDeclineFlag(0);
    		plan.update();
    	}
    	else if(title.equalsIgnoreCase("coming soon")){
    		Vehicle veh=Vehicle.findById(id);
        	veh.setNotifFlag(1);
        	veh.update();
    	}
    	
    	return ok();
    }
    
    public static Result releaseFromNotif(Long id,String leadType) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		if(leadType.equals("Schedule Test")) {
    			ScheduleTest schedule = ScheduleTest.findById(id);
    			schedule.setNotifFlag(1);
    			
    			schedule.update();
    			
    		}
			if(leadType.equals("Request More Info")) {
			    RequestMoreInfo info = RequestMoreInfo.findById(id);
			    info.setNotifFlag(1);
			    info.update();
			    
               
			}
			if(leadType.equals("Trade In")) {
				TradeIn tradeIn = TradeIn.findById(id);
				tradeIn.setNotifFlag(1);
				tradeIn.update();
				
			
			}
    		return ok();
    	}
    }
    
    public static Result getNotificationData(){
    	Map<String, Object> mapList = new HashMap<>();
		
		AuthUser user = (AuthUser) getLocalUser();
		
		/*-----------------------Like comment---------------------*/
    	
		
		List<UserVM> listU = new ArrayList<>();
		List<Comments> comments = Comments.getByListUserWithFlag(user);
		for(Comments comm:comments){
			UserVM uVm = new UserVM();
			uVm.firstName = comm.commentUser.getFirstName();
			uVm.lastName = comm.commentUser.getLastName();
			uVm.id = comm.commentUser.id;
			uVm.userComment=comm.comment;
			if(comm.commentUser.imageUrl != null) {
				if(comm.commentUser.imageName !=null){
					uVm.imageUrl = "http://glider-autos.com/MavenImg/images"+comm.commentUser.imageUrl;
				}else{
					uVm.imageUrl = comm.commentUser.imageUrl;
				}
				
			} else {
				uVm.imageUrl = "/profile-pic.jpg";
			}
			
			listU.add(uVm);
			
			comm.setCommentFlag(0);
			comm.update();
			
		}
    	
    	mapList.put("commentLike", listU);
    	
    	/*----------------------------Plan schedule-----------------------*/
    	
    	List<PlanScheduleMonthlySalepeople> salepeople = PlanScheduleMonthlySalepeople.findByAllMsg(user);
    	
    	List<RequestInfoVM> rList1 = new ArrayList<>();
    	for(PlanScheduleMonthlySalepeople sales:salepeople){
    	if(sales != null){
    	RequestInfoVM rVm = new RequestInfoVM();
    					rVm.month = sales.month;
    					Date date=new Date();
    					Date newDate=sales.saveDate;
    					Date curDate1=null;
    					Date curDate=null;
    					DateFormat form = new SimpleDateFormat("yyyy-MM-dd");
    					String currD=form.format(date);
    					//String currD="2016-07-01";
    					String arr[]=currD.split("-");
    					String newD=arr[0]+"-"+arr[1]+"-"+"01";
    					if(currD.equals(newD) && newDate != null){
    					String planD=form.format(newDate);
    					//if(currD.equals(arg0))
    					DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
    					 DateFormat df11 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
    				    Location location = Location.findById(Long.valueOf(session("USER_LOCATION")));
    						df11.setTimeZone(TimeZone.getTimeZone(location.time_zone));
    						String date1=df11.format(date);
    						String dateNew=df1.format(newDate);
    						String date11="00:00:AM";
    						
    						try {
    							curDate1=df1.parse(date1);
    							curDate = df1.parse(dateNew);
    						} catch (ParseException e1) {
    							// TODO Auto-generated catch block
    							e1.printStackTrace();
    						}
    						
    						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
    						  long diff = curDate1.getTime() - curDate.getTime();
    			  	        long diffSeconds = diff / 1000 % 60;
    			  	        long diffMinutes = diff / (60 * 1000) % 60;
    			  	        	long diffHours = diff / (60 * 60 * 1000)% 24;
    			  	        	int diffInDays = (int) ((curDate1.getTime() - curDate.getTime()) / (1000 * 60 * 60 * 24));
    			    	        String diffDay=null;
    			    	        String diffHr=null;
    			    	        if(diffInDays != 0){
    			    	        if(diffInDays <10){
    			    	        	
    			    	        	diffDay=""+diffInDays;
    			    	        }
    			    	        else{
    			    	        	diffDay=""+diffInDays;
    			    	        }
    			    	        if(diffHours <10){
    			    	        	diffHr="0"+diffHours;
    			    	        }
    			    	        else{
    			    	        	diffHr=""+diffHours;
    			    	        }
    			    	        rVm.diffDays=diffDay+" + days";
    			    	        }
    			    	        else if(diffInDays == 0 && diffHours == 0){
    			    	        	rVm.diffDays=diffMinutes+" minutes ago";;
    			        	     
    			        	        }
    			    	        else{
    			    	        	
    			    	        	 if(diffHours <10){
    			    	    	        	diffHr="0"+diffHours;
    			    	    	        }
    			    	    	        else{
    			    	    	        	diffHr=""+diffHours;
    			    	    	        }
    			    	        	 rVm.diffDays=diffHr+" hours "+diffMinutes+" minutes ago";
    			    	        }
    			  	        	
    			  	     rVm.id=sales.id;	
    			  	     rVm.flagMsg=sales.flagMsg; 	
    					rList1.add(rVm);
    					}
    			
    		}
		}
    	mapList.put("planScheduleMonthly", rList1);
    for(PlanScheduleMonthlySalepeople sales:salepeople){
    		
    		sales.setFlagMsg(0);
    		sales.update();
    		
    	}
    	
    	/*-------------------Coming soon--------------------------*/
    	
    	//List<PriceAlert> price=PriceAlert.getAllRecordPopUp();
    	DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
    	
    	
    	Date curDate = null;
    	Date curDate1 = null;
    	Date curDateNew = null;
    	//List<RequestInfoVM> rList = new ArrayList<>();
    	Date date=new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
		DateFormat format1 = new SimpleDateFormat("HH:mm:a");
		 DateFormat df11 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
			//Location location = Location.findById(Long.valueOf(session("USER_LOCATION")));
			//df2.setTimeZone(TimeZone.getTimeZone(location.time_zone));
			//df11.setTimeZone(TimeZone.getTimeZone(location.time_zone));
			String date1=df2.format(date);
			String dateNew=df11.format(date);
			String date11="00:00:AM";
			
			try {
				curDate1=df1.parse(dateNew);
				curDate = formatter.parse(date1);
				curDateNew=format1.parse(date11);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
			  long diff = curDate1.getTime() - curDateNew.getTime();
  	        long diffSeconds = diff / 1000 % 60;
  	        long diffMinutes = diff / (60 * 1000) % 60;
  	        	long diffHours = diff / (60 * 60 * 1000)% 24;
			
			
    		/*List<Vehicle> vehList=Vehicle.findByComingSoonDate(curDate);
    		
    		for(Vehicle vehicle:vehList){
    			if(vehicle.locations != null){
        	RequestInfoVM rVm = new RequestInfoVM();
        					rVm.id = vehicle.id;
        					rVm.vin = vehicle.vin;
        					rVm.make =  vehicle.make;
        					rVm.model = vehicle.model;
        					rVm.year = vehicle.year;
        					rVm.price = vehicle.price;
        					if(diffHours != 0){
        					rVm.diffDays=diffHours+" hours"+diffMinutes+" minutes ago";
        					}
        					else{
        						rVm.diffDays=diffMinutes+" minutes ago";
        					}
        					int vCount = 0;
        					List<PriceAlert> vehCount = PriceAlert.getByVin(vehicle.vin);
        					for(PriceAlert pAlert:vehCount){
        						vCount++;
        					}
        					rVm.subscribers = vCount;
        					
        					rVm.comingSoonDate = formatter.format(vehicle.comingSoonDate);
        					AddCollection vehicleImg = AddCollection.getDefaultImg(vehicle.id);
        					if(vehicleImg != null) {
        						rVm.imageUrl = "http://glider-autos.com/MavenImg/images"+vehicleImg.filePath;
        					}else {
        						rVm.imageUrl = "/profile-pic.jpg";
        					}
        					
        					rList.add(rVm);
        				
        			
        		}
    		}*/
    		
    		
    		//mapList.put("comingSoonData", rList);
    		
    	/*-----------------------Invitation---------------------------------*/
    		
    	      DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
  	        Date currD = new Date();
  	        String cDate = df.format(currD);
  	        Date datec = null;
  	        try {
  				datec = df.parse(cDate);
  			} catch (ParseException e) {
  				// TODO Auto-generated catch block
  				e.printStackTrace();
  			}
  	        
  		List<ScheduleTest> list = ScheduleTest.findAllByInvitation(user, datec);
  		
  		List<RequestInfoVM> checkData = new ArrayList<>();
  		for(ScheduleTest sche:list){
  			
  			RequestInfoVM sTestVM = new RequestInfoVM();
          	
          	
  			sTestVM.id = sche.id;
          	sTestVM.confirmDate = new SimpleDateFormat("MM-dd-yyyy").format(sche.confirmDate);
          	sTestVM.confirmTime = new SimpleDateFormat("hh:mm a").format(sche.confirmTime);
          	sTestVM.confirmDateOrderBy = sche.confirmDate;
          	sTestVM.typeOfLead = "Schedule Test Drive";
          	sTestVM.name = sche.name;
      		sTestVM.phone = sche.phone;
      		sTestVM.email = sche.email;
      		sTestVM.sendInvitation=sche.sendInvitation;
      		AuthUser user2 = AuthUser.findById(sche.user.id);
      		if(user2.imageUrl != null) {
  				if(user2.imageName !=null){
  					sTestVM.imageUrl = "http://glider-autos.com/MavenImg/images"+user2.imageUrl;
  				}else{
  					sTestVM.imageUrl = user2.imageUrl;
  				}
  				
  			} else {
  				sTestVM.imageUrl = "/profile-pic.jpg";
  			}
      		
      		
      		Date schDate=new Date();
			Date schcurDate11=null;
			Date schcurDate1=null;
      		DateFormat schDatedf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
			 DateFormat schDatedf11 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
		    Location location1 = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    schDatedf11.setTimeZone(TimeZone.getTimeZone(location1.time_zone));
				String schDate1=schDatedf11.format(schDate);
				String dateNew1=schDatedf11.format(sche.scheduleTime);
				
				try {
					schcurDate11=schDatedf1.parse(schDate1);
					schcurDate1 =schDatedf1.parse(dateNew1);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				  long schdiff = schcurDate11.getTime() - schcurDate1.getTime();
	  	        long diffSeconds1 = diff / 1000 % 60;
	  	        long diffMinutes1 = diff / (60 * 1000) % 60;
	  	        	long diffHours1 = diff / (60 * 60 * 1000)% 24;
	  	        	int diffInDays1 = (int) ((schcurDate11.getTime() - schcurDate1.getTime()) / (1000 * 60 * 60 * 24));
	    	        String diffDay=null;
	    	        String diffHr=null;
	    	        if(diffInDays1 != 0){
	    	        if(diffInDays1<10){
	    	        	
	    	        	diffDay=""+diffInDays1;
	    	        }
	    	        else{
	    	        	diffDay=""+diffInDays1;
	    	        }
	    	        if(diffHours <10){
	    	        	diffHr=""+diffHours1;
	    	        }
	    	        else{
	    	        	diffHr=""+diffHours1;
	    	        }
	    	        sTestVM.diffDays=diffDay+" + days";
	    	        }
	    	        else if(diffInDays1 == 0 && diffHours1 == 0){
	    	        	sTestVM.diffDays=diffMinutes1+" minutes ago";;
	        	     
	        	        }
	    	        else{
	    	        	
	    	        	 if(diffHours1 <10){
	    	    	        	diffHr=""+diffHours1;
	    	    	        }
	    	    	        else{
	    	    	        	diffHr=""+diffHours1;
	    	    	        }
	    	        	 sTestVM.diffDays=diffHr+" hours "+diffMinutes1+" minutes ago";
	    	        }
      		
      		
      		checkData.add(sTestVM);
      		
  			sche.setSendInvitation(0);
  			sche.update();
  		}
  		
  		mapList.put("invitationData", checkData);
  		
  		
  		/*-------------Decline Metting--------------------------------*/
  		
    	
    	List<ScheduleTest> sche = ScheduleTest.getdeclineMeeting(user);
    	List<RequestInfoVM> acList1 = new ArrayList<>();
    	for(ScheduleTest sch1:sche){
    	if(sch1 != null){
    	RequestInfoVM rVm = new RequestInfoVM();
    	AuthUser user1=AuthUser.findById(sch1.assignedTo.id);
    					rVm.firstName = user1.firstName;
    					rVm.lastName = user1.lastName;
    					rVm.name = sch1.name;
    					rVm.id = sch1.id;
    					rVm.declineMeeting=sch1.declineMeeting;
    					rVm.declineReason=sch1.declineReason;
    					Date schDate=new Date();
    					Date schcurDate11=null;
    					Date schcurDate1=null;
    					DateFormat schDatedf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
    					 DateFormat schDatedf11 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
    				    Location location1 = Location.findById(Long.valueOf(session("USER_LOCATION")));
    				    schDatedf11.setTimeZone(TimeZone.getTimeZone(location1.time_zone));
    						String schDate1=schDatedf11.format(schDate);
    						String dateNew1=schDatedf11.format(sch1.meetingActionTime);
    						
    						try {
    							schcurDate11=schDatedf1.parse(schDate1);
    							schcurDate1 =schDatedf1.parse(dateNew1);
    						} catch (ParseException e1) {
    							// TODO Auto-generated catch block
    							e1.printStackTrace();
    						}
    						
    						  long schdiff = schcurDate11.getTime() - schcurDate1.getTime();
    			  	        long diffSeconds1 = schdiff / 1000 % 60;
    			  	        long diffMinutes1 = schdiff / (60 * 1000) % 60;
    			  	        	long diffHours1 = diff / (60 * 60 * 1000)% 24;
    			  	        	int diffInDays1 = (int) ((schcurDate11.getTime() - schcurDate1.getTime()) / (1000 * 60 * 60 * 24));
    			    	        String diffDay=null;
    			    	        String diffHr=null;
    			    	        if(diffInDays1 != 0){
    			    	        if(diffInDays1<10){
    			    	        	
    			    	        	diffDay=""+diffInDays1;
    			    	        }
    			    	        else{
    			    	        	diffDay=""+diffInDays1;
    			    	        }
    			    	        if(diffHours <10){
    			    	        	diffHr="0"+diffHours1;
    			    	        }
    			    	        else{
    			    	        	diffHr=""+diffHours1;
    			    	        }
    			    	        rVm.diffDays=diffDay+" + days";
    			    	        }
    			    	        else if(diffInDays1 == 0 && diffHours1 == 0){
    			    	        	rVm.diffDays=diffMinutes1+" minutes ago";;
    			        	     
    			        	        }
    			    	        else{
    			    	        	
    			    	        	 if(diffHours1 <10){
    			    	    	        	diffHr="0"+diffHours1;
    			    	    	        }
    			    	    	        else{
    			    	    	        	diffHr=""+diffHours1;
    			    	    	        }
    			    	        	 rVm.diffDays=diffHr+" hours "+diffMinutes1+" minutes ago";
    			    	        }
    			  	        	
    			  	        	
    			  	        	
    			    	        acList1.add(rVm);
    			    	        
    			    	        for(ScheduleTest sch:sche){
    			    	    		sch.setDeclineMeeting(0);
    			    	    		sch.update();
    			    	    	}
    			
    		}
		}
    	
    	mapList.put("declineMeeting", acList1);
    	
    	
    	
    	
    	/*-----------------accept msg--------------------------*/
		
    	List<ScheduleTest> sche1 = ScheduleTest.getacceptMeeting(user);
    	List<RequestInfoVM> acList = new ArrayList<>();
    	for(ScheduleTest sch1:sche1){
    	if(sch1 != null){
    	RequestInfoVM rVm = new RequestInfoVM();
    	AuthUser usersData = AuthUser.findById(sch1.assignedTo.id);
    					rVm.firstName = usersData.firstName;
    					rVm.lastName = usersData.lastName;
    					rVm.name = sch1.name;
    					rVm.acceptMeeting=sch1.acceptMeeting;
    					rVm.id = sch1.id;
    					Date schDate=new Date();
    					Date schcurDate11=null;
    					Date schcurDate1=null;
    					DateFormat schDatedf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
    					 DateFormat schDatedf11 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
    				    Location location1 = Location.findById(Long.valueOf(session("USER_LOCATION")));
    				    schDatedf11.setTimeZone(TimeZone.getTimeZone(location1.time_zone));
    						String schDate1=schDatedf11.format(schDate);
    						String dateNew1=schDatedf11.format(sch1.meetingActionTime);
    						
    						try {
    							schcurDate11=schDatedf1.parse(schDate1);
    							schcurDate1 =schDatedf1.parse(dateNew1);
    						} catch (ParseException e1) {
    							// TODO Auto-generated catch block
    							e1.printStackTrace();
    						}
    						
    						  long schdiff = schcurDate11.getTime() - schcurDate1.getTime();
    			  	        long diffSeconds1 = diff / 1000 % 60;
    			  	        long diffMinutes1 = diff / (60 * 1000) % 60;
    			  	        	long diffHours1 = diff / (60 * 60 * 1000)% 24;
    			  	        	int diffInDays1 = (int) ((schcurDate11.getTime() - schcurDate1.getTime()) / (1000 * 60 * 60 * 24));
    			    	        String diffDay=null;
    			    	        String diffHr=null;
    			    	        if(diffInDays1 != 0){
    			    	        if(diffInDays1<10){
    			    	        	
    			    	        	diffDay=""+diffInDays1;
    			    	        }
    			    	        else{
    			    	        	diffDay=""+diffInDays1;
    			    	        }
    			    	        if(diffHours <10){
    			    	        	diffHr="0"+diffHours1;
    			    	        }
    			    	        else{
    			    	        	diffHr=""+diffHours1;
    			    	        }
    			    	        rVm.diffDays=diffDay+" + days";
    			    	        }
    			    	        else if(diffInDays1 == 0 && diffHours1 == 0){
    			    	        	rVm.diffDays=diffMinutes1+" minutes ago";;
    			        	     
    			        	        }
    			    	        else{
    			    	        	
    			    	        	 if(diffHours1 <10){
    			    	    	        	diffHr="0"+diffHours1;
    			    	    	        }
    			    	    	        else{
    			    	    	        	diffHr=""+diffHours1;
    			    	    	        }
    			    	        	 rVm.diffDays=diffHr+" hours "+diffMinutes1+" minutes ago";
    			    	        }
    			  	        	
    			  	        	
    			  	        	
    			    	        acList.add(rVm);
    			    	        for(ScheduleTest sch2:sche1){
    			    	    		sch2.setAcceptMeeting(0);
    			    	    		sch2.update();
    			    	    	}
    			
    		}
		}
    	mapList.put("acceptedMeeting", acList);
    	
    	
    	
    	
    	
    	
    	
    	/*---------------------delete meeting-------------------------------*/
    	
    	SimpleDateFormat df3 = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat parseTime = new SimpleDateFormat("hh:mm a");
    	List<ScheduleTest> sche2 = ScheduleTest.getdeleteMsg(user);
    	
    	List<ScheduleTestVM> list1 = new ArrayList<ScheduleTestVM>();
    	for(ScheduleTest sch:sche2){
    		if(sch.declineUser.equals("Host")){
    			if(!user.id.equals(sch.user.id)){
    				
    				sch.setDeleteMsgFlag(0);
    	    		sch.update();
    				
    				ScheduleTestVM sLVm = new ScheduleTestVM();
    	    		sLVm.name = sch.name;
    	    		sLVm.reason = sch.reason;
    	    		sLVm.confirmDate = df3.format(sch.confirmDate);
    	    		sLVm.confirmTime = parseTime.format(sch.confirmTime);
    	    		AuthUser usersData = AuthUser.findById(sch.assignedTo.id);
    	    		sLVm.firstName = usersData.firstName;
    	    		sLVm.lastName = usersData.lastName;
    	    		sLVm.declineUser = sch.declineUser;
    	    		list1.add(sLVm);
    			}
    		}else if(sch.declineUser.equals("this person")){
    			
    			if(!user.id.equals(sch.assignedTo.id)){
    				sch.setDeleteMsgFlag(0);
            		sch.update();
        			
        			ScheduleTestVM sLVm = new ScheduleTestVM();
    	    		sLVm.name = sch.name;
    	    		sLVm.reason = sch.reason;
    	    		sLVm.confirmDate = df.format(sch.confirmDate);
    	    		sLVm.confirmTime = parseTime.format(sch.confirmTime);
    	    		AuthUser usersData = AuthUser.findById(sch.assignedTo.id);
    	    		sLVm.firstName = usersData.firstName;
    	    		sLVm.lastName = usersData.lastName;
    	    		sLVm.declineUser = sch.declineUser;
    	    		list1.add(sLVm);
    			}
    		}
    	}
    	
    	mapList.put("deleteMeeting", list1);
    	
    	
    	/*-------update meeting----------------------------------------*/
    	
    	List<ScheduleTest> schedu = ScheduleTest.getUpdateMeeting(user);
    	List<ScheduleTestVM> listData = new ArrayList<>();
    	for(ScheduleTest sch:schedu){
    		ScheduleTestVM vm = new ScheduleTestVM();
    		vm.confirmTime = new SimpleDateFormat("hh:mm a").format(sch.confirmTime);
    		vm.confirmEndTime = new SimpleDateFormat("hh:mm a").format(sch.confirmEndTime);
    		vm.confirmDate = new SimpleDateFormat("MM-dd-yyyy").format(sch.confirmDate);
    		vm.name = sch.name;
    		vm.reason = sch.reason;
    		listData.add(vm);
    		sch.setDeclineUpdate(0);
    		
    		
    		Date schDate=new Date();
			Date schcurDate11=null;
			Date schcurDate1=null;
			DateFormat schDatedf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
			 DateFormat schDatedf11 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
		    Location location1 = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    schDatedf11.setTimeZone(TimeZone.getTimeZone(location1.time_zone));
				String schDate1=schDatedf11.format(schDate);
				String dateNew1=schDatedf11.format(sch.meetingActionTime);
				
				try {
					schcurDate11=schDatedf1.parse(schDate1);
					schcurDate1 =schDatedf1.parse(dateNew1);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				  long schdiff = schcurDate11.getTime() - schcurDate1.getTime();
	  	        long diffSeconds1 = diff / 1000 % 60;
	  	        long diffMinutes1 = diff / (60 * 1000) % 60;
	  	        	long diffHours1 = diff / (60 * 60 * 1000)% 24;
	  	        	int diffInDays1 = (int) ((schcurDate11.getTime() - schcurDate1.getTime()) / (1000 * 60 * 60 * 24));
	    	        String diffDay=null;
	    	        String diffHr=null;
	    	        if(diffInDays1 != 0){
	    	        if(diffInDays1<10){
	    	        	
	    	        	diffDay=""+diffInDays1;
	    	        }
	    	        else{
	    	        	diffDay=""+diffInDays1;
	    	        }
	    	        if(diffHours <10){
	    	        	diffHr="0"+diffHours1;
	    	        }
	    	        else{
	    	        	diffHr=""+diffHours1;
	    	        }
	    	        vm.diffDays=diffDay+" + days";
	    	        }
	    	        else if(diffInDays1 == 0 && diffHours1 == 0){
	    	        	vm.diffDays=diffMinutes1+" minutes ago";;
	        	     
	        	        }
	    	        else{
	    	        	
	    	        	 if(diffHours1 <10){
	    	    	        	diffHr="0"+diffHours1;
	    	    	        }
	    	    	        else{
	    	    	        	diffHr=""+diffHours1;
	    	    	        }
	    	        	 vm.diffDays=diffHr+" hours "+diffMinutes1+" minutes ago";
	    	        }

    		
    		
    		sch.update();
    	}
    	
    	mapList.put("updateMeeting", listData);
    	
    	/*----------------------------------------*/
    	
    	List<RequestInfoVM> actionVM= new ArrayList<RequestInfoVM>();
    	findReminderPopupFunction(actionVM);
    	mapList.put("reminderPopup", actionVM);

    	
    	return ok(Json.toJson(mapList));
		
    	
    }
    
    
    
    public static void findReminderPopupFunction(List<RequestInfoVM> actionVM){
    	
    	SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
      	 DateFormat df1 = new SimpleDateFormat("MM-dd-yyyy HH:mm a");
      	 DateFormat df2 = new SimpleDateFormat("MM-dd-yyyy HH:mm a");
      	 AuthUser user = (AuthUser) getLocalUser();
           DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
           SimpleDateFormat parseTime = new SimpleDateFormat("hh:mm a");
           Date currD = new Date();
           Date currentDate = null;
           Date aftHrDate = null;
           Date aftDay = null;
           Date aftHrDate1 = null;
           Date aftDay1 = null;
           Date infoDate = null;
           Date datec = null;
           
           Date lessDay = DateUtils.addDays(currD, -1);
           
        //   List<NoteVM> actionVM = new ArrayList<NoteVM>();
           
           
           
          // List<ScheduleTest> list = ScheduleTest.findAllByServiceTestPopup(user,lessDay);
           
       	List<RequestMoreInfo> requestMoreInfos = RequestMoreInfo.findByConfirmGraLeadsToPopUp(user,lessDay);
       //	List<TradeIn> tradeIns = TradeIn.findByConfirmGraLeadsToPopup(user,lessDay);
       	
       	//fillLeadsData(list, requestMoreInfos, tradeIns, infoVMList);
       	
       /*	for(ScheduleTest scTest:list){
          	 
       		RequestInfoVM acti = new RequestInfoVM();
          	 AuthUser aUser = AuthUser.findById(scTest.assignedTo.id);
          	 Location location = Location.findById(aUser.location.id);
          	
          	 df2.setTimeZone(TimeZone.getTimeZone(location.time_zone));
               String IST = df2.format(currD);
              
               Date istTimes = null;
   			try {
   				istTimes = df1.parse(IST);
   			} catch (ParseException e1) {
   				// TODO Auto-generated catch block
   				e1.printStackTrace();
   			}
          	
          	 
          	 String cDate = df.format(istTimes);
               String cTime = parseTime.format(istTimes);
               String crD =    df1.format(istTimes);
      		 
               try {
              	 currentDate = df1.parse(crD);
              	 datec = df.parse(cDate);
              	 aftHrDate = DateUtils.addHours(currentDate, 1);
              	 aftDay = DateUtils.addHours(currentDate, 24);
              	 aftHrDate1 = DateUtils.addMinutes(aftHrDate, 15);
              	 aftDay1 = DateUtils.addMinutes(aftDay, 15);
      		} catch (Exception e) {
      			e.printStackTrace();
      		}
          	 
          	 try {
          		 String str = df.format(scTest.confirmDate) +" "+parseTime.format(scTest.confirmTime);
          		 infoDate = df1.parse(str);

          		            	 
          		 if((infoDate.equals(aftHrDate)||infoDate.after(aftHrDate)) && ((infoDate.equals(aftHrDate1)||infoDate.before(aftHrDate1)))){
              		 if(scTest.meetingStatus == null){
              			acti.action = "Test drive reminder";
              			acti.notes = "You have a test drive scheduled in 1 hour ";
          			 }else if(scTest.meetingStatus.equals("meeting")){
          				acti.action = "Meeting reminder";
          				acti.notes = "You have a meeting scheduled in 1 hour ";
          			 }
              		 
              		acti.id = scTest.id;
           		Vehicle vehicle = Vehicle.findByVinAndStatus(scTest.vin);
           		acti.vin = scTest.vin;
           		if(vehicle != null) {
           			acti.model = vehicle.model;
           			acti.make = vehicle.make;
           			acti.stock = vehicle.stock;
           			acti.year = vehicle.year;
           			acti.mileage = vehicle.mileage;
           			acti.price = vehicle.price;
           		}
           		
           		acti.name = scTest.name;
           		acti.phone = scTest.phone;
           		acti.email = scTest.email;
           			
           		acti.howContactedUs = scTest.contactedFrom;
           		acti.howFoundUs = scTest.hearedFrom;
           		acti.custZipCode = scTest.custZipCode;
           		acti.enthicity = scTest.enthicity;
           		acti.status =scTest.leadStatus;
           		
           		acti.typeOfLead = "Schedule Test Drive";
           		findSchedulParentChildAndBro(actionVM, scTest, dfs, acti);
           		
              		 
              		 
              		 actionVM.add(acti);
              	 }
          		 if((infoDate.equals(aftDay)||infoDate.after(aftDay)) && ((infoDate.equals(aftDay1)||infoDate.before(aftDay1)))){
              		 if(scTest.meetingStatus == null){
              			acti.action =  "Test drive reminder";
              			acti.notes = "You have a test drive scheduled in 24 hours ";
          			 }else if(scTest.meetingStatus.equals("meeting")){
          				acti.action = "Meeting reminder";
          				acti.notes =  "You have a meeting scheduled in 24 hours ";
          			 }
              		 
              		 
              		acti.id = scTest.id;
           		Vehicle vehicle1 = Vehicle.findByVinAndStatus(scTest.vin);
           		acti.vin = scTest.vin;
           		if(vehicle1 != null) {
           			acti.model = vehicle1.model;
           			acti.make = vehicle1.make;
           			acti.stock = vehicle1.stock;
           			acti.year = vehicle1.year;
           			acti.mileage = vehicle1.mileage;
           			acti.price = vehicle1.price;
           		}
           		
           		acti.name = scTest.name;
           		acti.phone = scTest.phone;
           		acti.email = scTest.email;
           			
           		acti.howContactedUs = scTest.contactedFrom;
           		acti.howFoundUs = scTest.hearedFrom;
           		acti.custZipCode = scTest.custZipCode;
           		acti.enthicity = scTest.enthicity;
           		acti.status =scTest.leadStatus;
           		
           		acti.typeOfLead = "Schedule Test Drive";
           		findSchedulParentChildAndBro(actionVM, scTest, dfs, acti);
           		
           		Date curDate = null;
            	Date curDate1 = null;
            	Date curDateNew = null;
            	List<RequestInfoVM> rList = new ArrayList<>();
            	Date date=new Date();
        		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        		DateFormat newdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
        		DateFormat format1 = new SimpleDateFormat("HH:mm:a");
        		 DateFormat df11 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
        			Location location1 = Location.findById(Long.valueOf(session("USER_LOCATION")));
        			df2.setTimeZone(TimeZone.getTimeZone(location1.time_zone));
        			df11.setTimeZone(TimeZone.getTimeZone(location1.time_zone));
        			String date1=df2.format(date);
        			String dateNew=df11.format(date);
        			String date11="00:00:AM";
        			
        			try {
        				curDate1=newdf1.parse(dateNew);
        				curDate = formatter.parse(date1);
        				curDateNew=format1.parse(date11);
        			} catch (ParseException e1) {
        				// TODO Auto-generated catch block
        				e1.printStackTrace();
        			}
        			
        			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
        			  long diff = curDate1.getTime() - curDateNew.getTime();
          	        long diffSeconds = diff / 1000 % 60;
          	        long diffMinutes = diff / (60 * 1000) % 60;
          	        	long diffHours = diff / (60 * 60 * 1000)% 24;
          	        	if(diffHours != 0){
          	        		acti.diffDays=diffHours+" hours"+diffMinutes+" minutes ago";
        					}
        					else{
        						acti.diffDays=diffMinutes+" minutes ago";
        					}
           		
           		
           		
              		 actionVM.add(acti);
              	 }
   			} catch (Exception e) {
   				e.printStackTrace();
   			}
          	 
          	
           }
*/           
           for(RequestMoreInfo rInfo:requestMoreInfos){
           	
           	RequestInfoVM acti = new RequestInfoVM();
          	 AuthUser emailUser = AuthUser.findById(rInfo.assignedTo.id);
          	 
          	 Location location = Location.findById(emailUser.location.id);
          	
          	 df2.setTimeZone(TimeZone.getTimeZone(location.time_zone));
               String IST = df2.format(currD);
              
               Date istTimes = null;
   			try {
   				istTimes = df1.parse(IST);
   			} catch (ParseException e1) {
   				// TODO Auto-generated catch block
   				e1.printStackTrace();
   			}
          	
          	 
          	 String cDate = df.format(istTimes);
               String cTime = parseTime.format(istTimes);
               String crD =    df1.format(istTimes);
      		 
               try {
              	 currentDate = df1.parse(crD);
              	 datec = df.parse(cDate);
              	 aftHrDate = DateUtils.addHours(currentDate, 1);
              	 aftDay = DateUtils.addHours(currentDate, 24);
              	 aftHrDate1 = DateUtils.addMinutes(aftHrDate, 15);
              	 aftDay1 = DateUtils.addMinutes(aftDay, 15);
      		} catch (Exception e) {
      			e.printStackTrace();
      		}
          	 
          	 
          	 try {
          		 String str = df.format(rInfo.confirmDate) +" "+parseTime.format(rInfo.confirmTime);
          		 infoDate = df1.parse(str);
          		 if((infoDate.equals(aftHrDate)||infoDate.after(aftHrDate)) && ((infoDate.equals(aftHrDate1)||infoDate.before(aftHrDate1)))){
          			acti.action = "Test drive reminder";
          			acti.notes = "You have a test drive scheduled in 1 hour ";
          			
          			acti.id = rInfo.id;
           		AddCollection vehicle = AddCollection.findByVinAndStat(rInfo.id.toString());
           		acti.vin = rInfo.vin;
           		if(vehicle != null) {
           			
           			acti.title = vehicle.title;
           			acti.description = vehicle.description;
           			acti.fileName = vehicle.fileName;
           			acti.cost = String.valueOf(vehicle.cost);
           			
           		}
           		
           		acti.name = rInfo.name;
           		acti.phone = rInfo.phone;
           		acti.email = rInfo.email;
           			
           		acti.howContactedUs = rInfo.contactedFrom;
           		acti.howFoundUs = rInfo.hearedFrom;
           		acti.custZipCode = rInfo.custZipCode;
           		acti.enthicity = rInfo.enthicity;
           		acti.status =rInfo.leadStatus;
           		
           		//acti.typeOfLead = "Request More Info";
           		LeadType lType = null;
	    		if(rInfo.isContactusType != null){
		    		if(!rInfo.isContactusType.equals("contactUs")){
		    			lType = LeadType.findById(Long.parseLong(rInfo.isContactusType));
		    		}else{
		    			lType = LeadType.findByName(rInfo.isContactusType);
		    		}
		    		acti.typeOfLead = lType.leadName;
		    		findCustomeData(rInfo.id,acti,lType.id);
	    		}else{
	    			acti.typeOfLead = "Request More Info";
	    			findCustomeData(rInfo.id,acti,1L);
	    		}
           		
           		findRequestParentChildAndBro(actionVM, rInfo, dfs, acti);
          		 actionVM.add(acti);
          		 }
          		 if((infoDate.equals(aftDay)||infoDate.after(aftDay)) && ((infoDate.equals(aftDay1)||infoDate.before(aftDay1)))){
          			acti.action =  "Test drive reminder";
          			acti.notes = "You have a test drive scheduled in 24 hours ";
          			
          			
          			acti.id = rInfo.id;
           		AddCollection vehicle = AddCollection.findByVinAndStat(rInfo.id.toString());
           		acti.vin = rInfo.vin;
           		if(vehicle != null) {
           			acti.title = vehicle.title;
           			acti.description = vehicle.description;
           			acti.fileName = vehicle.fileName;
           			acti.cost = String.valueOf(vehicle.cost);
           		}
           		
           		acti.name = rInfo.name;
           		acti.phone = rInfo.phone;
           		acti.email = rInfo.email;
           			
           		acti.howContactedUs = rInfo.contactedFrom;
           		acti.howFoundUs = rInfo.hearedFrom;
           		acti.custZipCode = rInfo.custZipCode;
           		acti.enthicity = rInfo.enthicity;
           		acti.status =rInfo.leadStatus;
           		
           		acti.typeOfLead = "Request More Info";
           		
           		LeadType lType = null;
	    		if(rInfo.isContactusType != null){
		    		if(!rInfo.isContactusType.equals("contactUs")){
		    			lType = LeadType.findById(Long.parseLong(rInfo.isContactusType));
		    		}else{
		    			lType = LeadType.findByName(rInfo.isContactusType);
		    		}
		    		acti.typeOfLead = lType.leadName;
		    		findCustomeData(rInfo.id,acti,lType.id);
	    		}else{
	    			acti.typeOfLead = "Request More Info";
	    			findCustomeData(rInfo.id,acti,1L);
	    		}
           		findRequestParentChildAndBro(actionVM, rInfo, dfs, acti);
           		
           		
           		
           		Date curDate = null;
            	Date curDate1 = null;
            	Date curDateNew = null;
            	List<RequestInfoVM> rList = new ArrayList<>();
            	Date date=new Date();
        		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        		DateFormat newdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
        		DateFormat format1 = new SimpleDateFormat("HH:mm:a");
        		 DateFormat df11 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
        			Location location1 = Location.findById(Long.valueOf(session("USER_LOCATION")));
        			df2.setTimeZone(TimeZone.getTimeZone(location1.time_zone));
        			df11.setTimeZone(TimeZone.getTimeZone(location1.time_zone));
        			String date1=df2.format(date);
        			String dateNew=df11.format(date);
        			String date11="00:00:AM";
        			
        			try {
        				curDate1=newdf1.parse(dateNew);
        				curDate = formatter.parse(date1);
        				curDateNew=format1.parse(date11);
        			} catch (ParseException e1) {
        				// TODO Auto-generated catch block
        				e1.printStackTrace();
        			}
        			
        			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
        			  long diff = curDate1.getTime() - curDateNew.getTime();
          	        long diffSeconds = diff / 1000 % 60;
          	        long diffMinutes = diff / (60 * 1000) % 60;
          	        	long diffHours = diff / (60 * 60 * 1000)% 24;
          	        	if(diffHours != 0){
          	        		acti.diffDays=diffHours+" hours"+diffMinutes+" minutes ago";
        					}
        					else{
        						acti.diffDays=diffMinutes+" minutes ago";
        					}
           		
           		
          		 actionVM.add(acti);
          		 }
   			} catch (Exception e) {
   				e.printStackTrace();
   			}
           }
           
          
    }
    
    public static Result getLeadInfo() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser user = (AuthUser) getLocalUser();
	    	InfoCountVM vm = new InfoCountVM();
	    	List<RequestMoreInfo> rList = null;
	    	if(user.role.equals("Manager")){
	    		rList = RequestMoreInfo.findAllLeads(Long.valueOf(session("USER_LOCATION")));
	    	}else{
	    		rList = RequestMoreInfo.findByLead(Long.valueOf(session("USER_LOCATION")), user);
	    	}
	    	
	    	List<RequestInfoVM> list = new ArrayList<>();
	    	Date curr = new Date();
	    	
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
	    	
	    	
    		Location location = Location.findById(Long.parseLong(session("USER_LOCATION")));
      		 if(user.location != null){
      			 location = Location.findById(user.location.id);
      		 }
      		 DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
      		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
      		df1.setTimeZone(TimeZone.getTimeZone(location.time_zone));	
      		 String dat=df1.format(curr);
      		Date currD=null;
      		 try {
				currD=df2.parse(dat);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    	

	    	for(RequestMoreInfo sc: rList){
	    		RequestInfoVM vm1=new RequestInfoVM();
	    		vm1.name=sc.name;
	    		vm1.email = sc.email;
	    		vm1.phone = sc.phone;
	    		vm1.id=sc.id;
	    		int addleadFlag = 0;
	    		vm1.leadType="Request More Info";
	    		vm1.notifFlag=sc.notifFlag;
	    		vm1.richNotification = sc.richNotification;
	    		vm1.isContactusType = sc.isContactusType;
	    		String imagePath=null;
	    		String typeoflead=null;
	    		if(sc.productId != null || sc.isContactusType == null){
	    			if(sc.productId != null){
	    				Long pId = Long.parseLong(sc.productId);
		    			AddCollection image=AddCollection.getDefaultImg(pId);
		    			if(image != null){
		    				imagePath=image.filePath;
		    				if(cuSettings != null){
				    			for(CustomerRequestManufacturerSettings cSettings:cuSettings){
					    			if(cSettings.manufacturer.id == image.id){
					    				addleadFlag = 1;
					    			}
					    		}
			        		}
		    			}
		    			
		    			
	    			}
	    			
	    			typeoflead="Request More";
	    			vm1.typeOfLead="Request More Info";
	    		}
	    		if(sList != null){
	    			for(SalesPersonZipCode sZipCode:sList){
		    			if(sZipCode.zipCode.equals(sc.custZipCode)){
		    				addleadFlag = 1;
		    			}
		    		}
	    		}
	    		
	    		else if(sc.isContactusType.equals("contactUs")){
	    			imagePath="../../../assets/global/images/leadsImages/rmail.png" ;
	    			typeoflead="Contact Us";
	    			vm1.typeOfLead="Contact Us";
	    		}
	    		vm1.typeOfLead=typeoflead;
	    		vm1.imageUrl=imageUrlPath+imagePath;
    	        Date dt1=null;
    	        Date dt2=null;
    	        try {
    	        	dt1 =currD;
    	           // dt2 = sc.requestTime;
    	            String dat1=df1.format(sc.requestTime);
    	            dt2=df2.parse(dat1);
    	        } catch (Exception e) {
    	            e.printStackTrace();
    	        }

    	        // Get msec from each, and subtract.
    	        long diff = dt1.getTime() - dt2.getTime();
    	        vm1.timeDiff=diff;
    	        long diffSeconds = diff / 1000 % 60;
    	        long diffMinutes = diff / (60 * 1000) % 60;
    	        long diffHours = diff / (60 * 60 * 1000)% 24;
    	        int diffInh = (int) ((dt1.getTime() - dt2.getTime()) / (1000 * 60 * 60 ));
    	        int diffInDays = (int) ((dt1.getTime() - dt2.getTime()) / (1000 * 60 * 60 * 24));
    	        String diffDay=null;
    	        String diffHr=null;
    	        if(diffInDays != 0){
    	        if(diffInDays <10){
    	        	diffDay=""+diffInDays;
    	        }
    	        else{
    	        	diffDay=""+diffInDays;
    	        }
    	        if(diffHours <10){
    	        	diffHr=""+diffHours;
    	        }
    	        else{
    	        	diffHr=""+diffHours;
    	        }
    	        vm1.timeUnit=diffDay+" days "+diffHr+" hours "+diffMinutes+" minutes ago";
    	        vm1.diffDays=diffDay+" + days";
    	        }
    	        else if(diffInDays == 0 && diffHours == 0){
    	        	if(diffMinutes == 1){
    	        		vm1.diffDays=diffMinutes+" minute ago";
            	        vm1.timeUnit=diffMinutes+" minute ago";
    	        	}else{
    	        	vm1.diffDays=diffMinutes+" minutes ago";
        	        vm1.timeUnit=diffMinutes+" minutes ago";
    	        	}
        	     
        	        }
    	        else{
    	        	
    	        	 if(diffHours <10){
    	    	        	diffHr="0"+diffHours;
    	    	        }
    	    	        else{
    	    	        	diffHr=""+diffHours;
    	    	        }
    	        	vm1.timeUnit=diffHr+" hours "+diffMinutes+" minutes ago";
    	        	vm1.diffDays=diffHr+" hours "+diffMinutes+" minutes ago";
    	        }
	    		
    	        if(addleadFlag == 1 || addleadFlagAll == 1){
    	        	list.add(vm1);	
	    		}else if(cRequest == null){
	    			list.add(vm1);	
	    		}
	    			
	    		
	    	}
	    	
	    	
	    	  /*  	List<RequestMoreInfo> reInfos = RequestMoreInfo.findAllLocationDataManagerPremium(Long.valueOf(session("USER_LOCATION")));
	
	    	for(RequestMoreInfo sc: reInfos){
	    		RequestInfoVM vm1=new RequestInfoVM();
	    		vm1.name=sc.name;
	    		vm1.typeOfLead="Request More";
	    		vm1.leadTypeForNotif="Request More Info";
	    		vm1.id=sc.id;
	    		vm1.notifFlag=sc.notifFlag;
	    		vm1.leadType="Request More Info";
	    		vm1.richNotification = sc.richNotification;
	    		vm1.isContactusType = sc.isContactusType;
	    		String imagePath=null;
	    		if(sc.productId != null){
	    			if(!sc.productId.equals("null")){
	    				Long pId = Long.parseLong(sc.productId);
		    			AddCollection image=AddCollection.getDefaultImg(pId);
		    			if(image != null){
		    				imagePath=image.filePath;
		    			}
	    			}
	    		}
	    		vm1.imageUrl=imageUrlPath+imagePath;
	    		DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:a");
    	        Date dt1=null;
    	        Date dt2=null;
    	        try {
    	        	dt1 =currD;
    	        	 String dat1=df1.format(sc.requestTime);
     	            dt2=df2.parse(dat1);
    	        } catch (Exception e) {
    	            e.printStackTrace();
    	        }

    	        long diff = dt1.getTime() - dt2.getTime();
    	        vm1.timeDiff=diff;
    	        long diffSeconds = diff / 1000 % 60;
    	        long diffMinutes = diff / (60 * 1000) % 60;
    	        long diffHours = diff / (60 * 60 * 1000)% 24;
    	        int diffInh = (int) ((dt1.getTime() - dt2.getTime()) / (1000 * 60 * 60 ));
    	        int diffInDays = (int) ((dt1.getTime() - dt2.getTime()) / (1000 * 60 * 60 * 24));
    	        String diffDay=null;
    	        String diffHr=null;
    	        if(diffInDays != 0){
    	        if(diffInDays <10){
    	        	diffDay=""+diffInDays;
    	        }
    	        else{
    	        	diffDay=""+diffInDays;
    	        }
    	        if(diffHours <10){
    	        	diffHr=""+diffHours;
    	        }
    	        else{
    	        	diffHr=""+diffHours;
    	        }
    	        vm1.timeUnit=diffDay+" days "+diffHr+" hours "+diffMinutes+" minutes ago";
    	        vm1.diffDays=diffDay+" + days";
    	        }
    	        else if(diffInDays == 0 && diffHours == 0){
    	        	if(diffMinutes == 1){
    	        		vm1.diffDays=diffMinutes+" minute ago";
            	        vm1.timeUnit=diffMinutes+" minute ago";
    	        	}else{
    	        	vm1.diffDays=diffMinutes+" minutes ago";
        	        vm1.timeUnit=diffMinutes+" minutes ago";
    	        	}
    	     
    	        }
    	        else{
    	        	
    	        	 if(diffHours <10){
    	    	        	diffHr="0"+diffHours;
    	    	        }
    	    	        else{
    	    	        	diffHr=""+diffHours;
    	    	        }
    	        	vm1.timeUnit=diffHr+" hours "+diffMinutes+" minutes ago";
    	        	vm1.diffDays=diffHr+" hours "+diffMinutes+" minutes ago";
    	        }
	    		
	    		list.add(vm1);		
	    		
	    	}*/
	    	
	    	vm.userType = user.role;
	    	
	    	return ok(Json.toJson(list));
    	}
    }

    
    public static Result getImageById(Long id, String type) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	File file = null;
	    	InventoryImage image = InventoryImage.findById(id);
	    	if(type.equals("thumbnail")) {
		    	file = new File(rootDir+image.thumbPath);
	    	}
	    	
	    	if(type.equals("full")) {
	    		file = new File(rootDir+image.path);
	    	}
	    	return ok(file);
    	}	
    }
    
    public static Result editLeads(){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser user = (AuthUser) getLocalUser();
    		Date currDate = new Date();
    		MultipartFormData bodys = request().body().asMultipartFormData();
    		
    		LeadVM vm = null;
        	
        	Form<LeadVM> form = DynamicForm.form(LeadVM.class).bindFromRequest();
        	if(bodys != null){
        		LeadVM leadVM1 = new LeadVM();
        		saveBilndVm(leadVM1,bodys,form);
        		vm = leadVM1;
        	}else{
        		vm = form.get();
        	}
    		
    		
	    	
	    	
	    	
	    	
	    	int parentFlag = 0;
	    	Long parentLeadId = 0L;
	    	Date reqDate = null;
	    			
		    	 
		    		RequestMoreInfo rInfo = RequestMoreInfo.findById(Long.parseLong(vm.id));
		    		if(rInfo != null){
		    			rInfo.setProductId(vm.productId);
		    			rInfo.setName(vm.custName);
		    			rInfo.setEmail(vm.custEmail);
		    			rInfo.setPhone(vm.custNumber);
		    			rInfo.setCustZipCode(vm.custZipCode);
		    			rInfo.update();
		    			if(rInfo.isContactusType != null){
			    			if(!rInfo.isContactusType.equals("contactUs")){
			    				saveCustomData(rInfo.id,vm.customData,bodys, Long.parseLong(rInfo.isContactusType));
			    			}
		    			}
		    			reqDate = rInfo.requestDate;
		    			
			    		if(parentFlag == 0){
			    			parentFlag = 1;
			    			parentLeadId = rInfo.getId();
			    		}
		    			
		    			UserNotes uNotes = new UserNotes();
		        		uNotes.setNote("Client interested in another vehicle");
		        		uNotes.setAction("Other");
		        		uNotes.createdDate = currDate;
		        		uNotes.createdTime = currDate;
		        		uNotes.user = user;
		        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		        		uNotes.requestMoreInfo = RequestMoreInfo.findById(rInfo.id);
		        		uNotes.save();
		    		}
		    	
	    	
	    	return ok();
    	}	
    }
    
    public static Result   saveMakePosition() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	JsonNode nodes = ctx().request().body().asJson();
	    	ObjectMapper mapper = new ObjectMapper();
	    	try {
	    		List<VehicleHeader> images = mapper.readValue(nodes.toString(), new TypeReference<List<VehicleHeader>>() {});
				
		    	for(VehicleHeader image : images) {
		    		image.update();
		    	}
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	
	    	return ok();
    	}	
    }
   
	public static Result getAllVehiclesByType(String type) {
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		int visitorCount = 0;
    		List <Vehicle> vehicleObjList;
	    	/*List <Vehicle> vehicleObjList = Vehicle.getVehiclesByStatus("Newly Arrived");*/
    		if(type.equalsIgnoreCase("All"))
    			vehicleObjList = Vehicle.findByNewArrAndLocation(Long.valueOf(session("USER_LOCATION")));
    		else{
    			vehicleObjList = Vehicle.findByNewArrAndLocationType(Long.valueOf(session("USER_LOCATION")), type);
    		}
    		
	    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	    	ArrayList<SpecificationVM> NewVMs = new ArrayList<>();
	    	String params = "&date=last-28-days&type=visitors-list&limit=all";
	     	for(Vehicle vm : vehicleObjList){
	     		
	     		InventoryImage vehicleImg = InventoryImage.getDefaultImage(vm.vin);
	     		
	     		SpecificationVM vehicle = new SpecificationVM();
	     		vehicle.id = vm.id;
	     	    vehicle.title=vm.getTitle();
		    	vehicle.category = vm.category;
		    	vehicle.vin = vm.vin;
		    	vehicle.year = vm.year;
		    	vehicle.make = vm.make;
		    	vehicle.model = vm.model;
		    	vehicle.trim_level = vm.trim;
		    	vehicle.label = vm.label;
		    	vehicle.stock = vm.stock;
		    	vehicle.mileage = vm.mileage;
		    	vehicle.cost = vm.cost;
		    	vehicle.price = vm.price;
		    	vehicle.extColor = vm.exteriorColor;
		    	vehicle.intColor = vm.interiorColor;
		    	vehicle.colorDesc = vm.colorDescription;
		    	vehicle.doors = vm.doors;
		    	vehicle.stereo = vm.stereo;
		    	vehicle.engine = vm.engine;
		    	vehicle.fuel = vm.fuel;
		    	vehicle.city_mileage = vm.cityMileage;
		    	vehicle.highway_mileage = vm.highwayMileage;
		    	vehicle.bodyStyle = vm.bodyStyle;
		    	vehicle.drivetrain = vm.drivetrain;
		    	vehicle.transmission = vm.transmission;
		    	vehicle.location = vm.location;
		    	vehicle.status  =  vm.status;
		    	if(vehicleImg != null){
		    		vehicle.imagePath = vehicleImg.thumbPath;
		    		vehicle.imgId = vehicleImg.id;
		    	}	
		    	
		    	vehicle.sold = false;
		    	visitorCount = 0;
		    	
        		try {
    				JSONArray jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
    				for(int j=0;j<jsonArray.length();j++){
    	    			String data = jsonArray.getJSONObject(j).get("landing_page").toString();
    	    			String arr[] = data.split("/");
    	    			if(arr.length > 5){
    	    			  if(arr[5] != null){
    	    				  if(arr[5].equals(vm.vin)){
    	    					  visitorCount = visitorCount + 1;
    	    				  }
    	    			  }
    	    			}
    				}	
    				
    			} catch (JSONException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
		    	
        		vehicle.pageViewCount = visitorCount;
		    	
		    	List<SqlRow> rows = Vehicle.getDriveTimeAndName(vehicle.vin);
		    	for(SqlRow row : rows) {
		    		Date date = (Date) row.get("confirm_date");
		    		Date timeObj = (Date) row.get("confirm_time");
		    		vehicle.testDrive = df.format(date) +" ";
		    		Calendar time = Calendar.getInstance();
		    		if(timeObj != null){
		    			time.setTime(timeObj);
		    		}
		    		
	    			String ampm = "";
	    			if(time.get(Calendar.AM_PM) == Calendar.PM) {
	    				ampm = "PM";
	    			} else {
	    				ampm = "AM";
	    			}
	    			vehicle.testDrive = vehicle.testDrive + time.get(Calendar.HOUR) + ":" + time.get(Calendar.MINUTE) + " " + ampm;
		    		Integer userId = (Integer) row.get("assigned_to_id");
		    		AuthUser userData = AuthUser.findById(userId);
		    		vehicle.salesRep = userData.firstName +" "+userData.lastName;
		    		break;
		    	}
		    	vehicle.vehicleCnt = InventoryImage.getVehicleImageCountByVIN(vm.vin);
		    	NewVMs.add(vehicle);
	    	}
	     	
	     	
	     	
	     	
	     	
	    	return ok(Json.toJson(NewVMs));
    	}	
    } 
    public static Result addPublicCar(Long id){
    	
    	AddCollection vehicle= AddCollection.findById(id);
    	if(vehicle != null){
    		vehicle.setPublicStatus("public");
        	vehicle.update();
    	}
    	return ok();
    }
    
    public static Result isCarPublic(Long id){
    	
    	Vehicle vehicle= Vehicle.findById(id);
    	if(vehicle != null && vehicle.getPublicStatus().equals("public")){
    		return ok(Json.toJson(false));
    	}else{
    		return ok(Json.toJson(true));    		
    	}
    }
	public static Result getAllInventory() {
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		
    		List <Inventory> productObjList = Inventory.findProductsNotSale(Long.valueOf(session("USER_LOCATION")));
    		
	    	ArrayList<InventoryVM> NewVMs = new ArrayList<>();
	     	for(Inventory vm : productObjList){
	     		
	     		InventoryImage productImg = InventoryImage.getDefaultImage(vm.productId);
	     		
	     		InventoryVM product = new InventoryVM();
	     		product.id = vm.id;
	     		product.title=vm.getTitle();
	     		product.description = vm.description;
	     		product.cost = String.valueOf(vm.cost);
	     		product.price = String.valueOf(vm.price);
	     		product.collection = vm.collection.id;
	     		AddCollection aCollection = AddCollection.findById(vm.collection.id);
	     		product.collectionName = aCollection.title;
		   
		    	if(productImg != null){
		    		product.imagePath = productImg.thumbPath;
		    		product.imgId = productImg.id.toString();
		    	}	
		    
		    	NewVMs.add(product);
	    	}
	     	
	    	return ok(Json.toJson(NewVMs));
    	}	
    }
	
	public static Result getGoTodraft(Long id){
		
		AddCollection coll= AddCollection.findById(id);
		List<AddCollection> list = AddCollection.getProductByParentId(id);
		if(coll != null){
			if(coll.publicStatus.equals("deleted")){
				coll.setPublicStatus("draft");
			}else{
				coll.setPublicStatus("publish");
			}
			coll.update();
		}
		/*for (AddCollection ap : list) {
			if(coll.publicStatus.equals("publish")){
				ap.setPublicStatus("draft");
			}else{
				ap.setPublicStatus("publish");
			}
			ap.update();
		}*/
		return ok();
		
	}
	
  public static Result getGoTodraftProduct(Long id){
		
	    Product coll= Product.findById(id);
		//List<Product> list = Product.getProductByParentId(id);
		if(coll != null){
			if(coll.publicStatus.equals("deleted")){
				coll.setPublicStatus("draft");
			}else{
				coll.setPublicStatus("publish");
			}
			coll.update();
		}
		/*for (AddCollection ap : list) {
			if(coll.publicStatus.equals("publish")){
				ap.setPublicStatus("publish");
			}else{
				ap.setPublicStatus("draft");
			}
			ap.update();
		}*/
		return ok();
		
	}
	
	
    
	public static Result getVehicleHistory(String vin){
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	    	SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
    		List<UserNoteVM> userNote = new ArrayList<>();
    		List<RequestMoreInfo> infoList = RequestMoreInfo.findByVin(vin);
    		List<ScheduleTest>  scheduleList = ScheduleTest.findByVin(vin);
    		for (ScheduleTest scheduleTest : scheduleList) {
    			List<UserNotes> notes = UserNotes.findScheduleTest(scheduleTest);
				for (UserNotes note : notes) {
					UserNoteVM vm = new UserNoteVM();
					vm.id = note.id;
					vm.action = note.action;
					vm.note = note.note;
					vm.createdDate = df.format(note.createdDate);
					vm.createdTime = time.format(note.createdTime);
					userNote.add(vm);
				}
			}
    		List<TradeIn> traid = TradeIn.findByVin(vin);
    		for (TradeIn tradeIn : traid) {
    			List<UserNotes> notes = UserNotes.findTradeIn(tradeIn);
				for (UserNotes note : notes) {
					UserNoteVM vm = new UserNoteVM();
					vm.id = note.id;
					vm.action = note.action;
					vm.note = note.note;
					vm.createdDate = df.format(note.createdDate);
					vm.createdTime = time.format(note.createdTime);
					userNote.add(vm);
				}
			}
    		for (RequestMoreInfo info : infoList) {
				List<UserNotes> notes = UserNotes.findRequestMore(info);
				for (UserNotes note : notes) {
					UserNoteVM vm = new UserNoteVM();
					vm.id = note.id;
					vm.action = note.action;
					vm.note = note.note;
					vm.createdDate = df.format(note.createdDate);
					vm.createdTime = time.format(note.createdTime);
					userNote.add(vm);
				}
			}
    		return ok(Json.toJson(userNote));
    	}
	}
    
	public static Result getPriceHistory(String vin){
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	    	SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
    		List<PriceChangeVM> priceList = new ArrayList<>();
    		List<PriceChange> list = PriceChange.findByVin(vin);
    		for (PriceChange obj : list) {
				PriceChangeVM vm =new PriceChangeVM();
				vm.dateTime = obj.dateTime.toString();
				vm.person = obj.person;
				vm.price = obj.price;
				priceList.add(vm);
			}
    		return ok(Json.toJson(list));
    	}
	}
	
	
	
	
	public static Result getAllSoldVehiclesByType(String type) {
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	List <Vehicle> soldVehicleObjList;
	    	if(type.equalsIgnoreCase("All"))
	    		soldVehicleObjList = Vehicle.getVehiclesByStatusAndLocation("Sold",Long.valueOf(session("USER_LOCATION")));
	    	else{
	    		soldVehicleObjList = Vehicle.getVehiclesByStatusAndLocationType("Sold",Long.valueOf(session("USER_LOCATION")),type);
	    	}
	    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	    	ArrayList<SpecificationVM> soldVMs = new ArrayList<>(); 
	     	for(Vehicle vm : soldVehicleObjList){
	     		InventoryImage vehicleImg = InventoryImage.getDefaultImage(vm.vin);
	     		SpecificationVM vehicle = new SpecificationVM();
	     		vehicle.id = vm.id;
		    	vehicle.category = vm.category;
		    	vehicle.vin = vm.vin;
		    	vehicle.year = vm.year;
		    	//vehicle.make = vm.make+" "+vm.model;
		    	vehicle.make = vm.make;
		    	vehicle.model = vm.model;
		    	vehicle.trim_level = vm.trim;
		    	vehicle.label = vm.label;
		    	vehicle.stock = vm.stock;
		    	vehicle.mileage = vm.mileage;
		    	vehicle.cost = vm.cost;
		    	vehicle.price = vm.price;
		    	vehicle.extColor = vm.exteriorColor;
		    	vehicle.intColor = vm.interiorColor;
		    	vehicle.colorDesc = vm.colorDescription;
		    	vehicle.doors = vm.doors;
		    	vehicle.stereo = vm.stereo;
		    	vehicle.engine = vm.engine;
		    	vehicle.fuel = vm.fuel;
		    	vehicle.city_mileage = vm.cityMileage;
		    	vehicle.highway_mileage = vm.highwayMileage;
		    	vehicle.bodyStyle = vm.bodyStyle;
		    	vehicle.drivetrain = vm.drivetrain;
		    	vehicle.transmission = vm.transmission;
		    	vehicle.location = vm.location;
		    	vehicle.status  =  vm.status;
		    	vehicle.vehicleCnt = InventoryImage.getVehicleImageCountByVIN(vm.vin);
		    	vehicle.sold = true;
		    	vehicle.imagePath = vehicleImg.thumbPath;
		    	vehicle.imgId = vehicleImg.id;
		    	vehicle.testDrive = df.format(vm.soldDate);
		    	vehicle.title = vm.title;
		    	soldVMs.add(vehicle);
	    	}
	     	
	     	return ok(Json.toJson(soldVMs));
    	}	
    }
	
	public static Result getAllDraftVehicles(){
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	List <AddCollection> draftVehicleObjList = AddCollection.getProductByDraftStatusAndLocation(Long.valueOf(session("USER_LOCATION")));
	    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	    	ArrayList<SpecificationVM> draftVMs = new ArrayList<>(); 
	     	for(AddCollection vm : draftVehicleObjList){
	     	//	InventoryImage vehicleImg = InventoryImage.getDefaultImage(vm.vin);
	     		SpecificationVM vehicle = new SpecificationVM();
	     		vehicle.id = vm.id;
		    	//vehicle.category = vm.category;
		    	//vehicle.vin = vm.vin;
		    	vehicle.year = vm.year;
		    	vehicle.title =vm.title;
		    	//vehicle.make = vm.make+" "+vm.model;
		    	//vehicle.make = vm.make;
		    	//vehicle.model = vm.model;
		    	//vehicle.trim_level = vm.trim;
		    	//vehicle.label = vm.label;
		    //	vehicle.stock = vm.stock;
		    	//vehicle.mileage = vm.mileage;
		    	//vehicle.cost = vm.cost;
		    //	vehicle.price = vm.price;
		    	//vehicle.extColor = vm.exteriorColor;
		    //	vehicle.intColor = vm.interiorColor;
		    	/*vehicle.colorDesc = vm.colorDescription;
		    	vehicle.doors = vm.doors;
		    	vehicle.stereo = vm.stereo;
		    	vehicle.engine = vm.engine;
		    	vehicle.fuel = vm.fuel;*/
		    /*	vehicle.city_mileage = vm.cityMileage;
		    	vehicle.highway_mileage = vm.highwayMileage;
		    	vehicle.bodyStyle = vm.bodyStyle;
		    	vehicle.drivetrain = vm.drivetrain;
		    	vehicle.transmission = vm.transmission;
		    	vehicle.location = vm.location;
		    	vehicle.status  =  vm.status;*/
		    	/*vehicle.vehicleCnt = InventoryImage.getVehicleImageCountByVIN(vm.vin);
		    	vehicle.sold = true;
		    	if(vehicleImg != null){
		    		vehicle.imagePath = vehicleImg.thumbPath;
		    		vehicle.imgId = vehicleImg.id;
		    	}*/
		    	vehicle.sold = false;
		    	 vehicle.title=vm.getTitle();
		    	 draftVMs.add(vehicle);
	    	}
	     	
	     	return ok(Json.toJson(draftVMs));
    	}	
	}
	
	public static Result getAllSoldVehicles() {
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	List <Vehicle> soldVehicleObjList = Vehicle.getVehiclesByStatusAndLocation("Sold",Long.valueOf(session("USER_LOCATION")));
	    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	    	ArrayList<SpecificationVM> soldVMs = new ArrayList<>(); 
	     	for(Vehicle vm : soldVehicleObjList){
	     		InventoryImage vehicleImg = InventoryImage.getDefaultImage(vm.vin);
	     		SpecificationVM vehicle = new SpecificationVM();
	     		vehicle.id = vm.id;
		    	vehicle.category = vm.category;
		    	vehicle.vin = vm.vin;
		    	vehicle.year = vm.year;
		    	//vehicle.make = vm.make+" "+vm.model;
		    	vehicle.make = vm.make;
		    	vehicle.model = vm.model;
		    	vehicle.trim_level = vm.trim;
		    	vehicle.label = vm.label;
		    	vehicle.stock = vm.stock;
		    	vehicle.mileage = vm.mileage;
		    	vehicle.cost = vm.cost;
		    	vehicle.price = vm.price;
		    	vehicle.extColor = vm.exteriorColor;
		    	vehicle.intColor = vm.interiorColor;
		    	vehicle.colorDesc = vm.colorDescription;
		    	vehicle.doors = vm.doors;
		    	vehicle.stereo = vm.stereo;
		    	vehicle.engine = vm.engine;
		    	vehicle.fuel = vm.fuel;
		    	vehicle.city_mileage = vm.cityMileage;
		    	vehicle.highway_mileage = vm.highwayMileage;
		    	vehicle.bodyStyle = vm.bodyStyle;
		    	vehicle.drivetrain = vm.drivetrain;
		    	vehicle.transmission = vm.transmission;
		    	vehicle.location = vm.location;
		    	vehicle.status  =  vm.status;
		    	vehicle.vehicleCnt = InventoryImage.getVehicleImageCountByVIN(vm.vin);
		    	vehicle.sold = true;
		    	if(vehicleImg != null){
		    		vehicle.imagePath = vehicleImg.thumbPath;
			    	vehicle.imgId = vehicleImg.id;
		    	}
		    	
		    	vehicle.testDrive = df.format(vm.soldDate);
		    	vehicle.title = vm.title;
		    	soldVMs.add(vehicle);
	    	}
	     	
	     	return ok(Json.toJson(soldVMs));
    	}	
    }
	
	 public static Result deleteProductRowPer(Long id ){
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		try{
	    			/*List<CustomerRequestManufacturerSettings> listcust = CustomerRequestManufacturerSettings.getcustManufact(id);
	    			if(listcust.size() != 0){
		    			for (CustomerRequestManufacturerSettings lis : listcust) {
		    	    		lis.delete();
		    	    	}
	    			}*/
	    			List<ProductImages> pImages = ProductImages.getDeleteImagePath(id);
	        		if(pImages.size() != 0){
	        			for(ProductImages pI:pImages){
	        				pI.delete();
	        			}
	        		}
	    	    	Product vm = Product.findById(id);
	    	    	AuthUser user = (AuthUser) getLocalUser();
	    	    	if(vm != null){
	    	    		
	    	    		vm.delete();
	    	    		File file = new File(rootDir+File.separator+vm.id+"-"+user.id);
	    	    		file.delete();
	    	    	}
	    	    	return ok();
	    		}catch(Exception e){
	    			return ok("error");
	    		}
	    		
	    	}	
	    }
	
	
	 public static Result deleteVehicleByIdPer(Long id ){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		List<AddCollection> list = AddCollection.getProductByParentId(id);
    		if(list.size() == 0){
    			List<CustomerRequestManufacturerSettings> listcust = CustomerRequestManufacturerSettings.getcustManufact(id);
    			if(listcust.size() != 0){
	    			for (CustomerRequestManufacturerSettings lis : listcust) {
	    	    		lis.delete();
	    	    	}
    			}
    			List<CollectionImages> pImages = CollectionImages.getDeleteImagePath(id);
        		if(pImages.size() != 0){
        			for(CollectionImages pI:pImages){
        				pI.delete();
        			}
        		}
    	    	AddCollection vm = AddCollection.findById(id);
    	    	AuthUser user = (AuthUser) getLocalUser();
    	    	if(vm != null){
    	    		
    	    		vm.delete();
    	    		File file = new File(rootDir+File.separator+vm.id+"-"+user.id);
    	    		file.delete();
    	    	}
    	    	return ok();
    		}else if(list.size() != 0){
    			for (AddCollection ap : list) {
        			ap.setParentId(null);
        	    	ap.update();
    			}
    			List<CustomerRequestManufacturerSettings> listcust = CustomerRequestManufacturerSettings.getcustManufact(id);
    			if(listcust.size() != 0){
	    			for (CustomerRequestManufacturerSettings lis : listcust) {
	    	    		lis.delete();
	    	    	}
    			}
    			List<CollectionImages> pImages = CollectionImages.getDeleteImagePath(id);
        		if(pImages.size() != 0){
        			for(CollectionImages pI:pImages){
        				pI.delete();
        			}
        		}
    			AddCollection vm = AddCollection.findById(id);
    			vm.delete();
    			return ok();
    		}
    		else{
    			return ok("Error");
    		}
    	}	
    }
	 
	     
    public static Result deleteVehicleById(Long id ){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		List<AddCollection> list = AddCollection.getProductByParentId(id);
    		for (AddCollection ap : list) {
    			ap.setPublicStatus("deleted");
    	    	ap.update();
			}
	    	AddCollection vm = AddCollection.findById(id);
	    	vm.setPublicStatus("deleted");
	    	if(vm.parentId != null){
	    		List<Product> product = Product.getAllProductById(id);
	    		for(Product p:product){
	    			p.setPublicStatus("deleted");
	    			p.update();
	    		}
	    	}
	    	vm.update();
	    	return ok();
    	}	
    }
    
    public static Result updateVehicleStatus(Long id,String status){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	Vehicle vm = Vehicle.findById(id);
	    	Date currDate = new Date();
	    	if(vm != null){
	    		vm.setStatus(status);
	    		Date date = new Date();
	    		vm.setSoldDate(date);
	    		vm.update();
	    		
	    		if(status.equals("Sold")){
	    			List<TradeIn> tIn = TradeIn.findByVinAndLocation(vm.vin, Location.findById(Long.parseLong(session("USER_LOCATION"))));
	        		for(TradeIn tradeIn:tIn){
	        			if(tradeIn.status == null){
	        				tradeIn.setStatus("LOST");
	        				tradeIn.setStatusDate(currDate);
	        				tradeIn.setStatusTime(currDate);
	        				tradeIn.update();
	        				
	        				UserNotes uNotes1 = new UserNotes();
	        	    		uNotes1.setNote("Vehicle has been sold by another Sales Person");
	        	    		uNotes1.setAction("Other");
	        	    		uNotes1.createdDate = currDate;
	        	    		uNotes1.createdTime = currDate;
	        	    		//uNotes1.user = user;
	        	    		uNotes1.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	        	    		uNotes1.tradeIn = TradeIn.findById(tradeIn.id);
	        	    		uNotes1.save();
	        			}else if(!tradeIn.status.equals("COMPLETE")){
	        				tradeIn.setStatus("LOST");
	        				tradeIn.setStatusDate(currDate);
	        				tradeIn.setStatusTime(currDate);
	        				tradeIn.update();
	        				
	        				UserNotes uNotes1 = new UserNotes();
	        	    		uNotes1.setNote("Vehicle has been sold by another Sales Person");
	        	    		uNotes1.setAction("Other");
	        	    		uNotes1.createdDate = currDate;
	        	    		uNotes1.createdTime = currDate;
	        	    		//uNotes1.user = user;
	        	    		uNotes1.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	        	    		uNotes1.tradeIn = TradeIn.findById(tradeIn.id);
	        	    		uNotes1.save();
	        			}
	        		}
	        		
	        		List<RequestMoreInfo> rInfos = RequestMoreInfo.findByVinAndLocation(vm.vin, Location.findById(Long.parseLong(session("USER_LOCATION"))));
	        		for(RequestMoreInfo rMoreInfo:rInfos){
	        			if(rMoreInfo.status == null){
	        				rMoreInfo.setStatus("LOST");
	        				rMoreInfo.setStatusDate(currDate);
	        				rMoreInfo.setStatusTime(currDate);
	        				rMoreInfo.update();
	        				
	        				UserNotes uNotes1 = new UserNotes();
	        	    		uNotes1.setNote("Vehicle has been sold by another Sales Person");
	        	    		uNotes1.setAction("Other");
	        	    		uNotes1.createdDate = currDate;
	        	    		uNotes1.createdTime = currDate;
	        	    		//uNotes1.user = user;
	        	    		uNotes1.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	        	    		uNotes1.tradeIn = TradeIn.findById(rMoreInfo.id);
	        	    		uNotes1.save();
	        			}else if(!rMoreInfo.status.equals("COMPLETE")){
	        				rMoreInfo.setStatus("LOST");
	        				rMoreInfo.setStatusDate(currDate);
	        				rMoreInfo.setStatusTime(currDate);
	        				rMoreInfo.update();
	        				
	        				UserNotes uNotes1 = new UserNotes();
	        	    		uNotes1.setNote("Vehicle has been sold by another Sales Person");
	        	    		uNotes1.setAction("Other");
	        	    		uNotes1.createdDate = currDate;
	        	    		uNotes1.createdTime = currDate;
	        	    		//uNotes1.user = user;
	        	    		uNotes1.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	        	    		uNotes1.tradeIn = TradeIn.findById(rMoreInfo.id);
	        	    		uNotes1.save();
	        			}
	        		}
	        		
	        		
	        		List<ScheduleTest> sTests = ScheduleTest.findByVinAndLocation(vm.vin, Location.findById(Long.parseLong(session("USER_LOCATION"))));
	        		for(ScheduleTest scheduleTest:sTests){
	        			if(scheduleTest.leadStatus == null){
	        				scheduleTest.setLeadStatus("LOST");
	        				scheduleTest.setStatusDate(currDate);
	        				scheduleTest.setStatusTime(currDate);
	        				scheduleTest.update();
	        				
	        				UserNotes uNotes1 = new UserNotes();
	        	    		uNotes1.setNote("Vehicle has been sold by another Sales Person");
	        	    		uNotes1.setAction("Other");
	        	    		uNotes1.createdDate = currDate;
	        	    		uNotes1.createdTime = currDate;
	        	    		//uNotes1.user = user;
	        	    		uNotes1.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	        	    		uNotes1.tradeIn = TradeIn.findById(scheduleTest.id);
	        	    		uNotes1.save();
	        			}else if(!scheduleTest.leadStatus.equals("COMPLETE")){
	        				scheduleTest.setLeadStatus("LOST");
	        				scheduleTest.setStatusDate(currDate);
	        				scheduleTest.setStatusTime(currDate);
	        				scheduleTest.update();
	        				
	        				UserNotes uNotes1 = new UserNotes();
	        	    		uNotes1.setNote("Vehicle has been sold by another Sales Person");
	        	    		uNotes1.setAction("Other");
	        	    		uNotes1.createdDate = currDate;
	        	    		uNotes1.createdTime = currDate;
	        	    		//uNotes1.user = user;
	        	    		uNotes1.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	        	    		uNotes1.tradeIn = TradeIn.findById(scheduleTest.id);
	        	    		uNotes1.save();
	        			}
	        		}
	    		}
	    	}
	    	
	    	return ok();
    	}	
    }
    
    public static Result removeVehiclePdf(Long id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		Vehicle vehicle = Vehicle.findById(id);
    		if(vehicle.getPdfBrochurePath() != null){
    			String filePath = rootDir+File.separator+vehicle.getPdfBrochurePath();
        		File file = new File(filePath);
        		try {
        			file.delete();
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
        		vehicle.setPdfBrochureName(null);
        		vehicle.setPdfBrochurePath(null);
        		vehicle.update();
    		}
    		return ok();
    	}
    }
    
    
	public static void sendPriceAlertMail(String vin) {
		
		AuthUser user = getLocalUser();
		//AuthUser user = AuthUser.findById(Integer.getInteger(session("USER_KEY")));
		//MyProfile profile = MyProfile.findByLocation(Long.valueOf(session("USER_LOCATION")));  //findByUser(user);
		
		AuthUser aUser = AuthUser.getlocationAndManagerByType(Location.findById(Long.valueOf(session("USER_LOCATION"))), "Manager");
    	MyProfile profile = MyProfile.findByUser(aUser);
		
		AuthUser logoUser = getLocalUser();
		//AuthUser logoUser = AuthUser.findById(Integer.getInteger(session("USER_KEY")));
 	    SiteLogo logo = SiteLogo.findByLocation(Long.valueOf(session("USER_LOCATION"))); //findByUser(logoUser);
		
		List<PriceAlert> priceAlertList = PriceAlert.getEmailsByStatus(user);
		List<PriceAlert> priceAlerts = PriceAlert.getEmailsByStatusVin(vin);
		for(PriceAlert alert: priceAlerts) {
			
			Vehicle vehicle = Vehicle.findByVinAndStatus(alert.vin);
			List<Vehicle> sameBodyList = Vehicle.getRandom(vehicle.vin);
			
			Vehicle sameBodyStyle = sameBodyList.get(0);
			InventoryImage sameBodyStyleDefault = InventoryImage.getDefaultImage(sameBodyStyle.vin);
			
			Vehicle sameEngine = sameBodyList.get(1);
			InventoryImage sameEngineDefault = InventoryImage.getDefaultImage(sameEngine.vin);
			
			Vehicle sameMake =  sameBodyList.get(2);
			InventoryImage sameMakeDefault = InventoryImage.getDefaultImage(sameMake.vin);
			
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
	    		}
	    		catch(UnsupportedEncodingException e){
	    			e.printStackTrace();
	    		}
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(alert.email));
				message.setSubject("VEHICLE PRICE CHANGE ALERT");
				Multipart multipart = new MimeMultipart();
				BodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart = new MimeBodyPart();
				
				VelocityEngine ve = new VelocityEngine();
				ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
				ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
				ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
				ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
				ve.init();
			
				
		        Template t = ve.getTemplate("/public/emailTemplate/priceAlert_HTML.vm"); 
		        VelocityContext context = new VelocityContext();
		        
		        context.put("hostnameUrl", imageUrlPath);
		        context.put("siteLogo", logo.logoImagePath);
		        
		        context.put("year", vehicle.year);
		        context.put("make", vehicle.make);
		        context.put("model", vehicle.model);
		        context.put("oldPrice", "$"+alert.oldPrice);
		        context.put("newPrice", "$"+vehicle.price);
		        context.put("bodyStyle", vehicle.bodyStyle);
		        context.put("mileage", vehicle.mileage);
		        
		        if(vehicle.doors != null) {
		        	context.put("doors", vehicle.doors);
		        	} else {
		        		context.put("doors", "");
		        	}
		        
		        
		        if(vehicle.standardSeating != null) {
		        	context.put("seats", vehicle.standardSeating);
		        	} else {
		        		context.put("seats", "" );
		        	}
		        
		        if(vehicle.drivetrain != null) {
		        	context.put("driveTrain", vehicle.drivetrain);
		        	} else {
		        		context.put("driveTrain", "");
		        	}
		        
		        if(vehicle.engine != null) {
		        	context.put("engine", vehicle.engine);
		        	} else {
		        		context.put("engine", "");
		        	}
		        
		        
		        if(vehicle.transmission != null) {
		        	 context.put("transmission", vehicle.transmission);
		        	} else {
		        		 context.put("transmission", "");
		        	}
		        
		        if(vehicle.brakes != null) {
		        	context.put("brakes", vehicle.brakes);
		        	} else {
		        		context.put("brakes", "");
		        	}
		        
		        
		        if(vehicle.horsePower != null) {
		        	context.put("horsePower", vehicle.horsePower);
		        	} else {
		        		context.put("horsePower", "");
		        	}
		        
		        context.put("email", profile.email);
		        String firstThreeDigit=profile.phone;
		        firstThreeDigit=firstThreeDigit.substring(0, 3);
		        String secondThreeDigit=profile.phone;
		        secondThreeDigit=secondThreeDigit.substring(3, 6);
		        String thirdThreeDigit=profile.phone;
		        thirdThreeDigit=thirdThreeDigit.substring(6, 10);
		        context.put("firstThreeDigit", firstThreeDigit);
		        context.put("secondThreeDigit", secondThreeDigit);
		        context.put("thirdThreeDigit", thirdThreeDigit);
		        
		        context.put("phone", profile.phone);
		        if(sameBodyStyle != null) {
		        	if(sameBodyStyle.price != null) {
		        		context.put("bodyStylePrice", "$"+sameBodyStyle.price.toString());
		        	} else {
		        		context.put("bodyStylePrice", "");
		        	}
		        	context.put("bodyStyleVin", sameBodyStyle.vin);
		        	context.put("bodyStyleYear", sameBodyStyle.year);
		        	context.put("bodyStyleMake", sameBodyStyle.make);
		        	context.put("bodyStyleModel", sameBodyStyle.model);
		        } else {
		        	context.put("bodyStylePrice", "");
		        	context.put("bodyStyleVin", "");
		        }
		        if(sameEngine != null) {
		        	if(sameEngine.price != null) {
		        		context.put("enginePrice", "$"+sameEngine.price.toString());
		        	} else {
		        		context.put("enginePrice", "");
		        	}
		        	context.put("engineVin", sameEngine.vin);
		        	context.put("engineMake", sameEngine.make);
		        	context.put("engineYear", sameEngine.year);
		        	context.put("engineModel", sameEngine.model);
		        } else {
		        	context.put("enginePrice","");
		        	context.put("engineVin", "");
		        }
		        if(sameMake != null) {
		        	if(sameMake.price != null) {
		        		context.put("makePrice", "$"+sameMake.price.toString());
		        	} else {
		        		context.put("makePrice", "");
		        	}
		        	context.put("makeVin", sameMake.vin);
		        	context.put("sameMake", sameMake.make);
		        	context.put("sameModel", sameMake.model);
		        	context.put("sameYear", sameMake.year);
		        } else {
		        	context.put("makePrice", "");
		        	context.put("makeVin", "");
		        }
		        
		        if(sameBodyStyleDefault != null) {
		        	context.put("sameBodyStyleDefault", sameBodyStyleDefault.thumbPath);
		        } else {
		        	context.put("sameBodyStyleDefault", "/no-image.jpg");
		        }
		        if(sameEngineDefault != null) {
		        	context.put("sameEngineDefault", sameEngineDefault.thumbPath);
		        } else {
		        	context.put("sameEngineDefault", "/no-image.jpg");
		        }
		        if(sameMakeDefault != null) {
		        	context.put("sameMakeDefault", sameMakeDefault.thumbPath);
		        } else {
		        	context.put("sameMakeDefault", "/no-image.jpg");
		        }
		        InventoryImage image = InventoryImage.getDefaultImage(vehicle.vin);
		        if(image != null) {
		        	context.put("defaultImage", image.path);
		        } else {
		        	context.put("defaultImage", "/no-image.jpg");
		        }
		        StringWriter writer = new StringWriter();
		        t.merge( context, writer );
		        String content = writer.toString(); 
				
				messageBodyPart.setContent(content, "text/html");
				multipart.addBodyPart(messageBodyPart);
				message.setContent(multipart);
				Transport.send(message);
				alert.setSendEmail("N");
				alert.update();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			} 
		}	
	}
	public static Result updateProduct(){
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		Form<AddCollectionVM> form = DynamicForm.form(AddCollectionVM.class).bindFromRequest();
    		AddCollectionVM vm = form.get();
    		AddCollection product = AddCollection.findById(vm.id);
    		if(product != null){
    			product.setTitle(vm.title);
    			product.setDescription(vm.description);
    			product.setParentId(vm.parentId);
    			if(vm.parentId == null){
    				product.setSubhideWebsite(vm.subhideWebsite);
	    		}else{
	    			product.setSubhideWebsite(0);
	    		}
	    		/*AddCollection aCollection = AddCollection.findById(vm.parentId);
	    		if(aCollection != null){
	    			product.hideWebsite = aCollection.subhideWebsite;
	    		}else*/
    			product.update();
    			
    		}
    		return ok();
    	}
	}
   /* public static Result updateVehicle(){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		int flag=0;
    		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    		Date currDate = new Date();
    		AuthUser user = getLocalUser();
	    	Form<SpecificationVM> form = DynamicForm.form(SpecificationVM.class).bindFromRequest();
	    	SpecificationVM vm = form.get();
	    	Vehicle vehicle = Vehicle.findById(vm.id);
	    	if(vehicle != null) {
	    		
	    		String databasevalue=vehicle.price.toString();
	    		String vmvalue= vm.price.toString();
	    		if(!vmvalue.equals(databasevalue)) {
	    			
	    				List<PriceAlert> alertList = PriceAlert.getEmailsByVin(vehicle.vin, Long.valueOf(session("USER_LOCATION")));
	    				for(PriceAlert priceAlert: alertList) {
	    					priceAlert.setSendEmail("Y");
	    					priceAlert.setOldPrice(vehicle.price);
	    					priceAlert.setCurrDate(currDate);
	    					priceAlert.update();
	    				}
	    				Date crDate = new Date();
	    				PriceChange change = new PriceChange();
	    				change.dateTime = crDate;
	    				change.price = vm.price.toString();
	    				change.person = user.firstName +" "+user.lastName;
	    				change.vin = vm.vin;
	    				change.save();
	    				flag=1;
	    			//	sendPriceAlertMail(vehicle.vin);
	    		}
	    		vehicle.setTitle(vm.title);
	    		vehicle.setMake(vm.make);
	    		vehicle.setModel(vm.model);
	    		vehicle.setExteriorColor(vm.extColor);
	    		vehicle.setCityMileage(vm.city_mileage);
	    		vehicle.setHighwayMileage(vm.highway_mileage);
		    	vehicle.setStock(vm.stock);
		    	vehicle.setPrice(vm.price);
		    	vehicle.setBodyStyle(vm.bodyStyle);
		    	vehicle.setMileage(vm.mileage);
		    	
		    	vehicle.update();
		    	if(flag==1){
		    		sendPriceAlertMail(vehicle.vin);
		    	}
		    //	sendPriceAlertMail(vehicle.vin);
	    	}
	    	return ok();
    	}	
    } */
    
    public static Result uplaodSoundFile() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	MultipartFormData body = request().body().asMultipartFormData();
	    	DynamicForm dynamicForm = Form.form().bindFromRequest();
			String vin = dynamicForm.get("vinNum");
	    	
	    	Identity user = getLocalUser();
	    	AuthUser userObj = (AuthUser)user;
	    	
	    	FilePart picture = body.getFile("file0");
	    	  if (picture != null) {
	    	    String fileName = picture.getFilename();
	    	    File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+vin+"-"+userObj.id+File.separator+"audio");
	    	    if(!fdir.exists()) {
	    	    	fdir.mkdir();
	    	    }
	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+vin+"-"+userObj.id+File.separator+"audio"+File.separator+fileName;
	    	    File file = picture.getFile();
	    	    try {
	    	    		FileUtils.moveFile(file, new File(filePath));
	    	    		VehicleAudio audio = new VehicleAudio();
	    	    		audio.vin = vin;
	    	    		audio.path = File.separator+session("USER_LOCATION")+File.separator+vin+"-"+userObj.id+File.separator+"audio"+File.separator+fileName;
	    	    		audio.fileName = fileName;
	    	    		audio.user = userObj;
	    	    		audio.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	    	    		audio.save();
	    	  } catch (FileNotFoundException e) {
	  			e.printStackTrace();
		  		} catch (IOException e) {
		  			e.printStackTrace();
		  		} 
	    	  } 
	    	return ok();
    	}	
    }
	
    
    public static Result getAllAudio(String vin) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	List<VehicleAudio> audioList = VehicleAudio.getByUserAndVin(user, vin);
	    	List<AudioVM> vmList = new ArrayList<>();
	    	
	    	for(VehicleAudio audio: audioList) {
	    		AudioVM vm = new AudioVM();
	    		vm.id = audio.id;
	    		vm.path = audio.path;
	    		vm.fileName = audio.fileName;
	    		vmList.add(vm);
	    	}
	    	return ok(Json.toJson(vmList));
    	}
    }
    
    
    public static Result getVirtualTour(Long vid) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	Vehicle vehicle = Vehicle.findById(vid);
	    	VirtualTourVM vtVM = new VirtualTourVM();
	    	VideoVM vVM = new VideoVM();
	    	Map<String, Object> map = new HashMap<String, Object>();
	    	map.put("video", vVM);
	    	map.put("virtualTour", vtVM);
	    	
	    	if(vehicle != null){
	    		VirtualTour virtualTour = VirtualTour.findByUserAndVin(user, vehicle.vin);
	    		Video video = Video.findByUserAndVin(user, vehicle.vin);
		    	if(virtualTour != null) {
		    		vtVM.desktopUrl = virtualTour.desktopUrl;
		    		vtVM.mobileUrl = virtualTour.mobileUrl;
		    	}
		    	if(video != null) {
		    		vVM.desktopUrl = video.desktopUrl;
		    		vVM.mobileUrl = video.mobileUrl;
		    	}
	    	}
	    	return ok(Json.toJson(map));
    	}	
    }
    
    
    public static Result saveVData() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	Form<VirtualTourVM> form = DynamicForm.form(VirtualTourVM.class).bindFromRequest();
	    	VirtualTourVM vm = form.get();
	    	Vehicle vehicle = Vehicle.findById(vm.vehicleId);
	    	if(vehicle != null){
	    		VirtualTour virtualTour = VirtualTour.findByUserAndVin(user, vehicle.vin);
		    	if(virtualTour == null) {
			    	VirtualTour vt = new VirtualTour();
			    	if(vm.desktopUrl == null || vm.desktopUrl == ""){
		    			vt.desktopUrl = null;
		    		}else{
		    			vt.desktopUrl = vm.desktopUrl;
		    		}
		    		if(vm.mobileUrl == null || vm.mobileUrl == ""){
		    			vt.mobileUrl = null;
		    		}else{
		    			vt.mobileUrl = vm.mobileUrl;
		    		}
			    	vt.vin = vehicle.vin;
			    	vt.user = user;
			    	vt.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
			    	vt.save();
		    	} else {
		    		/*virtualTour.setDesktopUrl(vm.desktopUrl);
		    		virtualTour.setMobileUrl(vm.mobileUrl);*/
		    		if(vm.desktopUrl == null || vm.desktopUrl == ""){
		    			virtualTour.setDesktopUrl(null);
		    		}else{
		    			virtualTour.setDesktopUrl(vm.desktopUrl);
		    		}
		    		if(vm.mobileUrl == null || vm.mobileUrl == ""){
		    			virtualTour.setMobileUrl(null);
		    		}else{
		    			virtualTour.setMobileUrl(vm.mobileUrl);
		    		}
		    		virtualTour.update();
		    	}
	    	}
	    	return ok();
    	}	
    }
    
    public static Result saveVideoData() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	Form<VideoVM> form = DynamicForm.form(VideoVM.class).bindFromRequest();
	    	VideoVM vm = form.get();
	    	Vehicle vehicle = Vehicle.findById(vm.vehicleId);
	    	if(vehicle != null){
	    		Video virtualTour = Video.findByUserAndVin(user, vehicle.vin);
		    	
		    	if(virtualTour == null) {
		    		Video vt = new Video();
		    		if(vm.desktopUrl == null || vm.desktopUrl == ""){
		    			vt.desktopUrl = null;
		    		}else{
		    			vt.desktopUrl = vm.desktopUrl;
		    		}
		    		if(vm.mobileUrl == null || vm.mobileUrl == ""){
		    			vt.mobileUrl = null;
		    		}else{
		    			vt.mobileUrl = vm.mobileUrl;
		    		}
			    	vt.vin = vehicle.vin;
			    	vt.user = user;
			    	vt.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
			        vt.save();
			    	
		    	} else {
		    		if(vm.desktopUrl == null || vm.desktopUrl == ""){
		    			virtualTour.setDesktopUrl(null);
		    		}else{
		    			virtualTour.setDesktopUrl(vm.desktopUrl);
		    		}
		    		if(vm.mobileUrl == null || vm.mobileUrl == ""){
		    			virtualTour.setMobileUrl(null);
		    		}else{
		    			virtualTour.setMobileUrl(vm.mobileUrl);
		    		}
		    		virtualTour.update();
		    	}
	    	}
	    	return ok();
    	}	
    }
    
    
    
    
  /*  public static Result sendEmail(String email, String comment) {

    	Properties props = new Properties();
    	props.put("mail.smtp.auth", "true");
    	props.put("mail.smtp.starttls.enable", "true");
    	props.put("mail.smtp.host", "smtp.gmail.com");
    	props.put("mail.smtp.port", "587");
    	 
    	Session session = Session.getInstance(props,
    	 new javax.mail.Authenticator() {
    	protected PasswordAuthentication getPasswordAuthentication() {
    	return new PasswordAuthentication(emailUsername, emailPassword);
    	}
    	 });
    	 
    	try{

    	Message feedback = new MimeMessage(session);
    	 	feedback.setFrom(new InternetAddress("glider.autos@gmail.com"));
    	  feedback.setRecipients(Message.RecipientType.TO,
    	 	InternetAddress.parse(email));
    	  feedback.setSubject("Manager like your work");	 	
    	 	BodyPart messageBodyPart = new MimeBodyPart();	
    	 	        messageBodyPart.setText("Comment: "+comment);	   
    	 	        Multipart multipart = new MimeMultipart();	 	   
    	 	        multipart.addBodyPart(messageBodyPart);	           
    	 	        feedback.setContent(multipart);
    	 	    Transport.send(feedback);
    	   	System.out.println("email send");
    	      } catch (MessagingException e) {
    	 	 throw new RuntimeException(e);
    	 	}
    	return ok();
    	}*/
    
    
    public static Result uploadInventoryPhotosComingSoon() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	MultipartFormData body = request().body().asMultipartFormData();
	    	
	    	Identity user = getLocalUser();
	    	AuthUser userObj = (AuthUser)user;
	    	
	    	FilePart picture = body.getFile("file");
	    	  if (picture != null) {
	    	    String fileName = picture.getFilename();
	    	    String fname = fileName.replace("(", "-");
				String fname1=fname.replace(")","-");	
				fileName=fname1.replace(" ", "-");
	    	    File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+"Inventory"+File.separator+"CoverImg");
	    	    if(!fdir.exists()) {
	    	    	fdir.mkdirs();
	    	    }
	    	    
	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"Inventory"+File.separator+"CoverImg"+File.separator+fileName;
	    	    String thumbnailPath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"Inventory"+File.separator+"CoverImg"+File.separator+"thumbnail_"+fileName;
	    	    File thumbFile = new File(thumbnailPath);
	    	    File file = picture.getFile();
	    	    try {
	    	    BufferedImage originalImage = ImageIO.read(file);
	    	    Thumbnails.of(originalImage).size(1200, 315).toFile(thumbFile);
	    	    File _f = new File(filePath);
				Thumbnails.of(originalImage).scale(1.0).toFile(_f);
				
				SiteInventory sInventory = SiteInventory.findByLocationAndType(Long.parseLong(session("USER_LOCATION")), "comingSoon");
				
				if(sInventory == null){
					SiteInventory siteInventory = new SiteInventory();
					siteInventory.imageName = fileName;
					siteInventory.imageUrl = "/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+fileName;
					siteInventory.thumbPath = "/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+"thumbnail_"+fileName;
					siteInventory.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
					siteInventory.vType = "comingSoon";
					siteInventory.findNewId=1L;
					siteInventory.save();
				}else{
					if(sInventory.applyAll == 1){
						List<SiteInventory> sInventory1 = SiteInventory.findByLocation(Long.parseLong(session("USER_LOCATION")));
						for(SiteInventory siteInventory:sInventory1){
							siteInventory.setImageName(fileName);
							siteInventory.setImageUrl("/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+fileName);
							siteInventory.setThumbPath("/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+"thumbnail_"+fileName);
							Long value = siteInventory.findNewId + 1L;
							siteInventory.setFindNewId(value);
							siteInventory.update();
						}
					}else{
						sInventory.setImageName(fileName);
						sInventory.setImageUrl("/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+fileName);
						sInventory.setThumbPath("/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+"thumbnail_"+fileName);
						Long value = sInventory.findNewId + 1L;
						sInventory.setFindNewId(value);
						sInventory.update();
					}
					
					
				}
				
	    	  } catch (FileNotFoundException e) {
	  			e.printStackTrace();
		  		} catch (IOException e) {
		  			e.printStackTrace();
		  		} 
	    	  } 
	    	
	    	return ok();
    	}	
    }
    
    
    public static Result uploadInventoryPhotosNew() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	MultipartFormData body = request().body().asMultipartFormData();
	    	
	    	Identity user = getLocalUser();
	    	AuthUser userObj = (AuthUser)user;
	    	
	    	FilePart picture = body.getFile("file");
	    	  if (picture != null) {
	    	    String fileName = picture.getFilename();
	    	    String fname = fileName.replace("(", "-");
				String fname1=fname.replace(")","-");	
				fileName=fname1.replace(" ", "-");
	    	    File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+"Inventory"+File.separator+"CoverImg");
	    	    if(!fdir.exists()) {
	    	    	fdir.mkdirs();
	    	    }
	    	    
	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"Inventory"+File.separator+"CoverImg"+File.separator+fileName;
	    	    String thumbnailPath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"Inventory"+File.separator+"CoverImg"+File.separator+"thumbnail_"+fileName;
	    	    File thumbFile = new File(thumbnailPath);
	    	    File file = picture.getFile();
	    	    try {
	    	    BufferedImage originalImage = ImageIO.read(file);
	    	    Thumbnails.of(originalImage).size(1200, 315).toFile(thumbFile);
	    	    File _f = new File(filePath);
				Thumbnails.of(originalImage).scale(1.0).toFile(_f);
				
				SiteInventory sInventory = SiteInventory.findByLocationAndType(Long.parseLong(session("USER_LOCATION")), "New");
				
				if(sInventory == null){
					SiteInventory siteInventory = new SiteInventory();
					siteInventory.imageName = fileName;
					siteInventory.imageUrl = "/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+fileName;
					siteInventory.thumbPath = "/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+"thumbnail_"+fileName;
					siteInventory.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
					siteInventory.vType = "New";
					siteInventory.findNewId=1L;
					siteInventory.save();
				}else{
					if(sInventory.applyAll == 1){
						List<SiteInventory> sInventory1 = SiteInventory.findByLocation(Long.parseLong(session("USER_LOCATION")));
						for(SiteInventory siteInventory:sInventory1){
							siteInventory.setImageName(fileName);
							siteInventory.setImageUrl("/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+fileName);
							siteInventory.setThumbPath("/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+"thumbnail_"+fileName);
							Long value = siteInventory.findNewId + 1L;
							siteInventory.setFindNewId(value);
							siteInventory.update();
						}
					}else{
						sInventory.setImageName(fileName);
						sInventory.setImageUrl("/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+fileName);
						sInventory.setThumbPath("/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+"thumbnail_"+fileName);
						
						Long value = sInventory.findNewId + 1L;
						sInventory.setFindNewId(value);
						sInventory.update();
					}
				}
				
	    	  } catch (FileNotFoundException e) {
	  			e.printStackTrace();
		  		} catch (IOException e) {
		  			e.printStackTrace();
		  		} 
	    	  } 
	    	
	    	return ok();
    	}	
    }
    
    public static Result uploadInventoryPhotos() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	MultipartFormData body = request().body().asMultipartFormData();
	    	
	    	Identity user = getLocalUser();
	    	AuthUser userObj = (AuthUser)user;
	    	
	    	FilePart picture = body.getFile("file");
	    	  if (picture != null) {
	    	    String fileName = picture.getFilename();
	    	    String fname = fileName.replace("(", "-");
				String fname1=fname.replace(")","-");	
				fileName=fname1.replace(" ", "-");
	    	    File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+"Inventory"+File.separator+"CoverImg");
	    	    if(!fdir.exists()) {
	    	    	fdir.mkdirs();
	    	    }
	    	    
	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"Inventory"+File.separator+"CoverImg"+File.separator+fileName;
	    	    String thumbnailPath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"Inventory"+File.separator+"CoverImg"+File.separator+"thumbnail_"+fileName;
	    	    File thumbFile = new File(thumbnailPath);
	    	    File file = picture.getFile();
	    	    try {
	    	    BufferedImage originalImage = ImageIO.read(file);
	    	    Thumbnails.of(originalImage).size(1200, 315).toFile(thumbFile);
	    	    File _f = new File(filePath);
				Thumbnails.of(originalImage).scale(1.0).toFile(_f);
				
				SiteInventory sInventory = SiteInventory.findByLocationAndType(Long.parseLong(session("USER_LOCATION")), "Used");
				
				if(sInventory == null){
					SiteInventory siteInventory = new SiteInventory();
					siteInventory.imageName = fileName;
					siteInventory.imageUrl = "/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+fileName;
					siteInventory.thumbPath = "/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+"thumbnail_"+fileName;
					siteInventory.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
					siteInventory.vType = "Used";
					siteInventory.findNewId=1L;
					siteInventory.save();
				}else{
					if(sInventory.applyAll == 1){
						List<SiteInventory> sInventory1 = SiteInventory.findByLocation(Long.parseLong(session("USER_LOCATION")));
						for(SiteInventory siteInventory:sInventory1){
							siteInventory.setImageName(fileName);
							siteInventory.setImageUrl("/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+fileName);
							siteInventory.setThumbPath("/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+"thumbnail_"+fileName);
							Long value = siteInventory.findNewId + 1L;
							siteInventory.setFindNewId(value);
							siteInventory.update();
						}
					}else{
						sInventory.setImageName(fileName);
						sInventory.setImageUrl("/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+fileName);
						sInventory.setThumbPath("/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+"thumbnail_"+fileName);
						Long value = sInventory.findNewId + 1L;
						sInventory.setFindNewId(value);
						sInventory.update();
					}
				}
				
	    	  } catch (FileNotFoundException e) {
	  			e.printStackTrace();
		  		} catch (IOException e) {
		  			e.printStackTrace();
		  		} 
	    	  } 
	    	
	    	return ok();
    	}	
    }
    
    public static Result uploadSliderPhotos() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	MultipartFormData body = request().body().asMultipartFormData();
	    	
	    	Identity user = getLocalUser();
	    	AuthUser userObj = (AuthUser)user;
	    	
	    	FilePart picture = body.getFile("file");
	    	  if (picture != null) {
	    	    String fileName = picture.getFilename();
	    	    File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+userObj.id+File.separator+"SliderImages");
	    	    if(!fdir.exists()) {
	    	    	fdir.mkdirs();
	    	    }
	    	    
	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+userObj.id+File.separator+"SliderImages"+File.separator+fileName;
	    	    String thumbnailPath = rootDir+File.separator+session("USER_LOCATION")+File.separator+userObj.id+File.separator+"SliderImages"+File.separator+"thumbnail_"+fileName;
	    	    File thumbFile = new File(thumbnailPath);
	    	    File file = picture.getFile();
	    	    try {
	    	    BufferedImage originalImage = ImageIO.read(file);
	    	    Thumbnails.of(originalImage).size(150, 150).toFile(thumbFile);
	    	    File _f = new File(filePath);
				Thumbnails.of(originalImage).scale(1.0).toFile(_f);
				
				SliderImage imageObj = SliderImage.getByImagePath("/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"SliderImages"+"/"+fileName);
				if(imageObj == null) {
					List<SliderImage> sliderList = SliderImage.find.all();
					SliderImage vImage = new SliderImage();
					vImage.imgName = fileName;
					vImage.path = "/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"SliderImages"+"/"+fileName;
					vImage.thumbPath = "/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"SliderImages"+"/"+"thumbnail_"+fileName;
					vImage.user = userObj;
					vImage.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
					if(sliderList.size() == 0) {
						vImage.sliderNumber = 1;
					}
					if(sliderList.size() == 1) {
						if(sliderList.get(0).sliderNumber == 1) {
							vImage.sliderNumber = 2;
						}
						if(sliderList.get(0).sliderNumber == 2) {
							vImage.sliderNumber = 3;
						}
						if(sliderList.get(0).sliderNumber == 3) {
							vImage.sliderNumber = 1;
						}
					}
					if(sliderList.size() == 2) {
						if((sliderList.get(0).sliderNumber == 1 && sliderList.get(1).sliderNumber == 2)||(sliderList.get(0).sliderNumber == 2 && sliderList.get(1).sliderNumber == 1)) {
							vImage.sliderNumber = 3;
						}
						if((sliderList.get(0).sliderNumber == 1 && sliderList.get(1).sliderNumber == 3)||(sliderList.get(0).sliderNumber == 3 && sliderList.get(1).sliderNumber == 1)) {
							vImage.sliderNumber = 2;
						}
						if((sliderList.get(0).sliderNumber == 2 && sliderList.get(1).sliderNumber == 3)||(sliderList.get(0).sliderNumber == 3 && sliderList.get(1).sliderNumber == 2)) {
							vImage.sliderNumber = 1;
						}
					}
					vImage.save();
					
				}
	    	  } catch (FileNotFoundException e) {
	  			e.printStackTrace();
		  		} catch (IOException e) {
		  			e.printStackTrace();
		  		} 
	    	  } 
	    	
	    	return ok();
    	}	
    }
    
    public static Result  uploadContactPhotos() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	MultipartFormData body = request().body().asMultipartFormData();
	    	
	    	AuthUser userObj = (AuthUser)getLocalUser();
	    	
	    	FilePart picture = body.getFile("file");
	    	  if (picture != null) {
	    	    String fileName = picture.getFilename();
	    	    File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+"ContactImages");
	    	    if(!fdir.exists()) {
	    	    	fdir.mkdirs();
	    	    }
	    	    String fname = fileName.replace("(", "-");
				String fname1=fname.replace(")","-");	
				fileName=fname1.replace(" ", "-");
	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"ContactImages"+File.separator+fileName;
	    	    String thumbnailPath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"ContactImages"+File.separator+"thumbnail_"+fileName;
	    	    File thumbFile = new File(thumbnailPath);
	    	    File file = picture.getFile();
	    	    try {
	    	    BufferedImage originalImage = ImageIO.read(file);
	    	    Thumbnails.of(originalImage).size(150, 150).toFile(thumbFile);
	    	    File _f = new File(filePath);
				Thumbnails.of(originalImage).scale(1.0).toFile(_f);
				
				ContactHeader imageObj = ContactHeader.findByLocations(Long.valueOf(session("USER_LOCATION")));
				if(imageObj == null) {
					ContactHeader vImage = new ContactHeader();
					vImage.coverImageName = fname1;
					vImage.path = "/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"ContactImages"+"/"+fileName;
					vImage.thumbPath = "/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"ContactImages"+"/"+"thumbnail_"+fileName;
					vImage.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
					vImage.findNewId=1L;
					vImage.save();
					
				}
				else{
					imageObj.setCoverImageName(fname1);
					imageObj.setPath("/"+session("USER_LOCATION")+"/"+"ContactImages"+"/"+fileName);
					imageObj.setThumbPath("/"+session("USER_LOCATION")+"/"+"ContactImages"+"/"+"thumbnail_"+fileName);
					Long value = imageObj.findNewId + 1L;
					imageObj.setFindNewId(value);
					imageObj.update();
				}
	    	  } catch (FileNotFoundException e) {
	  			e.printStackTrace();
		  		} catch (IOException e) {
		  			e.printStackTrace();
		  		} 
	    	  } 
	    	
	    	return ok();
    	}	
    }
    
    
    public static Result  uploadWarrantyPhotos() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	MultipartFormData body = request().body().asMultipartFormData();
	    	
	    	AuthUser userObj = (AuthUser)getLocalUser();
	    	
	    	FilePart picture = body.getFile("file");
	    	  if (picture != null) {
	    	    String fileName = picture.getFilename();
	    	    File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+"Warranty");
	    	    if(!fdir.exists()) {
	    	    	fdir.mkdirs();
	    	    }
	    	    String fname = fileName.replace("(", "-");
				String fname1=fname.replace(")","-");	
				fileName=fname1.replace(" ", "-");
	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"Warranty"+File.separator+fileName;
	    	    String thumbnailPath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"Warranty"+File.separator+"thumbnail_"+fileName;
	    	    File thumbFile = new File(thumbnailPath);
	    	    File file = picture.getFile();
	    	    try {
	    	    BufferedImage originalImage = ImageIO.read(file);
	    	    Thumbnails.of(originalImage).size(150, 150).toFile(thumbFile);
	    	    File _f = new File(filePath);
				Thumbnails.of(originalImage).scale(1.0).toFile(_f);
				
				Warranty imageObj = Warranty.findByLocations(Long.valueOf(session("USER_LOCATION")));
				if(imageObj == null) {
					Warranty vImage = new Warranty();
					vImage.coverImageName = fname1;
					vImage.path = "/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"Warranty"+"/"+fileName;
					vImage.thumbPath = "/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"Warranty"+"/"+"thumbnail_"+fileName;
					vImage.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
					vImage.findNewId = 1L;
					vImage.save();
					
				}
				else{
					imageObj.setCoverImageName(fname1);
					imageObj.setPath("/"+session("USER_LOCATION")+"/"+"Warranty"+"/"+fileName);
					imageObj.setThumbPath("/"+session("USER_LOCATION")+"/"+"Warranty"+"/"+"thumbnail_"+fileName);
					Long value = imageObj.findNewId + 1L;
					imageObj.setFindNewId(value);
					imageObj.update();
				}
	    	  } catch (FileNotFoundException e) {
	  			e.printStackTrace();
		  		} catch (IOException e) {
		  			e.printStackTrace();
		  		} 
	    	  } 
	    	
	    	return ok();
    	}	
    }
    
    

    public static Result  uploadComparePhotos() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	MultipartFormData body = request().body().asMultipartFormData();
	    	
	    	AuthUser userObj = (AuthUser)getLocalUser();
	    	
	    	FilePart picture = body.getFile("file");
	    	  if (picture != null) {
	    	    String fileName = picture.getFilename();
	    	    File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+"Compare");
	    	    if(!fdir.exists()) {
	    	    	fdir.mkdirs();
	    	    }
	    	    String fname = fileName.replace("(", "-");
				String fname1=fname.replace(")","-");	
				fileName=fname1.replace(" ", "-");
	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"Compare"+File.separator+fileName;
	    	    String thumbnailPath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"Compare"+File.separator+"thumbnail_"+fileName;
	    	    File thumbFile = new File(thumbnailPath);
	    	    File file = picture.getFile();
	    	    try {
	    	    BufferedImage originalImage = ImageIO.read(file);
	    	    Thumbnails.of(originalImage).size(150, 150).toFile(thumbFile);
	    	    File _f = new File(filePath);
				Thumbnails.of(originalImage).scale(1.0).toFile(_f);
				
				SiteComparison imageObj = SiteComparison.findByLocations(Long.valueOf(session("USER_LOCATION")));
				if(imageObj == null) {
					SiteComparison vImage = new SiteComparison();
					vImage.coverImageName = fname1;
					vImage.path = "/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"Compare"+"/"+fileName;
					vImage.thumbPath = "/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"Compare"+"/"+"thumbnail_"+fileName;
					vImage.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
					vImage.findNewId=1L;
					vImage.save();
					
				}
				else{
					imageObj.setCoverImageName(fname1);
					imageObj.setPath("/"+session("USER_LOCATION")+"/"+"Compare"+"/"+fileName);
					imageObj.setThumbPath("/"+session("USER_LOCATION")+"/"+"Compare"+"/"+"thumbnail_"+fileName);
					Long value = imageObj.findNewId + 1L;
					imageObj.setFindNewId(value);
					imageObj.update();
				}
	    	  } catch (FileNotFoundException e) {
	  			e.printStackTrace();
		  		} catch (IOException e) {
		  			e.printStackTrace();
		  		} 
	    	  } 
	    	
	    	return ok();
    	}	
    }
    

    
    
    
    
    public static Result  uploadBlogPhotos() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	MultipartFormData body = request().body().asMultipartFormData();
	    	
	    	AuthUser userObj = (AuthUser)getLocalUser();
	    	
	    	FilePart picture = body.getFile("file");
	    	  if (picture != null) {
	    	    String fileName = picture.getFilename();
	    	    File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+"BlogImages");
	    	    if(!fdir.exists()) {
	    	    	fdir.mkdirs();
	    	    }
	    	    String fname = fileName.replace("(", "-");
				String fname1=fname.replace(")","-");	
				fileName=fname1.replace(" ", "-");
	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"BlogImages"+File.separator+fileName;
	    	    String thumbnailPath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"BlogImages"+File.separator+"thumbnail_"+fileName;
	    	    File thumbFile = new File(thumbnailPath);
	    	    File file = picture.getFile();
	    	    try {
	    	    BufferedImage originalImage = ImageIO.read(file);
	    	    Thumbnails.of(originalImage).size(150, 150).toFile(thumbFile);
	    	    File _f = new File(filePath);
				Thumbnails.of(originalImage).scale(1.0).toFile(_f);
				
				Blog imageObj = Blog .findByLocations(Long.valueOf(session("USER_LOCATION")));
				if(imageObj == null) {
					Blog vImage = new Blog();
					 vImage.coverImageName=fname1;
					vImage.path = "/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"BlogImages"+"/"+fileName;
					vImage.thumbPath = "/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"BlogImages"+"/"+"thumbnail_"+fileName;
					vImage.user = userObj;
					vImage.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
					vImage.findNewId=1L;
					vImage.save();
					
				}
				else{
					imageObj.setCoverImageName(fname1);
					imageObj.setPath("/"+session("USER_LOCATION")+"/"+"BlogImages"+"/"+fileName);
					imageObj.setThumbPath("/"+session("USER_LOCATION")+"/"+"BlogImages"+"/"+"thumbnail_"+fileName);
					Long value = imageObj.findNewId + 1L;
					imageObj.setFindNewId(value);
					imageObj.update();
				}
	    	  } catch (FileNotFoundException e) {
	  			e.printStackTrace();
		  		} catch (IOException e) {
		  			e.printStackTrace();
		  		} 
	    	  } 
	    	
	    	return ok();
    	}	
    }
    
    

    
    
    

    public static Result uploadCvrPhotos() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	MultipartFormData body = request().body().asMultipartFormData();
	    	
	    	AuthUser userObj = (AuthUser)getLocalUser();
	    	
	    	FilePart picture = body.getFile("file");
	    	  if (picture != null) {
	    	    String fileName = picture.getFilename();
	    	    File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+userObj.id+File.separator+"CvrImages");
	    	    if(!fdir.exists()) {
	    	    	fdir.mkdirs();
	    	    }
	    	    String fname = fileName.replace("(", "-");
				String fname1=fname.replace(")","-");	
				fileName=fname1.replace(" ", "-");
	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+File.separator+userObj.id+File.separator+"CvrImages"+File.separator+fileName;
	    	    String thumbnailPath = rootDir+File.separator+session("USER_LOCATION")+File.separator+File.separator+userObj.id+File.separator+"CvrImages"+File.separator+"thumbnail_"+fileName;
	    	    File thumbFile = new File(thumbnailPath);
	    	    File file = picture.getFile();
	    	    try {
	    	    BufferedImage originalImage = ImageIO.read(file);
	    	    Thumbnails.of(originalImage).size(150, 150).toFile(thumbFile);
	    	    File _f = new File(filePath);
				Thumbnails.of(originalImage).scale(1.0).toFile(_f);
				
				SiteAboutUs imageObj = SiteAboutUs.findByLocation(Long.valueOf(session("USER_LOCATION")));
				if(imageObj == null) {
					SiteAboutUs vImage = new SiteAboutUs();
					vImage.imgName = fname1;
					vImage.path = "/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"CvrImages"+"/"+fileName;
					vImage.thumbPath = "/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"CvrImages"+"/"+"thumbnail_"+fileName;
					vImage.user = userObj;
					vImage.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
					vImage.findNewId=1L;
					vImage.save();
					
				}
				else{
					imageObj.setImgName(fname1);
					imageObj.setPath("/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"CvrImages"+"/"+fileName);
					imageObj.setThumbPath("/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"CvrImages"+"/"+"thumbnail_"+fileName);
					Long value = imageObj.findNewId + 1L;
					imageObj.setFindNewId(value);
					imageObj.update();
				}
	    	  } catch (FileNotFoundException e) {
	  			e.printStackTrace();
		  		} catch (IOException e) {
		  			e.printStackTrace();
		  		} 
	    	  } 
	    	
	    	return ok();
    	}	
    }
    
    
    
    
    public static Result uploadFeaturedPhotos() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	MultipartFormData body = request().body().asMultipartFormData();
	    	
	    	AuthUser userObj = (AuthUser)getLocalUser();
	    	
	    	FilePart picture = body.getFile("file");
	    	  if (picture != null) {
	    	    String fileName = picture.getFilename();
	    	    File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+userObj.id+File.separator+"FeaturedImages");
	    	    if(!fdir.exists()) {
	    	    	fdir.mkdirs();
	    	    }
	    	    
	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+File.separator+userObj.id+File.separator+"FeaturedImages"+File.separator+fileName;
	    	    String thumbnailPath = rootDir+File.separator+session("USER_LOCATION")+File.separator+File.separator+userObj.id+File.separator+"FeaturedImages"+File.separator+"thumbnail_"+fileName;
	    	    File thumbFile = new File(thumbnailPath);
	    	    File file = picture.getFile();
	    	    try {
	    	    BufferedImage originalImage = ImageIO.read(file);
	    	    Thumbnails.of(originalImage).size(150, 150).toFile(thumbFile);
	    	    File _f = new File(filePath);
				Thumbnails.of(originalImage).scale(1.0).toFile(_f);
				
				FeaturedImage imageObj = FeaturedImage.getByImagePath("/"+userObj.id+"/"+"FeaturedImages"+"/"+fileName);
				if(imageObj == null) {
					FeaturedImage vImage = new FeaturedImage();
					vImage.imgName = fileName;
					vImage.path = "/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"FeaturedImages"+"/"+fileName;
					vImage.thumbPath = "/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"FeaturedImages"+"/"+"thumbnail_"+fileName;
					vImage.user = userObj;
					vImage.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
					vImage.save();
					
				}
	    	  } catch (FileNotFoundException e) {
	  			e.printStackTrace();
		  		} catch (IOException e) {
		  			e.printStackTrace();
		  		} 
	    	  } 
	    	
	    	return ok();
    	}	
    }
    
    
    public static Result getSliderImageById(Long id,String type) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	File file = null;
	    	SliderImage image = SliderImage.findById(id);
	    	if(type.equals("thumbnail")) {
		    	file = new File(rootDir+image.thumbPath);
	    	}
	    	
	    	if(type.equals("full")) {
	    		file = new File(rootDir+image.path);
	    	}
	    	return ok(file);
    	}	
    }
    
    
    
    public static Result getInventoryImage(Long id,String vType,String type) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	File file = null;
	    	SiteInventory image = SiteInventory.findByOtherId(id,vType,Long.valueOf(session("USER_LOCATION")));
	    	if(type.equals("thumbnail")) {
		    	file = new File(rootDir+image.thumbPath);
	    	}
	    	
	    	if(type.equals("full")) {
	    		file = new File(rootDir+image.imageUrl);
	    	}
	    	return ok(file);
    	}
    }
    
    
    public static Result contactImageById(Long id,String type) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	File file = null;
	    	ContactHeader image = ContactHeader.findByOtherId(id,Long.valueOf(session("USER_LOCATION")));
	    	if(image.thumbPath != null || image.path != null){
	    	if(type.equals("thumbnail")) {
		    	file = new File(rootDir+image.thumbPath);
	    	}
	    	
	    	if(type.equals("full")) {
	    		file = new File(rootDir+image.path);
	    	}
	    	}
	    	return ok(file);
    	}
    }
   
    
    public static Result  vehicleProfileImageByIdForCrop(Long id,String makeValue,String type) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	File file = null;
	    	VehicleHeader image = VehicleHeader.findByIdAndMake(id,makeValue);
	    	if(image.thumbPath != null || image.path != null){
	    	if(type.equals("thumbnail")) {
		    	file = new File(rootDir+image.thumbPath);
	    	}
	    	
	    	if(type.equals("full")) {
	    		file = new File(rootDir+image.path);
	    	}
	    	}
	    	return ok(file);
    	}
    }
    
    
    
    
    public static Result vehicleProfileImageById(Long id,String type) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	File file = null;
	    	VehicleHeader image = VehicleHeader.findByOtherId(id,Long.valueOf(session("USER_LOCATION")));
	    	if(image.thumbPath != null || image.path != null){
	    	if(type.equals("thumbnail")) {
		    	file = new File(rootDir+image.thumbPath);
	    	}
	    	
	    	if(type.equals("full")) {
	    		file = new File(rootDir+image.path);
	    	}
	    	}
	    	return ok(file);
    	}
    }
    
    

    public static Result warrantyImageById(Long id,String type) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	File file = null;
	    	Warranty image = Warranty.findByOtherId(id,Long.valueOf(session("USER_LOCATION")));
	    	if(image.thumbPath != null || image.path != null){
	    	if(type.equals("thumbnail")) {
		    	file = new File(rootDir+image.thumbPath);
	    	}
	    	
	    	if(type.equals("full")) {
	    		file = new File(rootDir+image.path);
	    	}
	    	}
	    	return ok(file);
    	}
    }
    
    
    public static Result compareImageById(Long id,String type) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	File file = null;
	    	SiteComparison image = SiteComparison.findByOtherId(id,Long.valueOf(session("USER_LOCATION")));
	    	if(image.thumbPath != null || image.path != null){
	    	if(type.equals("thumbnail")) {
		    	file = new File(rootDir+image.thumbPath);
	    	}
	    	
	    	if(type.equals("full")) {
	    		file = new File(rootDir+image.path);
	    	}
	    	}
	    	return ok(file);
    	}
    }
    
    
    
    
    
    
    public static Result blogImageById(Long id,String type) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	File file = null;
	    	Blog image = Blog.findByOtherId(id,Long.valueOf(session("USER_LOCATION")));
	    	if(image.thumbPath != null || image.path != null){
	    	if(type.equals("thumbnail")) {
		    	file = new File(rootDir+image.thumbPath);
	    	}
	    	
	    	if(type.equals("full")) {
	    		file = new File(rootDir+image.path);
	    	}
	    	}
	    	return ok(file);
    	}
    }
    
    
    
	   public static Result aboutUsCoverImageById(Long id,String type) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	File file = null;
	    	SiteAboutUs image = SiteAboutUs.findByOtherId(id,Long.valueOf(session("USER_LOCATION")));
	    	if(image.thumbPath != null || image.path != null){
	    	if(type.equals("thumbnail")) {
		    	file = new File(rootDir+image.thumbPath);
	    	}
	    	
	    	if(type.equals("full")) {
	    		file = new File(rootDir+image.path);
	    	}
	    	}
	    	return ok(file);
    	}
    }
    
	
    public static Result getFeaturedImageById(Long id,String type) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	File file = null;
	    	FeaturedImage image = FeaturedImage.findById(id);
	    	if(type.equals("thumbnail")) {
		    	file = new File(rootDir+image.thumbPath);
	    	}
	    	
	    	if(type.equals("full")) {
	    		file = new File(rootDir+image.path);
	    	}
	    	return ok(file);
    	}
    }
   
    
    public static Result deleteAudioFile(Long id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	VehicleAudio audio = VehicleAudio.findById(id);
	    	File file = new File(rootDir+audio.path);
	    	file.delete();
	    	audio.delete();
	    	return ok();
    	}	
    }
    
    private static void reorderSliderImagesForFirstTime(List<SliderImage> imageList) {
    	if(imageList.size() > 0) {
    			for(int i = 0, col = 0 ; i < imageList.size() ; i++) {
    				if(imageList.get(i).row == null) {
	    				imageList.get(i).setRow(  col / 6);
	    				imageList.get(i).setCol( col % 6);
	    				imageList.get(i).update();
    				}
    				col++;
    			}
    			
    		
    	}
		
    }
    
    
    private static void reorderFeaturedImagesForFirstTime(List<FeaturedImage> imageList) {
    	if(imageList.size() > 0) {
    			for(int i = 0, col = 0 ; i < imageList.size() ; i++) {
    				if(imageList.get(i).row == null) {
	    				imageList.get(i).setRow(  col / 6);
	    				imageList.get(i).setCol( col % 6);
	    				imageList.get(i).update();
    				}
    				col++;
    			}
    			
    	}
		
    }
    
    public static Result getImageDataById(Long id) throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		
    		AuthUser user = (AuthUser) getLocalUser();
	    	InventoryImage image = InventoryImage.findById(id);
	    	File file = new File(rootDir+image.path);
	    	
	    	BufferedImage originalImage = ImageIO.read(file);
	    	
	    	ImageVM vm = new ImageVM();
			vm.id = image.id;
			vm.imgName = image.imgName;
			vm.defaultImage = image.defaultImage;
			vm.row = originalImage.getHeight();
			vm.col = originalImage.getWidth();
			vm.path = image.path;
			vm.vin = image.productId;
			VehicleImageConfig config = VehicleImageConfig.findByUser(user);
			vm.width = config.cropWidth;
			vm.height = config.cropHeight;
	    	return ok(Json.toJson(vm));
    	}	
    }
    
		public static Result UpdateName() {
			Form<CreateNewFormVM> form = DynamicForm.form(CreateNewFormVM.class).bindFromRequest();
			CreateNewFormVM vm=form.get();
			Date date = new Date();
			//CreateNewForm newform = CreateNewForm.findByLocation(Long.valueOf(session("USER_LOCATION")));
			CreateNewForm newform = CreateNewForm.findById(vm.id);
			newform.setId(vm.id); 	 
			newform.setName(vm.name);
	    	  // lead.setLocations(vm.id);
	    	  
			newform.update();
	    	  
	    	   return ok();
		}	
		
	
		public static Result getLeadTypeList() {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	AuthUser user = (AuthUser) getLocalUser();
		    	List<LeadType> lead = LeadType.findByLocation(Long.valueOf(session("USER_LOCATION")));
		    	//List<LeadType> lead = LeadType.getLeadData();
		    	
		    	return ok(Json.toJson(lead));
	    	}	
	    }
		
    
    public static Result exportDataAsCSV() throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	CSVWriter writer = new CSVWriter(new FileWriter("vehicleInfo.csv"));
	    	List<Vehicle> vehicleList = Vehicle.getAllVehicles(user);
	    	
	    	for(Vehicle vehicle: vehicleList) {
	    		String []row = new String[22];
	    		row[0] = "ms5421";
	    		row[1] = vehicle.stock;
	    		row[2] = vehicle.year;
	    		row[3] = vehicle.make;
	    		row[4] = vehicle.model;
	    		row[5] = vehicle.trim;
	    		row[6] = vehicle.vin;
	    		row[7] = vehicle.mileage;
	    		row[8] = vehicle.price.toString();
	    		row[9] = vehicle.exteriorColor;
	    		row[10] = vehicle.interiorColor;
	    		row[11] = vehicle.transmission;
	    		row[12] = "";
	    		row[13] = vehicle.description;  //description
	    		row[14] = vehicle.bodyStyle;
	    		row[15] = vehicle.engine;
	    		row[16] = vehicle.drivetrain;
	    		row[17] = vehicle.fuel;
	    		String standardOptions = "";
	    		if(vehicle.activeHeadRestrains != null) {
	    			standardOptions = standardOptions + vehicle.activeHeadRestrains+",";
	    		}
	    		if(vehicle.bodySideReinforcements != null) {
	    			standardOptions = standardOptions + vehicle.bodySideReinforcements+",";
	    		}
	    		if(vehicle.crumpleZones != null) {
	    			standardOptions = standardOptions + vehicle.crumpleZones+",";
	    		}
	    		if(vehicle.impactAbsorbingBumpers != null) {
	    			standardOptions = standardOptions + vehicle.impactAbsorbingBumpers+",";
	    		}
	    		if(vehicle.impactSensor != null) {
	    			standardOptions = standardOptions + vehicle.impactSensor+",";
	    		}
	    		if(vehicle.parkingSensors != null) {
	    			standardOptions = standardOptions + vehicle.parkingSensors+",";
	    		}
	    		if(vehicle.seatbelts != null) {
	    			standardOptions = standardOptions + vehicle.seatbelts+",";
	    		}
	    		if(vehicle.interiorColor != null) {
	    			standardOptions = standardOptions + vehicle.interiorColor+",";
	    		}
	    		if(vehicle.powerOutlet != null) {
	    			standardOptions = standardOptions + vehicle.powerOutlet+",";
	    		}
	    		if(vehicle.powerSteering != null) {
	    			standardOptions = standardOptions + vehicle.powerSteering+",";
	    		}
	    		if(vehicle.rearViewCamera != null) {
	    			standardOptions = standardOptions + vehicle.rearViewCamera+",";
	    		}
	    		if(vehicle.rearViewMonitor != null) {
	    			standardOptions = standardOptions + vehicle.rearViewMonitor+",";
	    		}
	    		if(vehicle.remoteTrunkRelease != null) {
	    			standardOptions = standardOptions + vehicle.remoteTrunkRelease+",";
	    		}
	    		if(vehicle.steeringWheel != null) {
	    			standardOptions = standardOptions + vehicle.steeringWheel+",";
	    		}
	    		if(vehicle.steeringWheelControls != null) {
	    			standardOptions = standardOptions + vehicle.steeringWheelControls;
	    		}
	    		
	    		row[18] = standardOptions;
	    		List<InventoryImage> vImageList = InventoryImage.getByProductId(vehicle.vin);
	    		String str = "";
	    		for(InventoryImage img : vImageList) {
	    			str = str +imageUrlPath+img.path+",";
	    		}
	    		row[19] = str;
	    		VirtualTour vt = VirtualTour.findByUserAndVin(user,vehicle.vin);
	    		if(vt != null) {
	    			row[20] = vt.desktopUrl;
	    			row[21] = vt.mobileUrl;
	    		} else {
	    			row[20] = "";
	    			row[21] = "";
	    		}
	    		writer.writeNext(row);
	    	}
	    	
	    	 writer.close();
	    	 
	    	 File file = new File("vehicleInfo.csv");
	    	response().setContentType("application/csv");
	     	response().setHeader("Content-Disposition", "inline; filename=autoTrader.csv");
	 		return ok(file);
    	}	
    }
    
    
    public static Result exportCarfaxCSV() throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	CSVWriter writer = new CSVWriter(new FileWriter("D:\\vehicleCarfaxInfo.csv"));
	    	List<Vehicle> vehicleList = Vehicle.getAllVehicles(user);
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
			
	    	for(Vehicle vehicle: vehicleList) {
	    		String []row = new String[48];
	    		row[0] = "12345678";
	    		row[1] = vehicle.vin;
	    		row[2] = vehicle.stock;
	    		row[3] = vehicle.year+" "+vehicle.make+" "+vehicle.model;
	    		row[4] = "http://www.domain.com/cars/12345679.html";
	    		row[5] = vehicle.category;
	    		List<InventoryImage> vImageList = InventoryImage.getByProductId(vehicle.vin);
	    		String str = "";
	    		for(InventoryImage img : vImageList) {
	    			str = str +rootDir+img.path+"|";
	    		}
	    		row[6] = str;
	    		row[7] = "1234 Main Street";
	    		row[8] = "San Francisco";
	    		row[9] = "CA";
	    		row[10] = "94105";
	    		row[11] = "United States";
	    		row[12] = "Dealer";
	    		row[13] = "Dealer";
	    		row[14] = "4567";
	    		row[15] = "someone@dealerwebsite.com";
	    		row[16] = "800-123-4567";
	    		row[17] = "dealerwebsite.com";
	    		row[18] = "";
	    		row[19] = vehicle.make;
	    		row[20] = vehicle.model;
	    		row[21] = vehicle.trim;
	    		row[22] = vehicle.bodyStyle;
	    		row[23] = vehicle.mileage;
	    		row[24] = vehicle.year;
	    		row[25] = "USD";
	    		row[26] = vehicle.price.toString();
	    		row[27] = vehicle.getPrice().toString();
	    		row[28] = "";
	    		row[29] = "";
	    		row[30] = "";
	    		row[31] = "";
	    		row[32] = vehicle.exteriorColor;
	    		row[33] = vehicle.interiorColor;
	    		row[34] = "fabric";
	    		row[35] = vehicle.doors;
	    		row[36] = vehicle.cylinders;
	    		row[37] = vehicle.engine;
	    		row[38] = vehicle.drivetrain;
	    		row[39] = vehicle.transmission;
	    		row[40] = "YES";
	    		row[41] = vehicle.description; //description
	    		
	    		String standardFeatures = "";
	    		if(vehicle.drivetrain != null) {
	    			standardFeatures = standardFeatures + vehicle.drivetrain+",";
	    		}
	    		if(vehicle.fuelType != null) {
	    			standardFeatures = standardFeatures + vehicle.fuelType+",";
	    		}
	    		if(vehicle.fuelTank != null) {
	    			standardFeatures = standardFeatures + vehicle.fuelTank+",";
	    		}
	    		if(vehicle.headlights != null) {
	    			standardFeatures = standardFeatures + vehicle.headlights+",";
	    		}
	    		if(vehicle.mirrors != null) {
	    			standardFeatures = standardFeatures + vehicle.mirrors+",";
	    		}
	    		if(vehicle.roof != null) {
	    			standardFeatures = standardFeatures + vehicle.roof+",";
	    		}
	    		if(vehicle.acceleration != null) {
	    			standardFeatures = standardFeatures + vehicle.acceleration+",";
	    		}
	    		if(vehicle.standardSeating != null) {
	    			standardFeatures = standardFeatures + vehicle.standardSeating+",";
	    		}
	    		if(vehicle.engine != null) {
	    			standardFeatures = standardFeatures + vehicle.engine+",";
	    		}
	    		if(vehicle.camType != null) {
	    			standardFeatures = standardFeatures + vehicle.camType+",";
	    		}
	    		if(vehicle.valves != null) {
	    			standardFeatures = standardFeatures + vehicle.valves+",";
	    		}
	    		if(vehicle.cylinders != null) {
	    			standardFeatures = standardFeatures + vehicle.cylinders+",";
	    		}
	    		if(vehicle.fuelQuality != null) {
	    			standardFeatures = standardFeatures + vehicle.fuelQuality+",";
	    		}
	    		if(vehicle.horsePower != null) {
	    			standardFeatures = standardFeatures + vehicle.horsePower+",";
	    		}
	    		if(vehicle.transmission != null) {
	    			standardFeatures = standardFeatures + vehicle.transmission+",";
	    		}
	    		if(vehicle.gears != null) {
	    			standardFeatures = standardFeatures + vehicle.gears+",";
	    		}
	    		if(vehicle.brakes != null) {
	    			standardFeatures = standardFeatures + vehicle.brakes+",";
	    		}
	    		if(vehicle.frontBrakeDiameter != null) {
	    			standardFeatures = standardFeatures + vehicle.frontBrakeDiameter+",";
	    		}
	    		if(vehicle.frontBrakeType != null) {
	    			standardFeatures = standardFeatures + vehicle.frontBrakeType+",";
	    		}
	    		if(vehicle.rearBrakeDiameter != null) {
	    			standardFeatures = standardFeatures + vehicle.rearBrakeDiameter+",";
	    		}
	    		if(vehicle.rearBrakeType != null) {
	    			standardFeatures = standardFeatures + vehicle.rearBrakeType;
	    		}
	    		row[42] = standardFeatures;
	    		
	    		
	    		String standardOptions = "";
	    		
	    		if(vehicle.activeHeadRestrains != null) {
	    			standardOptions = standardOptions + vehicle.activeHeadRestrains+",";
	    		}
	    		if(vehicle.bodySideReinforcements != null) {
	    			standardOptions = standardOptions + vehicle.bodySideReinforcements+",";
	    		}
	    		if(vehicle.crumpleZones != null) {
	    			standardOptions = standardOptions + vehicle.crumpleZones+",";
	    		}
	    		if(vehicle.impactAbsorbingBumpers != null) {
	    			standardOptions = standardOptions + vehicle.impactAbsorbingBumpers+",";
	    		}
	    		if(vehicle.impactSensor != null) {
	    			standardOptions = standardOptions + vehicle.impactSensor+",";
	    		}
	    		if(vehicle.parkingSensors != null) {
	    			standardOptions = standardOptions + vehicle.parkingSensors+",";
	    		}
	    		if(vehicle.seatbelts != null) {
	    			standardOptions = standardOptions + vehicle.seatbelts+",";
	    		}
	    		if(vehicle.interiorColor != null) {
	    			standardOptions = standardOptions + vehicle.interiorColor+",";
	    		}
	    		if(vehicle.powerOutlet != null) {
	    			standardOptions = standardOptions + vehicle.powerOutlet+",";
	    		}
	    		if(vehicle.powerSteering != null) {
	    			standardOptions = standardOptions + vehicle.powerSteering+",";
	    		}
	    		if(vehicle.rearViewCamera != null) {
	    			standardOptions = standardOptions + vehicle.rearViewCamera+",";
	    		}
	    		if(vehicle.rearViewMonitor != null) {
	    			standardOptions = standardOptions + vehicle.rearViewMonitor+",";
	    		}
	    		if(vehicle.remoteTrunkRelease != null) {
	    			standardOptions = standardOptions + vehicle.remoteTrunkRelease+",";
	    		}
	    		if(vehicle.steeringWheel != null) {
	    			standardOptions = standardOptions + vehicle.steeringWheel+",";
	    		}
	    		if(vehicle.steeringWheelControls != null) {
	    			standardOptions = standardOptions + vehicle.steeringWheelControls;
	    		}
	    		
	    		row[43] = standardOptions;
	    		row[44] = "";
	    		row[45] = "Used";
	    		row[46] = "2012-09-16-11:00:00";
	    		row[47] = "2012-10-16-11:00:00";
	    		
	    		writer.writeNext(row);
	    	}
	    	
	    	 writer.close();
	    	return ok();
    	}	
    }
    
    
    public static Result exportCarGurusCSV() throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	CSVWriter writer = new CSVWriter(new FileWriter("D:\\vehicleCarGurusInfo.csv"));
	    	List<Vehicle> vehicleList = Vehicle.getAllVehicles(user);
	    	
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
	    	
	    	for(Vehicle vehicle: vehicleList) {
	    		String []row = new String[29];
	    		row[0] = vehicle.vin;
	    		row[1] = vehicle.make;
	    		row[2] = vehicle.model;
	    		row[3] = vehicle.year;
	    		row[4] = vehicle.trim;
	    		row[5] = vehicle.price.toString();
	    		row[6] = vehicle.mileage;
	    		List<InventoryImage> vImageList = InventoryImage.getByProductId(vehicle.vin);
	    		String str = "";
	    		for(InventoryImage img : vImageList) {
	    			str = str +rootDir+img.path+",";
	    		}
	    		row[7] = str;
	    		row[8] = vehicle.exteriorColor;
	    		row[9] = "";
	    		row[10] = vehicle.stock;
	    		row[11] = vehicle.transmission;
	    		
	    		String standardFeatures = "";
	    		if(vehicle.drivetrain != null) {
	    			standardFeatures = standardFeatures + vehicle.drivetrain+",";
	    		}
	    		if(vehicle.fuelType != null) {
	    			standardFeatures = standardFeatures + vehicle.fuelType+",";
	    		}
	    		if(vehicle.fuelTank != null) {
	    			standardFeatures = standardFeatures + vehicle.fuelTank+",";
	    		}
	    		if(vehicle.headlights != null) {
	    			standardFeatures = standardFeatures + vehicle.headlights+",";
	    		}
	    		if(vehicle.mirrors != null) {
	    			standardFeatures = standardFeatures + vehicle.mirrors+",";
	    		}
	    		if(vehicle.roof != null) {
	    			standardFeatures = standardFeatures + vehicle.roof+",";
	    		}
	    		if(vehicle.acceleration != null) {
	    			standardFeatures = standardFeatures + vehicle.acceleration+",";
	    		}
	    		if(vehicle.standardSeating != null) {
	    			standardFeatures = standardFeatures + vehicle.standardSeating+",";
	    		}
	    		if(vehicle.engine != null) {
	    			standardFeatures = standardFeatures + vehicle.engine+",";
	    		}
	    		if(vehicle.camType != null) {
	    			standardFeatures = standardFeatures + vehicle.camType+",";
	    		}
	    		if(vehicle.valves != null) {
	    			standardFeatures = standardFeatures + vehicle.valves+",";
	    		}
	    		if(vehicle.cylinders != null) {
	    			standardFeatures = standardFeatures + vehicle.cylinders+",";
	    		}
	    		if(vehicle.fuelQuality != null) {
	    			standardFeatures = standardFeatures + vehicle.fuelQuality+",";
	    		}
	    		if(vehicle.horsePower != null) {
	    			standardFeatures = standardFeatures + vehicle.horsePower+",";
	    		}
	    		if(vehicle.transmission != null) {
	    			standardFeatures = standardFeatures + vehicle.transmission+",";
	    		}
	    		if(vehicle.gears != null) {
	    			standardFeatures = standardFeatures + vehicle.gears+",";
	    		}
	    		if(vehicle.brakes != null) {
	    			standardFeatures = standardFeatures + vehicle.brakes+",";
	    		}
	    		if(vehicle.frontBrakeDiameter != null) {
	    			standardFeatures = standardFeatures + vehicle.frontBrakeDiameter+",";
	    		}
	    		if(vehicle.frontBrakeType != null) {
	    			standardFeatures = standardFeatures + vehicle.frontBrakeType+",";
	    		}
	    		if(vehicle.rearBrakeDiameter != null) {
	    			standardFeatures = standardFeatures + vehicle.rearBrakeDiameter+",";
	    		}
	    		if(vehicle.rearBrakeType != null) {
	    			standardFeatures = standardFeatures + vehicle.rearBrakeType;
	    		}
	    		
	    		row[12] = standardFeatures;
	    		row[13] = "1234";
	    		row[14] = "Autolinx Inc";
	    		row[15] = "3300 Sonoma Blvd";
	    		row[16] = "Vallejo";
	    		row[17] = "California";
	    		row[18] = "94590";
	    		row[19] = "info@autolinxinc.com";
	    		row[20] = vehicle.getPrice().toString();
	    		row[21] = vehicle.interiorColor;
	    		row[22] = "";
	    		row[23] = "N";
	    		row[24] = vehicle.engine;
	    		row[25] = "38.120301  -122.254508";
	    		row[26] = "1000";
	    		row[27] = "(707)552-5469";
	    		row[28] = "www.autolinxinc.com/";
	    		
	    		writer.writeNext(row);
	    	}
	    	
	    	 writer.close();
	    	return ok();
    	}	
    }
    
    
    
    public static Result getAllVehical(){
    		List<Vehicle> vehicles = Vehicle.findByNewArrAndLocation(Long.valueOf(session("USER_LOCATION")));
    		return ok(Json.toJson(vehicles));
    }
    
 /*   
    public static Result getAllCompletedLeadsbyId(Integer leadId){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser user;
    		if(leadId == 0){
    			user = getLocalUser();
    		}else{
    			user = AuthUser.findById(leadId);
    		}
	    	
	    	List<ScheduleTest> listData = ScheduleTest.findAllCompletedToUser(user);
	    	List<RequestMoreInfo> requestMoreInfos = RequestMoreInfo.findAllCompletedToUser(user);
	    	List<TradeIn> tradeIns = TradeIn.findAllCompletedToUser(user);
	    	List<RequestInfoVM> infoVMList = new ArrayList<>();
	    	
	    	fillLeadsData(listData, requestMoreInfos, tradeIns, infoVMList);
	    	
	    	return ok(Json.toJson(infoVMList));
    	}	
    }*/
    
   /* public static Result getTestDirConfirById(Integer leadId){
    	
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser user;
    		if(leadId == 0){
    			user = getLocalUser();
    		}else{
    			user = AuthUser.findById(leadId);
    		}
	    	
    		List<ScheduleTest> listData = ScheduleTest.findByConfirmLeads(Long.valueOf(session("USER_LOCATION")), user);
	    	List<RequestMoreInfo> requestMoreInfos = RequestMoreInfo.findByConfirmLeads(Long.valueOf(session("USER_LOCATION")), user);
	    	List<TradeIn> tradeIns = TradeIn.findByConfirmLeads(Long.valueOf(session("USER_LOCATION")), user);
	    	List<RequestInfoVM> infoVMList = new ArrayList<>();
	    	
	    	fillLeadsData(listData, requestMoreInfos, tradeIns, infoVMList);
	    	
	    	
	    	return ok(Json.toJson(infoVMList));
    	}	
    	
    }*/
    
   
	/* public static class infoVMListComparatorCountHigh implements Comparator<RequestInfoVM> {
			@Override
			public int compare(RequestInfoVM o2,RequestInfoVM o1) {
				return o1.confirmDate > o2.confirmDate ? -1 : o1.confirmDate < o2.confirmDate ? 1 : 0;
			}
	    }*/
	
    
    
    public static Result getAllSalesPersonLostAndComp(Integer leadId){
		
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser user;
    		if(leadId == 0){
    			user = getLocalUser();
    		}else{
    			user = AuthUser.findById(leadId);
    		}
	    	
	    	List<ScheduleTest> listData = ScheduleTest.findAllAssignedLeadsToUser(user);
	    	List<RequestMoreInfo> requestMoreInfos = RequestMoreInfo.findAllAssignedLeadsToUser(user);
	    	List<TradeIn> tradeIns = TradeIn.findAllAssignedLeadsToUser(user);
	    	List<RequestInfoVM> infoVMList = new ArrayList<>();
	    	
	    	fillLeadsData(requestMoreInfos, infoVMList);
	    	
	    /*	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    	SimpleDateFormat timedf = new SimpleDateFormat("HH:mm:ss");
	    	Calendar time = Calendar.getInstance();
	    	for(ScheduleTest info: listData) {
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
	    		}
	    		vm.name = info.name;
	    		vm.phone = info.phone;
	    		vm.email = info.email;
	    		vm.bestDay = info.bestDay;
	    		vm.bestTime = info.bestTime;
				vm.howContactedUs = info.contactedFrom;
	    		vm.howFoundUs = info.hearedFrom;
	    		vm.custZipCode = info.custZipCode;
	    		vm.enthicity = info.enthicity;
	    		vm.status = info.leadStatus;
	    		if(info.statusDate != null){
	    			vm.statusDate = df.format(info.statusDate);
	    		}	
	    		vm.typeOfLead = "Schedule Test Drive";
	    		List<UserNotes> notesList = UserNotes.findScheduleTestByUser(info, info.assignedTo);
	    		List<UserNotes> notesList = UserNotes.findScheduleTest(info);
	    		List<NoteVM> list = new ArrayList<>();
	    		for(UserNotes noteObj :notesList) {
	    			NoteVM obj = new NoteVM();
	    			obj.id = noteObj.id;
	    			obj.note = noteObj.note;
	    			obj.action = noteObj.action;
	    			obj.date = df.format(noteObj.createdDate);
	    			obj.time = timedf.format(noteObj.createdTime);
	    			list.add(obj);
	    		}
	    		vm.note = list;
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
	    		vm.option = 0;
	    		infoVMList.add(vm);
	    	}
	    	
	    	for(TradeIn info: tradeIns) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
	    		vm.vin = info.vin;
	    		if(vehicle != null) {
	    			vm.model = vehicle.model;
	    			vm.make = vehicle.make;
	    			vm.stock = vehicle.stock;
	    			vm.mileage = vehicle.mileage;
	    			vm.year = vehicle.year;
					vm.price = vehicle.price;
	    		}
	    		vm.name = info.firstName;
	    		vm.phone = info.phone;
	    		vm.email = info.email;
	    		//vm.bestDay = info.bestDay;
	    		//vm.bestTime = info.bestTime;
				vm.howContactedUs = info.contactedFrom;
	    		vm.howFoundUs = info.hearedFrom;
	    		vm.custZipCode = info.custZipCode;
	    		vm.enthicity = info.enthicity;
	    		vm.status = info.status;
	    		if(info.statusDate != null){
	    			vm.statusDate = df.format(info.statusDate);
	    		}
	    		vm.typeOfLead = "Trade-In Appraisal";
	    		//List<UserNotes> notesList = UserNotes.findTradeInByUser(info, info.assignedTo);
	    		List<UserNotes> notesList = UserNotes.findTradeIn(info);
	    		List<NoteVM> list = new ArrayList<>();
	    		for(UserNotes noteObj :notesList) {
	    			NoteVM obj = new NoteVM();
	    			obj.id = noteObj.id;
	    			obj.note = noteObj.note;
	    			obj.action = noteObj.action;
	    			obj.date = df.format(noteObj.createdDate);
	    			obj.time = timedf.format(noteObj.createdTime);
	    			list.add(obj);
	    		}
	    		vm.note = list;
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
	    		vm.option = 2;
	    		infoVMList.add(vm);
	    	}
	    	
	    	for(RequestMoreInfo info: requestMoreInfos) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
	    		vm.vin = info.vin;
	    		if(vehicle != null) {
	    			vm.model = vehicle.model;
	    			vm.make = vehicle.make;
	    			vm.stock = vehicle.stock;
	    			vm.mileage = vehicle.mileage;
	    			vm.year = vehicle.year;
					vm.price = vehicle.price;
	    		}
	    		vm.name = info.name;
	    		vm.phone = info.phone;
	    		vm.email = info.email;
	    		//vm.bestDay = info.bestDay;
	    		//vm.bestTime = info.bestTime;
				vm.howContactedUs = info.contactedFrom;
	    		vm.howFoundUs = info.hearedFrom;
	    		vm.custZipCode = info.custZipCode;
	    		vm.enthicity = info.enthicity;
	    		vm.status = info.status;
	    		if(info.statusDate != null){
	    			vm.statusDate = df.format(info.statusDate);
	    		}
	    		vm.typeOfLead = "Request More Info";
	    		//List<UserNotes> notesList = UserNotes.findRequestMoreByUser(info, info.assignedTo);
	    		List<UserNotes> notesList = UserNotes.findRequestMore(info);
	    		List<NoteVM> list = new ArrayList<>();
	    		for(UserNotes noteObj :notesList) {
	    			NoteVM obj = new NoteVM();
	    			obj.id = noteObj.id;
	    			obj.note = noteObj.note;
	    			obj.action = noteObj.action;
	    			obj.date = df.format(noteObj.createdDate);
	    			obj.time = timedf.format(noteObj.createdTime);
	    			list.add(obj);
	    		}
	    		vm.note = list;
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
	    		vm.option = 1;
	    		infoVMList.add(vm);
	    	}*/
	    
	    	return ok(Json.toJson(infoVMList));
    	}
    }
    
    
    
    public static void fillLeadsData(List<RequestMoreInfo> requestMoreInfos, List<RequestInfoVM> infoVMList){
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat timedf = new SimpleDateFormat("HH:mm");
    	SimpleDateFormat hourSDF = new SimpleDateFormat("hh:mm a");
    	Calendar time = Calendar.getInstance();
    	
    	 AuthUser user = getLocalUser();
    
  
    	for(RequestMoreInfo info: requestMoreInfos) {
    		RequestInfoVM vm = new RequestInfoVM();
    		vm.id = info.id;
    		if(info.productId != null){
    			AddCollection aProduct = AddCollection.findById(Long.parseLong(info.productId));
    			if(aProduct != null){
    				vm.year = aProduct.year;
    				vm.name = aProduct.title;
    				//vm.price = aProduct.price;
    			}
    		}
    		
    	
    		if(info.user != null){
	    		if(user.id.equals(info.user.id)){
	        		vm.setFlagSameUser = user.id;
	        	}
    		}
    		AuthUser auth = null;
    		vm.name = info.name;
    		vm.phone = info.phone;
    		vm.email = info.email;
    		vm.custZipCode = info.custZipCode;
    		if(info.assignedTo != null){
			 	 auth = AuthUser.findById(info.assignedTo.id);
			}
    		vm.salesRep = auth.firstName;
    		if(info.requestDate != null){
    			vm.requestDate = df.format(info.requestDate);
    		}
    		if(info.bestDay != null){
    			/*String chaArr[] = info.bestDay.split("-");
    			vm.bestDay = chaArr[1]+"/"+chaArr[2]+"/"+chaArr[0];*/
    			vm.bestDay = info.bestDay;
    		}
    		if(info.confirmTime != null){
    			vm.bestTime = hourSDF.format(info.confirmTime);
    		}
			vm.howContactedUs = info.contactedFrom;
    		vm.howFoundUs = info.hearedFrom;
    		vm.custZipCode = info.custZipCode;
    		vm.enthicity = info.enthicity;
    		vm.status =info.status;
    		if(info.statusDate != null){
    			vm.statusDate = df.format(info.statusDate);
    		}
    		vm.typeOfLead = "Request More Info";
    		//List<UserNotes> notesList = UserNotes.findRequestMoreByUser(info, info.assignedTo);
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
    				if(noteObj.saveHistory != null){
        				if(noteObj.saveHistory.equals(1)){
            				nFlag = 1;
            			}
        			}
    			}
    			list.add(obj);
    		}
    		vm.note = list;
    		vm.noteFlag = nFlag;
    		if(info.getConfirmDate() != null) {
    			vm.confirmDate = df.format(info.getConfirmDate());
    			vm.confirmDateOrderBy = info.getConfirmDate();
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
    		vm.option = 1;
    		LeadType lType = LeadType.findById(Long.parseLong(info.isContactusType));
    		if(lType != null){
    			
    				//if(vm.showOnWeb == 1 && vm.callToAction == true){
	    				Application.findCustomeData(info.id,vm,lType.id);
	    			//}
    			
    			
    		}
    		if(vm.customData != null){
    			for(KeyValueDataVM kvd: vm.customData){
        			if(kvd.key.equals("Mt_appointment_type") && kvd.formName.equals("My Leads - Schedule an appointment")){
        				vm.meeting = kvd.value;
        			}
        			
        		}
    		}
    		
    		
    		//findRequestParentChildAndBro(infoVMList, info, df, vm);
    		infoVMList.add(vm);
    	}
    }
   
    
    
	 public static Result exportLeadsData(){
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
    		
    		
    		String FILE_HEADER = "Id,Name,Email,Phone,RequestDate,RequestTime,IsRead,RichNotification,Status,Reason,IsScheduled,ConfirmDate,ConfirmTime,IsReassigned,ContactedFrom,CustZipCode,ScheduleEmail,StatusDate,StatusTime,PremiumFlag,OnlineOrOfflineLeads,IsContactusType,Message,ProductId,Section,PdfPath,NotifFlag,AssignedTo,User,Locations";
    		//PreferredContact ,Vin,BestDay,BestTime,ScheduleDate,LeadStatus,HearedFrom,Enthicity,TestDriveCompletedComment,TestDriveCompletedDuration,ParentId,TestDriveStatus,
    		try {
    			fileWriter = new FileWriter(filePath);
        		fileWriter.append(FILE_HEADER.toString());
        		fileWriter.append(NEW_LINE_SEPARATOR);
        		
        		List<RequestMoreInfo> list = RequestMoreInfo.getAllRequest();
        		AddCollection pro = null;
        		LeadType lead = null;
        		AuthUser auth = null;
        		AuthUser user = null;
        		Location location = null;
        		
        		for (RequestMoreInfo request : list) {
        			//List<CustomizationDataValue> cust = CustomizationDataValue.findByRequestId(request.id);
        			
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
            			fileWriter.append(NEW_LINE_SEPARATOR);
            		}
            		else{
            			fileWriter.append(String.valueOf(""));
            			fileWriter.append(NEW_LINE_SEPARATOR);
            		}
        			
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
	 
	 public static Result exportCsvFileCanceled(){
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
	    		
	    		String FILE_HEADER = "Id,Name,Email,Phone,RequestDate,RequestTime,IsRead,RichNotification,Status,Reason,IsScheduled,ConfirmDate,ConfirmTime,IsReassigned,ContactedFrom,CustZipCode,ScheduleEmail,StatusDate,StatusTime,PremiumFlag,OnlineOrOfflineLeads,IsContactusType,Message,ProductId,Section,PdfPath,NotifFlag,AssignedTo,User,Locations";
	    		//PreferredContact ,Vin,BestDay,BestTime,ScheduleDate,LeadStatus,HearedFrom,Enthicity,TestDriveCompletedComment,TestDriveCompletedDuration,ParentId,TestDriveStatus,
	    		try {

	    			fileWriter = new FileWriter(filePath);
	        		fileWriter.append(FILE_HEADER.toString());
	        		fileWriter.append(NEW_LINE_SEPARATOR);
	        		
	        		List<RequestMoreInfo> list = RequestMoreInfo.findAllCancel(Long.valueOf(session("USER_LOCATION")));
	        		
	        		AddCollection pro = null;
	        		LeadType lead = null;
	        		AuthUser auth = null;
	        		AuthUser user = null;
	        		Location location = null;
	        		
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
	            			fileWriter.append(NEW_LINE_SEPARATOR);
	            		}
	            		else{
	            			fileWriter.append(String.valueOf(""));
	            			fileWriter.append(NEW_LINE_SEPARATOR);
	            		}
	        			
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

	 public static Result exportCsvFileArchive(){
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
	    		
	    		String FILE_HEADER = "Id,Name,Email,Phone,RequestDate,RequestTime,IsRead,RichNotification,Status,Reason,IsScheduled,ConfirmDate,ConfirmTime,IsReassigned,ContactedFrom,CustZipCode,ScheduleEmail,StatusDate,StatusTime,PremiumFlag,OnlineOrOfflineLeads,IsContactusType,Message,ProductId,Section,PdfPath,NotifFlag,AssignedTo,User,Locations";
	    		//PreferredContact ,Vin,BestDay,BestTime,ScheduleDate,LeadStatus,HearedFrom,Enthicity,TestDriveCompletedComment,TestDriveCompletedDuration,ParentId,TestDriveStatus,
	    		try {

	    			fileWriter = new FileWriter(filePath);
	        		fileWriter.append(FILE_HEADER.toString());
	        		fileWriter.append(NEW_LINE_SEPARATOR);
	        		
	        		List<RequestMoreInfo> list = RequestMoreInfo.findAllAssignedLeadsArchive(Long.valueOf(session("USER_LOCATION")));
	        		
	        		AddCollection pro = null;
	        		LeadType lead = null;
	        		AuthUser auth = null;
	        		AuthUser user = null;
	        		Location location = null;
	        		
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
	            			fileWriter.append(NEW_LINE_SEPARATOR);
	            		}
	            		else{
	            			fileWriter.append(String.valueOf(""));
	            			fileWriter.append(NEW_LINE_SEPARATOR);
	            		}
	        			
	        			
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

	 
    public static Result getAllLostAndCompLeads(String id) {
      	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	List<Permission> userPermissions = user.getPermission();
	    	List<RequestMoreInfo> requestMoreInfos = null;
    		for(Permission per: userPermissions) {
    			if(per.name.equals("Only see own leads archive")){
    				//requestMoreInfos = RequestMoreInfo.getAllContactsByUserId(user.id.toString());
    				requestMoreInfos = RequestMoreInfo.getAllContactsByUserId(user);
    			}
    			else if(per.name.equals("Access to the whole leads archive")){
    				if(id.equals("0")){
    					requestMoreInfos = RequestMoreInfo.getAllContactsByAllData();
    				}else{
    						requestMoreInfos = RequestMoreInfo.getAllContactsByUserId(Integer.parseInt(id));
    				}
    				
    			}
    		
    		}
	    	
	    	List<ScheduleTest> listData = ScheduleTest.findAllAssignedLeadsToUser(user);
	    	//List<RequestMoreInfo> requestMoreInfos = RequestMoreInfo.findAllAssignedLeadsToUser(user);
	    	List<TradeIn> tradeIns = TradeIn.findAllAssignedLeadsToUser(user);
	    	List<RequestInfoVM> infoVMList = new ArrayList<>();
	    	
	    	fillLeadsData(requestMoreInfos, infoVMList);
	    	
	    	
	    	/*SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    	SimpleDateFormat timedf = new SimpleDateFormat("HH:mm:ss");
	    	Calendar time = Calendar.getInstance();*/
	    	
	    	
	    	/*for(ScheduleTest info: listData) {
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
	    		}
	    		vm.name = info.name;
	    		vm.phone = info.phone;
	    		vm.email = info.email;
	    		vm.bestDay = info.bestDay;
	    		vm.bestTime = info.bestTime;
				vm.howContactedUs = info.contactedFrom;
	    		vm.howFoundUs = info.hearedFrom;
	    		vm.custZipCode = info.custZipCode;
	    		vm.enthicity = info.enthicity;
	    		vm.status =info.leadStatus;
	    		if(info.statusDate != null){
	    			vm.statusDate = df.format(info.statusDate);
	    		}
	    		vm.typeOfLead = "Schedule Test Drive";
	    		List<UserNotes> notesList = UserNotes.findScheduleTestByUser(info, info.assignedTo);
	    		List<UserNotes> notesList = UserNotes.findScheduleTest(info);
	    		List<NoteVM> list = new ArrayList<>();
	    		for(UserNotes noteObj :notesList) {
	    			NoteVM obj = new NoteVM();
	    			obj.id = noteObj.id;
	    			obj.note = noteObj.note;
	    			obj.action = noteObj.action;
	    			obj.date = df.format(noteObj.createdDate);
	    			obj.time = timedf.format(noteObj.createdTime);
	    			list.add(obj);
	    		}
	    		vm.note = list;
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
	    		vm.option = 0;
	    		infoVMList.add(vm);
	    	}
	    	
	    	for(TradeIn info: tradeIns) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
	    		vm.vin = info.vin;
	    		if(vehicle != null) {
	    			vm.model = vehicle.model;
	    			vm.make = vehicle.make;
	    			vm.stock = vehicle.stock;
	    			vm.mileage = vehicle.mileage;
	    			vm.year = vehicle.year;
					vm.price = vehicle.price;
	    		}
	    		vm.name = info.firstName;
	    		vm.phone = info.phone;
	    		vm.email = info.email;
	    		//vm.bestDay = info.bestDay;
	    		//vm.bestTime = info.bestTime;
				vm.howContactedUs = info.contactedFrom;
	    		vm.howFoundUs = info.hearedFrom;
	    		vm.custZipCode = info.custZipCode;
	    		vm.enthicity = info.enthicity;
	    		vm.status =info.status;
	    		if(info.statusDate != null){
	    			vm.statusDate = df.format(info.statusDate);
	    		}
	    		vm.typeOfLead = "Trade-In Appraisal";
	    		//List<UserNotes> notesList = UserNotes.findTradeInByUser(info, info.assignedTo);
	    		List<UserNotes> notesList = UserNotes.findTradeIn(info);
	    		List<NoteVM> list = new ArrayList<>();
	    		for(UserNotes noteObj :notesList) {
	    			NoteVM obj = new NoteVM();
	    			obj.id = noteObj.id;
	    			obj.note = noteObj.note;
	    			obj.action = noteObj.action;
	    			obj.date = df.format(noteObj.createdDate);
	    			obj.time = timedf.format(noteObj.createdTime);
	    			list.add(obj);
	    		}
	    		vm.note = list;
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
	    		vm.option = 2;
	    		infoVMList.add(vm);
	    	}
	    	
	    	for(RequestMoreInfo info: requestMoreInfos) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
	    		vm.vin = info.vin;
	    		if(vehicle != null) {
	    			vm.model = vehicle.model;
	    			vm.make = vehicle.make;
	    			vm.stock = vehicle.stock;
	    			vm.mileage = vehicle.mileage;
	    			vm.year = vehicle.year;
					vm.price = vehicle.price;
	    		}
	    		vm.name = info.name;
	    		vm.phone = info.phone;
	    		vm.email = info.email;
	    		//vm.bestDay = info.bestDay;
	    		//vm.bestTime = info.bestTime;
				vm.howContactedUs = info.contactedFrom;
	    		vm.howFoundUs = info.hearedFrom;
	    		vm.custZipCode = info.custZipCode;
	    		vm.enthicity = info.enthicity;
	    		vm.status =info.status;
	    		if(info.statusDate != null){
	    			vm.statusDate = df.format(info.statusDate);
	    		}
	    		vm.typeOfLead = "Request More Info";
	    		//List<UserNotes> notesList = UserNotes.findRequestMoreByUser(info, info.assignedTo);
	    		List<UserNotes> notesList = UserNotes.findRequestMore(info);
	    		List<NoteVM> list = new ArrayList<>();
	    		for(UserNotes noteObj :notesList) {
	    			NoteVM obj = new NoteVM();
	    			obj.id = noteObj.id;
	    			obj.note = noteObj.note;
	    			obj.action = noteObj.action;
	    			obj.date = df.format(noteObj.createdDate);
	    			obj.time = timedf.format(noteObj.createdTime);
	    			list.add(obj);
	    		}
	    		vm.note = list;
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
	    		vm.option = 1;
	    		infoVMList.add(vm);
	    	}*/
	    
	    	return ok(Json.toJson(infoVMList));
    	}
    }
    
   /* public static Result getAllContactUsSeen() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	List<RequestMoreInfo> listData = RequestMoreInfo.findAllSeen(user);
	    	List<RequestInfoVM> infoVMList = new ArrayList<>();
	    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    	SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
	    	for(RequestMoreInfo info: listData) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	       		vm.name = info.name;
	    		vm.phone = info.phone;
	    		vm.email = info.email;
	    		vm.howContactedUs = info.contactedFrom;
	    		vm.howFoundUs = info.hearedFrom;
	    		vm.requestDate = df.format(info.requestDate);
	    		if(info.custZipCode != null){
	    			if(!info.custZipCode.equals("null")){
	    				vm.custZipCode = info.custZipCode;
	    			}
	    		}
	    		vm.enthicity = info.enthicity;
	    		vm.typeOfLead = "ContactUs Info";
	    		List<UserNotes> notesList = UserNotes.findRequestMore(info);
	    		List<NoteVM> list = new ArrayList<>();
	    		Integer nFlag = 0;
	    		for(UserNotes noteObj :notesList) {
	    			NoteVM obj = new NoteVM();
	    			obj.id = noteObj.id;
	    			obj.note = noteObj.note;
	    			obj.action = noteObj.action;
	    			obj.date = df.format(noteObj.createdDate);
	    			obj.time = time.format(noteObj.createdTime);
	    			if(noteObj.saveHistory != null){
	    				if(noteObj.saveHistory != null){
	        				if(noteObj.saveHistory.equals(1)){
	            				nFlag = 1;
	            			}
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
	    		
	    		findRequestParentChildAndBro(infoVMList, info, df, vm);
	    	}
	    	
	    	return ok(Json.toJson(infoVMList));
    	}	
    }*/
    
 /*   public static Result getAllRequestInfoSeen() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	List<RequestMoreInfo> listData = RequestMoreInfo.findAllSeen(user);
	    	List<RequestInfoVM> infoVMList = new ArrayList<>();
	    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    	SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
	    	for(RequestMoreInfo info: listData) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
	    		vm.vin = info.vin;
	    		if(vehicle != null) {
	    			vm.typeofVehicle=vehicle.typeofVehicle;
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
	    		vm.requestDate = df.format(info.requestDate);
	    		if(info.custZipCode != null){
	    		if(!info.custZipCode.equals("null")){
	    			vm.custZipCode = info.custZipCode;
	    		}
	    		}
	    		
	    		vm.enthicity = info.enthicity;
	    		vm.typeOfLead = "Request More Info";
	    		//List<UserNotes> notesList = UserNotes.findRequestMoreByUser(info, info.assignedTo);
	    		List<UserNotes> notesList = UserNotes.findRequestMore(info);
	    		List<NoteVM> list = new ArrayList<>();
	    		Integer nFlag = 0;
	    		for(UserNotes noteObj :notesList) {
	    			NoteVM obj = new NoteVM();
	    			obj.id = noteObj.id;
	    			obj.note = noteObj.note;
	    			obj.action = noteObj.action;
	    			obj.date = df.format(noteObj.createdDate);
	    			obj.time = time.format(noteObj.createdTime);
	    			if(noteObj.saveHistory != null){
	    				if(noteObj.saveHistory != null){
	        				if(noteObj.saveHistory.equals(1)){
	            				nFlag = 1;
	            			}
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
	    		
	    		findRequestParentChildAndBro(infoVMList, info, df, vm);
	    	}
	    	
	    	return ok(Json.toJson(infoVMList));
    	}	
    }*/
    
    public static void findSchedulParentChildAndBro(List<RequestInfoVM> infoVMList, ScheduleTest info,SimpleDateFormat df, RequestInfoVM vm){
    	SimpleDateFormat timedf = new SimpleDateFormat("hh:mm a");
    	List<RequestInfoVM> rList2 = new ArrayList<>();
		if(info.parentId != null){
			ScheduleTest sTest = ScheduleTest.findByIdAndParent(info.parentId);
			if(sTest != null){
				RequestInfoVM rList1 = new RequestInfoVM();
				rList1.id = sTest.id;
	    		Vehicle vehicle1 = Vehicle.findByVinAndStatus(sTest.vin);
	    		rList1.vin = sTest.vin;
	    		if(vehicle1 != null) {
	    			rList1.model = vehicle1.model;
	    			rList1.make = vehicle1.make;
	    			rList1.stock = vehicle1.stock;
	    			rList1.year = vehicle1.year;
	    			rList1.mileage = vehicle1.mileage;
	    			rList1.price = vehicle1.price;
	    			rList1.bodyStyle =vehicle1.bodyStyle;
	    			InventoryImage vehicleImage = InventoryImage.getDefaultImage(vehicle1.vin);
	        		if(vehicleImage!=null) {
	        			rList1.imgId = vehicleImage.getId().toString();
	        		}
	        		else {
	        			rList1.imgId = "/assets/images/no-image.jpg";
	        		}
	    			
	    		}
	    		if(sTest.confirmDate != null){
	    			rList1.bestDay = df.format(sTest.confirmDate);
	    		}
	    		if(sTest.confirmTime != null){
	    			rList1.bestTime = timedf.format(sTest.confirmTime);
	    		}
	    		rList1.name = sTest.name;
	    		rList1.phone = sTest.phone;
	    		rList1.email = sTest.email;
	    		rList1.requestDate = df.format(sTest.scheduleDate);
	    		rList1.typeOfLead = "Schedule Test Drive";
	    		
	    		rList2.add(rList1);
			}
		}
		
		if(info.parentId != null){
			List<ScheduleTest> tIns = ScheduleTest.findAllByParentID(info.parentId);
			for(ScheduleTest info1:tIns){
				if(!info.getId().equals(info1.getId())){
				RequestInfoVM rList1 = new RequestInfoVM();
				rList1.id = info1.id;
	    		Vehicle vehicle1 = Vehicle.findByVinAndStatus(info1.vin);
	    		rList1.vin = info1.vin;
	    		if(vehicle1 != null) {
	    			rList1.model = vehicle1.model;
	    			rList1.make = vehicle1.make;
	    			rList1.stock = vehicle1.stock;
	    			rList1.year = vehicle1.year;
	    			rList1.mileage = vehicle1.mileage;
	    			rList1.price = vehicle1.price;
	    			rList1.bodyStyle =vehicle1.bodyStyle;
	    			InventoryImage vehicleImage = InventoryImage.getDefaultImage(vehicle1.vin);
	        		if(vehicleImage!=null) {
	        			rList1.imgId = vehicleImage.getId().toString();
	        		}
	        		else {
	        			rList1.imgId = "/assets/images/no-image.jpg";
	        		}
	    		}
	    		if(info1.confirmDate != null){
	    			rList1.bestDay = df.format(info1.confirmDate);
	    		}
	    		if(info1.confirmTime != null){
	    			rList1.bestTime = timedf.format(info1.confirmTime);
	    		}
	    		rList1.name = info1.name;
	    		rList1.phone = info1.phone;
	    		rList1.email = info1.email;
	    		rList1.requestDate = df.format(info1.scheduleDate);
	    		rList1.typeOfLead = "Schedule Test Drive";
	    		
	    		rList2.add(rList1);
			}
			}
		}
		
		List<ScheduleTest> tIns = ScheduleTest.findAllByParentID(info.getId());
		for(ScheduleTest info1:tIns){
			RequestInfoVM rList1 = new RequestInfoVM();
			rList1.id = info1.id;
    		Vehicle vehicle1 = Vehicle.findByVinAndStatus(info1.vin);
    		rList1.vin = info1.vin;
    		if(vehicle1 != null) {
    			rList1.model = vehicle1.model;
    			rList1.make = vehicle1.make;
    			rList1.stock = vehicle1.stock;
    			rList1.year = vehicle1.year;
    			rList1.mileage = vehicle1.mileage;
    			rList1.price = vehicle1.price;
    			rList1.bodyStyle =vehicle1.bodyStyle;
    			InventoryImage vehicleImage = InventoryImage.getDefaultImage(vehicle1.vin);
        		if(vehicleImage!=null) {
        			rList1.imgId = vehicleImage.getId().toString();
        		}
        		else {
        			rList1.imgId = "/assets/images/no-image.jpg";
        		}
    		}
    		if(info1.confirmDate != null){
    			rList1.bestDay = df.format(info1.confirmDate);
    		}
    		if(info1.confirmTime != null){
    			rList1.bestTime = timedf.format(info1.confirmTime);
    		}
    		
    		rList1.name = info1.name;
    		rList1.phone = info1.phone;
    		rList1.email = info1.email;
    		rList1.requestDate = df.format(info1.scheduleDate);
    		rList1.typeOfLead = "Schedule Test Drive";
    		
    		rList2.add(rList1);
		}
		vm.parentChildLead = rList2;
		
		infoVMList.add(vm);
    	
    }
    
    public static void findTreadParentChildAndBro(List<RequestInfoVM> infoVMList, TradeIn info,SimpleDateFormat df, RequestInfoVM vm){
    	SimpleDateFormat timedf = new SimpleDateFormat("hh:mm a");
    	List<RequestInfoVM> rList2 = new ArrayList<>();
		if(info.parentId != null){
			TradeIn tIn = TradeIn.findByIdAndParent(info.parentId);
			if(tIn != null){
				RequestInfoVM rList1 = new RequestInfoVM();
				rList1.id = tIn.id;
	    		Vehicle vehicle1 = Vehicle.findByVinAndStatus(tIn.vin);
	    		rList1.vin = tIn.vin;
	    		if(vehicle1 != null) {
	    			rList1.model = vehicle1.model;
	    			rList1.make = vehicle1.make;
	    			rList1.stock = vehicle1.stock;
	    			rList1.year = vehicle1.year;
	    			rList1.mileage = vehicle1.mileage;
	    			rList1.price = vehicle1.price;
	    			rList1.bodyStyle =vehicle1.bodyStyle;
	    			InventoryImage vehicleImage = InventoryImage.getDefaultImage(vehicle1.vin);
	        		if(vehicleImage!=null) {
	        			rList1.imgId = vehicleImage.getId().toString();
	        		}
	        		else {
	        			rList1.imgId = "/assets/images/no-image.jpg";
	        		}
	    			
	    		}
	    		if(tIn.confirmDate != null){
	    			rList1.bestDay = df.format(tIn.confirmDate);
	    		}
	    		if(tIn.confirmTime != null){
	    			rList1.bestTime = timedf.format(tIn.confirmTime);
	    		}
	    		rList1.name = tIn.firstName;
	    		rList1.phone = tIn.phone;
	    		rList1.email = tIn.email;
	    		rList1.requestDate = df.format(tIn.tradeDate);
	    		rList1.typeOfLead = "Trade-In Appraisal";
	    		
	    		rList2.add(rList1);
			}
		}
		
		if(info.parentId !=null){
			
			List<TradeIn> tIns = TradeIn.findAllByParentID(info.parentId);
			for(TradeIn info1:tIns){
				if(!info.getId().equals(info1.getId())){
				RequestInfoVM rList1 = new RequestInfoVM();
				rList1.id = info1.id;
	    		Vehicle vehicle1 = Vehicle.findByVinAndStatus(info1.vin);
	    		rList1.vin = info1.vin;
	    		if(vehicle1 != null) {
	    			rList1.model = vehicle1.model;
	    			rList1.make = vehicle1.make;
	    			rList1.stock = vehicle1.stock;
	    			rList1.year = vehicle1.year;
	    			rList1.mileage = vehicle1.mileage;
	    			rList1.price = vehicle1.price;
	    			rList1.bodyStyle =vehicle1.bodyStyle;
	    			InventoryImage vehicleImage = InventoryImage.getDefaultImage(vehicle1.vin);
	        		if(vehicleImage!=null) {
	        			rList1.imgId = vehicleImage.getId().toString();
	        		}
	        		else {
	        			rList1.imgId = "/assets/images/no-image.jpg";
	        		}
	    		}
	    		if(info1.confirmDate != null){
	    			rList1.bestDay = df.format(info1.confirmDate);
	    		}
	    		if(info1.confirmTime != null){
	    			rList1.bestTime = timedf.format(info1.confirmTime);
	    		}
	    		rList1.name = info1.firstName;
	    		rList1.phone = info1.phone;
	    		rList1.email = info1.email;
	    		rList1.requestDate = df.format(info1.tradeDate);
	    		rList1.typeOfLead = "Trade-In Appraisal";
	    		
	    		rList2.add(rList1);
			}
			}
	    }
		
		
		List<TradeIn> tIns = TradeIn.findAllByParentID(info.getId());
		for(TradeIn info1:tIns){
			RequestInfoVM rList1 = new RequestInfoVM();
			rList1.id = info1.id;
    		Vehicle vehicle1 = Vehicle.findByVinAndStatus(info1.vin);
    		rList1.vin = info1.vin;
    		if(vehicle1 != null) {
    			rList1.model = vehicle1.model;
    			rList1.make = vehicle1.make;
    			rList1.stock = vehicle1.stock;
    			rList1.year = vehicle1.year;
    			rList1.mileage = vehicle1.mileage;
    			rList1.price = vehicle1.price;
    			rList1.bodyStyle =vehicle1.bodyStyle;
    			InventoryImage vehicleImage = InventoryImage.getDefaultImage(vehicle1.vin);
        		if(vehicleImage!=null) {
        			rList1.imgId = vehicleImage.getId().toString();
        		}
        		else {
        			rList1.imgId = "/assets/images/no-image.jpg";
        		}
    		}
    		if(info1.confirmDate != null){
    			rList1.bestDay = df.format(info1.confirmDate);
    		}
    		if(info1.confirmTime != null){
    			rList1.bestTime = timedf.format(info1.confirmTime);
    		}
    		rList1.name = info1.firstName;
    		rList1.phone = info1.phone;
    		rList1.email = info1.email;
    		rList1.requestDate = df.format(info1.tradeDate);
    		rList1.typeOfLead = "Trade-In Appraisal";
    		
    		rList2.add(rList1);
		}
		vm.parentChildLead = rList2;
		
		infoVMList.add(vm);
    }
    
    
    public static void findCustomeData(Long id,RequestInfoVM vm,Long leadType){
    	List<CustomizationDataValue> custData = CustomizationDataValue.findByCustomeLead(leadType, id);
    	List<KeyValueDataVM> keyValueList = new ArrayList<>();
    	Map<String, String> mapCar = new HashMap<String, String>();
    	for(CustomizationDataValue custD:custData){
    		mapCar.put(custD.keyValue, custD.value);
    		
    			if(!custD.formName.equals("Create New Lead")){
    				if(custD.displayGrid.equals("true")){
    	    			KeyValueDataVM keyValue = new KeyValueDataVM();
    	            	keyValue.key = custD.keyValue;
    	            	keyValue.value = custD.value;
    	            	keyValue.displayGrid = custD.displayGrid;
    	            	keyValue.formName = custD.formName;
    	            	keyValueList.add(keyValue);
    	    		}
    			}
    		
	    		
	    		if(custD.formName != null){
	    			if(custD.formName.equals("Create New Lead")){
	    				KeyValueDataVM keyValue2 = new KeyValueDataVM();
	            		keyValue2.key = custD.keyValue;
	            		keyValue2.value = custD.value;
	            		keyValue2.displayGrid = custD.displayGrid;
	            		keyValue2.formName = custD.formName;
	            		keyValueList.add(keyValue2);
	    			
		    		}
	    		}
	    		
    		
    	}
    	
    	vm.customData = keyValueList;
    	vm.customMapData = mapCar;
    	
    	
    	List<CustomizationDataValue> custDataAll = CustomizationDataValue.findByCustomeLead(leadType, id);
    	List<KeyValueDataVM> keyValueListAll = new ArrayList<>();
    	Map<String, String> mapCarAll = new HashMap<String, String>();
    	for(CustomizationDataValue custDat:custDataAll){
    		mapCar.put(custDat.keyValue, custDat.value);
    		if( custDat.formName != null){
	    		if(custDat.formName.equals("Create New Lead")){
	    				KeyValueDataVM keyValue1 = new KeyValueDataVM();
	            		keyValue1.key = custDat.keyValue;
	            		keyValue1.value = custDat.value;
	            		keyValue1.displayGrid = custDat.displayGrid;
	            		keyValue1.formName = custDat.formName;
	            		keyValueListAll.add(keyValue1);
	    			
	    		}
    		}
    		
    	}
    	
    	vm.customDataAll = keyValueListAll;
    	
    }

    
    public static void findCustomeDataRequest(Long id,RequestInfoVM vm,Long leadType){
    	List<CustomizationDataValue> custData = CustomizationDataValue.findByCustomeLead(leadType, id);
    	List<KeyValueDataVM> keyValueList = new ArrayList<>();
    	Map<String, String> mapCar = new HashMap<String, String>();
    	for(CustomizationDataValue custD:custData){
    		mapCar.put(custD.keyValue, custD.value);
    		
    			if(!custD.formName.equals("Create New Lead")){
    				if(custD.displayGrid.equals("true")){
    	    			KeyValueDataVM keyValue = new KeyValueDataVM();
    	            	keyValue.key = custD.keyValue;
    	            	keyValue.value = custD.value;
    	            	keyValue.displayGrid = custD.displayGrid;
    	            	keyValue.formName = custD.formName;
    	            	keyValueList.add(keyValue);
    	    		}
    			}
    		
    	}
    	
    	vm.customData = keyValueList;
    	vm.customMapData = mapCar;
    	
    	
    }
    
    public static void findRequestParentChildAndBro(List<RequestInfoVM> infoVMList, RequestMoreInfo info,SimpleDateFormat df, RequestInfoVM vm){
    	SimpleDateFormat timedf = new SimpleDateFormat("hh:mm a");
    	List<RequestInfoVM> rList2 = new ArrayList<>();
		if(info.parentId != null){
			
			RequestMoreInfo rMoreInfo = RequestMoreInfo.findByIdAndParent(info.parentId);
			if(rMoreInfo != null){
				
				RequestInfoVM rList1 = new RequestInfoVM();
				rList1.id = rMoreInfo.id;
				Inventory productInfo = Inventory.getByProductId(rMoreInfo.productId);
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
	    		if(rMoreInfo.confirmDate != null){
	    			rList1.bestDay = df.format(rMoreInfo.confirmDate);
	    		}
	    		if(rMoreInfo.confirmTime != null){
	    			rList1.bestTime = timedf.format(rMoreInfo.confirmTime);
	    		}
	    		rList1.name = rMoreInfo.name;
	    		rList1.phone = rMoreInfo.phone;
	    		rList1.email = rMoreInfo.email;
	    		rList1.requestDate = df.format(rMoreInfo.requestDate);
	    		rList1.typeOfLead = "Request More Info";
	    		
	    		rList2.add(rList1);
			}
		}
		
		if(info.parentId !=null){
			
		List<RequestMoreInfo> rMoreInfo1 = RequestMoreInfo.findAllByParentID(info.parentId);
		for(RequestMoreInfo info1:rMoreInfo1){
			if(!info1.getId().equals(info.getId())){
			RequestInfoVM rList1 = new RequestInfoVM();
			rList1.id = info1.id;
			Inventory productInfo = Inventory.getByProductId(info1.productId);
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
    		if(info1.confirmDate != null){
    			rList1.bestDay = df.format(info1.confirmDate);
    		}
    		if(info1.confirmTime != null){
    			rList1.bestTime = timedf.format(info1.confirmTime);
    		}
    		rList1.name = info1.name;
    		rList1.phone = info1.phone;
    		rList1.email = info1.email;
    		rList1.requestDate = df.format(info1.requestDate);
    		rList1.typeOfLead = "Request More Info";
    		
    		rList2.add(rList1);
		 }
		}
    }
		
		List<RequestMoreInfo> requestMoreInfo = RequestMoreInfo.findAllByParentID(info.getId());
		for(RequestMoreInfo info1:requestMoreInfo){
			RequestInfoVM rList1 = new RequestInfoVM();
			rList1.id = info1.id;
			Inventory productInfo = Inventory.getByProductId(info1.productId);
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
    		
    		if(info1.confirmDate != null){
    			rList1.bestDay = df.format(info1.confirmDate);
    		}
    		if(info1.confirmTime != null){
    			rList1.bestTime = timedf.format(info1.confirmTime);
    		}
    		rList1.name = info1.name;
    		rList1.phone = info1.phone;
    		rList1.email = info1.email;
    		rList1.requestDate = df.format(info1.requestDate);
    		rList1.typeOfLead = "Request More Info";
    		
    		rList2.add(rList1);
		}
		vm.parentChildLead = rList2;
		infoVMList.add(vm);
    }
    
    
    
    public static Result getAllScheduleTestAssigned() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	
	    	List<ScheduleTest> listData = ScheduleTest.findAllAssigned(user);
	    	List<RequestMoreInfo> requestMoreInfos = RequestMoreInfo.findAllScheduledUser(user);
	    	List<TradeIn> tradeIns = TradeIn.findAllScheduledUser(user);
	    	List<RequestInfoVM> infoVMList = new ArrayList<>();
	    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    	SimpleDateFormat timedf = new SimpleDateFormat("HH:mm:ss");
	    	Calendar time = Calendar.getInstance();
	    	/*for(ScheduleTest info: listData) {
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
	    			VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle.vin);
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
	    			String chaArr[] = info.bestDay.split("-");
	    			vm.bestDay = chaArr[2]+"-"+chaArr[1]+"-"+chaArr[0];
	    		}	
	    		vm.bestTime = info.bestTime;
				vm.howContactedUs = info.contactedFrom;
	    		vm.howFoundUs = info.hearedFrom;
	    		vm.custZipCode = info.custZipCode;
	    		vm.enthicity = info.enthicity;
	    		vm.typeOfLead = "Schedule Test Drive";
	    		List<UserNotes> notesList = UserNotes.findScheduleTest(info);
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
	    		vm.option = 0;
	    		
	    		findSchedulParentChildAndBro(infoVMList, info, df, vm);
	    		
	    	}*/
	    	
	    	for(TradeIn info: tradeIns) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
	    		vm.vin = info.vin;
	    		if(vehicle != null) {
	    			vm.model = vehicle.model;
	    			vm.make = vehicle.make;
	    			vm.stock = vehicle.stock;
	    			vm.mileage = vehicle.mileage;
	    			vm.year = vehicle.year;
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
	    		vm.name = info.firstName;
	    		vm.phone = info.phone;
	    		vm.email = info.email;
	    		if(info.bestDay != null){
	    			//vm.bestDay = chaArr[1]+"/"+chaArr[2]+"/"+chaArr[0];*/
	    			vm.bestDay = info.bestDay;
	    		}
	    		
	    		vm.bestTime = info.bestTime;
				vm.howContactedUs = info.contactedFrom;
	    		vm.howFoundUs = info.hearedFrom;
	    		vm.custZipCode = info.custZipCode;
	    		vm.pdfPath = info.pdfPath;
	    		vm.enthicity = info.enthicity;
	    		LeadVM lVm = new LeadVM();
	    		lVm.id = info.id.toString();
	    		
	    			lVm.comments = info.comments;
	    		if(info.year != null){
	    			if(!info.year.equals("null")){
		    			lVm.year = info.year;
		    		}
	    		}
	    		
	    		/*if(info.make != null){
	    			lVm.make = info.make;
	    		}
	    		if(info.model != null){
	    			lVm.model = info.model;
	    		}
	    		
	    		if(info.exteriorColour != null){
	    			if(!info.exteriorColour.equals("null")){
		    			lVm.exteriorColour = info.exteriorColour;
		    		}
	    		}
	    		
	    		if(info.kilometres != null){
	    			if(!info.kilometres.equals("null")){
		    			lVm.kilometres = info.kilometres;
		    		}
	    		}
	    		
	    		if(info.year != null){
	    			if(!info.engine.equals("null")){
	    				lVm.engine = info.engine;
	    			}
	    		}
	    		if(info.doors != null){
		    		if(!info.doors.equals("null")){
		    			lVm.doors = info.doors;
		    		}
	    		}
	    		if(info.transmission != null){
		    		if(!info.transmission.equals("null")){
		    			lVm.transmission = info.transmission;
		    		}
	    		}
	    		if(info.drivetrain != null){
		    		if(!info.drivetrain.equals("null")){
		    			lVm.drivetrain = info.drivetrain;
		    		}
	    		}
	    		if(info.bodyRating != null){
		    		if(!info.bodyRating.equals("null")){
		    			lVm.bodyRating = info.bodyRating;
		    		}
	    		}
	    		
	    		if(info.tireRating != null){
		    		if(!info.tireRating.equals("null")){
		    			lVm.tireRating = info.tireRating;
		    		}
	    		}
	    		
	    		if(info.engineRating != null){
		    		if(!info.engineRating.equals("null")){
		    			lVm.engineRating = info.engineRating;
		    		}
	    		}*/
	    			//lVm.glassRating = info.glassRating;
	    			//lVm.interiorRating = info.interiorRating;
	    			//lVm.exhaustRating = info.exhaustRating;
	    			lVm.rentalReturn = info.leaseOrRental;
	    			lVm.odometerAccurate = info.operationalAndAccurate;
	    			lVm.serviceRecords = info.serviceRecord;
	    			lVm.lienholder = info.lienholder;
	    			lVm.prefferedContact = info.preferredContact;
	    			lVm.equipment = info.equipment;
	    			//lVm.accidents = info.accidents;
	    			//lVm.vehiclenew = info.vehiclenew;
	    			lVm.paint = info.paint;
	    			lVm.salvage = info.salvage;
	    			lVm.damage = info.damage;
	    			lVm.titleholder = info.holdsThisTitle;
	    			lVm.prefferedContact = info.preferredContact;
	    			
	    			
	    			List<String> sList = new ArrayList<>();
	    			String arr[] =  info.optionValue.split(",");
	    			for(int i=0;i<arr.length;i++){
	    				sList.add(arr[i]);
	    			}
	    			lVm.options = sList;
	    			
	    		vm.leadsValue = lVm;	
	    		vm.typeOfLead = "Trade-In Appraisal";
	    		List<UserNotes> notesList = UserNotes.findTradeIn(info);
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
	    		vm.option = 2;
	    		
	    		findTreadParentChildAndBro(infoVMList, info, df, vm);
	    		
	    		
	    	}
	    	
	    	/*for(RequestMoreInfo info: requestMoreInfos) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
	    		vm.vin = info.vin;
	    		if(vehicle != null) {
	    			vm.model = vehicle.model;
	    			vm.make = vehicle.make;
	    			vm.stock = vehicle.stock;
	    			vm.mileage = vehicle.mileage;
	    			vm.year = vehicle.year;
					vm.price = vehicle.price;
					
					vm.bodyStyle =vehicle.bodyStyle;
	    			vm.drivetrain = vehicle.drivetrain;
	    			vm.engine = vehicle.engine;
	    			vm.transmission = vehicle.transmission;
	    			VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle.vin);
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
	    			vm.bestDay = info.bestDay;
	    		}
	    		vm.bestTime = info.bestTime;
				vm.howContactedUs = info.contactedFrom;
	    		vm.howFoundUs = info.hearedFrom;
	    		vm.custZipCode = info.custZipCode;
	    		vm.enthicity = info.enthicity;
	    		vm.typeOfLead = "Request More Info";
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
	    		vm.option = 1;
	    		
	    		findRequestParentChildAndBro(infoVMList, info, df, vm);
	    		
	    	}*/
	    	
	    	return ok(Json.toJson(infoVMList));
    	}	
    }
    
    /*public static Result getAllTradeInSeen() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	List<TradeIn> listData = TradeIn.findAllSeen(user);
	    	List<RequestInfoVM> infoVMList = new ArrayList<>();
	    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    	SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
	    	for(TradeIn info: listData) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
	    		vm.vin = info.vin;
	    		if(vehicle != null) {
	    			vm.typeofVehicle=vehicle.typeofVehicle;
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
	    			VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle.vin);
	        		if(vehicleImage!=null) {
	        			vm.imgId = vehicleImage.getId().toString();
	        		}
	        		else {
	        			vm.imgId = "/assets/images/no-image.jpg";
	        		}
	    		}
	    		vm.name = (info.firstName!=null?info.firstName:"")+" "+(info.lastName!=null?info.lastName:"");
	    		vm.phone = info.phone!=null ? info.phone:"";
	    		vm.email = info.email!=null ? info.email:"";
	    		vm.howContactedUs = info.contactedFrom;
	    		vm.howFoundUs = info.hearedFrom;
	    		vm.custZipCode = info.custZipCode;
	    		vm.enthicity = info.enthicity;
	    		vm.pdfPath = info.pdfPath;
	    		vm.requestDate = df.format(info.tradeDate);
	    		vm.typeOfLead = "Trade-In Appraisal";
	    		
	    		LeadVM lVm = new LeadVM();
	    		lVm.id = info.id.toString();
    			lVm.comments = info.comments;
    		if(info.year != null){
    			if(!info.year.equals("null")){
	    			lVm.year = info.year;
	    		}
    		}
    		
    		if(info.make != null){
    			lVm.make = info.make;
    		}
    		if(info.model != null){
    			lVm.model = info.model;
    		}
    		
    		if(info.exteriorColour != null){
    			if(!info.exteriorColour.equals("null")){
	    			lVm.exteriorColour = info.exteriorColour;
	    		}
    		}
    		
    		if(info.kilometres != null){
    			if(!info.kilometres.equals("null")){
	    			lVm.kilometres = info.kilometres;
	    		}
    		}
    		
    		if(info.year != null){
    			if(!info.engine.equals("null")){
    				lVm.engine = info.engine;
    			}
    		}
    		if(info.doors != null){
	    		if(!info.doors.equals("null")){
	    			lVm.doors = info.doors;
	    		}
    		}
    		if(info.transmission != null){
	    		if(!info.transmission.equals("null")){
	    			lVm.transmission = info.transmission;
	    		}
    		}
    		if(info.drivetrain != null){
	    		if(!info.drivetrain.equals("null")){
	    			lVm.drivetrain = info.drivetrain;
	    		}
    		}
    		if(info.bodyRating != null){
	    		if(!info.bodyRating.equals("null")){
	    			lVm.bodyRating = info.bodyRating;
	    		}
    		}
    		
    		if(info.tireRating != null){
	    		if(!info.tireRating.equals("null")){
	    			lVm.tireRating = info.tireRating;
	    		}
    		}
    		
    		if(info.engineRating != null){
	    		if(!info.engineRating.equals("null")){
	    			lVm.engineRating = info.engineRating;
	    		}
    		}
    			lVm.glassRating = info.glassRating;
    			lVm.interiorRating = info.interiorRating;
    			lVm.exhaustRating = info.exhaustRating;
    			lVm.rentalReturn = info.leaseOrRental;
    			lVm.odometerAccurate = info.operationalAndAccurate;
    			lVm.serviceRecords = info.serviceRecord;
    			lVm.lienholder = info.lienholder;
    			lVm.prefferedContact = info.preferredContact;
    			lVm.equipment = info.equipment;
    			lVm.accidents = info.accidents;
    			lVm.vehiclenew = info.vehiclenew;
    			lVm.paint = info.paint;
    			lVm.salvage = info.salvage;
    			lVm.damage = info.damage;
	    			lVm.titleholder = info.holdsThisTitle;
	    			lVm.prefferedContact = info.preferredContact;
    			List<String> sList = new ArrayList<>();
    			String arr[] =  info.optionValue.split(",");
    			for(int i=0;i<arr.length;i++){
    				sList.add(arr[i]);
    			}
    			lVm.options = sList;
    			
    		vm.leadsValue = lVm;
	    		
	    		List<UserNotes> notesList = UserNotes.findTradeIn(info);
	    		Integer nFlag = 0;
	    		List<NoteVM> list = new ArrayList<>();
	    		for(UserNotes noteObj :notesList) {
	    			NoteVM obj = new NoteVM();
	    			obj.id = noteObj.id;
	    			obj.note = noteObj.note;
	    			obj.action = noteObj.action;
	    			obj.date = df.format(noteObj.createdDate);
	    			obj.time = time.format(noteObj.createdTime);
	    			if(noteObj.saveHistory != null){
	    				if(noteObj.saveHistory.equals(1)){
	        				nFlag = 1;
	        			}
	    			}
	    			list.add(obj);
	    		}
	    		vm.note = list;
	    		vm.noteFlag = nFlag;
	    		vm.requestDate = df.format(info.tradeDate);
	    		if(info.isRead == 0) {
	    			vm.isRead = false;
	    		}
	    		
	    		if(info.isRead == 1) {
	    			vm.isRead = true;
	    		}
	    		
	    		findTreadParentChildAndBro(infoVMList, info, df, vm);
	    	}
	    	
	    	return ok(Json.toJson(infoVMList));
    	}	
    }*/
    
    public static Result getInfoCount() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser user = (AuthUser) getLocalUser();
	    	InfoCountVM vm = new InfoCountVM();
	    	vm.schedule = ScheduleTest.findAll(Long.valueOf(session("USER_LOCATION")));
	    	vm.trade = TradeIn.findAll(Long.valueOf(session("USER_LOCATION")));
	    	vm.req = RequestMoreInfo.findAll(Long.valueOf(session("USER_LOCATION")));
	    	if(user.location != null){
	    		Location location=Location.findById(user.location.id);
		    	vm.locationName=location.name;
	    	}
	    	
	    	List<ScheduleTest> sched = ScheduleTest.findAllLocationDataManagerPremium(Long.valueOf(session("USER_LOCATION")));
	    	List<RequestMoreInfo> reInfos = RequestMoreInfo.findAllLocationDataManagerPremium(Long.valueOf(session("USER_LOCATION")));
	    	List<TradeIn> tradeIns = TradeIn.findAllLocationDataManagerPremium(Long.valueOf(session("USER_LOCATION")));

	    	int premi = sched.size() + reInfos.size() + tradeIns.size();
	    	vm.premium = premi;
	    	
	    	vm.userType = user.role;
	    	
	    	return ok(Json.toJson(vm));
    	}
    }
    
    public static Result  getLocationName(Long locationId)     
    {
    	Location location=Location.findById(locationId);
		return ok(location.getName());
    	
    }
    
    public static Result uploadLogoFile() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	MultipartFormData body = request().body().asMultipartFormData();
	    	
	    	AuthUser userObj = (AuthUser) getLocalUser();
	    	
	    	FilePart picture = body.getFile("file0");
	    	  if (picture != null) {
	    	    String fileName = picture.getFilename();
	    	    File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+"logo");
	    	    if(!fdir.exists()) {
	    	    	fdir.mkdir();
	    	    }
	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"logo"+File.separator+fileName;
	    	    File file = picture.getFile();
	    	    try {
	    	    		FileUtils.moveFile(file, new File(filePath));
	    	    		
	    	    		SiteLogo logoObj = SiteLogo.findByLocation(Long.valueOf(session("USER_LOCATION")));
	    	    		
	    	    		if(logoObj == null) {
		    	    		SiteLogo logo = new SiteLogo();
		    	    		logo.logoImagePath = "/"+session("USER_LOCATION")+"/"+"logo"+"/"+fileName;
		    	    		logo.logoImageName = fileName;
		    	    		logo.user = userObj;
							logo.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    	    		logo.save();
	    	    		} else {
	    	    			File logoFile = new File(rootDir+File.separator+logoObj.locations.id+File.separator+"logo"+File.separator+logoObj.logoImageName);
	    	    			logoFile.delete();
	    	    			logoObj.setLogoImageName(fileName);
	    	    			logoObj.setLogoImagePath("/"+session("USER_LOCATION")+"/"+"logo"+"/"+fileName);
	    	    			logoObj.update();
	    	    		}
	    	  } catch (FileNotFoundException e) {
	  			e.printStackTrace();
		  		} catch (IOException e) {
		  			e.printStackTrace();
		  		} 
	    	  } 
	    	return ok();
    	}	
    }
    
    
    public static Result uploadFeviconFile() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	MultipartFormData body = request().body().asMultipartFormData();
	    	
	    	AuthUser userObj = (AuthUser) getLocalUser();
	    	
	    	FilePart picture = body.getFile("file0");
	    	  if (picture != null) {
	    	    String fileName = picture.getFilename();
	    	    File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+"fevicon");
	    	    if(!fdir.exists()) {
	    	    	fdir.mkdir();
	    	    }
	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"fevicon"+File.separator+fileName;
	    	    File file = picture.getFile();
	    	    try {
	    	    		FileUtils.moveFile(file, new File(filePath));
	    	    		
	    	    		//SiteLogo logoObj = SiteLogo.findByUser(userObj);
	    	    		SiteLogo logoObj = SiteLogo.findByLocation(Long.valueOf(session("USER_LOCATION")));
	    	    		
	    	    		if(logoObj == null) {
		    	    		SiteLogo logo = new SiteLogo();
		    	    		logo.faviconImagePath = "/"+session("USER_LOCATION")+"/"+"fevicon"+"/"+fileName;
		    	    		logo.faviconImageName = fileName;
		    	    		logo.user = userObj;
							logo.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    	    		logo.save();
	    	    		} else {
	    	    			File feviconFile = new File(rootDir+File.separator+logoObj.locations.id+File.separator+"fevicon"+File.separator+logoObj.faviconImageName);
	    	    			feviconFile.delete();
	    	    			logoObj.setFaviconImageName(fileName);
	    	    			logoObj.setFaviconImagePath("/"+session("USER_LOCATION")+"/"+"fevicon"+"/"+fileName);
	    	    			logoObj.update();
	    	    		}
	    	  } catch (FileNotFoundException e) {
	  			e.printStackTrace();
		  		} catch (IOException e) {
		  			e.printStackTrace();
		  		} 
	    	  } 
	    	return ok();
    	}	
    }
   

    public static Result getTradeInDataById(Long id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	TradeIn tradeIn = TradeIn.findById(id);
	    	TradeInVM vm = new TradeInVM();
	    	vm.firstName = tradeIn.firstName;
	    	vm.lastName = tradeIn.lastName;
	    	vm.workPhone = tradeIn.workPhone;
	    	vm.phone = tradeIn.phone;
	    	vm.email = tradeIn.email;
	    	vm.preferredContact = tradeIn.preferredContact;
	    	vm.comments = tradeIn.comments;
	    	vm.year = tradeIn.year;
	    	vm.make = tradeIn.make;
	    	vm.model = tradeIn.model;
	    	vm.exteriorColour = tradeIn.exteriorColour;
	    	vm.kilometres = tradeIn.kilometres;
	    	vm.engine = tradeIn.engine;
	    	vm.doors = tradeIn.doors;
	    	vm.transmission = tradeIn.transmission;
	    	vm.drivetrain = tradeIn.drivetrain;
	    	vm.bodyRating = tradeIn.bodyRating;
	    	vm.tireRating = tradeIn.tireRating;
	    	vm.engineRating = tradeIn.engineRating;
	    	vm.transmissionRating = tradeIn.transmissionRating;
	    	vm.glassRating = tradeIn.glassRating;
	    	vm.interiorRating = tradeIn.interiorRating;
	    	vm.exhaustRating = tradeIn.exhaustRating;
	    	vm.leaseOrRental = tradeIn.leaseOrRental;
	    	vm.operationalAndAccurate = tradeIn.operationalAndAccurate;
	    	vm.serviceRecord = tradeIn.serviceRecord;
	    	vm.lienHolder = tradeIn.lienholder;
	    	vm.holdsThisTitle = tradeIn.holdsThisTitle;
	    	vm.equipment = tradeIn.equipment;
	    	vm.vehiclenew = tradeIn.vehiclenew;
	    	vm.accidents = tradeIn.accidents;
	    	vm.damage = tradeIn.damage;
	    	vm.paint = tradeIn.paint;
	    	vm.salvage = tradeIn.salvage;
	    	vm.optionValue = tradeIn.optionValue;
	    	vm.vin = tradeIn.vin;
	    	return ok(Json.toJson(vm));
    	}	
    }
    
    public static Result getReminderPopup(){
    	
    	SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
   	 DateFormat df1 = new SimpleDateFormat("MM-dd-yyyy HH:mm a");
   	 DateFormat df2 = new SimpleDateFormat("MM-dd-yyyy HH:mm a");
   	 AuthUser user = (AuthUser) getLocalUser();
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        SimpleDateFormat parseTime = new SimpleDateFormat("hh:mm a");
        Date currD = new Date();
        Date currentDate = null;
        Date aftHrDate = null;
        Date aftDay = null;
        Date aftHrDate1 = null;
        Date aftDay1 = null;
        Date infoDate = null;
        Date datec = null;
        
        Date lessDay = DateUtils.addDays(currD, -1);
        
     //   List<NoteVM> actionVM = new ArrayList<NoteVM>();
        
        List<RequestInfoVM> actionVM= new ArrayList<RequestInfoVM>();
        
        List<ScheduleTest> list = ScheduleTest.findAllByServiceTestPopup(user,lessDay);
        
    	List<RequestMoreInfo> requestMoreInfos = RequestMoreInfo.findByConfirmGraLeadsToPopUp(user,lessDay);
    	List<TradeIn> tradeIns = TradeIn.findByConfirmGraLeadsToPopup(user,lessDay);
    	
    	//fillLeadsData(list, requestMoreInfos, tradeIns, infoVMList);
    	
    	for(ScheduleTest scTest:list){
       	 
    		RequestInfoVM acti = new RequestInfoVM();
       	 AuthUser aUser = AuthUser.findById(scTest.assignedTo.id);
       	 Location location = Location.findById(aUser.location.id);
       	
       	 df2.setTimeZone(TimeZone.getTimeZone(location.time_zone));
            String IST = df2.format(currD);
           
            Date istTimes = null;
			try {
				istTimes = df1.parse(IST);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
       	
       	 
       	 String cDate = df.format(istTimes);
            String cTime = parseTime.format(istTimes);
            String crD =    df1.format(istTimes);
   		 
            try {
           	 currentDate = df1.parse(crD);
           	 datec = df.parse(cDate);
           	 aftHrDate = DateUtils.addHours(currentDate, 1);
           	 aftDay = DateUtils.addHours(currentDate, 24);
           	 aftHrDate1 = DateUtils.addMinutes(aftHrDate, 15);
           	 aftDay1 = DateUtils.addMinutes(aftDay, 15);
   		} catch (Exception e) {
   			e.printStackTrace();
   		}
       	 
       	
       	 
       	 try {
       		 String str = df.format(scTest.confirmDate) +" "+parseTime.format(scTest.confirmTime);
       		 infoDate = df1.parse(str);

       		            	 
       		 if((infoDate.equals(aftHrDate)||infoDate.after(aftHrDate)) && ((infoDate.equals(aftHrDate1)||infoDate.before(aftHrDate1)))){
           		 if(scTest.meetingStatus == null){
           			acti.action = "Test drive reminder";
           			acti.notes = "You have a test drive scheduled in 1 hour ";
       			 }else if(scTest.meetingStatus.equals("meeting")){
       				acti.action = "Meeting reminder";
       				acti.notes = "You have a meeting scheduled in 1 hour ";
       			 }
           		 
           		acti.id = scTest.id;
        		Vehicle vehicle = Vehicle.findByVinAndStatus(scTest.vin);
        		acti.vin = scTest.vin;
        		if(vehicle != null) {
        			acti.model = vehicle.model;
        			acti.make = vehicle.make;
        			acti.stock = vehicle.stock;
        			acti.year = vehicle.year;
        			acti.mileage = vehicle.mileage;
        			acti.price = vehicle.price;
        		}
        		
        		acti.name = scTest.name;
        		acti.phone = scTest.phone;
        		acti.email = scTest.email;
        			
        		acti.howContactedUs = scTest.contactedFrom;
        		acti.howFoundUs = scTest.hearedFrom;
        		acti.custZipCode = scTest.custZipCode;
        		acti.enthicity = scTest.enthicity;
        		acti.status =scTest.leadStatus;
        		
        		acti.typeOfLead = "Schedule Test Drive";
        		findSchedulParentChildAndBro(actionVM, scTest, dfs, acti);
        		
           		 
           		 
           		 actionVM.add(acti);
           	 }
       		 if((infoDate.equals(aftDay)||infoDate.after(aftDay)) && ((infoDate.equals(aftDay1)||infoDate.before(aftDay1)))){
           		 if(scTest.meetingStatus == null){
           			acti.action =  "Test drive reminder";
           			acti.notes = "You have a test drive scheduled in 24 hours ";
       			 }else if(scTest.meetingStatus.equals("meeting")){
       				acti.action = "Meeting reminder";
       				acti.notes =  "You have a meeting scheduled in 24 hours ";
       			 }
           		 
           		 
           		acti.id = scTest.id;
        		Vehicle vehicle1 = Vehicle.findByVinAndStatus(scTest.vin);
        		acti.vin = scTest.vin;
        		if(vehicle1 != null) {
        			acti.model = vehicle1.model;
        			acti.make = vehicle1.make;
        			acti.stock = vehicle1.stock;
        			acti.year = vehicle1.year;
        			acti.mileage = vehicle1.mileage;
        			acti.price = vehicle1.price;
        		}
        		
        		acti.name = scTest.name;
        		acti.phone = scTest.phone;
        		acti.email = scTest.email;
        			
        		acti.howContactedUs = scTest.contactedFrom;
        		acti.howFoundUs = scTest.hearedFrom;
        		acti.custZipCode = scTest.custZipCode;
        		acti.enthicity = scTest.enthicity;
        		acti.status =scTest.leadStatus;
        		
        		acti.typeOfLead = "Schedule Test Drive";
        		findSchedulParentChildAndBro(actionVM, scTest, dfs, acti);
           		 actionVM.add(acti);
           	 }
			} catch (Exception e) {
				e.printStackTrace();
			}
       	 
       	
        }
        
        for(RequestMoreInfo rInfo:requestMoreInfos){
        	
        	RequestInfoVM acti = new RequestInfoVM();
       	 AuthUser emailUser = AuthUser.findById(rInfo.assignedTo.id);
       	 
       	 Location location = Location.findById(emailUser.location.id);
       	
       	 df2.setTimeZone(TimeZone.getTimeZone(location.time_zone));
            String IST = df2.format(currD);
           
            Date istTimes = null;
			try {
				istTimes = df1.parse(IST);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
       	
       	 
       	 String cDate = df.format(istTimes);
            String cTime = parseTime.format(istTimes);
            String crD =    df1.format(istTimes);
   		 
            try {
           	 currentDate = df1.parse(crD);
           	 datec = df.parse(cDate);
           	 aftHrDate = DateUtils.addHours(currentDate, 1);
           	 aftDay = DateUtils.addHours(currentDate, 24);
           	 aftHrDate1 = DateUtils.addMinutes(aftHrDate, 15);
           	 aftDay1 = DateUtils.addMinutes(aftDay, 15);
   		} catch (Exception e) {
   			e.printStackTrace();
   		}
       	 
       	 
       	 try {
       		 String str = df.format(rInfo.confirmDate) +" "+parseTime.format(rInfo.confirmTime);
       		 infoDate = df1.parse(str);
       		 if((infoDate.equals(aftHrDate)||infoDate.after(aftHrDate)) && ((infoDate.equals(aftHrDate1)||infoDate.before(aftHrDate1)))){
       			acti.action = "Test drive reminder";
       			acti.notes = "You have a test drive scheduled in 1 hour ";
       			
       			acti.id = rInfo.id;
        		Vehicle vehicle = Vehicle.findByVinAndStatus(rInfo.vin);
        		acti.vin = rInfo.vin;
        		if(vehicle != null) {
        			acti.model = vehicle.model;
        			acti.make = vehicle.make;
        			acti.stock = vehicle.stock;
        			acti.year = vehicle.year;
        			acti.mileage = vehicle.mileage;
        			acti.price = vehicle.price;
        		}
        		
        		acti.name = rInfo.name;
        		acti.phone = rInfo.phone;
        		acti.email = rInfo.email;
        			
        		acti.howContactedUs = rInfo.contactedFrom;
        		acti.howFoundUs = rInfo.hearedFrom;
        		acti.custZipCode = rInfo.custZipCode;
        		acti.enthicity = rInfo.enthicity;
        		acti.status =rInfo.leadStatus;
        		
        		acti.typeOfLead = "Request More Info";
        		findRequestParentChildAndBro(actionVM, rInfo, dfs, acti);
       		 actionVM.add(acti);
       		 }
       		 if((infoDate.equals(aftDay)||infoDate.after(aftDay)) && ((infoDate.equals(aftDay1)||infoDate.before(aftDay1)))){
       			acti.action =  "Test drive reminder";
       			acti.notes = "You have a test drive scheduled in 24 hours ";
       			
       			
       			acti.id = rInfo.id;
        		Vehicle vehicle = Vehicle.findByVinAndStatus(rInfo.vin);
        		acti.vin = rInfo.vin;
        		if(vehicle != null) {
        			acti.model = vehicle.model;
        			acti.make = vehicle.make;
        			acti.stock = vehicle.stock;
        			acti.year = vehicle.year;
        			acti.mileage = vehicle.mileage;
        			acti.price = vehicle.price;
        		}
        		
        		acti.name = rInfo.name;
        		acti.phone = rInfo.phone;
        		acti.email = rInfo.email;
        			
        		acti.howContactedUs = rInfo.contactedFrom;
        		acti.howFoundUs = rInfo.hearedFrom;
        		acti.custZipCode = rInfo.custZipCode;
        		acti.enthicity = rInfo.enthicity;
        		acti.status =rInfo.leadStatus;
        		
        		acti.typeOfLead = "Request More Info";
        		findRequestParentChildAndBro(actionVM, rInfo, dfs, acti);
       		 actionVM.add(acti);
       		 }
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        
        for(TradeIn tInfo:tradeIns){
        	RequestInfoVM acti = new RequestInfoVM();
       	 AuthUser emailUser = AuthUser.findById(tInfo.assignedTo.id);
       	 
       	 Location location = Location.findById(emailUser.location.id);
       	
       	 df2.setTimeZone(TimeZone.getTimeZone(location.time_zone));
            String IST = df2.format(currD);
           
            Date istTimes = null;
			try {
				istTimes = df1.parse(IST);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
       	
       	 
       	 String cDate = df.format(istTimes);
            String cTime = parseTime.format(istTimes);
            String crD =    df1.format(istTimes);
   		 
            try {
           	 currentDate = df1.parse(crD);
           	 datec = df.parse(cDate);
           	 aftHrDate = DateUtils.addHours(currentDate, 1);
           	 aftDay = DateUtils.addHours(currentDate, 24);
           	 aftHrDate1 = DateUtils.addMinutes(aftHrDate, 15);
           	 aftDay1 = DateUtils.addMinutes(aftDay, 15);
   		} catch (Exception e) {
   			e.printStackTrace();
   		}
       	 
       	 
       	 try {
       		 String str = df.format(tInfo.confirmDate) +" "+parseTime.format(tInfo.confirmTime);
       		 infoDate = df1.parse(str);
       		 if((infoDate.equals(aftHrDate)||infoDate.after(aftHrDate)) && ((infoDate.equals(aftHrDate1)||infoDate.before(aftHrDate1)))){
        			acti.action = "Test drive reminder";
           			acti.notes = "You have a test drive scheduled in 1 hour ";
           			
           			acti.id = tInfo.id;
            		Vehicle vehicle = Vehicle.findByVinAndStatus(tInfo.vin);
            		acti.vin = tInfo.vin;
            		if(vehicle != null) {
            			acti.model = vehicle.model;
            			acti.make = vehicle.make;
            			acti.stock = vehicle.stock;
            			acti.year = vehicle.year;
            			acti.mileage = vehicle.mileage;
            			acti.price = vehicle.price;
            		}
            		
            		acti.name = tInfo.firstName;
            		acti.phone = tInfo.phone;
            		acti.email = tInfo.email;
            			
            		acti.howContactedUs = tInfo.contactedFrom;
            		acti.howFoundUs = tInfo.hearedFrom;
            		acti.custZipCode = tInfo.custZipCode;
            		acti.enthicity = tInfo.enthicity;
            		acti.status =tInfo.leadStatus;
            		
            		acti.typeOfLead = "Trade-In Appraisal";
            		findTreadParentChildAndBro(actionVM, tInfo, dfs, acti);
           		 actionVM.add(acti);
       		 }
       		 if((infoDate.equals(aftDay)||infoDate.after(aftDay)) && ((infoDate.equals(aftDay1)||infoDate.before(aftDay1)))){
       			acti.action =  "Test drive reminder";
       			acti.notes = "You have a test drive scheduled in 24 hours ";
       			
       			
       			acti.id = tInfo.id;
        		Vehicle vehicle = Vehicle.findByVinAndStatus(tInfo.vin);
        		acti.vin = tInfo.vin;
        		if(vehicle != null) {
        			acti.model = vehicle.model;
        			acti.make = vehicle.make;
        			acti.stock = vehicle.stock;
        			acti.year = vehicle.year;
        			acti.mileage = vehicle.mileage;
        			acti.price = vehicle.price;
        		}
        		
        		acti.name = tInfo.firstName;
        		acti.phone = tInfo.phone;
        		acti.email = tInfo.email;
        			
        		acti.howContactedUs = tInfo.contactedFrom;
        		acti.howFoundUs = tInfo.hearedFrom;
        		acti.custZipCode = tInfo.custZipCode;
        		acti.enthicity = tInfo.enthicity;
        		acti.status =tInfo.leadStatus;
        		
        		acti.typeOfLead = "Trade-In Appraisal";
        		findTreadParentChildAndBro(actionVM, tInfo, dfs, acti);
       		 actionVM.add(acti);
       		 }
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        
        
   	return ok(Json.toJson(actionVM));
    }
   
    
    public static Result showPdf(Long id) {
    	try{
    		CustomerPdf pdfFile = CustomerPdf.findPdfById(id);
        	File file = new File(rootDir+"/" + 16L +"/"+ "OnlineLead"+"/"+ id + "/" + "onlineLead.pdf");
        	response().setContentType("application/pdf");
        	response().setHeader("Content-Disposition", "inline; filename=tradeIn.pdf");
    		return ok(file);
    	}catch(Exception e){
    		return ok("File Not Found");
    	}
    	
    }
    
   
    public static Result forgotPass(){
    	String email = Form.form().bindFromRequest().get("email");
    	AuthUser user = AuthUser.find.where().eq("email", email).findUnique();
    	if(user != null) {
			
    	
    		 EmailDetails details=EmailDetails.findByLocation(Long.valueOf(session("USER_LOCATION")));
 			String emailName=details.name;
 			String port=details.port;
 			String gmail=details.host;
 			final	String emailUser=details.username;
 			final	String emailPass=details.passward;
	 		Properties props = new Properties();
	 		props.put("mail.smtp.auth", "true");
	 		props.put("mail.smtp.starttls.enable", "true");
	 		props.put("mail.smtp.host", gmail);
	 		props.put("mail.smtp.port", port);
	  
	 		Session session = Session.getInstance(props,
	 		  new javax.mail.Authenticator() {
	 			protected PasswordAuthentication getPasswordAuthentication() {
	 				return new PasswordAuthentication(emailUser, emailPass);
	 			}
	 		  });
	  
	 		try{
	 		   
	  			Message feedback = new MimeMessage(session);
	  			feedback.setFrom(new InternetAddress(emailUser));
	  			feedback.setRecipients(Message.RecipientType.TO,
	  			InternetAddress.parse(user.email));
	  			feedback.setSubject("Your forgot password ");	  			
	  			 BodyPart messageBodyPart = new MimeBodyPart();	  	       
	  	         messageBodyPart.setText("Your forget password is "+user.password);	 	    
	  	         Multipart multipart = new MimeMultipart();	  	    
	  	         multipart.addBodyPart(messageBodyPart);	            
	  	         feedback.setContent(multipart);
	  		     Transport.send(feedback);
	       		} catch (MessagingException e) {
	  			  throw new RuntimeException(e);
	  		}
			return ok("success");
		} else {
			return ok("error");
		}
    	
    }
    
    
    
    private static void makeToDo(Long pId) {
    	AuthUser user = (AuthUser) getLocalUser();
    	ToDo todo = new ToDo();
    	AddCollection pAddCollection = AddCollection.findByIdNotSale(pId);
		//Vehicle vobj = Vehicle.findByVinAndStatus(vin);
		if(pAddCollection != null){
			todo.task = "Follow up with the client about test Drive for"+pAddCollection.designer+" "+pAddCollection.title;
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
		}
		
    }
    
    
    private static void saveBilndVm(LeadVM leadVM,MultipartFormData bodys,Form<LeadVM> form) {
    	 
    	 JSONArray jArr = null,jArr1 = null;
    	 List<InventoryVM> vmList = new ArrayList<>();
    	 List<KeyValueDataVM> vmList1 = new ArrayList<>();
		try {
			form.data().get("stockWiseData");
			System.out.println(form.data().get("stockWiseData"));
			if(form.data().get("stockWiseData") != null){
				jArr = new JSONArray(form.data().get("stockWiseData"));
				
				for (int i=0; i < jArr.length(); i++) {
					InventoryVM vm = new InventoryVM();
					JSONObject jsonObj = jArr.getJSONObject(i);
					vm.id = Long.parseLong(String.valueOf(jsonObj.get("id")));
					vm.imgId = String.valueOf(jsonObj.get("imgId")); 
					vm.title = String.valueOf(jsonObj.get("title"));
					vm.price =  String.valueOf(jsonObj.get("price"));
					vmList.add(vm);
					leadVM.stockWiseData.add(vm);
				}
			}
			
			
			
			
			jArr1 = new JSONArray(form.data().get("customData"));
			
			for (int i=0; i < jArr1.length(); i++) {
				KeyValueDataVM vm1 = new KeyValueDataVM();
				JSONObject jsonObj1 = jArr1.getJSONObject(i);
				vm1.key = String.valueOf(jsonObj1.get("key"));
				vm1.value = String.valueOf(jsonObj1.get("value"));
				vm1.formName = String.valueOf(jsonObj1.get("formName"));
				vm1.fieldId = Long.parseLong(String.valueOf(jsonObj1.get("fieldId")));
				vm1.displayGrid = String.valueOf(jsonObj1.get("displayGrid"));
				vm1.displayWebsite = String.valueOf(jsonObj1.get("displayWebsite"));
				vm1.savecrm = String.valueOf(jsonObj1.get("savecrm"));
				vm1.component = String.valueOf(jsonObj1.get("component"));
				vmList1.add(vm1);
				leadVM.customData.add(vm1);
			}
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		leadVM.id = form.data().get("id");
		leadVM.custEmail = form.data().get("custEmail");
		leadVM.custName = form.data().get("custName");
		leadVM.custNumber = form.data().get("custNumber");
		leadVM.custZipCode = form.data().get("custZipCode");
		leadVM.prefferedContact = form.data().get("prefferedContact");
		leadVM.leadType =form.data().get("leadType");
		leadVM.productList = form.data().get("productList");
		leadVM.manufacturers =form.data().get("manufacturers");
		leadVM.saveLeadTypeAs = form.data().get("saveLeadTypeAs");
		
         
    }
    
    
    private static void saveCustomData(Long infoId,List<KeyValueDataVM> customData,MultipartFormData bodys,Long leadtype) {
    	String formName = "";
    	for(KeyValueDataVM custom:customData){
    		
    		CustomizationDataValue cDataValue = CustomizationDataValue.findByKeyAndLeadId(custom.key,infoId);
    		if(cDataValue == null){
    			CustomizationDataValue cValue = new CustomizationDataValue();
    			cValue.fieldId = custom.fieldId;
    			cValue.keyValue = custom.key;
    			if(custom.component.equals("fileuploaders")){
    				String fileN = custom.value.replaceAll("[-+^:,() ]","");
    				cValue.value = File.separator+session("USER_LOCATION")+File.separator+"leads"+File.separator+leadtype+File.separator+infoId+File.separator+fileN; 
    			}else{
    				cValue.value = custom.value;
    			}
    			
    			cValue.leadId = infoId;
    			cValue.leadType = leadtype;
    			cValue.formName = custom.formName;
    			if(!cValue.formName.equals("Create New Lead")){
    				formName = cValue.formName;
    			}
    			if(custom.savecrm == null){
    				cValue.saveCrm = "false";
    			}else{
    				cValue.saveCrm = custom.savecrm;
    			}
    			
    			if(custom.displayGrid == null){
    				cValue.displayGrid = "false";
    			}else{
    				cValue.displayGrid = custom.displayGrid;
    			}
    			
    			if(custom.displayWebsite == null){
    				cValue.displayWebsite = "false";
    			}else{
    				cValue.displayWebsite = custom.displayWebsite;
    			}
    			
    			cValue.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    			cValue.save();
    			
    		}else{
    			cDataValue.setKeyValue(custom.key);
    			
    			if(custom.component.equals("fileuploaders")){
    				String fileN = custom.value.replaceAll("[-+^:,() ]","");
    				cDataValue.setValue(File.separator+session("USER_LOCATION")+File.separator+"leads"+File.separator+leadtype+File.separator+infoId+File.separator+fileN); 
    			}else{
    				cDataValue.setValue(custom.value);
    			}
    			
    			cDataValue.setFormName(custom.formName);
    			formName = custom.formName;
    			if(custom.savecrm == null){
    				cDataValue.setSaveCrm("false");
    			}else{
    				cDataValue.setSaveCrm(custom.savecrm);
    			}
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
    				File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+"leads"+File.separator+leadtype+File.separator+infoId+File.separator+fileName);
    	    	    if(!fdir.exists()) {
    	    	    	fdir.mkdir();
    	    	    }
    	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"leads"+File.separator+leadtype+File.separator+infoId+File.separator+fileName;
    	    	    FileUtils.moveFile(file, new File(filePath));
    	    	    
    			
    			} catch (Exception e) {
					e.printStackTrace();
				}
    		}
    	}
    	
		
    }
 
    
    private static void sendMailpremium() {
    	/*--------------------------------*/
		
		AuthUser aUser = AuthUser.getlocationAndManagerOne(Location.findById(Long.valueOf(session("USER_LOCATION"))));
		final String username = emailUsername;
		final String password = emailPassword;
		
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
				return new PasswordAuthentication(username, password);
			}
		});
		
		  
		    try {  
		     MimeMessage message = new MimeMessage(session);  
	    		try{
		     message.setFrom(new InternetAddress(emailUser,emailName));  
	    		}catch(UnsupportedEncodingException e){
	    			e.printStackTrace();
	    		}
		     message.addRecipient(Message.RecipientType.TO,new InternetAddress(aUser.communicationemail));  
		     message.setSubject("Premium Leads");  
		     message.setText("Premium Request has been submitted");  
		       
		     Transport.send(message);  
		  
		     System.out.println("message sent successfully...");  
		   
		     } catch (MessagingException e) {e.printStackTrace();} 
		
		
		/*------------------------------------*/
    }
    
private static void cancelTestDriveMail(Map map) {
    	
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
		props.put("mail.smtp.host",gmail);
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
    		try{
			message.setFrom(new InternetAddress(emailUser,emailName));
    		}
    		catch(UnsupportedEncodingException e){
    			e.printStackTrace();
    		}
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(map.get("email").toString()));
			message.setSubject("TEST DRIVE CANCELLED");
			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			ve.init();
		
			
	        Template t = ve.getTemplate("/public/emailTemplate/testDriveCancelled_HTML.vm"); 
	        VelocityContext context = new VelocityContext();
	        /*String months[] = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
	       
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
	        
	        String monthName = months[month-1];*/
	        context.put("hostnameUrl", imageUrlPath);
	        context.put("siteLogo", logo.logoImagePath);
	        /*context.put("dayOfmonth", dayOfmonth);
	        context.put("monthName", monthName);
	        context.put("confirmTime", map.get("confirmTime"));*/
	        
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
	        context.put("name", map.get("uname"));
	        context.put("email", map.get("uemail"));
	       // context.put("phone",  map.get("uphone"));
	        String phoneNum=map.get("uphone").toString();
	        String firstThreeDigit=phoneNum;
	        firstThreeDigit=firstThreeDigit.substring(0, 3);
	        String secondThreeDigit=phoneNum;
	        secondThreeDigit=secondThreeDigit.substring(3, 6);
	        String thirdThreeDigit=phoneNum;
	        thirdThreeDigit=thirdThreeDigit.substring(6, 10);
	        String phn = "("+firstThreeDigit+")"+secondThreeDigit+"-"+thirdThreeDigit;
	        context.put("phon", phn);
	     //   context.put("secondThreeDigit", secondThreeDigit);
	    //    context.put("thirdThreeDigit", thirdThreeDigit);
	        
	        /*String weather= map.get("CnfDateNature").toString();
	        String arr1[] = weather.split("&");
	        String nature=arr1[0];
	        String temp=arr1[1];
	        context.put("nature",nature);
	        context.put("temp", temp);*/
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
    
    
    
    
    
    private static void sendMail(Map map) {
    	
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
	 		try{
			message.setFrom(new InternetAddress(emailUser,emailName));
	 		}catch(UnsupportedEncodingException e){
	 			e.printStackTrace();
	 		}
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(map.get("email").toString()));
			message.setSubject("TEST DRIVE CONFIRMATION");
			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			ve.init();
		
			
	        Template t = ve.getTemplate("/public/emailTemplate/testDriveconfirmatioin.vm"); 
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
	        
	        //String monthName = months[month-1];
	        context.put("hostnameUrl", imageUrlPath);
	        context.put("siteLogo", logo.logoImagePath);
	        context.put("dayOfmonth", dayOfmonth);
	        context.put("monthName", "jan");// monthName);
	        context.put("confirmTime", map.get("confirmTime"));
	        context.put("title", map.get("leadTypes"));
	        context.put("manufacturers", map.get("manufacturers"));
	        
	        /*Vehicle vehicle = Vehicle.findByVinAndStatus(map.get("productId").toString());
	        context.put("year", vehicle.year);
	        context.put("make", vehicle.make);
	        */
	        context.put("name", map.get("uname"));
	        context.put("email", map.get("uemail"));
	        context.put("phone",  map.get("uphone"));
	       // String weather= map.get("weatherValue").toString();
	        //String arr1[] = weather.split("&");
	        //String nature=arr1[0];
	        //String temp=arr1[1];
	        //context.put("nature",nature);
	        //context.put("temp", temp);
	        /*InventoryImage image = InventoryImage.getDefaultImage(vehicle.vin);
	        if(image!=null) {
	        	context.put("defaultImage", image.path);
	        } else {
	        	context.put("defaultImage", "");
	        }*/
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
    
    public static Result sendPdfEmail() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		Form<UserVM> form = DynamicForm.form(UserVM.class).bindFromRequest();
    		AuthUser users = (AuthUser) getLocalUser();
    		MultipartFormData body = request().body().asMultipartFormData();
    		
    		EmailDetails details=EmailDetails.findByLocation(Long.valueOf(session("USER_LOCATION")));
    		String emailName=details.name;	
    		String port=details.port;
    		String gmail=details.host;
    	final	String emailUser=details.username;
    	final	String emailPass=details.passward;
	    	AuthUser userObj = new AuthUser();
	    	UserVM vm = form.get();
	    	//List<String> aa= new ArrayList<>();
	    		  
	    		   
	    		Properties props = new Properties();
		 		props.put("mail.smtp.auth", "true");
		 		props.put("mail.smtp.starttls.enable", "true");
		 		props.put("mail.smtp.host", gmail);
		 		props.put("mail.smtp.port",port);
		 		System.out.println(">>port"+port);
		    System.out.println(">>gmailhost"+gmail);
		    System.out.println(">>>>emailUser"+emailUser);
		    System.out.println(">>>>emailPass"+emailPass);
		 		Session session = Session.getInstance(props,
		 		  new javax.mail.Authenticator() {
		 			protected PasswordAuthentication getPasswordAuthentication() {
		 				return new PasswordAuthentication(emailUser,emailPass);
		 			}
		 		  });
		  
		 		try{
		 			System.out.println("emailUsername"+emailUsername);
		 			System.out.println("emailPassword"+emailPassword);
		  			Message message = new MimeMessage(session);
		  			try {
		  				System.out.println(">>>>>>>"+emailName);
						message.setFrom(new InternetAddress(emailUser,emailName));
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		  			
		  			String arr[] = vm.email.split(",");
		  			
		  			InternetAddress[] usersArray = new InternetAddress[arr.length];
		  			int index = 0;
		  			
		  			for (int i=0;i<arr.length;i++) {
		  				RequestMoreInfo rInfo = RequestMoreInfo.findById(Long.parseLong(arr[i]));
		  				if(rInfo != null){
		  					try {
			  					
			  					usersArray[index] = new InternetAddress(rInfo.email);
			  					index++;
			  				} catch (Exception e) {
			  					e.printStackTrace();
			  				}
		  				}
		  				
		  			}
		  			//message.setRecipients(Message.RecipientType.TO,
		  		//	InternetAddress.parse("yogeshpatil424@gmail.com"));
		  		   message.setRecipients(Message.RecipientType.TO, usersArray);
		  			message.setSubject("Requested Documents");	  			
		  			Multipart multipart = new MimeMultipart();
	    			BodyPart messageBodyPart = new MimeBodyPart();
	    			messageBodyPart = new MimeBodyPart();
	    			
	    			VelocityEngine ve = new VelocityEngine();
	    			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
	    			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
	    			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
	    			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
	    			ve.init();
	    			
	    			Template t = ve.getTemplate("/public/emailTemplate/PDFsent_HTML.html"); 
	    	        VelocityContext context = new VelocityContext();
	    	        context.put("hostnameUrl", imageUrlPath);
	    	        //context.put("siteLogo", logo.logoImagePath);
	    	        context.put("title", vm.title);
	    	        context.put("text", vm.text);
	    	        context.put("name", users.fullName());
	    	        context.put("email", users.email);
	    	        context.put("phone", users.phone);
	    	        
	    	        StringWriter writer = new StringWriter();
	    	        t.merge( context, writer );
	    	        String content = writer.toString(); 
	    			
	    			messageBodyPart.setContent(content, "text/html");
	    			String nameOfDocument= null;
	    			StringBuffer output = new StringBuffer(110);
	    	
	    			/*if(vm.vin != null){
	    				
	    				Vehicle vehicle=Vehicle.findByVin(vm.vin);
	    				  String PdfFile = rootDir + File.separator +vehicle.pdfBrochurePath;
		 	    		  File f = new File(PdfFile);
		 	    		 MimeBodyPart attachPart = new MimeBodyPart();
		 	    		 try {
		    					attachPart.attachFile(f);
		    		  	      } catch (IOException e) {
		    		  	       	// TODO Auto-generated catch block
		    		  	       		e.printStackTrace();
		    		  	    }
		    			 multipart.addBodyPart(attachPart);
	    				
	    				
	    				
	    			}*/
	    			CustomerPdf iPdf = null;
		    		   
		    		   for(String pdf:vm.pdfIds){
		    			   CustomerPdf cust = CustomerPdf.findPdfById(Long.parseLong(pdf));
		    			   if(cust != null){
		    				   String PdfFile = rootDir + File.separator +cust.pdf_path;
				 	    		  File f = new File(PdfFile);
				 	    		  
				 	    		 MimeBodyPart attachPart = new MimeBodyPart();
				 	    		 try {
				    					attachPart.attachFile(f);
				    		  	      } catch (IOException e) {
				    		  	       	// TODO Auto-generated catch block
				    		  	       		e.printStackTrace();
				    		  	    }
				    			 multipart.addBodyPart(attachPart); 
		    			   }
		    		   }
	    			
	    			String nameDoc= output.toString();
	    			String actionTitle="Followed up with the following documents: "+nameDoc;
	    			String action="Other";
	    			//Long ids=(long)vm.id;
	    			for (int i=0;i<arr.length;i++) {
	    				saveNoteOfUser(Long.parseLong(arr[i]),actionTitle,action);
	    			}	
	    			
	 	    		
	    			
	    			
	    			multipart.addBodyPart(messageBodyPart);
	    			message.setContent(multipart);
	    			Transport.send(message);
		       		} catch (MessagingException e) {
		  			 throw new RuntimeException(e);
		  		}
	    	   
	    	return ok();
    	}
    }
    
    /*public static Result getUserLocationByDateInfo(String startDate,String endDate,String locOrPer){
    	
    	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    	DateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");
    	Date dateobj = new Date();
    	Calendar cal1 = Calendar.getInstance();
    	AuthUser users = (AuthUser) getLocalUser();
    	String[] monthName = { "January", "February", "March", "April", "May", "June", "July",
		        "August", "September", "October", "November", "December" };
    	
    	Map<String, Integer> mapCar = new HashMap<String, Integer>();
    	
    	Date startD = null;
		try {
			startD = df1.parse(startDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Date endD = null;
		try {
			endD = df1.parse(endDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	int requestLeadCount = 0;
    	int scheduleLeadCount = 0;
    	int tradeInLeadCount = 0;
    	
    	int requestLeadCount1 = 0;
    	int scheduleLeadCount1 = 0;
    	int tradeInLeadCount1 = 0;
    	
    	List<RequestMoreInfo> rInfo = null;
    	List<ScheduleTest> sList = null;
    	List<TradeIn> tradeIns = null;
    	List<RequestMoreInfo> rInfoAll = null;
    	List<ScheduleTest> sListAll = null;
    	List<TradeIn> tradeInsAll = null;
    	
    	if(users.role.equals("Manager") && locOrPer.equals("location")){
    		rInfo = RequestMoreInfo.findAllSeenLocationSch(Long.parseLong(session("USER_LOCATION")));
    		sList = ScheduleTest.findAllAssignedLocation(Long.parseLong(session("USER_LOCATION")));
    		tradeIns = TradeIn.findAllSeenLocationSch(Long.parseLong(session("USER_LOCATION")));
    		
    		rInfoAll = RequestMoreInfo.findByLocation(Long.parseLong(session("USER_LOCATION")));
    		sListAll = ScheduleTest.findByLocation(Long.parseLong(session("USER_LOCATION")));
    		tradeInsAll = TradeIn.findByLocation(Long.parseLong(session("USER_LOCATION")));
    		
    	}else if(users.role.equals("Sales Person") || locOrPer.equals("person")){
    		rInfo = RequestMoreInfo.findAllSeenSch(users);
    		sList = ScheduleTest.findAllAssigned(users);
    		tradeIns = TradeIn.findAllSeenSch(users);
    		
    		rInfoAll = RequestMoreInfo.findAllByUser(users);
    		sListAll = ScheduleTest.findAllByUser(users);
    		tradeInsAll = TradeIn.findAllByUser(users);
    	}
    	
    	for(RequestMoreInfo rMoreInfo:rInfo){
    		if((rMoreInfo.requestDate.after(startD) && rMoreInfo.requestDate.before(endD)) || rMoreInfo.requestDate.equals(endD)){
    			requestLeadCount++;
    		}
    	}
    	
    	
    	for(ScheduleTest sTest:sList){
    		if((sTest.scheduleDate.after(startD) && sTest.scheduleDate.before(endD)) || sTest.scheduleDate.equals(endD)){
    			scheduleLeadCount++;
    		}
    	}

    	for(TradeIn tIn:tradeIns){
    		if((tIn.tradeDate.after(startD) && tIn.tradeDate.before(endD)) || tIn.tradeDate.equals(endD)){
    				tradeInLeadCount++;
    		}
    	}
    	
    	for(RequestMoreInfo rMoreInfo:rInfoAll){
    		if((rMoreInfo.requestDate.after(startD) && rMoreInfo.requestDate.before(endD)) || rMoreInfo.requestDate.equals(endD)){
    			requestLeadCount1++;
    		}
    	}
    	
    	
    	for(ScheduleTest sTest:sListAll){
    	if((sTest.scheduleDate.after(startD) && sTest.scheduleDate.before(endD)) || sTest.scheduleDate.equals(endD)){
    			scheduleLeadCount1++;
    	}
    	}

    	for(TradeIn tIn:tradeInsAll){
    	if((tIn.tradeDate.after(startD) && tIn.tradeDate.before(endD)) || tIn.tradeDate.equals(endD)){
				tradeInLeadCount1++;
    		}
    	}
    	
    	
    	int countLeads1 = requestLeadCount1 + scheduleLeadCount1 + tradeInLeadCount1;
    	int countLeads = requestLeadCount + scheduleLeadCount + tradeInLeadCount;
    	Location location = Location.findById(Long.parseLong(session("USER_LOCATION")));
    	LocationWiseDataVM lDataVM = new LocationWiseDataVM();
    	lDataVM.imageUrl = location.getImageUrl();
    	//List<AuthUser> uAuthUser = AuthUser.getlocationAndRoleByType(location, "Sales Person");
    	lDataVM.countSalePerson = countLeads;
    	
    	Integer pricecount = 0;
    	int saleCarCount = 0;
    	if(users.role.equals("Manager") && locOrPer.equals("location")){
    		List<Vehicle> vList = Vehicle.findByLocationAndSold(location.id);
        	double sucessCount= (double)vList.size()/(double)countLeads1*100;
        	lDataVM.successRate = (int) sucessCount;
        	
        	for(Vehicle vehList:vList){
        		//	if(vehList.soldDate.after(timeBack)) {
        		if(vehList.soldDate.after(startD) && vehList.soldDate.before(endD)){
            			saleCarCount++;
            			pricecount = pricecount + vehList.price;
            		}
        	}
        	
        	double sucessCount= (double)saleCarCount/(double)countLeads1*100;
        	lDataVM.successRate = (int) sucessCount;
    	}else if(users.role.equals("Sales Person") || locOrPer.equals("person")){
    		
    		List<RequestMoreInfo> rInfo1 = RequestMoreInfo.findAllSeenComplete(users);
    		List<ScheduleTest> sList1 = ScheduleTest.findAllSeenComplete(users);
    		List<TradeIn> tradeIns1 = TradeIn.findAllSeenComplete(users);
    		
    	//	saleCarCount = rInfo1.size() + sList1.size() + tradeIns1.size();
    		
    		for(RequestMoreInfo rMoreInfo: rInfo1){
    			List<Vehicle> vehicleVin = Vehicle.findByVidAndUserWise(rMoreInfo.vin,users);
    			for(Vehicle vehicle:vehicleVin){
    			if(vehicle != null){
    				if((vehicle.soldDate.after(startD) && vehicle.soldDate.before(endD)) || vehicle.soldDate.equals(endD)){
            			saleCarCount++;
            			pricecount = pricecount + vehicle.price;
    				}
    			}
    		 }
    		}
    		
    		for(ScheduleTest sTest: sList1){
    			List<Vehicle> vehicleVin = Vehicle.findByVidAndUserWise(sTest.vin,users);
    			for(Vehicle vehicle:vehicleVin){
    			if(vehicle != null){
    				if((vehicle.soldDate.after(startD) && vehicle.soldDate.before(endD)) || vehicle.soldDate.equals(endD)){
            			saleCarCount++;
            			pricecount = pricecount + vehicle.price;
    				}
    			}
    		}
    		}
    		
    		for(TradeIn tradeIn: tradeIns1){
    			List<Vehicle> vehicleVin = Vehicle.findByVidAndUser(tradeIn.vin);
    			for(Vehicle vehicle:vehicleVin){
    			if(vehicle != null){
    				if((vehicle.soldDate.after(startD) && vehicle.soldDate.before(endD)) || vehicle.soldDate.equals(endD)){
            			saleCarCount++;
            			pricecount = pricecount + vehicle.price;
    				}
    			}
    		}
    		}
    		
    		double sucessCount= (double)saleCarCount/(double)countLeads1*100;
    		lDataVM.successRate = (int) sucessCount;
    		
    	}
    	
    	lDataVM.totalSalePrice = pricecount;
    	lDataVM.totalsaleCar = saleCarCount;
    	
    	double valAvlPrice= ((double)pricecount/(double)saleCarCount);
    	lDataVM.angSalePrice = (int) valAvlPrice;
    	
    	Calendar cal = Calendar.getInstance();  
    	String monthCal = monthName[cal.get(Calendar.MONTH)];
    	PlanScheduleMonthlyLocation pMonthlyLocation = PlanScheduleMonthlyLocation.findByLocationAndMonth(Location.findById(Long.parseLong(session("USER_LOCATION"))), monthCal);
    	if(pMonthlyLocation != null){
    		double val= ((double)pricecount/Double.parseDouble(pMonthlyLocation.totalEarning));
        	lDataVM.AngSale = (int) (val*100);
    	}
    	
    	
    	
    	List<Vehicle> allVehiList = Vehicle.findByLocation(location.id);
    	int saleCar = 0;
    	int newCar = 0;
    	for(Vehicle vehicle:allVehiList){
    		if(vehicle.status.equals("Sold")){
    			if((vehicle.soldDate.after(startD) && vehicle.soldDate.before(endD)) || vehicle.soldDate.equals(endD)){
    				saleCar++;
    			}
    		}//else if(vehicle.status.equals("Newly Arrived")){
    				newCar++;
    		//}
    	}
    	
    	double val= ((double)saleCar/(double)newCar);
    	lDataVM.AngSale = (int) (val*100);
        
    	List<LeadsDateWise> lDateWises = LeadsDateWise.findByLocation(Location.findById(Long.parseLong(session("USER_LOCATION")))); 
    	for(LeadsDateWise lWise:lDateWises){
    		
    		if(lWise.goalSetTime.equals("1 week")){
    			cal.setTime(lWise.leadsDate);  
    			cal.add(Calendar.DATE, 7);
    			Date m =  cal.getTime(); 
    			if(m.after(dateobj)) {
    				lDataVM.leads = lWise.leads;
    				lDataVM.goalTime =lWise.goalSetTime;
    				
    			}
    			
    		}else if(lWise.goalSetTime.equals("1 month")){
    			cal.setTime(lWise.leadsDate);  
    			cal.add(Calendar.DATE, 30);
    			Date m =  cal.getTime(); 
    			if(m.after(dateobj)) {
    				lDataVM.leads = lWise.leads;
    				lDataVM.goalTime =lWise.goalSetTime;
    			}
    		}
    		
    	}
    	
    	List<DateAndValueVM> sAndValues = new ArrayList<>();
    	List<Long> sAndLong = new ArrayList<>();
    	int countPlanCarSold = 0;
    	
    	
    	
		Calendar now = Calendar.getInstance();
		String month = monthName[now.get(Calendar.MONTH)];
		lDataVM.monthCurr = month;
    	PlanScheduleMonthlyLocation pLocation = PlanScheduleMonthlyLocation.findByLocationAndMonth(Location.findById(Long.parseLong(session("USER_LOCATION"))), month);
    	if(pLocation != null){
    		List<Integer> longV = new ArrayList<>();
			DateAndValueVM sValue = new DateAndValueVM();
			sValue.name = "Plan";
			longV.add(Integer.parseInt(pLocation.totalEarning));
			sValue.data = longV;
			sAndValues.add(sValue);
    	}
    	
    	
    	int countPlanTotalErForLocation = 0;
    	DateFormat inputDF  = new SimpleDateFormat("MMMM");
    	List<Vehicle> vehicles = Vehicle.findByLocationAndSold(Long.parseLong(session("USER_LOCATION")));
		for(Vehicle veh1:vehicles){
			//countPlanTotalErForLocation = countPlanTotalErForLocation + veh1.price;
			String dateValue = inputDF.format(veh1.soldDate);
			if(month.equals(dateValue)){
				countPlanCarSold = countPlanCarSold + veh1.price;
			}
			
		}
    	
    	
    	if(countPlanCarSold > 0){
    		List<Integer> longV = new ArrayList<>();
    		DateAndValueVM sValue = new DateAndValueVM();
			sValue.name = "Record";
			longV.add(countPlanCarSold);
			sValue.data = longV;
			sAndValues.add(sValue);
    	}
    	
    	List<Vehicle> vList2 = Vehicle.findByLocationAndSold(Long.parseLong(session("USER_LOCATION")));
    	
    	if(vList2.size() != 0){
    		List<Integer> longV = new ArrayList<>();
    		DateAndValueVM sValue = new DateAndValueVM();
			sValue.name = "You";
			longV.add(countPlanCarSold);
			sValue.data = longV;
			sAndValues.add(sValue);
    	}
    	
    	lDataVM.sendData = sAndValues;

    	return ok(Json.toJson(lDataVM));
    	
    	
    }*/
    
  
   
   public static Result getUserLocationByDateInfo(Integer userKey,String startDate,String endDate,String locOrPer){
		AuthUser users = AuthUser.findById(userKey);
		LocationWiseDataVM lDataVM = new LocationWiseDataVM();
		/*long locationId = 0;
    	if(session("USER_LOCATION") == null){
    		locationId = 0L;
    	}else{
    		locationId = Long.valueOf(session("USER_LOCATION"));
    	}*/
	   mystatisticsDateWise(startDate,endDate,locOrPer,users.location.id,users,lDataVM);
   		return ok(Json.toJson(lDataVM));
   }
   
   public static void mystatisticsDateWise(String startDate,String endDate,String locOrPer,Long locationId,AuthUser users,LocationWiseDataVM lDataVM){
	    DateFormat onlyMonth = new SimpleDateFormat("MMMM");
   		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
   		DateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");
   		Date dateobj = new Date();
   		Calendar cal1 = Calendar.getInstance();
   		String[] monthName = { "January", "February", "March", "April", "May", "June", "July",
		        "August", "September", "October", "November", "December" };
   	
   		Calendar cal = Calendar.getInstance();  
    	String monthCal = monthName[cal.get(Calendar.MONTH)];
    	Map<String, Integer> mapCar = new HashMap<String, Integer>();
    	Date startD = null;
		try {
			startD = df1.parse(startDate);
		}catch (ParseException e) {
			e.printStackTrace();
		}
		Date endD = null;
		try {
			endD = df1.parse(endDate);
		}catch (ParseException e) {
			e.printStackTrace();
		}
	 	List<AddCollection> product1 = AddCollection.findByLocationAndSold(Long.parseLong(session("USER_LOCATION")));
		int flag1=0;
		lDataVM.flagForBestSaleIcon = 0;
		if(product1 != null){
			for(AddCollection product:product1){
				if((product != null && product.soldDate.after(startD) && product.soldDate.before(endD)) || product.soldDate.equals(startD) || product.soldDate.equals(endD)) {
					flag1=1;
					lDataVM.flagForBestSaleIcon=flag1;
					break;
				}
			}
	 	}
	
		int requestLeadCount = 0;
		int scheduleLeadCount = 0;
   	
		int requestLeadCount1 = 0;
		int scheduleLeadCount1 = 0;
   	
		int requestLeadCountTest = 0;
		int scheduleLeadCountTest = 0;
   	
		List<RequestMoreInfo> rInfo = null;
		List<ScheduleTest> sList = null;
		
		List<RequestMoreInfo> rInfoAll = null;
		List<ScheduleTest> sListAll = null;
   	
		List<RequestMoreInfo> rInfoTest = null;
		List<ScheduleTest> sListTest = null;
   	
		if(users.role.equals("Manager") && locOrPer.equals("location") || users.role.equals("Sales Person") && locOrPer.equals("location")){
			rInfo = RequestMoreInfo.findAllSeenLocationSch(locationId);
			sList = ScheduleTest.findAllAssignedLocation(locationId);
   		
			rInfoAll = RequestMoreInfo.findByLocationNotOpenLead(locationId);
			sListAll = ScheduleTest.findByLocationNotOpenLead(locationId);
   		
		}else if(users.role.equals("Sales Person") && locOrPer.equals("person")){
			rInfo = RequestMoreInfo.findAllSeenSch(users);
			sList = ScheduleTest.findAllAssigned(users);
   		
			rInfoAll = RequestMoreInfo.findAllByNotOpenLead(users);
			sListAll = ScheduleTest.findAllByNotOpenLead(users);
		}
   	
		for(RequestMoreInfo rMoreInfo:rInfo){
			if((rMoreInfo.requestDate.after(startD) && rMoreInfo.requestDate.before(endD)) || rMoreInfo.requestDate.equals(endD) || rMoreInfo.requestDate.equals(startD)){
				requestLeadCount++;
			}
		}
   	
		for(ScheduleTest sTest:sList){
			if((sTest.scheduleDate.after(startD) && sTest.scheduleDate.before(endD)) || sTest.scheduleDate.equals(endD) || sTest.scheduleDate.equals(startD)){
				scheduleLeadCount++;
			}
		}

		for(RequestMoreInfo rMoreInfo:rInfoAll){
			if((rMoreInfo.requestDate.after(startD) && rMoreInfo.requestDate.before(endD)) || rMoreInfo.requestDate.equals(endD) || rMoreInfo.requestDate.equals(startD)){
				requestLeadCount1++;
			}
		}
   	
		for(ScheduleTest sTest:sListAll){
			if((sTest.scheduleDate.after(startD) && sTest.scheduleDate.before(endD)) || sTest.scheduleDate.equals(endD) || sTest.scheduleDate.equals(startD)){
				scheduleLeadCount1++;
			}
		}

		int countLeads1 = requestLeadCount1 + scheduleLeadCount1;
		int countLeads = requestLeadCount + scheduleLeadCount;
		Location location = Location.findById(locationId);
   
		if(locOrPer.equals("location")){
			lDataVM.imageUrl = location.getImageUrl();
		}else if(locOrPer.equals("person")){
			lDataVM.imageUrl = users.imageUrl;
			lDataVM.imageName = users.imageName;
		}
   	
		lDataVM.countSalePerson = countLeads;
   	
		if(users.role.equals("Sales Person") || locOrPer.equals("person")){
			rInfoTest = RequestMoreInfo.testDriveForSalePerson(users);
			sListTest = ScheduleTest.testDriveForSalePerson(users);
			for(RequestMoreInfo rMoreInfo:rInfoTest){
   	   			if((rMoreInfo.requestDate.after(startD) && rMoreInfo.requestDate.before(endD)) || rMoreInfo.requestDate.equals(endD) || rMoreInfo.requestDate.equals(startD)){
   	   				requestLeadCountTest++;
   	   			}
   	   		}
   	   	
   	   		for(ScheduleTest sTest:sListTest){
   	   			if((sTest.scheduleDate.after(startD) && sTest.scheduleDate.before(endD)) || sTest.scheduleDate.equals(endD) || sTest.scheduleDate.equals(startD)){
   	   				scheduleLeadCountTest++;
   	   			}
   	   		}
		}
   	
		int countTestDrives = requestLeadCountTest + scheduleLeadCountTest;
		lDataVM.countTestDrives=countTestDrives;
   	
		Integer monthPriceCount = 0;
		Integer pricecount = 0;
		int saleCarCount = 0;
		if(users.role.equals("Manager") && locOrPer.equals("location") || users.role.equals("Sales Person") && locOrPer.equals("location")){
			List<SoldInventory> pList = SoldInventory.findBySold();
       	
			for(SoldInventory proList:pList){
				if((proList.soldDate.after(startD) && proList.soldDate.before(endD)) || proList.soldDate.equals(endD) || proList.soldDate.equals(startD)){
					saleCarCount++;
           			pricecount = (int) (pricecount + Integer.parseInt(proList.price));
           		}
				if(monthCal.equals(onlyMonth.format(proList.soldDate))){
					monthPriceCount = (int) (monthPriceCount + Integer.parseInt(proList.price));
				}
			}
			if(countLeads1 != 0 ){
				double sucessCount= (double)saleCarCount/(double)countLeads1*100;
				lDataVM.successRate = (int) sucessCount;
			}else{
				lDataVM.successRate = 0;
			}
       	
		}else if(users.role.equals("Sales Person") && locOrPer.equals("person")){
   		
			List<RequestMoreInfo> rInfo1 = RequestMoreInfo.findAllSeenComplete(users);
			List<ScheduleTest> sList1 = ScheduleTest.findAllSeenComplete(users);
   		
			List<SoldInventory> pList = SoldInventory.findBySoldUserAndSold(users);
			for (SoldInventory product : pList) {
				if((product.soldDate.after(startD) && product.soldDate.before(endD)) || product.soldDate.equals(endD) || product.soldDate.equals(startD)) {
					saleCarCount++;
					pricecount = (int) (pricecount + Integer.parseInt(product.price));
				}
				if(monthCal.equals(onlyMonth.format(product.soldDate))){
					monthPriceCount = (int) (monthPriceCount + Integer.parseInt(product.price));
				}
			}
			if(countLeads1 != 0){
				double sucessCount= (double)saleCarCount/(double)countLeads1*100;
   	   			lDataVM.successRate = (int) sucessCount;
			}else{
   	   			lDataVM.successRate = 0;
			}
		}
   	
		lDataVM.totalSalePrice =  pricecount;
		lDataVM.totalsaleCar = saleCarCount;
   	
		double valAvlPrice= ((double)pricecount/(double)saleCarCount);
		lDataVM.angSalePrice = (int) valAvlPrice;
   	
     	if(users.role.equals("Manager") && locOrPer.equals("location") || users.role.equals("Sales Person") && locOrPer.equals("location")){
     		PlanScheduleMonthlyLocation pMonthlyLocation = PlanScheduleMonthlyLocation.findByLocationAndMonth(Location.findById(locationId), monthCal);
     		if(pMonthlyLocation != null){
     			double val= ((double)pricecount/Double.parseDouble(pMonthlyLocation.totalEarning));
     			lDataVM.AngSale = (int) (val*100);
     		}
       	
     		}else if(users.role.equals("Sales Person") && locOrPer.equals("person")){
     			PlanScheduleMonthlySalepeople  pMonthlySalepeople = PlanScheduleMonthlySalepeople.findByUserMonth(users, monthCal); 
     			if(pMonthlySalepeople != null){
     				if(pMonthlySalepeople.totalBrought != null){
     					double val= ((double)pricecount/Double.parseDouble(pMonthlySalepeople.totalBrought));
         				lDataVM.AngSale = (int) (val*100);
     				}
     			}
     		}
   	
     		List<AddCollection> allProdList = AddCollection.findByLocation(location.id);
     		int saleCar = 0;
     		int newCar = 0;
     		for(AddCollection product:allProdList){
     			if(product.status.equals("Sold")){
     				if((product.soldDate.after(startD) && product.soldDate.before(endD)) || product.soldDate.equals(endD) || product.soldDate.equals(startD)){
     					saleCar++;
     				}
     			}
   				newCar++;
     		}

     		List<LeadsDateWise> lDateWises = LeadsDateWise.getAllVehicles(users);
     		for(LeadsDateWise lWise:lDateWises){
     			if(lWise.goalSetTime !=null){
     				if(lWise.goalSetTime.equals("1 week") ){
     					cal.setTime(lWise.leadsDate);  
     					cal.add(Calendar.DATE, 7);
     					Date m =  cal.getTime(); 
     					if(m.after(dateobj)) {
     						lDataVM.leads = lWise.leads;
     						lDataVM.goalTime =lWise.goalSetTime;
     					}
   			
     					}else if(lWise.goalSetTime.equals("1 month") && lWise.goalSetTime !=null){
     						cal.setTime(lWise.leadsDate);  
     						cal.add(Calendar.DATE, 30);
     						Date m =  cal.getTime(); 
     						if(m.after(dateobj)) {
     							lDataVM.leads = lWise.leads;
     							lDataVM.goalTime =lWise.goalSetTime;
     						}
     					}
     				}
     			}
   	
     			List<DateAndValueVM> sAndValues = new ArrayList<>();
     			List<Long> sAndLong = new ArrayList<>();
     			int countPlanCarSold = 0;
     			Calendar now = Calendar.getInstance();
     			String month = monthName[now.get(Calendar.MONTH)];
     			lDataVM.monthCurr = month;
     			PlanScheduleMonthlyLocation pLocation = null;
     			PlanScheduleMonthlySalepeople  pMonthlySalepeople = null;
     			if(locOrPer.equals("location")){
     				pLocation = PlanScheduleMonthlyLocation.findByLocationAndMonth(Location.findById(locationId), month);
     			}else{
     				pMonthlySalepeople = PlanScheduleMonthlySalepeople.findByUserMonth(users, month);
     			}
			
     			if(!locOrPer.equals("location")){
     				List<AuthUser> allLoUser = AuthUser.findByLocatio(Location.findById(locationId));
     				List<Integer> priceList = new ArrayList<>();
     				int pricecountOther = 0;
     				for(AuthUser aUser: allLoUser){
     					pricecountOther = 0;
     					if(allLoUser != null){
     						List<AddCollection> pList = AddCollection.findBySoldUserAndSold(aUser);
     						for (AddCollection product : pList) {
     							if(monthCal.equals(onlyMonth.format(product.soldDate))){
     								pricecountOther = (int) (pricecountOther + product.price);
     							}
     						}
     						priceList.add(pricecountOther);
     					}
     				}
     				Collections.sort(priceList, Collections.reverseOrder());
     				List<Integer> longV = new ArrayList<>();
     				DateAndValueVM sValue = new DateAndValueVM();
     				sValue.name = "Highest Result";
     				longV.add(priceList.get(0));
     				sValue.data = longV;
     				sAndValues.add(sValue);
     			}
   	
     			List<AddCollection> pList2 = AddCollection.findByLocationAndSold(locationId);
     			if(pList2.size() != 0){
     				List<Integer> longV = new ArrayList<>();
     				DateAndValueVM sValue = new DateAndValueVM();
     				if(locOrPer.equals("location"))
     					sValue.name = "Currently Earned";
     				else	
     					sValue.name = "Your Result";
     					longV.add(monthPriceCount);
     					sValue.data = longV;
     					sAndValues.add(sValue);
     			}
   	
     			lDataVM.sendData = sAndValues;
   			}
    
    
    
    /*-----------------------------------------*/
   
   public static Result getGraphdata(String planValue, String locat){
	   DateAndValueVM sAndValues = new DateAndValueVM();
	   AuthUser user = (AuthUser) getLocalUser();
	   Location loc = Location.findById(Long.parseLong(session("USER_LOCATION")));
	   List<Integer>list = new ArrayList<>();
	   DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	   DateFormat onlyMonth = new SimpleDateFormat("MMMM");
	   Calendar cal1 = Calendar.getInstance();
	   String[] monthName = { "January", "February", "March", "April", "May", "June", "July",
			        "August", "September", "October", "November", "December" };
	   Date dateobj = new Date();
	   Calendar cal = Calendar.getInstance();  
	   String monthCal = monthName[cal.get(Calendar.MONTH)];
	   sAndValues.name = "Highest Result";
	   sAndValues.price = "0";
	   sAndValues.plan = "0";
	   sAndValues.data = list;
	   int total = 0;
	   
	   Calendar c = Calendar.getInstance();
	    int year = c.get(Calendar.YEAR);
	    int month = c.get(Calendar.MONTH);
	    int day = 1;
	    c.set(year, month, day);
	    int numOfDaysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
	    Date startDate = c.getTime();
	    c.add(Calendar.DAY_OF_MONTH, numOfDaysInMonth-1);
	    Date endDate = c.getTime();
	   
	   if(!locat.equals("location")){
		   PlanScheduleMonthlySalepeople planSchedule = PlanScheduleMonthlySalepeople.findByUserMonth(user,monthCal);
		   if(planSchedule != null){
			   if("Leads Closed".equalsIgnoreCase(planValue)){
				   if(planSchedule.vehicalesToSell != null){
					   sAndValues.price = planSchedule.vehicalesToSell;
					   sAndValues.plan = planSchedule.vehicalesToSell;
				   }
				 //calculate total lead closed
				   List<AddCollection> totalCount = AddCollection.findBySoldUserAndSoldDate(user,startDate,endDate);
				   total = totalCount.size();
			   }
			   if("Leads to Generate".equalsIgnoreCase(planValue)){
				   if(planSchedule.leadsToGenerate != null){
					   sAndValues.price = planSchedule.leadsToGenerate;
					   sAndValues.plan = planSchedule.leadsToGenerate;
				   }
				 //calculate total leads to generated
				   List<RequestMoreInfo> totalCount = RequestMoreInfo.findByUserAndDate(user,startDate,endDate);
				   total = totalCount.size();
			   }
			   if("Total Earning".equalsIgnoreCase(planValue)){
				   if(planSchedule.totalBrought != null){
					   sAndValues.price = planSchedule.totalBrought;
					   sAndValues.plan = planSchedule.totalBrought;
				   }
				 //calculate total leads to generated
				   total = 50;
			   }
		   }
	   }else{
		   PlanScheduleMonthlyLocation planSchedule = PlanScheduleMonthlyLocation.findByLocationAndMonth(loc, monthCal);
		   if(planSchedule != null){
			   if("Total Earning".equalsIgnoreCase(planValue)){
				   if(planSchedule.totalEarning != null){
					   sAndValues.price = planSchedule.totalEarning;
					   sAndValues.plan = planSchedule.totalEarning;
				   }
				 //calculate total leads to generated
				   total = 50;
			   }
			   if("Leads Closed".equalsIgnoreCase(planValue)){
				   if(planSchedule.vehiclesSell != null){
					   sAndValues.price = planSchedule.vehiclesSell;
					   sAndValues.plan = planSchedule.vehiclesSell;
				   }
				 //calculate total lead closed
				   List<AddCollection> totalCount = AddCollection.findByLocationAndSoldDate(loc,startDate,endDate);
				   total = totalCount.size();
			   }
			   if("Leads to Generate".equalsIgnoreCase(planValue)){
				   if(planSchedule.leadsToGenerate != null){
					   sAndValues.price = planSchedule.leadsToGenerate;
					   sAndValues.plan = planSchedule.leadsToGenerate;
				   }
				 //calculate total leads to generated
				   List<RequestMoreInfo> totalCount = RequestMoreInfo.findByLocationAndDate(loc,startDate,endDate);
				   total = totalCount.size();
			   }
		   }
	   }
	   list.add(total);
	   return ok(Json.toJson(sAndValues));
   }
   
   public static Result getPlanWiseGraph(String planValue, String locat){
	   List<DateAndValueVM> sAndValues = new ArrayList<>();
	   if(!locat.equals("location")){
			List<AuthUser> allLoUser = AuthUser.findByLocatio(Location.findById(Long.parseLong(session("USER_LOCATION"))));
	    	List<Integer> priceList = new ArrayList<>();
	    	int pricecountOther = 0;
	    	for(AuthUser aUser: allLoUser){
	    		pricecountOther = 0;
	    		if(allLoUser != null){
	    			List<AddCollection> pList = AddCollection.findBySoldUserAndSold(aUser);
	        		/*for (AddCollection product : pList) {
	        			 productCount++;
	    				if(monthCal.equals(onlyMonth.format(product.soldDate))){
	    					pricecountOther = (int) (pricecountOther + product.price);
	    				}
	    			}*/
	        		priceList.add(pList.size());
	        	}
	    	}
	    	Collections.sort(priceList, Collections.reverseOrder());
	    	List<Integer> longV = new ArrayList<>();
			DateAndValueVM sValue = new DateAndValueVM();
			sValue.name = "Highest Result";
			longV.add(priceList.get(0));
			sValue.data = longV;
			sAndValues.add(sValue);
  }
		 return ok(Json.toJson(sAndValues));
		 
	 }
   
   
   
 public static Result gmLocationManager(Long locationId){
	 AuthUser auUser = AuthUser.getlocationAndManagerOne(Location.findById(locationId));
	 return ok(Json.toJson(auUser));
	 
 }

 public static Result getUserLocationInfo(Integer userkey,String timeSet,String locOrPer){
	 AuthUser users = AuthUser.findById(userkey);
	 LocationWiseDataVM lDataVM = new LocationWiseDataVM();
	 findStatistics(timeSet,locOrPer,users.location.id,users, lDataVM);
 	return ok(Json.toJson(lDataVM));
 }
 
    public static void findStatistics(String timeSet,String locOrPer,Long locationId,AuthUser users, LocationWiseDataVM lDataVM){
    	
    	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    	DateFormat onlyMonth = new SimpleDateFormat("MMMM");
    	Calendar cal1 = Calendar.getInstance();
    	String[] monthName = { "January", "February", "March", "April", "May", "June", "July",
		        "August", "September", "October", "November", "December" };
    	Date dateobj = new Date();
    	Calendar cal = Calendar.getInstance();  
    	String monthCal = monthName[cal.get(Calendar.MONTH)];
    	Map<String, Integer> mapCar = new HashMap<String, Integer>();
    	
    	Location loc = Location.findById(Long.parseLong(session("USER_LOCATION")));
   	 	List<AuthUser> authUser = AuthUser.findByLocatioUsers(loc);
    	List<Vehicle> vehicle1 = Vehicle.findByLocationAndSold(Long.parseLong(session("USER_LOCATION")));
   	    int flagForSale=0;	
   	    lDataVM.flagForBestSaleIcon=flagForSale;
    	for(Vehicle vehicle:vehicle1){
    		if(vehicle != null){
    			flagForSale=1;
    			lDataVM.flagForBestSaleIcon=flagForSale;
    			break;
    		}
    		else{
    			flagForSale=0;
    			lDataVM.flagForBestSaleIcon=flagForSale;
    		}
    	}
    	Date timeBack = null;
    	if(timeSet.equals("Week")){
    		cal1.setTime(dateobj);  
			cal1.add(Calendar.DATE, -7);
			timeBack =  cal1.getTime(); 
    	}else if(timeSet.equals("week")){
    		cal1.setTime(dateobj);  
			cal1.add(Calendar.DATE, -7);
			timeBack =  cal1.getTime(); 
    	}else if(timeSet.equals("Month")){
    		cal1.setTime(dateobj);  
			cal1.add(Calendar.MONTH, -1);
			timeBack =  cal1.getTime(); 
    	}else if(timeSet.equals("Quater")){
    		cal1.setTime(dateobj);  
			cal1.add(Calendar.MONTH, -3);
			timeBack =  cal1.getTime(); 
    	}else if(timeSet.equals("6 Month")){
    		cal1.setTime(dateobj);  
			cal1.add(Calendar.MONTH, -6);
			timeBack =  cal1.getTime(); 
    	}else if(timeSet.equals("Year")){
    		cal1.setTime(dateobj);  
			cal1.add(Calendar.MONTH, -12);
			timeBack =  cal1.getTime(); 
    	}
    	
    	int requestLeadCount = 0;
    	int scheduleLeadCount = 0;
    	int tradeInLeadCount = 0;
    	int requestLeadCount1 = 0;
    	int scheduleLeadCount1 = 0;
    	int tradeInLeadCount1 = 0;
    	List<RequestMoreInfo> rInfo = null;
    	List<ScheduleTest> sList = null;
    	List<TradeIn> tradeIns = null;
    	List<RequestMoreInfo> rInfoAll = null;
    	List<ScheduleTest> sListAll = null;
    	List<TradeIn> tradeInsAll = null;
    	
    	if((users.role.equals("Manager") && locOrPer.equals("location"))){
    		rInfo = RequestMoreInfo.findAllSeenLocationSch(locationId);
    		sList = ScheduleTest.findAllAssignedLocation(locationId);
    		tradeIns = TradeIn.findAllSeenLocationSch(locationId);
    		
    		rInfoAll = RequestMoreInfo.findByLocation(locationId);
    		sListAll = ScheduleTest.findByLocation(locationId);
    		tradeInsAll = TradeIn.findByLocation(locationId);
    		
    	}else if(users.role.equals("Sales Person") || locOrPer.equals("person")){
    		rInfo = RequestMoreInfo.findAllSeenSch(users);
    		sList = ScheduleTest.findAllAssigned(users);
    		tradeIns = TradeIn.findAllSeenSch(users);
    		
    		rInfoAll = RequestMoreInfo.findAllByNotOpenLead(users);
    		sListAll = ScheduleTest.findAllByNotOpenLead(users);
    		tradeInsAll = TradeIn.findAllByNotOpenLead(users);
    	}
    	
    	for(RequestMoreInfo rMoreInfo:rInfo){
    		if(rMoreInfo.requestDate != null){
    		  if(rMoreInfo.requestDate.after(timeBack)) {
    			requestLeadCount++;
    		 }
    		}
    	}
    	for(ScheduleTest sTest:sList){
    		if(sTest.scheduleDate != null){
    	    	if(sTest.scheduleDate.after(timeBack)) {
    	    		scheduleLeadCount++;
    	    	}
    		}
    	}
    	for(TradeIn tIn:tradeIns){
    		if(tIn.tradeDate != null){
    			if(tIn.tradeDate.after(timeBack)) {
    				tradeInLeadCount++;
    			}
    		}
    	}
    	for(RequestMoreInfo rMoreInfo:rInfoAll){
    		if(rMoreInfo.requestDate != null){
    		  if(rMoreInfo.requestDate.after(timeBack)) {
    			requestLeadCount1++;
    		 }
    		}
    	}
    	for(ScheduleTest sTest:sListAll){
    		if(sTest.scheduleDate != null){
    			if(sTest.scheduleDate.after(timeBack)) {
    				scheduleLeadCount1++;
    			}
    		}
    	}
    	for(TradeIn tIn:tradeInsAll){
    		if(tIn.tradeDate != null){
    			if(tIn.tradeDate.after(timeBack)) {
    				tradeInLeadCount1++;
    			}
    		}
    	}
    	int countLeads1 = requestLeadCount1 + scheduleLeadCount1 + tradeInLeadCount1;
    	int countLeads = requestLeadCount + scheduleLeadCount + tradeInLeadCount;
    	Location location = Location.findById(locationId);
    	
    	if(locOrPer.equals("location")){
    		lDataVM.imageUrl = location.getImageUrl();
    	}else if(locOrPer.equals("person")){
    		lDataVM.imageUrl = users.getImageUrl();
    	}
    	lDataVM.countSalePerson = countLeads;
    	
    	Integer pricecount = 0;
    	Integer monthPriceCount = 0;
    	int saleCarCount = 0;
    	if((users.role.equals("Manager") && locOrPer.equals("location"))){
    		List<Vehicle> vList = Vehicle.findByLocationAndSold(location.id);
        	for(Vehicle vehList:vList){
        			if(vehList.soldDate.after(timeBack)) {
            			saleCarCount++;
            			pricecount = pricecount + vehList.price;
            		}
        			if(monthCal.equals(onlyMonth.format(vehList.soldDate))){
            			monthPriceCount = monthPriceCount + vehList.price;
            		}
        	}
        	if(countLeads1 != 0 && saleCarCount != 0){
        		double sucessCount= (double)saleCarCount/(double)countLeads1*100;
            	lDataVM.successRate = (int) sucessCount;
        	}else{
        		lDataVM.successRate = 0;
        	}
    	}else if(users.role.equals("Sales Person") || locOrPer.equals("person")){
    		
    		List<RequestMoreInfo> rInfo1 = RequestMoreInfo.findAllSeenComplete(users);
    		List<ScheduleTest> sList1 = ScheduleTest.findAllSeenComplete(users);
    		List<TradeIn> tradeIns1 = TradeIn.findAllSeenComplete(users);
    		List<Vehicle> vList = Vehicle.findBySoldUserAndSold(users);
    		for (Vehicle vehicle : vList) {
    			if(vehicle.soldDate.after(timeBack)) {
        			saleCarCount++;
        			pricecount = pricecount + vehicle.price;
				}
				if(monthCal.equals(onlyMonth.format(vehicle.soldDate))){
					monthPriceCount = monthPriceCount + vehicle.price;
				}
			}
    		double sucessCount= (double)saleCarCount/(double)countLeads1*100;
    		lDataVM.successRate = (int) sucessCount;
    	}
    	
    	lDataVM.totalSalePrice =  pricecount;
    	lDataVM.totalsaleCar = saleCarCount;
    	
    	double valAvlPrice= ((double)pricecount/(double)saleCarCount);
    	lDataVM.angSalePrice = (int) valAvlPrice;
    	
      	if((users.role.equals("Manager") && locOrPer.equals("location"))){
      		PlanScheduleMonthlyLocation pMonthlyLocation = PlanScheduleMonthlyLocation.findByLocationAndMonth(Location.findById(locationId), monthCal);
        	if(pMonthlyLocation != null){
        		double val= ((double)pricecount/Double.parseDouble(pMonthlyLocation.totalEarning));
            	lDataVM.AngSale = (int) (val*100);
        	}
    	}else if(users.role.equals("Sales Person") || locOrPer.equals("person")){
    		PlanScheduleMonthlySalepeople  pMonthlySalepeople = PlanScheduleMonthlySalepeople.findByUserMonth(users, monthCal); 
        	if(pMonthlySalepeople != null){
        		if(pMonthlySalepeople.totalBrought != null){
        			double val= ((double)pricecount/Double.parseDouble(pMonthlySalepeople.totalBrought));
                	lDataVM.AngSale = (int) (val*100);
        		}
        	}
    	}
    	List<Vehicle> allVehiList = Vehicle.findByLocation(location.id);
    	int saleCar = 0;
    	int newCar = 0;
    	for(Vehicle vehicle:allVehiList){
    		if(vehicle.status.equals("Sold")){
    			if(vehicle.soldDate.after(timeBack)) {
    				saleCar++;
    			}
    		}//else if(vehicle.status.equals("Newly Arrived")){
    			newCar++;
    	//	}
    	}
    	List<LeadsDateWise> lDateWises = LeadsDateWise.getAllVehicles(users);
    	for(LeadsDateWise lWise:lDateWises){
    		
    		if(lWise.goalSetTime.equals("1 week")){
    			cal.setTime(lWise.leadsDate);  
    			cal.add(Calendar.DATE, 7);
    			Date m =  cal.getTime(); 
    			if(m.after(dateobj)) {
    				lDataVM.leads = lWise.leads;
    				lDataVM.goalTime =lWise.goalSetTime;
    				
    			}
    			
    		}else if(lWise.goalSetTime.equals("1 month")){
    			cal.setTime(lWise.leadsDate);  
    			cal.add(Calendar.DATE, 30);
    			Date m =  cal.getTime(); 
    			if(m.after(dateobj)) {
    				lDataVM.leads = lWise.leads;
    				lDataVM.goalTime =lWise.goalSetTime;
    			}
    		}
    		
    	}
    	
    	List<DateAndValueVM> sAndValues = new ArrayList<>();
    	List<Long> sAndLong = new ArrayList<>();
    	int countPlanCarSold = 0;
		Calendar now = Calendar.getInstance();
		String month = monthName[now.get(Calendar.MONTH)];
		lDataVM.monthCurr = month;
		
		PlanScheduleMonthlyLocation pLocation = null;
		PlanScheduleMonthlySalepeople  pMonthlySalepeople = null;
		if(locOrPer.equals("location")){
			pLocation = PlanScheduleMonthlyLocation.findByLocationAndMonth(Location.findById(locationId), month);
		}else{
			pMonthlySalepeople = PlanScheduleMonthlySalepeople.findByUserMonth(users, month);
		}
		if(!locOrPer.equals("location")){
    	List<AuthUser> allLoUser = AuthUser.findByLocatio(Location.findById(locationId));
    	List<Integer> priceList = new ArrayList<>();
    	int pricecountOther = 0;
    	for(AuthUser aUser: allLoUser){
    		pricecountOther = 0;
    		if(allLoUser != null){
        		
    			List<Vehicle> vList = Vehicle.findBySoldUserAndSold(aUser);
        		for (Vehicle vehicle : vList) {
    				if(monthCal.equals(onlyMonth.format(vehicle.soldDate))){
    					pricecountOther = pricecountOther + vehicle.price;
    				}
    			}
        		priceList.add(pricecountOther);
        	}
    	}
    	Collections.sort(priceList, Collections.reverseOrder());
    	List<Integer> longV = new ArrayList<>();
		DateAndValueVM sValue = new DateAndValueVM();
		sValue.name = "Highest Result";
		longV.add(priceList.get(0));
		sValue.data = longV;
		sAndValues.add(sValue);
    }
    	
    	List<Vehicle> vList2 = Vehicle.findByLocationAndSold(locationId);
    	
    	if(vList2.size() != 0){
    		List<Integer> longV = new ArrayList<>();
    		DateAndValueVM sValue = new DateAndValueVM();
    		if(locOrPer.equals("location"))
    			sValue.name = "Currently Earned";
    		else	
			sValue.name = "Your Result";
			longV.add(monthPriceCount);
			sValue.data = longV;
			sAndValues.add(sValue);
    	}
    	
    	lDataVM.sendData = sAndValues;
    }
   
    
    public static Result getAllLocation(String timeSet) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		
    		Date dateobj = new Date();
        	Calendar cal = Calendar.getInstance();  
    		
    		Date timeBack = null;
        	
        	if(timeSet.equals("Week")){
        		cal.setTime(dateobj);  
    			cal.add(Calendar.DATE, -7);
    			timeBack =  cal.getTime(); 
        	}else if(timeSet.equals("Month")){
        		cal.setTime(dateobj);  
    			cal.add(Calendar.MONTH, -1);
    			timeBack =  cal.getTime(); 
        	}else if(timeSet.equals("Quater")){
        		cal.setTime(dateobj);  
    			cal.add(Calendar.MONTH, -3);
    			timeBack =  cal.getTime(); 
        	}else if(timeSet.equals("6 Month")){
        		cal.setTime(dateobj);  
    			cal.add(Calendar.MONTH, -6);
    			timeBack =  cal.getTime(); 
        	}else if(timeSet.equals("Year")){
        		cal.setTime(dateobj);  
    			cal.add(Calendar.MONTH, -12);
    			timeBack =  cal.getTime(); 
        	}
    		
    		Integer totalPrice = 0;
    		List<AuthUser> userList = AuthUser.getUserByType();
    		List<LocationVM> vmList = new ArrayList<>();
    		List<Location> locationList = Location.findAllActiveType();
    		for(Location location : locationList) {
    			totalPrice = 0;
    			LocationVM vm = new LocationVM();
    			vm.id = location.id;
    			vm.locationaddress = location.address;
    			vm.locationemail = location.email;
    			vm.locationName = location.name;
    			vm.locationphone = location.phone;
    			vm.imageName = location.imageName;
    			vm.imageUrl = location.imageUrl;
    			String roles = "Manager";
    			AuthUser users = null;
    			users = AuthUser.getlocationAndManagerByType(location, roles);
    				if(location.manager != null){
    					users = AuthUser.findById(location.manager.id);
    					if(users != null){
    						vm.gmIsManager = 1; 
    					}
    				}else{
    					vm.gmIsManager = 0; 
    				}
    					
    			
    			
    			if(users != null){
	    			vm.managerId = users.id;
	    			vm.email = users.email;
	    			vm.firstName = users.firstName;
	    			vm.lastName = users.lastName;
	    			vm.phone = users.phone;
	    			vm.managerFullName = users.firstName+""+users.lastName;
	    			vm.mImageName = users.imageName;
	    			vm.mImageUrl = users.imageUrl;
    			}
    			List<AuthUser> saleUsers = AuthUser.getAllUserByLocation(location);
    			vm.salePeopleUserLocation = saleUsers.size();
    			List<Vehicle> vList = Vehicle.findByLocationAndSold(location.id);
    			int carCount = 0;
    			for(Vehicle vehicle:vList){
    				if(vehicle.soldDate.after(timeBack)){
    					totalPrice = totalPrice + vehicle.price; 
    					carCount++;
    				}
    			}
    			vm.carSoldLocation = carCount;
    			vm.totalMoneyBrougthLocation = totalPrice;
    			if(totalPrice != 0 && vm.carSoldLocation != 0){
    				Double values = (double) vm.totalMoneyBrougthLocation / (double) vm.carSoldLocation;
    				vm.avgSaleLocation = values;
    			}
    			
    			List<Vehicle> vListAllSold = Vehicle.findBySold();
    			int totalPriceForAllLocation = 0;
    			for(Vehicle vehicle:vListAllSold){
    				if(vehicle.soldDate.after(timeBack)){
    					totalPriceForAllLocation = totalPriceForAllLocation + vehicle.price; 
    				}
    			}
    			if(totalPrice != 0 && vm.carSoldLocation != 0){
    				vm.percentOfMoney = (double)(totalPrice / totalPriceForAllLocation) * 100;
    			}
    			
    			
    			List<RequestMoreInfo> rInfoAll = RequestMoreInfo.findByLocation(location.id);
    			List<ScheduleTest> sListAll = ScheduleTest.findByLocation(location.id);
    			List<TradeIn> tradeInsAll = TradeIn.findByLocation(location.id);
    			
    			int totalLead = 0;
    			
    			for(RequestMoreInfo rInfo:rInfoAll){
    				if(rInfo.requestDate.after(timeBack)){
    					totalLead++;
    				}
    			}
    			
    			for(ScheduleTest sInfo:sListAll){
    				if(sInfo.scheduleDate.after(timeBack)){
    					totalLead++;
    				}
    			}
    			
    			for(TradeIn tInfo:tradeInsAll){
    				if(tInfo.tradeDate.after(timeBack)){
    					totalLead++;
    				}
    			}
    			
    			if(carCount != 0 && totalLead != 0){
    				double value = (double)carCount / (double)totalLead;
    				vm.successRate = value * 100;
    			}
    			
    			int total = 0;
    			String[] monthName = { "January", "February", "March", "April", "May", "June", "July",
				        "August", "September", "October", "November", "December" };
		    	
		     	String crMonth = monthName[Calendar.MONTH];
		     	
				PlanScheduleMonthlyLocation  pMonthlyLocation = null;
				pMonthlyLocation = PlanScheduleMonthlyLocation.findByLocationAndMonth(location, crMonth);
				
				if(pMonthlyLocation != null){
					total = Integer.parseInt(pMonthlyLocation.totalEarning);
		    	}
				if(total > 0 && totalPrice > 0){
					Double PlanPer =  (double)((totalPrice*100)/total);
					vm.PlanPer = PlanPer;		
				}
    			
    			
    				vmList.add(vm);
    		}
    		return ok(Json.toJson(vmList));
    	}
    }
    
    public static Result getAllSalesUsers(){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
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
    }
    
    public static Result deleteUserById(Integer id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		
    		AuthUser user = AuthUser.findById(id);
    		//Location location = Location.findById(id);
    		List<RequestMoreInfo> rInfo = RequestMoreInfo.findAllByAssignedUser(user);
    		List<ScheduleTest> sTest = ScheduleTest.findAllByUserAssigned(user);
    		List<TradeIn> tIns = TradeIn.findAllByAssignedUser(user);
    		
    		for(RequestMoreInfo rMoreInfo:rInfo){
    			rMoreInfo.setStatus("UNCLAIMED");
    			rMoreInfo.setAssignedTo(null);
    			rMoreInfo.update();
    		}
    		
    		for(ScheduleTest schTest:sTest){
    			schTest.setLeadStatus("UNCLAIMED");
    			schTest.setAssignedTo(null);
    			schTest.update();
    		}
    		
    		for(TradeIn trIn:tIns){
    			trIn.setStatus("UNCLAIMED");
    			trIn.setAssignedTo(null);
    			trIn.update();
    		}
    		
    		user.delete();
    		return ok();
    	}
    }
    
    
    public static Result setVehicleStatus() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		Date currDate = new Date();
    		AuthUser user = (AuthUser) getLocalUser();
    		List<String> emailList = new ArrayList<>();
    		Form<SoldContactVM> form = DynamicForm.form(SoldContactVM.class).bindFromRequest();
    		SoldContactVM vm = form.get();
    		Date date = new Date();
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    		SoldContact contact = new SoldContact();
    		contact.name = vm.name;
    		contact.email = vm.email;
    		contact.phone = vm.phone;
    		contact.gender = vm.gender;
    		contact.age = vm.age;
    		contact.buyingFor = vm.buyingFor;
    		contact.howContactedUs = vm.howContactedUs;
    		contact.howFoundUs = vm.howFoundUs;
    		contact.title = vm.title;
    		contact.year = vm.year;
    		contact.designer = vm.designer;
    		contact.price = vm.price;
    		contact.custZipCode = vm.custZipCode;
    		contact.enthicity = vm.enthicity;
    		contact.save();
    		Contacts contactsObj = new Contacts();
    		String arr[] = vm.name.split(" ");
    		if(arr.length >= 1) {
    			contactsObj.firstName = arr[0];
    		} else {
    			contactsObj.firstName = vm.name;
    		}
    		if(arr.length >= 2) {
    			contactsObj.middleName = arr[1];
    		}
    		if(arr.length >= 3) {
    			contactsObj.lastName = arr[2];
    		} 
    		contactsObj.creationDate = df.format(date);
    		contactsObj.email = vm.email;
    		contactsObj.phone = vm.phone;
    		contactsObj.custZipCode = vm.custZipCode;
    		contactsObj.enthicity = vm.enthicity;
    		contactsObj.newsLetter = 0;
    		contactsObj.user = user.id;
    		contactsObj.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		contactsObj.save();
    		
    		
    		Vehicle vehicle = Vehicle.findByVinAndStatus(vm.vin);
    		
    		if(vehicle != null){
	    		vehicle.setStatus("Sold");
	    		Date dates = new Date();
	    		vehicle.setSoldDate(dates);
	    		vehicle.setSoldUser(user);
	    		vehicle.setPrice(Integer.parseInt(vm.price));
	    		vehicle.update();
    		}
    		
    		if(vm.statusVal.equals("Sold")){
    			List<TradeIn> tIn = TradeIn.findByVinAndLocation(vm.vin, Location.findById(Long.parseLong(session("USER_LOCATION"))));
        		for(TradeIn tradeIn:tIn){
        			if(tradeIn.status == null){
        				tradeIn.setStatus("LOST");
        				tradeIn.setStatusDate(currDate);
        				tradeIn.setStatusTime(currDate);
        				tradeIn.update();
        				
        				UserNotes uNotes1 = new UserNotes();
        	    		uNotes1.setNote("Vehicle has been sold by another Sales Person");
        	    		uNotes1.setAction("Other");
        	    		uNotes1.createdDate = currDate;
        	    		uNotes1.createdTime = currDate;
        	    		//uNotes1.user = user;
        	    		uNotes1.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
        	    		uNotes1.tradeIn = TradeIn.findById(tradeIn.id);
        	    		uNotes1.save();
        			}else if(!tradeIn.status.equals("COMPLETE")){
        				tradeIn.setStatus("LOST");
        				tradeIn.setStatusDate(currDate);
        				tradeIn.setStatusTime(currDate);
        				tradeIn.update();
        				
        				UserNotes uNotes1 = new UserNotes();
        	    		uNotes1.setNote("Vehicle has been sold by another Sales Person");
        	    		uNotes1.setAction("Other");
        	    		uNotes1.createdDate = currDate;
        	    		uNotes1.createdTime = currDate;
        	    		//uNotes1.user = user;
        	    		uNotes1.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
        	    		uNotes1.tradeIn = TradeIn.findById(tradeIn.id);
        	    		uNotes1.save();
        			}
        			
        			if(tradeIn.assignedTo !=null){
        				AuthUser userObj = AuthUser.findById(tradeIn.assignedTo.id);
        				if(userObj !=null){
        					emailList.add(userObj.email);
        				}
        			}
        		}
        		
        		List<RequestMoreInfo> rInfos = RequestMoreInfo.findByVinAndLocation(vm.vin, Location.findById(Long.parseLong(session("USER_LOCATION"))));
        		for(RequestMoreInfo rMoreInfo:rInfos){
        			if(rMoreInfo.status == null){
        				rMoreInfo.setStatus("LOST");
        				rMoreInfo.setStatusDate(currDate);
        				rMoreInfo.setStatusTime(currDate);
        				rMoreInfo.update();
        				
        				UserNotes uNotes1 = new UserNotes();
        	    		uNotes1.setNote("Vehicle has been sold by another Sales Person");
        	    		uNotes1.setAction("Other");
        	    		uNotes1.createdDate = currDate;
        	    		uNotes1.createdTime = currDate;
        	    		//uNotes1.user = user;
        	    		uNotes1.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
        	    		uNotes1.tradeIn = TradeIn.findById(rMoreInfo.id);
        	    		uNotes1.save();
        			}else if(!rMoreInfo.status.equals("COMPLETE")){
        				rMoreInfo.setStatus("LOST");
        				rMoreInfo.setStatusDate(currDate);
        				rMoreInfo.setStatusTime(currDate);
        				rMoreInfo.update();
        				
        				UserNotes uNotes1 = new UserNotes();
        	    		uNotes1.setNote("Vehicle has been sold by another Sales Person");
        	    		uNotes1.setAction("Other");
        	    		uNotes1.createdDate = currDate;
        	    		uNotes1.createdTime = currDate;
        	    		//uNotes1.user = user;
        	    		uNotes1.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
        	    		uNotes1.tradeIn = TradeIn.findById(rMoreInfo.id);
        	    		uNotes1.save();
        			}
        			if(rMoreInfo.assignedTo !=null){
        				AuthUser userObj = AuthUser.findById(rMoreInfo.assignedTo.id);
        				if(userObj !=null){
        					emailList.add(userObj.email);
        				}
        			}
        		}
        		
        		
        		List<ScheduleTest> sTests = ScheduleTest.findByVinAndLocation(vm.vin, Location.findById(Long.parseLong(session("USER_LOCATION"))));
        		for(ScheduleTest scheduleTest:sTests){
        			if(scheduleTest.leadStatus == null){
        				scheduleTest.setLeadStatus("LOST");
        				scheduleTest.setStatusDate(currDate);
        				scheduleTest.setStatusTime(currDate);
        				scheduleTest.update();
        				
        				UserNotes uNotes1 = new UserNotes();
        	    		uNotes1.setNote("Vehicle has been sold by another Sales Person");
        	    		uNotes1.setAction("Other");
        	    		uNotes1.createdDate = currDate;
        	    		uNotes1.createdTime = currDate;
        	    		//uNotes1.user = user;
        	    		uNotes1.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
        	    		uNotes1.tradeIn = TradeIn.findById(scheduleTest.id);
        	    		uNotes1.save();
        			}else if(!scheduleTest.leadStatus.equals("COMPLETE")){
        				scheduleTest.setLeadStatus("LOST");
        				scheduleTest.setStatusDate(currDate);
        				scheduleTest.setStatusTime(currDate);
        				scheduleTest.update();
        				
        				UserNotes uNotes1 = new UserNotes();
        	    		uNotes1.setNote("Vehicle has been sold by another Sales Person");
        	    		uNotes1.setAction("Other");
        	    		uNotes1.createdDate = currDate;
        	    		uNotes1.createdTime = currDate;
        	    		//uNotes1.user = user;
        	    		uNotes1.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
        	    		uNotes1.tradeIn = TradeIn.findById(scheduleTest.id);
        	    		uNotes1.save();
        			}
        			if(scheduleTest.assignedTo !=null){
        				AuthUser userObj = AuthUser.findById(scheduleTest.assignedTo.id);
        				if(userObj !=null){
        					emailList.add(userObj.email);
        				}
        			}
        		}
    		}
    		
    		if(emailList.size()>0){
    			vehicleSoldEmail(emailList);
    		}
    		
    		return ok();
    	}
    }
    
    
   /* public static Result setVehicleAndScheduleStatus() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		
    		Date currDate = new Date();
    		List<String> emailList = new ArrayList<>();
    		AuthUser user = (AuthUser) getLocalUser();
    		Form<SoldContactVM> form = DynamicForm.form(SoldContactVM.class).bindFromRequest();
    		SoldContactVM vm = form.get();
    		SoldContact contact = new SoldContact();
    		contact.name = vm.name;
    		contact.email = vm.email;
    		contact.phone = vm.phone;
    		contact.gender = vm.gender;
    		contact.age = vm.age;
    		contact.buyingFor = vm.buyingFor;
    		contact.howContactedUs = vm.howContactedUs;
    		contact.howFoundUs = vm.howFoundUs;
    		contact.make = vm.make;
    		contact.year = vm.year;
    		contact.mileage = vm.mileage;
    		contact.price = vm.price;
    		contact.custZipCode = vm.custZipCode;
    		contact.enthicity = vm.enthicity;
    		contact.user = user;
    		contact.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		contact.save();
    		Contacts contactsObj = new Contacts();
    		String arr[] = vm.name.split(" ");
    		if(arr.length >= 1) {
    			contactsObj.firstName = arr[0];
    		} else {
    			contactsObj.firstName = vm.name;
    		}
    		if(arr.length >= 2) {
    			contactsObj.middleName = arr[1];
    		}
    		if(arr.length >= 3) {
    			contactsObj.lastName = arr[2];
    		} 
    		contactsObj.email = vm.email;
    		contactsObj.phone = vm.phone;
    		contactsObj.custZipCode = vm.custZipCode;
    		contactsObj.enthicity = vm.enthicity;
    		contactsObj.newsLetter = 0;
    		contactsObj.user = user.id;
    		contactsObj.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		contactsObj.save();
    		
    		String vinNo = null;
    		if(vm.typeOfLead.equals("Request More Info")){
    			RequestMoreInfo rInfo = RequestMoreInfo.findById(vm.infoId);
    			rInfo.setLeadStatus("COMPLETE");
    			rInfo.setStatus("COMPLETE");
    			rInfo.setStatusDate(currDate);
    			rInfo.setCustZipCode(vm.custZipCode);
    			rInfo.setEnthicity(vm.enthicity);
    			rInfo.update();
    			
    			UserNotes uNotes = new UserNotes();
        		uNotes.setNote("Vehicle Sold");
        		uNotes.setAction("Other");
        		uNotes.createdDate = currDate;
        		uNotes.createdTime = currDate;
        		uNotes.user = user;
        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
        		uNotes.requestMoreInfo = RequestMoreInfo.findById(rInfo.id);
        		uNotes.save();
    			
    			vinNo = rInfo.vin;
    		}else if(vm.typeOfLead.equals("Schedule Test Drive")){
    			ScheduleTest schedule = ScheduleTest.findById(vm.infoId);
        		//schedule.setLeadStatus("SUCCESSFUL");
        		schedule.setLeadStatus("COMPLETE");
        		schedule.setStatusDate(currDate);
        		schedule.setCustZipCode(vm.custZipCode);
        		schedule.setEnthicity(vm.enthicity);
        		schedule.update();
        		
        		UserNotes uNotes = new UserNotes();
        		uNotes.setNote("Vehicle Sold");
        		uNotes.setAction("Other");
        		uNotes.createdDate = currDate;
        		uNotes.createdTime = currDate;
        		uNotes.user = user;
        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
        		uNotes.scheduleTest = ScheduleTest.findById(schedule.id);
        		uNotes.save();
        		
        		vinNo = schedule.vin;
    		}else if(vm.typeOfLead.equals("Trade-In Appraisal")){
    			TradeIn tIn = TradeIn.findById(vm.infoId);
    			tIn.setLeadStatus("COMPLETE");
    			tIn.setStatus("COMPLETE");
    			tIn.setStatusDate(currDate);
    			tIn.setCustZipCode(vm.custZipCode);
    			tIn.setEnthicity(vm.enthicity);
    			tIn.update();
    			
    			UserNotes uNotes = new UserNotes();
        		uNotes.setNote("Vehicle Sold");
        		uNotes.setAction("Other");
        		uNotes.createdDate = currDate;
        		uNotes.createdTime = currDate;
        		uNotes.user = user;
        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
        		uNotes.tradeIn = TradeIn.findById(tIn.id);
        		uNotes.save();
    			vinNo = tIn.vin;
    		}
    		
    		Vehicle vehicle = Vehicle.findByVinAndStatus(vinNo);
    		Date date = new Date();
    		if(vehicle != null){
	    		vehicle.setStatus("Sold");
	    		
	    		vehicle.setSoldDate(date);
	    		vehicle.setSoldUser(user);
	    		vehicle.update();
    		}
    		
    		for(RequestInfoVM rMoreInfo: vm.parentChildLead){
    			if(rMoreInfo.status.equals("Sold")){
    				if(rMoreInfo.typeOfLead.equals("Request More Info")){
    					RequestMoreInfo requestMoreInfo = RequestMoreInfo.findById(rMoreInfo.id);
    					requestMoreInfo.setStatus("COMPLETE");
    					requestMoreInfo.update();
    					
    					Vehicle vehicle1 = Vehicle.findByVinAndStatus(requestMoreInfo.vin);
    		    		if(vehicle1 != null){
    			    		vehicle1.setStatus("Sold");
    			    		vehicle1.setSoldDate(date);
    			    		vehicle1.setSoldUser(user);
    			    		vehicle1.update();
    		    		}
    		    		
    		    		UserNotes uNotes = new UserNotes();
    		    		uNotes.setNote("Vehicle Sold");
    		    		uNotes.setAction("Other");
    		    		uNotes.createdDate = currDate;
    		    		uNotes.createdTime = currDate;
    		    		uNotes.user = user;
    		    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		    		uNotes.requestMoreInfo = RequestMoreInfo.findById(requestMoreInfo.id);
    		    		uNotes.save();
    		    		
    		    		lostLeadsFunction(requestMoreInfo.vin, currDate);
    				}else if(rMoreInfo.typeOfLead.equals("Schedule Test Drive")){
    					ScheduleTest schTest = ScheduleTest.findById(rMoreInfo.id);
    					schTest.setLeadStatus("COMPLETE");
    					schTest.update();
    					
    					Vehicle vehicle1 = Vehicle.findByVinAndStatus(schTest.vin);
    		    		if(vehicle1 != null){
    			    		vehicle1.setStatus("Sold");
    			    		vehicle1.setSoldDate(date);
    			    		vehicle1.setSoldUser(user);
    			    		vehicle1.update();
    		    		}
    		    		
    		    		UserNotes uNotes = new UserNotes();
    		    		uNotes.setNote("Vehicle Sold");
    		    		uNotes.setAction("Other");
    		    		uNotes.createdDate = currDate;
    		    		uNotes.createdTime = currDate;
    		    		uNotes.user = user;
    		    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		    		uNotes.requestMoreInfo = RequestMoreInfo.findById(schTest.id);
    		    		uNotes.save();
    		    		
    		    		lostLeadsFunction(schTest.vin, currDate);
    				}else if(rMoreInfo.typeOfLead.equals("Trade-In Appraisal")){
    					TradeIn tTest = TradeIn.findById(rMoreInfo.id);
    					tTest.setStatus("COMPLETE");
    					tTest.update();
    					
    					Vehicle vehicle1 = Vehicle.findByVinAndStatus(tTest.vin);
    		    		if(vehicle1 != null){
    			    		vehicle1.setStatus("Sold");
    			    		vehicle1.setSoldDate(date);
    			    		vehicle1.setSoldUser(user);
    			    		vehicle1.update();
    		    		}
    		    		
    		    		UserNotes uNotes = new UserNotes();
    		    		uNotes.setNote("Vehicle Sold");
    		    		uNotes.setAction("Other");
    		    		uNotes.createdDate = currDate;
    		    		uNotes.createdTime = currDate;
    		    		uNotes.user = user;
    		    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		    		uNotes.requestMoreInfo = RequestMoreInfo.findById(tTest.id);
    		    		uNotes.save();
    		    		
    		    		lostLeadsFunction(tTest.vin, currDate);
    				}
    			}else if(rMoreInfo.status.equals("Cancel")){
    				if(rMoreInfo.typeOfLead.equals("Request More Info")){
    					RequestMoreInfo requestMoreInfo = RequestMoreInfo.findById(rMoreInfo.id);
    					requestMoreInfo.setStatus("CANCEL");
    					requestMoreInfo.update();
    				}else if(rMoreInfo.typeOfLead.equals("Schedule Test Drive")){
    					ScheduleTest schTest = ScheduleTest.findById(rMoreInfo.id);
    					schTest.setLeadStatus("CANCEL");
    					schTest.update();
    				}else if(rMoreInfo.typeOfLead.equals("Trade-In Appraisal")){
    					TradeIn tTest = TradeIn.findById(rMoreInfo.id);
    					tTest.setStatus("CANCEL");
    					tTest.update();
    				}
    			}
    		}
    	
    		return ok();
    	}
    }*/
    
    public static Result setScheduleConfirmClose(Long id,String leadType){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser user = getLocalUser();
    		Date currDate = new Date();
    		String clientEmail = null;
    		if(leadType.equals("Schedule Test Drive")) {
    			ScheduleTest schedule = ScheduleTest.findById(id);
    			schedule.setConfirmDate(null);
    			schedule.setConfirmTime(null);
    			schedule.update();
    			clientEmail = schedule.email;
    			
        		UserNotes uNotes = new UserNotes();
        		uNotes.setNote("Test Drive has been canceled");
        		uNotes.setAction("Other");
        		uNotes.createdDate = currDate;
        		uNotes.createdTime = currDate;
        		uNotes.user = user;
        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
        		uNotes.scheduleTest = ScheduleTest.findById(schedule.id);
        		uNotes.save();
        		
        		/* for template mail */
        		Map map = new HashMap();
	    		map.put("email",clientEmail);
	    		map.put("vin", schedule.vin);
	    		map.put("uname", user.firstName+" "+user.lastName);
	    		map.put("uphone", user.phone);
	    		map.put("uemail", user.email);
	    		map.put("clientName",schedule.name);
				cancelTestDriveMail(map);
        		
        		
        		
    		} else if(leadType.equals("Request More Info")) {
    			RequestMoreInfo info = RequestMoreInfo.findById(id);
    			info.setConfirmDate(null);
    			info.setConfirmTime(null);
    			info.update();
    			clientEmail = info.email;
    			
        		UserNotes uNotes = new UserNotes();
        		uNotes.setNote("Test Drive has been canceled");
        		uNotes.setAction("Other");
        		uNotes.createdDate = currDate;
        		uNotes.createdTime = currDate;
        		uNotes.user = user;
        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
        		uNotes.requestMoreInfo = RequestMoreInfo.findById(info.id);
        		uNotes.save();
        		String comments="Test Drive has been canceled";
        		String subject = "Test Drive cancelled";
        	  sendEmail(clientEmail,subject,comments);
        		
    		} else if(leadType.equals("Trade-In Appraisal")) {
    			TradeIn info = TradeIn.findById(id);
    			info.setConfirmDate(null);
    			info.setConfirmTime(null);
    			info.update();
    			
    			clientEmail = info.email;
    			
        		UserNotes uNotes = new UserNotes();
        		uNotes.setNote("Test Drive has been canceled");
        		uNotes.setAction("Other");
        		uNotes.createdDate = currDate;
        		uNotes.createdTime = currDate;
        		uNotes.user = user;
        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
        		uNotes.tradeIn = TradeIn.findById(info.id);
        		uNotes.save();
        		String comments="Test Drive has been canceled";
        		String subject = "Test Drive cancelled";
        	  sendEmail(clientEmail,subject,comments);
    		}
    		
    		
    		return ok();
    	}
    }
    
    public static Result setScheduleStatusClose() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		Form<CloseLeadVM> form = DynamicForm.form(CloseLeadVM.class).bindFromRequest();
    		CloseLeadVM vm = form.get();
    		String clientEmail=null;
    		String clientPhone=null;
    		String clientName=null;
    		MultipartFormData bodys = request().body().asMultipartFormData();
    		AuthUser user = getLocalUser();
    		Date currDate = new Date();
    		
    		String comments="Test Drive has been canceled";
    		String subject = "Test Drive cancelled";
    		
    		//String arr[] = arrayString.split(",");
    		for(String value:vm.actionSelectedLead){
    			RequestMoreInfo info = RequestMoreInfo.findById(Long.parseLong(value));
    			
    			String vin=info.vin;
    			info.setStatus("CANCEL");
    			info.setStatusDate(currDate);
    			info.setStatusTime(currDate);
    			info.setReason(vm.reasonToCancel);
    			info.update();
    			clientEmail=info.email;
    			if(vm.customData != null){
    				saveCustomData(info.id,vm.customData,bodys,Long.parseLong(info.isContactusType));
    			}
    			
    			
    			if(info.confirmDate != null){
    				Map map = new HashMap();
		    		map.put("email",clientEmail);
		    		map.put("vin", vin);
		    		map.put("uname", user.firstName+" "+user.lastName);
		    		map.put("uphone", user.phone);
		    		map.put("uemail", user.email);
		    		map.put("clintName", info.name);
    				cancelTestDriveMail(map);
        	    	  //sendEmail(clientEmail,subject,comments);
        			}
    			
        		UserNotes uNotes = new UserNotes();
        		uNotes.setNote("Client didn't buy vehicle");
        		uNotes.setAction("Other");
        		uNotes.createdDate = currDate;
        		uNotes.createdTime = currDate;
        		uNotes.user = user;
        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
        		uNotes.requestMoreInfo = RequestMoreInfo.findById(info.id);
        		uNotes.save();
    		}
    		
    		
    		return ok();
    	}
    }
    
    public static Result setScheduleStatusCancel() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		Form<CloseLeadVM> form = DynamicForm.form(CloseLeadVM.class).bindFromRequest();
    		CloseLeadVM vm = form.get();
    		String clientEmail=null;
    		String clientPhone=null;
    		String clientName=null;
    		MultipartFormData bodys = request().body().asMultipartFormData();
    		AuthUser user = getLocalUser();
    		Date currDate = new Date();
    		
    		String comments="Test Drive has been canceled";
    		String subject = "Test Drive cancelled";
    		
    		//String arr[] = arrayString.split(",");
    		for(String value:vm.actionSelectedLead){
    			RequestMoreInfo info = RequestMoreInfo.findById(Long.parseLong(value));
    			
    			String vin=info.vin;
    			info.setStatus(null);
    			info.setStatusDate(null);
    			info.setStatusTime(null);
    			info.setReason(vm.reasonToCancel);
    			info.setBestDay(null);
    			info.setBestTime(null);
    			info.setScheduleDate(null);
    			info.setConfirmDate(null);
    			info.setConfirmTime(null);
    			info.setIsScheduled(false);
    			info.update();
    			clientEmail=info.email;
    			if(vm.customData != null){
    				saveCustomData(info.id,vm.customData,bodys,Long.parseLong(info.isContactusType));
    			}
    			if(info.confirmDate != null){
    				Map map = new HashMap();
		    		map.put("email",clientEmail);
		    		map.put("vin", vin);
		    		map.put("uname", user.firstName+" "+user.lastName);
		    		map.put("uphone", user.phone);
		    		map.put("uemail", user.email);
		    		map.put("clintName", info.name);
    				cancelTestDriveMail(map);
        	    	  //sendEmail(clientEmail,subject,comments);
        			}
        		UserNotes uNotes = new UserNotes();
        		uNotes.setNote("Client didn't buy vehicle");
        		uNotes.setAction("Other");
        		uNotes.createdDate = currDate;
        		uNotes.createdTime = currDate;
        		uNotes.user = user;
        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
        		uNotes.requestMoreInfo = RequestMoreInfo.findById(info.id);
        		uNotes.save();
    		}
    		return ok();
    	}
    }
    
    public static Result setRequestStatusComplete() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		
    		Date currDate = new Date();
    		String msg="success";
    		AuthUser user = (AuthUser) getLocalUser();
    		Form<SoldContactVM> form = DynamicForm.form(SoldContactVM.class).bindFromRequest();
    		SoldContactVM vm = form.get();
    		
    		Date date = new Date();
    		List<CustomizationDataValue> keyValue = null;
    		
    			LeadType lType = LeadType.findByName(vm.typeOfLead);
	    		RequestMoreInfo info = RequestMoreInfo.findById(vm.infoId);
	    		keyValue = CustomizationDataValue.findByCustomeSaveCRMLead(lType.id, vm.infoId);
	    		AddCollection product = AddCollection.findById(Long.parseLong(info.productId));
	    		if(product != null){
	    			/*product.setSale("sale");*/
	    			product.setSoldDate(date);
	    			product.setSoldUser(user);
	    			product.setStatus("Sold");
	    			product.setLocations(Location.findById(Long.valueOf(session("USER_LOCATION"))));
	    			product.setPrice(Integer.parseInt(vm.price));
	    			product.update();
	    		}
	    		info.setStatus("COMPLETE");
	    		info.setCustZipCode(vm.custZipCode);
	    		info.setEnthicity(vm.enthicity);
	    		info.setStatusDate(currDate);
	    		info.setStatusTime(currDate);
	    		
	    		info.update();
	    		
	    		
	    		UserNotes uNotes = new UserNotes();
	    		uNotes.setNote("furniture Sold");
	    		uNotes.setAction("Other");
	    		uNotes.createdDate = currDate;
	    		uNotes.createdTime = currDate;
	    		uNotes.user = user;
	    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	    		uNotes.requestMoreInfo = RequestMoreInfo.findById(info.id);
	    		uNotes.save();
	    		if(vm.saveLeadTypeAs == null){
	    			vm.saveLeadTypeAs = "SubCollection";
	    		}
	    		if(vm.saveLeadTypeAs.equals("Product")){
	    			for(ProductVM pVm:vm.collectionIds){
	    				if(pVm.price != 0){
	    					SoldInventory sold = new SoldInventory();
				    		sold.requestMoreInfo = RequestMoreInfo.findById(info.id);
				    		sold.collectionId = pVm.id.toString();
				    		sold.saveLeadTypeAs = vm.saveLeadTypeAs;
				    		sold.soldDate = currDate;
				    		sold.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
				    		sold.price = String.valueOf(pVm.price);
				    		sold.user = user;
				    		sold.save();
	    				}
	    				
	    			}
	    		}else{
	    			SoldInventory sold = new SoldInventory();
		    		sold.requestMoreInfo = RequestMoreInfo.findById(info.id);
		    		sold.soldDate = currDate;
		    		sold.collectionId = info.productId;
		    		sold.saveLeadTypeAs = vm.saveLeadTypeAs;
		    		sold.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    		sold.price = vm.price;
		    		sold.user = user;
		    		sold.save();
	    		}
	    		
    	 
    	 SoldContact contact = new SoldContact();
 		contact.name = vm.name;
 		contact.email = vm.email;
 		contact.phone = vm.phone;
 		contact.gender = vm.gender;
 		contact.age = vm.age;
 		contact.buyingFor = vm.buyingFor;
 		contact.howContactedUs = vm.howContactedUs;
 		contact.howFoundUs = vm.howFoundUs;
 		contact.title = vm.title;
 		contact.year = vm.year;
 		contact.designer = vm.designer;
 		contact.price = vm.price;
 		contact.custZipCode = vm.custZipCode;
 		contact.enthicity = vm.enthicity;
 		contact.save();
 		try {
 			Contacts con = Contacts.findByEmail(vm.email);
     		if(con ==null){
     			Date dates = new Date();
    			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
     			Contacts contactsObj = new Contacts();
         		String arr[] = vm.name.split(" ");
         		if(arr.length >= 1) {
         			contactsObj.firstName = arr[0];
         		} else {
         			contactsObj.firstName = vm.name;
         		}
         		if(arr.length >= 2) {
         			contactsObj.middleName = arr[1];
         		}
         		if(arr.length >= 3) {
         			contactsObj.lastName = arr[2];
         		} 
         		contactsObj.creationDate = df.format(dates);
         		contactsObj.email = vm.email;
         		contactsObj.phone = vm.phone;
         		contactsObj.custZipCode = vm.custZipCode;
         		contactsObj.enthicity = vm.enthicity;
         		contactsObj.newsLetter = 1;
         		contactsObj.user = user.id;
         		contactsObj.type = "Online";
         		contactsObj.assignedTo = user.id.toString();
         		contactsObj.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
         		contactsObj.save();
         		
         		for(CustomizationDataValue cust:keyValue){
         			ContactOtherField cField = new ContactOtherField();
         			cField.keyValue = cust.keyValue;
         			cField.value = cust.value;
         			cField.contacts = contactsObj;
         			cField.save();
         		}
         		 
     		}else{
     			msg="contact error";
     		}
			} catch (Exception e) {
				e.printStackTrace();
			}
    	 return ok(msg);
    	}
    }
    
    public static void otherParentChildLeadsStatus(SoldContactVM vm,AuthUser user,Date currDate){
    	if(vm.parentChildLead != null){
			for(RequestInfoVM rMoreInfo: vm.parentChildLead){
    			if(rMoreInfo.status.equals("Sold")){
    				if(rMoreInfo.typeOfLead.equals("Request More Info")){
    					RequestMoreInfo requestMoreInfo = RequestMoreInfo.findById(rMoreInfo.id);
    					requestMoreInfo.setStatus("COMPLETE");
    					requestMoreInfo.update();
    					
    					Vehicle vehicle1 = Vehicle.findByVinAndStatus(requestMoreInfo.vin);
    		    		if(vehicle1 != null){
    			    		vehicle1.setStatus("Sold");
    			    		vehicle1.setSoldDate(currDate);
    			    		vehicle1.setSoldUser(user);
    			    		vehicle1.setPrice(rMoreInfo.price);
    			    		vehicle1.update();
    		    		}
    		    		
    		    		UserNotes uNotes = new UserNotes();
    		    		uNotes.setNote("Vehicle Sold");
    		    		uNotes.setAction("Other");
    		    		uNotes.createdDate = currDate;
    		    		uNotes.createdTime = currDate;
    		    		uNotes.user = user;
    		    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		    		uNotes.requestMoreInfo = RequestMoreInfo.findById(requestMoreInfo.id);
    		    		uNotes.save();
    		    		
    		    		lostLeadsFunction(requestMoreInfo.productId, currDate);
    				}else if(rMoreInfo.typeOfLead.equals("Schedule Test Drive")){
    					ScheduleTest schTest = ScheduleTest.findById(rMoreInfo.id);
    					schTest.setLeadStatus("COMPLETE");
    					schTest.update();
    					
    					Vehicle vehicle1 = Vehicle.findByVinAndStatus(schTest.vin);
    		    		if(vehicle1 != null){
    			    		vehicle1.setStatus("Sold");
    			    		vehicle1.setSoldDate(currDate);
    			    		vehicle1.setSoldUser(user);
    			    		vehicle1.setPrice(rMoreInfo.price);
    			    		vehicle1.update();
    		    		}
    		    		
    		    		UserNotes uNotes = new UserNotes();
    		    		uNotes.setNote("Vehicle Sold");
    		    		uNotes.setAction("Other");
    		    		uNotes.createdDate = currDate;
    		    		uNotes.createdTime = currDate;
    		    		uNotes.user = user;
    		    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		    		uNotes.requestMoreInfo = RequestMoreInfo.findById(schTest.id);
    		    		uNotes.save();
    		    		
    		    		lostLeadsFunction(schTest.productId, currDate);
    				}else if(rMoreInfo.typeOfLead.equals("Trade-In Appraisal")){
    					TradeIn tTest = TradeIn.findById(rMoreInfo.id);
    					tTest.setStatus("COMPLETE");
    					tTest.update();
    					
    					Vehicle vehicle1 = Vehicle.findByVinAndStatus(tTest.vin);
    		    		if(vehicle1 != null){
    			    		vehicle1.setStatus("Sold");
    			    		vehicle1.setSoldDate(currDate);
    			    		vehicle1.setSoldUser(user);
    			    		vehicle1.setPrice(rMoreInfo.price);
    			    		vehicle1.update();
    		    		}
    		    		
    		    		UserNotes uNotes = new UserNotes();
    		    		uNotes.setNote("Vehicle Sold");
    		    		uNotes.setAction("Other");
    		    		uNotes.createdDate = currDate;
    		    		uNotes.createdTime = currDate;
    		    		uNotes.user = user;
    		    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		    		uNotes.requestMoreInfo = RequestMoreInfo.findById(tTest.id);
    		    		uNotes.save();
    		    		
    		    		lostLeadsFunction(tTest.productId, currDate);
    				}
    			}else if(rMoreInfo.status.equals("Cancel")){
    				if(rMoreInfo.typeOfLead.equals("Request More Info")){
    					RequestMoreInfo requestMoreInfo = RequestMoreInfo.findById(rMoreInfo.id);
    					requestMoreInfo.setStatus("CANCEL");
    					requestMoreInfo.update();
    				}else if(rMoreInfo.typeOfLead.equals("Schedule Test Drive")){
    					ScheduleTest schTest = ScheduleTest.findById(rMoreInfo.id);
    					schTest.setLeadStatus("CANCEL");
    					schTest.update();
    				}else if(rMoreInfo.typeOfLead.equals("Trade-In Appraisal")){
    					TradeIn tTest = TradeIn.findById(rMoreInfo.id);
    					tTest.setStatus("CANCEL");
    					tTest.update();
    				}
    			}
    		}
    		
		}
    }
    
    public static Result saveCompletedLeads(String duration,String comment,String id,String typeOfLead){
    	
    	AuthUser user = getLocalUser();
    	Date currDate = new Date();
		if(typeOfLead.equals("requestMore") || typeOfLead.equals("Request More Info")) {
			RequestMoreInfo requestMore = RequestMoreInfo.findById(Long.parseLong(id));
			requestMore.setTestDriveStatus("TestDriveCompleted");
			requestMore.setTestDriveCompletedComment(comment);
			requestMore.setTestDriveCompletedDuration(duration);
			requestMore.update();
			
			UserNotes uNotes = new UserNotes();
    		uNotes.setNote("Test Drive Successfully Completed. Duration "+duration+" minutes");
    		uNotes.setAction("Other");
    		uNotes.createdDate = currDate;
    		uNotes.createdTime = currDate;
    		uNotes.user = user;
    		uNotes.requestMoreInfo = requestMore;
    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		uNotes.save();
			
		}
		if(typeOfLead.equals("scheduleTest") || typeOfLead.equals("Schedule Test Drive")) {
			ScheduleTest scheduleTest = ScheduleTest.findById(Long.parseLong(id));
			
			//scheduleTest.setLeadStatus("TestDriveCompleted");
			scheduleTest.setTestDriveStatus("TestDriveCompleted");
			scheduleTest.setTestDriveCompletedComment(comment);
			scheduleTest.setTestDriveCompletedDuration(duration);
			scheduleTest.update();
			
			UserNotes uNotes = new UserNotes();
			uNotes.setNote("Test Drive Successfully Completed. Duration "+duration+" minutes");
    		uNotes.setAction("Other");
    		uNotes.createdDate = currDate;
    		uNotes.createdTime = currDate;
    		uNotes.user = user;
    		uNotes.scheduleTest = scheduleTest;
    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		uNotes.save();
			
		}
		if(typeOfLead.equals("tradeIn") || typeOfLead.equals("Trade-In Appraisal")) {
			TradeIn tradeIn = TradeIn.findById(Long.parseLong(id));
			
			tradeIn.setTestDriveStatus("TestDriveCompleted");
			tradeIn.setTestDriveCompletedComment(comment);
			tradeIn.setTestDriveCompletedDuration(duration);
			tradeIn.update();
			
			UserNotes uNotes = new UserNotes();
			uNotes.setNote("Test Drive Successfully Completed. Duration "+duration+" minutes");
    		uNotes.setAction("Other");
    		uNotes.createdDate = currDate;
    		uNotes.createdTime = currDate;
    		uNotes.user = user;
    		uNotes.tradeIn = tradeIn;
    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		uNotes.save();
		}
    	return ok();
    }
    
    public static void lostLeadsFunction(String pId, Date currDate){
    	List<String> emailList = new ArrayList<>();
    	List<String> vinList = new ArrayList<>();
    	List<RequestInfoVM>vinAndEmailList =new ArrayList<>();
    	
		
		List<RequestMoreInfo> rInfos = RequestMoreInfo.findByProductIdAndLocation(pId.toString(), Location.findById(Long.parseLong(session("USER_LOCATION"))));
		for(RequestMoreInfo rMoreInfo:rInfos){
			if(rMoreInfo.status == null){
				
				rMoreInfo.setStatus("LOST");
				rMoreInfo.setStatusDate(currDate);
				rMoreInfo.setStatusTime(currDate);
				rMoreInfo.update();
				
				UserNotes uNotes1 = new UserNotes();
	    		uNotes1.setNote("Vehicle has been sold by another Sales Person");
	    		uNotes1.setAction("Other");
	    		uNotes1.createdDate = currDate;
	    		uNotes1.createdTime = currDate;
	    		//uNotes1.user = user;
	    		uNotes1.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	    		uNotes1.tradeIn = TradeIn.findById(rMoreInfo.id);
	    		
	    		uNotes1.save();
			}else if(!rMoreInfo.status.equals("COMPLETE")){
				rMoreInfo.setStatus("LOST");
				rMoreInfo.setStatusDate(currDate);
				rMoreInfo.setStatusTime(currDate);
				rMoreInfo.update();
				
				UserNotes uNotes1 = new UserNotes();
	    		uNotes1.setNote("Vehicle has been sold by another Sales Person");
	    		uNotes1.setAction("Other");
	    		uNotes1.createdDate = currDate;
	    		uNotes1.createdTime = currDate;
	    		//uNotes1.user = user;
	    		uNotes1.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	    		uNotes1.tradeIn = TradeIn.findById(rMoreInfo.id);
	    		uNotes1.save();
			}
			if(rMoreInfo.assignedTo !=null){
				
				RequestInfoVM vm1=new RequestInfoVM();
				vm1.vin=rMoreInfo.vin;
				AuthUser userObj = AuthUser.findById(rMoreInfo.assignedTo.id);
				if(userObj !=null){
				//	emailList.add(userObj.email);
					vm1.email=userObj.email;
					
				}
				vinAndEmailList.add(vm1);
			}
		}
		
		if(vinAndEmailList.size()>0){
			//vehicleSoldEmail(emailList);
			vehicleSoldMail(vinAndEmailList);
		}
    }
    
    public static Result setRequestStatusCancel(Long id,String reason) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser user = getLocalUser();
    		Date currDate = new Date();
    		RequestMoreInfo info = RequestMoreInfo.findById(id);
    		info.setStatus("CANCEL");
    		info.setStatusDate(currDate);
    		info.setStatusTime(currDate);
    		info.setReason(reason);
    		info.update();
    		
    		
    		UserNotes uNotes = new UserNotes();
    		uNotes.setNote("Client didn't buy vehicle");
    		uNotes.setAction("Other");
    		uNotes.createdDate = currDate;
    		uNotes.createdTime = currDate;
    		uNotes.user = user;
    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		uNotes.requestMoreInfo = RequestMoreInfo.findById(info.id);
    		uNotes.save();
    		return ok();
    	}
    }
    
    /*public static Result setTradeInStatusComplete() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		
    		Date currDate = new Date();
    		List<String> emailList = new ArrayList<>();
    		AuthUser user = (AuthUser) getLocalUser();
    		Form<SoldContactVM> form = DynamicForm.form(SoldContactVM.class).bindFromRequest();
    		SoldContactVM vm = form.get();
    		SoldContact contact = new SoldContact();
    		contact.name = vm.name;
    		contact.email = vm.email;
    		contact.phone = vm.phone;
    		contact.gender = vm.gender;
    		contact.age = vm.age;
    		contact.buyingFor = vm.buyingFor;
    		contact.howContactedUs = vm.howContactedUs;
    		contact.howFoundUs = vm.howFoundUs;
    		contact.make = vm.make;
    		contact.year = vm.year;
    		contact.mileage = vm.mileage;
    		contact.price = vm.price;
    		contact.custZipCode = vm.custZipCode;
    		contact.enthicity = vm.enthicity;
    		contact.save();
    		Contacts contactsObj = new Contacts();
    		String arr[] = vm.name.split(" ");
    		if(arr.length >= 1) {
    			contactsObj.firstName = arr[0];
    		} else {
    			contactsObj.firstName = vm.name;
    		}
    		if(arr.length >= 2) {
    			contactsObj.middleName = arr[1];
    		}
    		if(arr.length >= 3) {
    			contactsObj.lastName = arr[2];
    		} 
    		contactsObj.email = vm.email;
    		contactsObj.phone = vm.phone;
    		contactsObj.custZipCode = vm.custZipCode;
    		contactsObj.enthicity = vm.enthicity;
    		contactsObj.newsLetter = 0;
    		contactsObj.user = user.id;
    		contactsObj.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		contactsObj.save();
    		TradeIn info = TradeIn.findById(vm.infoId);
    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
    		Date date = new Date();
    		if(vehicle != null){
    		vehicle.setStatus("Sold");
    		vehicle.setSoldDate(date);
    		vehicle.setSoldUser(user);
    		vehicle.update();
    		}
    		info.setStatus("COMPLETE");
    		info.setCustZipCode(vm.custZipCode);
    		info.setEnthicity(vm.enthicity);
    		info.setStatusDate(currDate);
    		info.update();
    		
    		for(RequestInfoVM rMoreInfo: vm.parentChildLead){
    			if(rMoreInfo.status.equals("Sold")){
    				if(rMoreInfo.typeOfLead.equals("Request More Info")){
    					RequestMoreInfo requestMoreInfo = RequestMoreInfo.findById(rMoreInfo.id);
    					requestMoreInfo.setStatus("COMPLETE");
    					requestMoreInfo.update();
    					
    					Vehicle vehicle1 = Vehicle.findByVinAndStatus(requestMoreInfo.vin);
    		    		if(vehicle1 != null){
    			    		vehicle1.setStatus("Sold");
    			    		vehicle1.setSoldDate(date);
    			    		vehicle1.setSoldUser(user);
    			    		vehicle1.update();
    		    		}
    		    		
    		    		UserNotes uNotes = new UserNotes();
    		    		uNotes.setNote("Vehicle Sold");
    		    		uNotes.setAction("Other");
    		    		uNotes.createdDate = currDate;
    		    		uNotes.createdTime = currDate;
    		    		uNotes.user = user;
    		    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		    		uNotes.requestMoreInfo = RequestMoreInfo.findById(requestMoreInfo.id);
    		    		uNotes.save();
    		    		
    		    		lostLeadsFunction(requestMoreInfo.vin, currDate);
    				}else if(rMoreInfo.typeOfLead.equals("Schedule Test Drive")){
    					ScheduleTest schTest = ScheduleTest.findById(rMoreInfo.id);
    					schTest.setStatus("COMPLETE");
    					schTest.update();
    					
    					Vehicle vehicle1 = Vehicle.findByVinAndStatus(schTest.vin);
    		    		if(vehicle1 != null){
    			    		vehicle1.setStatus("Sold");
    			    		vehicle1.setSoldDate(date);
    			    		vehicle1.setSoldUser(user);
    			    		vehicle1.update();
    		    		}
    		    		
    		    		UserNotes uNotes = new UserNotes();
    		    		uNotes.setNote("Vehicle Sold");
    		    		uNotes.setAction("Other");
    		    		uNotes.createdDate = currDate;
    		    		uNotes.createdTime = currDate;
    		    		uNotes.user = user;
    		    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		    		uNotes.requestMoreInfo = RequestMoreInfo.findById(schTest.id);
    		    		uNotes.save();
    		    		
    		    		lostLeadsFunction(schTest.vin, currDate);
    				}else if(rMoreInfo.typeOfLead.equals("Trade-In Appraisal")){
    					ScheduleTest schTest = ScheduleTest.findById(rMoreInfo.id);
    					schTest.setStatus("COMPLETE");
    					schTest.update();
    					
    					Vehicle vehicle1 = Vehicle.findByVinAndStatus(schTest.vin);
    		    		if(vehicle1 != null){
    			    		vehicle1.setStatus("Sold");
    			    		vehicle1.setSoldDate(date);
    			    		vehicle1.setSoldUser(user);
    			    		vehicle1.update();
    		    		}
    		    		
    		    		UserNotes uNotes = new UserNotes();
    		    		uNotes.setNote("Vehicle Sold");
    		    		uNotes.setAction("Other");
    		    		uNotes.createdDate = currDate;
    		    		uNotes.createdTime = currDate;
    		    		uNotes.user = user;
    		    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		    		uNotes.requestMoreInfo = RequestMoreInfo.findById(schTest.id);
    		    		uNotes.save();
    		    		
    		    		lostLeadsFunction(schTest.vin, currDate);
    				}
    			}else if(rMoreInfo.status.equals("Cancel")){
    				if(rMoreInfo.typeOfLead.equals("Request More Info")){
    					RequestMoreInfo requestMoreInfo = RequestMoreInfo.findById(rMoreInfo.id);
    					requestMoreInfo.setStatus("CANCEL");
    					requestMoreInfo.update();
    				}
    			}
    		}
    		
    		UserNotes uNotes = new UserNotes();
    		uNotes.setNote("Vehicle Sold");
    		uNotes.setAction("Other");
    		uNotes.createdDate = currDate;
    		uNotes.createdTime = currDate;
    		uNotes.user = user;
    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		uNotes.tradeIn = TradeIn.findById(info.id);
    		uNotes.save();
    		
    		lostLeadsFunction(info.vin, currDate);
    		
    	
    		return ok();
    	}
    }*/
    
    public static Result setTradeInStatusCancel(Long id,String reason) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser user = getLocalUser();
    		Date currDate = new Date();
    		TradeIn info = TradeIn.findById(id);
    		info.setStatus("CANCEL");
    		info.setStatusDate(currDate);
    		info.setStatusTime(currDate);
    		info.setReason(reason);
    		info.update();
    		
    		
    		UserNotes uNotes = new UserNotes();
    		uNotes.setNote("Client didn't buy vehicle");
    		uNotes.setAction("Other");
    		uNotes.createdDate = currDate;
    		uNotes.createdTime = currDate;
    		uNotes.user = user;
    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		uNotes.tradeIn = TradeIn.findById(info.id);
    		uNotes.save();
    		return ok();
    	}
    }
   
    public static Result getUserType() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser user = getLocalUser();
    		String userType = user.getRole();
    		if(userType == null) {
    			userType = "";
    		}
    		return ok(userType);
    	}
    }
    
    public static Result sendEmailDaily(){
    	 //AuthUser user = getLocalUser();
    	 DateFormat df1 = new SimpleDateFormat("MM-dd-yyyy HH:mm a");
    	 DateFormat df2 = new SimpleDateFormat("MM-dd-yyyy HH:mm a");
    	 //DateFormat df2 = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
    	 
         DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
         SimpleDateFormat parseTime = new SimpleDateFormat("hh:mm a");
         Date currD = new Date();
         Date currentDate = null;
         Date aftHrDate = null;
         Date aftDay = null;
         Date aftHrDate1 = null;
         Date aftDay1 = null;
         Date infoDate = null;
         Date datec = null;
         
         
         Date lessDay = DateUtils.addDays(currD, -1);
         //https://maps.googleapis.com/maps/api/timezone/json?location=37.7870882,-122.39112790000001&timestamp=1331161200&key=AIzaSyAZDXHvlpRPy2R_LWP4iCoxN_UBDdMg6o4
         //Date newDate = DateUtils.addHours(info2.confirmTime, 4);
         List<ScheduleTest> list = ScheduleTest.findAllByServiceTestEmail(lessDay);
         List<RequestMoreInfo> requestMoreInfos = RequestMoreInfo.findByConfirmGraLeadsToEmail(lessDay);
     	 List<TradeIn> tradeIns = TradeIn.findByConfirmGraLeadsToEmail(lessDay);
     	
         for(ScheduleTest scTest:list){
        	 System.out.println("----------------");
        	 System.out.println(scTest.id);
        	 List<AuthUser> listForUser=null;
        	 List<AuthUser> listForUser1=null;
        	 if(scTest.user != null && scTest.assignedTo != null){
        		 AuthUser aUser = AuthUser.findById(scTest.assignedTo.id);
        		 Location location = Location.findById(16l);
        		 if(aUser.location != null){
        			 location = Location.findById(aUser.location.id);
        		 }
            	
            	 df2.setTimeZone(TimeZone.getTimeZone(location.time_zone));
                 String IST = df2.format(currD);
                
                 Date istTimes = null;
    			try {
    				istTimes = df1.parse(IST);
    			} catch (ParseException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}
            	
            	 
            	 String cDate = df.format(istTimes);
                 String cTime = parseTime.format(istTimes);
                 String crD =    df1.format(istTimes);
        		 
                 try {
                	 currentDate = df1.parse(crD);
                	 datec = df.parse(cDate);
                	 aftHrDate = DateUtils.addHours(currentDate, 1);
                	 aftDay = DateUtils.addHours(currentDate, 24);
                	 aftHrDate1 = DateUtils.addMinutes(aftHrDate, 15);
                	 aftDay1 = DateUtils.addMinutes(aftDay, 15);
        		} catch (Exception e) {
        			e.printStackTrace();
        		}
            	 AuthUser emailUser = AuthUser.findById(scTest.assignedTo.id);
            	 if(scTest.user != null){
            		 AuthUser userD = AuthUser.findById(scTest.user.id);
                	 try {
                		 System.out.println("scTest.confirmDate " + scTest.confirmDate);
                		 String str = df.format(scTest.confirmDate) +" "+parseTime.format(scTest.confirmTime);
                		 System.out.println("format confirm time " + str);
                		 infoDate = df1.parse(str);
                    	 System.out.println(df1.format(currD));
                    	 System.out.println(parseTime.format(scTest.confirmTime));
                    	 System.out.println(infoDate); //db date
                    	 System.out.println(aftHrDate); //+hour  dt
                    	 System.out.println(aftHrDate1);
                    	 System.out.println(aftDay); // +24hrs
                    	 System.out.println(aftDay1); //+15mins ahead
                    	 System.out.println(emailUser.email);
                    	 System.out.println("meetingStatus : "+scTest.meetingStatus);
                    	 System.out.println("-11---------------");
                    	 System.out.println(scTest.email);
                		 if((infoDate.equals(aftHrDate)||infoDate.after(aftHrDate)) && ((infoDate.equals(aftHrDate1)||infoDate.before(aftHrDate1)))){
                    		 if(scTest.meetingStatus == null){
                				 String subject = "Test drive reminder";
                 		    	 String comments = "You have a test drive scheduled in 1 hour \n"+str+" "+scTest.name;
                 		    	 sendEmail(emailUser.communicationemail, subject, comments);
                 		    	sendEmail(scTest.email, subject, comments);
                 		    	
                			 }else if(scTest.meetingStatus.equals("meeting")){
                				 
                				 listForUser=new ArrayList<>();
                				 List<Integer>userIds = new ArrayList<>();
                				 List<ScheduleTest> schedulegroupList = ScheduleTest.findAllGroupMeeting(scTest.groupId);
                				 for(ScheduleTest scList:schedulegroupList){
                					 
                					 AuthUser user = AuthUser.findById(scList.user.id);
                					 if(userIds.contains(user.id)){
                						 continue;
                					 }else{
                						 userIds.add(user.id);
                					 }
                					 listForUser.add(user);
                					 
                				 }
                				 
                				 System.out.println("----^^^^^^^^^^^^^^^-111-----------");
                				 String subject = "IN ONE HOUR";
                 		    	 String comments = "You have a meeting scheduled in 1 hour \n"+str+" "+scTest.name;
        						meetingReminder(listForUser,emailUser.communicationemail, scTest.confirmDate, scTest.confirmTime, subject);
                 		    	meetingReminder(listForUser,userD.communicationemail, scTest.confirmDate, scTest.confirmTime, subject);
                			
                			 }
                    	 }
                		 System.out.println("checking schedular condition.");
                		 //infoDate - dbtime
                		 //aftDay - next day
                		 //aftDay1 - next day 15mins...
                		 if((infoDate.equals(aftDay)||infoDate.after(aftDay)) && ((infoDate.equals(aftDay1)||infoDate.before(aftDay1)))){
                    		 if(scTest.meetingStatus == null){
                    			 String subject = "Test drive reminder";
                 		    	 String comments = "You have a test drive scheduled in 24 hours \n Date" +infoDate ;
                 		    	 sendEmailAfterDay(emailUser.communicationemail, subject, comments,scTest.vin,scTest.confirmDate,scTest.confirmTime,scTest.user,scTest.name);
                 		    	sendEmailAfterDay(scTest.email, subject, comments,scTest.vin,scTest.confirmDate,scTest.confirmTime,scTest.user,scTest.name);
                			 }else if(scTest.meetingStatus.equals("meeting")){
                				 
                				 listForUser1=new ArrayList<>();
                				 List<Integer>userIds = new ArrayList<>();
                				 List<ScheduleTest> schedulegroupList = ScheduleTest.findAllGroupMeeting(scTest.groupId);
                                  for(ScheduleTest scList:schedulegroupList){
                					 
                					 AuthUser user = AuthUser.findById(scList.user.id);
                					 
                					 if(userIds.contains(user.id)){
                						 continue;
                					 }else{
                						 userIds.add(user.id);
                					 }
                					 listForUser1.add(user);
                					 
                				 }
                				 String subject = "You have a meeting scheduled in 24 hours";
                 		    	 String comments = "You have a meeting scheduled in 24 hours \n"+df.format(scTest.confirmDate)+"   "+parseTime.format(scTest.confirmTime)+" "+scTest.name;
        						meetingReminderAfterDay(listForUser1,emailUser.communicationemail, scTest.confirmDate, scTest.confirmTime, subject);
                 		    	meetingReminderAfterDay(listForUser1,userD.communicationemail, scTest.confirmDate, scTest.confirmTime, subject);
                			 }
                    	 }
        			} catch (Exception e) {
        				e.printStackTrace();
        			}
            	 }
        	 }
        	 
         }
         
         for(RequestMoreInfo rInfo:requestMoreInfos){
        	 AuthUser emailUser = AuthUser.findById(rInfo.assignedTo.id);
        	 
        	 Location location = Location.findById(emailUser.location.id);
        	
        	 df2.setTimeZone(TimeZone.getTimeZone(location.time_zone));
             String IST = df2.format(currD);
            
             Date istTimes = null;
			try {
				istTimes = df1.parse(IST);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	
        	 
        	 String cDate = df.format(istTimes);
             String cTime = parseTime.format(istTimes);
             String crD =    df1.format(istTimes);
    		 
             try {
            	 currentDate = df1.parse(crD);
            	 datec = df.parse(cDate);
            	 aftHrDate = DateUtils.addHours(currentDate, 1);
            	 aftDay = DateUtils.addHours(currentDate, 24);
            	 aftHrDate1 = DateUtils.addMinutes(aftHrDate, 15);
            	 aftDay1 = DateUtils.addMinutes(aftDay, 15);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        	 
        	 
        	 
        	 
        	 try {
        		 String str = df.format(rInfo.confirmDate) +" "+parseTime.format(rInfo.confirmTime);
        		 infoDate = df1.parse(str);
        		 if((infoDate.equals(aftHrDate)||infoDate.after(aftHrDate)) && ((infoDate.equals(aftHrDate1)||infoDate.before(aftHrDate1)))){
        			 String subject = "Test drive reminder";
     		    	 String comments = "You have a test drive scheduled in 1 hour ";
     		    	 sendEmail(emailUser.communicationemail, subject, comments);
     		    	sendEmail(rInfo.email, subject, comments);
        		 }
        		 if((infoDate.equals(aftDay)||infoDate.after(aftDay)) && ((infoDate.equals(aftDay1)||infoDate.before(aftDay1)))){
        			 String subject = "Test drive reminder";
     		    	 String comments = "You have a test drive scheduled in 24 hours ";
     		    	// sendEmail(emailUser.communicationemail, subject, comments);
     		    	sendEmailAfterDay(emailUser.communicationemail, subject, comments,rInfo.vin,rInfo.confirmDate,rInfo.confirmTime,rInfo.user,rInfo.name);
     		    	sendEmailAfterDay(rInfo.email, subject, comments,rInfo.vin,rInfo.confirmDate,rInfo.confirmTime,rInfo.user,rInfo.name);
     		    	//sendEmail(rInfo.email, subject, comments);
        		 }
			} catch (Exception e) {
				e.printStackTrace();
			}
        	 /*if(rInfo.confirmDate.equals(datec)){
        		 if(rInfo.confirmTime.equals(timeSet)){
        			 String subject = "Request test D";
     		    	 String comments = "test D";
     		    	 sendEmail("yogeshpatil424@gmail.com", subject, comments);
        		 }
        	 }*/	 
         }
         
         for(TradeIn tInfo:tradeIns){
        	 
        	 AuthUser emailUser = AuthUser.findById(tInfo.assignedTo.id);
        	 
        	 Location location = Location.findById(emailUser.location.id);
        	
        	 df2.setTimeZone(TimeZone.getTimeZone(location.time_zone));
             String IST = df2.format(currD);
            
             Date istTimes = null;
			try {
				istTimes = df1.parse(IST);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	
        	 
        	 String cDate = df.format(istTimes);
             String cTime = parseTime.format(istTimes);
             String crD =    df1.format(istTimes);
    		 
             try {
            	 currentDate = df1.parse(crD);
            	 datec = df.parse(cDate);
            	 aftHrDate = DateUtils.addHours(currentDate, 1);
            	 aftDay = DateUtils.addHours(currentDate, 24);
            	 aftHrDate1 = DateUtils.addMinutes(aftHrDate, 15);
            	 aftDay1 = DateUtils.addMinutes(aftDay, 15);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        	 
        	 
        	 try {
        		 String str = df.format(tInfo.confirmDate) +" "+parseTime.format(tInfo.confirmTime);
        		 infoDate = df1.parse(str);
        		 if((infoDate.equals(aftHrDate)||infoDate.after(aftHrDate)) && ((infoDate.equals(aftHrDate1)||infoDate.before(aftHrDate1)))){
        			 String subject = "Test drive reminder";
     		    	 String comments = "You have a test drive scheduled in 1 hour ";
     		    	 sendEmail(emailUser.communicationemail, subject, comments);
     		    	sendEmail(tInfo.email, subject, comments);
        		 }
        		 if((infoDate.equals(aftDay)||infoDate.after(aftDay)) && ((infoDate.equals(aftDay1)||infoDate.before(aftDay1)))){
        			 String subject = "Test drive reminder";
     		    	 String comments = "You have a test drive scheduled in 24 hours ";
     		    	sendEmailAfterDay(emailUser.communicationemail, subject, comments,tInfo.vin,tInfo.confirmDate,tInfo.confirmTime,tInfo.user,tInfo.firstName+" "+tInfo.lastName);
     		    	sendEmailAfterDay(tInfo.email, subject, comments,tInfo.vin,tInfo.confirmDate,tInfo.confirmTime,tInfo.user,tInfo.firstName+" "+tInfo.lastName);
     		    	// sendEmail(emailUser.communicationemail, subject, comments);
     		    	//sendEmail(tInfo.email, subject, comments);
        		 }
			} catch (Exception e) {
				e.printStackTrace();
			}
        	 /*if(tInfo.confirmDate.equals(datec)){
        		 if(tInfo.confirmTime.equals(timeSet)){
        			 String subject = "TradeIn test D";
     		    	 String comments = "test D";
     		    	 sendEmail("yogeshpatil424@gmail.com", subject, comments);
        		 }
        	 }	 */
         }
         
         
         
        
        	/*_------------------------------------------------------------------------------------------------------------------*/
            // AuthUser user=getLocalUser();
         
         Date date1=new Date();
         DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
         String date2 = format1.format(date1);
         Date dat=null;
         try {
        	 dat = format1.parse(date2);
 		} catch (ParseException e1) {
 			// TODO Auto-generated catch block
 			e1.printStackTrace();
 		}
         Date beforeThreeDays = DateUtils.addDays(dat, -3);
         List <RequestMoreInfo> requestInfos=RequestMoreInfo.findAllNullStatusLeads(beforeThreeDays);
         if(requestInfos != null){
         for(RequestMoreInfo info:requestInfos){
        	 
        	 DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	 
        	 AuthUser emailUser = AuthUser.findById(info.assignedTo.id);
        	 if(emailUser.location != null){
        	 Location location = Location.findById(emailUser.location.id);
             
             df2.setTimeZone(TimeZone.getTimeZone(location.time_zone));
             String IST = df2.format(currD);
            
             Date istTimes = null;
    		try {
    			istTimes = df1.parse(IST);
    		} catch (ParseException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
             String cDate = df.format(istTimes);
             String cTime = parseTime.format(istTimes);
             String crD =    df1.format(istTimes);
             try {
            	 currentDate = df1.parse(crD);
            	 datec = df.parse(cDate);
            	 aftHrDate = DateUtils.addHours(currentDate, 1);
            	 aftDay = DateUtils.addHours(currentDate, -24);
            	 aftHrDate1 = DateUtils.addMinutes(aftHrDate, 15);
            	 aftDay1 = DateUtils.addMinutes(aftDay, 15);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        		 Date sourceDate1 = info.requestTime;
        		 //sourceDate = DateUtils.addHours(sourceDate, 24);
        		 System.out.println("source date"+sourceDate1);
        		 
        		// if((sourceDate.equals(aftHrDate)||sourceDate.after(aftHrDate)) && ((sourceDate.equals(aftHrDate1)||sourceDate.before(aftHrDate1)))){
        		 if((sourceDate1.equals(aftDay)||sourceDate1.after(aftDay)) && ((sourceDate1.equals(aftDay1)||sourceDate1.before(aftDay1)))){
        			 //List<UserNotes> note=UserNotes.findRequestMoreList(info);
        			// if(note != null){
        			 //if(!(note.size()>2)){
        				 AuthUser auser=AuthUser.findById(info.assignedTo.id);
        				sendEmailForNotFollowed(info.vin,auser);
        				 
        		// }
        		//	 }
        	 }
        	 
         } 
         }
         }
        
         List <ScheduleTest> test1=ScheduleTest.findAllNullStatusLeads(beforeThreeDays);
         for(ScheduleTest test:test1){
        	
        	 DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	 if(test.assignedTo.id != null){
        	 AuthUser emailUser = AuthUser.findById(test.assignedTo.id);
        	 Location location=null;
        	 if(emailUser !=null){
        		 if(emailUser.location != null){
        	 location = Location.findById(emailUser.location.id);
        		 }
             if(location != null){
             df2.setTimeZone(TimeZone.getTimeZone(location.time_zone));
             String IST = df2.format(currD);
            
             Date istTimes = null;
    		try {
    			istTimes = df1.parse(IST);
    		} catch (ParseException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
             String cDate = df.format(istTimes);
             String cTime = parseTime.format(istTimes);
             String crD =    df1.format(istTimes);
             try {
            	 currentDate = df1.parse(crD);
            	 datec = df.parse(cDate);
            	 aftHrDate = DateUtils.addHours(currentDate, 1);
            	 aftDay = DateUtils.addHours(currentDate, -24);
            	 aftHrDate1 = DateUtils.addMinutes(aftHrDate, 15);
            	 aftDay1 = DateUtils.addMinutes(aftDay, 15);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        		 Date sourceDate = test.scheduleTime;
        		// sourceDate = DateUtils.addHours(sourceDate, 24);
        		 System.out.println("source date"+sourceDate);
        		 System.out.println("aftDay"+aftDay);
        		 System.out.println("aftDay1"+aftDay1);
        		 
        		 if((sourceDate.equals(aftDay)||sourceDate.after(aftDay)) && ((sourceDate.equals(aftDay1)||sourceDate.before(aftDay1)))){
        			/*List<UserNotes> note=UserNotes.scheduleList(test);
        			 if(note != null){
        			 if(!(note.size()>2)){
        				 
        				 AuthUser auser=AuthUser.findById(test.assignedTo.id);
        				 sendEmailForNotFollowed(test.vin,auser);
        		 }*/
        			 if(test.meetingStatus == null){
        			 AuthUser auser=AuthUser.findById(test.assignedTo.id);
    				 sendEmailForNotFollowed(test.vin,auser);
        			 }
        	  
        	 }
        	 }
        	 }
        	 }
         }
         
         
         
         List <TradeIn> tradeIns1=TradeIn.findAllNullStatusLeads(beforeThreeDays);
         if(tradeIns1 !=null){
         for(TradeIn trade:tradeIns1){
        	 
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	 
        	 AuthUser emailUser = AuthUser.findById(trade.assignedTo.id);
        	 if(emailUser.location !=null){
        	 Location location = Location.findById(emailUser.location.id);
             
             df2.setTimeZone(TimeZone.getTimeZone(location.time_zone));
             String IST = df2.format(currD);
            
             Date istTimes = null;
    		try {
    			istTimes = df1.parse(IST);
    		} catch (ParseException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
             String cDate = df.format(istTimes);
             String cTime = parseTime.format(istTimes);
             String crD =    df1.format(istTimes);
             try {
            	 currentDate = df1.parse(crD);
            	 datec = df.parse(cDate);
            	 aftHrDate = DateUtils.addHours(currentDate, 1);
            	 aftDay = DateUtils.addHours(currentDate, -24);
            	 aftHrDate1 = DateUtils.addMinutes(aftHrDate, 15);
            	 aftDay1 = DateUtils.addMinutes(aftDay, 15);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        		 Date sourceDate = trade.tradeTime;
        		 //sourceDate = DateUtils.addHours(sourceDate, 24);
        		 System.out.println("source date"+sourceDate);
        		 
        		 if((sourceDate.equals(aftDay)||sourceDate.after(aftDay)) && ((sourceDate.equals(aftDay1)||sourceDate.before(aftDay1)))){
        			// List<UserNotes> note=UserNotes.tradeInList(trade);
        			// if(note !=null){
        			// if(!(note.size()>2)){
        				 
        				 AuthUser auser=AuthUser.findById(trade.assignedTo.id);
        				 sendEmailForNotFollowed(trade.vin,auser);
        		// }
        		// }	 
        	 }
         }
         }
         }
         
    
    	return ok();
    
}
    
    


    private static void sendEmailForNotFollowed(String vin,AuthUser user) {
     	
     //AuthUser logoUser = AuthUser.findById(Integer.getInteger(session("USER_KEY")));
     //	SiteLogo logo = SiteLogo.findByLocation(Long.valueOf(session("USER_LOCATION"))); // findByUser(logoUser);
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
 			try{
 			message.setFrom(new InternetAddress(emailUser,emailName));
 			}
 			catch(UnsupportedEncodingException e){
 				e.printStackTrace();
 			}
 			//message.setRecipients(Message.RecipientType.TO,
 			//		InternetAddress.parse(map.get("email").toString()));
 			if(user.role.equalsIgnoreCase("Manager")){
 				message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(user.communicationemail));
 			}
 			else if(user.role.equalsIgnoreCase("Sales Person")){
 				message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(user.communicationemail));
 				
 				AuthUser user1=AuthUser.getlocationAndManagerOne(user.location);
 				message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(user1.communicationemail));
 			}
 			
 			
 			message.setSubject("LEAD NOT FOLLOWED");
 			Multipart multipart = new MimeMultipart();
 			BodyPart messageBodyPart = new MimeBodyPart();
 			messageBodyPart = new MimeBodyPart();
 			
 			VelocityEngine ve = new VelocityEngine();
 			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
 			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
 			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
 			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
 			ve.init();
 		
 			
 	        Template t = ve.getTemplate("/public/emailTemplate/leadNotFollowedUp_HTML.html"); 
 	        VelocityContext context = new VelocityContext();
 	        
 	       Vehicle vehicle = Vehicle.findByVinAndStatus(vin.toString());
	        context.put("year", vehicle.year);
	        context.put("make", vehicle.make);
	        context.put("model", vehicle.model);
	        context.put("price", "$"+vehicle.price);
	        context.put("stock", vehicle.stock);
	        context.put("vin", vehicle.vin);
	        context.put("make", vehicle.make);
	        context.put("mileage", vehicle.mileage);
 	        
	        
	        if(user.role.equalsIgnoreCase("Manager")){
	        	context.put("name", user.fullName());
 	 	        context.put("email", user.email);
 	 	        context.put("phone", user.phone);
	        	
 			}
 			else if(user.role.equalsIgnoreCase("Sales Person")){
 				context.put("name", user.fullName());
 	 	        context.put("email", user.email);
 	 	        context.put("phone", user.phone);
 				
 			}
	        
	        InventoryImage image = InventoryImage.getDefaultImage(vehicle.vin);
 	        if(image!=null) {
 	        	context.put("defaultImage", image.path);
 	        } else {
 	        	context.put("defaultImage", "");
 	        }
	        
 	        /*
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
 	        context.put("mileage", vehicle.mileage);
 	        context.put("name", map.get("uname"));
 	        context.put("email", map.get("uemail"));
 	        context.put("phone",  map.get("uphone"));
 	        String weather= map.get("CnfDateNature").toString();
 	        String arr1[] = weather.split("&");
 	        String nature=arr1[0];
 	        String temp=arr1[1];
 	        context.put("nature",nature);
 	        context.put("temp", temp);
 	        VehicleImage image = VehicleImage.getDefaultImage(vehicle.vin);
 	        if(image!=null) {
 	        	context.put("defaultImage", image.path);
 	        } else {
 	        	context.put("defaultImage", "");
 	        }*/
 	        StringWriter writer = new StringWriter();
 	        t.merge( context, writer );
 	        String content = writer.toString(); 
 			
 			messageBodyPart.setContent(content, "text/html");
 			multipart.addBodyPart(messageBodyPart);
 			message.setContent(multipart);
 			Transport.send(message);
 			System.out.println("email sent not followed");
 			
 		}
 		catch (Exception e)
 		{
 			e.printStackTrace();
 		}
     }
     
     
    
    
    
	public static void meetingReminder(List<AuthUser> listForUser,String communicationEmail, Date confirmDate,Date confirmTime,String subject){
		/*InternetAddress[] usersArray = new InternetAddress[userList.size()];
		int index = 0;
		for (AuthUser assi : userList) {
			try {
				
				usersArray[index] = new InternetAddress(assi.getCommunicationemail());
				index++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
		List<UserVM> list = new ArrayList<>() ;
		for(AuthUser assi : listForUser){
			
			UserVM vm1=new UserVM();
			vm1.fullName=assi.firstName+" "+assi.lastName;
			list.add(vm1);
			
			
			
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
			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(communicationEmail));
			/*usersArray*/
			message.setSubject(subject);
			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			ve.init();
			
			Template t = ve.getTemplate("/public/emailTemplate/internalMeetingReminder.vm"); 
	        VelocityContext context = new VelocityContext();
	        
	      //  context.put("title", vm.name);
	       // context.put("location", loc.getName());
	       // context.put("meetingBy", user.getFirstName()+" "+user.getLastName());
	        
	        String months[] = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
		       
	        int dayOfmonth=1;
	        int month=0;
	        try {
	        	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	        	String dateInString = formatter.format(confirmDate);
	        	String arr[] = dateInString.toString().split("-");
		        if(arr.length >=2){
		        	dayOfmonth = Integer.parseInt(arr[0]);
			        month = Integer.parseInt(arr[1]);
		        }else{
		        	Date date = formatter.parse(dateInString);
		        	Calendar cal = Calendar.getInstance();
			         cal.setTime((Date)date);
			         dayOfmonth = cal.get(Calendar.DAY_OF_MONTH);
			         month = cal.get(Calendar.MONTH)+1;
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}
	        String monthName = months[month-1];
	        context.put("hostnameUrl", imageUrlPath);
	       // context.put("siteLogo", logo.logoImagePath);
	        context.put("dayOfmonth", dayOfmonth);
	        context.put("monthName", monthName);
	        //context.put("confirmTime", map.get("confirmTime"));
	        context.put("userList",list);
	        
	        SimpleDateFormat localDateFormat = new SimpleDateFormat("hh:mm:aa");
	        String time = localDateFormat.format(confirmTime);

	        context.put("time",time);
	       // context.put("disc", vm.getReason());
	       
	        StringWriter writer = new StringWriter();
	        t.merge( context, writer );
	        String content = writer.toString();
			
			messageBodyPart.setContent(content, "text/html");
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
			Transport.send(message);
			System.out.println("email Succ");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	    
    
    
	public static void meetingReminderAfterDay(List<AuthUser> listForUser,String communicationEmail, Date confirmDate,Date confirmTime,String subject){
		/*InternetAddress[] usersArray = new InternetAddress[userList.size()];
		int index = 0;
		for (AuthUser assi : userList) {
			try {
				
				usersArray[index] = new InternetAddress(assi.getCommunicationemail());
				index++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
		List<UserVM> list = new ArrayList<>() ;
		for(AuthUser assi : listForUser){
			
			UserVM vm1=new UserVM();
			vm1.fullName=assi.firstName+" "+assi.lastName;
			list.add(vm1);
			
			
			
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
			message.setFrom(new InternetAddress(emailUser,emailName));
			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(communicationEmail));
			/*usersArray*/
			message.setSubject(subject);
			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			ve.init();
			
			Template t = ve.getTemplate("/public/emailTemplate/internalMeetingReminderAfterDay.vm"); 
	        VelocityContext context = new VelocityContext();
	        
	      //  context.put("title", vm.name);
	       // context.put("location", loc.getName());
	       // context.put("meetingBy", user.getFirstName()+" "+user.getLastName());
	        
	        String months[] = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
		       
	        int dayOfmonth=1;
	        int month=0;
	        try {
	        	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	        	String dateInString = formatter.format(confirmDate);
	        	String arr[] = dateInString.toString().split("-");
		        if(arr.length >=2){
		        	dayOfmonth = Integer.parseInt(arr[0]);
			        month = Integer.parseInt(arr[1]);
		        }else{
		        	Date date = formatter.parse(dateInString);
		        	Calendar cal = Calendar.getInstance();
			         cal.setTime((Date)date);
			         dayOfmonth = cal.get(Calendar.DAY_OF_MONTH);
			         month = cal.get(Calendar.MONTH)+1;
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}
	        String monthName = months[month-1];
	        context.put("hostnameUrl", imageUrlPath);
	       // context.put("siteLogo", logo.logoImagePath);
	        context.put("dayOfmonth", dayOfmonth);
	        context.put("monthName", monthName);
	        //context.put("confirmTime", map.get("confirmTime"));
	        context.put("userList",list);
	        
	        SimpleDateFormat localDateFormat = new SimpleDateFormat("hh:mm:aa");
	        String time = localDateFormat.format(confirmTime);
	        
	        context.put("time",time);
	       // context.put("disc", vm.getReason());
	       
	        StringWriter writer = new StringWriter();
	        t.merge( context, writer );
	        String content = writer.toString();
			
			messageBodyPart.setContent(content, "text/html");
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
			Transport.send(message);
			System.out.println("email Succ");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	    
	
	
    
    public static Result getScheduleDates() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		Date curr = new Date();
    		String cDate = df.format(curr);
    		Date cD = null;
    		try {
				cD = df.parse(cDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		AuthUser user = getLocalUser();
    		List<SqlRow> rows = ScheduleTest.getScheduleDates(user, cDate);
    		List<RequestInfoVM> vmList = new ArrayList<>();
    		for(SqlRow row : rows) {
    			RequestInfoVM vm = new RequestInfoVM();
    			vm.confirmDate = row.getString("confirm_date");
    			vmList.add(vm);
    		}
    		
    		List<SqlRow> rowsRequest = RequestMoreInfo.getRequestedDates(user, cDate);
    		for(SqlRow row : rowsRequest) {
    			RequestInfoVM vm = new RequestInfoVM();
    			vm.confirmDate = row.getString("confirm_date");
    			vmList.add(vm);
    		}
    		
    		List<SqlRow> rowsTrad = TradeIn.getTradeDates(user, cDate);
    		for(SqlRow row : rowsTrad) {
    			RequestInfoVM vm = new RequestInfoVM();
    			vm.confirmDate = row.getString("confirm_date");
    			vmList.add(vm);
    		}
    		
    		/*List<SqlRow> toDoRows = ToDo.getToDoDates(cDate);
    		for(SqlRow todo : toDoRows) {
    			RequestInfoVM vm = new RequestInfoVM();
    			vm.confirmDate = todo.getString("due_date");
    			vmList.add(vm);
    		}*/
    		
    		return ok(Json.toJson(vmList));
    	}
    }
    
    public static Result getScheduleBySelectedDate(String date) throws ParseException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser user = getLocalUser();
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		SimpleDateFormat df2 = new SimpleDateFormat("MM/dd/yyyy");
    		Date dateObj = df.parse(date);
    		Map<Long,Integer> setGroupid = new HashMap<Long,Integer>();
    		//List<ScheduleTest> scheduleList = ScheduleTest.findByDateAndAssignedUser(user, dateObj);
    		List<RequestMoreInfo> requInfos = RequestMoreInfo.findByDateAndAssignedUser(user, dateObj);
    		//List<TradeIn> traIns = TradeIn.findByDateAndAssignedUser(user, dateObj);
    		List<RequestInfoVM> vmList = new ArrayList<>();
    		
    		Calendar time = Calendar.getInstance();
    	
    		
    		//List<ScheduleTest> scheduleList1 = null;
   		 fillLeadsData(requInfos, vmList);
    		
    		return ok(Json.toJson(vmList));
    	}
    }
    
    public static Result getUsersToAssign() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		List<AuthUser> usersList = AuthUser.getUserByType();
    		List<UserVM> vmList = new ArrayList<>();
    		for(AuthUser obj: usersList) {
    			UserVM vm = new UserVM();
    			vm.fullName = obj.firstName+" "+obj.lastName;
    			vm.id = obj.id;
    			vmList.add(vm);
    		}
    		return ok(Json.toJson(vmList));
    	}
    }
    
    public static Result saveToDoData() throws ParseException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		Form<ToDoVM> form = DynamicForm.form(ToDoVM.class).bindFromRequest();
    		AuthUser user = getLocalUser();
    		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    		ToDoVM vm = form.get();
    		ToDo toDo = new ToDo();
    		toDo.task = vm.task;
    		toDo.dueDate = df.parse(vm.dueDate);
    		toDo.assignedTo = AuthUser.findById(vm.assignedToId);
    		toDo.priority = vm.priority;
    		toDo.status = "Assigned";
    		toDo.assignedBy = user;
    		toDo.saveas = 0;
			//toDo.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		toDo.save();
    		
    		return ok();
    	}
    }
    
    public static Result getToDoList() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    		List<ToDo> toDoList = ToDo.findAll();
    		List<ToDoVM> vmList = new ArrayList<>();
    		for(ToDo todo: toDoList) {
    			ToDoVM vm = new ToDoVM();
    			vm.id = todo.id;
    			vm.task = todo.task;
    			vm.dueDate = df.format(todo.dueDate);
    			vm.assignedToId = todo.assignedTo.id;
    			vm.priority = todo.priority;
    			vm.status = todo.status;
    			vm.assignedById = todo.assignedBy.id;
    			vm.saveas = todo.saveas;
    			
    			vmList.add(vm);
    		}
    		return ok(Json.toJson(vmList));
    	}
    }
    
    public static Result saveCompleteTodoStatus(Long id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		ToDo todo = ToDo.findById(id);
    		todo.setStatus("Completed");
    		todo.update();
    		return ok();
    	}	
    }
    
    public static Result saveCancelTodoStatus(Long id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		ToDo todo = ToDo.findById(id);
    		todo.setStatus("Deleted");
    		todo.update();
    		return ok();
    	}	
    }
    
    public static Result getToDoBySelectedDate(String date) throws ParseException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    		SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
    		Date dateObj = df2.parse(date);
    		List<ToDo> toDoList = ToDo.findByDate(dateObj);
    		List<ToDoVM> vmList = new ArrayList<>();
    		for(ToDo todo: toDoList) {
    			ToDoVM vm = new ToDoVM();
    			vm.id = todo.id;
    			vm.task = todo.task;
    			vm.dueDate = df.format(todo.dueDate);
    			vm.assignedToId = todo.assignedTo.id;
    			vm.priority = todo.priority;
    			vm.status = todo.status;
    			vm.assignedById = todo.assignedBy.id;
    			vmList.add(vm);
    		}
    		return ok(Json.toJson(vmList));
    	}
    }
    public static Result getSalesUserList(Long managerid){
    	
    	AuthUser user = getLocalUser();	
        List<AuthUser> SalesUserList = AuthUser.findByLocatio(Location.findById(managerid));
        List<UserVM> vmList = new ArrayList<>();
        for(AuthUser obj: SalesUserList) {
        	if(obj.role.equalsIgnoreCase("Sales Person")){
        		UserVM vm = new UserVM();
        		vm.firstName = obj.firstName;
        		vm.id = obj.id;
        		vmList.add(vm);
        	}
     }
		return ok(Json.toJson(vmList));   	
    	    	   	   	
 }
    
    
    
    public static Result getSalesUser() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser user = getLocalUser();
    		
    		//List<AuthUser> SalesUserList = AuthUser.getAllUserByLocation(Location.findById(Long.valueOf(session("USER_LOCATION"))));
    		List<AuthUser> SalesUserList = AuthUser.findByLocatio(Location.findById(Long.valueOf(session("USER_LOCATION"))));
    		
    		
    		List<UserVM> vmList = new ArrayList<>();
    		if(user.role != null) {
    			if(user.role.equals("General Manager") && user.role.equals("Manager")) {
	    			UserVM gmObj = new UserVM();
		    		gmObj.fullName = user.firstName+" "+user.lastName;
		    		gmObj.id = user.id;
					vmList.add(gmObj);
    			}
    		}
    		for(AuthUser obj: SalesUserList) {
    			UserVM vm = new UserVM();
    			vm.fullName = obj.firstName+" "+obj.lastName;
    			vm.id = obj.id;
    			vmList.add(vm);
    		}
    		return ok(Json.toJson(vmList));
    	}
    }
    
   
    
    public static Result getSalesUserOnly(Long locationValue) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		String[] monthName = { "january", "february", "march", "april", "may", "june", "july",
    		        "august", "september", "october", "november", "december" };
    		Calendar now = Calendar.getInstance();
    		String month = monthName[now.get(Calendar.MONTH)];
    		
    		List<PlanScheduleMonthlySalepeople> pSalepeople = PlanScheduleMonthlySalepeople.findByAllLocationAndMonth(Location.findById(Long.valueOf(session("USER_LOCATION"))),month);
    		//session("USER_LOCATION")
    		List<AuthUser> SalesUserList;
    		if(locationValue == 0){
    			SalesUserList = AuthUser.getAllSalesUser();
    		}else{
    			SalesUserList = AuthUser.getAllUserByLocation(Location.findById(locationValue));
    		}
    		
    		List<UserVM> vmList = new ArrayList<>();
    		
    		for(AuthUser obj: SalesUserList) {
    			
    			UserVM vm = new UserVM();
    			vm.fullName = obj.firstName+" "+obj.lastName;
    			vm.id = obj.id;
    			for(PlanScheduleMonthlySalepeople ps:pSalepeople){
    				if(obj.id.equals(ps.user.id)){
        				vm.quota = ps.totalBrought;
        			}
    			}
    			
    			vmList.add(vm);
    		}
    		return ok(Json.toJson(vmList));
    	}
    }
    
    public static Result getAllSalesPersonScheduleTestAssigned(Integer id){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser user;
    		if(id == 0){
    			user = getLocalUser();
    		}else{
    			user = AuthUser.findById(id);
    		}
	    	
	    	//List<ScheduleTest> listData = ScheduleTest.findAllAssigned(user);
	    	List<RequestMoreInfo> requestMoreInfos = RequestMoreInfo.findAllScheduledUser(user);
	    	//List<TradeIn> tradeIns = TradeIn.findAllScheduledUser(user);
	    	List<RequestInfoVM> infoVMList = new ArrayList<>();
	    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    	SimpleDateFormat timedf = new SimpleDateFormat("HH:mm:ss");
	    	Calendar time = Calendar.getInstance();
	    
	    	for(RequestMoreInfo info: requestMoreInfos) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		AddCollection product = AddCollection.findById(Long.parseLong(info.productId));
	    		vm.productId = info.productId;
	    		if(product != null) {
	    			vm.title = product.title;
	    			vm.price = (int) product.price;
	    			ProductImages pImage = ProductImages.findDefaultImg(product.id);
	        		if(pImage!=null) {
	        			vm.imgId = pImage.getId().toString();
	        		}
	        		else {
	        			vm.imgId = "/assets/images/no-image.jpg";
	        		}
	    		}
	    		
	    		vm.isCompleteFlag = info.isCompleteFlag;
	    		vm.name = info.name;
	    		vm.phone = info.phone;
	    		vm.email = info.email;
	    		vm.custZipCode = info.custZipCode;
	    		vm.enthicity = info.enthicity;
	    		if(info.bestDay != null){
	    			vm.bestDay = info.bestDay;
	    		}
	    		vm.bestTime = info.bestTime;
	    		
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
	    		LeadType lType = LeadType.findById(Long.parseLong(info.isContactusType));
	    		if(lType != null){
	    			    vm.typeOfLead = lType.leadName;
		    			Application.findCustomeData(info.id,vm,lType.id);
	    		}
	    		vm.option = 1;
	    		infoVMList.add(vm);
	    		//findRequestParentChildAndBro(infoVMList, info, df, vm);
	    		
	    		
	    	}
	    	
	    	return ok(Json.toJson(infoVMList));
    	}
    	
    	
    }
    
    public static Result getAllSalesPersonContactUsSeen(Integer id){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser user;
    		if(id == 0){
    			user = getLocalUser();
    		}else{
    			user = AuthUser.findById(id);
    		}
    			
	    	List<RequestMoreInfo> listData = RequestMoreInfo.findAllSeenContactUs(user);
	    	List<RequestInfoVM> infoVMList = new ArrayList<>();
	    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    	SimpleDateFormat timedf = new SimpleDateFormat("HH:mm:ss");
	    	for(RequestMoreInfo info: listData) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
	    		vm.vin = info.vin;
	    		if(vehicle != null) {
	    			vm.model = vehicle.model;
	    			vm.make = vehicle.make;
	    			vm.typeofVehicle=vehicle.typeofVehicle;
	    			vm.stock = vehicle.stock;
	    			vm.mileage = vehicle.mileage;
	    			vm.year = vehicle.year;
	    			vm.bodyStyle =vehicle.bodyStyle;
	    			vm.drivetrain = vehicle.drivetrain;
	    			vm.engine = vehicle.engine;
	    			vm.transmission = vehicle.transmission;
	    			vm.price = vehicle.price;
	    			InventoryImage vehicleImage = InventoryImage.getDefaultImage(vehicle.vin);
	        		if(vehicleImage!=null) {
	        			vm.imgId = vehicleImage.getId().toString();
	        		}
	        		else {
	        			vm.imgId = "/assets/images/no-image.jpg";
	        		}
	        		vm.price = vehicle.price;
	    		}
	    		vm.name = info.name;
	    		vm.phone = info.phone;
	    		vm.email = info.email;
	    		vm.custZipCode = info.custZipCode;
	    		vm.enthicity = info.enthicity;
	    		
	    		vm.requestDate = df.format(info.requestDate);
	    		vm.typeOfLead = "Request More Info";
	    		//List<UserNotes> notesList = UserNotes.findRequestMoreByUser(info, info.assignedTo);
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
	    		
	    		findRequestParentChildAndBro(infoVMList, info, df, vm);
	    	
	    	}
	    	
	    	return ok(Json.toJson(infoVMList));
    	}	
    }
    
   /* public static Result getAllSalesPersonRequestInfoSeen(Integer id){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser user;
    		if(id == 0){
    			user = getLocalUser();
    		}else{
    			user = AuthUser.findById(id);
    		}
    			
	    	List<RequestMoreInfo> listData = RequestMoreInfo.findAllSeen(user);
	    	List<RequestInfoVM> infoVMList = new ArrayList<>();
	    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    	SimpleDateFormat timedf = new SimpleDateFormat("HH:mm:ss");
	    	for(RequestMoreInfo info: listData) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		Inventory product = Inventory.getByProductId(info.productId);
	    		vm.productId = info.productId;
	    		if(product != null) {
	    			vm.title = product.title;
	    			vm.price = (int) product.price;
	    			InventoryImage pImage = InventoryImage.getDefaultImage(product.productId);
	        		if(pImage!=null) {
	        			vm.imgId = pImage.getId().toString();
	        		}
	        		else {
	        			vm.imgId = "/assets/images/no-image.jpg";
	        		}
	    		}
	    		if(vm.title == null){
	    			vm.title = info.section;
	    		}
	    		vm.name = info.name;
	    		vm.phone = info.phone;
	    		vm.email = info.email;
	    		vm.custZipCode = info.custZipCode;
	    		vm.enthicity = info.enthicity;
	    		
	    		vm.requestDate = df.format(info.requestDate);
	    		vm.typeOfLead = "Request More Info";
	    		//List<UserNotes> notesList = UserNotes.findRequestMoreByUser(info, info.assignedTo);
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
	    		
	    		findCustomeData(info.id,vm,1L);
	    		
	    		findRequestParentChildAndBro(infoVMList, info, df, vm);
	    		
	    	
	    	}
	    	
	    	return ok(Json.toJson(infoVMList));
    	}	
    
    	
    }
    */
    
    /*public static Result getAllSalesPersonTradeInSeen(Integer id){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser user;
    		if(id == 0){
    			user = getLocalUser();
    		}else{
    			user = AuthUser.findById(id);
    		}
	    	List<TradeIn> listData = TradeIn.findAllSeen(user);
	    	List<RequestInfoVM> infoVMList = new ArrayList<>();
	    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	    	SimpleDateFormat timedf = new SimpleDateFormat("HH:mm:ss");
	    	for(TradeIn info: listData) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		Inventory product = Inventory.getByProductId(info.productId);
	    		vm.productId = info.productId;
	    		if(product != null) {
	    			vm.title = product.title;
	    			vm.price = (int) product.price;
	    			InventoryImage pImage = InventoryImage.getDefaultImage(product.productId);
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
	    		vm.custZipCode = info.custZipCode;
	    		vm.enthicity = info.enthicity;
	    		vm.pdfPath = info.pdfPath;
	    		vm.requestDate = df.format(info.tradeDate);
	    		vm.typeOfLead = "Trade-In Appraisal";
	    		
	    		LeadVM lVm = new LeadVM();
	    		lVm.id = info.id.toString();
	    		if(!info.comments.equals("null")){
	    			lVm.comments = info.comments;
	    		}
	    		if(!info.year.equals("null")){
	    			lVm.year = info.year;
	    		}
	    	
	    		if(!info.leaseOrRental.equals("null")){
	    			lVm.rentalReturn = info.leaseOrRental;
	    		}
	    		if(!info.operationalAndAccurate.equals("null")){
	    			lVm.odometerAccurate = info.operationalAndAccurate;
	    		}
	    			lVm.serviceRecords = info.serviceRecord;
	    		if(!info.lienholder.equals("null")){
	    			lVm.lienholder = info.lienholder;
	    		}
	    			lVm.prefferedContact = info.preferredContact;
	    		if(!info.equipment.equals("null")){
	    			lVm.equipment = info.equipment;
	    		}
	    	
	    		if(!info.paint.equals("null")){
	    			lVm.paint = info.paint;
	    		}
	    		if(!info.salvage.equals("null")){
	    			lVm.salvage = info.salvage;
	    		}
	    		if(!info.damage.equals("null")){
	    			lVm.damage = info.damage;
	    		}
	    			lVm.titleholder = info.holdsThisTitle;
	    			lVm.prefferedContact = info.preferredContact;
    			List<String> sList = new ArrayList<>();
    			String arr[] =  info.optionValue.split(",");
    			for(int i=0;i<arr.length;i++){
    				sList.add(arr[i]);
    			}
    			lVm.options = sList;
    		vm.leadsValue = lVm;
    		
	    		List<UserNotes> notesList = UserNotes.findTradeIn(info);
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
	    		vm.requestDate = df.format(info.tradeDate);
	    		if(info.isRead == 0) {
	    			vm.isRead = false;
	    		}
	    		
	    		if(info.isRead == 1) {
	    			vm.isRead = true;
	    		}
	    		
	    		findTreadParentChildAndBro(infoVMList, info, df, vm);
	    		
	    		
	    	}
	    	
	    	return ok(Json.toJson(infoVMList));
    	}	
    	
    	
    }*/
    public static Result getWeekChartData(Integer id) throws ParseException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser user = getLocalUser();
    		Date date = new Date();
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(date);
    		cal.add(Calendar.DATE, -7);
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		List<AuthUser> usersList = new ArrayList<>();
    		if(id == 0) {
    			usersList = AuthUser.getAllUsers();
    		} else {
    			AuthUser userData = AuthUser.findById(id);
    			usersList.add(userData);
    		}
    		List<SqlRow> vehicleList = Vehicle.getSoldDataOfRange(user, usersList, df.format(date), df.format(cal.getTime()));
    		List<BarChartVM> data = new ArrayList<>();
    		BarChartVM price = new BarChartVM();
    		List<List> priceListData = new ArrayList<>();
    		
    		for(SqlRow vehicle: vehicleList) {
    			List l = new ArrayList();
    			Date dt = (Date)vehicle.get("sold_date");
    			cal.setTime(dt);
    			l.add(cal.getTimeInMillis());
    			l.add(Float.parseFloat(vehicle.getString("sum(vehicle.price)")));
    			priceListData.add(l);
    		}
    		price.key = "Quantity";
    		price.bar = true;
    		price.values = priceListData;
    		data.add(price);
    		return ok(Json.toJson(data));
    	}
    }
    
    public static Result getMonthChartData(Integer id) throws ParseException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser user = getLocalUser();
    		Date date = new Date();
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(date);
    		cal.add(Calendar.DATE, -30);
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		List<AuthUser> usersList = new ArrayList<>();
    		if(id == 0) {
    			usersList = AuthUser.getAllUsers();
    		} else {
    			AuthUser userData = AuthUser.findById(id);
    			usersList.add(userData);
    		}
    		List<SqlRow> vehicleList = Vehicle.getSoldDataOfRange(user, usersList,df.format(date),df.format(cal.getTime()));
    		List<BarChartVM> data = new ArrayList<>();
    		BarChartVM price = new BarChartVM();
    		List<List> priceListData = new ArrayList<>();
    		
    		for(SqlRow vehicle: vehicleList) {
    			List l = new ArrayList();
    			Date dt = (Date)vehicle.get("sold_date");
    			cal.setTime(dt);
    			l.add(cal.getTimeInMillis());
    			l.add(Float.parseFloat(vehicle.getString("sum(vehicle.price)")));
    			priceListData.add(l);
    		}
    		price.key = "Quantity";
    		price.bar = true;
    		price.values = priceListData;
    		data.add(price);
    		return ok(Json.toJson(data));
    	}
    }
    
    public static Result getThreeMonthChartData(Integer id) throws ParseException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser user = getLocalUser();
    		Date date = new Date();
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(date);
    		cal.add(Calendar.DATE, -90);
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		List<AuthUser> usersList = new ArrayList<>();
    		if(id == 0) {
    			usersList = AuthUser.getAllUsers();
    		} else {
    			AuthUser userData = AuthUser.findById(id);
    			usersList.add(userData);
    		}
    		List<SqlRow> vehicleList = Vehicle.getSoldDataOfRange(user, usersList,df.format(date),df.format(cal.getTime()));
    		List<BarChartVM> data = new ArrayList<>();
    		BarChartVM price = new BarChartVM();
    		List<List> priceListData = new ArrayList<>();
    		
    		for(SqlRow vehicle: vehicleList) {
    			List l = new ArrayList();
    			Date dt = (Date)vehicle.get("sold_date");
    			cal.setTime(dt);
    			l.add(cal.getTimeInMillis());
    			l.add(Float.parseFloat(vehicle.getString("sum(vehicle.price)")));
    			priceListData.add(l);
    		}
    		price.key = "Quantity";
    		price.bar = true;
    		price.values = priceListData;
    		data.add(price);
    		return ok(Json.toJson(data));
    	}
    }
    
    public static Result getSixMonthChartData(Integer id) throws ParseException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser user = getLocalUser();
    		Date date = new Date();
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(date);
    		cal.add(Calendar.DATE, -180);
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		List<AuthUser> usersList = new ArrayList<>();
    		if(id == 0) {
    			usersList = AuthUser.getAllUsers();
    		} else {
    			AuthUser userData = AuthUser.findById(id);
    			usersList.add(userData);
    		}
    		List<SqlRow> vehicleList = Vehicle.getSoldDataOfRange(user, usersList,df.format(date),df.format(cal.getTime()));
    		List<BarChartVM> data = new ArrayList<>();
    		BarChartVM price = new BarChartVM();
    		List<List> priceListData = new ArrayList<>();
    		
    		for(SqlRow vehicle: vehicleList) {
    			List l = new ArrayList();
    			Date dt = (Date)vehicle.get("sold_date");
    			cal.setTime(dt);
    			l.add(cal.getTimeInMillis());
    			l.add(Float.parseFloat(vehicle.getString("sum(vehicle.price)")));
    			priceListData.add(l);
    		}
    		price.key = "Quantity";
    		price.bar = true;
    		price.values = priceListData;
    		data.add(price);
    		return ok(Json.toJson(data));
    	}
    }
    
    public static Result getYearChartData(Integer id) throws ParseException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser user = getLocalUser();
    		Date date = new Date();
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(date);
    		cal.add(Calendar.DATE, -365);
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		List<AuthUser> usersList = new ArrayList<>();
    		if(id == 0) {
    			usersList = AuthUser.getAllUsers();
    		} else {
    			AuthUser userData = AuthUser.findById(id);
    			usersList.add(userData);
    		}
    		List<SqlRow> vehicleList = Vehicle.getSoldDataOfRange(user, usersList,df.format(date),df.format(cal.getTime()));
    		List<BarChartVM> data = new ArrayList<>();
    		BarChartVM price = new BarChartVM();
    		List<List> priceListData = new ArrayList<>();
    		
    		for(SqlRow vehicle: vehicleList) {
    			List l = new ArrayList();
    			Date dt = (Date)vehicle.get("sold_date");
    			cal.setTime(dt);
    			l.add(cal.getTimeInMillis());
    			l.add(Float.parseFloat(vehicle.getString("sum(vehicle.price)")));
    			priceListData.add(l);
    		}
    		price.key = "Quantity";
    		price.bar = true;
    		price.values = priceListData;
    		data.add(price);
    		return ok(Json.toJson(data));
    	}
    }
    
    public static Result getRangeChartData(Integer id,String start,String end) throws ParseException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser user = getLocalUser();
    		Calendar cal = Calendar.getInstance();
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		List<AuthUser> usersList = new ArrayList<>();
    		if(id == 0) {
    			usersList = AuthUser.getAllUsers();
    		} else {
    			AuthUser userData = AuthUser.findById(id);
    			usersList.add(userData);
    		}
    		List<SqlRow> vehicleList = Vehicle.getSoldDataOfRange(user, usersList,end,start);
    		List<BarChartVM> data = new ArrayList<>();
    		BarChartVM price = new BarChartVM();
    		List<List> priceListData = new ArrayList<>();
    		
    		for(SqlRow vehicle: vehicleList) {
    			List l = new ArrayList();
    			Date dt = (Date)vehicle.get("sold_date");
    			cal.setTime(dt);
    			l.add(cal.getTimeInMillis());
    			l.add(Float.parseFloat(vehicle.getString("sum(vehicle.price)")));
    			priceListData.add(l);
    		}
    		price.key = "Quantity";
    		price.bar = true;
    		price.values = priceListData;
    		data.add(price);
    		return ok(Json.toJson(data));
    	}
    }
    
    public static Result getPerformanceOfUser(String top,String worst,String week,String month,String year,String allTime,Integer id,Long locationValue,String startD,String endD) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	}else{
    		Date date = new Date();
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(date);
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");
    		String start1 = "";
			String end1 = "";
			Date startDate=null;
			Date endDate=null;
			Date start = null;
			Date end = null;
			
			try{
				startDate=df1.parse(startD);
				endDate=df1.parse(endD);
				start=startDate;
				end=endDate;
			}catch (ParseException e1){
				e1.printStackTrace();
			}
			
			if(year.equals("true")){
				cal.add(Calendar.DATE, -365);
				start1 = df.format(cal.getTime());
    			end1 = df.format(date);
    			try {
					start = df.parse(start1);
					end = df.parse(end1);
				}catch (ParseException e){
					e.printStackTrace();
				}
			}
			
			if(allTime.equals("true")){
				cal.add(Calendar.DATE, -1000);
				start1 = df.format(cal.getTime());
    			end1 = df.format(date);
    			try {
					start = df.parse(start1);
					end = df.parse(end1);
				}catch (ParseException e){
					e.printStackTrace();
				}
			}
			
			int requestLeadCount = 0;
	    	int requestLeadCount1 = 0;
	    	
	    	List<RequestMoreInfo> rInfo = null;
	    	List<RequestMoreInfo> rInfoAll = null;
			
			List<UserVM> userList = new ArrayList<>();
			List<AuthUser> salesUsersList;
			if(locationValue == 0){
				salesUsersList = AuthUser.getAllSalesUser();
			}else{
				salesUsersList = AuthUser.getAllUserByLocation(Location.findById(locationValue));
			}
			
			UserVM[] tempuserList = new UserVM[salesUsersList.size()];
			int index=0;
			    				
			for(AuthUser sales: salesUsersList) {
				requestLeadCount = 0;
		    	requestLeadCount1 = 0;
		    	
		    	rInfo = null;
		    	rInfoAll = null;
		    	Integer total = 0;
				UserVM vm = new UserVM();
				SimpleDateFormat dform = new SimpleDateFormat("MMMM");

				String[] monthName = { "January", "February", "March", "April", "May", "June", "July",
				        "August", "September", "October", "November", "December" };
		     	String crMonth = monthName[cal.get(Calendar.MONTH)];
				PlanScheduleMonthlySalepeople  pMonthlySalepeople = null;
				pMonthlySalepeople = PlanScheduleMonthlySalepeople.findByUserMonth(sales, crMonth);
				vm.planFlag = 0; 
				
				if(pMonthlySalepeople != null){
					if(pMonthlySalepeople.totalBrought != null){
						total = Integer.parseInt(pMonthlySalepeople.totalBrought);
					}
					
		    	}else{
						vm.planFlag = 1; 
		    	}
				vm.fullName = sales.firstName+" "+sales.lastName;
				vm.userStatus = sales.account;
				vm.id = sales.id;
				if(sales.imageUrl != null){
					if(sales.imageName !=null){
						vm.imageUrl = "http://glider-autos.com/MavenImg/images"+sales.imageUrl;
					}else{
						vm.imageUrl = sales.imageUrl;
					}
				}else{
					vm.imageUrl = "/profile-pic.jpg";
				}
				
				rInfo = RequestMoreInfo.findAllSeenSch(sales);
	    		rInfoAll = RequestMoreInfo.findAllByNotOpenLead(sales);
	    	
	    	
	    	for(RequestMoreInfo rMoreInfo:rInfo){
	    			requestLeadCount++;
	    	}

	    	for(RequestMoreInfo rMoreInfo:rInfoAll){
	    		if(start != null && end !=null){
	    			if((rMoreInfo.requestDate.after(start) && rMoreInfo.requestDate.before(end)) || rMoreInfo.requestDate.equals(end)||  rMoreInfo.requestDate.equals(start)){
	    				requestLeadCount1++;
	    			}
	    		}
	    	}
				
	    	int countLeads1 = requestLeadCount1;
	    	int countLeads = requestLeadCount;
	    	int saleCarCount = 0;
	    	double pricecount = 0;
	    	int per = 0;
	    	List<RequestMoreInfo> rInfo1 = RequestMoreInfo.findAllSeenComplete(sales);
	    	List<SoldInventory> salesCollectionList = SoldInventory.findBySoldUserAndSold(sales);
    		for (SoldInventory collInventory : salesCollectionList) {
    			if(collInventory != null){
    				if((collInventory.soldDate.after(start) && collInventory.soldDate.before(end)) || collInventory.soldDate.equals(end) || collInventory.soldDate.equals(start)){
            			saleCarCount++;
            			pricecount = pricecount + Double.parseDouble(collInventory.price);
    				}
    			}
			}
    		
    		double sucessCount = 0;
    		if(countLeads1 != 0){
    			sucessCount= (double)saleCarCount/(double)countLeads1*100;
    		}else{
    			 sucessCount = 0;
    		}
			if(pricecount > 0 && total > 0){
				per = (int) ((pricecount*100)/total);
				vm.per = per;
			}
			
    		vm.successRate = (int) sucessCount;
    		vm.salesAmount = (long)pricecount;
    		vm.currentLeads = Long.parseLong(String.valueOf(countLeads));
			vm.saleCar = Long.parseLong(String.valueOf(saleCarCount));
			vm.email = sales.communicationemail;
				tempuserList[index] = vm;
				index++;
			}
			
    		if(top.equals("true")) {
    			if(id == 0) {
    				for(int i=0;i<tempuserList.length-1;i++) {
    					for(int j=i+1;j<tempuserList.length;j++) {
    						if(tempuserList[i].successRate <= tempuserList[j].successRate) {
    							UserVM temp = tempuserList[i];
    							tempuserList[i] = tempuserList[j];
    							tempuserList[j] = temp;
    						}
    					}
    				}
    				int iValue = 0;
    				for(int i=0;i<tempuserList.length;i++) {
    					
    						userList.add(tempuserList[i]);
    				}
    			  }	
    		}
    		
    		if(worst.equals("true")) {
    			if(id == 0) {
    				for(int i=0;i<tempuserList.length-1;i++) {
    					for(int j=i+1;j<tempuserList.length;j++) {
    						if(tempuserList[i].successRate >= tempuserList[j].successRate) {
    							UserVM temp = tempuserList[i];
    							tempuserList[i] = tempuserList[j];
    							tempuserList[j] = temp;
    						}
    					}
    				}
    				int iValue = 0;
    				for(int i=0;i<tempuserList.length;i++) {
    					
    						userList.add(tempuserList[i]);
    				}
    			}
    		}	
    		return ok(Json.toJson(userList));
    	}
    }
    
    public static Result saveNoteOfUser(Long id,String note,String action) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser user = getLocalUser();
    			RequestMoreInfo requestMore = RequestMoreInfo.findById(id);
    			UserNotes notes = new UserNotes();
    			notes.note = note;
    			notes.action = action;
    			notes.requestMoreInfo = requestMore;
    			notes.saveHistory = 1;
    			if(requestMore.assignedTo !=null){
    				notes.user = requestMore.assignedTo;
    			}
    			Date date = new Date();
    			notes.createdDate = date;
    			notes.createdTime = date;
				notes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    			notes.save();
    		
    		return ok();
    	}
    }
    
    public static Result saveNoteOfALLUser() {
    	Form<CloseLeadVM> form = DynamicForm.form(CloseLeadVM.class).bindFromRequest();
		CloseLeadVM vm = form.get();
		MultipartFormData bodys = request().body().asMultipartFormData();
		AuthUser user = getLocalUser();
		
		RequestMoreInfo requestMore = RequestMoreInfo.findById(vm.userNoteId);
		UserNotes notes = new UserNotes();
		notes.note = vm.userNote;
		notes.action = vm.action;
		notes.requestMoreInfo = requestMore;
		notes.saveHistory = 1;
		if(requestMore.assignedTo !=null){
			notes.user = requestMore.assignedTo;
		}
		Date date = new Date();
		notes.createdDate = date;
		notes.createdTime = date;
		notes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		notes.save();
		if(vm.customData != null){
			saveCustomData(requestMore.id,vm.customData,bodys,Long.parseLong(requestMore.isContactusType));
		}
		
	
	return ok();
    }
    
    
    public static Result getAllAction(){
    	List<ActionAdd> actionAdd = ActionAdd.getAll();
    	return ok(Json.toJson(actionAdd));
    }
    
    public static Result saveAction(String actionV){
    	ActionAdd actionAdd = new ActionAdd();
    	actionAdd.value = actionV;
    	actionAdd.save();
    	return ok();
    }
    
    public static Result saveTestDrive() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		
    		String msg = "success";
    		boolean flag = true;
    		int flagFirstComp = 0;
    		AuthUser user = getLocalUser();
    		 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		request().body().asJson();
    		Form<RequestInfoVM> form = DynamicForm.form(RequestInfoVM.class).bindFromRequest();
    		RequestInfoVM vm = form.get();
    		Date confirmDate = null;
    		//Vehicle obj = Vehicle.findByVinAndStatus(vm.vin);
    		
    			schTestDrive(vm, msg, flag, confirmDate);
    			
    			if(flag){
        			Map map = new HashMap();
                	map.put("email",vm.email);
                	map.put("confirmDate", vm.bestDay);
                	map.put("confirmTime", vm.bestTime);
                	map.put("vin", vm.vin);
                	map.put("weatherValue", vm.weatherValue);
                	map.put("uname", user.firstName+" "+user.lastName);
                	map.put("uphone", user.phone);
                	map.put("uemail", user.email);
                	map.put("clientName",vm.name);
                	//makeToDo(vm.vin);
                	sendMail(map);
        		}
    			
    			if(vm.parentChildLead != null){
    				for(RequestInfoVM rVm:vm.parentChildLead){
    					rVm.option = vm.option;
    					msg = "success";
    		    		flag = true;
    					schTestDrive(rVm, msg, flag, confirmDate);
    					if(rVm.bestDay != null && !rVm.bestDay.equals("")){
    						if(flag){
        		    			Map map = new HashMap();
        		            	map.put("email",vm.email);
        		            	map.put("confirmDate", rVm.bestDay);
        		            	map.put("confirmTime", rVm.bestTime);
        		            	map.put("vin", rVm.vin);
        		            	map.put("uname", user.firstName+" "+user.lastName);
        		            	map.put("uphone", user.phone);
        		            	map.put("uemail", user.email);
        		            	map.put("clientName",vm.name);
        		            	//makeToDo(vm.vin);
        		            	sendMail(map);
        		    		}
    					}
    				}
    				
    			}
    			
    		
    		/*if(flag){
    			Map map = new HashMap();
            	map.put("email",vm.email);
            	map.put("email",vm.email);
            	map.put("confirmDate", confirmDate);
            	map.put("confirmTime", vm.bestTime);
            	map.put("vin", vm.vin);
            	map.put("uname", user.firstName+" "+user.lastName);
            	map.put("uphone", user.phone);
            	map.put("uemail", user.email);
            	//makeToDo(vm.vin);
            	sendMail(map);
    		}*/
        	
    		return ok(msg);
    	}
    }
    
   public static void schTestDrive(RequestInfoVM vm, String msg,boolean flag,Date confirmDate){
	   AuthUser user = getLocalUser();
	   Date currDate = new Date();
	   SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat parseTime = new SimpleDateFormat("hh:mm a");
		MultipartFormData bodys = request().body().asMultipartFormData();
		
		if(vm.bestDay != null && !vm.bestDay.equals("")){
			
			RequestMoreInfo requestMoreInfo = RequestMoreInfo.findById(vm.id);
			requestMoreInfo.setName(vm.name);
			requestMoreInfo.setEmail(vm.email);
			requestMoreInfo.setPhone(vm.phone);
			requestMoreInfo.setBestDay(vm.bestDay);
			try {
				confirmDate = df.parse(vm.bestDay);
				requestMoreInfo.setConfirmDate(confirmDate);
				requestMoreInfo.setConfirmTime(parseTime.parse(vm.bestTime));
				List<RequestMoreInfo> list = RequestMoreInfo.findByProductId(vm.productId);
				Date date = parseTime.parse(vm.bestTime);
				
				for (RequestMoreInfo info2 : list) {
					if(info2.confirmDate != null && info2.confirmTime !=null){
						if(info2.confirmDate.equals(confirmDate)){
							Date newDate = DateUtils.addHours(info2.confirmTime, 1);
							if((date.after(info2.confirmTime) && date.before(newDate)) || date.equals(info2.confirmTime)){
								msg = "error";
								flag = false;
							}
						}
					}
				}
				
				
				
			} catch(Exception e) {}
			requestMoreInfo.setBestTime(vm.bestTime);
			requestMoreInfo.setPreferredContact(vm.prefferedContact);
			requestMoreInfo.setScheduleDate(new Date());
			requestMoreInfo.setUser(user);
			requestMoreInfo.setIsScheduled(true);
			if(msg.equals("success")){
				requestMoreInfo.update();
				if(vm.customData != null){
					saveCustomData(requestMoreInfo.id,vm.customData,bodys,Long.parseLong(requestMoreInfo.getIsContactusType()));
				}
				UserNotes uNotes = new UserNotes();
	    		uNotes.setNote("Scheduled lead");
	    		uNotes.setAction("Other");
	    		uNotes.createdDate = currDate;
	    		uNotes.createdTime = currDate;
	    		uNotes.user = user;
       		    uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	    		uNotes.requestMoreInfo = RequestMoreInfo.findById(requestMoreInfo.id);
	    		uNotes.save();
			}
     }
   }
    
    public static Result getAllCanceledLeads() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	List<ScheduleTest> listData = ScheduleTest.getAllFailed(Long.valueOf(session("USER_LOCATION")));
    		
	    	List<RequestInfoVM> infoVMList = new ArrayList<>();
	    	SimpleDateFormat timedf = new SimpleDateFormat("HH:mm:ss");
	    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    	for(ScheduleTest info: listData) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
	    		vm.vin = info.vin;
	    		if(vehicle != null) {
	    			vm.model = vehicle.model;
	    			vm.make = vehicle.make;
	    			vm.stock = vehicle.stock;
	    		}
	    		vm.name = info.name;
	    		vm.phone = info.phone;
	    		vm.email = info.email;
	    		vm.status = info.reason;
	    		if(info.statusDate != null){
	    			vm.statusDate = df.format(info.statusDate);
	    		}	
	    		if(info.assignedTo != null) {
	    			vm.salesRep = info.assignedTo.getFirstName()+" "+info.assignedTo.getLastName();
	    		}
	    		if(info.scheduleDate != null){
	    			vm.requestDate = df.format(info.scheduleDate);
	    		}
	    		
	    		/*List<UserNotes> notesList = UserNotes.findScheduleTestByUser(info, info.assignedTo);*/
	    		List<UserNotes> notesList = UserNotes.findScheduleTest(info);
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
	    		vm.typeOfLead = "Schedule Test";
	    		infoVMList.add(vm);
	    	}
	    	
	    	List<RequestMoreInfo> requestData = RequestMoreInfo.findAllCancel(Long.valueOf(session("USER_LOCATION")));
	    	for(RequestMoreInfo info: requestData) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		Inventory product = Inventory.getByProductId(info.productId);
	    		vm.vin = info.vin;
	    		if(product != null) {
	    			vm.title = product.title;
	    		}
	    		vm.name = info.name;
	    		vm.phone = info.phone;
	    		vm.email = info.email;
	    		vm.status = info.status;
	    		vm.reason = info.reason;
	    		vm.custZipCode = info.custZipCode;
	    		if(info.requestDate != null){
	    			vm.requestDate = df.format(info.requestDate);
	    		}
	    		if(info.statusDate != null){
	    			vm.statusDate = df.format(info.statusDate);
	    		}
	    		if(info.assignedTo != null) {
	    			vm.salesRep = info.assignedTo.getFirstName()+" "+info.assignedTo.getLastName();
	    		}
	    		
	    		//List<UserNotes> notesList = UserNotes.findRequestMoreByUser(info, info.assignedTo);
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
	    		vm.typeOfLead = "Request More Info";
	    		infoVMList.add(vm);
	    	}
	    	
	    	List<TradeIn> tradeInData = TradeIn.findAllCanceled(Long.valueOf(session("USER_LOCATION")));
	    	for(TradeIn info: tradeInData) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
	    		vm.vin = info.vin;
	    		if(vehicle != null) {
	    			vm.model = vehicle.model;
	    			vm.make = vehicle.make;
	    			vm.stock = vehicle.stock;
	    		}
	    		if(info.lastName != null){
	    			vm.name = info.firstName+" "+info.lastName;
	    		}else{
	    			vm.name = info.firstName;
	    		}

	    		vm.phone = info.phone;
	    		vm.email = info.email;
	    		vm.status = info.reason;
	    		if(info.statusDate != null){
	    			vm.statusDate = df.format(info.statusDate);
	    		}
	    		if(info.assignedTo != null) {
	    			vm.salesRep = info.assignedTo.getFirstName()+" "+info.assignedTo.getLastName();
	    		}
	    		
	    		//List<UserNotes> notesList = UserNotes.findTradeInByUser(info, info.assignedTo);
	    		List<UserNotes> notesList = UserNotes.findTradeIn(info);
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
	    		vm.typeOfLead = "Trade In";
	    		infoVMList.add(vm);
	    	}
	    	
	    	return ok(Json.toJson(infoVMList));
    	}
    }
    
    //added by Vinayak Mane on 23-Apr-2016
    public static Result getAllCanceledLeadsById(Integer leadId) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser user;
    		if(leadId == 0){
    			user = getLocalUser();
    		}else{
    			user = AuthUser.findById(leadId);
    		}
    		List<RequestInfoVM> infoVMList = new ArrayList<>();
    		SimpleDateFormat timedf = new SimpleDateFormat("HH:mm:ss");
	    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	    	List<RequestMoreInfo> requestData = RequestMoreInfo.findAllCancelByUser(Long.valueOf(session("USER_LOCATION")),user);
	    	for(RequestMoreInfo info: requestData) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		Inventory product = Inventory.getByProductId(info.productId);
	    		vm.vin = info.vin;
	    		if(product != null) {
	    			vm.title = product.title;
	    		}
	    		vm.name = info.name;
	    		vm.phone = info.phone;
	    		vm.email = info.email;
	    		vm.status = info.status;
	    		vm.reason = info.reason;
	    		vm.custZipCode = info.custZipCode;
	    		if(info.requestDate != null){
	    			vm.requestDate = df.format(info.requestDate);
	    		}
	    		if(info.statusDate != null){
	    			vm.statusDate = df.format(info.statusDate);
	    		}
	    		if(info.assignedTo != null) {
	    			vm.salesRep = info.assignedTo.getFirstName()+" "+info.assignedTo.getLastName();
	    		}
	    		
	    		//List<UserNotes> notesList = UserNotes.findRequestMoreByUser(info, info.assignedTo);
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
	    		LeadType lType = LeadType.findById(Long.parseLong(info.isContactusType));
	    		if(lType != null){
		    				Application.findCustomeData(info.id,vm,lType.id);
	    		}
	    		
	    		vm.note = list;
	    		vm.noteFlag = nFlag;
	    		vm.typeOfLead = "Request More Info";
	    		infoVMList.add(vm);
	    	}

	    	
	    	return ok(Json.toJson(infoVMList));
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
//	String dateInString = vm.getBestDay();

	
	        
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
	
    public static Result getNewToDoCount() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser userObj = getLocalUser();
    		int count = ToDo.findAllNewCountByUser(userObj);
    		Map map = new HashMap();
    		map.put("count", count);
    		if(count==1) {
    			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    			ToDo todo = ToDo.findAllNewByUser(userObj).get(0);
    			ToDoVM doVM = new ToDoVM();
    			doVM.assignedById = todo.getAssignedBy().id;
    			doVM.priority = todo.getPriority();
    			doVM.task = todo.getTask();
    			doVM.dueDate = df.format(todo.dueDate);
    			map.put("data", doVM);
    		}
    		return ok(Json.toJson(map));
    	}
    }
    
    public static Result setTodoSeen() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser userObj = getLocalUser();
    		List<ToDo> todoList = ToDo.findAllNewByUser(userObj);
    		for(ToDo todo : todoList) {
    			todo.setSeen("Seen");
    			todo.update();
    		}
    		return ok();
    	}
    }
    
    
    public static Result setApplointmentCancel(String id){
    	RequestMoreInfo info = RequestMoreInfo.findById(Long.parseLong(id));
    	if(info != null){
    		info.setIsCompleteFlag(false);
			info.update();
    	}
    	return ok();
    }
    
    public static Result setApplointmentComp(){
    	Form<CloseLeadVM> form = DynamicForm.form(CloseLeadVM.class).bindFromRequest();
		CloseLeadVM vm = form.get();
		MultipartFormData bodys = request().body().asMultipartFormData();
		AuthUser user = getLocalUser();
		Date currDate = new Date();
		
		
		//String arr[] = arrayString.split(",");
		for(String value:vm.leadIds){
			RequestMoreInfo info = RequestMoreInfo.findById(Long.parseLong(value));
			
			String vin=info.vin;
			//info.setStatus("COMPLETE");
			info.setIsCompleteFlag(true);
			info.setStatusDate(currDate);
			info.setStatusTime(currDate);
			info.update();
			if(vm.customData != null){
				saveCustomData(info.id,vm.customData,bodys,Long.parseLong(info.isContactusType));
			}
			
			
			/*if(info.confirmDate != null){
				Map map = new HashMap();
	    		map.put("email",clientEmail);
	    		map.put("vin", vin);
	    		map.put("uname", user.firstName+" "+user.lastName);
	    		map.put("uphone", user.phone);
	    		map.put("uemail", user.email);
	    		map.put("clintName", info.name);
				cancelTestDriveMail(map);
    	    	  //sendEmail(clientEmail,subject,comments);
    			}*/
			
    		UserNotes uNotes = new UserNotes();
    		uNotes.setNote("product sold");
    		uNotes.setAction("Other");
    		uNotes.createdDate = currDate;
    		uNotes.createdTime = currDate;
    		uNotes.user = user;
    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		uNotes.requestMoreInfo = RequestMoreInfo.findById(info.id);
    		uNotes.save();
		}
		
		
    	return ok();
    }
    
    public static Result restoreLead(Long id,String type){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		if(type.equals("Request More Info")) {
    			RequestMoreInfo info = RequestMoreInfo.findById(id);
    			info.setStatus(null);
    			info.update();
    		}
			if(type.equals("Schedule Test")) {
			    ScheduleTest schedule = ScheduleTest.findById(id);
			    schedule.setLeadStatus(null);
			    schedule.update();
			}
			if(type.equals("Trade In")) {
				TradeIn tradeIn = TradeIn.findById(id);
				tradeIn.setStatus(null);
				tradeIn.update();
			}
    		return ok();
    	}
    }
    
    public static Result deleteCanceledLead(String arrayString) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		
    		String arr[] = arrayString.split(",");
    		for(int i=0;i<arr.length;i++){
    			RequestMoreInfo info = RequestMoreInfo.findById(Long.parseLong(arr[i]));
    			List<UserNotes> noteList = UserNotes.findRequestMore(info);
    			for(UserNotes note: noteList) {
    				note.delete();
    			}
    			info.delete();
    		}
    			
			
    		return ok();
    	}
    }
    
    public static Result getAssignedLeads() {
    	AuthUser user = (AuthUser)getLocalUser();
    	List<ScheduleTest> tests = ScheduleTest.findAllReassigned(user);
    	List<RequestMoreInfo> infos = RequestMoreInfo.findAllReassigned(user);
    	List<TradeIn> tradeIns = TradeIn.findAllReassigned(user);
    	Map map = new HashMap();
    	map.put("count", tests.size()+infos.size()+tradeIns.size());
    	if(tests.size()+infos.size()+tradeIns.size()==1) {
    		if(tests.size()==1) {
    			RequestInfoVM infoVM = new RequestInfoVM();
    			Vehicle vehicle = Vehicle.findByVinAndStatus(tests.get(0).vin);
    			infoVM.id = tests.get(0).id;
    			infoVM.make = vehicle.getMake();
    			infoVM.leadType = "Schedule Test";
    			infoVM.model = vehicle.getModel();
    			infoVM.name = vehicle.getYear();
    			infoVM.trim = vehicle.getTrim();
    			infoVM.price = vehicle.getPrice();
    			infoVM.premiumFlag = tests.get(0).premiumFlag;
    			map.put("data", infoVM);
    		} else if(infos.size()==1) {
    			RequestInfoVM infoVM = new RequestInfoVM();
    			Vehicle vehicle = Vehicle.findByVinAndStatus(infos.get(0).vin);
    			infoVM.id = infos.get(0).id;
    			infoVM.make = vehicle.getMake();
    			if(infos.get(0).isScheduled) 
    				infoVM.leadType = "Schedule Test";
    			else
    				infoVM.leadType = "Request More Info";
    			infoVM.model = vehicle.getModel();
    			infoVM.name = vehicle.getYear();
    			infoVM.trim = vehicle.getTrim();
    			infoVM.price = vehicle.getPrice();
    			infoVM.premiumFlag = infos.get(0).premiumFlag;
    			map.put("data", infoVM);
    		} else {
    			RequestInfoVM infoVM = new RequestInfoVM();
    			Vehicle vehicle = Vehicle.findByVinAndStatus(tradeIns.get(0).vin);
    			infoVM.id = tradeIns.get(0).id;
    			infoVM.make = vehicle.getMake();
    			if(tradeIns.get(0).isScheduled) 
    				infoVM.leadType = "Schedule Test";
    			else
    				infoVM.leadType = "Trade In";
    			infoVM.model = vehicle.getModel();
    			infoVM.name = vehicle.getYear();
    			infoVM.trim = vehicle.getTrim();
    			infoVM.price = vehicle.getPrice();
    			infoVM.premiumFlag = tradeIns.get(0).premiumFlag;
    			map.put("data", infoVM);
    		}
    	}
    	return ok(Json.toJson(map));
    }
    
    public static Result setLeadSeen() {
    	AuthUser user = (AuthUser)getLocalUser();
    	List<ScheduleTest> tests = ScheduleTest.findAllReassigned(user);
    	List<RequestMoreInfo> infos = RequestMoreInfo.findAllReassigned(user);
    	List<TradeIn> tradeIns = TradeIn.findAllReassigned(user);
    	for(ScheduleTest test : tests) {
    		test.setIsReassigned(false);
    		test.update();
    	}
    	for(RequestMoreInfo info : infos) {
    		info.setIsReassigned(false);
    		info.update();
    	}
    	for(TradeIn tradeIn : tradeIns) {
    		tradeIn.setIsReassigned(false);
    		tradeIn.update();
    	}
    	return ok();
    }
    
    public static Result getAnalystData() {
    	String params = "&type=visitors-online,bounce-rate,pages&limit=all";
    	return ok(Json.parse(callClickAPI(params)));
    }
    
    
    public static Result getActionListTwo(String startDate,String endDate){
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    	Date sDate = null;
    	Date eDate = null;
    	
    	try {
			sDate = df.parse(startDate);
			eDate = df.parse(endDate); 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	List<ClickyActionList> cList = ClickyActionList.getAll(sDate, eDate);
    	
    	return ok(Json.toJson(cList));
    }
    
    
    
    
    
    public static Result getHeatMapListDale(String startD,String endD){
    	
    	String params = null;
    	params = "&type=pages&heatmap_url=1&date="+startD+","+endD+"&limit=all";
    	Location locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    	List<ClickyPagesVM> cList = new ArrayList<>();
/*    	try {
    		
			JSONArray jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
			for(int i=0;i<jsonArray.length();i++){
    			String data = jsonArray.getJSONObject(i).get("url").toString();
    			String arr[] = data.split("#_");
    			try {
	    			String findLoc[] = arr[0].split("locationId=");
	    			String locString = findLoc[1].replace("+", " ");
	    			if(locations.name.equals(locString)){
	    				ClickyPagesVM cPagesVM = new ClickyPagesVM();
	    				cPagesVM.value = jsonArray.getJSONObject(i).get("value").toString();
	    				cPagesVM.value_percent = jsonArray.getJSONObject(i).get("value_percent").toString();
	    				cPagesVM.title = jsonArray.getJSONObject(i).get("title").toString();
	    				cPagesVM.stats_url = jsonArray.getJSONObject(i).get("stats_url").toString();
	    				cPagesVM.url = jsonArray.getJSONObject(i).get("url").toString();
	    				cPagesVM.showUrl = arr[0];
	    				cList.add(cPagesVM);
	    			}
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    			
    			
			}	
			} catch (JSONException e) {
				e.printStackTrace();
			}
*/    	
    	return ok(Json.toJson(cList));
    }
    
    public static Result getSearchList(Integer value){
    	
    	int year = Calendar.getInstance().get(Calendar.YEAR);
    	String params = null;
    	
    	if(value == 30){
    		params = "&type=searches&date=last-30-days&limit=all";
    	}else if(value == 7){
    		params = "&type=searches&date=last-7-days&limit=all";
    	}else if(value == 1){
    		params = "&type=searches&date="+year+"&limit=all";
    	}
    	
    	return ok(Json.parse(callClickAPI(params)));
    }
    
    public static Result getAllVehicleDemographicsInData(){
    	Map map = new HashMap(2);
		Map<String, Integer> mapRM = new HashMap<String, Integer>();
		Map<String, Integer> mapWebBro = new HashMap<String, Integer>();
		Map<String, Integer> maplocation = new HashMap<String, Integer>();
		Map<String, Integer> mapoperatingSystem = new HashMap<String, Integer>();
		Map<String, Integer> mapSreenResoluation = new HashMap<String, Integer>();
		
		List<ClickyVisitorsList> cList= ClickyVisitorsList.getfindAll();
		//String params = "&type=visitors-list&date=last-30-days&limit=all";
    	//try {
    		
    		int lagCount = 1;
			//JSONArray jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
			for(ClickyVisitorsList click:cList){
    			String data = click.landingPage;
    			String arr[] = data.split("/");
    			if(arr.length > 5){
    			  if(arr[5] != null){
    				//  if(arr[5].equals(value)){
    					  
    					  Integer langValue = mapRM.get(click.language); 
    						if (langValue == null) {
    							mapRM.put(click.language, lagCount);
    						}else{
    							mapRM.put(click.language, mapRM.get(click.language) + 1);
    						}
    						
    						 Integer mapWebBroValue = mapWebBro.get(click.webBrowser); 
     						if (mapWebBroValue == null) {
     							mapWebBro.put(click.webBrowser, lagCount);
     						}else{
     							mapWebBro.put(click.webBrowser, mapWebBro.get(click.webBrowser) + 1);
     						}
     						
     						Integer maplocationValue = maplocation.get(click.geolocation); 
     						if (maplocationValue == null) {
     							maplocation.put(click.geolocation, lagCount);
     						}else{
     							maplocation.put(click.geolocation, maplocation.get(click.geolocation) + 1);
     						}
     						
     						Integer mapoperatingSystemValue = mapoperatingSystem.get(click.operatingSystem); 
     						if (mapoperatingSystemValue == null) {
     							mapoperatingSystem.put(click.operatingSystem, lagCount);
     						}else{
     							mapoperatingSystem.put(click.operatingSystem, mapoperatingSystem.get(click.operatingSystem) + 1);
     						}
     						
     						Integer mapSreenResoluationValue = mapSreenResoluation.get(click.screenResolution); 
     						if (mapSreenResoluationValue == null) {
     							mapSreenResoluation.put(click.screenResolution, lagCount);
     						}else{
     							mapSreenResoluation.put(click.screenResolution, mapSreenResoluation.get(click.screenResolution) + 1);
     						}
    						
     						
    				 // }
    			  }
    			}
    			
			}	
		/*} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

    	map.put("language", mapRM);
    	map.put("location", maplocation);
    	map.put("webBrowser", mapWebBro);
    	map.put("operatingSystem", mapoperatingSystem);
    	map.put("screenResoluation", mapSreenResoluation);
    	
    	
    	return ok(Json.toJson(map));
    }
    

    
    public static String callClickAPI(String params) {
    	StringBuffer response = new StringBuffer();
    	try {
    		String url = "https://api.clicky.com/api/stats/4?output=json&site_id=100998608&sitekey=8ec4755ccd21800f"+params;
		
    		URL obj = new URL(url);
    		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    		con.setRequestMethod("GET");
    		con.setRequestProperty("User-Agent", USER_AGENT);
    		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
    		String inputLine;
    		while ((inputLine = in.readLine()) != null) {
    			response.append(inputLine);
    		}
    		in.close();
    	} catch(Exception e) {}
    	return response.toString();
    }
    
    public static Result getVisitorOnline(){
    	JsonNode onlineVisitorsNode = Json.parse(callClickAPI("&type=visitors-online&limit=all"));
    	return ok(Json.toJson(onlineVisitorsNode.get(0).get("dates").get(0).get("items").get(0).get("value").asInt()));
    }
    
    private static String getbusinessname(String businessname) {
    	StringBuffer response = new StringBuffer();
    	try {
    		String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?key=AIzaSyCUNed71Bq6SCyI5SauRA7Rs9xT319HPP0&input="+businessname+"&callback=initMap";
		
    		URL obj = new URL(url);
    		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    		con.setRequestMethod("GET");
    		con.setRequestProperty("User-Agent", USER_AGENT);
    		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
    		String inputLine;
    		while ((inputLine = in.readLine()) != null) {
    			response.append(inputLine);
    		}
    		in.close();
    	} catch(Exception e) {}
    	return response.toString();
    }
    
    public static Result getbusinessData(String businessname){
    	//JsonNode onlineVisitorsNode = Json.parse(getbusinessname(businessname));
    	//return ok(Json.parse(onlineVisitorsNode));
    	String name = businessname.replaceAll(" ", " ");
    	return ok(Json.parse(getbusinessname(name)));
    }
    
    public static Result getMonthlyVisitorsStats(String startDate, String endDate) {
    	
    	Calendar c = Calendar.getInstance();
    	String[] monthsArr = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    	c.add(Calendar.MONTH, -11);
    	List<Integer> allVisitor = new ArrayList<Integer>(12);
    	List<Integer> onlineVisitor = new ArrayList<Integer>(12);
    	List<Integer> actionsList = new ArrayList<Integer>(12);
    	List<Integer> averageActionsList = new ArrayList<Integer>(12);
    	List<String> months = new ArrayList<String>(12);
    	
    	Location locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    	
    	for(int i=0;i<12;i++) {
    		String year = c.get(Calendar.YEAR)+"";
        	String month = c.get(Calendar.MONTH)+1>9?(c.get(Calendar.MONTH)+1)+"":"0"+(c.get(Calendar.MONTH)+1);
    		JsonNode node = Json.parse(callClickAPI("&date="+year+"-"+month+"&type=visitors,visitors-new,actions,actions-average&limit=all"));
    		JsonNode allVisitorNode = node.get(0);
    		JsonNode onlineVisitorNode = node.get(1);
    		JsonNode actionsNode = node.get(2);
    		JsonNode averageActionsNode = node.get(3);
    		allVisitor.add(allVisitorNode.get("dates").get(0).get("items").get(0).get("value").asInt());
    		onlineVisitor.add(onlineVisitorNode.get("dates").get(0).get("items").get(0).get("value").asInt());
    		actionsList.add(actionsNode.get("dates").get(0).get("items").get(0).get("value").asInt());
    		averageActionsList.add(averageActionsNode.get("dates").get(0).get("items").get(0).get("value").asInt());
    		months.add(monthsArr[c.get(Calendar.MONTH)]);
    		c.add(Calendar.MONTH, 1);
    	}
    	Date date = new Date();
        c.setTime(date);
        String year = c.get(Calendar.YEAR)+"";
    	String month = c.get(Calendar.MONTH)+1>9?(c.get(Calendar.MONTH)+1)+"":"0"+(c.get(Calendar.MONTH)+1);
    	Integer dateOfMonth = c.get(Calendar.DAY_OF_MONTH);
    	JsonNode onlineVisitorsNode = Json.parse(callClickAPI("&date="+startDate+","+endDate+"&type=visitors-online&limit=all"));
    	
    	JsonNode visitorsNode = Json.parse(callClickAPI("&type=visitors,actions,actions-average,time-total-pretty,time-average-pretty,bounce-rate,goals,revenue&date="+startDate+","+endDate+"&limit=all"));
    	JsonNode pagesNodeList = Json.parse(callClickAPI("&type=pages&date="+startDate+","+endDate+"&limit=all"));
    	JsonNode referersNodeList = Json.parse(callClickAPI("&type=links-domains&date="+startDate+","+endDate+"&limit=all"));
    	JsonNode searchesNodeList = Json.parse(callClickAPI("&type=searches-engines&date="+startDate+","+endDate+"&limit=all"));
    	List<PageVM> pagesList = new ArrayList<>();
    	List<PageVM> referersList = new ArrayList<>();
    	List<PageVM> searchesList = new ArrayList<>();
    	
    	for(JsonNode obj : pagesNodeList.get(0).get("dates").get(0).get("items")) {
    		try{
	    		String findLoc[] = obj.get("url").textValue().split("locationId=");
	    		String locString = findLoc[1].replace("+", " ");
	    		if(locations.name.equals(locString)){
	    			String sUrl = findLoc[0].replace("?", "");
	    			String arr[] = sUrl.split("/");
	        		PageVM vm = new PageVM();
	        		vm.count = obj.get("value").textValue();
	        		if(arr[arr.length-2].equals("mobile")) {
	        			vm.pageUrl = "mobile "+arr[arr.length-1];	
	        			vm.fullUrl = obj.get("url").textValue();
	    	    	} else {
	    	    		if(arr[arr.length-1].equals("") || arr[arr.length-1].equals("glivr")) {
	    	    			vm.pageUrl = "Home Page";
	    	    			vm.fullUrl = obj.get("url").textValue();
	    	    		} else {
	    	    			if(arr[arr.length-1].equals("aboutUs")){
	    	    				vm.pageUrl = "About Us";
	    	    			}else if(arr[arr.length-1].equals("blog")){
	    	    				vm.pageUrl = "Blog";
	    	    			}else if(arr[arr.length-1].equals("contactUs")){
	    	    				vm.pageUrl = "Contact Us";
	    	    			}else if(arr[arr.length-1].equals("warranty")){
	    	    				vm.pageUrl = "Warranty";
	    	    			}else{
	    	    				vm.pageUrl = arr[arr.length-1];
	    	    			}
	    	    			
	    	    			vm.fullUrl = obj.get("url").textValue();
	    	    		}
	    	    	}
	        		pagesList.add(vm);
	    		}
    		}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		
    	}
       	
    	
    	for(JsonNode obj : referersNodeList.get(0).get("dates").get(0).get("items")) {
    		PageVM vm = new PageVM();
    		vm.pageUrl = obj.get("title").textValue();
    		vm.count = obj.get("value").textValue();
    		referersList.add(vm);
    	}
    	
    	for(JsonNode obj : searchesNodeList.get(0).get("dates").get(0).get("items")) {
    		PageVM vm = new PageVM();
    		vm.pageUrl = obj.get("title").textValue();
    		vm.count = obj.get("value").textValue();
    		searchesList.add(vm);
    	}
    	
    	Map map = new HashMap(2);
    	map.put("allVisitor", allVisitor);
    	map.put("onlineVisitor", onlineVisitor);
    	map.put("months", months);
    	map.put("onlineVisitors", onlineVisitorsNode.get(0).get("dates").get(0).get("items").get(0).get("value").asInt());
    	map.put("totalVisitors", visitorsNode.get(0).get("dates").get(0).get("items").get(0).get("value").asInt());
    	map.put("actions", visitorsNode.get(1).get("dates").get(0).get("items").get(0).get("value").asInt());
    	map.put("averageActions", visitorsNode.get(2).get("dates").get(0).get("items").get(0).get("value").asInt());
    	map.put("totalTime", visitorsNode.get(3).get("dates").get(0).get("items").get(0).get("value"));
    	map.put("averageTime", visitorsNode.get(4).get("dates").get(0).get("items").get(0).get("value"));
    	map.put("bounceRate", visitorsNode.get(5).get("dates").get(0).get("items").get(0).get("value").asInt());
    	map.put("revenue", visitorsNode.get(7).get("dates").get(0).get("items").get(0).get("value").asInt());
    	map.put("pagesList", pagesList);
    	map.put("referersList", referersList);
    	map.put("searchesList", searchesList);
    	map.put("actionsList", actionsList);
    	map.put("averageActionsList", averageActionsList);
    	return ok(Json.toJson(map));
      }
    
    public static Result getVisitorStats() {
    	String params = "&type=visitors,visitors-new,bounce-rate&date=last-2-days&daily=1";
    	return ok(Json.parse(callClickAPI(params)));
    }
    
   /* public static Result getVisitedDataOther(String type,String filterBy,String search,String searchBy,Long locationId,Integer managerId, String gmInManag) {
    	AuthUser user = AuthUser.findById(managerId);
    	Map result = new HashMap(3);
    	
    	topListings(type,filterBy,search,searchBy,locationId,user,result,"All",gmInManag);
    	return ok(Json.toJson(result));
    }*/
    
    public static Result getVisitedData(Integer userKey,String type,String filterBy,String search,String searchBy,String mainColl,String startDate,String endDate) {
    	
    	Map result = new HashMap(3);
    	AuthUser user = AuthUser.findById(userKey);
    	long locationId = 0;
    	if(user.location == null){
    		locationId = 0L;
    	}else{
    		locationId = user.location.id;
    	}
    	
    	topListings(type,filterBy,search,searchBy,locationId,user,result,mainColl,"0",startDate,endDate);
    	return ok(Json.toJson(result));
    }
    
 
 public static void topListings(String type,String filterBy,String search,String searchBy,Long locationId,AuthUser user,Map result,String mainColl,String gmInManag,String startDate,String endDate){
    	
    	Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
		String start1 = "";
		String end1 = "";
		Date start = null;
		Date end = null;
		Date startDat=null;
		Date enddat=null;
		
		if(type.equals("datewise")) {
			try {
				startDat=df2.parse(startDate);
				enddat=df2.parse(endDate);
				start=startDat;
				end=enddat;
			}catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		if(type.equals("allTime")) {
			cal.add(Calendar.DATE, -1000);
			start1 = df.format(cal.getTime());
			end1 = df.format(date);
			try {
				start = df.parse(start1);
				end = df.parse(end1);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
    	
    	SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MMM-dd");
    	String params = null;
    	String checkDate = null;
    	Date thedate = null;
    	
    	
    	List<ClickyVisitorsList> cList = ClickyVisitorsList.getfindAll();
    	
    	/*-------------------edit click 12-05-2016---------------------------------------*/	
    		
    	List<String> vins = new ArrayList<String>();
    	List<String> vins1 = new ArrayList<String>();
    	
    	Map<String,Integer> pagesCount = new HashMap<String,Integer>();
    	Map<String,Integer> vinUnik = new HashMap<String,Integer>();
    	
    	Map<String,Integer> pagesCount1 = new HashMap<String,Integer>();
    	Map<String,Integer> vinUnik1 = new HashMap<String,Integer>();
    	int i = 1;
    	
    	/*-------------------edit click 12-05-2016---------------------------------------*/
    	
    	for(ClickyVisitorsList item:cList) {
    		String data = item.landingPage;
			String arrVin[] = data.split("/");
			if(arrVin.length > 5){
				if(!arrVin[5].equals("\"")){
					String van[] = arrVin[5].split("\"");
					AddCollection vehicle = null;
					if(user.role.equals("Sales Person") || user.role.equals("Manager") || gmInManag.equals("1")){
						vehicle = AddCollection.findByVinAndStatusForGM(van[0],Location.findById(locationId));
					}else{
						vehicle = AddCollection.findByVinAndStatus(van[0]);
					}
					
					if(vehicle !=null){
						thedate = item.DateClick;
						if (vehicle.addedDate.before(thedate) || vehicle.addedDate.equals(thedate)) {
							if(pagesCount1.get(vehicle.id) !=null){
								int j = pagesCount1.get(vehicle.id);
								pagesCount1.put(vehicle.id.toString(), j+1);
							}else{
								pagesCount1.put(vehicle.id.toString(), 1);
							}
							
						}
					}
				}
				
			}
    	}
    	/*-------------------edit click 12-05-2016---------------------------------------*/
    	
    	List<AddCollection> vlist = null;
    	if(user.role.equals("Sales Person") || user.role.equals("Manager") || gmInManag.equals("1")){
    		vlist = AddCollection.getProductByDraftStatus();
		}else{
			vlist = AddCollection.findByNewlyArrived();
		}
    	
    	if(user.role.equals("Sales Person") || user.role.equals("Manager") || gmInManag.equals("1")){
			List<RequestMoreInfo> rMoreInfo = RequestMoreInfo.findAllSeenSch(user);
			for(RequestMoreInfo rInfo :rMoreInfo){
					vinUnik.put(rInfo.vin, 1);
			}
			for (Map.Entry<String, Integer> entry : vinUnik.entrySet()) {
				vins.add(entry.getKey());
			}
    	}
    	List<AddCollection> topVisited =null;
    	List<AddCollection> topVisitedSold =null;
    	
    	if(mainColl.equals("All")){
    		topVisited = AddCollection.getProductByDraftStatus();
    	}else{
    		topVisited = AddCollection.findByMainCollAndType(vins,InventorySetting.findById(Long.parseLong(mainColl)));
    	}
	
    	List<VehicleAnalyticalVM> topVisitedVms = new ArrayList<>();
    	for(AddCollection vehicle:topVisited) {
    		InventorySetting iSetting = null;
    		if(vehicle.mainCollection != null){
    			iSetting = InventorySetting.getByMainCollection(vehicle.mainCollection.id);
    		}
    		if(iSetting != null){
    			if(!iSetting.equals("deleted")){
        			VehicleAnalyticalVM analyticalVM = new VehicleAnalyticalVM();
            		List<RequestMoreInfo> rInfos = RequestMoreInfo.findByVinAndLocati(vehicle.getId());
            		int req = 0;
            		for(RequestMoreInfo rInfo :rInfos){
        				if((rInfo.requestDate.after(start) && rInfo.requestDate.before(end)) || rInfo.requestDate.equals(end) || rInfo.requestDate.equals(start)){
        					req++;
        				}
        			}
            		analyticalVM.leadsCount = req;
            		
            		if(pagesCount.get(vehicle.getId()) == null){
            			analyticalVM.count = 0;
            		}else{
            			analyticalVM.count = pagesCount.get(vehicle.getId());
            		}
            		analyticalVM.followerCount = 0;
            		
            		AddCollection vehicleImage = AddCollection.getDefaultImg(vehicle.getId());
            		if(vehicleImage!=null) {
            			if(vehicle.fileType != null){
            				if(vehicle.fileType.equals("svg")){
                				analyticalVM.defaultImagePath = vehicleImage.filePath;
                			}else{
                				analyticalVM.id = vehicleImage.getId();
                    			analyticalVM.isImage = true;
                			}
            			}else{
            				analyticalVM.id = vehicleImage.getId();
                			analyticalVM.isImage = true;
            			}
            			
            		}
            		else {
            			analyticalVM.defaultImagePath = "/assets/images/no-image.jpg";
            		}
            		analyticalVM.fileType = vehicle.fileType;
            		analyticalVM.id = vehicle.id;
            		analyticalVM.parentId = vehicle.parentId;
            		analyticalVM.vin = vehicle.getId().toString();
            		analyticalVM.name=vehicle.getTitle();
            		if(!search.equals("0")){
            			if(vehicle.parentId != null){
            				if(vehicle.parentId == Long.parseLong(search)){
                				topVisitedVms.add(analyticalVM);
                			}
            			}
            			
            		}else{
            			topVisitedVms.add(analyticalVM);
            		}
        		}
    		}
    		
			
    		
	}
    	/*List<VehicleAnalyticalVM> worstVisitedVms = new ArrayList<>();
    	List<AddCollection> notVisitedVehicle = AddCollection.getAllProducts();
    	for(AddCollection vehicle:notVisitedVehicle) {
    		VehicleAnalyticalVM analyticalVM = new VehicleAnalyticalVM();
    		List<PriceAlert> pAlert;
    		if(user.role.equals("General Manager")){
    			List<RequestMoreInfo> rInfos = RequestMoreInfo.findByVinStat(vehicle.getId().toString());
        		analyticalVM.leadsCount = rInfos.size();
    		}else{
    			List<RequestMoreInfo> rInfos = RequestMoreInfo.findByVinAndLocati(vehicle.getId());
        		analyticalVM.leadsCount = rInfos.size();
    		}
    		
    		analyticalVM.count = 0;
    		analyticalVM.followerCount = 0;
    		
    		AddCollection vehicleImage = AddCollection.getDefaultImg(vehicle.getId());
    		if(vehicleImage!=null) {
    			analyticalVM.id = vehicleImage.getId();
    			analyticalVM.isImage = true;
    		}
    		else {
    			analyticalVM.defaultImagePath = "/assets/images/no-image.jpg";
    		}
    		analyticalVM.vin = vehicle.getId().toString();
    		analyticalVM.name=vehicle.getTitle();
    		
    		if(!searchBy.equals("0") && !search.equals("0")){
    			if(searchBy.equals("Model")){
    					worstVisitedVms.add(analyticalVM);
    			}else if(searchBy.equals("Make")){
    					worstVisitedVms.add(analyticalVM);
    			}
    		}else{
    			worstVisitedVms.add(analyticalVM);
    		}
    	}  	
*/    	
    	 List<VehicleAnalyticalVM> allVehical = new ArrayList<>();
    	 List<Product> aVehicles =null;
    	 if(mainColl.equals("All")){
    		 aVehicles = Product.getProductByDraftStatus();
    		}else{
    			aVehicles= Product.findByMainCollAndType(vins1,InventorySetting.findById(Long.parseLong(mainColl)));
    		}
    	 
    	for(Product vehicle:aVehicles) {
    		VehicleAnalyticalVM anVm = new VehicleAnalyticalVM();
    		Product vehicleImage = Product.getDefaultImg(vehicle.getId());
    		ProductImages proImg = ProductImages.getDefaultImg(vehicle.getId());
    		if(vehicleImage!=null) {
    			anVm.id = vehicleImage.getId();
    			anVm.price = (int) vehicleImage.getPrice();
    			anVm.isImage = true;
    			if(vehicleImage.getCollection() != null)
    				anVm.collectionId = vehicleImage.getCollection().getId();
    			if(vehicleImage.getMainCollection() != null)
    				anVm.mainCollectionId = vehicleImage.getMainCollection().getId();
    			anVm.vin = vehicleImage.getId().toString();
    			if(proImg != null){
    				anVm.defaultImagePath = proImg.thumbPath;
    			}
    			//anVm.defaultImagePath = proImg.thumbPath;
    			anVm.name = vehicleImage.getPrimaryTitle();
    		}
    		else {
    			anVm.defaultImagePath = "/assets/images/no-image.jpg";
    		}
    		List<RequestMoreInfo> rInfos;
    		List<PriceAlert> pAlert;
    		if(user.role.equals("General Manager")){
    			rInfos = RequestMoreInfo.findByVinStat(vehicle.getId().toString());
    		}else{
    			rInfos = RequestMoreInfo.findByVinAndLocati(vehicle.getId());
    		}
    		int requCount = 0;
    		int schedCount = 0;
    		int tradeCount = 0;
    		for(RequestMoreInfo rInfo : rInfos){
    			if((rInfo.requestDate.after(start) && rInfo.requestDate.before(end)) || rInfo.requestDate.equals(end) || rInfo.requestDate.equals(start)){
    				requCount++;
    			}
    		}
    		
    		anVm.leadsCount = requCount + schedCount + tradeCount;
    		
    		if(pagesCount1.get(vehicle.getId()) !=null){
    			anVm.count =  pagesCount1.get(vehicle.getId());
    		}else{
    			anVm.count = 0;
    		}
    		anVm.followerCount = 0;
    		if(!searchBy.equals("0") && !search.equals("0")){
    			if(searchBy.equals("Model")){
    					allVehical.add(anVm);
    			}else if(searchBy.equals("Make")){
    					allVehical.add(anVm);
    			}
    		}else{
    			allVehical.add(anVm);
    		}
    	}
    	
    	if(filterBy.equals("countLow")){
    		//java.util.Collections.sort(worstVisitedVms,new VehicleVMComparatorCountHigh());
    		java.util.Collections.sort(topVisitedVms,new VehicleVMComparatorCountHigh());
        	java.util.Collections.sort(allVehical,new VehicleVMComparatorCountHigh());
    	}else if(filterBy.equals("countHigh")){
    		//java.util.Collections.sort(worstVisitedVms,new VehicleVMComparatorCountLow());
        	java.util.Collections.sort(topVisitedVms,new VehicleVMComparatorCountLow());
        	java.util.Collections.sort(allVehical,new VehicleVMComparatorCountLow());
    	}
    	
    	if(filterBy.equals("priceHigh")){
    		//java.util.Collections.sort(worstVisitedVms,new VehicleVMComparatorPriceHigh());
        	java.util.Collections.sort(topVisitedVms,new VehicleVMComparatorPriceHigh());
        	java.util.Collections.sort(allVehical,new VehicleVMComparatorPriceHigh());
    	}else if(filterBy.equals("priceLow")){
    		//java.util.Collections.sort(worstVisitedVms,new VehicleVMComparatorPriceLow());
        	java.util.Collections.sort(topVisitedVms,new VehicleVMComparatorPriceLow());
        	java.util.Collections.sort(allVehical,new VehicleVMComparatorPriceLow());
    	}
    	if(filterBy.equals("followerHigh")){
    		//java.util.Collections.sort(worstVisitedVms,new VehicleVMComparatorFollowerHigh());
        	java.util.Collections.sort(topVisitedVms,new VehicleVMComparatorFollowerHigh());
        	java.util.Collections.sort(allVehical,new VehicleVMComparatorFollowerHigh());
    	}else if(filterBy.equals("followerLow")){
    		//java.util.Collections.sort(worstVisitedVms,new VehicleVMComparatorFollowerLow());
        	java.util.Collections.sort(topVisitedVms,new VehicleVMComparatorFollowerLow());
        	java.util.Collections.sort(allVehical,new VehicleVMComparatorFollowerLow());
    	}
    	
    	if(filterBy.equals("leadsHigh")){
    		//java.util.Collections.sort(worstVisitedVms,new VehicleVMComparatorLeadsHigh());
        	java.util.Collections.sort(topVisitedVms,new VehicleVMComparatorLeadsHigh());
        	java.util.Collections.sort(allVehical,new VehicleVMComparatorLeadsHigh());
    	}else if(filterBy.equals("leadsLow")){
    		//java.util.Collections.sort(worstVisitedVms,new VehicleVMComparatorLeadsLow());
        	java.util.Collections.sort(topVisitedVms,new VehicleVMComparatorLeadsLow());
        	java.util.Collections.sort(allVehical,new VehicleVMComparatorLeadsLow());
    	}
    	//result.put("worstVisited", worstVisitedVms);
    	result.put("topVisited", topVisitedVms);
    	result.put("allVehical", allVehical);
    }

    
    public static class VehicleVMComparatorCountHigh implements Comparator<VehicleAnalyticalVM> {
		@Override
		public int compare(VehicleAnalyticalVM o2,VehicleAnalyticalVM o1) {
			return o1.count > o2.count ? -1 : o1.count < o2.count ? 1 : 0;
		}
    }
    
    public static class VehicleVMComparatorPriceHigh implements Comparator<VehicleAnalyticalVM> {
		@Override
		public int compare(VehicleAnalyticalVM o2,VehicleAnalyticalVM o1) {
			return o1.price > o2.price ? -1 : o1.price < o2.price ? 1 : 0;
		}
    }
    
    public static class VehicleVMComparatorFollowerHigh implements Comparator<VehicleAnalyticalVM> {
		@Override
		public int compare(VehicleAnalyticalVM o2,VehicleAnalyticalVM o1) {
			return o1.followerCount > o2.followerCount ? -1 : o1.followerCount < o2.followerCount ? 1 : 0;
		}
    }
    
    public static class VehicleVMComparatorLeadsHigh implements Comparator<VehicleAnalyticalVM> {
		@Override
		public int compare(VehicleAnalyticalVM o2,VehicleAnalyticalVM o1) {
			return o1.leadsCount > o2.leadsCount ? -1 : o1.leadsCount < o2.leadsCount ? 1 : 0;
		}
    }
    
    public static class VehicleVMComparatorCountLow implements Comparator<VehicleAnalyticalVM> {
		@Override
		public int compare(VehicleAnalyticalVM o2,VehicleAnalyticalVM o1) {
			return o1.count < o2.count ? -1 : o1.count > o2.count ? 1 : 0;
		}
    }
    
    public static class VehicleVMComparatorPriceLow implements Comparator<VehicleAnalyticalVM> {
		@Override
		public int compare(VehicleAnalyticalVM o2,VehicleAnalyticalVM o1) {
			return o1.price< o2.price ? -1 : o1.price > o2.price ? 1 : 0;
		}
    }
    
    public static class VehicleVMComparatorFollowerLow implements Comparator<VehicleAnalyticalVM> {
		@Override
		public int compare(VehicleAnalyticalVM o2,VehicleAnalyticalVM o1) {
			return o1.followerCount < o2.followerCount ? -1 : o1.followerCount > o2.followerCount ? 1 : 0;
		}
    }
    
    public static class VehicleVMComparatorLeadsLow implements Comparator<VehicleAnalyticalVM> {
		@Override
		public int compare(VehicleAnalyticalVM o2,VehicleAnalyticalVM o1) {
			return o1.leadsCount < o2.leadsCount ? -1 : o1.leadsCount > o2.leadsCount ? 1 : 0;
		}
    }
    
    public static class VehicleAnalyticalVM {
    	public String name;
    	public String vehicleStatus;
    	public String location;
    	public int count;
    	public Long id;
    	public String vin;
    	public String stockNumber;
    	public String fileType;
    	public Integer price;
    	public Integer followerCount;
    	public Integer leadsCount;
    	public Integer flag = 0;
    	public boolean isImage = false;
    	public String defaultImagePath;
    	public Double proPrice;
    	public Long parentId;
    	public Long collectionId;
    	public Long mainCollectionId;
    }
    
    public static Result getMakes() {
    	Set<String> makes = new HashSet<String>();
    	
    	List<Vehicle> vehicles = Vehicle.findByLocation(Long.valueOf(session("USER_LOCATION")));
    	for(Vehicle vehicle:vehicles) {
    		makes.add(vehicle.getMake());
    	}
    	Map map = new HashMap();
    	map.put("makes", makes);
    	return ok(Json.toJson(map));
    }
    
    
    public static Result getAllParentAndChildCollection() {
			   List<AddCollection> pList = AddCollection.getProductByStatusMain(Long.valueOf(session("USER_LOCATION")),"publish");
			List<AddCollectionVM> aList = new ArrayList<AddCollectionVM>();
			
			for(AddCollection aProduct:pList){
				if(aProduct.mainCollection != null){
					InventorySetting iSetting = InventorySetting.findById(aProduct.mainCollection.id);
					if(iSetting != null){
						if(iSetting.status == null){
							AddCollectionVM aVm = new AddCollectionVM();
							aVm.title = aProduct.title;
							aVm.description = aProduct.description;
							aVm.fileName = aProduct.fileName;
							aVm.id = aProduct.id;
							aVm.publicStatus = aProduct.publicStatus;
							aVm.mainCollection = iSetting;				
					    	List<AddCollectionVM> aCollectionVMs = new ArrayList<AddCollectionVM>();
					    	List<AddCollection> aCollection =  AddCollection.getProductByParentIdStuts(aVm.id,"publish");
					    	for(AddCollection aColl:aCollection){
					    		AddCollectionVM aVmSub = new AddCollectionVM();
					    		aVmSub.title = aColl.title;
					    		aVmSub.description = aColl.description;
					    		aVmSub.fileName = aColl.fileName;
					    		aVmSub.id = aColl.id;
								aVmSub.publicStatus = aColl.publicStatus;
								aCollectionVMs.add(aVmSub);
					    	}
					    	aVm.subCollection = aCollectionVMs;
							aList.add(aVm);
						}
					}
				}
			}
			return ok(Json.toJson(aList));
    }
    
    public static Result getModels(String make) {
    	Set<String> models = new HashSet<String>();
    	List<Vehicle> vehicles = Vehicle.getVehiclesByMake(make, Location.findById(Long.valueOf(session("USER_LOCATION"))));
    	for(Vehicle vehicle:vehicles) {
    		models.add(vehicle.getModel());
    	}
    	return ok(Json.toJson(models));
    }
    
    public static Result checkStockNumber(String stockNumber) {
    	List<Vehicle> vehicles = Vehicle.findByStock(stockNumber,Location.findById(Long.valueOf(session("USER_LOCATION"))));
    	if(vehicles.size() > 0){
        	return ok(Json.toJson(vehicles.size()));
    	}else{
        	return ok(Json.toJson(vehicles.size()));
    	}
    }
    
    public static Result getStockDetails(String productName) {
    	List<Inventory> product = Inventory.findByProductTitle(productName,Location.findById(Long.valueOf(session("USER_LOCATION"))));
    	Map map = new HashMap();
    	if(product.size()>0) {
    		map.put("isData", true);
    		map.put("title", product.get(0).getTitle());
    		map.put("cost", product.get(0).getCost());
    		map.put("price", product.get(0).getPrice());
    		map.put("collectionId", product.get(0).getCollection().getId());
    		AddCollection addCollection = AddCollection.findById(product.get(0).getCollection().getId());
    		map.put("collectionName", addCollection.getTitle());
    		map.put("productId", product.get(0).getProductId());
    		map.put("id", product.get(0).getId());
    		InventoryImage productImage = InventoryImage.getDefaultImage(product.get(0).getProductId());
    		if(productImage!=null) {
    			map.put("imgId",productImage.getId());
    		}
    		else {
    			map.put("imgId","/assets/images/no-image.jpg");
    		}
    		
    		
    	} else {
    		map.put("isData", false);
    	}
    	return ok(Json.toJson(map));
    	
    }
    
    
    
    /*
    
    for(Vehicle vehicle:topVisited) {
		
		VehicleAnalyticalVM analyticalVM = new VehicleAnalyticalVM();
		List<RequestMoreInfo> rInfos = RequestMoreInfo.findByVinAndLocation(vehicle.getVin(), Location.findById(Long.valueOf(session("USER_LOCATION"))));
		List<ScheduleTest> sList = ScheduleTest.findByVinAndLocation(vehicle.getVin(), Location.findById(Long.valueOf(session("USER_LOCATION"))));
		List<TradeIn> tIns = TradeIn.findByVinAndLocation(vehicle.getVin(), Location.findById(Long.valueOf(session("USER_LOCATION"))));
		
		analyticalVM.leadsCount = rInfos.size() + sList.size() + tIns.size();
		
		if(pagesCount.get(vehicle.getVin()) == null){
			analyticalVM.count = 0;
		}else{
			analyticalVM.count = pagesCount.get(vehicle.getVin());
		}
		
		List<PriceAlert> pAlert = PriceAlert.getEmailsByVin(vehicle.getVin(), Long.valueOf(session("USER_LOCATION")));
		if(pAlert != null){
			analyticalVM.followerCount =  pAlert.size();
		}else{
			analyticalVM.followerCount = 0;
		}
		analyticalVM.price = vehicle.getPrice();
		VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle.getVin());
		if(vehicleImage!=null) {
			analyticalVM.id = vehicleImage.getId();
			analyticalVM.isImage = true;
		}
		else {
			analyticalVM.defaultImagePath = "/assets/images/no-image.jpg";
		}
		analyticalVM.vin = vehicle.getVin();
		analyticalVM.name = vehicle.getMake() + " "+ vehicle.getModel()+ " "+ vehicle.getYear();
		
		if(!searchBy.equals("0") && !search.equals("0")){
    		if(searchBy.equals("Model")){
				if(vehicle.model.toUpperCase().startsWith(search.toUpperCase())){
					topVisitedVms.add(analyticalVM);
				}
			}else if(searchBy.equals("Make")){
				if(vehicle.make.toUpperCase().startsWith(search.toUpperCase())){
					topVisitedVms.add(analyticalVM);
				}
			}
		}else{
			topVisitedVms.add(analyticalVM);
		}
}*/
    
    public static Result getDateRangSalePerson(String startDate,String endDate){
    	
    	DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
    	
    	Map<String, Integer> mapSalePerson = new HashMap<String, Integer>();
    	
    	Date startD = null;
 		try {
 			startD = df1.parse(startDate);
 		} catch (ParseException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
     	Date endD = null;
 		try {
 			endD = df1.parse(endDate);
 		} catch (ParseException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
 		
 		int pricecount = 0;
 		int maxPrice = 0;
 		int salePersonId = 0;
 		
 		List<AuthUser> aUser = AuthUser.getAllUserByLocation(Location.findById(Long.valueOf(session("USER_LOCATION"))));
    	
 		for(AuthUser authUser:aUser){
 			pricecount = 0;
 			List<Vehicle> vList = Vehicle.findBySoldUserAndSold(authUser);
 			for (Vehicle vehicle : vList) {
 				if((vehicle.soldDate.after(startD) && vehicle.soldDate.before(endD)) || vehicle.soldDate.equals(startD) || vehicle.soldDate.equals(endD)) {
 	    			pricecount = pricecount + vehicle.price;
 				}
 			}
 			if(pricecount > maxPrice){
 				maxPrice = pricecount;
 				salePersonId = authUser.id;
 			}
 			
 		}
    	
		return ok(Json.toJson(salePersonId));
    }
    
 
    public static Result getComperSalePersonData(Integer id,String startDate,String endDate){
    
    	DateFormat onlyMonth = new SimpleDateFormat("MMMM");
     	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
     	DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
     	Date dateobj = new Date();
     	Calendar cal1 = Calendar.getInstance();
     	//AuthUser users = (AuthUser) getLocalUser();
     	AuthUser users;
     	users = AuthUser.findById(id);
     	if(users == null){
     		users = (AuthUser) getLocalUser();
     	}
     	
     	String[] monthName = { "January", "February", "March", "April", "May", "June", "July",
 		        "August", "September", "October", "November", "December" };
     	
     	Calendar cal = Calendar.getInstance();  
      	String monthCal = monthName[cal.get(Calendar.MONTH)];
     	
     	Map<String, Integer> mapByType = new HashMap<String, Integer>();
     	
     	Date startD = null;
 		try {
 			startD = df1.parse(startDate);
 		} catch (ParseException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
     	Date endD = null;
 		try {
 			endD = df1.parse(endDate);
 		} catch (ParseException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
     	
     	int requestLeadCount = 0;
     	int scheduleLeadCount = 0;
     	int tradeInLeadCount = 0;
     	
     	int cancelRequestLeadCount = 0;
     	int cancelScheduleLeadCount = 0;
     	int cancelTradeInLeadCount = 0;
     	
     	int lostRequestLeadCount = 0;
     	int lostScheduleLeadCount = 0;
     	int lostTradeInLeadCount = 0;
     	
     	int requestLeadCount1 = 0;
     	int scheduleLeadCount1 = 0;
     	int tradeInLeadCount1 = 0;
     	
     	List<RequestMoreInfo> rInfo = null;
     	List<ScheduleTest> sList = null;
     	List<TradeIn> tradeIns = null;
     	List<RequestMoreInfo> rInfoAll = null;
     	List<ScheduleTest> sListAll = null;
     	List<TradeIn> tradeInsAll = null;
    	List<RequestMoreInfo> lostRInfo = null;
     	List<ScheduleTest> lostSList = null;
     	List<TradeIn> lostTradeIns = null;
     	
     	List<RequestMoreInfo> reCancelInfo = null;
     	List<ScheduleTest> scCancelInfo = null;
     	List<TradeIn> trCancelInfo = null;
     	
	     	lostRInfo = RequestMoreInfo.findAllLostSch(users);
	     	lostSList = ScheduleTest.findAllLostSch(users);
	     	lostTradeIns = TradeIn.findAllLostSch(users);
     	
     		rInfo = RequestMoreInfo.findAllSeenSch(users);
     		sList = ScheduleTest.findAllAssigned(users);
     		tradeIns = TradeIn.findAllSeenSch(users);
     		
     		rInfoAll = RequestMoreInfo.findAllByNotOpenLead(users);
     		sListAll = ScheduleTest.findAllByNotOpenLead(users);
     		tradeInsAll = TradeIn.findAllByNotOpenLead(users);
     		
     		Long difffoll = 0L;
     		Long countFollo = 0L; 
     		SimpleDateFormat convdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     		SimpleDateFormat convdTime = new SimpleDateFormat("HH:mm:ss");
     	for(RequestMoreInfo rMoreInfo:rInfo){
     		if((rMoreInfo.requestDate.after(startD) && rMoreInfo.requestDate.before(endD)) || rMoreInfo.requestDate.equals(endD) || rMoreInfo.requestDate.equals(startD)){
     			requestLeadCount++;
     		}
     	}
     	
     	for(ScheduleTest sTest:sList){
     		if((sTest.scheduleDate.after(startD) && sTest.scheduleDate.before(endD)) || sTest.scheduleDate.equals(endD) || sTest.scheduleDate.equals(startD)){
     			scheduleLeadCount++;
     		}
     	}

     	for(TradeIn tIn:tradeIns){
     		if((tIn.tradeDate.after(startD) && tIn.tradeDate.before(endD)) || tIn.tradeDate.equals(endD) || tIn.tradeDate.equals(startD)){
     				tradeInLeadCount++;
     		}
     	}
     	int folloLead = 0;
     	for(RequestMoreInfo rMoreInfo:rInfoAll){
     		if((rMoreInfo.requestDate.after(startD) && rMoreInfo.requestDate.before(endD)) || rMoreInfo.requestDate.equals(endD) || rMoreInfo.requestDate.equals(startD)){
     			requestLeadCount1++;
     			int twoTimes = 0;
     			difffoll = 0L;
     			List<UserNotes> uNotes = UserNotes.findRequestMoreAndFirstAdd(rMoreInfo);
     			for(UserNotes uN:uNotes){
     				if(twoTimes < 2){
     					twoTimes++;
	     				if(!uN.note.equals("Lead has been created")){
	     					folloLead++;
	         				String CretaeDateTime = df1.format(uN.createdDate)+" "+convdTime.format(uN.createdTime);
	         				
	         				Date cDate = null;
	         				try {
	    						cDate = convdf.parse(CretaeDateTime);
	    					} catch (ParseException e) {
	    						// TODO Auto-generated catch block
	    						e.printStackTrace();
	    					}
	         				
	         				difffoll = cDate.getTime() - rMoreInfo.requestTime.getTime();
	         				break;
	     				}
     				}
     			}
     			countFollo = countFollo + difffoll;
     		}
     	}
     	
     	for(ScheduleTest sTest:sListAll){
     	if((sTest.scheduleDate.after(startD) && sTest.scheduleDate.before(endD)) || sTest.scheduleDate.equals(endD) || sTest.scheduleDate.equals(startD)){
     			scheduleLeadCount1++;
     			int twoTimes = 0;
     			difffoll = 0L;
     			List<UserNotes> uNotes = UserNotes.findScheduleTestAndFirstAdd(sTest);
     			for(UserNotes uN:uNotes){
     				if(twoTimes < 2){
     					twoTimes++;
	     				if(!uN.note.equals("Lead has been created")){
	     					folloLead++;
	         				String CretaeDateTime = df1.format(uN.createdDate)+" "+convdTime.format(uN.createdTime);
	         				
	         				Date cDate = null;
	         				try {
	    						cDate = convdf.parse(CretaeDateTime);
	    					} catch (ParseException e) {
	    						// TODO Auto-generated catch block
	    						e.printStackTrace();
	    					}
	         				
	         				difffoll = cDate.getTime() - sTest.scheduleTime.getTime();
	         				//difffoll = (difffoll / 1000 /60 /60 /24);
	         				break;
	     				}
     				}
     			}
     			countFollo = countFollo + difffoll;
     	}
     	}

     	for(TradeIn tIn:tradeInsAll){
     	if((tIn.tradeDate.after(startD) && tIn.tradeDate.before(endD)) || tIn.tradeDate.equals(endD) || tIn.tradeDate.equals(startD)){
 				tradeInLeadCount1++;
 				int twoTimes = 0;
 				difffoll = 0L;
     			List<UserNotes> uNotes = UserNotes.findTradeInAndFirstAdd(tIn);
     			for(UserNotes uN:uNotes){
     				if(twoTimes < 2){
	     				if(!uN.note.equals("Lead has been created")){
	     					folloLead++;
	         				
	         				String CretaeDateTime = df1.format(uN.createdDate)+" "+convdTime.format(uN.createdTime);
	         				
	         				Date cDate = null;
	         				try {
	    						cDate = convdf.parse(CretaeDateTime);
	    					} catch (ParseException e) {
	    						// TODO Auto-generated catch block
	    						e.printStackTrace();
	    					}
	         				
	         				difffoll = cDate.getTime() - tIn.tradeTime.getTime();
	         				//difffoll = (difffoll / 1000 /60 /60 /24);
	         				break;
	     				}
     				}
     			}
     			countFollo = countFollo + difffoll;
 				
     		}
     	}
     	
    	for(RequestMoreInfo rMoreInfo:lostRInfo){
     		if((rMoreInfo.requestDate.after(startD) && rMoreInfo.requestDate.before(endD)) || rMoreInfo.requestDate.equals(endD) || rMoreInfo.requestDate.equals(startD)){
     			lostRequestLeadCount++;
     		}
     	}
     	
     	for(ScheduleTest sTest:lostSList){
     		if((sTest.scheduleDate.after(startD) && sTest.scheduleDate.before(endD)) || sTest.scheduleDate.equals(endD) || sTest.scheduleDate.equals(startD)){
     			lostScheduleLeadCount++;
     		}
     	}

     	for(TradeIn tIn:lostTradeIns){
     		if((tIn.tradeDate.after(startD) && tIn.tradeDate.before(endD)) || tIn.tradeDate.equals(endD) || tIn.tradeDate.equals(startD)){
     				lostTradeInLeadCount++;
     		}
     	}	
 		
     	
     	
     	int countLeads1 = requestLeadCount1 + scheduleLeadCount1 + tradeInLeadCount1;
     	int countLeads = requestLeadCount + scheduleLeadCount + tradeInLeadCount;
    	int countLostLeads = lostRequestLeadCount + lostScheduleLeadCount + lostTradeInLeadCount;
     	
    	
    	LocationWiseDataVM lDataVM = new LocationWiseDataVM();
     	
     	if(users !=null){
     	if(users.imageUrl != null) {
			if(users.imageName !=null){
				lDataVM.imageUrl = "http://glider-autos.com/MavenImg/images"+users.imageUrl;
			}else{
				lDataVM.imageUrl = users.imageUrl;
			}
			
		} 
     	}
     	else {
			lDataVM.imageUrl = "/profile-pic.jpg";
		}
     	
     	if(countFollo != 0 && folloLead != 0){
     		Long followUpTime = countFollo / folloLead;
	     	
	     	Long seconds = (followUpTime / 1000);
	     	Long minutes = (followUpTime / (1000 * 60));
	     	Long hours = (minutes / 60);
	     	Long displayMin = minutes - (hours * 60);
	     	
	     	String hrs=hours.toString();
	     	String mnt=displayMin.toString();
	     	if(hours.toString().length()<=1){
	     		hrs="0"+hours.toString();
	     	}
	     
	     	if(displayMin.toString().length()<=1){
	     		mnt="0"+displayMin.toString();
	     	}
	     	
	     	lDataVM.followUpTime =hrs+":"+mnt+" Hrs";
     		 /*Long followUpTime= (long)((double)countFollo / (double) countLeads) * 24;
     		lDataVM.followUpTime=followUpTime.toString()+":00 Hrs";*/
     	}else{
     		lDataVM.followUpTime = "00:00  Hrs";
     	}
     	
     	lDataVM.countSalePerson = countLeads;
     	lDataVM.lostLeadCount = countLostLeads;
     	lDataVM.id = users.id;
     	Integer monthPriceCount = 0;
     	Integer pricecount = 0;
     	int saleCarCount = 0;
     	
     	Map<String, Integer> priceRang = new HashMap<String, Integer>();
     	
     	addPriceRang(priceRang);
     	
     	//List<Vehicle> totalVehicleVM = Vehicle.findByLocation(Long.valueOf(session("USER_LOCATION")));
     	List<Vehicle> totalVehicleVM = Vehicle.getAllVehicles();
     	for(Vehicle vm:totalVehicleVM ){
     		Integer objectMake = mapByType.get(vm.getBodyStyle());
			if (objectMake == null) {
				mapByType.put(vm.getBodyStyle(), 0);
			}
          }
     	
     	
     	lDataVM.SalePersonName = users.firstName +" "+users.lastName; 
     		
     		List<RequestMoreInfo> rInfo1 = RequestMoreInfo.findAllSeenComplete(users);
     		List<ScheduleTest> sList1 = ScheduleTest.findAllSeenComplete(users);
     		List<TradeIn> tradeIns1 = TradeIn.findAllSeenComplete(users);
     		
     		Integer countBodyStyle = 1;
     		
     		List<Vehicle> vList = Vehicle.findBySoldUserAndSold(users);
    		for (Vehicle vehicle : vList) {
    			if((vehicle.soldDate.after(startD) && vehicle.soldDate.before(endD)) || vehicle.soldDate.equals(startD) || vehicle.soldDate.equals(endD)) {
        			saleCarCount++;
        			pricecount = pricecount + vehicle.price;
        			fillPriceRang(priceRang, vehicle.price);
        			
        			if(vehicle.getBodyStyle() != null){
     					
     					Integer objectMake = mapByType.get(vehicle.getBodyStyle());
     					if (objectMake == null) {
     						mapByType.put(vehicle.getBodyStyle(), countBodyStyle);
     					}else{
     						mapByType.put(vehicle.getBodyStyle(), mapByType.get(vehicle.getBodyStyle()) + 1);
     					}
     				}
				}
				if(monthCal.equals(onlyMonth.format(vehicle.soldDate))){
					monthPriceCount = monthPriceCount + vehicle.price;
				}
			}
   
     		
    		double sucessCount = 0;
    		if(countLeads1 != 0){
    			sucessCount= (double)saleCarCount/(double)countLeads1*100;
    		}else{
    			 sucessCount = 0;
    		}
     		lDataVM.successRate = (int) sucessCount;
     		
     	
     	lDataVM.totalSalePrice =  pricecount;
     	lDataVM.totalsaleCar = saleCarCount;
     	
     	double valAvlPrice= ((double)pricecount/(double)saleCarCount);
     	lDataVM.angSalePrice = (int) valAvlPrice;
     	
     	
     	reCancelInfo=RequestMoreInfo.findAllCancelLeadByUser(users);
  	   scCancelInfo=ScheduleTest.findAllCancelLeadsForUser(users);
  		trCancelInfo=TradeIn.findAllCancelLeadsForUser(users);
  		
  		
  		for(RequestMoreInfo rMoreInfo:reCancelInfo){
      		if((rMoreInfo.requestDate.after(startD) && rMoreInfo.requestDate.before(endD)) || rMoreInfo.requestDate.equals(endD) || rMoreInfo.requestDate.equals(startD)){
      			cancelRequestLeadCount++;
      		}
      	}
      	
      	for(ScheduleTest sTest:scCancelInfo){
      		if((sTest.scheduleDate.after(startD) && sTest.scheduleDate.before(endD)) || sTest.scheduleDate.equals(endD) || sTest.scheduleDate.equals(startD)){
      			cancelScheduleLeadCount++;
      		}
      	}

      	for(TradeIn tIn:trCancelInfo){
      		if((tIn.tradeDate.after(startD) && tIn.tradeDate.before(endD)) || tIn.tradeDate.equals(endD) || tIn.tradeDate.equals(startD)){
      			cancelTradeInLeadCount++;
      		}
      	}
  		
     	lDataVM.cancelLeadCount=cancelRequestLeadCount + cancelScheduleLeadCount + cancelTradeInLeadCount;
     	
     	
     	
     	
     	
     	
     	
     	List<bodyStyleSetVM> bSetVMs = new ArrayList<>();
     	
     	for (Entry<String , Integer> entryValue : mapByType.entrySet()) {
     		bodyStyleSetVM value = new bodyStyleSetVM();
			value.name = entryValue.getKey();
			value.value = entryValue.getValue();
			bSetVMs.add(value);
		}
     	lDataVM.byType = bSetVMs;
     	
     	
     	List<bodyStyleSetVM> bSetVMPriceRang = new ArrayList<>();
     	
     	for (Entry<String , Integer> entryValue : priceRang.entrySet()) {
     		bodyStyleSetVM value = new bodyStyleSetVM();
			value.name = entryValue.getKey();
			value.value = entryValue.getValue();
			bSetVMPriceRang.add(value);
		}
     	lDataVM.priceRang = bSetVMPriceRang;
     	
     	List<bodyStyleSetVM> bSetVMsPlan = new ArrayList<>();
      	List<PlanScheduleMonthlySalepeople> pMonthlySalepeople = PlanScheduleMonthlySalepeople.findByListUser(users);
      	
      	java.util.Date date= new Date();
      	Calendar cal11 = Calendar.getInstance();
      	cal.setTime(date);
      	int monthValue = cal.get(Calendar.MONTH);
      	 String[] changVls = { "January", "February", "March", "April", "May", "June", "July",
  		        "August", "September", "October", "November", "December" };
     	for(int i=0;i<monthValue+1;i++){
     		bodyStyleSetVM nSetVM1 = new bodyStyleSetVM();
     		nSetVM1.name = changVls[i];
     		nSetVM1.value=0;
     		bSetVMsPlan.add(nSetVM1);
     		
     	}
      	 
      	
        for(PlanScheduleMonthlySalepeople pSalepeople: pMonthlySalepeople){
        	//bodyStyleSetVM nSetVM = new bodyStyleSetVM();
        	Date nowDate = new Date();
        	for(bodyStyleSetVM nSetVM:bSetVMsPlan){
        		
        	
        	Calendar calnow = Calendar.getInstance();
        	calnow.setTime(nowDate);
           
            int month = calnow.get(Calendar.MONTH);
            String changVls1 = WordUtils.capitalize(pSalepeople.month);
        	//for(int i=0;i<12;i++){
        		
        		
        		if(changVls1.equals(nSetVM.name)){
        		//	if(i <= month){
        				nSetVM.name = changVls1;
        	        	double val= ((double)pricecount/Double.parseDouble(pSalepeople.totalBrought));
        	        	nSetVM.value = (int) (val*100);
        	        	
        	        	//bSetVMsPlan.add(nSetVM);
        			//}
        		//}
        	}
        		
        }
       }  
        lDataVM.planComplete = bSetVMsPlan;
        
        List<RequestMoreInfo> offlineRInfo = RequestMoreInfo.findAllAssignedOffine(users);
        List<ScheduleTest> offlineSList = ScheduleTest.findAllAssignedOffine(users);
        List<TradeIn> offlineTradeIns = TradeIn.findAllAssignedOffine(users);
        
        Map<String, Integer> mapOffline = new HashMap<String, Integer>();
        
        
        
        mapOffline.put("Phone", 0);
        mapOffline.put("Walk In", 0);
        mapOffline.put("Email", 0);
        mapOffline.put("Facebook", 0);
        mapOffline.put("Online", 0);
    	for(RequestMoreInfo rMoreInfo:offlineRInfo){
     		if((rMoreInfo.requestDate.after(startD) && rMoreInfo.requestDate.before(endD)) || rMoreInfo.requestDate.equals(endD) || rMoreInfo.requestDate.equals(startD)){
     			if(rMoreInfo.contactedFrom != null){
     				Integer objectMake = mapOffline.get(rMoreInfo.contactedFrom);
        			if (objectMake == null) {
        				mapOffline.put(rMoreInfo.contactedFrom, countBodyStyle);
        			}else{
        				mapOffline.put(rMoreInfo.contactedFrom, countBodyStyle + 1);
        			}
     			}
     			
     		}
     	}
     	
     	for(ScheduleTest sTest:offlineSList){
     		if((sTest.scheduleDate.after(startD) && sTest.scheduleDate.before(endD)) || sTest.scheduleDate.equals(endD) || sTest.scheduleDate.equals(startD)){
    			if(sTest.contactedFrom != null){
    				Integer objectMake = mapOffline.get(sTest.contactedFrom);
    				if (objectMake == null) {
        				mapOffline.put(sTest.contactedFrom, countBodyStyle);
        			}else{
        				mapOffline.put(sTest.contactedFrom, countBodyStyle + 1);
        			}
    			}
     		}
     	}

     	for(TradeIn tIn:offlineTradeIns){
     		if((tIn.tradeDate.after(startD) && tIn.tradeDate.before(endD)) || tIn.tradeDate.equals(endD) || tIn.tradeDate.equals(startD)){
     			if(tIn.contactedFrom != null){
     				Integer objectMake = mapOffline.get(tIn.contactedFrom);
        			if (objectMake == null) {
        				mapOffline.put(tIn.contactedFrom, countBodyStyle);
        			}else{
        				mapOffline.put(tIn.contactedFrom, countBodyStyle + 1);
        			}
     			}
     		}
     	}
     	List<bodyStyleSetVM> bSetVMsoffline = new ArrayList<>();
     	for (Entry<String , Integer> entryValue : mapOffline.entrySet()) {
     		bodyStyleSetVM value = new bodyStyleSetVM();
			value.name = entryValue.getKey();
			value.value = entryValue.getValue();
			bSetVMsoffline.add(value);
		}
     	lDataVM.offlineLead = bSetVMsoffline;
     	
     	
     	List<RequestMoreInfo> onlineRInfo = RequestMoreInfo.findAllAssignedOnline(users);
        List<ScheduleTest> onlineSList = ScheduleTest.findAllAssignedOnline(users);
        List<TradeIn> onlineTradeIns = TradeIn.findAllAssignedOnline(users);
        
        Map<String, Integer> mapOnline = new HashMap<String, Integer>();
        
        mapOnline.put("Request More Info", 0);
    	for(RequestMoreInfo rMoreInfo:onlineRInfo){
     		if((rMoreInfo.requestDate.after(startD) && rMoreInfo.requestDate.before(endD)) || rMoreInfo.requestDate.equals(endD) || rMoreInfo.requestDate.equals(startD)){
     			Integer objectMake = mapOnline.get("Request More Info");
    			if (objectMake != null) {
    				
    				mapOnline.put("Request More Info", countBodyStyle + 1);
    			}
     		}
     	}
     	
    	mapOnline.put("Schedule Test Drive", 0);
     	for(ScheduleTest sTest:onlineSList){
     		if((sTest.scheduleDate.after(startD) && sTest.scheduleDate.before(endD)) || sTest.scheduleDate.equals(endD) || sTest.scheduleDate.equals(startD)){
     			Integer objectMake = mapOnline.get("Schedule Test Drive");
    			if (objectMake != null) {
    				mapOnline.put("Schedule Test Drive", countBodyStyle + 1);
    			}
     		}
     	}

     	mapOnline.put("Trade-In Inquires", 0);
     	for(TradeIn tIn:onlineTradeIns){
     		if((tIn.tradeDate.after(startD) && tIn.tradeDate.before(endD)) || tIn.tradeDate.equals(endD) || tIn.tradeDate.equals(startD)){
     			Integer objectMake = mapOnline.get("Trade-In Inquires");
    			if (objectMake != null) {
    				mapOnline.put("Trade-In Inquires", countBodyStyle + 1);
    			}
     		}
     	}
     	List<bodyStyleSetVM> bSetVMsonline = new ArrayList<>();
     	for (Entry<String , Integer> entryValue : mapOnline.entrySet()) {
     		bodyStyleSetVM value = new bodyStyleSetVM();
			value.name = entryValue.getKey();
			value.value = entryValue.getValue();
			bSetVMsonline.add(value);
		}
     	lDataVM.onLineLead = bSetVMsonline;
     	
     	int requestGLeadCount = 0;
     	int scheduleGLeadCount = 0;
     	int tradeInGLeadCount = 0;
     	
     	List<RequestMoreInfo> rInfoAllG = RequestMoreInfo.findAllByAssignedUser(users);
     	List<ScheduleTest>  sListAllG = ScheduleTest.findAllByAssignedUser(users);
     	List<TradeIn> tradeInsAllG = TradeIn.findAllByAssignedUser(users);
 		
     	Long totalLeadDay = 0L;
 		
 	for(RequestMoreInfo rMoreInfo:rInfoAllG){
 		if((rMoreInfo.requestDate.after(startD) && rMoreInfo.requestDate.before(endD)) || rMoreInfo.requestDate.equals(endD) || rMoreInfo.requestDate.equals(startD)){
 			
 			 			
 			Long diff = 0L;
				if(rMoreInfo.statusTime != null){
					diff = rMoreInfo.statusTime.getTime() - rMoreInfo.requestTime.getTime();
					
					//diff = (diff / 1000 /60 /60 /24);
				}else{
					Date nowDate  =new Date();
 					/*String datef = df.format(nowDate);
 					Date  ndate = null;
 					
 					try {
						ndate = df.parse(datef);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
 					
 					diff = nowDate.getTime() - rMoreInfo.requestTime.getTime();
 					//diff = (diff / 1000 /60 /60 /24);
				}
				
				totalLeadDay = totalLeadDay + diff;
				
		
 			requestGLeadCount++;
 		}
 	}
 	
 	for(ScheduleTest sTest:sListAllG){
 		if((sTest.scheduleDate.after(startD) && sTest.scheduleDate.before(endD)) || sTest.scheduleDate.equals(endD) || sTest.scheduleDate.equals(startD)){
 			
 			Long diff = 0L;
			if(sTest.statusTime != null){
				diff = sTest.statusTime.getTime() - sTest.scheduleTime.getTime();
				
			//	diff = (diff / 1000 /60 /60 /24);
			}else{
				Date nowDate  =new Date();
					/*String datef = df.format(nowDate);
					Date  ndate = null;
					
					try {
					ndate = df.parse(datef);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
					
					diff = nowDate.getTime() - sTest.scheduleTime.getTime();
					//diff = (diff / 1000 /60 /60 /24);
			}
			
			totalLeadDay = totalLeadDay + diff;
			
 			scheduleGLeadCount++;
 		}
 	}

 	for(TradeIn tIn:tradeInsAllG){
 		if((tIn.tradeDate.after(startD) && tIn.tradeDate.before(endD)) || tIn.tradeDate.equals(endD) || tIn.tradeDate.equals(startD)){
 			
 			Long diff = 0L;
			if(tIn.statusTime != null){
				diff = tIn.statusTime.getTime() - tIn.tradeTime.getTime();
				
			//	diff = (diff / 1000 /60 /60 /24);
			}else{
				Date nowDate  =new Date();
					/*String datef = df.format(nowDate);
					Date  ndate = null;
					
					try {
					ndate = df.parse(datef);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
					
					diff = nowDate.getTime() - tIn.tradeTime.getTime();
				//	diff = (diff / 1000 /60 /60 /24);
			}
			totalLeadDay = totalLeadDay + diff;
 				tradeInGLeadCount++;
 		}
 	}
 	
 	int AllGeneratedLead = requestGLeadCount + scheduleGLeadCount + tradeInGLeadCount;
     	lDataVM.allGeneratedLeadCount = AllGeneratedLead;
     	
     	
     	if(totalLeadDay  != 0 || AllGeneratedLead != 0){
	     	Long AvgLeadLifeCyc = totalLeadDay / AllGeneratedLead;
	     	
	     	Long seconds = (AvgLeadLifeCyc / 1000);
	     	Long minutes = (AvgLeadLifeCyc / (1000 * 60));
	     	Long hours = (minutes / 60);
	     	Long displayMin = minutes - (hours * 60);
	     	Long min1 = minutes % 60;
	     	//Long days= (hours/24);
	     	Long hrs=(hours%24);
/*	     	float min=hours%24;
	     	int mnts=(int) (min-hrs);*/
	        //String mnts =min1.toString();
	        String mnts =displayMin.toString();
	        
	        //String days1 =days.toString();
	        //String hrs1=hrs.toString();
	        String hrs1=hours.toString();
	     	if(min1.toString().length()<=1)
	     	{
	     		mnts="0"+mnts;
	     	}
	     	
	     	/*if(days.toString().length()<=1)
	     	{
	     		days1="0"+ days1;
	     	}*/
	     	
	     	if(hrs.toString().length()<=1)
	     	{
	     		hrs1="0"+hrs1;
	     	}
	     	
	     	//lDataVM.avgLeadLifeCycle =hours.toString()+":"+displayMin.toString()+"Hrs";
	     	lDataVM.avgLeadLifeCycle=hrs1+":"+mnts+"  Hrs";
     	}else{
     		lDataVM.avgLeadLifeCycle = "00:00:00  Hrs";
     	}
     	
     	List<UserNotes> uNotes = UserNotes.findByUserAndcall(users);
     	int callActionCount =  0;
     	for(UserNotes sNot:uNotes){
     		if((sNot.createdDate.after(startD) && sNot.createdDate.before(endD)) || sNot.createdDate.equals(endD) || sNot.createdDate.equals(startD)){
     			callActionCount++;
     		}
     	}
     	lDataVM.callMade = callActionCount;
     	
     	List<UserNotes> uNotesEmail = UserNotes.findByUserAndemail(users);
     	int mailActionCount =  0;
     	for(UserNotes sNot:uNotesEmail){
     		if((sNot.createdDate.after(startD) && sNot.createdDate.before(endD)) || sNot.createdDate.equals(endD) || sNot.createdDate.equals(startD)){
     			mailActionCount++;
     		}
     	}
     	lDataVM.mailSent = callActionCount;
     	
     	List<UserNotes> uNotesTest = UserNotes.findByUserAndSched(users);
     	int testDriveActionCount =  0;
     	for(UserNotes sNot:uNotesTest){
     		if((sNot.createdDate.after(startD) && sNot.createdDate.before(endD)) || sNot.createdDate.equals(endD) || sNot.createdDate.equals(startD)){
     			testDriveActionCount++;
     		}
     	}
     	lDataVM.testDriveSched = callActionCount;
     	
     	
     	List<Comments> comments = Comments.getByListUser(users);
     	int likeCount = 0;
     	
     	for(Comments comm:comments){
     		if((comm.likeDate.after(startD) && comm.likeDate.before(endD)) || comm.likeDate.equals(endD) || comm.likeDate.equals(startD)){
     			likeCount++;
     		}
     	}
     	
     	lDataVM.likeCount = likeCount;
     	
     	
     	
     	Map<String, Integer> returnIng = new HashMap<String, Integer>();
      List<Contacts> cList = Contacts.findByUser(users.id);
      
      for(Contacts contacts:cList){
    	  Integer objectMake = returnIng.get(contacts.email);
  		if (objectMake == null) {
  			returnIng.put(contacts.email, countBodyStyle);
  		}else{
  			returnIng.put(contacts.email, countBodyStyle + 1);
  		}
      }
      
      int returningCount = 0;
 	for (Entry<String , Integer> entryValue : returnIng.entrySet()) {
 		if(entryValue.getValue() >= 2){
 			returningCount++;
 		}
	}
 	lDataVM.returningClints = returningCount;
 	
 	int parDataSalary = Integer.parseInt(users.salary) / 30;
 	int dayCount = 1;
 	int value1 = 2;
 	Date dts = startD;
 	if(!startD.equals(endD)){
 	while(value1 > 1){
				Calendar c = Calendar.getInstance(); 
				c.setTime(dts); 
				c.add(Calendar.DATE, 1);
				dts = c.getTime();
				dayCount++;
				String nextDate = df.format(dts);
				Date  dfnext = null;
				try {
				dfnext = df.parse(nextDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
				
				if(dfnext.equals(endD)){
					value1 = 0;
					break;
				}
 	}
 	}
      
 	lDataVM.salary = (parDataSalary * dayCount);
 	lDataVM.training = users.trainingPro;
 	int tcl = 0;
 	if(users.trainingCost != null){
 		if(!users.trainingCost.equals("null")){
 			lDataVM.trainingCost = Integer.parseInt(users.trainingCost);
 			if(lDataVM.allGeneratedLeadCount!=0){
 	 			tcl = Integer.parseInt(users.trainingCost) / lDataVM.allGeneratedLeadCount;
 			}
 		}
 	}else{
 		
 		lDataVM.trainingCost = 0;
 	}
 	lDataVM.comission = (lDataVM.totalSalePrice * Integer.parseInt(users.commission)) / 100; 
 	if(lDataVM.allGeneratedLeadCount != 0){
 		int sl = lDataVM.salary / lDataVM.allGeneratedLeadCount;
 	 	int cl = lDataVM.comission / lDataVM.allGeneratedLeadCount;
 	 	lDataVM.leadCost = sl + tcl + cl;
 	}

     	return ok(Json.toJson(lDataVM));
    }
    
    public static void addPriceRang(Map<String, Integer> priceRang){
    	 priceRang.put("0-$10,000", 0);
 		priceRang.put("$10,000-$20,000", 0);
 		priceRang.put("$20,000-$30,000", 0);
 		priceRang.put("$30,000-$40,000", 0);
 		priceRang.put("$40,000-$50,000", 0);
 		priceRang.put("$50,000-$60,000", 0);
 		priceRang.put("$60,000-$70,000", 0);
 		priceRang.put("$70,000-$80,000", 0);
 		priceRang.put("$80,000-$90,000", 0);
 		priceRang.put("$90,000-$1,00,000", 0);
 		priceRang.put("$1,00,000 +", 0);
 		
    }

    public static void fillPriceRang(Map<String, Integer> priceRang, Integer price){
    	 	int count = 1;	
 		if(price >= 0 && price < 10000){
 			priceRang.put("0-$10,000", priceRang.get("0-$10,000") + 1);
 		}else if(price >= 10000 && price < 20000){
 			priceRang.put("$10,000-$20,000", priceRang.get("$10,000-$20,000") + 1);
 		}else if(price >= 20000 && price < 30000){
 			priceRang.put("$20,000-$30,000", priceRang.get("$20,000-$30,000") + 1);
 		}else if(price >= 30000 && price < 40000){
 			priceRang.put("$30,000-$40,000", priceRang.get("$30,000-$40,000") + 1);
 		}else if(price >= 40000 && price < 50000){
 			priceRang.put("$40,000-$50,000", priceRang.get("$40,000-$50,000") + 1);
 		}else if(price >= 50000 && price < 60000){
 			priceRang.put("$50,000-$60,000", priceRang.get("$50,000-$60,000") + 1);
 		}else if(price >= 60000 && price < 70000){
 			priceRang.put("$60,000-$70,000", priceRang.get("$60,000-$70,000") + 1);
 		}else if(price >= 70000 && price < 80000){
 			priceRang.put("$70,000-$80,000", priceRang.get("$70,000-$80,000") + 1);
 		}else if(price >= 80000 && price < 90000){
 			priceRang.put("$80,000-$90,000", priceRang.get("$80,000-$90,000") + 1);
 		}else if(price >= 90000 && price < 100000){
 			priceRang.put("$90,000-$1,00,000", priceRang.get("$90,000-$1,00,000") + 1);
 		}else if(price >= 100000){
 			priceRang.put("$1,00,000 +", priceRang.get("$1,00,000 +") + 1);
 		}
 		
    }
    
    public static Result getHeardAboutUs(){
    	List<HeardAboutUsVm> vmList = new ArrayList<>();
    	List<HeardAboutUs> list = HeardAboutUs.getAll();
    	for (HeardAboutUs info : list) {
			HeardAboutUsVm vm = new HeardAboutUsVm();
			vm.id = info.id;
			vm.value = info.value;
			vmList.add(vm);
		}
    	return ok(Json.toJson(vmList));
    }
    
    public static Result addHeard(String name){
    	List<HeardAboutUs> nameList = HeardAboutUs.getByValue(name);
    	if(nameList.size() == 0){
    		HeardAboutUs obj = new HeardAboutUs();
    		obj.value = name;
    		obj.save();
    	}
    	return ok();
    }
    
    /**
     * @return
     */
    public static Result createLead() {
    	AuthUser user = (AuthUser)getLocalUser();
    	
    	SimpleDateFormat parseTime = new SimpleDateFormat("hh:mm a");
    	MultipartFormData bodys = request().body().asMultipartFormData();
    	
    	LeadVM leadVM = null;
    	
    	Form<LeadVM> form = DynamicForm.form(LeadVM.class).bindFromRequest();
    	if(bodys != null){
    		LeadVM leadVM1 = new LeadVM();
    		saveBilndVm(leadVM1,bodys,form);
    		leadVM = leadVM1;
    	}else{
    		leadVM = form.get();
    	}
    	String leadTypes = leadVM.leadType;
    	LeadType lType = LeadType.findByName(leadVM.leadType);
    	if(lType != null){
    		leadVM.leadType = lType.id.toString();
    	}
    	CustomizationForm cForm = CustomizationForm.findByLocationsAndType(Long.valueOf(session("USER_LOCATION")), leadTypes);
    	
    	
    	Date date = new Date();
    	int parentFlag = 0;
    	long parentLeadId = 0L;
    	String productId = null;
    	//List<SalesPersonZipCode> zipcode = SalesPersonZipCode.findByZipCode(Long.valueOf(session("USER_LOCATION")), leadVM.custZipCode);
    	//List<AddCollection> productlist = AddCollection.findByTitle(leadVM.manufacturers);
    	if(leadVM.saveLeadTypeAs.equals("SubCollection")  || leadVM.saveLeadTypeAs.equals("Product")){
    		AddCollection inventoryVM = AddCollection.findById(Long.parseLong(leadVM.manufacturers));
    		productId = inventoryVM.id.toString();
    	}else if(leadVM.saveLeadTypeAs.equals("MainCollection")){
    		InventorySetting inventoryVM = InventorySetting.findById(Long.parseLong(leadVM.manufacturers));
    		productId = inventoryVM.id.toString();
    	}
    	
    	RequestMoreInfo info = new RequestMoreInfo();
    	   // for(AddCollection inventoryVM:productlist){
    	   
	    		
	    		info.setIsReassigned(true);
	    		info.setLeadStatus(null);
	    		info.setEmail(leadVM.custEmail);
	    		info.setName(leadVM.custName);
	    		info.setSaveLeadTypeAs(leadVM.saveLeadTypeAs);
	    		info.setPhone(leadVM.custNumber);
	    		info.setCustZipCode(leadVM.custZipCode);
	    		info.setEnthicity(leadVM.enthicity);
	    		/*for(SalesPersonZipCode per:zipcode){
	    			if(per.zipCode != null){
	    				info.setAssignedTo(per.user);
	    				break;
	    			}
	    		}
	    		if(zipcode.size() == 0){
	    			AuthUser auth = AuthUser.findByOutLeftAll(Long.valueOf(session("USER_LOCATION")));
	    			if(auth != null){
	    				info.setAssignedTo(auth);
	    			}else{
	    				info.setAssignedTo(user);
	    			}
	    		}*/
	    		info.setAssignedTo(user);
	    		 //product = Inventory.findById(inventoryVM.id);
	    		info.setProductId(productId);
	    		info.setUser(user);
				info.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	    		info.setIsScheduled(false);
	    		info.setIsRead(1);
	    		info.setProductList(leadVM.productList);
	    		info.setHearedFrom(leadVM.hearedFrom);
	    		info.setContactedFrom(leadVM.contactedFrom);
	    		info.setPremiumFlag(0);
	    		info.setOnlineOrOfflineLeads(0);
	    		info.setRequestDate(new Date());
	    		info.setRequestTime(new Date());
	    		info.setIsContactusType(leadVM.leadType);
	    		info.save();
	    		
	    		saveCustomData(info.id,leadVM.customData,bodys,Long.parseLong(leadVM.leadType));
	    	
	    		
	    		UserNotes uNotes = new UserNotes();
	    		uNotes.setNote("Lead has been created");
	    		uNotes.setAction("Other");
	    		uNotes.createdDate = date;
	    		uNotes.createdTime = date;
	    		uNotes.saveHistory = 1;
	    		uNotes.user = user;
	    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	    		uNotes.requestMoreInfo = RequestMoreInfo.findById(info.id);
	    		uNotes.save();
    	   // }
	    
    	    String outcomeIds = null;
    	    if(cForm != null){
    	    	outcomeIds = cForm.outcome;
    	    }
    	    
    	    if(outcomeIds != null){
    	    	
    	    String[] out_id = outcomeIds.split(",");
    	    for(String sid:out_id){
    	    	if(sid.equals("4")){
    	    		Map map = new HashMap();
                	map.put("email",info.email);
                	//map.put("confirmDate", vm.bestDay);
                	//map.put("confirmTime", vm.bestTime);
                	//map.put("vin", vm.vin);
                	//map.put("weatherValue", vm.weatherValue);
                	map.put("leadTypes", leadTypes);
                	map.put("manufacturers",leadVM.manufacturers);
                	
                	map.put("uname", user.firstName+" "+user.lastName);
                	map.put("uphone", user.phone);
                	map.put("uemail", user.email);
                	map.put("clientName",info.name);
    	    		sendMail(map);
    	    	}
    	    	if(sid.equals("3")){
    	    		Date dates = new Date();
    				SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    	    		Contacts contactsObj = new Contacts();
    	    		String arr[] = info.name.split(" ");
    	    		if(arr.length >= 1) {
    	    			contactsObj.firstName = arr[0];
    	    			if(arr[0] == null || arr[0] == ""){
    	    				contactsObj.firstName = info.name;
    	    			}
    	    		} else {
    	    			contactsObj.firstName = info.name;
    	    		}
    	    		if(arr.length >= 2) {
    	    			contactsObj.middleName = arr[1];
    	    		}
    	    		if(arr.length >= 3) {
    	    			contactsObj.lastName = arr[2];
    	    		}
    	    		contactsObj.creationDate = df.format(dates);
    	    		contactsObj.type = "Offline";
    	    		contactsObj.email = info.email;
    	    		contactsObj.phone = info.phone;
    	    		contactsObj.custZipCode = info.custZipCode;
    	    		contactsObj.newsLetter = 0;
    	    		contactsObj.user = user.id;
    	    		contactsObj.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    	    		contactsObj.save();
    	    	}
    	    	if(sid.equals("2")){
    	    		
    	    		LeadType leadType = LeadType.findById(Long.parseLong(leadVM.leadType));
    	    		CustomizationForm cForm2 = null;
    	    		CustomerPdf cPdf = null;
    	    		if(leadType != null){
    	    			cForm2 = CustomizationForm.findByLeadType(leadType.leadName);
    	    			if(cForm2 != null){
    	    				cPdf = CustomerPdf.findPdfById(cForm2.sendPdf.id);
    	    			}
    	    		}
    	    		
    	    		
    	    		
    	    		   /*------------------------------------------*/
    	    		
    	    		
    	    		 
    	    		ProductImages pImages = ProductImages.findDefaultImg(Long.parseLong(info.productId));
    	    		
    	    		AuthUser defaultUser = getLocalUser();
    	    		//AuthUser defaultUser = AuthUser.findById(Integer.getInteger(session("USER_KEY")));
    	    		SiteLogo siteLogo = SiteLogo.findByLocation(Long.valueOf(session("USER_LOCATION")));
    	    		SiteContent siteContent = SiteContent.findByLocation(Long.valueOf(session("USER_LOCATION")));
    	    		String heading1 = "",heading2 = "";
    	    		if(siteContent.getHeading()!=null) {
    	    		    int index= siteContent.getHeading().lastIndexOf(" ");
    	    		    heading1 = "ssss";//siteContent.getHeading().substring(0, index);
    	    		    heading2 = "aaaa";//siteContent.getHeading().substring(index+1);
    	    		}
    	    		String filepath = null,findpath = null;

/*    	    		try {
    	    			Document document = new Document();
    	    			createDir(pdfRootDir, Long.parseLong(session("USER_LOCATION")), info.getId());
    	    			filepath = pdfRootDir + File.separator + Long.parseLong(session("USER_LOCATION"))
    	    					+ File.separator + "trade_in_pdf" + File.separator
    	    					+ info.getId() + File.separator + "Trade_In.pdf";
    	    			findpath = File.separator + Long.parseLong(session("USER_LOCATION")) + File.separator + "trade_in_pdf" + File.separator
    	    					+ info.getId() + File.separator + "Trade_In.pdf";
    	    			RequestMoreInfo tIn2 = RequestMoreInfo.findById(info.getId());
    	    			tIn2.setPdfPath(findpath);
    	    			tIn2.update();
    	    			PdfWriter pdfWriter = PdfWriter.getInstance(document,
    	    					new FileOutputStream(filepath));

    	    			document.addAuthor("Celinio");
    	    			document.addCreator("Celinio");
    	    			document.addSubject("iText with Maven");
    	    			document.addTitle(leadTypes);
    	    			document.addKeywords("iText, Maven, Java");

    	    			document.open();

    	    			Font font = new Font();
    	    			font.setStyle(Font.UNDERLINE);
    	    			font.setStyle(Font.ITALIC);

    	    			Font font1 = new Font(FontFamily.HELVETICA, 8, Font.NORMAL,
    	    					BaseColor.BLACK);
    	    			Font font2 = new Font(FontFamily.HELVETICA, 8, Font.BOLD,
    	    					BaseColor.BLACK);

    	    			PdfPTable Titlemain = new PdfPTable(1);
    	    			Titlemain.setWidthPercentage(100);
    	    			float[] TitlemainWidth = { 2f };
    	    			Titlemain.setWidths(TitlemainWidth);
    	    			


    	    			PdfPCell title = new PdfPCell(new Phrase(leadTypes));
    	    			title.setBorderColor(BaseColor.WHITE);
    	    			title.setBackgroundColor(new BaseColor(255, 255, 255));
    	    			Titlemain.addCell(title);

    	    			PdfPTable contactInfo = new PdfPTable(4);
    	    			contactInfo.setWidthPercentage(100);
    	    			float[] contactInfoWidth = { 2f, 2f, 2f, 2f };
    	    			contactInfo.setWidths(contactInfoWidth);

    	    			PdfPCell firstname = new PdfPCell(new Phrase("Name:",
    	    					font1));
    	    			firstname.setBorderColor(BaseColor.WHITE);
    	    			firstname.setBackgroundColor(new BaseColor(255, 255, 255));
    	    			contactInfo.addCell(firstname);

    	    			PdfPCell firstnameValue = new PdfPCell(new Paragraph(
    	    					info.getName(), font2));
    	    			firstnameValue.setBorderColor(BaseColor.WHITE);
    	    			firstnameValue.setBorderWidth(1f);
    	    			contactInfo.addCell(firstnameValue);

    	    			PdfPCell lastname = new PdfPCell(
    	    					new Phrase("Email:", font1));
    	    			lastname.setBorderColor(BaseColor.WHITE);
    	    			contactInfo.addCell(lastname);

    	    			PdfPCell lastnameValue = new PdfPCell(new Paragraph(info.email, font2));
    	    			lastnameValue.setBorderColor(BaseColor.WHITE);
    	    			lastnameValue.setBorderWidth(1f);
    	    			contactInfo.addCell(lastnameValue);
    	    			
    	    			PdfPCell phone = new PdfPCell(new Phrase("Phone:", font1));
    	    			phone.setBorderColor(BaseColor.WHITE);
    	    			contactInfo.addCell(phone);

    	    			PdfPCell phoneValue = new PdfPCell(new Paragraph(
    	    					info.getPhone(), font2));
    	    			phoneValue.setBorderColor(BaseColor.WHITE);
    	    			phoneValue.setBorderWidth(1f);
    	    			contactInfo.addCell(phoneValue);

    	    			
    	    			for(KeyValueDataVM custom:leadVM.customData){
    		        		
    		        		CustomizationDataValue cDataValue = CustomizationDataValue.findByKeyAndLeadId(custom.key,info.id);
    		        		if(cDataValue != null){
    	    	    			PdfPCell workPhone = new PdfPCell(new Phrase(custom.key,font1));
    	    	    			workPhone.setBorderColor(BaseColor.WHITE);
    	    	    			contactInfo.addCell(workPhone);

    	    	    			PdfPCell workPhoneValue = new PdfPCell(new Paragraph(custom.value, font2));
    	    	    			workPhoneValue.setBorderColor(BaseColor.WHITE);
    	    	    			workPhoneValue.setBorderWidth(1f);
    	    	    			contactInfo.addCell(workPhoneValue);
    		        			
    		        		}
    		    			
    		    		}




    	    			// ----------sub main Table----------

    	    			PdfPTable AddAllTableInMainTable = new PdfPTable(1);
    	    			AddAllTableInMainTable.setWidthPercentage(100);
    	    			float[] AddAllTableInMainTableWidth = { 2f };
    	    			AddAllTableInMainTable.setWidths(AddAllTableInMainTableWidth);

    	    			PdfPCell hotelVoucherTitlemain1 = new PdfPCell(Titlemain);
    	    			hotelVoucherTitlemain1.setBorder(Rectangle.NO_BORDER);
    	    			AddAllTableInMainTable.addCell(hotelVoucherTitlemain1);

    	    			PdfPCell contactInfoData = new PdfPCell(contactInfo);
    	    			contactInfoData.setBorder(Rectangle.NO_BORDER);
    	    			AddAllTableInMainTable.addCell(contactInfoData);

    	    			

    	    			// ----------main Table----------

    	    			PdfPTable AddMainTable = new PdfPTable(1);
    	    			AddMainTable.setWidthPercentage(100);
    	    			float[] AddMainTableWidth = { 2f };
    	    			AddMainTable.setWidths(AddMainTableWidth);

    	    			PdfPCell AddAllTableInMainTable1 = new PdfPCell(
    	    					AddAllTableInMainTable);
    	    			AddAllTableInMainTable1.setPadding(10);
    	    			AddAllTableInMainTable1.setBorderWidth(1f);
    	    			AddMainTable.addCell(AddAllTableInMainTable1);

    	    			document.add(AddMainTable);

    	    			document.close();

    	    		} catch (Exception e) {
    	    			e.printStackTrace();
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
    	    		props.put("mail.smtp.port", port);
    	    		props.put("mail.smtp.starttls.enable", "true");
    	    		Session session = Session.getInstance(props,
    	    				new javax.mail.Authenticator() {
    	    					protected PasswordAuthentication getPasswordAuthentication() {
    	    						return new PasswordAuthentication(emailUser,
    	    								emailPass);
    	    					}
    	    				});
    	    		try {
    	    			List<AuthUser> users = AuthUser.getAllUsers();

    	    			/*InternetAddress[] usersArray = new InternetAddress[users.size() + 1];
    	    			int index = 0;
    	    			usersArray[index] = new InternetAddress(user.getEmail());*/
    	    			
    	    			InternetAddress[] usersArray = new InternetAddress[1];
    	    			int index = 0;
    	    			usersArray[index] = new InternetAddress(info.email);
    	    			//usersArray[index] = new InternetAddress(usersArray);
    	    			
    	    			Message message = new MimeMessage(session);
    	        		try{
    	    			message.setFrom(new InternetAddress(emailUser,emailName));
    	        		}
    	        		catch(UnsupportedEncodingException e){
    	        			e.printStackTrace();
    	        			
    	        		}
    	    			message.setRecipients(Message.RecipientType.TO, usersArray);
    	    			message.setSubject(leadTypes);
    	    			Multipart multipart = new MimeMultipart();
    	    			BodyPart messageBodyPart = new MimeBodyPart();
    	    			messageBodyPart = new MimeBodyPart();

    	    			VelocityEngine ve = new VelocityEngine();
    	    			ve.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
    	    					"org.apache.velocity.runtime.log.Log4JLogChute");
    	    			ve.setProperty("runtime.log.logsystem.log4j.logger",
    	    					"clientService");
    	    			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
    	    			ve.setProperty("classpath.resource.loader.class",
    	    					ClasspathResourceLoader.class.getName());
    	    			ve.init();

    	    			String urlfind = "http://www.glider-autos.com/dealer/index.html#/tradeIn";

    	    			Template t = ve.getTemplate("/public/emailTemplate/trade_in_app.vm");
    	    			VelocityContext context = new VelocityContext();

    	    			// ---------Trad in info---------------

    	    			// contact info
    	    			context.put("first_name", info.getName());
    	    			//context.put("work_phone", "");
    	    			context.put("email", info.getEmail());
    	    			if(cPdf != null){
    	    				context.put("pdffilePath", "/"+cPdf.pdf_path);
    	    			}
    	    			
    	    			//context.put("pdffilePath", findpath);


    	    			// vehicale info


    	    			    
    	    			    context.put("title", leadTypes);
    	    			    context.put("subtitle", leadVM.manufacturers);
    	    			    
    	    				context.put("sitelogo", siteLogo.getLogoImageName());
    	    				context.put("path", siteLogo.getLogoImagePath());
    	    				context.put("heading1", heading1);
    	    				context.put("heading2", heading2);
    	    				context.put("urlLink", vehicleUrlPath);
    	    				context.put("urlfind", urlfind);
    	    				context.put("hostnameimg", imageUrlPath);

    	    				StringWriter writer = new StringWriter();
    	    				t.merge(context, writer);
    	    				String content = writer.toString();
    	    				// attachPart.attachFile(file);
    	    				messageBodyPart.setContent(content, "text/html");
    	    				multipart.addBodyPart(messageBodyPart);

    	    				message.setContent(multipart);
    	    				Transport.send(message);
    	    				System.out.println("Sent test message successfully....");
    	    			} catch (Exception e) {
    	    				e.printStackTrace();
    	    			}
    	        		
    	        	 
    	    		
    	    		
    	    		
    	    		
    	    		
    	    		
    	    		
    	    		 
    	    		
    	    		
    	    		
    	    		
    	    		  /*----------------------------------------------------*/
    	    	}
    	    }
        }
    	
    	return ok();
    }
    
	public static void createDir(String pdfRootDir,long locationId, long lastId) {
        File file = new File(pdfRootDir + File.separator+ locationId +File.separator+ "trade_in_pdf"+File.separator+lastId);
        if (!file.exists()) {
                file.mkdirs();
        }
	}
	
		
    public static void findCustomCrmData(Long id,ContactsVM inventoryvm){
    	List<CustomizationCrm> custData = CustomizationCrm.findByIdList(id);
    	List<KeyValueDataVM> keyValueList = new ArrayList<>();
    	Map<String, String> mapCar = new HashMap<String, String>();
    	for(CustomizationCrm custD:custData){
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
	
	
	 private static void saveCustomCrmData(Long InventoryId,ContactsVM vm) {
	       	
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
	       }
	
	
	public static Result sendNewsletterEmail() {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -30);
		List<Blog> blogList = Blog.getBlogsByDate(cal.getTime(),date);
		AuthUser user = getLocalUser();
		//AuthUser user = AuthUser.findById(Integer.getInteger(session("USER_KEY")));
		
		//MyProfile profile = MyProfile.findByUser(user);
		List<MyProfile> profile = MyProfile.findByLocation(Long.valueOf(session("USER_LOCATION")));
		String mapUrl = "https://maps.google.com/?q="+profile.get(0).address+","+profile.get(0).city+","+profile.get(0).zip+","+profile.get(0).state+","+profile.get(0).country;
		if(blogList.size() > 0) {
			AuthUser logoUser = getLocalUser();
			//AuthUser logoUser = AuthUser.findById(Integer.getInteger(session("USER_KEY")));
	    	SiteLogo logo = SiteLogo.findByLocation(Long.valueOf(session("USER_LOCATION"))); //findByUser(logoUser);
			List<Contacts> contactsList = Contacts.getAllNewsletter();
			for(Contacts contact : contactsList) {
				EmailDetails details=EmailDetails.findByLocation(Long.valueOf(session("USER_LOCATION")));
				String emailName=details.name;
				String port=details.port;
				String gmail=details.host;
				final	String emailUser=details.username;
				final	String emailPass=details.passward;
				
				Properties props = new Properties();
		 		props.put("mail.smtp.auth", "true");
		 		props.put("mail.smtp.starttls.enable", "true");
		 		props.put("mail.smtp.host", gmail);
		 		props.put("mail.smtp.port", port);
		  
		 		Session session = Session.getInstance(props,
		 		  new javax.mail.Authenticator() {
		 			protected PasswordAuthentication getPasswordAuthentication() {
		 				return new PasswordAuthentication(emailUser, emailPass);
		 			}
		 		  });
		  
		 		try{
		 		   
		  			Message message = new MimeMessage(session);
		  			try {
						message.setFrom(new InternetAddress(emailUser,emailName));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		  			message.setRecipients(Message.RecipientType.TO,
		  			InternetAddress.parse(contact.getEmail()));
		  			message.setSubject("Newsletter ");	  			
		  			Multipart multipart = new MimeMultipart();
	    			BodyPart messageBodyPart = new MimeBodyPart();
	    			messageBodyPart = new MimeBodyPart();
	    			
	    			VelocityEngine ve = new VelocityEngine();
	    			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
	    			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
	    			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
	    			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
	    			ve.init();
	    			
	    			Template t = ve.getTemplate("/public/emailTemplate/newsletterTemplate.vm"); 
	    	        VelocityContext context = new VelocityContext();
	    	        context.put("hostnameUrl", imageUrlPath);
	    	        context.put("siteLogo", logo.logoImagePath);
	    	        context.put("title", blogList.get(0).getTitle());
	    	        context.put("blogImage", blogList.get(0).getImageUrl());
	    	        context.put("description", blogList.get(0).getDescription());
	    	        context.put("mapUrl", mapUrl);
	    	        context.put("phone", profile.get(0).phone);
	    	        context.put("email", profile.get(0).email);
	    	        
	    	        StringWriter writer = new StringWriter();
	    	        t.merge( context, writer );
	    	        String content = writer.toString(); 
	    			
	    			messageBodyPart.setContent(content, "text/html");
	    			multipart.addBodyPart(messageBodyPart);
	    			message.setContent(multipart);
	    			Transport.send(message);
	    			System.out.println("email send");
		       		} catch (MessagingException e) {
		  			  throw new RuntimeException(e);
		  		}
			}
		}
		return ok();
	}
	
	public static Result googleConnectionStatus() throws Exception { 
		try {
			authorize();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return redirect(authorize());
		
	}
	/*public static Result googleConnectionStatusTasks() throws Exception { 
		try {
			authorizeTasks();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		flagValue = 1;
		return redirect(authorizeTasks());
		
	}*/
	
	public static Result oauth2Callback() {
		
		
		/*AuthUser user11 = (AuthUser)getLocalUser();
		session("USER_KEY", user11.id+"");
		session("USER_ROLE", user11.role+"");
		
		if(user11.location != null){
			session("USER_LOCATION", user11.location.id+"");
		}*/
		
		String code = request().getQueryString("code");
		//if(flagValue == 0){
			events1.clear();
			try {
				TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectURI).execute();
				credential = flow.createAndStoreCredential(response, "userID");
				client = new com.google.api.services.calendar.Calendar.Builder(httpTransport, JSON_FACTORY, credential).
						setApplicationName(APPLICATION_NAME).build();

				oauth2 = new Oauth2.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(
						APPLICATION_NAME).build();
				com.google.api.services.calendar.Calendar.Events events=client.events();
				com.google.api.services.calendar.model.Events eventList=events.list("primary").execute();
				events1 = eventList.getItems();
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
				SimpleDateFormat parseTime = new SimpleDateFormat("hh:mm a");
				Calendar calendar = Calendar.getInstance();
				Calendar calendar1 = Calendar.getInstance();
				for(Event ev:events1){
					ScheduleTest  scheduleTest = new ScheduleTest();
					calendar.setTimeInMillis(ev.getEnd().getDateTime().getValue());
					calendar1.setTimeInMillis(ev.getStart().getDateTime().getValue());
					String startDate = df.format(calendar1.getTime());
					String starttime = parseTime.format(calendar1.getTime());
					scheduleTest.setConfirmDate(df.parse(startDate));
					scheduleTest.setConfirmTime(parseTime.parse(starttime));
					scheduleTest.setEmail(ev.getSummary());
					scheduleTest.setVin("no");
					scheduleTest.setGoogle_id(ev.getId());
					scheduleTest.setIs_google_data(true);
					scheduleTest.setLocations(Location.findById(Long.valueOf(session("USER_LOCATION"))));
					scheduleTest.save();
					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		
			Calendar c1 = Calendar.getInstance();
			Calendar c = Calendar.getInstance();
	        c.add(Calendar.DAY_OF_YEAR, +7);
	        
			tasksList.clear();
			Tasks tasks = null;
			List<String> idtastlist = new ArrayList<>();
			
			try {
				//TokenResponse response = flowtask.newTokenRequest(code).setRedirectUri(redirectURI).execute();
				//credentialtask = flowtask.createAndStoreCredential(response, "userID");
				service = new com.google.api.services.tasks.Tasks.Builder(httpTransport, JSON_FACTORY, credential)
		           .setApplicationName(APPLICATION_NAME).build();
				TaskLists taskLists = service.tasklists().list().execute();
				//String code1 = null;
				for (TaskList taskList : taskLists.getItems()) {
				  idtastlist.add(taskList.getId());
				}
				AuthUser user = getLocalUser();
				for(String taskId:idtastlist){
					tasks = service.tasks().list(taskId).execute();
					tasksList.add(tasks);
					for (Task task : tasks.getItems()) {
						//tasksList.add(task);
						DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						Calendar calendar = Calendar.getInstance();
						try{
							calendar.setTimeInMillis(task.getDue().getValue());
						}catch(Exception e){
							calendar.setTimeInMillis(task.getUpdated().getValue());
						}
						
						String dateValue = df.format(calendar.getTime());
						String googleDate = sdf.format(calendar.getTime());
						String sevenDayDate = sdf.format(c.getTime());
						String currDate = sdf.format(c1.getTime());
						if(sdf.parse(sevenDayDate).after(sdf.parse(googleDate)) && sdf.parse(googleDate).after(sdf.parse(currDate))){
							List<ToDo> toDo1 = ToDo.findByDateAndTask(df.parse(dateValue), task.getTitle()); 
							if(toDo1.size() == 0){
								ToDo toDo = new ToDo();
					    		toDo.setTask(task.getTitle());
					    		toDo.setStatus("Assigned");
					    		toDo.setDueDate(df.parse(dateValue));
					    		toDo.setPriority("High");
					    		toDo.setAssignedBy(user);
					    		toDo.setAssignedTo(user);
					    		toDo.setSaveas(1);
					    		toDo.setGoogle_id(task.getId());
								//toDo.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
					    		toDo.save();
							}
						}else if(sdf.parse(currDate).equals(sdf.parse(googleDate))){
							List<ToDo> toDo1 = ToDo.findByDateAndTask(df.parse(dateValue), task.getTitle()); 
							if(toDo1.size() == 0){
								ToDo toDo = new ToDo();
								toDo.setTask(task.getTitle());
					    		toDo.setStatus("Assigned");
					    		toDo.setDueDate(df.parse(dateValue));
					    		toDo.setPriority("High");
					    		toDo.setAssignedBy(user);
					    		toDo.setAssignedTo(user);
					    		toDo.setSaveas(1);
					    		toDo.setGoogle_id(task.getId());
								//toDo.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
					    		toDo.save();
							}
						}
					}
				}
						
			} catch (Exception e) {
				e.printStackTrace();
			}
			/*AuthUser user = getLocalUser();
			HashMap<String, Boolean> permission = new HashMap<String, Boolean>();
			List<Permission> userPermissions = user.getPermission();
			for(Permission per: userPermissions) {
				permission.put(per.name, true);
			}*/
			return redirect("/authenticate");
	}
	
	

	static List<String> SCOPES = new ArrayList<String>();
	//static List<String> SCOPESTASK = new ArrayList<String>();

	private static String  authorize() throws Exception {
		AuthorizationCodeRequestUrl authorizationUrl;
		if(flow==null){
			Details web=new Details();
			web.setClientId(clientId);
			web.setClientSecret(clientSecret);
			clientSecrets = new GoogleClientSecrets().setWeb(web);
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			SCOPES.add(CalendarScopes.CALENDAR);
			SCOPES.add(TasksScopes.TASKS);
			SCOPES.add(TasksScopes.TASKS_READONLY);
			SCOPES.add(Oauth2Scopes.USERINFO_EMAIL);
			SCOPES.add(Oauth2Scopes.USERINFO_PROFILE);
			
			flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
					SCOPES).build();
		}
		authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectURI);
		
		return authorizationUrl.build();
	}

	/*private static String  authorizeTasks() throws Exception {
		AuthorizationCodeRequestUrl authorizationUrl;
		System.out.println(flowtask);
		if(flowtask==null){
			Details web=new Details();
			web.setClientId(clientId);
			web.setClientSecret(clientSecret);
			clientSecretstask = new GoogleClientSecrets().setWeb(web);
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			SCOPESTASK.add(TasksScopes.TASKS);
			SCOPESTASK.add(TasksScopes.TASKS_READONLY);
			SCOPESTASK.add(Oauth2Scopes.USERINFO_EMAIL);
			SCOPESTASK.add(Oauth2Scopes.USERINFO_PROFILE);
			flowtask = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecretstask,
					SCOPESTASK).build();
		}
		authorizationUrl = flowtask.newAuthorizationUrl().setRedirectUri(redirectURI);
		
		return authorizationUrl.build();
	}*/

	
	
	public Set<Event> getEvents() throws IOException{
		return this.events;
	}
	
	public static Result addSameNewCar(Long vinNo){
		Vehicle vm = Vehicle.findById(vinNo);
		Vehicle vObj = Vehicle.findByVinAndStatus(vm.vin);
		
		if(vObj == null){
			Identity user = getLocalUser();
	    	//Vehicle vehicleObj = Vehicle.findByVidAndUser(vm.vin);
	    	Vehicle vehicle = new Vehicle();
		    	vehicle.title = vm.title;
		    	vehicle.category = vm.category;
		    	vehicle.vin = vm.vin;
		    	vehicle.year = vm.year;
		    	vehicle.make = vm.make;
		    	vehicle.model = vm.model;
		    	vehicle.trim = vm.trim;
		    	vehicle.label = vm.label;
		    	vehicle.stock = vm.stock;
		    	vehicle.cityMileage = vm.cityMileage;
		        vehicle.mileage=vm.mileage;
		    	vehicle.highwayMileage = vm.highwayMileage;
		    	vehicle.cost = vm.cost;
		    	vehicle.price = vm.price;
		    	vehicle.madeIn = vm.madeIn;
		    	vehicle.optionalSeating = vm.optionalSeating;
		    	vehicle.exteriorColor = vm.exteriorColor;
		    	vehicle.colorDescription = vm.colorDescription;
		    	vehicle.doors = vm.doors;
		    	vehicle.stereo = vm.stereo;
		    	vehicle.engine = vm.engine;
		    	vehicle.bodyStyle = vm.bodyStyle;
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
		    	vehicle.engineType = vm.engineType;
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
		    	vehicle.activeHeadRestrains = vm.activeHeadRestrains;
		    	vehicle.bodySideReinforcements = vm.bodySideReinforcements;
		    	vehicle.crumpleZones = vm.crumpleZones;
		    	vehicle.impactAbsorbingBumpers = vm.impactAbsorbingBumpers;
		    	vehicle.impactSensor = vm.impactSensor;
		    	vehicle.parkingSensors = vm.parkingSensors;
		    	vehicle.seatbelts = vm.seatbelts;
		    	vehicle.audiSideAssist = vm.audiSideAssist;
		    	vehicle.interiorColor = vm.interiorColor;
		    	vehicle.comfortFeatures = vm.comfortFeatures;
		    	vehicle.powerOutlet = vm.powerOutlet;
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
				
		    	vehicle.madeIn = vm.madeIn;
		    	vehicle.optionalSeating = vm.optionalSeating;
		    	vehicle.status = "Newly Arrived";
		    	vehicle.postedDate = new Date();
		    	/*List<Site> siteList = new ArrayList<>();
		    	if(vm.siteIds != null) {
			    	for(Long obj: vm.siteIds) {
			    		Site siteObj = Site.findById(obj);
			    		siteList.add(siteObj);
			    	}
			    	vehicle.site = siteList;
		    	}*/
		    	vehicle.save();
		    	AddEditInventoryController.sendEmailToBrandFollowers(vehicle.make);
	    	return ok("success");
		}else{
			return ok("error");
		}
	}
	
	public static Result getScheduleTestData(){
        AuthUser user = getLocalUser();
        
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        Date currD = new Date();
        System.out.println("current time");
        System.out.println(currD);
        String cDate = df.format(currD);
        Date datec = null;
        try {
			datec = df.parse(cDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
       
    	List<RequestMoreInfo> requestMoreInfos = RequestMoreInfo.findByConfirmGraLeads(Long.valueOf(session("USER_LOCATION")), user, datec);
    	
        Map<Long,Integer> maps = new HashMap<Long, Integer>();
        List<RequestInfoVM> shList = new ArrayList<RequestInfoVM>();
        

    	 Date currD1 = new Date();
    	 Date currentDate = null;
    	 Date infoDate = null;
    	 DateFormat df1 = new SimpleDateFormat("MM-dd-yyyy HH:mm a");
    	 DateFormat df2 = new SimpleDateFormat("MM-dd-yyyy HH:mm a");
    	 SimpleDateFormat parseTime = new SimpleDateFormat("HH:mm a");
   		 Location location = Location.findById(16l);
   		 if(user.location != null){
   			 location = Location.findById(user.location.id);
   		 }
   		 df1.setTimeZone(TimeZone.getTimeZone(location.time_zone));
         String IST = df1.format(currD1);
         Date istTimes = null;
         try {
				istTimes = df2.parse(IST);
				String crD =    df2.format(istTimes);
	 			currentDate = df2.parse(crD);
	 			System.out.println("current Date :  "+currentDate);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
         
        fillLeadsData(requestMoreInfos, shList);
        
        //public static void fillLeadsData(List<ScheduleTest> listData, List<RequestMoreInfo> requestMoreInfos, List<TradeIn> tradeIns, List<RequestInfoVM> infoVMList){
        
        return ok(Json.toJson(shList));
    }
	
	

	public static Result updateCalender(){
		//DynamicForm form = DynamicForm.form().bindFromRequest();
		Form<ScheduleTestVM> form = DynamicForm.form(ScheduleTestVM.class).bindFromRequest();
		ScheduleTestVM vm = form.get();
		List<AuthUser> list=new ArrayList<>();
		List<AuthUser> newlist=new ArrayList<>();
		AuthUser user = getLocalUser();
		Long id =vm.id;
		String confDate = vm.confDate;
		String confTime = vm.confTime;
		String confEndTime = vm.confirmEndTime;
		String googleID = vm.google_id;
		boolean infoChange = false;
	    /* String id = form.get("id");
		String confDate = form.get("confDate");
		String confTime = form.get("confTime");
		String googleID = form.get("googleID");
		
		SessionUseVM vm = new SessionUseVM();
		vm.id = id;
		vm.cnfDate = confDate;
		vm.cnfTime = confTime;
		vm.eventID = googleID;
		session("sessionVmData", Json.stringify(Json.toJson(vm)));
		
		*/
		DateFormat df = new SimpleDateFormat("MM-dd-yyyy");

		SimpleDateFormat time= new SimpleDateFormat("hh:mm a");
		List<UserVM> newUser = new ArrayList<>();
		
		if(vm.groupId != null){
			try {
				for (UserVM obj : vm.getUsersList()) {
					boolean flg = true;
					List<ScheduleTest> test = ScheduleTest.findAllGroupMeeting(vm.groupId);
					for (ScheduleTest st : test) {
						AuthUser assiTest = AuthUser.findById(st.assignedTo.id);
						if(assiTest.id == obj.id){
							flg = false;
							break;
						}
					}
					if(flg){
						newUser.add(obj);
					}
				}
				
				List<ScheduleTest> test = ScheduleTest.findAllGroupMeeting(vm.groupId);
				for(ScheduleTest testloop:test){
					if(! testloop.getName().equals(vm.name) || ! testloop.getReason().equals(vm.reason) || ! testloop.getConfirmDate().equals(df.parse(confDate)) || ! testloop.getConfirmTime().equals(time.parse(confTime))|| ! testloop.getConfirmEndTime().equals(time.parse(confEndTime))){
						infoChange = true;
					}
					AuthUser assi = AuthUser.findById(testloop.assignedTo.id);
					testloop.setName(vm.name);
					testloop.setReason(vm.reason);
					if(!testloop.getConfirmDate().equals(df.parse(confDate)) || ! testloop.getConfirmTime().equals(time.parse(confTime))|| ! testloop.getConfirmEndTime().equals(time.parse(confEndTime))){
						list.add(assi);
						testloop.setDeclineUpdate(1);
            	    // testloop.setSendInvitation(1);
						String subject = "Meeting's information has been changed.";
 			   	    	String comments = "Meeting invitation received \n "+user.firstName+" "+user.lastName+"\n"+vm.getConfDate()+" "+vm.getConfirmTime()+".";
 			   	    	//sendEmail(assi.communicationemail, subject, comments);
 			   	    testloop.setConfirmDate(df.parse(confDate));
					testloop.setConfirmTime(new SimpleDateFormat("hh:mm a").parse(confTime));
					if(confEndTime!=null)
					testloop.setConfirmEndTime(new SimpleDateFormat("hh:mm a").parse(confEndTime));
			       }
					testloop.update();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{

			try {
				
				ScheduleTest test = ScheduleTest.findById(id);
				AuthUser assi = AuthUser.findById(test.assignedTo.id);
				test.setConfirmDate(df.parse(confDate));
				test.setConfirmTime(new SimpleDateFormat("hh:mm a").parse(confTime));
				test.setName(vm.name);
				test.setReason(vm.reason);
				if(! test.getName().equals(vm.name) || ! test.getReason().equals(vm.reason) || ! test.getConfirmDate().equals(df.parse(confDate)) || ! test.getConfirmTime().equals(time.parse(confTime))){
					infoChange = true;
				}
				if(!test.getConfirmDate().equals(df.parse(confDate)) || ! test.getConfirmTime().equals(time.parse(confTime))){
					list.add(assi);
					test.setSendInvitation(1);
					String subject = "Meeting invitation.";
			   	    String comments = "New meeting invitation received \n "+user.firstName+" "+user.lastName+"\n"+vm.getConfDate()+" "+vm.getConfirmTime()+".";
					//sendEmail(assi.communicationemail, subject, comments);
			       }
				test.update();
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}

		List<AuthUser> userList = new ArrayList<>();
		for (UserVM obj : newUser) {
			AuthUser assi = AuthUser.findById(obj.id);
			ScheduleTest moTest = new ScheduleTest();
			moTest.assignedTo = assi;
			newlist.add(assi);
			moTest.name=vm.getAssignedTo();
			moTest.bestDay = vm.getBestDay();
			moTest.bestTime = vm.getBestTime();
			moTest.email = user.getEmail();
			moTest.location = vm.getLocation();
			moTest.name = vm.name;
			moTest.groupId = vm.groupId;
			moTest.meetingStatus = "meeting";
		 	moTest.phone = user.getPhone();
			moTest.reason = vm.getReason();
			moTest.scheduleDate = new Date();
			moTest.scheduleTime = new Date();
		    moTest.user = user;
			moTest.isReassigned = false;
			moTest.sendInvitation = 1;
			moTest.acceptMeeting = 1;
			moTest.declineMeeting = 1;
			moTest.meeting = 1;
			moTest.is_google_data = false;
			userList.add(assi);
			try {			
				moTest.confirmDate = df.parse(vm.confDate);
				moTest.confirmTime = new SimpleDateFormat("hh:mm a").parse(vm.confTime);
				if(vm.confirmEndTime != null)
				moTest.confirmEndTime = new SimpleDateFormat("hh:mm a").parse(vm.confirmEndTime);
			} catch (Exception e) {
				e.printStackTrace();
			}
			moTest.save();
			
			String subject = "Meeting invitation.";
	   	    String comments = "New meeting invitation received \n "+user.firstName+" "+user.lastName+"\n"+vm.getConfDate()+" "+vm.getConfirmTime()+".";
	   	 //sendMeetingMailInfoChange(vm, user, userList);
		}
		if(newUser.size() > 0){
			String[] dt = vm.confirmDate.split("-");
			vm.bestDay = dt[1]+"-"+dt[0]+"-"+dt[2];
			vm.bestTime = vm.confirmTime;
			vm.bestEndTime = vm.confirmEndTime;
			sendMeetingMailToAssignee(vm, user, userList);
		}
		if(infoChange){
			sendMeetingMailInfoChange(vm, user, list);
		}
		
		
		/*try {
			if(test.getGoogle_id()!=null){
				return redirect(authorizeUpdate());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		/**/
		//return redirect("/authenticate");
		return ok();
		}
	

	public static void sendMeetingMailInfoChange(ScheduleTestVM vm, AuthUser user, List<AuthUser> userList){
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
		List<UserVM> list = new ArrayList<>() ;
		for(AuthUser assi : userList){
			
			UserVM vm1=new UserVM();
			vm1.fullName=assi.firstName+" "+assi.lastName;
			list.add(vm1);
			
			
			
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
    		}
    		catch(UnsupportedEncodingException e){
    			e.printStackTrace();
    		}
			message.setRecipients(Message.RecipientType.TO,usersArray);
			/*usersArray*/
			message.setSubject("Meeting Info changed");
			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			ve.init();
			
			Template t = ve.getTemplate("/public/emailTemplate/InternalMeetingInfoChanged_HTML.html"); 
	        VelocityContext context = new VelocityContext();
	        
	        context.put("title", vm.name);
	       // context.put("location", loc.getName());
	        context.put("meetingBy", user.getFirstName()+" "+user.getLastName());
	        
	        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");

			SimpleDateFormat time= new SimpleDateFormat("hh:mm a");
	        
	        if(!vm.confirmDate.equals(vm.confDate)){
      	         
				vm.confirmDate=vm.confDate;
				
       	   
		       }
	        
	        if( ! vm.confirmTime.equals(vm.confTime)){
     	         
				vm.confirmTime=vm.confTime;
				
       	   
		       }
	        
	        
	        String months[] = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
		       
	        int dayOfmonth=1;
	        int month=0;
	        try {
	        	SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
	        	String dateInString = vm.confirmDate;
	        	String arr[] = dateInString.toString().split("-");
		        if(arr.length >=2){
		        	dayOfmonth = Integer.parseInt(arr[1]);
			        month = Integer.parseInt(arr[0]);
		        }else{
		        	Date date = formatter.parse(dateInString);
		        	Calendar cal = Calendar.getInstance();
			         cal.setTime((Date)date);
			         dayOfmonth = cal.get(Calendar.DAY_OF_MONTH);
			         month = cal.get(Calendar.MONTH)+1;
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}
	        
	        String monthName = months[month-1];
	        context.put("hostnameUrl", imageUrlPath);
	       // context.put("siteLogo", logo.logoImagePath);
	        context.put("dayOfmonth", dayOfmonth);
	        context.put("monthName", monthName);
	        //context.put("confirmTime", map.get("confirmTime"));
	        context.put("userList",list);
	        
	        
	       /* SimpleDateFormat localDateFormat = new SimpleDateFormat("hh:mm:aa");
	        String confirmTime = localDateFormat.format(vm.confirmTime);
	        */
	        context.put("time",vm.confirmTime);
	        context.put("endTime",vm.confirmEndTime);
	        context.put("date", vm.getBestDay());
	       // context.put("time", vm.getBestTime());
	        context.put("disc", vm.getReason());
	       
	        StringWriter writer = new StringWriter();
	        t.merge( context, writer );
	        String content = writer.toString();
			
			messageBodyPart.setContent(content, "text/html");
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
			Transport.send(message);
			System.out.println("email Succ");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
		
	
	
	
	private static String  authorizeUpdate() throws Exception {
		AuthorizationCodeRequestUrl authorizationUrl;
		if(flow==null){
			Details web=new Details();
			web.setClientId(clientId);
			web.setClientSecret(clientSecret);
			clientSecrets = new GoogleClientSecrets().setWeb(web);
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			SCOPES.add(CalendarScopes.CALENDAR);
			
			flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
					SCOPES).build();
		}
		authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectURIUpdate);
		
		return authorizationUrl.build();
	}
	
	public static Result updatecalenderdata(){
		String  sessionValue =  session("sessionVmData");
		
		String code = request().getQueryString("code");
		events1.clear();
		try {
			TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectURIUpdate).execute();
			credential = flow.createAndStoreCredential(response, "userID");
			client = new com.google.api.services.calendar.Calendar.Builder(httpTransport, JSON_FACTORY, credential).
					setApplicationName(APPLICATION_NAME).build();

			oauth2 = new Oauth2.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(
					APPLICATION_NAME).build();
			/*com.google.api.services.calendar.Calendar.Events events=client.events();
			Event ev = events.get("primary", vm.eventID).execute();
			Date date = new Date(vm.cnfDate);
			Date time = new Date(vm.cnfTime);
			Date cpl = new Date(date.getYear(), date.getMonth(), date.getDay(), 
                    time.getHours(), time.getMinutes(), time.getSeconds());
			DateTime dateTime = new DateTime(cpl);
			ev.setStart(dateTime);
			Event updatedEvent = events.update("primary", ev.getId(), ev).execute();*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ok();
	}

	public static Result getManagers(Long value){
		Location loc = Location.findById(value);
		List<AuthUser> user = AuthUser.getlocationAndManager(loc);
		return ok(Json.toJson(user));
	}
	
	public static Result getLocationUsers(Long value){
		Location loc = Location.findById(value);
		List<AuthUser> user = AuthUser.getAllUserByLocation(loc);
		return ok(Json.toJson(user));
	}
	public static Result updatePlan(){
		Form<PlanScheduleVM> form = DynamicForm.form(PlanScheduleVM.class).bindFromRequest();
		PlanScheduleVM vm = form.get();
		int flag = 0;
		int flag1 = 0;
		if(vm.scheduleBy.equals("location")){
			flag = updateLocationWise(vm, vm.location);
			
			if(vm.locationList != null){
			for(Long locationid : vm.locationList){
				if(vm.location != null){
					if(locationid != vm.location){
						flag1 = updateLocationWise(vm, locationid);
					}
				}else{
					if(locationid != Long.valueOf(session("USER_LOCATION"))){
						flag1 = updateLocationWise(vm, locationid);
					}
				}
				
			}
			}
		}else if(vm.scheduleBy.equals("salePerson")){
			flag = updateSalepersonWise(vm, vm.salePerson);
			
			if(vm.salesList != null){
				for(Integer salesId : vm.salesList){
					if(!salesId.equals(vm.salePerson)){
						flag1 = updateSalepersonWise(vm, salesId);
					}
				}
				}
		}
		return ok(Json.toJson(flag));
	}
	
	public static int updateLocationWise(PlanScheduleVM vm,Long locationId){
		AuthUser user = getLocalUser();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		Location location = null;
		String[] parts = vm.startDate.split("-");
		String[] parts1 = vm.endDate.split("-");
		String startDateString = parts[2]+"-"+parts[1]+"-"+parts[0];
		String endDateString = parts1[2]+"-"+parts1[1]+"-"+parts1[0];
		int flag = 0;
		
		List<PlanSchedule> planSchedule = null;
		
		if(locationId != null){
			location = Location.findById(locationId);
			planSchedule = PlanSchedule.findAllByLocation(locationId);
		}else{
			if(session("USER_ROLE").equals("Manager")){
				location = Location.findById(Long.valueOf(session("USER_LOCATION")));
				planSchedule = PlanSchedule.findAllByLocation(Long.valueOf(session("USER_LOCATION")));
			}
		}
		
		
		
		for(PlanSchedule checkDate:planSchedule){
			if(checkDate.id != vm.id){
				try {
					if((df1.parse(startDateString).after(checkDate.getStartDate()) && df1.parse(startDateString).before(checkDate.getEndDate())) || (df1.parse(endDateString).after(checkDate.getStartDate())&& df1.parse(endDateString).before(checkDate.getEndDate()))){
						flag = 1;
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		if(flag == 0){
			PlanSchedule plSchedule = PlanSchedule.findById(vm.id);
			plSchedule.setCarsSold(vm.carsSold);
			plSchedule.setContractsSign(vm.contractsSign);
			plSchedule.setDayContract(vm.dayContract);
			try {
				plSchedule.setStartDate(df1.parse(startDateString));
				plSchedule.setEndDate(df1.parse(endDateString));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			plSchedule.setMeetingSalesAm(vm.meetingSalesAm);
			plSchedule.setMonthContract(vm.monthContract);
			plSchedule.setQuarterContract(vm.quarterContract);
			plSchedule.setSixMonthContract(vm.sixMonthContract);
			plSchedule.setTotalEarn(vm.totalEarn);
			plSchedule.setTotalMeetingAm(vm.totalMeetingAm);
			plSchedule.setWeekContract(vm.weekContract);
			plSchedule.setWorkWithClient(vm.workWithClient);
			plSchedule.setLocations(location);
			plSchedule.setUser(user);
			plSchedule.update();
		
		}
		
		return flag;
	}
	
	public static int updateSalepersonWise(PlanScheduleVM vm,int salesId){
		//AuthUser user = getLocalUser();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		String[] parts = vm.startDate.split("-");
		String[] parts1 = vm.endDate.split("-");
		String startDateString = parts[2]+"-"+parts[1]+"-"+parts[0];
		String endDateString = parts1[2]+"-"+parts1[1]+"-"+parts1[0];
		
		
		Location location = Location.findById(Long.valueOf(session("USER_LOCATION")));
		
		AuthUser user = AuthUser.findById(salesId);
		
		List<SalesPlanSchedule> planSchedule = SalesPlanSchedule.findAllByUser(user);
		
		int flag = 0;
		for(SalesPlanSchedule checkDate:planSchedule){
			
			try {
				if((df1.parse(startDateString).after(checkDate.getStartDate()) && df1.parse(startDateString).before(checkDate.getEndDate())) || (df1.parse(endDateString).after(checkDate.getStartDate())&& df1.parse(endDateString).before(checkDate.getEndDate()))){
					flag = 1;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(flag == 0){
				SalesPlanSchedule plSchedule = SalesPlanSchedule.findById(vm.id);
				plSchedule.setCarsSold(vm.carsSold);
				plSchedule.setContractsSign(vm.contractsSign);
				plSchedule.setDayContract(vm.dayContract);
				try {
					plSchedule.setStartDate(df1.parse(startDateString));
					plSchedule.setEndDate(df1.parse(endDateString));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				plSchedule.setMeetingSalesAm(vm.meetingSalesAm);
				plSchedule.setMonthContract(vm.monthContract);
				plSchedule.setQuarterContract(vm.quarterContract);
				plSchedule.setSixMonthContract(vm.sixMonthContract);
				plSchedule.setTotalEarn(vm.totalEarn);
				plSchedule.setTotalMeetingAm(vm.totalMeetingAm);
				plSchedule.setWeekContract(vm.weekContract);
				plSchedule.setWorkWithClient(vm.workWithClient);
				plSchedule.setLocations(location);
				plSchedule.setUser(user);
				plSchedule.update();
		}
		
		return flag;
	}
	
	public static Result savePlan(){
		
		Form<PlanScheduleVM> form = DynamicForm.form(PlanScheduleVM.class).bindFromRequest();
		PlanScheduleVM vm = form.get();
		int flag = 0;
		int flag1 = 0;
		if(vm.scheduleBy.equals("location")){
			flag = saveLocationWise(vm, vm.location);
			
			if(vm.locationList != null){
			for(Long locationid : vm.locationList){
				if(vm.location != null){
					if(locationid != vm.location){
						flag1 = saveLocationWise(vm, locationid);
					}
				}else{
					if(locationid != Long.valueOf(session("USER_LOCATION"))){
						flag1 = saveLocationWise(vm, locationid);
					}
				}
				
			}
			}
		}else if(vm.scheduleBy.equals("salePerson")){
			flag = saveSalepersonWise(vm, vm.salePerson);
			
			if(vm.salesList != null){
				for(Integer salesId : vm.salesList){
					if(!salesId.equals(vm.salePerson)){
						flag1 = saveSalepersonWise(vm, salesId);
					}
				}
				}
		}
		
		
		
		return ok(Json.toJson(flag));
	}
	
	public static int saveSalepersonWise(PlanScheduleVM vm,int salesId){
		//AuthUser user = getLocalUser();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		String[] parts = vm.startDate.split("-");
		String[] parts1 = vm.endDate.split("-");
		String startDateString = parts[2]+"-"+parts[1]+"-"+parts[0];
		String endDateString = parts1[2]+"-"+parts1[1]+"-"+parts1[0];
		Location location = Location.findById(Long.valueOf(session("USER_LOCATION")));
		
		AuthUser user = AuthUser.findById(salesId);
		
		List<SalesPlanSchedule> planSchedule = SalesPlanSchedule.findAllByUser(user);
		
		int flag = 0;
		for(SalesPlanSchedule checkDate:planSchedule){
			
			try {
				if((df1.parse(startDateString).after(checkDate.getStartDate()) && df1.parse(startDateString).before(checkDate.getEndDate())) || (df1.parse(endDateString).after(checkDate.getStartDate())&& df1.parse(endDateString).before(checkDate.getEndDate()))){
					flag = 1;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(flag == 0){
			SalesPlanSchedule plSchedule = new SalesPlanSchedule();
			
			plSchedule.setCarsSold(vm.carsSold);
			plSchedule.setContractsSign(vm.contractsSign);
			plSchedule.setDayContract(vm.dayContract);
			try {
				plSchedule.setStartDate(df1.parse(startDateString));
				plSchedule.setEndDate(df1.parse(endDateString));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			plSchedule.setMeetingSalesAm(vm.meetingSalesAm);
			plSchedule.setMonthContract(vm.monthContract);
			plSchedule.setQuarterContract(vm.quarterContract);
			plSchedule.setSixMonthContract(vm.sixMonthContract);
			plSchedule.setTotalEarn(vm.totalEarn);
			plSchedule.setTotalMeetingAm(vm.totalMeetingAm);
			plSchedule.setWeekContract(vm.weekContract);
			plSchedule.setWorkWithClient(vm.workWithClient);
			plSchedule.setLocations(location);
			plSchedule.setUser(user);
			plSchedule.save();
		}
		
		return flag;
	}
	
	
	public static int saveLocationWise(PlanScheduleVM vm,Long locationId){
		AuthUser user = getLocalUser();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		Location location = null;
		List<PlanSchedule> planSchedule = null;
		String[] parts = vm.startDate.split("-");
		String[] parts1 = vm.endDate.split("-");
		String startDateString = parts[2]+"-"+parts[1]+"-"+parts[0];
		String endDateString = parts1[2]+"-"+parts1[1]+"-"+parts1[0];
		if(locationId != null){
			location = Location.findById(locationId);
			planSchedule = PlanSchedule.findAllByLocation(locationId);
		}else{
			if(session("USER_ROLE").equals("Manager")){
				location = Location.findById(Long.valueOf(session("USER_LOCATION")));
				planSchedule = PlanSchedule.findAllByLocation(Long.valueOf(session("USER_LOCATION")));
			}
		}
		int flag = 0;
		for(PlanSchedule checkDate:planSchedule){
			
			try {
				if((df1.parse(startDateString).after(checkDate.getStartDate()) && df1.parse(startDateString).before(checkDate.getEndDate())) || (df1.parse(endDateString).after(checkDate.getStartDate())&& df1.parse(endDateString).before(checkDate.getEndDate()))){
					flag = 1;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(flag == 0){
			PlanSchedule plSchedule = new PlanSchedule();
			plSchedule.setCarsSold(vm.carsSold);
			plSchedule.setContractsSign(vm.contractsSign);
			plSchedule.setDayContract(vm.dayContract);
			try {
				plSchedule.setStartDate(df1.parse(startDateString));
				plSchedule.setEndDate(df1.parse(endDateString));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			plSchedule.setMeetingSalesAm(vm.meetingSalesAm);
			plSchedule.setMonthContract(vm.monthContract);
			plSchedule.setQuarterContract(vm.quarterContract);
			plSchedule.setSixMonthContract(vm.sixMonthContract);
			plSchedule.setTotalEarn(vm.totalEarn);
			plSchedule.setTotalMeetingAm(vm.totalMeetingAm);
			plSchedule.setWeekContract(vm.weekContract);
			plSchedule.setWorkWithClient(vm.workWithClient);
			plSchedule.setLocations(location);
			plSchedule.setUser(user);
			plSchedule.save();
		}
		return flag;
	}
	
	
	public static Result getsalesPlan(int salesId){
		AuthUser user = getLocalUser();
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
	    Date dateobj = new Date();
	    String stringDate = df.format(dateobj);
	    Date DateString = null;
		try {
			DateString = df.parse(stringDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Integer> intList = new ArrayList<>();
		
		List<PlanScheduleVM> pVms = new ArrayList<>();
		//List<SalesPlanSchedule> plSchedule;
		
		List<SalesPlanSchedule> plSchedule = SalesPlanSchedule.findAllByUser(AuthUser.findById(salesId));
		
		if(plSchedule != null){
			for(SalesPlanSchedule ps:plSchedule){
					PlanScheduleVM pVm = new PlanScheduleVM();
				//if(DateString.after(ps.getStartDate()) && DateString.before(ps.getEndDate())){
					pVm.carsSold = ps.carsSold;
					pVm.contractsSign = ps.contractsSign;
					pVm.dayContract = ps.dayContract;
					pVm.endDate = df.format(ps.endDate);
					pVm.startDate = df.format(ps.startDate);
					pVm.id = ps.id;
					pVm.location = ps.locations.id;
					pVm.meetingSalesAm = ps.meetingSalesAm;
					pVm.monthContract = ps.monthContract;
					pVm.quarterContract = ps.quarterContract;
					pVm.sixMonthContract = ps.sixMonthContract;
					pVm.totalEarn = ps.totalEarn;
					pVm.totalMeetingAm = ps.totalMeetingAm;
					pVm.weekContract = ps.weekContract;
					pVm.workWithClient = ps.workWithClient;
					if(session("USER_ROLE").equals("Manager")){
						List<SalesPlanSchedule> pList = SalesPlanSchedule.findAllByLocation(Long.valueOf(session("USER_LOCATION")));
						if(pList != null){
							for(SalesPlanSchedule ps1:pList){
								if(DateString.after(ps1.getStartDate()) && DateString.before(ps1.getEndDate())){
									intList.add(ps1.user.id);
								}
							}
						}
						pVm.salesList = intList;
					}
					pVms.add(pVm);
			//	}
			}
			
		}
		return ok(Json.toJson(pVms));
	}
	
	public static Result saveLocationTotal(String total,Long locationId){
		AuthUser user = getLocalUser();
		PlanLocationTotal planLocat = null;
		if(locationId == 0){
			 planLocat = PlanLocationTotal.findByLocation(Long.valueOf(session("USER_LOCATION")));
		}else{
			planLocat = PlanLocationTotal.findByLocation(locationId);
		}
		
		if(planLocat == null){
			PlanLocationTotal pTotal = new PlanLocationTotal();
			pTotal.setTotal(total);
			pTotal.setUser(user);
			if(locationId == 0){
				pTotal.setLocations(Location.findById(Long.valueOf(session("USER_LOCATION"))));
			}else{
				pTotal.setLocations(Location.findById(locationId));
			}
			
			pTotal.save();
		}else{
			planLocat.setTotal(total);
			planLocat.setUser(user);
			planLocat.update();
		}
		return ok();
	}
	
	public static Result saveSalePlan(){
		Form<SalepeopleMonthPlanVM> form = DynamicForm.form(SalepeopleMonthPlanVM.class).bindFromRequest();
		SalepeopleMonthPlanVM vm = form.get();
		AuthUser user = getLocalUser();
		
		for(Integer saleId:vm.salesList){
			
			AuthUser uAuthUser = AuthUser.findById(saleId);
			PlanScheduleMonthlySalepeople pSalePer = PlanScheduleMonthlySalepeople.findByUserMonth(uAuthUser,vm.month);
			
			if(pSalePer == null){
				PlanScheduleMonthlySalepeople planMoth = new PlanScheduleMonthlySalepeople();
				planMoth.setCell(vm.cell);
				planMoth.setEmails(vm.emails);
				planMoth.setMonth(vm.month);
				planMoth.setLeadsToGenerate(vm.leadsToGenerate);
				planMoth.setNewCustomers(vm.newCustomers);
				planMoth.setOutofSale(vm.outofSale);
				planMoth.setReturningCustomers(vm.returningCustomers);
				planMoth.setSuccessRate(vm.successRate);
				planMoth.setTestDrives(vm.testDrives);
				planMoth.setFlagMsg(1);
				planMoth.setTotalBrought(vm.totalBrought);
				planMoth.setVehicalesToSell(vm.vehicalesToSell);
				planMoth.setUser(uAuthUser);
				planMoth.setLocations(Location.findById(Long.valueOf(session("USER_LOCATION"))));
				planMoth.save();
			}else{
				pSalePer.setCell(vm.cell);
				pSalePer.setEmails(vm.emails);
				pSalePer.setMonth(vm.month);
				pSalePer.setLeadsToGenerate(vm.leadsToGenerate);
				pSalePer.setNewCustomers(vm.newCustomers);
				pSalePer.setOutofSale(vm.outofSale);
				pSalePer.setReturningCustomers(vm.returningCustomers);
				pSalePer.setSuccessRate(vm.successRate);
				pSalePer.setFlagMsg(1);
				pSalePer.setTestDrives(vm.testDrives);
				pSalePer.setTotalBrought(vm.totalBrought);
				pSalePer.setVehicalesToSell(vm.vehicalesToSell);
				pSalePer.update();
			}
			
			Map map = new HashMap();
    		map.put("email",uAuthUser.communicationemail);
    		map.put("amount", vm.totalBrought);
    		map.put("vehicleTosell", vm.vehicalesToSell);
    		map.put("leadGenerated",vm.leadsToGenerate);
    		map.put("testDrives",vm.testDrives);
    		if(vm.call != null){
    		map.put("callsMake", vm.call);
    		}
    		else{
    			map.put("callsMake", "");
    		}
    		if(vm.successRate !=null){
    			map.put("successRate", vm.successRate+"%");
    		}else{
    			map.put("successRate", "");
    		}
    		if(vm.emails !=null){
    		map.put("emailSend", vm.emails);
    		}else{
    			map.put("emailSend", "");
    		}
    		
    		if(vm.newCustomers !=null){
    		map.put("customers", vm.newCustomers);
    		}else{
    			map.put("customers", "");
    		}
    		map.put("uname",user.firstName+" "+user.lastName);
    		map.put("uphone", user.phone);
    		map.put("uemail", user.email);
    		map.put("month", vm.month.toString().toUpperCase());
    		salesPersonPlanMail(map);
			 	/*String subject = "Plan has been Assigned";
		    	 String comments = "plan for "+vm.month+" has been assigned";
		    	 sendEmail(uAuthUser.communicationemail, subject, comments);*/
		}
		
		
		
		/*else{
			pLocation.setAvgCheck(vm.avgCheck);
			pLocation.setMinEarning(vm.minEarning);
			//pLocation.setMonth(vm.month);
			pLocation.setTotalEarning(vm.totalEarning);
			pLocation.setVehiclesSell(vm.vehiclesSell);
			pLocation.setUser(user);
			//pLocation.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
			pLocation.update();
		}*/
				
		
		return ok();
	}
	
private static void salesPersonPlanMail(Map map) {
    	
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
    		try{
			message.setFrom(new InternetAddress(emailUser,emailName));
    		}
    		catch(UnsupportedEncodingException e){
    			e.printStackTrace();
    		}
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(map.get("email").toString()));
			message.setSubject("This month's plan has been added");
			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			ve.init();
		
			
	        Template t = ve.getTemplate("/public/emailTemplate/planAssigned_HTML.vm"); 
	        VelocityContext context = new VelocityContext();
	        /*String months[] = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
	       
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
			}*/
	        
	        //String monthName = months[month-1];
	        context.put("hostnameUrl", imageUrlPath);
	        context.put("siteLogo", logo.logoImagePath);
	      //  context.put("dayOfmonth", dayOfmonth);
	      //  context.put("monthName", monthName);
	        context.put("amount", map.get("amount"));
	        context.put("month", map.get("month"));
	        System.out.println(">>>>>>>>>>>>>>>>>"+map.get("month"));
	        context.put("vehicleTosell", map.get("vehicleTosell"));
	        context.put("leadGenerated", map.get("leadGenerated"));
	        context.put("callsMake", map.get("callsMake"));
	        context.put("testDrives", map.get("testDrives"));
	        context.put("successRate", map.get("successRate"));
	        context.put("emailSend", map.get("emailSend"));
	        context.put("customers", map.get("customers"));
	        context.put("name", map.get("uname"));
	        context.put("email", map.get("uemail"));
	        context.put("phone",  map.get("uphone"));
	        /*String weather= map.get("CnfDateNature").toString();
	        String arr1[] = weather.split("&");
	        String nature=arr1[0];
	        String temp=arr1[1];
	        context.put("nature",nature);
	        context.put("temp", temp);*/
	       
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
    
    
	
	
	
	public static Result getSalePerson(String month){
		List<PlanScheduleMonthlySalepeople> sMonthlySalepeoples = PlanScheduleMonthlySalepeople.findByListByMonth(month);
		return ok(Json.toJson(sMonthlySalepeoples));
	}
	
	public static Result getSaleMonthlyPlan(Integer saleId){
		List<PlanScheduleMonthlySalepeople> sMonthlySalepeoples = PlanScheduleMonthlySalepeople.findByListUser(AuthUser.findById(saleId));
		return ok(Json.toJson(sMonthlySalepeoples));
	}
	
	
	public static Result getPlanByMonthAndUserForLocation(Integer saleId,String month){
		int flagForPlan=0;
	   PlanScheduleMonthlyLocation sMonthlySalepeoples = PlanScheduleMonthlyLocation.findByUserMonth(AuthUser.findById(saleId),month);
		if(sMonthlySalepeoples == null){
			flagForPlan=1;
			
			 return ok(Json.toJson(flagForPlan));
		}
	   return ok(Json.toJson(sMonthlySalepeoples));
	}
	
	
	
	public static Result getPlanByMonthAndUser(Integer saleId,String month){
		int flagForPlan=0;
	   PlanScheduleMonthlySalepeople sMonthlySalepeoples = PlanScheduleMonthlySalepeople.findByUserMonth(AuthUser.findById(saleId),month);
		if(sMonthlySalepeoples == null){
			flagForPlan=1;
			
			 return ok(Json.toJson(flagForPlan));
		}
	   return ok(Json.toJson(sMonthlySalepeoples));
	}
	
	public static Result getlocationsMonthlyPlan(Integer userKey){
		AuthUser aUser = AuthUser.findById(userKey);
		List<PlanScheduleMonthlyLocation> pLocation = PlanScheduleMonthlyLocation.findByLocation(aUser.location.id);
		return ok(Json.toJson(pLocation));
	}
	
	public static Result saveSalesTotal(String total,Integer userkey){
		
		AuthUser user = AuthUser.findById(userkey);
		//AuthUser user = getLocalUser();
		PlanSalesTotal planLocat = PlanSalesTotal.findByUser(user);
		if(planLocat == null){
			PlanSalesTotal pTotal = new PlanSalesTotal();
			pTotal.setTotal(total);
			pTotal.setUser(user);
			pTotal.setLocations(Location.findById(user.location.id));
			pTotal.save();
		}else{
			planLocat.setTotal(total);
			planLocat.setUser(user);
			planLocat.update();
		}
		return ok();
	}
	public static Result saveLocationPlan(){
		
		Form<LocationMonthPlanVM> form = DynamicForm.form(LocationMonthPlanVM.class).bindFromRequest();
		LocationMonthPlanVM vm = form.get();
		//AuthUser user = getLocalUser();

		AuthUser user = AuthUser.findById(vm.userkey);
		
		if(user.role.equals("General Manager")){
			
			for(Long lvalue:vm.locationList){
				PlanScheduleMonthlyLocation pLocation = PlanScheduleMonthlyLocation.findByLocationAndMonth(Location.findById(lvalue),vm.month);
				
				if(pLocation == null){
					PlanScheduleMonthlyLocation planMoth = new PlanScheduleMonthlyLocation();
					planMoth.setAvgCheck(vm.avgCheck);
					planMoth.setMinEarning(vm.minEarning);
					planMoth.setMonth(vm.month);
					planMoth.setTotalEarning(vm.totalEarning);
					planMoth.setVehiclesSell(vm.vehiclesSell);
					planMoth.setLeadsToGenerate(vm.leadsToGenerate);
					planMoth.setUser(user);
					planMoth.setLocations(Location.findById(lvalue));
					planMoth.save();
				}else{
					pLocation.setAvgCheck(vm.avgCheck);
					pLocation.setMinEarning(vm.minEarning);
					pLocation.setTotalEarning(vm.totalEarning);
					pLocation.setVehiclesSell(vm.vehiclesSell);
					pLocation.leadsToGenerate = vm.leadsToGenerate;
					pLocation.setUser(user);
					pLocation.update();
				}
			}
			
		}else if(user.role.equals("Manager")){
			PlanScheduleMonthlyLocation pLocation = PlanScheduleMonthlyLocation.findByLocationAndMonth(Location.findById(user.location.id),vm.month);
			
			if(pLocation == null){
				PlanScheduleMonthlyLocation planMoth = new PlanScheduleMonthlyLocation();
				planMoth.setAvgCheck(vm.avgCheck);
				planMoth.setMinEarning(vm.minEarning);
				planMoth.setMonth(vm.month);
				planMoth.setTotalEarning(vm.totalEarning);
				planMoth.setVehiclesSell(vm.vehiclesSell);
				planMoth.setLeadsToGenerate(vm.leadsToGenerate);
				planMoth.setUser(user);
				planMoth.setLocations(Location.findById(user.location.id));
				planMoth.save();
			}else{
				pLocation.setAvgCheck(vm.avgCheck);
				pLocation.setMinEarning(vm.minEarning);
				pLocation.setTotalEarning(vm.totalEarning);
				pLocation.setVehiclesSell(vm.vehiclesSell);
				pLocation.setLeadsToGenerate(vm.leadsToGenerate);
				pLocation.setUser(user);
				pLocation.update();
			}
		}
		
		return ok();
	}
	
	
	public static Result getLocationPlan(Long locationId){
		
		List<PlanScheduleMonthlyLocation> pLocation = PlanScheduleMonthlyLocation.findByLocation(locationId);
		return ok(Json.toJson(pLocation));
	}
	
	public static Result isValidDatecheck(Long finderId, String checkDates, String scheduleBy){
		int flag = 0;
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		String[] parts = checkDates.split("-");
		String dateString = parts[2]+"-"+parts[1]+"-"+parts[0];
		
		List<PlanSchedule> planSchedule = null;
		List<SalesPlanSchedule> salesPlanSchedules = null;
		if(scheduleBy.equals("location")){
			if(finderId != 0){
				 planSchedule = PlanSchedule.findAllByLocation(finderId);
			}else{
				 planSchedule = PlanSchedule.findAllByLocation(Long.valueOf(session("USER_LOCATION")));
			}
			for(PlanSchedule checkDate:planSchedule){
				
				try {
					if(df1.parse(dateString).after(checkDate.getStartDate()) && df1.parse(dateString).before(checkDate.getEndDate())){
						flag = 1;
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else if(scheduleBy.equals("salePerson")){
			
			//if(locationId != 0){
				salesPlanSchedules = SalesPlanSchedule.findAllByUser(AuthUser.findById(finderId.intValue()));
			/*}else{
				salesPlanSchedules = SalesPlanSchedule.findAllByLocation(Long.valueOf(session("USER_LOCATION")));
			}*/
			for(SalesPlanSchedule checkDate:salesPlanSchedules){
				
				try {
					if(df1.parse(dateString).after(checkDate.getStartDate()) && df1.parse(dateString).before(checkDate.getEndDate())){
						flag = 1;
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
		
		return ok(Json.toJson(flag));
	}
	
	public static Result isValidCheckbok(Long locationId, String startDate, String endDate){
		int flag = 0;
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		String[] parts = startDate.split("-");
		String[] parts1 = endDate.split("-");
		String startDateString = parts[2]+"-"+parts[1]+"-"+parts[0];
		String endDateString = parts1[2]+"-"+parts1[1]+"-"+parts1[0];
		
		List<PlanSchedule> planSchedule = PlanSchedule.findAllByLocation(locationId);
		
		for(PlanSchedule checkDate:planSchedule){
			
			try {
				if((df1.parse(startDateString).after(checkDate.getStartDate()) && df1.parse(startDateString).before(checkDate.getEndDate())) || (df1.parse(endDateString).after(checkDate.getStartDate())&& df1.parse(endDateString).before(checkDate.getEndDate()))){
					flag = 1;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return ok(Json.toJson(flag));
	}
	
	public static Result getUserAppointment(String date, String time, String endTime){
		AuthUser authUser = getLocalUser();
		List<ScheduleTestVM> list = new ArrayList<>();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		try {
			Date cuDate = new Date();
			String cdat = df.format(cuDate);
			Date cd = df.parse(cdat);
			
			Date confirmDate = df.parse(date);
			Date confirmTime = new SimpleDateFormat("hh:mm a").parse(time);
			Date confirmEndTime = new SimpleDateFormat("hh:mm a").parse(endTime);
			AuthUser user = getLocalUser();
			
			List<ScheduleTest> testList = ScheduleTest.findAllByUserServiceTest(authUser, cd);
			List<RequestMoreInfo> requestInfo = RequestMoreInfo.findAllByUserServiceTest(authUser, confirmDate);
			List<TradeIn> tradeIn = TradeIn.findAllByUserServiceTest(authUser, confirmDate);
			for (TradeIn scheduleTest : tradeIn) {
				if(confirmDate.equals(scheduleTest.confirmDate)){
					System.out.println(scheduleTest.id);
					if((scheduleTest.confirmTime.equals(confirmTime) || scheduleTest.confirmTime.after(confirmTime)) && (scheduleTest.confirmTime.equals(confirmEndTime) || scheduleTest.confirmTime.before(confirmEndTime))){
						ScheduleTestVM vm = new ScheduleTestVM();
						vm.meetingStatus = null;
						vm.name = scheduleTest.firstName;
						vm.confDate = new SimpleDateFormat("dd-MM-yyyy").format(scheduleTest.confirmDate);
						vm.confirmTime = new SimpleDateFormat("hh:mm a").format(scheduleTest.confirmTime);
						AuthUser ass = AuthUser.findById(scheduleTest.assignedTo.getId());
						vm.fullName = ass.firstName +" "+ass.lastName;
						list.add(vm);
					}
				}
			}
			for (RequestMoreInfo scheduleTest : requestInfo) {
				if(confirmDate.equals(scheduleTest.confirmDate)){
					System.out.println(scheduleTest.id);
					if((scheduleTest.confirmTime.equals(confirmTime) || scheduleTest.confirmTime.after(confirmTime)) && (scheduleTest.confirmTime.equals(confirmEndTime) || scheduleTest.confirmTime.before(confirmEndTime))){
						ScheduleTestVM vm = new ScheduleTestVM();
						vm.meetingStatus = null;
						vm.name = scheduleTest.name;
						vm.confDate = new SimpleDateFormat("dd-MM-yyyy").format(scheduleTest.confirmDate);
						vm.confirmTime = new SimpleDateFormat("hh:mm a").format(scheduleTest.confirmTime);
						AuthUser ass = AuthUser.findById(scheduleTest.assignedTo.getId());
						vm.fullName = ass.firstName +" "+ass.lastName;
						list.add(vm);
					}
				}
			}
			for (ScheduleTest scheduleTest : testList) {
				if(scheduleTest.meetingStatus != null){
					if(confirmDate.equals(scheduleTest.confirmDate)){
						if(scheduleTest.confirmTime != null && scheduleTest.confirmEndTime != null){
							if((confirmTime.equals(scheduleTest.confirmTime)||confirmTime.equals(scheduleTest.confirmEndTime)) || (confirmTime.after(scheduleTest.confirmTime) && confirmTime.before(scheduleTest.confirmEndTime)) || (confirmEndTime.after(scheduleTest.confirmTime) && confirmEndTime.before(scheduleTest.confirmEndTime)) || (scheduleTest.confirmTime.after(confirmTime) && scheduleTest.confirmTime.before(confirmEndTime))){
								ScheduleTestVM vm = new ScheduleTestVM();
								vm.meetingStatus = scheduleTest.meetingStatus;
								vm.name = scheduleTest.name;
								vm.confDate = new SimpleDateFormat("dd-MM-yyyy").format(scheduleTest.confirmDate);
								vm.confirmTime = new SimpleDateFormat("hh:mm a").format(scheduleTest.confirmTime);
								vm.confirmEndTime = new SimpleDateFormat("hh:mm a").format(scheduleTest.confirmEndTime);
								AuthUser ass = AuthUser.findById(scheduleTest.assignedTo.getId());
								vm.fullName = ass.firstName +" "+ass.lastName;
								list.add(vm);
							}
						}else if(scheduleTest.confirmTime != null && scheduleTest.confirmEndTime == null){
							if(scheduleTest.confirmTime.equals(confirmTime) || scheduleTest.confirmTime.equals(confirmEndTime) || (scheduleTest.confirmTime.after(confirmTime) && scheduleTest.confirmTime.before(confirmEndTime))){
								ScheduleTestVM vm = new ScheduleTestVM();
								vm.meetingStatus = scheduleTest.meetingStatus;
								vm.name = scheduleTest.name;
								vm.confDate = new SimpleDateFormat("dd-MM-yyyy").format(scheduleTest.confirmDate);
								vm.confirmTime = new SimpleDateFormat("hh:mm a").format(scheduleTest.confirmTime);
								AuthUser ass = AuthUser.findById(scheduleTest.assignedTo.getId());
								vm.fullName = ass.firstName +" "+ass.lastName;
								list.add(vm);
							}
						}
					}
						/*if((confirmTime.equals(scheduleTest.confirmTime)||scheduleTest.confirmTime.after(confirmTime)) && (confirmEndTime.equals(scheduleTest.confirmTime)||scheduleTest.confirmTime.before(confirmEndTime))){
							if(scheduleTest.confirmEndTime != null){
								if((confirmTime.equals(scheduleTest.confirmEndTime)||scheduleTest.confirmEndTime.after(confirmTime)) && (confirmEndTime.equals(scheduleTest.confirmEndTime)||scheduleTest.confirmEndTime.before(confirmEndTime)) || (confirmEndTime.after(scheduleTest.confirmTime) && confirmEndTime.before(scheduleTest.confirmEndTime))){
									ScheduleTestVM vm = new ScheduleTestVM();
									vm.meetingStatus = scheduleTest.meetingStatus;
									vm.name = scheduleTest.name;
									vm.confDate = new SimpleDateFormat("dd-MM-yyyy").format(scheduleTest.confirmDate);
									vm.confirmTime = new SimpleDateFormat("hh:mm a").format(scheduleTest.confirmTime);
									vm.confirmEndTime = new SimpleDateFormat("hh:mm a").format(scheduleTest.confirmEndTime);
									AuthUser ass = AuthUser.findById(scheduleTest.assignedTo.getId());
									vm.fullName = ass.firstName +" "+ass.lastName;
									list.add(vm);
								}
							}else{
								ScheduleTestVM vm = new ScheduleTestVM();
								vm.meetingStatus = scheduleTest.meetingStatus;
								vm.name = scheduleTest.name;
								vm.confDate = new SimpleDateFormat("dd-MM-yyyy").format(scheduleTest.confirmDate);
								vm.confirmTime = new SimpleDateFormat("hh:mm a").format(scheduleTest.confirmTime);
								AuthUser ass = AuthUser.findById(scheduleTest.assignedTo.getId());
								vm.fullName = ass.firstName +" "+ass.lastName;
								list.add(vm);
							}
						}*/
					//}
				}
				else{
					if(confirmDate.equals(scheduleTest.confirmDate)){
						System.out.println(scheduleTest.id);
						if((scheduleTest.confirmTime.equals(confirmTime) || scheduleTest.confirmTime.after(confirmTime)) && (scheduleTest.confirmTime.equals(confirmEndTime) || scheduleTest.confirmTime.before(confirmEndTime))){
							ScheduleTestVM vm = new ScheduleTestVM();
							vm.meetingStatus = scheduleTest.meetingStatus;
							vm.name = scheduleTest.name;
							vm.confDate = new SimpleDateFormat("dd-MM-yyyy").format(scheduleTest.confirmDate);
							vm.confirmTime = new SimpleDateFormat("hh:mm a").format(scheduleTest.confirmTime);
							AuthUser ass = AuthUser.findById(scheduleTest.assignedTo.getId());
							vm.fullName = ass.firstName +" "+ass.lastName;
							list.add(vm);
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
		return ok(Json.toJson(list));
	}
	public static Result getUserForMeeting(String date, String time, String endTime){
		
		List<UserVM> vmList  = new ArrayList<>();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		try {			
			Date cuDate = new Date();
			String cdat = df.format(cuDate);
			Date cd = df.parse(cdat);
			
			Date confirmDate = df.parse(date);
			Date confirmTime = new SimpleDateFormat("hh:mm a").parse(time);
			Date confirmEndTime = new SimpleDateFormat("hh:mm a").parse(endTime);
			AuthUser user = getLocalUser();
			String type = null;
			String schTime = null;
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(confirmTime);
			calendar.add(Calendar.HOUR, 1);
			Date maxTime = calendar.getTime();
			
			calendar.setTime(confirmTime);
			calendar.add(Calendar.HOUR, -1);
			Date minTime = calendar.getTime();
			Location loc = Location.findById(Long.parseLong(session("USER_LOCATION")));
			List<AuthUser> userList = null;
			if(user.role.equals("Manager")){
				userList = AuthUser.findByLocatioUsersNotManager(loc);
			}else if(user.role.equals("Sales Person")){
				userList = AuthUser.findByLocatioUsersNotGM(loc, user.id);
			}
			System.out.println(userList.size());
			for (AuthUser authUser : userList) {
				Boolean flag = true;
				List<ScheduleTest> testList = ScheduleTest.findAllByUserServiceTest(authUser, cd);
				/*for (ScheduleTest scheduleTest : testList) {
					if(confirmDate.equals(scheduleTest.confirmDate)){
						if((confirmTime.equals(scheduleTest.confirmTime)||scheduleTest.confirmTime.after(confirmTime)) && (confirmEndTime.equals(scheduleTest.confirmTime)||scheduleTest.confirmTime.before(confirmEndTime)) || (confirmTime.after(scheduleTest.confirmTime) && confirmTime.before(scheduleTest.confirmEndTime))){
							if(scheduleTest.confirmEndTime != null){
								if((confirmTime.equals(scheduleTest.confirmEndTime)||scheduleTest.confirmEndTime.after(confirmTime)) && (confirmEndTime.equals(scheduleTest.confirmEndTime)||scheduleTest.confirmEndTime.before(confirmEndTime)) || (confirmEndTime.after(scheduleTest.confirmTime) && confirmEndTime.before(scheduleTest.confirmEndTime))){
									flag = false;
									break;
								}
							}else{
								flag = false;
								break;
							}
						}
					}
				}*/
				for (ScheduleTest scheduleTest : testList) {
					if(scheduleTest.meetingStatus != null){
						if(confirmDate.equals(scheduleTest.confirmDate)){
							if(scheduleTest.confirmTime != null && scheduleTest.confirmEndTime != null){
									if((confirmTime.equals(scheduleTest.confirmTime)||confirmTime.equals(scheduleTest.confirmEndTime)) || (confirmTime.after(scheduleTest.confirmTime) && confirmTime.before(scheduleTest.confirmEndTime)) || (confirmEndTime.after(scheduleTest.confirmTime) && confirmEndTime.before(scheduleTest.confirmEndTime)) || (scheduleTest.confirmTime.after(confirmTime) && scheduleTest.confirmTime.before(confirmEndTime))){
										type = "Meeting";
										schTime = new SimpleDateFormat("hh:mm a").format(scheduleTest.confirmTime)+"-"+new SimpleDateFormat("hh:mm a").format(scheduleTest.confirmEndTime);
										flag = false;
										break;
									}
							}else if(scheduleTest.confirmTime != null && scheduleTest.confirmEndTime == null){
								if(scheduleTest.confirmTime.equals(confirmTime) || scheduleTest.confirmTime.equals(confirmEndTime) || (scheduleTest.confirmTime.after(confirmTime) && scheduleTest.confirmTime.before(confirmEndTime))){
									type = "Meeting";
									schTime = new SimpleDateFormat("hh:mm a").format(scheduleTest.confirmTime);
									flag = false;
									break;
								}
							}
							
							/*if(scheduleTest.confirmTime != null){
								if((confirmTime.equals(scheduleTest.confirmTime)||scheduleTest.confirmTime.after(confirmTime)) && (confirmEndTime.equals(scheduleTest.confirmTime)||scheduleTest.confirmTime.before(confirmEndTime))){
									if(scheduleTest.confirmEndTime != null){
										if((confirmTime.equals(scheduleTest.confirmEndTime)||scheduleTest.confirmEndTime.after(confirmTime)) && (confirmEndTime.equals(scheduleTest.confirmEndTime)||scheduleTest.confirmEndTime.before(confirmEndTime)) || (confirmEndTime.after(scheduleTest.confirmTime) && confirmEndTime.before(scheduleTest.confirmEndTime))){
											type = "Meeting";
											schTime = new SimpleDateFormat("hh:mm a").format(scheduleTest.confirmTime)+"-"+new SimpleDateFormat("hh:mm a").format(scheduleTest.confirmEndTime);
											flag = false;
											break;
										}
									}else{
										type = "Meeting";
										schTime = new SimpleDateFormat("hh:mm a").format(scheduleTest.confirmTime);
										flag = false;
										break;
									}
								}
							}*/
						}
					}
					else{
						if(confirmDate.equals(scheduleTest.confirmDate)){
							if(scheduleTest.confirmTime != null){
								if((scheduleTest.confirmTime.equals(confirmTime) || scheduleTest.confirmTime.after(confirmTime)) && (scheduleTest.confirmTime.equals(confirmEndTime) || scheduleTest.confirmTime.before(confirmEndTime))){
									type = "Test Drive";
									schTime = new SimpleDateFormat("hh:mm a").format(scheduleTest.confirmTime);
									flag = false;
									break;
								}
							}
						}
					}
				}
				
				/*if((confirmTime.after(minTime) || confirmTime.equals(minTime)) && (confirmTime.before(maxTime) || confirmTime.equals(maxTime))){
					flag = false;
					break;
				}*/
				System.out.println(authUser.id);
				if(flag){
					UserVM vm = new UserVM();
					vm.id = authUser.getId();
					vm.fullName = authUser.getFirstName()+" "+authUser.getLastName();
					vm.role = authUser.getRole();
					vm.type = null;
					vm.time = null;
					vm.userStatus = "Yes";
					vm.isSelect = false;
					vmList.add(vm);
				}else{
					UserVM vm = new UserVM();
					vm.id = authUser.getId();
					vm.fullName = authUser.getFirstName()+" "+authUser.getLastName();
					vm.role = authUser.getRole();
					vm.type = type;
					vm.time = schTime;
					vm.userStatus = "N/A";
					vm.isSelect = false;
					vmList.add(vm);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ok(Json.toJson(vmList));
	}
	
	
	public static Result saveMeetingSchedule(){
		Location loc=null;
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Form<ScheduleTestVM> form = DynamicForm.form(ScheduleTestVM.class).bindFromRequest();
		ScheduleTestVM vm = form.get();
		AuthUser user = getLocalUser();
		if(vm.getLocation()!=null){
			 loc = Location.findById(Long.parseLong(vm.getLocation()));
			}
		
    	final String AB = "0123456789";
    	Random rnd = new Random();

    	   StringBuilder sb = new StringBuilder(2);
    	   for( int i = 0; i < 5; i++ ) 
    	      sb.append( AB.charAt(rnd.nextInt(AB.length()) ) );
    	
		
		List<AuthUser> userList = new ArrayList<>();
		for (UserVM obj : vm.getUsersList()) {
			AuthUser assi = AuthUser.findById(obj.id);
			ScheduleTest moTest = new ScheduleTest();
			moTest.assignedTo = assi;
			moTest.name=vm.getAssignedTo();
			moTest.bestDay = vm.getBestDay();
			moTest.bestTime = vm.getBestTime();
			moTest.email = user.getEmail();
			moTest.location = vm.getLocation();
			moTest.name = vm.name;
			moTest.groupId = Long.parseLong(sb.toString());
			moTest.meetingStatus = "meeting";
			moTest.phone = user.getPhone();
			moTest.reason = vm.getReason();
			moTest.scheduleDate = new Date();
			moTest.scheduleTime = new Date();
			moTest.user = user;
			moTest.isReassigned = false;
			moTest.sendInvitation = 1;
			moTest.acceptMeeting = 1;
			moTest.declineMeeting = 1;
			moTest.meeting = 1;
			moTest.is_google_data = false;
			userList.add(assi);
			try {			
				moTest.confirmDate = df.parse(vm.getBestDay());
				moTest.confirmTime = new SimpleDateFormat("hh:mm a").parse(vm.getBestTime());
				moTest.confirmEndTime = new SimpleDateFormat("hh:mm a").parse(vm.getBestEndTime());
			} catch (Exception e) {
				e.printStackTrace();
			}
			moTest.save();
			
			String subject = "Meeting invitation.";
			String comments = "New meeting invitation received \n "+user.firstName+" "+user.lastName+"\n"+vm.getBestDay()+" "+vm.getBestTime()+"-"+vm.getBestEndTime();
			
			//sendEmail(assi.communicationemail, subject, comments);
			//sendMeetingMailToAssignee(vm, user, userList);
		}
		  sendMeetingMailToAssignee(vm, user, userList);
		
		
		
		
		/*if(vm.getLocation()!=null){
			 loc = Location.findById(Long.parseLong(vm.getLocation()));
			}
		if(vm.allStaff.equals(false)){
				AuthUser assi = AuthUser.findById(Integer.parseInt(vm.getAssignedTo()));
				ScheduleTest moTest = new ScheduleTest();
				moTest.assignedTo = assi;
				moTest.name=vm.getAssignedTo();
				moTest.bestDay = vm.getBestDay();
				moTest.bestTime = vm.getBestTime();
				moTest.email = user.getEmail();
				moTest.location = vm.getLocation();
				moTest.name = vm.name;
				moTest.meetingStatus = "meeting";
				moTest.phone = user.getPhone();
				moTest.reason = vm.getReason();
				moTest.scheduleDate = new Date();
				moTest.user = user;
				moTest.isReassigned = false;
				moTest.is_google_data = false;
				userList.add(assi);
				try {			
					moTest.confirmDate = df.parse(vm.getBestDay());
					moTest.confirmTime = new SimpleDateFormat("hh:mm a").parse(vm.getBestTime());
				} catch (Exception e) {
					e.printStackTrace();
				}
				moTest.save();
		}else{
			List<AuthUser> userList1 = AuthUser.findByLocatioUsers(loc);
			for (AuthUser assi : userList1) {
					ScheduleTest moTest = new ScheduleTest();
					moTest.assignedTo = assi;
					moTest.name=vm.getAssignedTo();
					moTest.bestDay = vm.getBestDay();
					moTest.bestTime = vm.getBestTime();
					moTest.email = user.getEmail();
					moTest.location = vm.getLocation();
					moTest.name = vm.name;
					moTest.meetingStatus = "meeting";
					moTest.phone = user.getPhone();
					moTest.reason = vm.getReason();
					moTest.scheduleDate = new Date();
					moTest.user = user;
					moTest.isReassigned = false;
					moTest.is_google_data = false;
					userList.add(assi);
					try {			
						moTest.confirmDate = df.parse(vm.getBestDay());
						moTest.confirmTime = new SimpleDateFormat("hh:mm a").parse(vm.getBestTime());
					} catch (Exception e) {
						e.printStackTrace();
					}
					moTest.save();
			}
		}*/
		
		
		//sendMeetingMailToAssignee(vm, user, userList, loc);
		//sendMeetingMailToOrgnizer(vm, user, userList, loc);
		return ok();
	}
	
	public static void sendMeetingMailToAssignee(ScheduleTestVM vm, AuthUser user, List<AuthUser> userList){
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
		List<UserVM> list = new ArrayList<>() ;
		for(AuthUser assi : userList){
			
			UserVM vm1=new UserVM();
			vm1.fullName=assi.firstName+" "+assi.lastName;
			list.add(vm1);
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
    		}
    		catch(UnsupportedEncodingException e){
    			e.printStackTrace();
    		}
			message.setRecipients(Message.RecipientType.TO,usersArray);
			/*usersArray*/
			message.setSubject("Meeting Invitation");
			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			ve.init();
			
			Template t = ve.getTemplate("/public/emailTemplate/internalMeetingInvitation_HTML.html"); 
	        VelocityContext context = new VelocityContext();
	        
	        context.put("title", vm.name);
	       // context.put("location", loc.getName());
	        context.put("meetingBy", user.getFirstName()+" "+user.getLastName());
	        
	        String months[] = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
		       
	        int dayOfmonth=1;
	        int month=0;
	        try {
	        	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	        	String dateInString = vm.getBestDay();
	        	String arr[] = dateInString.toString().split("-");
		        if(arr.length >=2){
		        	dayOfmonth = Integer.parseInt(arr[0]);
			        month = Integer.parseInt(arr[1]);
		        }else{
		        	Date date = formatter.parse(dateInString);
		        	Calendar cal = Calendar.getInstance();
			         cal.setTime((Date)date);
			         dayOfmonth = cal.get(Calendar.DAY_OF_MONTH);
			         month = cal.get(Calendar.MONTH)+1;
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}
	        
	        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	        String dateInString = vm.getBestDay();
	        String monthName = months[month-1];
	        context.put("hostnameUrl", imageUrlPath);
	       // context.put("siteLogo", logo.logoImagePath);
	        context.put("dayOfmonth", dayOfmonth);
	        context.put("monthName", monthName);
	        //context.put("confirmTime", map.get("confirmTime"));
	        context.put("userList",list);
	        
	        context.put("date", vm.getBestDay());
	        context.put("time", vm.getBestTime());
	        context.put("disc", vm.getReason());
	       
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
	
	public static void sendMeetingMailToOrgnizer(ScheduleTestVM vm, AuthUser user, List<AuthUser> userList, Location loc){
		String meetingBy = "";
		for (AuthUser assi : userList) {
			meetingBy = meetingBy + assi.getFirstName()+" "+assi.getLastName()+" , ";
		}
		
		EmailDetails details=EmailDetails.findByLocation(Long.valueOf(session("USER_LOCATION")));
		String emailName=details.name;
		String port=details.port;
		String gmail=details.host;
		final	String emailUser=details.username;
		final	String emailPass=details.passward;
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
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
			InternetAddress.parse(user.getEmail()));
			message.setSubject("Meeting Scheduled");
			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			ve.init();
			
			Template t = ve.getTemplate("/public/emailTemplate/meetingemailtoorganizer.vm"); 
	        VelocityContext context = new VelocityContext();
	        
	        context.put("title", vm.name);
	        context.put("location", loc.getName());
	        context.put("meetingBy", meetingBy);
	        context.put("date", vm.getBestDay());
	        context.put("time", vm.getBestTime());
	        context.put("disc", vm.getReason());
	       
	        StringWriter writer = new StringWriter();
	        t.merge( context, writer );
	        String content = writer.toString();
			
			messageBodyPart.setContent(content, "text/html");
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
			Transport.send(message);
			System.out.println("email Succ");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static Result getLoginUser(){
		AuthUser user = getLocalUser();
		return ok(Json.toJson(user));
	}
	


	public static Result getPlanTarget(String type){
		
		String[] monthName = { "January", "February", "March", "April", "May", "June", "July",
		        "August", "September", "October", "November", "December" };
   	
		Calendar cal = Calendar.getInstance();  
    	String monthCal = monthName[cal.get(Calendar.MONTH)];
    	SetPriceChangeFlag sPrice = new SetPriceChangeFlag();
	 	List<PriceFormatDate> sAndValues = new ArrayList<>();
	 	PriceFormatDate pvalue = new PriceFormatDate();
		
		if(type.equalsIgnoreCase("location")){
			Location location = Location.findById(Long.valueOf(session("USER_LOCATION")));
			PlanScheduleMonthlyLocation loc = PlanScheduleMonthlyLocation.findByLocationAndMonth(location,monthCal);
			if(loc !=null){
				pvalue.price = Long.parseLong(loc.totalEarning);
				pvalue.product_sell = Long.parseLong(loc.vehiclesSell);
				pvalue.avg_check = Long.parseLong(loc.avgCheck);
			}else{
				pvalue.price = null;
			}
			//pvalue.x = 0l;
			pvalue.title = "";
			pvalue.text = "Plan "+ pvalue.price;
			sAndValues.add(pvalue);
			//sPrice.y = 100000;
			//sPrice.type = "flags";
			sPrice.data = sAndValues;
			sPrice.name = "";
			return ok(Json.toJson(sPrice));
		}else{
			AuthUser user = getLocalUser();
			PlanScheduleMonthlySalepeople obj = PlanScheduleMonthlySalepeople.findByUserMonth(user, monthCal);
			if(obj !=null){
				pvalue.price = Long.parseLong(obj.totalBrought);
				
			}else{
				pvalue.price = null;
			}
			
			pvalue.title = "";
			pvalue.text = "Plan "+ pvalue.price;
			sAndValues.add(pvalue);
			sPrice.data = sAndValues;
			sPrice.name = "";
			return ok(Json.toJson(sPrice));
		}	
	}
	
	public static Result getFinancialVehicleDetailsByBodyStyle(String startDate,String enddate){
		AuthUser user = getLocalUser();
		
		List<sendDateAndValue> sAndValues = new ArrayList<>();
		FinancialVehicleDetails(user,sAndValues,startDate,enddate);
		return ok(Json.toJson(sAndValues));
	}
	
	public static void FinancialVehicleDetails(AuthUser user, List<sendDateAndValue> sAndValues,String startD,String endD){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		List<Vehicle> vehicle = null;
		Map<String, Long> mapBodyStyle = new HashMap<String, Long>();
		//Map<Long, Long> mapdate = new HashMap<Long, Long>();
		
		Map<Long, Long> mapAlldate = new HashMap<Long, Long>();
		if(user.role.equals("General Manager")){
			vehicle = Vehicle.findBySold();
		}else if(user.role.equals("Manager")){
			vehicle = Vehicle.findByLocationAndSold(user.location.id);
		}else if(user.role.equals("Sales Person")){
			vehicle = Vehicle.findBySoldUserAndSold(user);
		}
		
		Long countvehical = 1L;
		
		for(Vehicle vhVehicle:vehicle){
			if(vhVehicle.getBodyStyle() != null){
				
				Long objectMake = mapBodyStyle.get(vhVehicle.getBodyStyle());
				if (objectMake == null) {
					mapBodyStyle.put(vhVehicle.getBodyStyle(), countvehical);
				}else{
					mapBodyStyle.put(vhVehicle.getBodyStyle(), countvehical + 1L);
				}
			}
		}
		
		for (Entry<String, Long> entry : mapBodyStyle.entrySet()) {
			Map<Long, Long> mapdate = new HashMap<Long, Long>();
			Map<Long, Long> treeMap = null;
			List<List<Long>> lonnn = new ArrayList<>();
			sendDateAndValue sValue = new sendDateAndValue();
			List<Vehicle> veList = null;
			
			if(user.role.equals("General Manager")){
				veList = Vehicle.findBySold();
			}else if(user.role.equals("Manager")){
				veList = Vehicle.findByBodyStyleAndSoldLocation(entry.getKey(), user.location.id);
			}else if(user.role.equals("Sales Person")){
				veList = Vehicle.findByBodyStyleAndSold(entry.getKey(), user);
			}
			
			Date startDate = null;
			Date endDate = null;
			
			try {
				startDate=df.parse(startD);
				endDate=df.parse(endD);
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
			sValue.name = entry.getKey();
			if(veList != null){
					
				for(Vehicle vhVehicle:veList){
					
					if((vhVehicle.getSoldDate().after(startDate) && vhVehicle.getSoldDate().before(endDate)) || vhVehicle.getSoldDate().equals(endDate) || vhVehicle.getSoldDate().equals(startDate))
					{
						Long countCar = 1L;
						Long objectDate = mapdate.get(vhVehicle.getSoldDate().getTime() + (1000 * 60 * 60 * 24));
						if (objectDate == null) {
							Long objectAllDate = mapAlldate.get(vhVehicle.getSoldDate().getTime() + (1000 * 60 * 60 * 24));
							if(objectAllDate == null){
								mapAlldate.put(vhVehicle.getSoldDate().getTime()+ (1000 * 60 * 60 * 24), 1L);
							}
							mapdate.put(vhVehicle.getSoldDate().getTime()+ (1000 * 60 * 60 * 24), countCar);
						}else{
							mapdate.put(vhVehicle.getSoldDate().getTime()+ (1000 * 60 * 60 * 24), objectDate + countCar);
						}
					}
				}
				
				for (Entry<Long, Long> entryValue : mapdate.entrySet()) {
					List<Long> value = new ArrayList<>();
					value.add(entryValue.getKey());
					value.add(entryValue.getValue()); //= {entryValue.getKey(),entryValue.getValue()};
					lonnn.add(value);
				  }
				sValue.data = lonnn;
			}
			sAndValues.add(sValue);
			
		  }
		
		for(sendDateAndValue sAndValue:sAndValues){
			for(List<Long> longs:sAndValue.data){
				int i = 0;
				for(Long long1:longs){
					if(i == 0){
						for (Entry<Long, Long> entryValue : mapAlldate.entrySet()) {
							if(!entryValue.getValue().equals(0L)){
								if(!long1.equals(entryValue.getKey())){
									mapAlldate.put(entryValue.getKey(), 1L);
								}else{
									mapAlldate.put(entryValue.getKey(), 0L);
								}
							}
							
						  }
						i++;
					}
					
				}
				
			}
			for (Entry<Long, Long> entryValue : mapAlldate.entrySet()) {
				if(entryValue.getValue().equals(1L)){
					List<Long> value = new ArrayList<>();
					value.add(entryValue.getKey());
					value.add(0L);//entryValue.getKey(),0L};
					sAndValue.data.add(value);
					
				}else{
					mapAlldate.put(entryValue.getKey(), 1L);
				}
			  }
			
		}
		
		for(sendDateAndValue sValue:sAndValues){
		
			Collections.sort(sValue.data, new Comparator<List<Long>>(){
				 @Override
		            public int compare(List<Long> o1, List<Long> o2) {
		                return o1.get(0).compareTo(o2.get(0));
		            }
				
			});
		}
		
	}
	
	
	public static Result getFinancialVehicleDetails(String startD,String endD){
		AuthUser user = getLocalUser();
		//List<List<>> sAndValues = new ArrayList<>();
		Map<String, Object> sAndValues = new HashMap<>();
		setFinancialVehicle(user,sAndValues,startD,endD);
		return ok(Json.toJson(sAndValues));
	}
	
	
	public static void setFinancialVehicle(AuthUser user,Map<String, Object> sAndValues,String startD,String endD){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat df1 = new SimpleDateFormat("dd-MMM");
		List<SoldInventory> vehicle = null;
		Map<String, Long> mapMake = new HashMap<String, Long>();
		Map<String, Long> mapForData = new HashMap<String, Long>();
		Map<Long, Long> mapAlldate = new HashMap<Long, Long>();
		List<sendDataVM> data = new ArrayList<>();
		List<Object> dates = new ArrayList<>();
		sAndValues.put("dates",dates);
		sAndValues.put("data",data);
		
		Date startDate = null;
		Date endDate = null;
		try {
			startDate=df.parse(startD);
			endDate=df.parse(endD);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(user.role.equals("General Manager")){
			vehicle = SoldInventory.findBySold();
		}else if(user.role.equals("Manager")){
			vehicle = SoldInventory.findByLocationAndSold(user.location.id);
		}else if(user.role.equals("Sales Person")){
			vehicle = SoldInventory.findBySoldUserAndSold(user);
		}
		
		Long countvehical = 1L;
		
		for(SoldInventory vhVehicle:vehicle){
			if(vhVehicle.requestMoreInfo != null){
				
				String prodname = vhVehicle.collectionId;
				Long prodId = Long.parseLong(prodname);
				if(vhVehicle.getSaveLeadTypeAs().equals("SubCollection")){
					AddCollection collName = AddCollection.findById(prodId);
					Long objectMake = mapMake.get(collName.title);
					if (objectMake == null) {
						mapMake.put(collName.title, countvehical);
						mapForData.put(collName.title+"_"+collName.id+"_"+vhVehicle.getSaveLeadTypeAs(), countvehical);
					}else{
						mapMake.put(collName.title, countvehical + 1L);
						mapForData.put(collName.title+"_"+collName.id+"_"+vhVehicle.getSaveLeadTypeAs(), countvehical + 1L);
					}
				}else if(vhVehicle.getSaveLeadTypeAs().equals("MainCollection")){
					InventorySetting collName = InventorySetting.findById(prodId);
					Long objectMake = mapMake.get(collName.collection);
					if (objectMake == null) {
						mapMake.put(collName.collection, countvehical);
						mapForData.put(collName.collection+"_"+collName.id+"_"+vhVehicle.getSaveLeadTypeAs(), countvehical);
					}else{
						mapMake.put(collName.collection, countvehical + 1L);
						mapForData.put(collName.collection+"_"+collName.id+"_"+vhVehicle.getSaveLeadTypeAs(), countvehical + 1L);
					}
				}else if(vhVehicle.getSaveLeadTypeAs().equals("Product")){
					Product collName = Product.findById(prodId);
					Long objectMake = mapMake.get(collName.primaryTitle);
					if (objectMake == null) {
						mapMake.put(collName.primaryTitle, countvehical);
						mapForData.put(collName.primaryTitle+"_"+collName.id+"_"+vhVehicle.getSaveLeadTypeAs(), countvehical);
					}else{
						mapMake.put(collName.primaryTitle, countvehical + 1L);
						mapForData.put(collName.primaryTitle+"_"+collName.id+"_"+vhVehicle.getSaveLeadTypeAs(), countvehical + 1L);
					}
				}
				
			}
		}
		
		for (Entry<String, Long> entry : mapForData.entrySet()) {
			Map<Long, Long> mapdate = new HashMap<Long, Long>();
			Map<Long, Long> treeMap = null;
			List<Long> lonnn = new ArrayList<>();
			sendDataVM sValue = new sendDataVM();
			List<SoldInventory> veList  = null;
			if(user.role.equals("General Manager")){
				veList = SoldInventory.findBySold();
			}else if(user.role.equals("Manager")){
				String[] collId = entry.getKey().split("_");
				veList = SoldInventory.findByMakeAndSoldLocation(collId[1], user.location.id);
			}else if(user.role.equals("Sales Person")){
				String[] collId = entry.getKey().split("_");
				veList = SoldInventory.findByMakeAndSold(collId[1], user);
			}
			String[] collData = entry.getKey().split("_");
			sValue.name = collData[0];
			if(veList != null){
				for(SoldInventory vhVehicle:veList){
					if((vhVehicle.getSoldDate().after(startDate) && vhVehicle.getSoldDate().before(endDate)) || vhVehicle.getSoldDate().equals(endDate) || vhVehicle.getSoldDate().equals(startDate))
					{
						Long countCar = 1L;
						Long objectDate = mapdate.get(vhVehicle.getSoldDate().getTime() + (1000 * 60 * 60 * 24));
						if (objectDate == null) {
							Long objectAllDate = mapAlldate.get(vhVehicle.getSoldDate().getTime() + (1000 * 60 * 60 * 24));
							if(objectAllDate == null){
								mapAlldate.put(vhVehicle.getSoldDate().getTime()+ (1000 * 60 * 60 * 24), 1L);
							}
							mapdate.put(vhVehicle.getSoldDate().getTime()+ (1000 * 60 * 60 * 24), countCar);
						}else{
							mapdate.put(vhVehicle.getSoldDate().getTime()+ (1000 * 60 * 60 * 24), objectDate + countCar);
						}
						
					}
				}
				Date d1 = startDate;
				Date d2 = endDate;
				while((d1.before(d2)|| d1.equals(d2))){
					//dates.add(d1.getTime() + (1000 * 60 * 60 * 24));
					Long objectDate = mapdate.get(d1.getTime() + (1000 * 60 * 60 * 24));
					if(objectDate == null){
						mapdate.put(d1.getTime() + (1000 * 60 * 60 * 24), 0l);
						lonnn.add(0l);
					}else{
						lonnn.add(objectDate);
					}
					d1 = DateUtils.addHours(d1, 24);
				}
				
				sValue.data = lonnn;
			}
			data.add(sValue);
		  }
		Date d1 = startDate;
		Date d2 = endDate;
		while((d1.before(d2)|| d1.equals(d2))){
			Date d = d1;
			try {
				String dt = df1.format(d);
				dates.add(dt);
			} catch (Exception e) {
				e.printStackTrace();
			}
			d1 = DateUtils.addHours(d1, 24);
		}
		
	}
	
	/*public static void setFinancialVehicle(AuthUser user,List<sendDateAndValue> sAndValues,String startD,String endD){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		List<Vehicle> vehicle = null;
		Map<String, Long> mapMake = new HashMap<String, Long>();
		Map<Long, Long> mapAlldate = new HashMap<Long, Long>();
		if(user.role.equals("General Manager")){
			vehicle = Vehicle.findBySold();
		}else if(user.role.equals("Manager")){
			vehicle = Vehicle.findByLocationAndSold(user.location.id);
		}else if(user.role.equals("Sales Person")){
			vehicle = Vehicle.findBySoldUserAndSold(user);
		}
		
		Long countvehical = 1L;
		
		for(Vehicle vhVehicle:vehicle){
			if(vhVehicle.getMake() != null){
				
				Long objectMake = mapMake.get(vhVehicle.getMake());
				if (objectMake == null) {
					mapMake.put(vhVehicle.getMake(), countvehical);
				}else{
					mapMake.put(vhVehicle.getMake(), countvehical + 1L);
				}
			}
		}
		
		for (Entry<String, Long> entry : mapMake.entrySet()) {
			Map<Long, Long> mapdate = new HashMap<Long, Long>();
			Map<Long, Long> treeMap = null;
			List<List<Long>> lonnn = new ArrayList<>();
			sendDateAndValue sValue = new sendDateAndValue();
			List<Vehicle> veList  = null;
			if(user.role.equals("General Manager")){
				veList = Vehicle.findBySold();
			}else if(user.role.equals("Manager")){
				veList = Vehicle.findByMakeAndSoldLocation(entry.getKey(), user.location.id);
			}else if(user.role.equals("Sales Person")){
				veList = Vehicle.findByMakeAndSold(entry.getKey(), user);
			}
			 
			Date startDate = null;
			Date endDate = null;
			try {
				startDate=df.parse(startD);
				endDate=df.parse(endD);
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			sValue.name = entry.getKey();
			if(veList != null){
					
				for(Vehicle vhVehicle:veList){
					
					Calendar c = Calendar.getInstance();
					c.setTime(vhVehicle.getSoldDate());
					c.add(Calendar.DATE, -1);
					
					String dateCheck = df.format(c.getTime());
					Date dateFomat = null;
					try {
						dateFomat = df.parse(dateCheck);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if((vhVehicle.getSoldDate().after(startDate) && vhVehicle.getSoldDate().before(endDate)) || vhVehicle.getSoldDate().equals(endDate) || vhVehicle.getSoldDate().equals(startDate))
					{
					
						Long countCar = 1L;
						Long objectDate = mapdate.get(vhVehicle.getSoldDate().getTime() + (1000 * 60 * 60 * 24));
						if (objectDate == null) {
							Long objectAllDate = mapAlldate.get(vhVehicle.getSoldDate().getTime() + (1000 * 60 * 60 * 24));
							if(objectAllDate == null){
								mapAlldate.put(vhVehicle.getSoldDate().getTime()+ (1000 * 60 * 60 * 24), 1L);
							}
							mapdate.put(vhVehicle.getSoldDate().getTime()+ (1000 * 60 * 60 * 24), countCar);
						}else{
							mapdate.put(vhVehicle.getSoldDate().getTime()+ (1000 * 60 * 60 * 24), objectDate + countCar);
						}
						
					}
				}
				Date d1 = startDate;
				Date d2 = endDate;
				while((d1.before(d2)|| d1.equals(d2))){
					Long objectDate = mapdate.get(d1.getTime() + (1000 * 60 * 60 * 24));
					if(objectDate == null){
						mapdate.put(d1.getTime() + (1000 * 60 * 60 * 24), 0l);
					}
					d1 = DateUtils.addHours(d1, 24);
				}
				
				
				for (Entry<Long, Long> entryValue : mapdate.entrySet()) {
					List<Long> value = new ArrayList<>();
					value.add(entryValue.getKey());
					value.add(entryValue.getValue()); //= {entryValue.getKey(),entryValue.getValue()};
					lonnn.add(value);
				  }
				sValue.data = lonnn;
			}
			sAndValues.add(sValue);
			
		  }
		
		
		
		
		for(sendDateAndValue sAndValue:sAndValues){
			for(List<Long> longs:sAndValue.data){
				int i = 0;
				for(Long long1:longs){
					if(i == 0){
						for (Entry<Long, Long> entryValue : mapAlldate.entrySet()) {
							if(!entryValue.getValue().equals(0L)){
								if(!long1.equals(entryValue.getKey())){
									mapAlldate.put(entryValue.getKey(), 1L);
								}else{
									mapAlldate.put(entryValue.getKey(), 0L);
								}
							}
							
						  }
						i++;
					}
					
				}
				
			}
			for (Entry<Long, Long> entryValue : mapAlldate.entrySet()) {
				if(entryValue.getValue().equals(1L)){
					List<Long> value = new ArrayList<>();
					value.add(entryValue.getKey());
					value.add(0L);//entryValue.getKey(),0L};
					sAndValue.data.add(value);
					
				}else{
					mapAlldate.put(entryValue.getKey(), 1L);
				}
			  }
			
		}
		
		for(sendDateAndValue sValue:sAndValues){
		
			Collections.sort(sValue.data, new Comparator<List<Long>>(){
				 @Override
		            public int compare(List<Long> o1, List<Long> o2) {
		                return o1.get(0).compareTo(o2.get(0));
		            }
				
			});
		}
	}
	*/
	
	
	
	public static Result getSoldVehicleDetailsAvgSale(String startD,String endD,String id){
		List<Long[]> lonnn = new ArrayList<>();
		//AuthUser user = getLocalUser();
		AuthUser user = null;
		if(!id.equals("0")){
			user = AuthUser.findById(Integer.parseInt(id));
		}else{
			user = getLocalUser();
		}
		soldSaleVolu(user,lonnn,startD,endD,id);
		return ok(Json.toJson(lonnn));
	}

	public static void soldSaleVolu(AuthUser user, List<Long[]> lonnn,String startD,String endD,String id){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		Map<Long, String> mapdate = new HashMap<Long, String>();
		Long pricevalue = 0L;
		List<AddCollection> vehicle = null;
		List<AddCollection> vehicaleNew = null;
		
		Map<Long, Long> treeMap = null;
		
		if(user.role.equals("General Manager")){
			vehicle = AddCollection.findBySold();
			vehicaleNew = AddCollection.findByNewlyArrived();
		}else if(user.role.equals("Manager")){
			if(!id.equals("0")){
				vehicle = AddCollection.findByLocationAndSold(user.location.id);
				vehicaleNew = AddCollection.findByNewArrAndLocation(user.location.id);
			}else{
				vehicle = AddCollection.findBySoldUserAndSold(user);
				vehicaleNew = AddCollection.findByUserAndNew(user);
			}
			//vehicle = AddCollection.findByLocationAndSold(user.location.id);
			//vehicaleNew = AddCollection.findByNewArrAndLocation(user.location.id);
		}else if(user.role.equals("Sales Person")){
			if(!id.equals("0")){
				vehicle = AddCollection.findByLocationAndSold(user.location.id);
				vehicaleNew = AddCollection.findByNewArrAndLocation(user.location.id);
			}else{
				vehicle = AddCollection.findBySoldUserAndSold(user);
				vehicaleNew = AddCollection.findByUserAndNew(user);
			}
		}		
		
		Date startDate = null;
		Date endDate = null;
		try {
			startDate=df.parse(startD);
			endDate=df.parse(endD);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int valueCount = 1;
			for(AddCollection vhVehicle:vehicle){
				if(vhVehicle.price != 0){
					pricevalue = (long)vhVehicle.price;
				}else{
					pricevalue = 0L;
				}
				//Calendar c = Calendar.getInstance();
				//c.setTime(vhVehicle.getSoldDate());
				//c.add(Calendar.DATE, -1);
				
				String dateCheck = df.format(vhVehicle.getSoldDate());
				Date dateFomat = null;
				try {
					dateFomat = df.parse(dateCheck);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if((dateFomat.after(startDate) && dateFomat.before(endDate)) || dateFomat.equals(endDate) || dateFomat.equals(startDate))
				{
					String objectDate = mapdate.get(dateFomat.getTime()+ (1000 * 60 * 60 * 24));
					if (objectDate == null) {
						
						mapdate.put(dateFomat.getTime()+ (1000 * 60 * 60 * 24), pricevalue+","+valueCount);
					}else{
						String arr[] = objectDate.split(",");
						mapdate.put(dateFomat.getTime()+ (1000 * 60 * 60 * 24), Integer.parseInt(arr[0])+pricevalue+","+(Integer.parseInt(arr[1]) + valueCount));
					}
					
				}
			}
			
		
			
			for (Entry<Long, String> entry : mapdate.entrySet()) {
				String arr[] = entry.getValue().split(",");
				//Long avgPic = Long.parseLong(arr[1]);
				//Integer totalDayCar = Integer.parseInt(arr[1]) + vehicaleNew.size();
				Integer totalDayCar = Integer.parseInt(arr[1]);
				Long avgPic = Long.parseLong(arr[0]) / totalDayCar.longValue();
				Long[] value = {entry.getKey(),avgPic};
				lonnn.add(value);
			  }
		
			
			Collections.sort(lonnn, new Comparator<Long[]>(){
				 @Override
		            public int compare(Long[] o1, Long[] o2) {
		                return o1[0].compareTo(o2[0]);
		            }
				
			});
	}
	
	
	/*public static Result getSoldVehicleDetailsOther(Long locationId,Integer managerId){
		AuthUser user = AuthUser.findById(managerId);
		List<Long[]> lonnn = new ArrayList<>();
		soldVehicalDetailsSaleVolu(user,lonnn);
		return ok(Json.toJson(lonnn));
	}*/
	
	public static Result getSoldVehicleDetails(String startD,String endD,String id){
		AuthUser user = null;
		if(!id.equals("0")){
			user = AuthUser.findById(Integer.parseInt(id));
		}else{
			user = getLocalUser();
		}
		List<Long[]> lonnn = new ArrayList<>();
		soldVehicalDetailsSaleVolu(user,lonnn,startD,endD,id);
		return ok(Json.toJson(lonnn));
	}
	
	public static void soldVehicalDetailsSaleVolu(AuthUser user,List<Long[]> lonnn,String startD,String endD,String id){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Map<Long, Long> mapdate = new HashMap<Long, Long>();
		Long pricevalue = 0L;
		List<SoldInventory> vehicle = null;
		Map<Long, Long> treeMap = null;
		
		if(user.role.equals("General Manager")){
			vehicle = SoldInventory.findBySold();
		}else if(user.role.equals("Manager")){
			if(!id.equals("0")){
				vehicle = SoldInventory.findBySoldUserAndSold(user);
			}else{
				vehicle = SoldInventory.findByLocationAndSold(user.location.id);
			}
			//vehicle = SoldInventory.findByLocationAndSold(user.location.id);
		}else if(user.role.equals("Sales Person")){
			if(!id.equals("0")){
				vehicle = SoldInventory.findBySoldUserAndSold(user);
				
			}else{
				vehicle = SoldInventory.findByLocationAndSold(user.location.id);
			}
		}
			for(SoldInventory vhVehicle:vehicle){
				if(vhVehicle.price != null){
					pricevalue = Long.parseLong(vhVehicle.price);
				}else{
					pricevalue = 0L;
				}
				
				//Calendar c = Calendar.getInstance();
				//c.setTime(vhVehicle.getSoldDate());
				//c.add(Calendar.DATE, -1);
				
				String dateCheck = df.format(vhVehicle.getSoldDate());
				Date dateFomat = null;
				Date startDate = null;
				Date endDate = null;
				try {
					dateFomat = df.parse(dateCheck);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					startDate=df.parse(startD);
					endDate=df.parse(endD);
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if((dateFomat.after(startDate) && dateFomat.before(endDate)) || dateFomat.equals(endDate) || dateFomat.equals(startDate))
				{
				Long objectDate = mapdate.get(dateFomat.getTime()/* + (1000 * 60 * 60 * 24)*/);
				if (objectDate == null) {
					mapdate.put(dateFomat.getTime()/*+ (1000 * 60 * 60 * 24)*/, pricevalue);
				}else{
					mapdate.put(dateFomat.getTime()/*+ (1000 * 60 * 60 * 24)*/, objectDate + pricevalue);
				}
				
				}
				
			}
			
			treeMap = new TreeMap<Long, Long>(mapdate);
			printMap(treeMap);
			
			for (Entry<Long, Long> entry : treeMap.entrySet()) {
				Long[] value = {entry.getKey(),entry.getValue()};
				lonnn.add(value);
			  }
		
	}
	
	
	
	public static void printMap(Map<Long, Long> map) {
		for (Map.Entry<Long, Long> entry : map.entrySet()) {
			System.out.println("Key : " + entry.getKey() 
                                      + " Value : " + entry.getValue());
		}
	}
	
	public static Result saveLeads(){
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		
    		Date date = new Date();
    		Calendar cal = Calendar.getInstance();  
    		AuthUser users = (AuthUser) getLocalUser();
	    	Form<LeadDateWiseVM> form = DynamicForm.form(LeadDateWiseVM.class).bindFromRequest();
	    	LeadDateWiseVM vm = form.get();
	    	int flag = 0;
	    	
	    	//List<LeadsDateWise> lDateWises = LeadsDateWise.findByLocation(Location.findById(Long.parseLong(session("USER_LOCATION"))));
	    	List<LeadsDateWise> lDateWises = LeadsDateWise.getAllVehicles(users);
	    	
	    	for(LeadsDateWise lWise:lDateWises){
	    		
	    		if(lWise.goalSetTime.equals("1 week")){
	    			cal.setTime(lWise.leadsDate);  
	    			cal.add(Calendar.DATE, 7);
	    			Date m =  cal.getTime(); 
	    			if(m.after(date)) {
	    				lWise.setLeads(vm.leads);
	    				lWise.setLeadsDate(date);
	    				lWise.setGoalSetTime(vm.goalSetTime);
	    				lWise.setUser(users);
	    				lWise.setLocations(Location.findById(users.location.id));
	    				lWise.update();
	    				flag = 1;
	    			}
	    			
	    		}else if(lWise.goalSetTime.equals("1 month")){
	    			cal.setTime(lWise.leadsDate);  
	    			cal.add(Calendar.DATE, 30);
	    			Date m =  cal.getTime(); 
	    			if(m.after(date)) {
	    				lWise.setLeads(vm.leads);
	    				lWise.setLeadsDate(date);
	    				lWise.setGoalSetTime(vm.goalSetTime);
	    				lWise.setUser(users);
	    				lWise.setLocations(Location.findById(users.location.id));
	    				lWise.update();
	    				flag = 1;
	    			}
	    		}
	    		
	    	}
	    	
	    	
	    	if(flag == 0){
		    	LeadsDateWise lDateWise = new LeadsDateWise();
		    	lDateWise.setLeads(vm.leads);
				lDateWise.setLeadsDate(date);
				lDateWise.setGoalSetTime(vm.goalSetTime);
		    	lDateWise.setUser(users);
		    	lDateWise.setLocations(Location.findById(users.location.id));
		    	lDateWise.save();
	    	}
	    	
	    	
    	}	
		
		return ok();
	}
	
	
	public static Result downloadStatusFile(){
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		String filePath = rootDir+File.separator+"CsvFile/contacts.csv";
    		File file = new File(filePath);
    		return ok(file);
    	}
	}
	
	public static Result downloadRequestMoreFile(){
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		String filePath = rootDir+File.separator+"CsvFile/Request.csv";
    		File file = new File(filePath);
    		return ok(file);
    	}
	}
	
	public static Result scheduleEmail(List<String> emailList) {
		
			for(String email : emailList) {
				
				EmailDetails details=EmailDetails.findByLocation(Long.valueOf(session("USER_LOCATION")));
				String emailName=details.name;
				String port=details.port;
				String gmail=details.host;
				final	String emailUser=details.username;
				final	String emailPass=details.passward;
				Properties props = new Properties();
		 		props.put("mail.smtp.auth", "true");
		 		props.put("mail.smtp.starttls.enable", "true");
		 		props.put("mail.smtp.host", gmail);
		 		props.put("mail.smtp.port", port);
		  
		 		Session session = Session.getInstance(props,
		 		  new javax.mail.Authenticator() {
		 			protected PasswordAuthentication getPasswordAuthentication() {
		 				return new PasswordAuthentication(emailUser, emailPass);
		 			}
		 		  });
		  
		 		try{
		 			
		 			Message feedback = new MimeMessage(session);
		  			feedback.setFrom(new InternetAddress(emailUser));
		  			feedback.setRecipients(Message.RecipientType.TO,
		  			InternetAddress.parse(email));
		  			 feedback.setSubject("Test Drive Alert");	  			
		  			 BodyPart messageBodyPart = new MimeBodyPart();	
		  	         messageBodyPart.setText("Test drive alert message from Glider Autos");	 	    
		  	         Multipart multipart = new MimeMultipart();	  	    
		  	         multipart.addBodyPart(messageBodyPart);	            
		  	         feedback.setContent(multipart);
		  		     Transport.send(feedback);
		 		   
		  			/*Message message = new MimeMessage(session);
		  			message.setFrom(new InternetAddress("glider.autos@gmail.com"));
		  			message.setRecipients(Message.RecipientType.TO,
		  			InternetAddress.parse(email));
		  			message.setSubject("Test Drive Alert");	  			
		  			Multipart multipart = new MimeMultipart();
	    			BodyPart messageBodyPart = new MimeBodyPart();
	    			messageBodyPart = new MimeBodyPart();
	    			
	    			VelocityEngine ve = new VelocityEngine();
	    			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
	    			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
	    			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
	    			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
	    			ve.init();
	    			
	    	        StringWriter writer = new StringWriter();
	    	        String content = writer.toString(); 
	    			
	    			messageBodyPart.setContent(content, "text/html");
	    			multipart.addBodyPart(messageBodyPart);
	    			message.setContent(multipart);
	    			Transport.send(message);*/
	    			System.out.println("email send");
		       		} catch (MessagingException e) {
		  			  throw new RuntimeException(e);
		  		}
		}
		return ok();
	}
	
	public static Result getScheduleTime(String productId,String comDate){
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    			List<String> timeList = new ArrayList<>();
    			try {
    				AuthUser user = getLocalUser();
    				DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
    				SimpleDateFormat time = new SimpleDateFormat("h:mm:a");
        			Date d = df1.parse(comDate);
        			List<RequestMoreInfo> moreInfo = RequestMoreInfo.findByVinDate(productId,d);
        			List<ScheduleTest> schList = ScheduleTest.findByUserDate(user,d);
        			
        			for (RequestMoreInfo info : moreInfo) {
        				timeList.add(time.format(info.confirmTime));
					}
        			
        			for (ScheduleTest info : schList) {
        				if(info.meetingStatus != null){
        					if(info.confirmEndTime != null)
        						timeList.add(time.format(info.confirmTime)+"-"+time.format(info.confirmEndTime));
        					else
        						timeList.add(time.format(info.confirmTime));
        				}
        				else{
        					timeList.add(time.format(info.confirmTime));
        				}
        			}
        			
				} catch (Exception e) {
					e.printStackTrace();
				}
    			
    		return ok(Json.toJson(timeList));
    	}
	}
	
	public static Result vehicleSoldEmail(List<String> emailList) {
		
			for(String email : emailList) {
				EmailDetails details=EmailDetails.findByLocation(Long.valueOf(session("USER_LOCATION")));
				String emailName=details.name;
				String port=details.port;
				String gmail=details.host;
				final	String emailUser=details.username;
				final	String emailPass=details.passward;
				Properties props = new Properties();
		 		props.put("mail.smtp.auth", "true");
		 		props.put("mail.smtp.starttls.enable", "true");
		 		props.put("mail.smtp.host", gmail);
		 		props.put("mail.smtp.port",port);
		  
		 		Session session = Session.getInstance(props,
		 		  new javax.mail.Authenticator() {
		 			protected PasswordAuthentication getPasswordAuthentication() {
		 				return new PasswordAuthentication(emailUser, emailPass);
		 			}
		 		  });
		  
		 		try{
		 			
		 			Message feedback = new MimeMessage(session);
		  			feedback.setFrom(new InternetAddress(emailUser));
		  			feedback.setRecipients(Message.RecipientType.TO,
		  			InternetAddress.parse(email));
		  			 feedback.setSubject("Vehicle Sold notificatio");	  			
		  			 BodyPart messageBodyPart = new MimeBodyPart();	
		  	         messageBodyPart.setText("Vehicle Sold notificatio");	 	    
		  	         Multipart multipart = new MimeMultipart();	  	    
		  	         multipart.addBodyPart(messageBodyPart);	            
		  	         feedback.setContent(multipart);
		  		     Transport.send(feedback);
	    			System.out.println("email send");
		       		} catch (MessagingException e) {
		  			  throw new RuntimeException(e);
		  		}
		}
		return ok();
	}
	
	 private static void vehicleSoldMail(List<RequestInfoVM> vinAndMailList) {
	    	
	         for(RequestInfoVM vm:vinAndMailList){
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
	    		try{
				message.setFrom(new InternetAddress(emailUser,emailName));
	    		}catch(UnsupportedEncodingException e){
	    			e.printStackTrace();
	    		}
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(vm.email.toString()));
				System.out.println(vm.email.toString());
				message.setSubject("VEHICLE SOLD CONFIRMATION");
				Multipart multipart = new MimeMultipart();
				BodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart = new MimeBodyPart();
				
				VelocityEngine ve = new VelocityEngine();
				ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
				ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
				ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
				ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
				ve.init();
			
				
		        Template t = ve.getTemplate("/public/emailTemplate/vehiclehasbeenSOLD.html"); 
		        VelocityContext context = new VelocityContext();
		        /*String months[] = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
		       
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
				}*/
		        
		       // String monthName = months[month-1];
		        context.put("hostnameUrl", imageUrlPath);
		        context.put("siteLogo", logo.logoImagePath);
		       /*context.put("dayOfmonth", dayOfmonth);
		       context.put("monthName", monthName);
		        context.put("confirmTime", map.get("confirmTime"));*/
		        
		        Vehicle vehicle = Vehicle.findByVin(vm.vin.toString());
		        context.put("year", vehicle.year);
		        context.put("make", vehicle.make);
		        context.put("model", vehicle.model);
		        context.put("price", "$"+vehicle.price);
		        context.put("stock", vehicle.stock);
		        context.put("vin", vehicle.vin);
		        context.put("mileage", vehicle.mileage);
		        /*context.put("name", map.get("uname"));
		        context.put("email", map.get("uemail"));
		        context.put("phone",  map.get("uphone"));
		        String weather= map.get("weatherValue").toString();
		        String arr1[] = weather.split("&");
		        String nature=arr1[0];
		        String temp=arr1[1];
		        context.put("nature",nature);
		        context.put("temp", temp);*/
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
	    	
	 
	    }
	    
	    
	
	
	
	
	
	
	
	
	public static Result getDataFromCrm(){
		AuthUser userObj = (AuthUser) getLocalUser();
		List<ContactsVM> contactsVMList = new ArrayList<>();
		List<Contacts> contactsList = Contacts.getAllContactsByLocation(Long.valueOf(session("USER_LOCATION")));
		//List<Contacts> contactsList = Contacts.getAllContacts();
		for (Contacts contacts : contactsList) {
			ContactsVM vm = new ContactsVM();
			String fullName=null;
			vm.setFirstName(contacts.firstName);
			vm.setLastName(contacts.lastName);
			if(!contacts.phone.equals(null) && !contacts.phone.equals("null")){
				vm.setPhone(contacts.phone);
			}
			
			if(!contacts.email.equals(null) && !contacts.email.equals("null")){
				vm.setEmail(contacts.email);
			}
			
			//if(!contacts.zip.equals(null) && !contacts.zip.equals("null")){
				vm.setZip(contacts.custZipCode);
			//}
			
			if(contacts.firstName !=null && contacts.lastName !=null){
				fullName = contacts.firstName+" "+contacts.lastName;
			}else{
				if(contacts.firstName !=null){
					fullName=contacts.firstName;
				}if(contacts.lastName !=null){
					fullName=contacts.lastName;
				}
			}
			vm.setFullName(fullName);
			contactsVMList.add(vm);
		}
		return ok(Json.toJson(contactsVMList));
	}
	
	
	
	public static Result getPdfPath(long id) {
		String found = ""; 
		//createDir(pdfRootDir,supplierCode);
		TradeIn tIn = TradeIn.findById(id);
		response().setContentType("application/pdf");
		response().setHeader("Content-Disposition", "inline; filename="+"SupplierAgreement.pdf");
		
		String PdfFile = pdfRootDir + File.separator + tIn.locations.id +File.separator+ "trade_in_pdf"+File.separator+tIn.id+File.separator+"Trade_In.pdf";
		File f = new File(PdfFile);
		
		response().setHeader("Content-Length", ((int)f.length())+"");
	    return ok(f);	
		
		
	}
	
	public static Result getUserPermission(){
		AuthUser userObj = (AuthUser) getLocalUser();
		List<Permission> permission = userObj.getPermission();
		for (Permission per : permission) {
			if(per.name.equalsIgnoreCase("Add Vehicle") || per.name.equalsIgnoreCase("Inventory")){
				return ok("true");
			}
		}
		return ok("false");
	}
	
	
	public static Result getAllPermissionData(){
		AuthUser userObj = (AuthUser) getLocalUser();
		List<PermissionVM> permisVMList = new ArrayList<>();
		List<Permission> permission = userObj.getPermission();
		for (Permission per : permission) {
			PermissionVM vm = new PermissionVM();
			vm.name = per.name;
			permisVMList.add(vm);
		}
		return ok(Json.toJson(permisVMList));
	}
	
	public static Result getTrimList(String model){
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		List<Vehicle> list = Vehicle.findByMake(model);
    		Map<String, Vehicle> mapMake = new HashMap<>();
    		List<VehicleVM> trimList = new ArrayList<>();
    		for (Vehicle vehicle : list) {
    			if(vehicle.getTrim() != null){
    				Vehicle objectMake = mapMake.get(vehicle.getTrim());
    				mapMake.put(vehicle.getTrim(), vehicle);
    			}
			}
    		for (Entry<String, Vehicle> value : mapMake.entrySet()) {
    			VehicleVM vm = new VehicleVM();
    			vm.trim = value.getKey();
    			trimList.add(vm);
    		}
    		return ok(Json.toJson(trimList));
    	}
	}	
	
	public static Result getModelList(String make){
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		List<Vehicle> list = Vehicle.findByMake(make);
    		Map<String, Vehicle> mapMake = new HashMap<>();
    		List<VehicleVM> modelList = new ArrayList<>();
    		for (Vehicle vehicle : list) {
    			if(vehicle.getModel() != null){
    				Vehicle objectMake = mapMake.get(vehicle.getModel());
    				mapMake.put(vehicle.getModel(), vehicle);
    			}
			}
    		for (Entry<String, Vehicle> value : mapMake.entrySet()) {
    			VehicleVM vm = new VehicleVM();
    			vm.model = value.getKey();
    			modelList.add(vm);
    		}
    		return ok(Json.toJson(modelList));
    	}
	}
	
	public static Result getMakeList(){
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		List<VehicleVM> makeList = new ArrayList<>();
    		List<VehicleVM> labelList = new ArrayList<>();
    		List<VehicleVM> madeInList = new ArrayList<>();
    		List<VehicleVM> exteriorColoList = new ArrayList<>();
    		List<VehicleVM> stereoList = new ArrayList<>();
    		List<VehicleVM> driveTypeList = new ArrayList<>();
    		List<VehicleVM> fuelTypeList = new ArrayList<>();
    		
    		List<Vehicle> list = Vehicle.getMakeList();
    		
    		Map<String, Vehicle> mapMake = new HashMap<>();
    		Map<String, Vehicle> mapLabel = new HashMap<>();
    		Map<String, Vehicle> mapMadeIn = new HashMap<>();
    		Map<String, Vehicle> mapExteriorColor = new HashMap<>();
    		Map<String, Vehicle> mapStereo = new HashMap<>();
    		Map<String, Vehicle> mapDriveType = new HashMap<>();
    		Map<String, Vehicle> mapFuelType = new HashMap<>();
    		
    		Map<String, Object> mapList = new HashMap<>();
    		mapList.put("make", makeList);
    		mapList.put("label", labelList);
    		mapList.put("madeIn", madeInList);
    		mapList.put("stereo", stereoList);
    		mapList.put("driveType", driveTypeList);
    		mapList.put("fuelType", fuelTypeList);
    		mapList.put("exteriorColor", exteriorColoList);
    		
    		for (Vehicle vehicle : list) {
    			if(vehicle.getMake() != null){
    				Vehicle objectMake = mapMake.get(vehicle.getMake());
    					mapMake.put(vehicle.getMake(), vehicle);
    			}
    			if(vehicle.getLabel() != null){
    				Vehicle objectMake = mapLabel.get(vehicle.getLabel());
    				mapLabel.put(vehicle.getLabel(), vehicle);
    			}
    			if(vehicle.getMadeIn() != null){
    				Vehicle objectMake = mapLabel.get(vehicle.getMadeIn());
    				mapMadeIn.put(vehicle.getMadeIn(), vehicle);
    			}
    			if(vehicle.getStereo() != null){
    				Vehicle objectMake = mapLabel.get(vehicle.getStereo());
    				mapStereo.put(vehicle.getStereo(), vehicle);
    			}
    			if(vehicle.getDrivetrain() != null){
    				Vehicle objectMake = mapLabel.get(vehicle.getDrivetrain());
    				mapDriveType.put(vehicle.getDrivetrain(), vehicle);
    			}
    			if(vehicle.getFuelType() != null){
    				Vehicle objectMake = mapLabel.get(vehicle.getFuelType());
    				mapFuelType.put(vehicle.getFuelType(), vehicle);
    			}
    			if(vehicle.getExteriorColor() != null){
    				Vehicle objectMake = mapLabel.get(vehicle.getExteriorColor());
    				mapExteriorColor.put(vehicle.getExteriorColor(), vehicle);
    			}
			}
    		
    		for (Entry<String, Vehicle> value : mapMadeIn.entrySet()) {
    			VehicleVM vm = new VehicleVM();
    			vm.madeIn = value.getKey();
    			madeInList.add(vm);
    		}
    		for (Entry<String, Vehicle> value : mapStereo.entrySet()) {
    			VehicleVM vm = new VehicleVM();
    			vm.stereo = value.getKey();
    			stereoList.add(vm);
    		}
    		for (Entry<String, Vehicle> value : mapDriveType.entrySet()) {
    			VehicleVM vm = new VehicleVM();
    			vm.drivetrain = value.getKey();
    			driveTypeList.add(vm);
    		}
    		for (Entry<String, Vehicle> value : mapFuelType.entrySet()) {
    			VehicleVM vm = new VehicleVM();
    			vm.fuelType = value.getKey();
    			fuelTypeList.add(vm);
    		}
    		for (Entry<String, Vehicle> value : mapExteriorColor.entrySet()) {
    			VehicleVM vm = new VehicleVM();
    			vm.extColor = value.getKey();
    			exteriorColoList.add(vm);
    		}
    		for (Entry<String, Vehicle> value : mapMake.entrySet()) {
    			VehicleVM vm = new VehicleVM();
    			vm.make = value.getKey();
    			makeList.add(vm);
    		}
    		for (Entry<String, Vehicle> value : mapLabel.entrySet()) {
    			VehicleVM vm = new VehicleVM();
    			vm.label = value.getKey();
    			labelList.add(vm);
    		}
    		return ok(Json.toJson(mapList));
    	}
	}
	
	public static Result updateVehiclePrice(String vin , String price){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		int flag=0;
    		Date currDate = new Date();
    		AuthUser user = getLocalUser();
    		Integer vprice = Integer.parseInt(price);
	    	Vehicle vehicle = Vehicle.findByVinAndStatus(vin);
	    	if(vehicle != null) {
	    		String databaseVal=vehicle.price.toString();
	    		String latestPrice=vprice.toString();
	    		if(!latestPrice.equals(databaseVal))
	    		{
	    				List<PriceAlert> alertList = PriceAlert.getEmailsByVin(vehicle.vin, Long.valueOf(session("USER_LOCATION")));
	    				for(PriceAlert priceAlert: alertList) {
	    					priceAlert.setSendEmail("Y");
	    					priceAlert.setOldPrice(vehicle.price);
	    					priceAlert.setCurrDate(currDate);
	    					priceAlert.update();
	    				}
	    				Date crDate = new Date();
	    				PriceChange change = new PriceChange();
	    				change.dateTime = crDate;
	    				change.price = price;
	    				change.person = user.firstName +" "+user.lastName;
	    				change.vin = vin;
	    				change.save();
	    				flag=1;
	    			//	sendPriceAlertMail(vehicle.vin);
	    		}
		    	vehicle.setPrice(vprice);
		    	
		    	vehicle.update();
		    	if(flag==1){
		    		sendPriceAlertMail(vehicle.vin);
		    	}
		    	//sendPriceAlertMail(vehicle.vin);
	    	}
	    	return ok();
    	}	
    } 
	
	public static Result updateVehicleName(String vin , String name){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		Long id = Long.parseLong(vin) ;
    		
    		AuthUser user = getLocalUser();
	    	AddCollection vehicle = AddCollection.findById(id);
	    	if(vehicle != null) {
		    	vehicle.setTitle(name);
		    	vehicle.update();
	    	}
	    	return ok();
    	}	
    }
	
    public static Result setProductName(String vin , String name){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		Long id = Long.parseLong(vin) ;
    		
    		AuthUser user = getLocalUser();
	    	Product pro = Product.findById(id);
	    	if(pro != null) {
		    	pro.setPrimaryTitle(name);
		    	pro.update();
	    	}
	    	return ok();
    	}	
    }
    
	public static Result updateUserComment(Integer id , String comment){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser user = getLocalUser();
	    	AuthUser userObj = AuthUser.findById(id);
	    	/*Comments comm = Comments.getByUser(userObj);
	    	if(comm != null) {
	    		comm.setComment(comment);
	    		comm.update();
	    	}else{*/
	    	Date currDate = new Date();
	    		Comments cm = new Comments();
	    		cm.comment = comment;
	    		cm.likeDate = currDate;
	    		cm.user = userObj;
	    		cm.commentUser = user;
	    		cm.commentFlag = 1;
	    		cm.save();
	    	//}
	    		String subject=null;
	    		int flag=0;
	    		if(user.role.equals("General Manager")){
	    			subject = "General Manager likes your work!";
	    			flag=1;
	    		}
	    		else{
	    			subject = "Manager likes your work!";
	    			flag=0;
	    		}
	    	String comments = "Comment : "+comment;
	    	managerLikeWork(userObj.communicationemail, subject, comments,flag);
	    	return ok();
    	}	
    }
	
	private static void managerLikeWork(String email,String subject,String comments,Integer flag) {
    	
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
			Message message = new MimeMessage(session);
    		try{
			message.setFrom(new InternetAddress(emailUser,emailName));
    		}
    		catch(UnsupportedEncodingException e){
    			e.printStackTrace();
    		}
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(email));
			message.setSubject(subject);
			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			ve.init();
		
			
	        Template t = ve.getTemplate("/public/emailTemplate/managerLikesyourWork_HTML.vm"); 
	        VelocityContext context = new VelocityContext();
	        context.put("comments",comments);
	        context.put("flag",flag);
	        context.put("hostnameUrl", imageUrlPath);
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
    
	public static Result getSendDemoLink(Long userId){
		
		Registration regi = Registration.findById(userId);
		String subject = "Manager like your work";
    	String comments = "Comment : ";
    	sendEmail(regi.email, subject, comments);
		return ok();
	}
	
	public static Result sendEmail(String email, String subject ,String comment) {
		
		final String username = emailUsername;
		final String password = emailPassword;
		
		
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
		System.out.println(email);
		System.out.println(username);
		System.out.println(password);
		
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(emailUser, emailPass);
			}
		});
		    try {  
		     MimeMessage message = new MimeMessage(session);  
	    		try{
		     message.setFrom(new InternetAddress(emailUser,emailName));  
	    		}
	    		catch(UnsupportedEncodingException e){
	    			e.printStackTrace();
	    		}
		     message.addRecipient(Message.RecipientType.TO,new InternetAddress(email));  
		     message.setSubject(subject);  
		     message.setText(comment);  
		     Transport.send(message);  
		  
		     System.out.println("message sent successfully...");  
		   
		     } catch (MessagingException e) {
		    	 e.printStackTrace();
		    }
				
		return ok();
	}
	
public static Result sendEmailAfterDay(String email, String subject ,String comment,String vin,Date confirmDate,Date confirmTime,AuthUser user,String clientName) {
		final String username = emailUsername;
		final String password = emailPassword;
		
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
    		usersArray[1] = new InternetAddress(map.get("custEmail").toString());
    		*/
			Message message = new MimeMessage(session);
    		try{
			message.setFrom(new InternetAddress(emailUser,emailName));
    		}
    		catch(UnsupportedEncodingException e){
    			e.printStackTrace();
    		}
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(email));
			message.setSubject(subject);
			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			ve.init();
		
		    Template t = ve.getTemplate("/public/emailTemplate/testDriveReminder48hours_HTML.vm"); 
	        VelocityContext context = new VelocityContext();
	        String months[] = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
	       
	        int dayOfmonth=1;
	        int month=0;
	        try {
	        	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
 	        	String dateInString = formatter.format(confirmDate); /*confirmDate*/
	        	String arr[] = dateInString.toString().split("-");
		        if(arr.length >=2){
		        	dayOfmonth = Integer.parseInt(arr[0]);
			        month = Integer.parseInt(arr[1]);
		        }else{
		        	Calendar cal = Calendar.getInstance();
			         cal.setTime(confirmDate);
			         dayOfmonth = cal.get(Calendar.DAY_OF_MONTH);
			         month = cal.get(Calendar.MONTH)+1;
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}
	        
	        String monthName = months[month-1];
	        context.put("hostnameUrl", imageUrlPath);
	        context.put("dayOfmonth", dayOfmonth);
	        context.put("monthName", monthName);
	        
	        
	        SimpleDateFormat time = new SimpleDateFormat("hh:mm a");
			String time1=time.format(confirmTime);
			context.put("confirmTime",time1);
	        Vehicle vehicle = Vehicle.findByVin(vin.toString());
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
             if(clientName != null){
    	        	
              	  context.put("clientName", clientName);
    	        	 
    	        }
    	        else{
    	        	 context.put("clientName","");
    	        }
            context.put("typeofVehicle", vehicle.typeofVehicle);
	        context.put("name",user.firstName+" "+user.lastName);
	        context.put("email", user.email);
	        context.put("phone", user.phone);
	        /*String weather= map.get("weatherValue").toString();
	        String arr1[] = weather.split("&");
	        String nature=arr1[0];
	        String temp=arr1[1];
	        context.put("nature",nature);
	        context.put("temp", temp);*/
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
    
		return ok();
	}
	
	
	/*public static Result sendEmail(final String email, final String subject ,final String comment) {
		ActorSystem newsLetter = Akka.system();
		newsLetter.scheduler().scheduleOnce(Duration.create(0, TimeUnit.MILLISECONDS), 
				new Runnable() {
			public void run() {
				Properties props = new Properties();
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.host", "smtp.gmail.com");
				props.put("mail.smtp.port", "587");
				props.put("mail.smtp.starttls.enable", "true");
				System.out.println(email);
				Session session = Session.getInstance(props, new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(emailUsername, emailPassword);
					}
				});
				Properties props = new Properties();
		 		props.put("mail.smtp.auth", "true");
		 		props.put("mail.smtp.starttls.enable", "true");
		 		props.put("mail.smtp.host", "smtp.gmail.com");
		 		props.put("mail.smtp.port", "587");
		 		Session session = Session.getInstance(props,
		 		  new javax.mail.Authenticator() {
		 			protected PasswordAuthentication getPasswordAuthentication() {
		 				return new PasswordAuthentication(emailUsername, emailPassword);
		 			}
		 		  });
		  
		 		try{
		 			
		 			Message feedback = new MimeMessage(session);
		  			feedback.setFrom(new InternetAddress("glider.autos@gmail.com"));
		  			feedback.setRecipients(Message.RecipientType.TO,
		  			InternetAddress.parse(email));
		  			 feedback.setSubject(subject);	  			
		  			 BodyPart messageBodyPart = new MimeBodyPart();	
		  	         messageBodyPart.setText(comment);	 	    
		  	         Multipart multipart = new MimeMultipart();	  	    
		  	         multipart.addBodyPart(messageBodyPart);	            
		  	         feedback.setContent(multipart);
		  		     Transport.send(feedback);
	    			System.out.println("email send");
		       		} catch (MessagingException e) {
		  			  throw new RuntimeException(e);
		  		}
			}}, newsLetter.dispatcher());
				
		return ok();
	}*/
	
	public static Result getAcceptAndDecline(Long id,String reason,String status){
		
		ScheduleTest stTest = ScheduleTest.findById(id);
		if(stTest != null){
			if(status.equals("accept")){
				
				SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		    	SimpleDateFormat time = new SimpleDateFormat("hh:mm a");
		    	Date date=new Date();
		    	String date1=df.format(stTest.confirmDate);
				String time1=time.format(stTest.confirmTime);
				stTest.setMeetingActionTime(date);
				stTest.setAcceptMeeting(2);
				stTest.setMeeting(0);
				stTest.setMeetingAcceptFlag(1);
				stTest.update();
				AuthUser userEmail = AuthUser.findById(stTest.user.id);
				AuthUser assigEmail = AuthUser.findById(stTest.assignedTo.id);
				String subject = assigEmail.firstName+"  "+assigEmail.lastName+" accepted your invitation.";
				String comments = "Your invitation to "+assigEmail.firstName+" "+assigEmail.lastName+" has been accepted \n "+date1+" "+time1+" "+stTest.name;
				
		    	
				
				
				sendEmail(userEmail.communicationemail, subject, comments);
			}
			if(status.equals("decline")){
				
				SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		    	SimpleDateFormat time = new SimpleDateFormat("hh:mm a");
		    	Date date=new Date();
		    	String date1=df.format(stTest.confirmDate);
				String time1=time.format(stTest.confirmTime);
				
				
				
				stTest.setMeetingActionTime(date);
					stTest.setDeclineMeeting(2);
				stTest.setMeeting(2);
				stTest.setMeetingAcceptFlag(1);
				stTest.setDeclineReason(reason);
				//stTest.setReason(reason);
				stTest.update();
				AuthUser userEmail = AuthUser.findById(stTest.user.id);
				AuthUser assigEmail = AuthUser.findById(stTest.assignedTo.id);
				String subject = assigEmail.firstName+"  "+assigEmail.lastName+" declined your Meeting Invitation.";
				String fullName=assigEmail.firstName+"  "+assigEmail.lastName;
				String comments = "Your Meeting Invitation to "+assigEmail.firstName+" "+assigEmail.lastName+" has been declined \n Reason : "+reason+"\n "+date1+" "+time1+" "+stTest.name;
				
		    	
				
				
				declineMeetingMail(userEmail.communicationemail,date1,time1,fullName);
			}
		}
		
		return ok();
		
	}
	
	

	public static void declineMeetingMail(String email,String confirmDate,String confirmTime,String hostName){
		/*InternetAddress[] usersArray = new InternetAddress[userList.size()];
		int index = 0;
		for (AuthUser assi : userList) {
			try {
				
				usersArray[index] = new InternetAddress(assi.getCommunicationemail());
				index++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
		/*List<UserVM> list = new ArrayList<>() ;
		for(AuthUser assi : userList){
			
			UserVM vm1=new UserVM();
			vm1.fullName=assi.firstName+" "+assi.lastName;
			list.add(vm1);
			
			
			
		}*/
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
			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(email));
			/*usersArray*/
			message.setSubject("Meeting Declined");
			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			ve.init();
			
			Template t = ve.getTemplate("/public/emailTemplate/meetingInvitationDeclined.html"); 
	        VelocityContext context = new VelocityContext();
	        
	        //context.put("title", vm.name);
	       // context.put("location", loc.getName());
	        //context.put("meetingBy", user.getFirstName()+" "+user.getLastName());
	        
	        String months[] = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
		       
	        int dayOfmonth=1;
	        int month=0;
	        try {
	        	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	        	String dateInString = confirmDate;
	        	String arr[] = dateInString.toString().split("-");
		        if(arr.length >=2){
		        	dayOfmonth = Integer.parseInt(arr[0]);
			        month = Integer.parseInt(arr[1]);
		        }else{
		        	Date date = formatter.parse(dateInString);
		        	Calendar cal = Calendar.getInstance();
			         cal.setTime((Date)date);
			         dayOfmonth = cal.get(Calendar.DAY_OF_MONTH);
			         month = cal.get(Calendar.MONTH)+1;
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}
	        String monthName = months[month-1];
	        context.put("hostnameUrl", imageUrlPath);
	       // context.put("siteLogo", logo.logoImagePath);
	        context.put("dayOfmonth", dayOfmonth);
	        context.put("monthName", monthName);
	        //context.put("confirmTime", map.get("confirmTime"));
	        //context.put("userList",list);
	        
	      //  context.put("date", vm.getBestDay());
	        context.put("time", confirmTime);
	        context.put("hostName", hostName);
	        //context.put("disc", vm.getReason());
	       
	        StringWriter writer = new StringWriter();
	        t.merge( context, writer );
	        String content = writer.toString();
			
			messageBodyPart.setContent(content, "text/html");
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
			Transport.send(message);
			System.out.println("email Succ");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	
	
	
	
	
	
	
	
	public static Result getinvitationMsg(){
		 AuthUser user = getLocalUser();
	        
	        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
	        Date currD = new Date();
	        String cDate = df.format(currD);
	        Date datec = null;
	        try {
				datec = df.parse(cDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
		List<ScheduleTest> list = ScheduleTest.findAllByInvitationTest(user, datec);
		
		List<RequestInfoVM> checkData = new ArrayList<>();
		for(ScheduleTest sche:list){
			
			RequestInfoVM sTestVM = new RequestInfoVM();
        	
        	
			sTestVM.id = sche.id;
        	sTestVM.confirmDate = new SimpleDateFormat("MM-dd-yyyy").format(sche.confirmDate);
        	sTestVM.confirmTime = new SimpleDateFormat("hh:mm a").format(sche.confirmTime);
        	sTestVM.confirmDateOrderBy = sche.confirmDate;
        	sTestVM.typeOfLead = "Schedule Test Drive";
        	sTestVM.name = sche.name;
    		sTestVM.phone = sche.phone;
    		sTestVM.email = sche.email;
    		
    		AuthUser user2 = AuthUser.findById(sche.user.id);
    		if(user2.imageUrl != null) {
				if(user2.imageName !=null){
					sTestVM.imageUrl = "http://glider-autos.com/MavenImg/images"+user2.imageUrl;
				}else{
					sTestVM.imageUrl = user2.imageUrl;
				}
				
			} else {
				sTestVM.imageUrl = "/profile-pic.jpg";
			}
    		
    		checkData.add(sTestVM);
    		
			sche.setSendInvitation(0);
			sche.update();
		}
		
		return ok(Json.toJson(checkData));
		
	}
	
	public static Result getcommentLike(){
		AuthUser user = (AuthUser) getLocalUser();
		
		List<UserVM> listU = new ArrayList<>();
		List<Comments> comments = Comments.getByListUserWithFlag(user);
		for(Comments comm:comments){
			UserVM uVm = new UserVM();
			uVm.firstName = comm.commentUser.getFirstName();
			uVm.lastName = comm.commentUser.getLastName();
			uVm.id = comm.commentUser.id;
			uVm.userComment=comm.comment;
			if(comm.commentUser.imageUrl != null) {
				if(comm.commentUser.imageName !=null){
					uVm.imageUrl = "http://glider-autos.com/MavenImg/images"+comm.commentUser.imageUrl;
				}else{
					uVm.imageUrl = comm.commentUser.imageUrl;
				}
				
			} else {
				uVm.imageUrl = "/profile-pic.jpg";
			}
			
			listU.add(uVm);
			
			comm.setCommentFlag(0);
			comm.update();
			
		}
		
		return ok(Json.toJson(listU));
	}
	
    public static Result getPlanMonday(){
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		
		int flag = 0;

		String cDate = df.format(date);
		Date cuD = null;
		try {
			cuD = df.parse(cDate);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Calendar c = Calendar.getInstance();
		System.out.println("Current Month is : " + (c.get(Calendar.MONTH) + 1));
		String DateString = 01 + "-" + (c.get(Calendar.MONTH) + 1) + "-"+ c.get(Calendar.YEAR);
		Date dates = null;
		try {
			dates = df.parse(DateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date maxTime = null;
		for (int i = 0; i < 8; i++) {
			maxTime = dates;
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(maxTime);
			calendar.add(Calendar.DATE, 1);
			dates = calendar.getTime();
			if (calendar.get(Calendar.DAY_OF_WEEK) == 2) {
				if (cuD.equals(dates)) {
					flag = 1;
				}
			}

		}
    
		return ok(Json.toJson(flag));
    }
    
    public static Result getdecline(){
    
    	AuthUser users = getLocalUser();
    	List<ScheduleTest> sche = ScheduleTest.getdecline(users);
    	for(ScheduleTest sch:sche){
    		sch.setDeclineMeeting(0);
    		sch.update();
    	}
    	
    	return ok(Json.toJson(sche));
    }
    
    public static Result getPlanMsg(){
    	AuthUser users = getLocalUser();
    	List<PlanScheduleMonthlySalepeople> salepeople = PlanScheduleMonthlySalepeople.findByAllMsgPlan(users);
    	for(PlanScheduleMonthlySalepeople sales:salepeople){
    		sales.setFlagMsg(0);
    		sales.update();
    		
    	}
    	
    	return ok(Json.toJson(salepeople));
    }
    
    
    public static Result getdeleteMeeting(){
    	
    	SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat parseTime = new SimpleDateFormat("hh:mm a");
    	AuthUser users = getLocalUser();
    	List<ScheduleTest> sche = ScheduleTest.getdeleteMsg(users);
    	
    	
    	List<ScheduleTestVM> list = new ArrayList<ScheduleTestVM>();
    	for(ScheduleTest sch:sche){
    		if(sch.declineUser.equals("Host")){
    			if(!users.id.equals(sch.user.id)){
    				
    				sch.setDeleteMsgFlag(0);
    	    		sch.update();
    				
    				ScheduleTestVM sLVm = new ScheduleTestVM();
    	    		sLVm.name = sch.name;
    	    		sLVm.reason = sch.reason;
    	    		sLVm.confirmDate = df.format(sch.confirmDate);
    	    		sLVm.confirmTime = parseTime.format(sch.confirmTime);
    	    		AuthUser usersData = AuthUser.findById(sch.assignedTo.id);
    	    		sLVm.firstName = usersData.firstName;
    	    		sLVm.lastName = usersData.lastName;
    	    		sLVm.declineUser = sch.declineUser;
    	    		list.add(sLVm);
    			}
    		}else if(sch.declineUser.equals("this person")){
    			
    			if(!users.id.equals(sch.assignedTo.id)){
    				sch.setDeleteMsgFlag(0);
            		sch.update();
        			
        			ScheduleTestVM sLVm = new ScheduleTestVM();
    	    		sLVm.name = sch.name;
    	    		sLVm.reason = sch.reason;
    	    		sLVm.confirmDate = df.format(sch.confirmDate);
    	    		sLVm.confirmTime = parseTime.format(sch.confirmTime);
    	    		AuthUser usersData = AuthUser.findById(sch.assignedTo.id);
    	    		sLVm.firstName = usersData.firstName;
    	    		sLVm.lastName = usersData.lastName;
    	    		sLVm.declineUser = sch.declineUser;
    	    		list.add(sLVm);
    			}
    		}
    		
    	}
    	
    	
    	return ok(Json.toJson(list));
    }
    
    public static Result getUpdateMeeting(){
    	
    	AuthUser users = getLocalUser();
    	List<ScheduleTest> sche = ScheduleTest.getUpdateMeeting(users);
    	List<ScheduleTestVM> list = new ArrayList<>();
    	for(ScheduleTest sch:sche){
    		ScheduleTestVM vm = new ScheduleTestVM();
    		vm.confirmTime = new SimpleDateFormat("hh:mm a").format(sch.confirmTime);
    		vm.confirmEndTime = new SimpleDateFormat("hh:mm a").format(sch.confirmEndTime);
    		vm.confirmDate = new SimpleDateFormat("MM-dd-yyyy").format(sch.confirmDate);
    		vm.name = sch.name;
    		vm.reason = sch.reason;
    		list.add(vm);
    		sch.setDeclineUpdate(0);
    		sch.update();
    	}
    	
    	return ok(Json.toJson(list));
    }
    
    public static Result getaccepted(){
    	
    	AuthUser users = getLocalUser();
    	List<ScheduleTest> sche = ScheduleTest.getaccepted(users);
    	for(ScheduleTest sch:sche){
    		sch.setAcceptMeeting(0);
    		sch.update();
    	}
    	
    	return ok(Json.toJson(sche));
    }
    
    public static Result deleteAppointById(Long id,String typeOfLead,String reason){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		
    		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    		SimpleDateFormat parseTime = new SimpleDateFormat("hh:mm a");
    		AuthUser users = getLocalUser();
    		String clientEmail = null;
    		String comments = null;
    		String subject = null;
    		if(typeOfLead.equals("Schedule Test Drive") || typeOfLead.equals("Meeting")){
    			ScheduleTest test = ScheduleTest.findById(id);
        		
        		if(test !=null){
        			if(test.groupId != null){
        				
        				subject ="Meeting has been canceled";
        				if(test.user.id.equals(users.id)){
        					String uEmail = null;
        					List<ScheduleTest> grouptest = ScheduleTest.findAllGroupMeeting(test.groupId);
        					for(ScheduleTest sche:grouptest){
        						
        						AuthUser userEmail = AuthUser.findById(sche.user.id);
        						/*//sche.setConfirmDate(null);
        						//sche.setConfirmTime(null);
        						sche.setLeadStatus(null);
        						//sche.setDeclineMeeting(2);
        						sche.setMeeting(2);
        						sche.setDeleteMsgFlag(1);
        						sche.setReason(reason);
        						sche.setDeclineUser("Host");
        						sche.update();*/
        						uEmail = userEmail.communicationemail;
        						
        						AuthUser userNames = AuthUser.findById(sche.assignedTo.id);
        						
        						comments= userNames.firstName+" "+userNames.lastName+" can't go to the "+sche.name+" \n"+df.format(sche.confirmDate)+"  "+parseTime.format(sche.confirmTime)+"\n"+sche.reason+".";
        						
        						meetingCancelMail(userNames.communicationemail,sche.confirmDate,sche.confirmTime);
        						sche.delete();
        					}
        					sendEmail(uEmail,subject,comments);
        				}else{
        					
        					ScheduleTest oneGrouptest = ScheduleTest.findAllGroupUserMeeting(test.groupId, users);
        					
        					if(oneGrouptest != null){
        						AuthUser userEmail = AuthUser.findById(oneGrouptest.user.id);
        						/*//oneGrouptest.setConfirmDate(null);
            					//oneGrouptest.setConfirmTime(null);
            					oneGrouptest.setLeadStatus(null);
            					//oneGrouptest.setDeclineMeeting(2);
            					oneGrouptest.setDeleteMsgFlag(1);
            					oneGrouptest.setMeeting(2);
            					oneGrouptest.setReason(reason);
            					oneGrouptest.setDeclineUser("this person");
            					oneGrouptest.update();*/
            					
            					AuthUser userNames = AuthUser.findById(oneGrouptest.assignedTo.id);
        						comments= userNames.firstName+" "+userNames.lastName+" can't go to the "+oneGrouptest.name+" \n"+df.format(oneGrouptest.confirmDate)+"  "+parseTime.format(oneGrouptest.confirmTime)+"\n"+oneGrouptest.reason+".";
        						sendEmail(userEmail.communicationemail,subject,comments);
        						sendEmail(userNames.communicationemail,subject,comments);
        						oneGrouptest.delete();
        					}
        					
        				}
        				
        			}else{
        				
        				test.setConfirmDate(null);
        				test.setConfirmTime(null);
        				test.setLeadStatus(null);
        				test.setDeclineMeeting(0);
        				test.update();
        				subject ="Test Drive has been canceled";
        				comments="Test Drive has been canceled";
        				sendEmail(test.email,subject,comments);
        				//test.delete();
        			}
        			
        			
        		}
    		}else if(typeOfLead.equals("Request More Info")){
    			
    			
    			RequestMoreInfo rInfo = RequestMoreInfo.findById(id);
    			if(rInfo != null){
    				rInfo.setConfirmDate(null);
        			rInfo.setConfirmTime(null);
        			rInfo.setStatus(null);
        			rInfo.update();
        			subject ="Test Drive has been canceled";
    				comments="Test Drive has been canceled";
    				sendEmail(rInfo.email,subject,comments);
    			}
    			
    		}else if(typeOfLead.equals("Trade-In Appraisal")){
    			TradeIn tIn = TradeIn.findById(id);
    			if(tIn != null){
    				tIn.setConfirmDate(null);
        			tIn.setConfirmTime(null);
        			tIn.setStatus(null);
        			tIn.update();
        			subject ="Test Drive has been canceled";
    				comments="Test Drive has been canceled";
    				sendEmail(tIn.email,subject,comments);
    			}
    		}
    		
    		
        	return ok();
    	}
    }
    
    
    public static void meetingCancelMail(String communicationMail,Date confirmDate,Date confirmTime){
		/*InternetAddress[] usersArray = new InternetAddress[userList.size()];
		int index = 0;
		for (AuthUser assi : userList) {
			try {
				
				usersArray[index] = new InternetAddress(assi.getCommunicationemail());
				index++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
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
    		}
    		catch(UnsupportedEncodingException e)
    		{
    			e.printStackTrace();
    		}
    			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(communicationMail));
			/*usersArray*/
			message.setSubject("Meeting Cancelled");
			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			ve.init();
			
			Template t = ve.getTemplate("/public/emailTemplate/internalMeetingCANCELED_HTML.html"); 
	        VelocityContext context = new VelocityContext();
	        
	        //context.put("title", vm.name);
	       // context.put("location", loc.getName());
	       // context.put("meetingBy", user.getFirstName()+" "+user.getLastName());
	        
	        String months[] = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
		       
	        int dayOfmonth=1;
	        int month=0;
	        try {
	        	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	        	String dateInString = formatter.format(confirmDate);
	        	String arr[] = dateInString.toString().split("-");
		        if(arr.length >=2){
		        	dayOfmonth = Integer.parseInt(arr[0]);
			        month = Integer.parseInt(arr[1]);
		        }else{
		        	Date date =confirmDate;
		        	Calendar cal = Calendar.getInstance();
			         cal.setTime((Date)date);
			         dayOfmonth = cal.get(Calendar.DAY_OF_MONTH);
			         month = cal.get(Calendar.MONTH)+1;
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}
	        
	        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
//	String dateInString = vm.getBestDay();

	
	        
	        
	        String monthName = months[month-1];
	        context.put("hostnameUrl", imageUrlPath);
	       // context.put("siteLogo", logo.logoImagePath);
	        context.put("dayOfmonth", dayOfmonth);
	        context.put("monthName", monthName);
	        //context.put("confirmTime", map.get("confirmTime"));
	    //    context.put("userList",list);
	        
	       // context.put("date", vm.getBestDay());
	        
	        SimpleDateFormat localDateFormat = new SimpleDateFormat("hh:mm:aa");
	        String time = localDateFormat.format(confirmTime);
	        
	        context.put("time", time);
	        //context.put("disc", vm.getReason());
	       
	        StringWriter writer = new StringWriter();
	        t.merge( context, writer );
	        String content = writer.toString();
			
			messageBodyPart.setContent(content, "text/html");
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
			Transport.send(message);
			System.out.println("email Succ");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
    
    
    public static Result downloadMoreFile(String leadType,String leadId,String keyValue){
		
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
			return ok(home.render("",userRegistration));
		} else {
			AuthUser userObj = (AuthUser) getLocalUser();
			//File fdir = new File(rootDir+paths);
			CustomizationDataValue cValue = CustomizationDataValue.findByCustomeLeadByName(Long.parseLong(leadId),Long.parseLong(leadType),keyValue);
			//AddCollection product = AddCollection.findById(id);
			String path =null;
			if(cValue != null){
				path = rootDir+cValue.value;
			}
					
			File f = new File(path);
			
			return ok(f);
		}
	} 
	
    public static Result issueReportMail(String title, String message) {
    	AuthUser logoUser = getLocalUser();
    		
        	 EmailDetails details=EmailDetails.findByLocation(Long.valueOf(session("USER_LOCATION")));
    			String emailName=details.name;
    			String port=details.port;
    			String gmail=details.host;
    			final	String emailUser=details.username;
    			final	String emailPass=details.passward;
        	Properties props = new Properties();
    		props.put("mail.smtp.auth", "true");
    		props.put("mail.smtp.host",gmail);
    		props.put("mail.smtp.port", port);
    		props.put("mail.smtp.starttls.enable", "true");
    		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
    			protected PasswordAuthentication getPasswordAuthentication() {
    				return new PasswordAuthentication(emailUser, emailPass);
    			}
    		});
        	try
    		{
        		
    			Message msg = new MimeMessage(session);
        		try{
        			msg.setFrom(new InternetAddress(emailUser,emailName));
        		}
        		catch(UnsupportedEncodingException e){
        			e.printStackTrace();
        		}
        		msg.setRecipients(Message.RecipientType.TO,
    					InternetAddress.parse("support@gliderllc.com"));
        		msg.setSubject(title);
    			Multipart multipart = new MimeMultipart();
    			BodyPart messageBodyPart = new MimeBodyPart();
    			messageBodyPart = new MimeBodyPart();
    			
    			VelocityEngine ve = new VelocityEngine();
    			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
    			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
    			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
    			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
    			ve.init();
    		
    			
    	        Template t = ve.getTemplate("/public/emailTemplate/issueReportingData.html"); 
    	        VelocityContext context = new VelocityContext();
    	       
    	        context.put("message", message);
    	       // context.put("siteLogo", logo.logoImagePath);
    	        
    	        StringWriter writer = new StringWriter();
    	        t.merge( context, writer );
    	        String content = writer.toString(); 
    			
    			messageBodyPart.setContent(content, "text/html");
    			multipart.addBodyPart(messageBodyPart);
    			msg.setContent(multipart);
    			Transport.send(msg);
    		}
    		catch (Exception e)
    		{
    			e.printStackTrace();
    		}
    		return ok();
    }
    
}