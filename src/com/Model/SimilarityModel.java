package com.Model;

public class SimilarityModel {
	private int id;
    private int activity_idA;
    private int activity_idB;
    private Double sim;
    
    public int getactivity_idA() {
        return activity_idA;
    }

    public void setactivity_idA(int id) {
        this.activity_idA = id;
    }
    
    public int getactivity_idB(){
    	return activity_idB;
    }
    
    public void setactivity_idB(int id){
    	this.activity_idB=id;
    }
    
    public Double getsim(){
    	return sim;
    }
    
    public void setsim(Double sim){
    	this.sim=sim;
    }
}
