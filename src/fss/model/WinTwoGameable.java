package fss.model;

public interface WinTwoGameable extends WinGameable {
    boolean isWinnerHomeTeamFirstMeet();
    boolean isDrawFirstMeet();
    boolean isWinnerHomeTeamSecondMeet();
    boolean isDrawSecondMeet();
}
