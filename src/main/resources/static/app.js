var stompClient = null;

function connect() {
    var socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/allMessages', function (msg) {
            showNewMessage(JSON.parse(msg.body));
        });
        stompClient.subscribe('/topic/allLogins', function (msg) {
            handleUsersActivity(JSON.parse(msg.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function sendMessage() {
    let content = $("#newMessageInput").val();
    let username = $("#usernameInput").val();

    console.log(content);
    console.log("username"+username);

    stompClient.send("/app/publishMessage", {}, JSON.stringify(
        {
            'content': content
        }
        ));
}

function showNewMessage(message) {
    let div = document.createElement('div');
    let timestampSpan = document.createElement('span');
    timestampSpan.textContent = message.createdAt + ' ';

    let userSpan = document.createElement('span');
    userSpan.textContent = message.user.username + ' ';
    userSpan.style = 'color: ' + message.user.colorCode + ';';

    let textSpan = document.createElement('span');
    textSpan.textContent = message.content;

    div.appendChild(timestampSpan);
    div.appendChild(userSpan);
    div.appendChild(textSpan);

    let allMessagesDiv = $("#allMessagesDiv");
    allMessagesDiv.append(div);

    allMessagesDiv.stop().animate({
        scrollTop: allMessagesDiv[0].scrollHeight
    }, 500);
}

function handleUsersActivity(message){

    console.log('message '+message);
    // message.type.localeCompare("LOGGED_IN")
    if (true){
        console.log("was true, should be appended")
        let li =  document.createElement("li");
        li.classList.add("active");

        let flexDiv = document.createElement("div");
        flexDiv.classList.add("d-flex");
        flexDiv.classList.add("cd-highlight");

        let imgContDiv = document.createElement("div");
        imgContDiv.classList.add("img_cont");

        let img = document.createElement("img");
        img.src = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQy0gcXavtE10AMBb1o_J_fIR0npuwuwVgJHg&usqp=CAU";
        img.classList.add("rounded-circle");
        img.classList.add("user_img");

        let onlineIconSpan = document.createElement("span");
        onlineIconSpan.classList.add("online_icon");

        let userInfoDiv = document.createElement("div");
        userInfoDiv.classList.add("user_info")

        let span = document.createElement("span");
        span.textContent = message.username;

        let p = document.createElement("p");
        p.textContent = "is online";

        userInfoDiv.appendChild(span);
        userInfoDiv.appendChild(p);

        imgContDiv.appendChild(img);
        imgContDiv.appendChild(onlineIconSpan);

        flexDiv.appendChild(imgContDiv);
        flexDiv.appendChild(userInfoDiv);

        li.appendChild(flexDiv);
        let allActiveUsersDiv = document.getElementById('#allActiveUsers');
        allActiveUsersDiv.appendChild(li);
    }
    console.log("end")

}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    connect();
    // $( "#connect" ).click(function() { connect(); });
    // $( "#disconnect" ).click(function() { disconnect(); });
    $( "#sendMessage" ).click(function() {
        sendMessage();
    });
});