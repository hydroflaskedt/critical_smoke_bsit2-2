function toggleDropdown() {
    const box = document.getElementById("dropdownBox");

    if (box.style.display === "grid") {
        box.style.display = "none";
    } else {
        box.style.display = "grid";
    }
}