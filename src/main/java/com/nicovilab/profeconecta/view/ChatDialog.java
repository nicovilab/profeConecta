package com.nicovilab.profeconecta.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatDialog extends JDialog {

    private JPanel messagesPanel;
    private JScrollPane scrollPane;
    private JTextField messageField;
    private JButton sendButton;
    private JButton attachButton;
    private ChatSendListener sendListener;

    public ChatDialog(Frame owner, String title) {
        super(owner, title, true);
        initUI();
    }

    private void initUI() {
        setSize(500, 600);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());

        messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        messagesPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        messagesPanel.setBackground(Color.WHITE);

        scrollPane = new JScrollPane(messagesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        inputPanel.setBackground(Color.WHITE);

        messageField = new JTextField();
        messageField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        inputPanel.add(messageField, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonsPanel.setBackground(Color.WHITE);

        attachButton = new JButton(new ImageIcon(getClass().getResource("/images/attachFile16px.png")));
        sendButton = new JButton(new ImageIcon(getClass().getResource("/images/sendChat16px.png")));

        attachButton.setBorderPainted(false);
        attachButton.setContentAreaFilled(false);
        attachButton.setOpaque(true);
        attachButton.setBackground(Color.WHITE);

        sendButton.setBorderPainted(false);
        sendButton.setContentAreaFilled(false);
        sendButton.setOpaque(true);
        sendButton.setBackground(Color.WHITE);

        buttonsPanel.add(attachButton);
        buttonsPanel.add(sendButton);

        inputPanel.add(buttonsPanel, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());
        attachButton.addActionListener(e -> chooseFile());

        getRootPane().registerKeyboardAction(e -> dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private void sendMessage() {
        String text = messageField.getText().trim();
        if (!text.isEmpty() && sendListener != null) {
            sendListener.onSendText(text);
            messageField.setText("");
        }
    }

    private void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION && sendListener != null) {
            File file = fileChooser.getSelectedFile();
            sendListener.onSendFile(file);
        }
    }

    public void setChatSendListener(ChatSendListener listener) {
        this.sendListener = listener;
    }

    public void addMessage(String contenido, boolean esMio, String nombreEmisor, LocalDateTime fechaHora) {
        addMessage(contenido, esMio, nombreEmisor, fechaHora, null);
    }

    public void addMessage(String contenido, boolean esMio, String nombreEmisor, LocalDateTime fechaHora, byte[] archivoAdjunto) {
        JPanel messageWrapper = new JPanel();
        messageWrapper.setLayout(new BoxLayout(messageWrapper, BoxLayout.Y_AXIS));
        messageWrapper.setOpaque(false);
        messageWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        messageWrapper.setMaximumSize(new Dimension(450, Integer.MAX_VALUE));

        JLabel headerLabel = new JLabel(nombreEmisor + "  " + fechaHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        headerLabel.setFont(new Font("Arial", Font.BOLD, 12));
        headerLabel.setForeground(Color.GRAY);
        headerLabel.setAlignmentX(esMio ? Component.RIGHT_ALIGNMENT : Component.LEFT_ALIGNMENT);

        MessageBubble bubble;
        if (archivoAdjunto != null) {
            bubble = new MessageBubble("[Archivo recibido: " + contenido + "]", esMio);
            bubble.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            bubble.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        File file = new File(System.getProperty("user.home") + File.separator + "Downloads" + File.separator + contenido.replaceAll("[\\\\/:*?\"<>|]", "_"));
                        Files.write(file.toPath(), archivoAdjunto);
                        Desktop.getDesktop().open(file);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(ChatDialog.this, "Error al guardar el archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        } else {
            bubble = new MessageBubble(contenido, esMio);
        }

        bubble.setAlignmentX(esMio ? Component.RIGHT_ALIGNMENT : Component.LEFT_ALIGNMENT);
        messageWrapper.add(headerLabel);
        messageWrapper.add(Box.createVerticalStrut(3));
        messageWrapper.add(bubble);

        messagesPanel.add(messageWrapper);
        messagesPanel.add(Box.createVerticalStrut(10));

        messagesPanel.revalidate();
        messagesPanel.repaint();
        scrollToBottom();
    }

    private void scrollToBottom() {
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    public interface ChatSendListener {

        void onSendText(String text);

        void onSendFile(File file);
    }

    private static class MessageBubble extends JPanel {

        private final boolean isMine;

        public MessageBubble(String text, boolean isMine) {
            this.isMine = isMine;
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            setOpaque(false);

            int bubbleWidth = 280;

            JTextArea messageArea = new JTextArea(text);
            messageArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            messageArea.setLineWrap(true);
            messageArea.setWrapStyleWord(true);
            messageArea.setEditable(false);
            messageArea.setOpaque(false);
            messageArea.setBorder(null);
            messageArea.setFocusable(false);

            messageArea.setColumns(1);

            messageArea.setMaximumSize(new Dimension(bubbleWidth, Integer.MAX_VALUE));
            messageArea.setAlignmentX(Component.LEFT_ALIGNMENT);

            JPanel bubble = new RoundedPanel(isMine ? new Color(179, 229, 252) : new Color(230, 230, 230));
            bubble.setLayout(new BorderLayout());
            bubble.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            bubble.add(messageArea, BorderLayout.CENTER);
            bubble.setMaximumSize(new Dimension(bubbleWidth + 30, Integer.MAX_VALUE));
            bubble.setOpaque(false);

            if (isMine) {
                add(Box.createHorizontalGlue());
                add(bubble);
            } else {
                add(bubble);
                add(Box.createHorizontalGlue());
            }

            setAlignmentX(Component.LEFT_ALIGNMENT);
            setMaximumSize(new Dimension(bubbleWidth + 60, Integer.MAX_VALUE));
        }
    }

    private static class RoundedPanel extends JPanel {

        private final Color backgroundColor;
        private static final int ARC_WIDTH = 20;
        private static final int ARC_HEIGHT = 20;

        public RoundedPanel(Color bgColor) {
            this.backgroundColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), ARC_WIDTH, ARC_HEIGHT);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
