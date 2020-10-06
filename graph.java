import tech.tablesaw.api;


public class graph {

{
public String toGraph(String[] 2darray)

{

inventory_team = Table.createFromCsv("inventory_team.csv");
NumericColumn x = inventory_team.nCol("Date");
NumericColumn y = inventory_team.nCol("Qty");
Line.show("Stats on Inventory", x, y);
}}
    
}
