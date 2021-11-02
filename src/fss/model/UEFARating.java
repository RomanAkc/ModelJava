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

class UEFARating implements Ratingable, CountryRatingable {

    private ArrayList<UEFARatingData> data = null;
    private HashMap<ClubTeam, Integer> clubPositions = new HashMap<>();
    private HashMap<Country, Integer> countryPositions = new HashMap<>();
    private ArrayList<Country> countries = new ArrayList<>();

    public UEFARating(ArrayList<UEFARatingData> data) {
        this.data = data;
        sortRatingData();
        createPositions();
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

    private void sortRatingData() {
        Collections.sort(data, new Comparator<UEFARatingData>() {
            @Override
            public int compare(UEFARatingData lhs, UEFARatingData rhs) {
                return lhs.point > rhs.point ? -1 : (lhs.point < rhs.point) ? 1 : 0;
            }
        });
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
