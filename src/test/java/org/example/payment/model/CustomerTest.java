package org.example.payment.model;

import org.example.payment.exception.InvalidOperationException;
import org.example.payment.utils.DateTimeUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    Customer customer;

    @BeforeEach
    void setUp() throws ParseException, InvalidOperationException {
        customer = new Customer("123", 1000000);
        customer.addBill(new Bill(1,BillType.ELECTRIC, 200000, DateTimeUtils.parseDate("15/11/2023"), BillState.NOT_PAID, "EVN"));
        customer.addBill(new Bill(2,BillType.WATER, 175000, DateTimeUtils.parseDate("16/11/2023"), BillState.NOT_PAID, "SAWACO"));
        customer.addBill(new Bill(3,BillType.INTERNET, 800000, DateTimeUtils.parseDate("21/11/2023"), BillState.NOT_PAID, "VNPT"));
    }

    @AfterEach
    void tearDown() {
        customer = new Customer("123", 1000000);
    }

    @Test
    void addFunds() {
        customer.addFunds(100000);
        assertEquals(1100000, customer.getAccountBalance());
    }

    @Test
    void addBillOk() throws ParseException {
        customer.addBill(new Bill(4,BillType.INTERNET, 1000000, DateTimeUtils.parseDate("21/11/2023"), BillState.NOT_PAID, "VIETTEL"));
        Assertions.assertEquals(4, customer.getBills().size() );
    }

    @Test
    void addBillFail() throws ParseException {
        customer.addBill(new Bill(1,BillType.ELECTRIC, 200000, DateTimeUtils.parseDate("15/11/2023"), BillState.NOT_PAID, "EVN"));
        Assertions.assertNotEquals(4, customer.getBills().size());
    }

    @Test
    void updateBillOk() throws ParseException {
        assertDoesNotThrow(() -> {
            customer.updateBill(new Bill(3,BillType.ELECTRIC, 300000, DateTimeUtils.parseDate("15/11/2023"), BillState.NOT_PAID, "EVN"));
        });
    }

    @Test
    void updateBillFailed() throws ParseException {
        Exception exception = assertThrows(InvalidOperationException.class, () -> {
            customer.updateBill(new Bill(4,BillType.ELECTRIC, 300000, DateTimeUtils.parseDate("15/11/2023"), BillState.NOT_PAID, "EVN"));
        });
    }
    @Test
    void deleteBillById() {
        assertDoesNotThrow(() -> {
            customer.deleteBillById(3);
        });
        assertEquals(2, customer.getBills().size());
    }

    @Test
    void listBills() {
        assertDoesNotThrow(() -> {
            customer.listBills();
        });
        assertEquals(3, customer.getBills().size());
    }

    @Test
    void payBill() {
        customer.payBill(1);
        assertEquals(800000, customer.getAccountBalance());
        assertEquals(1, customer.getPayments().size());
    }

    @Test
    void payMultipleBillsOk() {
        customer.payMultipleBills(Arrays.asList(1,2));
        assertEquals(625000, customer.getAccountBalance());
        assertEquals(2, customer.getPayments().size());
    }

    @Test
    void payMultipleBillsNotSufficientFunds() {
        customer.payMultipleBills(Arrays.asList(1,2,3));
        assertEquals(1000000, customer.getAccountBalance());
        assertEquals(0, customer.getPayments().size());
    }

    @Test
    void payMultipleBillsFailedNotExist() {
        customer.payMultipleBills(Arrays.asList(1,4));
        assertNotEquals(800000, customer.getAccountBalance());
        assertNotEquals(1, customer.getPayments().size());
    }


    @Test
    void schedulePayment() throws ParseException {
        customer.schedulePayment(3, DateTimeUtils.parseDate("21/11/2023") );
        assertEquals(1, customer.getScheduledPayments().size());
    }

    @Test
    void listPayments() {
        customer.payBill(1);
        customer.listPayments();
        assertEquals(1, customer.getPayments().size());
    }

    @Test
    void dueDate() throws ParseException {
        customer.addBill(new Bill(4,BillType.ELECTRIC, 300000, DateTimeUtils.parseDate("15/11/2023"), BillState.PAID, "EVN"));
        assertDoesNotThrow(() -> {
            customer.dueDate();
        });
    }

    @Test
    void searchBillByProvider() {
        assertDoesNotThrow(() -> {
            customer.searchBillByProvider("VNPT");
        });
    }
}