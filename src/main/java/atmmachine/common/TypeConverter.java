package atmmachine.common;

public class TypeConverter {

  public static Long stringToLong(String num) {
    try {
      return Long.parseLong(num);
    } catch (final Exception e) {
      return null;
    }
  }

}
