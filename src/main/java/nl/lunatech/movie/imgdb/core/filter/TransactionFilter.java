package nl.lunatech.movie.imgdb.core.filter;

import nl.lunatech.movie.imgdb.core.service.StatistcServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Order(1)
public class TransactionFilter implements Filter {


    private final static Logger LOG = LoggerFactory.getLogger(TransactionFilter.class);

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        StatistcServiceImpl.statistics.putIfAbsent(req.getRequestURI(), new AtomicInteger(0));
        StatistcServiceImpl.statistics.get(req.getRequestURI()).incrementAndGet();
        LOG.info("Starting a transaction for req : {}", req.getRequestURI());

        chain.doFilter(request, response);
        LOG.info("Committing a transaction for req : {}", req.getRequestURI());
    }

}
