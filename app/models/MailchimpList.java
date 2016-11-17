package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class MailchimpList extends Model{

	@Id
	public int id;
	public String nickName;
	public String listId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getListId() {
		return listId;
	}
	public void setListId(String listId) {
		this.listId = listId;
	}
	
	public static Finder<Long,MailchimpList> find = new Finder<>(Long.class,MailchimpList.class);
	public static List<MailchimpList> getAll() {
		return find.all();
	}
}
