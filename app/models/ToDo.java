package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class ToDo extends Model {

	@Id
	public Long id;
	public String task;
	public Date dueDate;
	@ManyToOne
	public AuthUser assignedTo;
	public String priority;
	public String status;
	public String seen;
	public Integer saveas;
	public String google_id;

	@ManyToOne
	public AuthUser assignedBy;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTask() {
		return task;
	}
	public void setTask(String task) {
		this.task = task;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public AuthUser getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(AuthUser assignedTo) {
		this.assignedTo = assignedTo;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public AuthUser getAssignedBy() {
		return assignedBy;
	}
	public void setAssignedBy(AuthUser assignedBy) {
		this.assignedBy = assignedBy;
	}
	public String getSeen() {
		return seen;
	}
	public void setSeen(String seen) {
		this.seen = seen;
	}

	public Integer getSaveas() {
		return saveas;
	}
	public void setSaveas(Integer saveas) {
		this.saveas = saveas;
	}

	public static Finder<Long,ToDo> find = new Finder<>(Long.class,ToDo.class);
	
	public static ToDo findById(Long id) {
		return find.byId(id);
	}
	
	public static List<ToDo> findAll() {
		return find.where().ne("status", "Deleted").orderBy("due_date desc").findList();
	}
	
	public static List<ToDo> findByDate(Date date) {
		return find.where().eq("status", "Assigned").eq("dueDate", date).findList();
	}
	
	public static List<ToDo> findByDateAndTask(Date date,String taskTitle) {
		return find.where().eq("task", taskTitle).eq("dueDate", date).findList();
	}
	
	public static int findAllNewCountByUser(AuthUser user) {
		return find.where().eq("status", "Assigned").eq("assignedTo", user).eq("seen", null).findRowCount();
	}
	
	public static List<ToDo> findAllNewByUser(AuthUser user) {
		return find.where().eq("status", "Assigned").eq("assignedTo", user).eq("seen", null).findList();
	}
	
	public static List<SqlRow> getToDoDates(String cD) {
		SqlQuery q = Ebean.createSqlQuery("select to_do.due_date from to_do where to_do.`status` = 'Assigned' and to_do.due_date >= '"+cD+"'");
		List<SqlRow> rows = q.findList();
		return rows;
	}
	public String getGoogle_id() {
		return google_id;
	}
	public void setGoogle_id(String google_id) {
		this.google_id = google_id;
	}
}
