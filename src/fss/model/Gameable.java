package fss.model;

public interface Gameable {
    SimpleTeam getTeamHome();
    SimpleTeam getTeamAway();

    void calc();

    Result getResultMeet();

    boolean isWinnerHomeTeam();
    boolean isDraw();

    SimpleTeam getWinner();
    SimpleTeam getLoser();
}
