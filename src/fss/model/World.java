package fss.model;
import java.util.*;

public class World {
    private int yearStart;
    private Map<Integer, NationalTeam> allNationalTeams;

    public World(int yearStart) {
        this.yearStart = yearStart;

        RandomWrapper.initInstanceNum = 1;

        //инициализируем команды-сборные


    }
}
