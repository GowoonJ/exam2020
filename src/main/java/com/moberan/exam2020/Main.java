package com.moberan.exam2020;
import com.moberan.exam2020.lib.Task;
import com.moberan.exam2020.lib.TestLibrary;

public class Main {
	public static void main(String[] args){
		String result = tasks();
		System.out.println(result);
	}
	private static final Object lock = new Object();
	private static String tasks(){
		TestLibrary lib = new TestLibrary();
		final String[] result = new String[1];
		synchronized (lib){
			lib.firstTask(new Task() {
				@Override
				public  void taskCallback(String s) {
					result[0] = lib.secondTask(s);
					System.out.println("second Task: "+result[0]);
					System.out.println(s);
					notifyAll();
				}
			});
			try {
				lib.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("second Task2 : "+result[0]);
		return result[0];
	}
}