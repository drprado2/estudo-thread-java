package estudo.assincrono;

import java.util.Arrays;
import java.util.List;

public class ControladorAcessoAreaTeste {
	public static void main(String ... args) {
		testeComMultiThread();
	}
	
	public static void testeComThreadUnica() {
		
		List<String> pessoasEntrar = Arrays.asList("adriano", "renata", "pedro", "julio", "maria", "lidia", "marlene", "laura");
		ControladorAcessoArea controlador = ControladorAcessoArea.getInstance();
		
		System.out.format("Iniciando teste, total de pessoas para entrar: %d", pessoasEntrar.size());
		
		pessoasEntrar.forEach(p -> controlador.EntrarNaArea(p));
		
		System.out.format("\n\n Teste concluído, total de pessoas que entraram %d \n\n Pessoas na área:\n", controlador.getPessoasNaArea().size());
		
		controlador.getPessoasNaArea().forEach(System.out::println);
	}
	
	public static void testeComMultiThread() {
		
		// Esse cenário de teste simula exatamente o cenário que teriámos com uma aplicação REST
		// onde cada request seria atendida por uma thread diferente, então em 10 requests paralelas teriámos
		// exatamente esse cenário, analise a classe ControladorAcessoArea, veja os comentários lá, e remova
		// para ver os problemas sumirem.
		List<Thread> threads = Arrays.asList(
			new Thread(() -> {
				List<String> pessoasEntrar = Arrays.asList("adriano", "renata", "pedro", "julio", "maria", "lidia", "marlene", "laura");
				ControladorAcessoArea controlador = ControladorAcessoArea.getInstance();
				pessoasEntrar.forEach(p -> controlador.EntrarNaArea(p));
			}),
			new Thread(() -> {
				List<String> pessoasEntrar = Arrays.asList("adriano", "renata", "pedro", "julio", "maria", "lidia", "marlene", "laura");
				ControladorAcessoArea controlador = ControladorAcessoArea.getInstance();
				pessoasEntrar.forEach(p -> controlador.EntrarNaArea(p));
			}),
			new Thread(() -> {
				List<String> pessoasEntrar = Arrays.asList("adriano", "renata", "pedro", "julio", "maria", "lidia", "marlene", "laura");
				ControladorAcessoArea controlador = ControladorAcessoArea.getInstance();
				pessoasEntrar.forEach(p -> controlador.EntrarNaArea(p));
			}),
			new Thread(() -> {
				List<String> pessoasEntrar = Arrays.asList("adriano", "renata", "pedro", "julio", "maria", "lidia", "marlene", "laura");
				ControladorAcessoArea controlador = ControladorAcessoArea.getInstance();
				pessoasEntrar.forEach(p -> controlador.EntrarNaArea(p));
			}),
			new Thread(() -> {
				List<String> pessoasEntrar = Arrays.asList("adriano", "renata", "pedro", "julio", "maria", "lidia", "marlene", "laura");
				ControladorAcessoArea controlador = ControladorAcessoArea.getInstance();
				pessoasEntrar.forEach(p -> controlador.EntrarNaArea(p));
			}),
			new Thread(() -> {
				List<String> pessoasEntrar = Arrays.asList("adriano", "renata", "pedro", "julio", "maria", "lidia", "marlene", "laura");
				ControladorAcessoArea controlador = ControladorAcessoArea.getInstance();
				pessoasEntrar.forEach(p -> controlador.EntrarNaArea(p));
			}),
			new Thread(() -> {
				List<String> pessoasEntrar = Arrays.asList("adriano", "renata", "pedro", "julio", "maria", "lidia", "marlene", "laura");
				ControladorAcessoArea controlador = ControladorAcessoArea.getInstance();
				pessoasEntrar.forEach(p -> controlador.EntrarNaArea(p));
			}),
			new Thread(() -> {
				List<String> pessoasEntrar = Arrays.asList("adriano", "renata", "pedro", "julio", "maria", "lidia", "marlene", "laura");
				ControladorAcessoArea controlador = ControladorAcessoArea.getInstance();
				pessoasEntrar.forEach(p -> controlador.EntrarNaArea(p));
			}),
			new Thread(() -> {
				List<String> pessoasEntrar = Arrays.asList("adriano", "renata", "pedro", "julio", "maria", "lidia", "marlene", "laura");
				ControladorAcessoArea controlador = ControladorAcessoArea.getInstance();
				pessoasEntrar.forEach(p -> controlador.EntrarNaArea(p));
			}),
			new Thread(() -> {
				List<String> pessoasEntrar = Arrays.asList("adriano", "renata", "pedro", "julio", "maria", "lidia", "marlene", "laura");
				ControladorAcessoArea controlador = ControladorAcessoArea.getInstance();
				pessoasEntrar.forEach(p -> controlador.EntrarNaArea(p));
			}
		));
		
		System.out.format("Iniciando teste, número de threads paralelas \nCada uma com 8 usuários para serem inseridos", threads.size());
		
		threads.forEach(t -> t.start());
		threads.forEach(t -> {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		
		System.out.format("Teste concluído\n Número de usuários na lista: %d\n",
					ControladorAcessoArea.getInstance().getPessoasNaArea().size());
	}
}
