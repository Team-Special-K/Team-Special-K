import java.util.ArrayList;

public class KType {

    public ArrayList<String[]> data;

    public KType(){
        data = new ArrayList<String[]>();
    }
    
    /*
     * Returns the size of this data ArrayList
     */
    public void add(String x, String y, String z){
        data.add(new String[]{x,y,z});
    }

    /*
     * Returns the size of this data ArrayList
     */ 
    public int getSize(){
        return data.size();
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
}