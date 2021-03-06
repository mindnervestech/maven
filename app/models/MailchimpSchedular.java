package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class MailchimpSchedular extends Model{

	@Id
	public Long id;
	public String schedularTime;
	public Date currDate;
	public String mailchimpUserName;
	public String mailchimpPassword;
	public String apikey;
	public boolean synchronizeContact;
	public boolean getSynchronizeContact() {
		return synchronizeContact;
	}

	public void setSynchronizeContact(boolean synchronizeContact) {
		this.synchronizeContact = synchronizeContact;
	}

	@ManyToOne
	public MailchimpList list;
	
	
	
	public String getMailchimpUserName() {
		return mailchimpUserName;
	}


	public void setMailchimpUserName(String mailchimpUserName) {
		this.mailchimpUserName = mailchimpUserName;
	}


	public String getMailchimpPassword() {
		return mailchimpPassword;
	}


	public void setMailchimpPassword(String mailchimpPassword) {
		this.mailchimpPassword = mailchimpPassword;
	}


	public String getApikey() {
		return apikey;
	}


	public void setApikey(String apikey) {
		this.apikey = apikey;
	}


	public MailchimpList getList() {
		return list;
	}


	public void setList(MailchimpList listId) {
		this.list = listId;
	}




	@ManyToOne
	public Location locations;

	public static Finder<Long,MailchimpSchedular> find = new Finder<>(Long.class,MailchimpSchedular.class);

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getSchedularTime() {
		return schedularTime;
	}


	public void setSchedularTime(String schedularTime) {
		this.schedularTime = schedularTime;
	}


	public Date getCurrDate() {
		return currDate;
	}


	public void setCurrDate(Date currDate) {
		this.currDate = currDate;
	}


	public Location getLocations() {
		return locations;
	}


	public void setLocations(Location locations) {
		this.locations = locations;
	}
	
	public static MailchimpSchedular findById(Long id) {
		return find.byId(id);
	}
	
	public static List<MailchimpSchedular> getAllContact() {
		return find.all();
	}

	
	public static MailchimpSchedular findByLocations(Long location) {
		return find.where().eq("locations.id", location).findUnique();
	}
	
	public static MailchimpSchedular findByLocationsAnfEnable(Long location) {
		return find.where().eq("locations.id", location).findUnique();
	}
	
}
