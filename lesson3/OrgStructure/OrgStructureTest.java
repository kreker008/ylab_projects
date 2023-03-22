import java.io.File;
import java.io.IOException;
import java.util.List;

public class OrgStructureTest {
    public static void main(String[] args) throws IOException {
        File file = new File("lesson3\\OrgStructure\\orgstructure.cfg");
        Employee boss = (new OrgStructureParserImpl().parseStructure(file));

        System.out.print(boss);
        System.out.println(boss.getSubordinate()); //2, 4, 5, 3, 6
        for(Employee employee: boss.getSubordinate()){
            System.out.print(employee);
            System.out.println(employee.getSubordinate());
        }
    }
}
