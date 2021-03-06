import java.util.ArrayList;
import java.util.Date;

public class Course {
	
	String url;		// courseURL
	String title;
	int course_id;
			
	// Time Invariant(categorical area; institution; lecturer; lecturer's gender, race, age; level; price)
	String average_effort_hours;
	String course_length;
	String level;
	String price;
	String school;		// institution
	String subject;		// subject
	String provider;	// platform
	String language;	// language
	
	// Time Varying(network; human, keywords) 
	ArrayList<String> keywordsList;
	
	// Control(Fixed Effect: course, time // Number of sessions for the course <0,,... n> )
	Date startingPoint;	// 1st element of the session list
	
	// Fixed Effect - Time data
	ArrayList<CourseTime> TimeData;
	
	// Session info
	ArrayList<Date> sessionList;
	int session_size;
	int session_length;	// session Length
	
	
	int totalReview_count;
	float totalReview_value = 0.0f;
	
	String cetification_type;
	String pace;
	
	ArrayList<Date> endSessionList;
	ArrayList<Date> reviewIntervalList;
	ArrayList<String> duplicated_flag;
	
	String flag_of_duplicated;
	ArrayList<Long> differenceBetweenSessions;
	
	ArrayList<Integer> reviewCount;
	ArrayList<Float> reviewValue;
	
}


