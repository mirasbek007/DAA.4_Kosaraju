package graph.dagsp;

import java.util.*;


public class Shortest_and_LongestPathDAG {
    public static class Edge {
        public final String to;
        public final double w;
        public Edge(String to, double w){ this.to=to; this.w=w; }
    }


    public static Map<String, Double> shortestPaths(Map<String, List<Edge>> g, String src) {
        List<String> topo = topoOrder(g);
        Map<String, Double> dist = new HashMap<>();
        for (String u : g.keySet()) dist.put(u, Double.POSITIVE_INFINITY);
        dist.put(src, 0.0);
        for (String u : topo) {
            double du = dist.getOrDefault(u, Double.POSITIVE_INFINITY);
            if (Double.isInfinite(du)) continue;
            for (Edge e : g.getOrDefault(u, Collections.emptyList())) {
                double nd = du + e.w;
                if (nd < dist.getOrDefault(e.to, Double.POSITIVE_INFINITY)) {
                    dist.put(e.to, nd);
                }
            }
        }
        return dist;
    }


    public static Map<String, Double> longestPaths(Map<String, List<Edge>> g, String src) {
        List<String> topo = topoOrder(g);
        Map<String, Double> dist = new HashMap<>();
        for (String u : g.keySet()) dist.put(u, Double.NEGATIVE_INFINITY);
        dist.put(src, 0.0);
        for (String u : topo) {
            double du = dist.getOrDefault(u, Double.NEGATIVE_INFINITY);
            if (Double.isInfinite(du) && du<0) continue;
            for (Edge e : g.getOrDefault(u, Collections.emptyList())) {
                double nd = du + e.w;
                if (nd > dist.getOrDefault(e.to, Double.NEGATIVE_INFINITY)) {
                    dist.put(e.to, nd);
                }
            }
        }
        return dist;
    }

    private static List<String> topoOrder(Map<String, List<Edge>> g) {

        Map<String, List<String>> adj = new HashMap<>();
        for (var kv : g.entrySet()) {
            adj.putIfAbsent(kv.getKey(), new ArrayList<>());
            for (Edge e : kv.getValue()) adj.get(kv.getKey()).add(e.to);
            for (Edge e : kv.getValue()) adj.putIfAbsent(e.to, new ArrayList<>());
        }
        return graph.topo.TopologicalSort.kahnTopo(adj);
    }


    public static List<String> reconstructShortestPath(Map<String,List<Edge>> g, String src, String tgt) {
        List<String> topo = topoOrder(g);
        Map<String, Double> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        for (String u : g.keySet()) dist.put(u, Double.POSITIVE_INFINITY);
        dist.put(src, 0.0);
        for (String u : topo) {
            double du = dist.getOrDefault(u, Double.POSITIVE_INFINITY);
            if (Double.isInfinite(du)) continue;
            for (Edge e : g.getOrDefault(u, Collections.emptyList())) {
                double nd = du + e.w;
                if (nd < dist.getOrDefault(e.to, Double.POSITIVE_INFINITY)) {
                    dist.put(e.to, nd);
                    prev.put(e.to, u);
                }
            }
        }
        if (!prev.containsKey(tgt) && !src.equals(tgt)) return Collections.emptyList();
        LinkedList<String> path = new LinkedList<>();
        String cur = tgt;
        path.addFirst(cur);
        while (!cur.equals(src)) {
            cur = prev.get(cur);
            if (cur==null) break;
            path.addFirst(cur);
        }
        return path;
    }
}
