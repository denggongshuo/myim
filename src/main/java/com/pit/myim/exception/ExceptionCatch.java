package com.pit.myim.exception;


import com.pit.myim.entity.RES;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 统一异常捕获类
 *
 * @author Administrator
 * @version 1.0
 * @create 2018-09-14 17:32
 **/
@ControllerAdvice//控制器增强
@Slf4j
public class ExceptionCatch {



    //捕获RRException此类异常
    @ExceptionHandler(RRException.class)
    @ResponseBody
    public RES handleRRException(RRException rRException) {
        int code = rRException.getCode();
        String msg = rRException.getMsg();
        RES r = RES.fail( code, msg);
        //记录日志
        log.error("catch exception:{}", rRException.getMessage());

        return r;
    }


    //捕获Exception此类异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public RES exception(Exception exception) {
        exception.printStackTrace();
        //记录日志
        log.error("catch exception:{}", exception.getMessage());

            //返回99999异常
            return  RES.fail(500,"服务器繁忙");



    }


}
