package org.example.payment.model;

import org.example.payment.exception.InvalidOperationException;
import org.example.payment.utils.DateTimeUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Customer {
    private final String customerId;

    public String getCustomerId() {
        return customerId;
    }

    public int getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(int accountBalance) {
        this.accountBalance = accountBalance;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public Map<Integer, Date> getScheduledPayments() {
        return scheduledPayments;
    }

    public void setScheduledPayments(Map<Integer, Date> scheduledPayments) {
        this.scheduledPayments = scheduledPayments;
    }

    private int accountBalance;

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    private List<Bill> bills =  new ArrayList<>();;
    private List<Payment> payments = new ArrayList<>();
    private Map<Integer, Date> scheduledPayments =  new HashMap<>();

    public Customer(String customerId, int initialBalance) {
        this.customerId = customerId;
        this.accountBalance = initialBalance;
    }

    public void addFunds(double amount) {
        accountBalance += amount;
        System.out.println("Funds added successfully. Current balance: " + accountBalance);
    }

    private Bill findBillById(int billId) {
        return bills.stream().filter(bill -> bill.getBillId() == billId).findFirst().orElse(null);
    }

    public void addBill(Bill newBill) {
        Bill existBill = findBillById(newBill.getBillId());
        if (existBill == null) {
            bills.add(newBill);
        } else {
            System.out.println("Add new bill failed, Bill Id exists");
        }
    }

    public void updateBill(Bill newBill) throws InvalidOperationException {
        Bill existBill = findBillById(newBill.getBillId());
        if (existBill == null) {
            System.out.println("No bill Id matched provided bill for updating");
            throw new InvalidOperationException("No bill Id matched provided bill for updating");
        } else {
            bills.remove(existBill);
            bills.add(newBill);
        }
    }

    public void deleteBillById(int billId) {
        bills.removeIf(bill -> billId == bill.getBillId());
        System.out.println("Bill deleted successfully.");
    }

    public void listBills() {
        if(bills == null || bills.isEmpty()){
            System.out.println("This customers has no bills.");
            return;
        }
        printHeaderBill();
        for (Bill bill : bills) {
            System.out.println(bill.getBillId() + "\t" + bill.getType().name() + "\t" + bill.getAmount() + "\t" + DateTimeUtils.formatDate(bill.getDueDate()) + "\t" + bill.getState() + "\t" + bill.getProvider());
        }
    }

    public void payBill(int billId) {
        Bill billToPay = findBillById(billId);

        if (billToPay != null) {
            if (accountBalance >= billToPay.getAmount()) {
                accountBalance -= billToPay.getAmount();
                billToPay.setState(BillState.PAID);
                payments.add(new Payment(billToPay.getAmount(), new Date(), PaymentState.PROCESSED, billId));
                System.out.println("Payment has been completed for Bill with id " + billId +
                        ". Your current balance is: " + accountBalance);
            } else {
                System.out.println("Sorry! Not enough funds to proceed with payment.");
            }
        } else {
            System.out.println("Sorry! Not found a bill with such id");
        }
    }

    public void payBill(Bill bill) {
        if(bill.getState() == BillState.PAID){
            System.out.println("This bill has been paid before.");
            return;
        }
        if (accountBalance >= bill.getAmount()) {
            accountBalance -= bill.getAmount();
            bill.setState(BillState.PAID);
            payments.add(new Payment(bill.getAmount(), new Date(), PaymentState.PROCESSED, bill.getBillId()));
            System.out.println("Payment has been completed for Bill with id " + bill.getBillId() +
                    ". Your current balance is: " + accountBalance);
        } else {
            payments.add(new Payment(bill.getAmount(), new Date(), PaymentState.PENDING, bill.getBillId()));
            System.out.println("Sorry! Not enough funds to proceed with payment.");
        }
    }

    public void payMultipleBills(List<Integer> billsId) {
        List<Bill> billsToPay = new ArrayList<>();
        for (Integer billId:billsId) {
           Bill bill =  findBillById(billId);
           if (bill == null) {
               System.out.println("Bill ID not exists! Bill ID = "+billId);
               System.out.println("Payments processing cancelled.");
               return;
           }
           billsToPay.add(bill);
        }
        List<Bill> sortedBills = billsToPay.stream()
                .sorted(Comparator.comparing(Bill::getDueDate))
                .collect(Collectors.toList());

        int amountToPay = sortedBills.stream().mapToInt(Bill::getAmount).sum();

        if(amountToPay > accountBalance){
            System.out.println("Insufficient funds to pay all bills.");
            return;
        }
        for (Bill bill : sortedBills) {
            payBill(bill);
        }
        System.out.println("Bill payments processed complete.");
    }

    public void schedulePayment(int billId, Date scheduledDate) {
        Bill billToSchedule = findBillById(billId);
        if (billToSchedule != null) {
            scheduledPayments.put(billId, scheduledDate);
            System.out.println("Payment for bill id " + billId + " is scheduled on " + DateTimeUtils.formatDate(scheduledDate));
        } else {
            System.out.println("Sorry! Not found a bill with such id");
        }
    }

    public void listPayments() {
        System.out.println("No.\tAmount\tPayment\tDate\tState\tBillId");
        for (Payment payment : payments) {
            System.out.println(payments.indexOf(payment) + 1 + "\t" + payment.getAmount() + "\t" +
                    DateTimeUtils.formatDate(payment.getPaymentDate())+ "\t" +
                    payment.getPaymentState() + "\t" + payment.getBillId());
        }
    }

    public void dueDate() {
        printHeaderBill();
        for (Bill bill : bills) {
            if (BillState.NOT_PAID.equals(bill.getState())) {
                System.out.println(bill.getBillId() + ". " + bill.getType() + " " + bill.getAmount() + " " +
                        new SimpleDateFormat("dd/MM/yyyy").format(bill.getDueDate()) + " " + bill.getState() + " " + bill.getProvider());
            }
        }
    }

    public void searchBillByProvider(String provider) {
        printHeaderBill();
        for (Bill bill : bills) {
            if (provider.equals(bill.getProvider())) {
                System.out.println(bill.getBillId() + ". " + bill.getType() + " " + bill.getAmount() + " " +DateTimeUtils.formatDate(bill.getDueDate()) + " " + bill.getState() + " " + bill.getProvider());
            }
        }
    }

    private void printHeaderBill(){
        System.out.println("BillNo.\tType\tAmount\tDueDate\tState\tPROVIDER");
    }

    private void printHeaderPayments(){
        System.out.println("No.\tAmount\tPayment\tDate\tState\tBillId");
    }
}
