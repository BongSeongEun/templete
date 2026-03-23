create table user
(
    user_id        int auto_increment
        primary key,
    user_uuid      char(36)        default (uuid())      not null,
    user_name      varchar(20)                     not null,
    login_id       varchar(50)                     not null,
    login_password varchar(255)                     not null,
    role           varchar(20) default 'ROLE_USER' not null,
    constraint table_name_pk_2
        unique (user_uuid),
    constraint table_name_pk_3
        unique (login_id)
);