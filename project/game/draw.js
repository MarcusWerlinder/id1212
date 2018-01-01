var ball;

function setup() {
    createCanvas(600, 1000);
    ball = new Ball();
}

function draw() {
    background(0);
    ball.show();
}