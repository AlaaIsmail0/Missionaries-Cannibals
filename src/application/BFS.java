package application;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class BFS {
	public static Node bfs(State initialState) {
		Queue<Node> queue = new LinkedList<>(); // to store nodes
		Set<State> visitedStates = new HashSet<>(); // keep track of visited states

		Node initialNode = new Node(initialState, null);
		queue.add(initialNode);
		visitedStates.add(initialState); // marks the initial state as visited

		while (!queue.isEmpty()) {
			Node currentNode = queue.poll();
			State currentState = currentNode.getState();

			if (currentState.isGoalState()) { // If the goal state is reached -> return the current node

				return currentNode;
			}

			for (State successorState : currentState.generateSuccessors()) {
				if (successorState.isValid() && visitedStates.add(successorState)) {
					Node successorNode = new Node(successorState, currentNode);
					queue.add(successorNode);
				}
			}
		}

		return null;
	}
}