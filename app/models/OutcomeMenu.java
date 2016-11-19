package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
@Entity
public class OutcomeMenu extends Model {
	
	@Id
	public Long id;
	public String menuName;
	

public static Finder<Long,OutcomeMenu> find = new Finder<>(Long.class,OutcomeMenu.class);
	
	
	public static List<OutcomeMenu> getAllData() {
		return find.all();
	}
	
	
	public static OutcomeMenu findById(Long id) {
		return find.byId(id);
	}
	
	
	
}
