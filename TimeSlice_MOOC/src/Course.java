import java.util.ArrayList;
import java.util.Date;

public class Course {
	
	String title;
	String url;
	
	ArrayList<Date> sessionList;
	ArrayList<Date> endSessionList;
	ArrayList<Date> reviewIntervalList;
	ArrayList<String> duplicated_flag;
	
	String flag_of_duplicated;
	ArrayList<Long> differenceBetweenSessions;
	
	ArrayList<Integer> reviewCount;
	ArrayList<Float> reviewValue;
	int totalReview_count;
	float totalReview_value;
	int course_length;
	String course_hours;
	String school;
	String subject;
	String provider;
	String pace;
	String cetification_type;
	
	
}