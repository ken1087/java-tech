package com.kang.webflux_reactive_stream.stream;

/**
 * WebFlux 
 * 단일 스레드, 비동기 + Stream을 통해 백프레셔가 적용된 데이터만큼 간헐적 응답이 가능함
 * 데이터 소비가 끝나면 응답이 종료
 * SSE 적용하면 데이터 소비가 끝나도 Stream 계속 유지 (Servlet, WebFlux)
 */
public class App {

    public static void main(String[] args) {
        MyPublisher pub = new MyPublisher();
        MySubscriber sub = new MySubscriber();

        // 구독하기
        pub.subscribe(sub);

    }
}