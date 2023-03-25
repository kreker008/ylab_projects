import java.util.HashMap;
import java.util.Map;

public class TransliteratorImpl implements Transliterator {
    private Map<Character,String> table;

    TransliteratorImpl(){
        this.table = new HashMap<>();
        this.table.put('А', "A");
        this.table.put('Б', "B");
        this.table.put('В', "V");
        this.table.put('Г', "G");
        this.table.put('Д', "D");
        this.table.put('Е', "E");
        this.table.put('Ё', "E");
        this.table.put('Ж', "ZH");
        this.table.put('З', "Z");
        this.table.put('И', "I");
        this.table.put('Й', "I");
        this.table.put('К', "K");
        this.table.put('Л', "L");
        this.table.put('М', "M");
        this.table.put('Н', "N");
        this.table.put('О', "O");
        this.table.put('П', "P");
        this.table.put('Р', "R");
        this.table.put('С', "S");
        this.table.put('Т', "T");
        this.table.put('У', "U");
        this.table.put('Ф', "F");
        this.table.put('Х', "KH");
        this.table.put('Ц', "TS");
        this.table.put('Ч', "CH");
        this.table.put('Ш', "SH");
        this.table.put('Щ', "SHCH");
        this.table.put('Ъ', "IE");
        this.table.put('Ы', "Y");
        this.table.put('Ь', "");
        this.table.put('Э', "E");
        this.table.put('Ю', "IU");
        this.table.put('Я', "IA");
    }

    @Override
    public String transliterate(String source) {
        StringBuilder stringBuilder = new StringBuilder();
        char symbol;

        for(int i = 0; i < source.length(); ++i){
            symbol = source.charAt(i);
            if( symbol >= 'А' && symbol <= 'Я' || symbol == 'Ё')
                stringBuilder.append(table.get(symbol));
            else
                stringBuilder.append(symbol);
        }
        return stringBuilder.toString();
    }
}
