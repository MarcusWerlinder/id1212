var leftTaken = false;
var rightTaken = false;
var players = [];
var ball = new Ball(0,0,20,0,0);
var score = new Score(0,0);

var ballFile = require('./ball.js');
ball = ballFile.data.reset(ball);

function Score(i, j){
    this.score1 = i;
    this.score2 = j;
}

function Ball(x, y, r, xspeed, yspeed) {
    this.x = x;
    this.y = y;
    this.r = r;
    this.xspeed = xspeed;
    this.yspeed = yspeed;
}

function Paddle(id, side, x, y, h, w) {
    this.side = side;
    this.id = id;
    this.x = x;
    this.y = y;
    this.h = h;
    this.w = w;
}

var express = require('express');

var app = express();
var server = app.listen(3000);

app.use(express.static('client'));

console.log("My socket server is running");

var io = require('socket.io')(server);

setInterval(heartbeat, 16);

function heartbeat() {
    io.sockets.emit('paddleBeat', players);
    if(players.length == 2){
        ball = ballFile.data.update(ball);
        ball = ballFile.data.edges(ball);
        ball = ballFile.data.checkPaddle(players[0], ball);
        ball = ballFile.data.checkPaddle(players[1], ball);
        var check = ballFile.data.goal(ball);
        if(check == 1){
            ball = ballFile.data.reset(ball);
            score.score1++;
        }else if(check == 2){
            ball = ballFile.data.reset(ball);
            score.score2++;
        }
        io.sockets.emit('ballBeat', ball);
        io.sockets.emit('scoreBeat', score);
    }
}

io.sockets.on('connection', function (socket) {
    console.log("New connection " + socket.id);

    //When a player joins the server
    socket.on('newPlayer', function() {
       console.log("socket id: " + socket.id + " has connected");

       if(leftTaken == false){
           var paddle = new Paddle(socket.id, 1, 900, 300, 200, 10);
           leftTaken = true;
           players.push(paddle);
           socket.emit("startCord", paddle);
       } else if(rightTaken == false) {
           var paddle = new Paddle(socket.id, 2, 50, 300, 200, 10);
           rightTaken = true;
           players.push(paddle);
           socket.emit("startCord", paddle);
       }
    });

    //When a player moves around
    socket.on('update', function(data) {
        for(var i = 0; i < players.length; i++){
            if(players[i].id == data.id && (players[i].y + data.add) > 0 && (players[i].y + 200 + data.add) < 600)
                players[i].y += data.add;
        }
    });

    socket.on('disconnect', function() {
       console.log(socket.id);
       for(var i = 0; i < players.length; i++){
           if(players[i].id == socket.id){
               if(players[i].side == 1){
                   leftTaken = false;
               } else if(players[i].side == 2){
                   rightTaken = false;
               }
               players.splice(i, 1);
           }
       }
    });
});