package com.kang.webflux_reactive_stream.stream;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class MySubscriber implements Subscriber<Integer> {

    private Subscription subscription;

    private int bufferSize = 3;

    /**
     * 중요
     */
    @Override
    public void onSubscribe(Subscription s) {
        
        System.out.println("구독자 : 구독 정보 잘 받았어");
        this.subscription = s;

        System.out.println("구독자 : 나 이제 신문 한개씩 줘");
        // (백프레셔) 한 번에 처리할 수 있는 개수를 요청
        this.subscription.request(bufferSize);

    }

    /**
     * 중요
     * @param t 받은 데이터
     */
    @Override
    public void onNext(Integer t) {
        System.out.println("구독 데이터 전달 : " + t);

        bufferSize--;

        // 데이터가 꼬이지 않게 로직
        if (bufferSize == 0) {
            System.out.println("하루 지남");
            // 초기화
            bufferSize = 3;
            this.subscription.request(bufferSize);
        }

    }

    @Override
    public void onError(Throwable t) {
        System.out.println("구독 에러");
    }

    @Override
    public void onComplete() {
        System.out.println("구독 완료");
    }
    
}