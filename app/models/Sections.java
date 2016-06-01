package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class Sections extends Model {

	@Id
	public Long id;
	public String title;
	public String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	public static Finder<Long,Sections> find = new Finder<>(Long.class,Sections.class);
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public static Sections findById(Long id) {
		return find.byId(id);
	}
	
	public static Sections findByValue(String value) {
		return find.where().eq("value", value).findUnique();
	}
	
	public static List<Sections> findAll() {
		return find.all();
	}
	public static List<Sections> findAllSection() {
		return find.where().gt("id", 4).findList();
	}
	public static List<Sections> findByNameList(String name) {
		return find.where().eq("title", name).findList();
	}
	
}
