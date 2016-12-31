package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.annotation.Where;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class CollectionImages extends Model {

	@Id
	public Long id;
	public String path;
	public String thumbPath;
	public String imageName;
	public boolean defaultImage;
	public String title;
	public String description;
	
	@ManyToOne
	public AddCollection collection;
	
	@ManyToOne
	public AuthUser user;
	
	
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getThumbPath() {
		return thumbPath;
	}

	public void setThumbPath(String thumbPath) {
		this.thumbPath = thumbPath;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public boolean isDefaultImage() {
		return defaultImage;
	}

	public void setDefaultImage(boolean defaultImage) {
		this.defaultImage = defaultImage;
	}

	public AddCollection getCollection() {
		return collection;
	}

	public void setCollection(AddCollection collection) {
		this.collection = collection;
	}

	public AuthUser getUser() {
		return user;
	}

	public void setUser(AuthUser user) {
		this.user = user;
	}
	
	public static Finder<Long,CollectionImages> find = new Finder<>(Long.class,CollectionImages.class);
	
	public static CollectionImages getByImagePath(String path) {
		return find.where().eq("path", path).findUnique();
	}
	
	public static List<CollectionImages> getDeleteImagePath(Long productId) {
		return find.where().eq("collection.id", productId).findList();
	}
	
	public static CollectionImages findById(Long id) {
		return find.byId(id);
	}
	public static CollectionImages findDefaultImg(Long id) {
		return find.where().eq("collection.id", id).eq("default_image", 1).findUnique();
	}
	
	public static List<CollectionImages> getByProduct(AddCollection product) {
		return find.where().eq("collection", product).findList();
	}
	public static List<CollectionImages> getByCollectionImg(AddCollection product) {
		return find.where().eq("collection", product).findList();
	}
	public static int countImage(Long id)
	{
		return find.where().eq("product_id", id).findRowCount();
		
	}
	public static CollectionImages getDefaultImg(Long pId) {
		return find.where().eq("collection.id", pId).eq("defaultImage", 1).findUnique();
	}
}
