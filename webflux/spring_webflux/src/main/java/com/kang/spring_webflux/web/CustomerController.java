package com.kang.spring_webflux.web;

import org.springframework.web.bind.annotation.RestController;

import com.kang.spring_webflux.entity.Customer;
import com.kang.spring_webflux.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@RestController
@RequestMapping("/customers")
public class CustomerController {

    /** CustomerRepository */
    private final CustomerRepository customerRepository;

    /** sink */
    private final Sinks.Many<Customer> sink;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        // multicast: 새로 Push된 데이터만 구독자에게 전해주는 방식
        // replay : 지정된 크기를 돌려주는 방식
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
    }

    /**
     * find All
     * @param param
     * @return Flux<Customer>
     */
    @GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Customer> getCustomers() {
        /**
         * 결과
         * onSubscribe(FluxUsingWhen.UsingWhenSubscriber)
         * request(1)
         * onNext(Customer(id=1, firstName=Jack, lastName=Bauer))
         * request(127)
         * onNext(Customer(id=2, firstName=Chloe, lastName=O'Brian))
         * onNext(Customer(id=3, firstName=Kim, lastName=Bauer))
         * onNext(Customer(id=4, firstName=David, lastName=Palmer))
         * onNext(Customer(id=5, firstName=Michelle, lastName=Dessler))
         * onComplete()
         */
        // JPA의 메소드가 똑같음
        return customerRepository.findAll().delayElements(Duration.ofSeconds(1)).log();
    }

    /**
     * Mode타입을 사용해서 데이터를 전달해 준다.
     * 
     * @param id
     * @return Mono<Customer>
     */
    @GetMapping("/{id}")
    public Mono<Customer> getCustomer(@PathVariable("id") Long id) {
        // 분할해서 데이터를 받지 않고 한 번에 데이터를 받는다
        log.info("Get Customer");
        return customerRepository.findById(id).log();
    }

    /**
     * 연결을 끊지 않고 계속 데이터를 받을 수 있도록 함 TEXT_EVENT_STREAM_VALUE
     * 
     * @return Flux<ServerSentEvent<Customer>>
     */
    @GetMapping(value = "/sse") // , produces = MediaType.TEXT_EVENT_STREAM_VALUE 생략 가능
    public Flux<ServerSentEvent<Customer>> getCustomerSse() {
        // sink에 있는 데이터를 확인을 하고 합쳐 지면 응답을 해준다.
        return sink.asFlux().map(c -> ServerSentEvent.builder(c).build()).doOnCancel(() -> {

            // 마지막 데이터가 들어왔을 때, onComplete를 실행
            sink.asFlux().blockLast();

        });
    }

    /**
     * 데이터 저장
     * 
     * @param customer
     * @return Mono<Customer>
     */
    @PostMapping
    public Mono<Customer> postCustomer(Customer customer) {
        // doOnNext save다음 처리
        return customerRepository.save(new Customer("in", "kang")).doOnNext(c -> {
            // 새로운 데이터를 sink에 적용
            sink.tryEmitNext(c);
        });
    }
    
    @GetMapping("/flux")
    public Flux<Integer> getFlux() {
        /**
         * Flux.just 데이터를 순차적으로 꺼내서 던져주는 것
         * 
         * 결과 5초가 지나서 응답을 함
         * request(1)
         * onNext(1)
         * request(127)
         * onNext(2)
         * onNext(3)
         * onNext(4)
         * onNext(5)
         * onComplete()
         */
        return Flux.just(1, 2, 3, 4, 5).delayElements(Duration.ofSeconds(1)).log();
    }

    @GetMapping(value = "/fluxstream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> getFluxStream() {
        // APPLICATION_STREAM_JSON_VALUE을 사용하면 데이터를 받아 올 때마다 바로 전달해 줌
        return Flux.just(1, 2, 3, 4, 5).delayElements(Duration.ofSeconds(1)).log();
    }

}