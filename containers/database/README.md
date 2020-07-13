# Janus server docker container and gremlin console


`docker exec -ti gremlin bin/gremlin.sh`

```
bash-4.4$ bin/gremlin.sh 

         \,,,/
         (o o)
-----oOOo-(3)-oOOo-----
plugin activated: tinkerpop.server
plugin activated: tinkerpop.utilities
plugin activated: tinkerpop.tinkergraph
gremlin> 


```

- to list all vertexes: `:submit g.V()`
- to list all edges `:submit g.E()`
- to delete all vertexes `:submit g.V().drop().iterate()`
- to get a count of vertexes ` :submit g.V().count();`
- to get the attribute of a vertex, in this case the 'id' of vertex 258,  `:submit g.V(258).values('id')`
