package com.Model;

public class Question {
	private int question_id;
	private String question;
	private String answer;
	private String question_seg;
	public void setQuestion_id(int question_id) {
		this.question_id = question_id;
	}
	public int getQuestion_id() {
		return question_id;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getQuestion() {
		return question;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getAnswer() {
		return answer;
	}
	public void setQuestion_seg(String question_seg) {
		this.question_seg = question_seg;
	}
	public String getQuestion_seg() {
		return question_seg;
	}
	

	

}

