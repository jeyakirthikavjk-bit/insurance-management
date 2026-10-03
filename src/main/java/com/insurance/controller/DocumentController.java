package com.insurance.controller;

import com.insurance.entity.Document;
import com.insurance.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "*")
public class DocumentController {

    @Autowired private DocumentService documentService;

    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments() {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.getDocumentById(id));
    }

    @PostMapping("/upload")
    public ResponseEntity<Document> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "documentType", defaultValue = "OTHER") Document.DocumentType documentType,
            @RequestParam(value = "uploadedBy", defaultValue = "System") String uploadedBy,
            @RequestParam(value = "customerId", required = false) Long customerId,
            @RequestParam(value = "policyId", required = false) Long policyId,
            @RequestParam(value = "claimId", required = false) Long claimId) {
        try {
            Document document = documentService.uploadDocument(
                    file, name, description, documentType, uploadedBy, customerId, policyId, claimId);
            return ResponseEntity.status(HttpStatus.CREATED).body(document);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload document: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long id) {
        try {
            Document document = documentService.getDocumentById(id);
            Resource resource = documentService.downloadDocument(id);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(
                            document.getFileType() != null ? document.getFileType() : "application/octet-stream"))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + document.getFileName() + "\"")
                    .body(resource);
        } catch (Exception e) {
            throw new RuntimeException("Failed to download document: " + e.getMessage());
        }
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Document>> getByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(documentService.getDocumentsByCustomer(customerId));
    }

    @GetMapping("/policy/{policyId}")
    public ResponseEntity<List<Document>> getByPolicy(@PathVariable Long policyId) {
        return ResponseEntity.ok(documentService.getDocumentsByPolicy(policyId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.ok(Map.of("message", "Document deleted successfully"));
    }
}
