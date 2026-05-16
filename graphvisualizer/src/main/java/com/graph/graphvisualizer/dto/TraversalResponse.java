package com.graph.graphvisualizer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class TraversalResponse {
    private List<Integer> order;
    private List<Map<String, Object>> steps;
}