import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class LogAspect {
    private static final String POINTCUT_METHOD = "execution(public * *(..))";

    @Pointcut(POINTCUT_METHOD)
    public void publicMethodExecuted() {}

    @After("publicMethodExecuted()")
     public void logMethod(JoinPoint joinPoint) throws Throwable {
        System.out.printf("starts method: %s. \n", joinPoint.getSignature());

        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg != null) {
                System.out.printf("with argument of type %s and value %s. \n", arg.getClass().toString(), arg);
            }
        }
        System.out.printf("successfuly exits method: %s. \n", joinPoint.getSignature());
    }
}