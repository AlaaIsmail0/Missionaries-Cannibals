package application;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class DFS {
	public static Node dfs(State initialState) {
		Set<State> visitedStates = new HashSet<>();
		Stack<Node> stack = new Stack<>(); // to store nodes

		Node initialNode = new Node(initialState, null);
		stack.push(initialNode);
		visitedStates.add(initialState);

		while (!stack.isEmpty()) {
			Node currentNode = stack.pop(); // Pop a node from the top of the stack
			State currentState = currentNode.getState();

			if (currentState.isGoalState()) {
				return currentNode;
			}

			for (State successorState : currentState.generateSuccessors()) {
				if (successorState.isValid() && visitedStates.add(successorState)) {
					Node successorNode = new Node(successorState, currentNode);
					stack.push(successorNode);
				}
			}
		}

		return null;
	}
}
