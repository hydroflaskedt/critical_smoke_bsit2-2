const profileBox = document.querySelector(".profile-box");
const arrowBox = document.querySelector(".arrow-box");
const logoutBtn = document.querySelector("#logout-btn");
const arrowImg = document.querySelector("#arrow-img");

//store
const store = document.querySelector(".store-box");
store.addEventListener("click", () =>  window.location.href = "/");

//library 
const library = document.querySelector(".library-box");
library.addEventListener("click", () => {
    
  if (!loggedIn){
      alert("pls Login / signUp to access");
      return;
    }

    window.location.href = "/library"
  });

//publish game
const publish = document.querySelector("#publish-btn");
publish.addEventListener("click", () => {
   if (!loggedIn){
      alert("pls Login / signUp to access");
      return;
    }


});

//inventory
const inventory = document.querySelector("#inventory-icon");
inventory.addEventListener("click", () => {
   if (!loggedIn){
      alert("pls Login / signUp to access");
      return;
    }


});

//shopping cart
const cart = document.querySelector("#shopping-cart-icon");
cart.addEventListener("click", () => {
   if (!loggedIn){
      alert("pls Login / signUp to access");
      return;
    }


});

//expanding box profile
profileBox.addEventListener("click", toggleExpandBox);
arrowBox.addEventListener("click", toggleExpandBox);

  function toggleExpandBox() {
      profileBox.classList.toggle("expanded");
      if(arrowImg.src.includes("arrow-down.png")){
        arrowImg.src = "/images/arrow-left.png"
      }
      else{
        arrowImg.src = "/images/arrow-down.png"
      }
};

//logout
logoutBtn.addEventListener("click", logout)

async function logout (){
  const response = await fetch("/logout", {
    method: "POST"
  })

  window.location.href = "/auth"
}



