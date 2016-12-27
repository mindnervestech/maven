package controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTPClient;

import com.tinify.Options;
import com.tinify.Source;
import com.tinify.Tinify;

import models.AddCollection;
import models.AddProduct;
import models.AuthUser;
import models.InventorySetting;
import models.Location;
import models.Product;
import models.ProductImages;
import play.Play;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import viewmodel.AddCollectionVM;
import viewmodel.AddProductVM;
import viewmodel.ImageVM;
import viewmodel.ProductVM;
import viewmodel.SpecificationVM;
import views.html.home;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;

/*----------*/
import securesocial.core.Identity;





public class productController extends Controller {
  
	final static String rootDir = Play.application().configuration()
			.getString("image.storage.path");
	
	final static String tinifyKey = Play.application().configuration()
			.getString("tinifyKey");
	
	
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
    
/*	public static Result getSectionCollection(Long id){
		
		
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
			return ok(home.render("",userRegistration));
		} else {
			AuthUser userObj = (AuthUser) getLocalUser();
			Sections section = Sections.findById(id);
			List<AddCollectionVM> collectionVM = new ArrayList<>();
			List<AddCollection> collection = AddCollection.getAllByName(section.title);
			
			for (AddCollection add : collection) {
				AddCollectionVM vm =new AddCollectionVM();
				System.out.println(add.id);
				vm.id = add.id;
				vm.title = add.title;
				collectionVM.add(vm);
			}
			return ok(Json.toJson(collectionVM));
		}
	}*/
	
	 
	/* public static Result getSections(){
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
				return ok(home.render("",userRegistration));
			} else {
				
				List<SectionsVM> list = new ArrayList<>();
				List<Sections> sections = Sections.findAll();
				for (Sections sec : sections) {
					SectionsVM vm =new SectionsVM();
					vm.id = sec.id;
					vm.value = sec.value;
					vm.title = sec.title;
					list.add(vm);
				}
				return ok(Json.toJson(list));
			}
		}*/
	
	
	 public static AuthUser getLocalUser() {
	    	String id = session("USER_KEY");
	    	AuthUser user = AuthUser.find.byId(Integer.parseInt(id));
			return user;
		}
	 
	 public static Result getAllProductByCollection(Long id){
		 InventorySetting mainColl = InventorySetting.findById(id);
		 List<Product> collectionList = Product.getAllProductByMainCollection(mainColl);
		 for (Product product : collectionList) {
			
		}
		 return ok(Json.toJson(collectionList));
	 }
	 
	 public static Result saveNewProduct(){
		 try {
			 AuthUser userObj = (AuthUser) getLocalUser();
			 MultipartFormData body = request().body().asMultipartFormData();
			 Form<ProductVM> form = DynamicForm.form(ProductVM.class).bindFromRequest();
			 ProductVM vm = form.get();
			 FilePart pdfFile = null ;
			 
			 Product product = new Product();
			 product.secondaryTitle = vm.secondaryTitle;
			 product.primaryTitle = vm.primaryTitle;
			 product.description = vm.description;
			 product.designer = vm.designer;
			 product.price = vm.price;
			 product.cost = vm.cost;
			 product.newFlag = vm.newFlag;
			 product.year = vm.year;
			 product.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
			 product.user = userObj;
			 if(vm.mainCollection != null)
				 product.mainCollection = InventorySetting.findById(vm.mainCollection);
			 if(vm.collection != null)
				 product.collection = AddProduct.findById(vm.collection);
			 product.save();
			 
			 if(body != null){			 
		    		List<FilePart> filePart =  body.getFiles();
		    		if (filePart != null) {
		    			if(filePart.size() > 0){   				
		    	    		for(int i= 0; i<filePart.size(); i++){	    	    			
				    	    		 pdfFile = filePart.get(i);	
				       				 String fileName = pdfFile.getFilename().replaceAll("[-+^:,() ]","");
				       	       		 System.out.println(fileName);
				       	     		 String ext = FilenameUtils.getExtension(fileName);
				       	     		 System.out.println(ext);
				       	       		 fileName = product.id +"_"+ fileName;
				       	       		 
				       	       	    String contentType = pdfFile.getContentType(); 
				       	       	    File fdir = new File(rootDir+File.separator+"productFiles"+File.separator+product.id);
				       	       	    if(!fdir.exists()) {
				       	       	    	fdir.mkdir();
				       	       	    }   	       	
				       	       	    String filePath = rootDir+File.separator+"productFiles"+File.separator+product.id+File.separator+fileName;
				       	       	    try {
				       	       	    	Boolean sts = FileUtils.deleteQuietly(new File(filePath));
				       	       	    	System.out.println("delete "+sts);
				       	       	    } catch (Exception e) {
				       	       	    	e.printStackTrace();
				       	       	    }
					       	       	try {
						       	       	File file = pdfFile.getFile();
						       	       	if(ext.equalsIgnoreCase("pdf")){
				       	       	    		FileUtils.moveFile(file, new File(filePath));
				       	       	    		product.setFileName(fileName);
				       	       	    		product.setFilePath(File.separator+"productFiles"+File.separator+product.id+File.separator+fileName);
				       	       	    		product.update();			       		    		   		
				       	       	    	}else if(ext.equalsIgnoreCase("cad")||ext.equalsIgnoreCase("zip")||ext.equalsIgnoreCase("rar")){
				       	       	    		FileUtils.moveFile(file, new File(filePath));
				       	       	    		product.setCadfileName(fileName);
				       	       	    		product.setCadfilePath(File.separator+"productFiles"+File.separator+product.id+File.separator+fileName);
				       	       	    		product.update();
				       	       	    	}
				       	       	    } catch (Exception e) {
				       	       	    	e.printStackTrace();
				       	       	    }			       	       	    
		    	    		}
		    			}
		            }
		    	}
			 return ok("success");
		} catch (Exception e) {
			 return ok("error");
		}		
	 }
	 
	 
	 public static Result addProduct(){
			
			MultipartFormData body = request().body().asMultipartFormData();
	    	Form<AddProductVM> form = DynamicForm.form(AddProductVM.class).bindFromRequest();
	    	AddProductVM vm = form.get();
	    	FilePart pdfFile = null ;
	    	Tinify.setKey(tinifyKey);
	     	
	    	
	    	Source source;
			/*try {
				source = Tinify.fromFile("https://glider-autos.com/MavenImg/images/336920109/userPhoto/11-167x119.jpg");
				source.toFile("/public/emailTemplate/2.jpg");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
	    		 
	    	SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			Date curDate = new Date();
	    	
	    	AuthUser userObj = (AuthUser) getLocalUser();
	    	AddProductVM productVM = new AddProductVM();
	    	if(body != null){
	    		List<FilePart> filePart =  body.getFiles();
	    		if (filePart != null) {
	    			if(filePart.size() > 0){
	    				AddProduct add = new AddProduct ();
	    				
	    	    		add.title = vm.title;
	    	    		add.description =vm.description;
	    	    		add.addedDate = curDate;
	    	    		add.publicStatus = vm.publicStatus;
	    	    		add.parentId = vm.parentId;
	    	    		add.externalUrlLink = vm.externalUrlLink;
	    	    		add.user = userObj;
	    	    		add.mainCollection = vm.mainCollection;
	    	    		add.save();
	    	    		
	    	    		productVM.id = add.id;
	    				
	    	    		for(int i= 0; i<filePart.size(); i++){
	    	    			
			    	    		 pdfFile = filePart.get(i);
			       				 String fileName = pdfFile.getFilename().replaceAll("[-+^:,() ]","");
			       	       		 System.out.println(fileName);
			       	     		 String ext = FilenameUtils.getExtension(fileName);
			       	     		 System.out.println(ext);
			       	       		 fileName = vm.getTitle() +"_"+ fileName;
			       	       		 
			       	       	    String contentType = pdfFile.getContentType(); 
			       	       	    File fdir = new File(rootDir+File.separator+add.getId()+File.separator+userObj.id+File.separator+"Logo");
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
			       	       	    File file = pdfFile.getFile();
			       	       	    try {
			       	       	    	
			       	       	    	if(ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("jpeg") || ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("gif")){
			       	       	    		FileUtils.moveFile(file, new File(filePath));
			       		    		   		AddProduct obj = AddProduct.findById(productVM.id);
			       		    		   		obj.setFileName(fileName);
			       		    		   		obj.setFilePath("/"+add.getId()+"-"+userObj.id+"/"+"Logo"+"/"+fileName);
			       		    		   		obj.update();
			       		    		   	try {
											source = Tinify.fromFile(rootDir+File.separator+add.getId()+"-"+userObj.id+File.separator+"Logo"+File.separator+fileName);
											source.toFile(rootDir+File.separator+add.getId()+"-"+userObj.id+File.separator+"Logo"+File.separator+fileName);
											
											/*source = Tinify.fromFile(rootDir+File.separator+add.getId()+"-"+userObj.id+File.separator+"Logo"+File.separator+fileName);
											Options options = new Options()
											    .with("method", "fit")
											    .with("width", 250)
											    .with("height", 200);
											Source resized = source.resize(options);
											resized.toFile(rootDir+File.separator+add.getId()+"-"+userObj.id+File.separator+"Logo"+File.separator+"thumbnail.jpg");*/
										} catch (IOException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
			       		    		   	
			       		    		   		
			       	       	    	}
			       	       	    		
			       	       	    		
			       	       	  } catch (FileNotFoundException e) {
			       	     			e.printStackTrace();
			       	    	  		} catch (IOException e) {
			       	    	  			e.printStackTrace();
			       	    	  		}
	    	    		}
	    			}
	            }
	    	}else{
		   		AddProduct add = new AddProduct ();
	    		add.title = vm.title;
	    		add.description =vm.description;
	    		add.publicStatus = "draft";
	    		add.parentId = vm.parentId;
	    		add.addedDate = curDate;
	    		add.user = userObj;
	    		add.mainCollection = vm.mainCollection;
	    		add.save();
	    		productVM.id = add.id;
	    	}
	    		
	    		return ok(Json.toJson(productVM));
	    }
	 
	 
	  public static Result removeDefaultProduct(Long newId) {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	
		    	ProductImages newImage = ProductImages.findById(newId);
		    	
		    	List<ProductImages> pImages = ProductImages.getDeleteImagePath(newImage.getProduct().getId());
		    	for(ProductImages pimg:pImages){
		    		pimg.defaultImage = (false);
		    		pimg.update();
		    	}
		    	newImage.defaultImage = (true);
		    	newImage.update();
		    	return ok();
	    	}	
	    }
	  
	 public static Result uploadProductPhotos() {
	    	
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	MultipartFormData body = request().body().asMultipartFormData();
		    	String productId = request().getHeader("id");
		    	Long id = Long.parseLong(productId);
		    	Identity user = getLocalUser();
		    	AuthUser userObj = (AuthUser)user;
		    	String fileName = null;

		    	Tinify.setKey(tinifyKey);
		    	
		    	Source source;
		    	Source source1;

		    	
		    	FilePart picture = body.getFile("file");
		    	  if (picture != null) {
		    	    AddProduct aProduct = AddProduct.findById(id);
		    	    aProduct.setPublicStatus("publish");
		    	    aProduct.update();
		    	    
		    	    fileName = picture.getFilename().replaceAll("[-+^:,() ]","");
		    	    String contentType = picture.getContentType(); 
		    	    File fdir = new File(rootDir+File.separator+id+"-"+userObj.id);
		    	    if(!fdir.exists()) {
		    	    	fdir.mkdir();
		    	    }
		    	    String filePath = rootDir+File.separator+id+"-"+userObj.id+File.separator+fileName;
		    	    String thumbnailPath = rootDir+File.separator+id+"-"+userObj.id+File.separator+"thumbnail_"+fileName;
		    	    File thumbFile = new File(thumbnailPath);
		    	    File file = picture.getFile();
		    	    try {
		    	    	System.out.println("????????");
		    	    	System.out.println(filePath);
		    	    BufferedImage originalImage = ImageIO.read(file);
		    	    Thumbnails.of(originalImage).size(150, 150).toFile(thumbFile);
		    	    File _f = new File(filePath);
					Thumbnails.of(originalImage).scale(1.0).toFile(_f);
					
					source = Tinify.fromFile(filePath);
					source.toFile(filePath);
					source = Tinify.fromFile(thumbnailPath);
					source.toFile(thumbnailPath);
					
					ProductImages imageObj = ProductImages.getByImagePath("/"+id+"-"+userObj.id+"/"+fileName);
					if(imageObj == null) {
						ProductImages vImage = new ProductImages();
						vImage.product = AddProduct.findById(id);
						vImage.imageName = fileName.replaceAll(" ","%20");
						vImage.path = "/"+id+"-"+userObj.id+"/"+fileName;
						vImage.path = vImage.path.replaceAll(" ","%20");
						vImage.thumbPath = "/"+id+"-"+userObj.id+"/"+"thumbnail_"+fileName;
						vImage.thumbPath = vImage.thumbPath.replaceAll(" ","%20");
						vImage.user = userObj;
						vImage.save();
						
						/*try {
							source = Tinify.fromFile(filePath);
							source.toFile(thumbnailPath);
						
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}*/
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
	 
	 public static Result getImagesByProduct(Long id) {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	Identity user = getLocalUser();
		    	AuthUser userObj = (AuthUser)user;
		    	AddProduct product = AddProduct.findById(id);
		    	List<ProductImages> imageList = ProductImages.getByProduct(product);
		    	//reorderImagesForFirstTime(imageList);
		    	List<ImageVM> vmList = new ArrayList<>();
		    	for(ProductImages image : imageList) {
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
	 
	 
	 public static Result getImageById(Long id, String type) {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	File file = null;
		    	AddProduct image = AddProduct.findById(id);
		    	if(type.equals("thumbnail")) {
			    	file = new File(rootDir+image.filePath.replaceAll("%20"," "));
		    	}
		    	
		    	if(type.equals("full")) {
		    		file = new File(rootDir+image.filePath.replaceAll("%20"," "));
		    	}
		    	return ok(file);
	    	}	
	    }
	 
	 public static Result getImageAll(Long id, String type) {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	File file = null;
		    	ProductImages image = ProductImages.findById(id);
		    	if(type.equals("thumbnail")) {
			    	file = new File(rootDir+image.thumbPath.replaceAll("%20"," "));
		    	}
		    	
		    	if(type.equals("full")) {
		    		file = new File(rootDir+image.path.replaceAll("%20"," "));
		    	}
		    	return ok(file);
	    	}	
	    }
	 
	 public static Result saveImageTitle() {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		Form<ProductImages> form = DynamicForm.form(ProductImages.class).bindFromRequest();
	    		ProductImages vm = form.get();
		    	ProductImages image = ProductImages.findById(vm.id);
		    	if("undefined".equalsIgnoreCase(vm.description) || "".equalsIgnoreCase(vm.description) || "null".equalsIgnoreCase(vm.description))
		    		vm.description = null;
		    	if("undefined".equalsIgnoreCase(vm.title) || "".equalsIgnoreCase(vm.title) || "null".equalsIgnoreCase(vm.title))
		    		vm.title = null;
		    	image.setDescription(vm.description);
		    	image.setTitle(vm.title);
		    	image.update();
		    	return ok();
	    	}
	    }
	 
	 
	 public static Result deleteImage(Long id) {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	AuthUser user = (AuthUser) getLocalUser();
		    	ProductImages image = ProductImages.findById(id);
		    	File file = new File(rootDir+File.separator+id+"-"+user.id+File.separator+image.imageName);
		    	File thumbFile = new File(rootDir+File.separator+id+"-"+user.id+File.separator+"thumbnail_"+image.imageName);
		    	file.delete();
		    	thumbFile.delete();
		    	image.delete();
		    	return ok();
	    	}
	    }
	 
	 
	 /* 
	  
	  
	  
	  
	  public static Result getImageDataById(Long id) throws IOException {
		  if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		AuthUser user = (AuthUser) getLocalUser();
	    		ProductImages image = ProductImages.findById(id);
		    	File file = new File(rootDir+image.path);
		    	
		    	BufferedImage originalImage = ImageIO.read(file);
		    	//FeaturedImageConfig featuredConfig = FeaturedImageConfig.findByUser(user);
		    	List<FeaturedImageConfig> featuredCon = FeaturedImageConfig.findByUser();
		    	FeaturedImageConfig featuredConfig = new FeaturedImageConfig();
		    	featuredConfig = featuredCon.get(0);
		    	ImageVM vm = new ImageVM();
				vm.id = image.id;
				vm.imgName = image.imageName;
				vm.defaultImage = image.defaultImage;
				vm.row = originalImage.getHeight();
				vm.col = originalImage.getWidth();
				vm.path = image.path;
	    		vm.height = featuredConfig.cropHeight;
	    		vm.width = featuredConfig.cropWidth;
				//vm.vin = image.vin;
		    	return ok(Json.toJson(vm));
	    	}	
	    }
	  
	  public static Result editImage() throws IOException {
		  if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	AuthUser user = (AuthUser) getLocalUser();
		    	Form<EditImageVM> form = DynamicForm.form(EditImageVM.class).bindFromRequest();
		    	EditImageVM vm = form.get();
		    	
		    	ProductImages image = ProductImages.findById(vm.imageId);
		    	File file = new File(rootDir+image.path);
		    	File thumbFile = new File(rootDir+image.thumbPath);
		    	
		    	BufferedImage originalImage = ImageIO.read(file);
		    	BufferedImage croppedImage = originalImage.getSubimage(vm.x.intValue(), vm.y.intValue(), vm.w.intValue(), vm.h.intValue());
		    	Thumbnails.of(croppedImage).scale(1.0).toFile(file);
		    	
		    	Thumbnails.of(croppedImage).height(155).toFile(thumbFile);
		    	
		    	return ok();
	    	}	
	    }*/
	
	}