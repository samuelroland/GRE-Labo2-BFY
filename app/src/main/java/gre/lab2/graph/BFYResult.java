package gre.lab2.graph;

import java.util.Arrays;
import java.util.List;

/**
 * Result type for the Bellman-Ford-Yens algorithm.
 * <p>
 * A result is either:
 * <ul>
 *   <li>a {@link ShortestPathTree}, when no negative cycle is reachable from the source vertex;</li>
 *   <li>a {@link NegativeCycle}, when a negative cycle is reachable from the source vertex.</li>
 * </ul>
 */
public sealed interface BFYResult {
  /**
   * Value used to represent an unreachable vertex in the shortest path tree.
   */
  int UNREACHABLE = -1;

  /**
   * Bellman-Ford-Yens result as the shortest path tree rooted at the source vertex.
   *
   * @param distances    Distance from the source vertex to each reachable vertex in the graph.
   *                     {@link Integer#MAX_VALUE} if the vertex is unreachable.
   * @param predecessors Predecessor of each reachable vertex in the shortest path tree.
   *                     {@link BFYResult#UNREACHABLE} if the vertex is unreachable.
   */
  record ShortestPathTree(int[] distances, int[] predecessors) implements BFYResult {
    @Override
    public boolean isNegativeCycle() {
      return false;
    }

    @Override
    public ShortestPathTree getShortestPathTree() {
      return this;
    }

    @Override
    public String toString() {
      return "ShortestPathTree{" +
            "distances=" + Arrays.toString(distances) +
            ", predecessors=" + Arrays.toString(predecessors) +
            '}';
    }
  }

  /**
   * Bellman-Ford-Yens result as the first negative cycle reachable from the source vertex.
   * <p>
   * The {@code vertices} List contains the vertices in the negative cycle in order. There are no guarantees
   * on the implementation type of the List.
   *
   * @param vertices List of vertices in the first negative cycle reachable from the source vertex.
   * @param length   Length of the negative cycle (always negative).
   */
  record NegativeCycle(List<Integer> vertices, int length) implements BFYResult {
    @Override
    public boolean isNegativeCycle() {
      return true;
    }

    @Override
    public NegativeCycle getNegativeCycle() {
      return this;
    }

    @Override
    public String toString() {
      return "NegativeCycle{" +
            "vertices=" + vertices +
            ", length=" + length +
            '}';
    }
  }

  /**
   * @return {@code true} if the result is a {@link NegativeCycle}, {@code false} otherwise.
   */
  boolean isNegativeCycle();

  /**
   * @return The {@link ShortestPathTree} if the result is a {@link ShortestPathTree}, {@code null} otherwise.
   */
  default ShortestPathTree getShortestPathTree() {
    return null;
  }

  /**
   * @return The {@link NegativeCycle} if the result is a {@link NegativeCycle}, {@code null} otherwise.
   */
  default NegativeCycle getNegativeCycle() {
    return null;
  }
}
