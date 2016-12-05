package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class SalesPersonZipCode extends Model {

	@Id
	public Long id;
	public String zipCode;
	public String city;
	public String state;
	@ManyToOne
	public AuthUser user;
	
	
	@ManyToOne
	public Location locations;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public Location getLocations() {
		return locations;
	}

	public void setLocations(Location locations) {
		this.locations = locations;
	}


	public AuthUser getUser() {
		return user;
	}

	public void setUser(AuthUser user) {
		this.user = user;
	}

	public static Finder<Long,SalesPersonZipCode> find = new Finder<>(Long.class,SalesPersonZipCode.class);
	
	public static SalesPersonZipCode findByUser(AuthUser user) {
		return find.where().eq("user", user).findUnique();
	}
	
	public static SalesPersonZipCode findByLocation(Long location) {
		return find.where().eq("locations.id", location).findUnique();
	}
	public static SalesPersonZipCode findById(Long id) {
		return find.byId(id);
	}
	public static List<SalesPersonZipCode> getAllcustManufactList() {
		return find.all();
	}
}
