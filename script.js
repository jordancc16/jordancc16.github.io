// Footer year
document.getElementById("year").textContent = new Date().getFullYear();

// ============================================================
// Theme toggle (persisted)
// ============================================================
(function () {
  const root = document.documentElement;
  const btn = document.getElementById("themeToggle");
  const saved = localStorage.getItem("theme");
  if (saved) root.setAttribute("data-theme", saved);

  function toggle() {
    const current =
      root.getAttribute("data-theme") ||
      (window.matchMedia("(prefers-color-scheme: dark)").matches ? "dark" : "light");
    const next = current === "dark" ? "light" : "dark";
    root.setAttribute("data-theme", next);
    localStorage.setItem("theme", next);
  }

  btn.addEventListener("click", toggle);
  document.addEventListener("keydown", (e) => {
    if (e.key.toLowerCase() === "t" && !e.metaKey && !e.ctrlKey && !e.altKey) {
      const tag = (e.target.tagName || "").toLowerCase();
      if (tag === "input" || tag === "textarea") return;
      toggle();
    }
  });
})();

// ============================================================
// Sticky header shadow on scroll
// ============================================================
(function () {
  const header = document.querySelector(".site-header");
  const onScroll = () => header.classList.toggle("scrolled", window.scrollY > 8);
  onScroll();
  window.addEventListener("scroll", onScroll, { passive: true });
})();

// ============================================================
// Scroll-reveal via IntersectionObserver
// ============================================================
(function () {
  const els = document.querySelectorAll(".reveal");
  const io = new IntersectionObserver(
    (entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          entry.target.classList.add("is-visible");
          io.unobserve(entry.target);
        }
      });
    },
    { threshold: 0.12, rootMargin: "0px 0px -40px 0px" }
  );
  els.forEach((el) => io.observe(el));
})();

// ============================================================
// Active nav link based on scroll position
// ============================================================
(function () {
  const links = document.querySelectorAll(".nav a[data-nav]");
  const sections = Array.from(links)
    .map((a) => document.querySelector(a.getAttribute("href")))
    .filter(Boolean);

  const io = new IntersectionObserver(
    (entries) => {
      entries.forEach((entry) => {
        if (!entry.isIntersecting) return;
        const id = entry.target.id;
        links.forEach((l) =>
          l.classList.toggle("is-active", l.getAttribute("href") === "#" + id)
        );
      });
    },
    { rootMargin: "-45% 0px -50% 0px" }
  );
  sections.forEach((s) => io.observe(s));
})();

// ============================================================
// Animated count-up for stats
// ============================================================
(function () {
  const nums = document.querySelectorAll(".stat-num[data-count]");
  const io = new IntersectionObserver((entries) => {
    entries.forEach((entry) => {
      if (!entry.isIntersecting) return;
      const el = entry.target;
      const target = parseInt(el.dataset.count, 10);
      const duration = 1200;
      const start = performance.now();
      function tick(now) {
        const t = Math.min(1, (now - start) / duration);
        const eased = 1 - Math.pow(1 - t, 3);
        el.textContent = Math.round(target * eased);
        if (t < 1) requestAnimationFrame(tick);
      }
      requestAnimationFrame(tick);
      io.unobserve(el);
    });
  }, { threshold: 0.4 });
  nums.forEach((n) => io.observe(n));
})();

// ============================================================
// Hero rotator (cycle through phrases)
// ============================================================
(function () {
  const items = document.querySelectorAll(".rotator .rotator-item");
  if (!items.length) return;
  let i = 0;
  items[0].classList.add("is-on");

  setInterval(() => {
    const cur = items[i];
    cur.classList.remove("is-on");
    cur.classList.add("is-out");
    i = (i + 1) % items.length;
    const next = items[i];
    setTimeout(() => {
      items.forEach((it) => it.classList.remove("is-out"));
      next.classList.add("is-on");
    }, 350);
  }, 2600);
})();

// ============================================================
// Skill tabs
// ============================================================
(function () {
  const wrap = document.getElementById("skillTabs");
  if (!wrap) return;
  const tabs = wrap.querySelectorAll(".tab");
  const panels = wrap.querySelectorAll(".tab-panel");

  tabs.forEach((tab) => {
    tab.addEventListener("click", () => {
      const key = tab.dataset.tab;
      tabs.forEach((t) => t.classList.toggle("is-active", t === tab));
      panels.forEach((p) =>
        p.classList.toggle("is-active", p.dataset.panel === key)
      );
    });
  });
})();
