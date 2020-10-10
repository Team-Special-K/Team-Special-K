
public class TableFactory{
 
    public static QueryBuilder TableFactory(Object obj){
        QueryBuilder tableType = null;
        if(obj instanceof OrderQuery) tableType = new OrderQuery();
        else if (obj instanceof ProductQuery) tableType = new ProductQuery();
        return tableType;
    }
}