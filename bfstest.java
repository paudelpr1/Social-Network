import java.util.LinkedList;

public class bfstest {
    public static void main(String[] args)
    {
        DiGraph graph = new DiGraph();
        int src = 0;
        int dest = 2; 
        int[] pred = new int[4];
        int dist[] = new int[4];
        int v = 8; 
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);

        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(0, 3);
        graph.addEdge(1, 2);
        graph.addEdge(3, 0);
        graph.addEdge(3, 1);
        graph.addEdge(3, 2);

        System.out.println(graph);
    
    
   
        LinkedList<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[v];

        for(int i = 0; i < v; i++)
        {
            visited[i] = false;
            dist[i] = Integer.MAX_VALUE;
            pred[i] = -1;
        }

        visited[src] = true;
        dist[src] = 0;
        queue.add(src);
        boolean flag = false;
        while(!queue.isEmpty() && !flag)
        {
            int u = queue.remove();
            for(int w : graph.getAdjacent(u))
            {
                if(visited[w] == false)
                {
                    visited[w] = true;
                    dist[w] = dist[u] + 1; 
                    pred[w] = u;
                    queue.add(w);

                    if(w == dest)
                        flag = true;
                }
            }
        }

    }
}
