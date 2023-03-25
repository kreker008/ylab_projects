public class DatedMapTest {
    public static void main(String[] args) {
        DatedMapImpl datedMap = new DatedMapImpl();

        datedMap.put("1", "A");
        datedMap.put("2", "B");
        datedMap.put("3", "C");
        datedMap.put("4", "D");
        datedMap.put("5", "E");

        try {
            Thread.sleep(2 * 1000);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        datedMap.put("6", "z");

        System.out.println(datedMap.getKeyLastInsertionDate("1"));
        System.out.println(datedMap.getKeyLastInsertionDate("6"));
        System.out.println(datedMap.getKeyLastInsertionDate("0"));

        datedMap.remove("1");
        System.out.println(datedMap.getKeyLastInsertionDate("1"));


        System.out.println(datedMap.keySet());
        System.out.println(datedMap.containsKey("1"));
        System.out.println(datedMap.containsKey("2"));
        System.out.println(datedMap.get("1"));
    }
}
