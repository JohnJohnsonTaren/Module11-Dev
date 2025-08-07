package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import thymeleaf.ThymeleafServlet;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

// TimeServlet відповідає за бізнес-логіку з часом
@WebServlet(value = "/time")
public class TimeServlet extends ThymeleafServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String timezone = req.getParameter("timezone");
        
        if (timezone == null || timezone.isBlank()) {
            timezone = getTimezoneFromCookie(req);
        } else {
            timezone = timezone.trim().replace(" ", "+");
            Cookie timezoneCookie = new Cookie("lastTimezone", timezone);
            timezoneCookie.setMaxAge(30 * 24 * 60 * 60);
            resp.addCookie(timezoneCookie);
        }
        
        LocalDateTime now = LocalDateTime.now(ZoneId.of(timezone));
        String currentTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " " + timezone;
        
        processTemplate(req, resp, currentTime);
    }
    
    private String getTimezoneFromCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("lastTimezone".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return "UTC";
    }
}