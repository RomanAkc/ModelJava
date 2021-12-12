package fss.model;

import java.util.*;

/*Т. к., предположительно, мы будем вычислять по годам,
  то рейтинг должен принимать результаты предыдущих 4х лет.
  Также он должен принимать результаты матчей в еврокубках (турнир?),
  чтобы корректно рассчитывать очки.
  Также он должен сохранять свое состояние (в файл или в БД).
  Вероятно он должен наследовать и переопределять некий
  абстрактный класс и/или интерфейс, который будет заниматься
  сохраненением рейтинга (и его вычитыаванием?)

  ИЛИ: вычисление и сохранение рейтинга - отдельные классы
  ТЕКУЩИЙ КЛАСС: только возвращает позицию команды

  Устройство:
  ArrayList который отсортирован в порядке убывания рейтинга
  Элемент - или ссылка на команду или ссылка на страну
  После создания ArrayList надо сделать 2 хэша:
  1-й позиция по команде
  2-й позиция по стране
  При поиске сначала ищем в 1м хэше, если нашли - возвращаем
  Если не нашли в 1м - возврашаем из 2го
*/

/*
  Работаем примерно так:
  1.Вычитываем рейтинг на старте сезона, используем его
  2.В конце сезона с помощью UEFARatingCalculator считаем результаты сезона
  3.Обновляем данные (UEFARatingData), выбрасывая те, что за прошлый год и добавляя новые
  4.Сохраняем данные для вычитывания рейтинга
* */

class UEFARating implements Ratingable, CountryRatingable {
    private ArrayList<UEFARatingData> data = null;
    private ArrayList<UEFARatingData> rawData = null;
    private HashMap<ClubTeam, Integer> clubPositions = new HashMap<>();
    private HashMap<Country, Integer> countryPositions = new HashMap<>();
    private ArrayList<Country> countries = new ArrayList<>();

    public UEFARating(ArrayList<UEFARatingData> data) {
        rawData = new ArrayList<>(data);
        calculate();
    }

    private void calculate() {
        data = createData();
        sortData();
        createPositions();
    }

    public void recalcWithChangeData(ArrayList<UEFARatingData> data) {
        Collections.sort(rawData,
           (UEFARatingData lhs, UEFARatingData rhs) ->
                lhs.year < rhs.year ? 1 : (lhs.year > rhs.year) ? -1 : 0
        );

        int yearForDel = !rawData.isEmpty() ? rawData.get(0).year : 0;
        rawData.removeIf(el -> el.year == yearForDel);

        rawData.addAll(data);
        calculate();
    }

    public ArrayList<UEFARatingData> getRawData() {
        return rawData;
    }

    @Override
    public int getTeamPosition(SimpleTeam team) {
        var club = (ClubTeam)team;

        if(clubPositions.containsKey(club)) {
            return clubPositions.get(club);
        }

        if(countryPositions.containsKey(club.getCountry())) {
            return countryPositions.get(club.getCountry());
        }

        return Integer.MAX_VALUE;
    }

    @Override
    public Country getCountryByPosition(int position) {
        return countries.get(position);
    }

    @Override
    public int getAllCountries() {
        return countries.size() - 1;
    }

    private ArrayList<UEFARatingData> createData() {
        HashMap<ClubTeam, UEFARatingData> hashDataTeams = new HashMap<>();
        HashMap<Country, UEFARatingData> hashDataCountries = new HashMap<>();

        for(var obj : rawData) {
            ClubTeam team = obj.team;
            if(team != null) {
                if(hashDataTeams.containsKey(team)) {
                    hashDataTeams.get(team).point += obj.point;
                } else {
                    UEFARatingData data = new UEFARatingData(0, team, obj.point);
                    hashDataTeams.put(team, data);
                }
            } else {
                if(hashDataCountries.containsKey(obj.country)) {
                    hashDataCountries.get(obj.country).point += obj.point;
                } else {
                    UEFARatingData data = new UEFARatingData(0, obj.country, obj.point);
                    hashDataCountries.put(obj.country, data);
                }
            }
        }

        ArrayList<UEFARatingData> data = new ArrayList<>();
        data.addAll(hashDataTeams.values());
        data.addAll(hashDataCountries.values());
        return data;
    }

    private void sortData() {
        Collections.sort(data,
            (UEFARatingData lhs, UEFARatingData rhs)
                    -> lhs.point > rhs.point ? -1 : (lhs.point < rhs.point) ? 1 : 0
        );
    }

    private void createPositions() {
        int teamIndex = 0;
        int countryIndex = 0;
        for(int i = 0; i < data.size(); ++i) {
            var value = data.get(i);
            if(value.team != null) {
                clubPositions.put(value.team, ++teamIndex);
            } else {
                countryPositions.put(value.country, ++countryIndex);
            }
        }

        for(int i = 0; i <= countryIndex; i++) {
            countries.add(null);
        }

        for(var kv : countryPositions.entrySet()) {
            countries.set(kv.getValue(), kv.getKey());
        }
    }
}
