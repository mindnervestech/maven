package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class InventorySetting extends Model {

	@Id
	public Long id;
	public String collection;
	public Boolean enableInven;
	public Boolean hideWebsite;
	public String path;
	public String thumbPath;
	public String imageName;
	public String status;
	@ManyToOne
	public Location locations;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getThumbPath() {
		return thumbPath;
	}
	public void setThumbPath(String thumbPath) {
		this.thumbPath = thumbPath;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public Boolean getHideWebsite() {
		return hideWebsite;
	}
	public void setHideWebsite(Boolean hideWebsite) {
		this.hideWebsite = hideWebsite;
	}
	public Boolean getEnableInven() {
		return enableInven;
	}
	public void setEnableInven(Boolean enableInven) {
		this.enableInven = enableInven;
	}
	public String getCollection() {
		return collection;
	}
	public void setCollection(String collection) {
		this.collection = collection;
	}
	public Location getLocations() {
		return locations;
	}

	public void setLocations(Location locations) {
		this.locations = locations;
	}
	
	public static Finder<Long,InventorySetting> find = new Finder<>(Long.class,InventorySetting.class);
	
	public static InventorySetting findById(Long id) {
		return find.byId(id);
	}
	
	public static InventorySetting findByName(String name) {
		return find.where().eq("collection", name).findUnique();
	}
	public static List<InventorySetting> getAllCollection() {
		return find.all();
	}
	public static List<InventorySetting> findByLocation(Long location) {
		return find.where().eq("locations.id", location).eq("status", null).findList();
	}
	public static InventorySetting getByImagePath(String path) {
		return find.where().eq("path", path).findUnique();
	}
	
}