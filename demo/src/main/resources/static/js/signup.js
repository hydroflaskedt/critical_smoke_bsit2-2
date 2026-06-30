const signUpBtn = document.getElementById("signup-btn").addEventListener("click", signUp)

async function signUp() {
    let email = document.getElementById("email-signup").value.trim();
    let username = document.getElementById("username-signup").value.trim();
    let password = document.getElementById("password-signup").value;

    if (email == "" || username == "" || password == "") {
        alert("Fill out all the required fields");
        return;
    }

    // Fixed: check @ and .com separately
    if (!email.includes("@") || !email.includes(".com")) {
        alert("Invalid email");
        return;
    }

    const userData = {
        email: email,
        username: username,
        userPassword: password
    }

    const response = await fetch("/signUser", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(userData)
    });

    const data = await response.json();

    if (data.success == true) {
        window.location.href = "/login";
    } else {
        if (data.reason === "username taken") {
            alert("That username is already taken");
        } else if (data.reason === "email taken") {
            alert("That email is already registered");
        } else {
            alert("Account already exists");
        }
    }
}