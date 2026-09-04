package com.rookies6.myspringboot4project.repository;

import com.rookies6.myspringboot4project.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
//@Transactional
class CustomerReppositoryTest {
    @Autowired
    CustomerReppository customerReppository;
    //1. Customer 등록
    @Test
    @Rollback(value = false) //Rollback 처리 x, 직접 데이터 확인해야함
    void testCreate(){
        //Given(준비;단계)
        Customer customer = new Customer();
        customer.setCustomerId("D002");
        customer.setCustomerName("길동3");
        //When(실행단계)
        Customer addCustomer = customerReppository.save(customer);
        //Then(검증단계)
        assertThat(addCustomer).isNotNull();
        assertThat(addCustomer.getCustomerName()).isEqualTo("길동3");

    }

    //2. Customer 조회
    @Test
    void testFindby(){
        Optional<Customer> optionalCustomer = customerReppository.findById(1L);
        if(optionalCustomer.isPresent()){
            Customer existCustomer = optionalCustomer.get();
            assertThat(existCustomer.getId()).isEqualTo(1L);
        }
        //ifPresent(Consumer) Consumer의 추상메서드가 void accept(T t)
        optionalCustomer.ifPresent(customer -> System.out.println(customer.getCustomerName()));
    }

    @Test
    void testFindByNotFound(){
        //Optional의 orElseGet(Supplier) Supplier의 추상메서드 T get() () -> T
        //orElseThrow(Suppier) 사용X get()    () -> X ==>X extends Throwable
        Customer notFoundCustomer = customerReppository.findByCustomerId("B001") //Optional<Customer>
                .orElseGet(()->new Customer() );
        //assertThat(notFoundCustomer.getCustomerId()).isEqualTo("A004");
        assertThat(notFoundCustomer.getCustomerId()).isNull();

        Customer notFound = customerReppository.findById(3L)
                .orElseThrow(() -> new RuntimeException("Customer Not Found"));
    }
    @Test
    //@Rollback(value = false)
    void testUpdate(){
        Customer customer = customerReppository.findByCustomerId("A004")
                .orElseGet(()-> new Customer());
        //Setter 호출 EntityManager가 Dirty Checking을 함
        customer.setCustomerName("박둘리2");
        Customer updatedCustomer = customerReppository.save(customer);
        assertThat(customer.getCustomerName()).isEqualTo("박둘리2");
    }
}