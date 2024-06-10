create sequence course_seq start with 1 increment by 50;
create table course (id integer not null, name varchar(255), primary key (id));
create table course_students (course_id integer not null, students_id integer not null);
create table course_teachers (course_id integer not null, teachers_id integer not null);
alter table if exists course_students add constraint FK532dg5ikp3dvbrbiiqefdoe6m foreign key (students_id) references student;
alter table if exists course_students add constraint FKgut5xj4l8sk6hg3l0t2su2pnc foreign key (course_id) references course;
alter table if exists course_teachers add constraint FKe3n62rwx3uc1yucgkmw6gjkm5 foreign key (teachers_id) references teacher;
alter table if exists course_teachers add constraint FKlmee8ivi6ymoe34wgwknlurpb foreign key (course_id) references course;
