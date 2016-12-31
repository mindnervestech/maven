package viewmodel;

import java.util.ArrayList;
import java.util.List;

import models.InventorySetting;



public class AddCollectionVM {

    public Long collectionId;
	public String title;
	public String description;
	public String sale;
	public int countImages;
	public String filePath;
	public String newFlag;
	public String primaryTitle;
	public String cadfileName;
	public String cadfilePath;
	public String publicStatus;
	public Long parentId;
	public Long pageViewCount;
	public int orderIndex;
	public int hideWebsite;
	public int subhideWebsite;
	public String addedDate;
	public String externalUrlLink;
	public String imgPath;
	public long imgId;
	public List<UserVM> userData;
	public int countProduct;
	public InventorySetting mainCollection;	
	
	public InventorySetting getMainCollection() {
		return mainCollection;
	}
	public void setMainCollection(InventorySetting mainCollection) {
		this.mainCollection = mainCollection;
	}
	
	
	public String getExternalUrlLink() {
		return externalUrlLink;
	}
	public void setExternalUrlLink(String externalUrlLink) {
		this.externalUrlLink = externalUrlLink;
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
	public String getDesigner() {
		return designer;
	}
	
	public int getCountProduct() {
		return countProduct;
	}
	public void setCountProduct(int countProduct) {
		this.countProduct = countProduct;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getPublicStatus() {
		return publicStatus;
	}
	public void setPublicStatus(String publicStatus) {
		this.publicStatus = publicStatus;
	}
	public void setDesigner(String designer) {
		this.designer = designer;
	}
	public String year;
	public String designer;
	
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
	public int getCountImages() {
		return countImages;
	}
	public void setCountImages(int countImages) {
		this.countImages = countImages;
	}
	public String getSale() {
		return sale;
	}
	public void setSale(String sale) {
		this.sale = sale;
	}
	public double price;
	public double cost;
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String path;
	public Long id;
	public String imageName;
	public String collectionTitle;
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
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	
	
	public int getSubhideWebsite() {
		return subhideWebsite;
	}
	public void setSubhideWebsite(int subhideWebsite) {
		this.subhideWebsite = subhideWebsite;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCollectionTitle() {
		return collectionTitle;
	}
	public void setCollectionTitle(String collectionTitle) {
		this.collectionTitle = collectionTitle;
	}
	public Long getCollectionId() {
		return collectionId;
	}
	public void setCollectionId(Long collectionId) {
		this.collectionId = collectionId;
	}
	
	
}
