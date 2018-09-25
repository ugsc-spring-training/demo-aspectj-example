package com.example.demoaspectjexample;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Configuration
@ComponentScan
@EnableAspectJAutoProxy
public class DemoAspectjExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoAspectjExampleApplication.class, args);
	}
}

@Component
class MyRunner implements CommandLineRunner {

	private final HelloClass helloClass;
	private final OtherHello otherHello;

	MyRunner(HelloClass helloClass, OtherHello otherHello) {
		this.helloClass = helloClass;
		this.otherHello = otherHello;
	}

	@Override
	public void run(String... args) throws Exception {
//		helloClass.sayHello("Miso");
//		helloClass.sayNo();
		otherHello.sayOther();

	}
}

@Aspect
@Component
class MyAspect {

	@Before("allSayMethods() && allSayWithoudArgs()")
	public void logSay(JoinPoint joinPoint) {
		String nameOfMethod = joinPoint.getSignature().getName();
		String arg = "";
		if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
			arg = (String)joinPoint.getArgs()[0];
		}
		System.out.println("say method was called: " + nameOfMethod + " arg: " + arg);
	}

	@Before("execution(void *..OtherHello.say*(..))")
	public void callingMethodFromMethod() {
		System.out.println("Proxy imperfection happens");
	}

	@After("stringArgument(arg)")
	public void logMethodWithStringArgument(String arg) {
		System.out.println("Argument je " + arg);
	}

	@Pointcut("execution(* say*(..))")
	public void allSayMethods() {}

	@Pointcut("execution(* say*())")
	public void allSayWithoudArgs() {}

	@Pointcut("args(argument)")
	public void stringArgument(String argument) {}
}

@Component
class HelloClass {
	public void sayHello(String hello) {
		System.out.println(hello);
	}

	public void sayNo() {
		System.out.println("no no");
	}
}

@Component
class OtherHello {

	@Autowired
	private ApplicationContext ctx;

	public void sayOther() {
		System.out.println("sayOther");
		OtherHello bean = ctx.getBean(OtherHello.class);
		bean.saySomething();
	}

	public void saySomething() {
		System.out.println("something");
	}
}

