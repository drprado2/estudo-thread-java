import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ControladorAcessoArea {
	
	private final String defaultArea = "�rea Padr�o";
	private final int defaultTotalPessoas = 5;
	
	private String area;
	private int totalPessoasPermitidas;
	// ****************** PRIMEIRA ALTERA��O ***************
	// Deixe isso comentado, rode o teste multi thread, e leia o console no in�cio
	// voc� ir� perceber que foram geradas mais de 1 instancia, isso � um problema, 
	// pois tentamos aplicar o pattern singleton aqui
	private volatile static ControladorAcessoArea instance;
//	private static ControladorAcessoArea instance;
	private List<String> pessoasNaArea;
	private UUID id;

	public List<String> getPessoasNaArea() {
		return pessoasNaArea.stream().collect(Collectors.toList());
	}

	public void setArea(String area) {
		this.area = area;
	}

	public void setTotalPessoas(int totalPessoas) {
		this.totalPessoasPermitidas = totalPessoas;
	}

	private ControladorAcessoArea() {
		this.area = defaultArea;
		this.totalPessoasPermitidas = defaultTotalPessoas;
		this.pessoasNaArea = new ArrayList<String>();
		// Esse UUID foi colocado exatamente para n�s conseguirmos controlar a instancia �nica,
		// deixe tudo comentando e rode o teste, olhe o in�cio dele e voc� ver� o primeiro problema
		// mais de uma instancia foi obtida, sendo que usamos o pattern de singleton aqui
		// e n�o queri�mos nunca obter instancias diferentes
		this.id = UUID.randomUUID();
		System.out.format("\n\n Id da instancia: %s", this.id.toString());
	}
	
	public static ControladorAcessoArea getInstance() {
	// ****************** PRIMEIRA ALTERA��O ***************
	// A uni�o do volatile e desse synchronized abaixo,
	// fazem com q esse trecho de c�digo se torne thread safe
	// ou seja, ele garante que apenas 1 thread acesse isso por vez
		synchronized(ControladorAcessoArea.class) {
			if(instance == null)
				instance = new ControladorAcessoArea();
		}
		
		return instance;
	}
	
	// ****************** SEGUNDA ALTERA��O ***************
	// essa palavra synchronized torna esse trecho de c�digo
	// thread safe tmb, a diferen�a � que aqui o m�todo todo � safe
	// no caso de cima apenas um trecho de c�digo
	// � equivalente ao lock() do C#
	public synchronized boolean EntrarNaArea(String pessoa) {
		if(this.pessoasNaArea.size() < this.totalPessoasPermitidas) {
			System.out.println("Nova pessoa adicionada com sucesso \n\n");
			this.pessoasNaArea.add(pessoa);
			return true;
		}
		
		System.out.println("N�o ser� adicionado pessoa na lista \n\n");
		return false;
	}
}
