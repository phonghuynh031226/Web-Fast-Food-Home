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
