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
	public String jsonDataAdd;
	public Boolean additionalData;
	public String dataType;
	public String mainTitle;
	public String searchTitle;
	public String searchSubTitle;
	public String leadTypeTitle;
	public String outcome;
	public String deleted;
	
	@ManyToOne
	public CustomerPdf sendPdf;
	
		
	@ManyToOne
	public Location locations;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getOutcome() {
		return outcome;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	
	public String getJsonDataAdd() {
		return jsonDataAdd;
	}

	public void setJsonDataAdd(String jsonDataAdd) {
		this.jsonDataAdd = jsonDataAdd;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}


	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public CustomerPdf getSendPdf() {
		return sendPdf;
	}

	public void setSendPdf(CustomerPdf sendPdf) {
		this.sendPdf = sendPdf;
	}

	public Boolean getAdditionalData() {
		return additionalData;
	}

	public void setAdditionalData(Boolean additionalData) {
		this.additionalData = additionalData;
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
	

	public String getMainTitle() {
		return mainTitle;
	}

	public void setMainTitle(String mainTitle) {
		this.mainTitle = mainTitle;
	}

	public String getSearchTitle() {
		return searchTitle;
	}

	public void setSearchTitle(String searchTitle) {
		this.searchTitle = searchTitle;
	}

	public String getSearchSubTitle() {
		return searchSubTitle;
	}

	public void setSearchSubTitle(String searchSubTitle) {
		this.searchSubTitle = searchSubTitle;
	}

	public String getLeadTypeTitle() {
		return leadTypeTitle;
	}

	public void setLeadTypeTitle(String leadTypeTitle) {
		this.leadTypeTitle = leadTypeTitle;
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

	public static CustomizationForm findByLeadType(String value) {
		return find.where().eq("dataType", value).findUnique();
	}
	
	
	
	
}
