package translation;

import javax.swing.*;
import java.awt.event.*;

public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Country selection (for now, only Canada)
            JPanel countryPanel = new JPanel();
            JLabel countryLabel = new JLabel("Country:");
            JComboBox<String> countryCombo = new JComboBox<>(new String[]{"can"});
            countryPanel.add(countryLabel);
            countryPanel.add(countryCombo);

            // Language selection (all supported languages from CanadaTranslator)
            JPanel languagePanel = new JPanel();
            JLabel languageLabel = new JLabel("Language:");
            Translator translator = new CanadaTranslator();
            JComboBox<String> languageCombo = new JComboBox<>(
                    translator.getLanguageCodes().toArray(new String[0]));
            languagePanel.add(languageLabel);
            languagePanel.add(languageCombo);

            // Submit button + result label
            JPanel buttonPanel = new JPanel();
            JButton submit = new JButton("Translate");
            JLabel resultLabelText = new JLabel("Translation:");
            JLabel resultLabel = new JLabel(" ");
            buttonPanel.add(submit);
            buttonPanel.add(resultLabelText);
            buttonPanel.add(resultLabel);

            // Action listener for button
            submit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String country = (String) countryCombo.getSelectedItem();
                    String language = (String) languageCombo.getSelectedItem();
                    String result = translator.translate(country, language);
                    if (result == null) {
                        result = "no translation found!";
                    }
                    resultLabel.setText(result);
                }
            });

            // Main frame
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(countryPanel);
            mainPanel.add(languagePanel);
            mainPanel.add(buttonPanel);

            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
}