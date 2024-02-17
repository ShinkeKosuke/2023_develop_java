ALTER TABLE events ADD price int(11) NOT NULL COMMENT '参加費' AFTER detail;
ALTER TABLE events ADD price_flg int(11) DEFAULT 0 COMMENT '参加費必要フラグ' AFTER price;