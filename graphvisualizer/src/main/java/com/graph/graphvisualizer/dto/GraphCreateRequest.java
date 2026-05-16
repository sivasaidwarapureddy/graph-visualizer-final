
package com.graph.graphvisualizer.dto;


import lombok.Data;
import java.util.List;


@Data // handles getters and setters auto matiuacally 
public class GraphCreateRequest {

    private int nodes;
    private boolean directed;
    private List<List<Integer>> edges;

}