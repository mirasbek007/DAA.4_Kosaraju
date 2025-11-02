package app;

import com.google.gson.*;
import graph.scc.KosarajuSCC;
import graph.dagsp.Shortest_and_LongestPathDAG;
import graph.dagsp.Shortest_and_LongestPathDAG.Edge;
import graph.topo.TopologicalSort;

import java.nio.file.*;
import java.util.*;


public class Main {
    static class EdgeJson { String from, to; double weight; }
    static class GraphJson { List<String> nodes; List<EdgeJson> edges; }

    public static void main(String[] args) throws Exception {
        Path data = Paths.get("data");
        if (!Files.exists(data)) {
            System.err.println("No data/ folder found.");
            return;
        }
        Gson gson = new Gson();
        try (var files = Files.list(data)) {
            files.filter(p->p.toString().endsWith(".json")).sorted().forEach(path-> {
                System.out.println("==== Processing: " + path.getFileName() + " ====");
                try {
                    String txt = Files.readString(path);
                    GraphJson gj = gson.fromJson(txt, GraphJson.class);
                    runOnGraph(gj);
                } catch (Exception e) { e.printStackTrace(); }
            });
        }
    }

    private static void runOnGraph(GraphJson gj) {

        Map<String,List<String>> adj = new LinkedHashMap<>();
        for (String n: gj.nodes) adj.put(n, new ArrayList<>());
        for (var e: gj.edges) {
            adj.get(e.from).add(e.to);
        }

        KosarajuSCC scc = new KosarajuSCC(adj);
        List<List<String>> comps = scc.getSCCs();
        System.out.println("SCCs (count=" + comps.size() + "):");
        for (int i=0;i<comps.size();i++) {
            System.out.println(" comp"+i+": " + comps.get(i) + " size=" + comps.get(i).size());
        }


        Map<String,Integer> compId = new HashMap<>();
        for (int i=0;i<comps.size();i++) for (String v: comps.get(i)) compId.put(v,i);
        Map<String,List<String>> cond = new LinkedHashMap<>();
        for (int i=0;i<comps.size();i++) cond.put("C"+i, new ArrayList<>());

        Set<String> seen = new HashSet<>();
        for (var e: gj.edges) {
            int a = compId.get(e.from), b = compId.get(e.to);
            if (a!=b) {
                String key = "C"+a+"->"+"C"+b;
                if (!seen.contains(key)) {
                    cond.get("C"+a).add("C"+b);
                    seen.add(key);
                }
            }
        }
        System.out.println("Condensation DAG nodes: " + cond.keySet());
        System.out.println("Condensation edges:");
        for (var kv : cond.entrySet()) System.out.println("  " + kv.getKey() + " -> " + kv.getValue());


        try {
            List<String> topo = TopologicalSort.kahnTopo(cond);
            System.out.println("Topological order of components: " + topo);
            // derived order of original tasks: expand components in topo order
            List<String> derived = new ArrayList<>();
            for (String c : topo) {
                int id = Integer.parseInt(c.substring(1));
                derived.addAll(comps.get(id));
            }
            System.out.println("Derived task order after compression: " + derived);
        } catch (Exception ex) {
            System.out.println("Unable to topologically sort condensation DAG: " + ex.getMessage());
        }


        Map<String, Map<String, Double>> cmap = new HashMap<>();
        for (int i=0;i<comps.size();i++) cmap.put("C"+i, new HashMap<>());
        for (var e: gj.edges) {
            String A = "C"+compId.get(e.from);
            String B = "C"+compId.get(e.to);
            if (A.equals(B)) continue;
            double w = e.weight;
            cmap.get(A).put(B, Math.min(cmap.get(A).getOrDefault(B, Double.POSITIVE_INFINITY), w));
        }

        Map<String, List<Edge>> dag = new LinkedHashMap<>();
        for (var kv : cmap.entrySet()) {
            List<Edge> list = new ArrayList<>();
            for (var kv2 : kv.getValue().entrySet()) list.add(new Edge(kv2.getKey(), kv2.getValue()));
            dag.put(kv.getKey(), list);
        }
        if (dag.size()>0) {
            String src = dag.keySet().iterator().next();
            var sd = Shortest_and_LongestPathDAG.shortestPaths(dag, src);
            System.out.println("Shortest distances from " + src + ": " + sd);
            var lg = Shortest_and_LongestPathDAG.longestPaths(dag, src);
            System.out.println("Longest distances from " + src + " (critical path lengths): " + lg);
        }
        System.out.println();
    }
}
