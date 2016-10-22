package controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.sun.org.apache.bcel.internal.generic.POP;

import models.AuthUser;
import models.Location;
import models.PhotographerHoursOfOperation;
import models.ScheduleTest;
import models.Site;
import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import viewmodel.HoursOperation;
import viewmodel.PortalNameVM;
import viewmodel.ScheduleTestVM;
import viewmodel.SiteVM;
import viewmodel.UserVM;

public class CalendarController extends Controller {

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

	public static AuthUser getLocalUser() {
    	String id = session("USER_KEY");
    	AuthUser user = AuthUser.find.byId(Integer.parseInt(id));
    	//AuthUser user = getLocalUser();
		return user;
	}
	
	 public static Result getSalesPerson() {
	    	
	    	List<AuthUser> authuser = AuthUser.findByLocatioion(Location.findById(Long.valueOf(session("USER_LOCATION"))));
	    	return ok(Json.toJson(authuser));
	    }
	 public static class DateUtil{
	    	public static Date addDays(Date date, int hours){
		        Calendar cal = Calendar.getInstance();
		        cal.setTime(date); // sets calendar time/date
		        cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour
		        cal.getTime();
		        return cal.getTime();
	    	}
	    }
	 
	 public static Result getTimeTableOf(){
		 AuthUser user = getLocalUser();
		 DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		 
		 System.out.println("user.>>>>>>>>>>"+user);
		 List<ScheduleTest> pOfOperation = ScheduleTest.findAllAssignedData(user);
		 
		 ArrayList<ScheduleTestVM> hList =  new ArrayList<>();
		 for(ScheduleTest pOperation:pOfOperation){
			 ScheduleTestVM hOperation = new ScheduleTestVM();
			 /*if(pOperation.meetingStatus.equals("meeting")){
				hOperation.id = pOperation.id;
				
				Date test;
				Date d1 = pOperation.confirmDate;
				Date d2 = pOperation.confirmTime;
				String dt = pOperation.confirmDate +" "+ pOperation.confirmTime.toString() ;
				Date mydate = new Date();
				mydate.setDate(d1.getDate());
				mydate.setMonth(d1.getMonth());
				mydate.setYear(d1.getYear());
				mydate.setHours(d2.getHours());
				mydate.setMinutes(d2.getMinutes());
				mydate.setSeconds(d2.getSeconds());
				System.out.println(mydate.toString());
				System.out.println("date formate"+dt);
				try {
					test = df.parse(dt);
					System.out.println("String formate"+test);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				hOperation.startTime = df.format(pOperation.confirmTime);
				hOperation.endTime = df.format(pOperation.confirmEndTime);
				hOperation.name = pOperation.name;
				hOperation.email = pOperation.email;
				
				hList.add(hOperation);
			 }
			 else{*/
				 hOperation.id = pOperation.id;
				 hOperation.startTime = df.format(pOperation.scheduleTime);
				 
				 Date endTime = pOperation.scheduleTime;
				 endTime = DateUtil.addDays(endTime, 1);
				 hOperation.endTime = df.format(endTime);
				 hOperation.name = pOperation.name;
				 hOperation.email = pOperation.email;
				 
				 hList.add(hOperation);
			// }
		}
		 return ok(Json.toJson(hList));
	}
	 
	 public static Result getTimeOfUser(Integer users){
		System.out.println("calendraData.>>>>>>>>>>"+users);
		 AuthUser user = AuthUser.findById(users);
		 DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		 List<ScheduleTest> pOfOperation = ScheduleTest.findAllAssignedData(user);
		 
		 ArrayList<ScheduleTestVM> hList =  new ArrayList<>();
		 for(ScheduleTest pOperation:pOfOperation){
			 ScheduleTestVM hOperation = new ScheduleTestVM();
			 hOperation.id = pOperation.id;
			 hOperation.startTime = df.format(pOperation.scheduleTime);
			 
			 Date endTime = pOperation.scheduleTime;
			 endTime = DateUtil.addDays(endTime, 1);
			 hOperation.endTime = df.format(endTime);
			 hOperation.name = pOperation.name;
			 hOperation.email = pOperation.email;
			 
			 hList.add(hOperation);
		}
		 return ok(Json.toJson(hList));
	}
}