package com.Model;

public class ActivityTest {
	
	public ActivityTest(){
		
	}
    public ActivityTest( int id, String title) {
		super();
		
		//this.id = id;
		this.title = title;
	}
	private int id;
    private String title;
    private Double sim;
    
	public String getTitle() {
		return title;
	}
	
	public int getId() {
		return id;
	}
	
	public Double getSim(){
		return sim;
	}
	
	public void setActivityId(int id) {
		this.id = id;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setSim(Double sim){
		this.sim=sim;
	}

}
