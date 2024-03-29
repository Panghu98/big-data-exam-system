package cn.edu.swpu.exam.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 暂时不需要使用
 */

@Aspect
@Component
@Slf4j
public class ControllerLogger {

    /**
     * 定义一个切入点表达式。确定那些类需要代理。以下切入点表示，controller包下的所有类的所有方法都会被代理
     */


    @Pointcut(value = "execution(public * cn.edu.swpu.exam.controller.UserController.*(..))")
    public void userControllerLog(){}

    @Pointcut(value = "execution(public * cn.edu.swpu.exam.controller.InfoController.*(..))")
    public void infoControllerLog(){}

    /**
     * 前置方法。执行在controller之前。
     * JoinPoint对象
     * 定义了三个切面  这样做是为了是的文件参数不被记录
     * @param point  封装了代理方法信息的对象，若用不到则可以忽略不写。
     */

    @Before(value = "userControllerLog() || infoControllerLog()")
    public void before(JoinPoint point){
        log.info("controller aspect beginning");


        /** point.getSignature().getName()——目标方法名
         *  point.getSignature().getDeclaringTypeName()——目标方法所属类的类名
         *  point.getArgs()——获取传入目标方法的参数
         * **/

        Object[] args = point.getArgs();
        for (Object arg :args){
            log.info("arg:"+arg);
        }
        /**类名+被访问的方法名**/
        String method = point.getSignature().getDeclaringTypeName()+"."+point.getSignature().getName();
        log.info("aspect finishing");
        log.info("calling "+ method);
    }

    /**
     * 后置增强。方法退出时执行。
     * @param ret
     */
    @AfterReturning(pointcut = "userControllerLog() ||infoControllerLog()",returning = "ret")
    public void afterReturing(Object ret){
        log.info("controller return "+ret);
    }

    /**
     * 异常抛出增长。
     * @param throwable
     */
    @AfterThrowing(pointcut = " userControllerLog() ||infoControllerLog()",throwing = "throwable")
    public void afterThrowing(Throwable throwable){

        log.info("controller throw "+ throwable.getMessage());
    }

    /**
     * @After 不管是抛出异常或者正常退出都会执行。
     * @Around 环绕增强 （@Around参数必须为ProceedingJoinPoint）
     */


}
