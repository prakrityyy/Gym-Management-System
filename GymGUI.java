import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.BiConsumer;

/**
 * GymGUI - A class to implement graphical user interface for the gym management system
 * This GUI allows management of regular and premium gym members with various features
 * including adding members, tracking attendance, upgrading plans, and calculating discounts.
 * 
 * @author Prakriti Pradhan 
 * @version 1.1
 */
public class GymGUI extends JFrame {
    private ArrayList<GymMember> members;
    
    private JTextField idField, nameField, locationField, phoneField, emailField;
    private JTextField dobField, startDateField, referralField, paidAmountField;
    private JTextField removalReasonField, trainerNameField;
    
    private JTextField regularPlanPriceField, premiumChargeField, discountAmountField;
    
    private JRadioButton maleButton, femaleButton, otherButton;
    
    private JComboBox<String> planComboBox;
    
    private JTextArea outputArea;
    
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    /**
     * Constructor for objects of class GymGUI
     * Sets up the user interface components and initializes data structures
     */
    public GymGUI() {
        members = new ArrayList<>();
        
        setTitle("Gym Management System");
        setSize(950, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        Font modernFont = new Font("Segoe UI", Font.PLAIN, 13);
        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        Color bgColor = new Color(240, 240, 245);
        Color panelColor = new Color(250, 250, 255);
        Color headerColor = new Color(220, 225, 240);
        Color buttonColor = new Color(225, 235, 245);
        Color buttonTextColor = new Color(20, 50, 90);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(bgColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel detailsPanel = new JPanel(new BorderLayout(0, 5));
        detailsPanel.setBackground(panelColor);
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JPanel detailsHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        detailsHeaderPanel.setBackground(headerColor);
        JLabel detailsHeaderLabel = new JLabel("Member Information");
        detailsHeaderLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        detailsHeaderPanel.add(detailsHeaderLabel);
        detailsPanel.add(detailsHeaderPanel, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridLayout(8, 4, 8, 6));
        formPanel.setBackground(panelColor);
        
        addFormField(formPanel, "Member ID:", idField = new JTextField(10), labelFont, modernFont);
        addFormField(formPanel, "Name:", nameField = new JTextField(15), labelFont, modernFont);
        
        addFormField(formPanel, "Location:", locationField = new JTextField(15), labelFont, modernFont);
        addFormField(formPanel, "Phone:", phoneField = new JTextField(10), labelFont, modernFont);
        
        addFormField(formPanel, "Email:", emailField = new JTextField(15), labelFont, modernFont);
        addFormField(formPanel, "Date of Birth:", dobField = new JTextField(10), labelFont, modernFont);
        
        addFormField(formPanel, "Start Date:", startDateField = new JTextField(10), labelFont, modernFont);
        addFormField(formPanel, "Referral:", referralField = new JTextField(15), labelFont, modernFont);
        
        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setFont(labelFont);
        formPanel.add(genderLabel);
        
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
        genderPanel.setBackground(panelColor);
        maleButton = new JRadioButton("Male");
        femaleButton = new JRadioButton("Female");
        otherButton = new JRadioButton("Other");
        
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);
        genderGroup.add(otherButton);
        
        for (JRadioButton btn : new JRadioButton[]{maleButton, femaleButton, otherButton}) {
            btn.setFont(modernFont);
            btn.setBackground(panelColor);
            genderPanel.add(btn);
        }
        formPanel.add(genderPanel);
        
        JLabel planLabel = new JLabel("Plan:");
        planLabel.setFont(labelFont);
        formPanel.add(planLabel);
        
        planComboBox = new JComboBox<>(new String[]{"basic", "standard", "deluxe"});
        planComboBox.setFont(modernFont);
        formPanel.add(planComboBox);
        
        addFormField(formPanel, "Trainer Name:", trainerNameField = new JTextField(15), labelFont, modernFont);
        addFormField(formPanel, "Removal Reason:", removalReasonField = new JTextField(15), labelFont, modernFont);
        
        paidAmountField = new JTextField(10);
        addFormField(formPanel, "Paid Amount:", paidAmountField, labelFont, modernFont);
        
        regularPlanPriceField = new JTextField(10);
        regularPlanPriceField.setEditable(false);
        regularPlanPriceField.setText("7150.0");
        addFormField(formPanel, "Plan Price:", regularPlanPriceField, labelFont, modernFont);
        
        premiumChargeField = new JTextField(10);
        premiumChargeField.setEditable(false);
        premiumChargeField.setText("55000.0");
        addFormField(formPanel, "Premium Charge:", premiumChargeField, labelFont, modernFont);
        
        discountAmountField = new JTextField(10);
        discountAmountField.setEditable(false);
        addFormField(formPanel, "Discount:", discountAmountField, labelFont, modernFont);
        
        detailsPanel.add(formPanel, BorderLayout.CENTER);
        
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 7, 5, 5));
        buttonsPanel.setBackground(headerColor);
        buttonsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
            BorderFactory.createEmptyBorder(7, 7, 7, 7)
        ));
        
        JButton[] buttons = new JButton[] {
            new JButton("Add Regular"),
            new JButton("Add Premium"),
            new JButton("Activate"),
            new JButton("Deactivate"),
            new JButton("Mark Attendance"),
            new JButton("Upgrade Plan"),
            new JButton("Calculate Discount"),
            new JButton("Revert Regular"),
            new JButton("Revert Premium"),
            new JButton("Pay Due"),
            new JButton("Display"),
            new JButton("Clear"),
            new JButton("Save"),
            new JButton("Load")
        };
        
        JButton addRegularButton = buttons[0];
        JButton addPremiumButton = buttons[1];
        JButton activateButton = buttons[2];
        JButton deactivateButton = buttons[3];
        JButton markAttendanceButton = buttons[4];
        JButton upgradePlanButton = buttons[5];
        JButton calculateDiscountButton = buttons[6];
        JButton revertRegularButton = buttons[7];
        JButton revertPremiumButton = buttons[8];
        JButton payDueButton = buttons[9];
        JButton displayButton = buttons[10];
        JButton clearButton = buttons[11];
        JButton saveButton = buttons[12];
        JButton readButton = buttons[13];
        
        for (JButton btn : buttons) {
            btn.setFont(modernFont);
            btn.setBackground(buttonColor);
            btn.setForeground(buttonTextColor);
            btn.setFocusPainted(false);
            btn.setBorderPainted(true);
            btn.setOpaque(true);
            btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 200, 220), 1),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)
            ));
            buttonsPanel.add(btn);
        }
        
        addRegularButton.addActionListener(e -> addRegularMember());
        addPremiumButton.addActionListener(e -> addPremiumMember());
        activateButton.addActionListener(e -> activateMembership());
        deactivateButton.addActionListener(e -> deactivateMembership());
        markAttendanceButton.addActionListener(e -> markAttendance());
        upgradePlanButton.addActionListener(e -> upgradePlan());
        calculateDiscountButton.addActionListener(e -> calculateDiscount());
        revertRegularButton.addActionListener(e -> revertRegularMember());
        revertPremiumButton.addActionListener(e -> revertPremiumMember());
        payDueButton.addActionListener(e -> payDueAmount());
        displayButton.addActionListener(e -> displayMembers());
        clearButton.addActionListener(e -> clearFields());
        saveButton.addActionListener(e -> saveToFile());
        readButton.addActionListener(e -> readFromFile());
        
        JPanel outputPanel = new JPanel(new BorderLayout(0, 5));
        outputPanel.setBackground(panelColor);
        outputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JPanel outputHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        outputHeaderPanel.setBackground(headerColor);
        JLabel outputHeaderLabel = new JLabel("System Output");
        outputHeaderLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        outputHeaderPanel.add(outputHeaderLabel);
        outputPanel.add(outputHeaderPanel, BorderLayout.NORTH);
        
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        outputArea.setBackground(Color.WHITE);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        
        JScrollPane outputScrollPane = new JScrollPane(outputArea);
        outputScrollPane.setPreferredSize(new Dimension(0, 220)); // Reduced height
        outputScrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 220), 1));
        outputPanel.add(outputScrollPane, BorderLayout.CENTER);
        
        mainPanel.add(detailsPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Reduced spacing
        mainPanel.add(buttonsPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Reduced spacing
        mainPanel.add(outputPanel);
        
        JScrollPane mainScrollPane = new JScrollPane(mainPanel);
        mainScrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(mainScrollPane);
        setVisible(true);
        
        updatePlanPrice();
        
        planComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                updatePlanPrice();
            }
        });
    }
    
    /**
     * Helper method to add form fields
     */
    private void addFormField(JPanel panel, String labelText, JTextField field, Font labelFont, Font fieldFont) {
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        panel.add(label);
        
        field.setFont(fieldFont);
        panel.add(field);
    }
    
    /**
     * Updates the regular plan price field based on selected plan
     */
    private void updatePlanPrice() {
        String selectedPlan = (String) planComboBox.getSelectedItem();
        try {
            // Create a temporary RegularMember to get the plan price
            RegularMember tempMember = new RegularMember(0, "", "", "", "", "", "", "", "");
            double price = tempMember.getPlanPrice(selectedPlan);
            regularPlanPriceField.setText(String.valueOf(price));
        } catch (Exception ex) {
            showError("⚠️ Unable to update plan price", "The system encountered an error: " + ex.getMessage());
        }
    }
    
    /**
     * Validates and adds a new regular member to the system
     */
    private void addRegularMember() {
        try {
            int id = validateId();
            validateRequiredFields();
            validateDateFormat(dobField.getText(), "Date of Birth");
            validateDateFormat(startDateField.getText(), "Membership Start Date");
            
            String gender = getSelectedGender();
            
            RegularMember member = new RegularMember(
                id,
                nameField.getText(),
                locationField.getText(),
                phoneField.getText(),
                emailField.getText(),
                gender,
                dobField.getText(),
                startDateField.getText(),
                referralField.getText()
            );
            
            String selectedPlan = (String) planComboBox.getSelectedItem();
            member.upgradePlan(selectedPlan);
            
            members.add(member);
            
            outputArea.setText("Regular member added successfully!\n");
            member.display();
            
            clearFields();
        } catch (Exception ex) {
            showError("⚠️ Unable to Add Regular Member", "Please check the information and try again: " + ex.getMessage());
        }
    }
    
    /**
     * Validates and adds a new premium member to the system
     */
    private void addPremiumMember() {
        try {
            int id = validateId();
            validateRequiredFields();
            validateDateFormat(dobField.getText(), "Date of Birth");
            validateDateFormat(startDateField.getText(), "Membership Start Date");
            
            if (trainerNameField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("Trainer name is required for Premium Members");
            }
            
            String gender = getSelectedGender();
            
            PremiumMember member = new PremiumMember(
                id,
                nameField.getText(),
                locationField.getText(),
                phoneField.getText(),
                emailField.getText(),
                gender,
                dobField.getText(),
                startDateField.getText(),
                trainerNameField.getText()
            );
            
            if (!paidAmountField.getText().trim().isEmpty()) {
                try {
                    double paidAmount = Double.parseDouble(paidAmountField.getText().trim());
                    String paymentResult = member.payDueAmount(paidAmount);
                    outputArea.setText(paymentResult + "\n");
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Invalid paid amount format. Please enter a valid number.");
                }
            }
            
            members.add(member);
            
            outputArea.append("Premium member added successfully!\n");
            member.display();
            
            clearFields();
        } catch (Exception ex) {
            showError("⚠️ Unable to Add Premium Member", "Please check the information and try again: " + ex.getMessage());
        }
    }
    
    /**
     * Activates the membership for a member with the given ID
     */
    private void activateMembership() {
        try {
            int id = validateId();
            GymMember member = findMemberById(id);
            
            member.activateMembership();
            
            outputArea.setText("Membership activated successfully for member with ID: " + id + "\n");
            member.display();
        } catch (Exception ex) {
            showError("⚠️ Activation Failed", "Could not activate membership: " + ex.getMessage());
        }
    }
    
    /**
     * Deactivates the membership for a member with the given ID
     */
    private void deactivateMembership() {
        try {
            int id = validateId();
            GymMember member = findMemberById(id);
            
            member.deactivateMembership();
            
            outputArea.setText("Membership deactivated successfully for member with ID: " + id + "\n");
            member.display();
        } catch (Exception ex) {
            showError("⚠️ Deactivation Failed", "Could not deactivate membership: " + ex.getMessage());
        }
    }
    
    /**
     * Marks attendance for a member with the given ID
     */
    private void markAttendance() {
        try {
            int id = validateId();
            GymMember member = findMemberById(id);
            
            if (!member.isActiveStatus()) {
                throw new IllegalStateException("This membership is currently inactive. Please activate it first.");
            }
            
            member.markAttendance();
            
            outputArea.setText("Attendance marked successfully for member with ID: " + id + "\n");
            outputArea.append("Current attendance: " + member.getAttendance() + "\n");
            outputArea.append("Loyalty points: " + member.getLoyaltyPoints() + "\n");
            
            if (member instanceof RegularMember) {
                RegularMember regularMember = (RegularMember) member;
                outputArea.append("Eligible for upgrade: " + regularMember.isEligibleForUpgrade() + "\n");
            }
        } catch (Exception ex) {
            showError("⚠️ Attendance Marking Failed", "Could not mark attendance: " + ex.getMessage());
        }
    }
    
    /**
     * Upgrades the plan for a regular member with the given ID
     */
    private void upgradePlan() {
        try {
            int id = validateId();
            GymMember member = findMemberById(id);
            
            if (!(member instanceof RegularMember)) {
                throw new IllegalArgumentException("The member with ID " + id + " is not a Regular Member. Only regular members can be upgraded.");
            }
            
            RegularMember regularMember = (RegularMember) member;
            
            String newPlan = (String) planComboBox.getSelectedItem();
            
            String result = regularMember.upgradePlan(newPlan);
            
            outputArea.setText(result + "\n");
            regularMember.display();
        } catch (Exception ex) {
            showError("⚠️ Plan Upgrade Failed", "Could not upgrade the plan: " + ex.getMessage());
        }
    }
    
    /**
     * Calculates discount for a premium member with the given ID
     */
    private void calculateDiscount() {
        try {
            int id = validateId();
            GymMember member = findMemberById(id);
            
            if (!(member instanceof PremiumMember)) {
                throw new IllegalArgumentException("The member with ID " + id + " is not a Premium Member. Discounts are only available for premium members.");
            }
            
            PremiumMember premiumMember = (PremiumMember) member;
            
            premiumMember.calculateDiscount();
            
            discountAmountField.setText(String.valueOf(premiumMember.getDiscountAmount()));
            
            outputArea.setText("Discount calculated for member with ID: " + id + "\n");
            outputArea.append("Discount amount: " + premiumMember.getDiscountAmount() + "\n");
            premiumMember.display();
        } catch (Exception ex) {
            showError("⚠️ Discount Calculation Failed", "Could not calculate discount: " + ex.getMessage());
        }
    }
    
    /**
     * Reverts a regular member with the given ID
     */
    private void revertRegularMember() {
        try {
            int id = validateId();
            GymMember member = findMemberById(id);
            
            if (!(member instanceof RegularMember)) {
                throw new IllegalArgumentException("The member with ID " + id + " is not a Regular Member. Only regular members can be reverted.");
            }
            
            RegularMember regularMember = (RegularMember) member;
            
            String reason = removalReasonField.getText().trim();
            if (reason.isEmpty()) {
                throw new IllegalArgumentException("A removal reason is required. Please provide a reason for reverting the membership.");
            }
            
            regularMember.revertRegularMember(reason);
            
            outputArea.setText("Regular member reverted successfully for ID: " + id + "\n");
            regularMember.display();
        } catch (Exception ex) {
            showError("⚠️ Member Reversion Failed", "Could not revert regular member: " + ex.getMessage());
        }
    }
    
    /**
     * Reverts a premium member with the given ID
     */
    private void revertPremiumMember() {
        try {
            int id = validateId();
            GymMember member = findMemberById(id);
            
            if (!(member instanceof PremiumMember)) {
                throw new IllegalArgumentException("The member with ID " + id + " is not a Premium Member. Only premium members can be reverted.");
            }
            
            PremiumMember premiumMember = (PremiumMember) member;
            
            premiumMember.revertPremiumMember();
            
            outputArea.setText("Premium member reverted successfully for ID: " + id + "\n");
            premiumMember.display();
        } catch (Exception ex) {
            showError("⚠️ Member Reversion Failed", "Could not revert premium member: " + ex.getMessage());
        }
    }
    
    /**
     * Processes payment for a premium member with the given ID
     */
    private void payDueAmount() {
        try{
            int id = validateId();
            GymMember member = findMemberById(id);
            
            if (!(member instanceof PremiumMember)) {
                throw new IllegalArgumentException("The member with ID " + id + " is not a Premium Member. Payment can only be processed for premium members.");
            }
            
            PremiumMember premiumMember = (PremiumMember) member;
            
            if (paidAmountField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("Payment amount is required. Please enter an amount to pay.");
            }
            
            double amount;
            try {
                amount = Double.parseDouble(paidAmountField.getText().trim());
                if (amount <= 0) {
                    throw new IllegalArgumentException("Payment amount must be positive. Please enter a value greater than zero.");
                }
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Invalid payment amount format. Please enter a valid number.");
            }
            
            String result = premiumMember.payDueAmount(amount);
            
            outputArea.setText(result + "\n");
            premiumMember.display();
        } catch (Exception ex) {
            showError("⚠️ Payment Processing Failed", "Could not process payment: " + ex.getMessage());
        }
    }
    
    /**
     * Displays all members in the system
     */
    private void displayMembers() {
        try {
            if (members.isEmpty()) {
                outputArea.setText("No members found in the system.\n");
                return;
            }
            
            outputArea.setText("All Members in the System:\n");
            outputArea.append("----------------------\n");
            
            int regularCount = 0;
            int premiumCount = 0;
            int activeCount = 0;
            
            for (GymMember member : members) {
                if (member instanceof RegularMember) {
                    regularCount++;
                } else if (member instanceof PremiumMember) {
                    premiumCount++;
                }
                
                if (member.isActiveStatus()) {
                    activeCount++;
                }
                
                outputArea.append("\nMember ID: " + member.getId() + "\n");
                outputArea.append("Name: " + member.getName() + "\n");
                outputArea.append("Type: " + (member instanceof RegularMember ? "Regular" : "Premium") + "\n");
                outputArea.append("Active: " + member.isActiveStatus() + "\n");
                outputArea.append("----------------------\n");
            }
            
            outputArea.append("\nSummary:\n");
            outputArea.append("Total Members: " + members.size() + "\n");
            outputArea.append("Regular Members: " + regularCount + "\n");
            outputArea.append("Premium Members: " + premiumCount + "\n");
            outputArea.append("Active Members: " + activeCount + "\n");
        } catch (Exception ex) {
            showError("Error displaying members", "Could not display members: " + ex.getMessage());
        }
    }
    
    /**
     * Clears all input fields
     */
    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        locationField.setText("");
        phoneField.setText("");
        emailField.setText("");
        dobField.setText("");
        startDateField.setText("");
        referralField.setText("");
        paidAmountField.setText("");
        removalReasonField.setText("");
        trainerNameField.setText("");
        
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);
        genderGroup.add(otherButton);
        genderGroup.clearSelection();
        
        planComboBox.setSelectedIndex(0);
        
        discountAmountField.setText("");
    }
    
    /**
     * Saves all members to a file
     */
    private void saveToFile() {
        try {
            if (members.isEmpty()) {
                showMessage("Information", "No members to save. Please add members before saving.");
                return;
            }
            
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Members");
            
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                
                if (!file.getName().toLowerCase().endsWith(".ser")) {
                    file = new File(file.getAbsolutePath() + ".ser");
                }
                
                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                    oos.writeObject(members);
                    showMessage("✅ Save Successful", "Members saved to file: " + file.getAbsolutePath());
                }
            }
        } catch (IOException ex) {
            showError("⚠️ File Save Failed", "Could not save to file: " + ex.getMessage());
        }
    }
    
    /**
     * Reads members from a file
     */
    @SuppressWarnings("unchecked")
    private void readFromFile() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Load Members");
            
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                    members = (ArrayList<GymMember>) ois.readObject();
                    showMessage("✅ Load Successful", "Members loaded from file: " + file.getAbsolutePath());
                    displayMembers();
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            showError("⚠️ File Load Failed", "Could not read from file: " + ex.getMessage());
        }
    }
    
    /**
     * Finds a member by ID
     * 
     * @param id The ID to search for
     * @return The member with the given ID
     * @throws IllegalArgumentException If no member with the given ID is found
     */
    private GymMember findMemberById(int id) throws IllegalArgumentException {
        for (GymMember member : members) {
            if (member.getId() == id) {
                return member;
            }
        }
        throw new IllegalArgumentException("No member found with ID: " + id + ". Please check the ID and try again.");
    }
    
    /**
     * Validates the ID field
     * 
     * @return The validated ID
     * @throws IllegalArgumentException If the ID field is empty or not a valid integer
     */
    private int validateId() throws IllegalArgumentException {
        String idText = idField.getText().trim();
        if (idText.isEmpty()) {
            throw new IllegalArgumentException("Member ID is required. Please enter a member ID.");
        }
        
        try {
            return Integer.parseInt(idText);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Member ID must be a valid integer. Please enter a numeric ID.");
        }
    }
    
    /**
     * Validates required fields
     * 
     * @throws IllegalArgumentException If any required field is empty
     */
    private void validateRequiredFields() throws IllegalArgumentException {
        if (nameField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required. Please enter a name.");
        }
        if (locationField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Location is required. Please enter a location.");
        }
        if (phoneField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Phone is required. Please enter a phone number.");
        }
        if (emailField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required. Please enter an email address.");
        }
        if (dobField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Date of Birth is required. Please enter a birth date.");
        }
        if (startDateField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Membership Start Date is required. Please enter a start date.");
        }
        if (!maleButton.isSelected() && !femaleButton.isSelected() && !otherButton.isSelected()) {
            throw new IllegalArgumentException("Gender selection is required. Please select a gender.");
        }
    }
    
    /**
     * Gets the selected gender
     * 
     * @return The selected gender
     * @throws IllegalArgumentException If no gender is selected
     */
    private String getSelectedGender() throws IllegalArgumentException {
        if (maleButton.isSelected()) {
            return "male";
        } else if (femaleButton.isSelected()) {
            return "female";
        } else if (otherButton.isSelected()) {
            return "other";
        } else {
            throw new IllegalArgumentException("No gender selected. Please select a gender option.");
        }
    }
    
    /**
     * Validates date format
     * 
     * @param date The date string to validate
     * @param fieldName The name of the field for error messages
     * @throws IllegalArgumentException If the date format is invalid
     */
    private void validateDateFormat(String date, String fieldName) throws IllegalArgumentException {
        if (date.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required. Please enter a date.");
        }
        
        try {
            dateFormat.setLenient(false);
            Date parsedDate = dateFormat.parse(date);
            
            if (fieldName.equals("Date of Birth")) {
                Date today = new Date();
                if (parsedDate.after(today)) {
                    throw new IllegalArgumentException("Date of Birth cannot be in the future. Please enter a valid birth date.");
                }
            }
        } catch (ParseException ex) {
            throw new IllegalArgumentException(fieldName + " must be in format yyyy-MM-dd (e.g., 1990-01-31).");
        }
    }
    
    /**
     * Shows an error message
     * 
     * @param title The title of the error message
     * @param message The error message to display
     */
    private void showError(String title, String message) {
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 16));
        UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.PLAIN, 16));
        UIManager.put("OptionPane.background", new Color(250, 240, 240));
        UIManager.put("Panel.background", new Color(250, 240, 240));
        JLabel label = new JLabel("<html><b>" + title + "</b><br>" + message + "</html>");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JOptionPane.showMessageDialog(
            this, 
            label, 
            "Error", 
            JOptionPane.ERROR_MESSAGE
        );
        outputArea.setText("Error: " + message);
    }
    
    /**
     * Shows an information message
     * 
     * @param title The title of the information message
     * @param message The message to display
     */
    private void showMessage(String title, String message) {
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 16));
        UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.PLAIN, 16));
        UIManager.put("OptionPane.background", new Color(240, 250, 240));
        UIManager.put("Panel.background", new Color(240, 250, 240));
        JLabel label = new JLabel("<html><b>" + title + "</b><br>" + message + "</html>");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JOptionPane.showMessageDialog(
            this, 
            label, 
            "Information", 
            JOptionPane.INFORMATION_MESSAGE
        );
        outputArea.setText(message);
    }
    
    /**
     * The main method to start the application
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    System.err.println("Error setting look and feel: " + e.getMessage());
                }
                
                new GymGUI();
            }
        });
    }
}