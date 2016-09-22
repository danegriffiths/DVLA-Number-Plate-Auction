import java.sql.*;
import java.util.Scanner;

public class Main_Class {

    private Main_Class() throws SQLException {

        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Number_Plate_Auction_Database", "postgres", "Bouncer");

            System.out.println("Opened database successfully");
        } catch (Exception ex) {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }

        Scanner scan = new Scanner(System.in);
        Menu menuItem = new Menu("Media Library.", scan);
        menuItem.addOption("Display all the registrations available.");
        menuItem.addOption("Add a registration and guide price.");
        menuItem.addOption("Delete a registration.");
        menuItem.addOption("Quit the auction database");

        boolean done;
        do {
            done = false;
            int menuSelection = menuItem.executeMenu();
            System.out.println();

            switch (menuSelection) {
                case 1:
                    showAllPlatesWithPrice(conn);
                    break;
                case 2:
                    addNumberPlateToAuction(conn);
                    break;

                case 3:
                    deleteNumberPlateFromAuction(conn);
                    break;
                case 4:
                    System.out.println("Thank you for using the number plate auction database.");
                    done = true;
                    break;
            }

            // Print a blank line to separate the next menu.
            System.out.println();

        } while (!done);
        conn.close();
    }

    private void showAllPlatesWithPrice(Connection conn) throws SQLException {

        Statement stmt = conn.createStatement();
        String sql = "SELECT " +
                "registration AS \"Reg Number\", guide_price AS \"Guide Price\", date_time_column AS \"Time\" " +
                "FROM " +
                "number_plate_auction " +
                "LEFT JOIN " +
                "categories ON number_plate_auction.category_id = categories.category_id " +
                "LEFT JOIN " +
                "date_time ON categories.date_time_id = date_time.date_time_id1" +
                " ORDER BY" +
                " number_plate_auction.category ASC;";

        ResultSet rs = stmt.executeQuery(sql);
        ResultSetMetaData rsmd = rs.getMetaData();

        System.out.printf("%s\t\t\t %s\t\t\t %s\n",rsmd.getColumnName(1), rsmd.getColumnName(2), rsmd.getColumnName(3));
        while (rs.next()) {
            for (int i = 1; i <= 3; i++) {
                String columnValue = rs.getString(i);
                System.out.printf("%s\t\t\t ",columnValue);
            }
            System.out.println("");
        }
        stmt.close();
    }

    private void addNumberPlateToAuction(Connection c) throws SQLException {

        Scanner scan = new Scanner(System.in);

        System.out.println("Please enter the number plate category e.g. A for A12 XYZ.");
        String category = scan.nextLine();
        System.out.println("Please enter the number plate.");
        String registration = scan.nextLine();
        System.out.println("Please enter the guide price.");
        int price = scan.nextInt();

        PreparedStatement ps = c.prepareCall("INSERT INTO number_plate_auction " +
                "(category, registration, guide_price) " +
                "VALUES " +
                "(?,?,?);" +
                "UPDATE number_plate_auction " +
                "SET category_id = categories.category_id " +
                "FROM categories " +
                "WHERE " +
                "number_plate_auction.category = categories.category;");

        ps.setString(1,category);
        ps.setString(2,registration);
        ps.setInt(3,price);
        ps.execute();
        System.out.println("Registration successfully added to the auction.");
    }

    private void deleteNumberPlateFromAuction(Connection c) throws SQLException {

        Scanner scan = new Scanner(System.in);

        System.out.println("Please enter the number plate to be deleted");
        String deletedPlate = scan.nextLine();

        PreparedStatement ps = c.prepareCall("DELETE FROM number_plate_auction " +
                "WHERE registration = ?;");

        ps.setString(1,deletedPlate);

        try {
            ps.executeQuery();
        } catch (SQLException sqlex) {
            sqlex.getSQLState();
            System.out.println("Number plate not in database.");
        }
    }

    public static void main(String[] args){
        try {
            new Main_Class();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}