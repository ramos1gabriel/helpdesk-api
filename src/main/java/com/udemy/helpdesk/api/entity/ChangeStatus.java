package com.udemy.helpdesk.api.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import com.udemy.helpdesk.api.enums.StatusEnum;

public class ChangeStatus {
	
	@Id
	private String id;
	
	@DBRef(lazy = true)
	private Ticket ticket;
	
	@DBRef(lazy = true)
	private User userChange;
	
	private java.util.Date dateChangeStatus;
	
	private StatusEnum status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public User getUserChange() {
		return userChange;
	}

	public void setUserChange(User userChange) {
		this.userChange = userChange;
	}

	public java.util.Date getDateChangeStatus() {
		return dateChangeStatus;
	}

	public void setDateChangeStatus(java.util.Date dateChangeStatus) {
		this.dateChangeStatus = dateChangeStatus;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}
}
