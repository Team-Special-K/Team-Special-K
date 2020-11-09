import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public abstract class ButtonReport {

    JButton button;
    JPanel outputArea;

    String name;

    public final int MAX_RESULTS = 10;

    protected ArrayList<Tup<Double,String>> result = null;

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

                Runnable dbWork = () -> {
                        getResult();
                        displayResult();
                };
                new Thread(dbWork).start();
            }
        });
    }

    /*
     * Calculates algorithm results once
     *
     * @return result the array of tuple results
     */
    private ArrayList<Tup<Double,String>> getResult(){
        if(result == null){
            calculateResult();
        }
        return result;
    }

    abstract void calculateResult();

    abstract void displayResult();
}