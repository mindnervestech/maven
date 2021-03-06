package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import play.db.ebean.Model;

@Entity
public class Location extends Model {

	@Id
	public Long id;
	public String name;
	public String email;
	public String address;
	public String phone;
	public String imageName;
	public String imageUrl;
	public String type;
	public Date createdDate;
	public String time_zone;
	
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}


	@OneToOne
	public AuthUser manager;
	

	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public static Finder<Long,Location> find = new Finder<>(Long.class,Location.class);
	
	
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getPhone() {
		return phone;
	}


	public String getImageName() {
		return imageName;
	}


	public void setImageName(String imageName) {
		this.imageName = imageName;
	}


	public String getTime_zone() {
		return time_zone;
	}
	public void setTime_zone(String time_zone) {
		this.time_zone = time_zone;
	}
	public String getImageUrl() {
		return imageUrl;
	}


	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}

	public static Location findById(Long id) {
		return find.byId(id);
	}
	
	
	public static List<Location> findAllData() {
		return find.all();
	}
	public static List<Location> findAllActiveType() {
		return find.where().eq("type", "active").findList();
	}
	public static List<Location> findAllDeactiveType() {
		return find.where().eq("type", "deactive").findList();
	}

	public static Location findManagerType(AuthUser user) {
		return find.where().eq("type", "active").eq("manager", user).findUnique();
	}
	
	public static Location findLocationByName(String name) {
		return find.where().eq("name", name).findUnique();
	}

	public AuthUser getManager() {
		return manager;
	}


	public void setManager(AuthUser manager) {
		this.manager = manager;
	}
	
	public static List<Location> findByIdAndRole(Long valueOf) {
		return find.where().eq("role","Sales Person").eq("id",valueOf).findList();
	}


	
	
	
	
	/*
	
	public static List<Location> findByVinAndAssignedUser(AuthUser user,String vin) {
		return find.where().eq("assignedTo", user).eq("vin", vin).findList();
	}
	
	public static List<Location> findByDateAndAssignedUser(AuthUser user,Date date) {
		return find.where().eq("assignedTo", user).eq("confirmDate", date).findList();
	}
	
	public static List<Location> getAllFailed() {
		return find.where().eq("leadStatus", "FAILED").findList();
	}
	
	public static List<SqlRow> getScheduleDates(AuthUser user) {
		SqlQuery q = Ebean.createSqlQuery("select distinct schedule_test.confirm_date from schedule_test where schedule_test.assigned_to_id = '"+user.id+"' and schedule_test.confirm_date is not null");
		List<SqlRow> rows = q.findList();
		return rows;
	}*/
	
	
	
}
