package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class CustomizationForm extends Model {

	@Id
	public Long id;
	public String jsonData;
	public String dataType;
	
		
	@ManyToOne
	public Location locations;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Location getLocations() {
		return locations;
	}

	public void setLocations(Location locations) {
		this.locations = locations;
	}
	public static Finder<Long,CustomizationForm> find = new Finder<>(Long.class,CustomizationForm.class);
	
	public static CustomizationForm findById(Long id) {
		return find.byId(id);
	}
	
	public static List<CustomizationForm> findByLocation(Long location) {
		return find.where().eq("locations.id", location).findList();
	}
	public static CustomizationForm findByLocationsAndType(Long location,String dataType) {
		return find.where().eq("locations.id", location).eq("dataType", dataType).findUnique();
	}
	
	
	
	
}
