

const button = document.getElementById('LotteryGenerate')
const inputError = document.getElementById('inputError')
const submit = document.getElementById('LotteryForm')
const input = document.getElementById('LotteryText')

const winningMsg = document.getElementById('winningMessage')
const draws = document.getElementById('draws')

const getNumbers = document.getElementById('getDrawsButton')

const checkwin = document.getElementById('checkWinButton')



let myString = ""
let NumMessages = []


if (winningMsg.innerText==="null") {
    winningMsg.style.display = "none";
}

if (draws.innerText==="Draws: null") {
    draws.style.display = "none";
}

if (showDraws==null){
    getNumbers.disabled = true
    checkwin.disabled = true
}

function getRandomInt(min, max) {
    let byteArray = new Uint8Array(1);
    window.crypto.getRandomValues(byteArray);
    let range = max - min + 1;
    let max_range = 256;
    if (byteArray[0] >= Math.floor(max_range / range) * range)
        return getRandomInt(min, max);
    return min + (byteArray[0] % range);
}


button.addEventListener('click',(e) => {
    console.log("RUNNING")

    document.getElementById('LotteryText').value = "";
    myString = "";
    let randomInt = 0;
    let randomString = ""

    for (let i=0;i<6;i++){
        randomInt = getRandomInt(0,60)
        if (randomInt<10){
            randomString = "0"+randomInt.toString()
        }else {
            randomString = randomInt.toString()
        }
        myString = myString + randomString

        if (i!==5){
            myString = myString+"-"
        }

        console.log("myString: "+myString)
        document.getElementById('LotteryText').value = myString

    }
})


submit.addEventListener('submit',(e) => {

    let lotteryCheck = /\d{2}-\d{2}-\d{2}-\d{2}-\d{2}-\d{2}/.test(input.value.toString())

    inputError.innerText = ""
    NumMessages = []

    if (!lotteryCheck){
        NumMessages.push("Please Enter a NUMBER in the format xx-xx-xx-xx-xx-xx")
    }

    if (NumMessages.length>0){
        e.preventDefault()
        inputError.innerText = NumMessages.join(', ')
        e.preventDefault()
    }



})



