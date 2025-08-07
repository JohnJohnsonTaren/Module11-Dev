package thymeleaf;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;
import org.thymeleaf.web.servlet.IServletWebExchange;

import java.io.IOException;

// ThymeleafServlet відповідає за роботу з шаблонами
public class ThymeleafServlet extends HttpServlet {
    private TemplateEngine engine;

    @Override
    public void init() throws ServletException {
        engine = new TemplateEngine();

        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false);

        engine.setTemplateResolver(resolver);
    }

    public void processTemplate(HttpServletRequest req, HttpServletResponse resp, String currentTime) throws IOException {
        resp.setContentType("text/html; charset=UTF-8");

        JakartaServletWebApplication application = JakartaServletWebApplication.buildApplication(req.getServletContext());
        IServletWebExchange webExchange = application.buildExchange(req, resp);

        WebContext context = new WebContext(webExchange, req.getLocale());
        context.setVariable("currentTime", currentTime);

        engine.process("time", context, resp.getWriter());
    }
}
