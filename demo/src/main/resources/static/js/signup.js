const signUpBtn = document.getElementById("signup-btn").addEventListener("click", signUp)

async function signUp () {

    let email = document.getElementById("email-signup").value.trim();
    let username = document.getElementById("username-signup").value.trim();
    let password = document.getElementById("password-signup").value;

    if (email == ""|| username == "" || password == ""){
        alert("fill out all the required fields");
        return;
    }

    if(!email.includes("@" && ".com")){
        alert("invalid email")
        return;
    } 
     
    const userData = {
        email: email,
        username: username,
        userPassword: password
    }

    const response = await fetch("/signUser", {
        method: "POST",
        headers: {"content-Type": "application/json"},
        body: JSON.stringify(userData)
    })

       const data = await response.json();

    if (data.success == true){
        window.location.href = "/login"
    }
    else{
        alert("account taken")
    }
 
}




