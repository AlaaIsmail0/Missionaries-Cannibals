package application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.Queue;

public class Main extends Application {
	private State initialState;
	private TextArea resultTextArea;
	private Stage imageStage;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		Button solveButton = new Button("Solve");
		solveButton.setOnAction(e -> solve());
		solveButton.setStyle("-fx-font-size: 14; -fx-padding: 10 20;");
		Button showImageButton = new Button("Show Solution Steps Image");
		showImageButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				showImage();
			}
		});
		showImageButton.setStyle("-fx-font-size: 14; -fx-padding: 10 20;");
		resultTextArea = new TextArea();
		resultTextArea.setEditable(false);
		resultTextArea.setWrapText(true);
		resultTextArea.setPrefRowCount(70);
		resultTextArea.setPrefColumnCount(150);
		resultTextArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 15;");

		VBox root = new VBox(10);
		root.setStyle("-fx-padding: 20; -fx-alignment: center;");
		root.getChildren().addAll(solveButton, showImageButton, resultTextArea);
		Scene scene = new Scene(root, 1000, 750);
		primaryStage.setTitle("Missionaries and Cannibals");
		primaryStage.setScene(scene);
		primaryStage.show();

		imageStage = new Stage();
		imageStage.setTitle("Image Viewer");
		imageStage.setResizable(false);
	}

	private void showImage() {
		Image image = new Image(getClass().getResourceAsStream("/application/photos/p1.gif"));
		ImageView imageView = new ImageView(image);
		VBox imageBox = new VBox(imageView);
		imageBox.setAlignment(Pos.CENTER);
		Scene imageScene = new Scene(imageBox, 800, 600);
		imageStage.setScene(imageScene);
		imageStage.show();
	}

	private void solve() {
		// Clear previous data
		resultTextArea.clear();
		// Define the initial state
		initialState = new State(3, 3, 0, 0, true);

		// Solve using BFS
		long startTimeBFS = System.nanoTime();
		Node solutionBFS = bfs(initialState);
		long endTimeBFS = System.nanoTime();

		// Solve using DFS
		long startTimeDFS = System.nanoTime();
		Node solutionDFS = dfs(initialState);
		long endTimeDFS = System.nanoTime();

		// Update results in TextArea
		updateGUI(startTimeBFS, endTimeBFS, solutionBFS, startTimeDFS, endTimeDFS, solutionDFS);
	}

	private void updateGUI(long startTimeBFS, long endTimeBFS, Node solutionBFS, long startTimeDFS, long endTimeDFS,
			Node solutionDFS) {

		resultTextArea.appendText("Results for BFS:\n");
		resultTextArea.appendText("Algorithm: BFS\n");
		if (solutionBFS != null) {
			resultTextArea.appendText("Execution Time: " + (endTimeBFS - startTimeBFS) / 1_000 + " microseconds\n");
			resultTextArea.appendText("Solution Steps:\n");
			appendSolutionSteps(solutionBFS);
		} else {
			resultTextArea.appendText("No solution found.\n");
		}

		resultTextArea.appendText("-_-_-_-_-_-_-_-_-_-_-_-_\n");

		resultTextArea.appendText("Results for DFS:\n");
		resultTextArea.appendText("Algorithm: DFS\n");
		if (solutionDFS != null) {
			resultTextArea.appendText("Execution Time: " + (endTimeDFS - startTimeDFS) / 1_000 + " microseconds\n");
			resultTextArea.appendText("Solution Steps:\n");
			appendSolutionSteps(solutionDFS);
		} else {
			resultTextArea.appendText("No solution found.\n");
		}
	}

	private void appendSolutionSteps(Node solution) {
		if (solution != null) {
			updateSolutionPath(solution);
		} else {
			resultTextArea.appendText("No solution found.\n");
		}
	}

	private void updateSolutionPath(Node node) {
		StringBuilder formattedSteps = new StringBuilder();
		updateSolutionPathHelper(node, formattedSteps);
		resultTextArea.appendText(formattedSteps.toString());
	}

	private void updateSolutionPathHelper(Node node, StringBuilder formattedSteps) {
		if (node == null) {
			return;
		}

		updateSolutionPathHelper(node.getParent(), formattedSteps);
		int stepNumber = (int) formattedSteps.chars().filter(c -> c == '\n').count() + 1;
		formattedSteps.append(stepNumber).append(". ").append(node.getState().formatState()).append("\n");
	}

	private Node bfs(State initialState) {
		// Create a queue for BFS
		Queue<Node> queue = new LinkedList<>();
		// Enqueue the initial state
		Node initialNode = new Node(initialState, null);
		queue.add(initialNode);

		while (!queue.isEmpty()) {
			Node currentNode = queue.poll();
			State currentState = currentNode.getState();

			if (currentState.isGoalState()) {
				return currentNode;
			}

			for (State successorState : currentState.generateSuccessors()) {
				// Ensure the successor state is valid and not already visited
				if (successorState.isValid() && !isVisited(successorState, currentNode)) {
					Node successorNode = new Node(successorState, currentNode);
					queue.add(successorNode);
				}
			}
		}

		return null; // No solution found
	}

	private Node dfs(State initialState) {
		return dfsHelper(initialState, null);
	}

	private Node dfsHelper(State currentState, Node parent) {

		if (currentState.isGoalState()) {
			return new Node(currentState, parent);
		}

		for (State successorState : currentState.generateSuccessors()) {
			// Ensure the successor state is valid and not already visited
			if (successorState.isValid() && !isVisited(successorState, parent)) {
				Node resultNode = dfsHelper(successorState, new Node(currentState, parent));
				if (resultNode != null) {
					return resultNode;
				}
			}
		}

		return null; // No solution found
	}

	private boolean isVisited(State state, Node node) {
		// Check if the state has already been visited in the path leading to the
		// current node
		while (node != null) {
			if (node.getState().equals(state)) {
				return true;
			}
			node = node.getParent();
		}
		return false;
	}
}
