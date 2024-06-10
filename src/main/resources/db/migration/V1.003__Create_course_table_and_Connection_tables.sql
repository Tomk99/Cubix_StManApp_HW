create sequence course_seq start with 1 increment by 50;
create table course (id integer not null, name varchar(255), primary key (id));
create table student_courses (courses_id integer not null, students_id integer not null);
create table teacher_courses (courses_id integer not null, teachers_id integer not null);
alter table if exists student_courses add constraint FKlwviiijdg10oc2ui4yl7adh1o foreign key (courses_id) references course;
alter table if exists student_courses add constraint FKrgo64lg01pxfwq2x9753jgywc foreign key (students_id) references student;
alter table if exists teacher_courses add constraint FKb0yh91nc23jyhxnrpmw9c64s1 foreign key (courses_id) references course;
alter table if exists teacher_courses add constraint FK3yob9rjrxc41o4wxwcfqneahv foreign key (teachers_id) references teacher;