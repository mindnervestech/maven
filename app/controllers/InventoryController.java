package controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import models.AddCollection;
import models.AuthUser;
import models.CollectionImages;
import models.InventorySetting;
import models.LeadType;
import models.Product;
import models.ProductImages;
import models.Sections;
import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
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
import viewmodel.AddCollectionVM;
import viewmodel.AddCollectionVM;
import viewmodel.ImageVM;
import viewmodel.LeadTypeVM;
import viewmodel.ProductVM;
import viewmodel.SectionsVM;
import views.html.home;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tinify.Source;
import com.tinify.Tinify;

public class InventoryController extends Controller {

	final static String rootDir = Play.application().configuration()
			.getString("image.storage.path");
	final static String pdfRootDir = Play.application().configuration()
			.getString("pdfRootDir");

	final static String tinifyKey = Play.application().configuration()
			.getString("tinifyKey");
	
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
			//AuthUser userObj = (AuthUser) getLocalUser();
			//List<AddCollection> collectionList = AddCollection.getAllReadyMadeCollection();

			List<AddCollectionVM> collectionVMList = new ArrayList<>();
			/*for (AddCollection collection : collectionList) {
				AddCollectionVM aCollectionVM = new AddCollectionVM();
				aCollectionVM.setId(collection.getId());
				aCollectionVM.setDescription(collection.getDescription());
				aCollectionVM.setSection(collection.getSection());
				aCollectionVM.setTitle(collection.getTitle());
				aCollectionVM.setPath(collection.getPath());
				aCollectionVM.setWebsite(collection.getWebsite());
				aCollectionVM.setImage_name(collection.getImageName());
				collectionVMList.add(aCollectionVM);

			}*/
			return ok(Json.toJson(collectionVMList));
		}

	}
	
	public static Result saveCollectionOrder(){
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
			return ok(home.render("",userRegistration));
		} else {
			System.out.println("success");
			JsonNode nodes = ctx().request().body().asJson();
			for(int i=0; i< nodes.size(); i++){
	    			ObjectNode obj = (ObjectNode) nodes.get(i);
	    			long id = Long.parseLong(obj.get("id").toString());
	    			//long ord = Long.parseLong(obj.get("collectionOrder").toString());
	    			
	    			AddCollection cl = AddCollection.findById(id);
	    			if(cl !=null){
	    				cl.setOrderIndex(i);
	    				cl.update();
	    			}
			}
			return ok();
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
		    	AuthUser user = (AuthUser) getLocalUser();
		    	CollectionImages image = CollectionImages.findById(id);
		    	File file = new File(rootDir+File.separator+id+"-"+user.id+File.separator+image.imageName);
		    	File thumbFile = new File(rootDir+File.separator+id+"-"+user.id+File.separator+"thumbnail_"+image.imageName);
		    	file.delete();
		    	thumbFile.delete();
		    	image.delete();
		    	return ok();
	    	}
		   }
	   
	   public static Result getAllSale() {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		/*AuthUser userObj = (AuthUser) getLocalUser();
		    	List<AddCollection> collectionList = AddCollection.getAllReadyMade();
		    	List<AddCollectionVM> productVMList = new ArrayList<>(); 
		    	for(AddCollection collection : collectionList) {
		    		List<AddCollection> productList = AddCollection.findProductsByCollectionSale(collection);
		    		
		    		for(AddCollection product : productList) {
		    			AddCollectionVM vm = new AddCollectionVM();
			    		vm.collectionTitle = collection.title;
		    			vm.title = product.title;
		    			vm.price = product.price;
		    			vm.cost = product.cost;
		    			vm.description = product.description;
		    			vm.id = product.id;
		    			vm.sale = product.sale;
		    			ProductImages img = ProductImages.findDefaultImg(product.id);
		    			if(img !=null){
		    				vm.imgId = img.id;
		    				vm.imgPath = rootDir+img.thumbPath;
		    			}else{
		    				vm.imgPath = null;
		    			}
		    	    	vm.countImages = ProductImages.countImage(product.id);
		    			productVMList.add(vm);
		    		}
		    	}
		    	return ok(Json.toJson(productVMList));*/
	    	}
	    	return ok();
	    }
	   
	   public static Result moveProductById(Long id ){
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	AddCollection vm = AddCollection.findById(id);
		    	AuthUser user = (AuthUser) getLocalUser();
		    	AddCollection add = AddCollection.findById(vm.id);
		    	add.setSale("null");
	    		add.update();
				return ok();
	    	}
	 
	    
	      }
	   
	   public static Result saleProductById(Long id ){
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		
	    		
		    	AddCollection vm = AddCollection.findById(id);
		    	AuthUser user = (AuthUser) getLocalUser();
		    	
		    	AddCollection add = AddCollection.findById(vm.id);
		    	add.setSale("sale");
	    		add.update();
				return ok();
	    	}
	 
	    
	      }
	   
	   public static Result getAllReadyMade() {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		/*AuthUser userObj = (AuthUser) getLocalUser();
		    	List<AddCollection> collectionList = AddCollection.getAllReadyMade();
		    	List<AddCollectionVM> productVMList = new ArrayList<>(); 
		    	for(AddCollection collection : collectionList) {
		    		List<AddCollection> productList = AddCollection.findProductsReadyMadeByCollection(collection);
		    		for(AddCollection product : productList) {
		    			AddCollectionVM vm = new AddCollectionVM();
			    		vm.collectionTitle = collection.title;
		    			vm.title = product.title;
		    			vm.price = product.price;
		    			vm.cost = product.cost;
		    			vm.description = product.description;
		    			vm.id = product.id;
		    			vm.countImages = ProductImages.countImage(product.id);
		    			ProductImages img = ProductImages.findDefaultImg(product.id);
		    			if(img !=null){
		    				vm.imgId = img.id;
		    				vm.imgPath = rootDir+img.thumbPath;
		    			}else{
		    				vm.imgPath = null;
		    			}
		    			productVMList.add(vm);
		    		}
		    	}
		    	return ok(Json.toJson(productVMList));*/
	    	}
	    	return ok();
	    }
	   
	   public static void saveVm(AddCollectionVM pVm,MultipartFormData bodys,Form<AddCollectionVM> form) {
	    	 
		   JSONObject jArr1 = null;
			try {
		
				jArr1 = new JSONObject(form.data().get("mainCollection"));
				
				//for (int i=0; i < jArr1.length(); i++) {
					InventorySetting vm1 = new InventorySetting();
					//JSONObject jsonObj1 = jArr1.getJSONObject(i);
					vm1.collection = String.valueOf(jArr1.get("collection"));
					vm1.enableInven = Boolean.parseBoolean(String.valueOf(jArr1.get("enableInven")));
					vm1.id = Long.parseLong(String.valueOf(jArr1.get("id")));
					pVm.mainCollection = vm1;
				//
				
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
	         pVm.id = Long.parseLong(form.data().get("id"));
	         pVm.description = form.data().get("description");
	         pVm.externalUrlLink = form.data().get("externalUrlLink");
	         pVm.externalUrlLink = form.data().get("externalUrlLink");
	         pVm.fileName = form.data().get("fileName");
	         if(form.data().get("parentId") != null){
	        	 pVm.parentId = Long.parseLong(form.data().get("parentId"));
	         }
	         
	         pVm.publicStatus = form.data().get("publicStatus");
	         pVm.title = form.data().get("title");
	    }
	   
	   public static Result updateProduct(){
	    	
	    	MultipartFormData body = request().body().asMultipartFormData();
	    	Form<AddCollectionVM> form = DynamicForm.form(AddCollectionVM.class).bindFromRequest();
	    	AddCollectionVM vm = new AddCollectionVM();
	    	if(body != null){
	    		AddCollectionVM vm1 = new AddCollectionVM();
	       		saveVm(vm1,body,form);
	       		vm = vm1;
	       	}else{
	       		vm = form.get();
	       	}
	    	
	    	System.out.println(vm.getId());
	    	FilePart picture = null ;
	    	
	    	if(body != null){
	    		List<FilePart> filePart =  body.getFiles();
	    		if (filePart != null) {
	    			if(filePart.size() > 0){
	    				AddCollection add = AddCollection.findById(vm.getId());
	    				add.setTitle(vm.getTitle());
	            		add.setDescription(vm.getDescription());
	            		add.setPublicStatus(vm.getPublicStatus());
	            		add.setParentId(vm.getParentId());
	            		add.setExternalUrlLink(vm.getExternalUrlLink());
	            		add.update();
	    				
	            		Tinify.setKey(tinifyKey);
	    		    	
	    		    	Source source;
	    		    	AuthUser userObj = (AuthUser) getLocalUser();
	    		    	
	    		    	for(int i= 0; i<filePart.size(); i++){
	    		    		picture = filePart.get(i);
	    		    		String fileName = picture.getFilename().replaceAll("[-+^:,() ]","");;
	    		      		fileName = vm.getTitle() +"_"+ fileName;
	    		      		String ext = FilenameUtils.getExtension(fileName);
		       	     		System.out.println(ext);
	    		      		
	    		      		String contentType = picture.getContentType();
	    		      		File fdir = new File(rootDir+File.separator+add.getId()+"-"+userObj.id+File.separator+"Logo");
	    		      		if(!fdir.exists()) {
	    		       	    	fdir.mkdir();
	    		       	    }
	    		      		String filePath = rootDir+File.separator+add.getId()+"-"+userObj.id+File.separator+"Logo"+File.separator+fileName;
	    		      		try {
	    		      			Boolean sts = FileUtils.deleteQuietly(new File(filePath));
	    		       			System.out.println("delete "+sts);
	    					} catch (Exception e) {
	    						e.printStackTrace();
	    					}
	    		      		
	    		       	    File file = picture.getFile();
	    		       	 try {
	    		       		 if(ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("jpeg") || ext.equalsIgnoreCase("svg") || ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("gif")){
			       	       	    	FileUtils.moveFile(file, new File(filePath));
			       	       	    	add.setFileName(fileName);
	     		    		   		add.setFilePath("/"+add.getId()+"-"+userObj.id+"/"+"Logo"+"/"+fileName);
	     		    		   		add.update();
	     		    		   		
	     		    		   	try {
	    							source = Tinify.fromFile(filePath);
	    							source.toFile(filePath);
	    						
	    						} catch (IOException e1) {
	    							// TODO Auto-generated catch block
	    							e1.printStackTrace();
	    						}
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
	   
	   
	   public static Result deleteProductById(Long id ){
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		
	    		List<ProductImages> pImages = ProductImages.getDeleteImagePath(id);
	    		if(pImages.size() != 0){
	    			for(ProductImages pI:pImages){
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
	    	}	
	    }
	   
	   public static Result findLocation(){
		   return ok(Json.toJson(Long.valueOf(session("USER_LOCATION"))));
	   }
	   
	   public static Result getOnlyReadyMade() {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    /*		AuthUser userObj = (AuthUser) getLocalUser();
		    	List<AddCollection> collectionList = AddCollection.getAllReadyMade();
		    	List<AddCollectionVM> productVMList = new ArrayList<>(); 
		    	for(AddCollection collection : collectionList) {
		    		List<AddCollection> productList = AddCollection.findProductsByCollectiongetOnlyReadyMade(collection);
		    		for(AddCollection product : productList) {
		    			AddCollectionVM vm = new AddCollectionVM();
			    		vm.collectionTitle = collection.title;
		    			vm.title = product.title;
		    			vm.price = product.price;
		    			vm.cost = product.cost;
		    			vm.description = product.description;
		    			vm.id = product.id;
		    			vm.countImages = ProductImages.countImage(product.id);
		    			ProductImages img = ProductImages.findDefaultImg(product.id);
		    			if(img !=null){
		    				vm.imgId = img.id;
		    				vm.imgPath = rootDir+img.thumbPath;
		    			}else{
		    				vm.imgPath = null;
		    			}
		    			productVMList.add(vm);
		    		}
		    	}
		    	return ok(Json.toJson(productVMList));*/
	    	}
	    	return ok();
	    }
	   
	   public static Result getAllAccessories() {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	       		return ok(home.render("",userRegistration));
	       	} else {
	       		/*AuthUser userObj = (AuthUser) getLocalUser();
	    	    	List<AddCollection> collectionList = AddCollection.getAllAccessories();
	    	    	
	    	    	List<AddCollectionVM> collectionVMList = new ArrayList<>(); 
	    	    	for(AddCollection collection : collectionList) {
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
	    	    	return ok(Json.toJson(collectionVMList));*/
	       	}
	    	return ok();
   }
	   
	   public static Result getAllProductTypes() {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		/*AuthUser userObj = (AuthUser) getLocalUser();
		    	List<AddCollection> collectionList = AddCollection.getAllProducts();
		    	List<AddCollectionVM> productVMList = new ArrayList<>(); 
		    	for(AddCollection collection : collectionList) {
		    		List<AddCollection> productList = AddCollection.findProductsByCollection(collection);
		    		for(AddCollection product : productList) {
		    			AddCollectionVM vm = new AddCollectionVM();
			    		vm.collectionTitle = collection.title;
		    			vm.title = product.title;
		    			vm.description = product.description;
		    			vm.id = product.id;
		    			vm.price = product.price;
		    			vm.cost = product.cost;
		    			vm.countImages = ProductImages.countImage(product.id);
		    			ProductImages img = ProductImages.findDefaultImg(product.id);
		    			if(img !=null){
		    				vm.imgId = img.id;
		    				vm.imgPath = rootDir+img.thumbPath;
		    			}else{
		    				vm.imgPath = null;
		    			}
		    			productVMList.add(vm);
		    		}
		    	}
		    	return ok(Json.toJson(productVMList));*/
	    	}
	    	return ok();
	    }
	   
	   public static Result getProductCollectionId(Long id) {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		/*AuthUser userObj = (AuthUser) getLocalUser();
	    		AddCollection product = AddCollection.findById(id);
	    		AddCollection coll = AddCollection.findById(product.collection.id);
	    		return ok(Json.toJson(coll.section));*/
	    	}
	    	return ok();
	    }
	   public static Result getProductCollection() {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	       		return ok(home.render("",userRegistration));
	       	} else {
	       		/*AuthUser userObj = (AuthUser) getLocalUser();
	    	    	List<AddCollection> collectionList = AddCollection.getAllProductCollection();
	    	    	
	    	    	List<AddCollectionVM> collectionVMList = new ArrayList<>(); 
	    	    	for(AddCollection collection : collectionList) {
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
	    	    	return ok(Json.toJson(collectionVMList));*/
	       	}
	    	return ok();
	    	    	
	}
	   
	   public static Result getProductData(Long id) {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		AuthUser userObj = (AuthUser) getLocalUser();
	    		String sec=null;
	    		File fdir = new File(rootDir+File.separator+userObj.id+File.separator+"product");
	    		AddCollection product = AddCollection.findById(id);
	    		AddCollectionVM vm =new AddCollectionVM();
	    		vm.id = product.id;
	    		vm.title =product.title;
	    		vm.description = product.description;
	    		vm.parentId = product.parentId;
	    		vm.externalUrlLink = product.externalUrlLink;
	    		vm.mainCollection = product.mainCollection;
	    		return ok(Json.toJson(vm));
	    	}
	    }
	   
	   public static Result collectionList(){
	    	AuthUser userObj = (AuthUser) getLocalUser();
			/*List<AddCollection> listData = AddCollection.findAllCollection();
	    	List<AddCollectionVM> addVMList = new ArrayList<>();
	    	for(AddCollection coll: listData) {
	    		if(coll.section.equals("readymade") || coll.section.equals("product")){
	    			AddCollectionVM vm = new AddCollectionVM();
	    			vm.title = coll.title;
	    			vm.id = coll.id;
	    			addVMList.add(vm);
	    		}
	    	}
			return ok(Json.toJson(addVMList));*/
	    	return ok();
	    
	    }

	   public static Result updateProductInfo(){
	    	
	    	Form<AddCollectionVM> form = DynamicForm.form(AddCollectionVM.class).bindFromRequest();
	    	AddCollectionVM vm = form.get();
	    	
	    	AuthUser userObj = (AuthUser) getLocalUser();

	       	    try {
	       	    	AddCollection add = AddCollection.findById(vm.getId());
	        		add.setTitle(vm.getTitle());
	        		add.setDescription(vm.getDescription());
	        		add.setPublicStatus(vm.getPublicStatus());
	        		add.setParentId(vm.getParentId());
	        		add.setMainCollection(vm.mainCollection);
	        		add.setExternalUrlLink(vm.getExternalUrlLink());
	        		add.update();
	    
				} catch (Exception e) {
					e.printStackTrace();
				}
				return ok();
	      }
	   
	   public static Result fileDownload(Long id){
			
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
				return ok(home.render("",userRegistration));
			} else {
				AuthUser userObj = (AuthUser) getLocalUser();
				File fdir = new File(rootDir+File.separator+userObj.id+File.separator+"product"+File.separator);
				AddCollection product = AddCollection.findById(id);
				
				String path = rootDir+product.filePath;
						
				File f = new File(path);
				
				return ok(f);
			}
		} 
	   
	   public static Result cadFileDownload(Long id){
			
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
				return ok(home.render("",userRegistration));
			} else {
				AuthUser userObj = (AuthUser) getLocalUser();
				File fdir = new File(rootDir+File.separator+userObj.id+File.separator+"product"+File.separator);
				AddCollection product = AddCollection.findById(id);
				String path = rootDir+product.cadfilePath;
						
				File f = new File(path);
				
				return ok(f);
			}
		} 
	   
	   
	   public static Result getAllCollection(Long id) {
		   	if(session("USER_KEY") == null || session("USER_KEY") == "") {
		   		return ok(home.render("",userRegistration));
		   	} else {
		   		AuthUser userObj = (AuthUser) getLocalUser();
		   		InventorySetting mainColl = InventorySetting.findById(id);
			    List<AddCollection> collectionList = AddCollection.getAllCollection(mainColl);
			    return ok(Json.toJson(collectionList));
		   	}
		   }
	   
	   public static Result getCollectionData(Long id) {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		
	    		AddCollection collection = AddCollection.findById(id);
	    		AddCollectionVM vm =new AddCollectionVM();
	    		/*vm.setTitle(collection.getTitle());
	    		vm.setDescription(collection.getDescription());
	    		vm.setSection(collection.getSection());
	    		vm.setNewFlag(collection.getNewFlag());
	    		vm.setImage_name(collection.getImageName());
	    		vm.setWebsite(collection.getWebsite());
	    		vm.setThumbnailImageName(collection.thumbnailImageName);
	    		vm.setThumbnailPath(collection.thumbnailPath);
	    		vm.setThumbnailThumbPath(collection.thumbnailThumbPath);*/
	    		return ok(Json.toJson(vm));
	    	}
	    
	    }
	   
	   public static Result updateCollection() {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    		
	    	} else {
		    	MultipartFormData body = request().body().asMultipartFormData();
		    	DynamicForm dynamicForm = Form.form().bindFromRequest();
		    	Form<AddCollectionVM> form = DynamicForm.form(AddCollectionVM.class).bindFromRequest();
		    	AddCollectionVM vm = form.get();
		    	Long value = vm.id;
		    
		    	AuthUser userObj = (AuthUser) getLocalUser();
		    	if(body != null){
		    	FilePart picture = body.getFile("file0");
		    	  String fileName = picture.getFilename().replaceAll("[-+^:,() ]","");
		    	  if (picture != null) {
		    	    String contentType = picture.getContentType(); 
		    	    
		    	    File fdir = new File(rootDir+File.separator+userObj.id+File.separator+"collection");
		    	    if(!fdir.exists()) {
		    	    	fdir.mkdir();
		    	    }
		    	    
		    	    String filePath = rootDir+File.separator+userObj.id+File.separator+"collection"+File.separator+fileName;
		    	    String thumbnailPath =rootDir+File.separator+userObj.id+File.separator+"collection"+File.separator+"thumbnail_"+fileName;
		    	    File thumbFile = new File(thumbnailPath);
		    	    File file = picture.getFile();
		    	    
		    	    File _f = new File(filePath);
		    		
		    	    try {
		    	    	BufferedImage originalImage = ImageIO.read(file);
						Thumbnails.of(originalImage).height(162).toFile(thumbFile);
						Thumbnails.of(originalImage).scale(1.0).toFile(_f);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
		    	    AddCollection coll = AddCollection.findById(vm.id);
		    	    String pathName = "/"+userObj.id+"/"+"collection"+"/"+fileName;
		    	    String thumbPath ="/"+userObj.id+"/"+"collection"+"/"+"thumbnail_"+fileName;
		      		/*coll.setPath(pathName);
		      		coll.setThumbPath(thumbPath);
		      		coll.setTitle(vm.title);//.title = vm.title;
		      		coll.setDescription(vm.description);//.description =vm.description;
		      		coll.setSection(vm.section);//.section =vm.section;
		      		coll.setWebsite(vm.website);
		      		coll.setNewFlag(vm.newFlag);
		      		coll.setImageName(fileName);*/
		      		coll.update();
		    	  } 
		    	}else{
		    		AddCollection coll = AddCollection.findById(vm.id);
		    		coll.setTitle(vm.title);//.title = vm.title;
		      		/*coll.setDescription(vm.description);//.description =vm.description;
		      		coll.setSection(vm.section);//.section =vm.section;
		      		coll.setWebsite(vm.website);
		      		coll.setNewFlag(vm.newFlag);*/
		      		coll.update();
		    	}
		
		    	  return ok();
	    	}
	    }
	   
	   public static Result updateReadyCollection() {
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		MultipartFormData body = request().body().asMultipartFormData();
		    	Form<AddCollectionVM> form = DynamicForm.form(AddCollectionVM.class).bindFromRequest();
		    	AddCollectionVM vm = form.get();
		    	AddCollection coll = AddCollection.findById(vm.id);
		    	AuthUser userObj = (AuthUser) getLocalUser();
		    	if(body != null){
		    		coll.setTitle(vm.title);
		      		/*coll.setDescription(vm.description);
		      		coll.setSection(vm.section);
		      		coll.setWebsite(vm.website);
		      		coll.setNewFlag(vm.newFlag);*/
		      		
		    		List<FilePart> list = body.getFiles();
		    		if(list.get(0).getKey().equalsIgnoreCase("featured")){
		    			FilePart picture = list.get(0);
		    			String fileName = picture.getFilename().replaceAll("[-+^:,() ]","");
		    			File fdir = new File(rootDir+File.separator+userObj.id+File.separator+"collection");
			    	    if(!fdir.exists()) {
			    	    	fdir.mkdir();
			    	    }
			    	    String filePath = rootDir+File.separator+userObj.id+File.separator+"collection"+File.separator+fileName;
			    	    String thumbnailPath =rootDir+File.separator+userObj.id+File.separator+"collection"+File.separator+"thumbnail_"+fileName;
			    	    File thumbFile = new File(thumbnailPath);
			    	    File file = picture.getFile();
			    	    File _f = new File(filePath);
			    	    try {
			    	    	BufferedImage originalImage = ImageIO.read(file);
							Thumbnails.of(originalImage).height(162).toFile(thumbFile);
							Thumbnails.of(originalImage).scale(1.0).toFile(_f);
						} catch (IOException e) {
							e.printStackTrace();
						}
				    	    String pathName = "/"+userObj.id+"/"+"collection"+"/"+fileName;
				    	    String thumbPath ="/"+userObj.id+"/"+"collection"+"/"+"thumbnail_"+fileName;
				      		/*coll.setPath(pathName);
				      		coll.setThumbPath(thumbPath);
				      		coll.setImageName(fileName);*/
		    		}
		    		if(list.get(1).getKey().equalsIgnoreCase("thumbnail")){
		    			FilePart picture = list.get(1);
		    			String fileName = picture.getFilename().replaceAll("[-+^:,() ]","");
		    			File fdir = new File(rootDir+File.separator+userObj.id+File.separator+"collection");
			    	    if(!fdir.exists()) {
			    	    	fdir.mkdir();
			    	    }
			    	    String filePath = rootDir+File.separator+userObj.id+File.separator+"collection"+File.separator+fileName;
			    	    String thumbnailPath =rootDir+File.separator+userObj.id+File.separator+"collection"+File.separator+"thumbnail_"+fileName;
			    	    File thumbFile = new File(thumbnailPath);
			    	    File file = picture.getFile();
			    	    File _f = new File(filePath);
			    	    try {
			    	    	BufferedImage originalImage = ImageIO.read(file);
							Thumbnails.of(originalImage).height(162).toFile(thumbFile);
							Thumbnails.of(originalImage).scale(1.0).toFile(_f);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    	    String pathName = "/"+userObj.id+"/"+"collection"+"/"+fileName;
				    	    String thumbPath ="/"+userObj.id+"/"+"collection"+"/"+"thumbnail_"+fileName;
				      		/*coll.setThumbnailPath(pathName);
				      		coll.setThumbnailThumbPath(thumbPath);
				      		coll.setThumbnailImageName(fileName);*/
		    		}
		    		coll.update();
		    	}else{
		    		coll.setTitle(vm.title);
		      		/*coll.setDescription(vm.description);
		      		coll.setSection(vm.section);
		      		coll.setWebsite(vm.website);
		      		coll.setNewFlag(vm.newFlag);*/
		      		coll.update();
		    	}
		    	  return ok();
	    	}
	}
	   
	   
	   public static Result getImagesByCollection(Long id){
		   if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	Identity user = getLocalUser();
		    	AuthUser userObj = (AuthUser)user;
		    	AddCollection product = AddCollection.findById(id);
		    	List<CollectionImages> imageList = CollectionImages.getByCollectionImg(product);
		    	//reorderImagesForFirstTime(imageList);
		    	List<ImageVM> vmList = new ArrayList<>();
		    	for(CollectionImages image : imageList) {
		    		ImageVM vm = new ImageVM();
		    		vm.id = image.id;
		    		vm.imgName = image.imageName;
		    		vm.path = image.path;
		    		vm.defaultImage = image.defaultImage;
		    		vm.description = image.description;
		    		vm.title = image.title;
		    		vmList.add(vm);
		    	}
		    	return ok(Json.toJson(vmList));
	    	}	
	    }
	   
	   public static Result getAllSectionTypes(Long id) {
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
				return ok(home.render("",userRegistration));
			} else {
				/*AuthUser userObj = (AuthUser) getLocalUser();
				Sections sec = Sections.findById(id);
		    	List<AddCollection> collectionList = AddCollection.getAllBySection(sec.value);
		    	List<AddCollectionVM> productVMList = new ArrayList<>(); 
		    	for(AddCollection collection : collectionList) {
		    		List<AddCollection> productList = AddCollection.findProductsByCollection(collection);
		    		for(AddCollection product : productList) {
		    			AddCollectionVM vm = new AddCollectionVM();
			    		vm.collectionTitle = collection.title;
		    			vm.title = product.title;
		    			vm.description = product.description;
		    			vm.id = product.id;
		    			vm.price = product.price;
		    			vm.cost = product.cost;
		    			vm.countImages = ProductImages.countImage(product.id);
		    			ProductImages img = ProductImages.findDefaultImg(product.id);
		    			if(img !=null){
		    				vm.imgId = img.id;
		    				vm.imgPath = rootDir+img.thumbPath;
		    			}else{
		    				vm.imgPath = null;
		    			}
		    			productVMList.add(vm);
		    		}
		    	}
		    	return ok(Json.toJson(productVMList));*/
			}
			return ok();
		}
	   
	   
	   public static Result getSectionCollectionData(Long id) {
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
		   		return ok(home.render("", userRegistration));
		   	} else {
		   		/*AuthUser userObj = (AuthUser) getLocalUser();
		   		
		   		Sections sec = Sections.findById(id);
			    	List<AddCollection> collectionList = AddCollection.getAllBySection(sec.value);
			    	
			    	List<AddCollectionVM> collectionVMList = new ArrayList<>(); 
			    	for(AddCollection collection : collectionList) {
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
			    	return ok(Json.toJson(collectionVMList));*/
		   	}
			return ok();
		}
	   
	   
	   public static Result getSelectedLeadType() {
		   List<LeadType> lType = LeadType.findByLocationsAndSelected(Long.valueOf(session("USER_LOCATION")));
		  /* List<LeadTypeVM> lVmList = new ArrayList<LeadTypeVM>(); 
		   for(LeadType lType2 : lType){
							   LeadTypeVM vm = new LeadTypeVM();
							   vm.leadName = lType2.leadName;
							   vm.profile = lType2.profile;
							   vm.callToAction = lType2.callToAction;
							   vm.actionOutcomes = lType2.actionOutcomes;
							   vm.actionTitle = lType2.actionTitle;
							   vm.maunfacturersIds = lType2.maunfacturersIds;
							   vm.actionClientPdf = lType2.actionClientPdf;
							   lVmList.add(vm);
			   }*/
			   
		   return ok(Json.toJson(lType));
	   }
	   
	   public static Result getSelectedLeadTypeWise(String productType) {
		   List<LeadType> lType = LeadType.findByLocationsAndSelected(Long.valueOf(session("USER_LOCATION")));
		   List<LeadTypeVM> lVmList = new ArrayList<LeadTypeVM>(); 
		   for(LeadType lType2 : lType){
			   if(lType2.maunfacturersIds != null){
				   String arr[] = lType2.maunfacturersIds.split(",");
				   for(int i=0;i<arr.length;i++){
					   AddCollection aProduct = AddCollection.findById(Long.parseLong(arr[i]));
					   if(aProduct != null){
						   if(aProduct.title.equals(productType)){
							   LeadTypeVM vm = new LeadTypeVM();
							   vm.leadName = lType2.leadName;
							   vm.profile = lType2.profile;
							   vm.callToAction = lType2.callToAction;
							   vm.actionOutcomes = lType2.actionOutcomes;
							   vm.actionTitle = lType2.actionTitle;
							   vm.maunfacturersIds = lType2.maunfacturersIds;
							   vm.actionClientPdf = lType2.actionClientPdf;
							   lVmList.add(vm);
						   }
					   }
					   
					   
				   }
			   }
			   
			   
		   }
		   return ok(Json.toJson(lVmList));
	   }
	   
	   
	   
	   public static Result getHideProduct(Long id) {
		   AddCollection aProduct = AddCollection.findById(id);
		   if(aProduct != null){
			   if(aProduct.hideWebsite == 1){
				   aProduct.setHideWebsite(0);
			   }else{
				   aProduct.setHideWebsite(1);
			   }
			   aProduct.update();
		   }
		   List<AddCollection> list = AddCollection.getProductByParentId(id);
		   for (AddCollection ap : list) {
			   if(aProduct.hideWebsite == 1){
				   ap.setHideWebsite(0);
			   }else{
				   ap.setHideWebsite(1);
			   }
			   aProduct.update();
		}
		   return ok();
	   }
	   
	   
	   public static Result getAllProductWise(String status, String date,String leadType) {
			List<AddCollection> pList = AddCollection.getProductByStatus(Long.valueOf(session("USER_LOCATION")), status);
			List<AddCollectionVM> aList = new ArrayList<AddCollectionVM>();
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			for(AddCollection aProduct:pList){
				AddCollectionVM aVm = new AddCollectionVM();
				aVm.title = aProduct.title;
				aVm.description = aProduct.description;
				aVm.fileName = aProduct.fileName;
				aVm.id = aProduct.id;
				aVm.orderIndex = aProduct.orderIndex;
				aVm.hideWebsite = aProduct.hideWebsite;
				if(aProduct.addedDate != null){
					aVm.addedDate = df.format(aProduct.addedDate);
				}
								
				aList.add(aVm);
			}
			return ok(Json.toJson(aList));
		}

	   
	   public static Result getAllProduct(String status, Long collId, String date) {
		   List<AddCollection> pList = new ArrayList<>();
		   if(collId != null){
			   InventorySetting mainCollection = InventorySetting.findById(collId);
			   pList = AddCollection.getProductByStatusMainColl(Long.valueOf(session("USER_LOCATION")), status,mainCollection);
		   }else{
			    pList = AddCollection.getProductByStatus(Long.valueOf(session("USER_LOCATION")), status);
		   }
			
			List<AddCollectionVM> aList = new ArrayList<AddCollectionVM>();
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			for(AddCollection aProduct:pList){
				AddCollectionVM aVm = new AddCollectionVM();
				aVm.title = aProduct.title;
				aVm.description = aProduct.description;
				aVm.fileName = aProduct.fileName;
				aVm.id = aProduct.id;
				aVm.orderIndex = aProduct.orderIndex;
				aVm.hideWebsite = aProduct.hideWebsite;
				if(aProduct.addedDate != null){
					aVm.addedDate = df.format(aProduct.addedDate);
				}
				List<Product> pro =Product.getAllProductById(aProduct.id);
				aVm.countProduct = pro.size();
				aVm.publicStatus = aProduct.publicStatus;
				List<CollectionImages> pImages = CollectionImages.getByProduct(aProduct); 
				aVm.countImages = pImages.size();
				
				String params = "&date=last-28-days&type=visitors-list&limit=all";
		    	Long visitorCount = 0l;
		    	
        		/*try {
    				JSONArray jsonArray = new JSONArray(Application.callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
    				for(int j=0;j<jsonArray.length();j++){
    	    			String data = jsonArray.getJSONObject(j).get("landing_page").toString();
    	    			String arr[] = data.split("/");
    	    			
    	    					  visitorCount = visitorCount + 1;
    	    				 
    				}	
    				
    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}*/
		    	aVm.pageViewCount = visitorCount;
				
				aList.add(aVm);
			}
			return ok(Json.toJson(aList));
		}

	   public static Result getAllProductData(String status) {
			List<AddCollection> pList = AddCollection.getProduct(Long.valueOf(session("USER_LOCATION")));
			List<AddCollectionVM> aList = new ArrayList<AddCollectionVM>();
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			for(AddCollection aProduct:pList){
				AddCollectionVM aVm = new AddCollectionVM();
				aVm.title = aProduct.title;
				aVm.description = aProduct.description;
				aVm.fileName = aProduct.fileName;
				aVm.id = aProduct.id;
				aVm.orderIndex = aProduct.orderIndex;
				aVm.hideWebsite = aProduct.hideWebsite;
				aVm.mainCollection = aProduct.mainCollection;
				if(aProduct.addedDate != null){
					aVm.addedDate = df.format(aProduct.addedDate);
				}
				
				aVm.publicStatus = aProduct.publicStatus;
				List<CollectionImages> pImages = CollectionImages.getByProduct(aProduct); 
				aVm.countImages = pImages.size();
				
				String params = "&date=last-28-days&type=visitors-list&limit=all";
		    	Long visitorCount = 0l;
       		
		    	aVm.pageViewCount = visitorCount;
				
				aList.add(aVm);
			}
			return ok(Json.toJson(aList));
		}

	   
	public static AuthUser getLocalUser() {
		String id = session("USER_KEY");
		AuthUser user = AuthUser.find.byId(Integer.parseInt(id));
		return user;
	}
	
	

}