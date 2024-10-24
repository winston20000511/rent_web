package rent189.servlet;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class SessionFilter extends HttpFilter implements Filter {

	private static final long serialVersionUID = 1L;


	public SessionFilter() {
        super();
        
    }

	
	public void destroy() {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
		
        String loginURI = httpRequest.getContextPath() + "/backstage-login.jsp";
        String loginServletURI = httpRequest.getContextPath() + "/BackstageLoginServlet";
        
        boolean loggedIn = (session != null && session.getAttribute("admin") != null);
        boolean loginRequest = httpRequest.getRequestURI().equals(loginURI) || httpRequest.getRequestURI().equals(loginServletURI);
        
        if (loggedIn) {
            // 如果已經登入，則繼續請求
            chain.doFilter(request, response);
        } else if (loginRequest) {
            // 如果是登入請求，則繼續請求
            chain.doFilter(request, response);
        } else {
            // 否則重定向到登入頁面
            httpResponse.sendRedirect(loginURI);
        }
	}


	public void init(FilterConfig fConfig) throws ServletException {
		
	}

}
