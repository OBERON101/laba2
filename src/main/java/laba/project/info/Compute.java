package laba.project.info;

import java.util.ArrayList;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

public class Compute {
  private final double quantile = 1.645;

  private Issue geometricMean(double[] arr) {
    Issue out = new Issue();
    out.name = new String("Среднее геометрическое");
    out.value = StatUtils.geometricMean(arr);
    return out;
  }

  private Issue arithmeticMean(double[] arr) {
    Issue out = new Issue();
    out.name = new String("Среднее арифметическое");
    out.value = StatUtils.mean(arr);
    return out;
  }

  private Issue totalNumber(double[] arr) {
    Issue out = new Issue();
    out.name = new String("Размер выборки");
    out.value = arr.length;
    return out;
  }

  private Issue standardDeviation(double[] arr) {
    Issue out = new Issue();
    StandardDeviation s = new StandardDeviation();
    out.name = new String("Оценка стандартного отклонения");
    out.value = s.evaluate(arr);
    return out;
  }

  private Issue max(double[] arr) {
    Issue out = new Issue();
    out.name = new String("Максимум");
    out.value = StatUtils.max(arr);
    return out;
  }

  private Issue min(double[] arr) {
    Issue out = new Issue();
    out.name = new String("Минимум");
    out.value = StatUtils.min(arr);
    return out;
  }

  private Issue variance(double[] arr) {
    Issue out = new Issue();
    out.name = new String("Оценка дисперсии");
    out.value = StatUtils.populationVariance(arr);
    return out;
  }

  private Issue range(double[] arr) {
    Issue out = new Issue();
    out.name = new String("Размах выборки");
    out.value = StatUtils.max(arr) - StatUtils.min(arr);
    return out;
  }

  private Issue coeffVariance(double[] arr) {
    Issue out = new Issue();
    StandardDeviation s = new StandardDeviation();
    out.value = s.evaluate(arr);
    out.name = new String("Коэффициент вариации");
    out.value = s.evaluate(arr) / StatUtils.mean(arr);
    return out;
  }

  private Issue interval(double[] arr) {
    Issue out = new Issue();
    out.name = new String("Доверительный интервал для мат ожидания");
    double mean = StatUtils.mean(arr);
    StandardDeviation s = new StandardDeviation();
    double sd = s.evaluate(arr);
    double lowBound = mean - quantile * sd / Math.sqrt(arr.length);
    double highBound = mean + quantile * sd / Math.sqrt(arr.length);
    out.value = new String("[" + Double.toString(lowBound) + "; " +
                           Double.toString(highBound) + "]");

    return out;
  }

  public ArrayList<Issue> calcPerArray(double[] arr) {
    ArrayList<Issue> out = new ArrayList<Issue>();
    out.add(totalNumber(arr));
    out.add(arithmeticMean(arr));
    Issue gmin = min(arr);
    if ((Double)gmin.value > 0)
      out.add(geometricMean(arr));
    out.add(standardDeviation(arr));
    out.add(variance(arr));
    out.add(max(arr));
    out.add(gmin);
    out.add(range(arr));
    out.add(coeffVariance(arr));
    out.add(interval(arr));

    return out;
  }

  public ArrayList<Issue> calcCov(double[] x, double[] y, double[] z) {
    ArrayList<Issue> out = new ArrayList<Issue>();
    Covariance cov = new Covariance();
    out.add(new Issue("XY", cov.covariance(x, y)));
    out.add(new Issue("XZ", cov.covariance(x, z)));
    out.add(new Issue("YZ", cov.covariance(y, z)));

    return out;
  }
}
