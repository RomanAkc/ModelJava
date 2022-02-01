package fss.model;

public class Result {
    private final int goalHome;
    private final int goalAway;

    public Result(int goalHome, int goalAway) {
        this.goalHome = goalHome;
        this.goalAway = goalAway;
    }

    public int getGoalHome() { return goalHome; }
    public int getGoalAway() { return goalAway; }
    public boolean isDraw() { return goalHome == goalAway; }
    public boolean isWin() { return  goalHome > goalAway; }
    public boolean isLose() { return goalHome < goalAway; }
    public Result reverse() { return new Result(goalAway, goalHome); }
    @Override
    public String toString() {
        String res = Integer.toString(goalHome);
        res += " - ";
        res += goalAway;
        return  res;
    }
}
