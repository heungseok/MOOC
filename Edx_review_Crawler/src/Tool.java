import java.awt.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class Tool {
	
	boolean endData=false;
	boolean endReviewData = false;
	int page = 1;
	int review_page = 1;
	int index = 0;
	ArrayList<Integer> pagelist = new ArrayList<Integer>(); 
	Data courseData = new Data();
	
	
	public ArrayList<String> getAllVideoURL(final String targetSITE)
	{
		
		final ArrayList<String> urlList=new ArrayList<String>();
		
		
		Integer mpage=new Integer(1);
														
		String urlString = targetSITE.concat("?page=").concat(
				String.valueOf(mpage));
		
		pagelist.add(mpage);
		
		// jSoup Connection instance 
		Connection mainConn;
		Document mainDom = null;
		mainConn = Jsoup.connect(urlString).timeout(0);

		try {
			mainDom = mainConn.get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Elements nav = mainDom.getElementsByTag("main").first().child(0)
				.getElementsByClass("course-listing__leftpanel").first().child(3).select("li");
				
		final int pageSize = Integer.valueOf(nav.get(nav.size()-2).text());
		System.out.println(pageSize);
		
											
		while (true) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					Integer mpage=new Integer(page);
					++page;
															
					String urlString = targetSITE.concat("?page=").concat(
							String.valueOf(mpage));
					
					pagelist.add(mpage);
					
					// jSoup Connection instance 
					Connection mainConn;
					Document mainDom = null;
					mainConn = Jsoup.connect(urlString).timeout(1000*5);

					try {
						mainDom = mainConn.get();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Elements hrefs = mainDom.getElementsByTag("main").first().child(0)
							.getElementsByClass("course-listing__leftpanel").first().child(2).select("div.course-listing");
					if( mpage <= pageSize){
						
						for(int i=0; i<hrefs.size(); i++){
							courseData.url = hrefs.get(i).select("a[data-analytics-course").attr("href").toString();
							urlList.add(courseData.url);
												
						}
					
					}else{
						endData = true;
					}
						
				}
			}).start();
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			if(endData)
			{
				//threadLock
				while(Thread.activeCount()!=1)
				{
				}
				for (String string : urlList) {
					System.out.print(string+"\n");
				}

				System.out.print("-----Url size(the number of Courses in couretalk.com)------\n");
				System.out.print(urlList.size()+"\n");
				page = 1;
				return urlList;
				
			}
		
		}
	}
	
	/*
	 * End of get All url function
	 */
	
	/*
	 * creatDomRoot
	 * 
	 */
	
	
	org.w3c.dom.Document newCreatedDocument = null;
	
	synchronized public org.w3c.dom.Document createDomRoot(){
		System.out.println("----------------Root create-----------------");

		try {
			newCreatedDocument = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		org.w3c.dom.Node root = newCreatedDocument.createElement("ROOT");
		newCreatedDocument.appendChild(root);
		return newCreatedDocument;
		
	}
	
	/*
	 * Crawl data.
	 * 
	 */
	
	public void crawlData(final String url, final Integer index){
		
		Connection connection;
		endReviewData = false;
		
		Integer mpage=new Integer(1);
		
		String urlString = url.concat("?page=").concat(
				String.valueOf(mpage).concat("#reviews"));
						
		connection = Jsoup.connect(urlString).timeout(10*1000);
		Document document = null;
								
		try {
			document = connection.get();
		} catch (Exception e) {
			e.printStackTrace();
		}
						
		Element review = document.getElementById("reviews");
		
		Elements review_nav = review.select("nav").select("li");
		final int page_size;
		if(review_nav.isEmpty()){
			page_size = 1;
		}else{
			page_size = Integer.valueOf(review_nav.get(review_nav.size()-2).text());
		}
		System.out.println("reviews page size - " + page_size);
				
		while(true){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Connection connection;
					
					Integer reviewPage=new Integer(review_page);
										
					synchronized (this) {
						++review_page;
						
					}
																	
					String urlString = url.concat("?page=").concat(
							String.valueOf(reviewPage).concat("#reviews"));
												
					connection = Jsoup.connect(urlString).timeout(10*1000);
					
					// newCreatedDocument is destination of XML.
					NodeList nodelist=newCreatedDocument.getElementsByTagName("ROOT");
					Node root=nodelist.item(0);
					
					Document document = null;
					
					Data data = new Data();
									
					try {
						document = connection.get();
					} catch (Exception e) {
						e.printStackTrace();
					}
						
					Element body = document.body();
					Element basic_info = body.getElementById("outbound");
					
					// course title
					data.title = basic_info.attr("data-analytics-course");
					// course-id
					data.id = basic_info.attr("data-course-id");
					// course rating
					data.ratingValue = basic_info.attr("data-analytics-rating").toString();
					// course provider
					data.provider = basic_info.attr("data-analytics-provider");

					Elements metadata = body.select("div.course-info__academic").first().children();
					// providing school				
					data.school = metadata.get(1).text();
								
					
					Elements reviews = document.getElementById("reviews").select("div.reviews-list__item");
					System.out.println("size of reviews - " + reviews.size());
					
					if( reviewPage <= page_size){
						
						
						System.out.println("current url- " + urlString);
						// making dom elements
						org.w3c.dom.Element review_info = newCreatedDocument.createElement("Course-Reivew");
						root.appendChild(review_info);
						{
							
							// course_id
							org.w3c.dom.Element course_id = newCreatedDocument
									.createElement("course_id");
							course_id.appendChild(newCreatedDocument.createTextNode(data.id));
							
							// course_title
							org.w3c.dom.Element course_title = newCreatedDocument.createElement("course_title");
							course_title.appendChild(newCreatedDocument.createTextNode(data.title));
							
							
							/*
							 *  review data is always starting second index of reviews Elements
							 *  Therefore, if the target course dosen't have any review, the for-loop below of this comment will be not operate 
							 */
							for(int i=2; i<reviews.size(); i++){
								
								data.reviewer_id = reviews.get(i).getElementsByAttribute("data-review-id").attr("data-review-id");
								data.review_date = reviews.get(i).select("div.review-body-info").select("time.review-body-info__pubdate").get(0).attr("datetime");
								data.review_value = reviews.get(i).select("meta[itemprop]").get(0).attr("content").toString();
								data.review = reviews.get(i).select("div.review-body__content").get(0).text();
								
								
								review_info.appendChild(course_id);
								review_info.appendChild(course_title);
								
								org.w3c.dom.Element reviewer_id = newCreatedDocument.createElement("reviewer_id");
								reviewer_id.appendChild(newCreatedDocument.createTextNode(data.reviewer_id));
								review_info.appendChild(reviewer_id);
								
								org.w3c.dom.Element review_date = newCreatedDocument.createElement("review_date");
								review_date.appendChild(newCreatedDocument.createTextNode(data.review_date));
								review_info.appendChild(review_date);
								
								org.w3c.dom.Element review_value = newCreatedDocument.createElement("review_value");
								review_value.appendChild(newCreatedDocument.createTextNode(data.review_value));
								review_info.appendChild(review_value);
								
								org.w3c.dom.Element review = newCreatedDocument.createElement("review");
								review.appendChild(newCreatedDocument.createTextNode(data.review));
								review_info.appendChild(review);
								
							}

						}

						
					}else{
						
						endReviewData = true;
						System.out.println("last page of the course - " + reviewPage);
					}
		
				}
			}).start();
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			if(endReviewData)
			{
				//threadLock
				while(Thread.activeCount()!=1)
				{
				}
				System.out.println("finish in - " + url);
				endReviewData = false;
				return;
											
				
			}
		}
		
	}

	
}