package fss.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class UEFARatingTest extends BaseTest{
    @Test
    public void UEFARatingTest() {
        var teamsWithCountries = generateClubTeams(8, 3);
        var countryWithoutTeam = generateCountryWithID(100500);

        var data = generateUEFARatingData(teamsWithCountries, countryWithoutTeam);
    }

    private ArrayList<UEFARatingData> generateUEFARatingData(HashMap<ClubTeam, Country> teamsWithCountries,
                                                             Country countryWithoutTeam) {
        var data = new ArrayList<UEFARatingData>();



        Country currentCountry = null;
        double pointCountry = 1.0;
        double pointClub = 3.0;
        double addPoint = 0.0;
        for(var obj : teamsWithCountries.entrySet()) {
            if(currentCountry != obj.getValue()) {
                currentCountry = obj.getValue();
                pointCountry = 1.0;
                pointClub = 3.0;
                data.add(new UEFARatingData(2021, currentCountry, pointCountry + addPoint));
            }

            data.add(new UEFARatingData(2021, obj.getKey(), pointClub + addPoint));
            addPoint += 0.1;
        }


        data.add(new UEFARatingData(2021, countryWithoutTeam, 2.22));

        return data;
    }

}