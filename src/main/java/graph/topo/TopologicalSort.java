package graph.topo;

import java.util.*;
public class TopologicalSort {
    public static List<String> kahnTopo(Map<String, List<String>> graph) {
        Map<String,Integer> indeg = new HashMap<>();
        for (String u : graph.keySet()) indeg.put(u, 0);
        for (var e : graph.entrySet()) {
            for (String v : e.getValue()) {
                indeg.put(v, indeg.getOrDefault(v,0)+1);
            }
        }
        Deque<String> q = new ArrayDeque<>();
        for (var kv : indeg.entrySet()) if (kv.getValue()==0) q.add(kv.getKey());
        List<String> order = new ArrayList<>();
        while (!q.isEmpty()) {
            String u = q.removeFirst();
            order.add(u);
            for (String v : graph.getOrDefault(u, Collections.emptyList())) {
                indeg.put(v, indeg.get(v)-1);
                if (indeg.get(v)==0) q.addLast(v);
            }
        }
        if (order.size() != graph.size()) {
            throw new IllegalArgumentException("Graph contains cycle; topological order not possible");
        }
        return order;
    }
}
