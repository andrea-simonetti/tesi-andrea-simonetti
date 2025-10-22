package com.univaq.vectorsearch.Controller;

import com.univaq.vectorsearch.Model.Agent;
import com.univaq.vectorsearch.Repository.AgentRepository;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/agent")
public class AgentController {

    private final VectorStore vectorStore;
    private final AgentRepository agentRepository;

    public AgentController(VectorStore vectorStore, AgentRepository agentRepository) {
        this.vectorStore = vectorStore;
        this.agentRepository = agentRepository;
    }

    @PostMapping("/add")
    public Agent addAgent(@RequestBody Agent agent) {
        Agent saved = agentRepository.save(agent);

        Document doc = new Document(
                agent.getName() + " " + agent.getDescription()
        );
        vectorStore.add(List.of(doc));

        return saved;
    }

    @PostMapping("/search")
    public List<String> semanticSearch(@RequestBody String request) {
        return extractContent(vectorStore.similaritySearch(
                SearchRequest.query(request).withTopK(1)
        ));
    }

    private List<String> extractContent(List<Document> documents) {
        return documents.stream()
                .map(Document::getContent)
                .toList();
    }
}