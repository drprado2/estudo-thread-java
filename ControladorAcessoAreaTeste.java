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
		
		System.out.format("\n\n Teste conclu�do, total de pessoas que entraram %d \n\n Pessoas na �rea:\n", controlador.getPessoasNaArea().size());
		
		controlador.getPessoasNaArea().forEach(System.out::println);
	}
	
	public static void testeComMultiThread() {
		
		// Esse cen�rio de teste simula exatamente o cen�rio que teri�mos com uma aplica��o REST
		// onde cada request seria atendida por uma thread diferente, ent�o em 10 requests paralelas teri�mos
		// exatamente esse cen�rio, analise a classe ControladorAcessoArea, veja os coment�rios l�, e remova
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
		
		System.out.format("Iniciando teste, n�mero de threads paralelas \nCada uma com 8 usu�rios para serem inseridos", threads.size());
		
		threads.forEach(t -> t.start());
		threads.forEach(t -> {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		
		System.out.format("Teste conclu�do\n N�mero de usu�rios na lista: %d\n",
					ControladorAcessoArea.getInstance().getPessoasNaArea().size());
	}
}
