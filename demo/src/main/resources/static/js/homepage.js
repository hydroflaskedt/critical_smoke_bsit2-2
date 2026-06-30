const profileBox = document.querySelector(".profile-box");
const arrowBox = document.querySelector(".arrow-box");
const logoutBtn = document.querySelector("#logout-btn");
const arrowImg = document.querySelector("#arrow-img");

// Store
const store = document.querySelector(".store-box");
if (store) {
    store.addEventListener("click", () => window.location.href = "/");
}

// Library
const library = document.querySelector(".library-box");
if (library) {
    library.addEventListener("click", () => {
        if (!loggedIn) {
            alert("pls Login / signUp to access");
            return;
        }
        window.location.href = "/library";
    });
}

// Publish
const publish = document.querySelector("#publish-btn");
if (publish) {
    publish.addEventListener("click", () => {
        if (!loggedIn) {
            alert("pls Login / signUp to access");
            return;
        }
        window.location.href = "/publishGames";
    });
}

// Inventory
const inventory = document.querySelector("#inventory-icon");
if (inventory) {
    inventory.addEventListener("click", () => {
        if (!loggedIn) {
            alert("pls Login / signUp to access");
            return;
        }
        window.location.href = "/publishedGames";
    });
}

// Shopping cart
const cart = document.querySelector("#shopping-cart-icon");
if (cart) {
    cart.addEventListener("click", () => {
        if (!loggedIn) {
            alert("pls Login / signUp to access");
            return;
        }
        window.location.href = "/cart";
    });
}

// Profile expand
if (profileBox && arrowBox) {
    profileBox.addEventListener("click", toggleExpandBox);
    arrowBox.addEventListener("click", toggleExpandBox);
}

function toggleExpandBox() {
    profileBox.classList.toggle("expanded");

    if (arrowImg.src.includes("arrow-down.png")) {
        arrowImg.src = "/images/arrow-left.png";
    } else {
        arrowImg.src = "/images/arrow-down.png";
    }
}

// Logout
if (logoutBtn) {
    logoutBtn.addEventListener("click", logout);
}

async function logout() {
    await fetch("/logout", {
        method: "POST"
    });

    window.location.href = "/auth";
}