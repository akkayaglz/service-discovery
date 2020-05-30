package com.api.bookcatalogservice.service;

import com.api.bookcatalogservice.models.Book;
import com.api.bookcatalogservice.models.Catalog;
import com.api.bookcatalogservice.models.Rating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BookInfo {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "getFallbackCatalogItem")
    public Catalog getCatalogItem(Rating rating) {
        Book book = restTemplate.getForObject("http://book-info-service/books/" + rating.getBookId(), Book.class);
        return new Catalog(book.getName(), book.getDescription(), rating.getRating());
    }

    private Catalog getFallbackCatalogItem(Rating rating) {
        return new Catalog("Book name not", "", rating.getRating());
    }

}