package org.loda.lalfred;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TimerHandler<T> implements InvocationHandler {
	
	private T target;
	
	public TimerHandler(T target) {
		super();
		this.target = target;
	}
	
	@SuppressWarnings("unchecked")
	public T getProxy(){
		return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		long start=System.currentTimeMillis();
		Object rs=method.invoke(target, args);
		long cost=System.currentTimeMillis()-start;
		System.out.println(cost+"ms");
		return rs;
	}

}
