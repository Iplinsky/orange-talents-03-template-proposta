package br.com.zupacademy.proposta.metrics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;

@Component
public class Metrics {

	private final MeterRegistry meterRegistry;

	public Metrics(MeterRegistry meterRegistry) {
		this.meterRegistry = meterRegistry;
	}
	
	private Collection<Tag> retornaTagCollection() {
		Collection<Tag> tags = new ArrayList<Tag>();
		tags.add(Tag.of("emissora", "Mastercard")); 
		tags.add(Tag.of("banco", "Itaú"));
		return tags;
	}

	public void counter(String metricName) {
		Counter counterPropostas = this.meterRegistry.counter(metricName, retornaTagCollection());
		counterPropostas.increment();
	}

	public void timer(String metricName, Long initial) {
		Timer timerConsultaProposta = this.meterRegistry.timer(metricName);
		timerConsultaProposta.record((System.currentTimeMillis() - initial), TimeUnit.MILLISECONDS);
	}

}
