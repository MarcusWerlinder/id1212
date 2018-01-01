function Ball() {
    this.pos = createVector(width/2, height/2);
    this.r = 10;
    var xspeed = 0;
    var yspeed = 0;

    function reset() {
        this.pos = createVector(width/2, height/2);
        var angle = random(-PI/4, PI/4);
        xspeed = 5 * Math.cos(angle);
        yspeed = 5 * Math.sin(angle);

        if(random(1) < 0.5) {
            xspeed *= -1;
        }
    }

    this.checkLPaddle = function (paddle) {
        if(this.pos.y - this.r < paddle.pos.y + paddle.h
            && this.pos.y + this.r > paddle.pos.y
            && this.pos.x - this.r < paddle.pos.x + paddle.w) {

            xspeed *= -1;
        }
    }

    this.checkRPaddle = function (paddle) {
        console.log(paddle.pos.y);
        if(this.pos.y - this.r < paddle.pos.y + paddle.h
            && this.pos.y + this.r > paddle.pos.y
            && this.pos.x + this.r > paddle.pos.x) {

            xspeed *= -1;
        }
    }

    this.update = function () {
        this.pos.add(xspeed, yspeed);
    }

    this.Edges = function (lScore, rScore) {
        if(this.pos.y < 0 || this.pos.y > height) {
            yspeed *= -1;
        }

        if(this.pos.x > width) {
            reset();
        }

        if(this.pos.x < 0) {
            reset();
        }
    }

    this.construct = function () {
        reset();
    }

    this.show = function() {
        fill(255);
        ellipse(this.pos.x, this.pos.y, this.r*2, this.r*2);
    }

}