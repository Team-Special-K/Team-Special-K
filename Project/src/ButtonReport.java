import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;

public abstract class ButtonReport {

    final protected JButton button;
    final protected JPanel graphArea;
    final protected JPanel outputArea;
    protected DateFields dateRange;
    protected KType result;
    public final int MAX_RESULTS = 10;
    private Font textResultFont = new Font("Helvetica", Font.PLAIN, 13);

    /*
     * Constructor
     * Links button to running an algorithm
     * 
     * @param outputArea the visible area to display the algorithm results
     * @param algorithm
     */
    public ButtonReport(JPanel outputArea, JPanel graphArea, String name){

        assert(name.length() > 0);

        button = new JButton(name);
        this.outputArea = outputArea;
        this.graphArea = graphArea;
    
        applyStyle();

        button.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent click) {

                if(dateRange.checkDatesValid() & (dateRange.checkFieldsChange() 
                   || dateRange.checkGraphObjectChange(name))){ 

                    dateRange.setLastGraphObject(name);
                    outputArea.removeAll();
                    graphArea.removeAll();
                    
                    ImageIcon loading = new ImageIcon("loading.gif");
                    outputArea.add(new JLabel("fetching...", loading, JLabel.CENTER));
    
                    outputArea.revalidate();
                    outputArea.updateUI();
                    graphArea.revalidate();
                    graphArea.updateUI();

                    Runnable dbWork = () -> {
                        dateRange.setFieldsLastState();
                        calculateResult();
                        displayResult();
                    };
                    new Thread(dbWork).start();
                }
            }
        });
    }

    /*
     * Applies specific style to this button.
     */
    private void applyStyle(){
        button.setFont(new Font("Helvetica", Font.PLAIN, 17));
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(new Color(220, 239, 252));
        button.setBorder(BorderFactory.createLineBorder(new Color(3, 140, 247), 0, true));
        button.setFocusable(false);
    }

    /*
     * Creates a graph image file and displays image to this graphArea.
     * 
     * @param yAxisName the string to display on the graphs y-axis.
     */
    protected void displayGraph(String yAxisName){
        var graphArray = result.toGraphArray(2,0);

        Graph graph = new Graph("");
        graph.addPoints(graphArray);
        graph.createLineGraph(yAxisName);
        graph.exportImg();

        ImageIcon graphFile = new ImageIcon(Graph.fileName);
        graphFile.getImage().flush();
        graphArea.add(new JLabel(graphFile));
    }

    /*
     * Formats text results to maximum display count and style. 
     */
    protected JLabel[] formatTextResults(){

        JLabel[] lines = null;

        if (result == null || result.getSize() < 1) {
            lines = new JLabel[]{new JLabel("NO RESULTS")};
        }
        else {    
            int size = result.getSize() < MAX_RESULTS ? result.getSize() : MAX_RESULTS;
            lines = new JLabel[size];
            int i = 0;
            for(var item : result.data.subList(0, size)){
                JLabel line = new JLabel();
                line.setFont(textResultFont);
                line.setText(formatTextResultLine(item[0], item[2]));
                lines[i++] = line;
            }
        }
        return lines;
    }

    protected abstract String formatTextResultLine(String x, String y);
    
    protected abstract void calculateResult();

    protected abstract void displayResult();
}