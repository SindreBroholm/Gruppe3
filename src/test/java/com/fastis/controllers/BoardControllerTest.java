package com.fastis.controllers;


import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureMockMvc
class BoardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(username = "test0@test.no", authorities = {"admin"})
    void shouldHaveOnePendingMember() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/members/1/true"))
                .andExpect(MockMvcResultMatchers.model().attribute("members", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "test0@test.no", authorities = {"admin"})
    void shouldNotHavePendingMembers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/members/1/false"))
                .andExpect(MockMvcResultMatchers.model().attribute("members", Matchers.hasSize(4)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }
}