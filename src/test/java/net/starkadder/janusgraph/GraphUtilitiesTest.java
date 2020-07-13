package net.starkadder.janusgraph;

import net.starkadder.utilities.StarkadderException;
import org.apache.commons.configuration.BaseConfiguration;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import static org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph.*;

import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphUtilitiesTest {


    @Test
    public void case1() throws StarkadderException {

        BaseConfiguration cfg = new BaseConfiguration();
        cfg.setProperty(Graph.GRAPH, TinkerGraph.class.getCanonicalName());
        cfg.setProperty(GREMLIN_TINKERGRAPH_VERTEX_ID_MANAGER, "LONG");
        cfg.setProperty(GREMLIN_TINKERGRAPH_EDGE_ID_MANAGER, "LONG");
        cfg.setProperty(GREMLIN_TINKERGRAPH_VERTEX_PROPERTY_ID_MANAGER, "LONG");

        TinkerGraph graph = TinkerGraph.open(cfg);


        Map<String, Object> requiredA = new HashMap<String, Object>();
        Map<String, Object> extraA = new HashMap<String, Object>();

        requiredA.put("id", "A");
        extraA.put("date", new Date());

        Map<String, Object> requiredB = new HashMap<String, Object>();
        Map<String, Object> extraB = new HashMap<String, Object>();

        requiredB.put("value", "B");
        extraB.put("status", new Integer(1));


        Map<String, Object> requiredE = new HashMap<String, Object>();
        Map<String, Object> extraE = new HashMap<String, Object>();
        requiredE.put("relationship", "purchase");

        // add vertex A if it does not exist
        GraphTraversal<Vertex, Vertex> gt = GraphUtilities.addVertex("Account", requiredA, extraA, graph.traversal().V());
        // add vertex B if it does not exist
        gt = GraphUtilities.addVertex("Product", requiredB, extraB, gt.V());
        gt = GraphUtilities.addVertex("Product", requiredB, extraB, gt.V());
        gt = GraphUtilities.addVertex("Account", requiredA, extraA, gt.V());
        gt.iterate();


        // Lets verify that only one of each edge was inserted
        List<Map<Object, Object>> vetexes = graph.traversal().V().valueMap().toList();
        assert (vetexes.size() == 2);
        for (Map<Object, Object> map : vetexes) {
            System.out.println("Vertex -------------------------");
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                System.out.println(entry.getKey() + " -> " + entry.getValue());
            }
        }


        //  get vertex A as 'source'
        gt = GraphUtilities.getVertex("Account", requiredA, graph.traversal().V()).as("source");
        //  get vertex B as 'target'
        gt = GraphUtilities.getVertex("Product", requiredB, gt.V()).as("target");
        // add edge between 'source' and 'target' if it does not exist

        gt = GraphUtilities.addEdge("source", "target", "EDGE", requiredE, extraE, gt);
        // try to add the edge again ... we should only get one
        gt = GraphUtilities.addEdge("source", "target", "EDGE", requiredE, extraE, gt);

        gt.iterate();


        List<Map<Object, Object>> edges = graph.traversal().E().valueMap().toList();

        System.out.println("Edges = " + edges.size());
        assert(edges.size() == 1);
        for (Map<Object, Object> map : edges) {
            System.out.println("edges -------------------------");
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                System.out.println(entry.getKey() + " -> " + entry.getValue());
            }
        }

        graph.close();
    }




}
