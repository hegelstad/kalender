package utilities;

public final class Validator {
    public boolean isValidNumber(String s) {
        return s.matches("[0-9]+");
    }

    public static void main(String[] args) {
        Validator v = new Validator();
        System.out.println(v.isValidNumber("3a3"));
    }
}