package com.kang.webflux_reactive_stream.stream;

import java.util.Iterator;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * 구독 정보 (구독자, 어떤 데이터를 구독할지)
 */
public class MySubscription implements Subscription {

    private Subscriber s;
    private Iterator<Integer> it;

    public MySubscription(Subscriber s, Iterable<Integer> its) {
        this.s = s;
        this.it = its.iterator();
    }

    /**
     * @param n 개수
     */
    @Override
    public void request(long n) {
        // n : 1
        // it : 데이터
        while (n > 0) {
            if (it.hasNext()) {
                
                // 그 다음 데이터를 넘겨 줌
                s.onNext(it.next());

            } else {

                // 데이터를 다 받은 경우
                s.onComplete();
                break;

            }

            n--;
        }

    }

    /**
     * 
     */
    @Override
    public void cancel() {
        
    }

    
}