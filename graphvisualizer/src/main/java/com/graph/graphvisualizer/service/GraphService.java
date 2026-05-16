package com.graph.graphvisualizer.service;

import com.graph.graphvisualizer.dto.EdgeDTO;
import com.graph.graphvisualizer.dto.GraphCreateRequest;
import com.graph.graphvisualizer.dto.GraphResponse;
import com.graph.graphvisualizer.model.Edge;
import com.graph.graphvisualizer.model.Graph;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class GraphService {

    // 🔥 In-memory storage
    private final Map<Long, Graph> graphStore = new HashMap<>();

    // 🔥 Auto increment ID
    private final AtomicLong idGenerator = new AtomicLong(1);

    // ✅ SAVE GRAPH
    public Graph saveGraph(GraphCreateRequest req) {

        Graph graph = new Graph();

        // 🔥 set auto id
        graph.setId(idGenerator.getAndIncrement());

        graph.setNodes(req.getNodes());
        graph.setDirected(req.isDirected());

        List<Edge> edges = new ArrayList<>();

        for (List<Integer> e : req.getEdges()) {

            Edge edge = new Edge();

            edge.setSource(e.get(0));
            edge.setDestination(e.get(1));

            edges.add(edge);
        }

        graph.setEdges(edges);

        // 🔥 save in memory
        graphStore.put(graph.getId(), graph);

        return graph;
    }

    // ✅ GET GRAPH
    public Graph getGraph(Long id) {

        Graph graph = graphStore.get(id);

        if (graph == null) {
            throw new RuntimeException("Graph not found");
        }

        return graph;
    }

    // ✅ BUILD ADJ LIST
    public List<List<Integer>> buildAdjList(Graph graph) {

        int n = graph.getNodes();

        List<List<Integer>> adj = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }

        for (Edge e : graph.getEdges()) {

            adj.get(e.getSource()).add(e.getDestination());

            if (!graph.isDirected()) {
                adj.get(e.getDestination()).add(e.getSource());
            }
        }

        return adj;
    }

    // ✅ CONVERT TO DTO
    public GraphResponse toDTO(Graph graph) {

        List<EdgeDTO> edgeList = graph.getEdges().stream()
                .map(e -> new EdgeDTO(
                        e.getSource(),
                        e.getDestination()
                ))
                .toList();

        return new GraphResponse(
                graph.getId(),
                graph.getNodes(),
                graph.isDirected(),
                edgeList
        );
    }
}