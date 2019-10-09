package br.com.caelum.agiletickets.domain.precos;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import br.com.caelum.agiletickets.models.Sessao;
import br.com.caelum.agiletickets.models.TipoDeEspetaculo;

public class CalculadoraDePrecos {

	private static final int MINUTOS_DURACAO_AUMENTO = 60;
	private static final double PERCENTUAL_AUMENTO_DURACAO = 0.10;
	private static final double PERCENTUAL_AUMENTO_CINEMA_SHOW = 0.10;
	private static final double PERCENTUAL_AUMENTO_BALLET_ORQUESTRA = 0.20;
	private static final double PERCENTUAL_VAGAS_BALLET_ORQUESTRA = 0.50;
	private static final double PERCENTUAL_VAGAS_CINEMA_SHOW = 0.05;
	private static Map<TipoDeEspetaculo, CalculadorAumento> mapaCalculadorAumento;

	private static Map<TipoDeEspetaculo, CalculadorAumento> getMapaCalculadoAumento() {
		if (mapaCalculadorAumento == null) {
			mapaCalculadorAumento = new HashMap<TipoDeEspetaculo, CalculadoraDePrecos.CalculadorAumento>();
			CalculadorAumento aumentoBalletOrquestra = new CalculadorAumento(PERCENTUAL_VAGAS_BALLET_ORQUESTRA,
					PERCENTUAL_AUMENTO_BALLET_ORQUESTRA, true);
			CalculadorAumento aumentoCinemaShow = new CalculadorAumento(PERCENTUAL_VAGAS_CINEMA_SHOW,
					PERCENTUAL_AUMENTO_CINEMA_SHOW, false);
			mapaCalculadorAumento.put(TipoDeEspetaculo.BALLET, aumentoBalletOrquestra);
			mapaCalculadorAumento.put(TipoDeEspetaculo.ORQUESTRA, aumentoBalletOrquestra);
			mapaCalculadorAumento.put(TipoDeEspetaculo.CINEMA, aumentoCinemaShow);
			mapaCalculadorAumento.put(TipoDeEspetaculo.SHOW, aumentoCinemaShow);
			mapaCalculadorAumento.put(TipoDeEspetaculo.TEATRO, null);
		}
		return mapaCalculadorAumento;
	}

	public static BigDecimal calcula(Sessao sessao, Integer quantidade) {
		BigDecimal preco = calculaPrecoEspetaculo(sessao);
		return preco.multiply(BigDecimal.valueOf(quantidade));
	}

	private static BigDecimal calculaPrecoEspetaculo(Sessao sessao) {
		CalculadorAumento calculadorAumento = getMapaCalculadoAumento().get(sessao.getEspetaculo().getTipo());
		BigDecimal preco = sessao.getPreco();
		if (calculadorAumento != null) {
			preco = calculaAumentoValorIngresso(sessao, calculadorAumento, preco);
		}
		return preco;
	}

	private static BigDecimal calculaAumentoValorIngresso(Sessao sessao, CalculadorAumento calculadorAumento,
			BigDecimal preco) {
		if (isPercentualVagasAumentoAtingido(sessao, calculadorAumento)) {
			double multiplicadorValor = calculadorAumento.getMultiplicadorValor();
			preco = sessao.getPreco().add(getPrecoAtualizadoComMultiplicador(sessao, multiplicadorValor));
		}
		if (deveAumentarComDuracao(sessao, calculadorAumento)) {
			preco = preco.add(getPrecoAtualizadoComMultiplicador(sessao, PERCENTUAL_AUMENTO_DURACAO));
		}
		return preco;
	}

	private static BigDecimal getPrecoAtualizadoComMultiplicador(Sessao sessao, double multiplicadorValor) {
		return sessao.getPreco().multiply(BigDecimal.valueOf(multiplicadorValor));
	}

	private static boolean deveAumentarComDuracao(Sessao sessao, CalculadorAumento calculadorAumento) {
		return calculadorAumento.isVariaComDuracaoMinutos() && sessao.getDuracaoEmMinutos() > MINUTOS_DURACAO_AUMENTO;
	}

	private static boolean isPercentualVagasAumentoAtingido(Sessao sessao, CalculadorAumento calculadorAumento) {
		return (sessao.getTotalIngressos() - sessao.getIngressosReservados())
				/ sessao.getTotalIngressos().doubleValue() <= calculadorAumento.getPercentualVagas();
	}

	private static class CalculadorAumento {
		private CalculadorAumento(double percentualVagas, double multiplicadorValor, boolean variaComDuracaoMinutos) {
			super();
			this.percentualVagas = percentualVagas;
			this.multiplicadorValor = multiplicadorValor;
			this.variaComDuracaoMinutos = variaComDuracaoMinutos;
		}

		private double percentualVagas;
		private double multiplicadorValor;
		private boolean variaComDuracaoMinutos;

		private double getPercentualVagas() {
			return percentualVagas;
		}

		private double getMultiplicadorValor() {
			return multiplicadorValor;
		}

		private boolean isVariaComDuracaoMinutos() {
			return variaComDuracaoMinutos;
		}

	}

}