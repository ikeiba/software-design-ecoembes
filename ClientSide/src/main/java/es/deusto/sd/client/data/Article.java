package es.deusto.sd.client.data;

import java.util.Date;

public record Article(
	    Long id,
	    String title,
	    Float initialPrice,
	    Float currentPrice,
	    Integer bids,
	    Date auctionEnd,
	    String categoryName,
	    String ownerName,
	    String currency
	) {}