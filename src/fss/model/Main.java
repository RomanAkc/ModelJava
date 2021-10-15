package fss.model;

public class Main {

    public static void main(String[] args) throws Exception {

       /* var teams = new ArrayList<SimpleTeam>();
        teams.add(new NationalTeam(1, "Italy", SimpleTeam.WorldPart.EUROPE, 30, 0, 0));
        teams.add(new NationalTeam(2, "USA", SimpleTeam.WorldPart.NORD_AMERICA, 27, 0, 0));
        teams.add(new NationalTeam(3, "Russia", SimpleTeam.WorldPart.EUROPE, 26, 0, 0));
        teams.add(new NationalTeam(4, "South Korea", SimpleTeam.WorldPart.ASIA, 24, 0, 0));
        var days = RoundSystem.fillDays(teams);
        for(var day : days) {
            day.calc();
        }

        var table = new Table(days);
        table.calc();
        System.out.println(table.toString());*/


       /* var teams = new ArrayList<SimpleTeam>();
        teams.add(new ClubTeam(1, "Real Madrid", "Spain", SimpleTeam.WorldPart.EUROPE, 30, 0, 0));
        teams.add(new ClubTeam(2, "Borussia M", "Germany", SimpleTeam.WorldPart.EUROPE, 27, 0, 0));
        teams.add(new ClubTeam(3, "Shakhtar D", "Ukraine", SimpleTeam.WorldPart.EUROPE, 25, 0, 0));
        teams.add(new ClubTeam(4, "Internazionale", "Italy", SimpleTeam.WorldPart.EUROPE, 28, 0, 0));

        var days = RoundSystem.fillDays2Round(teams);
        for(var day : days) {
            day.calcUseOwner();
        }

        var table = new Table(days);
        table.clearRules();
        table.addWinRule(Table.WinRules.BY_MEET);
        table.addWinRule(Table.WinRules.BY_COUNT_WIN);
        table.addWinRule(Table.WinRules.BY_DIFFERENCE_GOAL);
        table.addWinRule(Table.WinRules.BY_GOAL_FOR);
        table.calc();
        System.out.println(table.toString());*/

        /*var teams = new ArrayList<SimpleTeam>();
        teams.add(new ClubTeam(1, "Atalanta", "Italy", SimpleTeam.WorldPart.EUROPE, 25, 0, 0));
        teams.add(new ClubTeam(2, "Juventus", "Italy", SimpleTeam.WorldPart.EUROPE, 29, 0, 0));
        teams.add(new ClubTeam(3, "Milan", "Italy", SimpleTeam.WorldPart.EUROPE, 26, 0, 0));
        teams.add(new ClubTeam(4, "Internazionale", "Italy", SimpleTeam.WorldPart.EUROPE, 28, 0, 0));
        teams.add(new ClubTeam(3, "Napoli", "Italy", SimpleTeam.WorldPart.EUROPE, 25, 0, 0));
        teams.add(new ClubTeam(3, "Roma", "Italy", SimpleTeam.WorldPart.EUROPE, 26, 0, 0));
        teams.add(new ClubTeam(3, "Lazio", "Italy", SimpleTeam.WorldPart.EUROPE, 25, 0, 0));
        teams.add(new ClubTeam(3, "Sassuolo", "Italy", SimpleTeam.WorldPart.EUROPE, 22, 0, 0));
        teams.add(new ClubTeam(3, "Hellas Verona", "Italy", SimpleTeam.WorldPart.EUROPE, 21, 0, 0));
        teams.add(new ClubTeam(3, "Sampdoria", "Italy", SimpleTeam.WorldPart.EUROPE, 24, 0, 0));
        teams.add(new ClubTeam(3, "Bologna", "Italy", SimpleTeam.WorldPart.EUROPE, 21, 0, 0));
        teams.add(new ClubTeam(3, "Udinese", "Italy", SimpleTeam.WorldPart.EUROPE, 22, 0, 0));
        teams.add(new ClubTeam(3, "Genoa", "Italy", SimpleTeam.WorldPart.EUROPE, 20, 0, 0));
        teams.add(new ClubTeam(3, "Fiorentina", "Italy", SimpleTeam.WorldPart.EUROPE, 25, 0, 0));
        teams.add(new ClubTeam(3, "Spezia", "Italy", SimpleTeam.WorldPart.EUROPE, 19, 0, 0));
        teams.add(new ClubTeam(3, "Benevento", "Italy", SimpleTeam.WorldPart.EUROPE, 19, 0, 0));
        teams.add(new ClubTeam(3, "Torino", "Italy", SimpleTeam.WorldPart.EUROPE, 20, 0, 0));
        teams.add(new ClubTeam(3, "Cagliari", "Italy", SimpleTeam.WorldPart.EUROPE, 20, 0, 0));
        teams.add(new ClubTeam(3, "Parma", "Italy", SimpleTeam.WorldPart.EUROPE, 22, 0, 0));
        teams.add(new ClubTeam(3, "Crotone", "Italy", SimpleTeam.WorldPart.EUROPE, 18, 0, 0));

        var days = RoundSystem.fillDays2Round(teams);
        for(var day : days) {
            day.calcUseOwner();
        }

        var table = new Table(days, new ArrayList<>());
        table.addWinRule(Table.WinRules.BY_MEET);
        table.addWinRule(Table.WinRules.BY_COUNT_WIN);
        table.addWinRule(Table.WinRules.BY_DIFFERENCE_GOAL);
        table.addWinRule(Table.WinRules.BY_GOAL_FOR);
        table.calc();
        System.out.println(table.toString());*/

        /*var tournamentStage = new TournamentStage("WC Qualifying Group H", 2);
        tournamentStage.AddTeam(new NationalTeam(1, "Croatia", SimpleTeam.WorldPart.EUROPE, 27, 26,26));
        tournamentStage.AddTeam(new NationalTeam(1, "Russia", SimpleTeam.WorldPart.EUROPE, 26, 26,26));
        tournamentStage.AddTeam(new NationalTeam(1, "Slovakia", SimpleTeam.WorldPart.EUROPE, 24, 26,26));
        tournamentStage.AddTeam(new NationalTeam(1, "Cyrpus", SimpleTeam.WorldPart.EUROPE, 22, 26,26));
        tournamentStage.AddTeam(new NationalTeam(1, "Slovenia", SimpleTeam.WorldPart.EUROPE, 24, 26,26));
        tournamentStage.AddTeam(new NationalTeam(1, "Malta", SimpleTeam.WorldPart.EUROPE, 18, 26,26));
        tournamentStage.addWinRules(Table.WinRules.BY_MEET);
        tournamentStage.addWinRules(Table.WinRules.BY_DIFFERENCE_GOAL);
        tournamentStage.addWinRules(Table.WinRules.BY_GOAL_FOR);
        tournamentStage.calc();
        System.out.println(tournamentStage.toString());*/

        /*var poStage = new PlayOffStage("League Champions 1/2", true);
        poStage.addTeam(new ClubTeam(1, "Real Madrid", "Spain", WorldPart.EUROPE, 30, 0, 0));
        poStage.addTeam(new ClubTeam(1, "Chelsea", "England", WorldPart.EUROPE, 30, 0, 0));
        poStage.addTeam(new ClubTeam(1, "PSG", "France", WorldPart.EUROPE, 30, 0, 0));
        poStage.addTeam(new ClubTeam(1, "Manchester City", "England", WorldPart.EUROPE, 30, 0, 0));
        poStage.setSortTeams(false);
        poStage.calc();
        System.out.println(poStage.toString());

        System.out.println("Winners: ");
        System.out.println(System.lineSeparator());
        var winners = poStage.getWinners();
        for(var w : winners) {
            System.out.println(w);
        }*/
    }
}
