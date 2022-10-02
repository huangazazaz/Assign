import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

public class MovieAnalyzer {

  List<String> title = new ArrayList<>();
  List<Integer> years = new ArrayList<>();
  List<String> certification = new ArrayList<>();
  List<Integer> runtime = new ArrayList<>();
  List<String> genre = new ArrayList<>();
  List<Float> rating = new ArrayList<>();
  List<Integer> overview = new ArrayList<>();
  List<Integer> score = new ArrayList<>();
  List<String> director = new ArrayList<>();
  List<String> star1 = new ArrayList<>();
  List<String> star2 = new ArrayList<>();
  List<String> star3 = new ArrayList<>();
  List<String> star4 = new ArrayList<>();
  List<Integer> vote = new ArrayList<>();
  List<Integer> gross = new ArrayList<>();

  public List<String> getTitle() {
    return title;
  }

  public List<Integer> getYears() {
    return years;
  }


  public List<Integer> getRuntime() {
    return runtime;
  }

  public List<String> getGenre() {
    return genre;
  }

  public List<Float> getRating() {
    return rating;
  }

  public List<Integer> getOverview() {
    return overview;
  }

  public List<Integer> getScore() {
    return score;
  }

  public List<String> getStar1() {
    return star1;
  }

  public List<String> getStar2() {
    return star2;
  }

  public List<String> getStar3() {
    return star3;
  }

  public List<String> getStar4() {
    return star4;
  }

  public List<Integer> getVote() {
    return vote;
  }

  public List<Integer> getGross() {
    return gross;
  }

  public List<String> getTopStars(int top_k, String by) {
    List<String> stars1 = this.getStar1();
    List<String> stars2 = this.getStar2();
    List<String> stars3 = this.getStar3();
    List<String> stars4 = this.getStar4();
    List<Float> rating = this.getRating();
    Map<String, Integer> nums = new HashMap<>();
    if (by.equals("rating")) {
      Map<String, Double> ratings = new HashMap<>();
      for (int i = 0; i < star1.size(); i++) {
        if (ratings.containsKey(stars1.get(i))) {
          ratings.replace(stars1.get(i), ratings.get(stars1.get(i)) + rating.get(i));
        } else {
          ratings.put(stars1.get(i), Double.valueOf(rating.get(i)));
        }

        if (ratings.containsKey(stars2.get(i))) {
          ratings.replace(stars2.get(i), ratings.get(stars2.get(i)) + rating.get(i));
        } else {
          ratings.put(stars2.get(i), Double.valueOf(rating.get(i)));
        }

        if (ratings.containsKey(stars3.get(i))) {
          ratings.replace(stars3.get(i), ratings.get(stars3.get(i)) + rating.get(i));
        } else {
          ratings.put(stars3.get(i), Double.valueOf(rating.get(i)));
        }

        if (ratings.containsKey(stars4.get(i))) {
          ratings.replace(stars4.get(i), ratings.get(stars4.get(i)) + rating.get(i));
        } else {
          ratings.put(stars4.get(i), Double.valueOf(rating.get(i)));
        }

        nums.put(stars1.get(i), nums.getOrDefault(stars1.get(i), 0) + 1);
        nums.put(stars2.get(i), nums.getOrDefault(stars2.get(i), 0) + 1);
        nums.put(stars3.get(i), nums.getOrDefault(stars3.get(i), 0) + 1);
        nums.put(stars4.get(i), nums.getOrDefault(stars4.get(i), 0) + 1);
      }
      Map<String, Double> rs = new HashMap<>();
      for (String s : ratings.keySet()) {
        double a = ratings.get(s);
        rs.put(s, (a / nums.get(s)));
      }
      LinkedHashMap<String, Double> results = new LinkedHashMap<>();
      rs.entrySet().stream().sorted((k1, k2) -> {
        if (k1.getValue() < k2.getValue()) {
          return 1;
        } else if (k1.getValue().equals(k2.getValue())) {
          return k1.getKey().compareTo(k2.getKey());
        } else {
          return -1;
        }
      }).forEachOrdered((e -> results.put(e.getKey(), e.getValue())));
      return new ArrayList<>(results.keySet()).subList(0, top_k);
    } else {
      List<Integer> grosss = this.getGross();
      Map<String, Long> gross = new HashMap<>();
      for (int i = 0; i < star1.size(); i++) {
        if (grosss.get(i) == 0) {
          continue;
        }
        gross.put(stars1.get(i), gross.getOrDefault(stars1.get(i), 0L) + grosss.get(i));
        nums.put(stars1.get(i), nums.getOrDefault(stars1.get(i), 0) + 1);
        gross.put(stars2.get(i), gross.getOrDefault(stars2.get(i), 0L) + grosss.get(i));
        nums.put(stars2.get(i), nums.getOrDefault(stars2.get(i), 0) + 1);
        gross.put(stars3.get(i), gross.getOrDefault(stars3.get(i), 0L) + grosss.get(i));
        nums.put(stars3.get(i), nums.getOrDefault(stars3.get(i), 0) + 1);
        gross.put(stars4.get(i), gross.getOrDefault(stars4.get(i), 0L) + grosss.get(i));
        nums.put(stars4.get(i), nums.getOrDefault(stars4.get(i), 0) + 1);
      }
      Map<String, Long> gs = new HashMap<>();
      for (String s : gross.keySet()) {
        long a = gross.get(s);
        gs.put(s, a / nums.get(s));
      }
      LinkedHashMap<String, Long> results = new LinkedHashMap<>();
      gs.entrySet().stream().sorted((k1, k2) -> {
        if (k1.getValue() < k2.getValue()) {
          return 1;
        } else if (k1.getValue().equals(k2.getValue())) {
          return k1.getKey().compareTo(k2.getKey());
        } else {
          return -1;
        }
      }).forEachOrdered((e -> results.put(e.getKey(), e.getValue())));
      return new ArrayList<>(results.keySet()).subList(0, top_k);
    }
  }

  public Map<Integer, Integer> getMovieCountByYear() {
    Map<Integer, Integer> years = new HashMap<>();
    for (int year : this.getYears()) {
      years.put(year, years.getOrDefault(year, 0) + 1);
    }
    LinkedHashMap<Integer, Integer> year = new LinkedHashMap<>();
    years.entrySet().stream().sorted(Map.Entry.<Integer, Integer>comparingByKey().reversed())
        .forEachOrdered(e -> year.put(e.getKey(), e.getValue()));
    return year;
  }


  public Map<String, Integer> getMovieCountByGenre() {
    Map<String, Integer> genres = new HashMap<>();
    for (String genre : this.getGenre()) {
      String[] gen = genre.replaceAll("\\\"|\\ ", "").split(",");
      for (String g : gen) {
        genres.put(g.replace(" ", ""), genres.getOrDefault(g.replace(" ", ""), 0) + 1);

      }
    }
    LinkedHashMap<String, Integer> g = new LinkedHashMap<>();
    genres.entrySet().stream().sorted((k1, k2) -> {
      if (k1.getValue() < k2.getValue()) {
        return 1;
      } else if (k1.getValue().equals(k2.getValue())) {
        return k1.getKey().compareTo(k2.getKey());
      }
      return -1;
    }).forEachOrdered(e -> g.put(e.getKey(), e.getValue()));
    return g;
  }

  public List<String> getTopMovies(int top_k, String by) {
    if (by.equals("runtime")) {
      List<Integer> time = this.getRuntime();
      List<String> titlee = this.getTitle();
      String[] ts = new String[time.size()];
      for (int i = 0; i < time.size(); i++) {
        ts[i] = titlee.get(i);
      }
      int[] tis = new int[ts.length];
      for (int i = 0; i < time.size(); i++) {
        tis[i] = time.get(i);
      }
      String[] title = (String[]) mergeSort(tis, tis.length, ts).get(1);
      List<String> titles = new ArrayList<>(Arrays.asList(title));
      titles = titles.subList(0, top_k);
      return titles;
    } else {
      List<Integer> Overview = this.getOverview();
      Map<String, Integer> overviews = new HashMap<>();
      for (int i = 0; i < title.size(); i++) {
        overviews.put(title.get(i), Overview.get(i));
      }
      LinkedHashMap<String, Integer> overss = new LinkedHashMap<>();
      overviews.entrySet().stream().sorted((k1, k2) -> {
        if (k1.getValue() < k2.getValue()) {
          return 1;
        } else if (k1.getValue().equals(k2.getValue())) {
          return k1.getKey().compareTo(k2.getKey());
        }
        return -1;
      }).forEachOrdered(e -> overss.put(e.getKey(), e.getValue()));
      List<String> overview = new ArrayList<String>(overss.keySet());
      overview = overview.subList(0, top_k);
      return overview;
    }

  }

  public List<String> searchMovies(String genre, float min_rating, int max_runtime) {
    List<String> titles = this.getTitle();
    List<String> title = new ArrayList<String>();
    for (int i = 0; i < titles.size(); i++) {
      if (this.getGenre().get(i).contains(genre) && this.getRating().get(i) >= min_rating
          && this.getRuntime().get(i) <= max_runtime && !title.contains(titles.get(i))) {
        title.add(titles.get(i));
      }
    }
    title.sort(String::compareTo);
    return title;

  }

  public Map<List<String>, Integer> getCoStarCount() {
    Map<String, Integer> costar = new HashMap<>();
    List<String> stars1 = this.getStar1();
    List<String> stars2 = this.getStar2();
    List<String> stars3 = this.getStar3();
    List<String> stars4 = this.getStar4();
    for (int i = 0; i < star4.size(); i++) {
      costar.put(stars1.get(i).compareTo(stars2.get(i)) < 0
          ? stars1.get(i) + ",," + stars2.get(i) : stars2.get(i) + ",," + stars1.get(i),
          costar.getOrDefault(stars1.get(i).compareTo(stars2.get(i)) < 0
          ? stars1.get(i) + ",," + stars2.get(i) : stars2.get(i) + ",," + stars1.get(i), 0) + 1);
      costar.put(stars1.get(i).compareTo(stars3.get(i)) < 0
          ? stars1.get(i) + ",," + stars3.get(i) : stars3.get(i) + ",," + stars1.get(i),
          costar.getOrDefault(stars1.get(i).compareTo(stars3.get(i)) < 0
          ? stars1.get(i) + ",," + stars3.get(i) : stars3.get(i) + ",," + stars1.get(i), 0) + 1);
      costar.put(stars1.get(i).compareTo(stars4.get(i)) < 0
          ? stars1.get(i) + ",," + stars4.get(i) : stars4.get(i) + ",," + stars1.get(i),
          costar.getOrDefault(stars1.get(i).compareTo(stars4.get(i)) < 0
          ? stars1.get(i) + ",," + stars4.get(i) : stars4.get(i) + ",," + stars1.get(i), 0) + 1);
      costar.put(stars2.get(i).compareTo(stars3.get(i)) < 0
          ? stars2.get(i) + ",," + stars3.get(i) : stars3.get(i) + ",," + stars2.get(i),
          costar.getOrDefault(stars2.get(i).compareTo(stars3.get(i)) < 0
          ? stars2.get(i) + ",," + stars3.get(i) : stars3.get(i) + ",," + stars2.get(i), 0) + 1);
      costar.put(stars2.get(i).compareTo(stars4.get(i)) < 0
          ? stars2.get(i) + ",," + stars4.get(i) : stars4.get(i) + ",," + stars2.get(i),
          costar.getOrDefault(stars2.get(i).compareTo(stars4.get(i)) < 0
          ? stars2.get(i) + ",," + stars4.get(i) : stars4.get(i) + ",," + stars2.get(i), 0) + 1);
      costar.put(stars3.get(i).compareTo(stars4.get(i)) < 0
          ? stars3.get(i) + ",," + stars4.get(i) : stars4.get(i) + ",," + stars3.get(i),
          costar.getOrDefault(stars3.get(i).compareTo(stars4.get(i)) < 0
          ? stars3.get(i) + ",," + stars4.get(i) : stars4.get(i) + ",," + stars3.get(i), 0) + 1);
    }
    Map<List<String>, Integer> result = new HashMap<>();
    String[] cos;
    for (String costars : costar.keySet()) {
      List<String> strings = new ArrayList<>();
      cos = costars.split(",,");
      if (cos[0].compareTo(cos[1]) < 0) {
        strings.add(cos[0]);
        strings.add(cos[1]);
      } else {
        strings.add(cos[1]);
        strings.add(cos[0]);
      }
      result.put(strings, costar.get(costars));
    }
    return result;

  }


  public MovieAnalyzer(String dataset_path) {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(dataset_path,
          StandardCharsets.UTF_8));
      reader.readLine();
      String line;
      while ((line = reader.readLine()) != null) {
        String[] item = line.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)", -1);
        title.add(item[1].replace("\"", ""));
        years.add(Integer.parseInt(item[2]));
        certification.add(item[3]);
        runtime.add(Integer.parseInt(item[4].replace(" min", "").replace("\"", "")));
        Collections.addAll(genre, item[5]);
        rating.add(Float.parseFloat(item[6]));
        overview.add(item[7].charAt(0) == '\"' ? item[7].length() - 2 : item[7].length());
        score.add(item[8].equals("") ? 0 : Integer.parseInt(item[8]));
        director.add(item[9]);
        star1.add(item[10]);
        star2.add(item[11]);
        star3.add(item[12]);
        star4.add(item[13]);
        vote.add(Integer.parseInt(item[14]));
        gross.add(item[15].equals("") ? 0 : Integer.parseInt(item[15].replaceAll("\\,|\\\"", "")));
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static List mergeSort(int[] arr, int n, String[] sarray) {
    if (n > 1) {
      int p = n / 2;
      int[] left = Arrays.copyOfRange(arr, 0, p);
      int[] right = Arrays.copyOfRange(arr, p, n);
      String[] sarrl = Arrays.copyOfRange(sarray, 0, p);
      String[] sarrr = Arrays.copyOfRange(sarray, p, n);
      List listl = mergeSort(left, p, sarrl);
      List listr = mergeSort(right, n - p, sarrr);
      left = (int[]) listl.get(0);
      right = (int[]) listr.get(0);
      sarrl = (String[]) listl.get(1);
      sarrr = (String[]) listr.get(1);
      return merge(left, right, sarrl, sarrr);
    }
    List L = new ArrayList();
    L.add(arr);
    L.add(sarray);
    return L;
  }

  public static List merge(int[] left, int[] right, String[] sarrl, String[] sarrr) {
    int n = left.length + right.length;
    int i = 0;
    int j = 0;
    int[] arr = new int[n];
    String[] sarr = new String[n];
    for (int k = 0; k < n; k++) {
      if (i == left.length) {
        arr[k] = right[j];
        sarr[k] = sarrr[j];
        j++;
        continue;
      }
      if (j == right.length) {
        arr[k] = left[i];
        sarr[k] = sarrl[i];
        i++;
        continue;
      }
      if (left[i] > right[j]) {
        arr[k] = left[i];
        sarr[k] = sarrl[i];
        i++;
      } else if (left[i] < right[j]) {
        arr[k] = right[j];
        sarr[k] = sarrr[j];
        j++;

      } else {
        if (sarrl[i].compareTo(sarrr[j]) < 0) {
          arr[k] = left[i];
          sarr[k] = sarrl[i];
          i++;
        } else {
          arr[k] = right[j];
          sarr[k] = sarrr[j];
          j++;

        }
      }
    }
    List l = new ArrayList();
    l.add(arr);
    l.add(sarr);
    return l;
  }

}