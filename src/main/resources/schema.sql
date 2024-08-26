create table  IF NOT EXISTS  TB_MEMBER (
    member_id varchar(255) not null,
    password varchar(255) not null,
    primary key (member_id)
);