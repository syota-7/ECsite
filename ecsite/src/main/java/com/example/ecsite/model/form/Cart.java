package com.example.ecsite.model.form;

import java.io.Serializable;

public class Cart implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long id,price,count;
	private String goodsName;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	
	
	

}
