create sequence course_seq start with 1 increment by 50;
create table course (id integer not null, name varchar(255), primary key (id));