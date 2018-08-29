package estudo.assincrono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

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
//		futureTaskComExecutorService();
//		testeInvokeAll();
//		testeInvokeAny();
//		exemploInvokeAnyIrTratandoConformeRespondem();
//		exemploInvokeAnyTratandoExcecoes();
		exemploInvokeAllTratandoExcecpes();
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

	public static void testeInvokeAll() throws InterruptedException, ExecutionException{
		// A ideia do método invokeAll é que ele irá criar uma thread para executar cada tarefa e irá
		// iniciar elas juntas, quando você fizer o get em qualquer uma das tarefas ele só retorna após todas acabarem
		
		
		// Primeiro você deve criar uma lista de Callable
		List<Callable> tasks = Arrays.asList(new Callable(){
			@Override
			public Object call() {
				int result = gerarNumeroAleatorio(2000);
				System.out.format("A Task mais rápida de 2 segundos terminou valor gerado: %d\n", result);
				return 2;
			}
		},new Callable(){
			@Override
			public Object call() {
				int result = gerarNumeroAleatorio(4000);
				System.out.format("\nA Task de tempo médio 4 segundos terminou valor gerado: %d\n", result);
				return 4;
			}
		},new Callable(){
			@Override
			public Object call() {
				int result = gerarNumeroAleatorio(7000);
				System.out.format("\nA Task mais demorada de 7 segundos terminou valor gerado: %d\n\n", result);
				return 7;
			}
		});
		
		// Você deve possuir a instancia de um executor de serviços
		ExecutorService threadPool = Executors.newCachedThreadPool();
		
		List<Future<Integer>> futures = threadPool.invokeAll((Collection<? extends Callable<Integer>>) tasks);
		
		int valorMaisBaixo = futures.get(0).get();
		
		System.out.format("Veja que obtemos acima o valor que seria retornado em apenas 2 segundos"
				+ "\n %d \n"
				+ "Mas perceba que precisamos esperar pelo menos 7 segundos que é o tempo da mais demorado\n"
				+ "Isso ocorre porque o invokeAll espera todas serem resolvidas para retornar qualquer uma", valorMaisBaixo);
		
		threadPool.shutdown();
	}

	public static void testeInvokeAny() throws InterruptedException, ExecutionException{
		// A ideia do método invokeAny é que ele irá criar uma thread para executar cada tarefa e irá
		// iniciar elas juntas, porém o método não irá retornar nenhuma future, ele irá retornar o valor resolvido
		// mais rapidamente, diretamente
		
		// Primeiro você deve criar uma lista de Callable
		List<Callable> tasks = Arrays.asList(new Callable(){
			@Override
			public Object call() {
				int result = gerarNumeroAleatorio(4000);
				System.out.format("A Task de tempo mediano 4 segundos terminou valor gerado: %d\n", result);
				return 2;
			}
		},new Callable(){
			@Override
			public Object call() {
				int result = gerarNumeroAleatorio(2000);
				System.out.format("\nA Task de tempo mais rápido 2 segundos terminou valor gerado: %d\n", result);
				return 4;
			}
		},new Callable(){
			@Override
			public Object call() {
				int result = gerarNumeroAleatorio(7000);
				System.out.format("\nA Task mais demorada de 7 segundos terminou valor gerado: %d\n\n", result);
				return 7;
			}
		});
		
		// Você deve possuir a instancia de um executor de serviços
		ExecutorService threadPool = Executors.newCachedThreadPool();
		
		Integer result = threadPool.invokeAny((Collection<? extends Callable<Integer>>) tasks);
		
		System.out.format("Veja que obtemos acima o valor que seria retornado em apenas 2 segundos"
				+ "\n %d \n"
				+ "E esse valor foi o da segunda task que inserimos na collection\n"
				+ "Ele foi retornado pois foi o primeiro a terminar\n", result);
	}
	
	public static void exemploInvokeAnyIrTratandoConformeRespondem() throws InterruptedException, ExecutionException{
		// Nesse exemplo imagine que queremos fazer um serviço que vamos tratando conforme respondem
		// Tipo vamos consultar N web services, mais já queremos ir tratando conforme eles respondem
		
		// Iremos precisar de uma maneira de controlar as Callable e as Futures, como o método
		// invokeAny não retorna um Future, teremos que utilizar a FutureTask, que é Runnable e Future junto
		// Instanciamos aqui nossas futures que seriam as requisições para os nossos serviços
		FutureTask<Long> future1 = new FutureTask(() -> {
			System.out.format("\nIniciou a execução da TASK-1 DATA: %s", new Date().toString());
			// Utilizaremos essa função de calculoDemorado, que quanto maior o númeor que você passar mais tempo demora
			// Utilizar Thread Sleep é uma opção, mas o problema é que quando o InvokeAny tentar chamar o interrupt
			// dará uma exception. Outro problema é que Thread Sleep não simula muito o cenário real tmb
			long result = calculoDemorado(8, false);
			System.out.format("\n\nTerminou a execução da TASK-1 DATA: %s", new Date().toString());
			return result;
		});
		FutureTask<Long> future2 = new FutureTask(() -> {
			System.out.format("\nIniciou a execução da TASK-2 DATA: %s", new Date().toString());
			long result = calculoDemorado(12, false);
			System.out.format("\n\nTerminou a execução da TASK-2 DATA: %s", new Date().toString());
			return result;
		} );
		FutureTask<Long> future3 = new FutureTask(() -> {
			System.out.format("\nIniciou a execução da TASK-3 DATA: %s", new Date().toString());
			long result = calculoDemorado(10, false);
			System.out.format("\n\nTerminou a execução da TASK-3 DATA: %s", new Date().toString());
			return result;
		});

		// Agora que temos nossas Futures, precisamos de uma lista de Callables, pois o método
		// invokeAny trabalha apenas com lista de Callable, O retorno aqui não é importante para nós
		// pois iremos tratar as respostas fazendo o get direto na Future, apenas vamos por o retorno aqui
		// por ser obrigatório
		// Utilizamos um Map para termos controle de qual a FutureTask cuja Callable concluída pertence
		// Pois conforme uma Task seja concluída iremos remover ela da lista para continuar a executar as demais
		Map<FutureTask<Long>, Callable<Long>> tasks = new HashMap();
		tasks.put(future1, () -> {
			future1.run();
			long result = future1.get();
			return result;
		});
		tasks.put(future2, () -> {
			future2.run();
			long result = future2.get();
			return result;
		});
		tasks.put(future3, () -> {
			future3.run();
			long result = future3.get();
			return result;
		});
		
		// Criamos a nossa instancia de gerenciador de threads
		ExecutorService threadPool = Executors.newCachedThreadPool();
		// Fazemos um loop até que todas as Tasks sejam resolvidas, controlaremos isso removendo
		// elas do map de tarefas
		while(tasks.size() > 0){
			// Aqui invocamos o invokeAny, não precisamos associar o retorno dele em nenhuma variável
			// pois pegaremos o retorno direto da FutureTask. O importante é saber que o código
			// Irá travar nesse ponto até que alguma das tarefas seja concluída, a primeira que concluir
			// detravará
			threadPool.invokeAny(tasks.values());
			
			// Não podemos remover tasks do Map antigo, pois como os values desse map estão em execução
			// na Thread pool, a JVM irá disparar uma exceção por mexer em estrutura concorrente
			Map<FutureTask<Long>, Callable<Long>> newTasks = new HashMap<FutureTask<Long>, Callable<Long>>();
			tasks.entrySet()
				.stream()
				.forEach(e -> {
					// Aqui iremos passar por um loop em todas as tarefas, e com esse if conseguimos ver
					// quais acabaram, como queremos tratar todas e não apenas a primeira, temos que ter consciencia 
					// de que 2 tarefas podem acabar juntas e o invokeAny só retornaria 1 para nós
					// logo com esse loop garantimos que passamos por todas que vierem a acabar
					if(e.getKey().isDone()){
						try {
							// Nesse momento poderiamos dar sequencia no que fazer, como persistir em um banco
							// chamar um outro serviço, alimentar uma variável
							System.out.format("\n***Tarefa terminada*** Valor: %d", e.getKey().get());
						} catch (Throwable e1) {
							// O catch é obrigatório por conta da Checked Exception
							e1.printStackTrace();
						}
					}else{
						// As tarefas que ainda não foram concluídas iremos adicionar a lista para serem executadas novamente
						newTasks.put(e.getKey(), e.getValue());
					}
				});
			// por fim atualizamos a referencia da nossa variável para a nossa nova lista, e deixamos o loop rodar
			tasks = newTasks;
		}

		System.out.println("\n\nTodas as Tarefas foram concluídas");
	}
	
	public static void exemploInvokeAnyTratandoExcecoes() throws InterruptedException, ExecutionException{
		Callable<Long> task1 = () -> calculoDemorado(11, false);
		// Nosso método de calculoDemorado aceita um booleano para forçar o lançamento de uma EX
		// iremos utilizar com essa task, que seria a primeira a concluir
		Callable<Long> task2 = () -> calculoDemorado(6, true);
		
		// Outro teste que você pode fazer é por todos como true, ou seja dará uma exception em tudo
		// Nesse caso a exceção será lançada aqui nesse trecho do código, pois o invokeAny não
		// consegue encontrar nenhum valor válido
		Callable<Long> task3 = () -> calculoDemorado(12, false);

		List<Callable<Long>> tasks = Arrays.asList(task1, task2, task3);
		
		// Podemos perceber que aqui voltara ainda um valor, que é o valor da task1, e via debug é possível
		// perceber que a thread está rodando a task2 será interrompida e cancelada, pois o invokeAny já tem
		// esse tratamento automático caso ocorram erros, e ele continua a executar até 1 vir correta
		ExecutorService threadPool = Executors.newCachedThreadPool();
		
		Long result = threadPool.invokeAny(tasks);
		
		System.out.format("Concluído %d", result);
	}
	
	public static void exemploInvokeAllTratandoExcecpes() throws InterruptedException, ExecutionException{
		// Iremos ter 3 tarefas, sendo que a tarefa mais rápida, gerará uma exception
		Callable<Long> task1 = () -> calculoDemorado(8, false);
		Callable<Long> task2 = () -> calculoDemorado(6, true);
		Callable<Long> task3 = () -> calculoDemorado(10, false);

		List<Callable<Long>> tasks = Arrays.asList(task1, task2, task3);
		
		ExecutorService threadPool = Executors.newCachedThreadPool();
		
		// Recebemos a lista de futures sem problemas
		List<Future<Long>> futures = threadPool.invokeAll(tasks);
		
		// Quando fizermos 1 get, teremos o efeito do invokeAll, onde ele só retorna quando todas as tarefas tiverem concluído
		// porém, retornará sem problema o get, desde que a tarefa que você está tentando não tenha gerado exceção
		long result = futures.get(0).get();
		result = futures.get(2).get();
		// Perceba que nessa linha aqui teremos a exceção, pois ele tentará dar o get onde ocorreu a exceção
		result = futures.get(1).get();
		
		System.out.format("Concluído %d", result);
	}
	
	public static int gerarNumeroAleatorio() {
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			System.out.println("Aconteceu exception ao tentar interromper thread, pois foi usado thread sleep");
			Thread.currentThread().interrupt();
		}
		return ((Double)(Math.random() * 1000)).intValue();
	}

	public static int gerarNumeroAleatorio(Integer tempo){
		try {
			Thread.sleep(tempo);
		} catch (InterruptedException e) {
			System.out.println("Aconteceu exception ao tentar interromper thread, pois foi usado thread sleep");
			Thread.currentThread().interrupt();
		}
		return ((Double)(Math.random() * 1000)).intValue();
	}
	
	public static long fatorial(long fatorialDe){
		long result = fatorialDe == 0 ? 1 : fatorialDe;
		if((fatorialDe - 1) > 1)
			result *= fatorial(fatorialDe - 1);
		
		return result;
	}
	
	public static long calculoDemorado(long numeroInicial, boolean lancarExcecao) {
		long fatorial = fatorial(numeroInicial);
		long novoResultado = 0;
		for(int i = 1; i < fatorial; i ++){
			novoResultado += ((fatorial / i) * i) / Math.random();
		}
		if(lancarExcecao)
			throw new Error("Ocorreu um erro inesperado");
		return novoResultado;
	}
}

