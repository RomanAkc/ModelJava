package fss.model;

public interface Matchable {
    SimpleTeam getTeamHome();
    SimpleTeam getTeamAway();

    Result getResultMeet();

    boolean isWinnerHomeTeam();
    boolean isDraw();

    SimpleTeam getWinner();
    SimpleTeam getLoser();
}
