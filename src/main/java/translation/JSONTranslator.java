package translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface that reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    private final List<String> languageCodes = new ArrayList<>();

    private final List<String> countryCodes = new ArrayList<>();

    // the key used is "countryCode-languageCode"; the value is the translated country name
    private final Map<String, String> translations = new HashMap<>();
    /**
     * Construct a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Construct a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject countryData = jsonArray.getJSONObject(i);
                String countryCode = countryData.getString("alpha3");

                List<String> languages = new ArrayList<>();

                this.countryCodes.add(countryCode);

                // iterate through the other keys to get the information that we need
                for (String key : countryData.keySet()) {
                    if (!key.equals("id") && !key.equals("alpha2") && !key.equals("alpha3")) {
                        String languageCode = key;
                        this.translations.put(countryCode + "-" + languageCode, countryData.getString(key));

                        if (!this.languageCodes.contains(languageCode)) {
                            this.languageCodes.add(languageCode);
                        }
                    }
                }
            }
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Debug method to check if language codes are being processed correctly
     */
    public void debugLanguageCodes() {
        System.out.println("=== JSONTranslator Debug ===");
        System.out.println("Total countries: " + countryCodes.size());
        System.out.println("Total language codes: " + languageCodes.size());
        System.out.println("Total translations: " + translations.size());

        System.out.println("First 10 country codes: " + countryCodes.subList(0, Math.min(10, countryCodes.size())));
        System.out.println("All language codes: " + languageCodes);

        // Test some translations
        if (!countryCodes.isEmpty() && !languageCodes.isEmpty()) {
            String testCountry = countryCodes.get(0);
            String testLanguage = languageCodes.get(0);
            String translation = translate(testCountry, testLanguage);
            System.out.println("Test translation for " + testCountry + "-" + testLanguage + ": " + translation);
        }
    }
    @Override
    public List<String> getLanguageCodes() {
        ArrayList<String> languagecodes_copy = new ArrayList<>();
        for (int i = 0; i < languageCodes.size(); i++) {
            languagecodes_copy.add(languageCodes.get(i));
        }
        return languagecodes_copy;
    }

    @Override
    public List<String> getCountryCodes() {
        return new ArrayList<>(countryCodes);
    }

    @Override
    public String translate(String countryCode, String languageCode) {
        String key = countryCode + "-" + languageCode;
        if (this.translations.containsKey(key)) {
            return this.translations.get(key);
        }
        return "JSONTranslator's translate method is not implemented!";
    }
}
