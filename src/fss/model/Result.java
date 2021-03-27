package fss.model;

public class Result {
    private int goalHome;
    private int goalAway;

    public Result(int goalHome, int goalAway) {
        this.goalHome = goalHome;
        this.goalAway = goalAway;
    }

    public int getGoalHome() { return goalHome; }
    public int getGoalAway() { return goalAway; }
    public boolean isDraw() { return goalHome == goalAway; }
    public boolean isWin() { return  goalHome > goalAway; }
    public boolean isLose() { return goalHome < goalAway; }
    public String getString() {
        return Integer.toString(goalHome) + " - " + Integer.toString(goalAway);
    }
}
