package atmmachine.common;

public class TypeConverter {

  public static Long stringToLong(String num) {
    try {
      return Long.parseLong(num);
    } catch (final Exception e) {
      return null;
    }
  }

  public static Double stringToDouble(String num) {
    try {
      return Double.parseDouble(num);
    } catch (final Exception e) {
      return null;
    }
  }


}
