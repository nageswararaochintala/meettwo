package com.meettwo.dto;

public class Pagination {
	
	Integer start;
	String totalItemCount;
	Integer number;
	String numberOfPages;
	
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
	public String getTotalItemCount() {
		return totalItemCount;
	}
	public void setTotalItemCount(String totalItemCount) {
		this.totalItemCount = totalItemCount;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public String getNumberOfPages() {
		return numberOfPages;
	}
	public void setNumberOfPages(String numberOfPages) {
		this.numberOfPages = numberOfPages;
	}
	
}
