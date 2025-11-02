# Assignment 4 – Smart City / Smart Campus Scheduling  
# Report is in pdf file 



**Variant:** Kosaraju SCC + Topological Sort + Shortest and Longest Path in DAG





---

##  Goal
This project combines two major algorithmic topics into one unified Smart City scheduling application:

1. **Strongly Connected Components (SCC)** and **Topological Ordering**
2. **Shortest Paths in Directed Acyclic Graphs (DAGs)**

The objective was to simulate real-world scheduling of Smart City or Smart Campus tasks such as maintenance, sensor updates, or repairs, where dependencies may form both cyclic and acyclic graphs.

Cyclic dependencies are detected and compressed into strongly connected components (SCCs), while acyclic dependencies are used to plan optimal task order and compute shortest and longest paths for performance analysis.

---

##  Scenario
In Smart City or Smart Campus systems, operational tasks (e.g., camera maintenance, street repair, sensor calibration) often depend on others.  
Some dependencies form **cycles** (repeated dependencies), and others are **acyclic** (can be executed in sequence).

This project provides a framework to:
- Detect and collapse cyclic dependencies into components.
- Derive a **condensed DAG** (Directed Acyclic Graph).
- Compute **topological order** of components.
- Run **shortest and longest path** algorithms for scheduling optimization.

---

##  What Was Implemented

###  SCC (Kosaraju)
- Implemented **Kosaraju’s Algorithm** for detecting strongly connected components.
- Runs **two DFS passes**:
    - First pass: record finishing order.
    - Second pass: traverse reversed graph to group SCCs.
- Output:
    - Number of SCCs
    - List of SCCs (components)
    - Condensation DAG

---

###  Topological Sort
- Implemented **Kahn’s Algorithm** on the condensation DAG.
- Provides a valid **topological order** of components.
- Derived order of original vertices after SCC compression.

---

###  Shortest and Longest Paths in DAG
- The project uses **edge-weight model**, where each edge weight represents time or cost.
- Implemented:
    - **Single-source shortest paths** using dynamic programming over topological order.
    - **Longest paths (critical path)** by maximizing weights along the order.
- Output includes:
    - Shortest distances from source
    - Longest (critical) path and its length
    - Reconstructed optimal path

---

## 🗂️ Project Structure
```
Assignment4_Kasaraju/
│
├── src/
│ ├── app/
│ │ └── Main.java
│ ├── graph/
│ │ ├── scc/
│ │ │ └── KosarajuSCC.java
│ │ ├── topo/
│ │ │ └── TopologicalSort.java
│ │ └── dagsp/
│ │ └── ShortestPathDAG.java
│ └── test/
│ └── PlaceholderTest.java
│
├── data/
│ ├── small*, medium*, large*.json
│ ├── results_summary.csv
│ 
│
├── pom.xml
└── README.md
```


