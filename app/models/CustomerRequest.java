package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class CustomerRequest extends Model {

	@Id
	public Integer id;
	public String firstName;
	public Location location;
	public String redirectValue;
	public String personValue;
	
	
	
	public String getRedirectValue() {
		return redirectValue;
	}
	public void setRedirectValue(String redirectValue) {
		this.redirectValue = redirectValue;
	}
	public String getPersonValue() {
		return personValue;
	}
	public void setPersonValue(String personValue) {
		this.personValue = personValue;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public static Finder<Long,CustomerRequest> find = new Finder<>(Long.class,CustomerRequest.class);
	
	public static List<CustomerRequest> getAllPermission() {
		return find.all();
	}
	public static CustomerRequest getAllPermissionData() {
		return find.where().ne("id", null).findUnique();
	}
	public static List<CustomerRequest> getAllPermissionById() {
		return find.where().eq("parentId", null).findList();
	}
	
	public static List<CustomerRequest> getAllPermissionChildData(Integer id) {
		return find.where().eq("parentId", id).findList();
	}
	public static CustomerRequest findById(Long id) {
		return find.byId(id);
	}
}
