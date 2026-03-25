package com.example.api;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/insert")
public class ContentIngestionController {

    private final ContentIngestionService service;

    public ContentIngestionController(ContentIngestionService service) {
        this.service = service;
    }

    public ContentIngestionResponse ingest(ContentIngestionRequest request) {
        try {
            return service.ingest(request);
        } catch (Exception e) {
            return new ContentIngestionResponse("FAILED: " + e.getMessage(), 0);
        }
    }
}