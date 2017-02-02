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
	public Integer shows;
	public String profile;
	public Boolean callToAction;
	public String actionTitle;
	public String actionOutcomes;
	public String actionClientPdf;
	public String maunfacturersIds;
	public String confirmationMsg;
	public Boolean deleted;
	public Boolean hideLead;
	public Long dowpdfIds;
	
	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public String getMaunfacturersIds() {
		return maunfacturersIds;
	}

	public void setMaunfacturersIds(String maunfacturersIds) {
		this.maunfacturersIds = maunfacturersIds;
	}
	@ManyToOne
	public Location locations;


	
	public String getActionTitle() {
		return actionTitle;
	}

	public void setActionTitle(String actionTitle) {
		this.actionTitle = actionTitle;
	}

	public Boolean getHideLead() {
		return hideLead;
	}

	public void setHideLead(Boolean hideLead) {
		this.hideLead = hideLead;
	}

	public String getActionOutcomes() {
		return actionOutcomes;
	}

	public void setActionOutcomes(String actionOutcomes) {
		this.actionOutcomes = actionOutcomes;
	}

	public String getActionClientPdf() {
		return actionClientPdf;
	}

	public void setActionClientPdf(String actionClientPdf) {
		this.actionClientPdf = actionClientPdf;
	}

	public Boolean getCallToAction() {
		return callToAction;
	}

	public void setCallToAction(Boolean callToAction) {
		this.callToAction = callToAction;
	}

	public Long getDowpdfIds() {
		return dowpdfIds;
	}

	public void setDowpdfIds(Long dowpdfIds) {
		this.dowpdfIds = dowpdfIds;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getLeadName() {
		return leadName;
	}


	public Integer getShows() {
		return shows;
	}

	public void setShows(Integer shows) {
		this.shows = shows;
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
	
	
	
	public String getConfirmationMsg() {
		return confirmationMsg;
	}

	public void setConfirmationMsg(String confirmationMsg) {
		this.confirmationMsg = confirmationMsg;
	}
	public static Finder<Long,LeadType> find = new Finder<>(Long.class,LeadType.class);
	
	public static LeadType findById(Long id) {
		return find.byId(id);
	}
	
	public static List<LeadType> findByLocation(Long location) {
		return find.where().eq("locations.id", location).ne("deleted", 1).findList();
	}
	public static LeadType findByLocations(Long location) {
		return find.where().eq("locations.id", location).ne("deleted", 1).findUnique();
	}
	
	public static LeadType findByName(String name) {
		return find.where().eq("lead_name", name).findUnique();
	}
	
	public static List<LeadType> findByLocationsAndSelected(Long location) {
		return find.where().eq("locations.id", location).ne("deleted", 1).findList();
	}
	
	public static List<LeadType> getLeadData() {
		return find.where().ne("deleted", 1).findList();
	}
	
	public static List<LeadType> getAllLeadData() {
		return find.all();
	}
	
	
}
