import javax.swing.JTextField;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateFields {

    private JTextField dateStart;
    private JTextField dateEnd;
    private JTextField lastDateStart;
    private JTextField lastDateEnd;
    public static final int DATE_CHAR_LEN = 10;
    private static final String EOL = "T00:00:00.000000000";
    public static void main(String args[]){

        var a = new DateFields(new JTextField("2012-01-01"), new JTextField("2442-05-01"));
        System.out.println(a.checkDatesValid());
    }

    
    public DateFields(JTextField dateStart, JTextField dateEnd){
        this.dateStart = dateStart;
        this.dateEnd = dateStart;
        lastDateStart = null;
        lastDateEnd = null;
    }

    /*
     * Checks date range fields for validity
     * 
     * @return boolean true if fields meet size and format requirements
     */
    public boolean checkDatesValid(){

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
     * Creates a LocalDateTime object from the date range start field
     * 
     * @return LocalDateTime object created from the text field string
     */
    public LocalDateTime getStartTime(){
        return LocalDateTime.parse(dateStart.getText() + EOL);
    }

    /*
     * Creates a LocalDateTime object from the date range end field
     * 
     * @return LocalDateTime object created from the text field string
     */
    public LocalDateTime getEndTime(){
        return LocalDateTime.parse(dateEnd.getText() + EOL);
    }
    
}
