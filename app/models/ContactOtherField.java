package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.Expression;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class ContactOtherField extends Model {

	@Id
	public Long id;
	public String value;
	public String keyValue;
	
	
	
	
	public Long getId() {
		return id;
	}




	public void setId(Long id) {
		this.id = id;
	}




	public String getValue() {
		return value;
	}




	public void setValue(String value) {
		this.value = value;
	}




	public String getKeyValue() {
		return keyValue;
	}




	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}




	public Contacts getContacts() {
		return contacts;
	}




	public void setContacts(Contacts contacts) {
		this.contacts = contacts;
	}




	@ManyToOne
	public Contacts contacts;
	
	

	public static Finder<Long,ContactOtherField> find = new Finder<>(Long.class,ContactOtherField.class);
	
	public static ContactOtherField findById(Long id) {
		return find.byId(id);
	}
	
	
	
		
		public static List<ContactOtherField> findByLeadIdWise(Long leadId) {
			return find.where().eq("leadId", leadId).findList();
		}
	
}
