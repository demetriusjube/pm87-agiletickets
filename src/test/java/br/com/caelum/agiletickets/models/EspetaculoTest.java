package br.com.caelum.agiletickets.models;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

public class EspetaculoTest {

	@Test
	public void deveInformarSeEhPossivelReservarAQuantidadeDeIngressosDentroDeQualquerDasSessoes() {
		Espetaculo ivete = new Espetaculo();

		ivete.getSessoes().add(sessaoComIngressosSobrando(1));
		ivete.getSessoes().add(sessaoComIngressosSobrando(3));
		ivete.getSessoes().add(sessaoComIngressosSobrando(2));

		assertTrue(ivete.Vagas(5));
	}

	@Test
	public void deveInformarSeEhPossivelReservarAQuantidadeExataDeIngressosDentroDeQualquerDasSessoes() {
		Espetaculo ivete = new Espetaculo();

		ivete.getSessoes().add(sessaoComIngressosSobrando(1));
		ivete.getSessoes().add(sessaoComIngressosSobrando(3));
		ivete.getSessoes().add(sessaoComIngressosSobrando(2));

		assertTrue(ivete.Vagas(6));
	}

	@Test
	public void DeveInformarSeNaoEhPossivelReservarAQuantidadeDeIngressosDentroDeQualquerDasSessoes() {
		Espetaculo ivete = new Espetaculo();

		ivete.getSessoes().add(sessaoComIngressosSobrando(1));
		ivete.getSessoes().add(sessaoComIngressosSobrando(3));
		ivete.getSessoes().add(sessaoComIngressosSobrando(2));

		assertFalse(ivete.Vagas(15));
	}

	@Test
	public void DeveInformarSeEhPossivelReservarAQuantidadeDeIngressosDentroDeQualquerDasSessoesComUmMinimoPorSessao() {
		Espetaculo ivete = new Espetaculo();

		ivete.getSessoes().add(sessaoComIngressosSobrando(3));
		ivete.getSessoes().add(sessaoComIngressosSobrando(3));
		ivete.getSessoes().add(sessaoComIngressosSobrando(4));

		assertTrue(ivete.Vagas(5, 3));
	}

	@Test
	public void DeveInformarSeEhPossivelReservarAQuantidadeExataDeIngressosDentroDeQualquerDasSessoesComUmMinimoPorSessao() {
		Espetaculo ivete = new Espetaculo();

		ivete.getSessoes().add(sessaoComIngressosSobrando(3));
		ivete.getSessoes().add(sessaoComIngressosSobrando(3));
		ivete.getSessoes().add(sessaoComIngressosSobrando(4));

		assertTrue(ivete.Vagas(10, 3));
	}

	@Test
	public void DeveInformarSeNaoEhPossivelReservarAQuantidadeDeIngressosDentroDeQualquerDasSessoesComUmMinimoPorSessao() {
		Espetaculo ivete = new Espetaculo();

		ivete.getSessoes().add(sessaoComIngressosSobrando(2));
		ivete.getSessoes().add(sessaoComIngressosSobrando(2));
		ivete.getSessoes().add(sessaoComIngressosSobrando(2));

		assertFalse(ivete.Vagas(5, 3));
	}

	private Sessao sessaoComIngressosSobrando(int quantidade) {
		Sessao sessao = new Sessao();
		sessao.setTotalIngressos(quantidade * 2);
		sessao.setIngressosReservados(quantidade);

		return sessao;
	}

	/**
	 * Esse metodo eh responsavel por criar sessoes para o respectivo espetaculo,
	 * dado o intervalo de inicio e fim, mais a periodicidade.
	 * 
	 * O algoritmo funciona da seguinte forma: - Caso a data de inicio seja
	 * 01/01/2010, a data de fim seja 03/01/2010, e a periodicidade seja DIARIA, o
	 * algoritmo cria 3 sessoes, uma para cada dia: 01/01, 02/01 e 03/01.
	 * 
	 * - Caso a data de inicio seja 01/01/2010, a data fim seja 31/01/2010, e a
	 * periodicidade seja SEMANAL, o algoritmo cria 5 sessoes, uma a cada 7 dias:
	 * 01/01, 08/01, 15/01, 22/01 e 29/01.
	 * 
	 * Repare que a data da primeira sessao é sempre a data inicial.
	 */

	@Test
	public void criarSessoes_periodicidadeDiariaDataInicio01012010DataFim03012010_3sessoes010102010301() {
		Espetaculo espetaculo = new Espetaculo();
		DateTimeFormatter formato = DateTimeFormat.forPattern("yyyy-MM-dd");
		LocalDate dataInicio = LocalDate.parse("2010-01-01", formato);
		LocalDate dataFim = LocalDate.parse("2010-01-03", formato);
		LocalTime horario = LocalTime.parse("20:00");
		List<Sessao> sessoes = espetaculo.criaSessoes(dataInicio, dataFim, horario, Periodicidade.DIARIA);
		List<LocalDate> datasSessoesCalculadas = new ArrayList<LocalDate>();
		for (int i = 0; i < sessoes.size(); i++) {
			LocalDate dataSessaoCalculada = dataInicio.plusDays(i);
			datasSessoesCalculadas.add(dataSessaoCalculada);
		}
		assertTrue(sessoes.size() == 3);
	}

}
