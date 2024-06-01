package com.ctu.roommanagementportal.infrastracture;

import com.ctu.roommanagementportal.abstraction.Room;
import com.ctu.roommanagementportal.dbservices.SelectFromDB;
import com.ctu.roommanagementportal.model.RoomType;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * This class allows users to search for room records in the database.
 * It provides methods for searching rooms by name, type, or equipment.
 */

public class SearchRoomRecord {

    // Scanner object for reading user input
    private static final Scanner scanner = new Scanner(System.in);

    //flag to control the loop for searching room record
    private static boolean exit = false;


    /**
     * Method to execute room searching functionality.
     *
     * @throws SQLException if there is an error querying the database
     */
    public boolean execute() throws SQLException {
        // Loop to keep the program running until the user chooses to exit
        while (!exit) {
            System.out.println("---------------------");
            System.out.println("Search for rooms by:");
            System.out.println("---------------------");
            System.out.println("1. Room Name");
            System.out.println("2. Room Type");
            System.out.println("3. Equipments");
            System.out.print("\n Enter your choice: ");

            // Validates the input for search type
            int searchType = InputValidator.validateIntegerInput(scanner);

            try {
                List<? extends Room> foundRooms;

                if (searchType == 1) {
                    foundRooms = searchByRoomName(scanner);
                } else if (searchType == 2) {
                    foundRooms = searchByRoomType(scanner);
                } else if (searchType == 3) {
                    foundRooms = searchByEquipment(scanner);
                } else {
                    System.out.println("Invalid choice. Please select a valid option.");
                    continue; // Prompt user again for input
                }

                // Display search results in a formatted table
                if (!foundRooms.isEmpty()) {
                    System.out.println("\nSearching room records...");

                    System.out.println("\n---------------------------------------------------" +
                            "-------------------------------------------------------------------" +
                            "----------------------------------------------------------------------" +
                            "-----------------------------------");
                    System.out.printf("| %-20s | %-20s | %-10s | %-10s | %-20s | %-20s | %-10s | %-18s | %-10s | %-18s | %-15s | %-2s | %-15s |%n",
                            "Room Name", "Room Type", "Capacity", "Room Status", "Building Location", "Maintenance Notes", "Projector", "Number of Chairs",
                            "White Board", "Number of Computers", "Number of Desks", "TV", "Internet Access");
                    System.out.println("------------------------------------------------------" +
                            "--------------------------------------------------------------------------" +
                            "--------------------------------------------------------------------------" +
                            "-----------------------");
                    // Format output based on the type of room
                    for (Room room : foundRooms) {
                        if (room instanceof RoomType.CompLaboratory) {
                            RoomType.CompLaboratory compLabRoom = (RoomType.CompLaboratory) room;
                            System.out.printf("| %-20s | %-20s | %-10d | %-10s | %-20s | %-20s | %-10s | %-18s | %-10s | %-18s | %-15s | %-2s | %-15s |%n",
                                    room.getRoomName(), room.getRoomType(), room.getCapacity(), room.getRoomStatus(),
                                    room.getBuildingLocation(), room.getMaintenanceNotes(), room.isHasProjector() ? "Yes" : "No",
                                    room.getNumOfChairs(), "N/A", compLabRoom.getNumOfComputers(),
                                    "N/A", "N/A", "N/A");
                        } else if (room instanceof RoomType.Library) {
                            RoomType.Library libraryRoom = (RoomType.Library) room;
                            System.out.printf("| %-20s | %-20s | %-10d | %-10s | %-20s | %-20s | %-10s | %-18s | %-10s | %-18s | %-15s | %-2s | %-2s |%n",
                                    room.getRoomName(), room.getRoomType(), room.getCapacity(), room.getRoomStatus(),
                                    room.getBuildingLocation(), room.getMaintenanceNotes(), room.isHasProjector() ? "Yes" : "No",
                                    room.getNumOfChairs(), "N/A", "N/A",
                                    libraryRoom.getNumOfDesks(), "N/A", "N/A");
                        } else if (room instanceof RoomType.Classroom) {
                            RoomType.Classroom classroomRoom = (RoomType.Classroom) room;
                            System.out.printf("| %-20s | %-20s | %-10d | %-10s | %-20s | %-20s | %-10s | %-18s | %-10s | %-18s | %-15s | %-2s | %-15s |%n",
                                    room.getRoomName(), room.getRoomType(), room.getCapacity(), room.getRoomStatus(),
                                    room.getBuildingLocation(), room.getMaintenanceNotes(), room.isHasProjector() ? "Yes" : "No",
                                    room.getNumOfChairs(), classroomRoom.isWhiteboard() ? "Yes" : "No", "N/A",
                                    "N/A", "N/A", "N/A");
                        } else if (room instanceof RoomType.Smartroom) {
                            RoomType.Smartroom smartRoom = (RoomType.Smartroom) room;
                            System.out.printf("| %-20s | %-20s | %-10d | %-10s | %-20s | %-20s | %-10s | %-18s | %-10s | %-18s | %-15s | %-2s | %-15s |%n",
                                    room.getRoomName(), room.getRoomType(), room.getCapacity(), room.getRoomStatus(),
                                    room.getBuildingLocation(), room.getMaintenanceNotes(), room.isHasProjector() ? "Yes" : "No",
                                    room.getNumOfChairs(), "N/A", "N/A",
                                    "N/A", smartRoom.isTv() ? "Yes" : "No", smartRoom.isInternetAccess() ? "Yes" : "No");
                        } else {
                            // Handle other room types if needed
                            System.out.println("No rooms found.");
                            // Exit the loop if no rooms are found
                            exit = true;
                        }

                    }

                    System.out.println("---------------------------------------------------------------" +
                            "---------------------------------------------------------------------------" +
                            "-------------------------------------------------------------------------" +
                            "--------------");

                } else {

                    System.out.println("No rooms found matching the criteria.");
                }


            } catch (SQLException e) {

                System.err.println("Error during room search: " + e.getMessage());

            }
            // Ask user if they want to perform another search
            promptForAnotherSearch(scanner);
        }
        // Return whether to continue running the program
        return !exit;
    }

    /**
     * Prompts the user to search for another room or exit the program.
     *
     * @param scanner Scanner object for reading user input
     */
    public static void promptForAnotherSearch(Scanner scanner) {
        boolean validInput = false;

        // Loop to ensure valid input is received
        while (!validInput) {
            System.out.print("\nDo you want to search for another room? (yes/no): ");
            String choice = scanner.nextLine().toLowerCase().trim();
            if (choice.equals("yes")) {
                // Exit the loop and continue searching
                validInput = true;
            } else if (choice.equals("no")) {
                System.out.println("\nThank you for using the room record creation system. Goodbye!");
                validInput = true;
                // Set flag to true to exit the main loop
                exit = true;
            } else {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            }
        }
    }
    /**
     * Searches for rooms by room name.
     *
     * @param scanner Scanner object for reading user input
     * @return List of rooms matching the search criteria
     * @throws SQLException if there is an error querying the database
     */

    private static List<? extends Room> searchByRoomName(Scanner scanner) throws SQLException {
        System.out.print("\nEnter room name:");
        String roomName = InputValidator.validateNonEmptyStringInput(scanner);
        return SelectFromDB.getRoomsByName(roomName);
    }

    /**
     * Searches for rooms by room type.
     *
     * @param scanner Scanner object for reading user input
     * @return List of rooms matching the search criteria
     * @throws SQLException if there is an error querying the database
     */

    private static List<? extends Room> searchByRoomType(Scanner scanner) throws SQLException {
        List<? extends Room> foundRooms = null;
        boolean exit = false;
        // Loop to ensure valid room type input is received
        while (!exit) {
            System.out.println("--------------------");
            System.out.println("Room types to search:");
            System.out.println("--------------------");
            System.out.println("C - Classroom");
            System.out.println("CL - CompLab");
            System.out.println("L - Library");
            System.out.println("S - Smart room");
            System.out.print("\n Enter your choice:");
            String roomType = InputValidator.validateNonEmptyStringInput(scanner);

            switch (roomType.toUpperCase()) {
                case "C":
                    foundRooms = SelectFromDB.getRoomsByType("Classroom");
                    exit = true;
                    break;
                case "CL":
                    foundRooms = SelectFromDB.getCompLaboratoriesByType("CompLaboratory");
                    exit = true;
                    break;
                case "L":
                    foundRooms = SelectFromDB.getLibrariesByType("Library");
                    exit = true;
                    break;
                case "S":
                    foundRooms = SelectFromDB.getSmartroomsByType("Smart Room");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid room type. Please try again.");
            }
        }
        return foundRooms;
    }
    /**
     * Searches for rooms by equipment.
     *
     * @param scanner Scanner object for reading user input
     * @return List of rooms matching the search criteria
     * @throws SQLException if there is an error querying the database
     */

    private static List<? extends Room> searchByEquipment(Scanner scanner) throws SQLException {
        List<? extends Room> foundRooms = null;
        boolean exit = false;
        while (!exit) {
            System.out.println("----------------------");
            System.out.println("Equipments to search: ");
            System.out.println("----------------------");
            System.out.println("P - Projector");
            System.out.println("C - Chairs");
            System.out.println("W - Whiteboard");
            System.out.println("M - Computers");
            System.out.println("D - Desks");
            System.out.println("T - TV");
            System.out.println("I - Internet");
            System.out.print("\n Enter your choice: ");
            String equipment = scanner.nextLine().toUpperCase().trim();

            // Handle different types of equipments
            switch (equipment) {
                case "P":
                    foundRooms = SelectFromDB.getRoomsWithProjector(true);
                    exit = true;
                    break;
                case "C":
                    foundRooms = SelectFromDB.getRoomsWithChairs();
                    exit = true;
                    break;
                case "W":
                    foundRooms = SelectFromDB.getRoomsByWhiteboard(true);
                    exit = true;
                    break;
                case "M":
                    foundRooms = SelectFromDB.getRoomsByComputers();
                    exit = true;
                    break;
                case "D":
                    foundRooms = SelectFromDB.getRoomsByDesks();
                    exit = true;
                    break;
                case "T":
                    foundRooms = SelectFromDB.getRoomsByTV(true);
                    exit = true;
                    break;
                case "I":
                    foundRooms = SelectFromDB.getRoomsByInternetAccess(true);
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid equipment choice. Please try again.");
            }
        }
        return foundRooms;
    }
}


