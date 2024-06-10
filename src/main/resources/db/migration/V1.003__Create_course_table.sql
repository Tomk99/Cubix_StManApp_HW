create sequence course_seq start with 1 increment by 50;
create table course (id integer not null, name varchar(255), primary key (id));
alter table student add column course_id integer;
alter table teacher add column course_id integer;
alter table if exists student add constraint FK_student_to_course foreign key (course_id) references course;
alter table if exists teacher add constraint FK_teacher_to_course foreign key (course_id) references course;