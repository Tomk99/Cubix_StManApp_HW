package com.example.studymanagmentapp.service;

import com.example.studymanagmentapp.model.Course;
import com.example.studymanagmentapp.model.Student;
import com.example.studymanagmentapp.model.Teacher;
import com.example.studymanagmentapp.repository.CourseRepository;
import com.example.studymanagmentapp.repository.StudentRepository;
import com.example.studymanagmentapp.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
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

	public void deleteDb() {
		courseRepository.deleteAll();
		studentRepository.deleteAll();
		teacherRepository.deleteAll();
	}

	@Transactional
	public void addInitData() {

		Student student1 = studentRepository.save(Student.builder().name("John Doe").birthdate(LocalDate.of(2002,2,2)).semester(2).build());
		Student student2 = studentRepository.save(Student.builder().name("Jane Doe").birthdate(LocalDate.of(2001,1,1)).semester(3).build());
		Student student3 = studentRepository.save(Student.builder().name("John Smith").birthdate(LocalDate.of(2000,5,5)).semester(4).build());
		Student student4 = studentRepository.save(Student.builder().name("Jane Smith").birthdate(LocalDate.of(1999,3,3)).semester(5).build());

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
}
