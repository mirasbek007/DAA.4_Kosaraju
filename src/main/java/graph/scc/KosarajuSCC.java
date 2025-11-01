package graph.scc;

import java.util.*;

public class KosarajuSCC {
    private final Map<String, List<String>> g;
    private final Map<String, List<String>> grev;
    public KosarajuSCC(Map<String, List<String>> graph) {
        this.g = graph;
        this.grev = new HashMap<>();
        for (String u : graph.keySet()) grev.put(u, new ArrayList<>());
        for (var e : graph.entrySet()) {
            for (String v : e.getValue()) {
                grev.computeIfAbsent(v, k -> new ArrayList<>()).add(e.getKey());
            }
        }
    }

    public List<List<String>> getSCCs() {
        Set<String> visited = new HashSet<>();
        Deque<String> order = new ArrayDeque<>();
        for (String u : g.keySet()) if (!visited.contains(u)) dfs1(u, visited, order);
        visited.clear();
        List<List<String>> comps = new ArrayList<>();
        while (!order.isEmpty()) {
            String u = order.removeLast();
            if (!visited.contains(u)) {
                List<String> comp = new ArrayList<>();
                dfs2(u, visited, comp);
                comps.add(comp);
            }
        }
        return comps;
    }

    private void dfs1(String u, Set<String> vis, Deque<String> order) {
        vis.add(u);
        for (String v : g.getOrDefault(u, Collections.emptyList())) {
            if (!vis.contains(v)) dfs1(v, vis, order);
        }
        order.addLast(u);
    }

    private void dfs2(String u, Set<String> vis, List<String> comp) {
        vis.add(u);
        comp.add(u);
        for (String v : grev.getOrDefault(u, Collections.emptyList())) {
            if (!vis.contains(v)) dfs2(v, vis, comp);
        }
    }
}
