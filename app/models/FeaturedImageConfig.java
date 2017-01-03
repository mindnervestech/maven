package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class FeaturedImageConfig extends Model {

	@Id
	public Long id;
	public Integer cropWidth;
	public Integer cropHeight;
	public String typeCollection;
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

	public Integer getCropWidth() {
		return cropWidth;
	}

	public void setCropWidth(Integer cropWidth) {
		this.cropWidth = cropWidth;
	}

	public String getTypeCollection() {
		return typeCollection;
	}

	public void setTypeCollection(String typeCollection) {
		this.typeCollection = typeCollection;
	}

	public Integer getCropHeight() {
		return cropHeight;
	}

	public void setCropHeight(Integer cropHeight) {
		this.cropHeight = cropHeight;
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



	public static Finder<Long,FeaturedImageConfig> find = new Finder<>(Long.class,FeaturedImageConfig.class);
	
	public static List<FeaturedImageConfig> findByUser() {
		return find.all();
	}
	public static FeaturedImageConfig findByUser(AuthUser user) {
		return find.where().eq("user", user).findUnique();
	}
	public static FeaturedImageConfig findByLocation(Long location) {
		return find.where().eq("locations.id", location).findUnique();
	}
}
