package net.starkadder.janusgraph;

import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;


public class JanusGraphLoader {

    

    // sample loader --- not tested!

    public static void main(String[] args) throws Exception {
        Map<String, Object> empty = new HashMap();
        Map<String, Object> source = new HashMap();
        Map<String, Object> target = new HashMap();
        Map<String, Object> edge = new HashMap();


        assert (args.length == 4);
        String host = args[0];
        Integer port = Integer.parseInt(args[1]);
        String keyspace = args[2];
        String file = args[3];

        // connect
        GraphTraversalSource gts = traversal().withRemote(DriverRemoteConnection.using(host, port, keyspace));


        // file has simple layout of a source vertex and a target vertex, space delimited
        String line;
        BufferedReader reader = new BufferedReader((new FileReader(file)));
        while ((line = reader.readLine()) != null) {
            // Line of data has simple layout of a source vertex and a target vertex
            String[] tokens = line.split(" ", 2);

            source.put("ID", tokens[0]);
            target.put("ITEM_ID", tokens[2]);
            GraphTraversal<Vertex, Vertex> gt = GraphUtilities.addVertex("ACCOUNT", source, empty, gts.V());
            gt = GraphUtilities.addVertex("ITEM", target, empty, gt.V());
            gt = GraphUtilities.getVertex("ACCOUNT", source,  gt.V()).as("s");
            gt = GraphUtilities.getVertex("ITEM", source,  gt.V()).as("t");
            gt = GraphUtilities.addEdge("s","t","EDGE",empty,empty,gt);
            gt.iterate();

        }
        reader.close();
        gts.close();


    }

}