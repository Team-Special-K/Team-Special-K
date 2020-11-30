import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;


public class KType {

    final static int NUM_COLS = 3;
    public ArrayList<String[]> data;

    public KType(){
        data = new ArrayList<String[]>();
    }
    
    /*
     * Adds a string set to this data.
     */
    public void add(String x, String y, String z){
        data.add(new String[]{x,y,z});
    }

    /*
     * Adds a string set to this data at index i.
     */
    public void add(int i, String[] arr){
        data.add(i, new String[]{arr[0],arr[1], arr[2]});
    }

    /*
     * Returns the size of this data ArrayList.
     */ 
    public int getSize(){
        return data.size();
    }

    /*
     * Returns the set at index i.
     */ 
    public String[] get(int i){
        return data.get(i);
    }

    /*
     * Returns a graph ready 2d ArrayList from this data 
     * 
     * @param x the location in this data of the Epoch in seconds String
     * @param y the location in this data of the Y axis String element
     * @return ArrayList 2d graph ready array
     */
    public ArrayList<double[]> toGraphArray(int xLoc, int yLoc){

        var graphArray = new ArrayList<double[]>();

        for(var tuple : data){
            var line = new double[]{
                Double.parseDouble(tuple[xLoc]) * 1000, 
                Double.parseDouble(tuple[yLoc])
            };
            graphArray.add(line);
        }
        return graphArray;
    }

    public ArrayList<String[]> toBarGraphArray(int xLoc, int yLoc){

        var graphArray = new ArrayList<String[]>();

        for(var tuple : data){
            var line = new String[]{tuple[xLoc], tuple[yLoc]};
            graphArray.add(line);
        }
        return graphArray;
    }

     /*
     * Sorts data by sortField and returns numResults
     * Only numResults initial items are guaranteed to be sorted
     * 
     * @param sortField the column index of field to sort array by
     * @param numResults the number of sorted items to return
     * @return sorted KType
     */
    public static KType sortKTypeBy(int sortField, int numResults, KType unsorted){

        KType sorted = new KType();

        for(var set : unsorted.data){

            int totalItems = sorted.getSize();
            if(totalItems == 0){
                sorted.add(0, set);
                continue;
            }
            else if(totalItems > numResults) 
                totalItems = numResults;

            boolean found = false;
            for(int i = 0; i < totalItems; i++){
                if(Double.parseDouble(set[sortField]) > Double.parseDouble(sorted.get(i)[sortField])){
                    sorted.add(i, set);
                    found = true;
                    break;
                }
            }
            if(!found){sorted.add(set[0], set[1], set[2]);}
        }

        KType limitedResults = new KType();
        int rows = unsorted.getSize() < numResults ? unsorted.getSize() : numResults;
        for(int i = 0; i < rows; i++){
            var line = sorted.get(i);
            limitedResults.add(line[0], line[1], line[2]);
        }
        return limitedResults;
    }

    @SuppressWarnings("unchecked")
    public static KType toKType(Object it){
        KType converted = null;

        if(it instanceof CopyOnWriteArrayList){
            converted = new KType();
            var arr = (CopyOnWriteArrayList<Tup<Double,Integer,Long>>)it;
            for(var set : arr){
                converted.add(Double.toString(set.x), "0", Long.toString(set.z));
            }
        }else if(it instanceof HashMap){
            converted = new KType();
            var arr = (HashMap<String, Double>)it;
            for(var set : arr.entrySet()){
                converted.add(Double.toString(set.getValue()), "", set.getKey());
            }
        }
        return converted;
    }
}