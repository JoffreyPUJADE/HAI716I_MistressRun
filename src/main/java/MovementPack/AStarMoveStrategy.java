package MovementPack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import AlgoPack.Node;
import AlgoPack.Pair;
import CharacterPack.Character;
import TilePack.Tile;

public class AStarMoveStrategy {
        public static List<Node> findPath(Pair<Tile, int[]> start, Pair<Tile, int[]> goal,
                                          ArrayList<ArrayList<Tile>> tiles,
                                          ArrayList<ArrayList<Character>> characters) {
            int[] startCoords = start.getValue();
            int[] goalCoords = goal.getValue();
    
            List<Node> openList = new ArrayList<>();
            Set<Node> closedList = new HashSet<>();
            Map<Node, Node> cameFrom = new HashMap<>();
    
            Node startNode = new Node(startCoords[0], startCoords[1], null, 0, calculateHCost(startCoords, goalCoords));
            openList.add(startNode);
    
            while (!openList.isEmpty()) {
                Node currentNode = openList.stream()
                                           .min(Comparator.comparingInt(Node::getFCost))
                                           .orElseThrow(NoSuchElementException::new);
    
                openList.remove(currentNode);
                closedList.add(currentNode);
    
                if (currentNode.getX() == goalCoords[0] && currentNode.getY() == goalCoords[1]) {
                    return reconstructPath(cameFrom, currentNode);
                }
    
                List<Node> neighbors = getNeighbors(currentNode, tiles, characters, goalCoords);
    
                for (Node neighbor : neighbors) {
                    if (closedList.contains(neighbor)) {
                        continue;
                    }
    
                    int tentativeGCost = currentNode.getGCost() + 1;
    
                    if (!openList.contains(neighbor) || tentativeGCost < neighbor.getGCost()) {
                        cameFrom.put(neighbor, currentNode);
                        neighbor.setGCost(tentativeGCost);
                        neighbor.setFCost(neighbor.getGCost() + neighbor.getHCost());
    
                        if (!openList.contains(neighbor)) {
                            openList.add(neighbor);
                        }
                    }
                }
            }
    
            return Collections.emptyList();
        }
    
        private static List<Node> reconstructPath(Map<Node, Node> cameFrom, Node currentNode) {
            List<Node> path = new ArrayList<>();
    
            while (currentNode != null) {
                path.add(currentNode);
                currentNode = cameFrom.get(currentNode);
            }
    
            Collections.reverse(path);
    
            return path;
        }
    
        private static List<Node> getNeighbors(Node node, ArrayList<ArrayList<Tile>> tiles,
                                               ArrayList<ArrayList<Character>> characters, int[] targetPosition) {
            List<Node> neighbors = new ArrayList<>();
    
            int[] dx = {-1, 1, 0, 0};
            int[] dy = {0, 0, -1, 1};
    
            for (int i = 0; i < 4; i++) {
                int newX = node.getX() + dx[i];
                int newY = node.getY() + dy[i];
    
                if (isInBounds(newX, newY, tiles) && !isObstacle(tiles, characters, newX, newY)) {
                    int hCost = calculateHCost(new int[]{newX, newY}, targetPosition);
                    neighbors.add(new Node(newX, newY, node, node.getGCost() + 1, hCost));
                }
            }
    
            return neighbors;
        }
        
        private static boolean isObstacle(ArrayList<ArrayList<Tile>> map, ArrayList<ArrayList<Character>> charInClass,
                                          int x, int y) {
            return map.get(x).get(y).isObstacle() || charInClass.get(x).get(y) != null;
        }
    
        private static boolean isInBounds(int x, int y, ArrayList<ArrayList<Tile>> map) {
            return x >= 0 && x < map.size() && y >= 0 && y < map.get(x).size();
        }
    
        private static int calculateHCost(int[] currentPosition, int[] targetPosition) {
            int dx = Math.abs(currentPosition[0] - targetPosition[0]);
            int dy = Math.abs(currentPosition[1] - targetPosition[1]);
            return dx + dy;
        }
    }
    