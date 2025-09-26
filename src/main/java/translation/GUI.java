package translation;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            // Initialize Classes
            JSONTranslator translator = new JSONTranslator();
            CountryCodeConverter converter = new CountryCodeConverter();
            LanguageCodeConverter lanConverter = new LanguageCodeConverter();
            List<String> countryCodes = translator.getCountryCodes();
            List<String> languageCodes = translator.getLanguageCodes();

            // Country selection
            JPanel countryPanel = new JPanel();
            JLabel countryLabel = new JLabel("Country:");
            // Create a DefaultListModel
            DefaultListModel<String> listModel = new DefaultListModel<>();

            // Create JList with the model
            JList<String> list = new JList<>(listModel);

            //Convert country codes to country names and store in countryNames
            for (String countryCode : countryCodes) {
                listModel.addElement((converter.fromCountryCode(countryCode)));
            }

            countryPanel.add(countryLabel);

            list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

            // place the JList in a scroll pane so that it is scrollable in the UI
            JScrollPane scrollPane = new JScrollPane(list);
            countryPanel.add(scrollPane, 1);


            // Language selection
            JPanel languagePanel = new JPanel();
            JLabel languageLabel = new JLabel("Language:");

            List<String> languageNames = new ArrayList<>();
            for (String languageCode : languageCodes) {
                String languageName = lanConverter.fromLanguageCode(languageCode);
                languageNames.add(languageName);
            }

            // Create JComboBox with the array
            JComboBox<String> languageCombo = new JComboBox<>(languageNames.toArray(new String[0]));

            languagePanel.add(languageLabel);
            languagePanel.add(languageCombo);

            // The Button
            JPanel buttonPanel = new JPanel();
            JButton submit = new JButton("Submit");
            buttonPanel.add(submit);

            JLabel resultLabelText = new JLabel("Translation:");
            buttonPanel.add(resultLabelText);
            JLabel resultLabel = new JLabel("\t\t\t\t\t\t\t");
            buttonPanel.add(resultLabel);

            // adding listener for when the user clicks the submit button
            submit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    // Get selected country
                    String selectedCountryName = list.getSelectedValue();
                    if (selectedCountryName == null) {
                        resultLabel.setText("Please select a country");
                        return;
                    }

                    // Get selected language
                    String selectedLanguageName = (String) languageCombo.getSelectedItem();
                    if (selectedLanguageName == null) {
                        resultLabel.setText("Please select a language");
                        return;
                    }

                    // Convert country name back to country code
                    String countryCode = null;
                    for (String code : countryCodes) {
                        String countryNameFromCode = converter.fromCountryCode(code);
                        if (selectedCountryName.equals(countryNameFromCode)) {
                            countryCode = code;
                            break;
                        }
                    }

                    // Convert language name back to language code
                    String languageCode = lanConverter.fromLanguage(selectedLanguageName);
                    if (languageCode == null) {
                        // If direct conversion fails, try to extract from display name
                        for (String code : languageCodes) {
                            String languageNameFromCode = lanConverter.fromLanguageCode(code);
                            if (selectedLanguageName.equals(languageNameFromCode)) {
                                languageCode = code;
                                break;
                            }
                        }
                    }

                    if (countryCode == null || languageCode == null) {
                        resultLabel.setText("Error: Could not find code for selection");
                        return;
                    }

                    // Perform translation
                    String translation = translator.translate(countryCode, languageCode);

                    // Check if translation was successful
                    if (translation == null || translation.contains("not implemented")) {
                        resultLabel.setText("Translation not available");
                    } else {
                        resultLabel.setText(translation);
                    }
                }
            });


            // Main frame
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(languagePanel);
            mainPanel.add(countryPanel);
            mainPanel.add(buttonPanel);

            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
}