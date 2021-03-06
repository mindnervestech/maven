package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class UserNotes extends Model {

	@Id
	public Integer id;
	public String note;
	public String action;
	public Date createdDate;
	public Date createdTime;
	public Integer saveHistory;
	
	@ManyToOne
	public RequestMoreInfo requestMoreInfo;
	@ManyToOne
	public ScheduleTest scheduleTest;
	@ManyToOne
	public TradeIn tradeIn;
	@ManyToOne
	public AuthUser user;
	
	@ManyToOne
	public Location locations;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public RequestMoreInfo getRequestMoreInfo() {
		return requestMoreInfo;
	}
	public void setRequestMoreInfo(RequestMoreInfo requestMoreInfo) {
		this.requestMoreInfo = requestMoreInfo;
	}
	public ScheduleTest getScheduleTest() {
		return scheduleTest;
	}
	public void setScheduleTest(ScheduleTest scheduleTest) {
		this.scheduleTest = scheduleTest;
	}
	public TradeIn getTradeIn() {
		return tradeIn;
	}
	public void setTradeIn(TradeIn tradeIn) {
		this.tradeIn = tradeIn;
	}
	public AuthUser getUser() {
		return user;
	}
	public void setUser(AuthUser user) {
		this.user = user;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Location getLocations() {
		return locations;
	}
	public void setLocations(Location locations) {
		this.locations = locations;
	}

	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}



	public int getSaveHistory() {
		return saveHistory;
	}
	public void setSaveHistory(int saveHistory) {
		this.saveHistory = saveHistory;
	}



	public static Finder<Long,UserNotes> find = new Finder<>(Long.class,UserNotes.class);
	
	public static UserNotes findById(Long id) {
		return find.byId(id);
	}
	
	public static List<UserNotes> findRequestMoreByUser(RequestMoreInfo info,AuthUser user) {
		return find.where().eq("requestMoreInfo", info).eq("user",user).findList();
	}
	
	public static List<UserNotes> findRequestMore(RequestMoreInfo info) {
		return find.where().eq("requestMoreInfo", info).findList();
	}
	
	public static List<UserNotes> findRequestMoreList(RequestMoreInfo info) {
		return find.where().eq("requestMoreInfo", info).findList();
	}
	
	public static List<UserNotes> findRequestMoreAndFirstAdd(RequestMoreInfo info) {
		return find.where().eq("requestMoreInfo", info).eq("saveHistory",1).orderBy("createdDate asc").findList();
	}
	
	public static List<UserNotes> findScheduleTestAndFirstAdd(ScheduleTest schedule) {
		return find.where().eq("scheduleTest", schedule).eq("saveHistory",1).orderBy("createdDate asc").findList();
	}
	
	public static List<UserNotes> findTradeInAndFirstAdd(TradeIn tradeIn) {
		return find.where().eq("tradeIn", tradeIn).eq("saveHistory",1).orderBy("createdDate asc").findList();
	}
	
	
	public static List<UserNotes> findScheduleTestByUser(ScheduleTest schedule,AuthUser user) {
		return find.where().eq("scheduleTest", schedule).eq("user",user).findList();
	}
	
	public static List<UserNotes> findScheduleTest(ScheduleTest schedule) {
		return find.where().eq("scheduleTest", schedule).findList();
	}
	
	public static List<UserNotes> findTradeInByUser(TradeIn tradeIn,AuthUser user) {
		return find.where().eq("tradeIn", tradeIn).eq("user",user).findList();
	}
	
	public static List<UserNotes> findTradeIn(TradeIn tradeIn) {
		return find.where().eq("tradeIn", tradeIn).findList();
	}
	
	public static List<UserNotes> findByUser(AuthUser user) {
		return find.where().eq("user",user).findList();
	}
	
	public static List<UserNotes> findByUserAndcall(AuthUser user) {
		return find.where().eq("user",user).eq("action", "Called the Client").findList();
	}
	
	public static List<UserNotes> findByUserAndemail(AuthUser user) {
		return find.where().eq("user",user).eq("action", "Emailed the client").findList();
	}
	
	public static List<UserNotes> findByUserAndSched(AuthUser user) {
		return find.where().eq("user",user).eq("note", "Test Drive Scheduled").findList();
	}
	
}
