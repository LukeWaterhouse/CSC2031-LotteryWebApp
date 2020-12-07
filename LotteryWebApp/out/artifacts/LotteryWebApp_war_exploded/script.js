const phone = document.getElementById('phone')
const email = document.getElementById('email')
const pass = document.getElementById('password')
const firstName = document.getElementById('firstname')
const lastName = document.getElementById('lastname')
const userName = document.getElementById('username')
const form = document.getElementById('Register')
const role = document.getElementById('role')
const roleError = document.getElementById('roleError')
const passError = document.getElementById('passError')
const phoneError = document.getElementById('phoneError')
const mailError = document.getElementById('mailError')
const firstError = document.getElementById('firstError')
const lastError = document.getElementById('lastError')
const userError = document.getElementById('userError')


const userLogin = document.getElementById("usernameLogin")
const passLogin = document.getElementById("passwordLogin")
const loginForm = document.getElementById("UserLogin")
const loginButton = document.getElementById("loginButton")
const loginUserError = document.getElementById("userLoginError")
const loginPassError = document.getElementById("passLoginError")

const homeMessage = document.getElementById("homeMessage")




if (homeMessage.innerText==="null"){
    console.log("In the thing")
    homeMessage.style.display = "none";
}


if (test!=null){
    console.log(test)
    userLogin.disabled = true;
    passLogin.disabled = true;
    loginForm.disabled=true;
    loginButton.disabled=true;
}

loginForm.addEventListener('submit',(e) => {

    let usernameMessages = []
    let passwordMessages = []

    let userLoginLength = (userLogin.value.toString().length>0)
    let passLoginLength = (passLogin.value.toString().length>0)

    if(!userLoginLength){
        usernameMessages.push("Please enter a username!")
    }

    if (!passLoginLength){
        passwordMessages.push("Please enter a password!")
    }

    if (usernameMessages.length>0||passwordMessages.length>0){
        loginUserError.innerText = usernameMessages.join(', ')
        loginPassError.innerText = passwordMessages.join(', ')
        e.preventDefault()

    }
})




form.addEventListener('submit',(e) => {
    console.log("JS script running...")
    let Passmessages = []
    let Nummessages = []
    let Mailmessages = []
    let userMessages = []
    let firstMessages = []
    let lastMessages = []
    let roleMessages = []

    console.log("role: "+role.value.toString())

    let userLength = (userName.value.toString().length>0)
    let firstLength = (firstName.value.toString().length>0)
    let lastLength = (lastName.value.toString().length>0)
    let roleLength = (role.value.toString().length>0)
    let passlength = (pass.value.toString().length>=8&&pass.value.toString().length<=15)
    let passNum = /\d/.test(pass.value)
    let passCase = ((/[a-z]/.test(pass.value))&&(/[A-Z]/.test(pass.value)))
    let phoneCheck = /\d{2}-\d{4}-\d{7}/.test(phone.value.toString())
    let emailCheck = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/.test(email.value.toString())



    if (!userLength){
        userMessages.push("Please enter a username!")
    }
    if (!firstLength){
        firstMessages.push("Please enter your first name!")
    }
    if (!lastLength){
        lastMessages.push("Please enter your last name!")
    }
    if (!emailCheck){
        Mailmessages.push("Please use a valid email address")
    }

    if (!phoneCheck){
        Nummessages.push("Please make sure you use xx-xxxx-xxxxxxx format")
    }

    if (!passlength){
        Passmessages.push("Please make sure your password is between 8 and 15 letters")
    }

    if (!passNum){
        Passmessages.push("Please make sure your password contains digits")
    }


    if (!passCase){
        Passmessages.push("Please make sure your password contains at least one of each case")
    }

    if (!roleLength){
        console.log("Hello there?")
        roleMessages.push("Please select a role!")
    }

    if (Passmessages.length>0||Nummessages.length>0||Mailmessages.length>0||firstMessages.length>0||lastMessages.length>0||userMessages.length>0||roleMessages.length>0){
        passError.innerText = Passmessages.join(', ')
        phoneError.innerText=Nummessages.join(', ')
        mailError.innerText = Mailmessages.join(', ')
        firstError.innerText = firstMessages.join(', ')
        lastError.innerText = lastMessages.join(', ')
        userError.innerText = userMessages.join(', ')
        roleError.innerText = roleMessages.join(', ')
        e.preventDefault()

    }


})

