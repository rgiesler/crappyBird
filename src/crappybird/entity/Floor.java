package crappybird.entity;

import crappybird.graphics.Screen;
import crappybird.graphics.Sprite;

public class Floor extends Entity{
	
	private Sprite sprite;
	private int xvel;
	
	public static Floor theFloor = new Floor(0, 220, Sprite.floor, -1);
	
	public Floor(int x, int y, Sprite sprite, int xvel) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
		this.xvel = xvel;
	}
	
	public void update(){
		x += xvel;
		if (x <= -sprite.w) x = 0;
	}
	
	public void render(Screen screen){
		sprite.render(screen, x, y);
		sprite.render(screen, x+sprite.w, y);
	}
}
