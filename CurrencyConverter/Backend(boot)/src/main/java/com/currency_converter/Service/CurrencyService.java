package com.currency_converter.Service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class CurrencyService {
 final RestTemplate restTemplate=new RestTemplate();
 
	
	public Map<String,Object> fetchExchangeRates(String base){
		try {
			
			String url=String.format("https://v6.exchangerate-api.com/v6/b30d2f2211e76468001f46aa/latest/%s",base);
			return restTemplate.getForObject(url, Map.class);
		}
			catch (RestClientException e) {
	            throw new RuntimeException("Failed to fetch exchange rates from API.", e);
	        }
	}
	public Map<String, Object> convertCurrency(Map<String, Object> request) {
        try {
            String from = (String) request.get("from");
            String to = (String) request.get("to");
            int amount = (int) request.get("amount");
            System.out.print(from+" "+to);
            String url = String.format("https://v6.exchangerate-api.com/v6/b30d2f2211e76468001f46aa/latest/%s",from);
            Map<String, Map<String, Double>> response = restTemplate.getForObject(url, Map.class);

            if (response == null || !response.containsKey("conversion_rates") || !response.get("conversion_rates").containsKey(to)) {
                throw new IllegalArgumentException("Invalid currency code provided.");
            }
            System.out.println(response.get("base_code"));
            double rate = response.get("conversion_rates").get(to);
            double convertedAmount = (double)amount * rate;

            Map<String, Object> result = new HashMap<>();
            result.put("from", from);
            result.put("to", to);
            result.put("amount", amount);
            result.put("Rate", rate);
            result.put("convertedAmount", convertedAmount);
            return result;
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to fetch conversion data from API.", e);
        } catch (ClassCastException | NullPointerException e) {
            throw new IllegalArgumentException("Invalid input data format.", e);
        }
    }
	
	public Map<String, Object> currencyEquals(Map<String, Object> request){
		String url = String.format("https://v6.exchangerate-api.com/v6/b30d2f2211e76468001f46aa/latest/%s",request.get("from"));
        Map<String, Map<String, Double>> values = restTemplate.getForObject(url, Map.class);
        System.out.print(values.toString());
		Map<String, Object> response=new HashMap<>();
		response.put("from",request.get("from"));
		response.put("to", request.get("to"));
		response.put("amount", request.get("amount"));
		response.put("Rate",values.get("conversion_rates").get(request.get("to")));
		response.put("convertedAmount",request.get("amount"));
		return response;
	}
}
