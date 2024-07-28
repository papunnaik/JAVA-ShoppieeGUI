package com.shoppiee.gui;

import javax.swing.*;
import com.google.gson.Gson;
import com.shoppiee.model.User;
import java.awt.*;
import java.io.IOException;
import java.util.Stack;
import okhttp3.*;
import com.shoppiee.model.*;

public class Shoppiee {
    private static final String BASE_URL = "http://localhost:8081/api";
    private OkHttpClient client = new OkHttpClient();

    // Main Frame
    private JFrame frame;
    private JPanel initialPanel, loginPanel, createAccountPanel, shoppingPanel, checkoutPanel, pendingOrdersPanel, itemModificationsPanel, managerPanel, manageEmployeesPanel;
    private CardLayout cardLayout;
    private Stack<String> panelStack;

    // Constructor
    public Shoppiee() {
        frame = new JFrame("Shopify Application");
        cardLayout = new CardLayout();
        frame.setLayout(cardLayout);
        panelStack = new Stack<>();

        initialPanel = createInitialPanel();
        loginPanel = createLoginPanel();
        createAccountPanel = createAccountPanel();
        shoppingPanel = createShoppingPanel();
        checkoutPanel = createCheckoutPanel();
        pendingOrdersPanel = createPendingOrdersPanel();
        itemModificationsPanel = createItemModificationsPanel();
        managerPanel = createManagerPanel();
        manageEmployeesPanel = createManageEmployeesPanel();

        frame.add(initialPanel, "Initial");
        frame.add(loginPanel, "Login");
        frame.add(createAccountPanel, "CreateAccount");
        frame.add(shoppingPanel, "Shopping");
        frame.add(checkoutPanel, "Checkout");
        frame.add(pendingOrdersPanel, "PendingOrders");
        frame.add(itemModificationsPanel, "ItemModifications");
        frame.add(managerPanel, "Manager");
        frame.add(manageEmployeesPanel, "ManageEmployees");

        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        showPanel("Initial");
    }

    // Show specific panel
    private void showPanel(String panelName) {
        if (!panelStack.isEmpty() && !panelStack.peek().equals(panelName)) {
            panelStack.push(panelName);
        } else if (panelStack.isEmpty()) {
            panelStack.push(panelName);
        }
        cardLayout.show(frame.getContentPane(), panelName);
    }

    // Show previous panel
    private void showPreviousPanel() {
        if (panelStack.size() > 1) {
            panelStack.pop();
            String previousPanel = panelStack.peek();
            cardLayout.show(frame.getContentPane(), previousPanel);
        }
    }

    // Initial Panel
    private JPanel createInitialPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));

        JButton loginButton = new JButton("Login");
        JButton createAccountButton = new JButton("Create Account");

        loginButton.addActionListener(e -> showPanel("Login"));
        createAccountButton.addActionListener(e -> showPanel("CreateAccount"));

        panel.add(loginButton);
        panel.add(createAccountButton);

        return panel;
    }

    // Login Panel
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        JLabel userLabel = new JLabel("Username:");
        JTextField userText = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordText = new JPasswordField();
        JLabel roleLabel = new JLabel("Role:");
        String[] roles = {"customer", "employee", "manager"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back");

        loginButton.addActionListener(e -> {
            String username = userText.getText();
            String password = new String(passwordText.getPassword());
            String role = (String) roleComboBox.getSelectedItem();
            if (authenticateUser(username, password, role)) {
                if (role.equals("customer")) {
                    showPanel("Shopping");
                } else if (role.equals("employee")) {
                    showPanel("ItemModifications");
                } else if (role.equals("manager")) {
                    showPanel("Manager");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password");
            }
        });

        backButton.addActionListener(e -> showPreviousPanel());

        panel.add(userLabel);
        panel.add(userText);
        panel.add(passwordLabel);
        panel.add(passwordText);
        panel.add(roleLabel);
        panel.add(roleComboBox);
        panel.add(loginButton);
        panel.add(backButton);

        return panel;
    }

    // Authenticate user
    private boolean authenticateUser(String username, String password, String role) {
        User user = new User(username, password, null, null, null, null, role);
        RequestBody body = RequestBody.create(user.toJson(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
            .url(BASE_URL + "/login")
            .post(body)
            .build();
        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful() && Boolean.parseBoolean(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Create Account Panel
    private JPanel createAccountPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9, 2));

        JLabel userLabel = new JLabel("Username:");
        JTextField userText = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordText = new JPasswordField();
        JLabel firstNameLabel = new JLabel("First Name:");
        JTextField firstNameText = new JTextField();
        JLabel lastNameLabel = new JLabel("Last Name:");
        JTextField lastNameText = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailText = new JTextField();
        JLabel addressLabel = new JLabel("Address:");
        JTextField addressText = new JTextField();
        JLabel roleLabel = new JLabel("Role:");
        String[] roles = {"customer", "employee", "manager"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        JButton createButton = new JButton("Create Account");
        JButton backButton = new JButton("Back");

        createButton.addActionListener(e -> {
            String username = userText.getText();
            String password = new String(passwordText.getPassword());
            String firstName = firstNameText.getText();
            String lastName = lastNameText.getText();
            String email = emailText.getText();
            String address = addressText.getText();
            String role = (String) roleComboBox.getSelectedItem();
            User user = new User(username, password, firstName, lastName, email, address, role);
            createUser(user);
        });

        backButton.addActionListener(e -> showPreviousPanel());

        panel.add(userLabel);
        panel.add(userText);
        panel.add(passwordLabel);
        panel.add(passwordText);
        panel.add(firstNameLabel);
        panel.add(firstNameText);
        panel.add(lastNameLabel);
        panel.add(lastNameText);
        panel.add(emailLabel);
        panel.add(emailText);
        panel.add(addressLabel);
        panel.add(addressText);
        panel.add(roleLabel);
        panel.add(roleComboBox);
        panel.add(createButton);
        panel.add(backButton);

        return panel;
    }

    // Create user
    private void createUser(User user) {
        RequestBody body = RequestBody.create(new Gson().toJson(user), MediaType.parse("application/json"));
        Request request = new Request.Builder()
            .url(BASE_URL + "/createAccount")
            .post(body)
            .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                JOptionPane.showMessageDialog(frame, "Account created successfully");
                showPanel("Login");
            } else {
                JOptionPane.showMessageDialog(frame, "Error creating account");
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error creating account");
        }
    }

    // Shopping Panel
    private JPanel createShoppingPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextArea itemList = new JTextArea();
        JTextField itemField = new JTextField();
        JTextField quantityField = new JTextField();
        JButton addButton = new JButton("Add to Cart");
        JButton checkoutButton = new JButton("Checkout");
        JButton backButton = new JButton("Back");

        panel.add(new JScrollPane(itemList), BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new GridLayout(4, 2));
        bottomPanel.add(new JLabel("Item:"));
        bottomPanel.add(itemField);
        bottomPanel.add(new JLabel("Quantity:"));
        bottomPanel.add(quantityField);
        bottomPanel.add(addButton);
        bottomPanel.add(checkoutButton);
        bottomPanel.add(backButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            String item = itemField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            addToCart(item, quantity);
        });

        checkoutButton.addActionListener(e -> showPanel("Checkout"));
        backButton.addActionListener(e -> showPreviousPanel());

        return panel;
    }

    // Add to cart
    private void addToCart(String item, int quantity) {
        String json = new Gson().toJson(new OrderItem(item, quantity));
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
            .url(BASE_URL + "/addToCart")
            .post(body)
            .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                JOptionPane.showMessageDialog(frame, "Item added to cart");
            } else {
                JOptionPane.showMessageDialog(frame, "Error adding item to cart");
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error adding item to cart");
        }
    }

    // Checkout Panel
    private JPanel createCheckoutPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        JTextArea cartList = new JTextArea();
        JButton completeButton = new JButton("Complete Order");
        JButton backButton = new JButton("Back");

        panel.add(new JScrollPane(cartList));
        panel.add(completeButton);
        panel.add(backButton);

        completeButton.addActionListener(e -> completeOrder());
        backButton.addActionListener(e -> showPreviousPanel());

        return panel;
    }

    // Complete order
    private void completeOrder() {
        Request request = new Request.Builder()
            .url(BASE_URL + "/completeOrder")
            .post(RequestBody.create("", MediaType.parse("application/json")))
            .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                JOptionPane.showMessageDialog(frame, "Order completed successfully");
            } else {
                JOptionPane.showMessageDialog(frame, "Error completing order");
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error completing order");
        }
    }

    // Pending Orders Panel
    private JPanel createPendingOrdersPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextArea orderList = new JTextArea();
        JButton processButton = new JButton("Process Order");
        JButton backButton = new JButton("Back");

        panel.add(new JScrollPane(orderList), BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        bottomPanel.add(processButton);
        bottomPanel.add(backButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        processButton.addActionListener(e -> processOrder());
        backButton.addActionListener(e -> showPreviousPanel());

        return panel;
    }

    // Process order
    private void processOrder() {
        Request request = new Request.Builder()
            .url(BASE_URL + "/processOrder")
            .post(RequestBody.create("", MediaType.parse("application/json")))
            .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                JOptionPane.showMessageDialog(frame, "Order processed successfully");
            } else {
                JOptionPane.showMessageDialog(frame, "Error processing order");
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error processing order");
        }
    }

    // Item Modifications Panel
    private JPanel createItemModificationsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextArea itemList = new JTextArea();
        JTextField itemField = new JTextField();
        JTextField quantityField = new JTextField();
        JTextField priceField = new JTextField();
        JButton updateButton = new JButton("Update Item");
        JButton addButton = new JButton("Add Item");
        JButton deleteButton = new JButton("Delete Item");
        JButton backButton = new JButton("Back");

        panel.add(new JScrollPane(itemList), BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new GridLayout(6, 2));
        bottomPanel.add(new JLabel("Item:"));
        bottomPanel.add(itemField);
        bottomPanel.add(new JLabel("Quantity:"));
        bottomPanel.add(quantityField);
        bottomPanel.add(new JLabel("Price:"));
        bottomPanel.add(priceField);
        bottomPanel.add(updateButton);
        bottomPanel.add(addButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(backButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        updateButton.addActionListener(e -> {
            String item = itemField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());
            updateItem(item, quantity, price);
        });

        addButton.addActionListener(e -> {
            String item = itemField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());
            addItem(item, quantity, price);
        });

        deleteButton.addActionListener(e -> {
            String item = itemField.getText();
            deleteItem(item);
        });

        backButton.addActionListener(e -> showPreviousPanel());

        return panel;
    }

    // Update item
    private void updateItem(String item, int quantity, double price) {
        String json = new Gson().toJson(new Item(item, quantity, price));
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
            .url(BASE_URL + "/updateItem")
            .post(body)
            .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                JOptionPane.showMessageDialog(frame, "Item updated successfully");
            } else {
                JOptionPane.showMessageDialog(frame, "Error updating item");
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error updating item");
        }
    }

    // Add item
    private void addItem(String item, int quantity, double price) {
        String json = new Gson().toJson(new Item(item, quantity, price));
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
            .url(BASE_URL + "/addItem")
            .post(body)
            .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                JOptionPane.showMessageDialog(frame, "Item added successfully");
            } else {
                JOptionPane.showMessageDialog(frame, "Error adding item");
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error adding item");
        }
    }

    // Delete item
    private void deleteItem(String item) {
        String json = new Gson().toJson(new Item(item, 0, 0.0)); // quantity and price are not needed
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
            .url(BASE_URL + "/deleteItem")
            .post(body)
            .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                JOptionPane.showMessageDialog(frame, "Item deleted successfully");
            } else {
                JOptionPane.showMessageDialog(frame, "Error deleting item");
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error deleting item");
        }
    }

    // Manager Panel
    private JPanel createManagerPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        JButton manageItemsButton = new JButton("Manage Items");
        JButton pendingOrdersButton = new JButton("Pending Orders");
        JButton manageEmployeesButton = new JButton("Manage Employees");
        JButton backButton = new JButton("Back");

        manageItemsButton.addActionListener(e -> showPanel("ItemModifications"));
        pendingOrdersButton.addActionListener(e -> showPanel("PendingOrders"));
        manageEmployeesButton.addActionListener(e -> showPanel("ManageEmployees"));
        backButton.addActionListener(e -> showPreviousPanel());

        panel.add(manageItemsButton);
        panel.add(pendingOrdersButton);
        panel.add(manageEmployeesButton);
        panel.add(backButton);

        return panel;
    }

    // Manage Employees Panel
    private JPanel createManageEmployeesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextArea employeeList = new JTextArea();
        JTextField employeeUsernameField = new JTextField();
        JButton removeButton = new JButton("Remove Employee");
        JButton backButton = new JButton("Back");

        panel.add(new JScrollPane(employeeList), BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new GridLayout(2, 2));
        bottomPanel.add(new JLabel("Employee Username:"));
        bottomPanel.add(employeeUsernameField);
        bottomPanel.add(removeButton);
        bottomPanel.add(backButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        removeButton.addActionListener(e -> {
            String username = employeeUsernameField.getText();
            removeEmployee(username);
        });

        backButton.addActionListener(e -> showPreviousPanel());

        return panel;
    }

    // Remove employee
    private void removeEmployee(String username) {
        User user = new User();
        user.setUsername(username);
        RequestBody body = RequestBody.create(new Gson().toJson(user), MediaType.parse("application/json"));
        Request request = new Request.Builder()
            .url(BASE_URL + "/removeEmployee")
            .post(body)
            .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                JOptionPane.showMessageDialog(frame, "Employee removed successfully");
            } else {
                JOptionPane.showMessageDialog(frame, "Error removing employee");
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error removing employee");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Shoppiee());
    }
}
