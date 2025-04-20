package com.kang.virtual_thread.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/threads")
public class ThreadController {

    // 스레드 수 : 요청 수
    private static final int THREAD_COUNT = 2000;
    
    // 요청 하나당 걸리는 시간
    private static final int ITERATIONS = 1000;

    /**
     * Flatform Thread Test
     * 결과 : 48초 걸림
     * @return String
     * @throws InterruptedException
     */
    @GetMapping("/flatform")
    public String getFlatformThread() throws InterruptedException {

        Thread threads[] = new Thread[THREAD_COUNT];

        for (int i = 0; i < THREAD_COUNT; i++) {

            threads[i] = new Thread(() -> {

                for (int j = 0; j < ITERATIONS; j++) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            threads[i].start();
        }

        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i].join();
        }
        
        return "hello";
    }
    
    /**
     * Virtual Thread Test
     * 결과 : 11초 걸림
     * @return String
     * @throws InterruptedException
     */
    @GetMapping("/virtual")
    public String getVirtualThread() throws InterruptedException {
        Thread threads[] = new Thread[THREAD_COUNT];

        for (int i = 0; i < THREAD_COUNT; i++) {

            threads[i] = Thread.ofVirtual().start(() -> {

                for (int j = 0; j < ITERATIONS; j++) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                
            });

        }

        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i].join();
        }

        return "hello";
    }
}