package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class LeadType extends Model {

	@Id
	public Long id;
	public String leadName;
	
		
	@ManyToOne
	public Location locations;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLeadName() {
		return leadName;
	}

	public void setLeadName(String leadName) {
		this.leadName = leadName;
	}

	public Location getLocations() {
		return locations;
	}

	public void setLocations(Location locations) {
		this.locations = locations;
	}
	public static Finder<Long,LeadType> find = new Finder<>(Long.class,LeadType.class);
	
	public static LeadType findById(Long id) {
		return find.byId(id);
	}
	
	public static List<LeadType> findByLocation(Long location) {
		return find.where().eq("locations.id", location).findList();
	}
	public static LeadType findByLocations(Long location) {
		return find.where().eq("locations.id", location).findUnique();
	}
	
	
	
	
}
