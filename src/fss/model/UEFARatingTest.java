package fss.model;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

class UEFARatingTest extends BaseTest{
    @Test
    public void UEFARatingTest() {
        var data = generateUEFARatingData();

    }

    private ArrayList<UEFARatingData> generateUEFARatingData() {
        var data = new ArrayList<UEFARatingData>();

        var teamsWithCountries = generateClubTeams(8, 3);

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

        var countryWithoutTeam = generateCountryWithID(100500);
        data.add(new UEFARatingData(2021, countryWithoutTeam, 2.22));

        return data;
    }

}