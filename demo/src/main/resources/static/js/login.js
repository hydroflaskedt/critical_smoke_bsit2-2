let loginBtn = document.getElementById("login-btn");
loginBtn.addEventListener("click", logIn);

async function logIn(){
    
    let usernameData = document.getElementById("username-login").value.trim().replaceAll(" ", "");
    let passwordData = document.getElementById("password-login").value.trim();

      if (usernameData == "" || passwordData == ""){
        alert("fill out all the required fields");
        return;
    }

    const userData = {
        username: usernameData,
        userPassword: passwordData
    }

    const response = await fetch("/logUser", {
        method: "POST",
        headers: {"content-Type": "application/json"},
        body: JSON.stringify(userData)
    })

    const data = await response.json();

    if(!data.success){
        alert("wrong username or password")
        return;
    }
    
    window.location.href = data.redirect;
}



