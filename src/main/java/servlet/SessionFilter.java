//package servlet;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.FilterConfig;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.annotation.WebFilter;
//import jakarta.servlet.http.HttpFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//
//@WebFilter("/*")
//public class SessionFilter extends HttpFilter {
//
//    private static final long serialVersionUID = 1L;
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//        HttpSession session = httpRequest.getSession(false);
//
//        String role = (session != null) ? (String) session.getAttribute("role") : null;
//        String uri = httpRequest.getRequestURI();
//        String contextPath = httpRequest.getContextPath();
//
//        System.out.println("--------- Check URI and Session Role --------");
//        System.out.println("Request URI: " + uri);
//        System.out.println("Logged in: " + (session != null));
//        System.out.println("Role: " + role);
//        System.out.println("---------------- End of Check ----------------");
//
//        // Allow static resources
//        if (uri.matches(".*\\.(css|js|jpg|jpeg|png|gif|ico|woff|woff2|ttf|eot|svg|jsp|json)$")) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        // Allow white URIs
//        List<String> allowedURIs = Arrays.asList(
//            contextPath + "/backstage-panel.jsp",
//            contextPath + "/404.jsp",
//            contextPath + "/RegisterServlet",
//            contextPath + "/backstage-register.jsp",
//            contextPath + "/backstage-login.jsp",
//            contextPath + "/BackstageLoginServlet",
//            contextPath + "/LogoutServlet",
//            contextPath + "/UpdateAdminDataServlet",
//            contextPath + "/AdAnalysisServlet.do"
//        );
//        if (allowedURIs.contains(uri)) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        // Handle API Requests
//        if (isApiRequest(httpRequest)) {
//            System.out.println("----------- API Request Detected -----------");
//            System.out.println("API Request for URI: " + uri);
//
//            if (session == null || role == null) {
//                System.out.println("API Request blocked - Not logged in");
//                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
//                httpResponse.getWriter().write("{\"error\":\"Unauthorized: Please log in\"}");
//                return;
//            }
//
//            Map<String, List<String>> rolePermissions = getRolePermissions();
//            if (rolePermissions.containsKey(role) && isUriAllowed(uri, rolePermissions.get(role), contextPath)) {
//                chain.doFilter(request, response); 
//                return;
//            }
//
//            System.out.println("API Request blocked - Insufficient privileges");
//            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden
//            httpResponse.getWriter().write("{\"error\":\"Forbidden: Access Denied\"}");
//            return;
//        }
//
//        // Redirect for unauthorized requests
//        boolean loggedIn = (session != null && session.getAttribute("admin") != null);
//        if (!loggedIn) {
//            httpResponse.sendRedirect(contextPath + "/backstage-login.jsp");
//            return;
//        }
//
//        // Allow SAdmin access to all paths
//        if ("SAdmin".equals(role)) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        // Check role permissions for non-API requests
//        Map<String, List<String>> rolePermissions = getRolePermissions();
//        if (role != null && rolePermissions.containsKey(role)) {
//            List<String> allowedPaths = rolePermissions.get(role);
//            if (isUriAllowed(uri, allowedPaths, contextPath)) {
//                chain.doFilter(request, response);
//                return;
//            }
//        }
//
//        // Block unauthorized access
//        httpResponse.sendRedirect(contextPath + "/404.jsp");
//    }
//
//    // Helper to check if request is an API request
//    private boolean isApiRequest(HttpServletRequest request) {
//        String method = request.getMethod();
//        String uri = request.getRequestURI();
//        String contextPath = request.getContextPath();
//
//        System.out.println("Checking API Request:");
//        System.out.println("Method: " + method);
//
//        if (uri.startsWith(contextPath + "/Complaints")) {
//            return true;
//        }
//
//        return "POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method) ||
//               "DELETE".equalsIgnoreCase(method) || "PATCH".equalsIgnoreCase(method);
//    }
//
//    // Helper to get role permissions
//    private Map<String, List<String>> getRolePermissions() {
//        Map<String, List<String>> rolePermissions = new HashMap<>();
//        rolePermissions.put("SAdmin", Arrays.asList("/*"));
//        rolePermissions.put("Admin", Arrays.asList("/*"));
//        rolePermissions.put("RAdmin-ad", Arrays.asList("/AdAnalysisServlet.do", "/AdDataOperationServelt.do"));
//        rolePermissions.put("RAdmin-book", Arrays.asList("/BookingServlet"));
//        rolePermissions.put("RAdmin-complaint", Arrays.asList("/Complaints", "/Complaints/*"));
//        rolePermissions.put("RAdmin-house", Arrays.asList("/housesDelete.123", "/houses.123", "/houseUpdateServlet.do"));
//        rolePermissions.put("RAdmin-order", Arrays.asList("/OrderDataOperationServlet.do"));
//        rolePermissions.put("RAdmin-user", Arrays.asList("/UpdateUserServlet.do", "/LoadAllUsersServlet.do", "/SearchUserServlet.do"));
//        return rolePermissions;
//    }
//
//    // Helper to check if URI is allowed for the role
//    private boolean isUriAllowed(String uri, List<String> allowedPaths, String contextPath) {
//        for (String allowedPath : allowedPaths) {
//            String fullPath = contextPath + allowedPath;
//            if (allowedPath.endsWith("/*")) {
//                String regex = fullPath.replace("/*", "(/.*)?$");
//                if (uri.matches(regex)) {
//                    return true;
//                }
//            } else if (uri.equals(fullPath)) {
//                return true;
//            }
//        }
//        return false;
//    }
//}