package com.example.statistics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping
@CrossOrigin
public class fonksiyonlar {
    


    @PostMapping("/basitRassal")
    public String basitRassalÖrnekleme(@RequestBody RassalRequest req) {
        int max = req.getMax();
        int min = req.getMin();
        int x = req.getX();
        int[] array = new int[x];
        if (max<min) {
            System.out.println("Hata: max değeri min değerinden küçük olamaz.");
            return "Hata: max değeri min değerinden küçük olamaz.";
        }
        if (x<=0) {
            System.out.println("Hata: x değeri pozitif bir tam sayı olmalıdır.");
            return "Hata: x değeri pozitif bir tam sayı olmalıdır.";
        }
        int range = max - min + 1;
        // for(int i=0;i<x;i++){
        //     array[i]=(int)(Math.random()*(range)+min);
        // }
        System.out.println("Basit Rassal Örnekleme: ");
        // tamamen tekrarsız
        if (range >= x ){
            for(int i=0;i< x;i++){
                int random;
                boolean tekrar;
                do {
                    random = (int)(Math.random()*(range)+min);
                    tekrar = false;
                    for(int j=0;j<i;j++){
                        if(array[j]==random){
                            tekrar = true;
                            break;
                        }
                    }
                } while(tekrar);
                array[i] = random;
            }
        }
        // x den az seçenek olduğu için tekrar edilebilir.
        else{
            // tekrar etmeden önce hepsi kullanıldı mı kontrol
            for(int i=0;i<range;i++){
                int random;
                boolean tekrar;
                do {
                    random = (int)(Math.random()*(range)+min);
                    tekrar = false;
                    for(int j=0;j<i;j++){
                        if(array[j]==random){
                            tekrar = true;
                            break;
                        }
                    }
                } while(tekrar);
                array[i] = random;
            }
            // kalanı rastgele doldurulur.
            for (int i = range; i < x; i++) {
                array[i] = (int)(Math.random() * (range) + min);
            }
        }
        String result = "";
        for(int i=0;i<x;i++){
            result += array[i]+" ";
        }
        return result;
    }

   @PostMapping(
    value = "/sistematikRassal",
    consumes = "application/json",
    produces = "text/plain"
)
    public String sistematikRassalÖrnekleme(@RequestBody sistematikRassalRequest req) {
        int N = req.getTotal();
        int n = req.getN();
    if (n > N) {
        System.out.println("Hata: Örneklem büyüklüğü (n), evren büyüklüğünden (N) büyük olamaz.");
        return "Hata: Örneklem büyüklüğü (n), evren büyüklüğünden (N) büyük olamaz."    ;
    }
    if (N <= 0 || n <= 0) {
        System.out.println("Hata: N ve n pozitif tam sayılar olmalıdır.");
        return "Hata: N ve n pozitif tam sayılar olmalıdır.";
    }
    int k = N / n; // Sistematik örnekleme aralığı
    int a = (int)(Math.random() * k); // Rastgele başlangıç noktası
    System.out.println("Sistematik Rassal Örnekleme: ");
    
    String result = "";
    for (int i = 0; i < n; i++) {
        int index = (a + i * k) ; // Örneklem indeksini hesapla
        result += index + " ";
    }
    return result+"a= "+a+", k= "+k;
  }
    @PostMapping("/tabakalıRassal")
    public Map<String, List<Integer>> stratifiedSampling(
            @RequestBody tabakasalRequests request) {

        List<Integer> strataSizes = request.getStrataSizes();
        int totalSampleSize = request.getTotalSampleSize();

        Map<String, List<Integer>> result = new LinkedHashMap<>();
        int population = strataSizes.stream().mapToInt(Integer::intValue).sum();
        Random random = new Random();

        for (int i = 0; i < strataSizes.size(); i++) {
            int Ni = strataSizes.get(i);
            int ni = Math.round((float) Ni / population * totalSampleSize);

            // Tabaka içi bireyler
            List<Integer> individuals = new ArrayList<>();
            for (int j = 1; j <= Ni; j++) {
                individuals.add(j);
            }

            Collections.shuffle(individuals, random);

            result.put(
                "Tabaka " + (i + 1),
                individuals.subList(0, Math.min(ni, individuals.size()))
            );
        }

        return result;
    }



    @PostMapping("/ortalama")
    public double aritmetikOrtalama(@RequestBody List<Integer> numbers) {
        if (numbers.isEmpty()) return 0;

        int sum = 0;
        for (int n : numbers) {
            sum += n;
        }
        return (double) sum / numbers.size();
    }

  @PostMapping("basitSeri")
  public String basitSeri(@RequestBody List<Integer> numbers) {
      int[] array =numbers.stream().mapToInt(i -> i).toArray();
      
      int n = array.length;

    String result = "";
    for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
    }
    for (int i = 0; i < n; i++) {
        result += array[i] + " ";
    }
      return result;
  }

  @PostMapping("frekansSeri")
  public String frekansSeri(@RequestBody List<Integer> numbers) {
    // Frekans sayısı için Map
    Map<Integer, Integer> frekansMap = new TreeMap<>(); // TreeMap küçükten büyüğe sıralar

    for (int num : numbers) {
        frekansMap.put(num, frekansMap.getOrDefault(num, 0) + 1);
    }

    // HTML tablo formatı
    StringBuilder html = new StringBuilder();
    html.append("<table border='1' style='border-collapse: collapse;'>");
    html.append("<tr><th>Sayı</th><th>Frekans</th></tr>");

    for (Map.Entry<Integer, Integer> entry : frekansMap.entrySet()) {
        html.append("<tr><td>").append(entry.getKey())
            .append("</td><td>").append(entry.getValue())
            .append("</td></tr>");
    }

    html.append("</table>");
    return html.toString();
  
    }

    @PostMapping("frekansTablosu")
    public String frekansTablosu(@RequestBody List<Integer> numbers) {
      int[] array =numbers.stream().mapToInt(i -> i).toArray();
      int n = array.length;
      int max=array[0];
      int min=array[0];
      for(int i=1;i<n;i++){
          if(array[i]>max){
              max=array[i];
          }
          if(array[i]<min){
              min=array[i];
          }
      }
      int R=max-min;
      int k=(int)Math.ceil(Math.sqrt(n));
      int h=(int)Math.ceil((double)R/k);
      int sınıflimitalt[] = new int[k];
      for (int i = 0; i < k; i++) {
        sınıflimitalt[i] = min + i * h;
      }
      int sınıflimitust[] = new int[k];
      for (int i = 0; i < k; i++) {
        sınıflimitust[i] = sınıflimitalt[i] + h-1;
      }
      double sınıfsınıralt[] = new double[k];
      for (int i = 0; i < k; i++) {
        sınıfsınıralt[i] = sınıflimitalt[i] - 0.5;
      }
      double sınıfsınırust[] = new double[k];
      for (int i = 0; i < k; i++) {
        sınıfsınırust[i] = sınıflimitust[i] + 0.5;
      }

        int frekans[] = new int[k];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                if (array[i] >= sınıflimitalt[j] && array[i] <= sınıflimitust[j]) {
                    frekans[j]++;
                    break;
                }
            }
        }

        int SON[]= new int[k];
        for(int i=0;i<k;i++){
            SON[i]=(sınıflimitalt[i]+sınıflimitust[i])/2;
        }

        int eklenikFrekans[]= new int[k];
        eklenikFrekans[0] = frekans[0];
        for(int i=1;i<k;i++){
            eklenikFrekans[i] = eklenikFrekans[i-1] + frekans[i];
        }

        double oransalFrekans[]= new double[k];
        for(int i=0;i<k;i++){
            oransalFrekans[i] = (double) frekans[i]/n;
        }

        double oransalEklenikFrekans[]= new double[k];
        for(int i=0;i<k;i++){
            oransalEklenikFrekans[i] = (double) eklenikFrekans[i]/n;
        }

        // HTML tablo formatı
        StringBuilder html = new StringBuilder();
        html.append("<table border='1' style='border-collapse: collapse;'>");
        html.append("<tr><th>Sınıf Limiti Alt</th><th>Sınıf Limiti Üst</th><th>Sınıf Sınırı Alt</th><th>Sınıf Sınırı Üst</th><th>Frekans</th><th>SON</th><th>Oransal Frekans</th><th>Eklenik Frekans</th><th>Oransal Eklenik Frekans</th></tr>");

        for (int i = 0; i < k; i++) {
            html.append("<tr>");
            html.append("<td>").append(sınıflimitalt[i]).append("</td>");
            html.append("<td>").append(sınıflimitust[i]).append("</td>");
            html.append("<td>").append(sınıfsınıralt[i]).append("</td>");
            html.append("<td>").append(sınıfsınırust[i]).append("</td>");
            html.append("<td>").append(frekans[i]).append("</td>");
            html.append("<td>").append(SON[i]).append("</td>");
            html.append("<td>").append(oransalFrekans[i]).append("</td>");
            html.append("<td>").append(eklenikFrekans[i]).append("</td>");
            html.append("<td>").append(oransalEklenikFrekans[i]).append("</td>");
            html.append("</tr>");
        }

        html.append("</table>");
        return html.toString();

    }

    @PostMapping("/medyan")
    public double medyan(@RequestBody List<Integer> numbers) {
        if (numbers.isEmpty()) return 0;
        List<Integer> sortedNumbers = numbers.stream().sorted().collect(Collectors.toList());
        int n = sortedNumbers.size();
        if (n % 2 == 1) {
            return sortedNumbers.get(n / 2);
        } else {
            int mid1 = sortedNumbers.get(n / 2 - 1);
            int mid2 = sortedNumbers.get(n / 2);
            return (mid1 + mid2) / 2.0;
        }
    }

    @PostMapping("/mod")
    public List<Integer> mod(@RequestBody List<Integer> numbers) {
    if (numbers.isEmpty()) return List.of(); // boş liste döndür

    // Her sayının frekansını hesapla
    Map<Integer, Long> frequencyMap = numbers.stream()
            .collect(Collectors.groupingBy(n -> n, Collectors.counting()));

    // En yüksek frekansı bul
    long maxFrequency = frequencyMap.values().stream().mapToLong(v -> v).max().orElse(0);

    // Bu frekansa sahip tüm sayıları bul
    List<Integer> modes = frequencyMap.entrySet().stream()
            .filter(entry -> entry.getValue() == maxFrequency)
            .map(Map.Entry::getKey)
            .toList();

    return modes; // Liste döner, tek mod veya çoklu mod için uygun

    }
    
    @PostMapping("geometrikOrtalama")
    public String geometrikOrtalama(@RequestBody List<Integer> numbers) {
      int[] array =numbers.stream().mapToInt(i -> i).toArray();
      int n = array.length;

        double product = 1.0;
        for (int num : array) {
            product *= num;
        }
        double geoOrtalama = Math.pow(product, 1.0 / n);
        String entity = String.format("%.2f", geoOrtalama);
        
        return entity;
    }

    @PostMapping("harmonikOrtalama")
    public String harmonikOrtalama(@RequestBody List<Integer> numbers) {
      int[] array =numbers.stream().mapToInt(i -> i).toArray();
      int n = array.length; 

        double reciprocalSum = 0.0;
        for (int num : array) {
            if (num != 0) {
                reciprocalSum += 1.0 / num;
            } else {
                return "Hata: Sıfır değeri harmonik ortalamada kullanılamaz.";
            }
        }
        double harmOrtalama = n / reciprocalSum;
        String entity = String.format("%.2f", harmOrtalama);
        
        return entity;
        
    }

    @PostMapping("/varyans")
    public String varyans(@RequestBody List<Integer> numbers) {
        int[] array =numbers.stream().mapToInt(i -> i).toArray();
        int n = array.length;
        if (n == 0) return "Hata: Boş liste için varyans hesaplanamaz.";

        double mean = aritmetikOrtalama(numbers);
        double sumSquaredDiffs = 0.0;
        for (int num : array) {
            sumSquaredDiffs += Math.pow(num - mean, 2);
        }
        double variance = sumSquaredDiffs / (n - 1); // Varyans
        String entity = String.format("%.2f", variance);
        
        return entity;
    }

    @PostMapping("/standartSapma")
    public String standartSapma(@RequestBody List<Integer> numbers) {
        int[] array =numbers.stream().mapToInt(i -> i).toArray();
        int n = array.length;
        if (n == 0) return "Hata: Boş liste için standart sapma hesaplanamaz.";

        double mean = aritmetikOrtalama(numbers);
        double sumSquaredDiffs = 0.0;
        for (int num : array) {
            sumSquaredDiffs += Math.pow(num - mean, 2);
        }
        double variance = sumSquaredDiffs / (n - 1); // Varyans
        double stdDev = Math.sqrt(variance);
        String entity = String.format("%.2f", stdDev);
        return entity;
    }

    @PostMapping("/oms")
    public String oms(@RequestBody List<Integer> numbers) {
        int[] array =numbers.stream().mapToInt(i -> i).toArray();
        int n = array.length;
        if (n == 0) return "Hata: Boş liste için standart sapma hesaplanamaz.";

        double mean = aritmetikOrtalama(numbers);
        double sumAbsDiffs = 0.0;   
        for (int num : array) {
            sumAbsDiffs += Math.abs(num - mean);
        }
        double oms = sumAbsDiffs / n;
        String entity = String.format("%.2f", oms);
        return entity;
    }

    @PostMapping("/ceyreklikler")
    public String ceyreklikler(@RequestBody List<Integer> numbers) {
      int[] array =numbers.stream().mapToInt(i -> i).toArray();
      int n = array.length;
      int max=array[0];
      int min=array[0];
      for(int i=1;i<n;i++){
          if(array[i]>max){
              max=array[i];
          }
          if(array[i]<min){
              min=array[i];
          }
      }
      int R=max-min;
      int k=(int)Math.ceil(Math.sqrt(n));
      int h=(int)Math.ceil((double)R/k);
      int sınıflimitalt[] = new int[k];
      for (int i = 0; i < k; i++) {
        sınıflimitalt[i] = min + i * h;
      }
      int sınıflimitust[] = new int[k];
      for (int i = 0; i < k; i++) {
        sınıflimitust[i] = sınıflimitalt[i] + h-1;
      }
      double sınıfsınıralt[] = new double[k];
      for (int i = 0; i < k; i++) {
        sınıfsınıralt[i] = sınıflimitalt[i] - 0.5;
      }
    //   double sınıfsınırust[] = new double[k];
    //   for (int i = 0; i < k; i++) {
    //     sınıfsınırust[i] = sınıflimitust[i] + 0.5;
    //   }

        int frekans[] = new int[k];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                if (array[i] >= sınıflimitalt[j] && array[i] <= sınıflimitust[j]) {
                    frekans[j]++;
                    break;
                }
            }
        }
        int eklenikFrekans[]= new int[k];
        eklenikFrekans[0] = frekans[0]; 
        for (int i = 1; i < k; i++) {
            eklenikFrekans[i] = eklenikFrekans[i-1] + frekans[i];
        }

        //Q1 için eklenik frekansın n/4'ten büyük olduğu ilk sınıf bulunur
        int Q1Index = 0;
        for (int i = 0; i < k; i++) {
            if (eklenikFrekans[i] >= n / 4.0) {
                Q1Index = i;
                break;
            }
        }
        Q1Index = Math.max(Q1Index, 0); // Güvenlik kontrolü
        double Q1;
        if(Q1Index == 0) {
           Q1 = sınıfsınıralt[0] + ((n/4.0) * h) / frekans[0];
        } else {
           Q1 = sınıfsınıralt[Q1Index] + ((n/4.0 - eklenikFrekans[Q1Index-1]) * h) / frekans[Q1Index];
    }
        
        //Q3 için eklenik frekansın 3n/4'ten büyük olduğu ilk sınıf bulunur
        int Q3Index = 0;
        for (int i = 0; i < k; i++) {
            if (eklenikFrekans[i] >= 3 * n / 4.0) {
                Q3Index = i;
                break;
            }
        }
        Q3Index = Math.max(Q3Index, 0); // Güvenlik kontrolü
        double Q3;
        if(Q3Index == 0) {
            Q3 = sınıfsınıralt[0] + ((3*n/4.0) * h) / frekans[0];
        } else {
            Q3 = sınıfsınıralt[Q3Index] + ((3*n/4.0 - eklenikFrekans[Q3Index-1]) * h) / frekans[Q3Index];
}
        String entity = String.format("Q1: %.2f - Q3: %.2f", Q1, Q3);
        return entity;
    }

    @PostMapping("/carpıklık")
    public String carpıklık(@RequestBody List<Integer> numbers) {
        int[] array =numbers.stream().mapToInt(i -> i).toArray();
        int n = array.length;
        if (n == 0) return "Hata: Boş liste için carpıklık hesaplanamaz.";

        double mean = aritmetikOrtalama(numbers);
        double sumSquaredDiffs = 0.0;
        for (int num : array) {
            sumSquaredDiffs += Math.pow(num - mean, 3);
        }
        double carpıklık = sumSquaredDiffs / (n - 1); // Çarpıklık
        String entity = String.format("%.2f", carpıklık);
        
        return entity;
    }  
    @PostMapping("/basıklık")
    public String basıklık(@RequestBody List<Integer> numbers) {
        int[] array =numbers.stream().mapToInt(i -> i).toArray();
        int n = array.length;
        if (n == 0) return "Hata: Boş liste için basıklık hesaplanamaz.";

        double mean = aritmetikOrtalama(numbers);
        double sumSquaredDiffs = 0.0;
        for (int num : array) {
            sumSquaredDiffs += Math.pow(num - mean, 4);
        }
        double basıklık = sumSquaredDiffs / (n - 1); // Basıklık
        String entity = String.format("%.2f", basıklık);
        
        return entity;
    }  
    @PostMapping("/dk")
    public String dk(@RequestBody List<Integer> numbers) {
        int[] array =numbers.stream().mapToInt(i -> i).toArray();
        int n = array.length;
        if (n == 0) return "Hata: Boş liste için standart sapma hesaplanamaz.";

        double mean = aritmetikOrtalama(numbers);
        double sumSquaredDiffs = 0.0;
        for (int num : array) {
            sumSquaredDiffs += Math.pow(num - mean, 2);
        }
        double variance = sumSquaredDiffs / (n - 1); // Varyans
        double stdDev = Math.sqrt(variance);
        String entity = String.format(" %.2f", stdDev/mean);
        
        return entity;
    }
    
    
} 
    
    
