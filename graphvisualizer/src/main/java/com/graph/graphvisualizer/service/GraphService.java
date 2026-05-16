package com.graph.graphvisualizer.service;

import com.graph.graphvisualizer.dto.EdgeDTO;
import com.graph.graphvisualizer.dto.GraphCreateRequest;
import com.graph.graphvisualizer.dto.GraphResponse;
import com.graph.graphvisualizer.model.Edge;
import com.graph.graphvisualizer.model.Graph;
import com.graph.graphvisualizer.repository.EdgeRepository;
import com.graph.graphvisualizer.repository.GraphRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class GraphService {

    @Autowired
    private GraphRepository graphRepo;

    @Autowired
    private EdgeRepository edgeRepo;

    public Graph saveGraph(GraphCreateRequest req) {

        Graph graph = new Graph();
        graph.setNodes(req.getNodes());
        graph.setDirected(req.isDirected());

        graph = graphRepo.save(graph);

        List<Edge> edges = new ArrayList<>();

        for (List<Integer> e : req.getEdges()) {
            Edge edge = new Edge();
            edge.setSource(e.get(0));
            edge.setDestination(e.get(1));
            edge.setGraph(graph);
            edges.add(edge);
        }

        edgeRepo.saveAll(edges);
        graph.setEdges(edges);

        return graph;
    }

    public Graph getGraph(Long id) {
        Graph graph = graphRepo.findById(id).orElseThrow();
        graph.setEdges(edgeRepo.findByGraphId(id));
        return graph;
    }

    public List<List<Integer>> buildAdjList(Graph graph) {
        int n = graph.getNodes();
        List<List<Integer>> adj = new ArrayList<>();

        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());

        for (Edge e : graph.getEdges()) {
            adj.get(e.getSource()).add(e.getDestination());

            if (!graph.isDirected()) {
                adj.get(e.getDestination()).add(e.getSource());
            }
        }

        return adj;
    }
    public GraphResponse toDTO(Graph graph) {

    List<EdgeDTO> edgeList = graph.getEdges().stream()
            .map(e -> new EdgeDTO(e.getSource(), e.getDestination()))
            .toList();

    return new GraphResponse(
            graph.getId(),
            graph.getNodes(),
            graph.isDirected(),
            edgeList
    );
}
}