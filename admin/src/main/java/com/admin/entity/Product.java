package com.admin.entity;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
public class Product
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String name;
	private String category;
	private double listPrice;
	private double ourPrice;
	private boolean active=true;
	
	@Column(columnDefinition="text")
	private String description;
	private int inStockNumber;
	
	@Transient
	private MultipartFile productImage;


	@OneToMany(mappedBy = "product")
	@JsonIgnore
	private List<ProductToCartItem> productToCartItemList;
	
	
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getCategory()
	{
		return category;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}

	public double getListPrice()
	{
		return listPrice;
	}

	public void setListPrice(double listPrice)
	{
		this.listPrice = listPrice;
	}

	public double getOurPrice()
	{
		return ourPrice;
	}

	public void setOurPrice(double ourPrice)
	{
		this.ourPrice = ourPrice;
	}

	public boolean isActive()
	{
		return active;
	}

	public void setActive(boolean active)
	{
		this.active = active;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public int getInStockNumber()
	{
		return inStockNumber;
	}

	public void setInStockNumber(int inStockNumber)
	{
		this.inStockNumber = inStockNumber;
	}

	public MultipartFile getProductImage()
	{
		return productImage;
	}

	public void setProductImage(MultipartFile productImage)
	{
		this.productImage = productImage;
	}


	public List<ProductToCartItem> getProductToCartItemList() {
		return productToCartItemList;
	}

	public void setProductToCartItemList(List<ProductToCartItem> productToCartItemList) {
		this.productToCartItemList = productToCartItemList;
	}
	
}
