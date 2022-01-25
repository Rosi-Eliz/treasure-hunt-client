package client.AI.PathFinder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class AIEngineShortestPath implements AIEngineShortestPathFindable{
    private HashMap<GraphNode, DijkstraNode> dijkstraDataSource = new HashMap<>();
    private static Logger LOGGER = LoggerFactory.getLogger(AIEngineShortestPath.class);

    private class DijkstraNode  {
        private int distance;
        private DijkstraNode previousNode;
        private GraphNode currentNode;

        public DijkstraNode(int distance, DijkstraNode previousNode, GraphNode currentNode) {
            this.distance = distance;
            this.previousNode = previousNode;
            this.currentNode = currentNode;
        }

        public int getDistance() {
            return distance;
        }

        public GraphNode getCurrentNode() {
            return currentNode;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public void setPreviousNode(DijkstraNode previousNode) {
            this.previousNode = previousNode;
        }

        public void setCurrentNode(GraphNode currentNode) {
            this.currentNode = currentNode;
        }
    }

    private List<GraphNode> constructPathTo(DijkstraNode destinationNode){
        List<GraphNode> path = new LinkedList<>();
        if(destinationNode.previousNode == null){
            path.add(destinationNode.getCurrentNode());
            return path;
        }
        path.add(destinationNode.getCurrentNode());
        path.addAll(constructPathTo(destinationNode.previousNode));
        return path;
    }

    class NodeComparator implements Comparator<DijkstraNode> {
        public int compare(DijkstraNode node1, DijkstraNode node2)
        {
            return node1.getDistance() - node2.getDistance();
        }
    }

    @Override
    public List<GraphNode> calculateShortestPathBetween(GraphNode start, GraphNode destination, MapGraph graph) {
        LOGGER.info("Calculating shortest path between {} and {}", start, destination);
        initializeGraph(graph);
        dijkstraDataSource.put(start, new DijkstraNode(0, null, start));

        ArrayList<DijkstraNode> unvisitedNodes = new ArrayList<>(dijkstraDataSource.values());
        Set<DijkstraNode> visitedNodes = new HashSet<>();
        while(!unvisitedNodes.isEmpty())
        {
            Collections.sort(unvisitedNodes, new NodeComparator());
            DijkstraNode currentDijkstraNode = unvisitedNodes.get(0);
            visitedNodes.add(currentDijkstraNode);
            List<GraphEdge> outgoingEdges = currentDijkstraNode.getCurrentNode()
                    .getEdges()
                .stream()
                .filter(edge -> !visitedNodes.contains(dijkstraDataSource.get(edge.getToNode())))
                .collect(Collectors.toList());
            unvisitedNodes.remove(currentDijkstraNode);
            for(GraphEdge edge : outgoingEdges){
                DijkstraNode node = dijkstraDataSource.get(edge.getToNode());
                int distance = currentDijkstraNode.distance + edge.getWeight();
                if(node.getDistance() > distance)
                {
                    node.setPreviousNode(currentDijkstraNode);
                    node.setDistance(distance);
                }
            }
        }
        LOGGER.info("Shortest path between {} and {} calculated", start, destination);
        List<GraphNode> outputNodes = constructPathTo(dijkstraDataSource.get(destination));
        Collections.reverse(outputNodes);
        return outputNodes;
    }

    private void initializeGraph(MapGraph mapGraph){
        for(GraphNode node : mapGraph.getAllNodes()){
            dijkstraDataSource.put(node, new DijkstraNode(100000, null, node));
        }
    }
}
