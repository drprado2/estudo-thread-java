package estudo.assincrono;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class TesteFuture {
	
	// O Executors possui em sua API método para criar uma poolthread Dinamica
	// De forma onde as thread são criadas por demanda, procuram ser reaproveitadas
	// e são liberadas caso fiquem 6 segundos sem uso
	// Também podemos criar um pool com uma quantia fixa de threads que sempre ficam a disposição
	// Porém já ficam com recursos alocados
	private static final ExecutorService threadPool = Executors.newFixedThreadPool(3);
	
	public static void main(String ... args) throws InterruptedException, ExecutionException{
//		execucaoSimples();
//		testeCancelamentoComFuture();
//		testeCancelamentoComThread();
//		exemploComFutureTask();
		futureTaskComExecutorService();
	}
	
	public static void execucaoSimples() throws InterruptedException, ExecutionException{
		System.out.println("Iniciando execução da tarefa");
		
		// O método submit recebe um Callable<T> e retorna uma Future<T>, o gerenciador irá
		// delegar para uma thread no seu domínio, e você passa a controlar pela Future na main thread a
		// situação dessa outra thread
		Future<Integer> future = threadPool.submit(() -> gerarNumeroAleatorio());
		
		// O método isDone retornará true quando a thread tiver concluído sua tarefa
		while(future.isDone())
			Thread.sleep(600);

		// O método get retorna o valor da tarefa, caso você use ele sem passar nenhum parâmetro
		// ele pausa a main thread até que a tarefa da outra thread seja concluída, tornando desnecessário
		// o uso do loop a cima.
		// Caso você queira pode usar esse método passando um long como parâmetro, o qual representa o tempo
		// em milisegundos pelo qual você deseja esperar pela resposta
		// Caso ela não venha o código irá prosseguir. Você também pode definir uma resposta padrão
		Integer integer = future.get();
		System.out.format("Tarefa concluída valor retornado: %d \n completa: %b", integer, future.isDone());
		
		// O método shutdown derruba o pool de threads, destruindo as threads e consequentemente liberando
		// todos os recuros por elas usados
		threadPool.shutdown();
	}
	
	public static void testeCancelamentoComFuture() throws InterruptedException{
		System.out.println("Iniciando execução do método");
		Future<Integer> future = threadPool.submit(() -> gerarNumeroAleatorio());
		
		Thread.sleep(1500);
		// O método cancel irá cancelar a execução da thread apenas se ela ainda não iniciou
		// agora caso você passe true como argumento, ele irá interromper a execução caso ela tenha
		// iniciado e cancelar a thread
		future.cancel(true);

		Thread.sleep(3500);
		
		System.out.format("Após aguardar percebos que ainda não foi concluído \n Thread cancelada: %b", future.isCancelled());
		threadPool.shutdown();
	}

	public static void testeCancelamentoComThread() throws InterruptedException{
		System.out.println("Iniciando execução do método");
		Thread thread = new Thread(() -> {
			try {
				Thread.currentThread().sleep(2500);
				System.out.println("\n\n Concluí *****----*****\n\n");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		thread.start();
		
		Thread.sleep(1300);
		
		thread.interrupt();
		

		Thread.sleep(2000);
		
		System.out.format("Após aguardar percebos que ainda não foi concluído \n Thread cancelada: %b \n %b", thread.isInterrupted(), thread.isAlive());
		threadPool.shutdown();
	}
	
	public static void exemploComFutureTask() throws InterruptedException, ExecutionException{
		System.out.println("Iniciando exemplo");

		// FutureTask é uma classe que implementa tanto Runnable quanto Future, então ela consegue
		// fazer o papel dos dois, 
		// *1* - Ela pode ser passada para uma thread para ser executada
		// *2* - Através da mesma instancia você Cancela, monitora e obtem o valor após a execução
		
		// Para instanciar uma você deve passar a Callable ou Runnable que você deseja executar
		FutureTask<Integer> futureTask = new FutureTask(TesteFuture::gerarNumeroAleatorio);
		// Para executar você tem algumas opções, como usar uma ExecutorService que irá delegar para alguma
		// thread da pool  ou instancia uma Thread diretamente da Thread Main
		new Thread(futureTask).start();
		
		// Como ela implementa a interface future podemos usar o método get, que irá pausar a main thread
		// até que a thread seja concluída e em seguida obtem o seu valor
		Integer integer = futureTask.get();
		
		System.out.format("Exemplo concluído valor resultado: %d", integer.intValue());
	}
	
	public static void futureTaskComExecutorService() throws InterruptedException, ExecutionException{
		System.out.println("Iniciado execução do teste");

		// Aqui criamos uma pool de thread dinamica, onde as threads são criadas por demanda
		// são mantidas vivas por 6 segundo paradas, são reaproveitadas se ainda vivas
		ExecutorService pool = Executors.newCachedThreadPool();
		
		FutureTask<Integer> future = new FutureTask(TesteFuture::gerarNumeroAleatorio);
		
		// O método execute diferente do submit tem o retorno void, pois a ideia é executar um comando
		// algo que não possui retorno, logo não tem porque ter uma Future, para FutureTask funciona perfeito
		// pois a pool delega e você controla a thread pela própria instancia da FutureTask
		pool.execute(future);
		
		Integer result = future.get();
		System.out.format("Concluído teste Resultado: %d", result.intValue());
	}

	public static void testeInvokeAll(){
		// A ideia do método invokeAll é que ele irá criar uma thread para executar cada tarefa e irá
		// iniciar elas juntas, quando você fizer o get em qualquer uma das tarefas ele só retorna após todas acabarem
		
		
	}
	
	public static int gerarNumeroAleatorio() throws InterruptedException{
		Thread.sleep(4000);
		return ((Double)(Math.random() * 1000)).intValue();
	}
}

