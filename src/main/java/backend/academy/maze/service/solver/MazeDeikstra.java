package backend.academy.maze.service.solver;

import backend.academy.maze.model.Maze;
import backend.academy.maze.model.Node;
import backend.academy.maze.model.Wall;
import java.util.List;
import java.util.TreeSet;

/**
 * Finds the path between given cells in given maze, which has the lowest cost.
 * It collects all the unvisited cells which are connected by one step to the visited ones starting from the start cell.
 * Chooses from them using {@link TreeSet} the lowest one by cost, marks it as visited,
 * updates unvisited step-one cells from it and goes over again until reaching the end cell.
 */
public class MazeDeikstra implements MazeSolver {
    @Override
    public String findPath(Maze maze, Node startNode, Node endNode) {
        Node[][] par = new Node[maze.height()][maze.width()];
        TreeSet<NodeInfo> curPaths = new TreeSet<>();
        curPaths.add(new NodeInfo(maze.getCell(startNode).speed(), startNode, startNode));

        while (!curPaths.isEmpty()) {
            NodeInfo minCost = curPaths.removeFirst();
            Node cur = minCost.node;
            par[cur.row()][cur.col()] = minCost.par;

            if (cur.equals(endNode)) {
                setPath(maze, startNode, endNode, par);
                return String.format("Found path with total cost %d", minCost.weight);
            }

            List<Node> neighboors = cur.getNeighbours(maze.height(), maze.width(), vert ->
                par[vert.row()][vert.col()] == null
                    && maze.getWall(cur, vert) != Wall.EXIST
            );
            for (Node vert : neighboors) {
                curPaths.add(new NodeInfo(minCost.weight + maze.getCell(vert).speed(), cur, vert));
            }
        }
        return NOT_FOUND_RESPONSE;
    }

    private record NodeInfo(int weight, Node par, Node node) implements Comparable<NodeInfo> {
        @Override
        public int compareTo(NodeInfo o) {
            if (weight != o.weight) {
                return weight - o.weight;
            }
            int res = par.compareTo(o.par);
            if (res != 0) {
                return res;
            } else {
                return node.compareTo(o.node);
            }
        }
    }
}
