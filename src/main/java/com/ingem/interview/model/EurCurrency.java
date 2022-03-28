package com.ingem.interview.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

@Component
public class EurCurrency {

    @JsonProperty("broj_tecajnice")
    public String brojTecajnice;

    @JsonProperty("datum_primjene")
    public String datumPrimjene;

    @JsonProperty("drzava")
    public String drzava;

    @JsonProperty("drzava_iso")
    public String drzavaIso;

    @JsonProperty("sifra_valute")
    public String sifraValute;

    @JsonProperty("valuta")
    public String valuta;

    @JsonProperty("jedinica")
    public String jedinica;

    @JsonProperty("kupovni_tecaj")
    public String kupovniTecaj;

    @JsonProperty("srednji_tecaj")
    public String srednjiTecaj;

    @JsonProperty("prodajni_tecaj")
    public String prodajniTecaj;

    public String getBrojTecajnice() {
        return brojTecajnice;
    }

    public String getDatumPrimjene() {
        return datumPrimjene;
    }

    public String getDrzava() {
        return drzava;
    }

    public String getDrzavaIso() {
        return drzavaIso;
    }

    public String getSifraValute() {
        return sifraValute;
    }

    public String getValuta() {
        return valuta;
    }

    public String getJedinica() {
        return jedinica;
    }

    public String getKupovniTecaj() {
        return kupovniTecaj;
    }

    public String getSrednjiTecaj() {
        return srednjiTecaj;
    }

    public String getProdajniTecaj() {
        return prodajniTecaj;
    }
}
