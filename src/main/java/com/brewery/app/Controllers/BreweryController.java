package com.brewery.app.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

@RestController
public class BreweryController {

    @GetMapping("/search")
    public ResponseEntity<String> searchBrewery(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String type,
            @RequestParam(required = false, defaultValue = "10") int resultsPerPage,
            @RequestParam(required = false) String id
    ) {

        if(id == null || id.isEmpty()) {
            return ResponseEntity.status(400).body("ID hiányzik az URL-ből.");
        }

        StringBuilder url = new StringBuilder("https://api.openbrewerydb.org/v1/breweries?");
        boolean hasParameter = false;

        if (country != null && !country.isEmpty()) {
            url.append("by_country=").append(country);
            hasParameter = true;
        }

        if (type != null && !type.isEmpty()) {
            if (hasParameter) url.append("&");
            url.append("by_type=").append(type);
            hasParameter = true;
        }

        if(hasParameter) url.append("&");

        url.append("per_page=").append(resultsPerPage);

        Response response;
        try {
            response = given().get(url.toString());
        }catch (Exception e) {
            return ResponseEntity.status(500).body("Hiba történt a külső API hívásakor.");
        }

        boolean found = response.jsonPath().getList("id", String.class).contains(id);

        StringBuilder result = new StringBuilder();

        if(found) {
            result.append("Van találat a keresett ID-ra!\n\n");
            result.append("Ország: ").append(country != null ? country : "Unknown").append("\n");
            result.append("Típus: ").append(type != null ? type : "Unknown").append("\n");
            result.append("Elem/oldal: ").append(resultsPerPage).append("\n");
            result.append("Keresett ID: ").append(id).append("\n");
        } else {
            result.append("Nincs találat a keresett ID-ra. \n\n");
            result.append("Ország: ").append(country != null ? country : "Unknown").append("\n");
            result.append("Típus: ").append(type != null ? type : "Unknown").append("\n");
            result.append("Elem/oldal: ").append(resultsPerPage).append("\n");
            result.append("Keresett ID: ").append("FAKE ID").append("\n");
        }

        if(found) {
            return ResponseEntity.status(200).body(result.toString());
        } else {
            return ResponseEntity.status(404).body(result.toString());
        }
    }
}
