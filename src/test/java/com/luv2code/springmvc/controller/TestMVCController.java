package com.luv2code.springmvc.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application.properties")
public class TestMVCController {

    private static MockHttpServletRequest mockHttpServletRequest;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeAll
    static void addServlets(){
        mockHttpServletRequest=new MockHttpServletRequest();
        mockHttpServletRequest.addParameter("firstname","Karthik");
        mockHttpServletRequest.addParameter("lastname","Manyam");
        mockHttpServletRequest.addParameter("email","kj@gmail.com");
    }

    @BeforeEach
    public void createData(){
        jdbcTemplate.execute("insert into student(id,firstName,lastName,email_address) values(1,\'Karthik\',\'Manyam\',\'kj@gmail.com\')");

    }


    @Test
    public void checkModelAndView() throws Exception {
        MvcResult mvcResult= mockMvc.perform(MockMvcRequestBuilders.get("/")).andExpect(status().isOk()).andReturn();

        ModelAndView mav=mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav,"index");

    }
    @Test()
    public void checkCreateStudent() throws Exception {
        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders.post("/")
                .accept("Application/json")
                .param("firstname","Karthik")
                .param("lastname","Manyam")
                .param("email","kj@gmail.com")).andExpect(status().isOk()).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(modelAndView,"index");

    }

    @AfterEach
    public void cleanData(){

        jdbcTemplate.execute("DELETE FROM student");
    }
}
