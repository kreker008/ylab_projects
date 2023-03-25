import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DatedMapImpl implements DatedMap {
    private Map<String,String> mapValue;
    private Map<String,Date> mapDate;

    DatedMapImpl(){
        this.mapValue = new HashMap<>();
        this.mapDate = new HashMap<>();
    }

    @Override
    public void put(String key, String value) {
        mapValue.put(key, value);
        mapDate.put(key, new Date(System.currentTimeMillis()));
    }

    @Override
    public String get(String key) {
        return mapValue.get(key);
    }

    @Override
    public boolean containsKey(String key) {
        return mapValue.containsKey(key);
    }

    @Override
    public void remove(String key) {
        mapValue.remove(key);
        mapDate.remove(key);
    }

    @Override
    public Set<String> keySet() {
        return mapValue.keySet();
    }

    @Override
    public Date getKeyLastInsertionDate(String key) {
        return mapDate.get(key);
    }
}
