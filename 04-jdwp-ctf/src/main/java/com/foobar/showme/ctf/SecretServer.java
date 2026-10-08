package com.foobar.showme.ctf;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Serwer do dwuetapowego ćwiczenia z remote debuggingu.
 *
 * ETAP 1 (rozgrzewka): usługa /sum dla niektórych zapytań zwraca zły wynik. Nie masz
 *   logów — podpinasz debugger do żywego procesu i namierzasz przyczynę.
 * ETAP 2 (CTF): ten sam dostęp debuggera pozwala odczytać pamięć procesu i wykonać
 *   w nim dowolny kod. Serwer chowa flagę (przez HTTP widać tylko gwiazdki) — wyciągasz
 *   ją debuggerem i weryfikujesz pod /verify.
 *
 * Sens: nieszkodliwe "podłączenie się do debugowania" to w rzeczywistości pełne
 * przejęcie procesu. Dlatego JDWP nigdy nie wystawiamy publicznie.
 */
public class SecretServer {

    // Sekret żyje w pamięci procesu — API go nie zwraca, ale debugger go zobaczy.
    private static final String FLAG =
            System.getenv().getOrDefault("WORKSHOP_FLAG", "FLAG{jdwp-lokalny-przyklad}");

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", SecretServer::index);
        server.createContext("/sum", SecretServer::handleSum);
        server.createContext("/verify", SecretServer::verify);
        server.start();
        System.out.println("SecretServer: http://localhost:8080/  (etap 1: /sum?a=2&b=3, etap 2: /verify?flag=...)");
    }

    private static void index(HttpExchange exchange) throws IOException {
        // Przez HTTP wyciekają tylko gwiazdki. Prawdziwa flaga jest tylko w procesie.
        String masked = "FLAG{" + "*".repeat(Math.max(0, FLAG.length() - 6)) + "}";
        String body = """
                === Remote debug: 2 etapy ===

                ETAP 1 — znajdź bug:
                  /sum?a=2&b=3   -> 5  (ok)
                  /sum?a=2&b=-3  -> zły wynik. Podłącz debugger i ustal, dlaczego.

                ETAP 2 — ukradnij sekret:
                  Widzisz tylko zamaskowaną flagę: %s
                  Tym samym debuggerem wyciągnij prawdziwą flagę i sprawdź ją pod:
                  /verify?flag=TWOJA_FLAGA

                Podpowiedź: debugger widzi pamięć procesu i potrafi w nim wykonać kod.
                """.formatted(masked);
        respond(exchange, 200, body);
    }

    private static void handleSum(HttpExchange exchange) throws IOException {
        Map<String, String> q = parseQuery(exchange.getRequestURI().getRawQuery());
        int a = Integer.parseInt(q.getOrDefault("a", "0"));
        int b = Integer.parseInt(q.getOrDefault("b", "0"));
        respond(exchange, 200, add(a, b) + "\n");
    }

    static int add(int a, int b) {
        return a + b;
    }

    private static void verify(HttpExchange exchange) throws IOException {
        Map<String, String> q = parseQuery(exchange.getRequestURI().getRawQuery());
        String submitted = q.getOrDefault("flag", "");
        boolean ok = FLAG.equals(submitted);
        respond(exchange, ok ? 200 : 403, ok ? "✅ Brawo! Flaga poprawna.\n" : "❌ Nie ta flaga.\n");
    }

    private static void respond(HttpExchange exchange, int status, String text) throws IOException {
        byte[] body = text.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, body.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(body);
        }
    }

    private static Map<String, String> parseQuery(String raw) {
        java.util.HashMap<String, String> map = new java.util.HashMap<>();
        if (raw == null) return map;
        for (String pair : raw.split("&")) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) {
                map.put(kv[0], java.net.URLDecoder.decode(kv[1], StandardCharsets.UTF_8));
            }
        }
        return map;
    }
}
