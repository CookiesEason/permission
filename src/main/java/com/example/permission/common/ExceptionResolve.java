package com.example.permission.common;

import com.example.permission.exception.ParamException;
import com.example.permission.exception.PermissionException;
import com.example.permission.util.ResultVOUtil;
import com.example.permission.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author CookiesEason
 * 2019/01/13 18:04
 */
@Component
@Slf4j
public class ExceptionResolve implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        String url = httpServletRequest.getRequestURL().toString();
        ModelAndView modelAndView;
        String defaultMsg = "System error";
        if (url.endsWith(".json")) {
            if (e instanceof PermissionException || e instanceof ParamException) {
                ResultVO resultVO = ResultVOUtil.error(e.getMessage());
                modelAndView = new ModelAndView(new MappingJackson2JsonView(), resultVO.toMap());
            } else {
                log.error("unknow json exception, url:{}", url, e);
                ResultVO resultVO = ResultVOUtil.error(defaultMsg);
                modelAndView = new ModelAndView(new MappingJackson2JsonView(), resultVO.toMap());
            }
        } else if (url.endsWith(".page")){
            log.error("unknow json exception, url:{}", url, e);
            ResultVO resultVO = ResultVOUtil.error(defaultMsg);
            modelAndView = new ModelAndView("exception", resultVO.toMap());
        } else {
            log.error("unknow json exception, url:{}", url, e);
            ResultVO resultVO = ResultVOUtil.error(defaultMsg);
            modelAndView = new ModelAndView(new MappingJackson2JsonView(), resultVO.toMap());
        }
        return modelAndView;
    }

}
