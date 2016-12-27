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
	
	@ManyToOne
	public InventorySetting mainCollection;
	@ManyToOne
	public AddProduct collection;
	@ManyToOne
	public AuthUser user;
	@ManyToOne
	public Location locations;
	
	
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
	public AddProduct getCollection() {
		return collection;
	}
	public void setCollection(AddProduct collection) {
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
	
	
	public static Finder<Long,Product> find = new Finder<>(Long.class,Product.class);
	
	public static Product findById(Long id) {
		return find.byId(id);
	}
	
	public static Product findAll() {
		return (Product) find.all();
	}
	
	public static List<Product> getAllProductByMainCollection(InventorySetting mainColl) {
		return find.where().eq("mainCollection", mainColl).findList();
	}
}
