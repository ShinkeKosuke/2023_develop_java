package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Event;
import com.example.demo.entity.User;

public interface EventRepository extends JpaRepository<Event, Integer>{
//	@Query("SELECT id, name, max_participant, count(eu.id), category_id, user_id, created_at, modified_at FROM events e LEFT JOIN event_users eu on e.event_id = eu.id")
//	public List<Event> find();
//	
	public List<Event> findByUser(User user);
}

