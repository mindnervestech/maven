package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class CustomerRequestManufacturerSettings extends Model {

	@Id
	public Long id;
	
	@ManyToOne
	public AuthUser user;
	
	/*@ManyToOne
	public AddProduct manufacturer;*/
	@ManyToOne
	public InventorySetting manufacturer;
	@ManyToOne
	public Location locations;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

	
	public Location getLocations() {
		return locations;
	}

	public void setLocations(Location locations) {
		this.locations = locations;
	}

	public InventorySetting getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(InventorySetting manufacturer) {
		this.manufacturer = manufacturer;
	}

	public AuthUser getUser() {
		return user;
	}

	public void setUser(AuthUser user) {
		this.user = user;
	}

	public static Finder<Long,CustomerRequestManufacturerSettings> find = new Finder<>(Long.class,CustomerRequestManufacturerSettings.class);
	
	public static CustomerRequestManufacturerSettings findByUser(AuthUser user) {
		return find.where().eq("user", user).findUnique();
	}
	
	public static CustomerRequestManufacturerSettings findByLocation(Long location) {
		return find.where().eq("locations.id", location).findUnique();
	}
	public static CustomerRequestManufacturerSettings findById(Long id) {
		return find.byId(id);
	}
	public static List<CustomerRequestManufacturerSettings> getAllcustManufactList() {
		return find.all();
	}
	public static List<CustomerRequestManufacturerSettings> getcustManufact(Long id) {
		return find.where().eq("manufacturer.id", id).findList();
	}
}
