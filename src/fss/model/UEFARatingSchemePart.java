package fss.model;

enum UEFARatingStageType {
    QUALIFICATION,
    NORMAL
}

class UEFARatingSchemePart {
    public int stageID = 0;
    public UEFARatingStageType ratingStageType = UEFARatingStageType.NORMAL;
    public int addPoint = 0;
}
