/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Model;

/**
 *
 * @author Administrator
 */
public class Word {

	public Word(){
		
	}
    public Word( String word, String belongto, int stype) {
		super();
		
		this.word = word;
		this.belongto = belongto;
		this.stype = stype;
	}
	private int id;
    private String word;
    private String belongto;
    private int stype;
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getBelongto() {
		return belongto;
	}
	public void setBelongto(String belongto) {
		this.belongto = belongto;
	}
	public int getStype() {
		return stype;
	}
	public void setStype(int stype) {
		this.stype = stype;
	}
    
}
