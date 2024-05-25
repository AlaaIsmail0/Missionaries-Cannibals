package application;

import java.util.ArrayList;
import java.util.List;

public class State {
	private int missionariesOnLeft;
	private int cannibalsOnLeft;
	private int missionariesOnRight;
	private int cannibalsOnRight;
	private boolean boatOnLeft;

	public State(int missionariesOnLeft, int cannibalsOnLeft, int missionariesOnRight, int cannibalsOnRight,
			boolean boatOnLeft) {
		this.missionariesOnLeft = missionariesOnLeft;
		this.cannibalsOnLeft = cannibalsOnLeft;
		this.missionariesOnRight = missionariesOnRight;
		this.cannibalsOnRight = cannibalsOnRight;
		this.boatOnLeft = boatOnLeft;
	}

	public int getMissionariesOnLeft() {
		return missionariesOnLeft;
	}

	public int getCannibalsOnLeft() {
		return cannibalsOnLeft;
	}

	public int getMissionariesOnRight() {
		return missionariesOnRight;
	}

	public int getCannibalsOnRight() {
		return cannibalsOnRight;
	}

	public boolean isBoatOnLeft() {
		return boatOnLeft;
	}

	public void setMissionariesOnLeft(int missionariesOnLeft) {
		this.missionariesOnLeft = missionariesOnLeft;
	}

	public void setCannibalsOnLeft(int cannibalsOnLeft) {
		this.cannibalsOnLeft = cannibalsOnLeft;
	}

	public void setMissionariesOnRight(int missionariesOnRight) {
		this.missionariesOnRight = missionariesOnRight;
	}

	public void setCannibalsOnRight(int cannibalsOnRight) {
		this.cannibalsOnRight = cannibalsOnRight;
	}

	public void setBoatOnLeft(boolean boatOnLeft) {
		this.boatOnLeft = boatOnLeft;
	}

	public boolean isGoalState() {
		return missionariesOnLeft == 0 && cannibalsOnLeft == 0 && missionariesOnRight == 3 && cannibalsOnRight == 3
				&& !boatOnLeft;
	}

	public State[] generateSuccessors() {
		List<State> successors = new ArrayList<>();

		if (boatOnLeft) { // moving 1 or 2 missionaries to the right
			for (int missionaries = 1; missionaries <= 2; missionaries++) {
				if (missionariesOnLeft >= missionaries) {
					State newState = new State(missionariesOnLeft - missionaries, cannibalsOnLeft,
							missionariesOnRight + missionaries, cannibalsOnRight, false);
					if (newState.isValid()) {
						successors.add(newState);
					}
				}
			}

			for (int cannibals = 1; cannibals <= 2; cannibals++) { // moving 1 or 2 cannibals to the right

				if (cannibalsOnLeft >= cannibals) {
					State newState = new State(missionariesOnLeft, cannibalsOnLeft - cannibals, missionariesOnRight,
							cannibalsOnRight + cannibals, false);
					if (newState.isValid()) {
						successors.add(newState);
					}
				}
			}

			if (missionariesOnLeft >= 1 && cannibalsOnLeft >= 1) { // moving 1 missionary and 1 cannibal to the right

				State newState = new State(missionariesOnLeft - 1, cannibalsOnLeft - 1, missionariesOnRight + 1,
						cannibalsOnRight + 1, false);
				if (newState.isValid()) {
					successors.add(newState);
				}
			}
		} else { // moving 1 or 2 missionaries to the left

			for (int missionaries = 1; missionaries <= 2; missionaries++) {
				if (missionariesOnRight >= missionaries) {
					State newState = new State(missionariesOnLeft + missionaries, cannibalsOnLeft,
							missionariesOnRight - missionaries, cannibalsOnRight, true);
					if (newState.isValid()) {
						successors.add(newState);
					}
				}
			}

			for (int cannibals = 1; cannibals <= 2; cannibals++) { // moving 1 or 2 cannibals to the left

				if (cannibalsOnRight >= cannibals) {
					State newState = new State(missionariesOnLeft, cannibalsOnLeft + cannibals, missionariesOnRight,
							cannibalsOnRight - cannibals, true);
					if (newState.isValid()) {
						successors.add(newState);
					}
				}
			}

			if (missionariesOnRight >= 1 && cannibalsOnRight >= 1) { // moving 1 missionary and 1 cannibal to the left

				State newState = new State(missionariesOnLeft + 1, cannibalsOnLeft + 1, missionariesOnRight - 1,
						cannibalsOnRight - 1, true);
				if (newState.isValid()) {
					successors.add(newState);
				}
			}
		}

		return successors.toArray(new State[0]);
	}

	public boolean isValid() {
		return missionariesOnLeft >= 0 && cannibalsOnLeft >= 0 && missionariesOnRight >= 0 && cannibalsOnRight >= 0
				&& (missionariesOnLeft == 0 || missionariesOnLeft >= cannibalsOnLeft)
				&& (missionariesOnRight == 0 || missionariesOnRight >= cannibalsOnRight);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		State state = (State) obj;

		return missionariesOnLeft == state.missionariesOnLeft && cannibalsOnLeft == state.cannibalsOnLeft
				&& missionariesOnRight == state.missionariesOnRight && cannibalsOnRight == state.cannibalsOnRight
				&& boatOnLeft == state.boatOnLeft;
	}

	public String formatState() {
		return String.format(
				"Left Bank: Missionaries=%d, Cannibals=%d || Boat: %s || Right Bank: Missionaries=%d, Cannibals=%d",
				getMissionariesOnLeft(), getCannibalsOnLeft(), isBoatOnLeft() ? "Left" : "Right",
				getMissionariesOnRight(), getCannibalsOnRight());
	}
}