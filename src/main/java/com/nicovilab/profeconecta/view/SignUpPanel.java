/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.nicovilab.profeconecta.view;

import com.nicovilab.profeconecta.view.extraSwingComponents.TextPrompt;
import com.nicovilab.profeconecta.view.gradientComponents.ButtonGradient;
import com.nicovilab.profeconecta.view.gradientComponents.JPanelGradient;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import lombok.Getter;

/**
 *
 * @author Nico
 */
@Getter
public class SignUpPanel extends javax.swing.JPanel {

    public SignUpPanel(MainJFrame parent) {
        initComponents();
        changeWindowFocusOnLaunch();
        hoverEffectButtonManipulation(registerButton, Color.BLACK, new Color(37, 116, 169));
        hoverEffectButtonManipulation(signInButton, Color.BLACK, new Color(37, 116, 169));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        leftPanel = new JPanelGradient(new Color(52, 143, 80), new Color(86, 180, 211));
        appLogo = new javax.swing.JLabel();
        rightPanel = new JPanelGradient(new Color(230, 243, 250), new Color(255,255,255));
        mainLabel = new javax.swing.JLabel();
        signInButton = new javax.swing.JButton();
        nameLogo = new javax.swing.JLabel();
        surnameLogo = new javax.swing.JLabel();
        registerButton = new ButtonGradient(new Color(230, 243, 250), new Color(255,255,255));
        passwordLogo = new javax.swing.JLabel();
        emailLogo = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        surnameTextField = new javax.swing.JTextField();
        emailTextField = new javax.swing.JTextField();
        passwordField = new javax.swing.JPasswordField();
        confirmPasswordField = new javax.swing.JPasswordField();
        confirmPasswordLogo = new javax.swing.JLabel();
        informationLabel = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(60, 30));

        appLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/profeConecta225px.png"))); // NOI18N

        javax.swing.GroupLayout leftPanelLayout = new javax.swing.GroupLayout(leftPanel);
        leftPanel.setLayout(leftPanelLayout);
        leftPanelLayout.setHorizontalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leftPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(appLogo)
                .addContainerGap())
        );
        leftPanelLayout.setVerticalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addGap(126, 126, 126)
                .addComponent(appLogo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        rightPanel.setBackground(new java.awt.Color(235, 235, 235));
        rightPanel.setPreferredSize(new java.awt.Dimension(600, 0));

        mainLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        mainLabel.setText("Registra una cuenta");

        signInButton.setBackground(new java.awt.Color(235, 235, 235));
        signInButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        signInButton.setText("¿Ya tienes una cuenta? Inicia sesión aquí");
        signInButton.setBorder(null);
        signInButton.setBorderPainted(false);
        signInButton.setContentAreaFilled(false);
        signInButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        signInButton.setFocusPainted(false);
        signInButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signInButtonActionPerformed(evt);
            }
        });

        nameLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/nameIcon32px.png"))); // NOI18N

        surnameLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/surnameIcon32px.png"))); // NOI18N

        registerButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        registerButton.setText("Registrar");
        registerButton.setBorder(new LineBorder(Color.BLACK, 1, true));
        registerButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        registerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerButtonActionPerformed(evt);
            }
        });

        passwordLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/padlockLoginIcon32px.png"))); // NOI18N

        emailLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/mailLogo32px.png"))); // NOI18N

        nameTextField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        nameTextField.setToolTipText("");
        TextPrompt namePlaceholder = new TextPrompt("Nombre*", nameTextField);
        namePlaceholder.changeAlpha(0.75f);
        namePlaceholder.changeStyle(Font.ITALIC);
        nameTextField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(38, 117, 191), 2));
        nameTextField.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        nameTextField.setMaximumSize(new java.awt.Dimension(60, 30));
        nameTextField.setPreferredSize(new java.awt.Dimension(60, 30));
        nameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nameTextFieldFocusGained(evt);
            }
        });
        nameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameTextFieldActionPerformed(evt);
            }
        });

        surnameTextField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        TextPrompt surnamePlaceholder = new TextPrompt("Apellidos*", surnameTextField);
        surnamePlaceholder.changeAlpha(0.75f);
        surnamePlaceholder.changeStyle(Font.ITALIC);
        surnameTextField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(38, 117, 191), 2));
        surnameTextField.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        surnameTextField.setMaximumSize(new java.awt.Dimension(60, 30));
        surnameTextField.setPreferredSize(new java.awt.Dimension(60, 30));
        surnameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                surnameTextFieldFocusGained(evt);
            }
        });
        surnameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                surnameTextFieldActionPerformed(evt);
            }
        });

        emailTextField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        TextPrompt emailPlaceholder = new TextPrompt("Correo electrónico*", emailTextField);
        emailPlaceholder.changeAlpha(0.75f);
        emailPlaceholder.changeStyle(Font.ITALIC);
        emailTextField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(38, 117, 191), 2));
        emailTextField.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        emailTextField.setMaximumSize(new java.awt.Dimension(60, 30));
        emailTextField.setPreferredSize(new java.awt.Dimension(60, 30));
        emailTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                emailTextFieldFocusGained(evt);
            }
        });
        emailTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailTextFieldActionPerformed(evt);
            }
        });

        passwordField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        passwordField.setText("");
        TextPrompt passwordPlaceholder = new TextPrompt("Contraseña*", passwordField);
        passwordPlaceholder.changeAlpha(0.75f);
        passwordPlaceholder.changeStyle(Font.ITALIC);
        passwordField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(38, 117, 191), 2));

        confirmPasswordField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        confirmPasswordField.setText("");
        TextPrompt confirmPasswordPlaceholder = new TextPrompt("Repetir contraseña*", confirmPasswordField);
        confirmPasswordPlaceholder.changeAlpha(0.75f);
        confirmPasswordPlaceholder.changeStyle(Font.ITALIC);
        confirmPasswordField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(38, 117, 191), 2));
        confirmPasswordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmPasswordFieldActionPerformed(evt);
            }
        });

        confirmPasswordLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/padlockLoginIcon32px.png"))); // NOI18N

        informationLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout rightPanelLayout = new javax.swing.GroupLayout(rightPanel);
        rightPanel.setLayout(rightPanelLayout);
        rightPanelLayout.setHorizontalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightPanelLayout.createSequentialGroup()
                .addGap(210, 210, 210)
                .addComponent(mainLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rightPanelLayout.createSequentialGroup()
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(rightPanelLayout.createSequentialGroup()
                        .addGap(179, 179, 179)
                        .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, rightPanelLayout.createSequentialGroup()
                                .addComponent(emailLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(emailTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, rightPanelLayout.createSequentialGroup()
                                .addComponent(surnameLogo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(surnameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, rightPanelLayout.createSequentialGroup()
                                .addComponent(nameLogo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(nameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, rightPanelLayout.createSequentialGroup()
                                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(passwordLogo)
                                    .addComponent(confirmPasswordLogo))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(confirmPasswordField)
                                    .addComponent(passwordField)))
                            .addGroup(rightPanelLayout.createSequentialGroup()
                                .addGap(0, 5, Short.MAX_VALUE)
                                .addComponent(signInButton, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(rightPanelLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(registerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(205, 205, 205))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rightPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(informationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(125, 125, 125))
        );
        rightPanelLayout.setVerticalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(mainLabel)
                .addGap(32, 32, 32)
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(nameLogo)
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(surnameLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(surnameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(emailLogo)
                    .addComponent(emailTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(passwordLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(confirmPasswordLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(confirmPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(signInButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(registerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(informationLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(leftPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rightPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 660, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(rightPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
            .addComponent(leftPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void signInButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signInButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_signInButtonActionPerformed

    private void registerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_registerButtonActionPerformed

    private void nameTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameTextFieldFocusGained

    }//GEN-LAST:event_nameTextFieldFocusGained

    private void nameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameTextFieldActionPerformed

    private void surnameTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_surnameTextFieldFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_surnameTextFieldFocusGained

    private void surnameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_surnameTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_surnameTextFieldActionPerformed

    private void emailTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_emailTextFieldFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_emailTextFieldFocusGained

    private void emailTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailTextFieldActionPerformed

    private void confirmPasswordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmPasswordFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_confirmPasswordFieldActionPerformed

    public void addSignInButtonActionListener(ActionListener al) {
        this.signInButton.addActionListener(al);
    }

    public void addRegisterButtonActionListener(ActionListener al) {
        this.registerButton.addActionListener(al);
    }

    public void changeWindowFocusOnLaunch() {
        leftPanel.requestFocusInWindow();
    }

    public void clearAllFields() {
        nameTextField.setText("");
        surnameTextField.setText("");
        emailTextField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
    }

    private void hoverEffectButtonManipulation(JButton button, Color color, Color hoverColor) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setForeground(hoverColor);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setForeground(color);
            }
        });
    }

    public void setInformationTextField(String text, Color color) {
        this.informationLabel.setText(text);
        this.informationLabel.setForeground(color);

        Timer t = new Timer(2500, (ActionEvent e) -> {
            informationLabel.setText("");
            informationLabel.setForeground(Color.black);
        });
        t.setRepeats(false);
        t.start();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel appLogo;
    private javax.swing.JPasswordField confirmPasswordField;
    private javax.swing.JLabel confirmPasswordLogo;
    private javax.swing.JLabel emailLogo;
    private javax.swing.JTextField emailTextField;
    private javax.swing.JLabel informationLabel;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JLabel mainLabel;
    private javax.swing.JLabel nameLogo;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLogo;
    private javax.swing.JButton registerButton;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JButton signInButton;
    private javax.swing.JLabel surnameLogo;
    private javax.swing.JTextField surnameTextField;
    // End of variables declaration//GEN-END:variables
}
