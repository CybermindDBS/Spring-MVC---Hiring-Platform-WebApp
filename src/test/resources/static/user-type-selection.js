let recruiterField = document.getElementById("recruiter-form");
let applicantField = document.getElementById("applicant-form");

function getQueryParam(param) {
    let urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(param);
}

let userType = getQueryParam("register");

let selectElement = document.getElementById("user-type-select");

if (userType) {
    selectElement.value = userType;
}


if (selectElement.value === "recruiter") {
    recruiterField.style.display = "block";
    applicantField.style.display = "none";
} else {
    recruiterField.style.display = "none";
    applicantField.style.display = "block";
}


document.addEventListener("DOMContentLoaded", function () {
    document
        .getElementById("user-type-select")
        .addEventListener("change", function () {
            if (this.value === "recruiter") {
                recruiterField.style.display = "block";
                applicantField.style.display = "none";
            } else {
                recruiterField.style.display = "none";
                applicantField.style.display = "block";
            }
        });
});
