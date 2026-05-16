package com.graph.graphvisualizer.controller;

import com.graph.graphvisualizer.dto.*;
import com.graph.graphvisualizer.model.Graph;
import com.graph.graphvisualizer.service.GraphService;
import com.graph.graphvisualizer.util.GraphUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/graph")
@CrossOrigin(
        origins = "*",
        allowedHeaders = "*",
        methods = {
                RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.PUT,
                RequestMethod.DELETE,
                RequestMethod.OPTIONS
        }
)
public class GraphController {

    @Autowired
    private GraphService service;

    // ✅ PREFLIGHT FIX
    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions() {
        return ResponseEntity.ok().build();
    }

    // ✅ CREATE GRAPH
    @PostMapping("/create")
    public GraphResponse create(@RequestBody GraphCreateRequest req) {

        Graph graph = service.saveGraph(req);

        return service.toDTO(graph);
    }

    // ✅ GET GRAPH
    @GetMapping("/{id}")
    public GraphResponse getGraphById(@PathVariable Long id) {

        Graph graph = service.getGraph(id);

        return service.toDTO(graph);
    }

    // ✅ BFS
    @PostMapping("/bfs")
    public TraversalResponse bfs(@RequestBody AlgoRequest req) {

        Graph graph = service.getGraph(req.getGraphId());

        var adj = service.buildAdjList(graph);

        return GraphUtil.bfsWithSteps(
                graph.getNodes(),
                adj,
                req.getStartNode()
        );
    }

    // ✅ DFS
    @PostMapping("/dfs")
    public TraversalResponse dfs(@RequestBody AlgoRequest req) {

        Graph graph = service.getGraph(req.getGraphId());

        var adj = service.buildAdjList(graph);

        return GraphUtil.dfsWithSteps(
                graph.getNodes(),
                adj,
                req.getStartNode()
        );
    }
}