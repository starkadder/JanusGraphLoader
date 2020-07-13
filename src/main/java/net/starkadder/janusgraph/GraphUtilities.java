package net.starkadder.janusgraph;

import net.starkadder.utilities.StarkadderException;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.HashMap;
import java.util.Map;

public class GraphUtilities {


    public static GraphTraversal<Vertex, Vertex> getVertex(String label, Map<String, Object> required, GraphTraversal<Vertex, Vertex> gt) throws StarkadderException {
        GraphTraversal<Vertex, Vertex> g = gt;
        if (required.isEmpty()) throw new StarkadderException("required properties must be provided");

        // first check if the vertex exits
        for (Map.Entry<String, Object> entry : required.entrySet()) {
            g = g.has(entry.getKey(), entry.getValue());
        }
        return g.hasLabel(label);

    }

    /**
     * Add vertex if it does not exists
     *
     * @param label    label of the vertex
     * @param required required properties of the vertex that make it unique (eg its id)
     * @param extra    optional vertex properties
     * @param gt       a graph traversal
     * @return a graph traversal to continue working with
     * @throws StarkadderException
     */
    public static GraphTraversal<Vertex, Vertex> addVertex(String label, Map<String, Object> required, Map<String, Object> extra, GraphTraversal<Vertex, Vertex> gt) throws StarkadderException {
        GraphTraversal<Vertex, Vertex> g = getVertex(label, required, gt);
        // if the vertex exists, return it
        // otherwise add the new vertex
        return g.fold().coalesce(__.unfold(), addVertexProps(merge(required, extra), __.addV(label)));

    }

    /**
     * Add an edge between a source and a target if it does not exists
     *
     * @param source   step label for the source vertex
     * @param target   step label for the target vertex
     * @param label    label of the edge
     * @param required required properties of the edge that make it unique
     * @param extra    optional edge properties
     * @param gt       graph traversal with step labels for source and target
     * @return
     */
    public static GraphTraversal<Vertex, Vertex> addEdge(String source, String target, String label, Map<String, Object> required, Map<String, Object> extra, GraphTraversal<Vertex, Vertex> gt) {
        GraphTraversal<Vertex, Vertex> g = gt.select(source).coalesce(
                GraphUtilities.addEdgeHas(required, __.outE(label).where(__.inV().as(target))),
                GraphUtilities.addEdgeProps(merge(required,extra),  __.addE(label)).to(target)).select(source);
        return g;
    }

    private static GraphTraversal<Vertex, Vertex> addVertexProps(Map<String, Object> properites, GraphTraversal<Vertex, Vertex> gt) {
        GraphTraversal<Vertex, Vertex> g = gt;
        for (Map.Entry<String, Object> entry : properites.entrySet()) {
            g = g.property(entry.getKey(), entry.getValue());
        }
        return g;
    }

    public static GraphTraversal<Vertex, Edge> addEdgeHas(Map<String, Object> has, GraphTraversal<Vertex, Edge> gt) {
        GraphTraversal<Vertex, Edge> g = gt;
        for (Map.Entry<String, Object> entry : has.entrySet()) {
            g = g.has(entry.getKey(), entry.getValue());
        }
        return g;
    }


    public static GraphTraversal<Vertex, Edge> addEdgeProps(Map<String, Object> properites, GraphTraversal<Vertex, Edge> gt) {
        GraphTraversal<Vertex, Edge> g = gt;
        for (Map.Entry<String, Object> entry : properites.entrySet()) {
                g = g.property(entry.getKey(), entry.getValue());
        }
        return g;
    }


    private static Map<String, Object> merge(Map<String, Object> map1, Map<String, Object> map2) {
        Map<String, Object> map = new HashMap();
        for (Map.Entry<String, Object> entry : map2.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, Object> entry : map1.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

}
