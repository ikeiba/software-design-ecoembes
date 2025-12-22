package es.deusto.sd.client.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.deusto.sd.client.data.Article;
import es.deusto.sd.client.data.Category;
import es.deusto.sd.client.data.Credentials;
import es.deusto.sd.client.proxies.IAuctionsServiceProxy;
import jakarta.servlet.http.HttpServletRequest;


@Controller
public class WebClientController {

	@Autowired
	private IAuctionsServiceProxy auctionsServiceProxy;

	private String token; // Stores the session token

	// Add current URL and token to all views
	@ModelAttribute
	public void addAttributes(Model model, HttpServletRequest request) {
		String currentUrl = ServletUriComponentsBuilder.fromRequestUri(request).toUriString();
		model.addAttribute("currentUrl", currentUrl); // Makes current URL available in all templates
		model.addAttribute("token", token); // Makes token available in all templates
	}

	@GetMapping("/")
	public String home(Model model) {
		List<Category> categories;

		try {
			categories = auctionsServiceProxy.getAllCategories();
			model.addAttribute("categories", categories);
		} catch (RuntimeException e) {
			model.addAttribute("errorMessage", "Failed to load categories: " + e.getMessage());
		}

		return "index";
	}

	@GetMapping("/login")
	public String showLoginPage(@RequestParam(value = "redirectUrl", required = false) String redirection,
								Model model) {
		// Add redirectUrl to the model if needed
		model.addAttribute("redirectUrl", redirection);

		return "login"; // Return your login template
	}

	@PostMapping("/login")
	public String performLogin(@RequestParam("email") String userEmail, 
							   @RequestParam("password") String userPassword,
							   @RequestParam(value = "redirectUrl", required = false) String redirection, 
							   Model model) {
		Credentials credentials = new Credentials(userEmail, userPassword);

		try {
			token = auctionsServiceProxy.login(credentials);

			// Redirect to the original page or root if redirectUrl is null
			return "redirect:" + (redirection != null && !redirection.isEmpty() ? redirection : "/");
		} catch (RuntimeException e) {
			model.addAttribute("errorMessage", "Login failed: " + e.getMessage());
			return "login"; // Return to login page with error message
		}
	}

	@GetMapping("/logout")
	public String performLogout(@RequestParam(value = "redirectUrl", defaultValue = "/") String redirection,
								Model model) {
		try {
			auctionsServiceProxy.logout(token);
			token = null; // Clear the token after logout
			model.addAttribute("successMessage", "Logout successful.");
		} catch (RuntimeException e) {
			model.addAttribute("errorMessage", "Logout failed: " + e.getMessage());
		}

		// Redirect to the specified URL after logout
		return "redirect:" + redirection;
	}

	@GetMapping("/category/{name}")
	public String getCategoryArticles(@PathVariable("name") String categoryName,
									  @RequestParam(value = "currency", defaultValue = "EUR") String selectedCurrency, 
									  Model model) {
		List<Article> articles;

		try {
			articles = auctionsServiceProxy.getArticlesByCategory(categoryName, selectedCurrency);
			model.addAttribute("articles", articles);
			model.addAttribute("categoryName", categoryName);
			model.addAttribute("selectedCurrency", selectedCurrency);
		} catch (RuntimeException e) {
			model.addAttribute("errorMessage", "Failed to load articles for category: " + e.getMessage());
			model.addAttribute("articles", null);
			model.addAttribute("categoryName", categoryName);
			model.addAttribute("selectedCurrency", "EUR");
		}

		return "category";
	}

	@GetMapping("/article/{id}")
	public String getArticleDetails(@PathVariable("id") Long productId,
									@RequestParam(value = "currency", defaultValue = "EUR") String selectedCurrency,
									Model model) {
		Article article;

		try {
			article = auctionsServiceProxy.getArticleDetails(productId, selectedCurrency);
			model.addAttribute("article", article);
			model.addAttribute("selectedCurrency", selectedCurrency);
		} catch (RuntimeException e) {
			model.addAttribute("errorMessage", "Failed to load article details: " + e.getMessage());
			model.addAttribute("article", null);
			model.addAttribute("selectedCurrency", "EUR");
		}

		return "article";
	}

	@PostMapping("/bid")
	public String makeBid(@RequestParam("id") Long productId, 
						  @RequestParam("amount") Float bidAmount,
						  @RequestParam(value = "currency", defaultValue = "EUR") String selectedCurrency,
						  Model model,
						  RedirectAttributes redirectAttributes) {
		try {
			auctionsServiceProxy.makeBid(productId, bidAmount, selectedCurrency, token);
			// RedirectAttributes are used to pass attributes to the redirected page
			// Add a success message to be displayed in the article view
			redirectAttributes.addFlashAttribute("successMessage", "Bid placed successfully!");
		} catch (RuntimeException e) {
			// Add an error message to be displayed in the article view
			redirectAttributes.addFlashAttribute("errorMessage", "Failed to place bid: " + e.getMessage());
		}

		return "redirect:/article/" + productId + "?currency=" + selectedCurrency;
	}
}