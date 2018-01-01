function Paddle(xStart, yStart) {
    this.pos = createVector(xStart, yStart);
    this.w = 50;
    this.h = 200;

    this.update = function () {
        var vel = createVector(0,0);

        if (keyIsDown(UP_ARROW)) {
            vel.add(0, -5);
        } else if (keyIsDown(DOWN_ARROW)) {
            vel.add(0, 5);
        }
        this.pos.add(vel);
    }

    this.show = function () {
        fill(255);
        rect(this.pos.x, this.pos.y, this.w, this.h);
    }
}