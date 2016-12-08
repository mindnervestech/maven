package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Expr;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class AddProduct extends Model {
	
	@Id
	public Long id;
	public String title;
	public String description;
	public double price;
	public double cost;
	public Date soldDate;
	public String year;
	public String designer;
	public String sale;
	public String filePath;
	public String newFlag;
	public String primaryTitle;
	public String cadfileName;
	public String cadfilePath;
	public String publicStatus;
	public Long parentId;
	public int orderIndex;
	public int hideWebsite;
	public Date addedDate;
	public String externalUrlLink;
	public String status;
	
	
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getExternalUrlLink() {
		return externalUrlLink;
	}
	public void setExternalUrlLink(String externalUrlLink) {
		this.externalUrlLink = externalUrlLink;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@ManyToOne
	public Location locations;
	
	@ManyToOne
	public AuthUser soldUser;
	
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public int getOrderIndex() {
		return orderIndex;
	}
	public void setOrderIndex(int orderIndex) {
		this.orderIndex = orderIndex;
	}
	
	public Date getAddedDate() {
		return addedDate;
	}
	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}
	public int getHideWebsite() {
		return hideWebsite;
	}
	public void setHideWebsite(int hideWebsite) {
		this.hideWebsite = hideWebsite;
	}
	public String getCadfileName() {
		return cadfileName;
	}
	public void setCadfileName(String cadfileName) {
		this.cadfileName = cadfileName;
	}
	
	public String getPublicStatus() {
		return publicStatus;
	}
	public void setPublicStatus(String publicStatus) {
		this.publicStatus = publicStatus;
	}
	public String getCadfilePath() {
		return cadfilePath;
	}
	public void setCadfilePath(String cadfilePath) {
		this.cadfilePath = cadfilePath;
	}
	public String getPrimaryTitle() {
		return primaryTitle;
	}
	public void setPrimaryTitle(String primaryTitle) {
		this.primaryTitle = primaryTitle;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	
	public Location getLocations() {
		return locations;
	}
	public void setLocations(Location locations) {
		this.locations = locations;
	}
	public String getDesigner() {
		return designer;
	}
	public void setDesigner(String designer) {
		this.designer = designer;
	}
	

	public Date getSoldDate() {
		return soldDate;
	}
	public void setSoldDate(Date soldDate) {
		this.soldDate = soldDate;
	}
	public AuthUser getSoldUser() {
		return soldUser;
	}
	public void setSoldUser(AuthUser soldUser) {
		this.soldUser = soldUser;
	}

	
	public String getNewFlag() {
		return newFlag;
	}
	public void setNewFlag(String newFlag) {
		this.newFlag = newFlag;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String fileName;
	
	public String getSale() {
		return sale;
	}
	public void setSale(String sale) {
		this.sale = sale;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}

	@ManyToOne
	public AddCollection collection;
	@ManyToOne
	public AuthUser user;
	
	public static Finder<Long,AddProduct> find = new Finder<>(Long.class,AddProduct.class);
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public static AddProduct findByUser(AuthUser userObj) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static List<AddProduct> findAllProduct() {
		// TODO Auto-generated method stub
		return null;
	}

	public static AddProduct findById(Long id) {
		return find.byId(id);
	}
	public static AddProduct findByIdData(Long id) {
		return  find.byId(id);
	}
	public static AddProduct findByIdNotSale(Long id) {
		return find.where().eq("id", id).ne("sale", "sale").findUnique();
	}
	public static AddProduct findByVinAndStatus(String vid) {
		return find.where().eq("vin", vid).eq("status", "Newly Arrived").findUnique();
	}
	public static AddProduct findByVinAndStat(String vid) {
		return find.where().eq("vin", vid).eq("publicStatus", "publish").findUnique();
	}
	
	public static AddProduct findByVinAndStatusForGM(String vid,Location location) {
		return find.where().eq("vin", vid).eq("status", "Newly Arrived").eq("locations", location).findUnique();
	}
	public static List<AddProduct> getAllAccessories(AddCollection collection) {
		return find.where().eq("collection", collection).findList();
	}
	public static List<AddProduct> getAllProducts() {
		return find.all();
	}
	public static List<AddProduct> findProductsByCollection(AddCollection collection) {
		return find.where().eq("collection", collection).findList();
	}
	public static List<AddProduct> findProductsReadyMadeByCollection(AddCollection collection) {
		return find.where().ne("sale", "sale").eq("collection", collection).findList();
	}
	public static List<AddProduct> findProductsByCollectionSale(AddCollection collection) {
		return find.where().eq("sale", "sale").eq("collection", collection).findList();
	}
	public static List<AddProduct> findProductsByCollectiongetOnlyReadyMade(AddCollection collection) {
		return find.where().ne("sale", "sale").eq("collection", collection).findList();
	}
	public static List<AddProduct> findByProductId(String productname,Location location) {
		return find.where().eq("title", productname).ne("sale", "sale").findList();
	}
	public static List<AddProduct> findByTitle(String productname) {
		return find.where().eq("title", productname).eq("publicStatus", "publish").findList();
	}
	
	public static List<AddProduct> getProductByDraftStatusAndLocation(Long location) {
		return find.where().eq("publicStatus", "draft").findList();		
	}
	public static List<AddProduct> getProductByDraftStatus() {
		return find.where().eq("publicStatus", "publish").findList();		
	}
	public static List<AddProduct> getProductByParentId(Long id) {
		return find.where().eq("parentId", id).findList();		
	}
	public static List<AddProduct> getProductByStatus(Long location, String status) {
		return find.where().eq("publicStatus", status).orderBy("order_index").findList();		
	}
	public static List<AddProduct> getProduct(Long location) {
		return find.where().ne("publicStatus", "deleted").orderBy("order_index").findList();		
	}
	
	public static AddProduct findByProductIdOne(String productname,Location location) {
		return find.where().eq("title", productname).ne("sale", "sale").findUnique();
	}
	public static List<AddProduct> findByLocationNoDraft(Long location) {
		return find.all();
	}
	public static List<AddProduct> findProductsNotSale(Long location) {
		return find.where().ne("sale", "sale").findList();
	}
	public static List<AddProduct> findByNewlyArrivedForGM(Location location) {
		return find.where().eq("status", "Newly Arrived").eq("locations",location).findList();
	}
	public static List<AddProduct> findByNewlyArrivedFo(Location location) {
		return find.where().eq("locations",location).findList();
	}
	public static List<AddProduct> findByNewlyArrived() {
		return find.where().eq("status", "Newly Arrived").findList();
	}
	
	public static List<AddProduct> findByVins(List<String> vins) {
		return find.where().in("vin", vins).eq("status", "Newly Arrived").eq("publicStatus","public").findList();
	}
	public static List<AddProduct> findByVId(List<String> vins) {
		return find.where().in("vin", vins).findList();
	}
	public static List<AddProduct> findByVinsAndTypeVehi(List<String> vins,String typeVehi) {
		return find.where().in("vin", vins).eq("status", "Newly Arrived").eq("publicStatus","public").eq("typeofVehicle",typeVehi).findList();
	}
	public static List<AddProduct> findByNotInVins(List<String> vins) {
		return find.where().not(Expr.in("vin", vins)).findList();
	}
	public static AddProduct getDefaultImg(Long id2) {
		return find.where().eq("id", id2).findUnique();
	}
	public static List<AddProduct> findByLocationAndSold(Long location) {
		return find.where().eq("locations.id", location).eq("status", "Sold").findList();
	}
	public static List<AddProduct> findBySoldUserAndSoldDate(AuthUser user,Date start,Date end) {
		return find.where().eq("soldUser", user).eq("status", "Sold").ge("soldDate",start).le("soldDate", end).findList();
	}
	public static List<AddProduct> findByLocationAndSoldDate(Location location,Date start,Date end) {
		return find.where().eq("locations",location).eq("status", "Sold").ge("soldDate",start).le("soldDate", end).findList();
	}
	public static List<AddProduct> findBySoldUserAndSold(AuthUser user) {
		return find.where().eq("soldUser", user).eq("status", "Sold").findList();
	}
	public static List<AddProduct> findByLocation(Long location) {
		return find.where().eq("locations.id", location).findList();
	}
}
