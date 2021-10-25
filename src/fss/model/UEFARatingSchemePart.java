package fss.model;

enum UEFARatingStageType {
    QUALIFICATION,
    NORMAL
}

class UEFARatingSchemePart {
    public int tournamentID = 0;
    public int stageID = 0;
    public UEFARatingStageType ratingStageType = UEFARatingStageType.NORMAL;
    public int bonusPoint = 0;
}
