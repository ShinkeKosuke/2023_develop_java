package com.example.demo.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.common.DataNotFoundException;
import com.example.demo.entity.Category;
import com.example.demo.entity.Event;
import com.example.demo.entity.User;
import com.example.demo.repository.EventRepository;

@Repository
public class EventDao implements BaseDao<Event> {
	String SQL = "SELECT "
			+ "e.id as id, "
			+ "e.name as name, "
			+ "e.price as price, "
			+ "e.price_flg as price_flg, "
			+ "e.max_participant as max_participant, "
			+ "count(eu.id) as count, "
			+ "c.name as category_name, "
			+ "u.email as email, "
			+ "DATE_FORMAT(e.created_at, '%Y/%m/%d %T') as created_at, "
			+ "DATE_FORMAT(e.modified_at, '%Y/%m/%d %T') as modified_at"
			+ " FROM "
			+ "events e "
			+ "LEFT JOIN event_users eu ON e.id = eu.event_id "
			+ "LEFT JOIN users u ON e.user_id = u.id "
			+ "LEFT JOIN categories c ON e.category_id = c.id ";

	@Autowired
	EventRepository repository;

	@Autowired
	JdbcTemplate jdbc;

	@Override
	public List<Event> findAll() {
		return repository.findAll();
    }

	@Override
	public Event findById(Integer id) throws DataNotFoundException {
		return repository.findById(id).orElseThrow(() -> new DataNotFoundException());
	}

	@Override
	public void save(Event event) {
		this.repository.save(event);
	}

	@Override
	public void deleteById(Integer id) {
		try {
			Event event = this.findById(id);
			this.repository.deleteById(event.getId());
		} catch (DataNotFoundException e) {
			System.out.println("no data");
		}
	}

	public List<Map<String, Object>> findAllUserCount(){
		return 	jdbc.queryForList(SQL + " GROUP BY e.id");
	}

	public List<Map<String, Object>> findMyEventAllUserCount(Integer userId){
		return 	jdbc.queryForList(SQL+" WHERE e.user_id = '%s'".replace("%s", userId.toString()) + " GROUP BY e.id");
	}
}

