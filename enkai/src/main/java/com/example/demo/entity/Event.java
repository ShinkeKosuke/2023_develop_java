package com.example.demo.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "events")
public class Event extends AbstractEntity {
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 255, nullable = false)
	@NotEmpty(message = "イベント名は必須入力です")
	private String name;

	@Column(length = 255, nullable = false)
	@NotEmpty(message = "詳細は必須入力です")
	private String detail;

	@Column(length = 11, nullable = false)
	@NotNull(message = "最大参加者数は必須入力です")
	private Integer max_participant;

	@ManyToOne
	@JoinColumn(name = "category_id")
	@NotNull(message = "カテゴリは必須入力です")
	private Category category;

	@ManyToOne
	@JoinColumn(name = "user_id")
	@NotNull(message = "ユーザは必須入力です")
	private User user;

	@Column(length = 11, nullable = true)
	private Integer price;

	@Column(length = 11, nullable = false)
	private Integer price_flg;

	private Long count;
}
