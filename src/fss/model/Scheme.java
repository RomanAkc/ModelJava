package fss.model;


/*
В схеме польхователь определяет схему турнира
Он описывает пулы стадий турнира от первого до последнего
По сути это лист (вектор)

Идентификатор пула стадий
Источник команд (сколько их?, берутся с другого пула стадий или извне?)
Тип пула стадий


 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Scheme implements Iterable<SchemePart> {
    private ArrayList<SchemePart> parts = new ArrayList<>();
    private HashMap<Integer, SchemePart> partHash = new HashMap<>();

    void AddPart(SchemePart part) {
        parts.add(part);
        partHash.put(part.ID, part);
    }

    SchemePart GetPartByID(int ID) {
        return partHash.get(ID);
    }

    //Может перенести функцию check в другое место? Или вообще убрать?
    boolean check() {
        var prevID = new HashSet<Integer>();
        for(int i = 0; i < parts.size(); ++i) {
            var part = parts.get(i);

            if(i == 0) {
                if(!checkFirstPart(part)) {
                    return false;
                }
            } else {
                for(var src : part.teamSources) {
                    if (!checkNPart(src, prevID)) {
                        return false;
                    }
                }
            }

            prevID.add(part.ID);
        }

        return true;
    }

    private boolean checkNPart(TeamsSource src, HashSet<Integer> prevID) {
        if(src.source == SchemePart.Source.NO) {
            return false;
        }
        if(src.source == SchemePart.Source.PREV_STAGE) {
            if(!prevID.contains(src.sourcePrevID)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkFirstPart(SchemePart part) {
        if(part.teamSources.size() != 1) {
            return false;
        }
        if(part.teamSources.get(0).source != SchemePart.Source.FROM_OUT) {
            return false;
        }
        return true;
    }

    @Override
    public Iterator<SchemePart> iterator() {
        return parts.iterator();
    }
}
