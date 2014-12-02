import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.jspsmart.upload.*;

public class servletUpload extends HttpServlet {
	
	private ServletConfig config;
	/**
	* Init the servlet
	*/
	final public void init(ServletConfig config) throws ServletException {
		this.config = config;
	}
	
	final public ServletConfig getServletConfig() {
		return config;
	}
	/**
	* Handles GET requests
	*/
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		out.println("<HTML>");
		out.println("<BODY BGCOLOR='white'>");
		out.println("<H1>jspSmartUpload : Servlet Sample</H1>");
		out.println("<HR><BR>");
		out.println("The method of the HTML form must be POST.");
		out.println("</BODY>");
		out.println("</HTML>");
	}
	
	/**
	* Handles POST requests
	*/
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		out.println("<HTML>");
		out.println("<BODY BGCOLOR='white'>");
		out.println("<H1>jspSmartUpload : Servlet Sample</H1>");
		out.println("<HR>");

		// Variables
		int count=0;
		SmartUpload mySmartUpload = new SmartUpload();

		try {
			// Initialization
			mySmartUpload.initialize(config,request,response);

			// Upload
			mySmartUpload.upload();

			// Save the file with the original name
			// in a virtual path of the web server
			count = mySmartUpload.save(mySmartUpload.getRequest().getParameter("PATH"));
			
			// Display the result
			out.println(count + " file uploaded.");

		} catch (Exception e){
			out.println("Unable to upload the file.<br>");
			out.println("Error : " + e.toString());
		}
		
		out.println("</BODY>");
		out.println("</HTML>");
          }
	/**
	* Destroy the servlet
	*/
	public void  destroy () {
	}

} 
