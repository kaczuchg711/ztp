package pl.edu.pk.kacza;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.xml.ws.config.metro.parser.jsr109.UrlPatternType;

/**
 * Servlet implementation class IsServlet
 */
@WebServlet("/IsServlet")
public class IsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * Default constructor. 
     */
    public IsServlet() {
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String imagePath = request.getParameterValues("image")[0];
			imagePath = this.decode_image_path(imagePath,response);
			this.write_image(imagePath, response);
		}
		catch (NullPointerException e) {
			response.getWriter().write("Page without image");
		}
	}
	
	private void write_image(String imagePath,HttpServletResponse response) throws IOException {
		try {
			ServletOutputStream outputStream = response.getOutputStream();
			byte imageBytes[] = Files.readAllBytes(Paths.get(imagePath));
			outputStream.write(imageBytes);
		}
		catch (IOException e) {
			response.getWriter().write(e.getMessage());
			response.getWriter().write("File not found");
		}
	}
	
	private String decode_image_path(String imagePath,HttpServletResponse response) throws IOException {
		String result = "";
		try {
		    result = java.net.URLDecoder.decode(imagePath, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			response.getWriter().write(e.getMessage());
		}
		return result;
	}
		
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void print_parameters_with_values(HttpServletRequest request, HttpServletResponse response) throws IOException{
        PrintWriter out = response.getWriter();
        response.setContentType("text/plain");
        String[] paramValues;
        String paramName;
        Enumeration<String> parameterNames = request.getParameterNames();
 
        while (parameterNames.hasMoreElements()) {
 
            paramName = parameterNames.nextElement();
            out.write("Parametr: " + paramName + " = ");
 
            paramValues = request.getParameterValues(paramName);
            for (int i = 0; i < paramValues.length; i++) {
                String paramValue = paramValues[i];
                out.write(paramValue + ",");
                
            }
            out.write("\n");
        }
        out.close();
	}

	
}
