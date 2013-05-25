package rgegame.entities;

public class Entity {

	private int Health;
	private int Mana;
		
	public Entity(int health, int mana) {
		super();
		Health = health;
		Mana = mana;
	}

	public Entity() {
	}

	public int getMana() {
		return Mana;
	}

	public void setMana(int mana) {
		Mana = mana;
	}

	public int getHealth(){
		return Health;
	}
	
	public void setHealth(int health){
		Health = health;
	}
}
