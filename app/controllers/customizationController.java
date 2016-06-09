package controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import models.AddCollection;
import models.AddProduct;
import models.AuthUser;
import models.CustomizationForm;
import models.LeadType;
import models.Location;
import models.ProductImages;
import models.Sections;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import securesocial.core.Identity;
import viewmodel.AddCollectionVM;
import viewmodel.AddProductVM;
import viewmodel.ImageVM;
import viewmodel.SectionsVM;
import views.html.home;

public class customizationController extends Controller {

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

	public static Result getLeadCrateForm() {
		JsonNode json = request().body().asJson();
		
		CustomizationForm cForm = CustomizationForm.findByLocationsAndType(Long.valueOf(session("USER_LOCATION")),"Create Lead");
		if(cForm == null){
			CustomizationForm cust = new CustomizationForm();
			cust.setDataType("Create Lead");
			cust.setJsonData(json.toString());
			cust.setLocations(Location.findById(Long.valueOf(session("USER_LOCATION"))));
			cust.save();
		}else{
			//cForm.setDataType("Create Lead");
			cForm.setJsonData(json.toString());
			//cForm.setLocations(Location.findById(Long.valueOf(session("USER_LOCATION"))));
			cForm.update();
		}
		
		
		return ok();
	}
	
	
	public static Result getCustomizationform(String type) {
		CustomizationForm cForm = CustomizationForm.findByLocationsAndType(Long.valueOf(session("USER_LOCATION")),type);
		return ok(Json.toJson(cForm));
	}
	
	
}