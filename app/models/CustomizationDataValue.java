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
public class CustomizationDataValue extends Model {

	@Id
	public Long id;
	public String value;
	public String keyValue;
	public Long leadId;
	public String leadType;
	
	@ManyToOne
	public Location locations;
	
	
	public static Finder<Long,CustomizationDataValue> find = new Finder<>(Long.class,CustomizationDataValue.class);
	
	public static CustomizationDataValue findById(Long id) {
		return find.byId(id);
	}
	
	public static CustomizationDataValue findByIdAndParent(Long id) {
		return find.where().eq("id", id).eq("status", null).findUnique();
	}
	
		
		public static List<CustomizationDataValue> findByScheduler() {
		return find.where().eq("schedule_email", 0).findList();
	}
	
}
