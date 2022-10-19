package guru.springframework.msscssm.services;

import guru.springframework.msscssm.domain.Payment;
import guru.springframework.msscssm.domain.PaymentState;
import guru.springframework.msscssm.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaymentServiceImplTest {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private PaymentRepository paymentRepository;

    private Payment payment;

    @BeforeEach
    void setUp() {
        payment = Payment.builder()
                    .amount(new BigDecimal("2.99"))
                    .build();
    }

    @Transactional
    @Test
    //@RepeatedTest(5)
    void preAuth() {
        Payment savedPayment = paymentService.newPayment(payment);

        paymentService.preAuth(savedPayment.getId());

        Payment preAuthPayment = paymentRepository.getById(savedPayment.getId());

        System.out.println(preAuthPayment);

        if (PaymentState.PRE_AUTH.equals(preAuthPayment.getState())) {
            paymentService.authorizePayment(preAuthPayment.getId());
        }

        Payment authPayment = paymentRepository.getById(savedPayment.getId());

        System.out.println(authPayment);
    }
}