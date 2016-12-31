package viewmodel;

import models.AuthUser;
import models.InventorySetting;
import models.Location;

public class ProductVM {

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
	public Long mainCollection;
	public Long collection;
	public AuthUser user;
	public Location locations;
	public double amount;
	public boolean isAmountFlag;
	public String externalUrlLink;
	public String publicStatus;
	public InventorySetting mainCollecti;
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
	
	public String getExternalUrlLink() {
		return externalUrlLink;
	}
	public void setExternalUrlLink(String externalUrlLink) {
		this.externalUrlLink = externalUrlLink;
	}
	public String getPublicStatus() {
		return publicStatus;
	}
	public void setPublicStatus(String publicStatus) {
		this.publicStatus = publicStatus;
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
	public boolean isNewFlag() {
		return newFlag;
	}
	public void setNewFlag(boolean newFlag) {
		this.newFlag = newFlag;
	}
	public Long getMainCollection() {
		return mainCollection;
	}
	public void setMainCollection(Long mainCollection) {
		this.mainCollection = mainCollection;
	}
	public Long getCollection() {
		return collection;
	}
	public void setCollection(Long collection) {
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
	public boolean isAmountFlag() {
		return isAmountFlag;
	}
	public void setAmountFlag(boolean isAmountFlag) {
		this.isAmountFlag = isAmountFlag;
	}
	
	
	
}
