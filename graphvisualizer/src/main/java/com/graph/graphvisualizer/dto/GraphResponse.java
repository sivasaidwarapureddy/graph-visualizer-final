package com.graph.graphvisualizer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class GraphResponse {
    private Long id;
    private int nodes;
    private boolean directed;
    private List<EdgeDTO> edges;
}