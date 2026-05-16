package com.graph.graphvisualizer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import jakarta.annotation.PostConstruct;

public class DatabaseInitializer {

     @Autowired
    private JdbcTemplate jdbcTemplate;

     @PostConstruct
    public void init() {
        jdbcTemplate.execute("CREATE DATABASE IF NOT EXISTS graphdb");
        jdbcTemplate.execute("USE graphdb");
    }

    
}
