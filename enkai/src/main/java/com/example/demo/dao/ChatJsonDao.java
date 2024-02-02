package com.example.demo.dao;

import java.io.Serializable;

import lombok.Data;

@Data
public class ChatJsonDao implements Serializable {
	private Integer eventId;
	private String message;
	
	public ChatJsonDao() {
		super();
	}
	
	public ChatJsonDao(Integer eventId, String message) {
		this.eventId = eventId;
		this.message = message;
	}
}