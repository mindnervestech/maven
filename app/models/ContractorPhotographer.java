package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class ContractorPhotographer extends Model {

	@Id
	public Long id;
	public String email;
	public String communicationemail;
	public String firstName;
	public String lastName;
	public String password;
	public String role;
	public String phone;
	public String age;
	
	



	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getCommunicationemail() {
		return communicationemail;
	}


	public void setCommunicationemail(String communicationemail) {
		this.communicationemail = communicationemail;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getAge() {
		return age;
	}


	public void setAge(String age) {
		this.age = age;
	}


	public static Finder<Long,ContractorPhotographer> find = new Finder<>(Long.class,ContractorPhotographer.class);
	
		
	public static List<ContractorPhotographer> findByLocation(Long location) {
		return find.where().eq("locations.id", location).findList();
	}
	
	
}
