
package com.graph.graphvisualizer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.graph.graphvisualizer.model.Graph;

public interface GraphRepository extends JpaRepository<Graph, Long> {
}