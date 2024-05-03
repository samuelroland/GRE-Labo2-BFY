package gre.lab2.graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Henrik Akesson
 */
public final class WeightedDigraphReader {
  /** Number of tokens expected on a line */
  private static final int N_TOKENS = 3;
  /** Error message used if number of lines does not match number of edges. */
  private static final String ILLEGAL_N_LINES = "Illegal number of lines (%d lines, expected %d)";
  /** Error message used if a line contains an insufficient number of tokens. */
  private static final String ILLEGAL_N_TOKENS = "Invalid number of tokens in line (got %d, expected %d)";
  /** Error message used if an edge is declared with an invalid vertex id. */
  private static final String INVALID_VERTEX_ID = "Invalid vertex id (got %d, should be in [%d, %d])";

  private WeightedDigraphReader() {}

  private static boolean notInRange(final int value, final int max) {
    return value > max || value < 0;
  }

  public static WeightedDigraph fromInputStreamReader(final InputStreamReader inputStreamReader) throws IOException {
    try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
      String line = bufferedReader.readLine();
      if (line == null) {
        return null;
      }
      String[] tokens = line.split(" ");
      final int nVertices = Integer.parseInt(tokens[0]);
      final WeightedDigraph.Builder builder = new WeightedDigraph.Builder(nVertices);
      final int nEdges = Integer.parseInt(tokens[1]);
      for (int i = 0; i < nEdges; i++) {
        line = bufferedReader.readLine();
        if (line == null) {
          throw new IllegalArgumentException(String.format(ILLEGAL_N_LINES, i, nEdges));
        }
        tokens = line.split(" ");
        // Extra tokens are ignored
        if (tokens.length < N_TOKENS) {
          throw new IllegalArgumentException(String.format(ILLEGAL_N_TOKENS, N_TOKENS, tokens.length));
        }
        final int from = Integer.parseInt(tokens[0]);
        final int to = Integer.parseInt(tokens[1]);
        final int weight = Integer.parseInt(tokens[2]);
        if (notInRange(from, nVertices-1)) {
          throw new IllegalArgumentException(String.format(INVALID_VERTEX_ID, from, 0, nVertices-1));
        }
        if (notInRange(to, nVertices-1)) {
          throw new IllegalArgumentException(String.format(INVALID_VERTEX_ID, to, 0, nVertices-1));
        }
        builder.addEdge(from, to, weight);
      }

      return builder.build();
    }
  }

  public static WeightedDigraph fromFile(final String filename) throws IOException {
    return fromInputStreamReader(new FileReader(filename));
  }

}
