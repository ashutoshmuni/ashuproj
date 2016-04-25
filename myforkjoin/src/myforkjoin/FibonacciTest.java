package myforkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class FibonacciTest {
	Fibonacci fibonacci = new Fibonacci(4);
	ForkJoinPool mainPool = new ForkJoinPool(5);
	public Integer getFib(){
		return mainPool.invoke(fibonacci);
	}
	
	public static void main(String[] str){
		System.out.println("In main ");
		Integer result = new FibonacciTest().getFib();
		System.out.println("Out main " + result);
	}
}

class Fibonacci extends RecursiveTask<Integer> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final int n;

	Fibonacci(int n) {
		this.n = n;
	}
	@Override
	protected Integer compute() {
		System.out.println("In compute ");
		if (n <= 1)
			return n;
		Fibonacci f1 = new Fibonacci(n - 1); 
		f1.fork();
		Fibonacci f2 = new Fibonacci(n - 2);
		System.out.println("Out compute ");
		return f2.compute() + f1.join();
	}
}