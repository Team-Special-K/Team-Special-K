import java.awt.event.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.swing.*;

public abstract class ButtonReport {

    final protected JButton button;
    final protected JPanel outputArea;
    final private String name;
    protected DateFields dateRange;
    public final int MAX_RESULTS = 10;

    protected ArrayList<Tup<Double,String, LocalDateTime>> result = null;

    /*
     * Constructor
     * Links button to running an algorithm
     * 
     * @param outputArea the visible area to display the algorithm results
     * @param algorithm
     */
    public ButtonReport(JPanel outputArea, String name){
        assert(name.length() > 0);
        this.outputArea = outputArea;
        this.name = name;

        button = new JButton(this.name);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent click) {
                
                outputArea.removeAll();
                outputArea.revalidate();
                outputArea.updateUI();
                
                ImageIcon loading = new ImageIcon("loading.gif");
                outputArea.add(new JLabel("fetching...", loading, JLabel.CENTER));

                if(dateRange.checkDatesValid()){
                    Runnable dbWork = () -> {
                            getResult();
                            displayResult();
                    };
                    new Thread(dbWork).start();
                }
            }
        });
    }

    /*
     * Calculates algorithm results once
     *
     * @return result the array of tuple results
     */
    private ArrayList<Tup<Double,String, LocalDateTime>> getResult(){
        if(result == null || dateRange.checkFieldsChange()){
            dateRange.setFieldsLastState();
            calculateResult();
        }
        return result;
    }

    protected abstract void calculateResult();

    protected abstract void displayResult();
}