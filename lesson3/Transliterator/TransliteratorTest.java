public class TransliteratorTest {
    public static void main(String[] args) {
        TransliteratorImpl transliterator = new TransliteratorImpl();

        String s1 = "ABC ABC АБЦ 123";
        String s2 = "ABC ABC абц 123";
        String s3 = "Щ1Щ2Щ3Щ";
        String s4 = "Я";
        String s5 = "HELLO! ПРИВЕТ! Go, boy!";
        String s6 = "А Б В Г Д Е Ё Ж З И Й К Л М Н О П Р С Т У Ф Х Ц Ч Ш Щ Э Ю Я";

        System.out.println(transliterator.transliterate(s1));
        System.out.println(transliterator.transliterate(s2));
        System.out.println(transliterator.transliterate(s3));
        System.out.println(transliterator.transliterate(s4));
        System.out.println(transliterator.transliterate(s5));
        System.out.println(transliterator.transliterate(s6));
    }
}
