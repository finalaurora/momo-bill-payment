package org.example.payment;

import org.example.payment.model.Bill;
import org.example.payment.model.BillState;
import org.example.payment.model.BillType;
import org.example.payment.model.Customer;
import org.example.payment.utils.DateTimeUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class BillPaymentApp {

    static Customer customer = new Customer("123", 1000000);

    static {
        customer = new Customer("123", 1000000);
        try {
            customer.addBill(new Bill(1, BillType.ELECTRIC, 200000, DateTimeUtils.parseDate("15/11/2023"), BillState.NOT_PAID, "EVN"));
            customer.addBill(new Bill(2, BillType.WATER, 175000, DateTimeUtils.parseDate("16/11/2023"), BillState.NOT_PAID, "SAWACO"));
            customer.addBill(new Bill(3, BillType.INTERNET, 800000, DateTimeUtils.parseDate("21/11/2023"), BillState.NOT_PAID, "VNPT"));
        }catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter command along with parameters (CASH_IN, LIST_BILL, PAY, DUE_DATE, SCHEDULE, LIST_PAYMENT, SEARCH_BILL_BY_PROVIDER, EXIT):");
            String command = scanner.nextLine();
            if ("EXIT".equals(command)) {
                System.out.println("Good bye!");
                System.exit(0);
                break;
            }

            try {
                processCommand(scanner, command);
            } catch (ParseException e) {
                System.out.println("Parse error, Try again!");
            }
        }
    }

    public static void processCommand(Scanner scanner, String command) throws ParseException {
        String[] commandParams = command.split(" ");
        switch (commandParams[0]) {
            case "CASH_IN":
                if (commandParams.length == 1) {
                    System.out.println("Missing params. Please try again.");
                } else {
                    int cashInAmount = Integer.parseInt(commandParams[1]);
                    customer.addFunds(cashInAmount);
                }
                break;

            case "LIST_BILL":
                customer.listBills();
                break;

            case "PAY":
                if (commandParams.length < 2) {
                    System.out.println("Missing params. Please try again.");
                    break;
                } else {
                    List<Integer> billIds = new ArrayList<>();
                    for (int i = 1; i < commandParams.length; i++) {
                        billIds.add(Integer.parseInt(commandParams[i]));
                    }
                    customer.payMultipleBills(billIds);
                }
                break;

            case "DUE_DATE":
                customer.dueDate();
                break;

            case "SCHEDULE":
                if (commandParams.length != 3) {
                    System.out.println("Missing params. Please try again.");
                    break;
                }
                int scheduleBillId = Integer.parseInt(commandParams[1]);
                String scheduledDateStr = commandParams[2];
                Date scheduledDate = DateTimeUtils.parseDate(scheduledDateStr);
                customer.schedulePayment(scheduleBillId, scheduledDate);
                break;

            case "LIST_PAYMENT":
                customer.listPayments();
                break;

            case "SEARCH_BILL_BY_PROVIDER":
                if (commandParams.length != 2) {
                    System.out.println("Missing params. Please try again.");
                    break;
                }
                String provider = commandParams[1];
                customer.searchBillByProvider(provider);
                break;

            case "EXIT":
                System.exit(0);
                break;
            default:
                System.out.println("Invalid command. Please try again.");
                break;
        }
    }
}