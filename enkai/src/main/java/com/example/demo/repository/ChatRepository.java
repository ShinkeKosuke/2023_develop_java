package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Chat;
import com.example.demo.entity.Event;

public interface ChatRepository extends JpaRepository<Chat, Integer>{
	public List<Chat> findByEvent(Event event);
}

