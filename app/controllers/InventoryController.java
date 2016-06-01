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
import models.ProductImages;
import models.Sections;
import net.coobird.thumbnailator.Thumbnails;
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
import securesocial.core.Identity;
import viewmodel.AddCollectionVM;
import viewmodel.AddProductVM;
import viewmodel.ImageVM;
import viewmodel.SectionsVM;
import views.html.home;

public class InventoryController extends Controller {

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

	public static Result getReadyMadeCollection() {
		if (session("USER_KEY") == null || session("USER_KEY") == "") {
			return ok(home.render("", userRegistration));
		} else {
			AuthUser userObj = (AuthUser) getLocalUser();
			List<AddCollection> collectionList = AddCollection
					.getAllReadyMadeCollection();

			List<AddCollectionVM> collectionVMList = new ArrayList<>();
			for (AddCollection collection : collectionList) {
				AddCollectionVM aCollectionVM = new AddCollectionVM();
				aCollectionVM.setId(collection.getId());
				aCollectionVM.setDescription(collection.getDescription());
				aCollectionVM.setSection(collection.getSection());
				aCollectionVM.setTitle(collection.getTitle());
				aCollectionVM.setPath(collection.getPath());
				aCollectionVM.setWebsite(collection.getWebsite());
				aCollectionVM.setImage_name(collection.getImageName());
				collectionVMList.add(aCollectionVM);

			}
			return ok(Json.toJson(collectionVMList));
		}

	}
	
	public static Result getAllSection() {
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
		   	return ok(home.render("",userRegistration));
		   	} else {
		   		List<Sections> sec = Sections.findAllSection();
		   		List<SectionsVM> vmList = new ArrayList<>();
		   		for (Sections sections : sec) {
					SectionsVM vm = new SectionsVM();
					vm.id = sections.id;
					vm.title = sections.title;
					vm.value = sections.value;
					vmList.add(vm);
				}
		   		return ok(Json.toJson(vmList));
		   	}
	}
	
	   public static Result deleteCollectionById(Long id ){
		   	if(session("USER_KEY") == null || session("USER_KEY") == "") {
		   		return ok(home.render("",userRegistration));
		   	} else {
		   		System.out.println("in collection Delete");
		   			AuthUser user = (AuthUser) getLocalUser();
			    	AddCollection vmcoll = AddCollection.findById(id);
			    	List<AddProduct> aProduct = AddProduct.findProductsByCollection(vmcoll);
			    	for(AddProduct ap:aProduct){
			    		 AddProduct vm = AddProduct.findById(ap.id);
			    		 if(vm != null){
			    				String p = vm.filePath;	    		
			    				List<ProductImages> img = ProductImages.getByProduct(vm);
			    				for (ProductImages productImages : img) {
			    					
			    					File file = new File(rootDir+File.separator+id+"-"+user.id+File.separator+productImages.imageName);
			    			    	File thumbFile = new File(rootDir+File.separator+id+"-"+user.id+File.separator+"thumbnail_"+productImages.imageName);
			    			    	file.delete();
			    			    	thumbFile.delete();
			    			    	productImages.delete();
			    				}
			    				
			    		 }
			    		 vm.delete();
			    	}
			    	
			    	File file = new File(rootDir+File.separator+id+"collection"+File.separator+vmcoll.imageName);
			    	vmcoll.delete();
			    	
			    	return ok();
		   	}	
		   }

	public static AuthUser getLocalUser() {
		String id = session("USER_KEY");
		AuthUser user = AuthUser.find.byId(Integer.parseInt(id));
		return user;
	}

}