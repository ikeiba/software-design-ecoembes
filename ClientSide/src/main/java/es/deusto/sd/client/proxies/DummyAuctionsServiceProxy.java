package es.deusto.sd.client.proxies;

//PARA PROBAR SIN BACKEND CORRIENDO: 

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import es.deusto.sd.client.data.Article;
import es.deusto.sd.client.data.Category;
import es.deusto.sd.client.data.Credentials;

@Service
@Primary
public class DummyAuctionsServiceProxy implements IAuctionsServiceProxy {

    @Override
    public String login(Credentials credentials) {
        return "dummy-token";
    }

    @Override
    public void logout(String token) {}

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Electronics"));
        categories.add(new Category("Books"));
        return categories;
    }

    @Override
    public List<Article> getArticlesByCategory(String categoryName, String currency) {
        return new ArrayList<>();
    }

    @Override
    public Article getArticleDetails(Long articleId, String currency) {
        return null;
    }

    @Override
    public void makeBid(Long articleId, Float amount, String currency, String token) {}
}
