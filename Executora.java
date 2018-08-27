public class Executora {
	public static void main(String ...args) throws InterruptedException {
		
		new Thread() {
			@Override
			public void run() {
				metodoDemorado();
			}
		}.start();
		
		System.out.println("Enquanto o método demorado roda em paralelo já estou aqui");
		
		Thread.sleep(6000);

		System.out.println("Cheguei ao final geral");
	}
	
	public static void metodoDemorado() {
		try {
			Thread.sleep(5000);
			System.out.println("Concluiu o método demorado");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
