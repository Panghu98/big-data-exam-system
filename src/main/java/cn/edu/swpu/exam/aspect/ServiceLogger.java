package cn.edu.swpu.exam.aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author panghu
 */
//@Aspect
//@Component
@Slf4j
public class ServiceLogger {

    @Pointcut("execution(public * cn.edu.swpu.exam.service.*.*(..))")
    public void serviceLogger(){}

    @Before("serviceLogger()")
    public void before(JoinPoint joinPoint){
        log.info("service beginning");
        /** 获取封装了署名信息的对象,在该对象中可以获取到目标方法名,所属类的Class等信息 */
        Signature signature = joinPoint.getSignature();

        /**类名+被访问的方法名**/
        String method = signature.getDeclaringTypeName() +"."+ signature.getName();

        log.info("\n");
        log.info("calling : "+method);

        /** 获取传入该方法的参数 */
        Object[] args = joinPoint.getArgs();

        for (Object arg : args){
            log.info("arg : "+arg);
        }

        log.info("\n");
    }








}
