// Cấu hình Tailwind (Chỉ có tác dụng nếu bạn dùng Tailwind Play CDN)
window.tailwind.config = {
    darkMode: "class",
    theme: {
        extend: {
            colors: {
                "primary": "#f2200d",
                "background-light": "#fafaf9",
                "background-dark": "#1a1a1a",
                "card-light": "#ffffff",
                "accent-yellow": "#F2D824"
            },
            fontFamily: {
                "display": ["Plus Jakarta Sans", "sans-serif"]
            },
            borderRadius: {
                "DEFAULT": "0.5rem",
                "lg": "1rem",
                "xl": "1.5rem",
                "full": "9999px"
            }
        }
    }
};

document.addEventListener("DOMContentLoaded", () => {

    // ==========================================
    // 1. LOGIC CHUYỂN TAB LOGIN / REGISTER
    // ==========================================
    const loginTab = document.getElementById("loginTab");
    const registerTab = document.getElementById("registerTab");
    const loginForm = document.getElementById("loginForm");
    const registerForm = document.getElementById("registerForm");
    const goRegister = document.getElementById("goRegister");
    const goLogin = document.getElementById("goLogin");
    const loginText = document.getElementById("loginText");

    if (loginTab && registerTab) {
        function showLogin() {
            loginForm?.classList.remove("hidden");
            registerForm?.classList.add("hidden");
            loginTab.classList.add("border-primary", "text-[#1c0e0d]");
            loginTab.classList.remove("border-transparent", "text-[#9c5049]");
            registerTab.classList.remove("border-primary", "text-[#1c0e0d]");
            registerTab.classList.add("border-transparent", "text-[#9c5049]");
            loginText?.classList.add("hidden");
            goRegister?.parentElement.classList.remove("hidden");
        }

        function showRegister() {
            registerForm?.classList.remove("hidden");
            loginForm?.classList.add("hidden");
            registerTab.classList.add("border-primary", "text-[#1c0e0d]");
            registerTab.classList.remove("border-transparent", "text-[#9c5049]");
            loginTab.classList.remove("border-primary", "text-[#1c0e0d]");
            loginTab.classList.add("border-transparent", "text-[#9c5049]");
            loginText?.classList.remove("hidden");
            goRegister?.parentElement.classList.add("hidden");
        }

        loginTab.addEventListener("click", showLogin);
        registerTab.addEventListener("click", showRegister);
        goRegister?.addEventListener("click", showRegister);
        goLogin?.addEventListener("click", showLogin);
    }

    // ==========================================
    // 2. LOGIC TỰ ĐỘNG ẨN/HIỆN SẢN PHẨM (LOAD MORE)
    // ==========================================
    const cards = document.querySelectorAll(".product-card");
    const loadMoreBtn = document.getElementById("loadMoreBtn");
    const ITEMS_PER_LOAD = 20;
    let visibleCount = ITEMS_PER_LOAD;

    if (loadMoreBtn && cards.length > 0) {
        function updateProducts() {
            cards.forEach((card, index) => {
                card.style.display = index < visibleCount ? "block" : "none";
            });
            loadMoreBtn.style.display = visibleCount >= cards.length ? "none" : "inline-block";
        }
        loadMoreBtn.addEventListener("click", () => {
            visibleCount += ITEMS_PER_LOAD;
            updateProducts();
        });
        updateProducts();
    }

    // ==========================================
    // 3. LOGIC TĂNG GIẢM SỐ LƯỢNG (QUANTITY)
    // ==========================================
    const dBtn = document.getElementById("decreaseQty");
    const iBtn = document.getElementById("increaseQty");
    const qVal = document.getElementById("quantityValue");
    const qInp = document.getElementById("quantityInput");

    if (dBtn && iBtn && qVal && qInp) {
        let quantity = parseInt(qInp.value) || 1;
        const render = () => {
            qVal.textContent = quantity;
            qInp.value = quantity;
        };
        iBtn.addEventListener("click", () => { quantity++; render(); });
        dBtn.addEventListener("click", () => { if (quantity > 1) { quantity--; render(); } });
        render();
    }

    // ==========================================
    // 4. LOGIC SLIDER BANNER (AUTO PLAY)
    // ==========================================
    const container = document.querySelector(".slides-container");
    const slides = document.querySelectorAll(".slides-container > div");
    const dots = document.querySelectorAll(".absolute.bottom-8 div");

    // Lấy nút dựa theo class hoặc vị trí trong Section Slider
    const sliderSection = container?.closest('section');
    const prevBtn = sliderSection?.querySelector('button:first-of-type');
    const nextBtn = sliderSection?.querySelector('button:last-of-type');

    if (container && slides.length > 0) {
        let currentIndex = 0;
        const slideCount = slides.length;
        let timer;

        const goToSlide = (index) => {
            if (index < 0) index = slideCount - 1;
            if (index >= slideCount) index = 0;
            currentIndex = index;
            container.scrollTo({
                left: container.clientWidth * currentIndex,
                behavior: "smooth"
            });
            updateDots();
        };

        const updateDots = () => {
            dots.forEach((dot, i) => {
                if (i === currentIndex) {
                    dot.className = "h-3 w-8 rounded-full bg-white shadow-lg transition-all";
                } else {
                    dot.className = "size-3 rounded-full bg-white/40 hover:bg-white/60 cursor-pointer transition-all border border-white/20";
                }
            });
        };

        const startTimer = () => {
            timer = setInterval(() => goToSlide(currentIndex + 1), 5000);
        };

        nextBtn?.addEventListener("click", () => { goToSlide(currentIndex + 1); clearInterval(timer); startTimer(); });
        prevBtn?.addEventListener("click", () => { goToSlide(currentIndex - 1); clearInterval(timer); startTimer(); });

        dots.forEach((dot, i) => {
            dot.addEventListener("click", () => { goToSlide(i); clearInterval(timer); startTimer(); });
        });

        container.addEventListener("mouseenter", () => clearInterval(timer));
        container.addEventListener("mouseleave", startTimer);

        startTimer();
        window.addEventListener("resize", () => goToSlide(currentIndex));
    }
});
// Register terms gate: must tick terms before submitting register form.
document.addEventListener("DOMContentLoaded", () => {
    const terms = document.getElementById("terms");
    const submit = document.getElementById("registerSubmit");
    if (terms && submit) {
        const syncRegisterButton = () => {
            submit.disabled = !terms.checked;
            submit.title = terms.checked ? "" : "Vui lòng đồng ý điều khoản trước khi đăng ký";
        };
        terms.addEventListener("change", syncRegisterButton);
        syncRegisterButton();
    }
});
