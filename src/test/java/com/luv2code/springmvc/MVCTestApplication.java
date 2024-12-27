package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.repository.StudentDAO;
import com.luv2code.springmvc.service.StudentService;

import static org.assertj.core.api.Assertions.assertThat;
import static  org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;

@TestPropertySource("/application.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MVCTestApplication {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testEndpoint() {
        String url = "http://localhost:" + port + "/"; // Construct URL
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        // Add more assertions based on the response body, etc.
    }

    //Arrange-Act-Assert Pattren

    @Autowired
    StudentService studentService;

    @Autowired
    StudentDAO studentDAO;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void createData(){
        jdbcTemplate.execute("insert into student(id,firstName,lastName,email_address) values(1,\'Karthik\',\'Manyam\',\'kj@gmail.com\')");

    }



    @Test
    public void createStudentService(){

        //Create Student Service first and then by test driven approach get to the service package and create one
        studentService.createStudent("Karthik","Manyam","h@gmail.com");

        //DAO ACCESS by TDD
        CollegeStudent collegeStudent=studentDAO.findByEmailAddress("h@gmail.com");
        assertEquals("Karthik",collegeStudent.getFirstname(),"Student Creation Should be Successfull!!");
        assertEquals(2,collegeStudent.getId());

    }
    @Test
    public void checkStudentNull(){
        assertTrue(studentService.checkIfStudentIsNull(1));
        assertFalse(studentService.checkIfStudentIsNull(0));
    }

    @Test
    public void checkDeleteStudentById(){
        assertTrue(studentService.deleteStudentById(1));
        assertFalse(studentService.deleteStudentById(2));
    }

    @Test
    @Sql("/insertData.sql")//Exceutes sql file first but beforeeach will executed then this one will execute
    public  void checkAllStudents(){
        jdbcTemplate.execute("INSERT INTO student ( firstName, lastName, email_address) VALUES ( 'Jane', 'Smith', 'jane.smith@example.com')");
        List<CollegeStudent> collegeStudents=studentService.getAllStudents();
        assertEquals(6,collegeStudents.size(),"Length is not Equal");
        assertTrue(
                collegeStudents.stream().anyMatch( s -> s.getEmailAddress().equals("jane.smith@example.com"))
                ,"Both Emails are not matching in database"
        );
        assertTrue(
                collegeStudents.stream().anyMatch( s -> s.getFirstname().equals("Karthik"))
                ,"Names are missing and not matching in database"
        );

    }

    @AfterEach
    public void cleanData(){

        jdbcTemplate.execute("DELETE FROM student");
    }
}
