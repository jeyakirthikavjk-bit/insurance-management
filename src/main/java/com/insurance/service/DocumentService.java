package com.insurance.service;

import com.insurance.entity.Claim;
import com.insurance.entity.Customer;
import com.insurance.entity.Document;
import com.insurance.entity.Policy;
import com.insurance.exception.ResourceNotFoundException;
import com.insurance.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class DocumentService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);

    @Value("${file.upload.dir:uploads}")
    private String uploadDir;

    @Autowired private DocumentRepository documentRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private PolicyRepository policyRepository;
    @Autowired private ClaimRepository claimRepository;

    public Document uploadDocument(MultipartFile file, String name, String description,
                                   Document.DocumentType documentType, String uploadedBy,
                                   Long customerId, Long policyId, Long claimId) throws IOException {

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = "";
        if (originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        String uniqueFileName = UUID.randomUUID().toString() + extension;
        Path filePath = uploadPath.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        Document document = new Document();
        document.setName(name != null ? name : originalFileName);
        document.setFileName(originalFileName);
        document.setFilePath(uniqueFileName);
        document.setFileType(file.getContentType());
        document.setFileSize(file.getSize());
        document.setDocumentType(documentType);
        document.setDescription(description);
        document.setUploadedBy(uploadedBy);
        document.setStatus(Document.DocumentStatus.ACTIVE);

        if (customerId != null) {
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Customer", customerId));
            document.setCustomer(customer);
        }
        if (policyId != null) {
            Policy policy = policyRepository.findById(policyId)
                    .orElseThrow(() -> new ResourceNotFoundException("Policy", policyId));
            document.setPolicy(policy);
        }
        if (claimId != null) {
            Claim claim = claimRepository.findById(claimId)
                    .orElseThrow(() -> new ResourceNotFoundException("Claim", claimId));
            document.setClaim(claim);
        }

        logger.info("Document uploaded: {}", uniqueFileName);
        return documentRepository.save(document);
    }

    public Resource downloadDocument(Long documentId) throws MalformedURLException {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document", documentId));

        Path filePath = Paths.get(uploadDir).resolve(document.getFilePath()).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            throw new ResourceNotFoundException("File not found on disk: " + document.getFilePath());
        }
        return resource;
    }

    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    public Document getDocumentById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document", id));
    }

    public List<Document> getDocumentsByCustomer(Long customerId) {
        return documentRepository.findByCustomerId(customerId);
    }

    public List<Document> getDocumentsByPolicy(Long policyId) {
        return documentRepository.findByPolicyId(policyId);
    }

    public void deleteDocument(Long id) {
        Document document = getDocumentById(id);
        document.setStatus(Document.DocumentStatus.DELETED);
        documentRepository.save(document);
    }
}
