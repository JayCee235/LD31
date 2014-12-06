
public enum EnemyType {
	chaser, hunter, siege, circler;
	
	public static EnemyType random() {
		EnemyType[] c = new EnemyType[]{chaser, hunter, siege, circler};
		int i = (int) (c.length * Math.random());
		
		return c[i];
	}
}
