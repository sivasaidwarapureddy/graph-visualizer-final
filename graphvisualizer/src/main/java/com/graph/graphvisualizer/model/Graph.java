package com.graph.graphvisualizer.model;

import lombok.Data;
import java.util.List;

@Data
public class Graph {

    private Long id;
    private int nodes;
    private boolean directed;
    private List<Edge> edges;
}