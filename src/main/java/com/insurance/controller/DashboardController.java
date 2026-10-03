package com.insurance.controller;

import com.insurance.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class DashboardController {

    @Autowired private CustomerService customerService;
    @Autowired private PolicyService policyService;
    @Autowired private ClaimService claimService;
    @Autowired private PaymentService paymentService;
    @Autowired private AgentService agentService;
    @Autowired private DocumentService documentService;

    // ─── Root → Login ───────────────────────────────────────────────
    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    // ─── Login ──────────────────────────────────────────────────────
    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null) model.addAttribute("error", "Invalid username or password");
        return "login";
    }

    @PostMapping("/login")
    public String loginSubmit(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {
        if ("admin".equals(username) && "admin".equals(password)) {
            session.setAttribute("loggedIn", true);
            session.setAttribute("username", username);
            return "redirect:/dashboard";
        }
        model.addAttribute("error", "Invalid username or password");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // ─── Dashboard ──────────────────────────────────────────────────
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalCustomers", customerService.countCustomers());
        model.addAttribute("activePolicies", policyService.countActivePolicies());
        model.addAttribute("pendingClaims", claimService.countPendingClaims());
        model.addAttribute("totalRevenue", paymentService.totalPaymentsReceived());
        model.addAttribute("totalAgents", agentService.countAgents());
        model.addAttribute("recentCustomers", customerService.getAllCustomers()
                .stream().limit(5).toList());
        model.addAttribute("recentClaims", claimService.getAllClaims()
                .stream().limit(5).toList());
        return "dashboard";
    }

    // ─── Module pages ────────────────────────────────────────────────
    @GetMapping("/customers")
    public String customers(Model model, @RequestParam(required = false) String search) {
        if (search != null && !search.isEmpty()) {
            model.addAttribute("customers", customerService.searchCustomers(search));
            model.addAttribute("search", search);
        } else {
            model.addAttribute("customers", customerService.getAllCustomers());
        }
        model.addAttribute("agents", agentService.getAllAgents());
        return "customers";
    }

    @GetMapping("/policies")
    public String policies(Model model, @RequestParam(required = false) String search) {
        if (search != null && !search.isEmpty()) {
            model.addAttribute("policies", policyService.searchPolicies(search));
            model.addAttribute("search", search);
        } else {
            model.addAttribute("policies", policyService.getAllPolicies());
        }
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("agents", agentService.getAllAgents());
        return "policies";
    }

    @GetMapping("/claims")
    public String claims(Model model) {
        model.addAttribute("claims", claimService.getAllClaims());
        model.addAttribute("policies", policyService.getAllPolicies());
        return "claims";
    }

    @GetMapping("/payments")
    public String payments(Model model) {
        model.addAttribute("payments", paymentService.getAllPayments());
        model.addAttribute("policies", policyService.getAllPolicies());
        return "payments";
    }

    @GetMapping("/agents")
    public String agents(Model model) {
        model.addAttribute("agents", agentService.getAllAgents());
        return "agents";
    }

    @GetMapping("/documents")
    public String documents(Model model) {
        model.addAttribute("documents", documentService.getAllDocuments());
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("policies", policyService.getAllPolicies());
        return "documents";
    }

    @GetMapping("/reports")
    public String reports(Model model) {
        model.addAttribute("totalCustomers", customerService.countCustomers());
        model.addAttribute("activePolicies", policyService.countActivePolicies());
        model.addAttribute("pendingClaims", claimService.countPendingClaims());
        model.addAttribute("totalRevenue", paymentService.totalPaymentsReceived());
        model.addAttribute("totalClaimsPaid", claimService.totalClaimsPaid());
        model.addAttribute("totalPremiumRevenue", policyService.totalPremiumRevenue());
        model.addAttribute("allPolicies", policyService.getAllPolicies());
        model.addAttribute("allClaims", claimService.getAllClaims());
        return "reports";
    }
}
