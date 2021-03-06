package models;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import scala.Option;
import securesocial.core.AuthenticationMethod;
import securesocial.core.Identity;
import securesocial.core.IdentityId;
import securesocial.core.OAuth1Info;
import securesocial.core.OAuth2Info;
import securesocial.core.PasswordInfo;

import com.avaje.ebean.Expr;

@Entity
public class AuthUser extends Model implements Identity {

	 public static Finder<Integer,AuthUser> find = new Finder<Integer,AuthUser>(
			 Integer.class, AuthUser.class
			  ); 
	public String email;
	public String communicationemail;
	public String firstName;
	public String lastName;
	public String password;
	public String role;
	public String phone;
	public String avatarUrl;
	public String imageName;
	public String imageUrl;
	public String age;
	public String commission;
	public String contractDur;
	public String experience;
	public String trainingPro;
	public String trialPeriod;
	public String trial;
	public String premiumFlag;
	public int newUser;
	public String account;
	
	public Date contractDurStartDate;
	public Date contractDurEndDate;
	public Integer priceStart;
	public Integer priceEnd;
	public String outLeftAll;
	public String zipCode;

	
	
	
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getOutLeftAll() {
		return outLeftAll;
	}

	public void setOutLeftAll(String outLeftAll) {
		this.outLeftAll = outLeftAll;
	}

	public Integer getPriceStart() {
		return priceStart;
	}

	public void setPriceStart(Integer priceStart) {
		this.priceStart = priceStart;
	}

	public Integer getPriceEnd() {
		return priceEnd;
	}

	public void setPriceEnd(Integer priceEnd) {
		this.priceEnd = priceEnd;
	}

	public Date getContractDurStartDate() {
		return contractDurStartDate;
	}

	public void setContractDurStartDate(Date contractDurStartDate) {
		this.contractDurStartDate = contractDurStartDate;
	}

	public Date getContractDurEndDate() {
		return contractDurEndDate;
	}

	public void setContractDurEndDate(Date contractDurEndDate) {
		this.contractDurEndDate = contractDurEndDate;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public int getNewUser() {
		return newUser;
	}

	public void setNewUser(int newUser) {
		this.newUser = newUser;
	}

	public String getTrial() {
		return trial;
	}

	public void setTrial(String trial) {
		this.trial = trial;
	}

	public String userGender;
	public String salary;
	public String trainingCost;
	public String trainingHours;
	public String quota;
	
	@Id
	public Integer id;
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String provider;
	
	@ManyToOne
	public Location location;
	
	@ManyToMany(cascade = CascadeType.ALL)
	public List<Permission> permission;
	


	@Override
	public AuthenticationMethod authMethod() {
		return null;
	}

	@Override
	public Option<String> avatarUrl() {
		return null;
	}

	@Override
	public Option<String> email() {
		return null;
	}

	@Override
	public String firstName() {
		return firstName;
	}

	@Override
	public String fullName() {
		return firstName + " " +lastName;
	}

	@Override
	public IdentityId identityId() {
		return null;
	}
	
	@Override
	public String lastName() {
		return lastName;
	}

	@Override
	public Option<OAuth1Info> oAuth1Info() {
		return null;
	}

	@Override
	public Option<OAuth2Info> oAuth2Info() {
		return null;
	}

	@Override
	public Option<PasswordInfo> passwordInfo() {
		return null;
	}

	public List<Permission> getPermission() {
		return permission;
	}

	public void setPermission(List<Permission> permission) {
		this.permission = permission;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	
	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	

	
	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getCommission() {
		return commission;
	}

	public void setCommission(String commission) {
		this.commission = commission;
	}

	public String getContractDur() {
		return contractDur;
	}

	public void setContractDur(String contractDur) {
		this.contractDur = contractDur;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getTrainingPro() {
		return trainingPro;
	}

	public void setTrainingPro(String trainingPro) {
		this.trainingPro = trainingPro;
	}
	
	public String getCommunicationemail() {
		return communicationemail;
	}

	public void setCommunicationemail(String communicationemail) {
		this.communicationemail = communicationemail;
	}

	public String getTrialPeriod() {
		return trialPeriod;
	}

	public void setTrialPeriod(String trialPeriod) {
		this.trialPeriod = trialPeriod;
	}

	

	public String getPremiumFlag() {
		return premiumFlag;
	}

	public void setPremiumFlag(String premiumFlag) {
		this.premiumFlag = premiumFlag;
	}

	public String getUserGender() {
		return userGender;
	}

	public String getTrainingCost() {
		return trainingCost;
	}

	public void setTrainingCost(String trainingCost) {
		this.trainingCost = trainingCost;
	}

	public String getTrainingHours() {
		return trainingHours;
	}

	public void setTrainingHours(String trainingHours) {
		this.trainingHours = trainingHours;
	}

	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}

	public String getQuota() {
		return quota;
	}

	public void setQuota(String quota) {
		this.quota = quota;
	}

	public static List<AuthUser> getUserByType() {
		return find.where().or(Expr.eq("role", "General Manager"), Expr.eq("role", "Sales Person")).eq("account", "active").findList();
	}

	public static AuthUser findById(Integer id) {
		return find.byId(id);
	}

	public static List<AuthUser> getAllSalesUser() {
	
		return find.where().eq("role", "Sales Person").eq("account", "active").findList();
	}
	
	public static List<AuthUser> getAllSalesUserDeactive() {
		
		return find.where().eq("role", "Sales Person").findList();
	}
	
	public static List<AuthUser> getAllUsers() {
		
		return find.all();
	}
	
	public static AuthUser findByEmail(String email) {
		return find.where().eq("email", email).eq("account", "active").findUnique();
	}
	
	public static List<AuthUser> findByLocatio(Location locations) {
		return find.where().eq("location", locations).eq("account", "active").findList();
	}
	public static List<AuthUser> findByPhotographer(Long locations) {
		return find.where().eq("location.id", locations).eq("account", "active").eq("role", "Photographer").findList();
	}
	
	public static List<AuthUser> findByLocatioion(Location locations) {
		return find.where().eq("location", locations).eq("account", "active").or(Expr.eq("role", "Manager"), Expr.eq("role", "Sales Person")).findList();
	}
	public static List<AuthUser> findByLocatioUsers(Location locations) {
		return find.where().eq("role", "Sales Person").eq("location", locations).eq("account", "active").findList();
	}
	
	public static List<AuthUser> getOnlyActiveUser() {
		return find.where().eq("account", "active").findList();
	}
	
	
	public static List<AuthUser> findByLocatioUsersNotGM(Location locations, Integer id) {
		return find.where().ne("role", "General Manager").ne("id", id).ne("role", "Photographer").ne("role", "Front Desk").eq("location", locations).eq("account", "active").findList();
	}
	
	public static List<AuthUser> findByLocatioUsersNotManager(Location locations) {
		return find.where().or(Expr.eq("role", "General Manager"), Expr.eq("location", locations)).ne("role", "Manager").ne("role", "Photographer").ne("role", "Front Desk").eq("account", "active").findList();
	}
	
	public static List<AuthUser> findByLocationDeactive(Location locations) {
		return find.where().eq("location", locations).eq("account", "deactive").findList();
	}
	
	public static AuthUser getlocationAndManagerByType(Location locations,String roles) {
		return find.where().and(Expr.eq("location", locations), Expr.eq("role", roles)).eq("account", "active").findUnique();
	}
	
	public static List<AuthUser> getlocationAndRoleByType(Location locations,String roles) {
		return find.where().and(Expr.eq("location", locations), Expr.eq("role", roles)).eq("account", "active").findList();
	}
	
	public static List<AuthUser> getAllUserByLocation(Location locations){
		return find.where().or(Expr.eq("role", "General Manager"), Expr.eq("role", "Sales Person")).eq("location", locations).eq("account", "active").findList();
	}
	
	public static List<AuthUser> getAllUserByLocationDeactiveAlso(Location locations){
		return find.where().eq("location", locations).eq("role", "Sales Person").findList();
	}
	
	public static List<AuthUser> getlocationAndManager(Location locations) {
		return find.where().eq("location", locations).eq("role", "Manager").eq("account", "active").findList();
	}
	
	public static List<AuthUser> getOnlyPhotographer() {
		return find.where().eq("role", "Photographer").eq("account", "active").findList();
	}
	
	public static List<AuthUser> getOnlyManagers() {
		return find.where().eq("role", "Manager").eq("account", "active").findList();
	}
	
	public static AuthUser getOnlyGM() {
		return find.where().eq("role", "General Manager").eq("account", "active").findUnique();
	}
	
	public static AuthUser getlocationAndManagerOne(Location locations) {
		return find.where().eq("location", locations).eq("role", "Manager").eq("account", "active").findUnique();
	}

	public static List<AuthUser> findByIdAndRole() {
		return find.where().eq("role", "Sales Person").eq("location", "16").findList();
	}
	public static List<AuthUser> findByLocationDeactivePhoto(Long location) {
		return find.where().eq("location.id", location).eq("account", "deactive").eq("role", "Photographer").findList();
	}
	public static List<AuthUser> getAllSalesAndFrontUser() {
		return find.where().or(Expr.eq("role", "Sales Person"), Expr.eq("role", "Front Desk")).eq("account", "active").findList();
	}
	public static AuthUser findByOutLeftAll(Long location) {
		return find.where().eq("location.id", location).eq("outLeftAll", "Sent to one of the sales people").findUnique();
	}

	public static List<AuthUser> findByPermissionUser(Location location2,Integer id2) {
		return find.where().eq("location", location2).ne("id", id2).eq("account", "active").findList();
	}

	public static AuthUser findByAssignedTo(AuthUser assignedTo) {
		return find.byId(assignedTo.id);
	}
}
