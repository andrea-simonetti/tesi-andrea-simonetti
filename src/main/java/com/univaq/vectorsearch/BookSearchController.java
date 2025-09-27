package com.univaq.vectorsearch;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
public class BookSearchController {

    private final VectorStore vectorStore;
    private final ChatClient chatClient;

    public BookSearchController(VectorStore vectorStore, ChatClient.Builder chatClientBuilder) {
        this.vectorStore = vectorStore;
        this.chatClient = chatClientBuilder.build();
    }

    @PostMapping("/search")
    public List<String> semanticSearch(@RequestBody String query) {
        return vectorStore.similaritySearch(SearchRequest.query(query).withTopK(2))
                .stream()
                .map(Document::getContent)
                .toList();
    }

    /* momentaneamente non funzionante
    @PostMapping("/enhanced-search")
    public String enhancedSearch(@RequestBody String query) {
        String context = vectorStore.similaritySearch(SearchRequest.query(query).withTopK(3))
                .stream()
                .map(Document::getContent)
                .reduce("", (a, b) -> a + b + "\n");

        return chatClient.prompt()
                .system(context)
                .user(query)
                .call()
                .content();
    } */

    // endpoint CRUD
    @PostMapping("/add")
    public String addBook(@RequestBody Book book) {
        Document doc = new Document(book.toString(), Map.of(
                "title", book.title(),
                "author", book.author(),
                "type", "book"
        ));
        vectorStore.add(List.of(doc));
        return "Book added successfully";
    }

    @GetMapping("/all")
    public List<String> getAllBooks() {
        return vectorStore.similaritySearch(SearchRequest.query("").withTopK(100))
                .stream()
                .map(Document::getContent)
                .toList();
    }

    @DeleteMapping("/by-author/{author}")
    public String deleteByAuthor(@PathVariable String author) {
        List<Document> docs = vectorStore.similaritySearch(
                SearchRequest.query("").withTopK(100)
        );

        List<String> idsToDelete = docs.stream()
                .filter(doc -> doc.getContent().contains("author=" + author))
                .map(Document::getId)
                .toList();

        if (!idsToDelete.isEmpty()) {
            vectorStore.delete(idsToDelete);
            return "Books by " + author + " deleted";
        }
        return "No books found for author: " + author;
    }

    @PostMapping("/search-by-author")
    public List<String> searchByAuthor(@RequestBody String query, @RequestParam String author) {
        SearchRequest request = SearchRequest.query(query)
                .withTopK(5)
                .withFilterExpression("author == '" + author + "'");

        return vectorStore.similaritySearch(request)
                .stream()
                .map(Document::getContent)
                .toList();
    }

}