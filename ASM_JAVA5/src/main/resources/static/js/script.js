tailwind.config = {
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
    const loginTab = document.getElementById("loginTab");
    const registerTab = document.getElementById("registerTab");

    const loginForm = document.getElementById("loginForm");
    const registerForm = document.getElementById("registerForm");

    const goRegister = document.getElementById("goRegister");
    const goLogin = document.getElementById("goLogin");

    const loginText = document.getElementById("loginText");

    function showLogin() {
        loginForm.classList.remove("hidden");
        registerForm.classList.add("hidden");

        loginTab.classList.add("border-primary", "text-[#1c0e0d]");
        loginTab.classList.remove("border-transparent", "text-[#9c5049]");

        registerTab.classList.remove("border-primary", "text-[#1c0e0d]");
        registerTab.classList.add("border-transparent", "text-[#9c5049]");

        loginText.classList.add("hidden");
        goRegister.parentElement.classList.remove("hidden");
    }

    function showRegister() {
        registerForm.classList.remove("hidden");
        loginForm.classList.add("hidden");

        registerTab.classList.add("border-primary", "text-[#1c0e0d]");
        registerTab.classList.remove("border-transparent", "text-[#9c5049]");

        loginTab.classList.remove("border-primary", "text-[#1c0e0d]");
        loginTab.classList.add("border-transparent", "text-[#9c5049]");

        loginText.classList.remove("hidden");
        goRegister.parentElement.classList.add("hidden");
    }

    loginTab.addEventListener("click", showLogin);
    registerTab.addEventListener("click", showRegister);
    goRegister.addEventListener("click", showRegister);
    goLogin.addEventListener("click", showLogin);
});


document.addEventListener("DOMContentLoaded", function () {
    const cards = document.querySelectorAll(".product-card");
    const loadMoreBtn = document.getElementById("loadMoreBtn");

    const ITEMS_PER_LOAD = 20; // 5 dòng x 4 sản phẩm
    let visibleCount = ITEMS_PER_LOAD;

    function updateProducts() {
        cards.forEach((card, index) => {
            if (index < visibleCount) {
                card.style.display = "block";
            } else {
                card.style.display = "none";
            }
        });

        // Kiểm tra xem nút Load More có ID này không trước khi thay đổi style để tránh lỗi
        if (loadMoreBtn) {
            if (visibleCount >= cards.length) {
                loadMoreBtn.style.display = "none";
            } else {
                loadMoreBtn.style.display = "inline-block";
            }
        }
    }

    if (loadMoreBtn) {
        loadMoreBtn.addEventListener("click", function () {
            visibleCount += ITEMS_PER_LOAD;
            updateProducts();
        });
    }

    updateProducts();
});

document.addEventListener("DOMContentLoaded", function () {
    const decreaseBtn = document.getElementById("decreaseQty");
    const increaseBtn = document.getElementById("increaseQty");
    const quantityValue = document.getElementById("quantityValue");
    const quantityInput = document.getElementById("quantityInput");

    // Kiểm tra xem các phần tử có tồn tại trên trang không trước khi chạy code
    if (decreaseBtn && increaseBtn && quantityValue && quantityInput) {

        let quantity = parseInt(quantityInput.value) || 1;

        function renderQuantity() {
            quantityValue.textContent = quantity;
            quantityInput.value = quantity;
        }

        increaseBtn.addEventListener("click", function () {
            quantity++;
            renderQuantity();
        });

        decreaseBtn.addEventListener("click", function () {
            if (quantity > 1) {
                quantity--;
                renderQuantity();
            }
        });

        // Khởi tạo giá trị ban đầu
        renderQuantity();
    }
});