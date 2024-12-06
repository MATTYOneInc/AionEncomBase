/**
 * This file is part of the Aion Reconstruction Project Server.
 *
 * The Aion Reconstruction Project Server is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * The Aion Reconstruction Project Server is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with the Aion Reconstruction Project Server. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 * @AionReconstructionProjectTeam
 */
package com.aionemu.gameserver.world.geo.nav;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.geoEngine.scene.NavGeometry;
import com.aionemu.gameserver.world.geo.nav.NavService.NavPathway;

/**
 * Implements a pathfinding algorithm similar to A* to traverse through {@link NavGeometry}.
 * 
 * @author Yon (Aion Reconstruction Project)
 */
class NavHelper {
	
	/**
	 * The {@link Logger} for this class. This is only ever written to if
	 * {@link #retrace(NavHeapNode)} gives up early (for debugging purposes).
	 */
	private final Logger LOG = LoggerFactory.getLogger(NavHelper.class);
	
	/**
	 * A value used when attempting to pathfind to a target that is not on the Nav Mesh.
	 * <p>
	 * If the estimated distance of the target is within this value, then the path will be
	 * considered complete; from there, a straight line path will be used to finish traversal.
	 * <p>
	 * If this value is too large, then entities that are traversing to targets that are not on
	 * the Nav Mesh may clip through walls or other geometry in strange ways.
	 */
	public final static float ARBITRARY_SMALL_VALUE = 5; //TODO: Make config
	
	/**
	 * A value used when retracing or opening the list of nodes to create a pathway corridor.
	 * <p>
	 * This value limits how many line segments can be stored in a single list. If this
	 * value is exceeded, pathfinding attempts are considered to have failed.
	 * <p>
	 * If this value is too small, pathfinding will fail over long path distances. If this
	 * value is too large, then the JVM can consume all of its memory trying to store the corridor.
	 * <p>
	 * This value is mainly to prevent an issue with one of the pathfinding algorithm's assumptions
	 * that corrupts the data structure and forces an infinite loop, but is also used as a limit
	 * of operations while pathing.
	 */
	public final static int ARBITRARY_LARGE_VALUE = 800; //TODO: Make config
	
	/**
	 * A percentage of pathCost to add onto the basic path cost calculation
	 * if the next node is moving away from the target node.
	 * <p>
	 * A node is considered to be in a direction moving away from the target if a vector towards
	 * the target from the vertex opposite the edge the path passes through does not pass through
	 * said edge. See {@link NavGeometry#isTowardsEdge(byte, float[])}.
	 */
	public final static float PATH_WEIGHT = 0.2F; //TODO: Make config
	
	/**
	 * A multiplier for {@link NavHeapNode#targetDist}. When the target distance is estimated,
	 * it will be multiplied by this value. This is to give nodes that are closer to the target
	 * a higher priority than nodes that are further away.
	 */
	public final static float TARGET_WEIGHT = 20; //TODO: Make config
	
	/**
	 * A node designed to be stored in an array treated like a heap data structure.
	 * The heap structure should place nodes based on the {@link #compareTo(NavHeapNode)}
	 * method, with the lowest values at the top of the heap.
	 * 
	 * @author Yon (Aion Reconstruction Project)
	 */
	private class NavHeapNode implements Comparable<NavHeapNode> {
		
		/**
		 * If true, this node has been explored and opened by the pathfinding algorithm.
		 */
		boolean open = false;
		
		/**
		 * The {@link NavGeometry} this node represents.
		 */
		NavGeometry tile;
		
		/**
		 * The {@link NavHeapNode node} with the shortest {@link #pathCost} that connects to this node.
		 */
		NavHeapNode parent;
		
		/**
		 * A lookup value for the heap this node is stored within.
		 */
		int heapIndex;
		
		/**
		 * A value used to compare this node to other nodes. After a summation with another value,
		 * the summed value determines the priority of this node within the heap. 
		 */
		float pathCost, targetDist;
		
		/**
		 * Basic constructor. Only used by the initial starting node of the path.
		 * 
		 * @param node -- The {@link NavGeometry} this node represents.
		 */
		NavHeapNode(NavGeometry node) {
			this.tile = node;
			if (tile == endTile) {
				targetDist = 0;
			} else {
				targetDist = node.getPriority(x2, y2, z2)*TARGET_WEIGHT;
			}
		}
		
		/**
		 * Constructor. This node is created with the given parent node (which cannot be null),
		 * and estimates its {@link #pathCost} based on said parent node. The {@link #targetDist}
		 * is also estimated.
		 * 
		 * @param node -- The {@link NavGeometry} this node represents.
		 * @param parent -- The {@link NavHeapNode} that opened onto this node.
		 * @param useWeight -- If true, the {@link #pathCost} will have an extra percentage added onto it
		 * (see {@link NavHelper#PATH_WEIGHT PATH WEIGHT}).
		 */
		NavHeapNode(NavGeometry node, NavHeapNode parent, boolean useWeight) {
			this(node);
			this.parent = parent;
			float basePriority = parent.pathCost + parent.tile.getInRad();
			if (useWeight) {
				pathCost = basePriority + basePriority*PATH_WEIGHT;
			} else {
				pathCost = basePriority;
			}
		}
		
		/**
		 * Considers the passed in node and accepts it as the new {@link #parent} if
		 * it has a lower {@link #pathCost}. If the new parent is accepted, then the
		 * path cost of this node is updated.
		 * 
		 * @param newParent -- The node to consider as a new parent.
		 * @param useWeight -- If {@link NavHelper#PATH_WEIGHT} should be applied to
		 * the new path cost if the new parent is accepted.
		 */
		void checkAndUpdateParent(NavHeapNode newParent, boolean useWeight) {
			assert newParent != null;
			if (parent == null) return;
			if (newParent.parent == this) return;
			if (parent.pathCost > newParent.pathCost) {
				parent = newParent;
				if (useWeight) {
					pathCost = parent.pathCost + parent.tile.getInRad();
					pathCost += pathCost*PATH_WEIGHT;
					onUpdateNode(this);
				} else {
					pathCost = parent.pathCost + parent.tile.getInRad();
					onUpdateNode(this);
				}
			}
		}
		
		/**
		 * Sums {@link #pathCost} and {@link #targetDist} and returns the result.
		 * This value represents the overall priority of this node. Lower values
		 * are of a higher priority, and the position on the heap should take this
		 * value into consideration above all others. See {@link #compareTo(NavHeapNode)}.
		 * 
		 * @return the summation of {@link #pathCost} and {@link #targetDist}.
		 */
		float getPriority() {
			return pathCost + targetDist;
		}
		
		/**
		 * Opens this node by examining what the edges of {@link #tile} connect to. If the
		 * edges do not connect to anything, they are skipped. If the edge connection exists,
		 * and has yet to be explored, a new node is created to represent it and added to the heap.
		 * If the edge connection exists, and has already been explored, this node will attempt to
		 * become the new parent of the existing node by calling {@link #checkAndUpdateParent(NavHeapNode, boolean)}.
		 * <p>
		 * This operation sets {@link #open} to true.
		 */
		void open() {
			if (open) return;
			//Check connections to see if they are part of the heap
			float[] vec = {x2, y2};
			
			//This commented code made things worse.
//			if (parent != null) {
//				vec = new float[] {x2 - parent.tile.incenter[0], y2 - parent.tile.incenter[1]};
//			} else {
//				vec = new float[] {x2 - x1, y2 - y1};
//			}
			if (tile.getEdge1() != null) if (!contains(tile.getEdge1())) {
				//If they aren't, then create and add them
				NavHeapNode newNode = new NavHeapNode(tile.getEdge1(), this, !tile.isTowardsEdge((byte) 1, vec));
				add(newNode);
			} else {
				//If they are, run checkAndUpdateParent
				NavHeapNode child = getNode(tile.getEdge1());
				if (child != parent) child.checkAndUpdateParent(this, !tile.isTowardsEdge((byte) 1, vec));
			}
			
			if (tile.getEdge2() != null) if (!contains(tile.getEdge2())) {
				NavHeapNode newNode = new NavHeapNode(tile.getEdge2(), this, !tile.isTowardsEdge((byte) 2, vec));
				add(newNode);
			} else {
				NavHeapNode child = getNode(tile.getEdge2());
				if (child != parent) child.checkAndUpdateParent(this, !tile.isTowardsEdge((byte) 2, vec));
			}
			
			if (tile.getEdge3() != null) if (!contains(tile.getEdge3())) {
				NavHeapNode newNode = new NavHeapNode(tile.getEdge3(), this, !tile.isTowardsEdge((byte) 3, vec));
				add(newNode);
			} else {
				NavHeapNode child = getNode(tile.getEdge3());
				if (child != parent) child.checkAndUpdateParent(this, !tile.isTowardsEdge((byte) 3, vec));
			}
			open = true;
		}
		
		/**
		 * Considers the priority of this node compared to the other. If this node has a higher
		 * priority (lower value from {@link #getPriority()}) then -1 is returned. If this node
		 * has a lower priority, then 1 is returned. If the overall priority is equal to the
		 * given node, {@link #targetDist} is used as a tie-breaker (lower values are higher priority).
		 * If both the overall priority and the target distance are equal, 0 is returned. 
		 * <p>
		 * Note: this class has a natural ordering that is inconsistent with equals.
		 */
		@Override
		public int compareTo(NavHeapNode other) {
			float pThis = getPriority();
			float pOther = other.getPriority();
			if (pThis > pOther) return 1;
			if (pThis < pOther) return -1;
			if (targetDist > other.targetDist) return 1;
			if (targetDist < other.targetDist) return -1;
			//If priority is equal, and targetDist is equal, then pathCost is also equal.
//			if (pathCost > other.pathCost) return 1;
//			if (pathCost < other.pathCost) return -1;
			return 0;
		}
		
	}
	
	/**
	 * Starting coordinate component
	 */
	final float x1, y1, z1;
	
	/**
	 * Target coordinate component
	 */
	final float x2, y2, z2;
	
	/**
	 * The target {@link NavGeometry} to find a path to.
	 */
	NavGeometry endTile;
	
	/**
	 * A list of {@link NavGeometry} that has been explored and stored within a {@link NavHeapNode}.
	 * <p>
	 * This list is used to determine if a Nav Mesh node has already been explored after it's been
	 * removed from the heap.
	 */
	private HashMap<NavGeometry, NavHeapNode> list;
	
	/**
	 * A simple array that is treated as the underlying structure of a heap. The {@link NavHelper} class maintains
	 * this heap, and enforces the ordering within.
	 * <p>
	 * This array is expanded as needed. Due to the size being larger than the contents, {@link #currentHeapCount}
	 * is used to track how many indices of this array are relevant.
	 */
	private NavHeapNode[] heap;
	
	/**
	 * The current number of items being stored on the {@link #heap}.
	 */
	private int currentHeapCount = 0;
	
	/**
	 * Creates a new {@link NavHelper} that is ready to {@link #createPathway() construct a path}
	 * from the given starting point to the given end point.
	 * <p>
	 * Callers should run {@link #destroy()} when they are done with this object.
	 * 
	 * @param startTile -- The {@link NavGeometry} this should create a path from. Cannot be null.
	 * @param endTile -- The {@link NavGeometry} this should pathfind to. Can be null.
	 * @param x1 -- The x-component of the starting position.
	 * @param y1 -- The y-component of the starting position.
	 * @param z1 -- The z-component of the starting position.
	 * @param x2 -- The x-component of the end position.
	 * @param y2 -- The y-component of the end position.
	 * @param z2 -- The z-component of the end position.
	 */
	NavHelper(NavGeometry startTile, NavGeometry endTile, float x1, float y1, float z1, float x2, float y2, float z2) {
		assert startTile != null;
		heap = new NavHeapNode[100];
		list = new HashMap<NavGeometry, NavHeapNode>();
		this.endTile = endTile;
		this.x1 = x1; this.y1 = y1; this.z1 = z1;
		this.x2 = x2; this.y2 = y2; this.z2 = z2;
		init(startTile);
	}
	
	/**
	 * Creates the first node to be added to the {@link #heap} from the starting {@link NavGeometry},
	 * and adds it to the heap.
	 * 
	 * @param tile -- The starting {@link NavGeometry} for the path to be generated.
	 */
	private void init(NavGeometry tile) {
		NavHeapNode startNode = new NavHeapNode(tile);
		add(startNode);
	}
	
	/**
	 * Creates a new thread that iterates through {@link #list} and nulls all
	 * {@link NavHeapNode#parent} references to prevent memory leaks. The list is then
	 * {@link HashMap#clear() cleared}.
	 * <p>
	 * The thread's name is set to "NavHelper GC".
	 */
	public void destroy() {
		Thread gc = new Thread() {
			public void run() {
				for (NavGeometry key : list.keySet()) {
					list.get(key).parent = null;
				}
				list.clear();
			}
		};
		gc.setName("NavHelper GC");
		gc.start();
	}
	
	/**
	 * Creates a list of {@link NavHeapNode NavHeapNodes} that connect the starting
	 * node to the ending node or destination. The list is passed to {@link #retrace(NavHeapNode)},
	 * returning the result.
	 * <p>
	 * If this method considers more than an {@link #ARBITRARY_LARGE_VALUE} number of nodes, it gives up,
	 * returning an empty pathway.
	 * 
	 * @return A {@link NavPathway} that represents a corridor to path through.
	 */
	public NavPathway[] createPathway() {
		boolean finished = false;
		if (endTile == null) {
			NavHeapNode current;
			short opCount = 0;
			do {
				current = removeFirst();
				if (current.targetDist < ARBITRARY_SMALL_VALUE*TARGET_WEIGHT) {
					finished = true;
					break;
				}
				current.open();
				if (opCount++ > ARBITRARY_LARGE_VALUE) break;
			} while (currentHeapCount > 0);
			if (finished) return retrace(current);
		} else {
			NavHeapNode current;
			short opCount = 0;
			do {
				current = removeFirst();
				if (current.tile == endTile) {
					finished = true;
					break;
				}
				current.open();
				if (opCount++ > ARBITRARY_LARGE_VALUE) break;
			} while (currentHeapCount > 0);
			if (finished) return retrace(current);
		}
		return new NavPathway[0];
	}
	
	/**
	 * Attempts to backtrack through the given {@link NavHeapNode node's} parent references until
	 * the starting point is discovered. If more than an {@link #ARBITRARY_LARGE_VALUE} number of nodes
	 * is considered, this method will give up and return an empty pathway. 
	 * 
	 * @param node -- The {@link NavHeapNode} representing the final {@link NavGeometry} on the path.
	 * @return The {@link NavPathway} that represents the corridor of the found path.
	 */
	private NavPathway[] retrace(NavHeapNode node) {
		ArrayList<NavPathway> ret = new ArrayList<NavPathway>();
		NavHeapNode child = node;
		NavHeapNode parent = node;
		while (parent.parent != null) {
			parent = parent.parent;
			assert parent.parent == null || parent.parent != child: "Parent of parent node is child node! Infinite Loop!";
			NavPathway port;
			if (parent.tile.getEdge1() == child.tile) {
				port = new NavPathway(parent.tile, (byte) 1);
			} else if (parent.tile.getEdge2() == child.tile) {
				port = new NavPathway(parent.tile, (byte) 2);
			} else {
				assert parent.tile.getEdge3() == child.tile;
				port = new NavPathway(parent.tile, (byte) 3);
			}
			ret.add(port);
			/*
			 * TODO: Figure out how the nodes are getting looped together.
			 * This method is looping until all memory is consumed by the ArrayList in some odd edge case.
			 * Could be related to pathCost.
			 */
			if (ret.size() > ARBITRARY_LARGE_VALUE) {
				LOG.error("Retracing path produced too many portals: (" + x1 + ", " + y1 + ", " + z1 + ") --> (" + x2 + ", " + y2 + ", " + z2 + ")");
				return new NavPathway[0];
			}
			child = parent;
		}
		return ret.toArray(new NavPathway[0]);
	}
	
	/**
	 * Adds the given {@link NavHeapNode} to the heap, and enforces a new heap order as needed.
	 * 
	 * @param node -- The {@link NavHeapNode} to be added to the heap.
	 */
	private void add(NavHeapNode node) {
		list.put(node.tile, node);
		node.heapIndex = currentHeapCount;
		heap[currentHeapCount++] = node;
		if (currentHeapCount == heap.length) {
			NavHeapNode[] tempHeap = new NavHeapNode[heap.length + 50];
			System.arraycopy(heap, 0, tempHeap, 0, currentHeapCount);
			heap = tempHeap;
		}
		sortUp(node);
	}
	
	/**
	 * Checks if this {@link NavHelper} has considered the given {@link NavGeometry} yet, and returns
	 * true if it has. False otherwise.
	 * 
	 * @param tile -- the {@link NavGeometry} to check for.
	 * @return True if this NavHelper has encountered the given {@link NavGeometry}, false otherwise.
	 */
	private boolean contains(NavGeometry tile) {
		return list.containsKey(tile);
	}
	
	/**
	 * Returns the {@link NavHeapNode} that represents the given {@link NavGeometry} as specified
	 * by the {@link HashMap#get(Object) get()} method for {@link #list}.
	 * 
	 * @param tile -- the {@link NavGeometry} that the retrieved {@link NavHeapNode} represents.
	 * @return The {@link NavHeapNode} representing the given {@link NavGeometry}.
	 */
	private NavHeapNode getNode(NavGeometry tile) {
		return list.get(tile);
	}
	
	/**
	 * Removes the highest priority {@link NavHeapNode} from the {@link #heap}, and returns it.
	 * Before returning, the heap is rearranged to maintain the correct order.
	 * 
	 * @return The highest priority {@link NavHeapNode} stored within the {@link #heap}.
	 */
	private NavHeapNode removeFirst() {
		if (currentHeapCount == 0) return null;
		NavHeapNode ret = heap[0];
		currentHeapCount--;
		heap[0] = heap[currentHeapCount];
		heap[0].heapIndex = 0;
		sortDown(heap[0]);
		return ret;
	}
	
	/**
	 * Verifies that the given {@link NavHeapNode} is in the correct position on the {@link #heap}
	 * after its values have been adjusted.
	 * 
	 * @param node -- the {@link NavHeapNode} to verify the position of.
	 */
	private void onUpdateNode(NavHeapNode node) {
		if (node.open) return;
		sortUp(node);
		sortDown(node); //Unneeded for this application
	}
	
	/**
	 * Enforces the correct position of the given {@link NavHeapNode} within the heap.
	 * This method only verifies that the node should not be further down the {@link #heap}.
	 * 
	 * @param node -- The {@link NavHeapNode} to validate the position of.
	 */
	private void sortDown(NavHeapNode node) {
		do {
			//Child Index
			int ciLeft = node.heapIndex * 2 + 1;
			int ciRight = node.heapIndex * 2 + 2;
			int swapIndex;
			if (ciLeft < currentHeapCount) {
				swapIndex = ciLeft;
				if (ciRight < currentHeapCount) {
					if (heap[ciRight].compareTo(heap[ciLeft]) < 0) {
						swapIndex = ciRight;
					}
				}
				if (node.compareTo(heap[swapIndex]) > 0) {
					swap(node, heap[swapIndex]);
				} else {
					break;
				}
			} else {
				break;
			}
		} while (true);
	}
	
	/**
	 * Enforces the correct position of the given {@link NavHeapNode} within the heap.
	 * This method only verifies that the node should not be further up the {@link #heap}.
	 * 
	 * @param node -- The {@link NavHeapNode} to validate the position of.
	 */
	private void sortUp(NavHeapNode node) {
		int pi; //Parent Index
		do {
			pi = (node.heapIndex-1)/2;
			if (heap[pi].compareTo(node) > 0) {
				swap(heap[pi], node);
			} else {
				break;
			}
		} while (true);
	}
	
	/**
	 * Swaps the position of the two given {@link NavHeapNode nodes} within the {@link #heap}.
	 * 
	 * @param node1
	 * @param node2
	 */
	private void swap(NavHeapNode node1, NavHeapNode node2) {
		heap[node1.heapIndex] = node2;
		heap[node2.heapIndex] = node1;
		int heapIndex1 = node1.heapIndex;
		node1.heapIndex = node2.heapIndex;
		node2.heapIndex = heapIndex1;
	}
	
}
