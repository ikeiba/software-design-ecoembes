
package es.deusto.sd.client.proxies;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import es.deusto.sd.client.data.Article;
import es.deusto.sd.client.data.Category;
import es.deusto.sd.client.data.Credentials;


@Service
public class RestTemplateServiceProxy implements IAuctionsServiceProxy{

    private final RestTemplate restTemplate;

    @Value("${api.base.url}")
    private String apiBaseUrl;

    public RestTemplateServiceProxy(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String login(Credentials credentials) {
        String url = apiBaseUrl + "/auth/login";
        
        try {
            return restTemplate.postForObject(url, credentials, String.class);
        } catch (HttpStatusCodeException e) {
            switch (e.getStatusCode().value()) {
                case 401 -> throw new RuntimeException("Login failed: Invalid credentials.");
                default -> throw new RuntimeException("Login failed: " + e.getStatusText());
            }
        }
    }
    
    @Override    
    public void logout(String token) {
        String url = apiBaseUrl + "/auth/logout";
        
        try {
            restTemplate.postForObject(url, token, Void.class);
        } catch (HttpStatusCodeException e) {
            switch (e.getStatusCode().value()) {
                case 401 -> throw new RuntimeException("Logout failed: Invalid token.");
                default -> throw new RuntimeException("Logout failed: " + e.getStatusText());
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Category> getAllCategories() {
        String url = apiBaseUrl + "/auctions/categories";
        
        try {
            return restTemplate.getForObject(url, List.class);
        } catch (HttpStatusCodeException e) {
            switch (e.getStatusCode().value()) {
                case 404 -> throw new RuntimeException("No categories found.");
                default -> throw new RuntimeException("Failed to retrieve categories: " + e.getStatusText());
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Article> getArticlesByCategory(String categoryName, String currency) {
        String url = apiBaseUrl + "/auctions/categories/" + categoryName + "/articles?currency=" + currency;
        
        try {
            return restTemplate.getForObject(url, List.class);
        } catch (HttpStatusCodeException e) {
            switch (e.getStatusCode().value()) {
                case 404 -> throw new RuntimeException("Category not found: " + categoryName);
                case 400 -> throw new RuntimeException("Invalid currency: " + currency);
                default -> throw new RuntimeException("Failed to retrieve articles: " + e.getStatusText());
            }
        }
    }

    @Override
    public Article getArticleDetails(Long articleId, String currency) {
        String url = apiBaseUrl + "/auctions/articles/" + articleId + "/details?currency=" + currency;
        
        try {
            return restTemplate.getForObject(url, Article.class);
        } catch (HttpStatusCodeException e) {
            switch (e.getStatusCode().value()) {
                case 404 -> throw new RuntimeException("Article not found: ID " + articleId);
                case 400 -> throw new RuntimeException("Invalid currency: " + currency);
                default -> throw new RuntimeException("Failed to retrieve article details: " + e.getStatusText());
            }
        }
    }
    
    @Override
    public void makeBid(Long articleId, Float amount, String currency, String token) {
    	String url = apiBaseUrl + "/auctions/articles/" + articleId + "/bid?amount=" +  amount + "&currency=" + currency;
        
        try {
            restTemplate.postForObject(url, token, Void.class);
        } catch (HttpStatusCodeException e) {
            switch (e.getStatusCode().value()) {
                case 401 -> throw new RuntimeException("User not authenticated");
                case 404 -> throw new RuntimeException("Article not found");
                case 400 -> throw new RuntimeException("Invalid currency: " + currency);
                case 409 -> throw new RuntimeException("Bid amount must be greater than the current price");
                case 204 -> { /* Successful bid */ }
                case 500 -> throw new RuntimeException("Internal server error while processing bid");
                default -> throw new RuntimeException("Bid failed with status code: " + e.getStatusCode());
            }
        }
    }
}