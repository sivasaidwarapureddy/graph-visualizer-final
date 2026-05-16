package com.graph.graphvisualizer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EdgeDTO {
    private int source;
    private int destination;
}