package com.graph.graphvisualizer.repository;

import com.graph.graphvisualizer.model.Edge;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EdgeRepository extends JpaRepository<Edge, Long>  {
List<Edge> findByGraphId(Long graphId);
}