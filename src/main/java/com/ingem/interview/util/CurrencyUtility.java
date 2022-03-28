package com.ingem.interview.util;

import com.ingem.interview.model.EurCurrency;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

@Component
public class CurrencyUtility {

    @Value("${hnb.api.url}")
    String hnbApiUrl;
    
    public BigDecimal hrkToEur(BigDecimal hrk) throws ParseException {
        BigDecimal currentEur = getLatesEurCurrency();
        BigDecimal convertedEur = hrk.divide(currentEur,2, BigDecimal.ROUND_HALF_UP);
        return convertedEur;
    }

    public BigDecimal getLatesEurCurrency() throws ParseException {

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        String pattern = "#,##";
        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
        decimalFormat.setParseBigDecimal(true);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<EurCurrency[]> eurCurrency = restTemplate.getForEntity(hnbApiUrl, EurCurrency[].class);
        BigDecimal latestEurCurrency= (BigDecimal) decimalFormat.parse(eurCurrency.getBody()[0].getSrednjiTecaj());
        return latestEurCurrency;
    }

}
