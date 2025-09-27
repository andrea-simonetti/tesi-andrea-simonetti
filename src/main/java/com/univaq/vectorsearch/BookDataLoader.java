package com.univaq.vectorsearch;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookDataLoader /*implements CommandLineRunner*/ {  //TOGLI COMMENTO PER CARICARE I LIBRI QUI SOTTO ALL'AVVIO

    private final VectorStore vectorStore;

    public BookDataLoader(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }
 //################################################################################################
   // @Override    //TOGLI COMMENTO PER CARICARE I LIBRI QUI SOTTO ALL'AVVIO
 //################################################################################################
    public void run(String... args) throws Exception {
        // Controlla se ci sono già dati
        try {
            var existing = vectorStore.similaritySearch(SearchRequest.query("test").withTopK(1));
            if (!existing.isEmpty()) {
                System.out.println("Dati già presenti nel vector store, skip caricamento");
                return;
            }
        } catch (Exception e) {
            // Se c'è un errore nella ricerca, procedi con il caricamento
            System.out.println("Vector store vuoto o errore nella verifica, procedo con il caricamento");
        }

        // Dati di esempio dei libri
        var books = List.of(
                new Book("Diabolik", "Bella e Luciana Giussani",
                        "Diabolik è un fumetto italiano creato dalle sorelle Giussani nel 1962. Le storie seguono le avventure del ladro mascherato Diabolik e della sua complice Eva Kant in una serie di furti e crimini elaborati, ambientati nella fittizia città di Clerville."),
                new Book("Il villaggio di babbo natale", "Mauri Kunnas",
                        "Il villaggio di babbo natale è un libro illustrato per bambini che racconta la vita quotidiana di Babbo Natale, degli elfi e delle renne al Polo Nord. Una storia magica che svela i segreti della preparazione dei regali di Natale."),
                new Book("La bella e la bestia", "Jeanne-Marie Leprince de Beaumont",
                        "La bella e la bestia è una fiaba classica francese del 1740 che narra la storia d'amore tra Belle, una giovane coraggiosa, e una bestia misteriosa. È un racconto sull'importanza di guardare oltre le apparenze e sul potere trasformativo dell'amore."),
                new Book("Guida di una istruzione cinofila", "Roberto Marchesini",
                        "Una carina guida completa all'educazione del cane che copre i principi base dell'addestramento, la comunicazione uomo-cane, le tecniche di rinforzo positivo e i metodi per risolvere i problemi comportamentali più comuni nei cani domestici."),
                new Book("Il signore degli anelli", "J.R.R. Tolkien",
                        "Il Signore degli Anelli è un romanzo fantasy epico pubblicato tra il 1954 e il 1955. Racconta il viaggio di Frodo Baggins per distruggere l'Anello del Potere e sconfiggere il Signore Oscuro Sauron. È considerato il capolavoro del genere fantasy moderno.")
        );

        List<Document> documents = books.stream()
                .map(book -> new Document(book.toString()))
                .toList();

        System.out.println("Caricamento libri nel vector store...");
        vectorStore.add(documents);
        System.out.println("Caricati " + documents.size() + " libri nel vector store!");
    }
}