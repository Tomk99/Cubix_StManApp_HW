package com.example.studymanagementapp.service;

import com.example.studymanagementapp.model.Course;
import com.example.studymanagementapp.model.Student;
import com.example.studymanagementapp.model.Teacher;
import com.example.studymanagementapp.repository.CourseRepository;
import com.example.studymanagementapp.repository.StudentRepository;
import com.example.studymanagementapp.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class InitDbService {

	private final CourseRepository courseRepository;
	private final StudentRepository studentRepository;
	private final TeacherRepository teacherRepository;
	private final JdbcTemplate jdbcTemplate;

	@Transactional
	public void deleteDb() {
		courseRepository.deleteAll();
		studentRepository.deleteAll();
		teacherRepository.deleteAll();
	}

	@Transactional
	public void deleteAudTables() {
		jdbcTemplate.update("delete from teacher_aud");
		jdbcTemplate.update("delete from course_aud");
		jdbcTemplate.update("delete from student_aud");
		jdbcTemplate.update("delete from course_students_aud");
		jdbcTemplate.update("delete from course_teachers_aud");
		jdbcTemplate.update("delete from revinfo");
	}

	@Transactional
	public void addInitData() {

		Student student1 = studentRepository.save(Student.builder().name("John Doe").birthdate(LocalDate.of(2002,2,2)).semester(2).eduId(222).build());
		Student student2 = studentRepository.save(Student.builder().name("Jane Doe").birthdate(LocalDate.of(2001,1,1)).semester(3).eduId(333).build());
		Student student3 = studentRepository.save(Student.builder().name("John Smith").birthdate(LocalDate.of(2000,5,5)).semester(4).eduId(444).build());
		Student student4 = studentRepository.save(Student.builder().name("Jane Smith").birthdate(LocalDate.of(1999,3,3)).semester(5).eduId(555).build());

		Teacher teacher1 = teacherRepository.save(Teacher.builder().name("Jack Doe").birthdate(LocalDate.of(1982,8,2)).build());
		Teacher teacher2 = teacherRepository.save(Teacher.builder().name("Jill Doe").birthdate(LocalDate.of(1981,8,1)).build());
		Teacher teacher3 = teacherRepository.save(Teacher.builder().name("Jack Smith").birthdate(LocalDate.of(1979,7,9)).build());
		Teacher teacher4 = teacherRepository.save(Teacher.builder().name("Jill Smith").birthdate(LocalDate.of(1978,7,8)).build());

		Course course1 = courseRepository.save(Course.builder().name("discrete mathematics").build());
		course1.setStudents(Set.of(student1,student2));
		course1.setTeachers(Set.of(teacher1));
		Course course2 = courseRepository.save(Course.builder().name("robotics").build());
		course2.setStudents(Set.of(student3,student4));
		course2.setTeachers(Set.of(teacher3));
	}

	@Transactional
	public void updateNamesForTestingEnvers() throws InterruptedException {
		System.out.println("UPDATE COURSE NAMES FOR TESTING ENVERS");
		Thread.sleep(2000);
		courseRepository.findAll().forEach(course -> course.setName(course.getName().concat(" v2")));
	}
}
