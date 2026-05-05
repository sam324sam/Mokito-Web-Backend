package com.mokito.backend.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.mokito.backend.gui.helpers.CustomFontLoader;

import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ServerGui {

    private static final String FONT_KEY = "/fonts/upheavtt.ttf";

    // Paleta de colores
    private static final Color BG_DARK      = Color.decode("#1a1a2e");
    private static final Color BG_PANEL     = Color.decode("#16213e");
    private static final Color BG_LOG       = Color.decode("#0f3460");
    private static final Color ACCENT       = Color.decode("#f4c064");
    private static final Color TEXT_PRIMARY = Color.decode("#e0e0e0");
    private static final Color TEXT_MUTED   = Color.decode("#8892a4");
    private static final Color SUCCESS      = Color.decode("#4caf50");
    private static final Color ERROR_COLOR  = Color.decode("#f44336");

    private final JTextArea logsArea;
    private final JLabel statusDot;
    private final JLabel statusLabel;
    private final DefaultListModel<String> playersModel;
    private final JLabel playersCountLabel;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public ServerGui() {
        logsArea          = new JTextArea();
        statusDot         = new JLabel("●");
        statusLabel       = new JLabel("Iniciando...");
        playersModel      = new DefaultListModel<>();
        playersCountLabel = new JLabel("0 conectados");

        JFrame frame = new JFrame("Mokito Server");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(1280, 620);
        frame.setLocationRelativeTo(null);
        frame.setBackground(BG_DARK);

        // Root 
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_DARK);

        // Header 
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_DARK);
        header.setBorder(new EmptyBorder(20, 24, 16, 24));

        JLabel title = new JLabel("Mokito Server");
        title.setFont(CustomFontLoader.loadFont(FONT_KEY, 28));
        title.setForeground(ACCENT);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        statusPanel.setOpaque(false);
        statusDot.setFont(new Font("SansSerif", Font.PLAIN, 14));
        statusDot.setForeground(TEXT_MUTED);
        statusLabel.setFont(CustomFontLoader.loadFont(FONT_KEY, 13));
        statusLabel.setForeground(TEXT_MUTED);
        statusPanel.add(statusDot);
        statusPanel.add(statusLabel);

        header.add(title, BorderLayout.WEST);
        header.add(statusPanel, BorderLayout.EAST);

        // Separador 
        JSeparator separator = new JSeparator();
        separator.setForeground(BG_LOG);
        separator.setBackground(BG_LOG);

        // Log panel (izquierda) 
        JPanel logContainer = new JPanel(new BorderLayout());
        logContainer.setBackground(BG_PANEL);
        logContainer.setBorder(new EmptyBorder(16, 24, 16, 12));

        JLabel logsTitle = new JLabel("Logs del servidor");
        logsTitle.setFont(CustomFontLoader.loadFont(FONT_KEY, 13));
        logsTitle.setForeground(TEXT_MUTED);
        logsTitle.setBorder(new EmptyBorder(0, 0, 10, 0));

        logsArea.setEditable(false);
        logsArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        logsArea.setForeground(TEXT_PRIMARY);
        logsArea.setBackground(BG_LOG);
        logsArea.setCaretColor(ACCENT);
        logsArea.setBorder(new EmptyBorder(12, 14, 12, 14));
        logsArea.setLineWrap(true);
        logsArea.setWrapStyleWord(true);

        JScrollPane logScroll = new JScrollPane(logsArea);
        logScroll.setBorder(BorderFactory.createLineBorder(BG_LOG, 1));
        logScroll.getVerticalScrollBar().setBackground(BG_PANEL);
        logScroll.setBackground(BG_LOG);

        logContainer.add(logsTitle, BorderLayout.NORTH);
        logContainer.add(logScroll, BorderLayout.CENTER);

        // Players panel (derecha)
        JPanel playersContainer = new JPanel(new BorderLayout());
        playersContainer.setBackground(BG_PANEL);
        playersContainer.setBorder(new EmptyBorder(16, 12, 16, 24));
        playersContainer.setPreferredSize(new Dimension(280, 0));

        // Cabecera del panel de jugadores
        JPanel playersTitleRow = new JPanel(new BorderLayout());
        playersTitleRow.setOpaque(false);
        playersTitleRow.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel playersTitle = new JLabel("Jugadores");
        playersTitle.setFont(CustomFontLoader.loadFont(FONT_KEY, 13));
        playersTitle.setForeground(TEXT_MUTED);

        playersCountLabel.setFont(CustomFontLoader.loadFont(FONT_KEY, 12));
        playersCountLabel.setForeground(ACCENT);

        playersTitleRow.add(playersTitle, BorderLayout.WEST);
        playersTitleRow.add(playersCountLabel, BorderLayout.EAST);

        // Lista de jugadores
        JList<String> playersList = new JList<>(playersModel);
        playersList.setBackground(BG_LOG);
        playersList.setForeground(TEXT_PRIMARY);
        playersList.setFont(CustomFontLoader.loadFont(FONT_KEY, 13));
        playersList.setSelectionBackground(ACCENT);
        playersList.setSelectionForeground(BG_DARK);
        playersList.setBorder(new EmptyBorder(8, 10, 8, 10));
        playersList.setCellRenderer(new PlayerCellRenderer());

        JScrollPane playersScroll = new JScrollPane(playersList);
        playersScroll.setBorder(BorderFactory.createLineBorder(BG_LOG, 1));
        playersScroll.getVerticalScrollBar().setBackground(BG_PANEL);
        playersScroll.setBackground(BG_LOG);

        playersContainer.add(playersTitleRow, BorderLayout.NORTH);
        playersContainer.add(playersScroll, BorderLayout.CENTER);

        // Centro: logs + jugadores
        JPanel centerContent = new JPanel(new BorderLayout());
        centerContent.setBackground(BG_PANEL);
        centerContent.add(logContainer, BorderLayout.CENTER);
        centerContent.add(playersContainer, BorderLayout.EAST);

        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setBackground(BG_PANEL);
        centerWrapper.add(separator, BorderLayout.NORTH);
        centerWrapper.add(centerContent, BorderLayout.CENTER);

        // Footer 
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 10));
        footer.setBackground(BG_DARK);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BG_LOG));
        footer.add(makeFooterChip("Puerto", "8080"));
        footer.add(makeFooterChip("Perfil", "prod"));

        // Ensamblar 
        root.add(header, BorderLayout.NORTH);
        root.add(centerWrapper, BorderLayout.CENTER);
        root.add(footer, BorderLayout.SOUTH);

        frame.setContentPane(root);
        frame.setVisible(true);
    }

    /**
     * Mostrar logs
     * @param message
     */
    public void log(String message) {
        String timestamp = LocalTime.now().format(timeFormatter);
        String line = "[" + timestamp + "] " + message + "\n";
        SwingUtilities.invokeLater(() -> {
            logsArea.append(line);
            logsArea.setCaretPosition(logsArea.getDocument().getLength());
        });
    }

    public void setStatus(boolean online) {
        SwingUtilities.invokeLater(() -> {
            if (online) {
                statusDot.setForeground(SUCCESS);
                statusLabel.setForeground(SUCCESS);
                statusLabel.setText("Online");
            } else {
                statusDot.setForeground(ERROR_COLOR);
                statusLabel.setForeground(ERROR_COLOR);
                statusLabel.setText("Offline");
            }
        });
    }

    public void addPlayer(String playerName) {
        SwingUtilities.invokeLater(() -> {
            if (!playersModel.contains(playerName)) {
                playersModel.addElement(playerName);
                updatePlayersCount();
                log("➕ " + playerName + " se ha conectado");
            }
        });
    }

    public void removePlayer(String playerName) {
        SwingUtilities.invokeLater(() -> {
            playersModel.removeElement(playerName);
            updatePlayersCount();
            log("➖ " + playerName + " se ha desconectado");
        });
    }

    // Helpers

    private void updatePlayersCount() {
        int count = playersModel.getSize();
        playersCountLabel.setText(count + " conectado" + (count == 1 ? "" : "s"));
    }

    private JLabel makeFooterChip(String key, String value) {
        JLabel label = new JLabel(key + ":  " + value);
        label.setFont(CustomFontLoader.loadFont(FONT_KEY, 12));
        label.setForeground(TEXT_MUTED);
        return label;
    }

    // Cell renderer con punto verde
    private static class PlayerCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(
                JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

            JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);

            label.setText("● " + value);
            label.setBorder(new EmptyBorder(6, 8, 6, 8));

            if (!isSelected) {
                label.setBackground(index % 2 == 0
                        ? Color.decode("#0f3460")
                        : Color.decode("#112a52"));
                label.setForeground(Color.decode("#e0e0e0"));
            }

            return label;
        }
    }
}