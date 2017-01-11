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
public class AddCollection extends Model {
	
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
	public String fileType;
	public String newFlag;
	public String primaryTitle;
	public String cadfileName;
	public String cadfilePath;
	public String publicStatus;
	public Long parentId;
	public int orderIndex;
	public int hideWebsite;
	public int subhideWebsite;
	public Date addedDate;
	public String externalUrlLink;
	public String status;
	
	@ManyToOne
	public InventorySetting mainCollection;	
	
	@ManyToOne
	public Location locations;
	
	@ManyToOne
	public AuthUser soldUser;
	
	
	public InventorySetting getMainCollection() {
		return mainCollection;
	}
	public void setMainCollection(InventorySetting mainCollection) {
		this.mainCollection = mainCollection;
	}
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
	
	
	public int getSubhideWebsite() {
		return subhideWebsite;
	}
	public void setSubhideWebsite(int subhideWebsite) {
		this.subhideWebsite = subhideWebsite;
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

	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	@ManyToOne
	public AddCollection collection;
	@ManyToOne
	public AuthUser user;
	
	public static Finder<Long,AddCollection> find = new Finder<>(Long.class,AddCollection.class);
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
	public static AddCollection findByUser(AuthUser userObj) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static List<AddCollection> findAllProduct() {
		// TODO Auto-generated method stub
		return null;
	}

	public static AddCollection findById(Long id) {
		return find.byId(id);
	}
	public static AddCollection findByIdData(Long id) {
		return  find.byId(id);
	}
	public static AddCollection findByIdNotSale(Long id) {
		return find.where().eq("id", id).ne("sale", "sale").findUnique();
	}
	public static AddCollection findByVinAndStatus(String vid) {
		return find.where().eq("vin", vid).eq("status", "Newly Arrived").findUnique();
	}
	public static AddCollection findByVinAndStat(String vid) {
		return find.where().eq("vin", vid).eq("publicStatus", "publish").findUnique();
	}
	
	public static AddCollection findByVinAndStatusForGM(String vid,Location location) {
		return find.where().eq("vin", vid).eq("status", "Newly Arrived").eq("locations", location).findUnique();
	}
	public static List<AddCollection> getAllAccessories(AddCollection collection) {
		return find.where().eq("collection", collection).findList();
	}
	public static List<AddCollection> getAllProducts() {
		return find.all();
	}
	public static List<AddCollection> getAllCollection(InventorySetting mainColl) {
		return find.where().eq("mainCollection", mainColl).eq("publicStatus", "publish").findList();
	}
	public static List<AddCollection> findProductsByCollection(AddCollection collection) {
		return find.where().eq("collection", collection).findList();
	}
	public static List<AddCollection> findProductsReadyMadeByCollection(AddCollection collection) {
		return find.where().ne("sale", "sale").eq("collection", collection).findList();
	}
	public static List<AddCollection> findProductsByCollectionSale(AddCollection collection) {
		return find.where().eq("sale", "sale").eq("collection", collection).findList();
	}
	public static List<AddCollection> findProductsByCollectiongetOnlyReadyMade(AddCollection collection) {
		return find.where().ne("sale", "sale").eq("collection", collection).findList();
	}
	public static List<AddCollection> findByProductId(String productname,Location location) {
		return find.where().eq("title", productname).ne("sale", "sale").findList();
	}
	public static List<AddCollection> findByTitle(String productname) {
		return find.where().eq("title", productname).eq("publicStatus", "publish").findList();
	}
	
	public static List<AddCollection> getProductByDraftStatusAndLocation(Long location) {
		return find.where().eq("publicStatus", "draft").findList();		
	}
	public static List<AddCollection> getProductByDraftStatus() {
		return find.where().eq("publicStatus", "publish").findList();		
	}
	public static List<AddCollection> getProductByParentId(Long id) {
		return find.where().eq("parentId", id).findList();		
	}
	public static List<AddCollection> getProductByParentIdStuts(Long id,String status) {
		return find.where().eq("publicStatus", status).eq("parentId", id).findList();		
	}
	
	public static List<AddCollection> getProductByStatus(Long location, String status) {
		return find.where().eq("publicStatus", status).orderBy("order_index").findList();		
	}
	public static List<AddCollection> getProductByStatusMain(Long location, String status) {
		return find.where().eq("publicStatus", status).eq("parent_id", null).orderBy("order_index").findList();		
	}
	
	public static List<AddCollection> getProductByStatusMainColl(Long location, String status,InventorySetting coll) {
		return find.where().eq("mainCollection", coll).eq("publicStatus", status).orderBy("order_index").findList();		
	}
	public static List<AddCollection> getProductByStatusMainCollData(Long location, String status,InventorySetting coll) {
		return find.where().eq("mainCollection", coll).eq("publicStatus", status).eq("parent_id", null).orderBy("order_index").findList();		
	}
	
	public static List<AddCollection> getProduct(Long location) {
		return find.where().ne("publicStatus", "deleted").orderBy("order_index").findList();		
	}
	public static List<AddCollection> getProductParent(Long location) {
		return find.where().ne("publicStatus", "deleted").eq("parent_id", null).orderBy("order_index").findList();		
	}
	public static AddCollection findByProductIdOne(String productname,Location location) {
		return find.where().eq("title", productname).ne("sale", "sale").findUnique();
	}
	public static List<AddCollection> findByLocationNoDraft(Long location) {
		return find.all();
	}
	public static List<AddCollection> findProductsNotSale(Long location) {
		return find.where().ne("sale", "sale").findList();
	}
	public static List<AddCollection> findByNewlyArrivedForGM(Location location) {
		return find.where().eq("status", "Newly Arrived").eq("locations",location).findList();
	}
	public static List<AddCollection> findByNewlyArrivedFo(Location location) {
		return find.where().eq("locations",location).findList();
	}
	public static List<AddCollection> findByNewlyArrived() {
		return find.where().eq("status", "Newly Arrived").findList();
	}
	
	public static List<AddCollection> findByVins(List<String> vins) {
		return find.where().in("vin", vins).eq("status", "Newly Arrived").eq("publicStatus","public").findList();
	}
	public static List<AddCollection> findByVId(List<String> vins) {
		return find.where().in("vin", vins).findList();
	}
	public static List<AddCollection> findByMainCollAndType(List<String> vins,InventorySetting typeColl) {
		return find.where().eq("publicStatus","publish").eq("mainCollection",typeColl).findList();
	}
	public static List<AddCollection> findByNotInVins(List<String> vins) {
		return find.where().not(Expr.in("vin", vins)).findList();
	}
	public static AddCollection getDefaultImg(Long id2) {
		return find.where().eq("id", id2).findUnique();
	}
	public static List<AddCollection> findByLocationAndSold(Long location) {
		return find.where().eq("locations.id", location).eq("status", "Sold").findList();
	}
	public static List<AddCollection> findBySoldUserAndSoldDate(AuthUser user,Date start,Date end) {
		return find.where().eq("soldUser", user).eq("status", "Sold").ge("soldDate",start).le("soldDate", end).findList();
	}
	public static List<AddCollection> findByLocationAndSoldDate(Location location,Date start,Date end) {
		return find.where().eq("locations",location).eq("status", "Sold").ge("soldDate",start).le("soldDate", end).findList();
	}
	public static List<AddCollection> findBySoldUserAndSold(AuthUser user) {
		return find.where().eq("soldUser", user).eq("status", "Sold").findList();
	}
	public static List<AddCollection> findByLocation(Long location) {
		return find.where().eq("locations.id", location).findList();
	}
	public static List<AddCollection> findBySold() {
		return find.where().eq("status", "Sold").findList();
	}
	public static List<AddCollection> findByNewArrAndLocation(Long location) {
		return find.where().eq("locations.id", location).eq("publicStatus", "public").eq("status", "Newly Arrived").findList();
	}
	public static List<AddCollection> findByUserAndNew(AuthUser user) {
		return find.where().eq("user", user).eq("status", "Newly Arrived").findList();
	}
	public static List<AddCollection> getAllProductByIdColl(Long id) {
		return find.where().eq("mainCollection.id", id).findList();
	}
}
