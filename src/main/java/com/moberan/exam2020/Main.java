package com.moberan.exam2020;
import com.moberan.exam2020.lib.Task;
import com.moberan.exam2020.lib.TestLibrary;

/*
* [ 문제 해결과정 ]
1. 사용되는 라이브러리 코드 분석을 통해 정상적인 return 값 확인
2. 디버깅 + System.out.println 을 이용하여 어느부분에서 문제가 생기는지 확인.
3. 문제점 : firstTask + secondTask 로 success 가 나오기 이전에 task() 함수에서 null 이 먼저 리턴 + main() 에서 출력됨.

4. firstTask의 콜백 함수를 먼저 실행 시킨 후 출력을 할 수 있게하기 위해 지연 관련 자료들 구글링
5. 자바에서 코틀린의 코루틴과 같은 역할을 하는 것을 찾아봄

7. 쓰레드  sleep() 을 우선적으로 적용 시도
8. sleep은 시간을 정하여 정지시키는 것이기 때문에 wait() 을 사용하기로 변경
이때 참고한 링크
https://mainia.tistory.com/720
https://www.baeldung.com/java-wait-notify

+ 시행 착오 중 여기저기 wait()을 사용해보았으나 (lib.wait() 이나 task() 메소드 자체에 synchronized 사용 시도 등) monitor exception 발생

함수에 명시하는 것이 아닌 객체에 명시하는 방법으로 우회

9. Object를 중지시킬 때 사용하는 것이 wait() 이기때문에 Object 객체 생성 후 notify() 설정

10. wait()까지는 성공하였으나 notify()되지 않음을 확인 -> java wait not working, not notifying,  등의 키워드로 구글링

synchronized(object){
    object.notify();
}

형태로 사용해야 한다는 스택 오버플로우 답변 발견
https://stackoverflow.com/questions/19524594/java-wait-notify-not-working
*/

public class Main {
	public static void main(String[] args){
		String result = tasks();
		System.out.println(result);
	}

	private static final Object lock = new Object();

	private static String tasks(){
		TestLibrary lib = new TestLibrary();
		final String[] result = new String[1];
			lib.firstTask(new Task() {
				@Override
				public  void taskCallback(String s) {
					result[0] = lib.secondTask(s);
					synchronized (lock){
						lock.notify();
					}
				}
			});
			synchronized (lock){
				while(result[0] == null){
					try {
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		return result[0];
	}
}