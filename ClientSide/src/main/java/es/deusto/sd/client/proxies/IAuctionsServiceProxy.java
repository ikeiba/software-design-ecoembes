
package es.deusto.sd.client.proxies;

import java.util.List;

import es.deusto.sd.client.data.Article;
import es.deusto.sd.client.data.Category;
import es.deusto.sd.client.data.Credentials;


public interface IAuctionsServiceProxy {
	// Method for user login
	String login(Credentials credentials);

	// Method for user logout
	void logout(String token);

	// Method to retrieve all categories
	List<Category> getAllCategories();

	// Method to retrieve articles by category name
	List<Article> getArticlesByCategory(String categoryName, String currency);

	// Method to get details of a specific article by ID
	Article getArticleDetails(Long articleId, String currency);

	// Method to place a bid on an article
	void makeBid(Long articleId, Float amount, String currency, String token);
}

