import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class OrgStructureParserImpl implements OrgStructureParser{
    private Employee boss;
    private List<Employee> list;

    public OrgStructureParserImpl(){
        this.list = new ArrayList<>();
    }

    private void setEmployee(String line){
        Employee employee = new Employee();

        String elements[] = line.split(";");
        employee.setId(Long.valueOf(elements[0]));
        try {
            employee.setBossId(Long.valueOf(elements[1]));
        }catch (NumberFormatException e){
            this.boss = employee;
            employee.setBossId(0L);
        }
        employee.setName(elements[2]);
        employee.setPosition(elements[3]);

        this.list.add(employee);
    }

    List<Employee> setSubordinate(Employee currentEmployee){
        for(Employee employee: list){
            if(Objects.equals(currentEmployee.getId(), employee.getBossId()))
                currentEmployee.getSubordinate().add(employee);
        }
        for(Employee employee: list){
            if(Objects.equals(currentEmployee.getId(), employee.getBossId()))
                currentEmployee.getSubordinate().addAll(setSubordinate(employee));
        }
        return currentEmployee.getSubordinate();
    }


    @Override
    public Employee parseStructure(File csvFile) throws IOException {
        try(BufferedReader bufferedReader  = new BufferedReader(new FileReader(csvFile))) {
            String line;

            if (!bufferedReader.readLine().equals("id;boss_id;name;position")) {
                return null;
            }

            while ((line = bufferedReader.readLine()) != null) {
                setEmployee(line);
            }
            setSubordinate(boss);
            boss.setBossId(null);
        }
        return boss;
    }
}
