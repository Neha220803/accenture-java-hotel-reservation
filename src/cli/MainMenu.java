package cli;

import api.AdminResource;
import api.HotelResource;
import model.Customer;
import model.IRoom;
import model.Reservation;
import model.Room;
import model.RoomType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainMenu {
    private static final Scanner scanner = new Scanner(System.in);
    private static final HotelResource hotelResource = HotelResource.getInstance();

    public static void main(String[] args) {
        // Preload sample rooms
        preloadRooms();

        boolean running = true;
        while (running) {
            printMainMenu();
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    findAndReserveRoom();
                    break;
                case "2":
                    seeMyReservations();
                    break;
                case "3":
                    createAccount();
                    break;
                case "4":
                    AdminMenu.displayAdminMenu();
                    break;
                case "5":
                    System.out.println("Exiting... Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Please select a valid option (1–5).");
            }
        }
    }

    private static void printMainMenu() {
        System.out.println("\n=== Main Menu ===");
        System.out.println("1. Find and reserve a room");
        System.out.println("2. See my reservations");
        System.out.println("3. Create an account");
        System.out.println("4. Admin");
        System.out.println("5. Exit");
        System.out.print("Please select an option: ");
    }

    private static void findAndReserveRoom() {
        try {
            System.out.print("Enter your email (format: name@domain.com): ");
            String email = scanner.nextLine();

            Customer customer = hotelResource.getCustomer(email);
            if (customer == null) {
                System.out.println("No customer found. Please create an account first.");
                return;
            }

            System.out.print("Enter check-in date (MM/dd/yyyy): ");
            Date checkIn = new SimpleDateFormat("MM/dd/yyyy").parse(scanner.nextLine());

            System.out.print("Enter check-out date (MM/dd/yyyy): ");
            Date checkOut = new SimpleDateFormat("MM/dd/yyyy").parse(scanner.nextLine());

            Collection<IRoom> availableRooms = hotelResource.findARoom(checkIn, checkOut);

            if (availableRooms.isEmpty()) {
                System.out.println("No rooms available for the selected dates.");
                return;
            }

            System.out.println("Available rooms:");
            for (IRoom room : availableRooms) {
                System.out.println(room);
            }

            System.out.print("Enter the room number you want to reserve: ");
            String roomNumber = scanner.nextLine();
            IRoom room = hotelResource.getRoom(roomNumber);

            if (room == null) {
                System.out.println("Invalid room number.");
                return;
            }

            Reservation reservation = hotelResource.bookARoom(email, room, checkIn, checkOut);
            System.out.println("Room reserved successfully: \n" + reservation);

        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use MM/dd/yyyy.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void seeMyReservations() {
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        Collection<Reservation> reservations = hotelResource.getCustomersReservations(email);

        if (reservations == null || reservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            for (Reservation res : reservations) {
                System.out.println(res);
            }
        }
    }

    private static void createAccount() {
        try {
            System.out.print("Enter email: ");
            String email = scanner.nextLine();

            System.out.print("Enter first name: ");
            String firstName = scanner.nextLine();

            System.out.print("Enter last name: ");
            String lastName = scanner.nextLine();

            hotelResource.createACustomer(email, firstName, lastName);
            System.out.println("Account created successfully!");

        } catch (IllegalArgumentException e) {
            System.out.println("Error creating account: " + e.getMessage());
        }
    }

    private static void preloadRooms() {
        List<IRoom> sampleRooms = new ArrayList<>();
        sampleRooms.add(new Room("101", 500.0, RoomType.SINGLE));
        sampleRooms.add(new Room("102", 750.0, RoomType.DOUBLE));
        sampleRooms.add(new Room("103", 900.0, RoomType.DOUBLE));

        AdminResource.getInstance().addRoom(sampleRooms);
    }
}
