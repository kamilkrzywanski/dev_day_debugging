package com.foobar.showme.dckr;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * "Works on my machine": lokalnie liczy cenę brutto poprawnie (VAT 23%),
 * a w kontenerze pokazuje cenę = netto. Dlaczego?
 *
 * Aplikacja działa w pętli, żeby dało się podpiąć debugger do procesu w kontenerze.
 */
public class PriceApp {

    private static final Path CONFIG = Path.of("config", "app.properties");

    public static void main(String[] args) throws Exception {
        double net = 100.0;
        while (true) {
            double rate = loadTaxRate();
            double gross = net * (1.0 + rate);
            System.out.printf("netto=%.2f  vat=%.0f%%  brutto=%.2f%n", net, rate * 100, gross);
            Thread.sleep(3000);
        }
    }

    static double loadTaxRate() {
        // Config czytany z pliku obok aplikacji (./config/app.properties).
        // Jeśli pliku nie ma — po cichu wracamy do 0.
        if (!Files.exists(CONFIG)) {
            return 0.0; // <-- tu ląduje wykonanie w kontenerze: pliku nikt nie skopiował
        }
        try (InputStream in = Files.newInputStream(CONFIG)) {
            Properties props = new Properties();
            props.load(in);
            return Double.parseDouble(props.getProperty("tax.rate", "0.0"));
        } catch (Exception e) {
            return 0.0;
        }
    }
}
