package gre.lab2.graph;

import java.util.*;

/**
 * Immutable directed graph with integer weighted edges. Negative weights are allowed. Instances can be multigraphs,
 * as such, there can be multiple edges for the same ordered pair of vertices, as well as self-loops.
 * <p>
 * Vertices are indexed from 0 to n-1, where n is the number of vertices in the graph.
 * <p>
 * Use the {@link Builder} class to construct a new graph.
 */
public final class WeightedDigraph {
  /**
   * Edge in a weighted digraph.
   *
   * @param from   Vertex index of the source vertex.
   * @param to     Vertex index of the destination vertex.
   * @param weight Weight of the edge.
   */
  public record Edge(int from, int to, int weight) {
  }

  /**
   * Digraph builder
   * <p>
   * Number of vertices must be specified at construction. Edges are added using {@link #addEdge(int, int, int)}.
   */
  public static class Builder {
    /**
     * List of outgoing edges for each vertex
     */
    private final List<List<Edge>> edges;

    /**
     * @param nVertices Number of vertices in the graph
     * @throws IllegalArgumentException if nVertices <= 0
     */
    public Builder(final int nVertices) {
      if (nVertices <= 0)
        throw new IllegalArgumentException(String.format("nVertices (%d) cannot be <= 0", nVertices));

      this.edges = new ArrayList<>(nVertices);

      for (int i = 0; i < nVertices; ++i) {
        edges.add(new LinkedList<>());
      }
    }

    /**
     * Builds an edge and adds it to the graph
     *
     * @param from   Index of source vertex
     * @param to     Index of destination vertex
     * @param weight Weight of the edge
     * @throws IndexOutOfBoundsException if {@code from} or {@code to} invalid
     */
    public void addEdge(final int from, final int to, final int weight) {
      assertValidIndex(from, edges.size());
      assertValidIndex(to, edges.size());

      edges.get(from).add(new Edge(from, to, weight));
    }

    /**
     * Builds the graph
     *
     * @return a new {@link WeightedDigraph}
     */
    public WeightedDigraph build() {
      ArrayList<List<Edge>> immutableEdges = new ArrayList<>(this.edges.size());
      for (List<Edge> edgeList : this.edges) {
        immutableEdges.add(Collections.unmodifiableList(edgeList));
      }

      return new WeightedDigraph(immutableEdges);
    }
  }

  /**
   * List of outgoing edges of each vertex
   */
  private final List<List<Edge>> outgoingEdges;

  /**
   * Constructs a new graph with the given edges.
   */
  private WeightedDigraph(final List<List<Edge>> outgoingEdges) {
    this.outgoingEdges = outgoingEdges;
  }

  /**
   * @return Number of vertices.
   */
  public int getNVertices() {
    return outgoingEdges.size();
  }

  /**
   * Returns an immutable list of unsorted outgoing edges for the given vertex. There are no guarantees on the
   * implementation type of the returned List.
   * <p>
   * Retrieving the list of outgoing edges for a vertex is O(1).
   *
   * @param vertex Vertex index.
   * @return Immutable list of outgoing edges for the given vertex.
   * @throws IndexOutOfBoundsException if {@code vertex} is < 0 or >= number of vertices.
   */
  public List<Edge> getOutgoingEdges(final int vertex) {
    assertValidIndex(vertex, outgoingEdges.size());
    return outgoingEdges.get(vertex);
  }

  /**
   * Asserts the given index is valid (0 <= index < nVertices)
   *
   * @param index     an index
   * @param nVertices number of vertices
   * @throws IndexOutOfBoundsException if {@code index} is invalid
   */
  private static void assertValidIndex(final int index, final int nVertices) {
    if (index < 0 || index >= nVertices)
      throw new IndexOutOfBoundsException(String.format("Vertex index (%d) must in [0, %d)", index, nVertices));
  }
}
