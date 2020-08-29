package sxa190016;

import idsa.Graph.Vertex;
import idsa.Graph;
import idsa.Graph.Edge;
import idsa.Graph.GraphAlgorithm;
import idsa.Graph.Factory;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * @author sxa190016
 * @author axs190140
 * @author epm180002
 * @author nxs190026
 * @version 1.0 PERT: Long project 2 Implement PERT algorithm on graph g.
 */
public class PERT extends GraphAlgorithm<PERT.PERTVertex> {
	public static class PERTVertex implements Factory {

		public PERTVertex(Vertex u) {
		}

		public PERTVertex make(Vertex u) {
			return new PERTVertex(u);
		}
	}

	/**
	 * Hashmap variable for vertex,duration
	 */
	public static HashMap<Graph.Vertex, Integer> duration = new HashMap<Graph.Vertex, Integer>();
	/**
	 * Hashmap variable for vertex,earliest complete time
	 */
	public static HashMap<Graph.Vertex, Integer> EC = new HashMap<Graph.Vertex, Integer>();
	/**
	 * Hashmap variable for vertex,latest start time
	 */
	public static HashMap<Graph.Vertex, Integer> LS = new HashMap<Graph.Vertex, Integer>();
	/**
	 * Hashmap variable for vertex,latest complete time
	 */
	public static HashMap<Graph.Vertex, Integer> LC = new HashMap<Graph.Vertex, Integer>();
	/**
	 * Hashmap variable for vertex,LC-EC time
	 */
	public static HashMap<Graph.Vertex, Integer> Slack = new HashMap<Graph.Vertex, Integer>();
	/**
	 * Flag to check if EC LC LS Slack was calculated
	 */
	public static boolean calculated = false;
	public static HashMap<Graph.Vertex, Integer> Critical = new HashMap<Graph.Vertex, Integer>();
	/**
	 * Contains list of vertices
	 */
	public static LinkedList<Graph.Vertex> reversevertexlist = new LinkedList<Graph.Vertex>();
	/**
	 * Contains list of vertices with slack=0
	 */
	public static LinkedList<LinkedList<Vertex>> criticallist = new LinkedList<LinkedList<Vertex>>();
	/**
	 * variable for earliest complete time for entire graph
	 */
	public int maxECval = 0;
	/**
	 * variable for latest complete time for entire graph
	 */

	public int maxLCval = 0;

	public PERT(Graph g) {
		super(g, new PERTVertex(null));
	}

	/**
	 * Put duration of vertexes in a HashMap
	 * 
	 * @param Vertex   u
	 * @param duration d
	 */
	public void setDuration(Vertex u, int d) {
		reversevertexlist.add(u);
		duration.put(u, d);
	}

	public boolean pert() {
		return false;
	}

	/**
	 * calculates Earliest Completion time by calling calculate data function which
	 * creates all EC, LS and LC
	 * 
	 * @param Vertex who's EC is to be found u
	 * @return EC value
	 */
	public int ec(Vertex u) {
		if (!calculated) {
			calculatedata();
			calculated = true;
		}
		return EC.get(u);

	}

	/**
	 * This method is used to calculate EC, LC, LS and Slack Computes on
	 * reversevertexlist Updates EC, LC, LS list for all vertex
	 * 
	 */
	private void calculatedata() {
		/*
		 * Calculate EC Iterate through the vertex
		 */

		for (Vertex u : reversevertexlist) {
			/*
			 * find the adjacency list
			 */
			LinkedList<Graph.Vertex> topologicalList = getpredecessors(g, u);
			/*
			 * if vertex has to predecessors then the EC is duration of the vertex itself
			 */
			if (topologicalList.size() == 0) {
				EC.put(u, duration.get(u));
				LC.put(u, duration.get(u));
			} else {
				/*
				 * else find the max EC of the predecessors and duration of the vertex
				 */
				int maxEC = 0;
				if (u.outDegree() == 0) {
					LC.put(u, EC.get(u));
					LS.put(u, EC.get(u));
				}
				for (Graph.Vertex v : topologicalList) {
					if (maxEC <= EC.get(v)) {
						maxEC = EC.get(v);
					}
				}
				maxEC += duration.get(u);
				if (maxEC > maxECval) {
					maxECval = maxEC;
				}
				EC.put(u, maxEC);
				LC.put(u, maxEC);
			}
		}

		/*
		 * Calculate LS,LC Calculation for LS and LC will happen in the reverse order
		 */

		LinkedList<Vertex> linkedli = new LinkedList<Vertex>();
		linkedli = reverseLL(reversevertexlist);
		for (Vertex u : linkedli) {
			/*
			 * get successors
			 */
			LinkedList<Graph.Vertex> topologicalList = getsuccessors(g, u);
			if (topologicalList.size() == 0) {
				/*
				 * if no successors and predecessors then EC, LC is max EC, LC of the graph
				 */
				if (u.inDegree() == 0 && duration.get(u) == 0) {
					int EC_max = (Collections.max(EC.values()));
					int LC_max = (Collections.max(LC.values()));
					EC.put(u, EC_max);
					LC.put(u, LC_max);

				} else {
					LC.put(u, EC.get(u));
					LS.put(u, EC.get(u) - duration.get(u));
				}

			} else {
				int minLS = 100;
				for (Graph.Vertex v : topologicalList) {
					/*
					 * calculate LS and LC find minimum LS amongst sucessors
					 */

					if (minLS > LS.get(v)) {
						minLS = LS.get(v);
					}
				}
				LC.put(u, minLS);
				LS.put(u, LC.get(u) - duration.get(u));
			}
		}
		// Calculate slack
		for (Vertex u : g) {
			Slack.put(u, Math.abs(LC.get(u) - EC.get(u)));

		}
	}

	/**
	 * Returns reversed linked list
	 * 
	 * @param Linked List llist
	 * @return Reversed Linked List revLinkedList
	 */
	public static LinkedList<Vertex> reverseLL(LinkedList<Vertex> llist) {
		LinkedList<Vertex> revLinkedList = new LinkedList<Vertex>();
		for (int i = llist.size() - 1; i >= 0; i--) {
			/*
			 * Append the elements in reverse order
			 */
			revLinkedList.add(llist.get(i));
		}
		/*
		 * Return the reversed arraylist
		 */
		return revLinkedList;
	}

	/**
	 * returns the LC of a given vertex for given vertex
	 * 
	 * @param Vertex who's LC is to be found u
	 * @return Int Value LC for given vertex
	 */
	public int lc(Vertex u) {
		if (!calculated) {
			calculatedata();
			calculated = true;
		}
		return LC.get(u);
	}

	/**
	 * Returns the Slack of a given vertex by subtracting EC from LC for given
	 * vertex
	 * 
	 * @param Vertex who's slack is to be found u
	 * @return Int Value subtracting EC from LC for given vertex
	 */
	public int slack(Vertex u) {
		if (!calculated) {
			calculatedata();
			calculated = true;
		}
		return Slack.get(u);
	}

	/**
	 * Find the critical Path using DFS Iterate through the successors and call
	 * individual critical paths for those vertexes
	 * 
	 * @param null
	 * @return list of all critical paths
	 */
	public int criticalPath() {
		/*
		 * find all for(vertex u :g) if u is slack list of linked lists create new list
		 * while building the path
		 */
		if (!calculated) {
			calculatedata();
			calculated = true;
		}
		for (Vertex u : reversevertexlist) {
			if (u.inDegree() == 0) {
				LinkedList<Vertex> path = new LinkedList<Graph.Vertex>();
				findcriticalPaths(u, path);
			}
		}
		return 0;
	}

	public void findcriticalPaths(Vertex s, LinkedList<Vertex> path) {
		if (s.outDegree() == 0) {
			// check if the s is crticial
			if (critical(s)) {
				// if true add path to list
				path.add(s);
				criticallist.add(path);
			}

		}
		if (critical(s) && s.outDegree() != 0) {
			// add vertex to the path
			path.add(s);
			for (Vertex v : getsuccessors(g, s)) {
				if (critical(v)) {
					findcriticalPaths(v, path);
				}
			}
		}
	}

	/**
	 * Check if vertex is critical if slack =0
	 * 
	 * @param Vertex to be checked u
	 * @return Boolean true is vertex is critical else false
	 */
	public static boolean critical(Vertex u) {
		if (Slack.get(u) == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Find number of critical paths
	 * 
	 * @return number of critical paths
	 */
	public int numCritical() {
		return criticallist.size();
		// return 0;
	}

	// setDuration(u, duration[u.getIndex()]);
	public static PERT pert(Graph g, int[] duration) {
		return null;
	}

	public static void main(String[] args) throws Exception {
		// String graph = "9 5 2 3 1 3 4 1 4 7 1 5 6 1 6 8 1 0 2 3 5 5 5 5 5 0";
		String graph = "11 12   2 4 1   2 5 1   3 5 1   3 6 1   4 7 1   5 7 1   5 8 1   6 8 1   6 9 1   7 10 1   8 10 1   9 10 1      0 3 2 3 2 1 3 2 4 1 0";
		Scanner in;
		// If there is a command line argument, use it as file from which
		// input is read, otherwise use input from string.
		in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(graph);
		Graph g = Graph.readDirectedGraph(in);
		g.printGraph(false);
		PERT p = new PERT(g);
		for (Vertex u : g) {
			p.setDuration(u, in.nextInt());
		}

		System.out.println("  ");
		duration.forEach((key, value) -> System.out.println(key + ":" + value));
		// Run PERT algorithm. Returns null if g is not a DAG
		if (p.pert()) {
			System.out.println("Invalid graph: not a DAG");
		} else {
			System.out.println("Number of critical vertices: " + p.numCritical());
			System.out.println("u\tEC\tLC\tSlack\tCritical");
			for (Vertex u : g) {
				// remove graph methods g
				System.out.println(u + "\t" + p.ec(u) + "\t" + p.lc(u) + "\t" + p.slack(u) + "\t" + p.critical(u));
			}
			p.criticalPath();
			System.out.println(criticallist);
		}
	}

	/**
	 * Calculates the predecessors given a vertex
	 * 
	 * @param graph  g
	 * @param vertex u
	 * @return : list of predecessors for vertex
	 */
	private LinkedList<Graph.Vertex> getpredecessors(Graph g, Vertex u) {
		// TODO Auto-generated method stub\
		// per vertex finds the predecessors and their corresponding weights
		LinkedList<Graph.Vertex> predecessorlist = new LinkedList<Graph.Vertex>();
		for (Edge e : g.inEdges(u)) {
			// System.out.print( e.getName()+"From:" +" To:"+e.from+" "+e.to);
			predecessorlist.add(e.fromVertex());
		}
		return predecessorlist;

	}

	/**
	 * Calculates the successors given a vertex
	 * 
	 * @param graph  g
	 * @param vertex u
	 * @return list of successors for vertex
	 */
	private LinkedList<Graph.Vertex> getsuccessors(Graph g, Vertex u) {
		// per vertex finds the successors and their corresponding weights
		LinkedList<Graph.Vertex> successorlist = new LinkedList<Graph.Vertex>();
		for (Edge e : g.outEdges(u)) {
			successorlist.add(e.toVertex());
		}
		return successorlist;

	}

	public void getedgeslist(Graph g) {
		for (Vertex u : g) {
			for (Edge e : g.inEdges(u)) {
				System.out.print("In edges for " + u + "\n");
				System.out.print(e.getName() + "From:" + e.fromVertex() + " To:" + " " + e.toVertex() + "	Weight:"
						+ e.getWeight() + "\n");
			}
			for (Edge e : g.outEdges(u)) {
				System.out.print("Out edges for " + u + "\n");
				System.out.print(e.getName() + "From:" + e.fromVertex() + " To:" + " " + e.toVertex() + "	Weight:"
						+ e.getWeight() + "\n");
			}
			System.out.println();
		}
	}
}
