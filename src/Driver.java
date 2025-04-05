import model.Customer;

public class Driver {
    public static void main(String[] args) {
        // Valid Test Case
        try {
            Customer validCustomer = new Customer("First", "Second", "j@domain.com");
            System.out.println("Valid Customer: " + validCustomer);
        } catch (IllegalArgumentException e) {
            System.out.println("Error for valid customer: " + e.getMessage());
        }

        // Invalid Test Case Trying
        try {
            Customer invalidCustomer = new Customer("First", "Second", "email");
            System.out.println("Trying Invalid Customer Details: " + invalidCustomer);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected exception for invalid email: " + e.getMessage());
        }
    }
}