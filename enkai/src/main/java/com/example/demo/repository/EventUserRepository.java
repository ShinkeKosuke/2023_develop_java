package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Event;
import com.example.demo.entity.EventUser;
import com.example.demo.entity.User;

public interface EventUserRepository extends JpaRepository<EventUser, Integer>{
	public List<EventUser> findByEvent(Event event);
	public EventUser findByUserAndEvent(User user, Event event);
}

