package com.imust.aop;

import com.alibaba.fastjson.JSON;
import com.imust.aop.annotaion.WebLogger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;


@Aspect
@Component
public class WebLogAop {

    private Logger logger = LoggerFactory.getLogger(WebLogAop.class);

    @Pointcut(value = "execution(public * com.imust.controller.*.*(..)))")
    private void webLog() {

    }

    @Around("webLog()")
    public Object doWebLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Object returnVal;
        Object target = joinPoint.getTarget();
        String methodName = joinPoint.getSignature().getName();
        Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameterTypes();
        Method method;
        //通过反射获得拦截的method
        method = target.getClass().getMethod(methodName, parameterTypes);
        if (method == null) {
            return joinPoint.proceed();
        }
        WebLogger annotation = method.getAnnotation(WebLogger.class);
        //如果方法上没有注解@WebLogger，返回
        if (annotation == null) {
            return joinPoint.proceed();
        }
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String methodToUse = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        logger.info("URL : " + request.getRequestURL().toString());
        logger.info("HTTP_METHOD : " + request.getMethod());
        logger.info("IP : " + request.getRemoteAddr());
        logger.info("CLASS_METHOD : " + methodToUse);
        logger.info("ARGS : " + JSON.toJSONString(joinPoint.getArgs()));
        try {
            returnVal = joinPoint.proceed();
//            doWebLog(methodName, annotation.value(), Arrays.toString(joinPoint.getArgs()),"1", "操作成功");
//            logger.info("RESPONSE DATA : " + JsonUtil.parseObject2Str(returnVal));
        } catch (Exception e) {
        	e.printStackTrace();
//            doWebLog(methodName, annotation.value(), Arrays.toString(joinPoint.getArgs()), "0", e.getMessage());
            throw new RuntimeException(e);
        }
        return returnVal;
    }

//    String doWebLog(String method, String methodDesc, Object args,String status,String remark) {
//        String account = SessionUtil.getUserInfo().getLoginName();
//        if(StringUtils.isEmpty(account))
//            account="anyone";
//        String id = ServletUtil.getSessionId();
//        HandleLog handleLog = new HandleLog();
//
//        if (!Strings.isNullOrEmpty(methodDesc)) {
//            handleLog.setMethodDesc(methodDesc);
//        }
//        handleLog.setLoginAccount(account);
//        handleLog.setMethodArgs(args == null ? "" : args.toString());
//        handleLog.setLoginLogId(id);
//        handleLog.setMethod(method);
//        handleLog.setRemark(remark);
//        handleLog.setOperateStatue(status);
//        handleLog.setOperateTime(new Date());
//        String ip = ServletUtil.getIpAddr();
//        handleLog.setOperateIp(ip);
//
//        handleLog.insert();
//        return id;
//    }
}
