import javax.servlet.*;
import java.io.IOException;
/**
 * 将所有页面的编码设为utf-8
 */
public class EncodingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        filterChain.doFilter(request,response);
    }
}
