package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class State extends Model {

	@Id
	public Long id;
	public String stateCode;
	

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStateCode() {
		return stateCode;
	}
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}


	public static Finder<Long,State> find = new Finder<>(Long.class,State.class);
	
	public static State findByUser(AuthUser user) {
		return find.where().eq("user", user).findUnique();
	}
	
	public static State findByLocation(Long location) {
		return find.where().eq("locations.id", location).findUnique();
	}
	public static State findById(Long id) {
		return find.byId(id);
	}
	public static List<State> getAllStateCodes() {
		return find.all();
	}
}
