package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class Product extends Model {
	
	@Id
	public Long id;
	public String secondaryTitle;
	public String primaryTitle;
	public String description;
	public String year;
	public String designer;
	public double price;
	public double cost;
	public String fileName;
	public String filePath;
	public String cadfileName;
	public String cadfilePath;	
	public boolean newFlag;
	public double amount;
	public boolean isAmountFlag;
	public String publicStatus;
	public String externalUrlLink;
	public String imageName;
	public String imagePath;
	public int hideWebsite;
	@ManyToOne
	public InventorySetting mainCollection;
	@ManyToOne
	public AddCollection collection;
	@ManyToOne
	public AuthUser user;
	@ManyToOne
	public Location locations;
	
	
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getPublicStatus() {
		return publicStatus;
	}
	public void setPublicStatus(String publicStatus) {
		this.publicStatus = publicStatus;
	}
	public String getExternalUrlLink() {
		return externalUrlLink;
	}
	public void setExternalUrlLink(String externalUrlLink) {
		this.externalUrlLink = externalUrlLink;
	}
	public boolean getNewFlag() {
		return newFlag;
	}
	public void setNewFlag(boolean newFlag) {
		this.newFlag = newFlag;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSecondaryTitle() {
		return secondaryTitle;
	}
	public void setSecondaryTitle(String secondaryTitle) {
		this.secondaryTitle = secondaryTitle;
	}
	public String getPrimaryTitle() {
		return primaryTitle;
	}
	public void setPrimaryTitle(String primaryTitle) {
		this.primaryTitle = primaryTitle;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getDesigner() {
		return designer;
	}
	public void setDesigner(String designer) {
		this.designer = designer;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getCadfileName() {
		return cadfileName;
	}
	public void setCadfileName(String cadfileName) {
		this.cadfileName = cadfileName;
	}
	public String getCadfilePath() {
		return cadfilePath;
	}
	public void setCadfilePath(String cadfilePath) {
		this.cadfilePath = cadfilePath;
	}
	public InventorySetting getMainCollection() {
		return mainCollection;
	}
	public void setMainCollection(InventorySetting mainCollection) {
		this.mainCollection = mainCollection;
	}
	public AddCollection getCollection() {
		return collection;
	}
	public void setCollection(AddCollection collection) {
		this.collection = collection;
	}
	public AuthUser getUser() {
		return user;
	}
	public void setUser(AuthUser user) {
		this.user = user;
	}
	public Location getLocations() {
		return locations;
	}
	public void setLocations(Location locations) {
		this.locations = locations;
	}
	
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}

	public boolean isAmountFlag() {
		return isAmountFlag;
	}
	public void setAmountFlag(boolean isAmountFlag) {
		this.isAmountFlag = isAmountFlag;
	}
	public int getHideWebsite() {
		return hideWebsite;
	}
	public void setHideWebsite(int hideWebsite) {
		this.hideWebsite = hideWebsite;
	}
	public static Finder<Long,Product> find = new Finder<>(Long.class,Product.class);
	
	public static Product findById(Long id) {
		return find.byId(id);
	}
	
	public static Product findAll() {
		return (Product) find.all();
	}
	
	public static List<Product> getAllProductByMainCollection(InventorySetting mainColl,String status) {
		return find.where().eq("mainCollection", mainColl).eq("publicStatus", status).findList();
	}
	public static List<Product> getAllProductById(Long id) {
		return find.where().eq("collection.id", id).findList();
	}
	
	public static List<Product> getAllProductByIdAndStatus(Long id) {
		return find.where().ne("mainCollection", null).eq("publicStatus","publish").eq("collection.id", id).findList();
	}
	public static List<Product> getProductByStatus(Long location, String status) {
		return find.where().eq("publicStatus", status).findList();		
	}
	public static List<Product> getProductByStatusMainColl(Long location, String status,InventorySetting coll) {
		return find.where().eq("mainCollection", coll).eq("publicStatus", status).findList();		
	}
	public static List<Product> getProductByDraftStatus() {
		return find.where().eq("publicStatus", "publish").findList();		
	}
	public static List<Product> findByMainCollAndType(List<String> vins,InventorySetting mainColl) {
		return find.where().eq("publicStatus","publish").eq("mainCollection",mainColl).findList();
	}
	public static Product getDefaultImg(Long id2) {
		return find.where().eq("id", id2).findUnique();
	}
	public static List<Product> getAllProductByIdMain(Long id) {
		return find.where().eq("mainCollection.id", id).findList();
	}
}
