package com.graph.graphvisualizer.util;

import com.graph.graphvisualizer.dto.TraversalResponse;

import java.util.*;

public class GraphUtil {
public static TraversalResponse bfsWithSteps(int V, List<List<Integer>> adj, int start) {
    boolean[] visited = new boolean[V];
    Queue<Integer> q = new LinkedList<>();

    List<Integer> order = new ArrayList<>();
    List<Map<String, Object>> steps = new ArrayList<>();

    q.add(start);
    visited[start] = true;

    while (!q.isEmpty()) {
        int node = q.poll();
        order.add(node);

        for (int neighbor : adj.get(node)) {
            if (!visited[neighbor]) {
                visited[neighbor] = true;
                q.add(neighbor);
            }
        }

        Map<String, Object> step = new HashMap<>();
        step.put("current", node);
        step.put("queue", new ArrayList<>(q)); // 🔥 REAL QUEUE
        steps.add(step);
    }

    return new TraversalResponse(order, steps);
}
public static void dfsHelper(int node, boolean[] visited,
                             List<List<Integer>> adj,
                             List<Integer> order,
                             List<Map<String, Object>> steps,
                             List<Integer> path) {

    visited[node] = true;
    order.add(node);

    path.add(node); // 🔥 push to stack

    Map<String, Object> step = new HashMap<>();
    step.put("current", node);
    step.put("stack", new ArrayList<>(path)); // 🔥 send stack
    steps.add(step);

    for (int neighbor : adj.get(node)) {
        if (!visited[neighbor]) {
            dfsHelper(neighbor, visited, adj, order, steps, path);
        }
    }

    path.remove(path.size() - 1); // 🔥 pop (backtrack)
}
    public static TraversalResponse dfsWithSteps(int V, List<List<Integer>> adj, int start) {
    boolean[] visited = new boolean[V];
    List<Integer> order = new ArrayList<>();
    List<Map<String, Object>> steps = new ArrayList<>();

    List<Integer> path = new ArrayList<>(); // 🔥 ADD THIS

    dfsHelper(start, visited, adj, order, steps, path); // 🔥 PASS PATH

    return new TraversalResponse(order, steps);
    }
}