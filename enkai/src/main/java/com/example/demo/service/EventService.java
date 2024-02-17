package com.example.demo.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.common.DataNotFoundException;
import com.example.demo.dao.EventDao;
import com.example.demo.entity.Category;
import com.example.demo.entity.Event;
import com.example.demo.entity.User;

@Service
public class EventService implements BaseService<Event> {
	@Autowired
	private EventDao dao;

	@Override
	public List<Event> findAll() {
		return dao.findAll();
	}

	@Override
	public Event findById(Integer id) throws DataNotFoundException {
		return dao.findById(id);
	}

	@Override
	public void save(Event event) {
		dao.save(event);
	}

	@Override
	public void deleteById(Integer id) {
		dao.deleteById(id);
	}

	public List<Event> findAllUserCount() {
		return formatEventList(dao.findAllUserCount());
	}

	public List<Event> findMyEventAllUserCount(Integer userId) {
		return formatEventList(dao.findMyEventAllUserCount(userId));
	}

	private List<Event> formatEventList(List<Map<String, Object>> getList){
        // 結果返却用の変数
        List<Event> eventList = new ArrayList<>();

        // 取得したデータを結果返却用のListに格納していく
        for (Map<String, Object> map : getList) {
        	Event event = new Event();
            Category category = new Category();
            User user = new User();
            category.setName((String) map.get("category_name"));
            user.setEmail((String) map.get("email"));
            event.setId((Integer) map.get("id"));
            event.setName((String) map.get("name"));
            event.setPrice((Integer) map.get("price"));
            event.setPrice_flg((Integer) map.get("price_flg"));
            event.setMax_participant((Integer) map.get("max_participant"));
            event.setCount((Long) map.get("count"));
            event.setCategory((Category) category);
            event.setUser((User) user);
            event.setCreatedAt((Date) new Date(map.get("created_at").toString()));
            event.setModifiedAt((Date) new Date(map.get("modified_at").toString()));

            eventList.add(event);
        }
        return eventList;
	}
}
