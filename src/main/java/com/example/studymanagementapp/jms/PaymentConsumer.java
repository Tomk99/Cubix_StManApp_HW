package com.example.studymanagementapp.jms;

import com.example.studymanagementapp.service.StudentService;
import com.example.studymanagementapp.smafinance.dto.PaymentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentConsumer {

    private final StudentService studentService;

    @JmsListener(destination = "payments", containerFactory = "myFactory")
    public void onPaymentMessage(PaymentDto paymentDto) {

        studentService.updateBalance(paymentDto.getStudentId(), paymentDto.getAmount());
    }
}
