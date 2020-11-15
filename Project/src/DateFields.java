import javax.swing.JTextField;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateFields {

    private JTextField dateStart;
    private JTextField dateEnd;
    private JTextField lastDateStart;
    private JTextField lastDateEnd;

    public static final int DATE_CHAR_LEN = 10;
    static final String EOL = "T00:00:00.000000000";

    /*
     * Constructor
     * Provides interaction with date fields
     */
    public DateFields(){
        String dateNow = LocalDateTime.now().toString().substring(0, 10);
        dateStart = new JTextField(dateNow);
        dateEnd = new JTextField(dateNow);
        lastDateStart = null;
        lastDateEnd = null;
    }

    /*
     * Checks date range fields for length and format validity
     * 
     * @return boolean true if fields meet size and format requirements
     */
    protected boolean checkDatesValid(){

        String dateStartText = dateStart.getText();
        String dateEndText = dateEnd.getText();

        if(dateStartText.length() < DATE_CHAR_LEN || 
            dateEndText.length() < DATE_CHAR_LEN){
            return false;
        }

        Pattern dateFormat = Pattern.compile("\\d\\d\\d\\d-\\d\\d-\\d\\d");
        Matcher dateStartMatch = dateFormat.matcher(dateStartText);
        Matcher dateEndMatch = dateFormat.matcher(dateEndText);

        if (!dateStartMatch.find() || !dateEndMatch.find()){
            return false;
        }
        return true;
    }  

    /*
     * Checks date fields contents for change vs previous button click contents
     * 
     * @return boolean true fields have changed
     */
    protected boolean checkFieldsChange(){
        if(!dateStart.getText().equals(lastDateStart.getText()) 
        || !dateEnd.getText().equals(lastDateEnd.getText())){
            return true;
        }
        return false;
    }

    /*
     * Set previous date fields state to current fields contents
     */
    protected void setFieldsLastState(){
        this.lastDateStart = new JTextField(dateStart.getText());
        this.lastDateEnd = new JTextField(dateEnd.getText());
    }

    /*
     * Creates a LocalDateTime object from the date range start field
     * 
     * @return LocalDateTime object created from the text field string
     */
    protected LocalDateTime getStartTimeText(){
        return LocalDateTime.parse(dateStart.getText() + EOL);
    }

    /*
     * Parse and return start fields time in seconds epoch
     * 
     * @return long seconds from epoch
     */
    protected long getStartTimeEpoch(){
        return LocalDateTime.parse(dateStart.getText() + EOL).toEpochSecond(ZoneOffset.UTC);
    }

    /*
     * Parse and return end fields time in seconds epoch
     * 
     * @return long seconds from epoch
     */
    protected long getEndTimeEpoch(){
        return LocalDateTime.parse(dateEnd.getText() + EOL).toEpochSecond(ZoneOffset.UTC);
    }

    /*
     * Creates a LocalDateTime object from the date range end field
     * 
     * @return LocalDateTime object created from the text field string
     */
    protected LocalDateTime getEndTimeText(){
        return LocalDateTime.parse(dateEnd.getText() + EOL);
    }

    /*
     * Returns start date field
     * 
     * @return the JTextField start date field
     */
    protected JTextField getStartField(){
        return this.dateStart;
    }

    /*
     * Returns end date field
     * 
     * @return the JTextField end date field
     */
    protected JTextField getEndField(){
        return this.dateEnd;
    }
}
