package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.annotation.Where;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class ProductImages extends Model {

	@Id
	public Long id;
	public String path;
	public String thumbPath;
	public String imageName;
	public boolean defaultImage;
	
	@ManyToOne
	public AddProduct product;
	
	@ManyToOne
	public AuthUser user;

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

	public AddProduct getProduct() {
		return product;
	}

	public void setProduct(AddProduct product) {
		this.product = product;
	}

	public AuthUser getUser() {
		return user;
	}

	public void setUser(AuthUser user) {
		this.user = user;
	}
	
	public static Finder<Long,ProductImages> find = new Finder<>(Long.class,ProductImages.class);
	
	public static ProductImages getByImagePath(String path) {
		return find.where().eq("path", path).findUnique();
	}
	
	public static List<ProductImages> getDeleteImagePath(Long productId) {
		return find.where().eq("product.id", productId).findList();
	}
	
	public static ProductImages findById(Long id) {
		return find.byId(id);
	}
	public static ProductImages findDefaultImg(Long id) {
		return find.where().eq("product.id", id).eq("default_image", 1).findUnique();
	}
	
	public static List<ProductImages> getByProduct(AddProduct product) {
		return find.where().eq("product", product).findList();
	}
	
	public static int countImage(Long id)
	{
		return find.where().eq("product_id", id).findRowCount();
		
	}
}
