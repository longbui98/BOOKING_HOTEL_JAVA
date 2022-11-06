package service;

import model.Customer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * @author bqlong
 */

public final class CustomerService {
    private static CustomerService instance;
    private CustomerService(){}

    public static CustomerService getInstance() {
        if(instance == null) {
            instance = new CustomerService();
        }
        return instance;
    }
    private final ArrayList<Customer> customers = new ArrayList<>();
    public void addCustomer(String email, String firstName, String lastName){
        Customer customer = new Customer(firstName, lastName, email);
        Optional<Customer> first = customers.stream().filter(x -> x.getEmail().equalsIgnoreCase(email)).findFirst();
        if (first.isPresent()) {
            System.out.println("THIS ACCOUNT HAS ALREADY EXITS");
            return;
        }
        System.out.println("CREATE ACCOUNT SUCCESSFULLY");
        customers.add(customer);
    }

    public Customer getCustomer(String customerEmail) {
        Optional<Customer> result = Optional.empty();
        for (var customer : customers) {
            if (customerEmail.equalsIgnoreCase(customer.getEmail())) {
                result = Optional.of(customer);
                break;
            }
        }
        return result.orElse(null);
    }

    public Collection<Customer> getAllCustomers(){
        return customers;
    }
}
