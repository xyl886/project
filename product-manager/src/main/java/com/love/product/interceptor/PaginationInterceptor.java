package com.love.product.interceptor;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.love.product.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Optional;


import static com.love.product.constant.CommonConstant.*;

@Component
@Slf4j
@SuppressWarnings("all")
public class PaginationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String currentPage = request.getParameter(CURRENT);
        String pageSize = Optional.ofNullable(request.getParameter(SIZE)).orElse(DEFAULT_SIZE);

        if (!Objects.isNull(currentPage) && !StringUtils.isEmpty(currentPage)) {
            PageUtil.setCurrentPage(new Page<>(Long.parseLong(currentPage), Long.parseLong(pageSize)));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        PageUtil.remove();
    }

}
