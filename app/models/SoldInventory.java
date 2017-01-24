package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class SoldInventory extends Model {

	@Id
	public Long id;
	
	public Date soldDate;
	public String price;
	public String saveLeadTypeAs;
	public String collectionId;
	
	
	@ManyToOne
	public AuthUser user;
	
	@ManyToOne
	public Location locations;
	@ManyToOne
	public RequestMoreInfo requestMoreInfo;
	
	

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getSaveLeadTypeAs() {
		return saveLeadTypeAs;
	}
	public void setSaveLeadTypeAs(String saveLeadTypeAs) {
		this.saveLeadTypeAs = saveLeadTypeAs;
	}
	public String getCollectionId() {
		return collectionId;
	}
	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}
	public Date getSoldDate() {
		return soldDate;
	}
	public void setSoldDate(Date soldDate) {
		this.soldDate = soldDate;
	}
	
	public RequestMoreInfo getRequestMoreInfo() {
		return requestMoreInfo;
	}
	public void setRequestMoreInfo(RequestMoreInfo requestMoreInfo) {
		this.requestMoreInfo = requestMoreInfo;
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
	
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	

	public static Finder<Long,SoldInventory> find = new Finder<>(Long.class,SoldInventory.class);
	
	public static List<SoldInventory> getAllSoldContacts() {
		return find.all();
	}
	
	public static SoldInventory findById(Long id) {
		return find.byId(id);
	}
	public static List<SoldInventory> findBySold() {
		return find.all();
	}
	public static List<SoldInventory> findByLocationAndSold(Long location) {
		return find.where().eq("locations.id", location).findList();
	}
	public static List<SoldInventory> findBySoldUserAndSold(AuthUser user) {
		return find.where().eq("user", user).findList();
	}
	public static List<SoldInventory> findByMakeAndSoldLocation(String title, Long location) {
		return find.where().eq("locations.id", location).eq("collectionId", title).findList();
	}
	public static List<SoldInventory> findByMakeAndSold(String title, AuthUser user) {
		return find.where().eq("soldUser", user).eq("collectionId", title).findList();
	}
}
