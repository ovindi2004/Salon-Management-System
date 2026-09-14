/**
 * PINK BEAUTY SALON - UNIFIED SIDEBAR CONTROLLER
 * Automatically ensures consistent sidebar markup, active link states,
 * user session display, role permissions, and logout handling across all pages.
 */

(function () {
  const SIDEBAR_ITEMS = [
    { section: "MAIN" },
    { key: "dashboard", label: "Dashboard", icon: "fas fa-chart-line", href: "dashboard.html", roles: ["ADMIN", "RECEPTIONIST", "STAFF", "CUSTOMER"] },
    { key: "customer", label: "Customer Management", icon: "fas fa-users", href: "customerManagement.html", roles: ["ADMIN", "RECEPTIONIST", "STAFF"] },
    { key: "staff", label: "Staff Management", icon: "fas fa-user-tie", href: "staffManagement.html", roles: ["ADMIN"] },
    { key: "service", label: "Service Management", icon: "fas fa-hand-sparkles", href: "serviceManagement.html", roles: ["ADMIN", "RECEPTIONIST", "STAFF"] },
    { key: "appointment", label: "Appointment Management", icon: "fas fa-calendar-check", href: "appointmentManagement.html", roles: ["ADMIN", "RECEPTIONIST", "STAFF", "CUSTOMER"] },

    { section: "BUSINESS", roles: ["ADMIN", "RECEPTIONIST"] },
    { key: "product", label: "Product Management", icon: "fas fa-box", href: "productManagement.html", roles: ["ADMIN", "RECEPTIONIST"] },
    { key: "inventory", label: "Inventory Management", icon: "fas fa-warehouse", href: "inventoryMangement.html", roles: ["ADMIN", "RECEPTIONIST"] },
    { key: "payment", label: "Payments & Invoices", icon: "fas fa-file-invoice-dollar", href: "paymentMangement.html", roles: ["ADMIN", "RECEPTIONIST"] },
    { key: "feedback", label: "Feedback & Ratings", icon: "fas fa-star", href: "feedback.html", roles: ["ADMIN", "RECEPTIONIST", "STAFF"] },
    { key: "report", label: "Reports & Analytics", icon: "fas fa-chart-pie", href: "report.html", roles: ["ADMIN"] },

    { section: "SYSTEM", roles: ["ADMIN"] },
    { key: "user", label: "User Management", icon: "fas fa-user-shield", href: "userMangemnt.html", roles: ["ADMIN"] },
    { key: "settings", label: "Settings", icon: "fas fa-sliders", href: "settings.html", roles: ["ADMIN"] }
  ];

  function detectCurrentKey() {
    const p = window.location.pathname.toLowerCase();
    if (p.includes("customer")) return "customer";
    if (p.includes("staff")) return "staff";
    if (p.includes("service")) return "service";
    if (p.includes("appointment")) return "appointment";
    if (p.includes("inventory")) return "inventory";
    if (p.includes("product")) return "product";
    if (p.includes("payment") || p.includes("invoice")) return "payment";
    if (p.includes("feedback")) return "feedback";
    if (p.includes("report")) return "report";
    if (p.includes("user")) return "user";
    if (p.includes("settings")) return "settings";
    return "dashboard";
  }

  function getUserInfo() {
    let name = localStorage.getItem("userName");
    let role = localStorage.getItem("userRole");

    if (!name || !role) {
      const stored = localStorage.getItem("loggedInUser");
      if (stored) {
        try {
          const parsed = JSON.parse(stored);
          if (parsed.userName) name = parsed.userName;
          if (parsed.userRole) role = parsed.userRole;
        } catch (e) {
          // ignore
        }
      }
    }

    const raw = (role || "").trim().toUpperCase();
    let normalized = "ADMIN";
    let display = "Administrator";

    if (raw === "OWNER" || raw === "ADMIN" || raw === "ADMINISTRATOR") {
      normalized = "ADMIN";
      display = "Administrator";
    } else if (raw === "RECEPTIONIST" || raw === "RECEPTION") {
      normalized = "RECEPTIONIST";
      display = "Receptionist";
    } else if (raw === "STAFF") {
      normalized = "STAFF";
      display = "Staff";
    } else if (raw === "CUSTOMER") {
      normalized = "CUSTOMER";
      display = "Customer";
    } else if (raw) {
      normalized = raw;
      display = role;
    }

    const finalName = name || "Salon Manager";
    const parts = finalName.trim().split(/\s+/);
    let initials = "";
    if (parts.length >= 2) {
      initials = parts[0].charAt(0) + parts[parts.length - 1].charAt(0);
    } else {
      initials = parts[0].substring(0, 2);
    }

    return {
      userName: finalName,
      userRole: normalized,
      displayRole: display,
      initials: initials.toUpperCase()
    };
  }

  function buildUnifiedSidebarHTML(currentKey, user) {
    let navHtml = "";

    SIDEBAR_ITEMS.forEach(item => {
      if (item.section) {
        const rolesAttr = item.roles ? ` data-roles="${item.roles.join(",")}"` : "";
        navHtml += `<div class="nav-section-title"${rolesAttr}>${item.section}</div>`;
      } else {
        let href = item.href;
        if (item.key === "dashboard" && user.userRole === "RECEPTIONIST") {
          href = "receptionDashboard.html";
        }
        const isActive = item.key === currentKey ? " active" : "";
        const rolesAttr = item.roles ? ` data-roles="${item.roles.join(",")}"` : "";
        navHtml += `
          <a href="${href}" class="nav-item${isActive}" data-page="${item.key}"${rolesAttr}>
            <i class="${item.icon}"></i>
            <span>${item.label}</span>
          </a>`;
      }
    });

    return `
      <div class="sidebar-header">
        <div class="brand-logo">
          <i class="fas fa-spa"></i>
        </div>
        <div class="brand-info">
          <h2>Pink Beauty</h2>
          <span>LUXURY SALON</span>
        </div>
      </div>

      <nav class="sidebar-nav">
        ${navHtml}
      </nav>

      <div class="sidebar-footer">
        <div class="sidebar-user">
          <div class="user-avatar" id="sidebarAvatar">${user.initials}</div>
          <div class="user-details">
            <strong id="sidebarUserName">${user.userName}</strong>
            <span id="sidebarUserRole">${user.displayRole}</span>
          </div>
          <button class="sidebar-logout-btn" id="sidebarLogoutBtn" title="Logout">
            <i class="fas fa-right-from-bracket"></i>
          </button>
        </div>
      </div>
    `;
  }

  function initSidebar() {
    const sidebar = document.getElementById("sidebar");
    if (!sidebar) return;

    const currentKey = detectCurrentKey();
    const user = getUserInfo();

    // Replace sidebar content with the unified template
    sidebar.innerHTML = buildUnifiedSidebarHTML(currentKey, user);

    // Apply role-based visibility
    const roleItems = sidebar.querySelectorAll("[data-roles]");
    roleItems.forEach(el => {
      const allowed = el.getAttribute("data-roles").split(",").map(r => r.trim().toUpperCase());
      if (allowed.includes(user.userRole)) {
        el.style.display = "";
      } else {
        el.style.display = "none";
      }
    });

    // Handle logout
    const logoutBtn = sidebar.querySelector("#sidebarLogoutBtn");
    if (logoutBtn) {
      logoutBtn.addEventListener("click", () => {
        if (confirm("Are you sure you want to log out?")) {
          localStorage.removeItem("token");
          localStorage.removeItem("userRole");
          localStorage.removeItem("userName");
          localStorage.removeItem("userId");
          localStorage.removeItem("loggedInUser");
          sessionStorage.clear();
          window.location.href = "login.html";
        }
      });
    }

    // Handle mobile overlay and toggle
    let overlay = document.getElementById("sidebarOverlay") || document.getElementById("overlay");
    if (!overlay) {
      overlay = document.createElement("div");
      overlay.id = "sidebarOverlay";
      overlay.className = "unified-sidebar-overlay";
      document.body.appendChild(overlay);
    } else {
      overlay.classList.add("unified-sidebar-overlay");
    }

    const toggles = document.querySelectorAll("#sidebarToggle, #hamburgerBtn, .menu-toggle, .hamburger");
    toggles.forEach(btn => {
      btn.addEventListener("click", () => {
        sidebar.classList.toggle("mobile-open");
        sidebar.classList.toggle("open");
        overlay.classList.toggle("open");
        overlay.classList.toggle("active");
      });
    });

    overlay.addEventListener("click", () => {
      sidebar.classList.remove("mobile-open");
      sidebar.classList.remove("open");
      overlay.classList.remove("open");
      overlay.classList.remove("active");
    });
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", initSidebar);
  } else {
    initSidebar();
  }
})();
