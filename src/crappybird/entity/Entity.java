package crappybird.entity;

public abstract class Entity {
	public int x, y;
	
	public Entity(){
		x = 0;
		y = 0;
	}
	
	public Entity(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
