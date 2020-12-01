package domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Graf {

    private int nrV;
    private Map<Integer, HashSet<Integer>> adjMap;
    private Map<Integer, HashSet<Integer>> comunitati;
    private static Map<Integer, Integer> visited;

    public Graf(int nrNoduri) {
        this.nrV = nrNoduri;
        this.visited = new HashMap<Integer, Integer>();
        this.comunitati = new HashMap<Integer, HashSet<Integer>>();
        this.adjMap = new HashMap<Integer, HashSet<Integer>>();
        for (int i = 1; i < nrV; i++) {
            adjMap.putIfAbsent(i, new HashSet<Integer>());
            visited.put(i, 0);
        }
    }

    public void addEdge(int s, int d) {
        adjMap.putIfAbsent(s, new HashSet<Integer>());
        adjMap.putIfAbsent(d, new HashSet<Integer>());
        adjMap.get(s).add(d);
        adjMap.get(d).add(s);
        visited.put(s, 0);
        visited.put(d, 0);
    }

    private void findDFS(int vertex, int Count) {
        visited.put(vertex, 1);
        for (Integer child : adjMap.get(vertex)) {
            if (visited.get(child) == 0) {
                comunitati.get(Count).add(child);
                findDFS(child, Count);
            }
        }
    }


    public int count() {

        int Count = 0;
        for (Integer vertex : visited.keySet()) {
            if (visited.get(vertex) == 0) {
                comunitati.putIfAbsent(Count, new HashSet<Integer>());
                comunitati.get(Count).add(vertex);
                findDFS(vertex, Count);
                Count++;

            }
        }
     /*for (Integer v : comunitati.keySet()) {
            System.out.println(comunitati.get(v));
        }*/


        return Count;
    }

    public HashSet<Integer> sociabila() {
        count();
        int max = 0;
        int key = 0;
        for (Integer vertex : comunitati.keySet()) {
            if (comunitati.get(vertex).size() > max) {
                max = comunitati.get(vertex).size();
                key = vertex;
            }
        }
        return comunitati.get(key);

    }


}

