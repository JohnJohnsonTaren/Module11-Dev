package servlet;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

import static org.mockito.Mockito.*;

@MockitoSettings
class TimeServletTest {
    private TimeServlet servlet;
    
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private ServletContext servletContext;
    
    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() throws IOException, ServletException {
        servlet = new TimeServlet();
        servlet.init();
        
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        
        when(response.getWriter()).thenReturn(printWriter);
        when(request.getServletContext()).thenReturn(servletContext);
        when(request.getLocale()).thenReturn(Locale.getDefault());
    }

    @Test
    void testDoGetWithoutTimezoneAndNoCookie() throws IOException {
        when(request.getParameter("timezone")).thenReturn(null);
        when(request.getCookies()).thenReturn(null);

        servlet.doGet(request, response);

        verify(response, atLeastOnce()).setContentType("text/html; charset=UTF-8");
        verify(request).getCookies();
    }

    @Test
    void testDoGetWithTimezone() throws IOException {
        when(request.getParameter("timezone")).thenReturn("UTC+2");

        servlet.doGet(request, response);

        verify(response, atLeastOnce()).setContentType("text/html; charset=UTF-8");
        verify(response).addCookie(any(Cookie.class));
    }

    @Test
    void testDoGetWithoutTimezoneButWithCookie() throws IOException {
        when(request.getParameter("timezone")).thenReturn(null);
        Cookie[] cookies = {new Cookie("lastTimezone", "UTC+2")};
        when(request.getCookies()).thenReturn(cookies);

        servlet.doGet(request, response);

        verify(response, atLeastOnce()).setContentType("text/html; charset=UTF-8");
        verify(request).getCookies();
    }
}