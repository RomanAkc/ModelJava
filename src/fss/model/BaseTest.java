package fss.model;

import org.junit.Assert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

//Base test class
public class BaseTest {
    public class WinTwoMeetTest implements WinTwoGameable {
        private SimpleTeam firstTeam = null;
        private SimpleTeam secondTeam = null;
        private Result firstMeet = null;
        private Result secondMeet = null;
        private Result addTime = null;
        private Result pen = null;

        public WinTwoMeetTest(SimpleTeam firstTeam, SimpleTeam secondTeam) {
            this.firstTeam = firstTeam;
            this.secondTeam = secondTeam;
        }

        public void SetFirstMeetResult(int goalHome, int goalAway) {
            firstMeet = new Result(goalHome, goalAway);
        }

        public void SetSecondMeetResult(int goalHome, int goalAway) {
            secondMeet = new Result(goalHome, goalAway);
        }

        public void SetAddTimeMeetResult(int goalHome, int goalAway) {
            addTime = new Result(goalHome, goalAway);
        }

        public void SetPenMeetResult(int goalHome, int goalAway) {
            pen = new Result(goalHome, goalAway);
        }

        @Override
        public boolean isWinnerHomeTeamFirstMeet() {
            return firstMeet.isWin();
        }

        @Override
        public boolean isDrawFirstMeet() {
            return firstMeet.isDraw();
        }

        @Override
        public boolean isWinnerHomeTeamSecondMeet() {
            return !secondMeet.isWin() && !secondMeet.isDraw();
        }

        @Override
        public boolean isDrawSecondMeet() {
            return secondMeet.isDraw();
        }

        private int getGoalsFirstTeamMainTime() {
            return firstMeet.getGoalHome() + secondMeet.getGoalAway();
        }

        private int getGoalsSecondTeamMainTime() {
            return firstMeet.getGoalAway() + secondMeet.getGoalHome();
        }

        private Result getResultMeetWOPen() {
            var goalsFor = getGoalsFirstTeamMainTime();
            var goalsAway = getGoalsSecondTeamMainTime();

            if(addTime != null) {
                goalsFor += addTime.getGoalAway();
                goalsAway += addTime.getGoalHome();
            }

            return new Result(goalsFor, goalsAway);
        }

        @Override
        public boolean isWinnerHomeTeamWOPen() {
            return getResultMeetWOPen().isWin();
        }

        @Override
        public boolean isDrawWOPen() {
            return getResultMeetWOPen().isDraw();
        }

        @Override
        public SimpleTeam getTeamHome() {
            return firstTeam;
        }

        @Override
        public SimpleTeam getTeamAway() {
            return secondTeam;
        }

        @Override
        public void calc() {
        }

        @Override
        public Result getResultMeet() {
            return null;
        }

        @Override
        public boolean isWinnerHomeTeam() {
            var goalsFirstTeam = getGoalsFirstTeamMainTime();
            var goalsSecondTeam = getGoalsSecondTeamMainTime();

            if(goalsFirstTeam != goalsSecondTeam) {
                return goalsFirstTeam > goalsSecondTeam;
            }

            if(!addTime.isDraw()) {
                return addTime.getGoalAway() > addTime.getGoalHome();
            }

            return pen.getGoalAway() > pen.getGoalHome();
        }

        @Override
        public boolean isDraw() {
            return false;
        }

        @Override
        public SimpleTeam getWinner() {
            return isWinnerHomeTeam() ? firstTeam : secondTeam;
        }

        @Override
        public SimpleTeam getLoser() {
            return isWinnerHomeTeam() ? secondTeam : firstTeam;
        }
    }

    public class WinMeetTest implements WinGameable {
        private SimpleTeam firstTeam = null;
        private SimpleTeam secondTeam = null;
        private Result mainTime = null;
        private Result addTime = null;
        private Result pen = null;

        public WinMeetTest(SimpleTeam firstTeam, SimpleTeam secondTeam) {
            this.firstTeam = firstTeam;
            this.secondTeam = secondTeam;
        }

        public void SetMeetResult(int goalHome, int goalAway) {
            mainTime = new Result(goalHome, goalAway);
        }

        public void SetAddTimeMeetResult(int goalHome, int goalAway) {
            addTime = new Result(goalHome, goalAway);
        }

        public void SetPenMeetResult(int goalHome, int goalAway) {
            pen = new Result(goalHome, goalAway);
        }

        private Result getResultMeetWOPen() {
            var goalsFor = mainTime.getGoalHome();
            var goalsAway = mainTime.getGoalAway();

            if(addTime != null) {
                goalsFor += addTime.getGoalAway();
                goalsAway += addTime.getGoalHome();
            }

            return new Result(goalsFor, goalsAway);
        }

        @Override
        public boolean isWinnerHomeTeamWOPen() {
            return getResultMeetWOPen().isWin();
        }

        @Override
        public boolean isDrawWOPen() {
            return getResultMeetWOPen().isDraw();
        }

        @Override
        public SimpleTeam getTeamHome() {
            return firstTeam;
        }

        @Override
        public SimpleTeam getTeamAway() {
            return secondTeam;
        }

        @Override
        public void calc() {

        }

        @Override
        public Result getResultMeet() {
            return mainTime;
        }

        @Override
        public boolean isWinnerHomeTeam() {
            if(mainTime.getGoalHome() != mainTime.getGoalAway()) {
                return mainTime.getGoalHome() > mainTime.getGoalAway();
            }

            if(!addTime.isDraw()) {
                return addTime.getGoalAway() > addTime.getGoalHome();
            }

            return pen.getGoalAway() > pen.getGoalHome();
        }

        @Override
        public boolean isDraw() {
            return false;
        }

        @Override
        public SimpleTeam getWinner() {
            return isWinnerHomeTeam() ? firstTeam : secondTeam;
        }

        @Override
        public SimpleTeam getLoser() {
            return isWinnerHomeTeam() ? secondTeam : firstTeam;
        }
    }

    public class MeetTest implements Gameable {
        private SimpleTeam firstTeam = null;
        private SimpleTeam secondTeam = null;
        private Result result = null;

        public MeetTest(SimpleTeam firstTeam, SimpleTeam secondTeam) {
            this.firstTeam = firstTeam;
            this.secondTeam = secondTeam;
        }

        public MeetTest(SimpleTeam firstTeam, SimpleTeam secondTeam, int goalHome, int goalAway) {
            this.firstTeam = firstTeam;
            this.secondTeam = secondTeam;
            SetMeetResult(goalHome, goalAway);
        }

        public void SetMeetResult(int goalHome, int goalAway) {
            result = new Result(goalHome, goalAway);
        }

        @Override
        public SimpleTeam getTeamHome() {
            return firstTeam;
        }

        @Override
        public SimpleTeam getTeamAway() {
            return secondTeam;
        }

        @Override
        public void calc() {

        }

        @Override
        public Result getResultMeet() {
            return result;
        }

        @Override
        public boolean isWinnerHomeTeam() {
            return result.isWin();
        }

        @Override
        public boolean isDraw() {
            return result.isDraw();
        }

        @Override
        public SimpleTeam getWinner() {
            if(result.isWin()) {
                return firstTeam;
            } else if(result.isDraw()) {
                return null;
            }
            return secondTeam;
        }

        @Override
        public SimpleTeam getLoser() {
            if(result.isWin()) {
                return secondTeam;
            } else if(result.isDraw()) {
                return null;
            }
            return firstTeam;
        }
    }

    protected ArrayList<SimpleTeam> generateClubTeams(int cnt) {
        var country = generateCounty();
        var teams = new ArrayList<SimpleTeam>();
        for(int i = 0; i < cnt; ++i) {
            teams.add(new ClubTeam(i, String.format("Team %d", i), country, 30, 30, 30));
        }
        return teams;
    }

    protected ArrayList<SimpleTeam> generateClubTeamsWithPower(int cnt, int power) {
        var country = generateCounty();
        var teams = new ArrayList<SimpleTeam>();
        for(int i = 0; i < cnt; ++i) {
            teams.add(new ClubTeam(i, String.format("Team %d", i), country, power, power, power));
        }
        return teams;
    }

    protected Country generateCounty() {
        return generateCountryWithID(1);
    }

    protected HashMap<ClubTeam, Country> generateClubTeamsWithCountries(int cntCountries, int cntClubForCountry) {
        var result = new HashMap<ClubTeam, Country>();

        int teamID = 1;
        for(int i = 0; i < cntCountries; ++i) {
            var country = generateCountryWithID(i);
            for(int j = 0; j < cntClubForCountry; ++j) {
                teamID += 1;
                var team = new ClubTeam(teamID, String.format("Team %d", teamID), country, 30);
                result.put(team, country);
            }
        }

        return result;
    }

    public Country generateCountryWithID(int id) {
        return new Country(id, "Country" + id, WorldPart.EUROPE);
    }

    public ArrayList<NationalTeam> genereateNational(int cnt) {
        ArrayList<NationalTeam> result = new ArrayList<>();

        for(int i = 0; i < cnt; ++i) {
            result.add(new NationalTeam(i, new Country(i, Integer.toString(i), WorldPart.EUROPE), 30,30,30));
        }

        return result;
    }

    protected void saveRating(Ratingable rating, FileOutputStream fileStream) {
        Assert.fail();
    }

    protected void writeRatingToFile(Ratingable rating, String fileName) {
        FileOutputStream fileStream = null;
        try {
            fileStream = new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
            Assert.fail();
        }

        saveRating(rating, fileStream);

        try {
            fileStream.close();
        } catch (IOException e) {
            Assert.fail();
        }
    }

    protected Ratingable readRating(FileInputStream fileStream) {
        Assert.fail();
        return null;
    }

    protected Ratingable readRatingFromFile(String fileName) {
        FileInputStream fileStream;
        try {
            fileStream = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return null;
        }

        Ratingable rating = readRating(fileStream);
        Assert.assertNotNull(rating);

        try {
            fileStream.close();
        } catch (IOException e) {
            Assert.fail();
        }

        return rating;
    }
}
