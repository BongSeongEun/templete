create table todo
(
    todo_id         int auto_increment comment 'todo 고유 번호'
        primary key,
    todo_name       varchar(255)         not null,
    todo_start_time timestamp            not null,
    todo_end_time   timestamp            not null,
    todo_status     tinyint(1) default 0 not null
)
    comment '투두 테이블';
